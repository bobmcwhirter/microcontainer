/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, JBoss Inc., and individual contributors as indicated
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
package org.jboss.osgi.plugins.facade.helpers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentUnit;
import org.jboss.logging.Logger;
import org.jboss.virtual.VirtualFile;

/**
 * A BundleEntryHelper - Helper used to retrieve Bundle Entries from a VirtualFile 
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public class BundleEntryHelper
{

   /** The log */
   private static final Logger log = Logger.getLogger(BundleEntryHelper.class);

   /**
    *  Get paths to entries in DeploymentUnit for a given directory
    *  
    *  @param unit DeploymentUnit
    *  @param name name of entry
    *  @return Enumeration of URLs to entries in Bundle
    */
   @SuppressWarnings("unchecked")
   public static Enumeration getEntryPaths(DeploymentUnit unit, String dirPath)
   {
      VirtualFile file = getVirtualFile(unit, dirPath);
      if (file != null)
      {
         List<String> entryPaths = new ArrayList<String>();
         try
         {
            for (VirtualFile entry : file.getChildren())
            {
               entryPaths.add(entry.getPathName());
            }
            if (entryPaths.isEmpty() == false)
            {
               return Collections.enumeration(entryPaths);
            }
         }
         catch (IOException e)
         {
            if (log.isTraceEnabled())
            {
               log.trace("Error getting entry paths", e);
            }
         }
      }
      return null;
   }

   /**
    * Get an entry from the DeploymentUnit
    * 
    * @param path path to entry
    * @return URL to entry
    */
   public static URL getEntry(DeploymentUnit unit, String path)
   {
      try
      {
         VirtualFile file = getVirtualFile(unit, path);
         return file != null ? file.toURL() : null;
      }
      catch (Exception e)
      {
         if (log.isTraceEnabled())
         {
            log.trace("Error getting entry", e);
         }
      }
      return null;
   }

   /**
    * Search DeploymentUnit for entries 
    * 
    * @param path base path in Bundle
    * @param filePattern pattern used to select files
    * @param recurse should search recurse directories
    * @return Enumeration of URLs to matched entries
    */
   @SuppressWarnings("unchecked")
   public static Enumeration findEntries(DeploymentUnit unit, String path, String filePattern, boolean recurse)
   {
      VirtualFile file = getVirtualFile(unit, path);
      if (file != null)
      {
         BundleEntryVisitor bundleEntryVisitor = new BundleEntryVisitor(filePattern, recurse);
         try
         {
            file.visit(bundleEntryVisitor);
            List<URL> entries = bundleEntryVisitor.getEntries();
            if (entries.isEmpty() == false)
            {
               return Collections.enumeration(entries);
            }
            // TODO Consider finding entries from attached fragments (modules?).
         }
         catch (IOException e)
         {
            if(log.isTraceEnabled())
            {            
               log.trace("Error finding entries", e);
            }
         }
      }
      return null;
   }

   /**
    * Get the root VirtualFile from the DeploymentUnit
    * 
    * @param unit DeploymentUnit
    * @return root VirtualFile from DeploymentUnit
    */
   private static VirtualFile getVirtualFile(DeploymentUnit unit, String filePath)
   {
      VirtualFile returnFile = null;
      if (unit instanceof VFSDeploymentUnit)
      {
         VFSDeploymentUnit vfsUnit = VFSDeploymentUnit.class.cast(unit);
         returnFile = vfsUnit.getFile(filePath);
      }
      return returnFile;
   }

}
