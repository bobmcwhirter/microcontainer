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

import java.util.Set;

import junit.framework.Test;

import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.types.ImmutableCompositeMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.api.values.CompositeValue;
import org.jboss.test.metatype.AbstractMetaTypeTest;

/**
 * CompositeMetaTypeUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ImmutableCompositeMetaTypeUnitTestCase extends AbstractMetaTypeTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(ImmutableCompositeMetaTypeUnitTestCase.class);
   }
   
   /**
    * Create a new ImmutableCompositeMetaTypeUnitTestCase.
    * 
    * @param name the test name
    */
   public ImmutableCompositeMetaTypeUnitTestCase(String name)
   {
      super(name);
   }

   /**
    * Test the metatype for a composite type
    * 
    * @throws Exception for any problem
    */
   public void testCompositeMetaTypeMetaType() throws Exception
   {
      CompositeMetaType compositeType = initCompositeMetaType();
      assertEquals(CompositeValue.class.getName(), compositeType.getClassName());
      assertEquals("description", compositeType.getDescription());
      assertEquals("typeName", compositeType.getTypeName());
      assertTrue("Composite meta type should not be an array", compositeType.isArray() == false);
   }

   /**
    * Test the containsItem for a composite type
    * 
    * @throws Exception for any problem
    */
   public void testContainsItem() throws Exception
   {
      CompositeMetaType compositeType = initCompositeMetaType();
      assertTrue("Composite type should contain key name1", compositeType.containsItem("name1") == true);
      assertTrue("Composite type should contain key name2", compositeType.containsItem("name2") == true);
      assertTrue("Composite type should not contain key nameX", compositeType.containsItem("nameX") == false);
      assertTrue("Composite type should not contain key null", compositeType.containsItem(null) == false);
      assertTrue("Composite type should not contain key <empty>", compositeType.containsItem("") == false);
   }

   /**
    * Test the description for a composite type item
    * 
    * @throws Exception for any problem
    */
   public void testGetDescriptionForItemName() throws Exception
   {
      CompositeMetaType compositeType = initCompositeMetaType();
      assertEquals("desc1", compositeType.getDescription("name1"));
      assertEquals("desc2", compositeType.getDescription("name2"));
   }

   /**
    * Test the type for a composite type item
    * 
    * @throws Exception for any problem
    */
   public void testGetTypeForItemName() throws Exception
   {
      CompositeMetaType compositeType = initCompositeMetaType();
      assertEquals(SimpleMetaType.STRING, compositeType.getType("name1"));
      assertEquals(SimpleMetaType.INTEGER, compositeType.getType("name2"));
   }

   /**
    * Test the key set for a composite type
    * 
    * @throws Exception for any problem
    */
   public void testKeySet() throws Exception
   {
      CompositeMetaType compositeType = initCompositeMetaType();
      Set<String> keys = compositeType.keySet();
      assertTrue("Should be 2 items", keys.size() == 2);
      assertTrue("Should contain name1", keys.contains("name1"));
      assertTrue("Should contain name2", keys.contains("name2"));
   }

   /**
    * Test the isValue for a composite type
    * 
    * @throws Exception for any problem
    */
   public void testIsValue() throws Exception
   {
      // TODO testIsValue
   }

   /**
    * Test the equals for a composite type
    * 
    * @throws Exception for any problem
    */
   public void testEquals() throws Exception
   {
      CompositeMetaType compositeType = initCompositeMetaType();;

      assertTrue("null is not equal composite type", compositeType.equals(null) == false);
      assertTrue("object is not equal composite type", compositeType.equals(new Object()) == false);

      CompositeMetaType compositeType2 = initCompositeMetaType();
      assertTrue("compositeType2 should be equal composite type, even though not the same object instance",
         compositeType.equals(compositeType2));
      assertTrue("compositeType2 should be equal composite type, even though not the same object instance",
         compositeType2.equals(compositeType));

      compositeType2 = initCompositeMetaTypeDifferentItemTypes();
      assertTrue("compositeType2 should not be equal composite type, it has different types",
         compositeType.equals(compositeType2) == false);
      assertTrue("compositeType2 should not be equal composite type, it has different types",
         compositeType2.equals(compositeType) == false);

      compositeType2 = initCompositeMetaTypeDifferentTypeName();
      assertTrue("compositeType2 should not be equal composite type, it has a different type name",
         compositeType.equals(compositeType2) == false);
      assertTrue("compositeType2 should not be equal composite type, it has a different type name",
         compositeType2.equals(compositeType) == false);

      compositeType2 = initCompositeMetaTypeDifferentItemNames();
      assertTrue("compositeType2 should not be equal composite type, it has different item names",
         compositeType.equals(compositeType2) == false);
      assertTrue("compositeType2 should not be equal composite type, it has different item names",
         compositeType2.equals(compositeType) == false);
   }

   /**
    * Test the hashCode for a composite type
    * 
    * @throws Exception for any problem
    */
   public void testHashCode() throws Exception
   {
      CompositeMetaType compositeType = initCompositeMetaType();

      int myHashCode = "typeName".hashCode();
      myHashCode += SimpleMetaType.STRING.hashCode();
      myHashCode += SimpleMetaType.INTEGER.hashCode();
      myHashCode += "name1".hashCode();
      myHashCode += "name2".hashCode();
      assertTrue("Wrong hash code generated", myHashCode == compositeType.hashCode());
   }

   /**
    * Test the toString for a composite type
    * 
    * @throws Exception for any problem
    */
   public void testToString() throws Exception
   {
      CompositeMetaType compositeType = initCompositeMetaType();

      String toString = compositeType.toString();

      assertTrue("toString() should contain the composite type class name", toString.indexOf(compositeType.getClass().getSimpleName()) != -1);
      assertTrue("toString() should contain the item name name1", toString.indexOf("name1") != -1);
      assertTrue("toString() should contain the item name name2", toString.indexOf("name2") != -1);
      assertTrue("toString() should contain " + SimpleMetaType.STRING.getTypeName(), toString.indexOf(SimpleMetaType.STRING.getTypeName()) != -1);
      assertTrue("toString() should contain " + SimpleMetaType.INTEGER.getTypeName(), toString.indexOf(SimpleMetaType.INTEGER.getTypeName()) != -1);
   }

   /**
    * Test the serialization for a composite type
    * 
    * @throws Exception for any problem
    */
   public void testSerialization() throws Exception
   {
      CompositeMetaType compositeType = initCompositeMetaType();
      byte[] bytes = serialize(compositeType);
      Object result = deserialize(bytes);
      assertEquals(compositeType, result);
   }

   /**
    * Test the errors for a composite type
    * 
    * @throws Exception for any problem
    */
   public void testErrors() throws Exception
   {
      String[] itemNames = new String[] { "name1", "name2" };
      String[] itemDescriptions = new String[] { "desc1", "desc2" };
      MetaType<?>[] itemTypes = new MetaType[] { SimpleMetaType.STRING, SimpleMetaType.INTEGER };

      try
      {
         new ImmutableCompositeMetaType(null, "description", itemNames, itemDescriptions, itemTypes);
         fail("Excepted IllegalArgumentException for null typeName");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         new ImmutableCompositeMetaType("", "description", itemNames, itemDescriptions, itemTypes);
         fail("Excepted IllegalArgumentException for empty typeName");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         new ImmutableCompositeMetaType("typeName", null, itemNames, itemDescriptions, itemTypes);
         fail("Excepted IllegalArgumentException for null description");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         new ImmutableCompositeMetaType("typeName", "", itemNames, itemDescriptions, itemTypes);
         fail("Excepted IllegalArgumentException for empty description");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         new ImmutableCompositeMetaType("typeName", "description", null, itemDescriptions, itemTypes);
         fail("Excepted IllegalArgumentException for null item names");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         new ImmutableCompositeMetaType("typeName", "description", itemNames, null, itemTypes);
         fail("Excepted IllegalArgumentException for null item descriptions");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         new ImmutableCompositeMetaType("typeName", "description", itemNames, itemDescriptions, null);
         fail("Excepted IllegalArgumentException for null item types");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      String[] nullItemNames = new String[] { "name1", null };
      try
      {
         new ImmutableCompositeMetaType("typeName", "description", nullItemNames, itemDescriptions, itemTypes);
         fail("Excepted IllegalArgumentException for null element of item names");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      String[] nullItemDescriptions = new String[] { "desc1", null };
      try
      {
         new ImmutableCompositeMetaType("typeName", "description", itemNames, nullItemDescriptions, itemTypes);
         fail("Excepted IllegalArgumentException for null element of item descriptions");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      MetaType<?>[] nullItemTypes = new MetaType[] { SimpleMetaType.STRING, null };
      try
      {
         new ImmutableCompositeMetaType("typeName", "description", itemNames, itemDescriptions, nullItemTypes);
         fail("Excepted IllegalArgumentException for null element of item types");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      String[] wrongItemNames = new String[] { "name1" };
      try
      {
         new ImmutableCompositeMetaType("typeName", "description", wrongItemNames, itemDescriptions, itemTypes);
         fail("Excepted IllegalArgumentException for wrong number of elements for item names");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      String[] wrongItemDescriptions = new String[] { "desc1"};
      try
      {
         new ImmutableCompositeMetaType("typeName", "description", itemNames, wrongItemDescriptions, itemTypes);
         fail("Excepted IllegalArgumentException for wrong number of elements for item descriptions");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      MetaType<?>[] wrongItemTypes = new MetaType[] { SimpleMetaType.STRING };
      try
      {
         new ImmutableCompositeMetaType("typeName", "description", itemNames, itemDescriptions, wrongItemTypes);
         fail("Excepted IllegalArgumentException for wrong number of elements for item types");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      String[] duplicateItemNames = new String[] { "desc1", "desc1" };
      try
      {
         new ImmutableCompositeMetaType("typeName", "description", duplicateItemNames, itemDescriptions, itemTypes);
         fail("Excepted OpenDataException for duplicate item names");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }

      duplicateItemNames = new String[] { "desc1", " desc1 " };
      try
      {
         new ImmutableCompositeMetaType("typeName", "description", duplicateItemNames, itemDescriptions, itemTypes);
         fail("Excepted OpenDataException for duplicate item names");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }

   /**
    * Initialise a test composite metatype
    *
    * @return the meta type
    */
   protected CompositeMetaType initCompositeMetaType()
   {
      String[] itemNames = new String[] { "name1", "name2" };
      String[] itemDescriptions = new String[] { "desc1", "desc2" };
      MetaType<?>[] itemTypes = new MetaType[] { SimpleMetaType.STRING, SimpleMetaType.INTEGER };
      CompositeMetaType compositeType = new ImmutableCompositeMetaType("typeName", "description", itemNames, itemDescriptions, itemTypes);
      return compositeType;
   }

   /**
    * Initialise a test composite metatype with different item types
    *
    * @return the meta type
    */
   protected CompositeMetaType initCompositeMetaTypeDifferentItemTypes()
   {
      String[] itemNames = new String[] { "name1", "name2" };
      String[] itemDescriptions = new String[] { "desc1", "desc2" };
      MetaType<?>[] itemTypes = new MetaType[] { SimpleMetaType.STRING, SimpleMetaType.LONG };
      CompositeMetaType compositeType = new ImmutableCompositeMetaType("typeName", "description", itemNames, itemDescriptions, itemTypes);
      return compositeType;
   }

   /**
    * Initialise a test composite metatype with a different type name
    *
    * @return the meta type
    */
   protected CompositeMetaType initCompositeMetaTypeDifferentTypeName()
   {
      String[] itemNames = new String[] { "name1", "name2" };
      String[] itemDescriptions = new String[] { "desc1", "desc2" };
      MetaType<?>[] itemTypes = new MetaType[] { SimpleMetaType.STRING, SimpleMetaType.INTEGER };
      CompositeMetaType compositeType = new ImmutableCompositeMetaType("typeName2", "description", itemNames, itemDescriptions, itemTypes);
      return compositeType;
   }

   /**
    * Initialise a test composite metatype with different item names
    *
    * @return the meta type
    */
   protected CompositeMetaType initCompositeMetaTypeDifferentItemNames()
   {
      String[] itemNames = new String[] { "nameX", "name2" };
      String[] itemDescriptions = new String[] { "desc1", "desc2" };
      MetaType<?>[] itemTypes = new MetaType[] { SimpleMetaType.STRING, SimpleMetaType.INTEGER };
      CompositeMetaType compositeType = new ImmutableCompositeMetaType("typeName", "description", itemNames, itemDescriptions, itemTypes);
      return compositeType;
   }
}
