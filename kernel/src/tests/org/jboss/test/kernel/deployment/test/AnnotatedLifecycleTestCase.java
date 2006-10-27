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
package org.jboss.test.kernel.deployment.test;

import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.AbstractTestDelegate;
import org.jboss.test.kernel.deployment.support.AnnotatedLifecycleBean;
import org.jboss.test.kernel.junit.ManualMicrocontainerTestDelegate;

import junit.framework.Test;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class AnnotatedLifecycleTestCase extends AbstractDeploymentTest
{

   private static final String BEAN_NAME = "LifecycleBean";

   public AnnotatedLifecycleTestCase(String name) throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(AnnotatedLifecycleTestCase.class);
   }

   public void testAnnotetedLifecycle() throws Throwable
   {
      AnnotatedLifecycleBean target;

      KernelControllerContext context = getControllerContext(BEAN_NAME, ControllerState.NOT_INSTALLED);

      ManualMicrocontainerTestDelegate delegate = (ManualMicrocontainerTestDelegate) getMCDelegate();

      delegate.change(context, ControllerState.DESCRIBED);
      target = (AnnotatedLifecycleBean) context.getTarget();
      assertNull(target);

      delegate.change(context, ControllerState.CREATE);
      target = (AnnotatedLifecycleBean) context.getTarget();
      assertTrue(target.isCreate());
      assertFalse(target.isStart());
      assertFalse(target.isStop());
      assertFalse(target.isDestroy());

      delegate.change(context, ControllerState.START);
      target = (AnnotatedLifecycleBean) context.getTarget();
      assertTrue(target.isCreate());
      assertTrue(target.isStart());
      assertFalse(target.isStop());
      assertFalse(target.isDestroy());

      delegate.change(context, ControllerState.CREATE);
      target = (AnnotatedLifecycleBean) context.getTarget();
      assertTrue(target.isCreate());
      assertFalse(target.isStart());
      assertTrue(target.isStop());
      assertFalse(target.isDestroy());

      delegate.change(context, ControllerState.DESCRIBED);
      target = (AnnotatedLifecycleBean) context.getTarget();
      assertNull(target);

   }

   public static AbstractTestDelegate getDelegate(Class clazz) throws Exception
   {
      return new ManualMicrocontainerTestDelegate(clazz);
   }

}
