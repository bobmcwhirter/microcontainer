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
package org.jboss.test.kernel.controller.test;


import junit.framework.Test;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.test.kernel.controller.support.TestBeanRepository;

/**
 * InstallErrorTestCase 
 * 
 * TODO test others, e.g. ControllerContextAware
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class InstallErrorTestCase extends AbstractControllerTest
{
   public static Test suite()
   {
      return suite(InstallErrorTestCase.class);
   }

   public InstallErrorTestCase(String name) throws Throwable
   {
      super(name);
   }
   
   public void testErrorInInstallWithSupplies() throws Throwable
   {
      KernelDeployment repository = deploy("InstallErrorTestCase_Repositories.xml");
      try
      {
         KernelDeployment deployment = deploy("InstallErrorTestCase_BeanWithSupply.xml");
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
      KernelDeployment repository = deploy("InstallErrorTestCase_Repositories.xml");
      try
      {
         KernelDeployment deployment = deploy("InstallErrorTestCase_Bean.xml");
         try
         {
            ControllerContext context = getControllerContext("Bean", null);
            assertEquals(ControllerState.ERROR, context.getState());
            checkThrowable(Error.class, context.getError());
         }
         finally
         {
            undeploy(deployment);
         }

         deployment = deploy("InstallErrorTestCase_BeanNoInstall.xml");
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
      KernelDeployment repository = deploy("InstallErrorTestCase_Repositories.xml");
      try
      {
         TestBeanRepository repository1 = (TestBeanRepository) getBean("Repository1");
         TestBeanRepository repository2 = (TestBeanRepository) getBean("Repository2");
         KernelDeployment deployment = deploy("InstallErrorTestCase_Bean.xml");
         try
         {
            ControllerContext context = getControllerContext("Bean", null);
            assertEquals(ControllerState.ERROR, context.getState());
            checkThrowable(Error.class, context.getError());
            assertTrue(repository1.getBeans().isEmpty());
            assertTrue(repository2.getBeans().isEmpty());
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