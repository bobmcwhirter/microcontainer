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
package org.jboss.test.metatype.types.factory.test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import junit.framework.Test;

import org.jboss.metatype.api.types.ArrayMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.test.metatype.types.factory.support.TestSimpleComposite;

/**
 * CollectionMetaTypeFactoryUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class CollectionMetaTypeFactoryUnitTestCase extends AbstractMetaTypeFactoryTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(CollectionMetaTypeFactoryUnitTestCase.class);
   }
   
   /**
    * Create a new CollectionMetaTypeFactoryUnitTestCase.
    * 
    * @param name the test name
    */
   public CollectionMetaTypeFactoryUnitTestCase(String name)
   {
      super(name);
   }

   /**
    * Signature method for a simple collection
    * 
    * @return the signature
    */
   public Collection<String> simpleCollection()
   {
      return null;
   }

   /**
    * Signature method for a composite collection
    * 
    * @return the signature
    */
   public static Collection<TestSimpleComposite> simpleCompositeCollection()
   {
      return null;
   }

   /**
    * Signature method for a list
    * 
    * @return the signature
    */
   public List<String> simpleList()
   {
      return null;
   }

   /**
    * Signature method for a composite list
    * 
    * @return the signature
    */
   public static List<TestSimpleComposite> simpleCompositeList()
   {
      return null;
   }

   /**
    * Signature method for a composite set
    * 
    * @return the signature
    */
   public Set<String> simpleSet()
   {
      return null;
   }

   /**
    * Signature method for a composite set
    * 
    * @return the signature
    */
   public static Set<TestSimpleComposite> simpleCompositeSet()
   {
      return null;
   }

   /**
    * Test the correct meta type is generated for a simple collection
    * 
    * @throws Exception for any problem
    */
   public void testSimpleCollection() throws Exception
   {
      testCollection("simpleCollection", String.class);
   }

   /**
    * Test the correct meta type is generated for a composite collection
    * 
    * @throws Exception for any problem
    */
   public void testSimpleCompositeCollection() throws Exception
   {
      testCollection("simpleCompositeCollection", TestSimpleComposite.class);
   }

   /**
    * Test the correct meta type is generated for a simple list
    * 
    * @throws Exception for any problem
    */
   public void testSimpleList() throws Exception
   {
      testCollection("simpleList", String.class);
   }

   /**
    * Test the correct meta type is generated for a composite list
    * 
    * @throws Exception for any problem
    */
   public void testSimpleCompositeList() throws Exception
   {
      testCollection("simpleCompositeList", TestSimpleComposite.class);
   }

   /**
    * Test the correct meta type is generated for a simple set
    * 
    * @throws Exception for any problem
    */
   public void testSimpleSet() throws Exception
   {
      testCollection("simpleSet", String.class);
   }

   /**
    * Test the correct meta type is generated for a composite set
    * 
    * @throws Exception for any problem
    */
   public void testSimpleCompositeSet() throws Exception
   {
      testCollection("simpleCompositeSet", TestSimpleComposite.class);
   }
   
   /**
    * Test the correct meta type is generated for a collection
    * 
    * @param methodName the method name to lookup the connection signature
    * @param elementClass the expected element type of the collection
    * @throws Exception for any problem
    */
   protected void testCollection(String methodName, Type elementClass) throws Exception
   {
      Method method = getClass().getMethod(methodName, null);
      Type collectionType = method.getGenericReturnType();
      MetaType result = resolve(collectionType);
      ArrayMetaType actual = assertInstanceOf(result, ArrayMetaType.class);
      MetaType elementType = resolve(elementClass);
      ArrayMetaType expected = new ArrayMetaType(1, elementType);
      testArray(expected, actual);
   }
}
