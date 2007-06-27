/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.osgi.spi.metadata;

import java.net.URL;
import java.util.List;

import org.jboss.deployers.vfs.spi.deployer.ManifestMetaData;
import org.osgi.framework.Version;

/**
 * OSGi specific manifest meta data.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public interface OSGiMetaData extends ManifestMetaData
{
   /**
    * Get bundle activator class name.
    *
    * @return bundle activator classname or null if no such attribute
    */
   String getBundleActivator();

   /**
    * Get the bundle classpath
    *
    * @return list of JAR file path names or directories inside bundle
    */
   List<String> getBundleClassPath();

   /**
    * Get the description
    *
    * @return a description
    */
   String getBundleDescription();

   /**
    * Get the localization's location
    *
    * @return location in the bundle for localization files
    */
   String getBundleLocalization();

   /**
    * Get the bundle manifest version
    *
    * @return bundle's specification number
    */
   int getBundleManifestVersion();

   /**
    * Get the name
    *
    * @return readable name
    */
   String getBundleName();

   /**
    * Get native code libs
    * @return native libs contained in the bundle
    */
   List<ParameterizedAttribute> getBundleNativeCode();

   /**
    * Get required exectuion envs
    *
    * @return list of execution envs that must be present on the Service Platform
    */
   List<String> getRequiredExecutionEnvironment();

   /**
    * Get bundle symbolic name.
    *
    * @return bundle's symbolic name
    */
   String getBundleSymbolicName();

   /**
    * Get the update url.
    *
    * @return URL of an update bundle location
    */
   URL getBundleUpdateLocation();

   /**
    * Get bundle's version.
    *
    * @return version of this bundle
    */
   Version getBundleVersion();

   /**
    * Get dynamic imports.
    *
    * @return package names that should be dynamically imported when needed
    */
   List<PackageAttribute> getDynamicImports();

   /**
    * Get the export packages.
    *
    * @return exported packages
    */
   List<PackageAttribute> getExportPackages();

   /**
    * Get the fragment host.
    *
    * @return host bundle for this fragment
    */
   ParameterizedAttribute getFragmentHost();

   /**
    * Get the import packages.
    *
    * @return imported packages.
    */
   List<PackageAttribute> getImportPackages();

   /**
    * Get the required exports
    *
    * @return required exports from anoter bundle
    */
   List<ParameterizedAttribute> getRequireBundles();
}
