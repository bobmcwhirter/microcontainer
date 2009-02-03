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
package org.jboss.kernel.plugins.dependency;

import org.jboss.dependency.plugins.graph.Search;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.graph.LookupStrategy;

/**
 * A search Class context dependencyItem.
 *
 * @author <a href="ales.justin@gmail.com">Ales Justin</a>
 */
public class SearchClassContextDependencyItem extends ClassDependencyItem
{
   private Search search;

   public SearchClassContextDependencyItem(Object name, Class<?> demandClass, ControllerState whenRequired, ControllerState dependentState, Search search)
   {
      super(name, demandClass, whenRequired, dependentState);
      if (search == null)
         throw new IllegalArgumentException("Null search.");

      this.search = search;
   }

   /**
    * Get controller context.
    *
    * @param controller the controller
    * @return controller context
    */
   protected ControllerContext getControllerContext(Controller controller)
   {
      LookupStrategy strategy = search.getStrategy();
      return strategy.getContext(controller, getIDependOn(), ControllerState.INSTALLED);
   }
}