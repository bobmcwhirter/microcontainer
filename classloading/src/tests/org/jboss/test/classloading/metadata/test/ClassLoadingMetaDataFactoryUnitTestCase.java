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

import junit.framework.Test;

import org.jboss.classloading.plugins.metadata.ModuleCapability;
import org.jboss.classloading.plugins.metadata.ModuleRequirement;
import org.jboss.classloading.plugins.metadata.PackageCapability;
import org.jboss.classloading.plugins.metadata.PackageRequirement;
import org.jboss.classloading.plugins.metadata.UsesPackageRequirement;
import org.jboss.classloading.spi.metadata.Capability;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaDataFactory;
import org.jboss.classloading.spi.metadata.Requirement;
import org.jboss.classloading.spi.version.VersionRange;
import org.jboss.test.classloading.AbstractClassLoadingTestWithSecurity;

/**
 * ClassLoadingMetaDataFactoryUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ClassLoadingMetaDataFactoryUnitTestCase extends AbstractClassLoadingTestWithSecurity
{
   public static Test suite()
   {
      return suite(ClassLoadingMetaDataFactoryUnitTestCase.class);
   }

   public ClassLoadingMetaDataFactoryUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testCreateModuleNoVersion() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      Capability test = factory.createModule("test");
      assertEquals(new ModuleCapability("test"), test);
   }
   
   public void testCreateModuleVersioned() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      Capability test = factory.createModule("test", "1.0.0");
      assertEquals(new ModuleCapability("test", "1.0.0"), test);
   }
   
   public void testCreatePackageNoVersion() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      Capability test = factory.createPackage("test");
      assertEquals(new PackageCapability("test"), test);
   }
   
   public void testCreatePackageVersioned() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      Capability test = factory.createPackage("test", "1.0.0");
      assertEquals(new PackageCapability("test", "1.0.0"), test);
   }
   
   public void testCreateRequireModuleNoVersion() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      Requirement test = factory.createRequireModule("test");
      assertEquals(new ModuleRequirement("test"), test);
   }
   
   public void testCreateRequireModuleVersioned() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      VersionRange range = new VersionRange("1.0.0", "2.0.0");
      Requirement test = factory.createRequireModule("test", range);
      assertEquals(new ModuleRequirement("test", range), test);
   }
   
   public void testCreateRequireModuleVersionedOptionalReExportDynamic() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      VersionRange range = new VersionRange("1.0.0", "2.0.0");
      Requirement test = factory.createRequireModule("test", range, true, true, true);
      ModuleRequirement expected = new ModuleRequirement("test", range);
      expected.setOptional(true);
      expected.setReExport(true);
      expected.setDynamic(true);
      assertEquals(expected, test);
   }
   
   public void testCreateReExportModuleNoVersion() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      Requirement test = factory.createReExportModule("test");
      ModuleRequirement expected = new ModuleRequirement("test");
      expected.setReExport(true);
      assertEquals(expected, test);
   }
   
   public void testCreateReExportModuleVersioned() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      VersionRange range = new VersionRange("1.0.0", "2.0.0");
      Requirement test = factory.createReExportModule("test", range);
      ModuleRequirement expected = new ModuleRequirement("test", range);
      expected.setReExport(true);
      assertEquals(expected, test);
   }
   
   public void testCreateReExportModuleVersionedOptional() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      VersionRange range = new VersionRange("1.0.0", "2.0.0");
      Requirement test = factory.createReExportModule("test", range, true);
      ModuleRequirement expected = new ModuleRequirement("test", range);
      expected.setOptional(true);
      expected.setReExport(true);
      assertEquals(expected, test);
   }
   
   public void testCreateRequirePackageNoVersion() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      Requirement test = factory.createRequirePackage("test");
      assertEquals(new PackageRequirement("test"), test);
   }
   
   public void testCreateRequirePackageVersioned() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      VersionRange range = new VersionRange("1.0.0", "2.0.0");
      Requirement test = factory.createRequirePackage("test", range);
      assertEquals(new PackageRequirement("test", range), test);
   }
   
   public void testCreateRequirePackageVersionedOptionalReExportDynamic() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      VersionRange range = new VersionRange("1.0.0", "2.0.0");
      Requirement test = factory.createRequirePackage("test", range, true, true, true);
      PackageRequirement expected = new PackageRequirement("test", range);
      expected.setOptional(true);
      expected.setReExport(true);
      expected.setDynamic(true);
      assertEquals(expected, test);
   }
   
   public void testCreateReExportPackageNoVersion() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      Requirement test = factory.createReExportPackage("test");
      PackageRequirement expected = new PackageRequirement("test");
      expected.setReExport(true);
      assertEquals(expected, test);
   }
   
   public void testCreateReExportPackageVersioned() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      VersionRange range = new VersionRange("1.0.0", "2.0.0");
      Requirement test = factory.createReExportPackage("test", range);
      PackageRequirement expected = new PackageRequirement("test", range);
      expected.setReExport(true);
      assertEquals(expected, test);
   }
   
   public void testCreateReExportPackageVersionedOptional() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      VersionRange range = new VersionRange("1.0.0", "2.0.0");
      Requirement test = factory.createReExportPackage("test", range, true);
      PackageRequirement expected = new PackageRequirement("test", range);
      expected.setOptional(true);
      expected.setReExport(true);
      assertEquals(expected, test);
   }
   
   public void testCreateUsesPackageNoVersion() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      Requirement test = factory.createUsesPackage("test");
      UsesPackageRequirement expected = new UsesPackageRequirement("test");
      assertEquals(expected, test);
   }
   
   public void testCreateUsesPackageVersioned() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      VersionRange range = new VersionRange("1.0.0", "2.0.0");
      Requirement test = factory.createUsesPackage("test", range);
      UsesPackageRequirement expected = new UsesPackageRequirement("test", range);
      assertEquals(expected, test);
   }
   
   public void testCreateUsesPackageVersionedOptional() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      VersionRange range = new VersionRange("1.0.0", "2.0.0");
      Requirement test = factory.createUsesPackage("test", range, true);
      UsesPackageRequirement expected = new UsesPackageRequirement("test", range);
      expected.setReExport(true);
      assertEquals(expected, test);
   }
}
