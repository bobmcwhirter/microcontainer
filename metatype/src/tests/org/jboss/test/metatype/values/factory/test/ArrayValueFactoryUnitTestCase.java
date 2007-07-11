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

import org.jboss.metatype.api.types.ArrayMetaType;
import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.values.ArrayValue;
import org.jboss.metatype.api.values.ArrayValueSupport;
import org.jboss.metatype.api.values.CompositeValue;
import org.jboss.metatype.api.values.CompositeValueSupport;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.SimpleValueSupport;
import org.jboss.test.metatype.values.factory.support.TestSimpleComposite;

import junit.framework.Test;

/**
 * ArrayValueFactoryUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ArrayValueFactoryUnitTestCase extends AbstractMetaValueFactoryTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(ArrayValueFactoryUnitTestCase.class);
   }
   
   /**
    * Create a new ArrayValueFactoryUnitTestCase.
    * 
    * @param name the test name
    */
   public ArrayValueFactoryUnitTestCase(String name)
   {
      super(name);
   }

   /**
    * Test the correct value is generated for a simple array
    * 
    * @throws Exception for any problem
    */
   public void testSimpleArray() throws Exception
   {
      String[] array = { "Hello", "Goodbye" };
      ArrayMetaType arrayType = assertInstanceOf(resolve(array.getClass()), ArrayMetaType.class);
      MetaValue[] metaArray = { SimpleValueSupport.wrap("Hello"), SimpleValueSupport.wrap("Goodbye") };
      ArrayValueSupport expected = new ArrayValueSupport(arrayType, metaArray);
      
      MetaValue result = createMetaValue(array);
      ArrayValue actual = assertInstanceOf(result, ArrayValue.class);
      getLog().debug("Array Value: " + actual);
      assertEquals(expected, actual);
   }

   /**
    * Test the correct value is generated for a composite array
    * 
    * @throws Exception for any problem
    */
   public void testCompositeArray() throws Exception
   {
      TestSimpleComposite hello = new TestSimpleComposite("Hello");
      TestSimpleComposite goodbye = new TestSimpleComposite("Goodbye");
      TestSimpleComposite[] array = { hello, goodbye };
      ArrayMetaType arrayType = assertInstanceOf(resolve(array.getClass()), ArrayMetaType.class);
      CompositeMetaType compositeType = assertInstanceOf(resolve(TestSimpleComposite.class), CompositeMetaType.class);
      String[] itemNames = { "something" };
      MetaValue[] itemValues = { SimpleValueSupport.wrap("Hello") };
      CompositeValue helloValue = new CompositeValueSupport(compositeType, itemNames, itemValues);
      itemValues = new MetaValue[] { SimpleValueSupport.wrap("Goodbye") };
      CompositeValue goodbyeValue = new CompositeValueSupport(compositeType, itemNames, itemValues);
      MetaValue[] metaArray = { helloValue, goodbyeValue };
      ArrayValueSupport expected = new ArrayValueSupport(arrayType, metaArray);
      
      MetaValue result = createMetaValue(array);
      ArrayValue actual = assertInstanceOf(result, ArrayValue.class);
      getLog().debug("Array Value: " + actual);
      assertEquals(expected, actual);
   }

   /**
    * Test the correct value is generated for a mutlidimensional array
    * 
    * @throws Exception for any problem
    */
   public void testMultipleArray() throws Exception
   {
      String[][] array = { { "Hello" }, { "Goodbye" } };
      ArrayMetaType arrayType = assertInstanceOf(resolve(array.getClass()), ArrayMetaType.class);
      Object[][] metaArray = { { SimpleValueSupport.wrap("Hello") }, { SimpleValueSupport.wrap("Goodbye") } };
      ArrayValueSupport expected = new ArrayValueSupport(arrayType, metaArray);
      
      MetaValue result = createMetaValue(array);
      ArrayValue actual = assertInstanceOf(result, ArrayValue.class);
      getLog().debug("Array Value: " + actual);
      assertEquals(expected, actual);
   }
}
