/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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

import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.support.SimpleLifecycleCallback;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class InitialLifecycleCallbackTestCase extends AOPMicrocontainerTest
{
   public static Test suite()
   {
      return suite(InitialLifecycleCallbackTestCase.class);
   }
   
   public InitialLifecycleCallbackTestCase(String name)
   {
      super(name);
   }

   public void testLifecycleInterceptions() throws Exception
   {
      boolean itworked = false;
      try
      {
         SimpleLifecycleCallback.interceptions.clear();
         deploy("InitialLifecycleCallbackTestCaseNotAutomatic.xml");
         getCheckBeanExists("Intercepted");

         assertEquals(1, SimpleLifecycleCallback.interceptions.size());
         SimpleLifecycleCallback.Handled handled = SimpleLifecycleCallback.interceptions.get(0);
         assertEquals("Intercepted", handled.contextName);
         assertEquals(ControllerState.CONFIGURED, handled.toState);

         SimpleLifecycleCallback callback = (SimpleLifecycleCallback)getBean("LifecycleCallback");
         assertEquals("Test123", callback.getTestProperty());
         itworked = true;
         
      }
      finally
      {
         SimpleLifecycleCallback.interceptions.clear();
         undeploy("InitialLifecycleCallbackTestCaseNotAutomatic.xml");
         if (itworked)
         {
            assertEquals(1, SimpleLifecycleCallback.interceptions.size());
            SimpleLifecycleCallback.Handled handled = SimpleLifecycleCallback.interceptions.get(0);
            assertEquals("Intercepted", handled.contextName);
            assertEquals(ControllerState.CONFIGURED, handled.toState);
         }
      }
   }
   
   public Object getCheckBeanExists(String name)
   {
      Object bean = getBean(name);
      assertNotNull(bean);
      return bean;
   }
}
