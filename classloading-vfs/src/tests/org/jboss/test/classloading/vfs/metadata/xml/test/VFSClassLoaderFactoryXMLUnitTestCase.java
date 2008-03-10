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
package org.jboss.test.classloading.vfs.metadata.xml.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Test;

import org.jboss.classloading.spi.metadata.Capability;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaDataFactory;
import org.jboss.classloading.spi.metadata.ExportAll;
import org.jboss.classloading.spi.metadata.Requirement;
import org.jboss.classloading.spi.version.Version;
import org.jboss.classloading.spi.version.VersionRange;
import org.jboss.classloading.spi.vfs.metadata.VFSClassLoaderFactory;
import org.jboss.classloading.spi.vfs.metadata.VFSClassLoaderFactory10;
import org.jboss.test.classloading.vfs.metadata.xml.AbstractJBossXBTest;
import org.jboss.test.classloading.vfs.metadata.xml.support.TestCapability;
import org.jboss.test.classloading.vfs.metadata.xml.support.TestRequirement;

/**
 * VFSClassLoaderFactoryXMLUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class VFSClassLoaderFactoryXMLUnitTestCase extends AbstractJBossXBTest
{
   public static Test suite()
   {
      return suite(VFSClassLoaderFactoryXMLUnitTestCase.class);
   }

   public VFSClassLoaderFactoryXMLUnitTestCase(String name)
   {
      super(name);
   }

   public void testModuleName() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      assertEquals("test", result.getName());
      assertEquals(Version.DEFAULT_VERSION, result.getVersion());
      assertNull(result.getContextName());
      assertNull(result.getDomain());
      assertNull(result.getParentDomain());
      assertNull(result.getExportAll());
      assertNull(result.getIncludedPackages());
      assertNull(result.getExcludedPackages());
      assertNull(result.getExcludedExportPackages());
      assertFalse(result.isImportAll());
      assertTrue(result.isJ2seClassLoadingCompliance());
      assertTrue(result.isCacheable());
      assertTrue(result.isBlackListable());
      assertEmpty(result.getRoots());
      assertEquals(VFSClassLoaderFactory.DEFAULT_CLASSLOADER_SYSTEM_NAME, result.getClassLoaderSystemName());
      assertNull(result.getCapabilities().getCapabilities());
      assertNull(result.getRequirements().getRequirements());
   }

   public void testModuleVersion() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      assertEquals(Version.parseVersion("1.0.0"), result.getVersion());
   }

   public void testModuleContext() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      assertEquals("Context", result.getContextName());
   }

   public void testModuleDomain() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      assertEquals("testDomain", result.getDomain());
   }

   public void testModuleParentDomain() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      assertEquals("testParentDomain", result.getParentDomain());
   }

   public void testModuleExportAll() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      assertEquals(ExportAll.ALL, result.getExportAll());
   }

   public void testModuleIncluded() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      assertEquals("Included", result.getIncludedPackages());
   }

   public void testModuleExcluded() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      assertEquals("Excluded", result.getExcludedPackages());
   }

   public void testModuleExcludedExport() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      assertEquals("ExcludedExport", result.getExcludedExportPackages());
   }

   public void testModuleImportAll() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      assertTrue(result.isImportAll());
   }

   public void testModuleJ2seClassLoadingCompliance() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      assertFalse(result.isJ2seClassLoadingCompliance());
   }

   public void testModuleCache() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      assertFalse(result.isCacheable());
   }

   public void testModuleBlackList() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      assertFalse(result.isBlackListable());
   }

   public void testModuleSystem() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      assertEquals("System", result.getClassLoaderSystemName());
   }

   public void testModuleRoots() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      assertEquals(Arrays.asList(new String[] { "root1", "root2", "root3" }), result.getRoots());
   }

   public void testExportOneModuleNoVersion() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertCapabilities(result, factory.createModule("export1"));
   }

   public void testExportOneModuleVersioned() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertCapabilities(result, factory.createModule("export1", "1.0.0"));
   }

   public void testExportThreeModules() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertCapabilities(result, factory.createModule("export1", "1.0.0"), 
                                 factory.createModule("export2", "2.0.0"), 
                                 factory.createModule("export3", "3.0.0"));
   }

   public void testExportOnePackageNoVersion() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertCapabilities(result, factory.createPackage("export1"));
   }

   public void testExportOnePackageVersioned() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertCapabilities(result, factory.createPackage("export1", "1.0.0"));
   }

   public void testExportThreePackages() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertCapabilities(result, factory.createPackage("export1", "1.0.0"), 
                                 factory.createPackage("export2", "2.0.0"), 
                                 factory.createPackage("export3", "3.0.0"));
   }

   public void testImportOneModuleNoVersion() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertRequirements(result, factory.createRequireModule("export1"));
   }

   public void testImportOneModuleVersioned() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertRequirements(result, factory.createRequireModule("export1", new VersionRange("1.0.0", "2.0.0")));
   }

   public void testImportThreeModules() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertRequirements(result, factory.createRequireModule("export1", new VersionRange("1.0.0", "1.1.0")), 
                                 factory.createRequireModule("export2", new VersionRange("2.0.0", "2.1.0")), 
                                 factory.createRequireModule("export3", new VersionRange("3.0.0", "3.1.0")));
   }

   public void testImportOnePackageNoVersion() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertRequirements(result, factory.createRequirePackage("export1"));
   }

   public void testImportOnePackageVersioned() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertRequirements(result, factory.createRequirePackage("export1", new VersionRange("1.0.0", "2.0.0")));
   }

   public void testImportThreePackages() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertRequirements(result, factory.createRequirePackage("export1", new VersionRange("1.0.0", "1.1.0")), 
                                 factory.createRequirePackage("export2", new VersionRange("2.0.0", "2.1.0")), 
                                 factory.createRequirePackage("export3", new VersionRange("3.0.0", "3.1.0")));
   }

   public void testImportVersionRange() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertRequirements(result, factory.createRequireModule("export1"), 
                                 factory.createRequireModule("export2", new VersionRange("1.0.0")), 
                                 factory.createRequireModule("export3", new VersionRange("0.0.0", "1.0.0")),
                                 factory.createRequireModule("export4", new VersionRange("1.0.0", "2.0.0")),
                                 factory.createRequireModule("export5", new VersionRange("1.0.0", false, "2.0.0", false)),
                                 factory.createRequireModule("export6", new VersionRange("1.0.0", false, "2.0.0", true)),
                                 factory.createRequireModule("export7", new VersionRange("1.0.0", true, "2.0.0", false)),
                                 factory.createRequireModule("export8", new VersionRange("1.0.0", true, "2.0.0", true)));
   }

   public void testExportImportMixed() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal();
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertCapabilities(result, factory.createModule("test2", "2.0.0"), 
                                 factory.createPackage("test2", "2.0.0"), 
                                 factory.createModule("test1", "1.0.0"),
                                 factory.createPackage("test1", "1.0.0"));
      assertRequirements(result, factory.createRequireModule("test2", new VersionRange("2.0.0")), 
                                 factory.createRequirePackage("test2", new VersionRange("2.0.0")), 
                                 factory.createRequireModule("test1", new VersionRange("1.0.0")),
                                 factory.createRequirePackage("test1", new VersionRange("1.0.0")));
   }

   public void testWildcardCapability() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal(TestCapability.class);
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertCapabilities(result, factory.createModule("test1", "1.0.0"), 
                                 factory.createPackage("test1", "1.0.0"),
                                 new TestCapability("test", "1.0.0"));
   }

   public void testWildcardRequirement() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal(TestRequirement.class);
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertRequirements(result, factory.createRequireModule("test1", new VersionRange("1.0.0")), 
                                 factory.createRequirePackage("test1", new VersionRange("1.0.0")),
                                 new TestRequirement("test", new VersionRange("1.0.0")));
   }

   public void testOptionalRequirement() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal(TestRequirement.class);
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertRequirements(result, factory.createRequireModule("test1", new VersionRange("1.0.0"), true, false, false), 
                                 factory.createRequirePackage("test1", new VersionRange("1.0.0"), true, false, false));
   }

   public void testReExportRequirement() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal(TestRequirement.class);
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertRequirements(result, factory.createReExportModule("test1", new VersionRange("1.0.0")), 
                                 factory.createReExportPackage("test1", new VersionRange("1.0.0")));
   }

   public void testDynamicRequirement() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal(TestRequirement.class);
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertRequirements(result, factory.createRequireModule("test1", new VersionRange("1.0.0"), false, false, true), 
                                 factory.createRequirePackage("test1", new VersionRange("1.0.0"), false, false, true));
   }

   public void testUsesRequirement() throws Exception
   {
      VFSClassLoaderFactory result = unmarshal(TestRequirement.class);
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      assertRequirements(result, factory.createUsesPackage("test1", new VersionRange("1.0.0"))); 
   }
 
   public void assertCapabilities(VFSClassLoaderFactory metadata, Capability... expected)
   {
      List<Capability> temp = new ArrayList<Capability>();
      for (Capability capability : expected)
         temp.add(capability);
      assertEquals(temp, metadata.getCapabilities().getCapabilities());
   }

   public void assertRequirements(org.jboss.classloading.spi.vfs.metadata.VFSClassLoaderFactory metadata, Requirement... expected)
   {
      List<Requirement> temp = new ArrayList<Requirement>();
      for (Requirement requirement : expected)
         temp.add(requirement);
      assertEquals(temp, metadata.getRequirements().getRequirements());
   }
   
   protected VFSClassLoaderFactory unmarshal(Class<?>... extra) throws Exception
   {
      return unmarshalObject(VFSClassLoaderFactory10.class, VFSClassLoaderFactory10.class, extra);
   }
}
