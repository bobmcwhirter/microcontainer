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
import java.util.List;

import junit.framework.Test;

import org.jboss.beans.metadata.spi.factory.GenericBeanFactoryMetaData;
import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.dependency.spi.ControllerMode;

/**
 * BeanFactoryJaxbTestCase
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 62474 $
 */
public class BeanFactoryJaxbTestCase extends AbstractMCTest
{
   public void testBeanFactoryWithName() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertEquals("Name1", factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithClass() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals(Object.class.getName(), factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithMode() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertEquals(ControllerMode.MANUAL, factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithAccessMode() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertEquals(BeanAccessMode.FIELDS, factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithClassLoader() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNotNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithConstructor() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNotNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
      factory.getBeans();
   }

   public void testBeanFactoryWithProperty() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNotNull(factory.getProperties());
      HashSet<String> expected = new HashSet<String>();
      expected.add("Property1");
      assertProperties(expected, factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithProperties() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNotNull(factory.getProperties());
      HashSet<String> expected = new HashSet<String>();
      expected.add("Property1");
      expected.add("Property2");
      expected.add("Property3");
      assertProperties(expected, factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithCreate() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNotNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithStart() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNotNull(factory.getStart());
      assertNull(factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithDependency() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      HashSet<String> expected = new HashSet<String>();
      expected.add("Depends1");
      assertDepends(expected, factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithDependencies() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      HashSet<String> expected = new HashSet<String>();
      expected.add("Depends1");
      expected.add("Depends2");
      expected.add("Depends3");
      assertDepends(expected, factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithDemand() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
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
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
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
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
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
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
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
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      List<String> expected = new ArrayList<String>();
      expected.add("Install1");
      assertInstalls(expected, factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithInstalls() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      List<String> expected = new ArrayList<String>();
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
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      List<String> expected = new ArrayList<String>();
      expected.add("Uninstall1");
      assertInstalls(expected, factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithUninstalls() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      List<String> expected = new ArrayList<String>();
      expected.add("Uninstall1");
      expected.add("Uninstall2");
      expected.add("Uninstall3");
      assertInstalls(expected, factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithInstallCallback() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      List<String> expected = new ArrayList<String>();
      expected.add("Install1");
      assertCallbacks(expected, factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithInstallCallbacks() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      List<String> expected = new ArrayList<String>();
      expected.add("Install1");
      expected.add("Install2");
      expected.add("Install3");
      assertCallbacks(expected, factory.getInstallCallbacks());
      assertNull(factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithUninstallCallback() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      List<String> expected = new ArrayList<String>();
      expected.add("Uninstall1");
      assertCallbacks(expected, factory.getUninstallCallbacks());
   }

   public void testBeanFactoryWithUninstallCallbacks() throws Exception
   {
      GenericBeanFactoryMetaData factory = unmarshalBeanFactory();
      assertNull(factory.getName());
      assertEquals("Dummy", factory.getBean());
      assertNull(factory.getMode());
      assertNull(factory.getAccessMode());
      assertNull(factory.getAnnotations());
      assertNull(factory.getClassLoader());
      assertNull(factory.getConstructor());
      assertNull(factory.getProperties());
      assertNull(factory.getCreate());
      assertNull(factory.getStart());
      assertNull(factory.getDepends());
      assertNull(factory.getDemands());
      assertNull(factory.getSupplies());
      assertNull(factory.getInstalls());
      assertNull(factory.getUninstalls());
      assertNull(factory.getInstallCallbacks());
      List<String> expected = new ArrayList<String>();
      expected.add("Uninstall1");
      expected.add("Uninstall2");
      expected.add("Uninstall3");
      assertCallbacks(expected, factory.getUninstallCallbacks());
   }
   
   public static Test suite()
   {
      return BeanFactoryJaxbTestCase.suite(BeanFactoryJaxbTestCase.class);
   }

   public BeanFactoryJaxbTestCase(String name)
   {
      super(name);
   }
}
