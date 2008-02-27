package org.jboss.test.bundle;


import junit.framework.Test;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
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

   public void testGetBundleState() throws Exception
   {
      controllerContext.setState(ControllerState.ERROR);
      assertEquals(Bundle.UNINSTALLED, bundle.getState());

      controllerContext.setState(ControllerState.NOT_INSTALLED);
      assertEquals(Bundle.INSTALLED, bundle.getState());

      controllerContext.setState(ControllerState.PRE_INSTALL);
      assertEquals(Bundle.INSTALLED, bundle.getState());

      controllerContext.setState(ControllerState.DESCRIBED);
      assertEquals(Bundle.INSTALLED, bundle.getState());

      controllerContext.setState(ControllerState.INSTANTIATED);
      assertEquals(Bundle.RESOLVED, bundle.getState());

      controllerContext.setState(ControllerState.CONFIGURED);
      assertEquals(Bundle.RESOLVED, bundle.getState());

      controllerContext.setState(ControllerState.CREATE);
      assertEquals(Bundle.RESOLVED, bundle.getState());

      controllerContext.setState(ControllerState.START);
      assertEquals(Bundle.STARTING, bundle.getState());

      controllerContext.setState(ControllerState.INSTALLED);
      assertEquals(Bundle.ACTIVE, bundle.getState());
   }

   public void testGetSymbolicName() throws Exception
   {
      assertEquals("UniqueName", bundle.getSymbolicName());
   }

   public void testStartBundle() throws Exception
   {
      bundle.start();
      assertChangeRequested(deploymentUnit.getName(), DeploymentStages.INSTALLED);
   }

   public void assertChangeRequested(String deploymentName, DeploymentStage stage)
   {
      assertTrue(mainDeployer.changeCalled(deploymentName, stage));
   }

}
