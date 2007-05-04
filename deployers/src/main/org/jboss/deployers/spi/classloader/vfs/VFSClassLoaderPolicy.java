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
package org.jboss.deployers.spi.classloader.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.Set;
import java.util.jar.Manifest;

import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.PackageInformation;
import org.jboss.logging.Logger;
import org.jboss.virtual.VFSUtils;
import org.jboss.virtual.VirtualFile;

/**
 * VFSClassLoaderPolicy.
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class VFSClassLoaderPolicy extends ClassLoaderPolicy
{
   /** The log */
   private static Logger log = Logger.getLogger(VFSClassLoaderPolicy.class);
   
   /** The roots */
   private VirtualFile[] roots;

   /** Whether to export all */
   private ExportAll exportAll;
   
   /** The exported packages */
   private String[] exportedPackages;
   
   /**
    * Create a new VFSClassLoaderPolicy.
    * 
    * @param roots the roots
    * @return the classloader policy
    * @throws IllegalArgumentException for null roots
    */
   public static VFSClassLoaderPolicy createVFSClassLoaderPolicy(VirtualFile... roots)
   {
      return new VFSClassLoaderPolicy(roots);
   }

   /**
    * Create a new VFSClassLoaderPolicy.
    * 
    * @param roots the roots
    * @throws IllegalArgumentException for null roots
    */
   public VFSClassLoaderPolicy(VirtualFile[] roots)
   {
      if (roots == null)
         throw new IllegalArgumentException("Null roots");
      for (VirtualFile root : roots)
      {
         if (root == null)
            throw new IllegalArgumentException("Null root in " + Arrays.asList(roots));
      }

      this.roots = roots;
   }

   /**
    * Get the exportAll.
    * 
    * @return the exportAll.
    */
   public ExportAll isExportAll()
   {
      return exportAll;
   }

   /**
    * Set the exportAll.
    * 
    * @param exportAll the exportAll.
    */
   public void setExportAll(ExportAll exportAll)
   {
      this.exportAll = exportAll;
      if (exportAll != null)
         exportedPackages = determineAllPackages().toArray(new String[0]);
      else
         exportedPackages = null;
   }

   @Override
   public String[] getPackageNames()
   {
      return exportedPackages;
   }

   /**
    * Set the exportedPackages.
    * 
    * @param exportedPackages the exportedPackages.
    */
   public void setExportedPackages(String[] exportedPackages)
   {
      this.exportedPackages = exportedPackages;
   }

   @Override
   public URL getResource(String path)
   {
      VirtualFile child = findChild(path);
      if (child != null)
      {
         try
         {
            return child.toURL();
         }
         catch (Exception ignored)
         {
            log.trace("Error determining URL for " + child, ignored);
            return null;
         }
      }
      return null;
   }

   @Override
   public InputStream getResourceAsStream(String path)
   {
      VirtualFile child = findChild(path);
      if (child != null)
      {
         try
         {
            return child.openStream();
         }
         catch (Exception ignored)
         {
            log.trace("Error opening stream for " + child, ignored);
            return null;
         }
      }
      return null;
   }

   @Override
   public void getResources(String name, Set<URL> urls) throws IOException
   {
      for (VirtualFile root : roots)
      {
         try
         {
            VirtualFile child = root.findChild(name);
            urls.add(child.toURL());
         }
         catch (Exception ignored)
         {
            log.trace("Error getting resources for " + root, ignored);
         }
      }
   }

   /**
    * Find a child from a path
    * 
    * @param path the path
    * @return the child if found in the roots
    */
   protected VirtualFile findChild(String path)
   {
      for (VirtualFile root : roots)
      {
         try
         {
            return root.findChild(path);
         }
         catch (Exception ignored)
         {
            // not found
         }
      }
      return null;
   }
   
   @Override
   public PackageInformation getPackageInformation(String packageName)
   {
      String path = packageName.replace('.', '/');
      VirtualFile pkge = findChild(path);
      if (pkge == null)
         return null;
      try
      {
         Manifest manifest = VFSUtils.getManifest(pkge.getVFS());
         return new PackageInformation(packageName, manifest);
      }
      catch (IOException ignored)
      {
         log.trace("Unable to retrieve manifest for " + pkge + " " + ignored.getMessage());
         return null;
      }
   }

   @Override
   protected ProtectionDomain getProtectionDomain(String className, String path)
   {
      VirtualFile clazz = findChild(path);
      if (clazz == null)
      {
         log.trace("Unable to determine class file for " + className);
         return null;
      }
      try
      {
         VirtualFile root = clazz.getVFS().getRoot();
         URL codeSourceURL = root.toURL();
         Certificate[] certs = null; // TODO determine certificates
         CodeSource cs = new CodeSource(codeSourceURL, certs);
         PermissionCollection permissions = Policy.getPolicy().getPermissions(cs);
         return new ProtectionDomain(cs, permissions);
      }
      catch (Exception e)
      {
         throw new Error("Error determining protection domain for " + clazz, e);
      }
   }

   /**
    * Determine all the packages
    * 
    * @return the packages
    * @throws IllegalArgumentException if there is no exportAll policy
    */
   protected Set<String> determineAllPackages()
   {
      PackageVisitor visitor = new PackageVisitor(exportAll);
      for (VirtualFile root : roots)
      {
         try
         {
            root.visit(visitor);
         }
         catch (Exception e)
         {
            throw new Error("Error visiting " + root, e);
         }
      }
      return visitor.getPackages();
   }
}