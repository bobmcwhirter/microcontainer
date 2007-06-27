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
package org.jboss.deployers.structure.spi.classloading;

import java.io.Serializable;
import java.util.Set;

/**
 * ClassLoaderMetaData.
 * 
 * TODO needs greatly expanding
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ClassLoaderMetaData implements Serializable
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -2782951093046585620L;

   /** The name */
   private String name;
   
   /** The version */
   private Version version;
   
   /** The classloading domain */
   private String domain;

   /** The parent domain */
   private String parentDomain;
   
   /** Whether to enforce j2se classloading compliance */
   private boolean j2seClassLoadingCompliance;
   
   /** Whether to export all */
   private ExportAll exportAll;
   
   /** The requirements */
   private Set<Requirement> requirements;
   
   /** The capabilities */
   private Set<Capability> capabilities;

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
    * Set the name.
    * 
    * @param name the name.
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Get the version.
    * 
    * @return the version.
    */
   public Version getVersion()
   {
      return version;
   }

   /**
    * Set the version.
    * 
    * @param version the version.
    */
   public void setVersion(Version version)
   {
      this.version = version;
   }

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
   public void setExportAll(ExportAll exportAll)
   {
      this.exportAll = exportAll;
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
   public void setJ2seClassLoadingCompliance(boolean classLoadingCompliance)
   {
      j2seClassLoadingCompliance = classLoadingCompliance;
   }

   /**
    * Get the capabilities.
    * 
    * @return the capabilities.
    */
   public Set<Capability> getCapabilities()
   {
      return capabilities;
   }

   /**
    * Set the capabilities.
    * 
    * @param capabilities the capabilities.
    */
   public void setCapabilities(Set<Capability> capabilities)
   {
      this.capabilities = capabilities;
   }

   /**
    * Whether to import all
    * 
    * @return true when there are no requirements
    */
   public boolean isImportAll()
   {
      return requirements == null || requirements.isEmpty();
   }
   
   /**
    * Get the requirements.
    * 
    * @return the requirements.
    */
   public Set<Requirement> getRequirements()
   {
      return requirements;
   }

   /**
    * Set the requirements.
    * 
    * @param requirements the requirements.
    */
   public void setRequirements(Set<Requirement> requirements)
   {
      this.requirements = requirements;
   }
}
