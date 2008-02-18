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
package org.jboss.test.metatype.values.factory.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.SimpleValue;
import org.jboss.metatype.api.values.SimpleValueSupport;

import junit.framework.Test;

/**
 * SimpleValueFactoryUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class SimpleValueFactoryUnitTestCase extends AbstractMetaValueFactoryTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(SimpleValueFactoryUnitTestCase.class);
   }
   
   /**
    * Create a new SimpleValueFactoryUnitTestCase.
    * 
    * @param name the test name
    */
   public SimpleValueFactoryUnitTestCase(String name)
   {
      super(name);
   }
   
   BigDecimal BIG_DECIMAL = new BigDecimal(1);
   BigInteger BIG_INTEGER = BigInteger.ONE;
   Boolean BOOLEAN = new Boolean(false);
   Byte BYTE = Byte.MAX_VALUE;
   Character CHARACTER = new Character('a');
   Date DATE = new Date(System.currentTimeMillis());
   Double DOUBLE = new Double(1);
   Float FLOAT = new Float(1);
   Integer INTEGER = new Integer(1);
   Long LONG = new Long(1);
   Short SHORT = new Short(Short.MAX_VALUE);
   String STRING = new String("hello");

   Object[] values = new Object[]
   {
      BIG_DECIMAL,
      BIG_INTEGER,
      BOOLEAN,
      BYTE,
      CHARACTER,
      DATE,
      DOUBLE,
      FLOAT,
      INTEGER,
      LONG,
      SHORT,
      STRING,
   };

   @SuppressWarnings("unchecked")
   SimpleValue[] metaValues = new SimpleValue[]
   {
      new SimpleValueSupport<BigDecimal>(SimpleMetaType.BIGDECIMAL, BIG_DECIMAL),
      new SimpleValueSupport<BigInteger>(SimpleMetaType.BIGINTEGER, BIG_INTEGER),
      new SimpleValueSupport<Boolean>(SimpleMetaType.BOOLEAN, BOOLEAN),
      new SimpleValueSupport<Byte>(SimpleMetaType.BYTE, BYTE),
      new SimpleValueSupport<Character>(SimpleMetaType.CHARACTER, CHARACTER),
      new SimpleValueSupport<Date>(SimpleMetaType.DATE, DATE),
      new SimpleValueSupport<Double>(SimpleMetaType.DOUBLE, DOUBLE),
      new SimpleValueSupport<Float>(SimpleMetaType.FLOAT, FLOAT),
      new SimpleValueSupport<Integer>(SimpleMetaType.INTEGER, INTEGER),
      new SimpleValueSupport<Long>(SimpleMetaType.LONG, LONG),
      new SimpleValueSupport<Short>(SimpleMetaType.SHORT, SHORT),
      new SimpleValueSupport<String>(SimpleMetaType.STRING, STRING),
   };

   /**
    * Test the generated simple values
    * 
    * @throws Exception for any problem
    */
   public void testSimpleValues() throws Exception
   {
      for (int i = 0; i < values.length; i++)
      {
         Object value = values[i];
         MetaValue result = createMetaValue(value);
         SimpleValue<?> actual = assertInstanceOf(result, SimpleValue.class);
         SimpleValue<?> expected = metaValues[i]; 
         getLog().debug("Simple Value: expected=" + expected + " actual=" + actual);
         assertEquals(expected, actual);
      }
   }
}
