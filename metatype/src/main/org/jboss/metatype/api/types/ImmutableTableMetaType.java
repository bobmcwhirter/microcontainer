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
import java.util.Iterator;
import java.util.List;

import org.jboss.metatype.api.values.TableValue;

public class ImmutableTableMetaType extends AbstractMetaType implements TableMetaType
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 5791103660662775558L;

   /** The open type of the rows */
   private CompositeMetaType rowType;

   /** Index names */
   private List<String> indexNames;

   /** Cached hash code */
   private transient int cachedHashCode = Integer.MIN_VALUE;

   /** Cached string representation */
   private transient String cachedToString = null;

   /**
    * Construct a tabe type. The parameters are checked for validity.
    *
    * @param typeName the name of the tabular type, cannot be null or empty
    * @param description the human readable description of the tabular type, cannot be null or empty
    * @param rowType the type of the row elements in the tabular data, cannot be null
    * @param indexNames the names of the item values that uniquely index each
    *        row element in the tabular data, cannot be null or empty. Each
    *        element must be an item name in the rowType, nul or empty is not
    *        allowed. The order of the item names in this parameter is used
    *        by {@link TableValue#get} and {@link TableValue#remove} the 
    *        TabularValue to match the array of values to items.
    * @throws IllegalArgumentException when a parameter does not match
    *         what is described above or when an element of indexNames is not defined
    *         in rowType.
    */
   public ImmutableTableMetaType(String typeName, String description, CompositeMetaType rowType, String[] indexNames)
   {
      super(TableValue.class.getName(), typeName, description);
      if (rowType == null)
         throw new IllegalArgumentException("null rowType");
      if (indexNames == null || indexNames.length == 0)
         throw new IllegalArgumentException("null or empty indexNames");
      this.rowType = rowType;
      this.indexNames = new ArrayList<String>();
      for (int i = 0; i < indexNames.length; i++)
      {
          if (indexNames[i] == null)
             throw new IllegalArgumentException("null index name " + i);
          String indexName = indexNames[i].trim();
          if (indexName.length() == 0)
             throw new IllegalArgumentException("empty index name " + i);
          if (rowType.containsKey(indexName) == false)
             throw new IllegalArgumentException("no item name " + indexName);
          this.indexNames.add(indexName);
      }
   }

   public CompositeMetaType getRowType()
   {
      return rowType;
   }

   public List<String> getIndexNames()
   {
      return Collections.unmodifiableList(indexNames);
   }

   @Override
   public boolean isValue(Object obj)
   {
      if (obj == null || obj instanceof TableValue == false)
         return false;
      TableMetaType other = ((TableValue) obj).getMetaType();
      return equals(other);
   }
   
   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null || obj instanceof TableMetaType == false)
         return false;

      TableMetaType other = (TableMetaType) obj;
      if (this.getTypeName().equals(other.getTypeName()) == false)
         return false;
      if (this.getRowType().equals(other.getRowType()) == false)
         return false;
      Iterator<String> thisNames = this.getIndexNames().iterator();
      Iterator<String> otherNames = other.getIndexNames().iterator();
      while (thisNames.hasNext() && otherNames.hasNext())
      {
         String thisName = thisNames.next();
         String otherName = otherNames.next();
         if (thisName.equals(otherName) == false)
            return false;
      }
      if (thisNames.hasNext() || otherNames.hasNext())
         return false;

      return true;
   }

   @Override
   public int hashCode()
   {
      if (cachedHashCode != Integer.MIN_VALUE)
         return cachedHashCode;
      cachedHashCode = getTypeName().hashCode();
      cachedHashCode += getRowType().hashCode();
      for (Iterator i = indexNames.iterator(); i.hasNext();)
         cachedHashCode += i.next().hashCode();
      return cachedHashCode;
   }

   @Override
   public String toString()
   {
      if (cachedToString != null)
         return cachedToString;
      StringBuilder buffer = new StringBuilder(getClass().getSimpleName());
      buffer.append(": typeName=[");
      buffer.append(getTypeName());
      buffer.append("] rowType=[");
      buffer.append(getRowType());
      buffer.append("] indexNames=[");
      Iterator thisNames = getIndexNames().iterator();
      while(thisNames.hasNext())
      {
         buffer.append(thisNames.next());
         if (thisNames.hasNext())
            buffer.append(", ");
      }
      buffer.append("]");
      cachedToString = buffer.toString();
      return cachedToString;
   }
}
