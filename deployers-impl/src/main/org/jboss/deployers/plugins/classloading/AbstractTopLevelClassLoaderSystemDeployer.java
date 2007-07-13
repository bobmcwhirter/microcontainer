/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.deployers.plugins.classloading;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.classloading.spi.RealClassLoader;
import org.jboss.deployers.spi.deployer.helpers.AbstractTopLevelClassLoaderDeployer;
import org.jboss.deployers.structure.spi.DeploymentContext;

/**
 * AbstractTopLevelClassLoaderSystemDeployer.
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractTopLevelClassLoaderSystemDeployer extends AbstractTopLevelClassLoaderDeployer
{
   /** The classloading */
   private ClassLoading classLoading;
   
   /** The classloader system */
   private ClassLoaderSystem system;
   
   /** The MBeanServer */
   private MBeanServer mbeanServer;
   
   /**
    * Get the classLoading.
    * 
    * @return the classLoading.
    */
   public ClassLoading getClassLoading()
   {
      return classLoading;
   }

   /**
    * Set the classLoading.
    * 
    * @param classLoading the classLoading.
    */
   public void setClassLoading(ClassLoading classLoading)
   {
      this.classLoading = classLoading;
   }

   /**
    * Get the system.
    * 
    * @return the system.
    */
   public ClassLoaderSystem getSystem()
   {
      return system;
   }

   /**
    * Set the system.
    * 
    * @param system the system.
    */
   public void setSystem(ClassLoaderSystem system)
   {
      this.system = system;
   }

   /**
    * Get the mbeanServer.
    * 
    * @return the mbeanServer.
    */
   public MBeanServer getMbeanServer()
   {
      return mbeanServer;
   }

   /**
    * Set the mbeanServer.
    * 
    * @param mbeanServer the mbeanServer.
    */
   public void setMbeanServer(MBeanServer mbeanServer)
   {
      this.mbeanServer = mbeanServer;
   }

   @Override
   protected ClassLoader createTopLevelClassLoader(DeploymentContext context) throws Exception
   {
      if (classLoading == null)
         throw new IllegalStateException("The classLoading has not been set");
      if (system == null)
         throw new IllegalStateException("The system has not been set");

      Module module = context.getTransientAttachments().getAttachment(Module.class);
      if (module == null)
         throw new IllegalStateException("Deployment Context has no module: " + context);

      ClassLoaderPolicy policy = createTopLevelClassLoaderPolicy(context, module);
      
      ClassLoaderDomain domain;
      synchronized (this)
      {
         String domainName = module.getDomainName();
         domain = system.getDomain(domainName);
         if (domain == null)
         {
            ClassLoaderDomain parent = null;
            String parentDomain = module.getParentDomain();
            if (parentDomain != null)
               parent = system.getDomain(parentDomain);
            
            ParentPolicy parentPolicy = module.getParentPolicy(); 

            domain = system.createAndRegisterDomain(domainName, parentPolicy, parent);
         }
      }
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      try
      {
         registerClassLoaderWithMBeanServer(classLoader);
      }
      catch (Throwable t)
      {
         log.warn("Unable to register classloader with mbeanserver: " + classLoader, t);
      }
      return classLoader;
   }
   
   @Override
   protected void removeTopLevelClassLoader(DeploymentContext context) throws Exception
   {
      ClassLoader classLoader = context.getClassLoader();
      try
      {
         unregisterClassLoaderFromMBeanServer(classLoader);
      }
      catch (Throwable t)
      {
         log.warn("Unable to unregister classloader from mbeanserver: " + classLoader, t);
      }
      
      try
      {
         // Remove the classloader
         system.unregisterClassLoader(classLoader);
      }
      finally
      {
         // Reset the module to avoid possible memory leaks
         Module module = context.getTransientAttachments().getAttachment(Module.class);
         if (module == null)
            throw new IllegalStateException("Deployment Context has no module: " + context);
         module.reset();
      }
   }

   /**
    * Register the classloader with the mbeanserver
    * 
    * @param classLoader the classloader
    * @throws Exception for any error
    */
   protected void registerClassLoaderWithMBeanServer(ClassLoader classLoader) throws Exception
   {
      if (mbeanServer == null)
         return;
      
      if (classLoader instanceof RealClassLoader == false)
         return;
      
      RealClassLoader jmxClassLoader = (RealClassLoader) classLoader;
      ObjectName name = jmxClassLoader.getObjectName();
      if (mbeanServer.isRegistered(name))
         return;
      
      mbeanServer.registerMBean(classLoader, name);
   }

   /**
    * Unregister the classloader from the mbeanserver
    * 
    * @param classLoader the classloader
    * @throws Exception for any error
    */
   protected void unregisterClassLoaderFromMBeanServer(ClassLoader classLoader) throws Exception
   {
      if (mbeanServer == null)
         return;
      
      if (classLoader instanceof RealClassLoader == false)
         return;
      
      RealClassLoader jmxClassLoader = (RealClassLoader) classLoader;
      ObjectName name = jmxClassLoader.getObjectName();
      if (mbeanServer.isRegistered(name) == false)
         return;
      mbeanServer.unregisterMBean(name);
   }
   
   /**
    * Create a top level classloader policy
    * 
    * @param context the deployment context
    * @param module the module
    * @return the classloader
    * @throws Exception for any error
    */
   protected abstract ClassLoaderPolicy createTopLevelClassLoaderPolicy(DeploymentContext context, Module module) throws Exception;
}
