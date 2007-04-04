/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.beans.metadata.plugins;

import java.beans.PropertyEditor;
import java.io.Serializable;
import java.util.Iterator;

import org.jboss.beans.metadata.spi.AliasMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.logging.Logger;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;
import org.jboss.util.StringPropertyReplacer;
import org.jboss.util.propertyeditor.PropertyEditors;

/**
 * Metadata for an alias.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class AbstractAliasMetaData extends JBossObject
      implements AliasMetaData, Serializable
{
   /**
    * The log
    */
   private static final Logger log = Logger.getLogger(AbstractAliasMetaData.class);

   static
   {
      try
      {
         PropertyEditors.init();
      }
      catch (Throwable t)
      {
         log.debug("Unable to initialise property editors", t);
      }
   }

   private static final long serialVersionUID = 1L;

   public String alias;

   protected boolean replace = true;
   protected String clazz;

   /**
    * Create a new annotation meta data
    */
   public AbstractAliasMetaData()
   {
      super();
   }

   public String getAlias()
   {
      return alias;
   }

   public void setAlias(String alias)
   {
      this.alias = alias;
   }

   public boolean isReplace()
   {
      return replace;
   }

   public void setReplace(boolean replace)
   {
      this.replace = replace;
   }

   public String getClazz()
   {
      return clazz;
   }

   public void setClazz(String clazz)
   {
      this.clazz = clazz;
   }

   public Object getAliasValue()
   {
      try
      {
         String aliasString = alias;
         if (replace)
         {
            aliasString = StringPropertyReplacer.replaceProperties(aliasString);
         }
         if (clazz != null)
         {
            PropertyEditor editor = PropertyEditors.findEditor(clazz);
            if (editor != null)
            {
               editor.setAsText(aliasString);
               return editor.getValue();
            }
            else
            {
               log.warn("No matching PropertyEditor found for class: " + clazz);
            }
         }
         return aliasString;
      }
      catch (Exception e)
      {
         throw new RuntimeException("Error creating alias for " + alias, e);
      }
   }

   public void initialVisit(MetaDataVisitor visitor)
   {
      visitor.initialVisit(this);
   }

   public void describeVisit(MetaDataVisitor vistor)
   {
      vistor.describeVisit(this);
   }

   public Iterator<? extends MetaDataVisitorNode> getChildren()
   {
      return null;
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("alias=").append(alias);
      buffer.append(" replace=").append(replace);
      if (clazz != null)
         buffer.append(" class=").append(clazz);
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(alias);
   }

   protected int getHashCode()
   {
      return alias.hashCode();
   }

   public boolean equals(Object object)
   {
      if (object == null || object instanceof AbstractAliasMetaData == false)
         return false;

      AbstractAliasMetaData amd = (AbstractAliasMetaData)object;
      // this is what we probably want? - never saw duplicate annotation on a bean/prop/...
      return alias.equals(amd.alias) && (replace == amd.replace) && (clazz != null && clazz.equals(amd.clazz));
   }

}
