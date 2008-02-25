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
package org.jboss.osgi.plugins.facade;

import static org.jboss.osgi.plugins.metadata.ValueCreatorUtil.PACKAGE_LIST_VC;
import static org.jboss.osgi.plugins.metadata.ValueCreatorUtil.PARAM_ATTRIB_VC;
import static org.jboss.osgi.plugins.metadata.ValueCreatorUtil.PATH_ATTRIB_LIST_VC;
import static org.jboss.osgi.plugins.metadata.ValueCreatorUtil.QNAME_ATTRIB_LIST_VC;
import static org.jboss.osgi.plugins.metadata.ValueCreatorUtil.STRING_LIST_VC;
import static org.jboss.osgi.plugins.metadata.ValueCreatorUtil.STRING_VC;
import static org.jboss.osgi.plugins.metadata.ValueCreatorUtil.URL_VC;
import static org.jboss.osgi.plugins.metadata.ValueCreatorUtil.VERSION_VC;
import static org.osgi.framework.Constants.BUNDLE_ACTIVATOR;
import static org.osgi.framework.Constants.BUNDLE_CLASSPATH;
import static org.osgi.framework.Constants.BUNDLE_DESCRIPTION;
import static org.osgi.framework.Constants.BUNDLE_LOCALIZATION;
import static org.osgi.framework.Constants.BUNDLE_MANIFESTVERSION;
import static org.osgi.framework.Constants.BUNDLE_NAME;
import static org.osgi.framework.Constants.BUNDLE_NATIVECODE;
import static org.osgi.framework.Constants.BUNDLE_REQUIREDEXECUTIONENVIRONMENT;
import static org.osgi.framework.Constants.BUNDLE_SYMBOLICNAME;
import static org.osgi.framework.Constants.BUNDLE_UPDATELOCATION;
import static org.osgi.framework.Constants.BUNDLE_VERSION;
import static org.osgi.framework.Constants.DYNAMICIMPORT_PACKAGE;
import static org.osgi.framework.Constants.EXPORT_PACKAGE;
import static org.osgi.framework.Constants.FRAGMENT_HOST;
import static org.osgi.framework.Constants.IMPORT_PACKAGE;
import static org.osgi.framework.Constants.REQUIRE_BUNDLE;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import org.jboss.osgi.plugins.metadata.ValueCreator;
import org.jboss.osgi.spi.metadata.OSGiMetaData;
import org.osgi.framework.Constants;

/**
 * 
 * Container for Bundle headers providing localization for Manifest headers.
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public class BundleHeaders
{
   private static final String LOACAIZED_HEADER_IDENTIFIER = "%";

   private final OSGiMetaData metaData;

   @SuppressWarnings("unchecked")
   private static final Map<String, ValueCreator> valueCreators = new HashMap<String, ValueCreator>();

   private Map<String, Object> nonLocalizedHeaders;

   private Map<String, Map<String, Object>> localizedHeaderMap = new HashMap<String, Map<String, Object>>();

   private boolean scannedForLocalized;

   private boolean localized;

   static
   {
      valueCreators.put(BUNDLE_ACTIVATOR, STRING_VC);
      valueCreators.put(BUNDLE_CLASSPATH, STRING_LIST_VC);
      valueCreators.put(BUNDLE_DESCRIPTION, STRING_VC);
      valueCreators.put(BUNDLE_LOCALIZATION, STRING_VC);
      valueCreators.put(BUNDLE_NAME, STRING_VC);
      valueCreators.put(BUNDLE_NATIVECODE, PATH_ATTRIB_LIST_VC);
      valueCreators.put(BUNDLE_REQUIREDEXECUTIONENVIRONMENT, STRING_LIST_VC);
      valueCreators.put(BUNDLE_SYMBOLICNAME, STRING_VC);
      valueCreators.put(BUNDLE_UPDATELOCATION, URL_VC);
      valueCreators.put(BUNDLE_VERSION, VERSION_VC);
      valueCreators.put(DYNAMICIMPORT_PACKAGE, PACKAGE_LIST_VC);
      valueCreators.put(EXPORT_PACKAGE, PACKAGE_LIST_VC);
      valueCreators.put(FRAGMENT_HOST, PARAM_ATTRIB_VC);
      valueCreators.put(IMPORT_PACKAGE, PACKAGE_LIST_VC);
      valueCreators.put(REQUIRE_BUNDLE, QNAME_ATTRIB_LIST_VC);
   }

   public BundleHeaders(OSGiMetaData metaData)
   {
      this.metaData = metaData;
   }

   private Map<String, Object> getNonLocalizedHeaders()
   {
      if (nonLocalizedHeaders == null)
      {
         nonLocalizedHeaders = new HashMap<String, Object>();
         if (metaData != null)
         {
            if (metaData.getBundleActivator() != null)
            {
               nonLocalizedHeaders.put(BUNDLE_ACTIVATOR, metaData.getBundleActivator());
            }
            if (metaData.getBundleClassPath() != null)
            {
               nonLocalizedHeaders.put(BUNDLE_CLASSPATH, metaData.getBundleClassPath());
            }
            if (metaData.getBundleDescription() != null)
            {
               nonLocalizedHeaders.put(BUNDLE_DESCRIPTION, metaData.getBundleDescription());
            }
            if (metaData.getBundleLocalization() != null)
            {
               nonLocalizedHeaders.put(BUNDLE_LOCALIZATION, metaData.getBundleLocalization());
            }
            if (metaData.getBundleManifestVersion() > 0)
            {
               nonLocalizedHeaders.put(BUNDLE_MANIFESTVERSION, metaData.getBundleManifestVersion());
            }
            if (metaData.getBundleName() != null)
            {
               nonLocalizedHeaders.put(BUNDLE_NAME, metaData.getBundleName());
            }
            if (metaData.getBundleSymbolicName() != null)
            {
               nonLocalizedHeaders.put(BUNDLE_SYMBOLICNAME, metaData.getBundleSymbolicName());
            }
            if (metaData.getBundleUpdateLocation() != null)
            {
               nonLocalizedHeaders.put(BUNDLE_UPDATELOCATION, metaData.getBundleUpdateLocation());
            }
            if (metaData.getBundleVersion() != null)
            {
               nonLocalizedHeaders.put(BUNDLE_VERSION, metaData.getBundleVersion());
            }
            if (metaData.getBundleNativeCode() != null)
            {
               nonLocalizedHeaders.put(BUNDLE_NATIVECODE, metaData.getBundleNativeCode());
            }
            if (metaData.getDynamicImports() != null)
            {
               nonLocalizedHeaders.put(DYNAMICIMPORT_PACKAGE, metaData.getDynamicImports());
            }
            if (metaData.getExportPackages() != null)
            {
               nonLocalizedHeaders.put(EXPORT_PACKAGE, metaData.getExportPackages());
            }
            if (metaData.getFragmentHost() != null)
            {
               nonLocalizedHeaders.put(FRAGMENT_HOST, metaData.getFragmentHost());
            }
            if (metaData.getImportPackages() != null)
            {
               nonLocalizedHeaders.put(IMPORT_PACKAGE, metaData.getImportPackages());
            }
            if (metaData.getRequireBundles() != null)
            {
               nonLocalizedHeaders.put(REQUIRE_BUNDLE, metaData.getRequireBundles());
            }
            if (metaData.getRequiredExecutionEnvironment() != null)
            {
               nonLocalizedHeaders.put(BUNDLE_REQUIREDEXECUTIONENVIRONMENT, metaData.getRequiredExecutionEnvironment());
            }
         }
      }
      return nonLocalizedHeaders;
   }

   @SuppressWarnings("unchecked")
   public Dictionary getHeaders(String locale)
   {
      // TODO - Check permissions (METADATA)
      Map<String, Object> localizedHeaders = localizedHeaderMap.get(locale);
      if (localizedHeaders == null)
      {
         if (localizedHeadersFound(getNonLocalizedHeaders()))
         {
            localizedHeaders = new HashMap<String, Object>(getNonLocalizedHeaders());

            String bundleLocaleBase = (String) getNonLocalizedHeaders().get(Constants.BUNDLE_LOCALIZATION);
            if (bundleLocaleBase == null)
            {
               bundleLocaleBase = Constants.BUNDLE_LOCALIZATION_DEFAULT_BASENAME;
            }
            ResourceBundle resourceBundle = ResourceBundle.getBundle(bundleLocaleBase, convertToLocale(locale));
            for (Entry<String, Object> header : localizedHeaders.entrySet())
            {
               if (metaData.getMainAttribute(header.getKey()).startsWith(LOACAIZED_HEADER_IDENTIFIER))
               {
                  String localizedValue = resourceBundle.getString(header.getKey());
                  if (localizedValue != null)
                  {
                     ValueCreator valueCreator = valueCreators.get(header.getKey());
                     if (valueCreator == null)
                     {
                        valueCreator = STRING_VC;
                     }
                     header.setValue(valueCreator.createValue(localizedValue));
                  }
               }
            }
            localizedHeaderMap.put(locale, localizedHeaders);
         }
         else
         {
            localizedHeaders = getNonLocalizedHeaders();
            localizedHeaderMap.put(locale, getNonLocalizedHeaders());
         }
      }
      return new Hashtable(localizedHeaders);
   }

   private boolean localizedHeadersFound(Map<String, Object> headers)
   {
      if (scannedForLocalized == false)
      {
         for (String headerName : headers.keySet())
         {
            // Need the raw value.....
            if (metaData.getMainAttribute(headerName).startsWith(LOACAIZED_HEADER_IDENTIFIER))
            {
               localized = true;
               break;
            }
         }
         scannedForLocalized = true;
      }
      return localized;
   }

   private Locale convertToLocale(String localeString)
   {
      Locale locale = null;
      String[] parts = localeString.split("_");
      switch (parts.length)
      {
         case 1 :
            locale = new Locale(parts[0]);
            break;
         case 2 :
            locale = new Locale(parts[0], parts[1]);
            break;
         case 3 :
            locale = new Locale(parts[0], parts[1], parts[2]);
      }
      return locale;
   }
}
