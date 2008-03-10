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
package org.jboss.classloader.test.support;

import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.jboss.classloader.plugins.ClassLoaderUtils;
import org.jboss.classloader.plugins.filter.PatternClassFilter;
import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.DelegateLoader;
import org.jboss.classloader.spi.PackageInformation;
import org.jboss.classloader.spi.filter.ClassFilter;

/**
 * MockClassLoaderPolicy.<p>
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MockClassLoaderPolicy extends ClassLoaderPolicy
{
   /** The logical name of the policy */
   private String name;
   
   /** The prefix */
   private String prefix = "";
   
   /** The delegates */
   private List<? extends DelegateLoader> delegates;
   
   /** The paths */
   private String[] paths;
   
   /** The included resources */
   private String[] included;
   
   /** The excluded resources */
   private String[] excluded;

   /** The exported package names */
   private String[] packageNames;
   
   /** Whether to import all */
   private boolean importAll;

   /** The non JDK classes filter */
   private ClassFilter nonJDKFilter;
   
   /**
    * Create a new MockClassLoaderPolicy filtering org.jboss.* classes
    * with logical name "mock"
    */
   public MockClassLoaderPolicy()
   {
      this(null);
   }

   /**
    * Create a new MockClassLoaderPolicy filtering org.jboss.* classes
    * 
    * @param name the logical name of the policy
    */
   public MockClassLoaderPolicy(String name)
   {
      this(name, new String[] { "org\\.jboss\\..+" }, 
                 new String[] { "org/jboss/.+" },
                 new String[] { "org\\.jboss", "org\\.jboss\\..*" });
   }

   /**
    * Create a new MockClassLoaderPolicy filtering org.jboss.* classes
    * 
    * @param name the name
    * @param classPatterns the class patterns
    * @param resourcePatterns the resourcePatterns
    * @param packagePatterns the packagePatterns
    */
   public MockClassLoaderPolicy(String name, String[] classPatterns, String[] resourcePatterns, String[] packagePatterns)
   {
      this(name, new PatternClassFilter(classPatterns, resourcePatterns, packagePatterns));
   }

   /**
    * Create a new MockClassLoaderPolicy filtering the given patterns
    * 
    * @param name the logical name of the policy
    * @param nonJDKFilter the filter for nonJDK classes
    * @throws IllegalArgumentException for a null filter
    */
   public MockClassLoaderPolicy(String name, ClassFilter nonJDKFilter)
   {
      if (name == null)
         name = "mock";
      this.name = name;
      if (nonJDKFilter == null)
         throw new IllegalArgumentException("Null filter");
      this.nonJDKFilter = nonJDKFilter;
   }
   
   public String getName()
   {
      return name;
   }
   
   @Override
   public List<? extends DelegateLoader> getDelegates()
   {
      return delegates;
   }
   
   /**
    * Set the delegates
    * 
    * @param delegates the delegate imports
    */
   public void setDelegates(List<? extends DelegateLoader> delegates)
   {
      this.delegates = delegates;
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

   @Override
   public String[] getPackageNames()
   {
      return packageNames;
   }
   
   /**
    * Set the exported package names
    * 
    * @param packageNames the exported packages
    */
   public void setPackageNames(String... packageNames)
   {
      this.packageNames = packageNames;
   }
   
   /**
    * Set the exported package names
    * 
    * @param classes the classes to reference to determine the package names
    */
   public void setPackageNames(Class<?>... classes)
   {
      if (classes == null)
      {
         packageNames = null;
         return;
      }
      packageNames = new String[classes.length];
      for (int i = 0; i < classes.length; ++i)
         packageNames[i] = ClassLoaderUtils.getClassPackageName(classes[i].getName());
   }
   
   /**
    * Set the included classes
    * 
    * @param included the classes to include from the paths
    */
   public void setIncluded(String... included)
   {
      this.included = included;
   }
   
   /**
    * Set the included classes
    * 
    * @param classes the classes to include from the paths
    */
   public void setIncluded(Class<?>... classes)
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
    * Set the excluded classes
    * 
    * @param excluded the classes to include from the paths
    */
   public void setExcluded(String... excluded)
   {
      this.excluded = excluded;
   }
   
   /**
    * Set the excluded classes
    * 
    * @param classes the classes to exclude from the paths
    */
   public void setExcluded(Class<?>... classes)
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
    * Set the paths and the exported package names
    * 
    * @param classes the classes to reference
    */
   public void setPathsAndPackageNames(Class<?>... classes)
   {
      setPaths(classes);
      setPackageNames(classes);
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
         packageNames = null;
         return;
      }
      paths = new String[packages.length];
      for (int i = 0; i < packages.length; ++i)
         paths[i] = packages[i].replace('.', '/');
      
      setPackageNames(packages);
   }
   
   @Override
   public boolean isImportAll()
   {
      return importAll;
   }

   /**
    * Set the importAll.
    * 
    * @param importAll the importAll.
    */
   public void setImportAll(boolean importAll)
   {
      this.importAll = importAll;
   }

   @Override
   public URL getResource(String path)
   {
      if (paths == null)
         return null;

      if (excluded != null)
      {
         for (String excludedPath : excluded)
         {
            if (excludedPath.equals(path))
               return null;
         }
      }

      if (included != null)
      {
         boolean include = false;
         for (String includedPath : included)
         {
            if (includedPath.equals(path))
            {
               include = true;
               break;
            }
         }
         if (include == false)
            return null;
      }
      
      for (int i = 0; i < paths.length; ++i)
      {
         if (path.startsWith(paths[i]))
            return getClass().getClassLoader().getResource(prefix + path);
      }
      return null;
   }

   @Override
   public void getResources(String path, Set<URL> urls) throws IOException
   {
      if (paths == null)
         return;

      if (excluded != null)
      {
         for (String excludedPath : excluded)
         {
            if (excludedPath.equals(path))
               return;
         }
      }

      if (included != null)
      {
         boolean include = false;
         for (String includedPath : included)
         {
            if (includedPath.equals(path))
            {
               include = true;
               break;
            }
         }
         if (include == false)
            return;
      }
      
      ClassLoader parent = getClass().getClassLoader();
      for (int i = 0; i < paths.length; ++i)
      {
         if (path.startsWith(paths[i]))
         {
            Enumeration<URL> enumeration = parent.getResources(prefix + path);
            while (enumeration.hasMoreElements())
               urls.add(enumeration.nextElement());
         }
      }
   }
   
   @Override
   protected ProtectionDomain getProtectionDomain(String className, String path)
   {
      final Class<?> clazz;
      try
      {
         clazz = getClass().getClassLoader().loadClass(className);
      }
      catch (ClassNotFoundException e)
      {
         throw new Error("Could not load class: " + className, e);
      }
      return AccessController.doPrivileged(new PrivilegedAction<ProtectionDomain>()
      {
         public ProtectionDomain run()
         {
            return clazz.getProtectionDomain();
         }
      }, getAccessControlContext());
   }

   @Override
   public PackageInformation getPackageInformation(String packageName)
   {
      PackageInformation pi = new PackageInformation(packageName);
      pi.implTitle = name;
      return pi;
   }

   /*
    * Overridden to not expose classes in the nonJDKFilter
    * this is so we don't expose classes from the classpath
    * that we haven't explicitly declared in the policy
    */
   @Override
   protected ClassLoader isJDKRequest(String name)
   {
      if (nonJDKFilter.matchesClassName(name))
         return null;
      return super.isJDKRequest(name);
   }

   @Override
   public ObjectName getObjectName()
   {
      try
      {
         Hashtable<String, String> properties = new Hashtable<String, String>();
         properties.put("name", "'" + name + "'");
         properties.put("domain", "'" + getDomainName() + "'");
         return ObjectName.getInstance("jboss.classloader", properties);
      }
      catch (MalformedObjectNameException e)
      {
         throw new Error("Error creating object name", e);
      }
   }

   @Override 
   public void toLongString(StringBuilder builder)
   {
      builder.append(" name=").append(name);
      if (prefix.length() > 0)
         builder.append(" prefix=").append(prefix);
      if (paths != null)
         builder.append(" paths=").append(Arrays.asList(paths));
      if (included != null)
         builder.append(" included=").append(Arrays.asList(included));
      if (excluded != null)
         builder.append(" excluded=").append(Arrays.asList(excluded));
      super.toLongString(builder);
   }
   
   
   @Override
   public String toString()
   {
      return name;
   }
}
