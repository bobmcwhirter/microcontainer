/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, JBoss Inc., and individual contributors as indicated
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
package org.jboss.osgi.plugins.facade.helpers;

import java.util.ArrayList;
import java.util.List;

import org.jboss.classloading.plugins.metadata.PackageRequirement;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaData;
import org.jboss.classloading.spi.metadata.Requirement;
import org.jboss.classloading.spi.metadata.RequirementsMetaData;
import org.jboss.util.Strings;
import org.osgi.framework.Constants;

/**
 * 
 * A ImportPackageHeaderRetriever - Builds Import-Package BundleHeader from ClassLoadingMetaData
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public class ImportPackageHeaderBuilder implements BundleHeaderBuilder<ClassLoadingMetaData>
{
   /**
    * Create a new ImportPackageHeaderBuilder.
    * 
    */
   public ImportPackageHeaderBuilder()
   {
   }

   /**
    * Iterate through the ClassLoadingMetaData to check for a PackageRequirements and build Import-Package header.
    * 
    * @param classLoadingMetaData
    */
   public BundleHeader buildHeader(ClassLoadingMetaData metaData)
   {
      RequirementsMetaData requirementsMetaData = metaData.getRequirements();
      List<Requirement> requirements = requirementsMetaData.getRequirements();
      if (requirements != null)
      {
         List<String> packageAndVersions = new ArrayList<String>();
         for (Requirement requirement : requirements)
         {
            if (requirement instanceof PackageRequirement)
            {
               PackageRequirement packageRequirement = (PackageRequirement) requirement;
               packageAndVersions.add(new StringBuilder().append(packageRequirement.getName()).append(";version=").append(packageRequirement.getVersionRange()).toString());
            }
         }
         if (packageAndVersions.isEmpty() == false)
         {
            return new BundleHeader(Constants.IMPORT_PACKAGE, Strings.join(packageAndVersions.toArray(),","));
         }
      }
      return null;
   }
}
