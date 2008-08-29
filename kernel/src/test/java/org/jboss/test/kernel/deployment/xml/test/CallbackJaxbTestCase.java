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
import org.jboss.test.kernel.deployment.xml.support.Annotation1;
import org.jboss.test.kernel.deployment.xml.support.Annotation2;
import org.jboss.test.kernel.deployment.xml.support.Annotation3;

/**
 * CallbackJaxbTestCase.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class CallbackJaxbTestCase extends AbstractMCTest
{
   public CallbackJaxbTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return CallbackJaxbTestCase.suite(CallbackJaxbTestCase.class);
   }

   protected CallbackMetaData getInstallCallback() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      List<CallbackMetaData> callbacks = bean.getInstallCallbacks();
      assertNotNull(callbacks);
      assertEquals(1, callbacks.size());
      CallbackMetaData callback = callbacks.get(0);
      assertNotNull(callback);
      return callback;
   }

   protected CallbackMetaData getUninstallCallback() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      List<CallbackMetaData> callbacks = bean.getUninstallCallbacks();
      assertNotNull(callbacks);
      assertEquals(1, callbacks.size());
      CallbackMetaData callback = callbacks.get(0);
      assertNotNull(callback);
      return callback;
   }

   public void testCallbackInstallWithProperty() throws Exception
   {
      CallbackMetaData install = getInstallCallback();
      assertEquals("someProperty", install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertNull(install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testCallbackInstallWithMethod() throws Exception
   {
      CallbackMetaData install = getInstallCallback();
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("someMethod", install.getMethodName());
      assertNull(install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testCallbackInstallWithState() throws Exception
   {
      CallbackMetaData install = getInstallCallback();
      assertNull(install.getProperty());
      assertEquals(ControllerState.CONFIGURED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      assertNull(install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testCallbackInstallWithAnnotation() throws Exception
   {
      CallbackMetaData install = getInstallCallback();
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      HashSet<String> expected = new HashSet<String>();
      expected.add(Annotation1.class.getName());
      assertAnnotations(expected, install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testCallbackInstallWithAnnotations() throws Exception
   {
      CallbackMetaData install = getInstallCallback();
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      HashSet<String> expected = new HashSet<String>();
      expected.add(Annotation1.class.getName());
      expected.add(Annotation2.class.getName());
      expected.add(Annotation3.class.getName());
      assertAnnotations(expected, install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testCallbackInstallWithSignature() throws Exception
   {
      CallbackMetaData install = getInstallCallback();
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      assertNull(install.getAnnotations());
      assertNotNull(install.getSignature());
      assertEquals("someSignature", install.getSignature());
   }

   public void testCallbackInstallWithCardinality() throws Exception
   {
      CallbackMetaData install = getInstallCallback();
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      assertNull(install.getAnnotations());
      assertNotNull(install.getCardinality());
      assertEquals(install.getCardinality(), Cardinality.ONE_TO_MANY);
   }

   /* TODO
   public void testInstallBothMethodProperty() throws Exception
   {
      try
      {
         unmarshalBean("CallbackInstallBadMethodProperty.xml");
         fail("Should not be here");
      }
      catch (Exception expected)
      {
         checkJBossXBException(IllegalArgumentException.class, expected);
      }
   }
   */

   // --- Uninstall

   public void testCallbackUninstallWithProperty() throws Exception
   {
      CallbackMetaData install = getUninstallCallback();
      assertEquals("someProperty", install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertNull(install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testCallbackUninstallWithMethod() throws Exception
   {
      CallbackMetaData install = getUninstallCallback();
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("someMethod", install.getMethodName());
      assertNull(install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testCallbackUninstallWithState() throws Exception
   {
      CallbackMetaData install = getUninstallCallback();
      assertNull(install.getProperty());
      assertEquals(ControllerState.CONFIGURED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      assertNull(install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testCallbackUninstallWithAnnotation() throws Exception
   {
      CallbackMetaData install = getUninstallCallback();
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      HashSet<String> expected = new HashSet<String>();
      expected.add(Annotation1.class.getName());
      assertAnnotations(expected, install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testCallbackUninstallWithAnnotations() throws Exception
   {
      CallbackMetaData install = getUninstallCallback();
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      HashSet<String> expected = new HashSet<String>();
      expected.add(Annotation1.class.getName());
      expected.add(Annotation2.class.getName());
      expected.add(Annotation3.class.getName());
      assertAnnotations(expected, install.getAnnotations());
      assertNull(install.getParameters());
   }

   public void testCallbackUninstallWithSignature() throws Exception
   {
      CallbackMetaData install = getUninstallCallback();
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      assertNull(install.getAnnotations());
      assertNotNull(install.getSignature());
      assertEquals("someSignature", install.getSignature());
   }

   public void testCallbackUninstallWithCardinality() throws Exception
   {
      CallbackMetaData install = getUninstallCallback();
      assertNull(install.getProperty());
      assertEquals(ControllerState.INSTALLED, install.getDependentState());
      assertEquals("Dummy", install.getMethodName());
      assertNull(install.getAnnotations());
      assertNotNull(install.getCardinality());
      assertEquals(install.getCardinality(), Cardinality.fromString("2..10"));
   }

   /* TODO
   public void testUninstallBothMethodProperty() throws Exception
   {
      try
      {
         unmarshalBean("CallbackUninstallBadMethodProperty.xml");
         fail("Should not be here");
      }
      catch (Exception expected)
      {
         checkJBossXBException(IllegalArgumentException.class, expected);
      }
   }
   */
}
