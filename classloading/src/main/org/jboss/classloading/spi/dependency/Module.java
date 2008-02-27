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
package org.jboss.classloading.spi.dependency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.DelegateLoader;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.classloading.spi.helpers.NameAndVersionSupport;
import org.jboss.classloading.spi.metadata.Capability;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaDataFactory;
import org.jboss.classloading.spi.metadata.ExportAll;
import org.jboss.classloading.spi.metadata.ExportPackages;
import org.jboss.classloading.spi.metadata.Requirement;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;

/**
 * Module.
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class Module extends NameAndVersionSupport
{
   /** The context name */
   private String contextName;
   
   /** Our cached capabilities */
   private List<Capability> capabilities;

   /** The controller context */
   private ControllerContext context;

   /** The domain */
   private Domain domain;

   /** The requirements */
   private List<RequirementDependencyItem> requirementDependencies;
   
   /**
    * Create a new Module with the default version
    *
    * @param name the name
    * @throws IllegalArgumentException for a null parameter
    */
   public Module(String name)
   {
      this(name, name, null);
   }
   
   /**
    * Create a new Module with the given version
    *
    * @param name the name
    * @param version pass null for the default version
    * @throws IllegalArgumentException for a null parameter
    */
   public Module(String name, Object version)
   {
      this(name, name, version);
   }
   
   /**
    * Create a new Module with the given version
    *
    * @param name the name
    * @param contextName the real name of the module in the controller
    * @param version pass null for the default version
    * @throws IllegalArgumentException for a null parameter
    */
   public Module(String name, String contextName, Object version)
   {
      super(name, version);
      if (contextName == null)
         contextName = name + version.toString();
      this.contextName = contextName;
   }

   /**
    * Get the context name
    * 
    * @return the context name
    */
   public String getContextName()
   {
      return contextName;
   }
   
   /**
    * Get the domain.
    * 
    * @return the domain.
    */
   Domain getDomain()
   {
      return domain;
   }

   void setDomain(Domain domain)
   {
      this.domain = domain;
   }

   Domain checkDomain()
   {
      Domain result = domain;
      if (result == null)
         throw new IllegalStateException("Domain is not set for " + this);
      return result;
   }
   
   /**
    * Get the domain name.
    * 
    * @return the domain name.
    */
   public String getDomainName()
   {
      return null;
   }

   /**
    * Get the determined domain name.
    * 
    * @return the determined domain.
    */
   public String getDeterminedDomainName()
   {
      String domainName = getDomainName();
      if (domainName == null)
         domainName = ClassLoaderSystem.DEFAULT_DOMAIN_NAME;
      return domainName;
   }

   /**
    * Get the parent domain name.
    * 
    * @return the parent domain name.
    */
   public String getParentDomainName()
   {
      return null;
   }

   /**
    * Get the determined parentDomain name.
    * 
    * @return the parentDomain.
    */
   public String getDeterminedParentDomainName()
   {
      String parentDomain = getParentDomainName();
      if (parentDomain == null)
      {
         if (ClassLoaderSystem.DEFAULT_DOMAIN_NAME.equals(getDeterminedDomainName()) == false)
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
      return null;
   }

   /**
    * Get a filter for the included packages
    * 
    * @return the excluded packages
    */
   public ClassFilter getIncluded()
   {
      return null;
   }

   /**
    * Get a filter for the excluded packages
    * 
    * @return the excluded packages
    */
   public ClassFilter getExcluded()
   {
      return null;
   }

   /**
    * Get a filter for the excluded export packages
    * 
    * @return the excluded packages
    */
   public ClassFilter getExcludedExport()
   {
      return null;
   }

   /**
    * Get the import all for the module
    * 
    * @return the import all
    */
   public boolean isImportAll()
   {
      return false;
   }

   /**
    * Get delegate policy
    * 
    * @return the delegation policy
    */
   public boolean isJ2seClassLoadingCompliance()
   {
      return true;
   }

   public ParentPolicy getDeterminedParentPolicy()
   {
      if (isJ2seClassLoadingCompliance())
         return ParentPolicy.BEFORE;
      else
         return ParentPolicy.AFTER_BUT_JAVA_BEFORE;
   }
   
   /**
    * Whether to cache<p>
    * 
    * @return true to cache
    */
   protected boolean isCacheable()
   {
      return true;
   }

   /**
    * Whether to cache misses<p>
    * 
    * @return true to cache misses
    */
   protected boolean isBlackListable()
   {
      return true;
   }

   /**
    * Get the delegate loaders for this module
    * 
    * @return the delegates
    */
   public List<? extends DelegateLoader> getDelegates()
   {
      if (requirementDependencies == null || requirementDependencies.isEmpty())
         return null;

      List<DelegateLoader> result = new CopyOnWriteArrayList<DelegateLoader>();
      List<DelegateLoader> dynamic = new CopyOnWriteArrayList<DelegateLoader>();
      Set<Module> visited = new HashSet<Module>();
      addDelegates(this, result, dynamic, visited, false);
      
      // Make sure the dynamic delegates are last
      result.addAll(dynamic);
      
      return result;
   }
   
   /**
    * Get the dependency items
    * 
    * @return the depenency items
    */
   protected List<RequirementDependencyItem> getRequirementDependencyItems()
   {
      return requirementDependencies;
   }

   /**
    * Add delegates
    * 
    * @param module the module to add delegates from
    * @param delegates the current list of delegates
    * @param dynamic the dynamic delegates
    * @param visited the visited modules
    * @param reExport whether to only add re-exports
    */
   protected void addDelegates(Module module, List<DelegateLoader> delegates, List<DelegateLoader> dynamic, Set<Module> visited, boolean reExport)
   {
      // Check whether we already did this module
      if (visited.contains(module))
         return;
      visited.add(module);
      
      List<RequirementDependencyItem> dependencies = module.getRequirementDependencyItems();
      if (dependencies == null || dependencies.isEmpty())
         return;
      
      for (RequirementDependencyItem item : dependencies)
      {
         Requirement requirement = item.getRequirement();
         
         // If we are looking at everything or this is a re-export
         if (reExport == false || requirement.isReExport())
         {
            // Sanity checks
            if (item.isResolved() == false)
               throw new IllegalStateException("Item not resolved: " + item);
            
            // Dynamic requirement, create it lazily
            if (requirement.isDynamic())
            {
               DelegateLoader delegate = createLazyDelegateLoader(checkDomain(), item);
               dynamic.add(delegate);
               continue;
            }

            String name = (String) item.getIDependOn();
            if (name == null)
            {
               // Optional requirement, just ignore
               if (requirement.isOptional())
                  continue;
               // Something has gone wrong
               throw new IllegalStateException("No iDependOn for item: " + item);
            }
            Module iDependOnModule = checkDomain().getModule(name);
            if (iDependOnModule == null)
               throw new IllegalStateException("Module not found with name: " + name);

            // Determine the delegate loader for the module
            Module other = item.getModule();
            DelegateLoader delegate = iDependOnModule.getDelegateLoader(other, requirement);

            // Check for re-export by the module
            if (requirement.wantReExports())
               addDelegates(iDependOnModule, delegates, dynamic, visited, true);
            
            // We want a module's re-exports (i.e. part of its imports) before the module itself
            if (delegate != null)
               delegates.add(delegate);
         }
      }
   }

   /**
    * Create a lazy delegate loader
    * 
    * @param domain the domain
    * @param item the dependency item
    * @return the delegate loader
    */
   public abstract DelegateLoader createLazyDelegateLoader(Domain domain, RequirementDependencyItem item);

   /**
    * Get the delegate loader
    * 
    * @param requiringModule the requiring module
    * @param requirement the requirement
    * @return the delegate loader
    */
   public abstract DelegateLoader getDelegateLoader(Module requiringModule, Requirement requirement);

   /**
    * Get the capabilities.
    * 
    * @return the capabilities.
    */
   public List<Capability> getCapabilities()
   {
      // Have we already worked this out?
      if (capabilities != null)
         return capabilities;
      
      // Are there any configured ones?
      List<Capability> capabilities = determineCapabilities();
      
      // Use the defaults
      if (capabilities == null)
         capabilities = defaultCapabilities();
      
      // Cache it
      this.capabilities = capabilities;
      return capabilities;
   }

   /**
    * Determine the capabilities
    * 
    * @return the capabilities
    */
   protected List<Capability> determineCapabilities()
   {
      return null;
   }

   /**
    * Determine the default capabilities.<p>
    * 
    * By default it is just the module capability
    * 
    * @return the capabilities
    */
   protected List<Capability> defaultCapabilities()
   {
      List<Capability> capabilities = new CopyOnWriteArrayList<Capability>();
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      Capability capability = factory.createModule(getName(), getVersion());
      capabilities.add(capability);
      return capabilities;
   }

   /**
    * Get the package names
    * 
    * @return the package names
    */
   public String[] getPackageNames()
   {
      List<String> packageNames = new ArrayList<String>();

      List<Capability> capabilities = getCapabilities();
      if (capabilities != null && capabilities.isEmpty() == false)
      {
         for (Capability capability : capabilities)
         {
            if (capability instanceof ExportPackages)
            {
               ExportPackages exported = (ExportPackages) capability;
               Set<String> exportPackages = exported.getPackageNames(this);
               if (exportPackages != null)
                  packageNames.addAll(exportPackages);
            }
         }
      }

      List<Requirement> requirements = getRequirements();
      if (requirements != null && requirements.isEmpty() == false)
      {
         for (Requirement requirement : getRequirements())
         {
            if (requirement instanceof ExportPackages)
            {
               ExportPackages exported = (ExportPackages) requirement;
               Set<String> exportPackages = exported.getPackageNames(this);
               if (exportPackages != null)
                  packageNames.addAll(exportPackages);
            }
         }
      }

      return packageNames.toArray(new String[packageNames.size()]);
   }
   
   /**
    * Get the state for the classloader
    * 
    * @return the state
    */
   public ControllerState getClassLoaderState()
   {
      return ControllerState.INSTALLED;
   }

   /**
    * Get the requirements.
    * 
    * @return the requirements.
    */
   public List<Requirement> getRequirements()
   {
      return Collections.emptyList();
   }

   /**
    * Create the dependencies for the module
    */
   protected void createDependencies()
   {
      ControllerState classLoaderState = getClassLoaderState();
      
      List<Requirement> requirements = getRequirements();
      if (requirements != null)
      {
         requirementDependencies = new ArrayList<RequirementDependencyItem>();
         for (Requirement requirement : requirements)
         {
            RequirementDependencyItem item = new RequirementDependencyItem(this, requirement, classLoaderState);
            addIDependOn(item);
            requirementDependencies.add(item);
         }
      }
   }

   /**
    * Remove dependencies
    */
   protected void removeDependencies()
   {
      if (requirementDependencies != null && requirementDependencies.isEmpty() == false)
      {
         for (RequirementDependencyItem item : requirementDependencies)
            removeIDependOn(item);
      }
      requirementDependencies = null;
   }

   /**
    * Get the controller context.
    * 
    * @return the controller context.
    */
   protected ControllerContext getControllerContext()
   {
      return context;
   }

   /**
    * Set the controller context
    * 
    * @param context the context
    */
   protected void setControllerContext(ControllerContext context)
   {
      this.context = context;
   }
   
   /**
    * Add a dependency
    * 
    * @param item the dependency item
    */
   protected void addIDependOn(RequirementDependencyItem item)
   {
      if (context == null)
         throw new IllegalStateException("No controller context");
      context.getDependencyInfo().addIDependOn(item);
   }
   
   /**
    * Remove a dependency
    * 
    * @param item the dependency item
    */
   protected void removeIDependOn(RequirementDependencyItem item)
   {
      if (context == null)
         throw new IllegalStateException("No controller context");
      context.getDependencyInfo().removeIDependOn(item);
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
      return checkDomain().resolve(controller, this, requirement);
   }
   
   /**
    * Release the module
    */
   public void release()
   {
      checkDomain().removeModule(this);
      reset();
   }
   
   /**
    * Reset the module
    */
   public void reset()
   {
      this.capabilities = null;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;
      if (obj == null || obj instanceof Module == false)
         return false;
      return super.equals(obj);
   }
}
