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

import junit.framework.Test;

import org.jboss.classloading.spi.dependency.policy.mock.MockClassLoadingMetaData;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaDataFactory;
import org.jboss.classloading.spi.version.VersionRange;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.classloading.dependency.support.a.A;
import org.jboss.test.classloading.dependency.support.b.B;

/**
 * PackageDependencyUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class PackageDependencyUnitTestCase extends AbstractMockClassLoaderUnitTest
{
   public static Test suite()
   {
      return suite(PackageDependencyUnitTestCase.class);
   }

   public PackageDependencyUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testImportPackageNoVersionCheck() throws Exception
   {
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      a.getCapabilities().addCapability(factory.createModule("ModuleA"));
      a.getCapabilities().addCapability(factory.createPackage(A.class.getPackage().getName()));
      a.setPathsAndPackageNames(A.class);
      KernelControllerContext contextA = install(a);
      try
      {
         ClassLoader clA = assertClassLoader(contextA);
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);

         MockClassLoadingMetaData b = new MockClassLoadingMetaData("b");
         b.getRequirements().addRequirement(factory.createRequirePackage(A.class.getPackage().getName()));
         b.setPathsAndPackageNames(B.class);
         KernelControllerContext contextB = install(b);
         try
         {
            assertLoadClass(A.class, clA);
            assertLoadClassFail(B.class, clA);
            ClassLoader clB = assertClassLoader(contextB);
            assertLoadClass(B.class, clB);
            assertLoadClass(A.class, clB, clA);
         }
         finally
         {
            uninstall(contextB);
         }
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);
         assertNoClassLoader(contextB);
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoClassLoader(contextA);
   }
   
   public void testImportPackageVersionCheck() throws Exception
   {
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      a.getCapabilities().addCapability(factory.createModule("ModuleA"));
      a.getCapabilities().addCapability(factory.createPackage(A.class.getPackage().getName(), "1.0.0"));
      a.setPathsAndPackageNames(A.class);
      KernelControllerContext contextA = install(a);
      try
      {
         ClassLoader clA = assertClassLoader(contextA);
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);

         MockClassLoadingMetaData b = new MockClassLoadingMetaData("b");
         b.getRequirements().addRequirement(factory.createRequirePackage(A.class.getPackage().getName(), new VersionRange("1.0.0", "2.0.0")));
         b.setPathsAndPackageNames(B.class);
         KernelControllerContext contextB = install(b);
         try
         {
            assertLoadClass(A.class, clA);
            assertLoadClassFail(B.class, clA);
            ClassLoader clB = assertClassLoader(contextB);
            assertLoadClass(B.class, clB);
            assertLoadClass(A.class, clB, clA);
         }
         finally
         {
            uninstall(contextB);
         }
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);
         assertNoClassLoader(contextB);
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoClassLoader(contextA);
   }
   
   public void testImportPackageVersionCheckFailed() throws Exception
   {
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      a.getCapabilities().addCapability(factory.createModule("ModuleA"));
      a.getCapabilities().addCapability(factory.createPackage(A.class.getPackage().getName(), "3.0.0"));
      a.setPathsAndPackageNames(A.class);
      KernelControllerContext contextA = install(a);
      try
      {
         ClassLoader clA = assertClassLoader(contextA);
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);

         MockClassLoadingMetaData b = new MockClassLoadingMetaData("b");
         b.getRequirements().addRequirement(factory.createRequirePackage(A.class.getPackage().getName(), new VersionRange("1.0.0", "2.0.0")));
         b.setPathsAndPackageNames(B.class);
         KernelControllerContext contextB = install(b);
         try
         {
            assertLoadClass(A.class, clA);
            assertLoadClassFail(B.class, clA);
            assertNoClassLoader(contextB);
         }
         finally
         {
            uninstall(contextB);
         }
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);
         assertNoClassLoader(contextB);
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoClassLoader(contextA);
   }
}
