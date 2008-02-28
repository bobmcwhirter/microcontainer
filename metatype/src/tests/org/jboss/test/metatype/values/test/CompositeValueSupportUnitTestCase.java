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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;

import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.values.CompositeValue;
import org.jboss.metatype.api.values.CompositeValueSupport;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.SimpleValue;
import org.jboss.test.metatype.AbstractMetaTypeTest;

/**
 * CompositeValueSupportUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class CompositeValueSupportUnitTestCase extends AbstractMetaTypeTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(CompositeValueSupportUnitTestCase.class);
   }
   
   /**
    * Create a new CompositeValueSupportUnitTestCase.
    * 
    * @param name the test name
    */
   public CompositeValueSupportUnitTestCase(String name)
   {
      super(name);
   }

   /**
    * Test the composite value 
    * 
    * @throws Exception for any problem
    */
   public void testCompositeValueSupport() throws Exception
   {
      CompositeMetaType compositeMetaType = initCompositeMetaType();
      Map<String, MetaValue> map = initMapValues();
      String[] keys = initKeys();
      MetaValue[] values = initValues();
      new CompositeValueSupport(compositeMetaType, map);
      new CompositeValueSupport(compositeMetaType, keys, values);
   }

   /**
    * Test the meta type for a composite value 
    * 
    * @throws Exception for any problem
    */
   public void testGetCompositeMetaType() throws Exception
   {
      CompositeMetaType compositeMetaType = initCompositeMetaType();
      Map<String, MetaValue> map = initMapValues();
      CompositeValue v = new CompositeValueSupport(compositeMetaType, map);
      assertEquals(compositeMetaType, v.getMetaType());
   }

   /**
    * Test the get for a composite value 
    * 
    * @throws Exception for any problem
    */
   public void testGet()throws Exception
   {
      CompositeMetaType compositeMetaType = initCompositeMetaType();
      Map<String, MetaValue> map = initMapValues();
      CompositeValue v = new CompositeValueSupport(compositeMetaType, map);

      assertEquals(initStringValue1(), v.get("name1"));
      assertEquals(initInteger2(), v.get("name2"));
   }

   /**
    * Test the getAll for a composite value 
    * 
    * @throws Exception for any problem
    */
   public void testGetAll() throws Exception
   {
      SimpleValue value1 = initStringValue1();
      SimpleValue integer2 = initInteger2();
      CompositeMetaType compositeMetaType = initCompositeMetaType();
      Map<String, MetaValue> map = initMapValues();
      CompositeValue v = new CompositeValueSupport(compositeMetaType, map);

      MetaValue[] result = v.getAll(new String[] { "name1", "name2" });
      assertEquals(value1, result[0]);
      assertEquals(integer2, result[1]);
      result = v.getAll(new String[] { "name2", "name1" });
      assertEquals(value1, result[1]);
      assertEquals(integer2, result[0]);
      result = v.getAll(new String[] { "name1" });
      assertEquals(value1, result[0]);
      result = v.getAll(new String[] { "name2" });
      assertEquals(integer2, result[0]);
   }

   /**
    * Test the containsKey for a composite value 
    * 
    * @throws Exception for any problem
    */
   public void testContainsKey() throws Exception
   {
      CompositeMetaType compositeMetaType = initCompositeMetaType();
      Map<String, MetaValue> map = initMapValues();
      CompositeValue v = new CompositeValueSupport(compositeMetaType, map);
      assertTrue("data should contain key name1", v.containsKey("name1"));
      assertTrue("data should contain key name2", v.containsKey("name2"));
      assertFalse("data should not contain key nameX", v.containsKey("nameX"));
      assertFalse("data should not contain key null", v.containsKey(null));
      assertFalse("data should not contain key <empty>", v.containsKey(""));
   }

   /**
    * Test the containsValue for a composite value 
    * 
    * @throws Exception for any problem
    */
   public void testContainsValue() throws Exception
   {
      SimpleValue value1 = initStringValue1();
      SimpleValue integer2 = initInteger2();
      SimpleValue name1 = initStringName1();
      SimpleValue nullString = initStringNull();
      SimpleValue emptyString = initStringEmpty();
      SimpleValue nullInteger = initIntegerNull();
      CompositeMetaType compositeMetaType = initCompositeMetaType();
      Map<String, MetaValue> map = initMapValues();
      CompositeValue v = new CompositeValueSupport(compositeMetaType, map);

      assertTrue("data should contain value value1", v.containsValue(value1));
      assertTrue("data should contain value 2", v.containsValue(integer2));
      assertFalse("data should not contain value name1", v.containsValue(name1));
      assertFalse("data should not contain key null", v.containsValue(null));
      assertFalse("data should not contain key null", v.containsValue(nullString));
      assertFalse("data should not contain key <empty>", v.containsValue(emptyString));

      map.clear();
      map.put("name1", value1);
      map.put("name2", null);
      v = new CompositeValueSupport(compositeMetaType, map);
      assertTrue("data should contain value null", v.containsValue(null));
      assertFalse("data should not contain key null", v.containsValue(nullString));

      map.clear();
      map.put("name1", value1);
      map.put("name2", nullInteger);
      v = new CompositeValueSupport(compositeMetaType, map);
      assertTrue("data should contain value null", v.containsValue(nullInteger));
      assertFalse("data should not contain key null", v.containsValue(null));
   }

   /**
    * Test the values for a composite value 
    * 
    * @throws Exception for any problem
    */
   public void testValues() throws Exception
   {
      SimpleValue value1 = initStringValue1();
      SimpleValue integer2 = initInteger2();
      SimpleValue name1 = initStringName1();
      SimpleValue nullString = initStringNull();
      SimpleValue emptyString = initStringEmpty();
      SimpleValue nullInteger = initIntegerNull();

      CompositeMetaType compositeMetaType = initCompositeMetaType();
      Map<String, MetaValue> map = initMapValues();
      CompositeValue v = new CompositeValueSupport(compositeMetaType, map);
      Collection<?> values = v.values();
      assertTrue("data values contain 2 elements", values.size() == 2);
      assertTrue("data values should have value1", values.contains(value1));
      assertTrue("data values should have 2", values.contains(integer2));
      assertFalse("data values should not have name1", values.contains(name1));
      assertFalse("data values should not have null", values.contains(null));
      assertFalse("data values should not have null", values.contains(nullString));
      assertFalse("data values should not have <empty>", values.contains(emptyString));

      map.clear();
      map.put("name1", value1);
      map.put("name2", null);
      v = new CompositeValueSupport(compositeMetaType, map);
      values = v.values();
      assertTrue("data values should contain value null", values.contains(null));
      assertFalse("data values should not have null", values.contains(nullString));

      map.clear();
      map.put("name1", value1);
      map.put("name2", nullInteger);
      v = new CompositeValueSupport(compositeMetaType, map);
      values = v.values();
      assertTrue("data values should contain value null", values.contains(nullInteger));
      assertFalse("data values should not have null", values.contains(null));
   }

   /**
    * Test the equals for a composite value 
    * 
    * @throws Exception for any problem
    */
   public void testEquals() throws Exception
   {
      CompositeMetaType compositeMetaType = initCompositeMetaType();
      Map<String, MetaValue> map = initMapValues();
      CompositeValue v = new CompositeValueSupport(compositeMetaType, map);

      assertEquals("data should equal itself", v, v);
      assertNotSame("data should not equal null", v, null);
      assertNotSame("data should not equal non CompositeData", v, new Object());

      CompositeValue v2 = new CompositeValueSupport(compositeMetaType, map);

      assertEquals("data should equal with data2 with different instance of the same composite type", v, v2);
      assertEquals("data should equal with data2 with different instance of the same composite type", v2, v);

      CompositeMetaType compositeMetaType2 = initCompositeMetaType2();
      v2 = new CompositeValueSupport(compositeMetaType2, map);

      assertNotSame("data should not be equal with data2 with different composite type", v, v2);
      assertNotSame("data2 should not be equal with data with different composite type", v2, v);

      Map<String, MetaValue> map2 = initMapValues2();
      v2 = new CompositeValueSupport(compositeMetaType, map2);

      assertNotSame("data should not be equal with data2 with different values", v, v2);
      assertNotSame("data2 should not be equal with data with different value", v2, v);
   }

   /**
    * Test the hashCode for a composite value 
    * 
    * @throws Exception for any problem
    */
   public void testHashCode() throws Exception
   {
      CompositeMetaType compositeMetaType = initCompositeMetaType();
      Map<String, MetaValue> map = initMapValues();
      CompositeValue v = new CompositeValueSupport(compositeMetaType, map);

      int myHashCode = compositeMetaType.hashCode() + "value1".hashCode() + new Integer(2).hashCode();
      assertEquals("Wrong hash code generated", myHashCode, v.hashCode());
   }

   /**
    * Test the toString for a composite value 
    * 
    * @throws Exception for any problem
    */
   public void testToString() throws Exception
   {
      CompositeMetaType compositeMetaType = initCompositeMetaType();
      Map<String, MetaValue> map = initMapValues();
      CompositeValue v = new CompositeValueSupport(compositeMetaType, map);

      String toString = v.toString();

      assertTrue("toString() should contain the composite type", toString.indexOf(compositeMetaType.toString()) != -1);
      assertTrue("toString() should contain name1", toString.indexOf("name1") != -1);
      assertTrue("toString() should contain value1", toString.indexOf("value1") != -1);
      assertTrue("toString() should contain name2=", toString.indexOf("name2") != -1);
      assertTrue(toString + " should contain " + new Integer(2), toString.indexOf(new Integer(2).toString()) != -1);
   }

   /**
    * Test the serialization for a composite value 
    * 
    * @throws Exception for any problem
    */
   public void testSerialization() throws Exception
   {
      CompositeMetaType compositeMetaType = initCompositeMetaType();
      Map<String, MetaValue> map = initMapValues();
      CompositeValue v = new CompositeValueSupport(compositeMetaType, map);
      byte[] bytes = serialize(v);
      Object result = deserialize(bytes);
      assertEquals(v, result);
   }

   /**
    * Test the errors for a composite value 
    * 
    * @throws Exception for any problem
    */
   public void testErrorsArray() throws Exception
   {
      CompositeMetaType compositeMetaType = initCompositeMetaType();
      String[] itemNames = initKeys();
      MetaValue[] itemValues = initValues();

      try
      {
         new CompositeValueSupport(null, itemNames, itemValues);
         fail("Excepted IllegalArgumentException for null composite type");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         new CompositeValueSupport(compositeMetaType, null, itemValues);
         fail("Excepted IllegalArgumentException for null item names");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         new CompositeValueSupport(compositeMetaType, new String[0], itemValues);
         fail("Excepted IllegalArgumentException for empty item names");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         new CompositeValueSupport(compositeMetaType, itemNames, null);
         fail("Excepted IllegalArgumentException for null item values");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         new CompositeValueSupport(compositeMetaType, itemNames, new MetaValue[0]);
         fail("Excepted IllegalArgumentException for empty item values");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         new CompositeValueSupport(compositeMetaType, new String[] { "name1", null }, itemValues);
         fail("Excepted IllegalArgumentException for a null item name");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         new CompositeValueSupport(compositeMetaType, new String[] { "name1", "" }, itemValues);
         fail("Excepted IllegalArgumentException for an empty item name");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         new CompositeValueSupport(compositeMetaType, itemNames, new MetaValue[] { initStringWrong() });
         fail("Excepted IllegalArgumentException for mismatch in number of itemNames/itemValues");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         new CompositeValueSupport(compositeMetaType, new String[] { "name1", "wrongName" }, itemValues);
         fail("Excepted IllegalArgumentException for an item name not in the composite type");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         new CompositeValueSupport(compositeMetaType, itemNames, new MetaValue[] { initStringValue1(), initStringWrong() });
         fail("Excepted IllegalArgumentException for an item value of the wrong type");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      new CompositeValueSupport(compositeMetaType, itemNames, new MetaValue[] { initStringValue1(), null });
      new CompositeValueSupport(compositeMetaType, itemNames, new MetaValue[] { initStringValue1(), initIntegerNull() });
   }

   /**
    * Test the errors for a composite value 
    * 
    * @throws Exception for any problem
    */
   public void testErrorsMap() throws Exception
   {
      CompositeMetaType compositeMetaType = initCompositeMetaType();
      Map<String, MetaValue> map = initMapValues();

      try
      {
         new CompositeValueSupport(null, map);
         fail("Excepted IllegalArgumentException for null composite type");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         HashMap<String, MetaValue> map2 = new HashMap<String, MetaValue>();
         map2.put("name1", initStringValue1());
         map2.put(null, initInteger2());
         new CompositeValueSupport(compositeMetaType, map2);
         fail("Excepted IllegalArgumentException for a null key in map");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         HashMap<String, MetaValue> map2 = new HashMap<String, MetaValue>();
         map2.put("name1", initStringValue1());
         map2.put("", initInteger2());
         new CompositeValueSupport(compositeMetaType, map2);
         fail("Excepted IllegalArgumentException for an empty key in map");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         HashMap<String, MetaValue> map2 = new HashMap<String, MetaValue>();
         map2.put("name1", initStringValue1());
         map2.put("wrongName", initInteger2());
         new CompositeValueSupport(compositeMetaType, map2);
         fail("Excepted IllegalArgumentException for an item name not in the composite type");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         HashMap<String, MetaValue> map2 = new HashMap<String, MetaValue>();
         map2.put("name1", initStringValue1());
         map2.put("name2", initStringWrong());
         new CompositeValueSupport(compositeMetaType, map2);
         fail("Excepted IllegalArgumentException for an item value of the wrong type");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      HashMap<String, MetaValue> map2 = new HashMap<String, MetaValue>();
      map2.put("name1", initStringValue1());
      map2.put("name2", null);
      new CompositeValueSupport(compositeMetaType, map2);

      map2 = new HashMap<String, MetaValue>();
      map2.put("name1", initStringValue1());
      map2.put("name2", initIntegerNull());
      new CompositeValueSupport(compositeMetaType, map2);
   }

   /**
    * Test the errors for a composite value 
    * 
    * @throws Exception for any problem
    */
   public void testErrors() throws Exception
   {
      CompositeMetaType compositeMetaType = initCompositeMetaType();
      Map<String, MetaValue> map = initMapValues();
      CompositeValue data = new CompositeValueSupport(compositeMetaType, map);

      try
      {
         data.get(null);
         fail("Excepted IllegalArgumentException for get and a null key");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         data.get("");
         fail("Excepted IllegalArgumentException for get and an empty key");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         data.get("wrong");
         fail("Excepted IllegalArgumentException for get and a wrong key");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         data.getAll(new String[] { "name1", null });
         fail("Excepted IllegalArgumentException for getAll and a null key");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         data.getAll(new String[] { "name1", "" });
         fail("Excepted IllegalArgumentException for getAll and an empty key");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         data.getAll(new String[] { "name1", "wrong" });
         fail("Excepted IllegalArgumentException for getAll and an invalid key");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }
}
