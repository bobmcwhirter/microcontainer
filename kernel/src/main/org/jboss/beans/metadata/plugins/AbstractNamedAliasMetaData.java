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

import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.NamedAliasMetaData;
import org.jboss.util.JBossStringBuilder;

/**
 * Metadata for a named alias.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class AbstractNamedAliasMetaData extends AbstractAliasMetaData implements NamedAliasMetaData
{
   protected Object name;

   /**
    * Create a new named alias meta data
    */
   public AbstractNamedAliasMetaData()
   {
      super();
   }

   public Object getName()
   {
      return name;
   }

   public void setName(Object name)
   {
      this.name = name;
   }

   public void initialVisit(MetaDataVisitor visitor)
   {
      // todo
      super.initialVisit(visitor);
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("name=").append(name).append(" ");
      super.toString(buffer);
   }

   protected int getHashCode()
   {
      return name.hashCode() + 7 * super.getHashCode();
   }

   public boolean equals(Object object)
   {
      if (object == null || object instanceof AbstractNamedAliasMetaData == false || super.equals(object) == false)
         return false;
      
      AbstractNamedAliasMetaData amd = (AbstractNamedAliasMetaData)object;
      return name.equals(amd.name);
   }

}
