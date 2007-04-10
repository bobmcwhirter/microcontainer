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
package org.jboss.osgi.plugins.metadata;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Manifest;

import org.jboss.deployers.plugins.metadata.AbstractManifestMetaData;
import static org.jboss.osgi.plugins.metadata.ValueCreatorUtil.*;
import org.jboss.osgi.spi.metadata.OSGiMetaData;
import org.jboss.osgi.spi.metadata.PackageAttribute;
import org.jboss.osgi.spi.metadata.ParameterizedAttribute;
import static org.osgi.framework.Constants.*;
import org.osgi.framework.Version;

/**
 * Abstract OSGi meta data.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AbstractOSGiMetaData extends AbstractManifestMetaData implements OSGiMetaData
{
   private static final long serialVersionUID = 1L;

   protected Map<String, Object> cachedAttributes = new HashMap<String, Object>();

   public AbstractOSGiMetaData()
   {
   }

   public AbstractOSGiMetaData(Manifest manifest)
   {
      super(manifest);
   }

   public String getBundleActivator()
   {
      return get(BUNDLE_ACTIVATOR, STRING_VC);
   }

   public List<String> getBundleClassPath()
   {
      return get(BUNDLE_CLASSPATH, STRING_LIST_VC, Arrays.asList("."));
   }

   public String getBundleDescription()
   {
      return get(BUNDLE_DESCRIPTION, STRING_VC);
   }

   public String getBundleLocalization()
   {
      return get(BUNDLE_LOCALIZATION, STRING_VC, BUNDLE_LOCALIZATION_DEFAULT_BASENAME);
   }

   public int getBundleManifestVersion()
   {
      return get(BUNDLE_VERSION, INTEGER_VC, 1);
   }

   public String getBundleName()
   {
      return get(BUNDLE_NAME, STRING_VC);
   }

   public List<ParameterizedAttribute> getBundleNativeCode()
   {
      return get(BUNDLE_NATIVECODE, PARAM_ATTRIB_LIST_VC);
   }

   public List<String> getRequiredExecutionEnvironment()
   {
      return get(BUNDLE_REQUIREDEXECUTIONENVIRONMENT, STRING_LIST_VC);
   }

   public String getBundleSymbolicName()
   {
      return get(BUNDLE_SYMBOLICNAME, STRING_VC);
   }

   public URL getBundleUpdateLocation()
   {
      return get(BUNDLE_UPDATELOCATION, URL_VC);
   }

   public Version getBundleVersion()
   {
      return get(BUNDLE_VERSION, VERSION_VC , new Version("0.0.0"));
   }

   public List<PackageAttribute> getDynamicImports()
   {
      return get(DYNAMICIMPORT_PACKAGE, PACKAGE_LIST_VC);
   }

   public List<PackageAttribute> getExportPackages()
   {
      return get(EXPORT_PACKAGE, PACKAGE_LIST_VC);
   }

   public ParameterizedAttribute getFragmentHost()
   {
      return get(FRAGMENT_HOST, PARAM_ATTRIB_VC);
   }

   public List<PackageAttribute> getImportPackages()
   {
      return get(IMPORT_PACKAGE, PACKAGE_LIST_VC);
   }

   public List<ParameterizedAttribute> getRequireBundles()
   {
      return get(REQUIRE_BUNDLE, PARAM_ATTRIB_LIST_VC);
   }

   @SuppressWarnings("unchecked")
   protected <T> T get(String key, ValueCreator<T> creator)
   {
      return get(key, creator, null);
   }

   @SuppressWarnings("unchecked")
   protected <T> T get(String key, ValueCreator<T> creator, T defaultValue)
   {
      T value = (T)cachedAttributes.get(key);
      if (value == null)
      {
         String attribute = getMainAttribute(key);
         if (attribute != null)
         {
            value = creator.createValue(attribute);
            cachedAttributes.put(key, value);
         }
         else if (defaultValue != null)
         {
            value = defaultValue;
            cachedAttributes.put(key, value);
         }
      }
      return value;
   }

}
