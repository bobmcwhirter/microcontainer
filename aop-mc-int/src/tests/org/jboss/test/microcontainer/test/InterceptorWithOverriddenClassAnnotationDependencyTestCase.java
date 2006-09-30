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
import junit.textui.TestRunner;

import org.jboss.aop.microcontainer.junit.AOPMicrocontainerTest;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.microcontainer.support.InterceptorWithAnnotationDependency;
import org.jboss.test.microcontainer.support.SimpleBean;

public class InterceptorWithOverriddenClassAnnotationDependencyTestCase extends AOPMicrocontainerTest
{
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      return suite(InterceptorWithOverriddenClassAnnotationDependencyTestCase.class);
   }
   
   public InterceptorWithOverriddenClassAnnotationDependencyTestCase(String name)
   {
      super(name);
   }
   
   public void testInterceptorWithAnnotationDependencyCorrectOrder() throws Exception
   {
      deploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic0.xml");
      try
      {
         SimpleBean dependency = (SimpleBean) getBean("Dependency");
         assertNotNull(dependency);
         deploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic1.xml");
         try
         {
            checkInterceptedAndInjected("Intercepted", dependency);
            checkNotInstalledAndDescribed("AnnotatedIntercepted");
         }
         finally
         {
            undeploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic1.xml");
         }
      }
      finally
      {
         undeploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic0.xml");
      }
   }

   public void testInterceptorWithAnnotationDependencyWrongOrder() throws Exception
   {
      deploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic1.xml");
      try
      {
         checkNotInstalledAndDescribed("Intercepted");
         checkNotInstalledAndDescribed("AnnotatedIntercepted");
         
         deploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic0.xml");
         try
         {
            SimpleBean dependency = (SimpleBean) getBean("Dependency");
            assertNotNull(dependency);

            checkInterceptedAndInjected("Intercepted", dependency);
            checkNotInstalledAndDescribed("AnnotatedIntercepted");
         }
         finally
         {
            undeploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic0.xml");
            checkNotInstalledAndDescribed("Intercepted");
            checkNotInstalledAndDescribed("AnnotatedIntercepted");
         }
      }
      finally
      {
         undeploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic1.xml");
         checkNotInstalledAndNotDescribed("Intercepted");
         checkNotInstalledAndNotDescribed("AnnotatedIntercepted");
      }
   }

   public void testInterceptorWithAnnotationDependencyRedeploy() throws Exception
   {
      deploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic1.xml");
      try
      {
         checkNotInstalledAndDescribed("Intercepted");
         checkNotInstalledAndDescribed("AnnotatedIntercepted");
         
         deploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic0.xml");
         try
         {
            SimpleBean dependency = (SimpleBean) getBean("Dependency");
            assertNotNull(dependency);

            checkInterceptedAndInjected("Intercepted", dependency);
            checkNotInstalledAndDescribed("AnnotatedIntercepted");
         }
         finally
         {
            undeploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic0.xml");
         }
         
         checkNotInstalledAndDescribed("Intercepted");
         checkNotInstalledAndDescribed("AnnotatedIntercepted");
         
         deploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic0.xml");
         try
         {
            SimpleBean dependency = (SimpleBean) getBean("Dependency");
            assertNotNull(dependency);
            
            checkInterceptedAndInjected("Intercepted", dependency);
            checkNotInstalledAndDescribed("AnnotatedIntercepted");
         }
         finally
         {
            undeploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic0.xml");
         }
      }
      finally
      {
         undeploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic1.xml");
         checkNotInstalledAndNotDescribed("Intercepted");
         checkNotInstalledAndNotDescribed("AnnotatedIntercepted");
      }
   }
      
   public void testInterceptorWithAnnotationDependencyRedeploy2() throws Exception
   {
      deploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic0.xml");
      try
      {
         SimpleBean dependency = (SimpleBean) getBean("Dependency");
         assertNotNull(dependency);
         deploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic1.xml");
         try
         {
            checkInterceptedAndInjected("Intercepted", dependency);
            checkNotInstalledAndDescribed("AnnotatedIntercepted");
         }
         finally
         {
            undeploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic1.xml");
         }

         dependency = (SimpleBean) getBean("Dependency");
         assertNotNull(dependency);
         deploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic1.xml");
         try
         {
            checkInterceptedAndInjected("Intercepted", dependency);
            checkNotInstalledAndDescribed("AnnotatedIntercepted");
         }
         finally
         {
            undeploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic1.xml");
            checkNotInstalledAndNotDescribed("Intercepted");
            checkNotInstalledAndNotDescribed("AnnotatedIntercepted");
         }
      }
      finally
      {
         undeploy("InterceptorWithOverriddenClassAnnotationDependencyTestCaseNotAutomatic0.xml");
      }
   }

   
   protected void configureLogging()
   {
      //enableTrace("org.jboss.kernel.plugins.dependency");
   }


   private void checkInterceptedAndInjected(String name, SimpleBean dependency)
   {
      InterceptorWithAnnotationDependency.intercepted = null;
      SimpleBean bean = (SimpleBean) getBean(name);
      assertNotNull(bean);
      bean.someMethod();
      assertTrue(dependency == InterceptorWithAnnotationDependency.intercepted);
   }   

   private void checkNotInstalledAndNotDescribed(String name)
   {
      SimpleBean bean;
      try
      {
         bean = (SimpleBean) getBean(name);
         fail("'" + name + "' should not be installed yet");
      }
      catch (IllegalStateException expected)
      {
      }
      
      try
      {
         bean = (SimpleBean) getBean(name, ControllerState.DESCRIBED);
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
}
