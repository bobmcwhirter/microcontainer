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
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.Test;

import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.types.ImmutableCompositeMetaType;
import org.jboss.metatype.api.types.ImmutableTableMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.api.types.TableMetaType;
import org.jboss.metatype.api.values.CompositeValue;
import org.jboss.metatype.api.values.CompositeValueSupport;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.SimpleValueSupport;
import org.jboss.metatype.api.values.TableValue;
import org.jboss.metatype.api.values.TableValueSupport;
import org.jboss.metatype.plugins.types.DefaultMetaTypeFactory;
import org.jboss.metatype.plugins.types.MutableCompositeMetaType;
import org.jboss.test.metatype.values.factory.support.TestSimpleComposite;

/**
 * MapValueFactoryUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MapValueFactoryUnitTestCase extends AbstractMetaValueFactoryTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(MapValueFactoryUnitTestCase.class);
   }
   
   /**
    * Create a new MapValueFactoryUnitTestCase.
    * 
    * @param name the test name
    */
   public MapValueFactoryUnitTestCase(String name)
   {
      super(name);
   }

   /**
    * The signature and value to test a simple map
    * 
    * @return the value
    */
   public Map<String, Integer> simpleMap()
   {
      Map<String, Integer> result = new LinkedHashMap<String, Integer>();
      result.put("Hello", new Integer(1));
      result.put("Goodbye", new Integer(2));
      return result;
   }

   /**
    * The signature and value to test a map with a composite key
    * 
    * @return the value
    */
   public Map<TestSimpleComposite, Integer> compositeKeyMap()
   {
      Map<TestSimpleComposite, Integer> result = new LinkedHashMap<TestSimpleComposite, Integer>();
      result.put(new TestSimpleComposite("Hello"), new Integer(1));
      result.put(new TestSimpleComposite("Goodbye"), new Integer(2));
      return result;
   }

   /**
    * The signature and value to test a map with a composite value
    * 
    * @return the value
    */
   public Map<String, TestSimpleComposite> compositeValueMap()
   {
      Map<String, TestSimpleComposite> result = new LinkedHashMap<String, TestSimpleComposite>();
      result.put("Hello", new TestSimpleComposite("Hello"));
      result.put("Goodbye", new TestSimpleComposite("Goodbye"));
      return result;
   }

   /**
    * Test a simple map
    * 
    * @throws Exception for any problem
    */
   public void testSimpleMap() throws Exception
   {
      Method method = getClass().getMethod("simpleMap", null);
      Type collectionType = method.getGenericReturnType();
      Map<String, Integer> map = simpleMap();
      
      MetaType<?> keyType = resolve(String.class);
      MetaType<?> valueType = resolve(Integer.class);
      MetaType<?>[] itemTypes = { keyType, valueType };
      String entryName = Map.Entry.class.getName();
      CompositeMetaType entryType = new ImmutableCompositeMetaType(entryName, entryName, DefaultMetaTypeFactory.MAP_ITEM_NAMES, DefaultMetaTypeFactory.MAP_ITEM_NAMES, itemTypes);
      TableMetaType tableType = new ImmutableTableMetaType(Map.class.getName(), Map.class.getName(), entryType, DefaultMetaTypeFactory.MAP_INDEX_NAMES);
      
      TableValue expected = new TableValueSupport(tableType);
      String[] itemNames = DefaultMetaTypeFactory.MAP_ITEM_NAMES;
      
      MetaValue[] itemValues = new MetaValue[] { SimpleValueSupport.wrap("Hello"), SimpleValueSupport.wrap(new Integer(1)) };
      expected.put(new CompositeValueSupport(entryType, itemNames, itemValues));

      itemValues = new MetaValue[] { SimpleValueSupport.wrap("Goodbye"), SimpleValueSupport.wrap(new Integer(2)) };
      expected.put(new CompositeValueSupport(entryType, itemNames, itemValues));

      MetaValue result = createMetaValue(map, collectionType);
      TableValue actual = assertInstanceOf(result, TableValue.class);
      
      getLog().debug("Map Value: " + actual);
      assertEquals(expected, actual);
   }

   /**
    * Test a composite key map
    * 
    * @throws Exception for any problem
    */
   public void testCompositeKeyMap() throws Exception
   {
      Method method = getClass().getMethod("compositeKeyMap", null);
      Type collectionType = method.getGenericReturnType();
      Map<TestSimpleComposite, Integer> map = compositeKeyMap();
      
      MetaType<?> keyType = resolve(TestSimpleComposite.class);
      MetaType<?> valueType = resolve(Integer.class);
      MetaType<?>[] itemTypes = { keyType, valueType };
      String entryName = Map.Entry.class.getName();
      CompositeMetaType entryType = new ImmutableCompositeMetaType(entryName, entryName, DefaultMetaTypeFactory.MAP_ITEM_NAMES, DefaultMetaTypeFactory.MAP_ITEM_NAMES, itemTypes);
      TableMetaType tableType = new ImmutableTableMetaType(Map.class.getName(), Map.class.getName(), entryType, DefaultMetaTypeFactory.MAP_INDEX_NAMES);
      
      TableValue expected = new TableValueSupport(tableType);
      String[] itemNames = DefaultMetaTypeFactory.MAP_ITEM_NAMES;
      
      MutableCompositeMetaType compositeType = new MutableCompositeMetaType(TestSimpleComposite.class.getName(), TestSimpleComposite.class.getName());
      compositeType.addItem("something", "something", SimpleMetaType.STRING);
      compositeType.freeze();

      String[] compositeNames = { "something" };
      CompositeValue compositeValue = new CompositeValueSupport(compositeType, compositeNames, new MetaValue[] { SimpleValueSupport.wrap("Hello") });
      
      MetaValue[] itemValues = new MetaValue[] { compositeValue, SimpleValueSupport.wrap(new Integer(1)) };
      expected.put(new CompositeValueSupport(entryType, itemNames, itemValues));

      compositeValue = new CompositeValueSupport(compositeType, compositeNames, new MetaValue[] { SimpleValueSupport.wrap("Goodbye") });
      itemValues = new MetaValue[] { compositeValue, SimpleValueSupport.wrap(new Integer(2)) };
      expected.put(new CompositeValueSupport(entryType, itemNames, itemValues));

      MetaValue result = createMetaValue(map, collectionType);
      TableValue actual = assertInstanceOf(result, TableValue.class);
      
      getLog().debug("Map Value: " + actual);
      assertEquals(expected, actual);
   }

   /**
    * Test a composite value map
    * 
    * @throws Exception for any problem
    */
   public void testCompositeValueMap() throws Exception
   {
      Method method = getClass().getMethod("compositeValueMap", null);
      Type collectionType = method.getGenericReturnType();
      Map<String, TestSimpleComposite> map = compositeValueMap();
      
      MetaType<?> keyType = resolve(String.class);
      MetaType<?> valueType = resolve(TestSimpleComposite.class);
      MetaType<?>[] itemTypes = { keyType, valueType };
      String entryName = Map.Entry.class.getName();
      CompositeMetaType entryType = new ImmutableCompositeMetaType(entryName, entryName, DefaultMetaTypeFactory.MAP_ITEM_NAMES, DefaultMetaTypeFactory.MAP_ITEM_NAMES, itemTypes);
      TableMetaType tableType = new ImmutableTableMetaType(Map.class.getName(), Map.class.getName(), entryType, DefaultMetaTypeFactory.MAP_INDEX_NAMES);
      
      TableValue expected = new TableValueSupport(tableType);
      String[] itemNames = DefaultMetaTypeFactory.MAP_ITEM_NAMES;
      
      MutableCompositeMetaType compositeType = new MutableCompositeMetaType(TestSimpleComposite.class.getName(), TestSimpleComposite.class.getName());
      compositeType.addItem("something", "something", SimpleMetaType.STRING);
      compositeType.freeze();

      String[] compositeNames = { "something" };
      CompositeValue compositeValue = new CompositeValueSupport(compositeType, compositeNames, new MetaValue[] { SimpleValueSupport.wrap("Hello") });
      
      MetaValue[] itemValues = new MetaValue[] { SimpleValueSupport.wrap("Hello"), compositeValue };
      expected.put(new CompositeValueSupport(entryType, itemNames, itemValues));

      compositeValue = new CompositeValueSupport(compositeType, compositeNames, new MetaValue[] { SimpleValueSupport.wrap("Goodbye") });
      itemValues = new MetaValue[] { SimpleValueSupport.wrap("Goodbye"), compositeValue };
      expected.put(new CompositeValueSupport(entryType, itemNames, itemValues));

      MetaValue result = createMetaValue(map, collectionType);
      TableValue actual = assertInstanceOf(result, TableValue.class);
      
      getLog().debug("Map Value: " + actual);
      assertEquals(expected, actual);
   }
}
