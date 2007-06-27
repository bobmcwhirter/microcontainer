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

import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.ParentPolicy;
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
      return system.registerClassLoaderPolicy(domain, policy);
   }
   
   @Override
   protected void removeTopLevelClassLoader(DeploymentContext context) throws Exception
   {
      // Remove the classloader
      ClassLoader classLoader = context.getClassLoader();
      system.unregisterClassLoader(classLoader);

      // Reset the module to avoid possible memory leaks
      Module module = context.getTransientAttachments().getAttachment(Module.class);
      if (module == null)
         throw new IllegalStateException("Deployment Context has no module: " + context);
      module.reset();
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
