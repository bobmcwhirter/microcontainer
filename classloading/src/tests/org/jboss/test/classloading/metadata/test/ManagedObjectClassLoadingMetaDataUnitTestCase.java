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

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import junit.framework.Test;

import org.jboss.classloading.spi.metadata.CapabilitiesMetaData;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaData;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaDataFactory;
import org.jboss.classloading.spi.metadata.ExportAll;
import org.jboss.classloading.spi.metadata.RequirementsMetaData;
import org.jboss.classloading.spi.version.Version;
import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.api.ManagedProperty;
import org.jboss.managed.api.factory.ManagedObjectFactory;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.MetaTypeFactory;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.MetaValueFactory;
import org.jboss.test.BaseTestCase;

/**
 * ManagedObjectClassLoadingMetaDataUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ManagedObjectClassLoadingMetaDataUnitTestCase extends BaseTestCase
{
   private ManagedObjectFactory moFactory = ManagedObjectFactory.getInstance();
   private MetaTypeFactory mtFactory = MetaTypeFactory.getInstance();
   private MetaValueFactory mvFactory = MetaValueFactory.getInstance();
   
   public static Test suite()
   {
      return suite(ManagedObjectClassLoadingMetaDataUnitTestCase.class);
   }

   public ManagedObjectClassLoadingMetaDataUnitTestCase(String name)
   {
      super(name);
   }
   
   protected ManagedObject assertManagedObject(ClassLoadingMetaData test)
   {
      ManagedObject result = moFactory.initManagedObject(test, null, null);
      assertNotNull(result);
      List<String> expectedProperties = Arrays.asList("name", "version", "domain", "parentDomain", "exportAll", "included", "excluded", "excludedExport", "importAll", "parentFirst", "cache", "blackList", "capabilities", "requirements");
      Set<String> actualProperties = result.getPropertyNames();
      for (String expected : expectedProperties)
      {
         if (actualProperties.contains(expected) == false)
            fail("Expected property: " + expected);
      }
      for (String actual : actualProperties)
      {
         if (expectedProperties.contains(actual) == false)
            fail("Did not expect property: " + actual);
      }
      return result;
   }
   
   protected ManagedProperty assertManagedProperty(ManagedObject mo, String name, MetaType metaType, MetaValue metaValue)
   {
      ManagedProperty property = mo.getProperty(name);
      assertNotNull("No property " + name, property);
      assertEquals(metaType, property.getMetaType());
      assertEquals(metaValue, property.getValue());
      return property;
   }
   
   protected <T> ManagedProperty assertManagedProperty(ManagedObject mo, String name, Class<T> type, T value)
   {
      MetaType metaType = mtFactory.resolve(type);

      MetaValue metaValue = null;
      if (value != null)
         metaValue = mvFactory.create(value);
      return assertManagedProperty(mo, name, metaType, metaValue);
   }
   
   public void testConstructor() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      ManagedObject mo = assertManagedObject(test);
      assertManagedProperty(mo, "name", String.class, "<unknown>");
      assertManagedProperty(mo, "version", Version.class, Version.DEFAULT_VERSION);
      assertManagedProperty(mo, "domain", String.class, null);
      assertManagedProperty(mo, "parentDomain", String.class, null);
      assertManagedProperty(mo, "exportAll", ExportAll.class, null);
      assertManagedProperty(mo, "included", String.class, null);
      assertManagedProperty(mo, "excluded", String.class, null);
      assertManagedProperty(mo, "excludedExport", String.class, null);
      assertManagedProperty(mo, "importAll", Boolean.class, false);
      assertManagedProperty(mo, "parentFirst", Boolean.class, true);
      assertManagedProperty(mo, "capabilities", CapabilitiesMetaData.class, new CapabilitiesMetaData());
      assertManagedProperty(mo, "requirements", RequirementsMetaData.class, new RequirementsMetaData());
   }
   
   public void testSetName() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      test.setName("test");
      ManagedObject mo = assertManagedObject(test);
      assertManagedProperty(mo, "name", String.class, "test");
   }
   
   public void testSetVersion() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      test.setVersion("1.0.0");
      ManagedObject mo = assertManagedObject(test);
      assertManagedProperty(mo, "version", Version.class, Version.parseVersion("1.0.0"));
   }

   public void testSetDomain() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      test.setDomain("domain");
      ManagedObject mo = assertManagedObject(test);
      assertManagedProperty(mo, "domain", String.class, "domain");
   }
   
   public void testSetParentDomain() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      test.setParentDomain("parentDomain");
      ManagedObject mo = assertManagedObject(test);
      assertManagedProperty(mo, "parentDomain", String.class, "parentDomain");
   }
   
   public void testSetExportAll() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      test.setExportAll(ExportAll.ALL);
      ManagedObject mo = assertManagedObject(test);
      assertManagedProperty(mo, "exportAll", ExportAll.class, ExportAll.ALL);
   }
   
   public void testSetIncludedPackages() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      test.setIncludedPackages("Included");
      ManagedObject mo = assertManagedObject(test);
      assertManagedProperty(mo, "included", String.class, "Included");
   }
   
   public void testSetExcludedPackages() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      test.setExcludedPackages("Excluded");
      ManagedObject mo = assertManagedObject(test);
      assertManagedProperty(mo, "excluded", String.class, "Excluded");
   }
   
   public void testSetExcludedExportPackages() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      test.setExcludedExportPackages("ExcludedExport");
      ManagedObject mo = assertManagedObject(test);
      assertManagedProperty(mo, "excludedExport", String.class, "ExcludedExport");
   }
   
   public void testSetImportAll() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      test.setImportAll(true);
      ManagedObject mo = assertManagedObject(test);
      assertManagedProperty(mo, "importAll", Boolean.class, true);
   }
   
   public void testJ2seClassLoadingComplaince() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      test.setJ2seClassLoadingCompliance(false);
      ManagedObject mo = assertManagedObject(test);
      assertManagedProperty(mo, "parentFirst", Boolean.class, false);
   }
   
   public void testCacheable() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      test.setCacheable(false);
      ManagedObject mo = assertManagedObject(test);
      assertManagedProperty(mo, "cache", Boolean.class, false);
   }
   
   public void testBlackList() throws Exception
   {
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      test.setBlackListable(false);
      ManagedObject mo = assertManagedObject(test);
      assertManagedProperty(mo, "blackList", Boolean.class, false);
   }
   
   public void testCapabilities() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      test.getCapabilities().addCapability(factory.createModule("module"));
      test.getCapabilities().addCapability(factory.createPackage("package"));
      ManagedObject mo = assertManagedObject(test);
      assertManagedProperty(mo, "requirements", RequirementsMetaData.class, test.getRequirements());
   }
   
   public void testRequirements() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      ClassLoadingMetaData test = new ClassLoadingMetaData();
      test.getRequirements().addRequirement(factory.createRequireModule("module"));
      test.getRequirements().addRequirement(factory.createRequirePackage("package"));
      ManagedObject mo = assertManagedObject(test);
      assertManagedProperty(mo, "requirements", RequirementsMetaData.class, test.getRequirements());
   }
}
