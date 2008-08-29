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
package org.jboss.test.spring.test;

import junit.framework.Test;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.spring.support.BeanNameAwareBean;
import org.jboss.test.spring.support.IntBeanNameAwareBean;

/**
 * Name aware test.
 *
 * @author John Bailey
 * @author Davide Panelli
 */
public class BeanNameAwareSpringTestCase extends TempSpringMicrocontainerTest
{
   public BeanNameAwareSpringTestCase(String name)
   {
      super(name);
   }

   /**
    * Setup the test
    *
    * @return the test
    */
   public static Test suite()
   {
      return suite(BeanNameAwareSpringTestCase.class);
   }

   public void testConfigure() throws Exception
   {
      BeanNameAwareBean testBean = (BeanNameAwareBean) getBean("testBean", ControllerState.INSTANTIATED);
      assertNotNull(testBean);
      assertEquals("testBean" , testBean.getBeanName());
      BeanNameAwareBean subTestBean = (BeanNameAwareBean) getBean("subTestBean", ControllerState.INSTANTIATED);
      assertNotNull(subTestBean);
      assertEquals("subTestBean" , subTestBean.getBeanName());
      IntBeanNameAwareBean intTestBean = (IntBeanNameAwareBean) getBean("intTestBean", ControllerState.INSTANTIATED);
      assertNotNull(intTestBean);
      assertEquals("intTestBean" , intTestBean.getName());

      BeanNameAwareBean otherBean = (BeanNameAwareBean) getBean("testBeanWithExisingName", ControllerState.INSTANTIATED);
      assertNotNull(otherBean);
      assertEquals("OtherBean" , otherBean.getBeanName());
      BeanNameAwareBean subOtherBean = (BeanNameAwareBean) getBean("subTestBeanWithExisingName", ControllerState.INSTANTIATED);
      assertNotNull(subOtherBean);
      assertEquals("subOtherBean" , subOtherBean.getBeanName());
      IntBeanNameAwareBean intOtherBean = (IntBeanNameAwareBean) getBean("intTestBeanWithExisingName", ControllerState.INSTANTIATED);
      assertNotNull(intOtherBean);
      assertEquals("intOtherBean" , intOtherBean.getName());
   }
}
