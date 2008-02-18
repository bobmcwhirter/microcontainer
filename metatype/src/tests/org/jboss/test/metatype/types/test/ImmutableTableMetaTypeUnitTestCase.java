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

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.types.ImmutableCompositeMetaType;
import org.jboss.metatype.api.types.ImmutableTableMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.api.types.TableMetaType;
import org.jboss.metatype.api.values.TableValue;
import org.jboss.test.metatype.AbstractMetaTypeTest;

/**
 * ImmutableMetaTypeUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ImmutableTableMetaTypeUnitTestCase extends AbstractMetaTypeTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(ImmutableTableMetaTypeUnitTestCase.class);
   }
   
   /**
    * Create a new ImmutableTableMetaTypeUnitTestCase.
    * 
    * @param name the test name
    */
   public ImmutableTableMetaTypeUnitTestCase(String name)
   {
      super(name);
   }

   /**
    * Test the metatype for a table type
    * 
    * @throws Exception for any problem
    */
   public void testTableMetaType() throws Exception
   {
      TableMetaType tableType = initTableMetaType();

      assertEquals(TableValue.class.getName(), tableType.getClassName());
      assertEquals("description", tableType.getDescription());
      assertEquals("typeName", tableType.getTypeName());
      assertTrue("Tabular type should not be an array", tableType.isArray() == false);
   }

   /**
    * Test the getRow for a table type
    * 
    * @throws Exception for any problem
    */
   public void testGetRowType() throws Exception
   {
      CompositeMetaType rowType = initRowType();
      TableMetaType tableType = initTableMetaType();

      assertEquals(rowType, tableType.getRowType());
   }

   /**
    * Test the getIndexNames for a table type
    * 
    * @throws Exception for any problem
    */
   public void testIndexNames() throws Exception
   {
      TableMetaType tableType = initTableMetaType();

      List<String> indexList = tableType.getIndexNames();
      assertTrue("wrong number of index names", indexList.size() == 2);
      assertTrue("index list should contain name1", indexList.contains("name1"));
      assertTrue("index list should contain name2", indexList.contains("name2"));
      Iterator<String> i = indexList.iterator();
      assertTrue("first index is name1", i.next().equals("name1"));
      assertTrue("second index is name2", i.next().equals("name2"));
   }

   /**
    * Test the isValue for a table type
    * 
    * @throws Exception for any problem
    */
   public void testIsValue() throws Exception
   {
      // TODO testIsValue
   }

   /**
    * Test the equals for a table type
    * 
    * @throws Exception for any problem
    */
   public void testEquals() throws Exception
   {
      TableMetaType tableType = initTableMetaType();

      assertNotSame("null is not equal to table type", tableType, null);
      assertNotSame("object is not a equal to table type", tableType, new Object());

      TableMetaType tableType2 = initTableMetaType();
      assertEquals("Should be equal, even though the table type is a different instance", tableType, tableType2);
      assertEquals("Should be equal, even though the table type is a different instance", tableType2, tableType);

      tableType2 = initTableMetaTypeDifferentTypeName();
      assertNotSame("should not be equal, they have different type names", tableType, tableType2);
      assertNotSame("should not be equal, they have different type names", tableType2, tableType);

      tableType2 = initTableMetaTypeDifferentRowTypes();
      assertNotSame("should not be equal, they have different row types", tableType, tableType2);
      assertNotSame("should not be equal, they have different row types", tableType2, tableType);

      tableType2 = initTableMetaTypeDifferentIndexNames();
      assertNotSame("should not be equal, they have different index names", tableType, tableType2);
      assertNotSame("should not be equal, they have different index names", tableType2, tableType);
   }

   /**
    * Test the hashCdoe for a table type
    * 
    * @throws Exception for any problem
    */
   public void testHashCode() throws Exception
   {
      CompositeMetaType rowType = initRowType();
      TableMetaType tableType = initTableMetaType();

      int myHashCode = "typeName".hashCode() + rowType.hashCode() + "name1".hashCode() + "name2".hashCode();
      assertTrue("Wrong hash code generated", myHashCode == tableType.hashCode());
   }

   /**
    * Test the toString for a table type
    * 
    * @throws Exception for any problem
    */
   public void testToString() throws Exception
   {
      CompositeMetaType rowType = initRowType();
      TableMetaType tableType = initTableMetaType();

      String toString = tableType.toString();

      assertTrue("toString() should contain the tabular type class name", toString.indexOf(TableMetaType.class.getSimpleName()) != -1);
      assertTrue("toString() should contain the type name", toString.indexOf("typeName") != -1);
      assertTrue("toString() should contain the row type " + rowType, toString.indexOf(rowType.toString()) != -1);
      assertTrue("toString() should contain the index name1", toString.indexOf("name1") != -1);
      assertTrue("toString() should contain the index name2", toString.indexOf("name2") != -1);
   }

   /**
    * Test the serialization for a table type
    * 
    * @throws Exception for any problem
    */
   public void testSerialization() throws Exception
   {
      TableMetaType tableType = initTableMetaType();
      byte[] bytes = serialize(tableType);
      Object result = deserialize(bytes);
      assertEquals(tableType, result);
   }

   /**
    * Test the errors for a table type
    * 
    * @throws Exception for any problem
    */
   public void testErrors() throws Exception
   {
      CompositeMetaType rowType = initRowType();
      String[] indexNames = new String[] { "name1", "name2" };

      try
      {
         new ImmutableTableMetaType(null, "description", rowType, indexNames);
         fail("Expected IllegalArgumentException for null type name");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         new ImmutableTableMetaType("", "description", rowType, indexNames);
         fail("Expected IllegalArgumentException for empty type name");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         new ImmutableTableMetaType("typeName", null, rowType, indexNames);
         fail("Expected IllegalArgumentException for null description");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         new ImmutableTableMetaType("typeName", "", rowType, indexNames);
         fail("Expected IllegalArgumentException for empty description");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         new ImmutableTableMetaType("typeName", "description", null, indexNames);
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         new ImmutableTableMetaType("typeName", "description", rowType, null);
         fail("Expected IllegalArgumentException for null row type");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         new ImmutableTableMetaType("typeName", "description", rowType, new String[0]);
         fail("Expected IllegalArgumentException for empty index names");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         new ImmutableTableMetaType("typeName", "description", rowType, new String[] { "name1", null });
         fail("Expected IllegalArgumentException for null index name element");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         new ImmutableTableMetaType("typeName", "description", rowType, new String[] { "name1", "" });
         fail("Expected IllegalArgumentException for empty index name element");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      try
      {
         new ImmutableTableMetaType("typeName", "description", rowType, new String[] { "name1", "nameX" });
         fail("Expected IllegalArgumentException for invalid index name");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }

   /**
    * Initialise a test row type
    * 
    * @return the test row type
    * @throws Exception for any problem
    */
   protected CompositeMetaType initRowType() throws Exception
   {
      String[] itemNames = new String[] { "name1", "name2" };
      String[] itemDescriptions = new String[] { "desc1", "desc2" };
      MetaType<?>[] itemTypes = new MetaType[] { SimpleMetaType.STRING, SimpleMetaType.INTEGER };
      CompositeMetaType rowType = new ImmutableCompositeMetaType("rowTypeName", "rowDescription", itemNames, itemDescriptions, itemTypes);
      return rowType;
   }

   /**
    * Initialise a test table type
    * 
    * @return the type
    * @throws Exception for any problem
    */
   protected TableMetaType initTableMetaType() throws Exception
   {
      CompositeMetaType rowType = initRowType();

      String[] indexNames = new String[] { "name1", "name2" };
      TableMetaType tableType = new ImmutableTableMetaType("typeName", "description", rowType, indexNames);
      return tableType;
   }

   /**
    * Initialise a test table type with a different type name
    * 
    * @return the type
    * @throws Exception for any problem
    */
   protected TableMetaType initTableMetaTypeDifferentTypeName() throws Exception
   {
      CompositeMetaType rowType = initRowType();

      String[] indexNames = new String[] { "name1", "name2" };
      TableMetaType tableType = new ImmutableTableMetaType("typeName2", "description", rowType, indexNames);
      return tableType;
   }

   /**
    * Initialise a test row type 2
    * 
    * @return the type
    * @throws Exception for any problem
    */
   protected CompositeMetaType initRowType2() throws Exception
   {
      String[] itemNames = new String[] { "name1", "name2" };
      String[] itemDescriptions = new String[] { "desc1", "desc2" };
      MetaType<?>[] itemTypes = new MetaType[] { SimpleMetaType.STRING, SimpleMetaType.INTEGER };
      CompositeMetaType rowType = new ImmutableCompositeMetaType("rowTypeName2", "rowDescription", itemNames, itemDescriptions, itemTypes);
      return rowType;
   }

   /**
    * Initialise a test table type with different row types
    * 
    * @return the type
    * @throws Exception for any problem
    */
   protected TableMetaType initTableMetaTypeDifferentRowTypes() throws Exception
   {
      CompositeMetaType rowType = initRowType2();

      String[] indexNames = new String[] { "name1", "name2" };
      TableMetaType tableType = new ImmutableTableMetaType("typeName", "description", rowType, indexNames);
      return tableType;
   }

   /**
    * Initialise a test table type with different index names
    * 
    * @return the type
    * @throws Exception for any problem
    */
   protected TableMetaType initTableMetaTypeDifferentIndexNames() throws Exception
   {
      CompositeMetaType rowType = initRowType();

      String[] indexNames = new String[] { "name2", "name1" };
      TableMetaType tableType = new ImmutableTableMetaType("typeName", "description", rowType, indexNames);
      return tableType;
   }
}
