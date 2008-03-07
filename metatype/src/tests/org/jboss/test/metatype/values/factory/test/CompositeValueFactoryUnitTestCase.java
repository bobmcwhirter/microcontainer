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

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import junit.framework.Test;

import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.api.values.CompositeValue;
import org.jboss.metatype.api.values.CompositeValueSupport;
import org.jboss.metatype.api.values.MapCompositeValueSupport;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.SimpleValueSupport;
import org.jboss.metatype.plugins.types.MutableCompositeMetaType;
import org.jboss.test.metatype.values.factory.support.TestIgnoredCompositeItem;
import org.jboss.test.metatype.values.factory.support.TestRecursiveComposite;
import org.jboss.test.metatype.values.factory.support.TestRenamedCompositeItem;
import org.jboss.test.metatype.values.factory.support.TestSimpleComposite;

/**
 * CompositeValueFactoryUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class CompositeValueFactoryUnitTestCase extends AbstractMetaValueFactoryTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(CompositeValueFactoryUnitTestCase.class);
   }
   
   /**
    * Create a new CompositeValueFactoryUnitTestCase.
    * 
    * @param name the test name
    */
   public CompositeValueFactoryUnitTestCase(String name)
   {
      super(name);
   }

   /**
    * Test the correct value is generated for a simple composite
    * 
    * @throws Exception for any problem
    */
   public void testSimpleComposite() throws Exception
   {
      MutableCompositeMetaType compositeType = new MutableCompositeMetaType(TestSimpleComposite.class.getName(), TestSimpleComposite.class.getName());
      compositeType.addItem("something", "something", SimpleMetaType.STRING);
      compositeType.freeze();

      String[] compositeNames = { "something" };
      CompositeValue expected = new CompositeValueSupport(compositeType, compositeNames, new MetaValue[] { SimpleValueSupport.wrap("Hello") });
      
      MetaValue result = createMetaValue(new TestSimpleComposite("Hello"));
      CompositeValue actual = assertInstanceOf(result, CompositeValue.class);
      getLog().debug("Composite Value: " + actual);
      assertEquals(expected, actual);
   }

   /**
    * Test the correct value is generated for a recursive composite
    * 
    * @throws Exception for any problem
    */
   public void testRecursiveComposite() throws Exception
   {
      MutableCompositeMetaType compositeType = new MutableCompositeMetaType(TestRecursiveComposite.class.getName(), TestRecursiveComposite.class.getName());
      compositeType.addItem("id", "id", SimpleMetaType.STRING);
      compositeType.addItem("other", "other", compositeType);
      Set<String> keys = Collections.singleton("id");
      compositeType.setKeys(keys);
      compositeType.freeze();

      CompositeValueSupport expected = new CompositeValueSupport(compositeType);
      expected.set("id", SimpleValueSupport.wrap("Hello"));
      expected.set("other", expected);
      
      TestRecursiveComposite object = new TestRecursiveComposite("Hello");
      object.setOther(object);
      MetaValue result = createMetaValue(object);
      CompositeValue actual = assertInstanceOf(result, CompositeValue.class);
      getLog().debug("Composite Value: " + actual);
      assertEquals(expected, actual);
   }

   public void testIgnoreItem() throws Exception
   {
      MutableCompositeMetaType compositeType = new MutableCompositeMetaType(TestIgnoredCompositeItem.class.getName(), TestIgnoredCompositeItem.class.getName());
      compositeType.addItem("id", "id", SimpleMetaType.STRING);
      Set<String> keys = Collections.singleton("id");
      compositeType.setKeys(keys);
      compositeType.freeze();

      CompositeValueSupport expected = new CompositeValueSupport(compositeType);
      expected.set("id", SimpleValueSupport.wrap("Hello"));
      
      TestIgnoredCompositeItem object = new TestIgnoredCompositeItem();
      object.setId("Hello");
      object.setIgnored("Ignored?");
      MetaValue result = createMetaValue(object);
      CompositeValue actual = assertInstanceOf(result, CompositeValue.class);
      getLog().debug("Composite Value: " + actual);
      assertEquals(expected, actual);
   }

   public void testRenameItem() throws Exception
   {
      MutableCompositeMetaType compositeType = new MutableCompositeMetaType(TestRenamedCompositeItem.class.getName(), TestRenamedCompositeItem.class.getName());
      compositeType.addItem("id", "id", SimpleMetaType.STRING);
      compositeType.addItem("renamed", "renamed", SimpleMetaType.STRING);
      Set<String> keys = Collections.singleton("id");
      compositeType.setKeys(keys);
      compositeType.freeze();

      CompositeValueSupport expected = new CompositeValueSupport(compositeType);
      expected.set("id", SimpleValueSupport.wrap("Hello"));
      expected.set("renamed", SimpleValueSupport.wrap("Renamed"));
      
      TestRenamedCompositeItem object = new TestRenamedCompositeItem();
      object.setId("Hello");
      object.setValue("Renamed");
      MetaValue result = createMetaValue(object);
      CompositeValue actual = assertInstanceOf(result, CompositeValue.class);
      getLog().debug("Composite Value: " + actual);
      assertEquals(expected, actual);
   }
   
   public HashMap<String, String> compositeSignature;
   /**
    * JBMICROCONT-238, Map<String,?> should map to a MapCompositeValueSupport(MetaValue<?>)
    * @throws Exception
    */
   public void testMapWithStringKeyComposite() throws Exception
   {
      Field field = getClass().getField("compositeSignature");
      Type mapSignature = field.getGenericType();
      
      Map<String, String> values = new HashMap<String, String>();
      values.put("key1", "value1");
      values.put("key2", "value2");
      values.put("key3", "value3");
      MapCompositeValueSupport expected = new MapCompositeValueSupport(SimpleMetaType.STRING);
      expected.put("key1", SimpleValueSupport.wrap("value1"));
      expected.put("key2", SimpleValueSupport.wrap("value2"));
      expected.put("key3", SimpleValueSupport.wrap("value3"));
      MetaValue result = createMetaValue(values, mapSignature);
      CompositeValue actual = assertInstanceOf(result, CompositeValue.class);
      getLog().debug("Composite Value: " + actual);
      assertEquals(expected, actual);

   }
   /**
    * JBMICROCONT-238, java.util.Properties should map to a MapCompositeValueSupport(MetaValue<String>)
    * @throws Exception
    */
   public void testPropertiesComposite()
      throws Exception
   {
      Type propertiesType = Properties.class;

      Properties values = new Properties();
      values.put("key1", "value1");
      values.put("key2", "value2");
      values.put("key3", "value3");

      MapCompositeValueSupport expected = new MapCompositeValueSupport(SimpleMetaType.STRING);
      expected.put("key1", SimpleValueSupport.wrap("value1"));
      expected.put("key2", SimpleValueSupport.wrap("value2"));
      expected.put("key3", SimpleValueSupport.wrap("value3"));
      MetaValue result = createMetaValue(values, propertiesType);
      CompositeValue actual = assertInstanceOf(result, CompositeValue.class);
      getLog().debug("Composite Value: " + actual);
      assertEquals(expected, actual);
   }
}
