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
package org.jboss.metatype.api.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.metatype.api.values.SimpleValue;

/**
 * EnumMetaType.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class EnumMetaType extends AbstractMetaType
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 6786422588217893696L;

   /** The valid values */
   private List<String> validValues; 
   
   /**
    * Create a new EnumMetaType.
    * 
    * @param className the class name
    * @param validValues the valid values
    */
   public EnumMetaType(String className, List<String> validValues)
   {
      super(String.class.getName(), className, className);
      if (validValues == null)
         throw new IllegalArgumentException("Null valid values");
      this.validValues = validValues;
   }
   /**
    * Create a new EnumMetaType from the Enum values.
    * 
    * @param validValues the valid Enum values
    */
   public EnumMetaType(Enum[] validValues)
   {
      super(
            String.class.getName(),
            isValid(validValues) ? validValues[0].getClass().getName() : null,
            isValid(validValues) ? validValues[0].getClass().getName() : null
      );
      if (isValid(validValues) == false)
         throw new IllegalArgumentException("Null or empty valid values");
      ArrayList<String> values = new ArrayList<String>();
      for (Enum e : validValues)
         values.add(e.name());
      this.validValues = values;
   }

   /**
    * Are enums valid.
    *
    * @param values the enums
    * @return true if not null and not empty
    */
   protected static boolean isValid(Enum[] values)
   {
      return values != null && values.length > 0;
   }

   /**
    * Get the valid values
    * 
    * @return the valid values
    */
   public List<String> getValidValues()
   {
      return Collections.unmodifiableList(validValues);
   }
   
   @Override
   public boolean isEnum()
   {
      return true;
   }

   @Override
   public boolean isValue(Object obj)
   {
      if (obj == null || obj instanceof SimpleValue == false)
         return false;

      SimpleValue value = (SimpleValue) obj;
      if (SimpleMetaType.STRING == value.getMetaType() == false)
         return false;
      return validValues.contains(value.getValue());
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null || obj instanceof EnumMetaType == false)
         return false;
      EnumMetaType other = (EnumMetaType) obj;
      return getTypeName().equals(other.getTypeName()) && getValidValues().equals(other.getValidValues());
   }
   
   @Override
   public String toString()
   {
      return getTypeName() + "{" + validValues + "}";
   }
}
