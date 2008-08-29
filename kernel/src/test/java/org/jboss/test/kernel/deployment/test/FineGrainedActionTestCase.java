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
import org.jboss.test.kernel.deployment.support.FineGrainedBean;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class FineGrainedActionTestCase extends AbstractDeploymentTest
{

   private static final String BEAN_NAME = "FineGrainedBean";

   public FineGrainedActionTestCase(String name) throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(FineGrainedActionTestCase.class);
   }

   public void testFineGrainedActions() throws Throwable
   {
      KernelControllerContext context = getControllerContext(FineGrainedActionTestCase.BEAN_NAME, ControllerState.NOT_INSTALLED);      
      FineGrainedBean target;

      change(context, ControllerState.DESCRIBED);
      target = (FineGrainedBean) context.getTarget();
      assertNull(target);

      change(context, ControllerState.INSTANTIATED);
      target = (FineGrainedBean) context.getTarget();
      assertNotNull(target);
      assertEquals("INSTANTIATED", target.getStateString());

      change(context, ControllerState.CONFIGURED);
      target = (FineGrainedBean) context.getTarget();
      assertNotNull(target);
      assertEquals("CONFIGURED", target.getStateString());

      change(context, ControllerState.CREATE);
      target = (FineGrainedBean) context.getTarget();
      assertNotNull(target);
      assertEquals("CREATE", target.getStateString());

      change(context, ControllerState.START);
      target = (FineGrainedBean) context.getTarget();
      assertNotNull(target);
      assertEquals("START", target.getStateString());

      change(context, ControllerState.INSTALLED);
      target = (FineGrainedBean) context.getTarget();
      assertNotNull(target);
      assertEquals("INSTALLED", target.getStateString());

      change(context, ControllerState.START);
      target = (FineGrainedBean) context.getTarget();
      assertNotNull(target);
      assertEquals("START", target.getStateString());

      change(context, ControllerState.CREATE);
      target = (FineGrainedBean) context.getTarget();
      assertNotNull(target);
      assertEquals("CREATE", target.getStateString());

      change(context, ControllerState.CONFIGURED);
      target = (FineGrainedBean) context.getTarget();
      assertNotNull(target);
      assertEquals("CONFIGURED", target.getStateString());

      change(context, ControllerState.INSTANTIATED);
      target = (FineGrainedBean) context.getTarget();
      assertNotNull(target);
      assertEquals("INSTANTIATED", target.getStateString());

      change(context, ControllerState.DESCRIBED);
      target = (FineGrainedBean) context.getTarget();
      assertNull(target);

      change(context, ControllerState.NOT_INSTALLED);
      target = (FineGrainedBean) context.getTarget();
      assertNull(target);
   }
}
