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
package org.jboss.test.metatype.types.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import junit.framework.Test;

import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.api.values.SimpleValue;
import org.jboss.metatype.api.values.SimpleValueSupport;
import org.jboss.test.metatype.AbstractMetaTypeTest;

/**
 * SimpleMetaTypeUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class SimpleMetaTypeUnitTestCase extends AbstractMetaTypeTest
{
   public static Test suite()
   {
      return suite(SimpleMetaTypeUnitTestCase.class);
   }
   
   public SimpleMetaTypeUnitTestCase(String name)
   {
      super(name);
   }

   SimpleMetaType[] types = new SimpleMetaType[]
   {
      SimpleMetaType.BIGDECIMAL,
      SimpleMetaType.BIGINTEGER,
      SimpleMetaType.BOOLEAN,
      SimpleMetaType.BYTE,
      SimpleMetaType.CHARACTER,
      SimpleMetaType.DATE,
      SimpleMetaType.DOUBLE,
      SimpleMetaType.FLOAT,
      SimpleMetaType.INTEGER,
      SimpleMetaType.LONG,
      SimpleMetaType.SHORT,
      SimpleMetaType.STRING,
      SimpleMetaType.VOID
   };

   Class[] classes = new Class[]
   {
      BigDecimal.class,
      BigInteger.class,
      Boolean.class,
      Byte.class,
      Character.class,
      Date.class,
      Double.class,
      Float.class,
      Integer.class,
      Long.class,
      Short.class,
      String.class,
      Void.class
   };

   @SuppressWarnings("unchecked")
   SimpleValue[] values = new SimpleValue[]
   {
      new SimpleValueSupport<BigDecimal>(SimpleMetaType.BIGDECIMAL, new BigDecimal(1)),
      new SimpleValueSupport<BigInteger>(SimpleMetaType.BIGINTEGER, BigInteger.ONE),
      new SimpleValueSupport<Boolean>(SimpleMetaType.BOOLEAN, new Boolean(false)),
      new SimpleValueSupport<Byte>(SimpleMetaType.BYTE, new Byte(Byte.MAX_VALUE)),
      new SimpleValueSupport<Character>(SimpleMetaType.CHARACTER, new Character('a')),
      new SimpleValueSupport<Date>(SimpleMetaType.DATE, new Date(System.currentTimeMillis())),
      new SimpleValueSupport<Double>(SimpleMetaType.DOUBLE, new Double(1)),
      new SimpleValueSupport<Float>(SimpleMetaType.FLOAT, new Float(1)),
      new SimpleValueSupport<Integer>(SimpleMetaType.INTEGER, new Integer(1)),
      new SimpleValueSupport<Long>(SimpleMetaType.LONG, new Long(1)),
      new SimpleValueSupport<Short>(SimpleMetaType.SHORT, new Short(Short.MAX_VALUE)),
      new SimpleValueSupport<String>(SimpleMetaType.STRING, new String("hello")),
      new SimpleValueSupport(SimpleMetaType.VOID, null)
   };

   @SuppressWarnings("unchecked")
   SimpleValue[] nullValues = new SimpleValue[]
   {
      new SimpleValueSupport<BigDecimal>(SimpleMetaType.BIGDECIMAL, null),
      new SimpleValueSupport<BigInteger>(SimpleMetaType.BIGINTEGER, null),
      new SimpleValueSupport<Boolean>(SimpleMetaType.BOOLEAN, null),
      new SimpleValueSupport<Byte>(SimpleMetaType.BYTE, null),
      new SimpleValueSupport<Character>(SimpleMetaType.CHARACTER, null),
      new SimpleValueSupport<Date>(SimpleMetaType.DATE, null),
      new SimpleValueSupport<Double>(SimpleMetaType.DOUBLE, null),
      new SimpleValueSupport<Float>(SimpleMetaType.FLOAT, null),
      new SimpleValueSupport<Integer>(SimpleMetaType.INTEGER, null),
      new SimpleValueSupport<Long>(SimpleMetaType.LONG, null),
      new SimpleValueSupport<Short>(SimpleMetaType.SHORT, null),
      new SimpleValueSupport<String>(SimpleMetaType.STRING, null),
      new SimpleValueSupport(SimpleMetaType.VOID, null)
   };

   public void testSimpleTypes() throws Exception
   {
      for (int i = 0; i < types.length; i++)
      {
         String className = classes[i].getName();
         getLog().debug("SimpleMetaType: " + className + " className=" + types[i].getClassName() + " typeName=" + types[i].getTypeName() + " description=" + types[i].getDescription());
         assertEquals(className, types[i].getClassName());
         assertEquals(className, types[i].getTypeName());
         assertEquals(className, types[i].getDescription());
      }
   }

   public void testEquals() throws Exception
   {
      for (int i = 0; i < types.length; i++)
      {
        for (int j = 0; j < types.length; j++)
        {
           boolean resultEquals = types[i].equals(types[j]);
           boolean resultReference = types[i] != types[j];
           getLog().debug("equals  : " + types[i].getClassName() + " " + types[j] + " result=" + resultEquals);
           getLog().debug("equality: " + types[i].getClassName() + " " + types[j] + " result=" + resultReference);
           if (i == j)
              assertEquals("SimpleMetaTypes should be equal to itself " + classes[i], types[i], types[j]);
           else
           {
              assertNotSame("SimpleMetaTypes should be different under equality " + classes[i], types[i], types[j]);
              assertTrue("SimpleMetaTypes should be different under reference " + classes[i], types[i] != types[j]);
           }
        }
      }
   }

   public void testIsValue() throws Exception
   {
      for (int i = 0; i < types.length; ++i)
      {
         for (int j = 0; j < types.length; ++j)
         {

            // isValue makes no sense for Void
            if (values[i].getValue() == null)
               continue;

            boolean result = types[j].isValue(values[i]);
            getLog().debug("isValue: " + types[j].getClassName() + " value=" + values[i] + " result=" + result);
            
            if (i == j)
            {
               assertTrue(classes[i] + " should be a simple value of " + types[j], result);
               result = types[i].isValue(nullValues[j]);
               getLog().debug("isValue: " + types[i].getClassName() + " value=null value result=" + result);
               assertTrue(nullValues[j] + " should be a simple value of " + types[i], result);
            }
            else
            {
               assertFalse(classes[i] + " should NOT be a simple value of " + types[j], result);
               result = types[i].isValue(nullValues[j]);
               getLog().debug("isValue: " + types[i].getClassName() + " value=null value result=" + result);
               assertFalse(nullValues[j] + " should NOT be a simple value of " + types[i], result);
            }
         }
      }
   }

   public void testHashCode() throws Exception
   {
      for (int i = 0; i < types.length; i++)
      {
         int classHashCode = classes[i].getName().hashCode();
         int typeHashCode = types[i].hashCode();
         getLog().debug("hashCode: " + types[i].getClassName() + " expected=" + classHashCode + " actual=" + typeHashCode);
         assertEquals(classHashCode, typeHashCode);
      }
   }

   public void testToString() throws Exception
   {
      String smt = SimpleMetaType.class.getSimpleName();
      for (int i = 0; i < types.length; i++)
      {
         String className = classes[i].getName();
         String toString = types[i].toString();
         getLog().debug("toString: " + types[i].getClassName() + " value=" + toString);
         assertTrue("SimpleMetaType " + className + " should contain " + smt, toString.indexOf(smt) != -1);
         assertTrue("SimpleMetaType " + className + " should contain " + className, toString.indexOf(className) != -1);
      }
   }

   public void testSerialization() throws Exception
   {
      for (int i = 0; i < types.length; i++)
      {
         getLog().debug("serialization: " + types[i].getClassName() + " original=" + types[i]);
         byte[] bytes = serialize(types[i]);
         SimpleMetaType result = (SimpleMetaType) deserialize(bytes);
         getLog().debug("serialization: " + types[i].getClassName() + " result  =" + types[i]);

         assertTrue("Should resolve to same object after serialization " + types[i], types[i] == result);
      }
   }
}
