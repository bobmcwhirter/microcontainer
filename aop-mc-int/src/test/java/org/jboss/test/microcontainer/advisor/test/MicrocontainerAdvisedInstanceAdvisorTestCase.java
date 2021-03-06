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
package org.jboss.test.microcontainer.advisor.test;

import junit.framework.Test;

import org.jboss.aop.Advised;
import org.jboss.test.aop.junit.AOPMicrocontainerTest;

/**
 * MicrocontainerAdvisedInstanceAdvisorTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class MicrocontainerAdvisedInstanceAdvisorTestCase extends AOPMicrocontainerTest
{
   public void testAdvisor() throws Exception
   {
      TestObject o = (TestObject) getBean("Test");
      assertTrue(o instanceof Advised);
      TestInterceptor.intercepted = 0;
      DummyInterceptor.intercepted = 0;
      o.doSomething();
      assertEquals(1, TestInterceptor.intercepted);
      assertEquals(1, DummyInterceptor.intercepted);

      o = (TestObject) getBean("TestNoAnnotation");
      assertTrue(o instanceof Advised);
      TestInterceptor.intercepted = 0;
      DummyInterceptor.intercepted = 0;
      o.doSomething();
      assertEquals(0, TestInterceptor.intercepted);
      assertEquals(1, DummyInterceptor.intercepted);
   }
   
   public static Test suite()
   {
      return suite(MicrocontainerAdvisedInstanceAdvisorTestCase.class);
   }
   
   public MicrocontainerAdvisedInstanceAdvisorTestCase(String test)
   {
      super(test);
   }
}
