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
import java.util.List;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.CallbackMetaData;
import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.ControllerState;

/**
 * CallbackTestCase.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class CallbackTestCase extends AbstractXMLTest
{
   public CallbackTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(CallbackTestCase.class);
   }

   protected CallbackMetaData getInstallCallback(String name) throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean(name);
      List<CallbackMetaData> callbacks = bean.getInstallCallbacks();
      assertNotNull(callbacks);
      assertEquals(1, callbacks.size());
      CallbackMetaData callback = callbacks.get(0);
      assertNotNull(callback);
      return callback;
   }

   protected CallbackMetaData getUninstallCallback(String name) throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean(name);
      List<CallbackMetaData> callbacks = bean.getUninstallCallbacks();
      assertNotNull(callbacks);
      assertEquals(1, callbacks.size());
      CallbackMetaData callback = callbacks.get(0);
      assertNotNull(callback);
      return callback;
   }

   public void testInstallWithProperty() throws Exception
   {
      CallbackMetaData install = getInstallCallback("CallbackInstallWithProperty.xml");
      assertEquals("someProperty", install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertNull(install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testInstallWithMethod() throws Exception
   {
      CallbackMetaData install = getInstallCallback("CallbackInstallWithMethod.xml");
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("someMethod", install.getMethodName());
      assertNull(install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testInstallWithState() throws Exception
   {
      CallbackMetaData install = getInstallCallback("CallbackInstallWithState.xml");
      assertNull(install.getProperty());
      assertEquals(ControllerState.CONFIGURED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      assertNull(install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testInstallWithAnnotation() throws Exception
   {
      CallbackMetaData install = getInstallCallback("CallbackInstallWithAnnotation.xml");
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      HashSet<String> expected = new HashSet<String>();
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation1");
      assertAnnotations(expected, install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testInstallWithAnnotations() throws Exception
   {
      CallbackMetaData install = getInstallCallback("CallbackInstallWithAnnotations.xml");
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      HashSet<String> expected = new HashSet<String>();
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation1");
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation2");
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation3");
      assertAnnotations(expected, install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testInstallWithSignature() throws Exception
   {
      CallbackMetaData install = getInstallCallback("CallbackInstallWithSignature.xml");
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      assertNull(install.getAnnotations());
      assertNotNull(install.getSignature());
      assertEquals("someSignature", install.getSignature());
   }

   public void testInstallWithCardinality() throws Exception
   {
      CallbackMetaData install = getInstallCallback("CallbackInstallWithCardinality.xml");
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      assertNull(install.getAnnotations());
      assertNotNull(install.getCardinality());
      assertEquals(install.getCardinality(), Cardinality.ONE_TO_MANY);
   }

   // --- Uninstall

   public void testUninstallWithProperty() throws Exception
   {
      CallbackMetaData install = getUninstallCallback("CallbackUninstallWithProperty.xml");
      assertEquals("someProperty", install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertNull(install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testUninstallWithMethod() throws Exception
   {
      CallbackMetaData install = getUninstallCallback("CallbackUninstallWithMethod.xml");
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("someMethod", install.getMethodName());
      assertNull(install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testUninstallWithState() throws Exception
   {
      CallbackMetaData install = getUninstallCallback("CallbackUninstallWithState.xml");
      assertNull(install.getProperty());
      assertEquals(ControllerState.CONFIGURED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      assertNull(install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testUninstallWithAnnotation() throws Exception
   {
      CallbackMetaData install = getUninstallCallback("CallbackUninstallWithAnnotation.xml");
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      HashSet<String> expected = new HashSet<String>();
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation1");
      assertAnnotations(expected, install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testUninstallWithAnnotations() throws Exception
   {
      CallbackMetaData install = getUninstallCallback("CallbackUninstallWithAnnotations.xml");
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      HashSet<String> expected = new HashSet<String>();
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation1");
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation2");
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation3");
      assertAnnotations(expected, install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testUninstallWithSignature() throws Exception
   {
      CallbackMetaData install = getUninstallCallback("CallbackUninstallWithSignature.xml");
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      assertNull(install.getAnnotations());
      assertNotNull(install.getSignature());
      assertEquals("someSignature", install.getSignature());
   }

   public void testUninstallWithCardinality() throws Exception
   {
      CallbackMetaData install = getUninstallCallback("CallbackUninstallWithCardinality.xml");
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      assertNull(install.getAnnotations());
      assertNotNull(install.getCardinality());
      assertEquals(install.getCardinality(), Cardinality.fromString("2..10"));
   }
}
