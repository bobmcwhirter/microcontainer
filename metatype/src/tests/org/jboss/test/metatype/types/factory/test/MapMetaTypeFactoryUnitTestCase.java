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
import java.util.Map;

import junit.framework.Test;

import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.types.ImmutableCompositeMetaType;
import org.jboss.metatype.api.types.ImmutableTableMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.TableMetaType;
import org.jboss.metatype.plugins.types.DefaultMetaTypeFactory;
import org.jboss.test.metatype.types.factory.support.TestSimpleComposite;

/**
 * MapMetaTypeFactoryUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MapMetaTypeFactoryUnitTestCase extends AbstractMetaTypeFactoryTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(MapMetaTypeFactoryUnitTestCase.class);
   }
   
   /**
    * Create a new MapMetaTypeFactoryUnitTestCase.
    * 
    * @param name the test name
    */
   public MapMetaTypeFactoryUnitTestCase(String name)
   {
      super(name);
   }

   /**
    * Signature method for a simple map
    * 
    * @return the signature
    */
   public Map<String, Integer> simpleMap()
   {
      return null;
   }

   /**
    * Signature method for a map with a composite key
    * 
    * @return the signature
    */
   public Map<TestSimpleComposite, Integer> compositeKeyMap()
   {
      return null;
   }

   /**
    * Signature method for a map with a composite value
    * 
    * @return the signature
    */
   public Map<String, TestSimpleComposite> compositeValueMap()
   {
      return null;
   }

   /**
    * Test the correct meta type is generated for a simple map
    * 
    * @throws Exception for any problem
    */
   public void testSimpleMap() throws Exception
   {
      testMap("simpleMap", String.class, Integer.class);
   }

   /**
    * Test the correct meta type is generated for a map with a composite key
    * 
    * @throws Exception for any problem
    */
   public void testCompositeKeyMap() throws Exception
   {
      testMap("compositeKeyMap", TestSimpleComposite.class, Integer.class);
   }

   /**
    * Test the correct meta type is generated for a map with a composite value
    * 
    * @throws Exception for any problem
    */
   public void testCompositeValueMap() throws Exception
   {
      testMap("compositeValueMap", String.class, TestSimpleComposite.class);
   }
   
   /**
    * Test the correct meta type is generated for a map
    * 
    * @param methodName the method name to lookup the connection signature
    * @param keyClass the expected key type
    * @param valueClass the expected value type
    * @throws Exception for any problem
    */
   protected void testMap(String methodName, Type keyClass, Type valueClass) throws Exception
   {
      Method method = getClass().getMethod(methodName, null);
      Type collectionType = method.getGenericReturnType();
      MetaType<?> result = resolve(collectionType);
      TableMetaType actual = assertInstanceOf(result, TableMetaType.class);
      MetaType<?> keyType = resolve(keyClass);
      MetaType<?> valueType = resolve(valueClass);
      MetaType<?>[] itemTypes = { keyType, valueType };
      String entryName = Map.Entry.class.getName();
      CompositeMetaType entryType = new ImmutableCompositeMetaType(entryName, entryName, DefaultMetaTypeFactory.MAP_ITEM_NAMES, DefaultMetaTypeFactory.MAP_ITEM_NAMES, itemTypes);
      TableMetaType expected = new ImmutableTableMetaType(Map.class.getName(), Map.class.getName(), entryType, DefaultMetaTypeFactory.MAP_INDEX_NAMES);
      testTable(expected, actual);
   }
}
