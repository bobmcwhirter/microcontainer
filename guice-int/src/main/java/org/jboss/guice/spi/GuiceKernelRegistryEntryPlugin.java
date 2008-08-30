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
package org.jboss.guice.spi;

import org.jboss.kernel.spi.registry.KernelRegistryPlugin;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.kernel.plugins.registry.AbstractKernelRegistryEntry;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Guice;

/**
 * Guice kernel registry plugin.
 * Providing a way to inject Guice beans into MC.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class GuiceKernelRegistryEntryPlugin implements KernelRegistryPlugin
{
   private Injector injector;

   public GuiceKernelRegistryEntryPlugin(Module... modules)
   {
      injector = Guice.createInjector(modules);
   }

   public void destroy()
   {
      injector = null;
   }

   public KernelRegistryEntry getEntry(Object name)
   {
      KernelRegistryEntry entry = null;
      try
      {
         if (name instanceof Class<?>)
         {
            Class<?> clazz = (Class<?>)name;
            entry = new AbstractKernelRegistryEntry(name, injector.getInstance(clazz));
         }
         else if (name instanceof Key)
         {
            Key<?> key = (Key<?>)name;
            entry = new AbstractKernelRegistryEntry(name, injector.getInstance(key));
         }
      }
      catch (Exception ignored)
      {
      }
      return entry;
   }
}
