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
package org.jboss.deployers.vfs.plugins.structure;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.List;

import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.structure.spi.helpers.AbstractDeploymentContext;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentContext;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentResourceLoader;
import org.jboss.virtual.VFSUtils;
import org.jboss.virtual.VirtualFile;

/**
 * AbstractVFSDeploymentContext.
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class AbstractVFSDeploymentContext extends AbstractDeploymentContext implements VFSDeploymentContext
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 4474515937180482776L;

   /** The root virtual file */
   private VirtualFile root;
   
   /** The meta data location */
   private VirtualFile metaDataLocation;
   
   /** The class paths */
   private List<VirtualFile> classPath;
   
   /** The loader */
   private transient VFSDeploymentResourceLoader loader;

   /**
    * Get the vfs file name safely
    * 
    * @param root the virutal file
    * @return the name
    */
   static final String safeVirtualFileName(VirtualFile root)
   {
      if (root == null)
         throw new IllegalArgumentException("Null root");
      try
      {
         return root.toURI().toString();
      }
      catch (Exception e)
      {
         return root.getName();
      }
   }

   /**
    * For serialization
    */
   public AbstractVFSDeploymentContext()
   {
   }

   /**
    * Create a new AbstractVFSDeploymentContext.
    * 
    * @param root the virtual file
    * @param relativePath the relative path
    */
   public AbstractVFSDeploymentContext(VirtualFile root, String relativePath)
   {
      super(safeVirtualFileName(root), root.getName(), relativePath);
      this.root = root;
   }

   public VirtualFile getRoot()
   {
      return root;
   }
   
   public void setMetaDataPath(String path)
   {
      if (path == null)
         setMetaDataLocation(null);
      try
      {
         setMetaDataLocation(root.findChild(path));
      }
      catch (IOException e)
      {
         log.debug("Meta data path does not exist: root=" + root.getPathName() + " path=" + path);
      }
   }

   public VirtualFile getMetaDataLocation()
   {
      return metaDataLocation;
   }

   public void setMetaDataLocation(VirtualFile location)
   {
      this.metaDataLocation = location;
   }
   
   public VirtualFile getMetaDataFile(String name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      try
      {
         // There isn't a metadata location so let's see whether the root matches.
         if (metaDataLocation == null)
         {
            // It has to be a plain file
            if (root != null && SecurityActions.isLeaf(root))
            {
               String fileName = root.getName();
               if (fileName.equals(name))
                  return root;
            }
            
            // No match
            return null;
         }
         // Look in the meta data location
         VirtualFile result = metaDataLocation.findChild(name);
         if (result != null)
         {
            log.trace("Found " + name + " in " + metaDataLocation.getName());
            deployed();
         }
         return result;
      }
      catch (Exception e)
      {
         log.trace("Error retrieving meta data: " + name + " reason=" + e);
         return null;
      }
   }

   public List<VirtualFile> getMetaDataFiles(String name, String suffix)
   {
      if (name == null && suffix == null)
         throw new IllegalArgumentException("Null name and suffix");
      try
      {
         // There isn't a metadata location so let's see whether the root matches.
         // i.e. the top level is an xml
         if (metaDataLocation == null)
         {
            // It has to be a plain file
            if (root != null && SecurityActions.isLeaf(root))
            {
               String fileName = root.getName();
               if (name != null && fileName.equals(name))
                  return Collections.singletonList(root);
               if (suffix != null && fileName.endsWith(suffix))
                  return Collections.singletonList(root);
            }
            
            // No match
            return Collections.emptyList();
         }
         // Look in the meta data location
         List<VirtualFile> result = metaDataLocation.getChildren(new MetaDataMatchFilter(name, suffix));
         if (result != null && result.isEmpty() == false)
         {
            log.trace("Found " + name + " in " + metaDataLocation.getName());
            deployed();
         }
         return result;
      }
      catch (Exception e)
      {
         log.debug("Error retrieving meta data: name=" + name + " suffix=" + suffix, e);
         return Collections.emptyList();
      }
   }

   public VirtualFile getFile(String name)
   {
      return getResourceLoader().getFile(name);
   }

   public List<VirtualFile> getClassPath()
   {
      return classPath;
   }

   public void setClassPath(List<VirtualFile> paths)
   {
      this.classPath = paths;
      if (log.isTraceEnabled() && paths != null)
         log.trace("ClassPath for " + root.getPathName() + " is " + VFSUtils.getPathsString(paths));
   }

   @Override
   public VFSDeploymentContext getTopLevel()
   {
      return (VFSDeploymentContext) super.getTopLevel();
   }

   @Override
   public VFSDeploymentResourceLoader getResourceLoader()
   {
      if (loader != null)
         return loader;
      
      loader = new VFSDeploymentResourceLoaderImpl(getRoot());
      return loader;
   }

   protected DeploymentUnit createDeploymentUnit()
   {
      return new AbstractVFSDeploymentUnit(this);
   }

   @SuppressWarnings("unchecked")
   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
   {
      super.readExternal(in);
      root = (VirtualFile) in.readObject();
      metaDataLocation = (VirtualFile) in.readObject();
      classPath = (List) in.readObject();
   }

   /**
    * @serialData root
    * @serialData metaDataLocation
    * @serialData classPath
    * @param out the output
    * @throws IOException for any error
    */
   public void writeExternal(ObjectOutput out) throws IOException
   {
      super.writeExternal(out);
      out.writeObject(root);
      out.writeObject(metaDataLocation);
      out.writeObject(classPath);
   }
}
