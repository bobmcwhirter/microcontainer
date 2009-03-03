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
import org.jboss.test.kernel.inject.support.PropertyInjectTestObject;

/**
 * Callback tests.
 *
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class InjectionValueCallbackTestCase extends AbstractManualInjectTest
{
   public InjectionValueCallbackTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(InjectionValueCallbackTestCase.class);
   }

   public void testInjectionValueMetaData() throws Throwable
   {
      KernelControllerContext context = getControllerContext("testObject", ControllerState.NOT_INSTALLED);
      assertNull(context.getTarget());
      assertEquals(context.getState(), ControllerState.NOT_INSTALLED);

      ControllerState state = change(context, ControllerState.INSTALLED);
      assertEquals(ControllerState.INSTALLED, state);
      PropertyInjectTestObject injectee = (PropertyInjectTestObject)context.getTarget();
      assertNotNull(injectee);
      assertNull(injectee.getTesterInterface());

      KernelControllerContext tester = getControllerContext("tester", ControllerState.NOT_INSTALLED);
      assertNotNull(tester);
      change(tester, ControllerState.INSTALLED);

      assertNotNull(injectee.getTesterInterface());
   }

}
