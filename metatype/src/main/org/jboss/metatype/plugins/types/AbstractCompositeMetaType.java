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
package org.jboss.metatype.plugins.types;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import org.jboss.metatype.api.types.AbstractMetaType;
import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.values.CompositeValue;

/**
 * ImmutableCompositeMetaType.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractCompositeMetaType extends AbstractMetaType<Serializable> implements CompositeMetaType
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -7421421680257307598L;

   /** Item names to descriptions */
   private TreeMap<String, String> nameToDescription;

   /** Item names to meta types */
   private TreeMap<String, MetaType<?>> nameToType;
   
   /** The keys */
   private Set<String> keys;

   /**
    * Construct a composite meta type. The parameters are checked for validity.<p>
    *
    * The three arrays are internally copied. Future changes to these
    * arrays do not alter the composite type.<p>
    *
    * getClassName() returns {@link CompositeValue}
    *
    * @param typeName the name of the composite type, cannot be null or  empty
    * @param description the human readable description of the composite type, cannot be null or empty
    * @param itemNames the names of the items described by this type. Cannot
    *        be null, must contain at least one element, the elements cannot
    *        be null or empty. The order of the items is unimportant when
    *        determining equality.
    * @param itemDescriptions the human readable descriptions of the items
    *        in the same order as the itemNames, cannot be null must have the
    *        same number of elements as the itemNames. The elements cannot
    *        be null or empty.
    * @param itemTypes the MetaTypes of the items in the same order as the
    *        item names, cannot be null must have the
    *        same number of elements as the itemNames. The elements cannot
    *        be null.
    * @param ignoreItems whether to ignore items
    * @exception IllegalArgumentException when a parameter does not match
    *            what is described above or when itemNames contains a duplicate name.
    *            The names are case sensitive, leading and trailing whitespace
    *            is ignored.
    */
   protected AbstractCompositeMetaType(String typeName, String description, String[] itemNames, String[] itemDescriptions, MetaType<?>[] itemTypes, boolean ignoreItems)
   {
      super(CompositeValue.class.getName(), typeName, description);
      if (ignoreItems)
      {
         nameToDescription = new TreeMap<String, String>();
         nameToType = new TreeMap<String, MetaType<?>>();
         return;
      }
      if (itemNames == null || itemNames.length == 0)
         throw new IllegalArgumentException("null or empty itemNames");
      if (itemDescriptions == null || itemDescriptions.length == 0)
         throw new IllegalArgumentException("null or empty itemDescriptions");
      if (itemTypes == null || itemTypes.length == 0)
         throw new IllegalArgumentException("null or empty itemTypes");
      if (itemNames.length != itemDescriptions.length)
         throw new IllegalArgumentException("wrong number of itemDescriptions");
      if (itemNames.length != itemTypes.length)
         throw new IllegalArgumentException("wrong number of itemTypes");
      nameToDescription = new TreeMap<String, String>();
      nameToType = new TreeMap<String, MetaType<?>>();
      for (int i = 0; i < itemNames.length; ++i)
      {
         try
         {
            addItem(itemNames[i], itemDescriptions[i], itemTypes[i]);
         }
         catch (IllegalArgumentException e)
         {
            IllegalArgumentException e1 = new IllegalArgumentException(e.getMessage() + " for item " + i);
            e1.setStackTrace(e.getStackTrace());
            throw e1;
         }
      }
   }

   /**
    * Construct a composite meta type with no items.
    *
    * @param typeName the name of the composite type, cannot be null or  empty
    * @param description the human readable description of the composite type, cannot be null or empty
    * @throws IllegalArgumentException when a parameter does not match what is described above.
    */
   protected AbstractCompositeMetaType(String typeName, String description)
   {
      this(typeName, description, null, null, null, true);
   }

   /**
    * Set the keys
    * 
    * @param keySet the key set
    */
   protected void setKeys(Set<String> keySet)
   {
      if (keySet != null)
      {
         for (String key : keySet)
         {
            if (containsItem(key) == false)
               throw new IllegalArgumentException("Key " + key + " is not an item " + itemSet());
         }
      }
      keys = keySet;
   }
   
   /**
    * Add an item
    * 
    * @param itemName the item name
    * @param itemDescription the item description
    * @param itemType the item type
    * @throws IllegalArgumentException for a null or empty item name, description or type or duplicate item
    */
   protected void addItem(String itemName, String itemDescription, MetaType<?> itemType)
   {
      if (itemName == null)
         throw new IllegalArgumentException("null item name");
      itemName = itemName.trim();
      if (itemName.length() == 0)
         throw new IllegalArgumentException("empty item name");
      if (nameToDescription.containsKey(itemName))
         throw new IllegalArgumentException("duplicate item name " + itemName);
      if (itemDescription == null)
         throw new IllegalArgumentException("null item description");
      itemDescription = itemDescription.trim();
      if (itemDescription.length() == 0)
         throw new IllegalArgumentException("empty item description");
      if (itemType == null)
         throw new IllegalArgumentException("null item type");
      nameToDescription.put(itemName, itemDescription);
      nameToType.put(itemName, itemType);
   }

   public boolean containsItem(String itemName)
   {
      if (itemName == null)
         return false;
      return nameToDescription.containsKey(itemName);
   }

   public String getDescription(String itemName)
   {
      if (itemName == null)
         return null;
      return nameToDescription.get(itemName);
   }

   @Override
   public boolean isComposite()
   {
      return true;
   }

   public MetaType<?> getType(String itemName)
   {
      if (itemName == null)
         return null;
      return nameToType.get(itemName);
   }

   public Set<String> itemSet()
   {
      return Collections.unmodifiableSet(nameToDescription.keySet());
   }

   public Set<String> keySet()
   {
      if (keys == null)
         return itemSet();
      return Collections.unmodifiableSet(keys);
   }

   @Override
   public boolean isValue(Object obj)
   {
      if (obj == null || obj instanceof CompositeValue == false)
         return false;
      return equalsImpl(((CompositeValue) obj).getMetaType());
   }

   /**
    * Implementation of equals
    * 
    * @param obj the object to check
    * @return true when equals false otherwise
    */
   protected boolean equalsImpl(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null || obj instanceof CompositeMetaType == false)
         return false;

      CompositeMetaType other = (CompositeMetaType) obj;
      if (this.getTypeName().equals(other.getTypeName()) == false)
         return false;
      Iterator<String> thisNames = this.keySet().iterator();
      Iterator<String> otherNames = other.keySet().iterator();
      while(thisNames.hasNext() && otherNames.hasNext())
      {
         String thisName = thisNames.next();
         String otherName = otherNames.next();
         if (thisName.equals(otherName) == false)
            return false;
         
         if (this.getType(thisName).equals(other.getType(otherName)) == false)
            return false;
      }
      if (thisNames.hasNext() || otherNames.hasNext())
         return false;
      
      return true;
   }

   /**
    * Hashcode implementation
    * 
    * @return the hash code
    */
   protected int hashCodeImpl()
   {
      int hashCode = getTypeName().hashCode();
      for (Object o : nameToType.values())
         hashCode += o.hashCode();
      for (Object o : keySet())
         hashCode += o.hashCode();
      return hashCode;
   }

   @Override
   public String toString()
   {
      StringBuilder buffer = new StringBuilder(getClass().getSimpleName());
      buffer.append('{').append(getTypeName());
      Iterator<String> thisNames = keySet().iterator();
      if (thisNames.hasNext())
      {
         buffer.append(" items=");
         while(thisNames.hasNext())
         {
            String thisName = thisNames.next();
            buffer.append("[");
            buffer.append("name=");
            buffer.append(thisName);
            buffer.append(" type=");
            buffer.append(getType(thisName).getTypeName());
            buffer.append("]");
            if (thisNames.hasNext())
              buffer.append(", ");
         }
      }
      buffer.append('}');
      return buffer.toString();
   }
}
