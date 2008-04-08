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

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.jboss.beans.metadata.spi.AliasMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Metadata for an alias.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
@XmlType(name="aliasType", propOrder="alias")
public class AbstractAliasMetaData extends JBossObject
      implements AliasMetaData, Serializable
{
   private static final long serialVersionUID = 2L;

   private Object alias;

   public Object getAliasValue()
   {
      return alias;
   }

   @XmlValue
   public void setAliasValue(Object alias)
   {
      this.alias = alias;
   }

   @XmlAnyElement
   public void setAlias(Object alias)
   {
      setAliasValue(alias);
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
      return alias.equals(amd.alias);
   }

   public AbstractAliasMetaData clone()
   {
      return (AbstractAliasMetaData)super.clone();
   }
}
