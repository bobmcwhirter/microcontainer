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
package org.jboss.kernel.plugins.registry.basic;

import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.registry.AbstractKernelRegistry;
import org.jboss.kernel.spi.event.KernelEvent;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.kernel.spi.registry.KernelRegistryPlugin;

/**
 * Basic Kernel registry.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
@SuppressWarnings("deprecation")
public class BasicKernelRegistry extends AbstractKernelRegistry
{
   /** The default registry factory */
   protected BasicKernelRegistryFactory defaultFactory = new BasicKernelRegistryFactory();

   /**
    * Create a new basic registry
    * 
    * @throws Exception for any error
    */
   public BasicKernelRegistry() throws Exception
   {
      addKernelRegistryFactory(defaultFactory);
   }

   public void registerEntry(Object name, KernelRegistryEntry entry)
   {
      Kernel.checkConfigure();

      defaultFactory.registerEntry(name, entry);

      Object target = entry.getTarget();
      if (target != null && target instanceof KernelRegistryPlugin)
      {
         KernelRegistryPlugin factory = (KernelRegistryPlugin) target;
         addKernelRegistryFactory(factory);
      }
      
      if (hasListeners())
      {
         KernelEvent event = createEvent(org.jboss.kernel.spi.registry.KernelRegistry.KERNEL_REGISTRY_REGISTERED, name);
         fireKernelEvent(event);
      }
   }

   public KernelRegistryEntry unregisterEntry(Object name)
   {
      Kernel.checkConfigure();

      KernelRegistryEntry entry = defaultFactory.unregisterEntry(name);

      Object target = entry.getTarget();
      if (target != null && target instanceof KernelRegistryPlugin)
      {
         KernelRegistryPlugin factory = (KernelRegistryPlugin) target;
         removeKernelRegistryFactory(factory);
      }

      if (hasListeners())
      {
         KernelEvent event = createEvent(org.jboss.kernel.spi.registry.KernelRegistry.KERNEL_REGISTRY_UNREGISTERED, name);
         fireKernelEvent(event);
      }
      
      return entry;
   }
}
