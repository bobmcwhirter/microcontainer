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
package org.jboss.test.classloading.dependency.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Test;

import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.classloading.spi.dependency.policy.mock.MockClassLoaderPolicyModule;
import org.jboss.classloading.spi.dependency.policy.mock.MockClassLoadingMetaData;
import org.jboss.classloading.spi.metadata.Capability;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaDataFactory;
import org.jboss.classloading.spi.metadata.ExportAll;
import org.jboss.classloading.spi.metadata.Requirement;
import org.jboss.classloading.spi.version.Version;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.classloading.dependency.support.a.A;
import org.jboss.test.classloading.dependency.support.b.B;

/**
 * MockClassLoadingMetaDataUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MockClassLoadingMetaDataUnitTestCase extends AbstractMockClassLoaderUnitTest
{
   public static Test suite()
   {
      return suite(MockClassLoadingMetaDataUnitTestCase.class);
   }

   public MockClassLoadingMetaDataUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testName() throws Exception
   {
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      KernelControllerContext contextA = install(a);
      try
      {
         MockClassLoaderPolicyModule module = assertModule(contextA);
         assertEquals("a", module.getName());
         assertEquals(Version.DEFAULT_VERSION, module.getVersion());
         assertNull(module.getDomainName());
         assertNull(module.getParentDomainName());
         assertNull(module.getExportAll());
         assertNull(module.getIncluded());
         assertNull(module.getExcluded());
         assertNull(module.getExcludedExport());
         assertFalse(module.isImportAll());
         assertTrue(module.isJ2seClassLoadingCompliance());
         ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
         Capability capability = factory.createModule("a");
         assertEquals(Collections.singletonList(capability), module.getCapabilities());
         assertNull(module.getRequirements());
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoModule(contextA);
   }
   
   public void testVersion() throws Exception
   {
      Version version = Version.parseVersion("1.0.0");
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a", version);
      KernelControllerContext contextA = install(a);
      try
      {
         MockClassLoaderPolicyModule module = assertModule(contextA);
         assertEquals(version, module.getVersion());
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoModule(contextA);
   }
   
   public void testDomainName() throws Exception
   {
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      a.setDomain("DomainName");
      KernelControllerContext contextA = install(a);
      try
      {
         MockClassLoaderPolicyModule module = assertModule(contextA);
         assertEquals("DomainName", module.getDomainName());
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoModule(contextA);
   }
   
   public void testParentDomainName() throws Exception
   {
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      a.setDomain("DomainName");
      a.setParentDomain(ClassLoaderSystem.DEFAULT_DOMAIN_NAME);
      KernelControllerContext contextA = install(a);
      try
      {
         MockClassLoaderPolicyModule module = assertModule(contextA);
         assertEquals(ClassLoaderSystem.DEFAULT_DOMAIN_NAME, module.getParentDomainName());
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoModule(contextA);
   }
   
   public void testExportAll() throws Exception
   {
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      a.setExportAll(ExportAll.ALL);
      KernelControllerContext contextA = install(a);
      try
      {
         MockClassLoaderPolicyModule module = assertModule(contextA);
         assertEquals(ExportAll.ALL, module.getExportAll());
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoModule(contextA);
   }
   
   public void testIncluded() throws Exception
   {
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      a.setIncluded(ClassFilter.JAVA_ONLY);
      KernelControllerContext contextA = install(a);
      try
      {
         MockClassLoaderPolicyModule module = assertModule(contextA);
         assertEquals(ClassFilter.JAVA_ONLY, module.getIncluded());
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoModule(contextA);
   }
   
   public void testExcluded() throws Exception
   {
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      a.setExcluded(ClassFilter.JAVA_ONLY);
      KernelControllerContext contextA = install(a);
      try
      {
         MockClassLoaderPolicyModule module = assertModule(contextA);
         assertEquals(ClassFilter.JAVA_ONLY, module.getExcluded());
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoModule(contextA);
   }
   
   public void testExcludedExport() throws Exception
   {
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      a.setExcludedExport(ClassFilter.JAVA_ONLY);
      KernelControllerContext contextA = install(a);
      try
      {
         MockClassLoaderPolicyModule module = assertModule(contextA);
         assertEquals(ClassFilter.JAVA_ONLY, module.getExcludedExport());
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoModule(contextA);
   }
   
   public void testImportAll() throws Exception
   {
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      a.setImportAll(true);
      KernelControllerContext contextA = install(a);
      try
      {
         MockClassLoaderPolicyModule module = assertModule(contextA);
         assertTrue(module.isImportAll());
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoModule(contextA);
   }
   
   public void testJ2seClassLoading() throws Exception
   {
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      a.setJ2seClassLoadingCompliance(false);
      KernelControllerContext contextA = install(a);
      try
      {
         MockClassLoaderPolicyModule module = assertModule(contextA);
         assertFalse(module.isJ2seClassLoadingCompliance());
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoModule(contextA);
   }
   
   public void testManualCapabilities() throws Exception
   {
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      List<Capability> capabilities = new ArrayList<Capability>();
      capabilities.add(factory.createModule("b"));
      capabilities.add(factory.createPackage("p1"));
      capabilities.add(factory.createPackage("p2"));
      a.setCapabilities(capabilities);
      KernelControllerContext contextA = install(a);
      try
      {
         MockClassLoaderPolicyModule module = assertModule(contextA);
         assertEquals(capabilities, module.getCapabilities());
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoModule(contextA);
   }
   
   public void testAutoCapabilities() throws Exception
   {
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      a.setPathsAndPackageNames(A.class, B.class);
      KernelControllerContext contextA = install(a);
      try
      {
         MockClassLoaderPolicyModule module = assertModule(contextA);
         ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
         List<Capability> capabilities = new ArrayList<Capability>();
         capabilities.add(factory.createModule("a"));
         capabilities.add(factory.createPackage(A.class.getPackage().getName()));
         capabilities.add(factory.createPackage(B.class.getPackage().getName()));
         assertEquals(capabilities, module.getCapabilities());
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoModule(contextA);
   }
   
   public void testRequirements() throws Exception
   {
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      List<Requirement> requirements = new ArrayList<Requirement>();
      requirements.add(factory.createRequireModule("b"));
      requirements.add(factory.createRequirePackage(B.class.getPackage().getName()));
      a.setRequirements(requirements);
      KernelControllerContext contextA = install(a);
      try
      {
         MockClassLoadingMetaData b = new MockClassLoadingMetaData("b");
         b.setPathsAndPackageNames(B.class);
         KernelControllerContext contextB = install(b);
         try
         {
            MockClassLoaderPolicyModule module = assertModule(contextA);
            assertEquals(requirements, module.getRequirements());
         }
         finally
         {
            uninstall(contextB);
         }
         assertNoModule(contextB);
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoModule(contextA);
   }
}
