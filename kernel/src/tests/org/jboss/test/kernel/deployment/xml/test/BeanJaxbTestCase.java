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
import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ErrorHandlingMode;
import org.jboss.test.kernel.deployment.xml.support.Annotation1;
import org.jboss.test.kernel.deployment.xml.support.Annotation2;
import org.jboss.test.kernel.deployment.xml.support.Annotation3;

/**
 * BeanJaxbTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 62474 $
 */
public class BeanJaxbTestCase extends AbstractMCTest
{
   public void testBeanWithName() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertEquals("Name1", bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithClass() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals(Object.class.getName(), bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithMode() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertEquals(ControllerMode.MANUAL, bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithErrorHandlingMode() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertEquals(ErrorHandlingMode.MANUAL, bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithAccessMode() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertEquals(BeanAccessMode.FIELDS, bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithAnnotation() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      HashSet<String> expected = new HashSet<String>();
      expected.add(Annotation1.class.getName());
      assertAnnotations(expected, bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithAnnotations() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      HashSet<String> expected = new HashSet<String>();
      expected.add(Annotation1.class.getName());
      expected.add(Annotation2.class.getName());
      expected.add(Annotation3.class.getName());
      assertAnnotations(expected, bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithClassLoader() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNotNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithConstructor() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNotNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithProperty() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      HashSet<String> expected = new HashSet<String>();
      expected.add("Property1");
      assertProperties(expected, bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithProperties() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      HashSet<String> expected = new HashSet<String>();
      expected.add("Property1");
      expected.add("Property2");
      expected.add("Property3");
      assertProperties(expected, bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithCreate() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNotNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithStart() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNotNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithStop() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNotNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithDestroy() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNotNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithDependency() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      HashSet<String> expected = new HashSet<String>();
      expected.add("Depends1");
      assertDepends(expected, bean.getDepends());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithDependencies() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      HashSet<String> expected = new HashSet<String>();
      expected.add("Depends1");
      expected.add("Depends2");
      expected.add("Depends3");
      assertDepends(expected, bean.getDepends());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithDemand() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      HashSet<String> expected = new HashSet<String>();
      expected.add("Demand1");
      assertDemands(expected, bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithDemands() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      HashSet<String> expected = new HashSet<String>();
      expected.add("Demand1");
      expected.add("Demand2");
      expected.add("Demand3");
      assertDemands(expected, bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithSupply() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      HashSet<String> expected = new HashSet<String>();
      expected.add("Supply1");
      assertSupplies(expected, bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithSupplies() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      HashSet<String> expected = new HashSet<String>();
      expected.add("Supply1");
      expected.add("Supply2");
      expected.add("Supply3");
      assertSupplies(expected, bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithInstall() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Install1");
      assertInstalls(expected, bean.getInstalls());
      assertNull(bean.getUninstalls());
   }

   public void testBeanWithInstalls() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Install1");
      expected.add("Install2");
      expected.add("Install3");
      assertInstalls(expected, bean.getInstalls());
      assertNull(bean.getUninstalls());
   }

   public void testBeanWithUninstall() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Uninstall1");
      assertInstalls(expected, bean.getUninstalls());
   }

   public void testBeanWithUninstalls() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Uninstall1");
      expected.add("Uninstall2");
      expected.add("Uninstall3");
      assertInstalls(expected, bean.getUninstalls());
   }

   public void testBeanWithInstallCallback() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Install1");
      assertCallbacks(expected, bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithInstallCallbacks() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Install1");
      expected.add("Install2");
      expected.add("Install3");
      assertCallbacks(expected, bean.getInstallCallbacks());
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithUninstallCallback() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Uninstall1");
      assertCallbacks(expected, bean.getUninstallCallbacks());
   }

   public void testBeanWithUninstallCallbacks() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      assertNull(bean.getAnnotations());
      assertNull(bean.getClassLoader());
      assertNull(bean.getConstructor());
      assertNull(bean.getProperties());
      assertNull(bean.getCreate());
      assertNull(bean.getStart());
      assertNull(bean.getStop());
      assertNull(bean.getDestroy());
      assertNull(bean.getDemands());
      assertNull(bean.getSupplies());
      assertNull(bean.getInstalls());
      assertNull(bean.getUninstalls());
      assertNull(bean.getInstallCallbacks());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Uninstall1");
      expected.add("Uninstall2");
      expected.add("Uninstall3");
      assertCallbacks(expected, bean.getUninstallCallbacks());
   }

   /* TODO
   public void testBeanBadNoClassOrConstructor() throws Exception
   {
      try
      {
         unmarshalBean();
         fail("Should not be here");
      }
      catch (Exception expected)
      {
         checkJBossXBException(IllegalArgumentException.class, expected);
      }
   }
   */

   /* TODO
   public void testBeanBadNoClassOrFactoryMethod() throws Exception
   {
      try
      {
         unmarshalBean();
         fail("Should not be here");
      }
      catch (Exception expected)
      {
         checkJBossXBException(IllegalArgumentException.class, expected);
      }
   }
   */

   /* TODO
   public void testBeanBadNoClassOrFactory() throws Exception
   {
      try
      {
         unmarshalBean();
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
      return BeanJaxbTestCase.suite(BeanJaxbTestCase.class);
   }

   public BeanJaxbTestCase(String name)
   {
      super(name);
   }
}
