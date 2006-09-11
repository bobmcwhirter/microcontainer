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
package org.jboss.test.deployers.deployer.test;

import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.plugins.deployment.MainDeployerImpl;
import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.deployers.spi.deployment.MainDeployer;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.test.deployers.BaseDeployersTest;
import org.jboss.test.deployers.deployer.support.TestSimpleDeployer;

/**
 * DeployerProtocolUnitTestCase.
 * 
 * TODO implement the full deployment protocol
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class DeployerWidthFirstUnitTestCase extends BaseDeployersTest
{
   private MainDeployer mainDeployer;
   
   private TestSimpleDeployer deployer1 = new TestSimpleDeployer(1);
   private TestSimpleDeployer deployer2 = new TestSimpleDeployer(2);
   
   public static Test suite()
   {
      return new TestSuite(DeployerWidthFirstUnitTestCase.class);
   }
   
   public DeployerWidthFirstUnitTestCase(String name)
   {
      super(name);
   }

   public void testDeployWidthFirst() throws Exception
   {
      MainDeployer main = getMainDeployer();
      
      DeploymentContext context1 = createSimpleDeployment("deploy1");
      main.addDeploymentContext(context1);
      DeploymentContext context2 = createSimpleDeployment("deploy2");
      main.addDeploymentContext(context2);
      main.process();
      
      Map<DeploymentUnit, Integer> deployed1 = deployer1.getDeployed();
      Map<DeploymentUnit, Integer> deployed2 = deployer2.getDeployed();
      
      int c1d1 = deployed1.get(context1.getDeploymentUnit());
      int c1d2 = deployed2.get(context1.getDeploymentUnit());
      int c2d1 = deployed1.get(context2.getDeploymentUnit());
      int c2d2 = deployed2.get(context2.getDeploymentUnit());

      assertFalse(c1d1 == -1);
      assertFalse(c1d2 == -1);
      assertFalse(c2d1 == -1);
      assertFalse(c2d2 == -1);
      
      assertTrue("Deployer1 should be before Deployer2", c1d1 < c1d2 && c1d1 < c2d2 && c2d1 < c1d2 && c2d1 < c2d2);
   }

   public void testUndeployWidthFirst() throws Exception
   {
      MainDeployer main = getMainDeployer();
      
      DeploymentContext context1 = createSimpleDeployment("deploy1");
      main.addDeploymentContext(context1);
      DeploymentContext context2 = createSimpleDeployment("deploy2");
      main.addDeploymentContext(context2);
      main.process();
      
      main.removeDeploymentContext(context1.getName());
      main.removeDeploymentContext(context2.getName());
      main.process();
      
      Map<DeploymentUnit, Integer> undeployed1 = deployer1.getUndeployed();
      Map<DeploymentUnit, Integer> undeployed2 = deployer2.getUndeployed();
      
      int c1d1 = undeployed1.get(context1.getDeploymentUnit());
      int c1d2 = undeployed2.get(context1.getDeploymentUnit());
      int c2d1 = undeployed1.get(context2.getDeploymentUnit());
      int c2d2 = undeployed2.get(context2.getDeploymentUnit());
      
      assertFalse(c1d1 == -1);
      assertFalse(c1d2 == -1);
      assertFalse(c2d1 == -1);
      assertFalse(c2d2 == -1);
      
      assertTrue("Deployer2 should be before Deployer1", c1d2 < c1d1 && c1d2 < c2d1 && c2d2 < c1d1 && c2d2 < c2d1);
   }
   
   protected MainDeployer getMainDeployer()
   {
      if (mainDeployer != null)
         return mainDeployer;
      
      MainDeployerImpl result = new MainDeployerImpl();
      result.addDeployer(deployer1);
      result.addDeployer(deployer2);
      
      mainDeployer = result;
      return result;
   }
}
