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

import org.jboss.test.deployers.BaseDeployersVFSTest;
import org.jboss.deployers.client.spi.DeployerClient;
import org.jboss.deployers.vfs.plugins.structure.jar.JARStructure;
import org.jboss.deployers.vfs.plugins.structure.file.FileStructure;
import org.jboss.deployers.vfs.deployer.kernel.BeanDeployer;
import org.jboss.deployers.vfs.deployer.kernel.KernelDeploymentDeployer;
import org.jboss.deployers.vfs.deployer.kernel.BeanMetaDataDeployer;
import org.jboss.deployers.vfs.deployer.kernel.DeploymentAliasMetaDataDeployer;
import org.jboss.deployers.vfs.deployer.kernel.AliasDeploymentDeployer;
import org.jboss.deployers.vfs.spi.client.VFSDeployment;
import org.jboss.deployers.spi.DeploymentState;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.plugins.bootstrap.basic.BasicBootstrap;
import org.jboss.kernel.Kernel;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * AliasDeployerUnitTestCase.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class AliasDeployerUnitTestCase extends BaseDeployersVFSTest
{
   public static Test suite()
   {
      return new TestSuite(AliasDeployerUnitTestCase.class);
   }

   private DeployerClient main;

   private KernelController controller;

   public AliasDeployerUnitTestCase(String name) throws Throwable
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
         AliasDeploymentDeployer aliasDeploymentDeployer = new AliasDeploymentDeployer();
         BeanMetaDataDeployer beanMetaDataDeployer = new BeanMetaDataDeployer(kernel);
         DeploymentAliasMetaDataDeployer aliasMetaDataDeployer = new DeploymentAliasMetaDataDeployer(kernel);
         addDeployer(main, beanDeployer);
         addDeployer(main, kernelDeploymentDeployer);
         addDeployer(main, aliasDeploymentDeployer);
         addDeployer(main, beanMetaDataDeployer);
         addDeployer(main, aliasMetaDataDeployer);
      }
      catch (Throwable t)
      {
         throw new RuntimeException(t);
      }
   }

   public void testAliasSuccessful() throws Exception
   {
      VFSDeployment context = createDeployment("/alias", "toplevel/my-beans.xml");
      assertDeploy(context);
      assertNotNull(controller.getInstalledContext("Test"));

      VFSDeployment alias = createDeployment("/alias", "toplevel/aliases-beans.xml");
      assertDeploy(alias);
      assertNotNull("Missing Test bean.", controller.getInstalledContext("MyAlias"));
      assertEquals(controller.getInstalledContext("MyAlias"), controller.getInstalledContext("Test"));
      assertNotNull("Missing Injectee bean.", controller.getInstalledContext("Injectee"));

      assertUndeploy(alias);
      assertNull(controller.getContext("MyAlias", null));

      assertUndeploy(context);
      assertNull(controller.getContext("Test", null));
   }

   public void testJoinedSuccessful() throws Exception
   {
      VFSDeployment context = createDeployment("/alias", "toplevel/joined-beans.xml");
      assertDeploy(context);
      assertNotNull(controller.getInstalledContext("Test"));
      assertNotNull(controller.getInstalledContext("MyAlias"));     
      assertEquals(controller.getInstalledContext("MyAlias"), controller.getInstalledContext("Test"));

      assertUndeploy(context);
      assertNull(controller.getContext("MyAlias", null));
      assertNull(controller.getContext("Test", null));
   }

   public void testAliasMissing() throws Exception
   {
      VFSDeployment context = createDeployment("/alias", "toplevel/aliases-beans.xml");
      assertDeploy(context, DeploymentState.ERROR);
      assertUndeploy(context);
      assertNull(controller.getContext("Test", null));
   }

   protected void assertDeploy(VFSDeployment context) throws Exception
   {
      assertDeploy(context, DeploymentState.DEPLOYED);
   }

   protected void assertUndeploy(VFSDeployment context) throws Exception
   {
      assertUndeploy(context, DeploymentState.UNDEPLOYED);
   }

   protected void assertDeploy(VFSDeployment context, DeploymentState expectedState) throws Exception
   {
      main.addDeployment(context);
      main.process();
      assertEquals("Should be Deployed " + context, expectedState, main.getDeploymentState(context.getName()));
   }

   protected void assertUndeploy(VFSDeployment context, DeploymentState expectedState) throws Exception
   {
      main.removeDeployment(context.getName());
      main.process();
      assertEquals("Should be Undeployed " + context, expectedState, main.getDeploymentState(context.getName()));
   }
}
