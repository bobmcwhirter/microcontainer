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

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.jboss.metatype.api.values.SimpleValue;

/**
 * SimpleMetaType.
 *
 * @param <T> the underlying type
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class SimpleMetaType<T extends Serializable> extends AbstractMetaType<T>
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 6786422588217893696L;

   /** Cached hash code */
   private transient int cachedHashCode;

   /** Cached string representation */
   private transient String cachedToString;

   /** The simple type for java.math.BigDecimal */
   public static final SimpleMetaType<BigDecimal> BIGDECIMAL;

   /** The simple type for java.math.BigInteger */
   public static final SimpleMetaType<BigInteger> BIGINTEGER;

   /** The simple type for java.lang.Boolean */
   public static final SimpleMetaType<Boolean> BOOLEAN;

   /** The simple type for java.lang.Byte */
   public static final SimpleMetaType<Byte> BYTE;

   /** The simple type for java.lang.Character */
   public static final SimpleMetaType<Character> CHARACTER;

   /** The simple type for java.lang.Date */
   public static final SimpleMetaType<Date> DATE;

   /** The simple type for java.lang.Double */
   public static final SimpleMetaType<Double> DOUBLE;

   /** The simple type for java.lang.Float */
   public static final SimpleMetaType<Float> FLOAT;

   /** The simple type for java.lang.Integer */
   public static final SimpleMetaType<Integer> INTEGER;

   /** The simple type for java.lang.Long */
   public static final SimpleMetaType<Long> LONG;

   /** The simple type for java.lang.Short */
   public static final SimpleMetaType<Short> SHORT;

   /** The simple type for java.lang.String */
   public static final SimpleMetaType<String> STRING;

   /** The simple type for an object name */
   public static final SimpleMetaType<Name> NAMEDOBJECT;

   /** The simple type for java.lang.Void */
   public static final SimpleMetaType VOID;

   static
   {
      BIGDECIMAL = new SimpleMetaType<BigDecimal>(BigDecimal.class.getName());
      BIGINTEGER = new SimpleMetaType<BigInteger>(BigInteger.class.getName());
      BOOLEAN = new SimpleMetaType<Boolean>(Boolean.class.getName());
      BYTE = new SimpleMetaType<Byte>(Byte.class.getName());
      CHARACTER = new SimpleMetaType<Character>(Character.class.getName());
      DATE = new SimpleMetaType<Date>(Date.class.getName());
      DOUBLE = new SimpleMetaType<Double>(Double.class.getName());
      FLOAT = new SimpleMetaType<Float>(Float.class.getName());
      INTEGER = new SimpleMetaType<Integer>(Integer.class.getName());
      LONG = new SimpleMetaType<Long>(Long.class.getName());
      SHORT = new SimpleMetaType<Short>(Short.class.getName());
      STRING = new SimpleMetaType<String>(String.class.getName());
      NAMEDOBJECT = new SimpleMetaType<Name>(Name.class.getName());
      VOID = new SimpleMetaType(Void.class.getName());
   }

   /**
    * Resolve a simple type
    *
    * @param className the class name of the simple type
    * @return the simple type
    * @throws IllegalArgumentException for a null className or if it is not a simple type
    */
   public static SimpleMetaType resolve(String className)
   {
      SimpleMetaType result = isSimpleType(className);
      if (result != null)
         return result;
      throw new IllegalArgumentException("Class is not a simple type: " + className);
   }

   /**
    * Return the simple type if the class name is a simple type
    * otherwise null.
    *
    * @param className the class name of the simple type
    * @return the simple type
    * @throws IllegalArgumentException for a null className
    */
   public static SimpleMetaType isSimpleType(String className)
   {
      if (className == null)
         throw new IllegalArgumentException("Null class name");
      if (className.equals(STRING.getClassName()))
         return STRING;
      if (className.equals(INTEGER.getClassName()) || className.equals(Integer.TYPE.getName()))
         return INTEGER;
      if (className.equals(BOOLEAN.getClassName()) || className.equals(Boolean.TYPE.getName()))
         return BOOLEAN;
      if (className.equals(LONG.getClassName()) || className.equals(Long.TYPE.getName()))
         return LONG;
      if (className.equals(BYTE.getClassName()) || className.equals(Byte.TYPE.getName()))
         return BYTE;
      if (className.equals(CHARACTER.getClassName()) || className.equals(Character.TYPE.getName()))
         return CHARACTER;
      if (className.equals(DOUBLE.getClassName()) || className.equals(Double.TYPE.getName()))
         return DOUBLE;
      if (className.equals(FLOAT.getClassName()) || className.equals(Float.TYPE.getName()))
         return FLOAT;
      if (className.equals(SHORT.getClassName()) || className.equals(Short.TYPE.getName()))
         return SHORT;
      if (className.equals(BIGDECIMAL.getClassName()))
         return BIGDECIMAL;
      if (className.equals(BIGINTEGER.getClassName()))
         return BIGINTEGER;
      if (className.equals(VOID.getClassName()))
         return VOID;
      if (className.equals(DATE.getClassName()))
         return DATE;
      if (className.equals(NAMEDOBJECT.getClassName()))
         return NAMEDOBJECT;
      return null;
   }

   /**
    * Construct an SimpleMetaType.<p>
    *
    * This constructor is used to construct the static simple meta types.
    *
    * @param className the name of the class implementing the type
    */
   private SimpleMetaType(String className)
   {
      super(className);
      cachedHashCode = getClassName().hashCode();
      StringBuilder buffer = new StringBuilder(SimpleMetaType.class.getSimpleName());
      buffer.append(":");
      buffer.append(getClassName());
      cachedToString = buffer.toString();
   }

   @Override
   public boolean isSimple()
   {
      return true;
   }

   @Override
   public boolean isValue(Object obj)
   {
      if (obj == null || obj instanceof SimpleValue == false)
         return false;

      SimpleValue value = (SimpleValue) obj;
      return equals(value.getMetaType());
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null || obj instanceof SimpleMetaType == false)
         return false;
      SimpleMetaType other = (SimpleMetaType) obj;
      return getClassName().equals(other.getClassName());
   }

   @Override
   public int hashCode()
   {
      return cachedHashCode;
   }

   @Override
   public String toString()
   {
      return cachedToString;
   }

   /**
    * Resolve to the singletons

    * @return the singletons
    * @throws ObjectStreamException for a corrupted stream
    */
   private Object readResolve() throws ObjectStreamException
   {
      return resolve(getClassName());
   }
}
