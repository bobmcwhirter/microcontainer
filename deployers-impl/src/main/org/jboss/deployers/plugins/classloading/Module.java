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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.DelegateLoader;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.classloader.spi.filter.FilteredDelegateLoader;
import org.jboss.dependency.spi.Controller;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.structure.spi.classloading.Capability;
import org.jboss.deployers.structure.spi.classloading.ClassLoaderMetaData;
import org.jboss.deployers.structure.spi.classloading.ExportAll;
import org.jboss.deployers.structure.spi.classloading.PackageCapability;
import org.jboss.deployers.structure.spi.classloading.Requirement;
import org.jboss.util.id.GUID;

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

   /** The requirements */
   private List<RequirementDependencyItem> requirementDependencies;
   
   /** The URL for the dynamic classes */
   private URL dynamicClassRoot;
   
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
    * Get the name of the module
    * 
    * @return the string
    */
   public String getName()
   {
      return deploymentUnit.getName(); 
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
      // TODO JBMICROCONT-182 - this should be already determined
      if (requirementDependencies == null || requirementDependencies.isEmpty())
         return null;

      List<DelegateLoader> result = new ArrayList<DelegateLoader>();
      for (RequirementDependencyItem item : requirementDependencies)
      {
         String name = (String) item.getIDependOn();
         Module module = domain.getModule(name);
         if (module == null)
            throw new IllegalStateException("Module not found with name: " + name);
         result.add(module.getDelegateLoader());
      }
      return result;
   }
   
   /**
    * Get the delegate loader
    * 
    * @return the delegate loader
    */
   public DelegateLoader getDelegateLoader()
   {
      // TODO JBMICROCONT-182 - this should be already determined
      ClassLoaderPolicy policy = deploymentUnit.getAttachment(ClassLoaderPolicy.class);
      if (policy == null)
         throw new IllegalStateException("No policy for " + deploymentUnit.getName());
      return new FilteredDelegateLoader(policy);
   }
   
   public String[] getPackageNames()
   {
      // TODO JBMICROCONT-182 - this should be first class to include "uses" processing
      List<Capability> capabilities = metadata.getCapabilities();
      if (capabilities == null)
         return new String[0];
      
      List<String> packageNames = new ArrayList<String>();
      for (Capability capability : capabilities)
      {
         if (capability instanceof PackageCapability)
            packageNames.add(((PackageCapability) capability).getName());
      }
      return packageNames.toArray(new String[packageNames.size()]);
   }
   
   /**
    * Create the dependencies for the module
    */
   public void createDependencies()
   {
      List<Requirement> requirements = metadata.getRequirements();
      if (requirements != null)
      {
         requirementDependencies = new ArrayList<RequirementDependencyItem>();
         for (Requirement requirement : requirements)
         {
            RequirementDependencyItem item = new RequirementDependencyItem(this, requirement);
            requirementDependencies.add(item);
            deploymentUnit.addIDependOn(item);
         }
      }
   }
   
   /**
    * Resolve the requirement
    * 
    * @param controller the controller
    * @param requirement the requirement
    * @return the resolved name or null if not resolved
    */
   protected Object resolve(Controller controller, Requirement requirement)
   {
      return domain.resolve(controller, this, requirement);
   }
   
   /**
    * Reset the module
    */
   public void reset()
   {
   }
   
   /**
    * Release the module
    */
   public void release()
   {
      domain.removeModule(this);
   }
   
   public URL getDynamicClassRoot()
   {
      if (dynamicClassRoot == null)
      {
         try
         {
            dynamicClassRoot = new URL("vfsmemory://" + new GUID());
         }
         catch (MalformedURLException e)
         {
            throw new RuntimeException(e);
         }
      }
      return dynamicClassRoot;
   }
}
