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
package org.jboss.test.kernel.dependency.test;

import java.util.ArrayList;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractCallbackMetaData;
import org.jboss.beans.metadata.plugins.InstallCallbackMetaData;
import org.jboss.beans.metadata.plugins.UninstallCallbackMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.CallbackMetaData;
import org.jboss.beans.metadata.spi.factory.GenericBeanFactoryMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.kernel.dependency.support.GenericBeanFactoryRepositoryBF;
import org.jboss.test.kernel.dependency.support.SimpleBeanImpl;

/**
 * Callback Dependency Test Case.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class GenericBeanFactoryCallbackDependencyTestCase extends AbstractKernelDependencyTest
{
   public static Test suite()
   {
      return suite(GenericBeanFactoryCallbackDependencyTestCase.class);
   }

   public GenericBeanFactoryCallbackDependencyTestCase(String name) throws Throwable
   {
      super(name);
   }

   public GenericBeanFactoryCallbackDependencyTestCase(String name, boolean xmltest) throws Throwable
   {
      super(name, xmltest);
   }

   public void testInstallDependencyCorrectOrder() throws Throwable
   {
      setupBeanMetaDatas();

      ControllerContext context1 = assertInstall(0, "Name1");
      ControllerContext context2 = assertInstall(1, "Name2");

      GenericBeanFactoryRepositoryBF bean1 = (GenericBeanFactoryRepositoryBF) context1.getTarget();
      assertNotNull(bean1);

      SimpleBeanImpl bean2 = (SimpleBeanImpl) context2.getTarget();
      assertNotNull(bean2);

      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add(bean2);
      assertEquals(expected, bean1.getBeans());
   }

   public void testInstallDependencyWrongOrder() throws Throwable
   {
      setupBeanMetaDatas();

      ControllerContext context2 = assertInstall(1, "Name2");
      ControllerContext context1 = assertInstall(0, "Name1");

      GenericBeanFactoryRepositoryBF bean1 = (GenericBeanFactoryRepositoryBF) context1.getTarget();
      assertNotNull(bean1);

      SimpleBeanImpl bean2 = (SimpleBeanImpl) context2.getTarget();
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

      GenericBeanFactoryRepositoryBF bean1 = (GenericBeanFactoryRepositoryBF) context1.getTarget();
      assertNotNull(bean1);

      SimpleBeanImpl bean2 = (SimpleBeanImpl) context2.getTarget();
      assertNotNull(bean2);

      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add(bean2);
      assertEquals(expected, bean1.getBeans());

      assertUninstall("Name1");
      assertContext("Name2");

      expected = new ArrayList<Object>();
      assertEquals(expected, bean1.getBeans());

      context1 = assertInstall(0, "Name1");
      assertContext("Name2", ControllerState.INSTALLED);

      bean1 = (GenericBeanFactoryRepositoryBF) context1.getTarget();
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

      bean2 = (SimpleBeanImpl) context2.getTarget();
      assertNotNull(bean2);

      expected = new ArrayList<Object>();
      expected.add(bean2);
      assertEquals(expected, bean1.getBeans());
   }

   protected void setupBeanMetaDatas() throws Throwable
   {
      GenericBeanFactoryMetaData metaData1 = new GenericBeanFactoryMetaData("Name1", SimpleBeanImpl.class.getName());
      metaData1.setFactoryClass(GenericBeanFactoryRepositoryBF.class.getName());

      ArrayList<CallbackMetaData> installs = new ArrayList<CallbackMetaData>();
      AbstractCallbackMetaData install = new InstallCallbackMetaData();
      install.setMethodName("addGenericBeanFactory");
      installs.add(install);
      metaData1.setInstallCallbacks(installs);

      ArrayList<CallbackMetaData> uninstalls = new ArrayList<CallbackMetaData>();
      AbstractCallbackMetaData uninstall = new UninstallCallbackMetaData();
      uninstall.setMethodName("removeGenericBeanFactory");
      uninstalls.add(uninstall);
      metaData1.setUninstallCallbacks(uninstalls);

      AbstractBeanMetaData metaData2 = new AbstractBeanMetaData("Name2", SimpleBeanImpl.class.getName());
      setBeanMetaDatas(new BeanMetaData[] { getBeanMetaData(metaData1), metaData2 });
   }
}
