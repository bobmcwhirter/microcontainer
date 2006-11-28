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
package org.jboss.dependency.spi;

import org.jboss.util.JBossInterface;

/**
 * Information about a context.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface ControllerContext extends JBossInterface
{
   /**
    * Get the name
    * 
    * @return the name
    */
   Object getName();

   /**
    * Get the dependency information
    * 
    * @return the dependency information
    */
   DependencyInfo getDependencyInfo();
   
   /**
    * Get any target
    * 
    * @return the target
    */
   Object getTarget();
   
   /**
    * Get the controller
    * 
    * @return the controller
    */
   Controller getController();
   
   /**
    * Set the controller
    * 
    * @param controller the controller
    */
   void setController(Controller controller);

   /**
    * Install
    * 
    * @param fromState the old state
    * @param toState the new state
    * @throws Throwable for any error
    */
   void install(ControllerState fromState, ControllerState toState) throws Throwable;

   /**
    * Uninstall
    * 
    * @param fromState the old state
    * @param toState the new state
    */
   void uninstall(ControllerState fromState, ControllerState toState);

   /**
    * Get the state
    * 
    * @return the state
    */
   ControllerState getState();
   
   /**
    * Set the state
    * 
    * @param state the state
    */
   void setState(ControllerState state);

   /**
    * Get the required state
    * 
    * @return the required state
    */
   ControllerState getRequiredState();

   /**
    * Set the required state
    * 
    * @param state the required state
    */
   void setRequiredState(ControllerState state);

   /**
    * Get the mode
    * 
    * @return the mode
    */
   ControllerMode getMode();

   /**
    * Set the mode
    * 
    * @param mode the mode
    */
   void setMode(ControllerMode mode);
   
   /**
    * Get the error
    * 
    * @return the error
    */
   Throwable getError();
   
   /**
    * Set the error
    * 
    * @param error the error
    */
   void setError(Throwable error);
}
