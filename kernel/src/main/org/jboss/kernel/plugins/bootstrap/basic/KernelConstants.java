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

import org.jboss.kernel.spi.bootstrap.KernelInitializer;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.DependencyBuilder;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.event.KernelEventManager;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.kernel.spi.registry.KernelBus;

/**
 * Constants.<p>
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="mailto:les.hazlewood@jboss.org">Les A. Hazlewood</a>
 * @version $Revision$
 */
@SuppressWarnings("deprecation")
public interface KernelConstants
{
   /** The default kernel name */
   static final String KERNEL_NAME = "jboss.kernel:service=Kernel"; 
   
   /** The default kernel bus name */
   static final String KERNEL_BUS_NAME = "jboss.kernel:service=KernelBus"; 
   
   /** The kernel bus property */
   static final String KERNEL_BUS_PROPERTY = KernelBus.class.getName(); 
   
   /** The default kernel bus implementation */
   static final String KERNEL_BUS_CLASS = "org.jboss.kernel.plugins.registry.basic.BasicKernelBus"; 
   
   /** The default kernel config name */
   static final String KERNEL_CONFIG_NAME = "jboss.kernel:service=KernelConfig"; 
   
   /** The default kernel configurator name */
   static final String KERNEL_CONFIGURATOR_NAME = "jboss.kernel:service=KernelConfigurator"; 
   
   /** The kernel configurator property */
   static final String KERNEL_CONFIGURATOR_PROPERTY = KernelConfigurator.class.getName(); 
   
   /** The default kernel configurator implementation */
   static final String KERNEL_CONFIGURATOR_CLASS = "org.jboss.kernel.plugins.config.AbstractKernelConfigurator"; 
   
   /** The default kernel controller name */
   static final String KERNEL_CONTROLLER_NAME = "jboss.kernel:service=KernelController"; 
   
   /** The kernel controller property */
   static final String KERNEL_CONTROLLER_PROPERTY = KernelController.class.getName(); 
   
   /** The default kernel controller implementation */
   static final String KERNEL_CONTROLLER_CLASS = "org.jboss.kernel.plugins.dependency.AbstractKernelController"; 
   
   /** The default kernel event manager name */
   static final String KERNEL_EVENT_MANAGER_NAME = "jboss.kernel:service=KernelEventManager"; 
   
   /** The kernel event manager property */
   static final String KERNEL_EVENT_MANAGER_PROPERTY = KernelEventManager.class.getName(); 
   
   /** The default kernel event manager implementation */
   static final String KERNEL_EVENT_MANAGER_CLASS = "org.jboss.kernel.plugins.event.AbstractEventManager"; 
   
   /** The default kernel initializer */
   static final String KERNEL_INITIALIZER_NAME = "jboss.kernel:service=KernelInit"; 
   
   /** The kernel initializer property */
   static final String KERNEL_INITIALIZER_PROPERTY = KernelInitializer.class.getName(); 
   
   /** The default kernel initializer implementation */
   static final String KERNEL_INITIALIZER_CLASS = "org.jboss.kernel.plugins.bootstrap.basic.BasicKernelInitializer"; 
   
   /** The default kernel registry name */
   static final String KERNEL_REGISTRY_NAME = "jboss.kernel:service=KernelRegistry"; 
   
   /** The kernel registry property */
   static final String KERNEL_REGISTRY_PROPERTY = org.jboss.kernel.spi.registry.KernelRegistry.class.getName(); 
   
   /** The default kernel registry implementation */
   static final String KERNEL_REGISTRY_CLASS = "org.jboss.kernel.plugins.registry.basic.BasicKernelRegistry"; 
   
   /** The default meta data repository name */
   static final String KERNEL_METADATA_REPOSITORY_NAME = "jboss.kernel:service=MetaDataRepository"; 
   
   /** The meta data repository property */
   static final String KERNEL_METADATA_REPOSITORY_PROPERTY = KernelMetaDataRepository.class.getName(); 
   
   /** The default meta data repository implementation */
   static final String KERNEL_METADATA_REPOSITORY_CLASS = "org.jboss.kernel.plugins.metadata.basic.BasicKernelMetaDataRepository"; 

   /** The DependencyBuilder property name */
   static final String DEPENDENCY_BUILDER_NAME = DependencyBuilder.class.getName();
   
   /** The DependencyBuilder default value */
   static final String DEPENDENCY_BUILDER_DEFAULT="org.jboss.aop.microcontainer.integration.AOPDependencyBuilder:org.jboss.kernel.spi.dependency.helpers.AbstractDependencyBuilder";
}
