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

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import org.jboss.metatype.api.types.CollectionMetaType;
import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.values.CollectionValue;
import org.jboss.metatype.api.values.CollectionValueSupport;
import org.jboss.metatype.api.values.CompositeValue;
import org.jboss.metatype.api.values.CompositeValueSupport;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.SimpleValueSupport;
import org.jboss.test.metatype.values.factory.support.TestSimpleComposite;

/**
 * CollectionValueFactoryUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class CollectionValueFactoryUnitTestCase extends AbstractMetaValueFactoryTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(CollectionValueFactoryUnitTestCase.class);
   }
   
   /**
    * Create a new CollectionValueFactoryUnitTestCase.
    * 
    * @param name the test name
    */
   public CollectionValueFactoryUnitTestCase(String name)
   {
      super(name);
   }

   /**
    * Create a test simple collection
    * 
    * @return the test value
    */
   public Collection<String> simpleCollection()
   {
      Collection<String> result = new ArrayList<String>();
      result.add("Hello");
      result.add("Goodbye");
      return result;
   }

   /**
    * Create a test composite collection
    * 
    * @return the test value
    */
   public Collection<TestSimpleComposite> compositeCollection()
   {
      Collection<TestSimpleComposite> result = new ArrayList<TestSimpleComposite>();
      result.add(new TestSimpleComposite("Hello"));
      result.add(new TestSimpleComposite("Goodbye"));
      return result;
   }

   /**
    * Create a test simple list
    * 
    * @return the test value
    */
   public List<String> simpleList()
   {
      List<String> result = new ArrayList<String>();
      result.add("Hello");
      result.add("Goodbye");
      return result;
   }

   /**
    * Create a test composite list
    * 
    * @return the test value
    */
   public List<TestSimpleComposite> compositeList()
   {
      List<TestSimpleComposite> result = new ArrayList<TestSimpleComposite>();
      result.add(new TestSimpleComposite("Hello"));
      result.add(new TestSimpleComposite("Goodbye"));
      return result;
   }

   /**
    * Create a test simple set
    * 
    * @return the test value
    */
   public Set<String> simpleSet()
   {
      Set<String> result = new LinkedHashSet<String>();
      result.add("Hello");
      result.add("Goodbye");
      return result;
   }

   /**
    * Create a test composite set
    * 
    * @return the test value
    */
   public Set<TestSimpleComposite> compositeSet()
   {
      Set<TestSimpleComposite> result = new LinkedHashSet<TestSimpleComposite>();
      result.add(new TestSimpleComposite("Hello"));
      result.add(new TestSimpleComposite("Goodbye"));
      return result;
   }
   
   /**
    * Test the correct value is generated for a simple collection
    * 
    * @throws Exception for any problem
    */
   public void testSimpleCollection() throws Exception
   {
      testSimple("simpleCollection", simpleCollection());
   }
   
   /**
    * Test the correct value is generated for a composite collection
    * 
    * @throws Exception for any problem
    */
   public void testCompositeCollection() throws Exception
   {
      testComposite("compositeCollection", compositeCollection());
   }
   
   /**
    * Test the correct value is generated for a simple list
    * 
    * @throws Exception for any problem
    */
   public void testSimpleList() throws Exception
   {
      testSimple("simpleList", simpleList());
   }
   
   /**
    * Test the correct value is generated for a composite list
    * 
    * @throws Exception for any problem
    */
   public void testCompositeList() throws Exception
   {
      testComposite("compositeList", compositeList());
   }
   
   /**
    * Test the correct value is generated for a simple set
    * 
    * @throws Exception for any problem
    */
   public void testSimpleSet() throws Exception
   {
      testSimple("simpleSet", simpleSet());
   }
   
   /**
    * Test the correct value is generated for a composite set
    * 
    * @throws Exception for any problem
    */
   public void testCompositeSet() throws Exception
   {
      testComposite("compositeSet", compositeSet());
   }
   
   /**
    * Test a simple collection
    * 
    * @param methodName that gives the collection
    * @param collection the collection value
    * @throws Exception for any problem
    */
   public void testSimple(String methodName, Collection<String> collection) throws Exception
   {
      Method method = getClass().getMethod(methodName);
      Type type = method.getGenericReturnType();

      CollectionMetaType<?> arrayType = assertInstanceOf(resolve(type), CollectionMetaType.class);
      MetaValue[] metaArray = { SimpleValueSupport.wrap("Hello"), SimpleValueSupport.wrap("Goodbye") };
      CollectionValueSupport expected = new CollectionValueSupport(arrayType, metaArray);
      
      MetaValue result = createMetaValue(collection, type);
      CollectionValue actual = assertInstanceOf(result, CollectionValue.class);
      getLog().debug("Collection Value: " + actual);
      assertEquals(expected, actual);
   }

   /**
    * Test a composite collection
    * 
    * @param methodName that gives the collection
    * @param collection the collection value
    * @throws Exception for any problem
    */
   public void testComposite(String methodName, Collection<TestSimpleComposite> collection) throws Exception
   {
      Method method = getClass().getMethod(methodName);
      Type type = method.getGenericReturnType();
      
      CollectionMetaType<?> arrayType = assertInstanceOf(resolve(type), CollectionMetaType.class);
      CompositeMetaType compositeType = assertInstanceOf(resolve(TestSimpleComposite.class), CompositeMetaType.class);
      String[] itemNames = { "something" };
      MetaValue[] itemValues = { SimpleValueSupport.wrap("Hello") };
      CompositeValue helloValue = new CompositeValueSupport(compositeType, itemNames, itemValues);
      itemValues = new MetaValue[] { SimpleValueSupport.wrap("Goodbye") };
      CompositeValue goodbyeValue = new CompositeValueSupport(compositeType, itemNames, itemValues);
      MetaValue[] metaArray = { helloValue, goodbyeValue };
      CollectionValueSupport expected = new CollectionValueSupport(arrayType, metaArray);
      
      MetaValue result = createMetaValue(collection, type);
      CollectionValue actual = assertInstanceOf(result, CollectionValue.class);
      getLog().debug("Collection Value: " + actual);
      assertEquals(expected, actual);
   }
}
