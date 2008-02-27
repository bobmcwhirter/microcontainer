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
package org.jboss.osgi.plugins.facade.helpers;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.jboss.classloading.spi.metadata.ClassLoadingMetaData;
import org.jboss.deployers.structure.spi.DeploymentUnit;

/**
 * 
 * Container for Bundle headers providing localization for Manifest headers.
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public class BundleHeaders
{
   private List<BundleHeader> headers = new ArrayList<BundleHeader>();

   public BundleHeaders(DeploymentUnit unit)
   {
      ClassLoadingMetaData classLoadingMetaData = getClassLoaderMetaData(unit);
      if(classLoadingMetaData != null) {
         headers.add(BundleHeaderBuilders.EXPORT_PACKAGE_BUILDER.buildHeader(classLoadingMetaData));
         headers.add(BundleHeaderBuilders.IMPORT_PACKAGE_BUILDER.buildHeader(classLoadingMetaData));
         /* TODO
          * 
          * BUNDLE_CLASSPATH
          * REQUIRE_BUNDLE
          * DYNAMICIMPORT_PACKAGE
          * 
          */
         
      }
      /* TODO
       * 
       * BUNDLE_NAME
       * BUNDLE_VERSION
       * BUNDLE_ACTIVATOR
       * BUNDLE_DESCRIPTION
       * BUNDLE_MANIFESTVERSION
       * BUNDLE_UPDATELOCATION - Maybe...
       * 
       */
   }

   /**
    * Converts the BundleHeaders into Dictionarry
    * 
    * @return Dictionary of headers
    */
   public Dictionary<String, Object> toDictionary()
   {
      Dictionary<String, Object> headerDictionary = new Hashtable<String, Object>();
      for (BundleHeader header : headers)
      {
         if(header != null)
         {
            headerDictionary.put(header.getKey(), header.getValue());
         }
      }
      return headerDictionary;
   }

   private ClassLoadingMetaData getClassLoaderMetaData(DeploymentUnit unit)
   {
      Set<? extends ClassLoadingMetaData> metaDatas = unit.getAllMetaData(ClassLoadingMetaData.class);
      if(metaDatas != null && !metaDatas.isEmpty()) {
         return metaDatas.iterator().next();
      }
      return null;
   }
}
