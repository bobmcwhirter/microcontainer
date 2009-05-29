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
 * The controller is the state-machine at the heart of the JBoss
 * Microcontainer. It keeps track of {@link ControllerContext}s
 * to make sure the configuration and lifecycle are
 * done in the correct order including dependencies and
 * classloading considerations. Several controllers can exist in a hiearchy.
 * <p>
 * The {@link ControllerContext}s each represent a bean that is to
 * be installed in the Microcontainer.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface Controller extends JBossInterface
{
   /**
    * Install a controller context.
    * This will attempt to move the controller context as far
    * as possible through its states. For contexts using
    * {@link ControllerMode#AUTOMATIC} it will attempt to move it to
    * the {@link ControllerState#INSTALLED} state. If in any state the
    * context's dependencies are not satisfied, the context will be held 
    * at that state until its depdencies are satisfied. Once its dependencies 
    * are satisfied the install initiated by calling this method will resume.
    * 
    * @param context the context
    * @throws Throwable for any error
    */
   void install(ControllerContext context) throws Throwable;

   /**
    * Change a context to the given state. The given state can
    * either be before or after the state the context is currently
    * in.
    * 
    * @param context the context
    * @param state the state
    * @throws Throwable for any error
    */
   void change(ControllerContext context, ControllerState state) throws Throwable;

   /**
    * Enable an on demand context and move it to the {@link ControllerState#INSTALLED}
    * state.
    * 
    * @param context the context
    * @throws Throwable for any error
    */
   void enableOnDemand(ControllerContext context) throws Throwable;
   
   /**
    * Uninstall a context. This will move the context to the {@link ControllerState#NOT_INSTALLED}
    * atate. If other contexts depend on this context, they will be uninstalled first.
    * 
    * @param name the name of the component to uninstall
    * @return the context
    */
   ControllerContext uninstall(Object name);

   /**
    * Add an alias. An alias is an alternative name to be used for a bean, and are 
    *
    * @param alias the alias to add
    * @param original original name
    * @throws Throwable for any error
    */
   void addAlias(Object alias, Object original) throws Throwable;

   /**
    * Remove alias.
    *
    * @param alias alias to remove
    */
   void removeAlias(Object alias);

   /**
    * Get a context
    *
    * @param name the name of the component
    * @param state the state (pass null for any state)
    * @return the context
    */
   ControllerContext getContext(Object name, ControllerState state);
   
   /**
    * Get an installed context
    *
    * @param name the name of the component
    * @return the context
    */
   ControllerContext getInstalledContext(Object name);
   
   /**
    * Get the contexts not installed
    * 
    * @return Set<ControllerContext>
    */
   Set<ControllerContext> getNotInstalled();
   
   /**
    * Add a state.
    * 
    * @param state the state to add
    * @param before the state to add before or null to add to the end
    */
   void addState(ControllerState state, ControllerState before);
   
   /**
    * Get the states model.
    * 
    * @return the states in order
    */
   ControllerStateModel getStates();

   /**
    * Get the contexts in certain state
    *
    * @param state controller state to get contexts for
    * @return set of contexts in certain state
    */
   Set<ControllerContext> getContextsByState(ControllerState state);

   /**
    * Whether the controller is shutdown
    * 
    * @return true when shutdown
    */
   boolean isShutdown();

   /**
    * Shutdown the controller
    */
   void shutdown();
}
