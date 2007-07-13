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

import java.util.List;

import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.DelegateLoader;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.structure.spi.classloading.ClassLoaderMetaData;
import org.jboss.deployers.structure.spi.classloading.ExportAll;

/**
 * Module.
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class Module
{
   /** The domain */
   private Domain domain;
   
   /** The deployment unit */
   private DeploymentUnit deploymentUnit;
   
   /** The classloader metadata */
   private ClassLoaderMetaData metadata;

   /** The delegates */
   private List<? extends DelegateLoader> delegates;
   
   /**
    * Create a new Module.
    * 
    * @param domain the domain
    * @param deploymentUnit the deployment unit
    * @param metadata the metadata
    * @throws IllegalArgumentException for a null parameter
    */
   public Module(Domain domain, DeploymentUnit deploymentUnit, ClassLoaderMetaData metadata)
   {
      if (domain == null)
         throw new IllegalArgumentException("Null domain");
      if (deploymentUnit == null)
         throw new IllegalArgumentException("Null unit");
      if (metadata == null)
         throw new IllegalArgumentException("Null metadata");
      this.domain = domain;
      this.deploymentUnit = deploymentUnit;
      this.metadata = metadata;
   }

   /**
    * Get the deploymentUnit.
    * 
    * @return the deploymentUnit.
    */
   public DeploymentUnit getDeploymentUnit()
   {
      return deploymentUnit;
   }

   /**
    * Get the domain.
    * 
    * @return the domain.
    */
   public Domain getDomain()
   {
      return domain;
   }

   /**
    * Get the metadata.
    * 
    * @return the metadata.
    */
   public ClassLoaderMetaData getMetadata()
   {
      return metadata;
   }

   /**
    * Get the domain name.
    * 
    * @return the domain name.
    */
   public String getDomainName()
   {
      return getDomain().getName();
   }

   /**
    * Get the parentDomain.
    * 
    * @return the parentDomain.
    */
   public String getParentDomain()
   {
      String parentDomain = getMetadata().getParentDomain();
      if (parentDomain == null)
      {
         if (ClassLoaderSystem.DEFAULT_DOMAIN_NAME.equals(getDomainName()) == false)
            return ClassLoaderSystem.DEFAULT_DOMAIN_NAME;
      }
      return parentDomain;
   }

   /**
    * Get the export all for the module
    * 
    * @return the export all
    */
   public ExportAll getExportAll()
   {
      return getMetadata().getExportAll();
   }

   /**
    * Get the import all for the module
    * 
    * @return the import all
    */
   public boolean isImportAll()
   {
      return getMetadata().isImportAll();
   }

   /**
    * Get delegate policy
    * 
    * @return the delegation policy
    */
   public ParentPolicy getParentPolicy()
   {
      if (getMetadata().isJ2seClassLoadingCompliance())
         return ParentPolicy.BEFORE;
      else
         return ParentPolicy.AFTER;
   }

   /**
    * Get the delegate loaders for this module
    * 
    * @return the delegates
    */
   public List<? extends DelegateLoader> getDelegates()
   {
      return delegates;
   }
   
   /**
    * Create the dependencies for the module
    */
   public void createDependencies()
   {
      // TODO JBMICROCONT-186 createDependencies
   }
   
   /**
    * Reset the module
    */
   public void reset()
   {
      delegates = null;
   }
   
   /**
    * Release the module
    */
   public void release()
   {
      domain.removeModule(this);
   }
}
