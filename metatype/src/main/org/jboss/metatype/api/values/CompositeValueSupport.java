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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.types.MetaType;

/**
 * CompositeValueSupport.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class CompositeValueSupport extends AbstractMetaValue implements CompositeValue
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 6262188760975631870L;
   
   /** The serialized form */
   private static final ObjectStreamField[] serialPersistentFields =
      new ObjectStreamField[]
      {
         new ObjectStreamField("contents", SortedMap.class),
         new ObjectStreamField("metaType", CompositeMetaType.class),
      };

   /** The contents */
   private SortedMap<String, MetaValue> contents;

   /** The composite type */
   private CompositeMetaType metaType;

   /** cached hashCode */
   private transient int cachedHashCode = Integer.MIN_VALUE;

   /**
    * Construct Composite Value 
    *
    * @param metaType the composite meta type of the data
    * @param itemNames the names of the values
    * @param itemValues the values
    * @throws IllegalArgumentException for a null or empty argument or when the items do not match the
    *         CompositeType
    */
   public CompositeValueSupport(CompositeMetaType metaType, String[] itemNames, MetaValue[] itemValues)
   {
      if (metaType == null)
         throw new IllegalArgumentException("null meta type");
      if (itemNames == null)
         throw new IllegalArgumentException("null itemNames");
      if (itemValues == null)
         throw new IllegalArgumentException("null itemValues");
      if (itemNames.length == 0)
         throw new IllegalArgumentException("empty itemNames");
      if (itemValues.length == 0)
         throw new IllegalArgumentException("empty itemValues");
      if (itemNames.length != itemValues.length)
         throw new IllegalArgumentException("itemNames has size " + itemNames.length + " but itemValues has size " + itemValues.length);

      int compositeNameSize = metaType.keySet().size();
      if (itemNames.length != compositeNameSize)
         throw new IllegalArgumentException("itemNames has size " + itemNames.length + " but composite type has size " + compositeNameSize);

      this.metaType = metaType;
      contents = new TreeMap<String, MetaValue>();

      for (int i = 0; i < itemNames.length; i++)
      {
         if (itemNames[i] == null || itemNames[i].length() == 0)
            throw new IllegalArgumentException("Item name " + i + " is null or empty");
         if (contents.get(itemNames[i]) != null)
            throw new IllegalArgumentException("duplicate item name " + itemNames[i]);
         MetaType itemType = metaType.getType(itemNames[i]);
         if (itemType == null)
            throw new IllegalArgumentException("item name not in composite type " + itemNames[i]);
         if (itemValues[i] != null && itemType.isValue(itemValues[i]) == false)
            throw new IllegalArgumentException("item value " + itemValues[i] + " for item name " + itemNames[i] + " is not a " + itemType);
         contents.put(itemNames[i], itemValues[i]);
      }
   }

   /**
    * Construct Composite Value 
    *
    * @param compositeMetaType the composite type of the data
    * @param items map of strings to values
    * @throws IllegalArgumentException for a null or empty argument or when the items do not match the
    *         CompositeType
    */
   public CompositeValueSupport(CompositeMetaType compositeMetaType, Map<String, MetaValue> items)
   {
      init(compositeMetaType, items);
   }

   public CompositeMetaType getMetaType()
   {
      return metaType;
   }

   public MetaValue get(String key)
   {
      validateKey(key);
      return contents.get(key);
   }

   public MetaValue[] getAll(String[] keys)
   {
      if (keys == null)
         throw new IllegalArgumentException("Null keys");

      MetaValue[] result = new MetaValue[keys.length];
      for (int i = 0; i < keys.length; i++)
      {
         validateKey(keys[i]);
         result[i] = contents.get(keys[i]);
      }
      return result;
   }

   public boolean containsKey(String key)
   {
      if (key == null || key.length() == 0)
         return false;
      return contents.containsKey(key);
   }

   public boolean containsValue(MetaValue value)
   {
      return contents.containsValue(value);
   }

   public Collection<MetaValue> values()
   {
      return Collections.unmodifiableCollection(contents.values());
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;
      if (obj == null || obj instanceof CompositeValue == false)
         return false;

      CompositeValue other = (CompositeValue) obj;
      if (getMetaType().equals(other.getMetaType()) == false)
         return false;
      if (values().size() != other.values().size())
         return false;

      for (String key : contents.keySet())
      {
         Object thisValue = this.get(key);
         Object otherValue = other.get(key);
         if ((thisValue == null && otherValue == null || thisValue != null && thisValue.equals(otherValue)) == false)
            return false;
      }
      return true;
   }
   
   @Override
   public int hashCode()
   {
      if (cachedHashCode != Integer.MIN_VALUE)
         return cachedHashCode;

      cachedHashCode = getMetaType().hashCode();
      for (Object value : contents.values())
      {
         if (value != null)
            cachedHashCode += value.hashCode();
      }
      
      return cachedHashCode;
   }

   @Override
   public String toString()
   {
      CompositeMetaType metaType = getMetaType();
      StringBuilder buffer = new StringBuilder(getClass().getSimpleName());
      buffer.append(": netaType=[");
      buffer.append(metaType);
      buffer.append("] mappings=[");
      Iterator keys = metaType.keySet().iterator();
      while(keys.hasNext())
      {
         Object key = keys.next();
         buffer.append(key + "=" + contents.get(key));
         if (keys.hasNext())
            buffer.append(",");
      }
      buffer.append("]");
      return buffer.toString();
   }

   /**
    * Validates the key against the composite type
    *
    * @param key the key to check
    * @throws IllegalArgumentException for a null or empty key or when 
    *         the key not a valid item name for the composite type
    */
   private void validateKey(String key)
   {
      if (key == null || key.length() == 0)
         throw new IllegalArgumentException("null or empty key");
      CompositeMetaType metaType = getMetaType();
      if (metaType.containsKey(key) == false)
         throw new IllegalArgumentException("no such item name " + key + " for composite type " + metaType);
   }

   /**
    * Construct Composite Value 
    *
    * @param metaType the composite type of the data
    * @param items map of strings to values
    * @throws IllegalArgumentException for a null or empty argument or when the items do not match the
    *         CompositeType
    */
   private void init(CompositeMetaType metaType, Map<String, MetaValue> items)
   {
      if (metaType == null)
         throw new IllegalArgumentException("null meta type");
      if (items == null)
         throw new IllegalArgumentException("null items");
      if (items.size() == 0)
         throw new IllegalArgumentException("empty items");
      int compositeNameSize = metaType.keySet().size();
      if (items.size() != compositeNameSize)
         throw new IllegalArgumentException("items has size " + items.size() + " but composite type has size " + compositeNameSize);

      this.metaType = metaType;
      contents = new TreeMap<String, MetaValue>();

      for (Entry<String, MetaValue> entry : items.entrySet())
      {
         String key = entry.getKey();
         if (key == null || key.length() == 0)
            throw new IllegalArgumentException("Key is null or empty");
         MetaType itemType = metaType.getType(key);
         if (itemType == null)
            throw new IllegalArgumentException("item name not in composite type " + key);
         MetaValue value = items.get(key);
         if (value != null && itemType.isValue(value) == false)
            throw new IllegalArgumentException("item value " + value + " for item name " + key + " is not a " + itemType);
         contents.put(key, value);
      }
   }

   @SuppressWarnings("unchecked")
   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
   {
      ObjectInputStream.GetField getField = in.readFields();
      SortedMap contents = (SortedMap) getField.get("contents", null);
      CompositeMetaType compositeType = (CompositeMetaType) getField.get("metaType", null);
      try
      {
         init(compositeType, contents);
      }
      catch (Exception e)
      {
         throw new RuntimeException("Error deserializing composite value", e);
      }
   }
}