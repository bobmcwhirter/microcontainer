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
package org.jboss.kernel.plugins.bootstrap.basic;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.bootstrap.AbstractKernelInitializer;
import org.jboss.kernel.plugins.registry.BeanKernelRegistryEntry;
import org.jboss.kernel.spi.config.KernelConfig;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.event.KernelEventManager;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.kernel.spi.registry.KernelBus;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.kernel.spi.validation.KernelBeanValidator;

/**
 * Bootstrap the kernel.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @version $Revision$
 */
@SuppressWarnings("deprecation")
public class BasicKernelInitializer extends AbstractKernelInitializer
{
   /**
    * Create a new basic kernel factory
    * 
    * @throws Exception for any error
    */
   public BasicKernelInitializer() throws Exception
   {
   }
   
   public void initKernel(Kernel kernel) throws Throwable
   {
      KernelMetaDataRepository metaDataRepository = createKernelMetaDataRepository(kernel);
      if (trace)
         log.trace("Using MetaDataRepository: " + metaDataRepository);
      kernel.setMetaDataRepository(metaDataRepository);

      org.jboss.kernel.spi.registry.KernelRegistry registry = createKernelRegistry(kernel);
      if (trace)
         log.trace("Using Registry: " + registry);
      registry.setKernel(kernel);
      kernel.setRegistry(registry);
      
      KernelEventManager eventManager = createKernelEventManager(kernel);
      if (trace)
         log.trace("Using EventManager: " + eventManager);
      eventManager.setKernel(kernel);
      kernel.setEventManager(eventManager);
      
      KernelConfigurator configurator = createKernelConfigurator(kernel);
      if (trace)
         log.trace("Using Configurator: " + configurator);
      configurator.setKernel(kernel);
      kernel.setConfigurator(configurator);
      
      KernelController controller = createKernelController(kernel);
      if (trace)
         log.trace("Using Controller: " + controller);
      controller.setKernel(kernel);
      kernel.setController(controller);
      
      KernelBus bus = createKernelBus(kernel);
      if (trace)
         log.trace("Using Bus: " + bus);
      bus.setKernel(kernel);
      kernel.setBus(bus);

      KernelBeanValidator validator = createKernelBeanValidator(kernel);
      if (trace)
         log.trace("Using Validator: " + validator);
      validator.setKernel(kernel);
      kernel.setValidator(validator);

      // Register everything
      register(kernel, KernelConstants.KERNEL_CONFIG_NAME, kernel.getConfig());
      register(kernel, KernelConstants.KERNEL_INITIALIZER_NAME, this);
      register(kernel, KernelConstants.KERNEL_NAME, kernel);
      register(kernel, KernelConstants.KERNEL_REGISTRY_NAME, registry);
      register(kernel, KernelConstants.KERNEL_EVENT_MANAGER_NAME, eventManager);
      register(kernel, KernelConstants.KERNEL_BUS_NAME, bus);
      register(kernel, KernelConstants.KERNEL_CONFIGURATOR_NAME, configurator);
      register(kernel, KernelConstants.KERNEL_CONTROLLER_NAME, controller);
      register(kernel, KernelConstants.KERNEL_METADATA_REPOSITORY_NAME, metaDataRepository);
      register(kernel, KernelConstants.KERNEL_BEAN_VALIDATOR_NAME, validator);
   }
   
   /**
    * Create the kernel bus
    * 
    * @param kernel the kernel
    * @return the kernel bus
    * @throws Throwable for any error
    */
   protected KernelBus createKernelBus(Kernel kernel) throws Throwable
   {
      return kernel.getConfig().createKernelBus();
   }
   
   /**
    * Create the configurator
    * 
    * @param kernel the kernel
    * @return the configurator
    * @throws Throwable for any error
    */
   protected KernelConfigurator createKernelConfigurator(Kernel kernel) throws Throwable
   {
      return kernel.getConfig().createKernelConfigurator();
   }
   
   /**
    * Create the kernel controller 
    * 
    * @param kernel the kernel
    * @return the kernel controller
    * @throws Throwable for any error
    */
   protected KernelController createKernelController(Kernel kernel) throws Throwable
   {
      return kernel.getConfig().createKernelController();
   }
   
   /**
    * Create the kernel event manager
    * 
    * @param kernel the kernel
    * @return the kernel event manager
    * @throws Throwable for any error
    */
   protected KernelEventManager createKernelEventManager(Kernel kernel) throws Throwable
   {
      return kernel.getConfig().createKernelEventManager();
   }
   
   /**
    * Create the kernel registry
    * 
    * @param kernel the kernel
    * @return the kernel registry
    * @throws Throwable for any error
    */
   protected org.jboss.kernel.spi.registry.KernelRegistry createKernelRegistry(Kernel kernel) throws Throwable
   {
      return kernel.getConfig().createKernelRegistry();
   }
   
   /**
    * Create the meta data repository
    * 
    * @param kernel the kernel
    * @return the meta data repository
    * @throws Throwable for any error
    */
   protected KernelMetaDataRepository createKernelMetaDataRepository(Kernel kernel) throws Throwable
   {
      return kernel.getConfig().createKernelMetaDataRepository();
   }

   /**
    * Create the bean validator
    *
    * @param kernel the kernel
    * @return the bean validator
    * @throws Throwable for any error
    */
   protected KernelBeanValidator createKernelBeanValidator(Kernel kernel) throws Throwable
   {
      return kernel.getConfig().createKernelBeanValidator();
   }

   /**
    * Register an object
    * 
    * @param kernel the kernel
    * @param name the name
    * @param object the object
    * @throws Throwable for any error
    */
   protected void register(Kernel kernel, String name, Object object) throws Throwable
   {
      register(kernel, getName(name), object);
   }

   /**
    * Register an object
    * 
    * @param kernel the kernel
    * @param name the name
    * @param object the object
    * @throws Throwable for any error
    */
   protected void register(Kernel kernel, Object name, Object object) throws Throwable
   {
      KernelRegistryEntry entry = createKernelRegistryEntry(kernel, object);
      org.jboss.kernel.spi.registry.KernelRegistry registry = kernel.getRegistry();
      registry.registerEntry(name, entry);
   }
   
   /**
    * Get the registration name
    * 
    * @param name the name
    * @return the registration name
    * @throws Throwable for any error
    */
   protected Object getName(String name) throws Throwable
   {
      return name;
   }

   /**
    * Create a kernel registry entry
    * 
    * @param kernel the kernel
    * @param object the object
    * @return the entry
    * @throws Throwable for any error
    */
   protected KernelRegistryEntry createKernelRegistryEntry(Kernel kernel, Object object) throws Throwable
   {
      KernelConfig config = kernel.getConfig();
      BeanInfo info = config.getBeanInfo(object.getClass());
      return new BeanKernelRegistryEntry(object, info);
   }
}
