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
package org.jboss.test.metatype.values.test;

import junit.framework.Test;

import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.api.values.SimpleValue;
import org.jboss.metatype.api.values.SimpleValueSupport;
import org.jboss.test.metatype.AbstractMetaTypeTest;

/**
 * SimpleValueSupportUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class SimpleValueSupportUnitTestCase extends AbstractMetaTypeTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(SimpleValueSupportUnitTestCase.class);
   }
   
   /**
    * Create a new SimpleValueSupportUnitTestCase.
    * 
    * @param name the test name
    */
   public SimpleValueSupportUnitTestCase(String name)
   {
      super(name);
   }

   /**
    * Test the simple value
    * 
    * @throws Exception for any problem
    */
   public void testSimpleValueSupport() throws Exception
   {
      initStringValue1();
   }

   /**
    * Test the meta type for a simple value
    * 
    * @throws Exception for any problem
    */
   public void testGetSimpleMetaType() throws Exception
   {
      SimpleValue<String> value = initStringValue1();
      assertEquals(SimpleMetaType.STRING, value.getMetaType());
   }

   /**
    * Test the getValue for a simple value
    * 
    * @throws Exception for any problem
    */
   public void testGetValue()throws Exception
   {
      SimpleValue<String> value = initStringValue1();
      assertEquals("value1", value.getValue());
   }

   /**
    * Test the setValue for a simple value
    * 
    * @throws Exception for any problem
    */
   public void testSetValue()throws Exception
   {
      SimpleValueSupport<String> value = (SimpleValueSupport<String>) initStringValue1();
      value.setValue("value2");
      assertEquals("value2", value.getValue());
   }

   /**
    * Test the equals for a simple value
    * 
    * @throws Exception for any problem
    */
   public void testEquals() throws Exception
   {
      SimpleValue<String> v = initStringValue1();

      assertEquals("data should equal itself", v, v);
      assertNotSame("data should not equal null", v, null);
      assertNotSame("data should not equal null value", v, initStringNull());
      assertNotSame("data should not equal empty value", v, initStringEmpty());
      assertNotSame("data should not equal non SimpleValue", v, new Object());

      SimpleValue<?> v2 = initStringValue1();

      assertEquals("data should equal with data2 with different instance of the same simple type", v, v2);
      assertEquals("data should equal with data2 with different instance of the same simple type", v2, v);

      v2 = initInteger2();

      assertNotSame("data should not be equal with data2 with different simple type", v, v2);
      assertNotSame("data2 should not be equal with data with different simple type", v2, v);

      v2 = initStringName1();

      assertNotSame("data should not be equal with data2 with different values", v, v2);
      assertNotSame("data2 should not be equal with data with different value", v2, v);
   }

   /**
    * Test the hashCode for a simple value
    * 
    * @throws Exception for any problem
    */
   public void testHashCode() throws Exception
   {
      SimpleValue<String> v = initStringValue1();

      int myHashCode = "value1".hashCode();
      assertEquals("Wrong hash code generated", myHashCode, v.hashCode());
   }

   /**
    * Test the toString for a simple value
    * 
    * @throws Exception for any problem
    */
   public void testToString() throws Exception
   {
      SimpleValue<String> v = initStringValue1();

      String toString = v.toString();

      assertTrue("toString() should contain the simple type", toString.indexOf(SimpleMetaType.STRING.toString()) != -1);
      assertTrue("toString() should contain value1", toString.indexOf("value1") != -1);
   }

   /**
    * Test the serialization for a simple value
    * 
    * @throws Exception for any problem
    */
   public void testSerialization() throws Exception
   {
      SimpleValue<String> v = initStringValue1();
      byte[] bytes = serialize(v);
      Object result = deserialize(bytes);
      assertEquals(v, result);
   }

   /**
    * Test the errors for a simple value
    * 
    * @throws Exception for any problem
    */
   public void testErrors() throws Exception
   {
      try
      {
         new SimpleValueSupport<String>(null, "value1");
         fail("Excepted IllegalArgumentException for null simple type");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      new SimpleValueSupport<String>(SimpleMetaType.STRING, null);
   }
}
