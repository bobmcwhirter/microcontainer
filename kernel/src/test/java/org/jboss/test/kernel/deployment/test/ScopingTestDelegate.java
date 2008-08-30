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

import org.jboss.dependency.plugins.AbstractController;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.kernel.junit.MicrocontainerTestDelegate;

/**
 * Scoping Deployment Test Delegate.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class ScopingTestDelegate extends MicrocontainerTestDelegate
{
   public ScopingTestDelegate(Class<?> clazz) throws Exception
   {
      super(clazz);
   }

   protected KernelControllerContext getControllerContext(final Object name, final ControllerState state)
   {
      Controller controller;
      KernelControllerContext context;
      try
      {
         controller = new TestController((AbstractController)kernel.getController());
         context = (KernelControllerContext)controller.getContext(name, state);
      }
      catch (Exception e)
      {
         throw new Error(e);
      }
      if (context == null)
         handleNotFoundContext(controller, name, state);
      return context;
   }

   private class TestController extends AbstractController
   {
      private AbstractController delegate;

      public boolean isShutdown()
      {
         return delegate.isShutdown();
      }

      public TestController(AbstractController controller) throws Exception
      {
         this.delegate = controller;
      }

      public ControllerContext getContext(Object name, ControllerState state)
      {
         return findContext(delegate, name, state);
      }

      private ControllerContext findContext(AbstractController controller, Object name, ControllerState state)
      {
         ControllerContext context = controller.getContext(name, state);
         if (context != null)
         {
            return context;
         }
         else
         {
            for (AbstractController childController : controller.getControllers())
            {
               ControllerContext ctx = findContext(childController, name, state);
               if (ctx != null)
                  return ctx;
            }
         }
         return null;
      }
   }
}
