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
package org.jboss.test.deployers.vfs.deployer.bean.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.client.spi.DeployerClient;
import org.jboss.deployers.spi.DeploymentState;
import org.jboss.deployers.vfs.deployer.kernel.BeanDeployer;
import org.jboss.deployers.vfs.deployer.kernel.BeanMetaDataDeployer;
import org.jboss.deployers.vfs.deployer.kernel.KernelDeploymentDeployer;
import org.jboss.deployers.vfs.plugins.structure.file.FileStructure;
import org.jboss.deployers.vfs.plugins.structure.jar.JARStructure;
import org.jboss.deployers.vfs.spi.client.VFSDeployment;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.bootstrap.basic.BasicBootstrap;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.test.deployers.BaseDeployersVFSTest;

/**
 * BeanDeployerUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class BeanDeployerUnitTestCase extends BaseDeployersVFSTest
{
   public static Test suite()
   {
      return new TestSuite(BeanDeployerUnitTestCase.class);
   }

   private DeployerClient main;
   
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
         
         main = createMainDeployer();
         addStructureDeployer(main, new JARStructure());
         addStructureDeployer(main, new FileStructure());
         
         BeanDeployer beanDeployer = new BeanDeployer();
         KernelDeploymentDeployer kernelDeploymentDeployer = new KernelDeploymentDeployer();
         BeanMetaDataDeployer beanMetaDataDeployer = new BeanMetaDataDeployer(kernel);
         addDeployer(main, beanDeployer);
         addDeployer(main, kernelDeploymentDeployer);
         addDeployer(main, beanMetaDataDeployer);
      }
      catch (Throwable t)
      {
         throw new RuntimeException(t);
      }
   }

   public void testTopLevelFile() throws Exception
   {
      VFSDeployment context = createDeployment("/bean", "toplevel/my-beans.xml");
      assertDeploy(context);
      assertNotNull(controller.getInstalledContext("Test"));
      
      assertUndeploy(context);
      assertNull(controller.getContext("Test", null));
   }

   public void testMetaInfFile() throws Exception
   {
      VFSDeployment context = createDeployment("/bean", "toplevel/test.jar");
      assertDeploy(context);
      assertNotNull(controller.getInstalledContext("Test"));
      
      assertUndeploy(context);
      assertNull(controller.getContext("Test", null));
   }

   protected void assertDeploy(VFSDeployment context) throws Exception
   {
      main.addDeployment(context);
      main.process();
      assertEquals("Should be Deployed " + context, DeploymentState.DEPLOYED, main.getDeploymentState(context.getName()));
   }

   protected void assertUndeploy(VFSDeployment context) throws Exception
   {
      main.removeDeployment(context.getName());
      main.process();
      assertEquals("Should be Undeployed " + context, DeploymentState.UNDEPLOYED, main.getDeploymentState(context.getName()));
   }
}
