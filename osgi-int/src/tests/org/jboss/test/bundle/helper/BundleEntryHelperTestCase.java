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

import java.net.URL;
import java.util.Enumeration;

import junit.framework.Test;

import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentUnit;
import org.jboss.osgi.plugins.facade.helpers.BundleEntryHelper;
import org.jboss.virtual.VirtualFile;

/**
 * A BundleEntryHelperTestCase.
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public class BundleEntryHelperTestCase extends AbstractBundleEntryTestCase
{
   private VirtualFile root;

   private DeploymentUnit deploymentUnit;

   /**
    * Create a new BundleEntryHelperTestCase.
    * 
    * @param name
    */
   public BundleEntryHelperTestCase(String name)
   {
      super(name);
   }

   /**
    * Get Suite for BundleEntryHelperTestCase
    * 
    * @return test suite
    */
   public static Test suite()
   {
      return suite(BundleEntryHelperTestCase.class);
   }

   /**
    * Setup the test
    */
   protected void setUp() throws Exception
   {
      super.setUp();
      deploymentUnit = addDeployment("/bundle", "simple.jar");
      root = VFSDeploymentUnit.class.cast(deploymentUnit).getRoot();
   }

   protected void tearDown() throws Exception
   {
      if (deploymentUnit != null)
      {
         try
         {
            removeDeployment(deploymentUnit);
         }
         catch (Exception ignored)
         {
            getLog().warn("Ignored undeployment error", ignored);
         }
         finally
         {
            deploymentUnit = null;
            root = null;
         }
      }
      super.tearDown();
   }

   /**
    * Test findEntryMethod
    * 
    * @throws Exception
    */
   public void testGetEntry() throws Exception
   {
      URL entry = BundleEntryHelper.getEntry(deploymentUnit, "META-INF/Manifest.mf");
      assertNotNull(entry);
      assertEntry(root, "META-INF/Manifest.mf", entry);
   }

   /**
    * Test findEntryMethod with no results
    * 
    * @throws Exception
    */
   public void testGetEntryNoResult() throws Exception
   {
      URL entry = BundleEntryHelper.getEntry(deploymentUnit, "META-INF/missing.mf");
      assertNull(entry);
   }

   /**
    * Test findEntryMethod
    * 
    * @throws Exception
    */
   @SuppressWarnings("unchecked")
   public void testGetEntryPaths() throws Exception
   {
      Enumeration<String> entries = BundleEntryHelper.getEntryPaths(deploymentUnit, "META-INF/");
      assertNotNull(entries);
      assertTrue(entries.hasMoreElements());
      assertEquals("simple.jar/META-INF/jboss-service.xml", entries.nextElement());
      assertTrue(entries.hasMoreElements());
      assertEquals("simple.jar/META-INF/Manifest.mf", entries.nextElement());
      assertFalse(entries.hasMoreElements());
      
   }

   /**
    * Test findEntryMethod with no results
    * 
    * @throws Exception
    */
   @SuppressWarnings("unchecked")
   public void testGetEntryPathsNoResult() throws Exception
   {
         // TODO MAKE WORK WITH MAVEN
         assertNull(BundleEntryHelper.getEntryPaths(deploymentUnit, "org/jboss/osgi/empty"));
   }

   /**
    * Test findEntryMethod with missing path
    * 
    * @throws Exception
    */
   public void testGetEntryPathsMissingDirPath() throws Exception
   {
         assertNull(BundleEntryHelper.getEntryPaths(deploymentUnit, "mising/path"));
   }

   /**
    * Test finding entries without wild cards (*)
    * 
    * @throws Exception
    */
   @SuppressWarnings("unchecked")
   public void testFindEntriesNoWildCard() throws Exception
   {
      Enumeration<URL> entries = BundleEntryHelper.findEntries(deploymentUnit, "/", "Manifest.mf", true);
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/META-INF/Manifest.mf", entries.nextElement());

      entries = BundleEntryHelper.findEntries(deploymentUnit, "/org", "custom.properties", true);
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/org/jboss/osgi/test/custom.properties", entries.nextElement());

      entries = BundleEntryHelper.findEntries(deploymentUnit, "/org/jboss", "custom.xml", true);
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/org/jboss/osgi/custom.xml", entries.nextElement());
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/org/jboss/osgi/test/custom.xml", entries.nextElement());

      entries = BundleEntryHelper.findEntries(deploymentUnit, "/", "fromroot.xml", true);
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/fromroot.xml", entries.nextElement());

      entries = BundleEntryHelper.findEntries(deploymentUnit, "/", "bogus.txt", true);
      assertNull(entries);
   }

   /**
    * Test finding entries without wild cards (*) or recursion 
    * 
    * @throws Exception
    */
   @SuppressWarnings("unchecked")
   public void testFindEntriesNoWildCardOrRecurse() throws Exception
   {
      Enumeration<URL> entries = BundleEntryHelper.findEntries(deploymentUnit, "/", "Manifest.mf", false);
      assertNull(entries);

      entries = BundleEntryHelper.findEntries(deploymentUnit, "/", "fromroot.xml", false);
      assertEntry(root, "/fromroot.xml", entries.nextElement());
      assertTrue(entries.hasMoreElements() == false);
   }

   /**
    * Test finding entries with wild cards (*)  
    * 
    * @throws Exception
    */
   @SuppressWarnings("unchecked")
   public void testFindEntriesWildCard() throws Exception
   {
      // starts with wildcard
      Enumeration<URL> entries = BundleEntryHelper.findEntries(deploymentUnit, "/", "*.mf", true);
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/META-INF/Manifest.mf", entries.nextElement());

      entries = BundleEntryHelper.findEntries(deploymentUnit, "/org/jboss", "*.properties", true);
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/org/jboss/osgi/test/custom.properties", entries.nextElement());

      // starts with wildcard and matches multiple
      entries = BundleEntryHelper.findEntries(deploymentUnit, "/", "*.xml", true);
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/META-INF/jboss-service.xml", entries.nextElement());
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/fromroot.xml", entries.nextElement());
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/org/jboss/osgi/custom.xml", entries.nextElement());
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/org/jboss/osgi/test/custom.xml", entries.nextElement());
      assertTrue(entries.hasMoreElements() == false);

      // ends in wildcard and matches multiple
      entries = BundleEntryHelper.findEntries(deploymentUnit, "org/", "custom*", true);
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/org/jboss/osgi/custom.xml", entries.nextElement());
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/org/jboss/osgi/test/custom.properties", entries.nextElement());
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/org/jboss/osgi/test/custom.xml", entries.nextElement());
      assertTrue(entries.hasMoreElements() == false);

      // begins and ends with wildcard
      entries = BundleEntryHelper.findEntries(deploymentUnit, "org/", "*xml*", true);
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/org/jboss/osgi/custom.xml", entries.nextElement());
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/org/jboss/osgi/notanxmlfile.txt", entries.nextElement());
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/org/jboss/osgi/test/custom.xml", entries.nextElement());
      assertTrue(entries.hasMoreElements() == false);

      // wildcard within
      entries = BundleEntryHelper.findEntries(deploymentUnit, "META-INF/", "jboss*.xml", true);
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/META-INF/jboss-service.xml", entries.nextElement());

      // several wildcards
      entries = BundleEntryHelper.findEntries(deploymentUnit, "org/", "*t*xml*", true);
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/org/jboss/osgi/custom.xml", entries.nextElement());
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/org/jboss/osgi/notanxmlfile.txt", entries.nextElement());
      assertTrue(entries.hasMoreElements());
      assertEntry(root, "/org/jboss/osgi/test/custom.xml", entries.nextElement());
      assertTrue(entries.hasMoreElements() == false);
   }

}
