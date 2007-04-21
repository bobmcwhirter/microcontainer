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

import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * A Callback dependencyItem.
 *
 * @author <a href="ales.justin@gmail.com">Ales Justin</a>
 */
public class CallbackDependencyItem extends ClassDependencyItem
{
   private Cardinality cardinality;

   public CallbackDependencyItem(Object name, Class demandClass, ControllerState whenRequired, ControllerState dependentState, Cardinality cardinality)
   {
      super(name, demandClass, whenRequired, dependentState);
      if (cardinality == null)
         throw new IllegalArgumentException("Null cardinality.");
      this.cardinality = cardinality;
   }

   public boolean resolve(Controller controller)
   {
      if (controller instanceof KernelController == false)
         throw new IllegalArgumentException("Controller not KernelController!");

      KernelController kernelController = (KernelController)controller;
      Set<KernelControllerContext> contexts = kernelController.getInstantiatedContexts(getDemandClass());
      int size = contexts != null ? contexts.size() : 0;
      if (cardinality.isInRange(size))
      {
         setIDependOn(getIDependOn());
         if (contexts != null)
         {
            for (KernelControllerContext context : contexts)
               addDependsOnMe(controller, context);
         }
         setResolved(true);
      }
      else
      {
         setResolved(false);
      }
      return isResolved();
   }

}
