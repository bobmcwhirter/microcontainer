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
package org.jboss.kernel.plugins.dependency;

import java.util.Set;

import org.jboss.dependency.plugins.SingleCallbackItem;
import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.dispatch.InvokeDispatchContext;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * Class single dependency item - class dependency.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ClassSingleCallbackItem extends SingleCallbackItem<Class<?>>
{
   protected Cardinality cardinality;

   public ClassSingleCallbackItem(Class<?> name, InvokeDispatchContext owner, String method)
   {
      super(name, owner, method);
   }

   public ClassSingleCallbackItem(Class<?> name, InvokeDispatchContext owner, String method, String signature)
   {
      super(name, owner, method, signature);
   }

   public ClassSingleCallbackItem(Class<?> name, ControllerState whenRequired, ControllerState dependentState, Cardinality cardinality, InvokeDispatchContext owner, String method)
   {
      super(name, whenRequired, dependentState, owner, method);
   }

   public ClassSingleCallbackItem(Class<?> name, ControllerState whenRequired, ControllerState dependentState, Cardinality cardinality, InvokeDispatchContext owner, String method, String signature)
   {
      super(name, whenRequired, dependentState, owner, method, signature);
      this.cardinality = cardinality;
   }

   public void ownerCallback(Controller controller, boolean isInstallPhase) throws Throwable
   {
      if (controller instanceof KernelController)
      {
         KernelController kc = (KernelController)controller;
         Set<KernelControllerContext> contexts = kc.getContexts(getIDependOn(), getDependentState());
         if (contexts != null && contexts.isEmpty() == false)
         {
            for(KernelControllerContext context : contexts)
            {
               Object target = context.getTarget();
               if (signature == null)
                  signature = target.getClass().getName();
               owner.invoke(getAttributeName(), new Object[]{target}, new String[]{signature});
            }
         }
      }
      else
         log.info("Controller not KernelController instance, cannot execute owner callback.");
   }

   protected DependencyItem createDependencyItem(ControllerContext owner)
   {
      if (cardinality != null)
         return new CallbackDependencyItem(owner.getName(), getIDependOn(), whenRequired, dependentState, cardinality);
      else
         return null;
   }
}
