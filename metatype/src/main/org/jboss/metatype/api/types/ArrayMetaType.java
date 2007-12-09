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

import org.jboss.metatype.api.values.ArrayValue;

/**
 * ArrayMetaType.
 *
 * @param <T> exact type
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ArrayMetaType<T extends Serializable> extends AbstractMetaType
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -2062790692152055156L;

   /** The number of dimensions in the array */
   private int dimension = 0;

   /** The element type for the array */
   private MetaType elementType;

   /** Is elementType a primative array */
   private boolean primitiveArray;

   /** Cached hash code */
   private transient int cachedHashCode = Integer.MIN_VALUE;

   /** Cached string representation */
   private transient String cachedToString = null;

   private static final int PRIMITIVE_WRAPPER_NAME_INDEX = 0;
   private static final int PRIMITIVE_TYPE_NAME_INDEX = 1;
   private static final int PRIMITIVE_TYPE_ENCODING_INDEX  = 2;
   private static final int PRIMITIVE_OPEN_TYPE_INDEX  = 3;

   private static final Object[][] PRIMITIVE_ARRAY_TYPES = {
       { Boolean.class.getName(),   boolean.class.getName(), "Z", SimpleMetaType.BOOLEAN },
       { Character.class.getName(), char.class.getName(),    "C", SimpleMetaType.CHARACTER },
       { Byte.class.getName(),      byte.class.getName(),    "B", SimpleMetaType.BYTE },
       { Short.class.getName(),     short.class.getName(),   "S", SimpleMetaType.SHORT },
       { Integer.class.getName(),   int.class.getName(),     "I", SimpleMetaType.INTEGER },
       { Long.class.getName(),      long.class.getName(),    "J", SimpleMetaType.LONG },
       { Float.class.getName(),     float.class.getName(),   "F", SimpleMetaType.FLOAT },
       { Double.class.getName(),    double.class.getName(),  "D", SimpleMetaType.DOUBLE }
   };

   /**
    * Is primitive key.
    *
    * @param primitiveKey the key to check
    * @return true if key is primitive
    */
   public static boolean isPrimitiveEncoding(final String primitiveKey)
   {
      for (Object[] typeDescr : PRIMITIVE_ARRAY_TYPES)
      {
          if (typeDescr[PRIMITIVE_TYPE_ENCODING_INDEX].equals(primitiveKey))
          {
              return true;
          }
      }
      return false;
   }

   /**
    * Get primitive meta type.
    *
    * @param primitiveTypeName primitive type name
    * @return primitive meta type or null if param is not primitive
    */
   public static SimpleMetaType<?> getPrimitiveMetaType(String primitiveTypeName)
   {
      for (Object[] typeDescr : PRIMITIVE_ARRAY_TYPES)
      {
         if (primitiveTypeName.equals(typeDescr[PRIMITIVE_TYPE_NAME_INDEX]))
              return (SimpleMetaType<?>) typeDescr[PRIMITIVE_OPEN_TYPE_INDEX];
      }
      return null;
   }

   /**
    * Get the char encoding string for the type name.
    *
    * @param typeName - the primitive wrapper type name
    * @return char encoding string.
    */
   public static String getPrimitiveEncoding(String typeName)
   {
      for (Object[] typeDescr : PRIMITIVE_ARRAY_TYPES)
      {
         if (typeName.equals(typeDescr[PRIMITIVE_WRAPPER_NAME_INDEX]))
              return (String) typeDescr[PRIMITIVE_TYPE_ENCODING_INDEX];
      }
      return null;
   }

   /**
    * Get the char encoding string for the type name.
    *
    * @param typeName - the primitive wrapper type name
    * @return primitive type name string.
    */
   public static String getPrimitiveName(String typeName)
   {
      for (Object[] typeDescr : PRIMITIVE_ARRAY_TYPES)
      {
         if (typeName.equals(typeDescr[PRIMITIVE_WRAPPER_NAME_INDEX]))
              return (String) typeDescr[PRIMITIVE_TYPE_NAME_INDEX];
      }
      return null;
   }

   /**
    * Get array meta type.
    *
    * @param elementType the element meta type
    * @return array meta type
    */
   public static <E extends Serializable> ArrayMetaType<E[]> getArrayType(MetaType<E> elementType)
   {
      return new ArrayMetaType<E[]>(1, elementType);
   }

   /**
    * Get primitive array meta type.
    *
    * @param arrayClass array class
    * @return array meta type
    */
   public static <T extends Serializable> ArrayMetaType<T> getPrimitiveArrayType(Class<T> arrayClass)
   {
      if (!arrayClass.isArray())
      {
         throw new IllegalArgumentException("arrayClass must be an array");
      }

      int n = 1;
      Class<?> componentType = arrayClass.getComponentType();
      while (componentType.isArray())
      {
         n++;
         componentType = componentType.getComponentType();
      }
      String componentTypeName = componentType.getName();

      if (!componentType.isPrimitive())
      {
          throw new IllegalArgumentException(
              "component type of the array must be a primitive type");
      }

      SimpleMetaType<?> simpleType = getPrimitiveMetaType(componentTypeName);

      // Build primitive array
      //
       ArrayMetaType at = new ArrayMetaType(simpleType, true);
       if (n > 1)
           at = new ArrayMetaType<T>(n - 1, at);
       return at;
   }

   /**
    * Generate the class name
    * 
    * @param dimension the dimension
    * @param elementType the element type
    * @param isPrimitive is this a primitive type
    * @return the class name
    */
   private static String genName(int dimension, MetaType elementType, boolean isPrimitive)
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
      if (isPrimitive)
      {
         buffer.append(getPrimitiveEncoding(elementType.getClassName()));
      }
      else
      {
         buffer.append('L');
         buffer.append(elementType.getClassName());
         buffer.append(';');
      }
      return buffer.toString();
   }

   /**
    * Generate the type name
    * 
    * @param dimension the dimension
    * @param elementType the element type
    * @param isPrimitive is this a primitive type
    * @return the type name
    */
   private static String genType(int dimension, MetaType elementType, boolean isPrimitive)
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
      if (isPrimitive)
      {
         buffer.append(getPrimitiveEncoding(elementType.getClassName()));
      }
      else
      {
         buffer.append('L');
         buffer.append(elementType.getClassName());
         buffer.append(';');
      }
      return buffer.toString();
   }

   /**
    * Generate the description
    * 
    * @param dimension the dimension
    * @param elementType the element type
    * @param isPrimitive is this a primitive type
    * @return the description
    */
   private static String genDesc(int dimension, MetaType elementType, boolean isPrimitive)
   {
      StringBuilder buffer = new StringBuilder();
      buffer.append(new Integer(dimension));
      buffer.append("-dimension array of ");
      if (isPrimitive)
         buffer.append(getPrimitiveName(elementType.getTypeName()));
      else
         buffer.append(elementType.getTypeName());
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
      super(genName(dimension, elementType, false),
            genType(dimension, elementType, false),
            genDesc(dimension, elementType, false));
      this.dimension = dimension;
      this.elementType = elementType;
      this.primitiveArray = false;
   }

   /**
    * Construct an ArrayMetaType.
    *
    * @param elementType the element type
    * @param primitiveArray is primitive array
    */
   public ArrayMetaType(SimpleMetaType<?> elementType, boolean primitiveArray)
   {
      this(1, elementType, primitiveArray);
   }

   /**
    * Construct an ArrayMetaType.
    *
    * @param dimension the number of dimensions in the array
    * @param elementType the open type of the array elements
    * @param primitiveArray is primitive array
    * @throws IllegalArgumentException for a null argument or non-negative dimension or when meta type is an ArrayMetaType
    */
   public ArrayMetaType(int dimension, MetaType<?> elementType, boolean primitiveArray)
   {
      super(genName(dimension, elementType, primitiveArray),
            genType(dimension, elementType, primitiveArray),
            genDesc(dimension, elementType, primitiveArray));
      this.dimension = dimension;
      this.elementType = elementType;
      this.primitiveArray = primitiveArray;
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

   /**
    * Is primitive array.
    *
    * @return true for primitive array
    */
   public boolean isPrimitiveArray()
   {
      return primitiveArray;
   } 

   @Override
   @SuppressWarnings("unchecked")
   public boolean isValue(Object obj)
   {
      if (obj == null)
         return false;

      Class clazz = obj.getClass();
      if (clazz.isArray() == false && (obj instanceof ArrayValue) == false)
         return false;
      
      if (elementType instanceof SimpleMetaType)
         return recursiveCheck((Object[]) obj, dimension);
      
      if (elementType instanceof TableMetaType || elementType instanceof CompositeMetaType)
      {
         // If this is an ArrayValue check its MetaType
         if (obj instanceof ArrayValue)
         {
            ArrayValue av = (ArrayValue) obj;
            return this.equals(av.getMetaType());
         }
         // Check the element classes
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
      buffer.append(" elementType=");
      buffer.append(elementType);
      cachedToString = buffer.toString();
      return cachedToString;
   }

   /**
    * Recursively check array elements
    * 
    * @param elements the elements
    * @param dimension the dimension
    * @return true if elements match, false otherwise
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
