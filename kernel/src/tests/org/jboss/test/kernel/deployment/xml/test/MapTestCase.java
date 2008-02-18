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
package org.jboss.test.kernel.deployment.xml.test;

import java.util.Set;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractMapMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;

/**
 * MapTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class MapTestCase extends AbstractXMLTest
{
   protected AbstractMapMetaData getMap(String name) throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean(name);
      Set<PropertyMetaData> properties = bean.getProperties();
      assertNotNull(properties);
      assertEquals(1, properties.size());
      PropertyMetaData property = properties.iterator().next();
      assertNotNull(property);
      ValueMetaData value = property.getValue();
      assertNotNull(property);
      assertTrue(value instanceof AbstractMapMetaData);
      return (AbstractMapMetaData) value;
   }
   
   public void testMap() throws Exception
   {
      AbstractMapMetaData map = getMap("Map.xml");
      assertNull(map.getType());
      assertNull(map.getKeyType());
      assertNull(map.getValueType());
   }
   
   public void testMapWithClass() throws Exception
   {
      AbstractMapMetaData map = getMap("MapWithClass.xml");
      assertEquals("MapClass", map.getType());
      assertNull(map.getKeyType());
      assertNull(map.getValueType());
   }
   
   public void testMapWithKeyClass() throws Exception
   {
      AbstractMapMetaData map = getMap("MapWithKeyClass.xml");
      assertNull(map.getType());
      assertEquals("KeyClass", map.getKeyType());
      assertNull(map.getValueType());
   }
   
   public void testMapWithValueClass() throws Exception
   {
      AbstractMapMetaData map = getMap("MapWithValueClass.xml");
      assertNull(map.getType());
      assertNull(map.getKeyType());
      assertEquals("ValueClass", map.getValueType());
   }
   
   public void testMapWithValue() throws Exception
   {
      AbstractMapMetaData map = getMap("MapWithValue.xml");
      assertNull(map.getType());
      assertNull(map.getKeyType());
      assertNull(map.getValueType());
      assertValue("Key", getKey(map));
      assertValue("Value", getValue(map));
   }
   
   public void testMapWithInjection() throws Exception
   {
      AbstractMapMetaData map = getMap("MapWithInjection.xml");
      assertNull(map.getType());
      assertNull(map.getKeyType());
      assertNull(map.getValueType());
      assertInjection(getKey(map));
      assertInjection(getValue(map));
   }
   
   public void testMapWithCollection() throws Exception
   {
      AbstractMapMetaData map = getMap("MapWithCollection.xml");
      assertNull(map.getType());
      assertNull(map.getKeyType());
      assertNull(map.getValueType());
      assertCollection(getKey(map));
      assertCollection(getValue(map));
   }
   
   public void testMapWithList() throws Exception
   {
      AbstractMapMetaData map = getMap("MapWithList.xml");
      assertNull(map.getType());
      assertNull(map.getKeyType());
      assertNull(map.getValueType());
      assertList(getKey(map));
      assertList(getValue(map));
   }
   
   public void testMapWithSet() throws Exception
   {
      AbstractMapMetaData map = getMap("MapWithSet.xml");
      assertNull(map.getType());
      assertNull(map.getKeyType());
      assertNull(map.getValueType());
      assertSet(getKey(map));
      assertSet(getValue(map));
   }
   
   public void testMapWithArray() throws Exception
   {
      AbstractMapMetaData map = getMap("MapWithArray.xml");
      assertNull(map.getType());
      assertNull(map.getKeyType());
      assertNull(map.getValueType());
      assertArray(getKey(map));
      assertArray(getValue(map));
   }
   
   public void testMapWithMap() throws Exception
   {
      AbstractMapMetaData map = getMap("MapWithMap.xml");
      assertNull(map.getType());
      assertNull(map.getKeyType());
      assertNull(map.getValueType());
      assertMap(getKey(map));
      assertMap(getValue(map));
   }
   
   public void testMapWithNull() throws Exception
   {
      AbstractMapMetaData map = getMap("MapWithNull.xml");
      assertNull(map.getType());
      assertNull(map.getKeyType());
      assertNull(map.getValueType());
      assertNullValue(getKey(map));
      assertNullValue(getValue(map));
   }
   
   public void testMapWithThis() throws Exception
   {
      AbstractMapMetaData map = getMap("MapWithThis.xml");
      assertNull(map.getType());
      assertNull(map.getKeyType());
      assertNull(map.getValueType());
      assertThis(getKey(map));
      assertThis(getValue(map));
   }
   
   public void testMapWithWildcard() throws Exception
   {
      AbstractMapMetaData map = getMap("MapWithWildcard.xml");
      assertNull(map.getType());
      assertNull(map.getKeyType());
      assertNull(map.getValueType());
      assertWildcard(getKey(map));
      assertWildcard(getValue(map));
   }
   
   public void testMapWithBean() throws Exception
   {
      AbstractMapMetaData map = getMap("MapWithBean.xml");
      assertNull(map.getType());
      assertNull(map.getKeyType());
      assertNull(map.getValueType());
      assertBean(getKey(map));
      assertBean(getValue(map));
   }

   protected ValueMetaData getKey(AbstractMapMetaData map)
   {
      assertEquals(1, map.size());
      return (ValueMetaData) map.keySet().iterator().next();
   }
   
   protected ValueMetaData getValue(AbstractMapMetaData map)
   {
      assertEquals(1, map.size());
      return (ValueMetaData) map.values().iterator().next();
   }
   
   public static Test suite()
   {
      return suite(MapTestCase.class);
   }

   public MapTestCase(String name)
   {
      super(name);
   }
}
