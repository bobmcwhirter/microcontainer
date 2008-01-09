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
import org.jboss.beans.metadata.plugins.AbstractArrayMetaData;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;

/**
 * ArrayTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class ArrayTestCase extends AbstractXMLTest
{
   protected AbstractArrayMetaData getCollection(String name) throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean(name);
      Set properties = bean.getProperties();
      assertNotNull(properties);
      assertEquals(1, properties.size());
      PropertyMetaData property = (PropertyMetaData) properties.iterator().next();
      assertNotNull(property);
      ValueMetaData value = property.getValue();
      assertNotNull(property);
      assertTrue(value instanceof AbstractArrayMetaData);
      return (AbstractArrayMetaData) value;
   }
   
   public void testArray() throws Exception
   {
      AbstractArrayMetaData collection = getCollection("Array.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
   }
   
   public void testArrayWithClass() throws Exception
   {
      AbstractArrayMetaData collection = getCollection("ArrayWithClass.xml");
      assertEquals("CollectionClass", collection.getType());
      assertNull(collection.getElementType());
   }
   
   public void testArrayWithElementClass() throws Exception
   {
      AbstractArrayMetaData collection = getCollection("ArrayWithElementClass.xml");
      assertNull(collection.getType());
      assertEquals("ElementClass", collection.getElementType());
   }
   
   public void testArrayWithValue() throws Exception
   {
      AbstractArrayMetaData collection = getCollection("ArrayWithValue.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertValue("Value", getValue(collection));
   }
   
   public void testArrayWithInjection() throws Exception
   {
      AbstractArrayMetaData collection = getCollection("ArrayWithInjection.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertInjection(getValue(collection));
   }
   
   public void testArrayWithCollection() throws Exception
   {
      AbstractArrayMetaData collection = getCollection("ArrayWithCollection.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertCollection(getValue(collection));
   }
   
   public void testArrayWithList() throws Exception
   {
      AbstractArrayMetaData collection = getCollection("ArrayWithList.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertList(getValue(collection));
   }
   
   public void testArrayWithSet() throws Exception
   {
      AbstractArrayMetaData collection = getCollection("ArrayWithSet.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertSet(getValue(collection));
   }
   
   public void testArrayWithArray() throws Exception
   {
      AbstractArrayMetaData collection = getCollection("ArrayWithArray.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertArray(getValue(collection));
   }
   
   public void testArrayWithMap() throws Exception
   {
      AbstractArrayMetaData collection = getCollection("ArrayWithMap.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertMap(getValue(collection));
   }
   
   public void testArrayWithNull() throws Exception
   {
      AbstractArrayMetaData collection = getCollection("ArrayWithNull.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertNullValue(getValue(collection));
   }
   
   public void testArrayWithThis() throws Exception
   {
      AbstractArrayMetaData collection = getCollection("ArrayWithThis.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertThis(getValue(collection));
   }
   
   public void testArrayWithWildcard() throws Exception
   {
      AbstractArrayMetaData collection = getCollection("ArrayWithWildcard.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertWildcard(getValue(collection));
   }
   
   public void testArrayWithBean() throws Exception
   {
      AbstractArrayMetaData collection = getCollection("ArrayWithBean.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertBean(getValue(collection));
   }

   protected ValueMetaData getValue(AbstractArrayMetaData collection)
   {
      assertEquals(1, collection.size());
      return (ValueMetaData) collection.iterator().next();
   }
   
   public static Test suite()
   {
      return suite(ArrayTestCase.class);
   }

   public ArrayTestCase(String name)
   {
      super(name);
   }
}
