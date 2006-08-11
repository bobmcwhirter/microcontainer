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
package org.jboss.kernel.plugins.dependency;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.SupplyMetaData;
import org.jboss.dependency.plugins.AbstractController;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.event.AbstractEventEmitter;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.event.KernelEvent;
import org.jboss.kernel.spi.event.KernelEventFilter;
import org.jboss.kernel.spi.event.KernelEventListener;
import org.jboss.kernel.spi.registry.KernelRegistry;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.kernel.spi.registry.KernelRegistryPlugin;
import org.jboss.util.collection.CollectionsFactory;

/**
 * Abstract Kernel controller.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractKernelController extends AbstractController implements KernelController, KernelRegistryPlugin
{
   /** The kernel */
   protected Kernel kernel;

   /** The emitter delegate */
   protected AbstractEventEmitter emitterDelegate = new AbstractEventEmitter();
   /** The supplies */
   protected Map<Object, List<KernelControllerContext>> suppliers = CollectionsFactory.createConcurrentReaderMap();

   /**
    * Create an abstract kernel controller
    * 
    * @throws Exception for any error
    */
   public AbstractKernelController() throws Exception
   {
   }

   public KernelControllerContext install(BeanMetaData metaData) throws Throwable
   {
      return install(metaData, null);
   }

   public KernelControllerContext install(BeanMetaData metaData, Object target) throws Throwable
   {
      KernelControllerContext context = new AbstractKernelControllerContext(null, metaData, target);
      install(context);
      return context;
   }

   public KernelRegistryEntry getEntry(Object name)
   {
      List<KernelControllerContext> list = suppliers.get(name);
      if (list != null && list.isEmpty() == false)
         return list.get(0);
      else
         return null;
   }

   public ControllerContext getContext(Object name, ControllerState state)
   {
      ControllerContext context = super.getContext(name, state);
      if (context != null)
         return context;
      if (state == null || state == ControllerState.INSTALLED)
      {
         KernelRegistry registry = kernel.getRegistry();
         try
         {
            return registry.getEntry(name);
         }
         catch (Throwable ignored)
         {
         }
      }
      return null;
   }

   public void addSupplies(KernelControllerContext context)
   {
      BeanMetaData metaData = context.getBeanMetaData();
      Set<SupplyMetaData> supplies = metaData.getSupplies();
      if (supplies != null)
      {
         boolean trace = log.isTraceEnabled();

         if (supplies.isEmpty() == false)
         {
            lockWrite();
            try
            {
               for (SupplyMetaData supplied : supplies)
               {
                  Object supply = supplied.getSupply();
                  List<KernelControllerContext> list = suppliers.get(supply);
                  if (list == null)
                  {
                     list = CollectionsFactory.createCopyOnWriteList();
                     suppliers.put(supply, list);
                  }
                  list.add(context);
                  if (trace)
                     log.trace("Suppliers of " + supply + ": " + list);
               }
            }
            finally
            {
               unlockWrite();
            }
         }
      }
   }

   public void removeSupplies(KernelControllerContext context)
   {
      BeanMetaData metaData = context.getBeanMetaData();
      Set<SupplyMetaData> supplies = metaData.getSupplies();
      if (supplies != null)
      {
         boolean trace = log.isTraceEnabled();

         if (supplies.isEmpty() == false)
         {
            lockWrite();
            try
            {
               for (SupplyMetaData supplied : supplies)
               {
                  Object supply = supplied.getSupply();
                  List<KernelControllerContext> list = suppliers.get(supply);
                  if (list != null)
                  {
                     list.remove(context);
                     if (list.isEmpty())
                        suppliers.remove(supply);
                     if (trace)
                        log.trace("Suppliers of " + supply  + ": " + list);
                  }
               }
            }
            finally
            {
               unlockWrite();
            }
         }
      }
   }

   public Kernel getKernel()
   {
      Kernel.checkAccess();
      return kernel;
   }

   public void setKernel(Kernel kernel) throws Throwable
   {
      Kernel.checkConfigure();
      this.kernel = kernel;
   }

   public void fireKernelEvent(KernelEvent event)
   {
      emitterDelegate.fireKernelEvent(event);
   }

   public void registerListener(KernelEventListener listener, KernelEventFilter filter, Object handback) throws Throwable
   {
      emitterDelegate.registerListener(listener, filter, handback);
   }

   public void unregisterListener(KernelEventListener listener, KernelEventFilter filter, Object handback) throws Throwable
   {
      emitterDelegate.unregisterListener(listener, filter, handback);
   }
}
