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

import java.io.Serializable;

/**
 * ArrayMetaType.
 *
 * @param <T> the underlying type
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ArrayMetaType<T extends Serializable> extends AbstractMetaType<T>
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -2062790692152055156L;

   /** The number of dimensions in the array */
   private int dimension = 0;

   /** The element type for the array */
   private MetaType elementType;

   /** Cached hash code */
   private transient int cachedHashCode = Integer.MIN_VALUE;

   /** Cached string representation */
   private transient String cachedToString = null;

   /**
    * Generate the class and type name
    * 
    * @param dimension the dimension
    * @param elementType the element type
    */
   private static String genName(int dimension, MetaType elementType)
   {
      if (dimension < 1)
         throw new IllegalArgumentException("negative dimension");
      if (elementType == null)
         throw new IllegalArgumentException("null element type");
      if (elementType instanceof ArrayMetaType)
         throw new IllegalArgumentException("array type cannot be an element of an array type");
      StringBuilder buffer = new StringBuilder();
      for (int i=0; i < dimension; i++)
         buffer.append('[');
      buffer.append('L');
      buffer.append(elementType.getClassName());
      buffer.append(';');
      return buffer.toString();
   }

   /**
    * Generate the description
    * 
    * @param dimension the dimension
    * @param elementType the element type
    */
   private static String genDesc(int dimension, MetaType elementType)
   {
      StringBuilder buffer = new StringBuilder();
      buffer.append(new Integer(dimension));
      buffer.append("-dimension array of ");
      buffer.append(elementType.getClassName());
      return buffer.toString();
   }

   /**
    * Construct an ArrayMetaType.
    *
    * @param dimension the number of dimensions in the array
    * @param elementType the open type of the array elements
    * @throws IllegalArgumentException for a null argument or non-negative dimension or when meta type is an ArrayMetaType
    */
   public ArrayMetaType(int dimension, MetaType elementType)
   {
      super(genName(dimension, elementType), genDesc(dimension, elementType));
      this.dimension = dimension;
      this.elementType = elementType;
   }

   /**
    * Get the dimension of the array
    *
    * @return the dimension
    */
   public int getDimension()
   {
      return dimension;
   }

   /**
    * Get the meta type of the array elements
    *
    * @return the element type
    */
   public MetaType getElementType()
   {
      return elementType;
   }

   @Override
   @SuppressWarnings("unchecked")
   public boolean isValue(Object obj)
   {
      if (obj == null)
         return false;

      Class clazz = obj.getClass();
      if (clazz.isArray() == false)
         return false;
      
      if (elementType instanceof SimpleMetaType)
         return recursiveCheck((Object[]) obj, dimension);
      
      if (elementType instanceof TableMetaType || elementType instanceof CompositeMetaType)
      {
         Class thisClass = null;
         try
         {
            thisClass = Thread.currentThread().getContextClassLoader().loadClass(getClassName());
         }
         catch (ClassNotFoundException e)
         {
            return false;
         }
         if (thisClass.isAssignableFrom(clazz) == false)
            return false;
         return recursiveCheck((Object[]) obj, dimension);
      }
      return false;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null || obj instanceof ArrayMetaType == false)
         return false;
      ArrayMetaType other = (ArrayMetaType) obj;
      return getDimension() == other.getDimension() && getElementType().equals(other.getElementType());
   }

   @Override
   public int hashCode()
   {
      if (cachedHashCode != Integer.MIN_VALUE)
         return cachedHashCode;
      cachedHashCode = getDimension() + getElementType().hashCode();
      return cachedHashCode;
   }

   @Override
   public String toString()
   {
      if (cachedToString != null)
         return cachedToString;
      StringBuilder buffer = new StringBuilder(ArrayMetaType.class.getSimpleName());
      buffer.append("{type=");
      buffer.append(getTypeName());
      buffer.append(" dims=");
      buffer.append(dimension);
      buffer.append(" elementType=" + elementType);
      cachedToString = buffer.toString();
      return cachedToString;
   }

   /**
    * Recursively check array elements
    * 
    * @param elements the elements
    * @param dimension the dimension
    */
   private boolean recursiveCheck(Object[] elements, int dimension)
   {
      // Reached the end
      if (dimension == 1)
      {
         // Check each element is the correct type
         for (int i = 0; i < elements.length; i++)
         {
            if (elements[i] != null && elementType.isValue(elements[i]) == false)
               return false;
         }
      }
      else
      {
         // Check the array element in this array element
         for (int i = 0; i < elements.length; i++)
         {
            if (recursiveCheck((Object[]) elements[i], dimension-1) == false)
               return false;
         }
      }
      return true;
   }
}
