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

import java.lang.reflect.Method;

import junit.framework.Test;

import org.jboss.aop.proxy.container.AspectManaged;
import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.support.AnnotatedBean;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 43840 $
 */
public class AnnotationsInProxyTestCase extends AOPMicrocontainerTest
{
   public static Test suite()
   {
      return suite(AnnotationsInProxyTestCase.class);
   }
   
   public AnnotationsInProxyTestCase(String name)
   {
      super(name);
   }
   
   public void testAnnotationsInProxy() throws Exception
   {
      AnnotatedBean bean = (AnnotatedBean)getBean("Bean");
      assertTrue(bean instanceof AspectManaged);
      org.jboss.test.microcontainer.support.Test test = bean.getClass().getAnnotation(org.jboss.test.microcontainer.support.Test.class);
      assertNotNull(test);
      
      Method method = bean.getClass().getMethod("method");
      test = method.getAnnotation(org.jboss.test.microcontainer.support.Test.class);
      assertNotNull(test);      
   }
}
