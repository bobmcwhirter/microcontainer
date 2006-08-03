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
package org.jboss.test.kernel.dependency.test;

import java.util.ArrayList;

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractInstallMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.test.kernel.dependency.support.SimpleBeanInstallSelf;

/**
 * Install Dependency Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class InstallSelfDependencyTestCase extends AbstractKernelDependencyTest
{
   public static Test suite()
   {
      return suite(InstallSelfDependencyTestCase.class);
   }
   
   public InstallSelfDependencyTestCase(String name) throws Throwable
   {
      super(name);
   }

   public InstallSelfDependencyTestCase(String name, boolean xmltest) throws Throwable
   {
      super(name, xmltest);
   }

   public void testInstallSelfDependencyCorrectOrder() throws Throwable
   {
      setupBeanMetaDatas();
      
      ControllerContext context = assertInstall(0, "Name1");
      
      SimpleBeanInstallSelf bean = (SimpleBeanInstallSelf) context.getTarget();
      assertNotNull(bean);
      assertTrue(bean.getInstalled());
   }

   public void testInstallSelfDependencyReinstall() throws Throwable
   {
      setupBeanMetaDatas();
      
      ControllerContext context = assertInstall(0, "Name1");
      
      SimpleBeanInstallSelf bean = (SimpleBeanInstallSelf) context.getTarget();
      assertNotNull(bean);
      assertTrue(bean.getInstalled());
      
      assertUninstall("Name1");
      assertFalse(bean.getInstalled());
      
      context = assertInstall(0, "Name1");
      
      bean = (SimpleBeanInstallSelf) context.getTarget();
      assertNotNull(bean);
      assertTrue(bean.getInstalled());
   }

   protected void setupBeanMetaDatas() throws Throwable
   {
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("Name1", SimpleBeanInstallSelf.class.getName());

      ArrayList<InstallMetaData> installs = new ArrayList<InstallMetaData>();
      AbstractInstallMetaData install = new AbstractInstallMetaData();
      install.setMethodName("install");
      installs.add(install);
      metaData1.setInstalls(installs);
      
      ArrayList<InstallMetaData> uninstalls = new ArrayList<InstallMetaData>();
      AbstractInstallMetaData uninstall = new AbstractInstallMetaData();
      uninstall.setMethodName("uninstall");
      uninstalls.add(uninstall);
      metaData1.setUninstalls(uninstalls);

      setBeanMetaDatas(new BeanMetaData[] { metaData1 });
   }
}