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
package org.jboss.kernel;

import org.jboss.kernel.spi.config.KernelConfig;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.event.KernelEventManager;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.kernel.spi.registry.KernelBus;
import org.jboss.kernel.spi.registry.KernelRegistry;

/**
 * The kernel.<p>
 * 
 * The kernel is the entry point into the kernel services. 
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="mailto:les.hazlewood@jboss.org">Les A. Hazlewood</a>
 * @version $Revision$
 */
@SuppressWarnings("deprecation")
public class Kernel
{
   /** The permission required to access the kernel */
   public static final KernelPermission ACCESS = new KernelPermission("access");
   
   /** The permission required to configure the kernel */
   public static final KernelPermission CONFIGURE = new KernelPermission("configure");
   
   /** Whether kernel permission checking is active */
   private static final boolean enablePermissionChecking;
   
   /** The kernel bus */
   protected KernelBus bus;
   
   /** The kernel config */
   protected KernelConfig config;
   
   /** The kernel configurator */
   protected KernelConfigurator configurator;
   
   /** The kernel controller */
   protected KernelController controller;
   
   /** The kernel event manager */
   protected KernelEventManager eventManager;
   
   /** The kernel meta data repository */
   protected KernelMetaDataRepository metaDataRepository;
   
   /** The kernel registry */
   protected KernelRegistry registry;

   static
   {
      boolean checkingEnabled = false;
      try
      {
         
         checkingEnabled = Boolean.getBoolean(KernelPermission.class.getName()); 
      }
      catch (Throwable ignored)
      {
      }
      enablePermissionChecking = checkingEnabled;
   }
   
   /**
    * Check whether we can access the kernel
    * 
    * @throws SecurityException if the you don't have KernelPermission('access')
    */
   public static void checkAccess()
   {
      if (enablePermissionChecking)
      {
         SecurityManager sm = System.getSecurityManager();
         if (sm != null)
            sm.checkPermission(ACCESS);
      }
   }
   
   /**
    * Check whether we can configure the kernel
    * 
    * @throws SecurityException if the you don't have KernelPermission('configure')
    */
   public static void checkConfigure()
   {
      if (enablePermissionChecking)
      {
         SecurityManager sm = System.getSecurityManager();
         if (sm != null)
            sm.checkPermission(CONFIGURE);
      }
   }

   /**
    * Get the kernel bus
    * 
    * @return the kernel bus
    * @throws SecurityException if the you don't have KernelPermission('access')
    */
   public KernelBus getBus()
   {
      checkAccess();
      return bus;
   }
   
   /**
    * Set the kernel bus.<p>
    * 
    * @param bus the kernel bus 
    * @throws SecurityException if the you don't have KernelPermission('configure')
    */
   public void setBus(KernelBus bus)
   {
      checkConfigure();
      this.bus = bus;
   }

   /**
    * Get the kernel config
    * 
    * @return the kernel config
    * @throws SecurityException if the you don't have KernelPermission('access')
    */
   public KernelConfig getConfig()
   {
      checkAccess();
      return config;
   }
   
   /**
    * Set the kernel config.<p>
    * 
    * @param config the kernel config 
    * @throws SecurityException if the you don't have KernelPermission('configure')
    */
   public void setConfig(KernelConfig config)
   {
      checkConfigure();
      this.config = config;
   }

   /**
    * Get the kernel configurator
    * 
    * @return the kernel configurator
    * @throws SecurityException if the you don't have KernelPermission('access')
    */
   public KernelConfigurator getConfigurator()
   {
      checkAccess();
      return configurator;
   }
   
   /**
    * Set the kernel configurator.<p>
    * 
    * @param configurator the kernel configurator 
    * @throws SecurityException if the you don't have KernelPermission('configure')
    */
   public void setConfigurator(KernelConfigurator configurator)
   {
      checkConfigure();
      this.configurator = configurator;
   }

   /**
    * Get the kernel controller
    * 
    * @return the kernel controller
    * @throws SecurityException if the you don't have KernelPermission('access')
    */
   public KernelController getController()
   {
      checkAccess();
      return controller;
   }
   
   /**
    * Set the kernel controller.<p>
    * 
    * @param controller the kernel controller 
    * @throws SecurityException if the you don't have KernelPermission('configure')
    */
   public void setController(KernelController controller)
   {
      checkConfigure();
      this.controller = controller;
   }

   /**
    * Get the event manager
    * 
    * @return the event manager
    * @throws SecurityException if the you don't have KernelPermission('access')
    */
   public KernelEventManager getEventManager()
   {
      checkAccess();
      return eventManager;
   }
   
   /**
    * Set the event manager.
    * 
    * @param eventManager the event manager 
    * @throws SecurityException if the you don't have KernelPermission('configure')
    */
   public void setEventManager(KernelEventManager eventManager)
   {
      checkConfigure();
      this.eventManager = eventManager;
   }

   /**
    * Get the kernel registry
    * 
    * @return the kernel registry
    * @throws SecurityException if the you don't have KernelPermission('access')
    */
   public KernelRegistry getRegistry()
   {
      checkAccess();
      return registry;
   }
   
   /**
    * Set the kernel registry.<p>
    * 
    * @param registry the kernel registry 
    * @throws SecurityException if the you don't have KernelPermission('configure')
    */
   public void setRegistry(KernelRegistry registry)
   {
      checkConfigure();
      this.registry = registry;
   }

   /**
    * Get the Meta Data Repository.
    * 
    * @return the meta data repository.
    * @throws SecurityException if the you don't have KernelPermission('access')
    */
   public KernelMetaDataRepository getMetaDataRepository()
   {
      checkAccess();
      return metaDataRepository;
   }

   /**
    * Set the meta data repository.
    * 
    * @param metaDataRepository the meta data repository.
    * @throws SecurityException if the you don't have KernelPermission('configure')
    */
   public void setMetaDataRepository(KernelMetaDataRepository metaDataRepository)
   {
      checkConfigure();
      this.metaDataRepository = metaDataRepository;
   }
}
