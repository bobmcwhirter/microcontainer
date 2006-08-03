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
package org.jboss.dependency.plugins;

import java.util.Map;

import org.jboss.dependency.plugins.spi.action.ControllerContextAction;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerContextActions;
import org.jboss.dependency.spi.ControllerState;

/**
 * ControllerContextActions.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractControllerContextActions implements ControllerContextActions
{
   /** The actions Map<ControllerState, ControllerContextAction> */
   private Map<ControllerState, ControllerContextAction> actions;
   
   public AbstractControllerContextActions(Map<ControllerState, ControllerContextAction> actions)
   {
      this.actions = actions;
   }
   
   public void install(ControllerContext context, ControllerState fromState, ControllerState toState) throws Throwable
   {
      ControllerContextAction action = getAction(context, toState);
      if (action != null)
         action.install(context);
   }

   public void uninstall(ControllerContext context, ControllerState fromState, ControllerState toState)
   {
      ControllerContextAction action = getAction(context, fromState);
      if (action != null)
         action.uninstall(context);
   }
   
   /**
    * Get the controller context action
    * 
    * @param context the context
    * @param state the state
    * @return the action
    */
   protected ControllerContextAction getAction(ControllerContext context, ControllerState state)
   {
      return actions.get(state);
   }
}
