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
import java.util.Properties;
import java.util.Set;

import org.jboss.dependency.plugins.AbstractController;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.KernelFactory;
import org.jboss.kernel.plugins.bootstrap.basic.BasicKernelInitializer;
import org.jboss.kernel.plugins.config.property.PropertyKernelConfig;
import org.jboss.kernel.spi.bootstrap.KernelInitializer;
import org.jboss.kernel.spi.config.KernelConfig;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.event.KernelEvent;
import org.jboss.kernel.spi.event.KernelEventFilter;
import org.jboss.kernel.spi.event.KernelEventListener;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.kernel.spi.registry.KernelRegistryPlugin;

/**
 * Scoped Kernel controller.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class ScopedKernelController extends AbstractKernelController
{
   protected Kernel parentKernel;
   protected AbstractController underlyingController;
   protected AbstractController parentController;

   public ScopedKernelController(Kernel parentKernel, AbstractController parentController) throws Exception
   {
      super();
      this.parentKernel = parentKernel;
      if (parentKernel.getController() instanceof AbstractController == false)
         throw new IllegalArgumentException("Underlying controller not AbstractController instance!");
      this.underlyingController = (AbstractController)parentKernel.getController();
      this.parentController = parentController;
      KernelConfig config = new ScopedKernelConfig(System.getProperties());
      kernel = KernelFactory.newInstance(config);
      this.parentController.addController(this);
   }

   private boolean isParentKernelController()
   {
      return (parentController instanceof KernelController);
   }

   private KernelController getParentKernelController()
   {
      if (isParentKernelController() == false)
         throw new IllegalArgumentException("Illegal call to parent Controller, not of KernelController instance!");
      return (KernelController)parentController;
   }

   // Scoped helper methods 

   public AbstractController getUnderlyingController()
   {
      return underlyingController;
   }

   // TODO See comments on super implementation
   public void addControllerContext(ControllerContext context)
   {
      underlyingController.removeControllerContext(context);
      registerControllerContext(context);
   }

   // TODO See comments on super implementation
   public void removeControllerContext(ControllerContext context)
   {
      unregisterControllerContext(context);
      underlyingController.addControllerContext(context);
   }

   public void release()
   {
      parentController.removeController(this);
      underlyingController = null;
      parentController = null;
      parentKernel = null;
   }

   // Controller methods

   public ControllerContext getContext(Object name, ControllerState state)
   {
      ControllerContext context = super.getContext(name, state);
      if (context != null)
      {
         return context;
      }
      return parentController.getContext(name, state);
   }

   public Set<ControllerContext> getNotInstalled()
   {
      Set<ControllerContext> uninstalled = new HashSet<ControllerContext>(parentController.getNotInstalled());
      uninstalled.addAll(super.getNotInstalled());
      return uninstalled;
   }

   protected void install(ControllerContext context, boolean trace) throws Throwable
   {
      throw new IllegalArgumentException("Should not be called!");
   }

   // KernelController methods

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
      lockRead();
      try
      {
         Set<KernelControllerContext> contexts = new HashSet<KernelControllerContext>();
         Set<KernelControllerContext> currentContexts = super.getInstantiatedContexts(clazz);
         if (currentContexts != null && currentContexts.size() > 0)
         {
            contexts.addAll(currentContexts);
         }
         if (isParentKernelController())
         {
            Set<KernelControllerContext> parentContexts = getParentKernelController().getInstantiatedContexts(clazz);
            if (parentContexts != null && parentContexts.size() > 0)
            {
               contexts.addAll(parentContexts);
            }
         }
         return contexts.size() > 0 ? contexts : null;
      }
      finally{
         unlockRead();
      }
   }

   // KernelRegistry plugin method

   public KernelRegistryEntry getEntry(Object name)
   {
      KernelRegistryEntry entry = super.getEntry(name);
      if (entry != null)
         return entry;
      if (parentController instanceof KernelRegistryPlugin)
      {
         return ((KernelRegistryPlugin)parentController).getEntry(name);
      }
      return null;
   }

   // Kernel creation util classes

   private class ScopedKernelConfig extends PropertyKernelConfig
   {
      public ScopedKernelConfig(Properties properties)
      {
         super(properties);
      }

      public KernelInitializer createKernelInitializer() throws Throwable
      {
         return new ScopedKernelInitializer();
      }
   }

   /**
    * Scoped Kernel Initializer.
    * Overriddes the creation of configurator, controller, metadatarepo
    */
   private class ScopedKernelInitializer extends BasicKernelInitializer
   {
      public ScopedKernelInitializer()
            throws Exception
      {
         super();
      }

      protected KernelConfigurator createKernelConfigurator(Kernel kernel) throws Throwable
      {
         return parentKernel.getConfigurator();
      }

      protected KernelController createKernelController(Kernel kernel) throws Throwable
      {
         return ScopedKernelController.this;
      }

      protected KernelMetaDataRepository createKernelMetaDataRepository(Kernel kernel) throws Throwable
      {
         return parentKernel.getMetaDataRepository();
      }
   }

}
