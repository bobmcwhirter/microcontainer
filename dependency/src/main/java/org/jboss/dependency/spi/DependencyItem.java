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
 * Information about a single dependency on a {@link ControllerContext}. These are
 * held within a {@link ControllerContext}'s {@link DependencyInfo}.
 * <p>
 * When the owning {@link ControllerContext} enters the state in 
 * {@link #getWhenRequired()}, if the {@link ControllerContext} we have a dependency
 * on has not reached the state in {@link #getDependentState()} the owning
 * {@link ControllerContext} cannot proceed to the state in {@link #getWhenRequired()}.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface DependencyItem 
{
   /**
    * Get the name of the {@link ControllerContext} I depend on.
    * 
    * @return the name
    */
   Object getIDependOn();

   /**
    * Get the name of the {@link ControllerContext} this dependency belongs to.
    * 
    * @return the name of the owning {@link ControllerContext}
    */
   Object getName();

   /**
    * Get when the dependency is required. This is the state of the owning
    * {@link ControllerContext}. 
    * 
    * @return the state when required
    */
   ControllerState getWhenRequired();

   /**
    * Get the dependencies state. This is the state of dependency the {@link ControllerContext}
    * represented by this dependency item that the owning {@link ControllerContext}
    * depends on.
    * 
    * @return the state of the required of the dependent
    */
   ControllerState getDependentState();
   
   /**
    * Whether we are resolved. Resolved means that the dependency has reached the
    * state given by {@link #getDependentState()}
    * 
    * @return true for resolved, false otherwise
    */
   boolean isResolved();

   /**
    * Try to resolve. This will look up the dependency {@link ControllerContext} in the
    * controller.
    * 
    * @param controller the controller
    * @return true for resolved, false otherwise
    */
   boolean resolve(Controller controller);
   
   /**
    * Mark the dependency as unresolved
    * 
    * @param controller the controller
    * @return true if proceed with unresolving, false otherwise
    */
   boolean unresolved(Controller controller);
   
   /**
    * Return a human readable version of the dependency
    * 
    * @return the string
    */
   String toHumanReadableString();
}