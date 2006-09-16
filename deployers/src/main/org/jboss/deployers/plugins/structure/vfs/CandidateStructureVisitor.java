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

import org.jboss.deployers.plugins.structure.AbstractDeploymentContext;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.logging.Logger;
import org.jboss.virtual.VirtualFile;
import org.jboss.virtual.VisitorAttributes;
import org.jboss.virtual.plugins.vfs.helpers.AbstractVirtualFileVisitor;

/**
 * Visits the structure and creates candidates
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class CandidateStructureVisitor extends AbstractVirtualFileVisitor
{
   /** The log */
   private static final Logger log = Logger.getLogger(CandidateStructureVisitor.class);
   
   /** The parent deployment context */
   private final DeploymentContext parent;

   /** The meta data location */
   private final String metaDataPath;

   /** Ignore directories */
   private boolean ignoreDirectories;
   
   /**
    * Create a new CandidateStructureVisitor.
    * 
    * @param parent the parent
    * @throws IllegalArgumentException for a null parent
    */
   public CandidateStructureVisitor(DeploymentContext parent)
   {
      this(parent, null, false);
   }
   
   /**
    * Create a new CandidateStructureVisitor.
    * 
    * @param parent the parent
    * @param attributes the attributes
    * @param ignoreDirectories whether to ignore directories
    * @throws IllegalArgumentException for a null parent
    */
   public CandidateStructureVisitor(DeploymentContext parent, VisitorAttributes attributes, boolean ignoreDirectories)
   {
      super(attributes);
      if (parent == null)
         throw new IllegalArgumentException("Null parent");
      this.parent = parent;
      VirtualFile metaDataLocation = parent.getMetaDataLocation();
      if (metaDataLocation != null)
         metaDataPath = metaDataLocation.getPathName(); 
      else
         metaDataPath = null;
      this.ignoreDirectories = ignoreDirectories;
   }
   
   public void visit(VirtualFile virtualFile)
   {
      DeploymentContext candidate = createCandidate(virtualFile);
      if (candidate != null)
         parent.addChild(candidate);
   }

   /**
    * Create a new candidate deployment context
    * 
    * @param virtualFile the virtual file
    * @return the candidate or null if it is not a candidate
    */
   protected DeploymentContext createCandidate(VirtualFile virtualFile)
   {
      // Exclude the meta data location
      if (metaDataPath != null && virtualFile.getPathName().startsWith(metaDataPath))
         return null;

      try
      {
         if (ignoreDirectories && virtualFile.isDirectory() && virtualFile.isArchive() == false)
            return null;
      }
      catch (IOException e)
      {
         log.debug("Ignoring " + virtualFile + " reason=" + e);
         return null;
      }
      
      return new AbstractDeploymentContext(virtualFile, true, parent);
   }
}
