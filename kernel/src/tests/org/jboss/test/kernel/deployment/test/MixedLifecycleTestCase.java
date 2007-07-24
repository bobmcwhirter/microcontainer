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
package org.jboss.test.kernel.deployment.test;

import junit.framework.Test;

import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.kernel.deployment.support.SimpleLifecycleBean;

/**
 * Test mixed lifecycle with annotation override.
 *
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class MixedLifecycleTestCase extends AbstractManualDeploymentTest
{

   private static final String BEAN_NAME = "LifecycleBean";

   public MixedLifecycleTestCase(String name) throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(MixedLifecycleTestCase.class);
   }

   public void testAnnotatedLifecycle() throws Throwable
   {
      SimpleLifecycleBean target;

      KernelControllerContext context = getControllerContext(BEAN_NAME, ControllerState.NOT_INSTALLED);
      assertEquals(context.getState(), ControllerState.NOT_INSTALLED);

      change(context, ControllerState.CREATE);
      assertEquals(context.getState(), ControllerState.CREATE);
      target = (SimpleLifecycleBean) context.getTarget();
      assertNotNull(target);
      assertFalse(target.isCreate());
      assertFalse(target.isStart());
      assertFalse(target.isStop());
      assertFalse(target.isDestroy());

      change(context, ControllerState.START);
      assertEquals(context.getState(), ControllerState.START);
      target = (SimpleLifecycleBean) context.getTarget();
      assertNotNull(target);
      assertFalse(target.isCreate());
      assertTrue(target.isStart());
      assertFalse(target.isStop());
      assertFalse(target.isDestroy());

      change(context, ControllerState.CREATE);
      assertEquals(context.getState(), ControllerState.CREATE);
      target = (SimpleLifecycleBean) context.getTarget();
      assertNotNull(target);
      assertFalse(target.isCreate());
      assertTrue(target.isStart());
      assertTrue(target.isStop());
      assertFalse(target.isDestroy());

      change(context, ControllerState.CONFIGURED);
      assertEquals(context.getState(), ControllerState.CONFIGURED);
      target = (SimpleLifecycleBean) context.getTarget();
      assertNotNull(target);
      assertFalse(target.isCreate());
      assertTrue(target.isStart());
      assertTrue(target.isStop());
      assertTrue(target.isDestroy());
   }
}
