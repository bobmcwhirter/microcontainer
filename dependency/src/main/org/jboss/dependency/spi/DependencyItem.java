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
 * Information about a single dependency.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface DependencyItem 
{
   /**
    * Get the object name i depend on
    * 
    * @return the name
    */
   Object getIDependOn();

   /**
    * Get my object name
    * 
    * @return the name
    */
   Object getName();

   /**
    * Get when the dependency is required
    * 
    * @return the state when required
    */
   ControllerState getWhenRequired();

   /**
    * Get the dependent's state
    * 
    * @return the state of the required of the dependent
    */
   ControllerState getDependentState();
   
   /**
    * Whether we are resolved
    * 
    * @return true for resolved, false otherwise
    */
   boolean isResolved();

   /**
    * Try to resolve
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
}