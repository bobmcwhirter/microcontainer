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
package org.jboss.test.kernel.inject.test;

import junit.framework.Test;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.kernel.inject.support.CallbackTestObject;
import org.jboss.test.kernel.junit.ManualMicrocontainerTest;

/**
 * Cardinality tests.
 *
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class CardinalityCallbackTestCase extends ManualMicrocontainerTest
{
   public CardinalityCallbackTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(CardinalityCallbackTestCase.class);
   }

   public void testCallback() throws Throwable
   {
      KernelControllerContext context = getControllerContext("testObject", ControllerState.NOT_INSTALLED);
      assertNull(context.getTarget());
      assertEquals(ControllerState.NOT_INSTALLED, context.getState());

      // move conetxt to change - triggering callback addition + cardinality dependency
      change(context, ControllerState.CREATE);
      CallbackTestObject injectee = (CallbackTestObject)context.getTarget();
      assertNotNull(injectee);
      assertNull(injectee.getTesterInterfaces());

      // install 1st - cardinality dependency not yet satisfied
      KernelControllerContext tester1 = getControllerContext("tester1", ControllerState.NOT_INSTALLED);
      assertNotNull(tester1);
      change(tester1, ControllerState.INSTALLED);

      assertNull(injectee.getTesterInterfaces());

      // install 1st - cardinality dependency now satisfied
      KernelControllerContext tester2 = getControllerContext("tester2", ControllerState.NOT_INSTALLED);
      assertNotNull(tester2);
      change(tester2, ControllerState.INSTALLED);

      // can be moved to installed
      change(context, ControllerState.INSTALLED);
      assertEquals(ControllerState.INSTALLED, context.getState());

      // collection should be there
      assertNotNull(injectee.getTesterInterfaces());
      assertEquals(2, injectee.getTesterInterfaces().size());

      // add another tester, should be injected as well
      KernelControllerContext tester3 = getControllerContext("tester3", ControllerState.NOT_INSTALLED);
      assertNotNull(tester3);
      change(tester3, ControllerState.INSTALLED);

      assertNotNull(injectee.getTesterInterfaces());
      assertEquals(3, injectee.getTesterInterfaces().size());

      // remove one of the first one added
      change(tester1, ControllerState.NOT_INSTALLED);
      assertEquals(2, injectee.getTesterInterfaces().size());
      assertEquals(ControllerState.INSTALLED, context.getState());

      // remove the one that was added after cardinality dependency was already resolved
      // now the cardinality dependency should uninstall main context - since cardinality is out of range
      change(tester3, ControllerState.NOT_INSTALLED);
      assertEquals(ControllerState.CONFIGURED, context.getState());
   }

}
