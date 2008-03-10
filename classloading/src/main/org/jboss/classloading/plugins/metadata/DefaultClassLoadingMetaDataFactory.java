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
package org.jboss.classloading.plugins.metadata;

import org.jboss.classloading.spi.metadata.Capability;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaDataFactory;
import org.jboss.classloading.spi.metadata.Requirement;
import org.jboss.classloading.spi.version.VersionRange;

/**
 * DefaultClassLoadingMetaDataFactory.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class DefaultClassLoadingMetaDataFactory extends ClassLoadingMetaDataFactory
{
   public Capability createModule(String name, Object version)
   {
      return new ModuleCapability(name, version);
   }

   public Requirement createRequireModule(String name, VersionRange versionRange, boolean optional, boolean reExport, boolean dynamic)
   {
      ModuleRequirement result = new ModuleRequirement(name, versionRange);
      result.setOptional(optional);
      result.setReExport(reExport);
      result.setDynamic(dynamic);
      return result;
   }

   public Capability createPackage(String name, Object version)
   {
      return new PackageCapability(name, version);
   }

   public Requirement createRequirePackage(String name, VersionRange versionRange, boolean optional, boolean reExport, boolean dynamic)
   {
      PackageRequirement result = new PackageRequirement(name, versionRange);
      result.setOptional(optional);
      result.setReExport(reExport);
      result.setDynamic(dynamic);
      return result;
   }

   public Requirement createUsesPackage(String name, VersionRange versionRange, boolean reExport)
   {
      UsesPackageRequirement result = new UsesPackageRequirement(name, versionRange);
      result.setReExport(reExport);
      return result;
   }
}
