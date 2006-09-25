/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.vfs.bundle;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;
import org.osgi.framework.AdminPermission;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

/**
 * An implementation of the OSGi bundle interface that uses a vfs to access the bundle contents.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class VFSBundle implements Bundle
{
   private VFS vfs;
   private String vfsPath;
   private long bundleID;
   /** Is this an extension bundle */
   private boolean extensionBundle;
   /** Bundle headers - how to handle localization */
   private Hashtable<String, String> headers = new Hashtable<String, String>();
   /** Constants.BUNDLE_CATEGORY = Bundle-Category */
   private String category;
   /** Constants.BUNDLE_CLASSPATH  Bundle-ClassPath */
   private ArrayList<String> classpath;
   /** Constants.BUNDLE_DESCRIPTION = "Bundle-Description" */
   private String description;

   /**
    * Create a bundle from a bundle path in the given VFS.
    * 
    * @param vfs - the VFS holding the bundle
    * @param vfsPath - the path in the VFS for the bundle root
    * @param bundleID the bundle id
    */
   public VFSBundle(VFS vfs, String vfsPath, long bundleID)
   {
      this.vfs = vfs;
      this.vfsPath = vfsPath;
      this.bundleID = bundleID;
   }

   // -------- Begin Bundle implementation
   /*
    * (non-Javadoc)
    * 
    * @see org.osgi.framework.Bundle#findEntries(java.lang.String,
    *      java.lang.String, boolean)
    */
   public Enumeration findEntries(String path, String filePattern,
         boolean recurse)
   {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * Get the bundle id
    * 
    * @see org.osgi.framework.Bundle#getBundleId()
    */
   public long getBundleId()
   {
      return bundleID;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.osgi.framework.Bundle#getEntry(java.lang.String)
    */
   public URL getEntry(String name)
   {
      // TODO Auto-generated method stub
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.osgi.framework.Bundle#getEntryPaths(java.lang.String)
    */
   public Enumeration getEntryPaths(String path)
   {
      // TODO Auto-generated method stub
      return null;
   }

   /*
    * Return the manifest headers
    * 
    * @see org.osgi.framework.Bundle#getHeaders()
    */
   public Dictionary getHeaders()
   {
      if( System.getSecurityManager() != null )
      {
         AdminPermission p = new AdminPermission(this, "metadata");
         AccessController.checkPermission(p);
      }
      return headers;
   }

   /**
    * Get the category.
    * 
    * @return the category.
    */
   public String getCategory()
   {
      return category;
   }

   /**
    * Get the classpath.
    * 
    * @return the classpath.
    */
   public ArrayList<String> getClasspath()
   {
      return classpath;
   }

   /**
    * Get the description.
    * 
    * @return the description.
    */
   public String getDescription()
   {
      return description;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.osgi.framework.Bundle#getHeaders(java.lang.String)
    */
   public Dictionary getHeaders(String locale)
   {
      // TODO Auto-generated method stub
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.osgi.framework.Bundle#getLastModified()
    */
   public long getLastModified()
   {
      // TODO Auto-generated method stub
      return 0;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.osgi.framework.Bundle#getLocation()
    */
   public String getLocation()
   {
      // TODO Auto-generated method stub
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.osgi.framework.Bundle#getRegisteredServices()
    */
   public ServiceReference[] getRegisteredServices()
   {
      // TODO Auto-generated method stub
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.osgi.framework.Bundle#getResource(java.lang.String)
    */
   public URL getResource(String name)
   {
      // TODO Auto-generated method stub
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.osgi.framework.Bundle#getResources(java.lang.String)
    */
   public Enumeration getResources(String name) throws IOException
   {
      // TODO Auto-generated method stub
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.osgi.framework.Bundle#getServicesInUse()
    */
   public ServiceReference[] getServicesInUse()
   {
      // TODO Auto-generated method stub
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.osgi.framework.Bundle#getState()
    */
   public int getState()
   {
      // TODO Auto-generated method stub
      return 0;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.osgi.framework.Bundle#getSymbolicName()
    */
   public String getSymbolicName()
   {
      // TODO Auto-generated method stub
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.osgi.framework.Bundle#hasPermission(java.lang.Object)
    */
   public boolean hasPermission(Object permission)
   {
      // TODO Auto-generated method stub
      return false;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.osgi.framework.Bundle#loadClass(java.lang.String)
    */
   public Class loadClass(String name) throws ClassNotFoundException
   {
      if( System.getSecurityManager() != null )
      {
         AdminPermission p = new AdminPermission(this, "class");
         AccessController.checkPermission(p);
      }
      // TODO Auto-generated method stub
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.osgi.framework.Bundle#start()
    */
   public void start() throws BundleException
   {
      if( System.getSecurityManager() != null )
      {
         AdminPermission p = new AdminPermission(this, "execute");
         AccessController.checkPermission(p);
      }
      // TODO Auto-generated method stub

   }

   /*
    * (non-Javadoc)
    * 
    * @see org.osgi.framework.Bundle#stop()
    */
   public void stop() throws BundleException
   {
      if( System.getSecurityManager() != null )
      {
         AdminPermission p = new AdminPermission(this, "execute");
         AccessController.checkPermission(p);
      }
      // TODO Auto-generated method stub

   }

   /*
    * (non-Javadoc)
    * 
    * @see org.osgi.framework.Bundle#uninstall()
    */
   public void uninstall() throws BundleException
   {
      if( System.getSecurityManager() != null )
      {
         AdminPermission p = new AdminPermission(this, extensionBundle ? "extensionLifecycle" : "lifecycle");
         AccessController.checkPermission(p);
      }
      // TODO Auto-generated method stub

   }

   /**
    * Update the bundle from the vfspath
    * @see org.osgi.framework.Bundle#update()
    */
   public void update() throws BundleException
   {
      if( System.getSecurityManager() != null )
      {
         AdminPermission p = new AdminPermission(this, "extensionLifecycle");
         AccessController.checkPermission(p);
      }

      try
      {
         VirtualFile bundleFile = vfs.findChildFromRoot(this.vfsPath);
         update(bundleFile);
      }
      catch(IOException e)
      {
         throw new BundleException("Failed to update from vfsPath: "+vfsPath, e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.osgi.framework.Bundle#update(java.io.InputStream)
    */
   public void update(InputStream in) throws BundleException
   {
      try
      {
         JarInputStream jis = new JarInputStream(in);
         Manifest mf = jis.getManifest();
         Attributes attrs = mf.getMainAttributes();
         Iterator<Entry<Object,Object>> iter = attrs.entrySet().iterator();
         while( iter.hasNext() )
         {
            Entry<Object,Object> entry = iter.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            String lkey = key.toLowerCase();
            headers.put(lkey, value);
         }
         jis.close();
         // Validate the bundle headers
         // TODO: Need to parse the values to extract the header attributes
         extractImports();
         extractExports();
      }
      catch(IOException e)
      {
         throw new BundleException("", e);
      }
   }

   // -------- End Bundle implementation
   public void update(VirtualFile bundleFile)
      throws IOException, BundleException
   {
      InputStream is = bundleFile.openStream();
      update(is);
   }

   protected void validateFragmentHost()
   {
      String key = Constants.FRAGMENT_HOST.toLowerCase();
      headers.get(key); // TODO what?
      
   }
   /**
    Import-Package ::= import ( ',' import )*
    import ::= package-names ( ';' parameter )*
    package-names ::= package-name ( ';' package-name )*
    */
   protected void extractImports()
   {
      String key = Constants.IMPORT_PACKAGE;
      headers.get(key); // TODO what?
      new HeaderValue.PkgInfo(); // TODO what?
   }
   protected void extractExports()
   {
      
   }

}
