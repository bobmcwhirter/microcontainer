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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamField;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.TableMetaType;

/**
 * TableValueSupport.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class TableValueSupport extends AbstractMetaValue implements TableValue
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -6862672408820383430L;

   /** The serialized form */
   private static final ObjectStreamField[] serialPersistentFields =
      new ObjectStreamField[]
      {
         new ObjectStreamField("dataMap",  HashMap.class),
         new ObjectStreamField("tableType", TableMetaType.class),
      };

   /** The data map of this tabular data */
   private HashMap<List<MetaValue>, CompositeValue> dataMap;

   /** The table meta data type */
   private TableMetaType tableType;

   /** The index names */
   private transient String[] indexNames;

   /**
    * Construct Table Value with an initial capacity of 101 and a load
    * factor of 0.75
    *
    * @param tableType the table type of the data
    * @exception IllegalArgumentException for a null argument
    */
   public TableValueSupport(TableMetaType tableType)
   {
      this(tableType, 101, 0.75f);
   }

   /**
    * Construct Table value
    *
    * @param tableType the table type
    * @param initialCapacity the initial capacity of the map
    * @param loadFactor the load factory of the map
    * @exception IllegalArgumentException for a null argument
    */
   public TableValueSupport(TableMetaType tableType, int initialCapacity, float loadFactor)
   {
      init(new HashMap<List<MetaValue>, CompositeValue>(initialCapacity, loadFactor), tableType);
   }

   public TableMetaType getMetaType()
   {
      return tableType;
   }

   public MetaValue[] calculateIndex(CompositeValue value)
   {
      validateCompositeValue(value);
      return value.getAll(indexNames);
   }

   public void clear()
   {
      dataMap.clear();
   }

   public boolean containsKey(MetaValue[] key)
   {
      if (key == null)
         return false;
      return dataMap.containsKey(Arrays.asList(key));
   }

   public boolean containsValue(CompositeValue value)
   {
      return dataMap.containsValue(value);
   }

   public CompositeValue get(MetaValue[] key)
   {
      validateKey(key);
      return dataMap.get(Arrays.asList(key));
   }

   public boolean isEmpty()
   {
      return dataMap.isEmpty();
   }

   public Set<List<MetaValue>> keySet()
   {
      return dataMap.keySet();
   }

   public void put(CompositeValue value)
   {
      List<MetaValue> index = Arrays.asList(calculateIndex(value));
      if (dataMap.containsKey(index))
         throw new IllegalArgumentException("The index is already used " + index);
      dataMap.put(index, value);
   }

   public void putAll(CompositeValue[] values)
   {
      if (values == null)
         return;

      HashSet<List<MetaValue>> keys = new HashSet<List<MetaValue>>();
      for (int i = 0; i < values.length; ++i)
      {
         List<MetaValue> index = Arrays.asList(calculateIndex(values[i]));
         if (keys.contains(index))
            throw new IllegalArgumentException("Duplicate index in values " + index + " for value " + values[i]);
         keys.add(index);
         if (dataMap.containsKey(index))
            throw new IllegalArgumentException("Index already used " + index + " for value " + values[i]);
      }
      for (int i = 0; i < values.length; i++)
         put(values[i]);
   }

   public CompositeValue remove(MetaValue[] key)
   {
      validateKey(key);
      return dataMap.remove(Arrays.asList(key));
   }

   public int size()
   {
      return dataMap.size();
   }

   public Collection<CompositeValue> values()
   {
      return dataMap.values();
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null || (obj instanceof TableValue) == false)
         return false;

      TableValue other = (TableValue) obj;
      if (tableType.equals(other.getMetaType()) == false)
         return false;
      if (size() != other.size())
         return false;
      for (Entry<List<MetaValue>, CompositeValue> entry : dataMap.entrySet())
      {
         List<MetaValue> list = entry.getKey();
         MetaValue[] indexes = list.toArray(new MetaValue[list.size()]);
         CompositeValue thisValue = entry.getValue();
         CompositeValue otherValue = other.get(indexes);
         if (thisValue == null && otherValue == null)
            return true;
         if (thisValue == null && otherValue != null)
            return false;
         if (thisValue.equals(otherValue) == false)
            return false;
      }
      return true;
   }

   @Override
   public int hashCode()
   {
      int hash = tableType.hashCode();
      for (CompositeValue value : dataMap.values())
         hash += value.hashCode();
      return hash;
   }

   @Override
   public String toString()
   {
      StringBuilder buffer = new StringBuilder(getClass().getSimpleName());
      buffer.append(": tableType=[");
      buffer.append(getMetaType());
      buffer.append("] mappings=[");
      Iterator<Entry<List<MetaValue>, CompositeValue>> entries = dataMap.entrySet().iterator();
      while(entries.hasNext())
      {
         Entry<List<MetaValue>, CompositeValue> entry = entries.next(); 
         buffer.append(entry.getKey());
         buffer.append("=");
         buffer.append(entry.getValue());
         if (entries.hasNext())
            buffer.append(",");
      }
      buffer.append("]");
      return buffer.toString();
   }

   @Override
   @SuppressWarnings("unchecked")
   public TableValueSupport clone()
   {
      TableValueSupport result = (TableValueSupport) super.clone();
      result.dataMap = (HashMap<List<MetaValue>, CompositeValue>) dataMap.clone();
      return result;
   }

   /**
    * Initialise the table value
    *
    * @param dataMap the data
    * @param tableType the table type
    * @exception IllegalArgumentException for a null
    */
   private void init(HashMap<List<MetaValue>, CompositeValue> dataMap, TableMetaType tableType)
   {
      if (dataMap == null)
         throw new IllegalArgumentException("null dataMap");
      if (tableType == null)
         throw new IllegalArgumentException("null table type");

      this.dataMap = dataMap;
      this.tableType = tableType;
      List<String> indexNameList = tableType.getIndexNames();
      this.indexNames = indexNameList.toArray(new String[indexNameList.size()]);
   }

   /**
    * Validate the composite type against the row type
    *
    * @param value the composite value
    * @throws IllegalArgumentException for a null value or if the value is not valid for the table value's row type
    */
   private void validateCompositeValue(CompositeValue value)
   {
      if (value == null)
         throw new IllegalArgumentException("null value");
      if (value.getMetaType().equals(tableType.getRowType()) == false)
         throw new IllegalArgumentException("value has composite type " + value.getMetaType() + " expected row type " + tableType.getRowType());
   }

   /**
    * Validate the key against the row type
    *
    * @param key the key to check
    * @throws IllegalArgumentException for a null key or if the key is not valid for the table value's row type
    */
   private void validateKey(MetaValue[] key)
   {
      if (key == null || key.length == 0)
         throw new IllegalArgumentException("null or empty key");

      if (key.length != indexNames.length)
         throw new IllegalArgumentException("key has " + key.length + " elements, " + "should be " + indexNames.length);
      for (int i = 0; i < key.length; i++)
      {
         MetaType<?> metaType = tableType.getRowType().getType(indexNames[i]);
         if (key[i] != null && metaType.isValue(key[i]) == false)
            throw new IllegalArgumentException("key element " + i + " " + key + " is not a value for " + metaType);
      }
   }

   @SuppressWarnings("unchecked")
   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
   {
      ObjectInputStream.GetField getField = in.readFields();
      HashMap<List<MetaValue>, CompositeValue> dataMap = (HashMap<List<MetaValue>, CompositeValue>) getField.get("dataMap", null);
      TableMetaType tableType = (TableMetaType) getField.get("tableType", null);
      try
      {
         init(dataMap, tableType);
      }
      catch (Exception e)
      {
         throw new RuntimeException("Unexpected error during deserialization", e);
      }
   }
}
