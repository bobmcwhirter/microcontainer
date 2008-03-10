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
package org.jboss.test.bundle.helper;

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
import org.jboss.test.BaseTestCase;
import org.osgi.framework.Constants;

/**
 * A BundleHeaderTestCase.
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public class BundleHeaderTestCase extends BaseTestCase
{
   private BundleHeaders bundleHeaders;

   /**
    * Create a new BundleHeaderTestCase.
    * 
    * @param name
    */
   public BundleHeaderTestCase(String name)
   {
      super(name);
   }

   /**
    * Get test suite
    * 
    * @return
    */
   public static Test suite()
   {
      return suite(BundleHeaderTestCase.class);
   }

   /**
    * Setup test
    */
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
   /**
    * Test getting BundleHeaders as a Dictionary
    * 
    * @throws Exception
    */
   @SuppressWarnings("unchecked")
   public void testToDictionary() throws Exception
   {
      Dictionary<String, Object> headers = bundleHeaders.toDictionary();
      assertNotNull(headers);
      assertEquals("org.jboss.test;version=1.2.0,org.jboss.test.other;version=1.5.0", headers.get(Constants.EXPORT_PACKAGE));
      assertEquals("org.jboss.test;version=[1.2.0,?),org.jboss.test.other;version=[1.5.0,2.0.1)", headers.get(Constants.IMPORT_PACKAGE));
   }
}
