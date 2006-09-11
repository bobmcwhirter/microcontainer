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

import java.util.HashSet;
import java.util.Set;

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
public class DeployerProtocolUnitTestCase extends BaseDeployersTest
{
   private MainDeployer mainDeployer;
   
   private TestSimpleDeployer deployer = new TestSimpleDeployer();
   
   public static Test suite()
   {
      return new TestSuite(DeployerProtocolUnitTestCase.class);
   }
   
   public DeployerProtocolUnitTestCase(String name)
   {
      super(name);
   }

   public void testDeploy() throws Exception
   {
      MainDeployer main = getMainDeployer();
      
      DeploymentContext context = createSimpleDeployment("deploy");
      main.addDeploymentContext(context);
      main.process();
      Set<DeploymentUnit> expected = new HashSet<DeploymentUnit>();
      expected.add(context.getDeploymentUnit());
      assertEquals(expected, deployer.getDeployedUnits());
   }

   public void testUndeploy() throws Exception
   {
      MainDeployer main = getMainDeployer();

      DeploymentContext context = createSimpleDeployment("undeploy");
      main.addDeploymentContext(context);
      main.process();
      Set<DeploymentUnit> expected = new HashSet<DeploymentUnit>();
      expected.add(context.getDeploymentUnit());
      assertEquals(expected, deployer.getDeployedUnits());
      
      main.removeDeploymentContext(context.getName());
      main.process();
      assertEquals(expected, deployer.getUndeployedUnits());
   }

   public void testRedeploy() throws Exception
   {
      MainDeployer main = getMainDeployer();

      DeploymentContext context = createSimpleDeployment("redeploy");
      main.addDeploymentContext(context);
      main.process();
      Set<DeploymentUnit> expected = new HashSet<DeploymentUnit>();
      expected.add(context.getDeploymentUnit());
      assertEquals(expected, deployer.getDeployedUnits());
      
      main.removeDeploymentContext(context.getName());
      main.process();
      assertEquals(expected, deployer.getUndeployedUnits());
      
      deployer.clear();
      main.addDeploymentContext(context);
      main.process();
      expected.clear();
      expected.add(context.getDeploymentUnit());
      assertEquals(expected, deployer.getDeployedUnits());
   }
   
   protected MainDeployer getMainDeployer()
   {
      if (mainDeployer != null)
         return mainDeployer;
      
      MainDeployerImpl result = new MainDeployerImpl();
      result.addDeployer(deployer);
      
      mainDeployer = result;
      return result;
   }
}
