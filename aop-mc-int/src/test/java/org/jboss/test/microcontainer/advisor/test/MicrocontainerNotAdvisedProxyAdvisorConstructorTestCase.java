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
import org.jboss.aop.proxy.container.AspectManaged;
import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.advisor.test.some.Pojo;

/**
 * MicrocontainerAdvisedAdvisorTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class MicrocontainerNotAdvisedProxyAdvisorConstructorTestCase extends AOPMicrocontainerTest
{
   public void testAdvisor() throws Exception
   {
      Object o1 = getBean("One");
      assertFalse(o1 instanceof Advised);
      assertTrue(o1 instanceof AspectManaged);
      assertTrue(Pojo.class.isAssignableFrom(o1.getClass()));
      Pojo pojo1 = (Pojo)o1;

      Object o2 = getBean("Two");
      assertFalse(o2 instanceof Advised);
      assertTrue(o2 instanceof AspectManaged);
      assertTrue(Pojo.class.isAssignableFrom(o2.getClass()));
      Pojo pojo2 = (Pojo)o2;
      
      Object o3 = getBean("Three");
      assertFalse(o3 instanceof Advised);
      assertFalse(o3 instanceof AspectManaged);
      assertTrue(Pojo.class.equals(o3.getClass()));
      Pojo pojo3 = (Pojo)o3;
      
      Object o4 = getBean("Four");
      assertFalse(o4 instanceof Advised);
      assertTrue(o4 instanceof AspectManaged);
      assertTrue(org.jboss.test.microcontainer.advisor.test.another.Pojo.class.isAssignableFrom(o4.getClass()));
      assertTrue(o4 instanceof org.jboss.test.microcontainer.advisor.test.another.Pojo);

      assertTrue(pojo1.getInstance() == pojo2.getInstance());
      assertFalse(pojo1.getInstance() == pojo3.getInstance());
   }
   
   public static Test suite()
   {
      return suite(MicrocontainerNotAdvisedProxyAdvisorConstructorTestCase.class);
   }
   
   public MicrocontainerNotAdvisedProxyAdvisorConstructorTestCase(String test)
   {
      super(test);
   }
}
