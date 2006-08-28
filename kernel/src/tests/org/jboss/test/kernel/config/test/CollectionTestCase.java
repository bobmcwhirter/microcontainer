/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.kernel.config.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.jboss.beans.metadata.plugins.*;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.test.kernel.config.support.CustomCollection;
import org.jboss.test.kernel.config.support.MyObject;
import org.jboss.test.kernel.config.support.SimpleBean;

import junit.framework.Test;

/**
 * Collection Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class CollectionTestCase extends AbstractKernelConfigTest
{
   MyObject object1 = new MyObject("object1");
   MyObject object2 = new MyObject("object2");
   String string1 = "string1";
   String string2 = "string2";

   public static Test suite()
   {
      return suite(CollectionTestCase.class);
   }

   public CollectionTestCase(String name)
   {
      super(name);
   }

   public CollectionTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public void testSimpleCollectionFromObjects() throws Throwable
   {
      SimpleBean bean = simpleCollectionFromObjects();
      assertNotNull(bean);
      
      Collection result = bean.getCollection();
      assertNotNull("Should be a collection", result);
      
      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add(object1);
      expected.add(object2);
      expected.add(object2);
      expected.add(object1);
      assertEquals(expected, result);
   }

   public SimpleBean simpleCollectionFromObjects() throws Throwable
   {
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      AbstractValueMetaData vmd1 = new AbstractValueMetaData(object1);
      AbstractValueMetaData vmd2 = new AbstractValueMetaData(object2);
      AbstractValueMetaData vmd3 = new AbstractValueMetaData(object1);

      AbstractCollectionMetaData smd = new AbstractCollectionMetaData();
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("collection", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(bmd);
   }

   public void testSimpleCollectionFromStrings() throws Throwable
   {
      SimpleBean bean = simpleCollectionFromStrings();
      assertNotNull(bean);
      
      Collection result = bean.getCollection();
      assertNotNull("Should be a collection", result);
      
      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add(string1);
      expected.add(string2);
      expected.add(string2);
      expected.add(string1);
      assertEquals(expected, result);
   }

   protected SimpleBean simpleCollectionFromStrings() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractCollectionMetaData smd = new AbstractCollectionMetaData();
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("collection", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testCustomCollectionExplicit() throws Throwable
   {
      SimpleBean bean = customCollectionExplicit();
      assertNotNull(bean);
      
      Collection result = bean.getCollection();
      assertNotNull("Should be a collection", result);
      assertTrue("Not a CustomCollection: " + result.getClass(), result instanceof CustomCollection);
      
      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add(string1);
      expected.add(string2);
      expected.add(string2);
      expected.add(string1);
      assertEquals(expected, result);
   }

   protected SimpleBean customCollectionExplicit() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractCollectionMetaData smd = new AbstractCollectionMetaData();
      smd.setType(CustomCollection.class.getName());
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("collection", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testCustomCollectionFromSignature() throws Throwable
   {
      SimpleBean bean = customCollectionFromSignature();
      assertNotNull(bean);
      
      Collection result = bean.getCustomCollection();
      assertNotNull("Should be a collection", result);
      assertTrue("Not a CustomCollection: " + result.getClass(), result instanceof CustomCollection);
      
      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add(string1);
      expected.add(string2);
      expected.add(string2);
      expected.add(string1);
      assertEquals(expected, result);
   }

   protected SimpleBean customCollectionFromSignature() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractCollectionMetaData smd = new AbstractCollectionMetaData();
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("customCollection", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testCustomCollectionPreInstantiated() throws Throwable
   {
      SimpleBean bean = customCollectionPreInstantiated();
      assertNotNull(bean);
      
      Collection result = bean.getPreInstantiatedCollection();
      assertNotNull("Should be a collection", result);
      assertTrue("Not a CustomCollection: " + result.getClass(), result instanceof CustomCollection);
      assertTrue("Not preinstantiated", ((CustomCollection) result).getPreInstantiated());
      
      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add(string1);
      expected.add(string2);
      expected.add(string2);
      expected.add(string1);
      assertEquals(expected, result);
   }

   protected SimpleBean customCollectionPreInstantiated() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractCollectionMetaData smd = new AbstractCollectionMetaData();
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("preInstantiatedCollection", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testCollectionWithValueTypeOverride() throws Throwable
   {
      SimpleBean bean = collectionWithValueTypeOverride();
      assertNotNull(bean);
      
      Collection result = bean.getCollection();
      assertNotNull("Should be a collection", result);
      assertTrue("Not a CustomCollection: " + result.getClass(), result instanceof CustomCollection);
      
      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add(string1);
      expected.add(string2);
      expected.add(string2);
      expected.add(string1);
      expected.add(new Integer(1));
      assertEquals(expected, result);
   }

   protected SimpleBean collectionWithValueTypeOverride() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);
      StringValueMetaData vmd4 = new StringValueMetaData("1");
      vmd4.setConfigurator(configurator);
      vmd4.setType("java.lang.Integer");

      AbstractCollectionMetaData smd = new AbstractCollectionMetaData();
      smd.setType(CustomCollection.class.getName());
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates
      smd.add(vmd4);

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("collection", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testCollectionInjectOnObject() throws Throwable
   {
      SimpleBean bean = collectionInjectOnObject();
      assertNotNull(bean);
      
      Object result = bean.getAnObject();
      assertNotNull("Should be a collection", result);
      assertTrue("Not a CustomCollection: " + result.getClass(), result instanceof ArrayList);
      
      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add(string1);
      expected.add(string2);
      expected.add(string2);
      expected.add(string1);
      expected.add(new Integer(1));
      assertEquals(expected, result);
   }

   protected SimpleBean collectionInjectOnObject() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);
      StringValueMetaData vmd4 = new StringValueMetaData("1");
      vmd4.setConfigurator(configurator);
      vmd4.setType("java.lang.Integer");

      AbstractCollectionMetaData smd = new AbstractCollectionMetaData();
      smd.setType(CustomCollection.class.getName());
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates
      smd.add(vmd4);

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("anObject", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testCollectionNotACollection() throws Throwable
   {
      try
      {
         collectionNotACollection();
         fail("Expected a failure trying to set a collection on a non collection attribute");
      }
      catch (Exception expected)
      {
         checkCollectionNotACollectionException(expected);
      }
   }

   protected void checkCollectionNotACollectionException(Exception exception)
   {
      checkThrowable(ClassCastException.class, exception);
   }
   
   protected SimpleBean collectionNotACollection() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractCollectionMetaData smd = new AbstractCollectionMetaData();
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("AString", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testCollectionIsInterface() throws Throwable
   {
      try
      {
         collectionIsInterface();
         fail("Expected a failure trying to use an interface for the collection type");
      }
      catch (Exception expected)
      {
         checkCollectionIsInterfaceException(expected);
      }
   }

   protected void checkCollectionIsInterfaceException(Exception exception)
   {
      checkThrowable(IllegalArgumentException.class, exception);
   }

   protected SimpleBean collectionIsInterface() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractCollectionMetaData smd = new AbstractCollectionMetaData();
      smd.setType("java.util.Collection");
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("collection", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }
}