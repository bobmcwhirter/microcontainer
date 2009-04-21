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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlTransient;

import org.jboss.beans.metadata.spi.NamedAliasMetaData;
import org.jboss.util.JBossStringBuilder;

/**
 * Metadata for a named alias.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
@XmlType(name="namedAliasType")
public class AbstractNamedAliasMetaData extends AbstractAliasMetaData implements NamedAliasMetaData
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 2L;
   
   /** The name */
   protected Object name;

   /**
    * Create a new named alias meta data
    */
   public AbstractNamedAliasMetaData()
   {
      super();
   }

   @XmlTransient
   public Object getName()
   {
      if (name == null)
         throw new IllegalArgumentException("Name should not be null");

      return name;
   }

   @XmlAttribute(name = "name", required = true)
   public void setNameString(String name)
   {
      setName(name);
   }

   public void setName(Object name)
   {
      this.name = name;
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("name=").append(getName()).append(" ");
      super.toString(buffer);
   }

   protected int getHashCode()
   {
      return getName().hashCode() + 7 * super.getHashCode();
   }

   public boolean equals(Object object)
   {
      if (object == null || object instanceof AbstractNamedAliasMetaData == false || super.equals(object) == false)
         return false;
      
      AbstractNamedAliasMetaData amd = (AbstractNamedAliasMetaData)object;
      return getName().equals(amd.getName());
   }

   public AbstractNamedAliasMetaData clone()
   {
      return (AbstractNamedAliasMetaData)super.clone();
   }
}
