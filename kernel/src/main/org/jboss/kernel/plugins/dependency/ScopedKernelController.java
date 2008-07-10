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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.jboss.dependency.plugins.AbstractController;
import org.jboss.dependency.plugins.action.ControllerContextAction;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.ScopeInfo;
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
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.util.JBossStringBuilder;

/**
 * Scoped Kernel controller.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class ScopedKernelController extends AbstractKernelController
{
   protected Kernel parentKernel;
   private ScopeKey scopeKey;

   public ScopedKernelController(Kernel parentKernel, AbstractController parentController, ScopeKey scopeKey) throws Exception
   {
      super();
      this.parentKernel = parentKernel;
      this.scopeKey = scopeKey;
      if (parentKernel.getController() instanceof AbstractController == false)
         throw new IllegalArgumentException("Underlying controller not AbstractController instance!");
      setUnderlyingController((AbstractController)parentKernel.getController());
      setParentController(parentController);
      KernelConfig config = new ScopedKernelConfig(System.getProperties());
      kernel = KernelFactory.newInstance(config);
      getParentController().addController(this);
   }

   /**
    * Get scope key.
    *
    * @return the scope key
    */
   protected ScopeKey getScopeKey()
   {
      return scopeKey;
   }

   /**
    * Is parent controller a kernel controller.
    *
    * @return true if parent controller in kernel controller
    */
   private boolean isParentKernelController()
   {
      return (getParentController() instanceof KernelController);
   }

   /**
    * Get parent kernel controller.
    *
    * Exception is thrown if the underlying controller
    * is not kernel controller.
    *
    * @return kernel controller
    */
   private KernelController getParentKernelController()
   {
      if (isParentKernelController() == false)
         throw new IllegalArgumentException("Illegal call to parent Controller, not of KernelController instance!");
      return (KernelController)getParentController();
   }

   // Scoped helper methods 

   void addScopedControllerContext(ControllerContext context)
   {
      super.addControllerContext(context);
   }

   void removeScopedControllerContext(ControllerContext context)
   {
      super.removeControllerContext(context);
   }

   /**
    * Perform release of resources.
    */
   void release()
   {
      getParentController().removeController(this);
      setUnderlyingController(null);
      setParentController(null);
      parentKernel = null;
   }

   // Controller methods

   protected Map<ControllerState, ControllerContextAction> createAliasActions()
   {
      Map<ControllerState, ControllerContextAction> map = new HashMap<ControllerState, ControllerContextAction>(super.createAliasActions());
      map.put(ControllerState.PRE_INSTALL, InstallExistingScopeAction.INSTANCE);
      return map;
   }

   protected void preAliasInstall(ControllerContext aliasContext)
   {
      ScopeInfo scopeInfo = aliasContext.getScopeInfo();
      scopeInfo.setInstallScope(scopeKey);
   }

   // override, since kernel's contexts are extended with registry plugin
   public ControllerContext getContextLocally(Object name, ControllerState state)
   {
      return super.getContext(name, state);
   }

   public ControllerContext getContext(Object name, ControllerState state)
   {
      ControllerContext context = super.getContext(name, state);
      if (context != null)
      {
         return context;
      }
      return getParentController().getContext(name, state);
   }

   public Set<ControllerContext> getNotInstalled()
   {
      Set<ControllerContext> uninstalled = new HashSet<ControllerContext>(getParentController().getNotInstalled());
      uninstalled.addAll(super.getNotInstalled());
      return uninstalled;
   }

   protected void install(ControllerContext context, boolean trace) throws Throwable
   {
      // we only allow install at top level
      getParentController().install(context);
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

   public Set<KernelControllerContext> getInstantiatedContexts(Class<?> clazz)
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

   public Set<KernelControllerContext> getContexts(Class<?> clazz, ControllerState state)
   {
      lockRead();
      try
      {
         Set<KernelControllerContext> contexts = new HashSet<KernelControllerContext>();
         Set<KernelControllerContext> currentContexts = super.getContexts(clazz, state);
         if (currentContexts != null && currentContexts.size() > 0)
         {
            contexts.addAll(currentContexts);
         }
         if (isParentKernelController())
         {
            Set<KernelControllerContext> parentContexts = getParentKernelController().getContexts(clazz, state);
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

   // org.jboss.kernel.spi.registry.KernelRegistry plugin method

   public KernelRegistryEntry getEntry(Object name)
   {
      KernelRegistryEntry entry = super.getEntry(name);
      if (entry != null)
         return entry;
      if (getParentController() instanceof KernelRegistryPlugin)
      {
         return ((KernelRegistryPlugin)getParentController()).getEntry(name);
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

   /**
    * Add scope key info to toString.
    *
    * @param buffer the string buffer
    */
   protected void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
      buffer.append(getScopeKey());
   }
}
