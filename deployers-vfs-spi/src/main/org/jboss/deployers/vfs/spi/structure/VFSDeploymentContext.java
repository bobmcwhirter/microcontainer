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
package org.jboss.deployers.vfs.spi.structure;

import java.util.List;

import org.jboss.deployers.structure.spi.DeploymentContext;
import org.jboss.virtual.VirtualFile;

/**
 * VFSDeploymentContext.
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public interface VFSDeploymentContext extends DeploymentContext
{
   /**
    * Get the root file
    * 
    * @return the root
    */
   VirtualFile getRoot();

   /**
    * Set the meta data path relative to the root
    * 
    * @param path the path
    */
   void setMetaDataPath(String path);

   /**
    * Get the meta data location
    * 
    * @return the meta data location
    */
   VirtualFile getMetaDataLocation();
   
   /**
    * Set the meta data location
    * 
    * @param location the meta data location
    */
   void setMetaDataLocation(VirtualFile location);
   
   /**
    * Gets a metadata file
    * 
    * @param name the name to exactly match
    * @return the virtual file or null if not found
    * @throws IllegalArgumentException for a null name
    */
   VirtualFile getMetaDataFile(String name);

   /**
    * Gets metadata files for this deployment
    * 
    * @param name the name to exactly match
    * @param suffix the suffix to partially match
    * @return the virtual files that match
    * @throws IllegalArgumentException if both the name and suffix are null
    */
   List<VirtualFile> getMetaDataFiles(String name, String suffix);

   /**
    * Gets a file from this deployment
    * 
    * @param name the name to exactly match
    * @return the file or null if not found
    * @throws IllegalArgumentException if both the name
    */
   VirtualFile getFile(String name);

   /**
    * Get the class path
    * 
    * @return the class path
    */
   List<VirtualFile> getClassPath();
   
   /**
    * Set the class path
    * 
    * @param paths the paths 
    */
   void setClassPath(List<VirtualFile> paths);
   
   /**
    * Get the top level deployment context
    * 
    * @return the top level deployment context
    */
   VFSDeploymentContext getTopLevel();
   
   /**
    * Get a resource loader
    * 
    * @return the resource loader
    */
   VFSDeploymentResourceLoader getResourceLoader();
}
