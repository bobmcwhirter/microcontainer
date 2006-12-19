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
package org.jboss.metatype.api.values;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.jboss.metatype.api.types.TableMetaType;

public interface TableValue extends MetaValue
{
   TableMetaType getMetaType();

   /**
    * Calculate the index for the value passed if it were added to the
    * table value. The validity of the passed value is checked. But the
    * table value isn't checked to see whether the index is already used.
    *
    * @param value the value for which the index is calculated.
    * @return the calculated index
    * @throws IllegalArgumentException for a null value or when the passed value is not valid for the table value's row type.
    */
   MetaValue[] calculateIndex(CompositeValue value);

   /**
    * Retrieve the number of rows in the table value.
    *
    * @return the number of rows.
    */
   int size();

   /**
    * Determine whether the table value is empty.
    *
    * @return true when there are no rows, false otherwise
    */
   boolean isEmpty();

   /**
    * Determine whether the table value contains the passed value as a row.
    * If the passed value is null or invalid, false is returned.
    *
    * @param key the value to check
    * @return true when the value is a row index, false otherwise
    */
   boolean containsKey(MetaValue[] key);

   /**
    * Determine whether the table value contains the passed value.
    * If the passed value is null or invalid, false is returned.
    *
    * @param value the value to check
    * @return true when the value is a row index, false otherwise
    */
   boolean containsValue(CompositeValue value);

   /**
    * Retrieve the composite value for the passed index.
    *
    * @param key the index to retrieve
    * @return the composite value
    * @throws IllegalArgumentException when the passed key is null or when the passed key does match the row type of the table value.
    */
   CompositeValue get(MetaValue[] key);

   /**
    * Add a value to the table value. The value must have the same
    * CompositeMetaType has the table value and there is no value already
    * occupying the index for the value.
    *
    * @param value the value to add
    * @throws IllegalArgumentException when the passed value is null or when the value is not valid for
    *         the row type of the tabular data or when the index for the value is already occupied.
    */
   void put(CompositeValue value);

   /**
    * Removes the value for the passed and returns the removed value, or
    * null if the key was not present.
    *
    * @param key the index of the value to remove
    * @return the removed value
    * @throws IllegalArgumentException when the passed key is null or when the key is not valid for the table value
    */
   CompositeValue remove(MetaValue[] key);
      
   /**
    * Add all the passed values. All the values are checked before
    * addition including any duplicates that might be added. Either all
    * or no value is added.
    *
    * @param values the values to add
    * @throws IllegalArgumentException when the passed values is null or an element of the values is null
    *         or when one of value is not valid for the row type of the table value or when
    *         the index for one of the values is already occupied.
    */
   void putAll(CompositeValue[] values);
      
   /**
    * Removes all CompositeValues from the Table value
    */
   void clear();
      
   /**
    * Returns a set view of the index values.
    *
    * @return the set of index values.
    */
   Set<List<MetaValue>> keySet();
   
   /**
    * Returns a set view of the row values.
    *
    * @return the set of row values.
    */
   Collection<CompositeValue> values();
}
