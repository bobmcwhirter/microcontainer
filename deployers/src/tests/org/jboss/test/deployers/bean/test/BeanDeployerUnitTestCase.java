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
package org.jboss.test.deployers.bean.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.plugins.deployers.kernel.BeanDeployer;
import org.jboss.deployers.plugins.deployers.kernel.KernelDeployer;
import org.jboss.deployers.plugins.deployment.MainDeployerImpl;
import org.jboss.deployers.plugins.structure.vfs.file.FileStructure;
import org.jboss.deployers.plugins.structure.vfs.jar.JARStructure;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.deployers.spi.structure.DeploymentState;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.bootstrap.basic.BasicBootstrap;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.test.deployers.BaseDeployersTest;

/**
 * BeanDeployerUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class BeanDeployerUnitTestCase extends BaseDeployersTest
{
   public static Test suite()
   {
      return new TestSuite(BeanDeployerUnitTestCase.class);
   }

   private MainDeployerImpl main;
   
   private KernelController controller;
   
   public BeanDeployerUnitTestCase(String name) throws Throwable
   {
      super(name);
   }

   protected void setUp() throws Exception
   {
      super.setUp();
      try
      {
         BasicBootstrap bootstrap = new BasicBootstrap();
         bootstrap.run();
         Kernel kernel = bootstrap.getKernel();
         controller = kernel.getController();
         
         main = new MainDeployerImpl();
         main.addStructureDeployer(new JARStructure());
         main.addStructureDeployer(new FileStructure());
         
         BeanDeployer beanDeployer = new BeanDeployer();
         KernelDeployer kernelDeployer = new KernelDeployer(kernel);
         main.addDeployer(beanDeployer);
         main.addDeployer(kernelDeployer);
      }
      catch (Throwable t)
      {
         throw new RuntimeException(t);
      }
   }

   public void testTopLevelFile() throws Exception
   {
      DeploymentContext context = createDeploymentContext("/bean", "toplevel/my-beans.xml");
      assertDeploy(context);
      assertNotNull(controller.getInstalledContext("Test"));
      
      assertUndeploy(context);
      assertNull(controller.getContext("Test", null));
   }

   public void testMetaInfFile() throws Exception
   {
      DeploymentContext context = createDeploymentContext("/bean", "toplevel/test.jar");
      assertDeploy(context);
      assertNotNull(controller.getInstalledContext("Test"));
      
      assertUndeploy(context);
      assertNull(controller.getContext("Test", null));
   }

   protected void assertDeploy(DeploymentContext context) throws Exception
   {
      main.addDeploymentContext(context);
      main.process();
      assertTrue(context.getState() == DeploymentState.DEPLOYED);
   }

   protected void assertUndeploy(DeploymentContext context) throws Exception
   {
      main.removeDeploymentContext(context.getName());
      main.process();
      assertTrue(context.getState() == DeploymentState.UNDEPLOYED);
   }
}
