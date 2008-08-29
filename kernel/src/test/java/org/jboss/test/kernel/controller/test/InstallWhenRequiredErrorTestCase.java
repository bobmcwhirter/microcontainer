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
package org.jboss.test.kernel.controller.test;

import junit.framework.Test;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.test.kernel.controller.support.TestBean;
import org.jboss.test.kernel.controller.support.TestBeanRepository;

/**
 * Install when required test case.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class InstallWhenRequiredErrorTestCase extends AbstractControllerTest
{
   public static Test suite()
   {
      return suite(InstallWhenRequiredErrorTestCase.class);
   }

   public InstallWhenRequiredErrorTestCase(String name) throws Throwable
   {
      super(name);
   }

   protected ControllerState[] getStates()
   {
      return new ControllerState[]{
            ControllerState.CONFIGURED,
            ControllerState.CREATE,
            ControllerState.START,
      };
   }

   protected BeanMetaData createBeanMetaData(ControllerState state)
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder("Bean", TestBean.class.getName());
      builder.addInstallWithThis("add", "Repository1", null, state);
      builder.addInstallWithThis("add", "Repository2", null, state);
      builder.addUninstallWithThis("remove", "Repository1", null, state);
      builder.addUninstallWithThis("remove", "Repository2", null, state);
      return builder.getBeanMetaData();
   }

   public void testErrorInInstallWithSupplies() throws Throwable
   {
      ControllerState[] states = getStates();
      for (ControllerState state : states)
         runErrorInInstallWithSupplies(state);
   }

   public void runErrorInInstallWithSupplies(ControllerState state) throws Throwable
   {
      KernelDeployment repository = deploy("InstallErrorTestCase_Repositories.xml");
      try
      {
         BeanMetaData metaData = createBeanMetaData(state);
         KernelControllerContext deployment = deploy(metaData);
         try
         {
            ControllerContext context = getControllerContext("Bean", null);
            assertEquals(ControllerState.ERROR, context.getState());
            checkThrowable(Error.class, context.getError());
            try
            {
               getControllerContext("TestSupply", null);
               fail("Should not be here!");
            }
            catch (Throwable throwable)
            {
               checkThrowable(IllegalStateException.class, throwable);
            }
         }
         finally
         {
            undeploy(deployment);
         }
      }
      finally
      {
         undeploy(repository);
      }
   }

   public void testErrorInInstallRedeploy() throws Throwable
   {
      ControllerState[] states = getStates();
      for (ControllerState state : states)
         runErrorInInstallRedeploy(state);
   }

   public void runErrorInInstallRedeploy(ControllerState state) throws Throwable
   {
      KernelDeployment repository = deploy("InstallErrorTestCase_Repositories.xml");
      try
      {
         BeanMetaData metaData = createBeanMetaData(state);
         KernelControllerContext kcc = deploy(metaData);
         try
         {
            ControllerContext context = getControllerContext("Bean", null);
            assertEquals(ControllerState.ERROR, context.getState());
            checkThrowable(Error.class, context.getError());
         }
         finally
         {
            undeploy(kcc);
         }

         KernelDeployment deployment = deploy("InstallErrorTestCase_BeanNoInstall.xml");
         try
         {
            assertNotNull(getControllerContext("Bean"));
         }
         finally
         {
            undeploy(deployment);
         }
      }
      finally
      {
         undeploy(repository);
      }
   }

   public void testErrorInInstall() throws Throwable
   {
      ControllerState[] states = getStates();
      for (ControllerState state : states)
         runErrorInInstall(state);         
   }

   public void runErrorInInstall(ControllerState state) throws Throwable
   {
      KernelDeployment repository = deploy("InstallErrorTestCase_Repositories.xml");
      try
      {
         TestBeanRepository repository1 = (TestBeanRepository)getBean("Repository1");
         TestBeanRepository repository2 = (TestBeanRepository)getBean("Repository2");
         BeanMetaData metaData = createBeanMetaData(state);
         KernelControllerContext deployment = deploy(metaData);
         try
         {
            ControllerContext context = getControllerContext("Bean", null);
            assertEquals(ControllerState.ERROR, context.getState());
            checkThrowable(Error.class, context.getError());
            assertEmpty(repository1.getBeans());
            assertEmpty(repository2.getBeans());
         }
         finally
         {
            undeploy(deployment);
         }
      }
      finally
      {
         undeploy(repository);
      }
   }
}
