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
package org.jboss.deployers.plugins.structure.vfs.war;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jboss.deployers.plugins.structure.vfs.AbstractStructureDeployer;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.virtual.VFSUtils;
import org.jboss.virtual.VirtualFile;

/**
 * WARStructure.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class WARStructure extends AbstractStructureDeployer
{
   @Override
   public int getRelativeOrder()
   {
      return 1000;
   }

   public boolean determineStructure(DeploymentContext context)
   {
      try
      {
         VirtualFile root = context.getRoot();
         if (root.isDirectory())
         {
            // We require either a WEB-INF or the name ends in .war
            if (root.getName().endsWith(".war") == false)
            {
               try
               {
                  root.findChild("WEB-INF");
                  log.trace("... ok - directory has a WEB-INF subdirectory");
               }
               catch (IOException e)
               {
                  log.trace("... no - doesn't look like a war and no WEB-INF subdirectory.");
                  return false;
               }
            }
            else
            {
               log.trace("... ok - name ends in .war.");
            }

            // The metadata path is WEB-INF
            context.setMetaDataPath("WEB-INF");

            List<VirtualFile> paths = new ArrayList<VirtualFile>();
            VirtualFile webinf = context.getMetaDataLocation();
            if (webinf != null)
            {
               // The classpath is WEB-INF/classes
               try
               {
                  VirtualFile webinfClasses = webinf.findChild("classes");
                  paths.add(webinfClasses);
               }
               catch (IOException ignored)
               {
                  log.trace("No WEB-INF/classes for: " + root.getPathName());
               }
               // and the top level jars in WEB-INF/lib
               try
               {
                  VirtualFile webinfLib = webinf.findChild("lib");
                  List<VirtualFile> archives = webinfLib.getChildren(WebInfLibFilter.INSTANCE);
                  for (VirtualFile archive : archives)
                     paths.add(archive);
               }
               catch (IOException ignored)
               {
                  log.trace("No WEB-INF/lib for: " + root.getPathName());
               }
            }
            // Add the manifest locations
            VFSUtils.addManifestLocations(root, paths);
            context.setClassPath(paths);

            // There are no subdeployments for wars
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
