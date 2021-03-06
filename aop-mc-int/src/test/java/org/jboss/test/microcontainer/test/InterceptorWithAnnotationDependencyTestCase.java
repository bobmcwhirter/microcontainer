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

import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.support.InterceptorWithAnnotationDependency;
import org.jboss.test.microcontainer.support.SimpleBean;
import org.jboss.test.microcontainer.support.SimpleBeanImpl;

public class InterceptorWithAnnotationDependencyTestCase extends AOPMicrocontainerTest
{
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      return suite(InterceptorWithAnnotationDependencyTestCase.class);
   }
   
   public InterceptorWithAnnotationDependencyTestCase(String name)
   {
      super(name);
   }
   
   public void testInterceptorWithAnnotationDependencyCorrectOrder() throws Exception
   {
      deploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic0.xml");
      try
      {
         SimpleBean dependency = (SimpleBean) getBean("Dependency");
         assertNotNull(dependency);
         deploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic1.xml");
         try
         {
            validate();
            checkInterceptedAndInjected("Intercepted", dependency);
            checkInterceptedAndInjected("AnnotatedIntercepted", dependency);
            checkInterceptedAndInjected("MethodAnnotatedIntercepted", dependency);
            
            //No way to annotate methods via xml yet
            //checkInterceptedAndInjected("MethodIntercepted", dependency);
            checkInterceptedAndInjectedMethodAnnotatedXml(dependency);
         }
         finally
         {
            undeploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic1.xml");
         }
      }
      finally
      {
         undeploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic0.xml");
      }
   }

   public void testInterceptorWithAnnotationDependencyWrongOrder() throws Exception
   {
      deploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic1.xml");
      try
      {
         checkNotInstalledAndNotDescribed("Intercepted");
         checkNotInstalledAndNotDescribed("AnnotatedIntercepted");
         checkNotInstalledAndNotDescribed("MethodIntercepted");
         checkNotInstalledAndNotDescribed("MethodAnnotatedIntercepted");
         
         deploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic0.xml");
         try
         {
            validate();
            SimpleBean dependency = (SimpleBean) getBean("Dependency");
            assertNotNull(dependency);

            checkInterceptedAndInjected("Intercepted", dependency);
            checkInterceptedAndInjected("AnnotatedIntercepted", dependency);
            checkInterceptedAndInjected("MethodAnnotatedIntercepted", dependency);

            //No way to annotate methods via xml yet
            //checkInterceptedAndInjected("MethodIntercepted", dependency);
            checkInterceptedAndInjectedMethodAnnotatedXml(dependency);
         }
         finally
         {
            undeploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic0.xml");
         }
      }
      finally
      {
         undeploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic1.xml");
      }
   }

   public void testInterceptorWithAnnotationDependencyRedeploy() throws Exception
   {
      deploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic1.xml");
      try
      {
         checkNotInstalledAndDescribed("Intercepted");
         checkNotInstalledAndDescribed("AnnotatedIntercepted");
         checkNotInstalledAndDescribed("MethodIntercepted");
         checkNotInstalledAndDescribed("MethodAnnotatedIntercepted");
         
         deploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic0.xml");
         try
         {
            validate();
            SimpleBean dependency = (SimpleBean) getBean("Dependency");
            assertNotNull(dependency);

            checkInterceptedAndInjected("Intercepted", dependency);
            checkInterceptedAndInjected("AnnotatedIntercepted", dependency);
            checkInterceptedAndInjected("MethodAnnotatedIntercepted", dependency);

            checkInterceptedAndInjectedMethodAnnotatedXml(dependency);
         }
         finally
         {
            undeploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic0.xml");
         }
         
         checkNotInstalledAndDescribed("Intercepted");
         checkNotInstalledAndDescribed("AnnotatedIntercepted");
         checkNotInstalledAndDescribed("MethodIntercepted");
         checkNotInstalledAndDescribed("MethodAnnotatedIntercepted");
         
         deploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic0.xml");
         try
         {
            validate();
            SimpleBean dependency = (SimpleBean) getBean("Dependency");
            assertNotNull(dependency);
            
            checkInterceptedAndInjected("Intercepted", dependency);
            checkInterceptedAndInjected("AnnotatedIntercepted", dependency);
            checkInterceptedAndInjected("MethodAnnotatedIntercepted", dependency);

            checkInterceptedAndInjectedMethodAnnotatedXml(dependency);
         }
         finally
         {
            undeploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic0.xml");
         }
      }
      finally
      {
         undeploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic1.xml");
      }
   }
      
   public void testInterceptorWithAnnotationDependencyRedeploy2() throws Exception
   {
      deploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic0.xml");
      try
      {
         SimpleBean dependency = (SimpleBean) getBean("Dependency");
         assertNotNull(dependency);
         deploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic1.xml");
         try
         {
            validate();
            
            checkInterceptedAndInjected("Intercepted", dependency);
            checkInterceptedAndInjected("AnnotatedIntercepted", dependency);
            checkInterceptedAndInjected("MethodAnnotatedIntercepted", dependency);

            checkInterceptedAndInjectedMethodAnnotatedXml(dependency);
         }
         finally
         {
            undeploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic1.xml");
         }

         dependency = (SimpleBean) getBean("Dependency");
         assertNotNull(dependency);
         deploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic1.xml");
         try
         {
            checkInterceptedAndInjected("Intercepted", dependency);
            checkInterceptedAndInjected("AnnotatedIntercepted", dependency);
            checkInterceptedAndInjected("MethodAnnotatedIntercepted", dependency);

            checkInterceptedAndInjectedMethodAnnotatedXml(dependency);
         }
         finally
         {
            undeploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic1.xml");
         }
      }
      finally
      {
         undeploy("InterceptorWithAnnotationDependencyTestCaseNotAutomatic0.xml");
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
      assertTrue(dependency + "==" + InterceptorWithAnnotationDependency.intercepted, dependency == InterceptorWithAnnotationDependency.intercepted);
   }   

   private void checkInterceptedAndInjectedMethodAnnotatedXml(SimpleBean dependency)
   {
      InterceptorWithAnnotationDependency.intercepted = null;
      SimpleBeanImpl bean = (SimpleBeanImpl) getBean("MethodIntercepted");
      assertNotNull(bean);
      bean.setProperty(1);
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
      bean = (SimpleBean) getBean(name, ControllerState.DESCRIBED);
      assertNull("'" + name + "' should not be deployed until the interceptor is", bean);
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
