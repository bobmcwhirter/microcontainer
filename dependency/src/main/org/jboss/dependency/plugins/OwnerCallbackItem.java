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
package org.jboss.dependency.plugins;

import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.util.JBossStringBuilder;

/**
 * Owner callback item.
 *
 * @param <C> owner type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class OwnerCallbackItem<T, C> extends AbstractCallbackItem<T>
{
   protected C owner;

   protected OwnerCallbackItem(T name, C owner)
   {
      this(name, null, null, owner);
   }

   protected OwnerCallbackItem(T name, ControllerState whenRequired, ControllerState dependentState, C owner)
   {
      super(name, whenRequired, dependentState);
      if (owner == null)
         throw new IllegalArgumentException("Null owner!");
      this.owner = owner;
   }

   protected void addDependency(Controller controller, ControllerContext context)
   {
      if (owner instanceof ControllerContext)
      {
         ControllerContext co = (ControllerContext)owner;
         DependencyItem dependency = createDependencyItem(co);
         if (dependency != null && dependency.resolve(controller))
         {
            context.getDependencyInfo().addDependsOnMe(dependency);
            co.getDependencyInfo().addIDependOn(dependency);
         }
      }
   }

   /**
    * Create dependency - if it exists.
    *
    * @param owner if owner is controller context
    * @return dependency or null if no such dependency exists
    */
   protected DependencyItem createDependencyItem(ControllerContext owner)
   {
      return null;
   }

   public void changeCallback(Controller controller, ControllerContext context) throws Throwable
   {
      super.changeCallback(controller, context);
      addDependency(controller, context);
   }

   protected void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
      buffer.append(" owner=").append(owner);
   }
}
