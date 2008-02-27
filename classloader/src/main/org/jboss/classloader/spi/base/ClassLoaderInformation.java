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

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.jboss.classloader.spi.DelegateLoader;
import org.jboss.classloader.spi.Loader;

/**
 * ClassLoaderInformation.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ClassLoaderInformation
{
   /** The classloader */
   private BaseClassLoader classLoader;
   
   /** The policy */
   private BaseClassLoaderPolicy policy;

   /** The order */
   private int order;
   
   /** The delegates */
   private List<? extends DelegateLoader> delegates; 
   
   /** The exports of the classloader */
   private BaseDelegateLoader exported;
   
   /** The class cache */
   private Map<String, Loader> classCache;
   
   /** The class black list */
   private Set<String> classBlackList;
   
   /** The resource cache */
   private Map<String, URL> resourceCache;
   
   /** The resource black list */
   private Set<String> resourceBlackList;

   /**
    * Create a new ClassLoaderInformation.
    * 
    * @param classLoader the classloader
    * @param policy the policy
    * @param order the added order
    * @throws IllegalArgumentException for a null parameter
    */
   public ClassLoaderInformation(BaseClassLoader classLoader, BaseClassLoaderPolicy policy, int order)
   {
      if (classLoader == null)
         throw new IllegalArgumentException("Null classloader");
      if (policy == null)
         throw new IllegalArgumentException("Null policy");
      this.classLoader = classLoader;
      this.policy = policy;
      this.order = order;
      this.exported = policy.getExported();
      this.delegates = policy.getDelegates();
      
      boolean canCache = policy.isCacheable();
      boolean canBlackList = policy.isBlackListable();
      if (delegates != null && delegates.isEmpty() == false)
      {
         for (DelegateLoader delegate : delegates)
         {
            if (delegate == null)
               throw new IllegalStateException(policy + " null delegate in " + delegates);
            BaseDelegateLoader baseDelegate = delegate;
            BaseClassLoaderPolicy delegatePolicy = baseDelegate.getPolicy();
            if (delegatePolicy == null || delegatePolicy.isCacheable() == false)
               canCache = false;
            if (delegatePolicy == null || delegatePolicy.isBlackListable() == false)
               canBlackList = false;
         }
      }

      if (canCache)
      {
         classCache = new ConcurrentHashMap<String, Loader>();
         resourceCache = new ConcurrentHashMap<String, URL>();
      }
      
      if (canBlackList)
      {
         classBlackList = new CopyOnWriteArraySet<String>();
         resourceBlackList = new CopyOnWriteArraySet<String>();
      }
   }

   /**
    * Flush the caches
    */
   public void flushCaches()
   {
      if (classCache != null)
         classCache.clear();
      if (classBlackList != null)
         classBlackList.clear();
      if (resourceCache != null)
         resourceCache.clear();
      if (resourceBlackList != null)
         resourceBlackList.clear();
   }
   
   /**
    * Get the classLoader.
    * 
    * @return the classLoader.
    */
   public BaseClassLoader getClassLoader()
   {
      return classLoader;
   }

   /**
    * Get the policy.
    * 
    * @return the policy.
    */
   public BaseClassLoaderPolicy getPolicy()
   {
      return policy;
   }

   /**
    * Get the order.
    * 
    * @return the order.
    */
   public int getOrder()
   {
      return order;
   }

   /**
    * Get the exported.
    * 
    * @return the exported.
    */
   public BaseDelegateLoader getExported()
   {
      return exported;
   }

   /**
    * Get the delegates.
    * 
    * @return the delegates.
    */
   public List<? extends DelegateLoader> getDelegates()
   {
      return delegates;
   }
   
   /**
    * Get the cached loader for a class 
    * 
    * @param name the class name
    * @return any cached loader
    */
   public Loader getCachedLoader(String name)
   {
      if (classCache != null)
         return classCache.get(name);
      return null;
   }
   
   /**
    * Cache a loader for a class
    * 
    * @param name the class name
    * @param loader the cached loader
    */
   public void cacheLoader(String name, Loader loader)
   {
      if (classCache != null)
         classCache.put(name, loader);
   }
   
   /**
    * Check whether this is a black listed class
    * 
    * @param name the class name
    * @return true when black listed
    */
   public boolean isBlackListedClass(String name)
   {
      if (classBlackList != null)
         return classBlackList.contains(name);
      return false;
   }
   
   /**
    * Blacklist a class
    * 
    * @param name the class name to black list
    */
   public void blackListClass(String name)
   {
      if (classBlackList != null)
         classBlackList.add(name);
   }
   
   /**
    * Get the cached url for a resource 
    * 
    * @param name the resource name
    * @return any cached url
    */
   public URL getCachedResource(String name)
   {
      if (resourceCache != null)
         return resourceCache.get(name);
      return null;
   }
   
   /**
    * Cache a url for a resource
    * 
    * @param name the resource name
    * @param url the cached url
    */
   public void cacheResource(String name, URL url)
   {
      if (resourceCache != null)
         resourceCache.put(name, url);
   }
   
   /**
    * Check whether this is a black listed resource
    * 
    * @param name the resource name
    * @return true when black listed
    */
   public boolean isBlackListedResource(String name)
   {
      if (resourceBlackList != null)
         return resourceBlackList.contains(name);
      return false;
   }
   
   /**
    * Blacklist a resource
    * 
    * @param name the resource name to black list
    */
   public void blackListResource(String name)
   {
      if (resourceBlackList != null)
         resourceBlackList.add(name);
   }
   
   @Override
   public String toString()
   {
      return policy.toString();
   }
}
