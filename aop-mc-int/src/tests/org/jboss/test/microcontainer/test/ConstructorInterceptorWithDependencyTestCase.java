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
package org.jboss.test.microcontainer.test;

import junit.framework.Test;

import org.jboss.aop.microcontainer.junit.AOPMicrocontainerTest;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.microcontainer.support.InterceptorWithDependency;
import org.jboss.test.microcontainer.support.SimpleBean;

public class ConstructorInterceptorWithDependencyTestCase extends AOPMicrocontainerTest
{
   public static Test suite()
   {
      return suite(ConstructorInterceptorWithDependencyTestCase.class);
   }
   
   public ConstructorInterceptorWithDependencyTestCase(String name)
   {
      super(name);
   }
   
   public void testInterceptorWithDependencyCorrectOrder() throws Exception
   {
      InterceptorWithDependency.intercepted = null;
      deploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic0.xml");
      try
      {
         assertNull(InterceptorWithDependency.intercepted);
         SimpleBean dependency = (SimpleBean) getBean("Dependency");
         assertNotNull(dependency);
         deploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic1.xml");
         try
         {
            validate();
            SimpleBean bean = (SimpleBean) getBean("Intercepted");
            assertNotNull(bean);
            assertTrue(dependency == InterceptorWithDependency.intercepted);
         }
         finally
         {
            undeploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic1.xml");
         }
      }
      finally
      {
         undeploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic0.xml");
      }
   }
   
   public void testInterceptorWithDependencyWrongOrder() throws Exception
   {
      InterceptorWithDependency.intercepted = null;
      deploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic1.xml");
      try
      {
         SimpleBean bean;
         try
         {
            bean = (SimpleBean) getBean("Intercepted");
            fail("'Interceped' should not be installed yet");
         }
         catch (IllegalStateException expected)
         {
         }
         
         bean = (SimpleBean) getBean("Intercepted", ControllerState.DESCRIBED);
         assertNull("This should not be deployed until the interceptor is", bean);
         assertNull(InterceptorWithDependency.intercepted);
         
         deploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic0.xml");
         try
         {
            validate();
            SimpleBean dependency = (SimpleBean) getBean("Dependency");
            assertNotNull(dependency);
            bean = (SimpleBean) getBean("Intercepted");
            assertNotNull(bean);
            assertTrue(dependency == InterceptorWithDependency.intercepted);
         }
         finally
         {
            undeploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic0.xml");
         }
      }
      finally
      {
         undeploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic1.xml");
      }
   }
   
   public void testInterceptorWithDependencyRedeploy() throws Exception
   {
      InterceptorWithDependency.intercepted = null;
      deploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic1.xml");
      try
      {
         SimpleBean bean;
         try
         {
            bean = (SimpleBean) getBean("Intercepted");
            fail("Bean should not be installed until the dependency is");
         }
         catch (IllegalStateException expected)
         {
            KernelControllerContext context = getControllerContext("Intercepted", ControllerState.DESCRIBED);
            assertNotNull(context);
         }
         assertNull(InterceptorWithDependency.intercepted);

         deploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic0.xml");
         try
         {
            validate();
            SimpleBean dependency = (SimpleBean) getBean("Dependency");
            assertNotNull(dependency);
            bean = (SimpleBean) getBean("Intercepted");
            assertNotNull(bean);
            assertTrue(dependency == InterceptorWithDependency.intercepted);
         }
         finally
         {
            InterceptorWithDependency.intercepted = null;
            undeploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic0.xml");
         }
         
         try
         {
            bean = (SimpleBean) getBean("Intercepted");
            fail("Bean should not be installed after the dependency is undeployed");
         }
         catch (IllegalStateException expected)
         {
            KernelControllerContext context = getControllerContext("Intercepted", ControllerState.DESCRIBED);
            assertNotNull(context);
         }

         
         assertNull(InterceptorWithDependency.intercepted);
         deploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic0.xml");
         try
         {
            validate();
            SimpleBean dependency = (SimpleBean) getBean("Dependency");
            assertNotNull(dependency);
            bean = (SimpleBean) getBean("Intercepted");
            assertNotNull(bean);
            assertTrue("Should not be caching the interceptor/dependency across rebinding", dependency == InterceptorWithDependency.intercepted);
         }
         finally
         {
            undeploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic0.xml");
         }
      }
      finally
      {
         undeploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic1.xml");
      }
   }
   
      
   public void testInterceptorWithDependencyRedeploy2() throws Exception
   {
      InterceptorWithDependency.intercepted = null;
      deploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic0.xml");
      try
      {
         SimpleBean dependency = (SimpleBean) getBean("Dependency");
         assertNotNull(dependency);
         deploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic1.xml");
         try
         {
            validate();
            SimpleBean bean = (SimpleBean) getBean("Intercepted");
            assertNotNull(bean);
            assertTrue(dependency == InterceptorWithDependency.intercepted);
         }
         finally
         {
            InterceptorWithDependency.intercepted = null;
            undeploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic1.xml");
         }
         assertNull(InterceptorWithDependency.intercepted);
         
         dependency = (SimpleBean) getBean("Dependency");
         assertNotNull(dependency);
         deploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic1.xml");
         try
         {
            SimpleBean bean = (SimpleBean) getBean("Intercepted");
            assertNotNull(bean);
            bean.someMethod();
            assertTrue(dependency == InterceptorWithDependency.intercepted);
         }
         finally
         {
            undeploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic1.xml");
         }
      }
      finally
      {
         undeploy("ConstructorInterceptorWithDependencyTestCaseNotAutomatic0.xml");
      }
   }

   protected void configureLogging()
   {
      //enableTrace("org.jboss.kernel.plugins.dependency");
   }
}
