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
import org.jboss.test.microcontainer.support.LifecycleCallbackWithBeanDependency;
import org.jboss.test.microcontainer.support.SimpleBean;

/**
 * Tests the beans that are created under the hood using the <aop:configure/> <aop:instantiate/> etc.
 * configuration 
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class CreatedBeansLifecycleCallbackWithDependencyTestCase extends AOPMicrocontainerTest
{
   public static Test suite()
   {
      return suite(CreatedBeansLifecycleCallbackWithDependencyTestCase.class);
   }
   
   public CreatedBeansLifecycleCallbackWithDependencyTestCase(String name)
   {
      super(name);
   }

   public void testLifecycleInterceptionsWithDependencyCorrectOrder() throws Exception
   {
      deploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
      try
      {
         Object dependency = getCheckBeanExists("Dependency");
         assertNotNull(dependency);
         boolean itworked = false;

         LifecycleCallbackWithBeanDependency.interceptions.clear();
         deploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
         try
         {
            validate();
            getCheckBeanExists("Intercepted");
            LifecycleCallbackWithBeanDependency lifecycle = (LifecycleCallbackWithBeanDependency)getCheckBeanExists("LifecycleCallback");
            assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
            LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
            assertEquals("Intercepted", handled.contextName);
            assertEquals(ControllerState.INSTANTIATED, handled.fromState);
            assertNotNull(lifecycle.getDependency());
            assertEquals(dependency, lifecycle.getDependency());
            itworked = true;
         }
         finally
         {
            LifecycleCallbackWithBeanDependency.interceptions.clear();
            undeploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
            if (itworked)
            {
               assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
               LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
               assertEquals("Intercepted", handled.contextName);
               assertEquals(ControllerState.CONFIGURED, handled.fromState);
            }
         }
      }
      finally
      {
         undeploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
      }
   }
   
   public void testLifecycleInterceptionsWithDependencyWrongOrder() throws Exception
   {
      deploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
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
         deploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
         try
         {
            validate();
            Object dependency = getCheckBeanExists("Dependency");
            LifecycleCallbackWithBeanDependency lifecycle = (LifecycleCallbackWithBeanDependency)getCheckBeanExists("LifecycleCallback");
            getCheckBeanExists("Intercepted");
            assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
            LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
            assertEquals("Intercepted", handled.contextName);
            assertEquals(ControllerState.INSTANTIATED, handled.fromState);
            assertNotNull(lifecycle.getDependency());
            assertEquals(dependency, lifecycle.getDependency());
            
            itworked = true;
            
         }
         finally
         {
            LifecycleCallbackWithBeanDependency.interceptions.clear();
            undeploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
            if (itworked)
            {
               assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
               LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
               assertEquals("Intercepted", handled.contextName);
               assertEquals(ControllerState.CONFIGURED, handled.fromState);
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
         undeploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
      }
   }
   
   public void testLifecycleInterceptionsWithDependencyRedeploy() throws Exception
   {
      deploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
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
         deploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
         try
         {
            validate();
            Object dependency = getCheckBeanExists("Dependency");
            LifecycleCallbackWithBeanDependency lifecycle = (LifecycleCallbackWithBeanDependency)getCheckBeanExists("LifecycleCallback");
            getCheckBeanExists("Intercepted");
            assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
            LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
            assertEquals("Intercepted", handled.contextName);
            assertEquals(ControllerState.INSTANTIATED, handled.fromState);
            assertNotNull(lifecycle.getDependency());
            assertEquals(dependency, lifecycle.getDependency());
            
            itworked = true;
            
         }
         finally
         {
            LifecycleCallbackWithBeanDependency.interceptions.clear();
            undeploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
            if (itworked)
            {
               assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
               LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
               assertEquals("Intercepted", handled.contextName);
               assertEquals(ControllerState.CONFIGURED, handled.fromState);
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
         deploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
         try
         {
            validate();
            Object dependency = getCheckBeanExists("Dependency");
            LifecycleCallbackWithBeanDependency lifecycle = (LifecycleCallbackWithBeanDependency)getCheckBeanExists("LifecycleCallback");
            getCheckBeanExists("Intercepted");
            assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
            LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
            assertEquals("Intercepted", handled.contextName);
            assertEquals(ControllerState.INSTANTIATED, handled.fromState);
            assertNotNull(lifecycle.getDependency());
            assertTrue("Should not be caching the lifecycle's callback across rebinding", dependency == lifecycle.getDependency());
            
            itworked = true;
         }
         finally
         {
            LifecycleCallbackWithBeanDependency.interceptions.clear();
            undeploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
            if (itworked)
            {
               assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
               LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
               assertEquals("Intercepted", handled.contextName);
               assertEquals(ControllerState.CONFIGURED, handled.fromState);
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
         undeploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
      }
   }

   public void testLifecycleInterceptionsWithDependencyRedeploy2() throws Exception
   {
      deploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
      try
      {
         Object dependency = getCheckBeanExists("Dependency");
         boolean itworked = false;

         LifecycleCallbackWithBeanDependency.interceptions.clear();
         deploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
         try
         {
            validate();
            getCheckBeanExists("Intercepted");
            LifecycleCallbackWithBeanDependency lifecycle = (LifecycleCallbackWithBeanDependency)getCheckBeanExists("LifecycleCallback");
            assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
            LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
            assertEquals("Intercepted", handled.contextName);
            assertEquals(ControllerState.INSTANTIATED, handled.fromState);
            assertNotNull(lifecycle.getDependency());
            assertTrue(dependency == lifecycle.getDependency());
            itworked = true;
         }
         finally
         {
            LifecycleCallbackWithBeanDependency.interceptions.clear();
            undeploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
            if (itworked)
            {
               assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
               LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
               assertEquals("Intercepted", handled.contextName);
               assertEquals(ControllerState.CONFIGURED, handled.fromState);
            }
         }
         
         Object dependency2 = getCheckBeanExists("Dependency");
         assertTrue(dependency == dependency2);
         LifecycleCallbackWithBeanDependency.interceptions.clear();
         deploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
         try
         {
            validate();
            getCheckBeanExists("Intercepted");
            LifecycleCallbackWithBeanDependency lifecycle = (LifecycleCallbackWithBeanDependency)getCheckBeanExists("LifecycleCallback");
            assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
            LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
            assertEquals("Intercepted", handled.contextName);
            assertEquals(ControllerState.INSTANTIATED, handled.fromState);
            assertNotNull(lifecycle.getDependency());
            assertTrue(dependency == lifecycle.getDependency());
            itworked = true;
         }
         finally
         {
            LifecycleCallbackWithBeanDependency.interceptions.clear();
            undeploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic1.xml");
            if (itworked)
            {
               assertEquals(1, LifecycleCallbackWithBeanDependency.interceptions.size());
               LifecycleCallbackWithBeanDependency.Handled handled = LifecycleCallbackWithBeanDependency.interceptions.get(0);
               assertEquals("Intercepted", handled.contextName);
               assertEquals(ControllerState.CONFIGURED, handled.fromState);
            }
         }
      }
      finally
      {
         undeploy("CreatedBeansLifecycleCallbackWithDependencyTestCaseNotAutomatic0.xml");
      }
   }
   
   public Object getCheckBeanExists(String name)
   {
      Object bean = getBean(name);
      assertNotNull(bean);
      return bean;
   }
}
