/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.metatype.api.types;

import org.jboss.metatype.api.values.GenericValue;

/**
 * GenericMetaType.<p>
 * 
 * This type allows objects that otherwise wouldn't otherwise be metatypes
 * to be passed as is inside the type system. Assuming that both
 * sides understand the value.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class GenericMetaType extends AbstractMetaType
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 6783554622082477064L;

   /**
    * Create a new GenericMetaType.
    * 
    * @param typeName the type name
    * @param description the description
    */
   public GenericMetaType(String typeName, String description)
   {
      super(GenericValue.class.getName(), typeName, description);
   }

   @Override
   public boolean isGeneric()
   {
      return true;
   }

   @Override
   public boolean isValue(Object obj)
   {
      if (obj == null || obj instanceof GenericValue == false)
         return false;

      // TODO some other check, e.g. typeName?
      return true;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null || obj instanceof GenericMetaType == false)
         return false;
      GenericMetaType other = (GenericMetaType) obj;
      return getTypeName().equals(other.getTypeName());
   }
   
   @Override
   public String toString()
   {
      return "GenericMetaType:" + getTypeName();
   }
}
