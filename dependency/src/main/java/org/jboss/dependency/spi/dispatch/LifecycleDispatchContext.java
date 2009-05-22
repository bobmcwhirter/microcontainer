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
package org.jboss.dependency.spi.dispatch;

import org.jboss.dependency.spi.ControllerState;

/**
 * This context knows how to handle lifecycle invocation.
 * Resulting in valid lifecycle invocation will force this context to change state.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public interface LifecycleDispatchContext extends InvokeDispatchContext
{
   /**
    * Is this invocation a lifecycle invocation.
    * 
    * Return state value to which this context should be moved
    * or return current state if we're already past the lifecycle state
    * or null if the invocation is actually not a lifecycle invocation.
    *
    * @param name method name
    * @param parameters parameter values
    * @param signature method's parameter types / signatures
    * @return state to which we should move this context, or null if this is not lifecycle invocation
    * @throws Throwable for any error
    */
   ControllerState lifecycleInvocation(String name, Object parameters[], String[] signature) throws Throwable;
}