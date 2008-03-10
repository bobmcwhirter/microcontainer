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
package org.jboss.classloading.spi.dependency.policy.mock;

import org.jboss.classloader.plugins.ClassLoaderUtils;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaData;
import org.jboss.classloading.spi.version.Version;

/**
 * MockClassLoadingMetaData.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MockClassLoadingMetaData extends ClassLoadingMetaData
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;
   
   /** The prefix */
   private String prefix = "";
   
   /** The paths */
   private String[] paths;
   
   /** The included resources */
   private String[] included;
   
   /** The excluded resources */
   private String[] excluded;

   /** The exported package names */
   private String[] exported;

   /**
    * Create a new MockClassLoadingMetaData.
    * 
    * @param name the name
    */
   public MockClassLoadingMetaData(String name)
   {
      this(name, Version.DEFAULT_VERSION);
   }
   
   /**
    * Create a new MockClassLoadingMetaData.
    * 
    * @param name the name
    * @param version the version
    */
   public MockClassLoadingMetaData(String name, String version)
   {
      setName(name);
      setVersion(Version.parseVersion(version));
   }
   
   /**
    * Create a new MockClassLoadingMetaData.
    * 
    * @param name the name
    * @param version the version
    */
   public MockClassLoadingMetaData(String name, Version version)
   {
      setName(name);
      setVersion(version);
   }
   
   /**
    * Get the prefix.
    * 
    * @return the prefix.
    */
   public String getPrefix()
   {
      return prefix;
   }

   /**
    * Set the prefix.
    * 
    * @param prefix the prefix.
    */
   public void setPrefix(String prefix)
   {
      this.prefix = prefix;
   }

   /**
    * Get the paths to expose
    * 
    * @return the paths
    */
   public String[] getPaths()
   {
      return paths;
   }
   
   /**
    * Set the path to expose
    * 
    * @param path the path
    */
   public void setPath(String path)
   {
      setPaths(path);
   }
   
   /**
    * Set the paths to expose
    * 
    * @param paths the paths to expose
    */
   public void setPaths(String... paths)
   {
      this.paths = paths;
   }
   
   /**
    * Set the paths to expose
    * 
    * @param classes the classes to reference to determine the package paths
    */
   public void setPaths(Class<?>... classes)
   {
      if (classes == null)
      {
         paths = null;
         return;
      }
      paths = new String[classes.length];
      for (int i = 0; i < classes.length; ++i)
         paths[i] = ClassLoaderUtils.packageNameToPath(classes[i].getName());
   }

   public String[] getExportedPackages()
   {
      return exported;
   }
   
   /**
    * Set the exported package names
    * 
    * @param packageNames the exported packages
    */
   public void setExportedPackages(String... packageNames)
   {
      this.exported = packageNames;
   }
   
   /**
    * Set the exported package names
    * 
    * @param classes the classes to reference to determine the package names
    */
   public void setExportedPackages(Class<?>... classes)
   {
      if (classes == null)
      {
         exported = null;
         return;
      }
      exported = new String[classes.length];
      for (int i = 0; i < classes.length; ++i)
         exported[i] = ClassLoaderUtils.getClassPackageName(classes[i].getName());
   }
   
   /**
    * Get the included.
    * 
    * @return the included.
    */
   public String[] getIncludedClasses()
   {
      return included;
   }

   /**
    * Set the included.
    * 
    * @param included the included.
    */
   public void setIncluded(String[] included)
   {
      this.included = included;
   }

   /**
    * Get the excluded.
    * 
    * @return the excluded.
    */
   public String[] getExcludedClasses()
   {
      return excluded;
   }

   /**
    * Set the excluded.
    * 
    * @param excluded the excluded.
    */
   public void setExcluded(String[] excluded)
   {
      this.excluded = excluded;
   }

   /**
    * Set the included classes
    * 
    * @param classes the classes to include from the paths
    */
   public void setIncludedClasses(Class<?>... classes)
   {
      if (classes == null)
      {
         included = null;
         return;
      }
      included = new String[classes.length];
      for (int i = 0; i < classes.length; ++i)
         included[i] = ClassLoaderUtils.classNameToPath(classes[i].getName());
   }
   
   /**
    * Set the included classes
    * 
    * @param included the classes to include from the paths
    */
   public void setIncludedClasses(String... included)
   {
      this.included = included;
   }
   
   /**
    * Set the excluded classes
    * 
    * @param classes the classes to exclude from the paths
    */
   public void setExcludedClasses(Class<?>... classes)
   {
      if (classes == null)
      {
         excluded = null;
         return;
      }
      excluded = new String[classes.length];
      for (int i = 0; i < classes.length; ++i)
         excluded[i] = ClassLoaderUtils.classNameToPath(classes[i].getName());
   }
   
   /**
    * Set the excluded classes
    * 
    * @param excluded the classes to include from the paths
    */
   public void setExcludedClasses(String... excluded)
   {
      this.excluded = excluded;
   }

   /**
    * Set the paths and the exported package names
    * 
    * @param classes the classes to reference
    */
   public void setPathsAndPackageNames(Class<?>... classes)
   {
      setPaths(classes);
      setExportedPackages(classes);
   }

   /**
    * Set the paths and the exported package names
    * 
    * @param packages the packages
    */
   public void setPathsAndPackageNames(String... packages)
   {
      if (packages == null)
      {
         paths = null;
         exported = null;
         return;
      }
      paths = new String[packages.length];
      for (int i = 0; i < packages.length; ++i)
         paths[i] = packages[i].replace('.', '/');
      
      setExportedPackages(packages);
   }
}
