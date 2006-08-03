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
import org.jboss.test.microcontainer.support.InterceptorWithNestedAnnotationDependency;
import org.jboss.test.microcontainer.support.NestedAnnotatedSimpleBeanImpl;
import org.jboss.test.microcontainer.support.SimpleBean;

public class InterceptorWithNestedAnnotationDependencyTestCase extends AOPMicrocontainerTest
{
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      return suite(InterceptorWithNestedAnnotationDependencyTestCase.class);
   }
   
   public InterceptorWithNestedAnnotationDependencyTestCase(String name)
   {
      super(name);
   }

   
   public void testInterceptorWithAnnotationDependencyCorrectOrder() throws Exception
   {
      deploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic0.xml");
      deploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic1.xml");
      deploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic2.xml");
      try
      {
         deploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic3.xml");
         try
         {
            checkInterceptedAndInjected("AnnotatedIntercepted");
         }
         finally
         {
            undeploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic3.xml");
         }
      }
      finally
      {
         undeploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic2.xml");
         undeploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic1.xml");
         undeploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic0.xml");
      }
   }

   public void testInterceptorWithAnnotationDependencyWrongOrderAndRedeploy() throws Exception
   {
      deploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic3.xml");
      try
      {
         checkNotInstalled("AnnotatedIntercepted");

         try
         {
            deploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic0.xml");
            checkNotInstalled("AnnotatedIntercepted");
            deploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic1.xml");
            checkNotInstalled("AnnotatedIntercepted");
            deploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic2.xml");
            checkInterceptedAndInjected("AnnotatedIntercepted");
            undeploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic2.xml");
            checkNotInstalled("AnnotatedIntercepted");
         }
         finally
         {
            undeploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic1.xml");
            undeploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic0.xml");
         }

         try
         {
            deploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic1.xml");
            checkNotInstalled("AnnotatedIntercepted");
            deploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic2.xml");
            checkNotInstalled("AnnotatedIntercepted");
            deploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic0.xml");
            checkInterceptedAndInjected("AnnotatedIntercepted");
            undeploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic0.xml");
            checkNotInstalled("AnnotatedIntercepted");
         }
         finally
         {
            undeploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic1.xml");
            undeploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic2.xml");
         }

         
         try
         {
            deploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic1.xml");
            checkNotInstalled("AnnotatedIntercepted");
            deploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic2.xml");
            checkNotInstalled("AnnotatedIntercepted");
            deploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic0.xml");
            checkInterceptedAndInjected("AnnotatedIntercepted");
            undeploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic0.xml");
            checkNotInstalled("AnnotatedIntercepted");
         }
         finally
         {
            undeploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic1.xml");
            undeploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic2.xml");
         }
      }
      finally
      {
         undeploy("InterceptorWithNestedAnnotationDependencyTestCaseNotAutomatic3.xml");
      }
   }

   protected void configureLogging()
   {
      //enableTrace("org.jboss.kernel.plugins.dependency");
   }


   private void checkInterceptedAndInjected(String name)
   {
      SimpleBean dependency1 = (SimpleBean) getBean("Dependency1");
      assertNotNull(dependency1);
      SimpleBean dependency2 = (SimpleBean) getBean("Dependency2");
      assertNotNull(dependency2);
      SimpleBean dependency3 = (SimpleBean) getBean("Dependency3");
      assertNotNull(dependency3);

      InterceptorWithAnnotationDependency.intercepted = null;
      NestedAnnotatedSimpleBeanImpl bean = (NestedAnnotatedSimpleBeanImpl) getBean(name);
      assertNotNull(bean);
      bean.someMethod();
      assertNotNull(InterceptorWithNestedAnnotationDependency.intercepted1);
      assertTrue(dependency1 == InterceptorWithNestedAnnotationDependency.intercepted1);
      assertNotNull(InterceptorWithNestedAnnotationDependency.intercepted2);
      assertTrue(dependency2 == InterceptorWithNestedAnnotationDependency.intercepted2);
      assertNotNull(InterceptorWithNestedAnnotationDependency.intercepted3);
      assertTrue(dependency3 == InterceptorWithNestedAnnotationDependency.intercepted3);
   }   

   private void checkNotInstalled(String name)
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
