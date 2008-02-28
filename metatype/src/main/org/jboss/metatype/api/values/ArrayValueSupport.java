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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

import org.jboss.metatype.api.types.ArrayMetaType;

/**
 * ArrayValue.
 * 
 * TODO tests
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ArrayValueSupport extends AbstractMetaValue implements ArrayValue
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1131827130033538066L;

   /** The array meta type */
   private ArrayMetaType metaType;
   
   /** The value */
   private Object value;
   
   /**
    * Create a new ArrayValueSupport.
    * 
    * @param metaType the array meta type
    * @throws IllegalArgumentException for a null array MetaType
    */
   public ArrayValueSupport(ArrayMetaType metaType)
   {
      if (metaType == null)
         throw new IllegalArgumentException("Null array meta type");
      this.metaType = metaType;
   }
   
   /**
    * Create a new ArrayValueSupport.
    * 
    * @param metaType the array meta type
    * @param value the value
    * @throws IllegalArgumentException for a null array MetaType
    */
   public ArrayValueSupport(ArrayMetaType metaType, Object value)
   {
      this(metaType);
      this.value = value;
   }

   public ArrayMetaType getMetaType()
   {
      return metaType;
   }

   /**
    * Get the value.
    * 
    * @return the value.
    */
   public Object getValue()
   {
      return value;
   }

   /**
    * Get the length of the array.
    * @return length of the array.
    */
   public int getLength()
   {
      return Array.getLength(value);
   }

   public Object getValue(int index)
   {
      return Array.get(value, index);
   }

   public Iterator<Object> iterator()
   {
      return new ArrayValueIterator(value);
   }

   /**
    * Set the value.
    * 
    * @param value the value.
    */
   public void setValue(Object value)
   {
      this.value = value;
   } 

   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;

      if (obj == null || obj instanceof ArrayValue == false)
         return false;

      ArrayValue other = (ArrayValue) obj;
      if (metaType.equals(other.getMetaType()) == false)
         return false;

      Object otherValue = other.getValue();
      if (value == null && otherValue == null)
         return true;
      if (value == null && otherValue != null)
         return false;

      // Deep equals check
      boolean equals = false;
      if (value instanceof Object[] && otherValue instanceof Object[])
         equals = Arrays.deepEquals((Object[]) value, (Object[]) otherValue);
      else if (value instanceof byte[] && otherValue instanceof byte[])
         equals = Arrays.equals((byte[]) value, (byte[]) otherValue);
      else if (value instanceof short[] && otherValue instanceof short[])
         equals = Arrays.equals((short[]) value, (short[]) otherValue);
      else if (value instanceof int[] && otherValue instanceof int[])
         equals = Arrays.equals((int[]) value, (int[]) otherValue);
      else if (value instanceof long[] && otherValue instanceof long[])
         equals = Arrays.equals((long[]) value, (long[]) otherValue);
      else if (value instanceof char[] && otherValue instanceof char[])
         equals = Arrays.equals((char[]) value, (char[]) otherValue);
      else if (value instanceof float[] && otherValue instanceof float[])
         equals = Arrays.equals((float[]) value, (float[]) otherValue);
      else if (value instanceof double[] && otherValue instanceof double[])
         equals = Arrays.equals((double[]) value, (double[]) otherValue);
      else if (value instanceof boolean[] && otherValue instanceof boolean[])
         equals = Arrays.equals((boolean[]) value, (boolean[]) otherValue);
      else
         equals = value.equals(otherValue);
      return equals;
   }

   @Override
   public int hashCode()
   {
      if (value == null)
         return 0;
      return value.hashCode();
   }

   @Override
   public String toString()
   {
      return metaType + ":" + deepToString();
   }

   @Override
   public MetaValue clone()
   {
      ArrayValueSupport result = (ArrayValueSupport) super.clone();
      int length = getLength();
      if (value != null && length > 0)
      {
         // TODO: This is wrong as value is not an Object[] in general
         result.value = new Object[length];
         System.arraycopy(value, 0, result.value, 0, length);
      }
      return result;
   }

   /**
    * 
    * @return the deep string
    */
   protected String deepToString()
   {
      String deepToString;
      if (value == null)
         deepToString = "null";
      else if (value instanceof byte[])
         deepToString = Arrays.toString((byte[]) value);
      else if (value instanceof short[])
         deepToString = Arrays.toString((short[]) value);
      else if (value instanceof int[])
         deepToString = Arrays.toString((int[]) value);
      else if (value instanceof long[])
         deepToString = Arrays.toString((long[]) value);
      else if (value instanceof char[])
         deepToString = Arrays.toString((char[]) value);
      else if (value instanceof float[])
         deepToString = Arrays.toString((float[]) value);
      else if (value instanceof double[])
         deepToString = Arrays.toString((double[]) value);
      else if (value instanceof boolean[])
         deepToString = Arrays.toString((boolean[]) value);
      else if (value instanceof Object[])
         deepToString = Arrays.deepToString((Object[]) value);
      else
         deepToString = value.toString();
      return deepToString;
   }

   private static class ArrayValueIterator implements Iterator<Object>
   {
      private int index;
      private int length;
      private Object array;

      ArrayValueIterator(Object array)
      {
         this.array = array;
         this.index = 0;
         this.length = Array.getLength(array);
      }

      public boolean hasNext()
      {
         return index < length;
      }

      public Object next()
      {
         return Array.get(array, index ++);
      }

      public void remove()
      {
         throw new UnsupportedOperationException(); 
      }
      
   }
}
