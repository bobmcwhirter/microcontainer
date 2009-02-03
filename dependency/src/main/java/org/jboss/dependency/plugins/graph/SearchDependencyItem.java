/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.dependency.plugins.graph;

import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.graph.GraphController;
import org.jboss.dependency.spi.graph.LookupStrategy;
import org.jboss.dependency.plugins.AbstractDependencyItem;

/**
 * Search dependency item.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SearchDependencyItem extends AbstractDependencyItem
{
   private Search search;

   public SearchDependencyItem(Object name, Object iDependOn, ControllerState whenRequired, ControllerState dependentState, Search search)
   {
      super(name, iDependOn, whenRequired, dependentState);
      this.search = search;
   }

   /**
    * Get controller context.
    *
    * @param controller the controller
    * @param name the name
    * @param state the state
    * @return the controller context
    */
   protected ControllerContext getControllerContext(Controller controller, Object name, ControllerState state)
   {
      LookupStrategy strategy = search.getStrategy();
      return strategy.getContext(controller, name, state);
   }

   public boolean resolve(Controller controller)
   {
      if (controller instanceof GraphController)
      {
         boolean previous = isResolved();
         ControllerContext context;

         if (getDependentState() == null)
         {
            context = getControllerContext(controller, getIDependOn(), ControllerState.INSTALLED);
         }
         else
         {
            context = getControllerContext(controller, getIDependOn(), getDependentState());
         }

         if (context == null)
         {
            setResolved(false);
            ControllerContext unresolvedContext = getControllerContext(controller, getIDependOn(), null);
            if (unresolvedContext != null && ControllerMode.ON_DEMAND.equals(unresolvedContext.getMode()))
            {
               try
               {
                  controller.enableOnDemand(unresolvedContext);
               }
               catch (Throwable ignored)
               {
                  if (log.isTraceEnabled())
                     log.trace("Unexpected error", ignored);
               }
            }
         }
         else
         {
            addDependsOnMe(controller, context);
            setResolved(true);
         }

         if (previous != isResolved())
         {
            flushJBossObjectCache();
            if (log.isTraceEnabled())
            {
               if (isResolved())
                  log.trace("Resolved " + this);
               else
                  log.trace("Unresolved " + this);
            }
         }
         return isResolved();
      }
      return super.resolve(controller);
   }

   protected void toHumanReadableString(StringBuilder builder)
   {
      super.toHumanReadableString(builder);
      builder.append("search=").append(search);
   }
}
