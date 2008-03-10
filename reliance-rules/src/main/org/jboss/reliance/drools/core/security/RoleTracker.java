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
package org.jboss.reliance.drools.core.security;

import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.logging.Logger;
import org.jboss.reliance.drools.core.rules.Nameable;

/**
 * Role tracker.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class RoleTracker extends Nameable
{
   protected static Logger log = Logger.getLogger(RoleTracker.class);
   private static final long serialVersionUID = 3855075694271934392L;

   public RoleTracker(Object name)
   {
      super(name);
   }

   public static void change(Controller controller, Object name, String stateString)
   {
      if (controller == null)
      {
         log.warn("Null Controller, set global variable or use ManagedWorkingMemory.");
         return;
      }

      ControllerContext context = controller.getContext(name, null);
      if (context != null)
      {
         ControllerState state = new ControllerState(stateString);
         try
         {
            controller.change(context, state);
         }
         catch (Throwable t)
         {
            log.error("Exception while unwinding context: " + context, t);
         }
      }
   }
}
