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
package org.jboss.dependency.spi;

import java.util.ListIterator;

/**
 * ControllerState model.
 * Helper/util methods.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public interface ControllerStateModel extends Iterable<ControllerState>
{
   /**
    * Get the list iterator.
    *
    * @return list iterator
    */
   ListIterator<ControllerState> listIteraror();

   /**
    * Get previous state from state param
    * or null if state param is the first.
    *
    * Throw exception if state is not recognized.
    *
    * @param state the current state
    * @return previous state
    */
   ControllerState getPreviousState(ControllerState state);

   /**
    * Get next state from state param
    * or null if state param is the last.
    *
    * Throw exception if state is not recognized.
    *
    * @param state the current state
    * @return next state
    */
   ControllerState getNextState(ControllerState state);

   /**
    * Is state param before reference state param.
    *
    * @param state the state we are checking
    * @param reference the state we are checking <b>against</b>
    * @return true if state is before reference
    */
   boolean isBeforeState(ControllerState state, ControllerState reference);

   /**
    * Is state param after reference state param.
    *
    * @param state the state we are checking
    * @param reference the state we are checking <b>against</b>
    * @return true if state is after reference
    */
   boolean isAfterState(ControllerState state, ControllerState reference);
}
