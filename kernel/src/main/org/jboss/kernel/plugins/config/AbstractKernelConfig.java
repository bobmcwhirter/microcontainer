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
package org.jboss.kernel.plugins.config;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.BeanInfoFactory;
import org.jboss.classadapter.spi.ClassAdapterFactory;
import org.jboss.config.spi.Configuration;
import org.jboss.config.spi.ConfigurationPermission;
import org.jboss.joinpoint.spi.JoinpointFactoryBuilder;
import org.jboss.kernel.plugins.AbstractKernelObject;
import org.jboss.kernel.spi.config.KernelConfig;
import org.jboss.kernel.spi.dependency.DependencyBuilder;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.reflect.spi.TypeInfoFactory;

/**
 * Abstract Kernel configuration.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="mailto:les.hazlewood@jboss.org">Les A. Hazlewood</a>
 * @version $Revision$
 */
public abstract class AbstractKernelConfig extends AbstractKernelObject implements KernelConfig
{
   /** The configuration */
   protected Configuration configuration;

   /** The dependency builder */
   private DependencyBuilder dependencyBuilder;
   
   /**
    * Create an abstract kernel configuration
    * 
    * @param configuration the configuration
    */
   public AbstractKernelConfig(Configuration configuration)
   {
      this.configuration = configuration;
   }
   
   public BeanInfo getBeanInfo(String className, ClassLoader cl) throws Throwable
   {
      return configuration.getBeanInfo(className, cl);
   }
   
   public BeanInfo getBeanInfo(Class<?> clazz) throws Throwable
   {
      return configuration.getBeanInfo(clazz);
   }
   
   public BeanInfo getBeanInfo(TypeInfo typeInfo) throws Throwable
   {
      return configuration.getBeanInfo(typeInfo);
   }
   
   public TypeInfo getTypeInfo(String className, ClassLoader cl) throws Throwable
   {
      return configuration.getTypeInfo(className, cl);
   }
   
   public TypeInfo getTypeInfo(Class<?> clazz) throws Throwable
   {
      return configuration.getTypeInfo(clazz);
   }
   
   public ClassInfo getClassInfo(String className, ClassLoader cl) throws Throwable
   {
      return configuration.getClassInfo(className, cl);
   }
   
   public ClassInfo getClassInfo(Class<?> clazz) throws Throwable
   {
      return configuration.getClassInfo(clazz);
   }

   /**
    * Set the beanInfoFactory.
    * 
    * @param beanInfoFactory the beanInfoFactory.
    * @throws SecurityException if you don't have the ConfigurationPermission
    */
   public void setBeanInfoFactory(BeanInfoFactory beanInfoFactory)
   {
      configuration.setBeanInfoFactory(beanInfoFactory);
   }

   /**
    * Set the classAdapterFactory.
    * 
    * @param classAdapterFactory the classAdapterFactory.
    * @throws SecurityException if you don't have the ConfigurationPermission
    */
   public void setClassAdapterFactory(ClassAdapterFactory classAdapterFactory)
   {
      configuration.setClassAdapterFactory(classAdapterFactory);
   }

   /**
    * Set the typeInfoFactory.
    * 
    * @param typeInfoFactory the typeInfoFactory.
    * @throws SecurityException if you don't have the ConfigurationPermission
    */
   public void setTypeInfoFactory(TypeInfoFactory typeInfoFactory)
   {
      configuration.setTypeInfoFactory(typeInfoFactory);
   }

   /**
    * Set the joinpointFactoryBuilder.
    * 
    * @param joinpointFactoryBuilder the joinpointFactoryBuilder.
    * @throws SecurityException if you don't have the ConfigurationPermission
    */
   public void setJoinpointFactoryBuilder(JoinpointFactoryBuilder joinpointFactoryBuilder)
   {
      configuration.setJoinpointFactoryBuilder(joinpointFactoryBuilder);
   }

   /**
    * Get the dependency builder
    * 
    * @return the dependency builder
    */
   public DependencyBuilder getDependencyBuilder()
   {
      if (dependencyBuilder == null)
      {
         try
         {
            dependencyBuilder = createDefaultDependencyBuilder();
         }
         catch (RuntimeException e)
         {
            throw e;
         }
         catch (Error e)
         {
            throw e;
         }
         catch (Throwable t)
         {
            throw new RuntimeException("Error creating dependency builder", t);
         }
      }
      return dependencyBuilder;
   }
   
   /**
    * Set the dependencyBuilder.
    * 
    * @param dependencyBuilder the dependencyBuilder.
    * @throws SecurityException if you don't have the ConfigurationPermission
    */
   public void setDependencyBuilder(DependencyBuilder dependencyBuilder)
   {
      checkPermissionName("dependencyBuilder");
      this.dependencyBuilder = dependencyBuilder;
   }

   /**
    * Create the default dependency builder
    * 
    * @return the type info factory
    * @throws Throwable for any error
    */
   protected abstract DependencyBuilder createDefaultDependencyBuilder() throws Throwable;

   /**
    * Check a permission
    * 
    * @param name the name of the permission
    */
   private void checkPermissionName(String name)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(new ConfigurationPermission(name));
      
   }
}
