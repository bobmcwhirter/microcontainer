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
package org.jboss.dependency.plugins.graph;

import org.jboss.dependency.plugins.AbstractController;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.graph.LookupStrategy;
import org.jboss.dependency.spi.graph.SearchInfo;

/**
 * Abstract lookup strategy.
 * Only working on AbstractController controller instances.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractLookupStrategy implements LookupStrategy, SearchInfo
{
   private String type;

   public String type()
   {
      if (type == null)
      {
         String simpleName = getClass().getSimpleName();
         int p = simpleName.indexOf("LookupStrategy");
         type = simpleName.substring(0, p);
      }
      return type;
   }

   public LookupStrategy getStrategy()
   {
      return this;
   }

   public ControllerContext getContext(Controller controller, Object name, ControllerState state)
   {
      if (controller instanceof AbstractController == false)
         throw new IllegalArgumentException("Can only handle AbstractController: " + controller);

      return getContextInternal((AbstractController)controller, name, state);
   }

   /**
    * Get context based on this strategy.
    *
    * @param controller the current abstract controller
    * @param name the name of the context
    * @param state the context's state
    * @return context or null if not available
    */
   protected abstract ControllerContext getContextInternal(AbstractController controller, Object name, ControllerState state);
}