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
import java.util.Set;

import junit.framework.Test;
import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.beans.metadata.api.model.AutowireType;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractRelatedClassMetaData;
import org.jboss.beans.metadata.spi.RelatedClassMetaData;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ErrorHandlingMode;

/**
 * BeanTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class BeanTestCase extends AbstractXMLTest
{
   public void testBeanWithName() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean("BeanWithName.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithClass.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithMode.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithErrorHandlingMode.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithAccessMode.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithAnnotation.xml");
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      HashSet<String> expected = new HashSet<String>();
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation1");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithAnnotations.xml");
      assertNull(bean.getName());
      assertEquals("Dummy", bean.getBean());
      assertNull(bean.getMode());
      assertNull(bean.getErrorHandlingMode());
      assertNull(bean.getAccessMode());
      HashSet<String> expected = new HashSet<String>();
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation1");
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation2");
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation3");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithClassLoader.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithConstructor.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithProperty.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithProperties.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithCreate.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithStart.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithStop.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithDestroy.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithDependency.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithDependencies.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithDemand.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithDemands.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithSupply.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithSupplies.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithInstall.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithInstalls.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithUninstall.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithUninstalls.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithInstallCallback.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithInstallCallbacks.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithUninstallCallback.xml");
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
      AbstractBeanMetaData bean = unmarshalBean("BeanWithUninstallCallbacks.xml");
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

   public void testBeanWithCandidate() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean("BeanWithCandidate.xml");
      assertFalse(bean.isAutowireCandidate());
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
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithParent() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean("BeanWithParent.xml");
      assertEquals("OldDummy", bean.getParent());
      assertNull(bean.getName());
      assertNull(bean.getBean());
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

   public void testBeanWithAbstract() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean("BeanWithAbstract.xml");
      assertTrue(bean.isAbstract());
      assertNull(bean.getName());
      assertNull(bean.getBean());
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

   public void testBeanWithAutowire() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean("BeanWithAutowire.xml");
      assertEquals(AutowireType.CONSTRUCTOR, bean.getAutowireType());
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
      assertNull(bean.getUninstallCallbacks());
   }

   public void testBeanWithRelated() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean("BeanWithRelated.xml");
      assertNull(bean.getAutowireType());
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
      assertNull(bean.getUninstallCallbacks());
      Set<RelatedClassMetaData> expected = new HashSet<RelatedClassMetaData>();
      expected.add(new AbstractRelatedClassMetaData("Dummy"));
      assertEquals(expected, bean.getRelated());
   }

   public void testBeanWithRelateds() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean("BeanWithRelateds.xml");
      assertNull(bean.getAutowireType());
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
      assertNull(bean.getUninstallCallbacks());
      Set<RelatedClassMetaData> expected = new HashSet<RelatedClassMetaData>();
      expected.add(new AbstractRelatedClassMetaData("Dummy"));
      AbstractRelatedClassMetaData arcmd = new AbstractRelatedClassMetaData("Dummy");
      arcmd.setEnabledValue("md");
      expected.add(arcmd);
      assertEquals(expected, bean.getRelated());
   }

   public static Test suite()
   {
      return suite(BeanTestCase.class);
   }

   public BeanTestCase(String name)
   {
      super(name);
   }

   protected BeanTestCase(String name, boolean useClone)
   {
      super(name, useClone);
   }
}
