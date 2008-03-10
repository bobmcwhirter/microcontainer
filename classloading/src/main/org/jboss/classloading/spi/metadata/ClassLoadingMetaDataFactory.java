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
package org.jboss.classloading.spi.metadata;

import org.jboss.classloading.spi.version.VersionRange;

/**
 * ClassLoadingMetaDataFactory.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class ClassLoadingMetaDataFactory
{
   /**
    * Get an instance of the factory
    * 
    * @return the factory
    */
   public static ClassLoadingMetaDataFactory getInstance()
   {
      return ClassLoadingMetaDataFactoryBuilder.getInstance();
   }

   /**
    * Create a new module with the default version
    * 
    * @param name the name
    * @return the module capability
    * @throws IllegalArgumentException for a null name
    */
   public Capability createModule(String name)
   {
      return createModule(name, null);
   }

   /**
    * Create a new module
    * 
    * @param name the name
    * @param version the version
    * @return the module capability
    * @throws IllegalArgumentException for a null name
    */
   public abstract Capability createModule(String name, Object version);

   /**
    * Create a new module requirement with no version constraint
    * 
    * @param name the name
    * @return the module requirement
    * @throws IllegalArgumentException for a null name
    */
   public Requirement createRequireModule(String name)
   {
      return createRequireModule(name, null);
   }

   /**
    * Create a new module requirement
    * 
    * @param name the name
    * @param versionRange the version range
    * @return the module requirement
    * @throws IllegalArgumentException for a null name
    */
   public Requirement createRequireModule(String name, VersionRange versionRange)
   {
      return createRequireModule(name, versionRange, false, false, false);
   }

   /**
    * Create a new module requirement
    * 
    * @param name the name
    * @param versionRange the version range
    * @param optional whether the requirement is optional
    * @param reExport whether the requirement is a re-export
    * @param dynamic whether the requirement is dynamic
    * @return the module requirement
    * @throws IllegalArgumentException for a null name
    */
   public abstract Requirement createRequireModule(String name, VersionRange versionRange, boolean optional, boolean reExport, boolean dynamic);

   /**
    * Create a new re-export module requirement with no version constraint
    * 
    * @param name the name
    * @return the module requirement
    * @throws IllegalArgumentException for a null name
    */
   public Requirement createReExportModule(String name)
   {
      return createReExportModule(name, null);
   }

   /**
    * Create a new re-export module requirement
    * 
    * @param name the name
    * @param versionRange the version range
    * @return the module requirement
    * @throws IllegalArgumentException for a null name
    */
   public Requirement createReExportModule(String name, VersionRange versionRange)
   {
      return createReExportModule(name, versionRange, false);
   }

   /**
    * Create a new re-export module requirement
    * 
    * @param name the name
    * @param versionRange the version range
    * @param optional whether the requirement is optional
    * @return the module requirement
    * @throws IllegalArgumentException for a null name
    */
   public Requirement createReExportModule(String name, VersionRange versionRange, boolean optional)
   {
      return createRequireModule(name, versionRange, optional, true, false);
   }

   /**
    * Create a new package with the default version
    * 
    * @param name the name
    * @return the package capability
    * @throws IllegalArgumentException for a null name
    */
   public Capability createPackage(String name)
   {
      return createPackage(name, null);
   }

   /**
    * Create a new package
    * 
    * @param name the name
    * @param version the version
    * @return the package capability
    * @throws IllegalArgumentException for a null name
    */
   public abstract Capability createPackage(String name, Object version);

   /**
    * Create a new packagerequirement with no version constraint
    * 
    * @param name the name
    * @return the package requirement
    * @throws IllegalArgumentException for a null name
    */
   public Requirement createRequirePackage(String name)
   {
      return createRequirePackage(name, null);
   }

   /**
    * Create a new package requirement
    * 
    * @param name the name
    * @param versionRange the version range
    * @return the package requirement
    * @throws IllegalArgumentException for a null name
    */
   public Requirement createRequirePackage(String name, VersionRange versionRange)
   {
      return createRequirePackage(name, versionRange, false, false, false);
   }

   /**
    * Create a new package requirement
    * 
    * @param name the name
    * @param versionRange the version range
    * @param optional whether the requirement is optional
    * @param reExport whether the requirement is a re-export
    * @param dynamic whether the requirement is dynamic
    * @return the package requirement
    * @throws IllegalArgumentException for a null name
    */
   public abstract Requirement createRequirePackage(String name, VersionRange versionRange, boolean optional, boolean reExport, boolean dynamic);

   /**
    * Create a new re-export package requirement with no version constraint
    * 
    * @param name the name
    * @return the module requirement
    * @throws IllegalArgumentException for a null name
    */
   public Requirement createReExportPackage(String name)
   {
      return createReExportPackage(name, null, false);
   }

   /**
    * Create a new re-export package requirement
    * 
    * @param name the name
    * @param versionRange the version range
    * @return the module requirement
    * @throws IllegalArgumentException for a null name
    */
   public Requirement createReExportPackage(String name, VersionRange versionRange)
   {
      return createReExportPackage(name, versionRange, false);
   }

   /**
    * Create a new re-export module requirement
    * 
    * @param name the name
    * @param versionRange the version range
    * @param optional whether the requirement is optional
    * @return the module requirement
    * @throws IllegalArgumentException for a null name
    */
   public Requirement createReExportPackage(String name, VersionRange versionRange, boolean optional)
   {
      return createRequirePackage(name, versionRange, optional, true, false);
   }

   /**
    * Create a new usespackagerequirement with no version constraint
    * 
    * @param name the name
    * @return the package requirement
    * @throws IllegalArgumentException for a null name
    */
   public Requirement createUsesPackage(String name)
   {
      return createUsesPackage(name, null);
   }

   /**
    * Create a new uses package requirement
    * 
    * @param name the name
    * @param versionRange the version range
    * @return the package requirement
    * @throws IllegalArgumentException for a null name
    */
   public Requirement createUsesPackage(String name, VersionRange versionRange)
   {
      return createUsesPackage(name, versionRange, false);
   }

   /**
    * Create a new uses package requirement
    * 
    * @param name the name
    * @param versionRange the version range
    * @param reExport whether the requirement is a re-export
    * @return the package requirement
    * @throws IllegalArgumentException for a null name
    */
   public abstract Requirement createUsesPackage(String name, VersionRange versionRange, boolean reExport);
}
