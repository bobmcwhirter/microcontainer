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
package org.jboss.dependency.spi.graph;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerState;

/**
 * Context lookup strategy.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public interface LookupStrategy
{
   /**
    * Get context based on this strategy.
    *
    * @param controller the current controller
    * @param name the name of the context
    * @param state the context's state
    * @return context or null if not available
    */
   ControllerContext getContext(Controller controller, Object name, ControllerState state);
}
