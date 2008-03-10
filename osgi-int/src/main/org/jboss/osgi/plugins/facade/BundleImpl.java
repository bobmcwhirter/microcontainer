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
package org.jboss.osgi.plugins.facade;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Permission;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Locale;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.deployers.client.spi.DeployerClient;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentStages;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentUnit;
import org.jboss.logging.Logger;
import org.jboss.osgi.plugins.facade.helpers.BundleEntryHelper;
import org.jboss.osgi.plugins.facade.helpers.BundleHeaders;
import org.jboss.osgi.plugins.facade.helpers.DeploymentStage2BundleStateMapper;
import org.jboss.virtual.VirtualFile;
import org.osgi.framework.AdminPermission;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;

/**
 * Bundle implementation on top of existing DeploymentContext.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="mailto:baileyje@gmail.com">John Bailey</a>
 */
public class BundleImpl implements Bundle
{
   /** The log */
   private static final Logger log = Logger.getLogger(BundleImpl.class);

   protected DeploymentUnit unit;

   protected BundleHeaders bundleHeaders;

   private long lastModified = System.currentTimeMillis();

   /**
    * Create a new BundleImpl.
    * 
    * @param unit the DeploymentUnit
    */
   public BundleImpl(DeploymentUnit unit)
   {
      this.unit = unit;

   }

   /**
    *   Get Bundle state based on the current DeployentStage
    *   
    *   @return the Bundle's state
    */
   public int getState()
   {
      try
      {
         return DeploymentStage2BundleStateMapper.mapBundleState(unit.getMainDeployer().getDeploymentStage(unit.getName())).getState();
      }
      catch (DeploymentException e)
      {
         log.error("Unable to get DeploymentStage for DeploymentUnit " + unit.getName(), e);
         return Bundle.INSTALLED;
      }
   }

   /**
    * Start the Bundle
    */
   public void start() throws BundleException
   {
      checkPermission(AdminPermission.EXECUTE);
      try
      {
         DeployerClient main = unit.getMainDeployer();
         main.change(unit.getName(), DeploymentStages.INSTALLED);
      }
      catch (DeploymentException e)
      {
         throw new BundleException("Failed to start Bundle", e);
      }
   }

   public void stop() throws BundleException
   {
      checkPermission(AdminPermission.EXECUTE);
      try
      {
         DeployerClient main = unit.getMainDeployer();
         main.change(unit.getName(), DeploymentStages.DESCRIBE);
      }
      catch (DeploymentException e)
      {
         throw new BundleException("Failed to stop Bundle", e);
      }
   }

   public void update() throws BundleException
   {
      checkPermission(AdminPermission.LIFECYCLE);
   }

   public void update(InputStream inputStream) throws BundleException
   {
      checkPermission(AdminPermission.LIFECYCLE);
   }

   public void uninstall() throws BundleException
   {
      checkPermission(AdminPermission.LIFECYCLE);
      try
      {
         DeployerClient main = unit.getMainDeployer();
         main.change(unit.getName(), DeploymentStages.NOT_INSTALLED);
      }
      catch (DeploymentException e)
      {
         throw new BundleException("Failed to uninstall Bundle", e);
      }
   }

   public long getBundleId()
   {
      checkPermission(AdminPermission.METADATA);
      return unit.getName().hashCode();
   }

   public String getLocation()
   {
      checkPermission(AdminPermission.METADATA);
      if (unit instanceof VFSDeploymentUnit)
      {
         VFSDeploymentUnit vfsUnit = VFSDeploymentUnit.class.cast(unit);
         VirtualFile file = vfsUnit.getRoot();
         return file.getPathName();
      }
      return null;  // TODO What if the DeploymentUnit is not VFS
   }

   public ServiceReference[] getRegisteredServices()
   {
      return new ServiceReference[0];
   }

   public ServiceReference[] getServicesInUse()
   {
      return new ServiceReference[0];
   }

   public boolean hasPermission(Object permission)
   {
      if (permission instanceof Permission)
      {
         try
         {
            //  TODO check if bundle implies the permission
         }
         catch (SecurityException se)
         {
            return false;
         }
      }
      return true;
   }

   /**
    * Get the Bundle's headers
    * 
    * @return the Bundle's headers
    */
   @SuppressWarnings("unchecked")
   public Dictionary getHeaders()
   {
      return getHeaders(Locale.getDefault().toString());
   }

   @SuppressWarnings("unchecked")
   public Dictionary getHeaders(String locale)
   {
      checkPermission(AdminPermission.METADATA);
      if (bundleHeaders == null)
      {
         bundleHeaders = new BundleHeaders(unit);
      }
      return bundleHeaders.toDictionary();
   }

   /**
    * Get the Bundle's symbolic name
    * 
    * @return the Bundle's symbolic name
    */
   public String getSymbolicName()
   {
      return (String) getControllerContext().getName();
   }

   /**
    * Load class from Bundles classloader
    * 
    * @param name a class name
    * @return the class
    * @throws ClassNotFoundException If the class can not be loaded by DeploymentUnit ClassLoader
    * @throws IllegalStateException If the DeploymentUnit is NOT_INSTALLED
    * @throws IllegalStateException If the DeploymentUnit classloader is not set
    */
   public Class<?> loadClass(String name) throws ClassNotFoundException
   {
      checkPermission(AdminPermission.CLASS);
      checkForUninstalledDeploymentUnit();
      return unit.getClassLoader().loadClass(name);

   }

   /**
    * Get resource from Bundle classloader
    * 
    * @param name a resource name
    * @return URL URL to resource
    */
   public URL getResource(String name)
   {
      checkPermission(AdminPermission.RESOURCE);
      checkForUninstalledDeploymentUnit();
      return unit.getClassLoader().getResource(name); // TODO Should it propagate the IllegalStateException or trap and return null?
   }

   /**
    * Get resources from bundle classloader
    * 
    * @param name a resource name
    * @return Enumeration of URLs to resources
    */
   @SuppressWarnings("unchecked")
   public Enumeration getResources(String name) throws IOException
   {
      checkPermission(AdminPermission.RESOURCE);
      checkForUninstalledDeploymentUnit();
      return unit.getClassLoader().getResources(name); // TODO Should it propagate the IllegalStateException or trap and return null?
   }

   /**
    *  Get paths to entries in Bundle for a given directory
    *  
    *  @param name name of entry
    *  @return Enumeration of URLs to entries in Bundle
    */
   @SuppressWarnings("unchecked")
   public Enumeration getEntryPaths(String dirPath)
   {
      checkPermission(AdminPermission.RESOURCE);
      return BundleEntryHelper.getEntryPaths(unit, dirPath);
   }

   /**
    * Get an entry from the bundle
    * 
    * @param path path to entry
    * @return URL to entry
    */
   public URL getEntry(String path)
   {
      checkPermission(AdminPermission.RESOURCE);
      return BundleEntryHelper.getEntry(unit, path);
   }

   public long getLastModified()
   {
      return lastModified;
   }

   /**
    * Search Bundle for entries 
    * 
    * @param path base path in Bundle
    * @param filePattern pattern used to select files
    * @param recurse should search recurse directories
    * @return Enumeration of URLs to matched entries
    */
   @SuppressWarnings("unchecked")
   public Enumeration findEntries(String path, String filePattern, boolean recurse)
   {
      checkPermission(AdminPermission.RESOURCE);
      return BundleEntryHelper.findEntries(unit, path, filePattern, recurse);
   }

   /**
    * Returns the ControllerContext for the DeploymentUnit
    * 
    * @return the ControllerContext
    */
   private ControllerContext getControllerContext()
   {
      return unit.getAttachment(ControllerContext.class.getName(), ControllerContext.class);
   }

   /** 
    * Checks administrative permissions
    * 
    * @param adminPermission the permission to check
    */
   private void checkPermission(String adminPermission)
   {
      if (System.getSecurityManager() != null)
      {
         System.getSecurityManager().checkPermission(new AdminPermission(this, adminPermission));
      }
   }

   /**
    * Checks to see if the DeploymentUnit has been uninstalled.  
    *
    * @throws IllegalStateException If there is a problem determining the DeploymentUnitState
    */
   private void checkForUninstalledDeploymentUnit()
   {
      try
      {
         if (DeploymentStages.NOT_INSTALLED.equals(unit.getMainDeployer().getDeploymentStage(unit.getName())))
         {
            throw new IllegalStateException("Bundle has been uninstalled");
         }
      }
      catch (DeploymentException e)
      {
         throw new IllegalStateException("Failed to determine current DeploymentStage for Deployment: "
               + unit.getName(), e);
      }
   }
}
