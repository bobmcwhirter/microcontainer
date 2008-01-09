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

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.test.kernel.deployment.xml.support.Annotation1;
import org.jboss.test.kernel.deployment.xml.support.Annotation2;
import org.jboss.test.kernel.deployment.xml.support.Annotation3;

/**
 * PropertyJaxbTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 56471 $
 */
public class PropertyJaxbTestCase extends AbstractMCTest
{
   protected PropertyMetaData getProperty() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      Set properties = bean.getProperties();
      assertNotNull(properties);
      assertEquals(1, properties.size());
      PropertyMetaData property = (PropertyMetaData) properties.iterator().next();
      assertNotNull(property);
      return property;
   }
   
   public void testProperty() throws Exception
   {
      PropertyMetaData property = getProperty();
      assertNotNull("PropertyName", property.getName());
      assertNull(property.getAnnotations());
      assertNull(property.getValue());
   }
   
   public void testPropertyWithBean() throws Exception
   {
      PropertyMetaData property = getProperty();
      assertNotNull("PropertyName", property.getName());
      assertNull(property.getAnnotations());
      ValueMetaData value = property.getValue();
      assertBean(value);
   }
   
   public void testPropertyWithClass() throws Exception
   {
      PropertyMetaData property = getProperty();
      assertNotNull("PropertyName", property.getName());
      assertNull(property.getAnnotations());
      ValueMetaData value = property.getValue();
      assertNotNull(value);
      assertTrue(value instanceof StringValueMetaData);
      StringValueMetaData string = (StringValueMetaData) value;
      assertEquals("PropertyClass", string.getType());
   }

   public void testPropertyWithAnnotation() throws Exception
   {
      PropertyMetaData property = getProperty();
      assertNotNull("PropertyName", property.getName());
      HashSet<String> expected = new HashSet<String>();
      expected.add(Annotation1.class.getName());
      assertAnnotations(expected, property.getAnnotations());
      assertNull(property.getValue());
   }
   
   public void testPropertyWithAnnotations() throws Exception
   {
      PropertyMetaData property = getProperty();
      assertNotNull("PropertyName", property.getName());
      HashSet<String> expected = new HashSet<String>();
      expected.add(Annotation1.class.getName());
      expected.add(Annotation2.class.getName());
      expected.add(Annotation3.class.getName());
      assertAnnotations(expected, property.getAnnotations());
      assertNull(property.getValue());
   }
   
   public void testPropertyWithPlainValue() throws Exception
   {
      PropertyMetaData property = getProperty();
      assertNotNull("PropertyName", property.getName());
      assertNull(property.getAnnotations());
      assertPlainValue("PlainValue", property.getValue());
   }
   
   public void testPropertyWithValue() throws Exception
   {
      PropertyMetaData property = getProperty();
      assertNotNull("PropertyName", property.getName());
      assertNull(property.getAnnotations());
      assertValue("Value", property.getValue());
   }
   
   public void testPropertyWithInjection() throws Exception
   {
      PropertyMetaData property = getProperty();
      assertNotNull("PropertyName", property.getName());
      assertNull(property.getAnnotations());
      assertInjection(property.getValue());
   }
   
   public void testPropertyWithCollection() throws Exception
   {
      PropertyMetaData property = getProperty();
      assertNotNull("PropertyName", property.getName());
      assertNull(property.getAnnotations());
      assertCollection(property.getValue());
   }
   
   public void testPropertyWithList() throws Exception
   {
      PropertyMetaData property = getProperty();
      assertNotNull("PropertyName", property.getName());
      assertNull(property.getAnnotations());
      assertList(property.getValue());
   }
   
   public void testPropertyWithSet() throws Exception
   {
      PropertyMetaData property = getProperty();
      assertNotNull("PropertyName", property.getName());
      assertNull(property.getAnnotations());
      assertSet(property.getValue());
   }
   
   public void testPropertyWithArray() throws Exception
   {
      PropertyMetaData property = getProperty();
      assertNotNull("PropertyName", property.getName());
      assertNull(property.getAnnotations());
      assertArray(property.getValue());
   }
   
   public void testPropertyWithMap() throws Exception
   {
      PropertyMetaData property = getProperty();
      assertNotNull("PropertyName", property.getName());
      assertNull(property.getAnnotations());
      assertMap(property.getValue());
   }
   
   public void testPropertyWithThis() throws Exception
   {
      PropertyMetaData property = getProperty();
      assertNotNull("PropertyName", property.getName());
      assertNull(property.getAnnotations());
      assertThis(property.getValue());
   }
   
   public void testPropertyWithWildcard() throws Exception
   {
      PropertyMetaData property = getProperty();
      assertNotNull("PropertyName", property.getName());
      assertNull(property.getAnnotations());
      assertWildcard(property.getValue());
   }

   /* TODO
   public void testPropertyBadNoName() throws Exception
   {
      try
      {
         unmarshalBean("PropertyBadNoName.xml");
         fail("Should not be here");
      }
      catch (Exception expected)
      {
         checkJBossXBException(IllegalArgumentException.class, expected);
      }
   } */

   public static Test suite()
   {
      return PropertyJaxbTestCase.suite(PropertyJaxbTestCase.class);
   }

   public PropertyJaxbTestCase(String name)
   {
      super(name);
   }
}
