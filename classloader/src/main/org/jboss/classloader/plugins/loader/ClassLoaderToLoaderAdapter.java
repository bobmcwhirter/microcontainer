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
package org.jboss.classloader.plugins.loader;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import org.jboss.classloader.spi.Loader;

/**
 * ClassLoaderToLoaderAdapter.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ClassLoaderToLoaderAdapter implements Loader
{
   /** The classloader */
   private ClassLoader classLoader;

   /**
    * Create a new ClassLoaderToLoaderAdapter.
    * 
    * @param classLoader the classloader
    */
   public ClassLoaderToLoaderAdapter(ClassLoader classLoader)
   {
      if (classLoader == null)
         throw new IllegalArgumentException("Null classLoader");
      this.classLoader = classLoader;
   }

   public URL getResource(String name, String resourceName)
   {
      return classLoader.getResource(name);
   }

   public void getResources(String name, String resourceName, Set<URL> urls) throws IOException
   {
      Enumeration<URL> enumeration = classLoader.getResources(name);
      while (enumeration.hasMoreElements())
         urls.add(enumeration.nextElement());
   }

   public Class<?> loadClass(String className)
   {
      try
      {
         return classLoader.loadClass(className);
      }
      catch (ClassNotFoundException e)
      {
         return null;
      }
   }

   @Override
   public String toString()
   {
      return classLoader.toString();
   }
}
