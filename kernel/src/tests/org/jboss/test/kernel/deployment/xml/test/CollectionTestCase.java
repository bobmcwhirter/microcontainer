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
import org.jboss.beans.metadata.plugins.AbstractCollectionMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;

/**
 * CollectionTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class CollectionTestCase extends AbstractXMLTest
{
   protected AbstractCollectionMetaData getCollection(String name) throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean(name);
      Set<PropertyMetaData> properties = bean.getProperties();
      assertNotNull(properties);
      assertEquals(1, properties.size());
      PropertyMetaData property = properties.iterator().next();
      assertNotNull(property);
      ValueMetaData value = property.getValue();
      assertNotNull(property);
      assertTrue(value instanceof AbstractCollectionMetaData);
      return (AbstractCollectionMetaData) value;
   }
   
   public void testCollection() throws Exception
   {
      AbstractCollectionMetaData collection = getCollection("Collection.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
   }
   
   public void testCollectionWithClass() throws Exception
   {
      AbstractCollectionMetaData collection = getCollection("CollectionWithClass.xml");
      assertEquals("CollectionClass", collection.getType());
      assertNull(collection.getElementType());
   }
   
   public void testCollectionWithElementClass() throws Exception
   {
      AbstractCollectionMetaData collection = getCollection("CollectionWithElementClass.xml");
      assertNull(collection.getType());
      assertEquals("ElementClass", collection.getElementType());
   }
   
   public void testCollectionWithValue() throws Exception
   {
      AbstractCollectionMetaData collection = getCollection("CollectionWithValue.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertValue("Value", getValue(collection));
   }
   
   public void testCollectionWithInjection() throws Exception
   {
      AbstractCollectionMetaData collection = getCollection("CollectionWithInjection.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertInjection(getValue(collection));
   }
   
   public void testCollectionWithCollection() throws Exception
   {
      AbstractCollectionMetaData collection = getCollection("CollectionWithCollection.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertCollection(getValue(collection));
   }
   
   public void testCollectionWithList() throws Exception
   {
      AbstractCollectionMetaData collection = getCollection("CollectionWithList.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertList(getValue(collection));
   }
   
   public void testCollectionWithSet() throws Exception
   {
      AbstractCollectionMetaData collection = getCollection("CollectionWithSet.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertSet(getValue(collection));
   }
   
   public void testCollectionWithArray() throws Exception
   {
      AbstractCollectionMetaData collection = getCollection("CollectionWithArray.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertArray(getValue(collection));
   }
   
   public void testCollectionWithMap() throws Exception
   {
      AbstractCollectionMetaData collection = getCollection("CollectionWithMap.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertMap(getValue(collection));
   }
   
   public void testCollectionWithNull() throws Exception
   {
      AbstractCollectionMetaData collection = getCollection("CollectionWithNull.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertNullValue(getValue(collection));
   }
   
   public void testCollectionWithThis() throws Exception
   {
      AbstractCollectionMetaData collection = getCollection("CollectionWithThis.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertThis(getValue(collection));
   }
   
   public void testCollectionWithWildcard() throws Exception
   {
      AbstractCollectionMetaData collection = getCollection("CollectionWithWildcard.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertWildcard(getValue(collection));
   }
   
   public void testCollectionWithBean() throws Exception
   {
      AbstractCollectionMetaData collection = getCollection("CollectionWithBean.xml");
      assertNull(collection.getType());
      assertNull(collection.getElementType());
      assertBean(getValue(collection));
   }

   protected ValueMetaData getValue(AbstractCollectionMetaData collection)
   {
      assertEquals(1, collection.size());
      return (ValueMetaData) collection.iterator().next();
   }
   
   public static Test suite()
   {
      return suite(CollectionTestCase.class);
   }

   public CollectionTestCase(String name)
   {
      super(name);
   }
}
