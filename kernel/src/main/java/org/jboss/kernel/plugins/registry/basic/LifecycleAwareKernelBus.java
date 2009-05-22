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
package org.jboss.kernel.plugins.registry.basic;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.dispatch.LifecycleDispatchContext;
import org.jboss.dependency.spi.dispatch.InvokeDispatchContext;

/**
 * A kernel bus that understands lifecycle invocation.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class LifecycleAwareKernelBus extends BasicKernelBus
{
   public Object invoke(Object name, final String methodName, final Object parameters[], final String[] signature) throws Throwable
   {
      ControllerContext context = controller.getContext(name, null);
      if (context == null)
         throw new IllegalArgumentException("No such context: " + name);

      if (context instanceof LifecycleDispatchContext)
      {
         LifecycleDispatchContext ldc = (LifecycleDispatchContext)context;
         ControllerState state = ldc.lifecycleInvocation(methodName, parameters, signature);
         if (state != null)
         {
            if (state.equals(context.getState()) == false)
               controller.change(context, state);
            
            return null;
         }
      }

      if (ControllerState.INSTALLED.equals(context.getState()))
      {
         return execute(context, InvokeDispatchContext.class, new Dispatcher<InvokeDispatchContext>()
         {
            public Object dispatch(InvokeDispatchContext dispatchContext) throws Throwable
            {
               return dispatchContext.invoke(methodName, parameters, signature);
            }

            @Override
            public String toString()
            {
               return "invoke";
            }
         });
      }
      else
      {
         throw new IllegalArgumentException("The invocation is not a lifecycle invocation or context is not fully installed: " + context);
      }
   }
}