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

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.deployers.client.spi.main.MainDeployer;
import org.jboss.deployers.spi.deployer.DeploymentStage;
import org.jboss.deployers.spi.deployer.DeploymentStages;
import org.jboss.deployers.structure.spi.DeploymentContext;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.structure.spi.helpers.AbstractDeploymentContext;
import org.jboss.deployers.structure.spi.helpers.AbstractDeploymentUnit;
import org.jboss.osgi.plugins.facade.BundleImpl;
import org.jboss.test.BaseTestCase;
import org.jboss.test.bundle.support.TestControllerContext;
import org.jboss.test.bundle.support.TestMainDeployer;
import org.osgi.framework.Bundle;

/**
 * A BundleImplTestCase.
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public class BundleImplTestCase extends BaseTestCase
{
   private Bundle bundle;
   
   private DeploymentContext deploymentContext;

   private DeploymentUnit deploymentUnit;

   private ControllerContext controllerContext;

   private TestMainDeployer mainDeployer;

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

      deploymentContext = new AbstractDeploymentContext("UniqueName", "/simple.jar");
      deploymentContext.setClassLoader(BundleImplTestCase.class.getClassLoader());
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
    * Returns the id for a Bundle
    * 
    * @throws Exception
    */
   public void testGetBundleId() throws Exception
   {
      assertEquals("UniqueName".hashCode(), bundle.getBundleId());
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
    * Verifies the Bundle.stop method calls the MainDeployer.change method with the DESCRIBE DeploymentStage
    * 
    * @throws Exception
    */
   public void testStopBundle() throws Exception
   {
      bundle.stop();
      assertChangeRequested(deploymentUnit.getName(), DeploymentStages.DESCRIBE);
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
   public void testLoadClassNoClassLoader() throws Exception
   {
      deploymentContext.setClassLoader(null);
      try
      {
         bundle.loadClass("org.jboss.test.bundle.support.MissingClass");
         fail("Should have thrown ClassNotFoundException");
      }
      catch (ClassNotFoundException expectedException)
      {
         assertEquals("No classloader found for class: org.jboss.test.bundle.support.MissingClass", expectedException.getMessage());
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
   public void testGetResourceMissingClassLoader() throws Exception
   {
      deploymentContext.setClassLoader(null);
      try {
         bundle.getResource("org/jboss/test/bundle/metadata/Manifest.mf");
         fail("Should have thrown IllegalStateException");
      } catch (IllegalStateException expectedException) {
         assertEquals("ClassLoader has not been set", expectedException.getMessage());
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
   public void testGetResourcesMissingClassLoader() throws Exception
   {
      deploymentContext.setClassLoader(null);
      try {
         bundle.getResources("org/jboss/test/bundle/metadata/Manifest.mf");
         fail("Should have thrown IllegalStateException");
      } catch (IllegalStateException expectedException) {
         assertEquals("ClassLoader has not been set", expectedException.getMessage());
      }
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
