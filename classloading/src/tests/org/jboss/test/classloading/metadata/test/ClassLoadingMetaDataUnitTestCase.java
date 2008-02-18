/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.test.classloading.metadata.test;

import java.util.Collection;
import java.util.Collections;

import junit.framework.Test;

import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.classloading.spi.metadata.CapabilitiesMetaData;
import org.jboss.classloading.spi.metadata.Capability;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaData;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaDataFactory;
import org.jboss.classloading.spi.metadata.ExportAll;
import org.jboss.classloading.spi.metadata.Requirement;
import org.jboss.classloading.spi.metadata.RequirementsMetaData;
import org.jboss.classloading.spi.version.Version;
import org.jboss.classloading.spi.version.VersionRange;
import org.jboss.test.classloading.AbstractClassLoadingTestWithSecurity;

/**
 * ClassLoadingMetaDataUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ClassLoadingMetaDataUnitTestCase extends AbstractClassLoadingTestWithSecurity
{
   public static Test suite()
   {
      return suite(ClassLoadingMetaDataUnitTestCase.class);
   }

   public ClassLoadingMetaDataUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testConstructor() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      assertNotNull(test.getName());
      assertEquals(Version.DEFAULT_VERSION, test.getVersion());
      assertNull(test.getDomain());
      assertNull(test.getParentDomain());
      assertNull(test.getExportAll());
      assertNull(test.getIncluded());
      assertNull(test.getExcluded());
      assertNull(test.getExcludedExport());
      assertNull(test.getIncludedPackages());
      assertNull(test.getExcludedPackages());
      assertNull(test.getExcludedExportPackages());
      assertFalse(test.isImportAll());
      assertTrue(test.isJ2seClassLoadingCompliance());
      assertNull(test.getCapabilities().getCapabilities());
      assertNull(test.getRequirements().getRequirements());
   }
   
   public void testSetName() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      assertNotNull(test.getName());
      test.setName("test");
      assertEquals("test", test.getName());
      
      try
      {
         test.setName(null);
      }
      catch (Throwable t)
      {
         checkDeepThrowable(IllegalArgumentException.class, t);
      }
      
      ClassLoadingMetaData test2 = new ClassLoadingMetaData();
      testEquals(test, test2, false);
      test2.setName("test");
      testEquals(test, test2, true);
   }
   
   public void testSetVersion() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      assertEquals(Version.DEFAULT_VERSION, test.getVersion());
      test.setVersion("1.0.0");
      assertEquals("1.0.0", test.getVersion());
      test.setVersion(null);
      assertEquals(Version.DEFAULT_VERSION, test.getVersion());

      test.setVersion("1.0.0");
      ClassLoadingMetaData test2 = new ClassLoadingMetaData();
      testEquals(test, test2, false);
      test2.setVersion("1.0.0");
      testEquals(test, test2, true);
   }

   public void testSetDomain() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      assertNull(test.getDomain());
      test.setDomain("test");
      assertEquals("test", test.getDomain());

      ClassLoadingMetaData test2 = new ClassLoadingMetaData();
      testEquals(test, test2, false);
      test2.setDomain("test");
      testEquals(test, test2, true);
   }
   
   public void testSetParentDomain() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      assertNull(test.getParentDomain());
      test.setParentDomain("test");
      assertEquals("test", test.getParentDomain());

      ClassLoadingMetaData test2 = new ClassLoadingMetaData();
      testEquals(test, test2, false);
      test2.setParentDomain("test");
      testEquals(test, test2, true);
   }
   
   public void testSetExportAll() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      assertNull(test.getExportAll());
      test.setExportAll(ExportAll.ALL);
      assertEquals(ExportAll.ALL, test.getExportAll());

      ClassLoadingMetaData test2 = new ClassLoadingMetaData();
      testEquals(test, test2, false);
      test2.setExportAll(ExportAll.ALL);
      testEquals(test, test2, true);
   }
   
   public void testSetIncludedPackages() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      assertNull(test.getIncludedPackages());
      test.setIncludedPackages("Included");
      assertEquals("Included", test.getIncludedPackages());
   }
   
   public void testSetExcludedPackages() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      assertNull(test.getExcludedPackages());
      test.setExcludedPackages("Excluded");
      assertEquals("Excluded", test.getExcludedPackages());
   }
   
   public void testSetExcludedExportPackages() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      assertNull(test.getExcludedExportPackages());
      test.setExcludedExportPackages("ExcludedExport");
      assertEquals("ExcludedExport", test.getExcludedExportPackages());
   }
   
   public void testSetIncluded() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      assertNull(test.getIncluded());
      test.setIncluded(ClassFilter.EVERYTHING);
      assertEquals(ClassFilter.EVERYTHING, test.getIncluded());
   }
   
   public void testSetExcluded() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      assertNull(test.getExcluded());
      test.setExcluded(ClassFilter.EVERYTHING);
      assertEquals(ClassFilter.EVERYTHING, test.getExcluded());
   }
   
   public void testSetExcludedExport() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      assertNull(test.getExcludedExport());
      test.setExcludedExport(ClassFilter.EVERYTHING);
      assertEquals(ClassFilter.EVERYTHING, test.getExcludedExport());
   }
   
   public void testGetIncluded() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      assertNull(test.getIncluded());
      
      test.setIncludedPackages("java.lang");
      ClassFilter filter = test.getIncluded();
      assertTrue(filter.matchesClassName(Object.class.getName()));
      assertFalse(filter.matchesClassName(Collection.class.getName()));
      
      test.setIncluded(ClassFilter.JAVA_ONLY);
      filter = test.getIncluded();
      assertTrue(filter.matchesClassName(Object.class.getName()));
      assertFalse(filter.matchesClassName(Collection.class.getName()));
      
      test.setIncludedPackages(null);
      filter = test.getIncluded();
      assertEquals(ClassFilter.JAVA_ONLY, filter);
      assertTrue(filter.matchesClassName(Object.class.getName()));
      assertTrue(filter.matchesClassName(Collection.class.getName()));
   }
   
   public void testGetExcluded() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      assertNull(test.getExcluded());
      
      test.setExcludedPackages("java.lang");
      ClassFilter filter = test.getExcluded();
      assertTrue(filter.matchesClassName(Object.class.getName()));
      assertFalse(filter.matchesClassName(Collection.class.getName()));
      
      test.setExcluded(ClassFilter.JAVA_ONLY);
      filter = test.getExcluded();
      assertTrue(filter.matchesClassName(Object.class.getName()));
      assertFalse(filter.matchesClassName(Collection.class.getName()));
      
      test.setExcludedPackages(null);
      filter = test.getExcluded();
      assertEquals(ClassFilter.JAVA_ONLY, filter);
      assertTrue(filter.matchesClassName(Object.class.getName()));
      assertTrue(filter.matchesClassName(Collection.class.getName()));
   }
   
   public void testGetExcludedExport() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      assertNull(test.getExcludedExport());
      
      test.setExcludedExportPackages("java.lang");
      ClassFilter filter = test.getExcludedExport();
      assertTrue(filter.matchesClassName(Object.class.getName()));
      assertFalse(filter.matchesClassName(Collection.class.getName()));
      
      test.setExcludedExport(ClassFilter.JAVA_ONLY);
      filter = test.getExcludedExport();
      assertTrue(filter.matchesClassName(Object.class.getName()));
      assertFalse(filter.matchesClassName(Collection.class.getName()));
      
      test.setExcludedExportPackages(null);
      filter = test.getExcludedExport();
      assertEquals(ClassFilter.JAVA_ONLY, filter);
      assertTrue(filter.matchesClassName(Object.class.getName()));
      assertTrue(filter.matchesClassName(Collection.class.getName()));
   }
   
   public void testSetImportAll() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      assertFalse(test.isImportAll());
      test.setImportAll(true);
      assertTrue(test.isImportAll());

      ClassLoadingMetaData test2 = new ClassLoadingMetaData();
      testEquals(test, test2, false);
      test2.setImportAll(true);
      testEquals(test, test2, true);
   }
   
   public void testJ2seClassLoadingComplaince() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      assertTrue(test.isJ2seClassLoadingCompliance());
      test.setJ2seClassLoadingCompliance(false);
      assertFalse(test.isJ2seClassLoadingCompliance());

      ClassLoadingMetaData test2 = new ClassLoadingMetaData();
      testEquals(test, test2, false);
      test2.setJ2seClassLoadingCompliance(false);
      testEquals(test, test2, true);
   }
   
   public void testCacheable() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      assertTrue(test.isCacheable());
      test.setCacheable(false);
      assertFalse(test.isCacheable());

      ClassLoadingMetaData test2 = new ClassLoadingMetaData();
      testEquals(test, test2, false);
      test2.setCacheable(false);
      testEquals(test, test2, true);
   }
   
   public void testBlackList() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      assertTrue(test.isBlackListable());
      test.setBlackListable(false);
      assertFalse(test.isBlackListable());

      ClassLoadingMetaData test2 = new ClassLoadingMetaData();
      testEquals(test, test2, false);
      test2.setBlackListable(false);
      testEquals(test, test2, true);
   }
   
   public void testCapabilities() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      CapabilitiesMetaData capabilitiesMetaData = test.getCapabilities();
      assertNull(capabilitiesMetaData.getCapabilities());
      Capability capability = factory.createModule("test");
      capabilitiesMetaData.addCapability(capability);
      assertEquals(Collections.singletonList(capability), capabilitiesMetaData.getCapabilities());

      ClassLoadingMetaData test2 = new ClassLoadingMetaData();
      testEquals(test, test2, false);
      test2.getCapabilities().addCapability(capability);
      testEquals(test, test2, true);
   }
   
   public void testRequirements() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      RequirementsMetaData requirementsMetaData = test.getRequirements();
      assertNull(requirementsMetaData.getRequirements());
      Requirement requirement = factory.createRequireModule("test");
      requirementsMetaData.addRequirement(requirement);
      assertEquals(Collections.singletonList(requirement), requirementsMetaData.getRequirements());

      ClassLoadingMetaData test2 = new ClassLoadingMetaData();
      testEquals(test, test2, false);
      test2.getRequirements().addRequirement(requirement);
      testEquals(test, test2, true);
   }
   
   public void testSerialization() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      test.setName("test");
      test.setVersion("1.0.0");
      test.setDomain("domain");
      test.setParentDomain("parent-domain");
      test.setExportAll(ExportAll.ALL);
      test.setImportAll(true);
      test.setJ2seClassLoadingCompliance(false);
      test.getCapabilities().addCapability(factory.createModule("module", "1.0.0"));
      test.getCapabilities().addCapability(factory.createPackage("package", "1.0.0"));
      test.getRequirements().addRequirement(factory.createRequireModule("module", new VersionRange("1.0.0", "2.0.0")));
      test.getRequirements().addRequirement(factory.createRequireModule("package", new VersionRange("1.0.0", "2.0.0")));
      ClassLoadingMetaData other = serializeDeserialize(test, ClassLoadingMetaData.class);
      assertEquals(test, other);
   }
   
   protected void testEquals(ClassLoadingMetaData test1, ClassLoadingMetaData test2, boolean result)
   {
      if (result)
      {
         assertTrue("Expected " + test1 + ".equals(" + test2 + ") to be true", test1.equals(test2));
         assertTrue("Expected " + test2 + ".equals(" + test1 + ") to be true", test2.equals(test1));
      }
      else
      {
         assertFalse("Expected " + test1 + ".equals(" + test2 + ") to be false", test1.equals(test2));
         assertFalse("Expected " + test2 + ".equals(" + test1 + ") to be false", test2.equals(test1));
      }
   }
}
