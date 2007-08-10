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

import junit.framework.Test;

import org.jboss.metatype.api.types.ArrayMetaType;
import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.types.ImmutableCompositeMetaType;
import org.jboss.metatype.api.types.ImmutableTableMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.api.types.TableMetaType;
import org.jboss.metatype.api.values.TableValue;
import org.jboss.test.metatype.AbstractMetaTypeTest;
import org.jboss.test.metatype.types.support.MockCompositeValue;
import org.jboss.test.metatype.types.support.MockSimpleValue;
import org.jboss.test.metatype.types.support.MockTableValue;

/**
 * ArrayMetaTypeUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ArrayMetaTypeUnitTestCase extends AbstractMetaTypeTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(ArrayMetaTypeUnitTestCase.class);
   }
   
   /**
    * Create a new ArrayMetaTypeUnitTestCase.
    * 
    * @param name the test name
    */
   public ArrayMetaTypeUnitTestCase(String name)
   {
      super(name);
   }

   /**
    * Test the meta type for an array meta type
    * 
    * @throws Exception for any problem
    */
   public void testArrayTypeMetaType() throws Exception
   {
      ArrayMetaType arrayType = new ArrayMetaType(3, SimpleMetaType.STRING);
      assertEquals("[[[Ljava.lang.String;", arrayType.getClassName());
      assertEquals("3-dimension array of java.lang.String", arrayType.getDescription());
      assertEquals("[[[Ljava.lang.String;", arrayType.getTypeName());
      assertTrue("Type should be an array", arrayType.isArray());
   }

   public void testCharArrayType()
   {
      ArrayMetaType arrayType = ArrayMetaType.getPrimitiveArrayType(char[].class);
      assertEquals("[C", arrayType.getClassName());
      assertEquals("1-dimension array of char", arrayType.getDescription());
      assertEquals("[C", arrayType.getTypeName());
      assertTrue("Type should be an array", arrayType.isArray());
      assertEquals(SimpleMetaType.CHARACTER, arrayType.getElementType());
   }

   /**
    * Test the the dimension for an array meta type
    * 
    * @throws Exception for any problem
    */
   public void testGetDimension() throws Exception
   {
      ArrayMetaType arrayType = new ArrayMetaType(3, SimpleMetaType.STRING);
      assertEquals("Dimension should be 3", 3, arrayType.getDimension());
   }

   /**
    * Test the element type
    * 
    * @throws Exception for any problem
    */
   public void testElementType() throws Exception
   {
      ArrayMetaType arrayType = new ArrayMetaType(3, SimpleMetaType.STRING);
      assertEquals("Element MetaType should be " + SimpleMetaType.STRING, SimpleMetaType.STRING, arrayType.getElementType());
   }

   /**
    * Test the isValue for an array meta type
    * 
    * @throws Exception for any problem
    */
   public void testIsValueSimpleValue() throws Exception
   {
      SimpleMetaType<String> simpleType = SimpleMetaType.STRING;
      MockSimpleValue sv = new MockSimpleValue<String>(simpleType);
      MockSimpleValue[][] compData1 = new MockSimpleValue[][]
      {
         { sv, null }, { sv, sv }
      };
            
      ArrayMetaType compArrayType1 = new ArrayMetaType(2, SimpleMetaType.STRING);
      assertTrue("compData1 should be a value of array type", compArrayType1.isValue(compData1));

      ArrayMetaType compArrayType2 = new ArrayMetaType(1, SimpleMetaType.STRING);
      assertFalse("compData1 should not be a value of array type, wrong dimension", compArrayType2.isValue(compData1));

      SimpleMetaType<Integer> simpleType2 = SimpleMetaType.INTEGER;
      ArrayMetaType compArrayType3 = new ArrayMetaType(2, simpleType2);
      assertFalse("compData1 should not be a value of array type, wrong element type", compArrayType3.isValue(compData1));
   }

   /**
    * Test the isValue for an array meta type
    * 
    * @throws Exception for any problem
    */
   public void testIsValueComposite() throws Exception
   {
      String[] itemNames = new String[] { "name1", "name2" };
      String[] itemDescriptions = new String[] { "desc1", "desc2" };
      MetaType[] itemTypes = new MetaType[] { SimpleMetaType.STRING, SimpleMetaType.INTEGER };
      CompositeMetaType compositeType = new ImmutableCompositeMetaType("typeName", "description", itemNames, itemDescriptions, itemTypes);
      MockCompositeValue cv = new MockCompositeValue(compositeType);
      MockCompositeValue[][] compData1 = new MockCompositeValue[][]
      {
         { cv, null }, { cv, cv }
      };

      ArrayMetaType compArrayType1 = new ArrayMetaType(2, compositeType);
      assertTrue("compData1 should be a value of array type", compArrayType1.isValue(compData1));

      ArrayMetaType compArrayType2 = new ArrayMetaType(1, compositeType);
      assertFalse("compData1 should not be a value of array type, wrong dimension", compArrayType2.isValue(compData1));

      CompositeMetaType compositeType2 = new ImmutableCompositeMetaType("typeName2", "description", itemNames, itemDescriptions, itemTypes);
      ArrayMetaType compArrayType3 = new ArrayMetaType(2, compositeType2);
      assertFalse("compData1 should not be a value of array type, wrong element type", compArrayType3.isValue(compData1));
   }

   /**
    * Test the isValue for an array meta type
    * 
    * @throws Exception for any problem
    */
   public void testIsValueTable() throws Exception
   {
      String[] itemNames = new String[] { "name1", "name2" };
      String[] itemDescriptions = new String[] { "desc1", "desc2" };
      MetaType[] itemTypes = new MetaType[] { SimpleMetaType.STRING, SimpleMetaType.INTEGER };
      CompositeMetaType compositeType = new ImmutableCompositeMetaType("typeName", "description", itemNames, itemDescriptions, itemTypes);
      TableMetaType tableType = new ImmutableTableMetaType("typeName", "description", compositeType, new String[] { "name1" });
      TableValue tv = new MockTableValue(tableType);
      TableValue[][] tabData1 = new TableValue[][]
      {
         { tv, null }, { tv, tv }
      };

      ArrayMetaType tabArrayType1 = new ArrayMetaType(2, tableType);
      assertTrue("tabData1 should be a value of array type", tabArrayType1.isValue(tabData1));

      ArrayMetaType tabArrayType2 = new ArrayMetaType(1, tableType);
      assertFalse("tabData1 should not be a value of array type, wrong number of dimensions", tabArrayType2.isValue(tabData1));

      TableMetaType tableType2 = new ImmutableTableMetaType("typeName2", "description", compositeType, new String[] { "name1" });
      ArrayMetaType tabArrayType3 = new ArrayMetaType(2, tableType2);
      assertFalse("tabData1 should not be a value of array type, wrong element type", tabArrayType3.isValue(tabData1));
   }

   /**
    * Test the equals for an array meta type
    * 
    * @throws Exception for any problem
    */
   public void testEquals() throws Exception
   {
      ArrayMetaType arrayType = new ArrayMetaType(3, SimpleMetaType.STRING);

      assertNotSame("null is not an array type", null, arrayType);
      assertNotSame("object is not an array type", new Object(), arrayType);

      assertEquals("should be equal to itself", arrayType, arrayType);

      ArrayMetaType arrayType2 = new ArrayMetaType(3, SimpleMetaType.STRING);
      assertEquals("should be equal, even though different instances", arrayType, arrayType2);
      assertEquals("should be equal, even though different instances", arrayType2, arrayType);

      arrayType2 = new ArrayMetaType(2, SimpleMetaType.STRING);
      assertNotSame("should not be equal, wrong number of dimensions", arrayType, arrayType2);
      assertNotSame("should not be equal, wrong number of dimensions", arrayType2, arrayType);

      arrayType2 = new ArrayMetaType(3, SimpleMetaType.INTEGER);
      assertNotSame("should not be equal, wrong element type", arrayType, arrayType2);
      assertNotSame("should not be equal, wrong element type", arrayType2, arrayType);
   }

   /**
    * Test the hashCode for an array meta type
    * 
    * @throws Exception for any problem
    */
   public void testHashCode() throws Exception
   {
      ArrayMetaType arrayType = new ArrayMetaType(3, SimpleMetaType.STRING);

      int myHashCode = 3 + SimpleMetaType.STRING.hashCode();
      assertTrue("Wrong hash code generated", myHashCode == arrayType.hashCode());
   }

   /**
    * Test the toString for an array meta type
    * 
    * @throws Exception for any problem
    */
   public void testToString() throws Exception
   {
      ArrayMetaType arrayType = new ArrayMetaType(3, SimpleMetaType.STRING);

      String toString = arrayType.toString();

      assertTrue("toString() should contain the array type class name", toString.indexOf(ArrayMetaType.class.getSimpleName()) != -1);
      assertTrue("toString() should contain the dimension", toString.indexOf("3") != -1);
      assertTrue("toString() should contain the element type", toString.indexOf(SimpleMetaType.STRING.toString()) != -1);
   }

   /**
    * Test the serialization for an array meta type
    * 
    * @throws Exception for any problem
    */
   public void testSerialization() throws Exception
   {
      ArrayMetaType arrayType = new ArrayMetaType(3, SimpleMetaType.STRING);

      byte[] bytes = serialize(arrayType);
      Object result = deserialize(bytes);

      assertEquals(arrayType, result);
   }

   /**
    * Test the errors for an array meta type
    * 
    * @throws Exception for any problem
    */
   public void testErrors() throws Exception
   {
      try
      {
         new ArrayMetaType(-1, SimpleMetaType.STRING);
         fail("Excepted IllegalArgumentException for negative dimension");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         new ArrayMetaType(1, new ArrayMetaType(2, SimpleMetaType.STRING));
         fail("Excepted IllegalArgumentException for ArrayMetaType element type");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         new ArrayMetaType(1, null);
         fail("Excepted IllegalArgumentException for null element type");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }
}
