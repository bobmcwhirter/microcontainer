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
package org.jboss.kernel.plugins.config.property;

import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.config.plugins.property.PropertyConfiguration;
import org.jboss.kernel.plugins.bootstrap.basic.KernelConstants;
import org.jboss.kernel.plugins.config.AbstractKernelConfig;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.bootstrap.KernelInitializer;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.DependencyBuilder;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.event.KernelEventManager;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.kernel.spi.registry.KernelBus;

/**
 * Kernel configuration using properties.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="mailto:les.hazlewood@jboss.org">Les A. Hazlewood</a>
 * @version $Revision$
 */
@SuppressWarnings("deprecation")
public class PropertyKernelConfig extends AbstractKernelConfig
{
   /**
    * Create a configuration
    * 
    * @param properties the properties
    */
   public PropertyKernelConfig(Properties properties)
   {
      super(new PropertyConfiguration(properties));
      initializeProperties();
   }

   protected Properties getProperties()
   {
      PropertyConfiguration config = (PropertyConfiguration) configuration;
      return config.getProperties();
   }
   
   protected void initializeProperties()
   {
      Properties properties = getProperties();
      
      if (properties.isEmpty() == false && log.isTraceEnabled())
      {
         log.trace("Dumping properties");
         TreeSet<Object> names = new TreeSet<Object>(properties.keySet());
         for (Iterator<Object> i = names.iterator(); i.hasNext();)
         {
            String name = (String) i.next();
            log.trace(name + "=" + properties.get(name));
         }
      }
   }
   
   public KernelBus createKernelBus() throws Throwable
   {
      return (KernelBus) getImplementation(
         KernelConstants.KERNEL_BUS_PROPERTY,
         KernelConstants.KERNEL_BUS_CLASS
      );
   }
   
   public KernelConfigurator createKernelConfigurator() throws Throwable
   {
      return (KernelConfigurator) getImplementation(
         KernelConstants.KERNEL_CONFIGURATOR_PROPERTY,
         KernelConstants.KERNEL_CONFIGURATOR_CLASS
      );
   }
   
   public KernelController createKernelController() throws Throwable
   {
      return (KernelController) getImplementation(
         KernelConstants.KERNEL_CONTROLLER_PROPERTY,
         KernelConstants.KERNEL_CONTROLLER_CLASS
      );
   }
   
   public KernelEventManager createKernelEventManager() throws Throwable
   {
      return (KernelEventManager) getImplementation(
         KernelConstants.KERNEL_EVENT_MANAGER_PROPERTY,
         KernelConstants.KERNEL_EVENT_MANAGER_CLASS
      );
   }
   
   public KernelInitializer createKernelInitializer() throws Throwable
   {
      return (KernelInitializer) getImplementation(
         KernelConstants.KERNEL_INITIALIZER_PROPERTY,
         KernelConstants.KERNEL_INITIALIZER_CLASS
      );
   }
   
   public org.jboss.kernel.spi.registry.KernelRegistry createKernelRegistry() throws Throwable
   {
      return (org.jboss.kernel.spi.registry.KernelRegistry) getImplementation(
         KernelConstants.KERNEL_REGISTRY_PROPERTY,
         KernelConstants.KERNEL_REGISTRY_CLASS
      );
   }
   
   public KernelMetaDataRepository createKernelMetaDataRepository() throws Throwable
   {
      return (KernelMetaDataRepository) getImplementation(
         KernelConstants.KERNEL_METADATA_REPOSITORY_PROPERTY,
         KernelConstants.KERNEL_METADATA_REPOSITORY_CLASS
      );
   }
   
   public DependencyBuilder createDefaultDependencyBuilder() throws Throwable
   {
      return (DependencyBuilder) getImplementation(
         KernelConstants.DEPENDENCY_BUILDER_NAME,
         KernelConstants.DEPENDENCY_BUILDER_DEFAULT
      );
   }

   /**
    * Get the implementation for a type
    * 
    * @param type the type
    * @param defaultType the default implementation
    * @return the implementation object
    * @throws Throwable for any error
    */
   protected Object getImplementation(String type, String defaultType) throws Throwable
   {
      Properties properties = getProperties();

      String value = properties.getProperty(type, defaultType);
      if (log.isTraceEnabled())
         log.trace(type + " using property " + value); 

      StringTokenizer tokenizer = new StringTokenizer(value, ":");
      ClassNotFoundException error = null;
      while (tokenizer.hasMoreTokens())
      {
         String className = tokenizer.nextToken();

         ClassLoader cl = Configurator.getClassLoader((BeanMetaData) null);
         try
         {
            BeanInfo info = getBeanInfo(className, cl);
            BeanMetaData metaData = getBeanMetaData(info, className);
            return Configurator.instantiateAndConfigure(this, info, metaData);
         }
         catch (ClassNotFoundException ignored)
         {
            log.trace(className + " not found: " + ignored.getMessage());
            error = ignored;
         }
      }
      if (error != null)
         throw error;
      throw new RuntimeException("Invalid configuration for property " + type + " expected a class name that implements " + type);
   }
   
   /**
    * Get the bean metadata for the class
    * 
    * @param info the bean info
    * @param className the class
    * @return the metadata
    * @throws Exception for any error
    */
   protected BeanMetaData getBeanMetaData(BeanInfo info, String className) throws Exception
   {
      return null;
   }
}
