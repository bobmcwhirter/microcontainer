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
package org.jboss.deployers.plugins.structure.vfs;

import java.io.IOException;
import java.net.MalformedURLException;

import org.jboss.deployers.plugins.structure.AbstractDeploymentContext;
import org.jboss.deployers.spi.structure.vfs.VFSDeploymentContext;
import org.jboss.vfs.spi.VirtualFile;

/**
 * VFSDeploymentContext.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class AbstractVFSDeploymentContext extends AbstractDeploymentContext implements VFSDeploymentContext
{
   /** The root */
   private VirtualFile root;

   /** Whether this is a candidate deployment */
   private boolean candidate;
   
   /**
    * Get the deployment name
    * 
    * @param file the file
    * @return the name;
    */
   public static String getDeploymentName(VirtualFile file)
   {
      if (file == null)
         throw new IllegalArgumentException("Null file");
      try
      {
         return file.toURL().toString();
      }
      catch (MalformedURLException e)
      {
         throw new IllegalArgumentException("File does not have a valid url: " + file, e);
      }
   }
   
   /**
    * Create a new AbstractDeploymentContext.
    * 
    * @param root the root
    */
   public AbstractVFSDeploymentContext(VirtualFile root)
   {
      this(root, false);
   }
   
   /**
    * Create a new AbstractDeploymentContext.
    * 
    * @param root the root
    * @param candidate whether this is a candidate
    */
   public AbstractVFSDeploymentContext(VirtualFile root, boolean candidate)
   {
      super(getDeploymentName(root));
      this.root = root;
      this.candidate = candidate;
   }

   public VirtualFile getRoot()
   {
      return root;
   }

   public boolean isCandidate()
   {
      return candidate;
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
}
