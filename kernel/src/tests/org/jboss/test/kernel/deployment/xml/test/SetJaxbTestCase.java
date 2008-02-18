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
import org.jboss.beans.metadata.plugins.AbstractSetMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;

/**
 * SetJaxbTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 40781 $
 */
public class SetJaxbTestCase extends AbstractMCTest
{
   protected AbstractSetMetaData getCollection() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      Set<PropertyMetaData> properties = bean.getProperties();
      assertNotNull(properties);
      assertEquals(1, properties.size());
      PropertyMetaData property = properties.iterator().next();
      assertNotNull(property);
      ValueMetaData value = property.getValue();
      assertNotNull(property);
      assertTrue(value instanceof AbstractSetMetaData);
      return (AbstractSetMetaData) value;
   }
   
   public void testSet() throws Exception
   {
      AbstractSetMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
   }
   
   public void testSetWithClass() throws Exception
   {
      AbstractSetMetaData collection = getCollection();
      assertEquals("CollectionClass", collection.getType());
      assertNull(collection.getElementType());
   }
   
   public void testSetWithElementClass() throws Exception
   {
      AbstractSetMetaData collection = getCollection();
      assertNull(collection.getType());
      assertEquals("ElementClass", collection.getElementType());
   }
   
   public void testSetWithValue() throws Exception
   {
      AbstractSetMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertValue("Value", getValue(collection));
   }
   
   public void testSetWithInjection() throws Exception
   {
      AbstractSetMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertInjection(getValue(collection));
   }
   
   public void testSetWithCollection() throws Exception
   {
      AbstractSetMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertCollection(getValue(collection));
   }
   
   public void testSetWithList() throws Exception
   {
      AbstractSetMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertList(getValue(collection));
   }
   
   public void testSetWithSet() throws Exception
   {
      AbstractSetMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertSet(getValue(collection));
   }
   
   public void testSetWithArray() throws Exception
   {
      AbstractSetMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertArray(getValue(collection));
   }
   
   public void testSetWithMap() throws Exception
   {
      AbstractSetMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertMap(getValue(collection));
   }
   
   public void testSetWithNull() throws Exception
   {
      AbstractSetMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertNullValue(getValue(collection));
   }
   
   public void testSetWithThis() throws Exception
   {
      AbstractSetMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertThis(getValue(collection));
   }
   
   public void testSetWithWildcard() throws Exception
   {
      AbstractSetMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertWildcard(getValue(collection));
   }
   
   public void testSetWithBean() throws Exception
   {
      AbstractSetMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertBean(getValue(collection));
   }

   protected ValueMetaData getValue(AbstractSetMetaData collection)
   {
      assertEquals(1, collection.size());
      return (ValueMetaData) collection.iterator().next();
   }
   
   public static Test suite()
   {
      return SetJaxbTestCase.suite(SetJaxbTestCase.class);
   }

   public SetJaxbTestCase(String name)
   {
      super(name);
   }
}
