/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
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

import java.util.Collections;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.deployers.plugins.deployers.kernel.BeanMetaDataDeployer;
import org.jboss.deployers.plugins.deployers.kernel.KernelDeploymentDeployer;
import org.jboss.deployers.plugins.deployment.MainDeployerImpl;
import org.jboss.deployers.spi.deployment.MainDeployer;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.deployers.spi.structure.StructureDetermined;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.bootstrap.basic.BasicBootstrap;
import org.jboss.kernel.plugins.deployment.AbstractKernelDeployment;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.test.deployers.BaseDeployersTest;
import org.jboss.test.deployers.bean.support.Simple;
import org.jboss.test.deployers.deployer.support.TestDeployment;
import org.jboss.test.deployers.deployer.support.TestDeploymentDeployer;
import org.jboss.test.deployers.deployer.support.TestMetaData;

/**
 * Test of deployments with multiple deployment types that map to
 * multiple components.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class MultipleComponentTypeUnitTestCase extends BaseDeployersTest
{
   private MainDeployerImpl mainDeployer;
   private KernelController controller;
   
   public static Test suite()
   {
      return new TestSuite(MultipleComponentTypeUnitTestCase.class);
   }
   
   public MultipleComponentTypeUnitTestCase(String name)
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
         
         mainDeployer = new MainDeployerImpl();
         
         TestDeploymentDeployer testDeployer = new TestDeploymentDeployer();
         KernelDeploymentDeployer kernelDeploymentDeployer = new KernelDeploymentDeployer();
         BeanMetaDataDeployer beanMetaDataDeployer = new BeanMetaDataDeployer(kernel);
         mainDeployer.addDeployer(testDeployer);
         mainDeployer.addDeployer(kernelDeploymentDeployer);
         mainDeployer.addDeployer(beanMetaDataDeployer);
      }
      catch (Throwable t)
      {
         throw new RuntimeException(t);
      }
   }

   public void testDeploy() throws Exception
   {
      MainDeployer main = mainDeployer;
      
      DeploymentContext context = createSimpleDeployment("deploy");
      main.addDeploymentContext(context);
      // Add a kernel deployment
      context.setStructureDetermined(StructureDetermined.PREDETERMINED);

      AbstractKernelDeployment deployment = new AbstractKernelDeployment();
      deployment.setName("KernelDeployerTest");
      
      BeanMetaDataFactory metaData = new AbstractBeanMetaData("MCBean1", Simple.class.getName());
      deployment.setBeanFactories(Collections.singletonList(metaData));
      
      context.getPredeterminedManagedObjects().addAttachment("KernelDeployerTest", deployment);
      // Add a TestDeploymentDeployer
      TestDeployment deployment2 = new TestDeployment();
      deployment2.addBean(new TestMetaData("TestBean1"));
      context.getTransientAttachments().addAttachment(TestDeployment.class, deployment2);
      main.process();

      assertNotNull(controller.getInstalledContext("MCBean1"));
   }

}
