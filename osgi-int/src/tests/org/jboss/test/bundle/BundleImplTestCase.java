/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.bundle;


import junit.framework.Test;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.deployers.client.spi.main.MainDeployer;
import org.jboss.deployers.spi.deployer.DeploymentStage;
import org.jboss.deployers.spi.deployer.DeploymentStages;
import org.jboss.deployers.structure.spi.DeploymentContext;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.structure.spi.helpers.AbstractDeploymentContext;
import org.jboss.deployers.structure.spi.helpers.AbstractDeploymentUnit;
import org.jboss.osgi.plugins.facade.BundleImpl;
import org.jboss.test.bundle.metadata.AbstractManifestTestCase;
import org.jboss.test.bundle.support.TestControllerContext;
import org.jboss.test.bundle.support.TestMainDeployer;
import org.osgi.framework.Bundle;

/**
 * 
 * A BundleImplTestCase.
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public class BundleImplTestCase extends AbstractManifestTestCase
{
   private Bundle bundle;

   private DeploymentUnit deploymentUnit;

   private ControllerContext controllerContext;

   private TestMainDeployer mainDeployer;

   public BundleImplTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(BundleImplTestCase.class);
   }

   @Override
   protected void setUp() throws Exception
   {
      super.setUp();

      DeploymentContext deploymentContext = new AbstractDeploymentContext("UniqueName", "/simple.jar");
      deploymentUnit = new AbstractDeploymentUnit(deploymentContext);
      controllerContext = new TestControllerContext(deploymentContext);
      deploymentUnit.addAttachment(ControllerContext.class.getName(), controllerContext);
      mainDeployer = new TestMainDeployer();
      deploymentUnit.addAttachment(MainDeployer.class.getName(), mainDeployer);
      bundle = new BundleImpl(deploymentUnit);
   }

   /** 
    * Test the getState method.  Should correctly call DeploymentStage2BundleStateMapper
    * 
    * @throws Exception
    */
   public void testGetBundleState() throws Exception
   {
      mainDeployer.change(deploymentUnit.getName(), DeploymentStages.NOT_INSTALLED);
      assertEquals(Bundle.UNINSTALLED, bundle.getState());

      mainDeployer.change(deploymentUnit.getName(), DeploymentStages.CLASSLOADER);
      assertEquals(Bundle.RESOLVED, bundle.getState());

      mainDeployer.change(deploymentUnit.getName(), DeploymentStages.REAL);
      assertEquals(Bundle.STARTING, bundle.getState());

      mainDeployer.change(deploymentUnit.getName(), DeploymentStages.INSTALLED);
      assertEquals(Bundle.ACTIVE, bundle.getState());
   }

   /**
    * Returns the DeploymentContext.name as the Unique name
    * 
    * @throws Exception
    */
   public void testGetSymbolicName() throws Exception
   {
      assertEquals("UniqueName", bundle.getSymbolicName());
   }

   /**
    * Verifies the Bundle.start method calls the MainDeployer.change method with the INSTALL DeploymentStage
    * 
    * @throws Exception
    */
   public void testStartBundle() throws Exception
   {
      bundle.start();
      assertChangeRequested(deploymentUnit.getName(), DeploymentStages.INSTALLED);
   }

   /**
    * Assert the correct change was requested of the MainDeployer
    * 
    * @param deploymentName
    * @param stage
    */
   public void assertChangeRequested(String deploymentName, DeploymentStage stage)
   {
      assertTrue(mainDeployer.changeCalled(deploymentName, stage));
   }

}
