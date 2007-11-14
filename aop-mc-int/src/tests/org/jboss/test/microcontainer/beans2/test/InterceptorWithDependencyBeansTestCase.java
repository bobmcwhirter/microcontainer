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
package org.jboss.test.microcontainer.beans2.test;

import junit.framework.Test;

import org.jboss.aop.microcontainer.junit.AOPMicrocontainerTest;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.microcontainer.beans2.Dependency;
import org.jboss.test.microcontainer.beans2.POJO;
import org.jboss.test.microcontainer.beans2.TestInterceptorWithDependency;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class InterceptorWithDependencyBeansTestCase extends AOPMicrocontainerTest
{
   public static Test suite()
   {
      return suite(InterceptorWithDependencyBeansTestCase.class);
   }
   
   public InterceptorWithDependencyBeansTestCase(String test)
   {
      super(test);
   }
   
   public void testInterceptorWithDependencyCorrectOrder() throws Exception
   {
      deploy("InterceptorWithDependencyBeansTestCaseNotAutomatic0.xml");
      try
      {
         Dependency dependency = (Dependency) getBean("Dependency");
         assertNotNull(dependency);
         deploy("InterceptorWithDependencyBeansTestCaseNotAutomatic1.xml");
         try
         {
            validate();
            POJO pojo = (POJO) getBean("Intercepted");
            assertNotNull(pojo);
            pojo.method(2);
            assertNotNull(TestInterceptorWithDependency.invoked);
            assertTrue(dependency == TestInterceptorWithDependency.invoked);
         }
         finally
         {
            undeploy("InterceptorWithDependencyBeansTestCaseNotAutomatic1.xml");
         }
      }
      finally
      {
         undeploy("InterceptorWithDependencyBeansTestCaseNotAutomatic0.xml");
      }
   }
   
   public void testInterceptorWithDependencyWrongOrder() throws Exception
   {
      deploy("InterceptorWithDependencyBeansTestCaseNotAutomatic1.xml");
      try
      {
         POJO pojo;
         try
         {
            pojo = (POJO) getBean("Intercepted");
            fail("'Interceped' should not be installed yet");
         }
         catch (IllegalStateException expected)
         {
         }
         
         pojo = (POJO) getBean("Intercepted", ControllerState.DESCRIBED);
         assertNull("This should not be deployed until the interceptor is", pojo);
         
         deploy("InterceptorWithDependencyBeansTestCaseNotAutomatic0.xml");
         try
         {
            validate();
            Dependency dependency = (Dependency) getBean("Dependency");
            assertNotNull(dependency);
            pojo = (POJO) getBean("Intercepted");
            assertNotNull(pojo);
            pojo.method(2);
            assertTrue(dependency == TestInterceptorWithDependency.invoked);
         }
         finally
         {
            undeploy("InterceptorWithDependencyBeansTestCaseNotAutomatic0.xml");
         }
      }
      finally
      {
         undeploy("InterceptorWithDependencyBeansTestCaseNotAutomatic1.xml");
      }
   }
   
   public void testInterceptorWithDependencyRedeploy() throws Exception
   {
      deploy("InterceptorWithDependencyBeansTestCaseNotAutomatic1.xml");
      try
      {
         POJO pojo;
         try
         {
            pojo = (POJO) getBean("Intercepted");
            fail("Bean should not be installed until the dependency is");
         }
         catch (IllegalStateException expected)
         {
            KernelControllerContext context = getControllerContext("Intercepted", ControllerState.DESCRIBED);
            assertNotNull(context);
         }

         deploy("InterceptorWithDependencyBeansTestCaseNotAutomatic0.xml");
         try
         {
            validate();
            Dependency dependency = (Dependency) getBean("Dependency");
            assertNotNull(dependency);
            pojo = (POJO) getBean("Intercepted");
            assertNotNull(pojo);
            pojo.method(2);
            assertTrue(dependency == TestInterceptorWithDependency.invoked);
         }
         finally
         {
            undeploy("InterceptorWithDependencyBeansTestCaseNotAutomatic0.xml");
         }
         
         try
         {
            pojo = (POJO) getBean("Intercepted");
            fail("Bean should not be installed after the dependency is undeployed");
         }
         catch (IllegalStateException expected)
         {
            KernelControllerContext context = getControllerContext("Intercepted", ControllerState.DESCRIBED);
            assertNotNull(context);
         }

         
         deploy("InterceptorWithDependencyBeansTestCaseNotAutomatic0.xml");
         try
         {
            validate();
            Dependency dependency = (Dependency) getBean("Dependency");
            assertNotNull(dependency);
            pojo = (POJO) getBean("Intercepted");
            assertNotNull(pojo);
            int called = TestInterceptorWithDependency.called;
            pojo.method(2);
            assertTrue("Interceptor was not rebound", called + 1 == TestInterceptorWithDependency.called);
            assertTrue("Should not be caching the interceptor/dependency across rebinding", dependency == TestInterceptorWithDependency.invoked);
         }
         finally
         {
            undeploy("InterceptorWithDependencyBeansTestCaseNotAutomatic0.xml");
         }
      }
      finally
      {
         undeploy("InterceptorWithDependencyBeansTestCaseNotAutomatic1.xml");
      }
   }
   
      
   public void testInterceptorWithDependencyRedeploy2() throws Exception
   {
      deploy("InterceptorWithDependencyBeansTestCaseNotAutomatic0.xml");
      try
      {
         Dependency dependency = (Dependency) getBean("Dependency");
         assertNotNull(dependency);
         deploy("InterceptorWithDependencyBeansTestCaseNotAutomatic1.xml");
         try
         {
            validate();
            POJO pojo = (POJO) getBean("Intercepted");
            assertNotNull(pojo);
            pojo.method(2);
            assertTrue(dependency == TestInterceptorWithDependency.invoked);
         }
         finally
         {
            undeploy("InterceptorWithDependencyBeansTestCaseNotAutomatic1.xml");
         }

         dependency = (Dependency) getBean("Dependency");
         assertNotNull(dependency);
         deploy("InterceptorWithDependencyBeansTestCaseNotAutomatic1.xml");
         try
         {
            POJO pojo = (POJO) getBean("Intercepted");
            assertNotNull(pojo);
            pojo.method(2);
            assertTrue(dependency == TestInterceptorWithDependency.invoked);
         }
         finally
         {
            undeploy("InterceptorWithDependencyBeansTestCaseNotAutomatic1.xml");
         }
      }
      finally
      {
         undeploy("InterceptorWithDependencyBeansTestCaseNotAutomatic0.xml");
      }
   }

   
}