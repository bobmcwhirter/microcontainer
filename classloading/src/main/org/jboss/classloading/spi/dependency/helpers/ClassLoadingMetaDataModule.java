/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.classloading.spi.dependency.helpers;

import java.util.List;

import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.classloading.spi.dependency.Module;
import org.jboss.classloading.spi.metadata.Capability;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaData;
import org.jboss.classloading.spi.metadata.ExportAll;
import org.jboss.classloading.spi.metadata.Requirement;

/**
 * ClassLoadingMetaDataModule.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class ClassLoadingMetaDataModule extends Module
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -1834649865956381804L;
   
   /** The classloading metadata */
   private ClassLoadingMetaData classLoadingMetaData;

   /**
    * Determine the name from the classloading metadta
    * 
    * @param classLoadingMetaData the classloading metadata
    * @return the name
    */
   private static String determineName(ClassLoadingMetaData classLoadingMetaData)
   {
      if (classLoadingMetaData == null)
         throw new IllegalArgumentException("Null classLoading metadata");
      return classLoadingMetaData.getName();
   }

   /**
    * Determine the version from the classloading metadta
    * 
    * @param classLoadingMetaData the classloading metadata
    * @return the version
    */
   private static Object determineVersion(ClassLoadingMetaData classLoadingMetaData)
   {
      if (classLoadingMetaData == null)
         throw new IllegalArgumentException("Null classLoading metadata");
      return classLoadingMetaData.getVersion();
   }
   
   /**
    * Create a new ClassLoadingMetaDataModule.
    * 
    * @param classLoadingMetaData the classLoading metadata
    * @param contextName the context name 
    * @throws IllegalArgumentException for null classloading metadata
    */
   public ClassLoadingMetaDataModule(ClassLoadingMetaData classLoadingMetaData, String contextName)
   {
      super(determineName(classLoadingMetaData), contextName, determineVersion(classLoadingMetaData));
      this.classLoadingMetaData = classLoadingMetaData;
   }

   /**
    * Get the classloading metadata
    *  
    * @return the metadata
    */
   protected ClassLoadingMetaData getClassLoadingMetaData()
   {
      return classLoadingMetaData;
   }
   
   @Override
   public String getDomainName()
   {
      return classLoadingMetaData.getDomain();
   }

   @Override
   public String getParentDomainName()
   {
      return classLoadingMetaData.getParentDomain();
   }

   @Override
   public ExportAll getExportAll()
   {
      return classLoadingMetaData.getExportAll();
   }

   @Override
   public ClassFilter getIncluded()
   {
      return classLoadingMetaData.getIncluded();
   }

   @Override
   public ClassFilter getExcluded()
   {
      return classLoadingMetaData.getExcluded();
   }

   @Override
   public ClassFilter getExcludedExport()
   {
      return classLoadingMetaData.getExcludedExport();
   }

   @Override
   public boolean isImportAll()
   {
      return classLoadingMetaData.isImportAll();
   }

   @Override
   public boolean isJ2seClassLoadingCompliance()
   {
      return classLoadingMetaData.isJ2seClassLoadingCompliance();
   }

   @Override
   public boolean isCacheable()
   {
      return classLoadingMetaData.isCacheable();
   }

   @Override
   public boolean isBlackListable()
   {
      return classLoadingMetaData.isBlackListable();
   }

   @Override
   protected List<Capability> determineCapabilities()
   {
      return classLoadingMetaData.getCapabilities().getCapabilities();
   }

   @Override
   public List<Requirement> getRequirements()
   {
      return classLoadingMetaData.getRequirements().getRequirements();
   }
}
