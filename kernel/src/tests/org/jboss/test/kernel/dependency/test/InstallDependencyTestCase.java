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
import java.util.Collections;
import java.util.List;
import java.util.Set;

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractInstallMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.ThisValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractInjectionValueMetaData;
import org.jboss.beans.metadata.api.enums.FromContext;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.kernel.dependency.support.SimpleBean;
import org.jboss.test.kernel.dependency.support.SimpleBeanImpl;
import org.jboss.test.kernel.dependency.support.SimpleBeanRepository;

/**
 * Install Dependency Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class InstallDependencyTestCase extends AbstractKernelDependencyTest
{
   public static Test suite()
   {
      return suite(InstallDependencyTestCase.class);
   }
   
   public InstallDependencyTestCase(String name) throws Throwable
   {
      super(name);
   }

   public InstallDependencyTestCase(String name, boolean xmltest) throws Throwable
   {
      super(name, xmltest);
   }

   public void testInstallDependencyCorrectOrder() throws Throwable
   {
      setupBeanMetaDatas();
      
      ControllerContext context1 = assertInstall(0, "Name1");
      ControllerContext context2 = assertInstall(1, "Name2");
      
      SimpleBeanRepository bean1 = (SimpleBeanRepository) context1.getTarget();
      assertNotNull(bean1);
      
      SimpleBean bean2 = (SimpleBean) context2.getTarget();
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
      
      SimpleBeanRepository bean1 = (SimpleBeanRepository) context1.getTarget();
      assertNotNull(bean1);
      
      SimpleBean bean2 = (SimpleBean) context2.getTarget();
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
      
      SimpleBeanRepository bean1 = (SimpleBeanRepository) context1.getTarget();
      assertNotNull(bean1);
      
      SimpleBean bean2 = (SimpleBean) context2.getTarget();
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
      
      bean1 = (SimpleBeanRepository) context1.getTarget();
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
      
      bean2 = (SimpleBean) context2.getTarget();
      assertNotNull(bean2);
      
      expected = new ArrayList<Object>();
      expected.add(bean2);
      assertEquals(expected, bean1.getBeans());
   }

   public void testWhenRequiredInstalls() throws Throwable
   {
      setupBeanMetaDatas();

      ControllerContext context1 = assertInstall(0, "Name1");
      ControllerContext context2 = assertInstall(2, "Name3");

      SimpleBeanRepository bean1 = (SimpleBeanRepository) context1.getTarget();
      assertNotNull(bean1);
      Set<ControllerState> states = bean1.getStates();
      assertNotNull(states);
      assertEquals(getExpectedInstallSize(), states.size());

      assertUninstall(context2.getName().toString());

      int uninstallSize = getExpectedUninstallSize();
      assertEquals(uninstallSize, states.size());
      if (uninstallSize > 0)
      {
         ControllerState state = states.iterator().next();
         assertEquals(ControllerState.NOT_INSTALLED, state);
      }
   }

   protected int getExpectedInstallSize()
   {
      return 6;
   }

   protected int getExpectedUninstallSize()
   {
      return 1;
   }

   protected void setupBeanMetaDatas() throws Throwable
   {
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("Name1", SimpleBeanRepository.class.getName());
      
      AbstractBeanMetaData metaData2 = new AbstractBeanMetaData("Name2", SimpleBeanImpl.class.getName());

      ArrayList<InstallMetaData> installs = new ArrayList<InstallMetaData>();
      AbstractInstallMetaData install = new AbstractInstallMetaData();
      install.setBean("Name1");
      install.setMethodName("addSimpleBean");
      ArrayList<ParameterMetaData> parameters = new ArrayList<ParameterMetaData>();
      parameters.add(new AbstractParameterMetaData(null, new ThisValueMetaData()));
      install.setParameters(parameters);
      installs.add(install);
      metaData2.setInstalls(installs);

      ArrayList<InstallMetaData> uninstalls = new ArrayList<InstallMetaData>();
      AbstractInstallMetaData uninstall = new AbstractInstallMetaData();
      uninstall.setBean("Name1");
      uninstall.setMethodName("removeSimpleBean");
      parameters = new ArrayList<ParameterMetaData>();
      parameters.add(new AbstractParameterMetaData(null, new ThisValueMetaData()));
      uninstall.setParameters(parameters);
      uninstalls.add(uninstall);
      metaData2.setUninstalls(uninstalls);

      AbstractBeanMetaData metaData3 = new AbstractBeanMetaData("Name3", SimpleBeanImpl.class.getName());

      ArrayList<InstallMetaData> stateInstalls = new ArrayList<InstallMetaData>();
      addInstallMetaData(ControllerState.PRE_INSTALL, stateInstalls);
      addInstallMetaData(ControllerState.DESCRIBED, stateInstalls);
      addInstallMetaData(ControllerState.INSTANTIATED, stateInstalls);
      addInstallMetaData(ControllerState.CONFIGURED, stateInstalls);
      addInstallMetaData(ControllerState.CREATE, stateInstalls);
      addInstallMetaData(ControllerState.START, stateInstalls);

      ArrayList<InstallMetaData> stateUninstalls = new ArrayList<InstallMetaData>();
      addUninstallMetaData(ControllerState.PRE_INSTALL, stateUninstalls);
      addUninstallMetaData(ControllerState.DESCRIBED, stateUninstalls);
      addUninstallMetaData(ControllerState.INSTANTIATED, stateUninstalls);
      addUninstallMetaData(ControllerState.CONFIGURED, stateUninstalls);
      addUninstallMetaData(ControllerState.CREATE, stateUninstalls);
      addUninstallMetaData(ControllerState.START, stateUninstalls);

      metaData3.setInstalls(stateInstalls);
      metaData3.setUninstalls(stateUninstalls);

      setBeanMetaDatas(new BeanMetaData[] { metaData1, metaData2, metaData3 });
   }

   protected void addInstallMetaData(ControllerState state, List<InstallMetaData> states)
   {
      addInstallMetaData(state, states, "add");
   }

   protected void addUninstallMetaData(ControllerState state, List<InstallMetaData> states)
   {
      addInstallMetaData(state, states, "remove");         
   }

   protected void addInstallMetaData(ControllerState state, List<InstallMetaData> states, String prefix)
   {
      AbstractInstallMetaData install = new AbstractInstallMetaData();
      install.setBean("Name1");
      install.setMethodName(prefix + "State");
      AbstractInjectionValueMetaData injection = new AbstractInjectionValueMetaData();
      injection.setFromContext(FromContext.STATE);
      ParameterMetaData parameter = new AbstractParameterMetaData(injection);
      install.setParameters(Collections.singletonList(parameter));
      install.setState(state);
      states.add(install);
   }
}