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

import java.util.Collection;
import java.util.Set;

import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.dispatch.InvokeDispatchContext;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * Collection callback item.
 *
 * @param <T> expected collection type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class CollectionCallbackItem<T extends Collection<Object>> extends ClassAttributeCallbackItem
{
   public CollectionCallbackItem(Class name, InvokeDispatchContext owner, String attribute)
   {
      super(name, owner, attribute);
   }

   public CollectionCallbackItem(Class name, ControllerState whenRequired, ControllerState dependentState, Cardinality cardinality, InvokeDispatchContext context, String attribute)
   {
      super(name, whenRequired, dependentState, cardinality, context, attribute);
   }

   /**
    * Create new collection impl instance - HashSet, ArrayList, ...
    *
    * @return new collection impl instance
    */
   protected abstract T getCollectionParameterHolder();

   protected T fillHolder(Controller controller) throws Throwable
   {
      if (controller instanceof KernelController)
      {
         KernelController kernelController = (KernelController)controller;
         T holder = getCollectionParameterHolder();
         Set<KernelControllerContext> contexts = kernelController.getContexts(getIDependOn(), dependentState);
         if (contexts != null && contexts.isEmpty() == false)
         {
            for(ControllerContext context : contexts)
            {
               holder.add(context.getTarget());
            }
         }
         return holder;
      }
      else
         throw new IllegalArgumentException("Cannot execute Collection call back - controller not KernelController instance.");
   }

   public void ownerCallback(Controller controller, boolean isInstallPhase) throws Throwable
   {
      execute(fillHolder(controller));
   }

   public void changeCallback(Controller controller, ControllerContext context, boolean isInstallPhase) throws Throwable
   {
      T holder = fillHolder(controller);
      if (isInstallPhase == false)
         holder.remove(context.getTarget());
      execute(holder);
      addDependency(controller, context, isInstallPhase);
   }
}
