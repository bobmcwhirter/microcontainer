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
package org.jboss.deployers.vfs.plugins.classloader;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.deployers.structure.spi.classloading.ExportAll;
import org.jboss.virtual.VirtualFile;
import org.jboss.virtual.VirtualFileVisitor;
import org.jboss.virtual.VisitorAttributes;

/**
 * Visits a virtual file system recursively
 * to determine package names based on the exportAll policy
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
class PackageVisitor implements VirtualFileVisitor
{
   /** The packages */
   private Set<String> packages = new HashSet<String>();
   
   /** The root */
   private String rootPath;
   
   /** The exportAll policy */
   private ExportAll exportAll;

   /**
    * Create a new PackageVisitor.
    *
    * @param exportAll the export all policy
    * @throws IllegalArgumentException for a null exportAll policy
    */
   public PackageVisitor(ExportAll exportAll)
   {
      if (exportAll == null)
         throw new IllegalArgumentException("Null exportAll policy");
      this.exportAll = exportAll;
   }

   /**
    * Set the root
    * 
    * @param root the root
    * @throws IllegalArgumentException for a null root
    */
   public void setRoot(VirtualFile root)
   {
      if (root == null)
         throw new IllegalArgumentException("Null root");
      rootPath = root.getPathName() + "/";
   }
   
   /**
    * Get the packages.
    * 
    * @return the packages.
    */
   public Set<String> getPackages()
   {
      return packages;
   }

   public VisitorAttributes getAttributes()
   {
      VisitorAttributes attributes = new VisitorAttributes();
      attributes.setIncludeRoot(true);
      attributes.setRecurseFilter(VisitorAttributes.RECURSE_ALL);
      return attributes;
   }
   
   public void visit(VirtualFile file)
   {
      try
      {
         // We only want only directories
         if (file.isLeaf() == false)
         {
            boolean empty = true;
            // Include empty directories?
            if (exportAll == ExportAll.ALL)
               empty = false;
            else
            {
               // Determine whether there is anything there
               List<VirtualFile> children = file.getChildren();
               if (children != null && children.isEmpty() == false)
               {
                  for (VirtualFile child : children)
                  {
                     // We must have a leaf to be non-empty
                     if (child.isLeaf())
                     {
                        empty = false;
                        break;
                     }
                  }
               }
            }
            // This looks interesting
            if (empty == false)
            {
               String path = file.getPathName();
               if (path.startsWith(rootPath))
                  path = path.substring(rootPath.length());
               packages.add(path.replace('/', '.'));
            }
         }
      }
      catch (IOException e)
      {
         throw new Error("Error visiting " + file, e);
      }
   }
}