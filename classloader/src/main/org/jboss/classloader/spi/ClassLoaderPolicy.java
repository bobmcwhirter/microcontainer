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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.jboss.classloader.spi.base.BaseClassLoaderPolicy;
import org.jboss.classloader.spi.filter.FilteredDelegateLoader;
import org.jboss.classloader.spi.filter.PackageClassFilter;
import org.jboss.classloader.spi.jdk.JDKChecker;
import org.jboss.classloader.spi.jdk.JDKCheckerFactory;
import org.jboss.logging.Logger;

/**
 * ClassLoader policy.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class ClassLoaderPolicy extends BaseClassLoaderPolicy
{
   /** The log */
   private static final Logger log = Logger.getLogger(ClassLoaderPolicy.class);

   /**
    * Get the delegate loader for exported stuff<p>
    *
    * By default this uses {@link #getPackageNames()} to create a {@link FilteredDelegateLoader}
    * 
    * @return the delegate loader
    */
   public DelegateLoader getExported()
   {
      String[] packageNames = getPackageNames();
      if (packageNames == null)
         return null;
      return new FilteredDelegateLoader(this, PackageClassFilter.createPackageClassFilter(packageNames));
   }

   /**
    * Get the exported packages<p>
    *
    * Provides a hint for indexing<p>
    * 
    * No packages are exported by default<p>
    * 
    * The returned package names can be null to indicate
    * nothing is exported, but if an array is returned
    * it should not include a null package element.
    * 
    * @return the package names
    */
   public String[] getPackageNames()
   {
      return null;
   }
   
   /**
    * Get the delegate loaders for imported stuff<p>
    * 
    * There are no imports by default<p>
    * 
    * NOTE: Protected access for security reasons
    * 
    * @return the delegate loaders
    */
   protected List<? extends DelegateLoader> getDelegates()
   {
      return Collections.emptyList();
   }

   /**
    * Whether to import all exports from other classloaders in the domain<p>
    *
    * False by default
    * 
    * @return true to import all
    */
   protected boolean isImportAll()
   {
      return false;
   }

   /**
    * Whether to cache<p>
    * 
    * True by default
    * 
    * @return true to cache
    */
   protected boolean isCacheable()
   {
      return true;
   }

   /**
    * Whether to cache misses<p>
    * 
    * True by default
    * 
    * @return true to cache misses
    */
   protected boolean isBlackListable()
   {
      return true;
   }
   
   /**
    * Get the resource
    * 
    * @param path the path
    * @return the url or null if not found
    */
   public abstract URL getResource(String path);

   /**
    * Get the resource as a stream<p>
    * 
    * Uses {@link #getResource(String)} by default
    * 
    * @param path the path
    * @return the stream or null if not found
    */
   public InputStream getResourceAsStream(String path)
   {
      URL url = getResource(path);
      if (url != null)
      {
         try
         {
            return url.openStream();
         }
         catch (IOException e)
         {
            log.debug("Unable to open URL: " + url + " for path " + path + " from " + toLongString());
         }
      }
      return null;
   }

   /**
    * Get resources
    * 
    * @param name the resource name
    * @param urls the list of urls to add to
    * @throws IOException for any error
    */
   public abstract void getResources(String name, Set<URL> urls) throws IOException;
   
   /**
    * Get the protection domain<p>
    * 
    * By default there is no protection domain<p>
    * 
    * NOTE: Protected access for security reasons
    * 
    * @param className the class name
    * @param path the path
    * @return the protection domain
    */
   protected ProtectionDomain getProtectionDomain(String className, String path)
   {
      return null;
   }
   
   /**
    * Get the package information<p>
    * 
    * There is no package information by default
    * 
    * @param packageName the package information
    * @return the information or null if there is none
    */
   public PackageInformation getPackageInformation(String packageName)
   {
      return null;
   }

   /**
    * Check whether this a request from the jdk if it is return the relevant classloader<p>
    * 
    * By default this uses the {@Link JDKCheckerFactory} and returns the system classloader if true.
    * 
    * @param name the class name
    * @return the classloader
    */
   protected ClassLoader isJDKRequest(String name)
   {
      JDKChecker checker = JDKCheckerFactory.getChecker();
      if (checker.isJDKRequest(name))
         return getSystemClassLoader();
      return null;
   }

   @Override
   public ObjectName getObjectName()
   {
      try
      {
         String name = getName();
         if (name != null && name.trim().length() > 0)
            return ObjectName.getInstance("jboss.classloader", "id", "'" + name + "'");
         return ObjectName.getInstance("jboss.classloader", "id", "" + System.identityHashCode(this));
      }
      catch (MalformedObjectNameException e)
      {
         throw new Error("Error creating object name", e);
      }
   }
   
   @Override
   protected void toLongString(StringBuilder builder)
   {
      builder.append(" delegates=").append(getDelegates());
      String[] packageNames = getPackageNames();
      if (packageNames != null)
         builder.append(" exported=").append(Arrays.asList(packageNames));
      boolean importAll = isImportAll();
      if (importAll)
         builder.append(" <IMPORT-ALL>");
   }

   /**
    * Get the system classloader
    * 
    * @return the classloader
    */
   private ClassLoader getSystemClassLoader()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm == null)
         return ClassLoader.getSystemClassLoader();
      
      return AccessController.doPrivileged(GetSystemClassLoader.INSTANCE, getAccessControlContext());
   }
   
   /**
    * GetSystemClassLoader.
    */
   private static class GetSystemClassLoader implements PrivilegedAction<ClassLoader>
   {
      private static GetSystemClassLoader INSTANCE = new GetSystemClassLoader();
      
      public ClassLoader run()
      {
         return ClassLoader.getSystemClassLoader();
      }
   }
}
