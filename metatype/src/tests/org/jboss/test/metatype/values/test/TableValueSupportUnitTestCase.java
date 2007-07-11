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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;

import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.types.ImmutableTableMetaType;
import org.jboss.metatype.api.types.TableMetaType;
import org.jboss.metatype.api.values.CompositeValue;
import org.jboss.metatype.api.values.CompositeValueSupport;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.TableValue;
import org.jboss.metatype.api.values.TableValueSupport;
import org.jboss.test.metatype.AbstractMetaTypeTest;

/**
 * TableValueSupportUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class TableValueSupportUnitTestCase extends AbstractMetaTypeTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(TableValueSupportUnitTestCase.class);
   }
   
   /**
    * Create a new TableValueSupportUnitTestCase.
    * 
    * @param name the test name
    */
   public TableValueSupportUnitTestCase(String name)
   {
      super(name);
   }

   /**
    * Test the table value support
    * 
    * @throws Exception for any problem
    */
   public void testTableValueSupport() throws Exception
   {
      TableMetaType tableType = initTableType();

      new TableValueSupport(tableType);
      new TableValueSupport(tableType, 100, .5f);
   }
   
   /**
    * Test the table type for a table value
    * 
    * @throws Exception for any problem
    */
   public void testGetTableType() throws Exception
   {
      TableMetaType tableType = initTableType();

      TableValueSupport data = new TableValueSupport(tableType);
      assertTrue("Expected the same table type", data.getMetaType().equals(tableType));
   }
   
   /**
    * Test the calculate index for a table value
    * 
    * @throws Exception for any problem
    */
   public void testCalculateIndex() throws Exception
   {
      TableMetaType tableType = initTableType();

      TableValueSupport data = new TableValueSupport(tableType);

      CompositeValue compData = initCompositeValue(data);
      Object[] index = data.calculateIndex(compData);

      assertEquals("Expected index element 0 to be value1", index[0], initStringValue1());
      assertEquals("Expected index element 1 to be 2", index[1], initInteger2());

      compData = initCompositeValue3(data);
      index = data.calculateIndex(compData);

      assertEquals("Expected index element 0 to be value2", index[0], initStringValue2());
      assertEquals("Expected index element 1 to be 3", index[1], initInteger3());
   }

   /**
    * Test the contains key for a table value
    * 
    * @throws Exception for any problem
    */
   public void testContainsKey() throws Exception
   {
      TableMetaType tableType = initTableType();

      TableValueSupport data = new TableValueSupport(tableType);

      assertFalse("Didn't expect containsKey null", data.containsKey(null));

      MetaValue[] index = new MetaValue[] { initStringValue1(), initInteger2() };
      assertFalse("Didn't expect containsKey on empty data", data.containsKey(index));

      CompositeValue compData = initCompositeValue2(data);
      assertFalse("Didn't expect containsKey on index not present", data.containsKey(index));

      compData = initCompositeValue(data);
      data.put(compData);
      assertTrue("Expected containsKey", data.containsKey(index));

      compData = initCompositeValue2(data);
      assertFalse("Didn't expect containsKey on index still not present", data.containsKey(data.calculateIndex(compData)));

      data.remove(index);
      assertFalse("Didn't expect removed data in containsKey", data.containsKey(index));
   }

   /**
    * Test the contains value for a table value
    * 
    * @throws Exception for any problem
    */
   public void testContainsValue() throws Exception
   {
      TableMetaType tableType = initTableType();

      TableValueSupport data = new TableValueSupport(tableType);

      assertFalse("Didn't expect containsValue null", data.containsValue(null));

      CompositeMetaType rowType2 = initCompositeMetaType2();

      CompositeValue compData2 = initCompositeValue(rowType2);

      assertFalse("Didn't expect containsValue wrong composite type", data.containsValue(compData2));

      CompositeValue compData = initCompositeValue(data);
      assertFalse("Didn't expect containsValue on data not present", data.containsValue(compData));

      data.put(compData);
      assertTrue("Expected containsValue", data.containsValue(compData));

      compData = initCompositeValue2(data);
      assertFalse("Didn't expect containsValue on value still not present", data.containsValue(compData));

      assertFalse("Didn't expect containsValue still wrong composite type", data.containsValue(compData2));

      data.remove(data.calculateIndex(compData));
      assertFalse("Didn't expect removed data in containsValue", data.containsValue(compData));
   }

   /**
    * Test the get for a table value
    * 
    * @throws Exception for any problem
    */
   public void testGet() throws Exception
   {
      TableMetaType tableType = initTableType();

      TableValueSupport data = new TableValueSupport(tableType);

      MetaValue[] index = initValues2();
      assertNull("Expected null for get on data not present", data.get(index));

      CompositeValue compData = initCompositeValue(data);
      index = initValues();
      data.put(compData);
      assertEquals("Expected get to return the same value", compData, data.get(index));

      index = initValues2();
      assertNull("Didn't expect get on value still not present", data.get(index));

      index = initValues();
      data.remove(index);
      assertNull("Didn't expect removed data in get", data.get(index));
   }

   /**
    * Test the put for a table value
    * 
    * @throws Exception for any problem
    */
   public void testPut() throws Exception
   {
      TableMetaType tableType = initTableType();

      TableValueSupport data = new TableValueSupport(tableType);

      CompositeValue compData = initCompositeValue(data);
      MetaValue[] index = initValues();
      data.put(compData);
      assertEquals("The data should be present after put", compData, data.get(index));

      CompositeValue compData2 = initCompositeValue2(data);
      index = initValues2();
      data.put(compData2);
      assertEquals("Another data should be present after put", compData2, data.get(index));

      index = initValues();
      assertEquals("The previous data should be present after put", compData, data.get(index));

      data.remove(index);
      data.put(compData);
      assertEquals("Data should be present after remove/put", compData, data.get(index));
   }

   /**
    * Test the remove for a table value
    * 
    * @throws Exception for any problem
    */
   public void testRemove() throws Exception
   {
      TableMetaType tableType = initTableType();

      TableValueSupport data = new TableValueSupport(tableType);

      CompositeValue compData = initCompositeValue(data);
      MetaValue[] index = initValues();

      assertNull("Remove on data not present returns null", data.remove(index));

      data.put(compData);
      assertEquals("Remove on data present returns the data", compData, data.remove(index));
   }

   /**
    * Test the put all for a table value
    * 
    * @throws Exception for any problem
    */
   public void testPutAll() throws Exception
   {
      TableMetaType tableType = initTableType();

      TableValueSupport data = new TableValueSupport(tableType);

      data.putAll((CompositeValue[]) null);
      assertTrue("Put all null is ok", data.isEmpty());

      CompositeValue compData = initCompositeValue(data);

      CompositeValue[] toPut = new CompositeValue[] { compData };
      data.putAll(toPut);
      assertEquals("Put all added one", 1, data.size());
      assertTrue("Put all added the correct data", data.containsValue(compData));

      CompositeValue compData2 = initCompositeValue2(data);
      CompositeValue compData3 = initCompositeValue4(data);

      toPut = new CompositeValue[] { compData2, compData3 };
      data.putAll(toPut);
      assertEquals("Put all added two", 3, data.size());
      assertTrue("Put all added the correct data", data.containsValue(compData2));
      assertTrue("Put all added the correct data", data.containsValue(compData3));
      assertTrue("Put all original data still present", data.containsValue(compData));
   }

   /**
    * Test the clear for a table value
    * 
    * @throws Exception for any problem
    */
   public void testClear() throws Exception
   {
      TableMetaType tableType = initTableType();

      TableValueSupport data = new TableValueSupport(tableType);

      data.putAll((CompositeValue[]) null);
      assertTrue("Put all null is ok", data.isEmpty());

      CompositeValue compData = initCompositeValue(data);
      CompositeValue compData2 = initCompositeValue2(data);
      CompositeValue compData3 = initCompositeValue4(data);

      data.putAll(new CompositeValue[] { compData, compData2, compData3 });

      data.clear();
      assertTrue("Clear should clear the data", data.isEmpty());
   }

   /**
    * Test the size for a table value
    * 
    * @throws Exception for any problem
    */
   public void testSize() throws Exception
   {
      TableMetaType tableType = initTableType();

      TableValueSupport data = new TableValueSupport(tableType);

      assertEquals("Initial size is zero", 0, data.size());

      CompositeValue compData = initCompositeValue(data);

      data.putAll(new CompositeValue[] { compData });
      assertEquals("Expected one element", 1, data.size());

      CompositeValue compData2 = initCompositeValue2(data);
      CompositeValue compData3 = initCompositeValue4(data);

      data.putAll(new CompositeValue[] { compData2, compData3 });
      assertEquals("Expected three elements", 3, data.size());

      data.remove(initValues4());
      assertEquals("Expected two elements", 2, data.size());

      data.clear();
      assertEquals("Expected no elements", 0, data.size());
   }

   /**
    * Test the isEmpty for a table value
    * 
    * @throws Exception for any problem
    */
   public void testIsEmpty() throws Exception
   {
      TableMetaType tableType = initTableType();

      TableValueSupport data = new TableValueSupport(tableType);

      assertTrue("Initially empty", data.isEmpty());

      CompositeValue compData = initCompositeValue(data);

      data.putAll(new CompositeValue[] { compData });
      assertFalse("Not empty after a put", data.isEmpty());

      data.clear();
      assertTrue("Expected no elements", data.isEmpty());
   }

   /**
    * Test the keySet for a table value
    * 
    * @throws Exception for any problem
    */
   public void testKeySet() throws Exception
   {
      TableMetaType tableType = initTableType();

      TableValueSupport data = new TableValueSupport(tableType);

      CompositeValue compData = initCompositeValue(data);
      CompositeValue compData2 = initCompositeValue2(data);
      CompositeValue compData3 = initCompositeValue4(data);

      data.putAll(new CompositeValue[] { compData, compData2, compData3 });

      Set<List<MetaValue>> keySet = data.keySet();
      assertEquals("Key set should contain 3 elements", 3, keySet.size());
      assertTrue("Key set should contain index [value1, 2]", keySet.contains(Arrays.asList(initValues())));
      assertTrue("Key set should contain index [value1, 3]", keySet.contains(Arrays.asList(initValues2())));
      assertTrue("Key set should contain index [value1, 4]", keySet.contains(Arrays.asList(initValues4())));
   }

   /**
    * Test the values for a table value
    * 
    * @throws Exception for any problem
    */
   public void testValues() throws Exception
   {
      TableMetaType tableType = initTableType();

      TableValueSupport data = new TableValueSupport(tableType);

      CompositeValue compData = initCompositeValue(data);
      CompositeValue compData2 = initCompositeValue2(data);
      CompositeValue compData3 = initCompositeValue4(data);

      data.putAll(new CompositeValue[] { compData, compData2, compData3 });

      Collection<CompositeValue> values = data.values();
      assertEquals("Values should contain 3 elements", 3, values.size());
      assertTrue("Values should contain index compData", values.contains(compData));
      assertTrue("Values should contain index compData2", values.contains(compData2));
      assertTrue("Values should contain index compData3", values.contains(compData3));
   }

   /**
    * Test the clone for a table value
    * 
    * @throws Exception for any problem
    */
   public void testClone() throws Exception
   {
      TableMetaType tableType = initTableType();

      TableValueSupport data = new TableValueSupport(tableType);

      CompositeValue compData = initCompositeValue(data);
      CompositeValue compData2 = initCompositeValue2(data);
      CompositeValue compData3 = initCompositeValue4(data);

      data.putAll(new CompositeValue[] { compData, compData2, compData3 });

      TableValue clone = data.clone();
      assertEquals("Clone should have the same tabular type", data.getMetaType(), clone.getMetaType());
      assertEquals("Clone should have the same number of elements", data.size(), clone.size());
      CompositeValue compDataClone = clone.get(initValues());
      assertTrue("Should be a shallow clone", compData == compDataClone);
   }

   /**
    * Test the equals for a table value
    * 
    * @throws Exception for any problem
    */
   public void testEquals() throws Exception
   {
      TableMetaType tableType = initTableType();

      TableValueSupport data = new TableValueSupport(tableType);

      assertNotSame("Null should not be equal", data, null);
      assertNotSame("Only TableValues should be equal", data, new Object());

      assertEquals("An instance should equal itself", data, data);

      TableValueSupport data2 = new TableValueSupport(tableType);

      assertEquals("Two different instances with the same table type are equal", data, data2);
      assertEquals("Two different instances with the same table type are equal", data2, data);

      TableMetaType tableType2 = initTableType2();
      data2 = new TableValueSupport(tableType2);

      assertNotSame("Instances with different table type are not equal", data, data2);
      assertNotSame("Instances with different table type are not equal", data2, data);

      CompositeValue compData = initCompositeValue(data);
      CompositeValue compData2 = initCompositeValue2(data);
      CompositeValue compData3 = initCompositeValue4(data);

      data.putAll(new CompositeValue[] { compData, compData2, compData3 });

      data2 = new TableValueSupport(tableType);
      data2.putAll(new CompositeValue[] { compData, compData2, compData3 });
      assertEquals("Instances with the same composite values are equal", data, data2);
      assertEquals("Instances with the same composite values are equal", data2, data);

      data2 = new TableValueSupport(tableType);
      data2.putAll(new CompositeValue[] { compData, compData2});
      assertNotSame("Instances with different composite values are not equal", data, data2);
      assertNotSame("Instances with different composite values are not equal", data2, data);
   }

   /**
    * Test the hashCode for a table value
    * 
    * @throws Exception for any problem
    */
   public void testHashCode() throws Exception
   {
      TableMetaType tableType = initTableType();

      TableValueSupport data = new TableValueSupport(tableType);

      CompositeValue compData = initCompositeValue(data);
      CompositeValue compData2 = initCompositeValue2(data);
      CompositeValue compData3 = initCompositeValue4(data);

      data.putAll(new CompositeValue[] { compData, compData2, compData3 });

      int myHashCode = tableType.hashCode() + compData.hashCode() + compData2.hashCode() + compData3.hashCode();
      assertEquals("Wrong hash code generated", myHashCode, data.hashCode());
   }

   /**
    * Test the toString for a table value
    * 
    * @throws Exception for any problem
    */
   public void testToString() throws Exception
   {
      TableMetaType tableType = initTableType();

      TableValueSupport data = new TableValueSupport(tableType);

      CompositeValue compData = initCompositeValue(data);
      CompositeValue compData2 = initCompositeValue2(data);
      CompositeValue compData3 = initCompositeValue4(data);

      data.putAll(new CompositeValue[] { compData, compData2, compData3 });

      String toString = data.toString();

      assertTrue("toString() should contain the tabular type", toString.indexOf(tableType.toString()) != -1);
      assertTrue("toString() should contain index=compositeValue for compData",
         toString.indexOf(Arrays.asList(data.calculateIndex(compData)) + "=" + compData) != -1);
      assertTrue("toString() should contain index=compositeValue for compData2",
         toString.indexOf(Arrays.asList(data.calculateIndex(compData2)) + "=" + compData2) != -1);
      assertTrue("toString() should contain index=compositeValue for compData3",
         toString.indexOf(Arrays.asList(data.calculateIndex(compData3)) + "=" + compData3) != -1);
   }

   /**
    * Test the serialization for a table value
    * 
    * @throws Exception for any problem
    */
   public void testSerialization() throws Exception
   {
      TableMetaType tableType = initTableType();

      TableValueSupport data = new TableValueSupport(tableType);

      CompositeValue compData = initCompositeValue(data);
      CompositeValue compData2 = initCompositeValue2(data);
      CompositeValue compData3 = initCompositeValue4(data);

      data.putAll(new CompositeValue[] { compData, compData2, compData3 });

      byte[] bytes = serialize(data);
      Object result = deserialize(bytes);
      assertEquals(data, result);
   }

   /**
    * Test the errors for a table value
    * 
    * @throws Exception for any problem
    */
   public void testErrors() throws Exception
   {
      TableMetaType tableType = initTableType();

      CompositeValue compData = initCompositeValue(tableType.getRowType());
      CompositeValue compData2 = initCompositeValue2(initCompositeMetaType2());

      try
      {
         new TableValueSupport(null);
         fail("Expected IllegalArgumentException for null tabular type");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         new TableValueSupport(null, 10, .5f);
         fail("Expected IllegalArgumentException for null tabular type");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         new TableValueSupport(tableType, -1, .5f);
         fail("Expected IllegalArgumentException for negative initial capacity");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         new TableValueSupport(tableType, 10, 0f);
         fail("Expected IllegalArgumentException for zero load factor");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         new TableValueSupport(tableType, 10, -0.5f);
         fail("Expected IllegalArgumentException for negative load factor");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         TableValueSupport data = new TableValueSupport(tableType);
         data.calculateIndex(null);
         fail("Expected IllegalArgumentException for calculate index on null object");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         TableValueSupport data = new TableValueSupport(tableType);
         data.calculateIndex(compData2);
         fail("Expected IllegalArgumentException for calculate index on wrong composite type");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         TableValueSupport data = new TableValueSupport(tableType);
         data.get((MetaValue[]) null);
         fail("Expected IllegalArgumentException for get(null)");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         TableValueSupport data = new TableValueSupport(tableType);
         data.get(new MetaValue[] { initStringWrong() });
         fail("Expected IllegalArgumentException for get(wrong)");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         TableValueSupport data = new TableValueSupport(tableType);
         data.put(null);
         fail("Expected IllegalArgumentException for put(CompositeValue) with null value");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         TableValueSupport data = new TableValueSupport(tableType);
         data.put(compData2);
         fail("Expected IllegalArgumentException for put(CompositeValue) with wrong CompositeType");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         TableValueSupport data = new TableValueSupport(tableType);
         data.put(compData);
         data.put(compData);
         fail("Expected IllegalArgumentException for put(CompositeValue)");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         TableValueSupport data = new TableValueSupport(tableType);
         data.remove((MetaValue[]) null);
         fail("Expected IllegalArgumentException for remove(null)");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         TableValueSupport data = new TableValueSupport(tableType);
         data.remove(new MetaValue[] { initStringWrong() });
         fail("Expected IllegalArgumentException for remove(wrong)");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         TableValueSupport data = new TableValueSupport(tableType);
         CompositeValue[] toPut = new CompositeValue[] { compData, null };
         data.putAll(toPut);
         fail("Expected IllegalArgumentException for putAll(CompositeValue[]) null");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         TableValueSupport data = new TableValueSupport(tableType);
         CompositeValue[] toPut = new CompositeValue[] { compData, compData2 };
         data.putAll(toPut);
         fail("Expected IllegalArgumentException for putAll(CompositeValue[]) wrong composite type");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         TableValueSupport data = new TableValueSupport(tableType);
         CompositeValue[] toPut = new CompositeValue[] { compData, compData };
         data.putAll(toPut);
         fail("Expected IllegalArgumentException for putAll(CompositeValue[]) with duplicate data");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         TableValueSupport data = new TableValueSupport(tableType);
         CompositeValue[] toPut = new CompositeValue[] { compData };
         data.putAll(toPut);
         data.putAll(toPut);
         fail("Expected IllegalArgumentException for putAll(CompositeValue[]) adding a duplicate");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }

   /**
    * Initialize a test table type
    * 
    * @return the type
    */
   protected TableMetaType initTableType()
   {
      CompositeMetaType rowType = initCompositeMetaType();

      String[] indexNames = new String[] { "name1", "name2" };
      TableMetaType tableType = new ImmutableTableMetaType("typeName", "description", rowType, indexNames);
      return tableType;
   }

   /**
    * Initialize a test table type 2
    * 
    * @return the type
    */
   protected TableMetaType initTableType2()
   {
      CompositeMetaType rowType = initCompositeMetaType();

      String[] indexNames = new String[] { "name1", "name2" };
      TableMetaType tableType = new ImmutableTableMetaType("typeName2", "description", rowType, indexNames);
      return tableType;
   }

   /**
    * Initialize a test composite value
    *
    * @param tableValue the table value
    * @return the type
    */
   protected CompositeValue initCompositeValue(TableValue tableValue)
   {
      return initCompositeValue(tableValue.getMetaType().getRowType());
   }

   /**
    * Initialize a test composite value
    *
    * @param rowType the row type
    * @return the type
    */
   protected CompositeValue initCompositeValue(CompositeMetaType rowType)
   {
      Map<String, MetaValue> map = initMapValues();
      CompositeValue compData = new CompositeValueSupport(rowType, map);
      return compData;
   }

   /**
    * Initialize a test composite value 2
    *
    * @param tableValue the table value
    * @return the type
    */
   protected CompositeValue initCompositeValue2(TableValue tableValue)
   {
      return initCompositeValue2(tableValue.getMetaType().getRowType());
   }

   /**
    * Initialize a test composite value 2
    *
    * @param rowType the row type
    * @return the type
    */
   protected CompositeValue initCompositeValue2(CompositeMetaType rowType)
   {
      Map<String, MetaValue> map = initMapValues2();
      CompositeValue compData = new CompositeValueSupport(rowType, map);
      return compData;
   }

   /**
    * Initialize a test composite value 3
    *
    * @param tableValue the table value
    * @return the type
    */
   protected CompositeValue initCompositeValue3(TableValue tableValue)
   {
      Map<String, MetaValue> map = initMapValues3();
      CompositeValue compData = new CompositeValueSupport(tableValue.getMetaType().getRowType(), map);
      return compData;
   }

   /**
    * Initialize a test composite value 4
    *
    * @param tableValue the table value
    * @return the type
    */
   protected CompositeValue initCompositeValue4(TableValue tableValue)
   {
      Map<String, MetaValue> map = initMapValues4();
      CompositeValue compData = new CompositeValueSupport(tableValue.getMetaType().getRowType(), map);
      return compData;
   }
}
