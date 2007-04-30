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
package org.jboss.test.classloader.support;

import java.io.IOException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import org.jboss.classloader.plugins.ClassLoaderUtils;
import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.DelegateLoader;
import org.jboss.test.classloader.AbstractClassLoaderTest;

/**
 * MockClassLoaderPolicy.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MockClassLoaderPolicy extends ClassLoaderPolicy
{
   private String name;
   
   private List<? extends DelegateLoader> delegates;
   
   private String[] paths;
   
   private String[] included;
   
   private String[] excluded;

   private String[] packageNames;
   
   private boolean importAll;
   
   private AbstractClassLoaderTest test;
   
   public MockClassLoaderPolicy(AbstractClassLoaderTest test)
   {
      this(null, test);
   }

   public MockClassLoaderPolicy(String name, AbstractClassLoaderTest test)
   {
      if (name == null)
         name = "mock";
      this.name = name;
      this.test = test;
   }
   
   @Override
   public List<? extends DelegateLoader> getDelegates()
   {
      return delegates;
   }
   
   public void setDelegates(List<? extends DelegateLoader> delegates)
   {
      this.delegates = delegates;
   }
   
   public String[] getPaths()
   {
      return paths;
   }
   
   public void setPath(String path)
   {
      setPaths(new String[] { path });
   }
   
   public void setPaths(String[] paths)
   {
      this.paths = paths;
   }
   
   public void setPaths(Class... classes)
   {
      if (classes == null)
         paths = null;
      paths = new String[classes.length];
      for (int i = 0; i < classes.length; ++i)
         paths[i] = ClassLoaderUtils.packageNameToPath(classes[i].getName());
   }

   @Override
   public String[] getPackageNames()
   {
      return packageNames;
   }
   
   public void setPackageNames(String[] packageNames)
   {
      this.packageNames = packageNames;
   }
   
   public void setPackageNames(Class... classes)
   {
      if (classes == null)
         packageNames = null;
      packageNames = new String[classes.length];
      for (int i = 0; i < classes.length; ++i)
         packageNames[i] = ClassLoaderUtils.getClassPackageName(classes[i].getName());
   }
   
   public void setIncluded(Class... classes)
   {
      if (classes == null)
         included = null;
      included = new String[classes.length];
      for (int i = 0; i < classes.length; ++i)
         included[i] = ClassLoaderUtils.classNameToPath(classes[i].getName());
   }
   
   public void setExcluded(Class... classes)
   {
      if (classes == null)
         excluded = null;
      excluded = new String[classes.length];
      for (int i = 0; i < classes.length; ++i)
         excluded[i] = ClassLoaderUtils.classNameToPath(classes[i].getName());
   }

   public void setPathsAndPackageNames(Class... classes)
   {
      setPaths(classes);
      setPackageNames(classes);
   }
   
   /**
    * Get the importAll.
    * 
    * @return the importAll.
    */
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
            return getClass().getClassLoader().getResource(path);
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
            Enumeration<URL> enumeration = parent.getResources(path);
            while (enumeration.hasMoreElements())
               urls.add(enumeration.nextElement());
         }
      }
   }
   
   @Override
   protected ProtectionDomain getProtectionDomain(String className, String path)
   {
      return test.getProtectionDomain(className);
   }

   /*
    * Overridden to not load jboss classes
    * this is so we don't expose classes from the classpath
    * that we haven't explicitly declared in the policy
    */
   protected ClassLoader isJDKRequest(String name)
   {
      if (name.startsWith("org.jboss."))
         return null;
      return super.isJDKRequest(name);
   }

   @Override 
   public void toLongString(StringBuilder builder)
   {
      builder.append(" name=").append(name);
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
