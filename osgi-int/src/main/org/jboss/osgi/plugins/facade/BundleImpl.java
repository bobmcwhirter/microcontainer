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
package org.jboss.osgi.plugins.facade;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Permission;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Set;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.deployers.plugins.deployers.DeploymentControllerContext;
import org.jboss.deployers.spi.DeploymentState;
import org.jboss.deployers.structure.spi.DeploymentContext;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.osgi.spi.metadata.OSGiMetaData;
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
   protected DeploymentUnit unit;

   protected BundleHeaders bundleHeaders;

   private long lastModified = -1;

   public BundleImpl(DeploymentUnit unit)
   {
      this.unit = unit;
   }

   public int getState()
   {
      int bundleState = Bundle.UNINSTALLED;
      if (getControllerContext() != null)
      {
         ControllerState controllerState = getControllerContext().getState();
         if (ControllerState.ERROR.equals(controllerState))
         {
            bundleState = Bundle.INSTALLED; // Seems strange, but see javadoc 
         }
         else if (ControllerState.NOT_INSTALLED.equals(controllerState))
         {
            bundleState = Bundle.UNINSTALLED;
         }
         else if (ControllerState.PRE_INSTALL.equals(controllerState)
               || ControllerState.DESCRIBED.equals(controllerState))
         {
            bundleState = Bundle.INSTALLED;
         }
         else if (ControllerState.INSTANTIATED.equals(controllerState)
               || ControllerState.CONFIGURED.equals(controllerState) || ControllerState.CREATE.equals(controllerState))
         {
            bundleState = Bundle.RESOLVED;
         }
         else if (ControllerState.START.equals(controllerState))
         {
            bundleState = Bundle.STARTING;
         }
         else if (ControllerState.INSTALLED.equals(controllerState))
         {
            bundleState = Bundle.ACTIVE;
         }
      }
      return bundleState;
   }

   public void start() throws BundleException
   {
      checkPermission(AdminPermission.EXECUTE);

      DeploymentControllerContext deploymentControllerContext = getControllerContext();
      DeploymentContext deploymentContext = deploymentControllerContext.getDeploymentContext();

      try
      {
         deploymentControllerContext.getController().change(getControllerContext(), ControllerState.INSTALLED);
      }
      catch (Throwable t)
      {
         deploymentContext.setState(DeploymentState.ERROR);
         deploymentContext.setProblem(t);
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
      return getBundleHeaders().getHeaders(locale);
   }

   public String getSymbolicName()
   {
      if (getOSGIMetaData() != null)
      {
         return getOSGIMetaData().getBundleSymbolicName();
      }
      return null;
   }

   public Class<?> loadClass(String name) throws ClassNotFoundException
   {
      checkPermission(AdminPermission.CLASS);
      if (getControllerContext() != null)
      {
         //  Verify ControllerState required to load CL resources
         if (ControllerState.INSTANTIATED.equals(getControllerContext().getState()))
         {
            return getClassLoader().loadClass(name);
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
            return getClassLoader().getResource(name);
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
            return getClassLoader().getResources(name);
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

   private DeploymentControllerContext getControllerContext()
   {
      return unit.getAttachment(ControllerContext.class.getName(), DeploymentControllerContext.class);
   }

   private OSGiMetaData getOSGIMetaData()
   {
      OSGiMetaData metaData = null;
      Set<? extends OSGiMetaData> metaDatas = unit.getAllMetaData(OSGiMetaData.class);
      if (metaDatas != null && metaDatas.isEmpty() == false)
      {
         metaData = (OSGiMetaData) metaDatas.iterator().next();
      }
      return metaData;
   }

   private BundleHeaders getBundleHeaders()
   {
      if (bundleHeaders == null)
      {
         bundleHeaders = new BundleHeaders(getOSGIMetaData());
      }
      return bundleHeaders;
   }

   private ClassLoader getClassLoader()
   {
      ClassLoader classLoader = null;
      if (getControllerContext() != null)
      {
         classLoader = getControllerContext().getDeploymentContext().getClassLoader();
      }
      return classLoader;
   }

   private void checkPermission(String adminPermission)
   {
      if (System.getSecurityManager() != null)
      {
         System.getSecurityManager().checkPermission(new AdminPermission(this, adminPermission));
      }
   }

}
