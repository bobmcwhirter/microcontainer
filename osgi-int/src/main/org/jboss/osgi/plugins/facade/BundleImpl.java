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
import org.jboss.dependency.spi.ControllerState;
import org.jboss.deployers.client.spi.DeployerClient;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentStages;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.logging.Logger;
import org.jboss.osgi.plugins.facade.helpers.BundleHeaders;
import org.jboss.osgi.plugins.facade.helpers.DeploymentStage2BundleStateMapper;
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

   private long lastModified = -1;

   /**
    * 
    * Create a new BundleImpl.
    * 
    * @param unit
    */
   public BundleImpl(DeploymentUnit unit)
   {
      this.unit = unit;

   }

   /**
    *   Get Bundle state based on the current DeployentStage
    */
   public int getState()
   {
      try
      {
         return DeploymentStage2BundleStateMapper.mapBundleState(
               unit.getMainDeployer().getDeploymentStage(unit.getName())).getState();
      }
      catch (DeploymentException e)
      {
         log.error("Unable to get DeploymentStage for DeploymentUnit " + unit.getName(), e);
         return Bundle.INSTALLED;
      }
   }

   /**
    * Deligate to the MainDeployer to start the Bundle
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
   }

   public long getBundleId()
   {
      checkPermission(AdminPermission.METADATA);
      return 1L;
   }

   public String getLocation()
   {
      checkPermission(AdminPermission.METADATA);
      return null;
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

   public String getSymbolicName()
   {
      return (String) getControllerContext().getName();
   }

   public Class<?> loadClass(String name) throws ClassNotFoundException
   {
      checkPermission(AdminPermission.CLASS);
      if (getControllerContext() != null)
      {
         //  Verify ControllerState required to load CL resources
         if (ControllerState.INSTANTIATED.equals(getControllerContext().getState()))
         {
            return unit.getClassLoader().loadClass(name);
         }
      }
      return null;
   }

   public URL getResource(String name)
   {
      checkPermission(AdminPermission.RESOURCE);

      if (getControllerContext() != null)
      {
         //  Verify ControllerState required to load CL resources
         if (ControllerState.INSTANTIATED.equals(getControllerContext().getState()))
         {
            return unit.getClassLoader().getResource(name);
         }
      }
      return null;
   }

   @SuppressWarnings("unchecked")
   public Enumeration getResources(String name) throws IOException
   {
      checkPermission(AdminPermission.RESOURCE);

      if (getControllerContext() != null)
      {
         //  Verify ControllerState required to load CL resources
         if (ControllerState.INSTANTIATED.equals(getControllerContext().getState()))
         {
            return unit.getClassLoader().getResources(name);
         }
      }
      return null;
   }

   @SuppressWarnings("unchecked")
   public Enumeration getEntryPaths(String string)
   {
      checkPermission(AdminPermission.RESOURCE);
      return null;
   }

   public URL getEntry(String string)
   {
      checkPermission(AdminPermission.RESOURCE);
      return null;
   }

   public long getLastModified()
   {
      //  TODO - Get a better scheme for this..   This should be set at INSTALL, UPDATE, UNSTALL
      if (lastModified == -1)
      {
         lastModified = System.currentTimeMillis();
      }
      return lastModified;
   }

   @SuppressWarnings("unchecked")
   public Enumeration findEntries(String string, String string1, boolean b)
   {
      checkPermission(AdminPermission.RESOURCE);

      return null;
   }

   private ControllerContext getControllerContext()
   {
      return unit.getAttachment(ControllerContext.class.getName(), ControllerContext.class);
   }

   private void checkPermission(String adminPermission)
   {
      if (System.getSecurityManager() != null)
      {
         System.getSecurityManager().checkPermission(new AdminPermission(this, adminPermission));
      }
   }
}
