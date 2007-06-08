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
package org.jboss.test.dependency.controller.test;

import java.util.List;
import java.util.Set;

import junit.framework.Test;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ShutdownControllerTestCase extends AbstractDependencyTest
{
   public ShutdownControllerTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(ShutdownControllerTestCase.class);
   }

   protected void validateEmptyContexts()
   {
      List<ControllerState> states = controller.getStates();
      for(ControllerState state : states)
      {
         Set<ControllerContext> contexts = controller.getContextsByState(state);
         if (contexts != null)
            assertEmpty(contexts);
      }
   }

   public void testSimpleShutdown() throws Throwable
   {
      ControllerContext c1 = createControllerContext("test1", "alias1");
      ControllerContext c2 = createControllerContext("test2", "alias2");
      controller.install(c1);
      controller.install(c2);

      assertFalse(controller.isShutdown());
      ControllerContext i1 = controller.getInstalledContext("test1");
      assertContext(i1);
      ControllerContext i2 = controller.getInstalledContext("test2");
      assertContext(i2);

      controller.shutdown();
      assertTrue(controller.isShutdown());

      validateEmptyContexts();
   }
}
