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
package org.jboss.test.deployers.structure.main.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.plugins.deployment.MainDeployerImpl;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.test.deployers.BaseDeployersTest;
import org.jboss.test.deployers.structure.main.support.TestStructureOrdering;

/**
 * MainDeployerStructureOrderingUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MainDeployerStructureOrderingUnitTestCase extends BaseDeployersTest
{
   public static Test suite()
   {
      return new TestSuite(MainDeployerStructureOrderingUnitTestCase.class);
   }
   
   public MainDeployerStructureOrderingUnitTestCase(String name)
   {
      super(name);
   }
   
   protected void setUp() throws Exception
   {
      super.setUp();
      TestStructureOrdering.reset();
   }

   public void testCorrectOrder() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      TestStructureOrdering deployer1 = new TestStructureOrdering(1);
      main.addStructureDeployer(deployer1);
      TestStructureOrdering deployer2 = new TestStructureOrdering(2);
      main.addStructureDeployer(deployer2);
      
      DeploymentContext context = createDeploymentContext("/structure/", "jar/simple");
      main.addDeploymentContext(context);
      
      assertEquals(1, deployer1.getStructureOrder());
      assertEquals(2, deployer2.getStructureOrder());
   }
   
   public void testWrongOrder() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      TestStructureOrdering deployer2 = new TestStructureOrdering(2);
      main.addStructureDeployer(deployer2);
      TestStructureOrdering deployer1 = new TestStructureOrdering(1);
      main.addStructureDeployer(deployer1);
      
      DeploymentContext context = createDeploymentContext("/structure/", "jar/simple");
      main.addDeploymentContext(context);
      
      assertEquals(1, deployer1.getStructureOrder());
      assertEquals(2, deployer2.getStructureOrder());
   }
}
