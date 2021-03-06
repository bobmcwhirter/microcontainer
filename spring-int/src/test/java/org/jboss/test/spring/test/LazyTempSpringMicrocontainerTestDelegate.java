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

import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class LazyTempSpringMicrocontainerTestDelegate extends TempSpringMicrocontainerTestDelegate
{
   public LazyTempSpringMicrocontainerTestDelegate(Class<?> clazz) throws Exception
   {
      super(clazz);
   }

   protected KernelControllerContext handleNotFoundContext(Controller controller, Object name, ControllerState state)
   {
      KernelControllerContext context = (KernelControllerContext)controller.getContext(name, null);
      if (context != null && ControllerMode.ON_DEMAND.equals(context.getMode()))
      {
         try
         {
            controller.change(context, ControllerState.INSTALLED);
            return context;
         }
         catch (Throwable t)
         {
            throw new Error("Unable to change on demand context to Installed.", t);
         }
      }
      throw new IllegalStateException("Bean not found " + name + " at state " + state);
   }
}
