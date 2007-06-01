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
 * A representation of a ManagedOperation parameter
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public interface ManagedParameter extends Serializable
{
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
   
}
