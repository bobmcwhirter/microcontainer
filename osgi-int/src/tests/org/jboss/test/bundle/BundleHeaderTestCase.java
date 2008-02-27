package org.jboss.test.bundle;

import java.util.Dictionary;

import junit.framework.Test;

import org.jboss.classloading.plugins.metadata.PackageCapability;
import org.jboss.classloading.plugins.metadata.PackageRequirement;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaData;
import org.jboss.classloading.spi.version.Version;
import org.jboss.classloading.spi.version.VersionRange;
import org.jboss.deployers.structure.spi.DeploymentContext;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.structure.spi.helpers.AbstractDeploymentContext;
import org.jboss.deployers.structure.spi.helpers.AbstractDeploymentUnit;
import org.jboss.osgi.plugins.facade.helpers.BundleHeaders;
import org.jboss.test.bundle.metadata.AbstractManifestTestCase;
import org.osgi.framework.Constants;

public class BundleHeaderTestCase extends AbstractManifestTestCase
{
   private BundleHeaders bundleHeaders;

   public BundleHeaderTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(BundleHeaderTestCase.class);
   }

   @Override
   protected void setUp() throws Exception
   {
      super.setUp();
      DeploymentContext deploymentContext = new AbstractDeploymentContext("aDeployment", "/test.jar");

      DeploymentUnit deploymentUnit = new AbstractDeploymentUnit(deploymentContext);

      ClassLoadingMetaData clMetaData = new ClassLoadingMetaData();
      clMetaData.getCapabilities().addCapability(new PackageCapability("org.jboss.test", new Version(1,2,0)));
      clMetaData.getCapabilities().addCapability(new PackageCapability("org.jboss.test.other", new Version(1,5,0)));
      
      
      clMetaData.getRequirements().addRequirement(new PackageRequirement("org.jboss.test", new VersionRange(new Version(1,2,0))));
      clMetaData.getRequirements().addRequirement(new PackageRequirement("org.jboss.test.other", new VersionRange(new Version(1,5,0),new Version(2,0,1))));
      deploymentUnit.addAttachment(ClassLoadingMetaData.class, clMetaData);
      
      bundleHeaders = new BundleHeaders(deploymentUnit);
   }

   @SuppressWarnings("unchecked")
   public void testToDictionary() throws Exception
   {
      Dictionary<String, Object> headers = bundleHeaders.toDictionary();
      assertNotNull(headers);
      assertEquals("org.jboss.test;version=1.2.0,org.jboss.test.other;version=1.5.0", headers.get(Constants.EXPORT_PACKAGE));
      assertEquals("org.jboss.test;version=[1.2.0,?),org.jboss.test.other;version=[1.5.0,2.0.1)", headers.get(Constants.IMPORT_PACKAGE));
   }
}
