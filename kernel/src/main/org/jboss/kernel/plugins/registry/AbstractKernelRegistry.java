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
package org.jboss.kernel.plugins.registry;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.jboss.kernel.plugins.AbstractKernelObject;
import org.jboss.kernel.spi.registry.KernelRegistry;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.kernel.spi.registry.KernelRegistryEntryNotFoundException;
import org.jboss.kernel.spi.registry.KernelRegistryPlugin;
import org.jboss.util.collection.CollectionsFactory;

/**
 * Abstract Kernel registry.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="mailto:les.hazlewood@jboss.org">Les A. Hazlewood</a>
 * @version $Revision$
 */
public abstract class AbstractKernelRegistry extends AbstractKernelObject implements KernelRegistry
{
   /** The registry factories */
   protected List<KernelRegistryPlugin> factories = CollectionsFactory.createCopyOnWriteList();

   /**
    * Create an abstract kernel registry
    * 
    * @throws Exception for any error
    */
   public AbstractKernelRegistry() throws Exception
   {
   }

   /**
    * Add a kernel registry factory
    * 
    * @param factory the factory to add
    */
   public void addKernelRegistryFactory(KernelRegistryPlugin factory)
   {
      factories.add(factory);
      if (log.isTraceEnabled())
         log.trace("Registry " + this + " added registry factory " + factory);
   }

   /**
    * Remove a kernel registry factory
    * 
    * @param factory the factory to remove
    */
   public void removeKernelRegistryFactory(KernelRegistryPlugin factory)
   {
      factories.remove(factory);
      if (log.isTraceEnabled())
         log.trace("Registry " + this + " removed registry factory " + factory);
   }

   public KernelRegistryEntry getEntry(Object name)
   {
      for (ListIterator i = factories.listIterator(); i.hasNext();)
      {
         KernelRegistryPlugin factory = (KernelRegistryPlugin) i.next();
         KernelRegistryEntry entry = factory.getEntry(name);
         if (entry != null)
            return entry;
      }
      throw new KernelRegistryEntryNotFoundException("Entry not found with name: " + name);
   }

   public boolean containsEntry(Object name)
   {
      Iterator i = factories.iterator();
      while (i.hasNext())
      {
         KernelRegistryPlugin factory = (KernelRegistryPlugin) i.next();
         KernelRegistryEntry entry = factory.getEntry(name);
         if (entry != null)
            return true;
      }
      return false;
   }
}
