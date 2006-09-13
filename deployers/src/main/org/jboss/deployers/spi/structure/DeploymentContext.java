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
package org.jboss.deployers.spi.structure;

import java.util.List;
import java.util.Set;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.attachments.Attachments;
import org.jboss.deployers.spi.classloader.ClassLoaderFactory;
import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.virtual.VirtualFile;

/**
 * DeploymentContext.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public interface DeploymentContext
{
   /**
    * Get the deployment name
    * 
    * @return the name
    */
   String getName();

   /**
    * Whether the structure is determined
    * 
    * @return true when the structure is determined
    */
   StructureDetermined getStructureDetermined();
   
   /**
    * Set whether the structure is determined
    * 
    * @param determined true when it is determined
    */
   void setStructureDetermined(StructureDetermined determined);
   
   /**
    * Whether this deployment context is a candidate deployment context
    * 
    * @return true when it is only a candidate
    */
   boolean isCandidate();
   
   /**
    * Get the deployment state
    * 
    * @return the state
    */
   DeploymentState getState();
   
   /**
    * Set the deployment state
    * 
    * @param state the state
    */
   void setState(DeploymentState state);

   /**
    * Get the deployment unit
    * 
    * @return the deployment
    */
   DeploymentUnit getDeploymentUnit();

   /**
    * Set the deployment unit
    * 
    * @param unit the deployment unit
    */
   void setDeploymentUnit(DeploymentUnit unit);

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
    * Gets the classloader for this deployment unit
    * 
    * @return the classloader
    */
   ClassLoader getClassLoader();
   
   /**
    * Set the class loader
    * 
    * @param classLoader the new classloader
    */
   void setClassLoader(ClassLoader classLoader);

   /**
    * Create a classloader
    * 
    * @param factory the factory
    * @return false if there is already is a classloader
    * @throws DeploymentException for any error
    */
   boolean createClassLoader(ClassLoaderFactory factory) throws DeploymentException;

   /**
    * Remove the classloader created by the factory
    */
   void removeClassLoader();

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
    * Whether this is a top level deployment
    * 
    * @return true when top level
    */
   boolean isTopLevel();
   
   /**
    * Get the top level deployment
    * 
    * @return the top level deployment
    */
   DeploymentContext getTopLevel();
   
   /**
    * The parent
    * 
    * @return the parent
    */
   DeploymentContext getParent();
   
   /**
    * Set the parent
    * 
    * @param parent the parent
    */
   void setParent(DeploymentContext parent);
   
   /**
    * The children
    * 
    * @return the children
    */
   Set<DeploymentContext> getChildren();

   /**
    * Add a child
    * 
    * @param child the child to add
    */
   void addChild(DeploymentContext child);

   /**
    * Remove a child
    * 
    * @param child the child to remove
    * @return whether it was removed
    */
   boolean removeChild(DeploymentContext child);

   /**
    * Get the predetermined managed objects
    * 
    * @return the predetermined managed objects
    */
   Attachments getPredeterminedManagedObjects();

   /**
    * Get the managed objects
    * 
    * @return the managed objects
    */
   Attachments getTransientManagedObjects();

   /**
    * Get the transient attachments
    * 
    * @return the attachments
    */
   Attachments getTransientAttachments();

   /**
    * Get the problem for this context
    * 
    * @return the problem
    */
   Throwable getProblem();

   /**
    * Set the problem for this context
    * 
    * @param problem the problem
    */
   void setProblem(Throwable problem);
   
   /**
    * Reset a deployment context that is about to be deployed
    */
   void reset();
}
