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
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactory;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData;
import org.jboss.dependency.spi.ControllerMode;

/**
 * BeanFactoryTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class BeanFactoryTestCase extends AbstractXMLTest
{
   public void testBeanFactoryWithName() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithName.xml");
      assertEquals("Name1", factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithClass() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithClass.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals(Object.class.getName(), factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithMode() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithMode.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertEquals(ControllerMode.MANUAL, factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithClassLoader() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithClassLoader.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNotNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithConstructor() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithConstructor.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNotNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithProperty() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithProperty.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      HashSet<String> expected = new HashSet<String>();
      expected.add("Property1");
      assertBeanFactoryProperties(expected, factory);
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithProperties() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithProperties.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      HashSet<String> expected = new HashSet<String>();
      expected.add("Property1");
      expected.add("Property2");
      expected.add("Property3");
      assertBeanFactoryProperties(expected, factory);
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithCreate() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithCreate.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNotNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithStart() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithStart.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNotNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithDependency() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithDependency.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      HashSet<String> expected = new HashSet<String>();
      expected.add("Depends1");
      assertDepends(expected, factory.getDepends());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithDependencies() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithDependencies.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      HashSet<String> expected = new HashSet<String>();
      expected.add("Depends1");
      expected.add("Depends2");
      expected.add("Depends3");
      assertDepends(expected, factory.getDepends());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithDemand() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithDemand.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      HashSet<String> expected = new HashSet<String>();
      expected.add("Demand1");
      assertDemands(expected, factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithDemands() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithDemands.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      HashSet<String> expected = new HashSet<String>();
      expected.add("Demand1");
      expected.add("Demand2");
      expected.add("Demand3");
      assertDemands(expected, factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithSupply() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithSupply.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      assertNull(factory.getDemands());
      HashSet<String> expected = new HashSet<String>();
      expected.add("Supply1");
      assertSupplies(expected, factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithSupplies() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithSupplies.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      assertNull(factory.getDemands());
      HashSet<String> expected = new HashSet<String>();
      expected.add("Supply1");
      expected.add("Supply2");
      expected.add("Supply3");
      assertSupplies(expected, factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithInstall() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithInstall.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Install1");
      assertInstalls(expected, factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithInstalls() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithInstalls.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Install1");
      expected.add("Install2");
      expected.add("Install3");
      assertInstalls(expected, factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithUninstall() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithUninstall.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Uninstall1");
      assertInstalls(expected, factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithUninstalls() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithUninstalls.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Uninstall1");
      expected.add("Uninstall2");
      expected.add("Uninstall3");
      assertInstalls(expected, factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

/*
   public void testBeanFactoryWithInstallCallback() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithInstallCallback.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Install1");
      assertCallbacks(expected, factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithInstallCallbacks() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithInstallCallbacks.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Install1");
      expected.add("Install2");
      expected.add("Install3");
      assertCallbacks(expected, factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithUninstallCallback() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithUninstallCallback.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Uninstall1");
      assertCallbacks(expected, factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithUninstallCallbacks() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory("BeanFactoryWithUninstallCallbacks.xml");
      assertNull(factory.getName());
      assertEquals(GenericBeanFactory.class.getName(), factory.getBean());
      assertEquals("Dummy", factory.getBeanClass());
      assertNull(factory.getMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperty("constructor"));
      assertNotNull(factory.getProperties());
      assertNull(factory.getProperty("properties"));
      assertNull(factory.getCreate());
      assertNull(factory.getProperty("create"));
      assertNull(factory.getStart());
      assertNull(factory.getProperty("start"));
      assertNull(factory.getStop());
      assertNull(factory.getDestroy());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Uninstall1");
      expected.add("Uninstall2");
      expected.add("Uninstall3");
      assertCallbacks(expected, factory.getUninstallCallbacks());
   }
*/

   public void testBeanFactoryBadNoClassOrConstructor() throws Exception
   {
      try
      {
         unmarshalBeanFactory("BeanFactoryBadNoClassOrConstructor.xml");
         fail("Should not be here");
      }
      catch (Exception expected)
      {
         checkJBossXBException(IllegalArgumentException.class, expected);
      }
   }

   public void testBeanFactoryBadNoClassOrFactoryMethod() throws Exception
   {
      try
      {
         unmarshalBeanFactory("BeanFactoryBadNoClassOrFactoryMethod.xml");
         fail("Should not be here");
      }
      catch (Exception expected)
      {
         checkJBossXBException(IllegalArgumentException.class, expected);
      }
   }

   public void testBeanFactoryBadNoClassOrFactory() throws Exception
   {
      try
      {
         unmarshalBeanFactory("BeanFactoryBadNoClassOrFactory.xml");
         fail("Should not be here");
      }
      catch (Exception expected)
      {
         checkJBossXBException(IllegalArgumentException.class, expected);
      }
   }
   
   public static Test suite()
   {
      return suite(BeanFactoryTestCase.class);
   }

   public BeanFactoryTestCase(String name)
   {
      super(name);
   }
}
