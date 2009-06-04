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
package org.jboss.kernel.spi.event;

import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.KernelObject;

/**
 * An event manager.<p>
 * 
 * The event manager co-ordinates events in the {@link Kernel}.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface KernelEventManager extends KernelObject
{
   /**
    * Register a listener
    * 
    * @param name the name of the object to register on
    * @param listener the listener
    * @param filter the filter
    * @param handback the handback object
    * @throws Throwable for any error
    */
   void registerListener(Object name, KernelEventListener listener, KernelEventFilter filter, Object handback) throws Throwable;
   
   /**
    * Unregister a listener
    * 
    * @param name the name of the object to register on
    * @param listener the listener
    * @param filter the filter
    * @param handback the handback object
    * @throws Throwable for any error
    */
   void unregisterListener(Object name, KernelEventListener listener, KernelEventFilter filter, Object handback) throws Throwable;
}
