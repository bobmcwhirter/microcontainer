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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.plugins.deployment.MainDeployerImpl;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.test.deployers.BaseDeployersTest;
import org.jboss.test.deployers.deployer.support.TestSimpleDeployer;

/**
 * DeployerOrderingUnitTestCase.
 * 
 * TODO implement the full deployment protocol
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class DeployerOrderingUnitTestCase extends BaseDeployersTest
{
   public static Test suite()
   {
      return new TestSuite(DeployerOrderingUnitTestCase.class);
   }
   
   public DeployerOrderingUnitTestCase(String name)
   {
      super(name);
   }
   
   protected void setUp() throws Exception
   {
      super.setUp();
      TestSimpleDeployer.reset();
   }

   public void testCorrectOrder() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      TestSimpleDeployer deployer1 = new TestSimpleDeployer(1);
      main.addDeployer(deployer1);
      TestSimpleDeployer deployer2 = new TestSimpleDeployer(2);
      main.addDeployer(deployer2);
      
      DeploymentContext context = createSimpleDeployment("correctOrder");
      main.addDeploymentContext(context);
      main.process();
      
      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(-1, deployer1.getUndeployOrder());
      assertEquals(-1, deployer2.getUndeployOrder());

      main.removeDeploymentContext(context.getName());
      main.process();
      
      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(4, deployer1.getUndeployOrder());
      assertEquals(3, deployer2.getUndeployOrder());

      main.addDeploymentContext(context);
      main.process();
      
      assertEquals(5, deployer1.getDeployOrder());
      assertEquals(6, deployer2.getDeployOrder());
      assertEquals(4, deployer1.getUndeployOrder());
      assertEquals(3, deployer2.getUndeployOrder());
   }
   
   public void testWrongOrder() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      TestSimpleDeployer deployer2 = new TestSimpleDeployer(2);
      main.addDeployer(deployer2);
      TestSimpleDeployer deployer1 = new TestSimpleDeployer(1);
      main.addDeployer(deployer1);
      
      DeploymentContext context = createSimpleDeployment("wrongOrder");
      main.addDeploymentContext(context);
      main.process();
      
      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(-1, deployer1.getUndeployOrder());
      assertEquals(-1, deployer2.getUndeployOrder());

      main.removeDeploymentContext(context.getName());
      main.process();
      
      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(4, deployer1.getUndeployOrder());
      assertEquals(3, deployer2.getUndeployOrder());

      main.addDeploymentContext(context);
      main.process();
      
      assertEquals(5, deployer1.getDeployOrder());
      assertEquals(6, deployer2.getDeployOrder());
      assertEquals(4, deployer1.getUndeployOrder());
      assertEquals(3, deployer2.getUndeployOrder());
   }
}
