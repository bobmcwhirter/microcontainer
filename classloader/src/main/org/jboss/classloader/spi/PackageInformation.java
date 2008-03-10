/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.classloader.spi;

import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;

/**
 * PackageInformation.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class PackageInformation
{
   /** The package name */
   public String packageName;

   /** The specification title */
   public String specTitle;
   
   /** The specification version */
   public String specVersion; 
   
   /** The specification vendor */
   public String specVendor;
   
   /** The implementation title */
   public String implTitle;
   
   /** The implementation version */
   public String implVersion;
   
   /** The implementation vendor */
   public String implVendor;
   
   /** The seal base url */
   public URL sealBase;
   
   /**
    * Create a new PackageInformation.
    * 
    * @param packageName the package name
    * @throws IllegalArgumentException for a null parameter
    */
   public PackageInformation(String packageName)
   {
      this(packageName, null, null);
   }
   
   /**
    * Create a new PackageInformation from a manifest
    * 
    * @param packageName the package name
    * @param manifest the manifest
    * @throws IllegalArgumentException for a null package name
    */
   public PackageInformation(String packageName, Manifest manifest)
   {
      this(packageName, manifest, null);
   }
   
   /**
    * Create a new PackageInformation from a manifest
    * 
    * @param packageName the package name
    * @param manifest the manifest
    * @param seal the url to seal
    * @throws IllegalArgumentException for a null package name
    */
   public PackageInformation(String packageName, Manifest manifest, URL seal)
   {
      if (packageName == null)
         throw new IllegalArgumentException("Null package name");

      if (manifest != null)
      {
         String path = packageName.replace('.', '/').concat("/");
         String sealed = null;
         Attributes attributes = manifest.getAttributes(path);
         if (attributes != null)
         {
            specTitle = attributes.getValue(Name.SPECIFICATION_TITLE);
            specVersion = attributes.getValue(Name.SPECIFICATION_VERSION);
            specVendor = attributes.getValue(Name.SPECIFICATION_VENDOR);
            implTitle = attributes.getValue(Name.IMPLEMENTATION_TITLE);
            implVersion = attributes.getValue(Name.IMPLEMENTATION_VERSION);
            implVendor = attributes.getValue(Name.IMPLEMENTATION_VENDOR);
            
            sealed = attributes.getValue(Name.SEALED);
         }
         attributes = manifest.getMainAttributes();
         if (attributes != null)
         {
            if (specTitle == null)
            {
               specTitle = attributes.getValue(Name.SPECIFICATION_TITLE);
               specVersion = attributes.getValue(Name.SPECIFICATION_VERSION);
               specVendor = attributes.getValue(Name.SPECIFICATION_VENDOR);
            }
            if (implTitle == null)
            {
               implTitle = attributes.getValue(Name.IMPLEMENTATION_TITLE);
               implVersion = attributes.getValue(Name.IMPLEMENTATION_VERSION);
               implVendor = attributes.getValue(Name.IMPLEMENTATION_VENDOR);
            }
            
            if (sealed == null)
               sealed = attributes.getValue(Name.SEALED);
         }
         if (seal != null && "true".equalsIgnoreCase(sealed))
            sealBase = seal;
      }
   }
}
