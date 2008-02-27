/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.osgi.plugins.facade.helpers;

import java.util.ArrayList;
import java.util.List;

import org.jboss.classloading.plugins.metadata.PackageCapability;
import org.jboss.classloading.spi.metadata.CapabilitiesMetaData;
import org.jboss.classloading.spi.metadata.Capability;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaData;
import org.jboss.util.Strings;
import org.osgi.framework.Constants;

/**
 * 
 * A ExportPackageHeaderBuilder - Builds the Export-Package BundleHeader from CLassLoadingMetaData
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public class ExportPackageHeaderBuilder implements BundleHeaderBuilder<ClassLoadingMetaData>
{
   /**
    * Create a new ExportPackageHeaderRetriever.
    * 
    */
   public ExportPackageHeaderBuilder()
   {
   }

   /**
    * Iterate through the ClassLoadingMetaData to check for a PackageCapabilities and create Export-Package BundleHeader.
    * 
    * @param classLoadingMetaData
    */
   public BundleHeader buildHeader(ClassLoadingMetaData metaData)
   {
      CapabilitiesMetaData capabilitiesMetaData = metaData.getCapabilities();
      List<Capability> capabilities = capabilitiesMetaData.getCapabilities();
      if (capabilities != null)
      {
         List<String> packageAndVersions = new ArrayList<String>();
         for (Capability capability : capabilities)
         {
            if (capability instanceof PackageCapability)
            {
               PackageCapability packageCapability = (PackageCapability) capability;
               packageAndVersions.add(new StringBuilder().append(packageCapability.getName()).append(";version=").append(packageCapability.getVersion()).toString());
            }
         }
         if (packageAndVersions.isEmpty() == false)
         {
            return new BundleHeader(Constants.EXPORT_PACKAGE, Strings.join(packageAndVersions.toArray(),","));
         }
      }
      return null;
   }
}
