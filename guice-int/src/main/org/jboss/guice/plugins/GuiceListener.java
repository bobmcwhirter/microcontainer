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
package org.jboss.guice.plugins;

import com.google.inject.Binder;
import com.google.inject.spi.SourceProviders;
import org.jboss.guice.spi.ControllerContextBindFilter;
import org.jboss.guice.spi.GuiceIntegration;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.event.KernelEvent;
import org.jboss.kernel.spi.event.KernelEventListener;
import org.jboss.kernel.spi.registry.KernelRegistry;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;

/**
 * Microcontainer listener that binds newly added
 * controller context to binder.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class GuiceListener extends GuiceIntegration implements KernelEventListener
{
   static
   {
      SourceProviders.skip(GuiceListener.class);
   }

   private KernelRegistry registry;
   private ControllerContextBindFilter filter;

   public GuiceListener(Kernel kernel, Binder binder)
   {
      super(kernel, binder);
      this.registry = kernel.getRegistry();
   }

   public void start() throws Throwable
   {
      registry.registerListener(this, null, null);
   }

   public void stop() throws Throwable
   {
      registry.unregisterListener(this, null, null);
   }

   public void onEvent(KernelEvent event, Object handback)
   {
      Object name = event.getContext();
      String type = event.getType();
      if (KernelRegistry.KERNEL_REGISTRY_REGISTERED.equals(type))
      {
         Object source = event.getSource();
         if (source instanceof KernelRegistry)
         {
            KernelRegistry registry = (KernelRegistry)source;
            KernelRegistryEntry entry = registry.getEntry(name);
            if (filter == null || filter.bind(entry))
               bindContext(getBinder(), getController(), entry);
         }
      }
      else if (KernelRegistry.KERNEL_REGISTRY_UNREGISTERED.equals(type))
      {
         // todo - can we unbind from binder?
      }
   }

   public void setFilter(ControllerContextBindFilter filter)
   {
      this.filter = filter;
   }
}
