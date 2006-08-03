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

import org.jboss.aop.microcontainer.beans.Aspect;
import org.jboss.aop.microcontainer.beans.AspectBinding;
import org.jboss.aop.microcontainer.junit.AOPMicrocontainerTest;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactory;
import org.jboss.test.microcontainer.support.CalledInterceptor;
import org.jboss.test.microcontainer.support.InterceptorWithDependency;
import org.jboss.test.microcontainer.support.SimpleBean;

public class InterceptedUseCaseXmlTestCase extends AOPMicrocontainerTest
{
   public static Test suite()
   {
      return suite(InterceptedUseCaseXmlTestCase.class);
   }
   
   public InterceptedUseCaseXmlTestCase(String name)
   {
      super(name);
   }
   
   public void testIntercepted()
   {
      SimpleBean bean = (SimpleBean) getBean("Intercepted");
      SimpleBean dependency = (SimpleBean) getBean("Dependency");
      CalledInterceptor.intercepted = false;
      InterceptorWithDependency.intercepted = null;
      bean.someMethod();
      assertTrue("Should have invoked the CalledInterceptor", CalledInterceptor.intercepted);
      assertTrue("Should have invoked the DependentInterceptor", InterceptorWithDependency.intercepted == dependency);
   }
   
   public void testCheckNotDependentAdvice()
   {
      GenericBeanFactory factory = (GenericBeanFactory)getBean("InterceptedAdvice");
      assertNotNull(factory);
      
      AspectBinding binding = (AspectBinding)getBean("InterceptedAdvice$AspectBinding");
      assertNotNull(binding);
      
      Aspect aspect = (Aspect)getBean("InterceptedAdvice$Aspect");
      assertNotNull(aspect);
      assertNotNull(aspect.getAdvice());
      assertNull(aspect.getAdviceBean());
   }

   public void testCheckDependentAdvice()
   {
      GenericBeanFactory factory = (GenericBeanFactory)getBean("DependentAdvice");
      assertNotNull(factory);
      
      AspectBinding binding = (AspectBinding)getBean("DependentAdvice$AspectBinding");
      assertNotNull(binding);
      
      Aspect aspect = (Aspect)getBean("DependentAdvice$Aspect");
      assertNotNull(aspect);
      assertNotNull(aspect.getAdviceBean());
      assertNotNull(aspect.getAdvice()); //Gets set by the install

   }
}
