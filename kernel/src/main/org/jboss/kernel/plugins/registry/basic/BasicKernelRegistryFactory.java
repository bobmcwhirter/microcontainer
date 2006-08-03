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

import java.util.Map;

import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.kernel.spi.registry.KernelRegistryEntryAlreadyRegisteredException;
import org.jboss.kernel.spi.registry.KernelRegistryEntryNotFoundException;
import org.jboss.kernel.spi.registry.KernelRegistryPlugin;
import org.jboss.logging.Logger;
import org.jboss.util.collection.CollectionsFactory;

/**
 * Basic Kernel registry factory.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class BasicKernelRegistryFactory implements KernelRegistryPlugin
{
   /** The log */
   private static final Logger log = Logger.getLogger(BasicKernelRegistryFactory.class);

   /** The registred entries */
   protected Map<Object, KernelRegistryEntry> entries = CollectionsFactory.createConcurrentReaderMap();

   /**
    * Create a new basic registry factory
    * 
    * @throws Exception for any error
    */
   public BasicKernelRegistryFactory() throws Exception
   {
   }

   public void registerEntry(Object name, KernelRegistryEntry entry)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      if (entry == null)
         throw new IllegalArgumentException("Null entry");

      boolean trace = log.isTraceEnabled();

      synchronized (entries)
      {
         if (entries.containsKey(name))
            throw new KernelRegistryEntryAlreadyRegisteredException("Already registered: " + name);
         entries.put(name, entry);
         entry.setName(name);
      }

      if (trace)
         log.trace("Registered object: '" + entry + "' with name '" + name + "'");
   }

   public KernelRegistryEntry unregisterEntry(Object name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");

      boolean trace = log.isTraceEnabled();

      KernelRegistryEntry entry = null;

      synchronized (entries)
      {
         entry = (KernelRegistryEntry) entries.remove(name);
         if (entry == null)
            throw new KernelRegistryEntryNotFoundException("Not found: " + name);
      }

      if (trace)
         log.trace("Unregistered name: '" + name + "'");

      return entry;
   }

   public KernelRegistryEntry getEntry(Object name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");

      return (KernelRegistryEntry) entries.get(name);
   }
}
