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
package org.jboss.classloader.spi.filter;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.DelegateLoader;
import org.jboss.logging.Logger;

/**
 * FilteredDelegateLoader.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class FilteredDelegateLoader extends DelegateLoader
{
   /** The log */
   private static final Logger log = Logger.getLogger(FilteredDelegateLoader.class);
   
   /** The filter */
   private ClassFilter filter;

   /**
    * Create a new FilteredDelegateLoader that does not filter
    * 
    * @param delegate the delegate classloading policy
    * @throws IllegalArgumentException for a null parameter
    */
   public FilteredDelegateLoader(ClassLoaderPolicy delegate)
   {
      this(delegate, ClassFilter.EVERYTHING);
   }

   /**
    * Create a new FilteredDelegateLoader.
    * 
    * @param delegate the delegate classloading policy
    * @param filter the filter
    * @throws IllegalArgumentException for a null parameter
    */
   public FilteredDelegateLoader(ClassLoaderPolicy delegate, ClassFilter filter)
   {
      super(delegate);
      if (filter == null)
         throw new IllegalArgumentException("Null filter");
      this.filter = filter;
   }

   /**
    * Get the filter.
    * 
    * @return the filter.
    */
   public ClassFilter getFilter()
   {
      return filter;
   }

   @Override
   public Class<?> loadClass(String className)
   {
      boolean trace = log.isTraceEnabled();
      if (filter.matches(className))
      {
         if (trace)
            log.trace(this + " " + className + " matches filter=" + filter);
         return super.loadClass(className);
      }
      if (trace)
         log.trace(this + " " + className + " does NOT match filter=" + filter);
      return null;
   }

   public URL getResource(String name, String resourceName)
   {
      boolean trace = log.isTraceEnabled();
      if (filter.matches(resourceName))
      {
         if (trace)
            log.trace(this + " " + name + " matches filter=" + filter);
         return super.getResource(name, resourceName);
      }
      if (trace)
         log.trace(this + " " + name + " NOT match filter=" + filter);
      return null;
   }

   public void getResources(String name, String resourceName, Set<URL> urls) throws IOException
   {
      boolean trace = log.isTraceEnabled();
      if (filter.matches(resourceName))
      {
         if (trace)
            log.trace(this + " " + name + " matches filter=" + filter);
         super.getResources(name, resourceName, urls);
      }
      if (trace)
         log.trace(this + " " + name + " matches does NOT match filter=" + filter);
   }

   @Override
   protected void toLongString(StringBuilder builder)
   {
      builder.append(" filter=").append(getFilter());
   }
}
