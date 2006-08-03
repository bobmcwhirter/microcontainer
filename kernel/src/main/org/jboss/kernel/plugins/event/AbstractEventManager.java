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
package org.jboss.kernel.plugins.event;

import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.AbstractKernelObject;
import org.jboss.kernel.spi.event.KernelEventEmitter;
import org.jboss.kernel.spi.event.KernelEventFilter;
import org.jboss.kernel.spi.event.KernelEventListener;
import org.jboss.kernel.spi.event.KernelEventManager;
import org.jboss.kernel.spi.registry.KernelRegistry;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;

/**
 * Abstract Event manager.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractEventManager extends AbstractKernelObject implements KernelEventManager
{
   /** The registry */
   protected KernelRegistry registry;

   /**
    * Create a new abstract kernel manager
    * 
    * @throws Throwable for any error
    */
   public AbstractEventManager() throws Throwable
   {
   }
   
   public void setKernel(Kernel kernel) throws Throwable
   {
      super.setKernel(kernel);
      registry = kernel.getRegistry();
   }

   public void registerListener(Object name, KernelEventListener listener, KernelEventFilter filter, Object handback) throws Throwable
   {
      getEmitter(name).registerListener(listener, filter, handback);
   }

   public void unregisterListener(Object name, KernelEventListener listener, KernelEventFilter filter, Object handback) throws Throwable
   {
      getEmitter(name).unregisterListener(listener, filter, handback);
   }
   
   protected KernelEventEmitter getEmitter(Object name) throws Throwable
   {
      KernelRegistryEntry entry = registry.getEntry(name);

      Object object = entry.getTarget();
      if (object == null || object instanceof KernelEventEmitter == false)
         throw new ClassCastException(name + " is not a " + KernelEventEmitter.class.getName());
      
      return (KernelEventEmitter) object;
   }
}
