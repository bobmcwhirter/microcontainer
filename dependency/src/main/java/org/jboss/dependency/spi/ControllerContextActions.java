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

/**
 * The default implementation of {@link ControllerContext} has a reference to 
 * {@link ControllerContextActions}. When a {@link ControllerContext} is installed to
 * or uninstalled from a given state that can be associated with an action.
 * <p>
 * The default actions when running the full kernel are on installing to/uninstalling from:
 * <ul>
 *   <li>{@link ControllerState#NOT_INSTALLED} - This is the starting state 
 *   for {@link ControllerContext}s to be installed</li> 
 *   <li>{@link ControllerState#PRE_INSTALL} - Determine the scoping policy 
 *   of the {@link ControllerContext} to see if it should go in the main controller 
 *   or in a child controller</li>
 *   <li>{@link ControllerState#DESCRIBED} - Determine the bean's dependencies</li>
 *   <li>{@link ControllerState#PRE_INSTALL} - Instantiate the bean instance and set it 
 *   in the {@link ControllerContext}'s target</li>
 *   <li>{@link ControllerState#CONFIGURED} - Configure the bean instance with the properties 
 *   from the bean metadata and perform injection of other bean instances.</li>
 *   <li>{@link ControllerState#CREATE} - Call any create/destroy lifecycle methods</li>
 *   <li>{@link ControllerState#START} - Call any start/stop lifecycle methods</li>
 *   <li>{@link ControllerState#INSTALLED} - The bean is properly started. Any lifecycle 
 *   install/uninstall methods are called on the bean</li>
 * </ul>
 * If something went wrong installing the {@link ControllerContext}, the {@link ControllerContext} 
 * enters the {@link ControllerState#ERROR} state.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface ControllerContextActions
{
   /**
    * Invokes the install action that as associated with the {@link ControllerContext}'s toState.
    * If there is no such action, do nothing
    * 
    * @param context the context being installed
    * @param fromState the old state
    * @param toState the new state
    * @throws Throwable for any error
    */
   void install(ControllerContext context, ControllerState fromState, ControllerState toState) throws Throwable;

   /**
    * Invokes the uninstall action that as associated with the {@link ControllerContext}'s fromState.
    * If there is no such action, do nothing
    * 
    * @param context the context being uninstalled
    * @param fromState the old state
    * @param toState the new state
    */
   void uninstall(ControllerContext context, ControllerState fromState, ControllerState toState);
}
