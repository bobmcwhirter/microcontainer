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
import java.util.Set;

/**
 * Loader.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public interface Loader
{
   /**
    * Load a class
    * 
    * @param className the class name
    * @return the class or null if not found
    */
   Class<?> loadClass(String className);

   /**
    * Get a resource
    * 
    * @param name the resource name
    * @return the url or null if not found
    */
   URL getResource(String name);

   /**
    * Get resources
    * 
    * @param name the resource name
    * @param urls the list of urls to add to
    * @throws IOException for any error
    */
   void getResources(String name, Set<URL> urls) throws IOException;
   
   /**
    * Get a package
    * 
    * @param name the package name
    * @return the package
    */
   Package getPackage(String name);
   
   /**
    * Get all the packages visible from this  loader
    * 
    * @param packages the packages
    */
   void getPackages(Set<Package> packages);
}
