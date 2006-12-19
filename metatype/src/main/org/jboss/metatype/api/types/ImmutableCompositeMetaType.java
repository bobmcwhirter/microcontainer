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

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import org.jboss.metatype.api.values.CompositeValue;

/**
 * ImmutableCompositeMetaType.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ImmutableCompositeMetaType extends AbstractMetaType implements CompositeMetaType
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1133171306971861455L;

   /** Item names to descriptions */
   private TreeMap<String, String> nameToDescription;

   /** Item names to meta types */
   private TreeMap<String, MetaType> nameToType;

   /** Cached hash code */
   private transient int cachedHashCode = Integer.MIN_VALUE;

   /** Cached string representation */
   private transient String cachedToString = null;

   /**
    * Construct a composite meta type. The parameters are checked for validity.<p>
    *
    * The three arrays are internally copied. Future changes to these
    * arrays do not alter the composite type.<p>
    *
    * getClassName() returns {@link CompositeValue}<p>
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
    * @exception IllegalArgumentException when a parameter does not match
    *            what is described above or when itemNames contains a duplicate name.
    *            The names are case sensitive, leading and trailing whitespace
    *            is ignored.
    */
   public ImmutableCompositeMetaType(String typeName, String description, String[] itemNames, String[] itemDescriptions, MetaType[] itemTypes)
   {
      super(CompositeValue.class.getName(), typeName, description);
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
      nameToType = new TreeMap<String, MetaType>();
      for (int i = 0; i < itemNames.length; ++i)
      {
          if (itemNames[i] == null)
             throw new IllegalArgumentException("null item name " + i);
          String itemName = itemNames[i].trim();
          if (itemName.length() == 0)
             throw new IllegalArgumentException("empty item name " + i);
          if (nameToDescription.containsKey(itemName))
             throw new IllegalArgumentException("duplicate item name " + itemName);
          if (itemDescriptions[i] == null)
             throw new IllegalArgumentException("null item description " + i);
          String itemDescription = itemDescriptions[i].trim();
          if (itemDescription.length() == 0)
             throw new IllegalArgumentException("empty item description " + i);
          if (itemTypes[i] == null)
             throw new IllegalArgumentException("null item type " + i);
          nameToDescription.put(itemName, itemDescription);
          nameToType.put(itemName, itemTypes[i]);
      }
   }

   public boolean containsKey(String itemName)
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

   public MetaType getType(String itemName)
   {
      if (itemName == null)
         return null;
      return nameToType.get(itemName);
   }

   public Set<String> keySet()
   {
      return Collections.unmodifiableSet(nameToDescription.keySet());
   }

   @Override
   public boolean isValue(Object obj)
   {
      if (obj == null || obj instanceof CompositeValue == false)
         return false;
      return equals(((CompositeValue) obj).getMetaType());
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null || obj instanceof ImmutableCompositeMetaType == false)
         return false;

      ImmutableCompositeMetaType other = (ImmutableCompositeMetaType) obj;
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

   @Override
   public int hashCode()
   {
      if (cachedHashCode != Integer.MIN_VALUE)
         return cachedHashCode;
      cachedHashCode = getTypeName().hashCode();
      for (Iterator i = nameToType.values().iterator(); i.hasNext();)
         cachedHashCode += i.next().hashCode();
      for (Iterator i = nameToDescription.keySet().iterator(); i.hasNext();)
         cachedHashCode += i.next().hashCode();
      return cachedHashCode;
   }

   @Override
   public String toString()
   {
      if (cachedToString != null)
         return cachedToString;
      StringBuilder buffer = new StringBuilder(getClass().getSimpleName());
      buffer.append("{items=");
      Iterator<String> thisNames = keySet().iterator();
      while(thisNames.hasNext())
      {
         String thisName = thisNames.next();
         buffer.append("[");
         buffer.append("name=");
         buffer.append(thisName);
         buffer.append(" type=");
         buffer.append(getType(thisName));
         buffer.append("]");
         if (thisNames.hasNext())
           buffer.append(", ");
      }
      cachedToString = buffer.toString();
      return cachedToString;
   }
}
