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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.deployers.structure.spi.DeploymentContext;
import org.jboss.deployers.structure.spi.classloading.ClassLoaderMetaData;

/**
 * Domain.
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class Domain
{
   /** The domain name */
   private String name;
   
   /** The registered deployment contexts */
   private Map<DeploymentContext, Module> contexts = new ConcurrentHashMap<DeploymentContext, Module>();

   /**
    * Create a new Domain.
    * 
    * @param name the name
    * @throws IllegalArgumentException for a null domain
    */
   public Domain(String name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      this.name = name;
   }

   /**
    * Get the name.
    * 
    * @return the name.
    */
   public String getName()
   {
      return name;
   }

   /**
    * Add a deployment context
    * 
    * @param deploymentContext the deployment context
    * @param metadata the classloader metadata 
    * @throws IllegalArgumentException for a null parameter
    */
   public void addDeploymentContext(DeploymentContext deploymentContext, ClassLoaderMetaData metadata)
   {
      Module module = new Module(this, deploymentContext, metadata);
      deploymentContext.getTransientAttachments().addAttachment(Module.class, module);
      contexts.put(deploymentContext, module);
      module.createDependencies();
   }
   
   /**
    * Remove a deployment context
    * 
    * @param module the module
    * @throws IllegalArgumentException for a null parameter
    */
   protected void removeModule(Module module)
   {
      if (module == null)
         throw new IllegalArgumentException("Null module");
      DeploymentContext context = module.getDeploymentContext();
      contexts.remove(context);
   }
}
