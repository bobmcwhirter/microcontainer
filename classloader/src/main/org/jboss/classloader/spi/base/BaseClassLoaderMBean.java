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
import java.util.List;
import java.util.Set;

import javax.management.ObjectName;

/**
 * BaseClassLoaderMBean.
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public interface BaseClassLoaderMBean
{
   /**
    * Get the classloader domain
    * 
    * @return the domain
    */
   ObjectName getClassLoaderDomain();
   
   /**
    * Get the name of the classloader
    * 
    * @return the name
    */
   String getName();
   
   /**
    * Whether to import all exports from other classloaders in the domain
    * 
    * @return true to import all
    */
   boolean isImportAll();

   /**
    * Whether to cache<
    * 
    * @return true to cache
    */
   boolean isCacheable();

   /**
    * Whether to cache misses
    * 
    * @return true to cache misses
    */
   boolean isBlackListable();
   
   /**
    * Whether the classloader is still valid
    * 
    * @return true when still valid
    */
   boolean isValid();
   
   /**
    * Get the exported packages
    * 
    * @return the exported packages
    */
   Set<String> getExportedPackages();

   /**
    * Get the imports of this classloader
    * 
    * @return the imports
    */
   List<ObjectName> getImports();
   
   /**
    * Get the policy as a string
    * 
    * @return the policy string
    */
   String getPolicyDetails();
   
   /**
    * Get the loaded classes
    * 
    * @return the loaded classes
    */
   Set<String> getLoadedClasses();
   
   /**
    * Get the loaded resource names
    * 
    * @return the loaded resources names
    */
   Set<String> getLoadedResourceNames();
   
   /**
    * Get the loaded resources
    * 
    * @return the loaded resources
    */
   Set<URL> getLoadedResources();
   
   /**
    * Load a class
    * 
    * @param name the class name
    * @return the class
    * @throws ClassNotFoundException when the class is not found
    */
   Class<?> loadClass(String name) throws ClassNotFoundException;
   
   /**
    * Get resources
    * 
    * @param name the name of the resource
    * @return the resource urls
    * @throws IOException for any error
    */
   Set<URL> loadResources(String name) throws IOException;
   
   /**
    * Find the classloader for a class
    * 
    * @param name the class name
    * @return the classloader or null if it is not loaded by a managed classloader
    * @throws ClassNotFoundException when the class is not found
    */
   ObjectName findClassLoaderForClass(String name) throws ClassNotFoundException; 

   /**
    * Clear the black list
    */
   void clearBlackList();

   /**
    * Clear an entry from the black list
    * 
    * @param name the name of the entry to remove
    */
   void clearBlackList(String name);
}
