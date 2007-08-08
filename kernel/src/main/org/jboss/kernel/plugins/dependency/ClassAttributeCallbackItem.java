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

import org.jboss.dependency.plugins.AttributeCallbackItem;
import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.dependency.spi.dispatch.AttributeDispatchContext;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * Class callback item - class dependency.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ClassAttributeCallbackItem extends AttributeCallbackItem<Class>
{
   protected Cardinality cardinality;

   public ClassAttributeCallbackItem(Class name, AttributeDispatchContext owner, String attribute)
   {
      super(name, owner, attribute);
   }

   public ClassAttributeCallbackItem(Class name, ControllerState whenRequired, ControllerState dependentState, Cardinality cardinality, AttributeDispatchContext owner, String attribute)
   {
      super(name, whenRequired, dependentState, owner, attribute);
      this.cardinality = cardinality;
   }

   public void ownerCallback(Controller controller, boolean isInstallPhase) throws Throwable
   {
      if (controller instanceof KernelController)
      {
         KernelController kc = (KernelController)controller;
         KernelControllerContext context = kc.getContextByClass(getIDependOn());
         if (context != null)
         {
            Object target = context.getTarget();
            owner.set(getAttributeName(), target);
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
