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
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Set;

import org.jboss.classloader.plugins.loader.ClassLoaderToLoaderAdapter;
import org.jboss.classloader.spi.base.BaseClassLoaderDomain;
import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.logging.Logger;

/**
 * ClassLoaderDomain.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ClassLoaderDomain extends BaseClassLoaderDomain implements Loader
{
   /** The log */
   private static final Logger log = Logger.getLogger(ClassLoaderDomain.class);
   
   /** The name of the domain */
   private String name;

   /** The parent classloading rules */
   private ParentPolicy parentPolicy = ParentPolicy.BEFORE; 
   
   /** The parent */
   private Loader parent;
   
   /**
    * Create a new ClassLoaderDomain with the {@link ParentPolicy#BEFORE} loading rules.
    * 
    * @param name the name of the domain
    * @throws IllegalArgumentException for a null name
    */
   public ClassLoaderDomain(String name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      this.name = name;
      fixUpParent();
   }
   
   /**
    * Get the name.
    * 
    * @return the name.
    */
   public String getName()
   {
      return name;
   }
   
   /**
    * Get the parent policy
    * 
    * @return the parent policy.
    */
   public ParentPolicy getParentPolicy()
   {
      return parentPolicy;
   }
   
   /**
    * Set the parentPolicy.
    * 
    * @param parentPolicy the parentPolicy.
    * @throws IllegalArgumentException for a null parent policy
    */
   public void setParentPolicy(ParentPolicy parentPolicy)
   {
      if (parentPolicy == null)
         throw new IllegalArgumentException("Null parent policy");
      this.parentPolicy = parentPolicy;
   }

   /**
    * Get the parent
    * 
    * @return the parent.
    */
   public Loader getParent()
   {
      return parent;
   }

   /**
    * Set the parent.
    * 
    * @param parent the parent.
    */
   public void setParent(Loader parent)
   {
      this.parent = parent;
      fixUpParent();
   }
   
   /**
    * For subclasses to add information for toLongString()
    * 
    * @param builder the builder
    */
   protected void toLongString(StringBuilder builder)
   {
      builder.append("name=").append(getName());
      builder.append(" parentPolicy=").append(getParentPolicy());
      builder.append(" parent=");
      Loader parent = getParent();
      if (parent != null)
         builder.append(parent);
      else
         builder.append(getParentClassLoader());
   }

   @Override
   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(getClass().getSimpleName());
      builder.append("@").append(Integer.toHexString(System.identityHashCode(this)));
      builder.append("{").append(name).append('}');
      return builder.toString();
   }
   
   @Override
   protected Loader findBeforeLoader(String name)
   {
      boolean trace = log.isTraceEnabled();
      ClassFilter filter = getParentPolicy().getBeforeFilter();
      if (filter.matchesResourcePath(name))
      {
         if (trace)
            log.trace(this + " " + name + " matches parent beforeFilter=" + filter);
         return findLoaderFromParent(name);
      }
      if (trace)
         log.trace(this + " " + name + " does NOT match parent beforeFilter=" + filter);
      return null;
   }

   @Override
   protected Loader findAfterLoader(String name)
   {
      boolean trace = log.isTraceEnabled();
      ClassFilter filter = getParentPolicy().getAfterFilter();
      if (filter.matchesResourcePath(name))
      {
         if (trace)
            log.trace(this + " " + name + " matches parent afterFilter=" + filter);
         return findLoaderFromParent(name);
      }
      if (trace)
         log.trace(this + " " + name + " does NOT match parent afterFilter=" + filter);
      return null;
   }

   /**
    * Try to find a loader from the parent
    * 
    * @param name the name
    * @return the loader if found
    */
   protected Loader findLoaderFromParent(String name)
   {
      Loader parentLoader = getParent();

      boolean trace = log.isTraceEnabled();
      if (parentLoader == null)
      {
         if (trace)
            log.trace(this + " not loading from non-existant parent");
         return null;
      }

      if (trace)
         log.trace(this + " load from parent " + name + " parent=" + parent);

      // Recurse into parent domains
      if (parentLoader instanceof ClassLoaderDomain)
      {
         ClassLoaderDomain parentDomain = (ClassLoaderDomain) parentLoader;
         return parentDomain.findLoader(name);
      }
      
      // A normal loader
      if (parentLoader.getResource(name) != null)
         return parentLoader;
      
      return null;
   }
   
   @Override
   protected URL beforeGetResource(String name)
   {
      boolean trace = log.isTraceEnabled();
      ClassFilter filter = getParentPolicy().getBeforeFilter();
      if (filter.matchesResourcePath(name))
      {
         if (trace)
            log.trace(this + " " + name + " matches parent beforeFilter=" + filter);
         return getResourceFromParent(name);
      }
      if (trace)
         log.trace(this + " " + name + " does NOT match parent beforeFilter=" + filter);
      return null;
   }

   @Override
   protected URL afterGetResource(String name)
   {
      boolean trace = log.isTraceEnabled();
      ClassFilter filter = getParentPolicy().getAfterFilter();
      if (filter.matchesResourcePath(name))
      {
         if (trace)
            log.trace(this + " " + name + " matches parent afterFilter=" + filter);
         return getResourceFromParent(name);
      }
      if (trace)
         log.trace(this + " " + name + " does NOT match parent afterFilter=" + filter);
      return null;
   }

   /**
    * Try to get a resource from the parent
    * 
    * @param name the name
    * @return the url if found
    */
   protected URL getResourceFromParent(String name)
   {
      Loader parentLoader = getParent();

      boolean trace = log.isTraceEnabled();
      if (parentLoader == null)
      {
         if (trace)
            log.trace(this + " not getting resource from non-existant parent");
         return null;
      }

      if (trace)
         log.trace(this + " get resource from parent " + name + " parent=" + parentLoader);
      
      URL result = parentLoader.getResource(name);

      if (trace)
      {
         if (result != null)
            log.trace(this + " got resource from parent " + name + " parent=" + parentLoader + " " + result);
         else
            log.trace(this + " resource not found in parent " + name + " parent=" + parentLoader);
      }
      
      return result;
   }
   
   @Override
   protected void beforeGetResources(String name, Set<URL> urls) throws IOException
   {
      boolean trace = log.isTraceEnabled();
      ClassFilter filter = getParentPolicy().getBeforeFilter();
      if (filter.matchesResourcePath(name))
      {
         if (trace)
            log.trace(this + " " + name + " matches parent beforeFilter=" + filter);
         getResourcesFromParent(name, urls);
      }
      else if (trace)
         log.trace(this + " " + name + " does NOT match parent beforeFilter=" + filter);
   }

   @Override
   protected void afterGetResources(String name, Set<URL> urls) throws IOException
   {
      boolean trace = log.isTraceEnabled();
      ClassFilter filter = getParentPolicy().getAfterFilter();
      if (filter.matchesResourcePath(name))
      {
         if (trace)
            log.trace(this + " " + name + " matches parent afterFilter=" + filter);
         getResourcesFromParent(name, urls);
      }
      else if (trace)
         log.trace(this + " " + name + " does NOT match parent afterFilter=" + filter);
   }

   /**
    * Try to get resources from the parent
    * 
    * @param name the name
    * @param urls the urls to add to
    * @throws IOException for any error
    */
   protected void getResourcesFromParent(String name, Set<URL> urls) throws IOException
   {
      Loader parentLoader = getParent();

      boolean trace = log.isTraceEnabled();
      if (parentLoader == null)
      {
         if (trace)
            log.trace(this + " not getting resources from non-existant parent");
         return;
      }

      if (trace)
         log.trace(this + " get resources from parent " + name + " parent=" + parentLoader);
      
      parentLoader.getResources(name, urls);
   }

   /**
    * Fixup the parent to the our classloader as parent if we don't have an explicit one
    */
   private void fixUpParent()
   {
      if (parent == null)
      {
         final ClassLoader classLoader = getParentClassLoader();
         if (classLoader != null)
         {
            parent = AccessController.doPrivileged(new PrivilegedAction<Loader>()
            {
               public Loader run()
               {
                  return new ClassLoaderToLoaderAdapter(classLoader);
               }
            });
         }
      }
   }
}
