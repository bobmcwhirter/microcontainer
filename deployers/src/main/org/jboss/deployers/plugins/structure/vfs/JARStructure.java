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

import org.jboss.deployers.spi.structure.vfs.VFSDeploymentContext;
import org.jboss.vfs.spi.VirtualFile;

/**
 * JARStructure.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class JARStructure extends AbstractStructureDeployer
{
   public boolean determineStructure(VFSDeploymentContext context)
   {
      try
      {
         VirtualFile file = context.getRoot();
         if (file.isDirectory())
         {
            if (file.isArchive() == false)
            {
               // For non top level directories we require a META-INF
               // otherwise each subdirectory would be a subdeployment
               if  (context.isTopLevel() == false)
               {
                  try
                  {
                     VirtualFile test = file.findChild("META-INF");
                     if (test == null)
                        return false;
                  }
                  catch (IOException e)
                  {
                     return false;
                  }
               }
            }

            // The metadata path is META-INF
            context.setMetaDataPath("META-INF");

            // We tentatively try all the children as potential subdeployments
            // but ignore subdirectories if it is an archive
            addAllChildren(context, file.isArchive());
            
            return true;
         }
         else
         {
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
