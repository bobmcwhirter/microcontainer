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
package org.jboss.test.kernel.bootstrap.test;

import junit.framework.Test;

import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.bootstrap.basic.BasicBootstrap;
import org.jboss.kernel.plugins.bootstrap.basic.KernelConstants;
import org.jboss.kernel.plugins.config.property.PropertyKernelConfig;
import org.jboss.kernel.spi.config.KernelConfig;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.event.KernelEventManager;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.kernel.spi.registry.KernelBus;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.test.AbstractTestCaseWithSetup;
import org.jboss.test.AbstractTestDelegate;

/**
 * Bootstrap Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
@SuppressWarnings("deprecation")
public class BootstrapTestCase extends AbstractTestCaseWithSetup
{
   public static Test suite()
   {
      return suite(BootstrapTestCase.class);
   }
   
   /**
    * Default setup with security manager enabled
    * 
    * @param clazz the class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      AbstractTestDelegate delegate = new AbstractTestDelegate(clazz);
      delegate.enableSecurity = true;
      return delegate;
   }

   public BootstrapTestCase(String name)
   {
      super(name);
   }

   public void testBasicBootstrap() throws Exception
   {
      BasicBootstrap bootstrap = new BasicBootstrap();
      bootstrap.run();
      Kernel kernel = bootstrap.getKernel();
      getLog().debug("Got kernel: " + kernel);
      assertNotNull(kernel);
      
      org.jboss.kernel.spi.registry.KernelRegistry registry = kernel.getRegistry();
      assertNotNull(registry);

      check("Kernel", registry, kernel, KernelConstants.KERNEL_NAME, Kernel.class.getName());

      check("KernelRegistry", registry, registry, KernelConstants.KERNEL_REGISTRY_NAME, KernelConstants.KERNEL_REGISTRY_CLASS);

      KernelBus bus = kernel.getBus();
      check("KernelBus", registry, bus, KernelConstants.KERNEL_BUS_NAME, KernelConstants.KERNEL_BUS_CLASS);

      KernelConfig config = kernel.getConfig();
      check("KernelConfig", registry, config, KernelConstants.KERNEL_CONFIG_NAME, PropertyKernelConfig.class.getName());

      KernelConfigurator configurator = kernel.getConfigurator();
      check("KernelConfigurator", registry, configurator, KernelConstants.KERNEL_CONFIGURATOR_NAME, KernelConstants.KERNEL_CONFIGURATOR_CLASS);

      KernelController controller = kernel.getController();
      check("KernelController", registry, controller, KernelConstants.KERNEL_CONTROLLER_NAME, KernelConstants.KERNEL_CONTROLLER_CLASS);

      KernelEventManager eventManager = kernel.getEventManager();
      check("KernelEventManager", registry, eventManager, KernelConstants.KERNEL_EVENT_MANAGER_NAME, KernelConstants.KERNEL_EVENT_MANAGER_CLASS);

      KernelMetaDataRepository metaDataRepository = kernel.getMetaDataRepository();
      check("KernelMetaDataRepository", registry, metaDataRepository, KernelConstants.KERNEL_METADATA_REPOSITORY_NAME, KernelConstants.KERNEL_METADATA_REPOSITORY_CLASS);
   }

   protected void check(String context, org.jboss.kernel.spi.registry.KernelRegistry registry, Object object, Object name, String className) throws Exception
   {
      getLog().debug("Checking " + context + ": " + object);

      assertNotNull(object);
      
      assertEquals(object.getClass().getName(), className);
      
      KernelRegistryEntry entry = registry.getEntry(name);
      Object registered = entry.getTarget();
      assertEquals(object, registered);
   }

   protected void configureLogging()
   {
      //enableTrace("org.jboss");
   }
}
