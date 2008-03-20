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
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.ThisValueMetaData;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactory;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.kernel.dependency.support.GenericBeanFactoryRepository;
import org.jboss.test.kernel.dependency.support.SimpleBeanImpl;

/**
 * Install Dependency Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class GenericBeanFactoryInstallDependencyTestCase extends AbstractKernelDependencyTest
{
   public static Test suite()
   {
      return suite(GenericBeanFactoryInstallDependencyTestCase.class);
   }
   
   public GenericBeanFactoryInstallDependencyTestCase(String name) throws Throwable
   {
      super(name);
   }

   public GenericBeanFactoryInstallDependencyTestCase(String name, boolean xmltest) throws Throwable
   {
      super(name, xmltest);
   }

   public void testInstallDependencyCorrectOrder() throws Throwable
   {
      setupBeanMetaDatas();
      
      ControllerContext context1 = assertInstall(0, "Name1");
      ControllerContext context2 = assertInstall(1, "Name2");
      
      GenericBeanFactoryRepository bean1 = (GenericBeanFactoryRepository) context1.getTarget();
      assertNotNull(bean1);
      
      GenericBeanFactory bean2 = (GenericBeanFactory) context2.getTarget();
      assertNotNull(bean2);
      
      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add(bean2);
      assertEquals(expected, bean1.getBeans());
   }

   public void testInstallDependencyWrongOrder() throws Throwable
   {
      setupBeanMetaDatas();
      
      ControllerContext context2 = assertInstall(1, "Name2", ControllerState.START);
      ControllerContext context1 = assertInstall(0, "Name1");
      
      GenericBeanFactoryRepository bean1 = (GenericBeanFactoryRepository) context1.getTarget();
      assertNotNull(bean1);
      
      GenericBeanFactory bean2 = (GenericBeanFactory) context2.getTarget();
      assertNotNull(bean2);
      
      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add(bean2);
      assertEquals(expected, bean1.getBeans());
   }

   public void testInstallDependencyReinstall() throws Throwable
   {
      setupBeanMetaDatas();
      
      ControllerContext context1 = assertInstall(0, "Name1");
      ControllerContext context2 = assertInstall(1, "Name2");
      
      GenericBeanFactoryRepository bean1 = (GenericBeanFactoryRepository) context1.getTarget();
      assertNotNull(bean1);
      
      GenericBeanFactory bean2 = (GenericBeanFactory) context2.getTarget();
      assertNotNull(bean2);
      
      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add(bean2);
      assertEquals(expected, bean1.getBeans());
      
      assertUninstall("Name1");
      assertContext("Name2", ControllerState.START);

      expected = new ArrayList<Object>();
      assertEquals(expected, bean1.getBeans());

      context1 = assertInstall(0, "Name1");
      assertContext("Name2", ControllerState.INSTALLED);
      
      bean1 = (GenericBeanFactoryRepository) context1.getTarget();
      assertNotNull(bean1);
      
      expected = new ArrayList<Object>();
      expected.add(bean2);
      assertEquals(expected, bean1.getBeans());
      
      assertUninstall("Name2");
      assertContext("Name1", ControllerState.INSTALLED);

      expected = new ArrayList<Object>();
      assertEquals(expected, bean1.getBeans());
      
      context2 = assertInstall(1, "Name2");
      assertContext("Name1", ControllerState.INSTALLED);
      
      bean2 = (GenericBeanFactory) context2.getTarget();
      assertNotNull(bean2);
      
      expected = new ArrayList<Object>();
      expected.add(bean2);
      assertEquals(expected, bean1.getBeans());
   }

   @SuppressWarnings("deprecation")
   protected void setupBeanMetaDatas() throws Throwable
   {
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("Name1", GenericBeanFactoryRepository.class.getName());
      
      org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData metaData2 = new org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData("Name2", SimpleBeanImpl.class.getName());

      ArrayList<InstallMetaData> installs = new ArrayList<InstallMetaData>();
      AbstractInstallMetaData install = new AbstractInstallMetaData();
      install.setBean("Name1");
      install.setMethodName("addGenericBeanFactory");
      ArrayList<ParameterMetaData> parameters = new ArrayList<ParameterMetaData>();
      parameters.add(new AbstractParameterMetaData(null, new ThisValueMetaData()));
      install.setParameters(parameters);
      installs.add(install);
      metaData2.setInstalls(installs);
      
      ArrayList<InstallMetaData> uninstalls = new ArrayList<InstallMetaData>();
      AbstractInstallMetaData uninstall = new AbstractInstallMetaData();
      uninstall.setBean("Name1");
      uninstall.setMethodName("removeGenericBeanFactory");
      parameters = new ArrayList<ParameterMetaData>();
      parameters.add(new AbstractParameterMetaData(null, new ThisValueMetaData()));
      uninstall.setParameters(parameters);
      uninstalls.add(uninstall);
      metaData2.setUninstalls(uninstalls);

      setBeanMetaDatas(new BeanMetaData[] { metaData1, metaData2 });
   }
}