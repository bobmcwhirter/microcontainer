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
import org.jboss.util.HashCode;
import org.jboss.util.JBossStringBuilder;

/**
 * A Callback dependencyItem.
 *
 * @author <a href="ales.justin@gmail.com">Ales Justin</a>
 */
public class CallbackDependencyItem extends ClassDependencyItem
{
   private Cardinality cardinality;

   public CallbackDependencyItem(Object name, Class<?> demandClass, ControllerState whenRequired, ControllerState dependentState, Cardinality cardinality)
   {
      super(name, demandClass, whenRequired, dependentState);
      if (cardinality == null)
         throw new IllegalArgumentException("Null cardinality.");
      this.cardinality = cardinality;
   }

   public Cardinality getCardinality()
   {
      return cardinality;
   }

   protected Set<KernelControllerContext> getContexts(Controller controller)
   {
      if (controller instanceof KernelController == false)
         throw new IllegalArgumentException("Controller not KernelController!");

      KernelController kernelController = (KernelController)controller;
      return kernelController.getContexts(getDemandClass(), getDependentState());
   }

   public boolean resolve(Controller controller)
   {
      Set<KernelControllerContext> contexts = getContexts(controller);
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

   public boolean unresolved(Controller controller)
   {
      if (getIDependOn() == null)
         return true;
      
      Set<KernelControllerContext> contexts = getContexts(controller);
       // minus one, since this is called when unistalling dependent context
      int size = contexts != null ? contexts.size() - 1 : 0;
      return cardinality.isInRange(size) == false;
   }

   protected int getHashCode()
   {
      int result = HashCode.generate(getName());
      result += 3 * HashCode.generate(getIDependOn());
      result += 7 * HashCode.generate(getWhenRequired());
      result += 11 * HashCode.generate(getDependentState());
      result += 19 * HashCode.generate(getCardinality());
      return result;
   }

   public boolean equals(Object obj)
   {
      if (obj instanceof CallbackDependencyItem == false)
         return false;

      CallbackDependencyItem cdi = (CallbackDependencyItem)obj;
      if (isDifferent(getName(), cdi.getName()))
         return false;
      if (isDifferent(getIDependOn(), cdi.getIDependOn()))
         return false;
      if (isDifferent(getWhenRequired(), cdi.getWhenRequired()))
         return false;
      if (isDifferent(getDependentState(), cdi.getDependentState()))
         return false;
      if (isDifferent(getCardinality(), cdi.getCardinality()))
         return false;
      return true;
   }

   protected static boolean isDifferent(Object first, Object second)
   {
      if (first == null)
         return second != null;
      else
         return first.equals(second) == false;
   }

   public void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
      buffer.append(" cardinality=" + cardinality);
   }

   @Override
   public String toHumanReadableString()
   {
      // TODO toHumanReadableString
      return super.toString();
   }
}
