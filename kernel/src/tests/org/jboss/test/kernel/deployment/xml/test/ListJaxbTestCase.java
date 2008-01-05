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
import org.jboss.beans.metadata.plugins.AbstractListMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;

/**
 * ListJaxbTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 40781 $
 */
public class ListJaxbTestCase extends AbstractMCTest
{
   protected AbstractListMetaData getCollection() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      Set properties = bean.getProperties();
      assertNotNull(properties);
      assertEquals(1, properties.size());
      PropertyMetaData property = (PropertyMetaData) properties.iterator().next();
      assertNotNull(property);
      ValueMetaData value = property.getValue();
      assertNotNull(property);
      assertTrue(value instanceof AbstractListMetaData);
      return (AbstractListMetaData) value;
   }
   
   public void testList() throws Exception
   {
      AbstractListMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
   }
   
   public void testListWithClass() throws Exception
   {
      AbstractListMetaData collection = getCollection();
      assertEquals("CollectionClass", collection.getType());
      assertNull(collection.getElementType());
   }
   
   public void testListWithElementClass() throws Exception
   {
      AbstractListMetaData collection = getCollection();
      assertNull(collection.getType());
      assertEquals("ElementClass", collection.getElementType());
   }
   
   public void testListWithValue() throws Exception
   {
      AbstractListMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertValue("Value", getValue(collection));
   }
   
   public void testListWithInjection() throws Exception
   {
      AbstractListMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertInjection(getValue(collection));
   }

   public void testListWithCollection() throws Exception
   {
      AbstractListMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertCollection(getValue(collection));
   }
   
   public void testListWithList() throws Exception
   {
      AbstractListMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertList(getValue(collection));
   }
   
   public void testListWithSet() throws Exception
   {
      AbstractListMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertSet(getValue(collection));
   }
   
   public void testListWithArray() throws Exception
   {
      AbstractListMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertArray(getValue(collection));
   }
   
   public void testListWithMap() throws Exception
   {
      AbstractListMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertMap(getValue(collection));
   }
   
   public void testListWithNull() throws Exception
   {
      AbstractListMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertNullValue(getValue(collection));
   }
   
   public void testListWithThis() throws Exception
   {
      AbstractListMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertThis(getValue(collection));
   }
   
   public void testListWithWildcard() throws Exception
   {
      AbstractListMetaData collection = getCollection();
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertWildcard(getValue(collection));
   }
   
   protected ValueMetaData getValue(AbstractListMetaData collection)
   {
      assertEquals(1, collection.size());
      return (ValueMetaData) collection.iterator().next();
   }
   
   public static Test suite()
   {
      return ListJaxbTestCase.suite(ListJaxbTestCase.class);
   }

   public ListJaxbTestCase(String name)
   {
      super(name);
   }
}
