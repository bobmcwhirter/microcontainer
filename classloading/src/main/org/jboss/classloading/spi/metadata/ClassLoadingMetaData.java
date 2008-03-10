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
package org.jboss.classloading.spi.metadata;

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.jboss.classloader.plugins.filter.CombiningClassFilter;
import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.classloader.spi.filter.PackageClassFilter;
import org.jboss.classloading.spi.helpers.NameAndVersionSupport;
import org.jboss.managed.api.annotation.ManagementObject;
import org.jboss.managed.api.annotation.ManagementProperties;
import org.jboss.managed.api.annotation.ManagementProperty;

/**
 * ClassLoadingMetaData.
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
@ManagementObject(properties=ManagementProperties.EXPLICIT, name="org.jboss.classloading.spi.metadata.ClassLoadingMetaData")
public class ClassLoadingMetaData extends NameAndVersionSupport
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -2782951093046585620L;
   
   /** The classloading domain */
   private String domain;

   /** The parent domain */
   private String parentDomain;
   
   /** Whether to enforce j2se classloading compliance */
   private boolean j2seClassLoadingCompliance = true;
   
   /** Whether we are cacheable */
   private boolean cacheable = true;
   
   /** Whether we are blacklistable */
   private boolean blackListable = true;
   
   /** Whether to export all */
   private ExportAll exportAll;
   
   /** Whether to import all */
   private boolean importAll;

   /** The included packages */
   private String includedPackages;

   /** The excluded packages */
   private String excludedPackages;

   /** The excluded for export */
   private String excludedExportPackages;

   /** The included packages */
   private ClassFilter included;

   /** The excluded packages */
   private ClassFilter excluded;

   /** The excluded for export */
   private ClassFilter excludedExport;
   
   /** The requirements */
   private RequirementsMetaData requirements = new RequirementsMetaData();
   
   /** The capabilities */
   private CapabilitiesMetaData capabilities = new CapabilitiesMetaData();
   
   /**
    * Get the domain.
    * 
    * @return the domain.
    */
   public String getDomain()
   {
      return domain;
   }

   /**
    * Set the domain.
    * 
    * @param domain the domain.
    */
   @ManagementProperty
   @XmlAttribute
   public void setDomain(String domain)
   {
      this.domain = domain;
   }

   /**
    * Get the parentDomain.
    * 
    * @return the parentDomain.
    */
   public String getParentDomain()
   {
      return parentDomain;
   }

   /**
    * Set the parentDomain.
    * 
    * @param parentDomain the parentDomain.
    */
   @ManagementProperty
   @XmlAttribute
   public void setParentDomain(String parentDomain)
   {
      this.parentDomain = parentDomain;
   }

   /**
    * Get the exportAll.
    * 
    * @return the exportAll.
    */
   public ExportAll getExportAll()
   {
      return exportAll;
   }

   /**
    * Set the exportAll.
    * 
    * @param exportAll the exportAll.
    */
   @ManagementProperty
   @XmlAttribute(name="export-all")
   public void setExportAll(ExportAll exportAll)
   {
      this.exportAll = exportAll;
   }

   /**
    * Get the includedPackages.
    * 
    * @return the includedPackages.
    */
   public String getIncludedPackages()
   {
      return includedPackages;
   }

   /**
    * Set the includedPackages.
    * 
    * @param includedPackages the includedPackages.
    */
   @ManagementProperty(name="included")
   @XmlAttribute(name="included")
   public void setIncludedPackages(String includedPackages)
   {
      this.includedPackages = includedPackages;
   }

   /**
    * Get a filter for the included packages
    * 
    * @return the included packages
    */
   public ClassFilter getIncluded()
   {
      ClassFilter packageFilter = null;
      if (includedPackages != null)
         packageFilter = PackageClassFilter.createPackageClassFilterFromString(includedPackages);
      
      if (packageFilter == null)
         return included;
      if (included == null)
         return packageFilter;
      return CombiningClassFilter.create(true, packageFilter, included);
   }

   /**
    * Set the included.
    * 
    * @param included the included.
    */
   @XmlTransient
   public void setIncluded(ClassFilter included)
   {
      this.included = included;
   }

   /**
    * Get the excludedPackages.
    * 
    * @return the excludedPackages.
    */
   public String getExcludedPackages()
   {
      return excludedPackages;
   }

   /**
    * Set the excludedPackages.
    * 
    * @param excludedPackages the excludedPackages.
    */
   @ManagementProperty(name="excluded")
   @XmlAttribute(name="excluded")
   public void setExcludedPackages(String excludedPackages)
   {
      this.excludedPackages = excludedPackages;
   }

   /**
    * Get a filter for the excluded packages
    * 
    * @return the excluded packages
    */
   public ClassFilter getExcluded()
   {
      ClassFilter packageFilter = null;
      if (excludedPackages != null)
         packageFilter = PackageClassFilter.createPackageClassFilterFromString(excludedPackages);
      
      if (packageFilter == null)
         return excluded;
      if (excluded == null)
         return packageFilter;
      return CombiningClassFilter.create(true, packageFilter, excluded);
   }

   /**
    * Set the excluded.
    * 
    * @param excluded the excluded.
    */
   @XmlTransient
   public void setExcluded(ClassFilter excluded)
   {
      this.excluded = excluded;
   }
   
   /**
    * Get the excludedExportPackages.
    * 
    * @return the excludedExportPackages.
    */
   public String getExcludedExportPackages()
   {
      return excludedExportPackages;
   }

   /**
    * Set the excludedExportPackages.
    * 
    * @param excludedExportPackages the excludedExportPackages.
    */
   @ManagementProperty(name="excludedExport")
   @XmlAttribute(name="excludedExport")
   public void setExcludedExportPackages(String excludedExportPackages)
   {
      this.excludedExportPackages = excludedExportPackages;
   }

   /**
    * Get a filter for the excluded export packages
    * 
    * @return the excluded export packages
    */
   public ClassFilter getExcludedExport()
   {
      ClassFilter packageFilter = null;
      if (excludedExportPackages != null)
         packageFilter = PackageClassFilter.createPackageClassFilterFromString(excludedExportPackages);
      
      if (packageFilter == null)
         return excludedExport;
      if (excludedExport == null)
         return packageFilter;
      return CombiningClassFilter.create(true, packageFilter, excludedExport);
   }

   /**
    * Set the excludedExport.
    * 
    * @param excludedExport the excludedExport.
    */
   @XmlTransient
   public void setExcludedExport(ClassFilter excludedExport)
   {
      this.excludedExport = excludedExport;
   }

   /**
    * Whether to import all
    * 
    * @return true when there are no requirements
    */
   public boolean isImportAll()
   {
      return importAll;
   }
   
   /**
    * Set the importAll.
    * 
    * @param importAll the importAll.
    */
   @ManagementProperty
   @XmlAttribute(name="import-all")
   public void setImportAll(boolean importAll)
   {
      this.importAll = importAll;
   }

   /**
    * Get the j2seClassLoadingCompliance.
    * 
    * @return the j2seClassLoadingCompliance.
    */
   public boolean isJ2seClassLoadingCompliance()
   {
      return j2seClassLoadingCompliance;
   }

   /**
    * Set the j2seClassLoadingCompliance.
    * 
    * @param classLoadingCompliance the j2seClassLoadingCompliance.
    */
   @ManagementProperty(name="parentFirst")
   @XmlAttribute(name="parent-first")
   public void setJ2seClassLoadingCompliance(boolean classLoadingCompliance)
   {
      j2seClassLoadingCompliance = classLoadingCompliance;
   }

   /**
    * Get the cacheable.
    * 
    * @return the cacheable.
    */
   public boolean isCacheable()
   {
      return cacheable;
   }

   /**
    * Set the cacheable.
    * 
    * @param cacheable the cacheable.
    */
   @ManagementProperty(name="cache")
   @XmlAttribute(name="cache")
   public void setCacheable(boolean cacheable)
   {
      this.cacheable = cacheable;
   }

   /**
    * Get the blackListable.
    * 
    * @return the blackListable.
    */
   public boolean isBlackListable()
   {
      return blackListable;
   }

   /**
    * Set the blackListable.
    * 
    * @param blackListable the blackListable.
    */
   @ManagementProperty(name="blackList")
   @XmlAttribute(name="blackList")
   public void setBlackListable(boolean blackListable)
   {
      this.blackListable = blackListable;
   }

   /**
    * Get the capabilities.
    * 
    * @return the capabilities.
    */
   public CapabilitiesMetaData getCapabilities()
   {
      return capabilities;
   }

   /**
    * Set the capabilities.
    * 
    * @param capabilities the capabilities.
    * @throws IllegalArgumentException for null capabilities
    */
   @ManagementProperty
   public void setCapabilities(CapabilitiesMetaData capabilities)
   {
      if (capabilities == null)
         throw new IllegalArgumentException("Null capabilities");
      this.capabilities = capabilities;
   }

   /**
    * Set the capabilities.
    * 
    * @param capabilities the capabilities.
    */
   public void setCapabilities(List<Capability> capabilities)
   {
      if (this.capabilities == null)
         this.capabilities = new CapabilitiesMetaData();
      this.capabilities.setCapabilities(capabilities);
   }

   /**
    * Get the requirements.
    * 
    * @return the requirements.
    */
   public RequirementsMetaData getRequirements()
   {
      return requirements;
   }

   /**
    * Set the requirements.
    * 
    * @param requirements the requirements.
    * @throws IllegalArgumentException for null requirements
    */
   @ManagementProperty
   public void setRequirements(RequirementsMetaData requirements)
   {
      if (requirements == null)
         throw new IllegalArgumentException("Null requirements");
      this.requirements = requirements;
   }

   /**
    * Set the requirements.
    * 
    * @param requirements the requirements.
    */
   public void setRequirements(List<Requirement> requirements)
   {
      if (this.requirements == null)
         this.requirements = new RequirementsMetaData();
      this.requirements.setRequirements(requirements);
   }

   @Override 
   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(getClass().getSimpleName());
      builder.append("@");
      builder.append(Integer.toHexString(System.identityHashCode(this)));
      builder.append("{");
      toString(builder);
      builder.append("}");
      return builder.toString();
   }
   
   /**
    * For subclasses to override the toString contents
    * 
    * @param builder the builder
    */
   protected void toString(StringBuilder builder)
   {
      builder.append("name=").append(getName());
      builder.append(" version=").append(getVersion());
      String domain = getDomain();
      if (domain != null)
         builder.append(" domain=").append(domain);
      String parentDomain = getParentDomain();
      if (parentDomain != null)
         builder.append(" parentDomain=").append(parentDomain);
      ExportAll exportAll = getExportAll();
      if (exportAll != null)
         builder.append(" ").append(exportAll);
      if (isImportAll())
         builder.append(" IMPORT-ALL");
      builder.append(" parent-first=").append(isJ2seClassLoadingCompliance());
      if (isCacheable() == false)
         builder.append(" NO-CACHE");
      if (isBlackListable() == false)
         builder.append(" NO-BLACK-LIST");
      List<Capability> capabilities = getCapabilities().getCapabilities();
      if (capabilities != null)
         builder.append(" capabilities=").append(capabilities);
      List<Requirement> requirements = getRequirements().getRequirements();
      if (requirements != null)
         builder.append(" requirements=").append(requirements);
   }
   
   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;
      if (obj == null || obj instanceof ClassLoadingMetaData == false)
         return false;
      if (super.equals(obj) == false)
         return false;
      ClassLoadingMetaData other = (ClassLoadingMetaData) obj;
      if (equals(this.getDomain(), other.getDomain()) == false)
         return false;
      if (equals(this.getParentDomain(), other.getParentDomain()) == false)
         return false;
      if (equals(this.getExportAll(), other.getExportAll()) == false)
         return false;
      if (this.isImportAll() != other.isImportAll())
         return false;
      if (this.isJ2seClassLoadingCompliance() != other.isJ2seClassLoadingCompliance())
         return false;
      if (this.isCacheable() != other.isCacheable())
         return false;
      if (this.isBlackListable() != other.isBlackListable())
         return false;
      if (equals(this.getCapabilities().getCapabilities(), other.getCapabilities().getCapabilities()) == false)
         return false;
      if (equals(this.getRequirements().getRequirements(), other.getRequirements().getRequirements()) == false)
         return false;
      return true;
   }
   
   private static boolean equals(Object one, Object two)
   {
      if (one == null && two == null)
         return true;
      if (one == null && two != null)
         return false;
      return one.equals(two);
   }

   @Override
   public ClassLoadingMetaData clone()
   {
      ClassLoadingMetaData clone = (ClassLoadingMetaData) super.clone();
      requirements = clone.requirements.clone();
      capabilities = clone.capabilities.clone();
      return clone;
   }
}
