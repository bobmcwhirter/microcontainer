/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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

import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.support.LifecycleCallbackWithBeanDependency;
import org.jboss.test.microcontainer.support.SimpleBean;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class InitialLifecycleCallbackWithDependencyTestCase extends AOPMicrocontainerTest
{
   public static Test suite()
   {
      return suite(InitialLifecycleCallbackWithDependencyTestCase.class);
   }
   
   public InitialLifecycleCallbackWithDependencyTestCase(String name)
   {
      super(name);
   }

   public void testLifecycleInterceptionsWithDependencyCorrectOrder() throws Exception
   {
      deploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
      try
      {
         Object dependency = getCheckBeanExists("Dependency");
         assertNotNull(dependency);
         boolean itworked = false;

         LifecycleCallbackWithBeanDependency.interceptions.clear();
         deploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
         try
         {
            validate();
            getCheckBeanExists("Intercepted");
            LifecycleCallbackWithBeanDependency lifecycle = (LifecycleCallbackWithBeanDependency)getCheckBeanExists("LifecycleCallback");
            assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
            LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
            assertEquals("Intercepted", handled.contextName);
            assertEquals(ControllerState.CONFIGURED, handled.toState);
            assertNotNull(lifecycle.getDependency());
            assertEquals(dependency, lifecycle.getDependency());
            itworked = true;
         }
         finally
         {
            LifecycleCallbackWithBeanDependency.interceptions.clear();
            undeploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
            if (itworked)
            {
               assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
               LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
               assertEquals("Intercepted", handled.contextName);
               assertEquals(ControllerState.CONFIGURED, handled.toState);
            }
         }
      }
      finally
      {
         undeploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
      }
   }
   
   public void testLifecycleInterceptionsWithDependencyWrongOrder() throws Exception
   {
      deploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
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

         boolean itworked = false;
         LifecycleCallbackWithBeanDependency.interceptions.clear();
         deploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
         try
         {
            validate();
            Object dependency = getCheckBeanExists("Dependency");
            LifecycleCallbackWithBeanDependency lifecycle = (LifecycleCallbackWithBeanDependency)getCheckBeanExists("LifecycleCallback");
            getCheckBeanExists("Intercepted");
            assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
            LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
            assertEquals("Intercepted", handled.contextName);
            assertEquals(ControllerState.CONFIGURED, handled.toState);
            assertNotNull(lifecycle.getDependency());
            assertEquals(dependency, lifecycle.getDependency());
            
            itworked = true;
            
         }
         finally
         {
            LifecycleCallbackWithBeanDependency.interceptions.clear();
            undeploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
            if (itworked)
            {
               assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
               LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
               assertEquals("Intercepted", handled.contextName);
               assertEquals(ControllerState.CONFIGURED, handled.toState);
            }

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
         }
      }
      finally
      {
         undeploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
      }
   }
   
   public void testLifecycleInterceptionsWithDependencyRedeploy() throws Exception
   {
      deploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
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

         boolean itworked = false;
         LifecycleCallbackWithBeanDependency.interceptions.clear();
         deploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
         try
         {
            validate();
            Object dependency = getCheckBeanExists("Dependency");
            LifecycleCallbackWithBeanDependency lifecycle = (LifecycleCallbackWithBeanDependency)getCheckBeanExists("LifecycleCallback");
            getCheckBeanExists("Intercepted");
            assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
            LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
            assertEquals("Intercepted", handled.contextName);
            assertEquals(ControllerState.CONFIGURED, handled.toState);
            assertNotNull(lifecycle.getDependency());
            assertEquals(dependency, lifecycle.getDependency());
            
            itworked = true;
            
         }
         finally
         {
            LifecycleCallbackWithBeanDependency.interceptions.clear();
            undeploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
            if (itworked)
            {
               assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
               LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
               assertEquals("Intercepted", handled.contextName);
               assertEquals(ControllerState.CONFIGURED, handled.toState);
            }

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
         }
         
         try
         {
            bean = (SimpleBean) getBean("Intercepted");
            fail("'Interceped' should not be installed yet");
         }
         catch (IllegalStateException expected)
         {
            KernelControllerContext context = getControllerContext("Intercepted", ControllerState.DESCRIBED);
            assertNotNull(context);
         }

         itworked = false;
         LifecycleCallbackWithBeanDependency.interceptions.clear();
         deploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
         try
         {
            validate();
            Object dependency = getCheckBeanExists("Dependency");
            LifecycleCallbackWithBeanDependency lifecycle = (LifecycleCallbackWithBeanDependency)getCheckBeanExists("LifecycleCallback");
            getCheckBeanExists("Intercepted");
            assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
            LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
            assertEquals("Intercepted", handled.contextName);
            assertEquals(ControllerState.CONFIGURED, handled.toState);
            assertNotNull(lifecycle.getDependency());
            assertTrue("Should not be caching the lifecycle's callback across rebinding", dependency == lifecycle.getDependency());
            
            itworked = true;
         }
         finally
         {
            LifecycleCallbackWithBeanDependency.interceptions.clear();
            undeploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
            if (itworked)
            {
               assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
               LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
               assertEquals("Intercepted", handled.contextName);
               assertEquals(ControllerState.CONFIGURED, handled.toState);
            }

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
         }
      }
      finally
      {
         undeploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
      }
   }

   public void testLifecycleInterceptionsWithDependencyRedeploy2() throws Exception
   {
      deploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
      try
      {
         Object dependency = getCheckBeanExists("Dependency");
         boolean itworked = false;

         LifecycleCallbackWithBeanDependency.interceptions.clear();
         deploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
         try
         {
            validate();
            getCheckBeanExists("Intercepted");
            LifecycleCallbackWithBeanDependency lifecycle = (LifecycleCallbackWithBeanDependency)getCheckBeanExists("LifecycleCallback");
            assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
            LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
            assertEquals("Intercepted", handled.contextName);
            assertEquals(ControllerState.CONFIGURED, handled.toState);
            assertNotNull(lifecycle.getDependency());
            assertTrue(dependency == lifecycle.getDependency());
            itworked = true;
         }
         finally
         {
            LifecycleCallbackWithBeanDependency.interceptions.clear();
            undeploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
            if (itworked)
            {
               assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
               LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
               assertEquals("Intercepted", handled.contextName);
               assertEquals(ControllerState.CONFIGURED, handled.toState);
            }
         }
         
         Object dependency2 = getCheckBeanExists("Dependency");
         assertTrue(dependency == dependency2);
         LifecycleCallbackWithBeanDependency.interceptions.clear();
         deploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
         try
         {
            validate();
            getCheckBeanExists("Intercepted");
            LifecycleCallbackWithBeanDependency lifecycle = (LifecycleCallbackWithBeanDependency)getCheckBeanExists("LifecycleCallback");
            assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
            LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
            assertEquals("Intercepted", handled.contextName);
            assertEquals(ControllerState.CONFIGURED, handled.toState);
            assertNotNull(lifecycle.getDependency());
            assertTrue(dependency == lifecycle.getDependency());
            itworked = true;
         }
         finally
         {
            LifecycleCallbackWithBeanDependency.interceptions.clear();
            undeploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
            if (itworked)
            {
               assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
               LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
               assertEquals("Intercepted", handled.contextName);
               assertEquals(ControllerState.CONFIGURED, handled.toState);
            }
         }
      }
      finally
      {
         undeploy("InitialLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
      }
   }
   
   public Object getCheckBeanExists(String name)
   {
      Object bean = getBean(name);
      assertNotNull(bean);
      return bean;
   }
}
