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

import java.util.Set;

import org.jboss.util.JBossInterface;

/**
 * A controller context represents a bean installed in the Microcontainer's
 * {@link Controller}. The {@link Controller} does work on the ControllerContext to move it through
 * its lifecycle. The lifecycle is made up of a series of {@link ControllerState}s. Entry to each 
 * {@link ControllerState} can be associated to trigger an action configured in the {@link ControllerContextActions}
 * 
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface ControllerContext extends JBossInterface
{
   /**
    * Get the name of the context. The name uniquely identifies the context in the {@link Controller}
    * (or hierarchy of {@link Controller}s).
    * 
    * @return the name
    */
   Object getName();

   /**
    * Return a set of aliases for the context. Aliases must be unique within a {@link Controller}.
    * 
    * @return the aliases or null if there are no aliases
    */
   Set<Object> getAliases();
   
   /**
    * Get the dependency information
    * 
    * @return the dependency information
    */
   DependencyInfo getDependencyInfo();
   
   /**
    * Get the scope information
    * 
    * @return the scope information
    */
   ScopeInfo getScopeInfo();
   
   /**
    * Get the target of the context. This will normally be the bean instance, available after 
    * the context reaches the {@link ControllerState#INSTANTIATED} state. 
    * 
    * @return the target
    */
   Object getTarget();
   
   /**
    * Get the controller that manages this controller context
    * 
    * @return the controller
    */
   Controller getController();
   
   /**
    * Set the controller that manages this controller context
    * 
    * @param controller the controller
    */
   void setController(Controller controller);

   /**
    * Moves the context from fromState to toState, where toState is a later state than fromState. 
    * The corresponding install action set up in its {@link ControllerContextActions} for toState
    * will be invoked.
    * 
    * @param fromState the old state
    * @param toState the new state
    * @throws Throwable for any error
    */
   void install(ControllerState fromState, ControllerState toState) throws Throwable;

   /**
    * Moves the context from fromState to toState, where toState is an earlier state than fromState. 
    * The corresponding uninstall action set up in its {@link ControllerContextActions} for fromState
    * will be invoked.
    * 
    * @param fromState the old state
    * @param toState the new state
    */
   void uninstall(ControllerState fromState, ControllerState toState);

   /**
    * Get the current state of this controller context
    * 
    * @return the state
    */
   ControllerState getState();
   
   /**
    * Set the current state of this controller context. Once under control of the {@link Controller},
    * only the {@link Controller} should call this method
    * 
    * @param state the state
    */
   void setState(ControllerState state);

   /**
    * Get the state that this controller context should reach.
    * For contexts using {@link ControllerMode#AUTOMATIC} this will typically
    * be {@link ControllerState#INSTANTIATED}
    * 
    * @return the required state
    */
   ControllerState getRequiredState();

   /**
    * Set the state that this controller context should reach.
    * For contexts using {@link ControllerMode#AUTOMATIC} in the out-of-the-box configuration
    * this will typically be {@link ControllerState#INSTANTIATED}
    * 
    * @param state the required state
    */
   void setRequiredState(ControllerState state);

   /**
    * Get the mode for this controller context
    * 
    * @return the mode
    */
   ControllerMode getMode();

   /**
    * Set the mode for this controller context. Once under control of the {@link Controller},
    * only the {@link Controller} should call this method.
    * 
    * @param mode the mode
    */
   void setMode(ControllerMode mode);

   /**
    * Get the error handling mode.
    * The default is {@link ErrorHandlingMode#DISCARD}
    *
    * @return the error handling mode
    */
   ErrorHandlingMode getErrorHandlingMode();

   /**
    * If installing this controller context caused an error, this method
    * will return the underlying error. 
    * 
    * @return the error
    */
   Throwable getError();
   
   /**
    * If installing this controller context caused an error, set the error here.
    * This method should only be called by the {@link Controller}.
    * 
    * @param error the error
    */
   void setError(Throwable error);
}
