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

import java.io.Serializable;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.jboss.beans.metadata.spi.AliasMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.reflect.plugins.introspection.IntrospectionTypeInfoFactory;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.reflect.spi.TypeInfoFactory;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;
import org.jboss.util.StringPropertyReplacer;

/**
 * Metadata for an alias.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
@XmlType
public class AbstractAliasMetaData extends JBossObject
      implements AliasMetaData, Serializable
{
   private static final long serialVersionUID = 2L;
   private static TypeInfoFactory typeInfoFactory = new IntrospectionTypeInfoFactory();

   public String alias;

   protected boolean replace = true;
   protected String clazz;

   /**
    * Create a new alias meta data
    */
   public AbstractAliasMetaData()
   {
      super();
   }

   public String getAlias()
   {
      return alias;
   }

   @XmlValue
   public void setAlias(String alias)
   {
      this.alias = alias;
   }

   public boolean isReplace()
   {
      return replace;
   }

   @XmlAttribute
   public void setReplace(boolean replace)
   {
      this.replace = replace;
   }

   public String getClazz()
   {
      return clazz;
   }

   @XmlAttribute(name="class")
   public void setClazz(String clazz)
   {
      this.clazz = clazz;
   }

   @XmlTransient
   public Object getAliasValue()
   {
      try
      {
         if (clazz != null)
         {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            TypeInfo info = typeInfoFactory.getTypeInfo(clazz, cl);
            return info.convertValue(alias, replace);
         }
         String aliasString = alias;
         if (replace)
         {
            aliasString = StringPropertyReplacer.replaceProperties(aliasString);
         }
         return aliasString;
      }
      catch (Throwable t)
      {
         throw new RuntimeException("Error creating alias for " + alias, t);
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

   @XmlTransient
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
