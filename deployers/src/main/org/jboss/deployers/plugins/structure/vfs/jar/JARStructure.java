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
package org.jboss.deployers.plugins.structure.vfs.jar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jboss.deployers.plugins.structure.vfs.AbstractStructureDeployer;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.virtual.VFSUtils;
import org.jboss.virtual.VirtualFile;

/**
 * JARStructure.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class JARStructure extends AbstractStructureDeployer
{
   public boolean determineStructure(DeploymentContext context)
   {
      try
      {
         VirtualFile root = context.getRoot();
         if (root.isDirectory())
         {
            if (root.isArchive() == false)
            {
               // For non top level directories that don't look like jars
               // we require a META-INF otherwise each subdirectory would be a subdeployment
               if  (context.isTopLevel() == false)
               {
                  try
                  {
                     root.findChild("META-INF");
                     log.trace("... ok - non top level directory has a META-INF subdirectory");
                  }
                  catch (IOException e)
                  {
                     log.trace("... no - doesn't look like a jar and no META-INF subdirectory.");
                     return false;
                  }
               }
               else
               {
                  log.trace("... ok - doesn't look like a jar but it is a top level directory.");
               }
            }
            else
            {
               log.trace("... ok - its an archive or at least pretending to be.");
            }

            // The metadata path is META-INF
            context.setMetaDataPath("META-INF");

            // The classpath is the root
            List<VirtualFile> paths = new ArrayList<VirtualFile>();
            paths.add(root);
            // Add the manifest locations
            VFSUtils.addManifestLocations(root, paths);
            context.setClassPath(paths);
            
            // We tentatively try all the children as potential subdeployments
            // but ignore subdirectories if it is an archive
            addAllChildren(context, root.isArchive());
            
            return true;
         }
         else
         {
            log.trace("... no - not a directory or an archive.");
            return false;
         }
      }
      catch (Exception e)
      {
         log.warn("Error determining structure: " + context.getName(), e);
         return false;
      }
   }
}
