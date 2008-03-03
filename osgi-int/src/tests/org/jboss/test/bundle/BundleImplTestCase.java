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

import java.net.URL;
import java.util.Enumeration;

import junit.framework.Test;

import org.jboss.deployers.spi.deployer.DeploymentStages;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.osgi.plugins.facade.BundleImpl;
import org.jboss.test.OSGiTestCase;
import org.osgi.framework.Bundle;

/**
 * A BundleImplTestCase.
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public class BundleImplTestCase extends OSGiTestCase
{
   private Bundle bundle;
   
   private DeploymentUnit deploymentUnit;

   /**
    * 
    * Create a new BundleImplTestCase.
    * 
    * @param name
    */
   public BundleImplTestCase(String name)
   {
      super(name);
   }

   /**
    * Get suite for test
    * 
    * @return test suite
    */
   public static Test suite()
   {
      return suite(BundleImplTestCase.class);
   }

   /**
    * Setup test
    */
   @Override
   protected void setUp() throws Exception
   {
      super.setUp();

      deploymentUnit = addDeployment("/bundle", "simple.jar");
      bundle = new BundleImpl(deploymentUnit);
   }

   /** 
    * Test the getState method.  Should correctly call DeploymentStage2BundleStateMapper
    * 
    * @throws Exception
    */
   public void testGetBundleState() throws Exception
   {
      getDeployerClient().change(deploymentUnit.getName(), DeploymentStages.NOT_INSTALLED);
      assertEquals(Bundle.UNINSTALLED, bundle.getState());

      getDeployerClient().change(deploymentUnit.getName(), DeploymentStages.CLASSLOADER);
      assertEquals(Bundle.RESOLVED, bundle.getState());

      getDeployerClient().change(deploymentUnit.getName(), DeploymentStages.REAL);
      assertEquals(Bundle.STARTING, bundle.getState());

      getDeployerClient().change(deploymentUnit.getName(), DeploymentStages.INSTALLED);
      assertEquals(Bundle.ACTIVE, bundle.getState());
   }

   /**
    * Returns the DeploymentContext.name as the Unique name
    * 
    * @throws Exception
    */
   public void testGetSymbolicName() throws Exception
   {
      assertEquals(deploymentUnit.getName(), bundle.getSymbolicName());
   }

   /**
    * Returns the id for a Bundle
    * 
    * @throws Exception
    */
   public void testGetBundleId() throws Exception
   {
      assertEquals(deploymentUnit.getName().hashCode(), bundle.getBundleId());
   }

   /**
    * Verifies the Bundle.start method calls the MainDeployer.change method with the INSTALL DeploymentStage
    * 
    * @throws Exception
    */
   public void testStartBundle() throws Exception
   {
      getDeployerClient().change(deploymentUnit.getName(), DeploymentStages.NOT_INSTALLED);
      assertStage(deploymentUnit, DeploymentStages.NOT_INSTALLED); // Sanity Check...
      bundle.start();
      assertStage(deploymentUnit, DeploymentStages.INSTALLED);
   }

   /**
    * Verifies the Bundle.stop method calls the MainDeployer.change method with the DESCRIBE DeploymentStage
    * 
    * @throws Exception
    */
   public void testStopBundle() throws Exception
   {
      getDeployerClient().change(deploymentUnit.getName(), DeploymentStages.INSTALLED);
      assertStage(deploymentUnit, DeploymentStages.INSTALLED); // Sanity Check...
      bundle.stop();
      assertStage(deploymentUnit, DeploymentStages.DESCRIBE);
   }
   
   /**
    * Verifies the Bundle.uninstall method calls the MainDeployer.change method with the NOT_INSTALLED DeploymentStage
    * 
    * @throws Exception
    */
   public void testUninstallBundle() throws Exception
   {
      getDeployerClient().change(deploymentUnit.getName(), DeploymentStages.INSTALLED);
      assertStage(deploymentUnit, DeploymentStages.INSTALLED); // Sanity Check...
      bundle.uninstall();
      assertStage(deploymentUnit, DeploymentStages.NOT_INSTALLED);
   }

   /**
    * Test the Bundle.loadClass method with valid class
    * 
    * @throws Exception
    */
   public void testLoadClass() throws Exception
   {
      Class<?> testClass = bundle.loadClass("org.jboss.test.bundle.support.TestActivator");
      assertNotNull(testClass);
   }

   /** 
    * Test Bundle.loadClass with a missing class.  Should throw normal ClassNotFound
    * 
    * @throws Exception
    */
   public void testLoadClassNotFound() throws Exception
   {
      try
      {
         bundle.loadClass("org.jboss.test.bundle.support.MissingClass");
         fail("Should have thrown ClassNotFoundException");
      }
      catch (ClassNotFoundException expectedException)
      {
         assertEquals("org.jboss.test.bundle.support.MissingClass", expectedException.getMessage());
      }
   }
   
   /**
    * Test Bundle.loadClass with no ClassLoader.  Should throw ClassNotFound with message relating to missing classloader
    * 
    * @throws Exception
    */
   public void testLoadClassNoClassLoaderForDeployment() throws Exception
   {
      getDeployerClient().change(deploymentUnit.getName(), DeploymentStages.DESCRIBE);
      try
      {
         bundle.loadClass("org.jboss.test.bundle.support.MissingClass");
         fail("Should have thrown ClassNotFoundException");
      }
      catch (IllegalStateException expectedException)
      {
         assertEquals("ClassLoader has not been set", expectedException.getMessage());
      }
   }
   
   /**
    * Test Bundle.loadClass with a DeploymentUnit that is in the NOT_INSTALLED stage, should throw IllegalStateException.
    * 
    * @throws Exception
    */
   public void testLoadClassBundleUninstalled() throws Exception
   {
      getDeployerClient().change(deploymentUnit.getName(), DeploymentStages.NOT_INSTALLED);
      try
      {
         bundle.loadClass("org.jboss.test.bundle.support.MissingClass");
         fail("Should have thrown ClassNotFoundException");
      }
      catch (IllegalStateException expectedException)
      {
         assertEquals("Bundle has been uninstalled", expectedException.getMessage());
      }
   }
   
   /**
    * Test the Bundle.getResource method with valid resource
    * 
    * @throws Exception
    */
   public void testGetResource() throws Exception
   {
      URL resource = bundle.getResource("org/jboss/test/bundle/metadata/Manifest.mf");
      assertNotNull(resource);
   }
   
   /**
    * Test the Bundle.getResource method with missing resource
    * 
    * @throws Exception
    */
   public void testGetResourceMissingResource() throws Exception
   {
      URL resource = bundle.getResource("org/jboss/test/bundle/metadata/Missing.mf");
      assertNull(resource);
   }
   
   /**
    * Test the Bundle.getResource method with missing DeploymentUnit ClassLoader
    * 
    * @throws Exception
    */
   public void testGetResourceNoClassLoaderForDeploymentUnit() throws Exception
   {
      getDeployerClient().change(deploymentUnit.getName(), DeploymentStages.DESCRIBE);
      try {
         bundle.getResource("org/jboss/test/bundle/metadata/Manifest.mf");
         fail("Should have thrown IllegalStateException");
      } catch (IllegalStateException expectedException) {
         assertEquals("ClassLoader has not been set", expectedException.getMessage());
      }
   }
   
   /**
    * Test the Bundle.getResource method for an uninstalled Bundle
    * 
    * @throws Exception
    */
   public void testGetResourceBundleUnintalled() throws Exception
   {
      getDeployerClient().change(deploymentUnit.getName(), DeploymentStages.NOT_INSTALLED);
      try {
         bundle.getResource("org/jboss/test/bundle/metadata/Manifest.mf");
         fail("Should have thrown IllegalStateException");
      } catch (IllegalStateException expectedException) {
         assertEquals("Bundle has been uninstalled", expectedException.getMessage());
      }
   }

   /**
    * Test the Bundle.getResources method with valid resource
    * 
    * @throws Exception
    */
   @SuppressWarnings("unchecked")
   public void testGetResources() throws Exception
   {
      Enumeration<URL> resources = bundle.getResources("org/jboss/test/bundle/metadata/Manifest.mf");
      assertNotNull(resources);
      assertTrue(resources.hasMoreElements());
   }
   
   /**
    * Test the Bundle.getResources method with missing resource
    * 
    * @throws Exception
    */
   @SuppressWarnings("unchecked")
   public void testGetResourceMissingResources() throws Exception
   {
      Enumeration<URL> resources = bundle.getResources("org/jboss/test/bundle/metadata/Missing.mf");
      assertNotNull(resources);
      assertFalse(resources.hasMoreElements());
   }
   
   /**
    * Test the Bundle.getResources method with missing DeploymentUnit ClassLoader
    * 
    * @throws Exception
    */
   public void testGetResourcesNoClassLoaderForDeploymentUnit() throws Exception
   {
      getDeployerClient().change(deploymentUnit.getName(), DeploymentStages.DESCRIBE);
      try {
         bundle.getResources("org/jboss/test/bundle/metadata/Manifest.mf");
         fail("Should have thrown IllegalStateException");
      } catch (IllegalStateException expectedException) {
         assertEquals("ClassLoader has not been set", expectedException.getMessage());
      }
   }
   
   /**
    * Test the Bundle.getResources method with missing DeploymentUnit ClassLoader
    * 
    * @throws Exception
    */
   public void testGetResourcesUninstalled() throws Exception
   {
      getDeployerClient().change(deploymentUnit.getName(), DeploymentStages.NOT_INSTALLED);
      try {
         bundle.getResources("org/jboss/test/bundle/metadata/Manifest.mf");
         fail("Should have thrown IllegalStateException");
      } catch (IllegalStateException expectedException) {
         assertEquals("Bundle has been uninstalled", expectedException.getMessage());
      }
   }
}
