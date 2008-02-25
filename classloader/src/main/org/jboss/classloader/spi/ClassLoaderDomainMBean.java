/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
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
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.ObjectName;

/**
 * ClassLoaderSystemMBean.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public interface ClassLoaderDomainMBean
{
   /**
    * Get the classloader system
    * 
    * @return the system
    */
   ObjectName getSystem();

   /**
    * Get the name.
    * 
    * @return the name.
    */
   String getName();
   
   /**
    * Get the parent policy name
    * 
    * @return the parent policy name.
    */
   String getParentPolicyName();

   /**
    * Get the parent
    * 
    * @return the parent.
    */
   ObjectName getParentDomain();

   /**
    * Get the parent
    * 
    * @return the parent.
    */
   String getParentDomainName();

   /**
    * Get the classloaders
    * 
    * @return the classloaders
    */
   List<ObjectName> getClassLoaders();

   /**
    * Get the exporting classloaders
    * 
    * @return a map of packages to classloaders
    */
   Map<String, List<ObjectName>> getExportingClassLoaders();

   /**
    * Get the classloaders export a package
    * 
    * @param packageName the package name
    * @return the classloaders
    */
   List<ObjectName> getExportingClassLoaders(String packageName);
   
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
    * Flush the caches
    */
   void flushCaches();
}
