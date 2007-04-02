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
import java.util.Dictionary;
import java.util.Enumeration;

import org.jboss.deployers.spi.structure.DeploymentContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;

/**
 * Bundle implementation on top of existing DeploymentContext.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class BundleImpl implements Bundle
{
   protected DeploymentContext context;

   public BundleImpl(DeploymentContext context)
   {
      this.context = context;
   }

   public int getState()
   {
      return 0;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public void start() throws BundleException
   {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public void stop() throws BundleException
   {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public void update() throws BundleException
   {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public void update(InputStream inputStream) throws BundleException
   {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public void uninstall() throws BundleException
   {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public Dictionary getHeaders()
   {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public long getBundleId()
   {
      return 0;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public String getLocation()
   {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public ServiceReference[] getRegisteredServices()
   {
      return new ServiceReference[0];  //To change body of implemented methods use File | Settings | File Templates.
   }

   public ServiceReference[] getServicesInUse()
   {
      return new ServiceReference[0];  //To change body of implemented methods use File | Settings | File Templates.
   }

   public boolean hasPermission(Object object)
   {
      return false;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public URL getResource(String string)
   {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public Dictionary getHeaders(String string)
   {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public String getSymbolicName()
   {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public Class loadClass(String string) throws ClassNotFoundException
   {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public Enumeration getResources(String string) throws IOException
   {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public Enumeration getEntryPaths(String string)
   {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public URL getEntry(String string)
   {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public long getLastModified()
   {
      return 0;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public Enumeration findEntries(String string, String string1, boolean b)
   {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }
}
