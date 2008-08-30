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

import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.support.InterceptorWithAnnotationDependency;
import org.jboss.test.microcontainer.support.OverriddenPropertyAnnotatedBean;
import org.jboss.test.microcontainer.support.SimpleBean;

public abstract class InterceptorWithOverriddenMethodAnnotationDependencyBaseTest extends AOPMicrocontainerTest
{
   public InterceptorWithOverriddenMethodAnnotationDependencyBaseTest(String name)
   {
      super(name);
   }
   
   public void testInterceptorWithAnnotationDependencyCorrectOrder() throws Exception
   {
      deploy(getDependency1DD());
      deploy(getDependency2DD());
      try
      {
         SimpleBean dependency1 = (SimpleBean) getBean("Dependency1");
         assertNotNull(dependency1);
         SimpleBean dependency2 = (SimpleBean) getBean("Dependency2");
         assertNotNull(dependency2);
         deploy(getMainDD());
         try
         {
            checkInterceptedAndInjected("Depends1", "A", dependency1);
            checkInterceptedAndInjected("Depends1", "B", dependency1);
            checkInterceptedAndInjected("Depends2", "B", dependency2);
            checkInterceptedAndInjected("Depends2", "B", dependency2);
            checkInterceptedAndInjected("Depends1And2", "A", dependency1);
            checkInterceptedAndInjected("Depends1And2", "B", dependency2);
            checkNotInstalledAndDescribed("NotDeployable");
            checkNotInstalledAndDescribed("NotDeployable1");
            checkNotInstalledAndDescribed("NotDeployable2");
         }
         finally
         {
            undeploy(getMainDD());
         }
      }
      finally
      {
         undeploy(getDependency1DD());
         undeploy(getDependency2DD());
      }
   }

   public void testInterceptorWithAnnotationDependencyWrongOrderAndRedeploy() throws Exception
   {
      deploy(getMainDD());
      try
      {
         checkNotInstalledAndDescribed("Depends1");
         checkNotInstalledAndDescribed("Depends2");
         checkNotInstalledAndDescribed("Depends1And2");
         checkNotInstalledAndDescribed("NotDeployable");
         checkNotInstalledAndDescribed("NotDeployable1");
         checkNotInstalledAndDescribed("NotDeployable2");

         deploy(getDependency1DD());
         try
         {
            SimpleBean dependency1 = (SimpleBean) getBean("Dependency1");
            assertNotNull(dependency1);
            checkInterceptedAndInjected("Depends1", "A", dependency1);
            checkInterceptedAndInjected("Depends1", "B", dependency1);
            checkNotInstalledAndDescribed("Depends2");
            checkNotInstalledAndDescribed("Depends1And2");
            checkNotInstalledAndDescribed("NotDeployable");
            checkNotInstalledAndDescribed("NotDeployable1");
            checkNotInstalledAndDescribed("NotDeployable2");

         }
         finally
         {
            undeploy(getDependency1DD());
            checkNotInstalledAndDescribed("Depends1");
            checkNotInstalledAndDescribed("Depends2");
            checkNotInstalledAndDescribed("Depends1And2");
            checkNotInstalledAndDescribed("NotDeployable");
            checkNotInstalledAndDescribed("NotDeployable1");
            checkNotInstalledAndDescribed("NotDeployable2");
         }

         deploy(getDependency2DD());
         try
         {
            SimpleBean dependency2 = (SimpleBean) getBean("Dependency2");
            assertNotNull(dependency2);
            checkNotInstalledAndDescribed("Depends1");
            checkInterceptedAndInjected("Depends2", "A", dependency2);
            checkInterceptedAndInjected("Depends2", "B", dependency2);
            checkNotInstalledAndDescribed("Depends1And2");
            checkNotInstalledAndDescribed("NotDeployable");
            checkNotInstalledAndDescribed("NotDeployable1");
            checkNotInstalledAndDescribed("NotDeployable2");

         }
         finally
         {
            undeploy(getDependency2DD());
            checkNotInstalledAndDescribed("Depends1");
            checkNotInstalledAndDescribed("Depends2");
            checkNotInstalledAndDescribed("Depends1And2");
            checkNotInstalledAndDescribed("NotDeployable");
            checkNotInstalledAndDescribed("NotDeployable1");
            checkNotInstalledAndDescribed("NotDeployable2");
         }

         deploy(getDependency1DD());
         deploy(getDependency2DD());
         try
         {
            SimpleBean dependency1 = (SimpleBean) getBean("Dependency1");
            assertNotNull(dependency1);
            SimpleBean dependency2 = (SimpleBean) getBean("Dependency2");
            assertNotNull(dependency2);
            checkInterceptedAndInjected("Depends1", "A", dependency1);
            checkInterceptedAndInjected("Depends1", "B", dependency1);
            checkInterceptedAndInjected("Depends2", "A", dependency2);
            checkInterceptedAndInjected("Depends2", "B", dependency2);
            checkInterceptedAndInjected("Depends1And2", "A", dependency1);
            checkInterceptedAndInjected("Depends1And2", "B", dependency2);
            checkNotInstalledAndDescribed("NotDeployable");
            checkNotInstalledAndDescribed("NotDeployable1");
            checkNotInstalledAndDescribed("NotDeployable2");

         }
         finally
         {
            undeploy(getDependency1DD());
            undeploy(getDependency2DD());
            checkNotInstalledAndDescribed("Depends1");
            checkNotInstalledAndDescribed("Depends2");
            checkNotInstalledAndDescribed("Depends1And2");
            checkNotInstalledAndDescribed("NotDeployable");
            checkNotInstalledAndDescribed("NotDeployable1");
            checkNotInstalledAndDescribed("NotDeployable2");
         }
      }
      finally
      {
         undeploy(getMainDD());
         checkNotInstalledAndNotDescribed("Depends1");
         checkNotInstalledAndNotDescribed("Depends2");
         checkNotInstalledAndNotDescribed("Depends1And2");
         checkNotInstalledAndNotDescribed("NotDeployable");
         checkNotInstalledAndNotDescribed("NotDeployable1");
         checkNotInstalledAndNotDescribed("NotDeployable2");
      }
   }

   protected void configureLogging()
   {
      //enableTrace("org.jboss.kernel.plugins.dependency");
   }


   private void checkInterceptedAndInjected(String name, String property, SimpleBean dependency)
   {
      InterceptorWithAnnotationDependency.intercepted = null;
      OverriddenPropertyAnnotatedBean bean = (OverriddenPropertyAnnotatedBean) getBean(name);
      assertNotNull(bean);
      
      if (property.equals("A"))
      {
         bean.setPropertyA(5);   
      }
      else if (property.equals("B"))
      {
         bean.setPropertyB(5);   
      }
      else
      {
         throw new RuntimeException("Invalid property passed in");
      }
      
      assertTrue(dependency == InterceptorWithAnnotationDependency.intercepted);
   }   

   private void checkNotInstalledAndNotDescribed(String name)
   {
      try
      {
         getBean(name);
         fail("'" + name + "' should not be installed yet");
      }
      catch (IllegalStateException expected)
      {
      }
      
      try
      {
         getBean(name, ControllerState.DESCRIBED);
         fail("'" + name + "' should not be described");
      }
      catch(IllegalStateException expected)
      {
         
      }
   }
   
   private void checkNotInstalledAndDescribed(String name)
   {
      try
      {
         getBean(name);
         fail("'" + name + "' should not be installed yet");
      }
      catch (IllegalStateException expected)
      {
         KernelControllerContext context = getControllerContext(name, ControllerState.DESCRIBED);
         assertNotNull(context);
      }
   }
   
   protected abstract String getDependency1DD();
   
   protected abstract String getDependency2DD();
   
   protected abstract String getMainDD();
}
