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
import java.util.Set;

import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.ClassLoaderPolicyFactory;
import org.jboss.classloader.spi.Loader;
import org.jboss.logging.Logger;

/**
 * Base DelegateLoader.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class BaseDelegateLoader implements Loader
{
   /** The log */
   private static final Logger log = Logger.getLogger(BaseDelegateLoader.class);
   
   /** The delegate loader policy */
   private volatile BaseClassLoaderPolicy delegate;

   /** The policy factory */
   private ClassLoaderPolicyFactory factory;
   
   /**
    * Create a new BaseDelegateLoader.
    * 
    * @param delegate the delegate
    * @throws IllegalArgumentException for a null delegate
    */
   public BaseDelegateLoader(BaseClassLoaderPolicy delegate)
   {
      if (delegate == null)
         throw new IllegalArgumentException("Null delegate");
      this.delegate = delegate;
   }
   
   /**
    * Create a new BaseDelegateLoader.
    * 
    * @param factory the factory
    * @throws IllegalArgumentException for a null factory
    */
   public BaseDelegateLoader(ClassLoaderPolicyFactory factory)
   {
      if (factory == null)
         throw new IllegalArgumentException("Null factory");
      this.factory = factory;
   }
   
   BaseClassLoaderPolicy getPolicy()
   {
      BaseClassLoaderPolicy delegate = this.delegate;
      if (delegate == null)
      {
         try
         {
            delegate = factory.createClassLoaderPolicy();
            if (delegate == null)
            {
               log.trace("Factory did not create a delegate: " + factory);
            }
            else
            {
               ClassLoaderPolicy policy = (ClassLoaderPolicy) delegate;
               initialise(policy);
               this.delegate = delegate;
            }
         }
         catch (Throwable t)
         {
            log.warn("Unexpected error creating policy from factory: " + factory, t);
         }
      }
      return delegate;
   }

   /**
    * Callback to initialise policy
    * 
    * @param policy the policy
    */
   protected void initialise(ClassLoaderPolicy policy)
   {
      // Nothing by default
   }
   
   BaseClassLoader getBaseClassLoader(String context)
   {
      BaseClassLoader result = null;
      try
      {
         BaseClassLoaderPolicy policy = getPolicy();
         if (policy != null)
            result = policy.getClassLoader();
      }
      catch (IllegalStateException ignored)
      {
      }
      if (result == null)
         log.warn("Not " + context + " from policy that has no classLoader: " + toLongString());
      return result;
   }
   
   public Class<?> loadClass(String className)
   {
      BaseClassLoader classLoader = getBaseClassLoader("loading class " + className);
      if (classLoader != null)
         return classLoader.loadClassLocally(className);
      return null;
   }
   
   public URL getResource(String name)
   {
      BaseClassLoader classLoader = getBaseClassLoader("getting resource " + name);
      if (classLoader != null)
         return classLoader.getResourceLocally(name);
      return null;
   }

   public void getResources(String name, Set<URL> urls) throws IOException
   {
      BaseClassLoader classLoader = getBaseClassLoader("getting resources " + name);
      if (classLoader != null)
         classLoader.getResourcesLocally(name, urls);
   }

   public Package getPackage(String name)
   {
      BaseClassLoader classLoader = getBaseClassLoader("getting package " + name);
      if (classLoader != null)
         return classLoader.getPackageLocally(name);
      return null;
   }

   public void getPackages(Set<Package> packages)
   {
      BaseClassLoader classLoader;
      try
      {
         classLoader = delegate.getClassLoader();
      }
      catch (IllegalStateException e)
      {
         log.warn("Not getting packages from policy that has no classLoader: " + toLongString());
         return;
      }
      classLoader.getPackagesLocally(packages);
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
      if (delegate != null)
         builder.append("{delegate=").append(delegate.toLongString());
      else
         builder.append("{factory=").append(factory);
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

   @Override
   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(getClass().getSimpleName());
      builder.append("@").append(Integer.toHexString(System.identityHashCode(this)));
      if (delegate != null)
         builder.append("{delegate=").append(delegate);
      else
         builder.append("{factory=").append(factory);
      builder.append('}');
      return builder.toString();
   }
}
