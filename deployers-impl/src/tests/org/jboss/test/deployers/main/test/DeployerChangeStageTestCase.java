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
package org.jboss.test.deployers.main.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;

import org.jboss.deployers.client.spi.DeployerClient;
import org.jboss.deployers.client.spi.Deployment;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.DeploymentState;
import org.jboss.deployers.spi.deployer.DeploymentStages;
import org.jboss.deployers.structure.spi.DeploymentContext;
import org.jboss.deployers.structure.spi.DeploymentUnit;

/**
 * DeployerChangeStageTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class DeployerChangeStageTestCase extends AbstractMainDeployerTest
{
   public DeployerChangeStageTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(DeployerChangeStageTestCase.class);
   }

   public void testMainDeployerInUnit() throws Throwable
   {
      DeployerClient main = getMainDeployer();

      Deployment single = createSimpleDeployment("single");
      main.deploy(single);
      List<String> expected = new ArrayList<String>();
      expected.add(single.getName());
      assertEquals(expected, deployer.getDeployedUnits());

      DeploymentUnit unit = assertDeploymentUnit(main, single.getName());
      assertEquals(main, unit.getMainDeployer());
      
      main.undeploy(single);
      assertNull(unit.getMainDeployer());
   }

   public void testChangeStage() throws Throwable
   {
      DeployerClient main = getMainDeployer();

      Deployment single = createSimpleDeployment("single");
      main.deploy(single);
      List<String> expected = new ArrayList<String>();
      expected.add(single.getName());
      assertEquals(expected, deployer.getDeployedUnits());

      main.change(single.getName(), DeploymentStages.CLASSLOADER);
      assertEquals(expected, deployer.getUndeployedUnits());
      
      main.undeploy(single);
      assertEquals(expected, deployer.getDeployedUnits());
      assertEquals(expected, deployer.getUndeployedUnits());
      try
      {
         main.change(single.getName(), DeploymentStages.REAL);
         fail("Should not be here");
      }
      catch (Throwable t)
      {
         checkThrowable(DeploymentException.class, t);
      }
      
      deployer.clear();
      main.deploy(single);
      assertEquals(expected, deployer.getDeployedUnits());
      main.change(single.getName(), DeploymentStages.CLASSLOADER);
      deployer.clear();
      main.change(single.getName(), DeploymentStages.REAL);
      assertEquals(expected, deployer.getDeployedUnits());
   }

   public void testChangeStageFail() throws Throwable
   {
      DeployerClient main = getMainDeployer();

      Deployment single = createSimpleDeployment("single");
      main.deploy(single);
      List<String> expected = new ArrayList<String>();
      expected.add(single.getName());
      assertEquals(expected, deployer.getDeployedUnits());

      main.change(single.getName(), DeploymentStages.CLASSLOADER);
      DeploymentUnit unit = assertDeploymentUnit(main, single.getName());
      DeploymentContext context = assertDeploymentContext(main, single.getName());
      unit.addAttachment("fail", deployer);
      
      deployer.clear();
      try
      {
         main.change(single.getName(), DeploymentStages.REAL);
         fail("Should not be here");
      }
      catch (Throwable t)
      {
         checkThrowable(DeploymentException.class, t);
      }
      assertEquals(expected, deployer.getFailed());
      assertEquals(DeploymentState.ERROR, context.getState());
      checkThrowable(DeploymentException.class, context.getProblem());
   }
}
