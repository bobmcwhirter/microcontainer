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

import java.util.ArrayList;
import java.util.HashSet;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.test.kernel.deployment.xml.support.Annotation1;
import org.jboss.test.kernel.deployment.xml.support.Annotation2;
import org.jboss.test.kernel.deployment.xml.support.Annotation3;

/**
 * ConstructorJaxbTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 56471 $
 */
public class ConstructorJaxbTestCase extends AbstractMCTest
{
   protected ConstructorMetaData getConstructor() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      ConstructorMetaData constructor = bean.getConstructor();
      assertNotNull(constructor);
      return constructor;
   }

   public void testConstructor() throws Exception
   {
      ConstructorMetaData constructor = getConstructor();
      assertNull(constructor.getFactoryClass());
      assertNull(constructor.getFactoryMethod());
      assertNull(constructor.getAnnotations());
      assertNull(constructor.getFactory());
      assertNull(constructor.getParameters());
      assertNull(constructor.getValue());
   }

   public void testConstructorWithBean() throws Exception
   {
      ConstructorMetaData constructor = getConstructor();
      assertNull(constructor.getFactoryClass());
      assertNull(constructor.getFactoryMethod());
      assertNull(constructor.getAnnotations());
      assertNull(constructor.getFactory());
      assertNull(constructor.getParameters());
      assertBean(constructor.getValue());
   }

   public void testConstructorWithFactoryClass() throws Exception
   {
      ConstructorMetaData constructor = getConstructor();
      assertEquals("FactoryClass", constructor.getFactoryClass());
      assertEquals("Dummy", constructor.getFactoryMethod());
      assertNull(constructor.getAnnotations());
      assertNull(constructor.getFactory());
      assertNull(constructor.getParameters());
      assertNull(constructor.getValue());
   }

   public void testConstructorWithFactoryMethod() throws Exception
   {
      ConstructorMetaData constructor = getConstructor();
      assertEquals("Dummy", constructor.getFactoryClass());
      assertEquals("FactoryMethod", constructor.getFactoryMethod());
      assertNull(constructor.getAnnotations());
      assertNull(constructor.getFactory());
      assertNull(constructor.getParameters());
      assertNull(constructor.getValue());
   }

   public void testConstructorWithAnnotation() throws Exception
   {
      ConstructorMetaData constructor = getConstructor();
      assertNull(constructor.getFactoryClass());
      assertNull(constructor.getFactoryMethod());
      HashSet<String> expected = new HashSet<String>();
      expected.add(Annotation1.class.getName());
      assertAnnotations(expected, constructor.getAnnotations());
      assertNull(constructor.getFactory());
      assertNull(constructor.getParameters());
      assertNull(constructor.getValue());
   }

   public void testConstructorWithAnnotations() throws Exception
   {
      ConstructorMetaData constructor = getConstructor();
      assertNull(constructor.getFactoryClass());
      assertNull(constructor.getFactoryMethod());
      HashSet<String> expected = new HashSet<String>();
      expected.add(Annotation1.class.getName());
      expected.add(Annotation2.class.getName());
      expected.add(Annotation3.class.getName());
      assertAnnotations(expected, constructor.getAnnotations());
      assertNull(constructor.getFactory());
      assertNull(constructor.getParameters());
      assertNull(constructor.getValue());
   }

   public void testConstructorWithFactory() throws Exception
   {
      ConstructorMetaData constructor = getConstructor();
      assertNull(constructor.getFactoryClass());
      assertEquals("Dummy", constructor.getFactoryMethod());
      assertNull(constructor.getAnnotations());
      assertNotNull(constructor.getFactory());
      assertNull(constructor.getParameters());
      assertNull(constructor.getValue());
   }

   public void testConstructorWithParameter() throws Exception
   {
      ConstructorMetaData constructor = getConstructor();
      assertNull(constructor.getFactoryClass());
      assertNull(constructor.getFactoryMethod());
      assertNull(constructor.getAnnotations());
      assertNull(constructor.getFactory());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Parameter1");
      assertParameters(expected, constructor.getParameters());
      assertNull(constructor.getValue());
   }

   public void testConstructorWithParameters() throws Exception
   {
      ConstructorMetaData constructor = getConstructor();
      assertNull(constructor.getFactoryClass());
      assertNull(constructor.getFactoryMethod());
      assertNull(constructor.getAnnotations());
      assertNull(constructor.getFactory());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Parameter1");
      expected.add("Parameter2");
      expected.add("Parameter3");
      assertParameters(expected, constructor.getParameters());
      assertNull(constructor.getValue());
   }

   public void testConstructorWithValue() throws Exception
   {
      ConstructorMetaData constructor = getConstructor();
      assertNull(constructor.getFactoryClass());
      assertNull(constructor.getFactoryMethod());
      assertNull(constructor.getAnnotations());
      assertNull(constructor.getFactory());
      assertNull(constructor.getParameters());
      assertValue("Value", constructor.getValue());
   }

   public void testConstructorWithCollection() throws Exception
   {
      ConstructorMetaData constructor = getConstructor();
      assertNull(constructor.getFactoryClass());
      assertNull(constructor.getFactoryMethod());
      assertNull(constructor.getAnnotations());
      assertNull(constructor.getFactory());
      assertNull(constructor.getParameters());
      assertCollection(constructor.getValue());
   }

   public void testConstructorWithList() throws Exception
   {
      ConstructorMetaData constructor = getConstructor();
      assertNull(constructor.getFactoryClass());
      assertNull(constructor.getFactoryMethod());
      assertNull(constructor.getAnnotations());
      assertNull(constructor.getFactory());
      assertNull(constructor.getParameters());
      assertList(constructor.getValue());
   }

   public void testConstructorWithSet() throws Exception
   {
      ConstructorMetaData constructor = getConstructor();
      assertNull(constructor.getFactoryClass());
      assertNull(constructor.getFactoryMethod());
      assertNull(constructor.getAnnotations());
      assertNull(constructor.getFactory());
      assertNull(constructor.getParameters());
      assertSet(constructor.getValue());
   }

   public void testConstructorWithArray() throws Exception
   {
      ConstructorMetaData constructor = getConstructor();
      assertNull(constructor.getFactoryClass());
      assertNull(constructor.getFactoryMethod());
      assertNull(constructor.getAnnotations());
      assertNull(constructor.getFactory());
      assertNull(constructor.getParameters());
      assertArray(constructor.getValue());
   }

   public void testConstructorWithMap() throws Exception
   {
      ConstructorMetaData constructor = getConstructor();
      assertNull(constructor.getFactoryClass());
      assertNull(constructor.getFactoryMethod());
      assertNull(constructor.getAnnotations());
      assertNull(constructor.getFactory());
      assertNull(constructor.getParameters());
      assertMap(constructor.getValue());
   }

   public void testConstructorWithWildcard() throws Exception
   {
      ConstructorMetaData constructor = getConstructor();
      assertNull(constructor.getFactoryClass());
      assertNull(constructor.getFactoryMethod());
      assertNull(constructor.getAnnotations());
      assertNull(constructor.getFactory());
      assertNull(constructor.getParameters());
      assertWildcard(constructor.getValue());
   }
   
   /* TODO
   public void testConstructorBadFactoryClassNoFactoryMethod() throws Exception
   {
      try
      {
         unmarshalBean("ConstructorBadFactoryClassNoFactoryMethod.xml");
         fail("Should not be here");
      }
      catch (Exception expected)
      {
         checkJBossXBException(IllegalArgumentException.class, expected);
      }
   }
   */

   /* TODO
   public void testConstructorBadFactoryNoFactoryMethod() throws Exception
   {
      try
      {
         unmarshalBean("ConstructorBadFactoryNoFactoryMethod.xml");
         fail("Should not be here");
      }
      catch (Exception expected)
      {
         checkJBossXBException(IllegalArgumentException.class, expected);
      }
   }
   */

   public static Test suite()
   {
      return ConstructorJaxbTestCase.suite(ConstructorJaxbTestCase.class);
   }

   public ConstructorJaxbTestCase(String name)
   {
      super(name);
   }
}
