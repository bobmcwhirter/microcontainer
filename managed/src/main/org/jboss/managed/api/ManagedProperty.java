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
package org.jboss.managed.api;

import java.io.Serializable;
import java.util.Set;

import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.values.MetaValue;

/**
 * ManagedProperty.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public interface ManagedProperty extends Serializable
{
   /**
    * Get the managed object
    * 
    * @return the managed object
    */
   ManagedObject getManagedObject();
   
   /**
    * Get the fields
    * 
    * @return the fields
    */
   Fields getFields();
   
   /**
    * Get a field
    *
    * @param <T> the expected type
    * @param fieldName the field name
    * @param expected the expected type
    * @return the value
    */
   <T> T getField(String fieldName, Class<T> expected);
   
   /**
    * Set a field
    *
    * @param fieldName the field name
    * @param value the value
    */
   void setField(String fieldName, Serializable value);
   
   /**
    * Get the property's name
    * 
    * @return the property's name
    */
   String getName();

   /**
    * Get the description
    * 
    * @return the description
    */
   String getDescription();

   /**
    * Get the type
    * 
    * @return the type
    */
   MetaType getMetaType();

   /**
    * Get the value
    * 
    * @return the value
    */
   Object getValue();

   /**
    * Set the value
    * 
    * @param value the value
    */
   void setValue(Serializable value);

   /**
    * Get the legal values
    * 
    * @return the legal values
    */
   Set<MetaValue> getLegalValues();

   /**
    * Get the minimum value
    * 
    * @return the minimum value
    */
   Comparable getMinimumValue();

   /**
    * Get the miximum value
    * 
    * @return the maximum value
    */
   Comparable getMaximumValue();

   /**
    * Check whether this is a valid value
    * 
    * @param value the value
    * @return null for a valid value, an error message otherwise
    */
   String checkValidValue(Serializable value);
   
   /**
    * Whether the property is mandatory
    * 
    * @return true when mandatory
    */
   boolean isMandatory();
}
