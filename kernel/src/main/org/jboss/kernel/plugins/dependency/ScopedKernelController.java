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
package org.jboss.kernel.plugins.dependency;

import java.util.HashSet;
import java.util.Set;

import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.event.KernelEvent;
import org.jboss.kernel.spi.event.KernelEventFilter;
import org.jboss.kernel.spi.event.KernelEventListener;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.kernel.spi.registry.KernelRegistryPlugin;

/**
 * Scoped Kernel controller.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class ScopedKernelController extends AbstractKernelController
{
   protected Controller parent;

   public ScopedKernelController(Controller parentController)
         throws Exception
   {
      super();
      parent = parentController;
      parent.addController(this);
   }

   private boolean isParentKernelController()
   {
      return (parent instanceof KernelController);
   }

   private KernelController getParentKernelController()
   {
      if (isParentKernelController() == false)
         throw new IllegalArgumentException("Illegal call to parent Controller, not of KernelController instance!");
      return (KernelController)parent;
   }

   // Controller methods

   public ControllerContext getContext(Object name, ControllerState state)
   {
      ControllerContext context = super.getContext(name, state);
      if (context != null)
      {
         return context;
      }
      return parent.getContext(name, state);
   }

   public Set<ControllerContext> getNotInstalled()
   {
      Set<ControllerContext> uninstalled = new HashSet<ControllerContext>(parent.getNotInstalled());
      uninstalled.addAll(super.getNotInstalled());
      return uninstalled;
   }

   public ControllerContext uninstall(Object name)
   {
      return super.uninstall(name);    //todo
   }

   protected void install(ControllerContext context, boolean trace) throws Throwable
   {
      super.install(context, trace);    //todo
   }

   protected void change(ControllerContext context, ControllerState state, boolean trace) throws Throwable
   {
      super.change(context, state, trace);    //todo
   }

   protected void enableOnDemand(ControllerContext context, boolean trace) throws Throwable
   {
      super.enableOnDemand(context, trace);    //todo
   }

   protected boolean incrementState(ControllerContext context, boolean trace)
   {
      return super.incrementState(context, trace);    //todo
   }

   protected void resolveContexts(boolean trace)
   {
      super.resolveContexts(trace);    //todo
   }

   protected boolean resolveContexts(ControllerState fromState, ControllerState toState, boolean trace)
   {
      return super.resolveContexts(fromState, toState, trace);    //todo
   }

   protected void uninstallContext(ControllerContext context, boolean trace)
   {
      super.uninstallContext(context, trace);    //todo
   }

   // KernelController methods

   public Kernel getKernel()
   {
      if (isParentKernelController())
      {
         return getParentKernelController().getKernel();
      }
      else
      {
         return super.getKernel();
      }
   }

   public void setKernel(Kernel kernel) throws Throwable
   {
      if (isParentKernelController())
      {
         getParentKernelController().setKernel(kernel);
      }
      else
      {
         super.setKernel(kernel);
      }
   }

   public void fireKernelEvent(KernelEvent event)
   {
      if (isParentKernelController())
      {
         getParentKernelController().fireKernelEvent(event);
      }
      else
      {
         super.fireKernelEvent(event);
      }
   }

   public void registerListener(KernelEventListener listener, KernelEventFilter filter, Object handback) throws Throwable
   {
      if (isParentKernelController())
      {
         getParentKernelController().registerListener(listener, filter, handback);
      }
      else
      {
         super.registerListener(listener, filter, handback);
      }
   }

   public void unregisterListener(KernelEventListener listener, KernelEventFilter filter, Object handback) throws Throwable
   {
      if (isParentKernelController())
      {
         getParentKernelController().unregisterListener(listener, filter, handback);
      }
      else
      {
         super.unregisterListener(listener, filter, handback);
      }
   }

   public Set<KernelControllerContext> getInstantiatedContexts(Class clazz)
   {
      // todo - some locking?
      Set<KernelControllerContext> contexts = new HashSet<KernelControllerContext>();
      Set<KernelControllerContext> currentContexts = super.getInstantiatedContexts(clazz);
      if (currentContexts != null && currentContexts.size() > 0)
      {
         contexts.addAll(currentContexts);
      }
      if (isParentKernelController())
      {
         Set<KernelControllerContext> parentContexts = ((KernelController)parent).getInstantiatedContexts(clazz);
         if (parentContexts != null && parentContexts.size() > 0)
         {
            contexts.addAll(parentContexts);
         }
      }
      return contexts.size() > 0 ? contexts : null;
   }

   // KernelRegistry plugin method

   public KernelRegistryEntry getEntry(Object name)
   {
      KernelRegistryEntry entry = super.getEntry(name);
      if (entry != null)
         return entry;
      if (parent instanceof KernelRegistryPlugin)
      {
         return ((KernelRegistryPlugin)parent).getEntry(name);
      }
      return null;
   }

}
