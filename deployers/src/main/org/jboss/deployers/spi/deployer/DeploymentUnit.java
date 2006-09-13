/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.deployers.spi.deployer;

import java.util.List;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.attachments.Attachments;
import org.jboss.deployers.spi.classloader.ClassLoaderFactory;
import org.jboss.virtual.VirtualFile;

/**
 * DeploymentUnit.<p>
 * 
 * A deployment unit represents a single unit
 * that deployers work with.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public interface DeploymentUnit extends Attachments
{
   /**
    * Get the deployment units name
    * 
    *  @return the name;
    */
   String getName();
   
   /**
    * Gets a metadata file
    * 
    * @param name the name to exactly match
    * @return the virtual file or null if not found
    * @throws IllegalArgumentException for a null name
    */
   VirtualFile getMetaDataFile(String name);
   
   /**
    * Gets the metadata files for this deployment unit
    * 
    * @param name the name to exactly match
    * @param suffix the suffix to partially match
    * @return the virtual files that match
    * @throws IllegalArgumentException if both the name and suffix are null
    */
   List<VirtualFile> getMetaDataFiles(String name, String suffix);
   
   /**
    * Gets the classloader for this deployment unit
    * 
    * @return the classloader
    */
   ClassLoader getClassLoader();

   /**
    * Create the classloader
    * 
    * @param factory the classloader factory
    * @return false if the classloader already exists
    * @throws IllegalArgumentException for a null factory
    * @throws DeploymentException for any error
    */
   boolean createClassLoader(ClassLoaderFactory factory) throws DeploymentException;
   
   /**
    * Get the managed objects
    * 
    * @return the managed objects
    */
   Attachments getTransientManagedObjects();
}
