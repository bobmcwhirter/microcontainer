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

import java.util.Arrays;
import java.util.Iterator;

import org.jboss.metatype.api.types.CollectionMetaType;

/**
 * CollectionValueSupport.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class CollectionValueSupport extends AbstractMetaValue implements CollectionValue
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1131827130033538066L;

   /** The collection meta type */
   private CollectionMetaType<?> metaType;

   /** The elements */
   private MetaValue[] elements;

   /**
    * Create a new CollectionValueSupport.
    *
    * @param metaType the collection meta type
    * @throws IllegalArgumentException for a null array MetaType
    */
   public CollectionValueSupport(CollectionMetaType<?> metaType)
   {
      if (metaType == null)
         throw new IllegalArgumentException("Null collection meta type");
      this.metaType = metaType;
   }

   /**
    * Create a new ArrayValueSupport.
    *
    * @param metaType the collection meta type
    * @param elements the elements
    * @throws IllegalArgumentException for a null array MetaType
    */
   public CollectionValueSupport(CollectionMetaType<?> metaType, MetaValue[] elements)
   {
      this(metaType);
      this.elements = elements;
   }

   public CollectionMetaType<?> getMetaType()
   {
      return metaType;
   }

   /**
    * Get the value.
    *
    * @return the value.
    */
   public MetaValue[] getElements()
   {
      return elements;
   }

   /**
    * Get the size of the collection.
    * @return size of the collection.
    */
   public int getSize()
   {
      return elements != null ? elements.length : 0;
   }

   public Iterator<MetaValue> iterator()
   {
      return new ElementsIterator(elements);
   }

   /**
    * Set the value.
    *
    * @param elements the elements.
    */
   public void setElements(MetaValue[] elements)
   {
      this.elements = elements;
   }

   @Override
   @SuppressWarnings("unchecked")
   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;

      if (obj == null || obj instanceof CollectionValue == false)
         return false;

      CollectionValue other = (CollectionValue) obj;
      if (metaType.equals(other.getMetaType()) == false)
         return false;

      Object[] otherElements = other.getElements();
      if (elements == null)
         return otherElements == null;

      return otherElements != null && Arrays.equals(elements, otherElements);
   }

   @Override
   public int hashCode()
   {
      if (elements == null)
         return 0;
      return elements.hashCode();
   }

   @Override
   public String toString()
   {
      return metaType + ": " + elements;
   }

   @Override
   @SuppressWarnings("unchecked")
   public MetaValue clone()
   {
      CollectionValueSupport result = (CollectionValueSupport) super.clone();
      int size = getSize();
      if (size > 0)
      {
         result.elements = new MetaValue[size];
         System.arraycopy(elements, 0, result.elements, 0, size);
      }
      return result;
   }

   private static class ElementsIterator implements Iterator<MetaValue>
   {
      private int index;
      private int length;
      private MetaValue[] elements;

      ElementsIterator(MetaValue[] elements)
      {
         this.elements = elements;
         this.index = 0;
         this.length = elements != null ? elements.length : 0;
      }

      public boolean hasNext()
      {
         return index < length;
      }

      public MetaValue next()
      {
         return elements[index++];
      }

      public void remove()
      {
         throw new UnsupportedOperationException();
      }
   }
}
