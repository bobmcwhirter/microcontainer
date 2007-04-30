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
package org.jboss.classloader.spi.base;

import java.io.IOException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jboss.classloader.plugins.ClassLoaderUtils;
import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.DelegateLoader;
import org.jboss.classloader.spi.Loader;
import org.jboss.logging.Logger;

/**
 * BaseClassLoaderDomain.<p>
 * 
 * This class hides some of the implementation details and allows
 * package access to the protected methods.
 *
 * TODO add caching (needs to be per classloader when not AllExports policy)
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class BaseClassLoaderDomain implements Loader
{
   /** The log */
   private static final Logger log = Logger.getLogger(BaseClassLoaderDomain.class);

   /** The classloader system to which we belong */
   private BaseClassLoaderSystem system;
   
   /** The classloaders  in the order they were registered */
   private List<ClassLoaderInformation> classLoaders = new CopyOnWriteArrayList<ClassLoaderInformation>();

   /** The classloader information by classloader */
   private Map<ClassLoader, ClassLoaderInformation> infos = new ConcurrentHashMap<ClassLoader, ClassLoaderInformation>();
   
   /** The classloaders by package name */
   private Map<String, List<ClassLoaderInformation>> classLoadersByPackageName = new ConcurrentHashMap<String, List<ClassLoaderInformation>>();
   
   /** Keep track of the added order */
   private int order = 0;
   
   /**
    * Get the classloader system
    * 
    * @return the classloader system
    */
   synchronized BaseClassLoaderSystem getClassLoaderSystem()
   {
      return system;
   }
   
   /**
    * Get the classloader system
    * 
    * @param system the classloader system
    */
   synchronized void setClassLoaderSystem(BaseClassLoaderSystem system)
   {
      if (system == null)
         shutdownDomain();
      this.system = system;
   }

   /**
    * Shutdown the domain<p>
    * 
    * The default implementation just unregisters all classloaders
    */
   protected void shutdownDomain()
   {
      log.debug(toLongString() + " shutdown!");

      // Unregister all classloaders
      while (true)
      {
         Iterator<ClassLoaderInformation> iterator = classLoaders.iterator();
         if (iterator.hasNext() == false)
            break;

         while (iterator.hasNext())
         {
            ClassLoaderInformation info = iterator.next();
            if (info != null)
               unregisterClassLoader(info.getClassLoader());
         }
      }
   }
   
   /**
    * Transform the byte code<p>
    * 
    * By default, this delegates to the classloader system
    * 
    * @param className the class name
    * @param byteCode the byte code
    * @param protectionDomain the protection domain
    * @return the transformed byte code
    */
   protected byte[] transform(String className, byte[] byteCode, ProtectionDomain protectionDomain)
   {
      BaseClassLoaderSystem system = getClassLoaderSystem();
      if (system != null)
         system.transform(className, byteCode, protectionDomain);
      return byteCode;
   }

   /**
    * Invoked before classloading is attempted to allow a preload attempt, e.g. from the parent
    * 
    * @param name the class resource name
    * @param resourceName the resource name in dot notation
    * @return the loader if found or null otherwise
    */
   protected abstract Loader findBeforeLoader(String name, String resourceName);
   
   /**
    * Invoked after classloading is attempted to allow a postload attempt, e.g. from the parent
    * 
    * @param name the class resource name
    * @param resourceName the resource name in dot notation
    * @return the loader if found or null otherwise
    */
   protected abstract Loader findAfterLoader(String name, String resourceName);
   
   public Class<?> loadClass(String name)
   {
      try
      {
         return loadClass(null, name, true);
      }
      catch (ClassNotFoundException e)
      {
         return null;
      }
   }
   
   /**
    * Load a class from the domain
    * 
    * @param classLoader the classloader
    * @param name the class name
    * @return the class
    * @throws ClassNotFoundException for any error
    */
   Class<?> loadClass(BaseClassLoader classLoader, String name) throws ClassNotFoundException
   {
      return loadClass(classLoader, name, false);
   }
   
   /**
    * Load a class from the domain
    * 
    * @param classLoader the classloader
    * @param name the class name
    * @param allExports whether we should look at all exports
    * @return the class
    * @throws ClassNotFoundException for any error
    */
   Class<?> loadClass(BaseClassLoader classLoader, String name, boolean allExports) throws ClassNotFoundException
   {
      boolean trace = log.isTraceEnabled();

      if (getClassLoaderSystem() == null)
         throw new IllegalStateException("Domain is not registered with a classloader system: " + toLongString());
      
      String path = ClassLoaderUtils.classNameToPath(name);
      
      // Try the before attempt (e.g. from the parent)
      Loader loader = findBeforeLoader(path, name);
      
      if (loader == null)
      {
         // Work out the rules
         List<? extends DelegateLoader> delegates = null;
         BaseClassLoaderPolicy policy = null;
         if (classLoader != null)
         {
            policy = classLoader.getPolicy();
            delegates = policy.getDelegates();
            if (policy.isImportAll())
               allExports = true;
         }

         // Next we try the old "big ball of mud" model      
         if (allExports)
         {
            String packageName = ClassLoaderUtils.getClassPackageName(name);
            List<ClassLoaderInformation> list = classLoadersByPackageName.get(packageName);
            if (list != null && list.isEmpty() == false)
            {
               if (trace)
                  log.trace(this + " trying to load " + name + " from all exports " + list);
               for (ClassLoaderInformation info : list)
               {
                  BaseDelegateLoader exported = info.getExported();
                  if (exported.getResource(path, name) != null)
                  {
                     loader = exported;
                     break;
                  }
               }
            }
         }
         
         // Next we try the imports
         if (loader == null && delegates != null && delegates.isEmpty() == false)
         {
            for (DelegateLoader delegate : delegates)
            {
               if (trace)
                  log.trace(this + " trying to load " + name + " from import " + delegate + " for " + classLoader);
               if (delegate.getResource(path, name) != null)
               {
                  loader = delegate;
                  break;
               }
            }
         }

         // Next use any requesting classloader, this will look at everything not just what it exports
         if (loader == null && classLoader != null)
         {
            if (trace)
               log.trace(this + " trying to load " + name + " from requesting " + classLoader);
            if (classLoader.getResourceLocally(path, name) != null)
               loader = classLoader.getLoader();
         }

         // Try the after attempt (e.g. from the parent)
         if (loader == null)
            loader = findAfterLoader(path, name);

         // Finally see whether this is the JDK assuming it can load its classes from any classloader
         if (loader == null)
         {
            ClassLoader hack = policy.isJDKRequest(name);
            if (hack != null)
            {
               if (trace)
                  log.trace(this + " trying to load " + name + " using hack " + hack);
               Class<?> result = hack.loadClass(name);
               if (result != null)
               {
                  if (trace)
                     log.trace(this + " loaded from hack " + hack + " " + ClassLoaderUtils.classToString(result));
                  return result;
               }
            }
         }
      }

      if (loader != null)
      {
         Thread thread = Thread.currentThread();
         ClassLoadingTask task = new ClassLoadingTask(name, classLoader, thread);
         ClassLoaderManager.scheduleTask(task, loader, false);
         return ClassLoaderManager.process(thread, task);
      }
      
      // Didn't find it
      return null;
   }
   
   /**
    * Invoked before getResource is attempted to allow a preload attempt, e.g. from the parent
    * 
    * @param name the resource name
    * @param resourceName the name of the resource in dot notation
    * @return the url if found or null otherwise
    */
   protected abstract URL beforeGetResource(String name, String resourceName);
   
   /**
    * Invoked after getResource is attempted to allow a postload attempt, e.g. from the parent
    * 
    * @param name the class name
    * @param resourceName the name of the resource in dot notation
    * @return the url if found or null otherwise
    */
   protected abstract URL afterGetResource(String name, String resourceName);
   
   public URL getResource(String name, String resourceName)
   {
      return getResource(null, name, resourceName, true);
   }
   
   /**
    * Get a resource from the domain
    * 
    * @param classLoader the classloader
    * @param name the resource name
    * @param resourceName the name of the resource in dot notation
    * @return the url
    */
   URL getResource(BaseClassLoader classLoader, String name, String resourceName)
   {
      return getResource(classLoader, name, resourceName, false);
   }
   
   /**
    * Load a resource from the domain
    * 
    * @param classLoader the classloader
    * @param name the resource name
    * @param resourceName the name of the resource in dot notation
    * @param allExports whether we should look at all exports
    * @return the url
    */
   URL getResource(BaseClassLoader classLoader, String name, String resourceName, boolean allExports)
   {
      boolean trace = log.isTraceEnabled();

      if (getClassLoaderSystem() == null)
         throw new IllegalStateException("Domain is not registered with a classloader system: " + toLongString());

      // Try the before attempt
      URL result = beforeGetResource(name, resourceName);
      if (result != null)
         return result;

      // Work out the rules
      List<? extends DelegateLoader> delegates = null;
      BaseClassLoaderPolicy policy = null;
      if (classLoader != null)
      {
         policy = classLoader.getPolicy();
         delegates = policy.getDelegates();
         if (policy.isImportAll())
            allExports = true;
      }

      // Next we try the old "big ball of mud" model      
      if (allExports)
      {
         String packageName = ClassLoaderUtils.getResourcePackageName(name);
         List<ClassLoaderInformation> list = classLoadersByPackageName.get(packageName);
         if (list != null && list.isEmpty() == false)
         {
            if (trace)
               log.trace(this + " trying to get resource " + name + " from all exports " + list);
            for (ClassLoaderInformation info : list)
            {
               BaseDelegateLoader loader = info.getExported();
               result = loader.getResource(name, resourceName);
               if (result != null)
                  return result;
            }
         }
      }
      
      // Next we try the imports
      if (delegates != null && delegates.isEmpty() == false)
      {
         for (DelegateLoader delegate : delegates)
         {
            if (trace)
               log.trace(this + " trying to get resource " + name + " from import " + delegate + " for " + classLoader);
            result = delegate.getResource(name, resourceName);
            if (result != null)
               return result;
         }
      }

      // Finally use any requesting classloader
      if (classLoader != null)
      {
         if (trace)
            log.trace(this + " trying to get resource " + name + " from requesting " + classLoader);
         result = classLoader.getResourceLocally(name, resourceName);
         if (result != null)
         {
            if (trace)
               log.trace(this + " got resource from requesting " + classLoader + " " + result);
            return result;
         }
      }

      // Try the after attempt
      result = afterGetResource(name, resourceName);
      if (result != null)
         return result;
      
      // Didn't find it
      return null;
   }
   
   /**
    * Invoked before getResources is attempted to allow a preload attempt, e.g. from the parent
    * 
    * @param name the resource name
    * @param resourceName the name of the resource in dot notation
    * @param urls the urls to add to
    * @throws IOException for any error
    */
   protected abstract void beforeGetResources(String name, String resourceName, Set<URL> urls) throws IOException;
   
   /**
    * Invoked after getResources is attempted to allow a postload attempt, e.g. from the parent
    * 
    * @param name the resource name
    * @param resourceName the name of the resource in dot notation
    * @param urls the urls to add to
    * @throws IOException for any error
    */
   protected abstract void afterGetResources(String name, String resourceName, Set<URL> urls) throws IOException;
   
   public void getResources(String name, String resourceName, Set<URL> urls) throws IOException
   {
      getResources(null, name, resourceName, urls, true);
   }
   
   /**
    * Get a resource from the domain
    * 
    * @param classLoader the classloader
    * @param name the resource name
    * @param resourceName the name of the resource in dot notation
    * @param urls the urls to add to
    * @throws IOException for any error
    */
   void getResources(BaseClassLoader classLoader, String name, String resourceName, Set<URL> urls) throws IOException
   {
      getResources(classLoader, name, resourceName, urls, false);
   }
   
   /**
    * Load a resource from the domain
    * 
    * @param classLoader the classloader
    * @param name the resource name
    * @param resourceName the name of the resource in dot notation
    * @param allExports whether we should look at all exports
    * @param urls the urls to add to
    * @throws IOException for any error
    */
   void getResources(BaseClassLoader classLoader, String name, String resourceName, Set<URL> urls, boolean allExports) throws IOException
   {
      boolean trace = log.isTraceEnabled();

      if (getClassLoaderSystem() == null)
         throw new IllegalStateException("Domain is not registered with a classloader system: " + toLongString());

      // Try the before attempt
      beforeGetResources(name, resourceName, urls);

      // Work out the rules
      List<? extends DelegateLoader> delegates = null;
      BaseClassLoaderPolicy policy = null;
      if (classLoader != null)
      {
         policy = classLoader.getPolicy();
         delegates = policy.getDelegates();
         if (policy.isImportAll())
            allExports = true;
      }

      // Next we try the old "big ball of mud" model      
      if (allExports)
      {
         String packageName = ClassLoaderUtils.getResourcePackageName(name);
         List<ClassLoaderInformation> list = classLoadersByPackageName.get(packageName);
         if (list != null && list.isEmpty() == false)
         {
            if (trace)
               log.trace(this + " trying to get resources " + name + " from all exports " + list);
            for (ClassLoaderInformation info : list)
            {
               BaseDelegateLoader loader = info.getExported();
               loader.getResources(name, resourceName, urls);
            }
         }
      }
      
      // Next we try the imports
      if (delegates != null && delegates.isEmpty() == false)
      {
         for (DelegateLoader delegate : delegates)
         {
            if (trace)
               log.trace(this + " trying to get resources " + name + " from import " + delegate + " for " + classLoader);
            delegate.getResources(name, resourceName, urls);
         }
      }

      // Finally use any requesting classloader
      if (classLoader != null)
      {
         if (trace)
            log.trace(this + " trying to get resources " + name + " from requesting " + classLoader);
         classLoader.getResourcesLocally(name, resourceName, urls);
      }

      // Try the after attempt
      afterGetResources(name, resourceName, urls);
   }

   /**
    * A long version of toString()
    * 
    * @return the long string
    */
   public String toLongString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(getClass().getSimpleName());
      builder.append("@").append(Integer.toHexString(System.identityHashCode(this)));
      builder.append("{");
      toLongString(builder);
      builder.append('}');
      return builder.toString();
   }
   
   /**
    * For subclasses to add information for toLongString()
    * 
    * @param builder the builder
    */
   protected void toLongString(StringBuilder builder)
   {
   }
   
   /**
    * Invoked before adding a classloader policy 
    * 
    * @param classLoader the classloader
    * @param policy the classloader policy
    */
   protected void beforeRegisterClassLoader(ClassLoader classLoader, ClassLoaderPolicy policy)
   {
      // nothing
   }
   /**
    * Invoked after adding a classloader policy 
    * 
    * @param classLoader the classloader
    * @param policy the classloader policy
    */
   protected void afterRegisterClassLoader(ClassLoader classLoader, ClassLoaderPolicy policy)
   {
      // nothing
   }
   
   /**
    * Invoked before adding a classloader policy 
    * 
    * @param classLoader the classloader
    * @param policy the classloader policy
    */
   protected void beforeUnregisterClassLoader(ClassLoader classLoader, ClassLoaderPolicy policy)
   {
      // nothing
   }
   /**
    * Invoked after adding a classloader policy 
    * 
    * @param classLoader the classloader
    * @param policy the classloader policy
    */
   protected void afterUnregisterClassLoader(ClassLoader classLoader, ClassLoaderPolicy policy)
   {
      // nothing
   }
   
   /**
    * Remove a classloader 
    * 
    * @param classLoader the classloader
    */
   protected void removeClassLoader(BaseClassLoader classLoader)
   {
      // nothing
   }

   /**
    * Get the parent classloader
    * 
    * @return the parent classloader
    */
   protected ClassLoader getParentClassLoader()
   {
      return getClass().getClassLoader();
   }

   /**
    * Register a classloader 
    * 
    * @param classLoader the classloader
    */
   void registerClassLoader(BaseClassLoader classLoader)
   {
      log.debug(this + " registerClassLoader " + classLoader.toLongString());

      if (getClassLoaderSystem() == null)
         throw new IllegalStateException("Domain is not registered with a classloader system: " + toLongString());
      
      try
      {
         beforeRegisterClassLoader(classLoader, classLoader.getPolicy());
      }
      catch (Throwable t)
      {
         log.warn("Error in beforeRegisterClassLoader: " + this + " classLoader=" + classLoader.toLongString(), t);
      }
      
      BaseClassLoaderPolicy policy = classLoader.getPolicy();
      policy.setClassLoaderDomain(this);

      synchronized (classLoaders)
      {
         // Create the information
         ClassLoaderInformation info = new ClassLoaderInformation(classLoader, policy, order++);
         classLoaders.add(info);
         infos.put(classLoader, info);

         // Index the packages
         // TODO Test base package and add support for dynamic packages to the policy
         String[] packageNames = policy.getPackageNames();
         if (packageNames != null && info.getExported() != null)
         {
            for (String packageName : packageNames)
            {
               List<ClassLoaderInformation> list = classLoadersByPackageName.get(packageName);
               if (list == null)
               {
                  list = new CopyOnWriteArrayList<ClassLoaderInformation>();
                  classLoadersByPackageName.put(packageName, list);
               }
               list.add(info);
               log.trace("Registered " + policy + " as providing package=" + packageName);
            }
         }
      }

      try
      {
         afterRegisterClassLoader(classLoader, classLoader.getPolicy());
      }
      catch (Throwable t)
      {
         log.warn("Error in afterRegisterClassLoader: " + this + " classLoader=" + classLoader.toLongString(), t);
      }
   }
   
   /**
    * Remove a classloader 
    * 
    * @param classLoader the classloader
    */
   synchronized void unregisterClassLoader(BaseClassLoader classLoader)
   {
      log.debug(this + " unregisterClassLoader " + classLoader.toLongString());

      try
      {
         beforeUnregisterClassLoader(classLoader, classLoader.getPolicy());
      }
      catch (Throwable t)
      {
         log.warn("Error in beforeUnegisterClassLoader: " + this + " classLoader=" + classLoader.toLongString(), t);
      }

      BaseClassLoaderPolicy policy = classLoader.getPolicy();
      policy.unsetClassLoaderDomain(this);

      synchronized (classLoaders)
      {
         // Remove the classloader
         ClassLoaderInformation info = infos.remove(classLoader);
         classLoaders.remove(info);
         
         // Remove the package index
         String[] packageNames = policy.getPackageNames();
         if (packageNames != null && info.getExported() != null)
         {
            for (String packageName : packageNames)
            {
               List<ClassLoaderInformation> list = classLoadersByPackageName.get(packageName);
               if (list != null)
               {
                  list.remove(info);
                  log.trace("Unregistered " + policy + " as providing package=" + packageName);
                  if (list.isEmpty())
                     classLoadersByPackageName.remove(packageName);
               }
            }
         }
      }

      try
      {
         afterUnregisterClassLoader(classLoader, classLoader.getPolicy());
      }
      catch (Throwable t)
      {
         log.warn("Error in afterUnegisterClassLoader: " + this + " classLoader=" + classLoader.toLongString(), t);
      }
   }
}
