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

import org.jboss.classloading.plugins.metadata.PackageCapability;
import org.jboss.classloading.spi.metadata.CapabilitiesMetaData;
import org.jboss.classloading.spi.metadata.Capability;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaData;
import org.jboss.util.Strings;
import org.osgi.framework.Constants;

/**
 * A ExportPackageHeaderBuilder.
 * Builds the Export-Package BundleHeader from CLassLoadingMetaData
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public class ExportPackageHeaderBuilder implements BundleHeaderBuilder<ClassLoadingMetaData>
{
   /**
    * Create a new ExportPackageHeaderRetriever.
    */
   public ExportPackageHeaderBuilder()
   {
   }

   /**
    * Iterate through the ClassLoadingMetaData to check for a PackageCapabilities and create Export-Package BundleHeader.
    * 
    * @param metaData the classloading meta data
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
