package org.jboss.test.bundle;

import java.util.jar.Manifest;

import junit.framework.Test;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.deployers.plugins.deployers.DeployersImpl;
import org.jboss.deployers.plugins.deployers.DeploymentControllerContext;
import org.jboss.deployers.structure.spi.DeploymentContext;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.structure.spi.helpers.AbstractDeploymentContext;
import org.jboss.deployers.structure.spi.helpers.AbstractDeploymentUnit;
import org.jboss.kernel.plugins.bootstrap.basic.BasicBootstrap;
import org.jboss.osgi.plugins.facade.BundleImpl;
import org.jboss.osgi.plugins.metadata.AbstractOSGiMetaData;
import org.jboss.osgi.spi.metadata.OSGiMetaData;
import org.jboss.test.bundle.metadata.AbstractManifestTestCase;
import org.osgi.framework.Bundle;

public class BundleImplTestCase extends AbstractManifestTestCase
{
   private OSGiMetaData metaData;
   private Bundle bundle;
   private DeploymentUnit deploymentUnit;
   private ControllerContext controllerContext;

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
      
      BasicBootstrap bootstrap = new BasicBootstrap();
      bootstrap.run();
      
      DeploymentContext deploymentContext = new AbstractDeploymentContext("deployment", "/");
      deploymentUnit = new AbstractDeploymentUnit(deploymentContext);
      controllerContext = new DeploymentControllerContext(deploymentContext, new DeployersImpl(bootstrap.getKernel().getController()));
      deploymentUnit.addAttachment(ControllerContext.class.getName(), controllerContext);
      bundle = new BundleImpl(deploymentUnit);
      
      Manifest manifest = getManifest("/org/jboss/test/bundle/metadata/SimpleManifest.mf");
      metaData = new AbstractOSGiMetaData(manifest);
      deploymentUnit.addAttachment(OSGiMetaData.class, metaData);
      
      try
      {
         bootstrap.getKernel().getController().install(controllerContext);
      }
      catch (Throwable e)
      {
         throw new Exception(e);
      }
   }

   public void testGetBundleState() throws Exception
   {
      controllerContext.setState(ControllerState.ERROR);
      assertEquals(Bundle.INSTALLED, bundle.getState());

      controllerContext.setState(ControllerState.NOT_INSTALLED);
      assertEquals(Bundle.UNINSTALLED, bundle.getState());

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
      assertEquals(Bundle.UNINSTALLED, bundle.getState());
      bundle.start();
      assertEquals(Bundle.ACTIVE, bundle.getState());
   }

}
