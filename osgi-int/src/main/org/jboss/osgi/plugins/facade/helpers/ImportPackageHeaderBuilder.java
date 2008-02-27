/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
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
