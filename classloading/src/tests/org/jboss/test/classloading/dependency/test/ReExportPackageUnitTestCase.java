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
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.classloading.dependency.support.a.A;
import org.jboss.test.classloading.dependency.support.b.B;
import org.jboss.test.classloading.dependency.support.c.C;

/**
 * ReExportPackageUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ReExportPackageUnitTestCase extends AbstractMockClassLoaderUnitTest
{
   public static Test suite()
   {
      return suite(ReExportPackageUnitTestCase.class);
   }

   public ReExportPackageUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testReExport() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      a.setPathsAndPackageNames(A.class);
      KernelControllerContext contextA = install(a);
      try
      {
         ClassLoader clA = assertClassLoader(contextA);
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);
         assertLoadClassFail(C.class, clA);

         MockClassLoadingMetaData b = new MockClassLoadingMetaData("b");
         b.getRequirements().addRequirement(factory.createReExportPackage(A.class.getPackage().getName()));
         b.setPathsAndPackageNames(B.class);
         KernelControllerContext contextB = install(b);
         try
         {
            assertLoadClass(A.class, clA);
            assertLoadClassFail(B.class, clA);
            assertLoadClassFail(C.class, clA);
            ClassLoader clB = assertClassLoader(contextB);
            assertLoadClass(A.class, clB, clA);
            assertLoadClass(B.class, clB);
            assertLoadClassFail(C.class, clB);

            MockClassLoadingMetaData c = new MockClassLoadingMetaData("c");
            c.getRequirements().addRequirement(factory.createRequireModule("b"));
            c.setPathsAndPackageNames(C.class);
            KernelControllerContext contextC = install(c);
            try
            {
               assertLoadClass(A.class, clA);
               assertLoadClassFail(B.class, clA);
               assertLoadClassFail(C.class, clA);
               assertLoadClass(A.class, clB, clA);
               assertLoadClass(B.class, clB);
               assertLoadClassFail(C.class, clB);
               ClassLoader clC = assertClassLoader(contextC);
               assertLoadClass(A.class, clC, clA);
               assertLoadClass(B.class, clC, clB);
               assertLoadClass(C.class, clC);
            }
            finally
            {
               uninstall(contextC);
            }
            assertNoClassLoader(contextC);
            assertLoadClass(A.class, clA);
            assertLoadClassFail(B.class, clA);
            assertLoadClassFail(C.class, clA);
            assertLoadClass(A.class, clB, clA);
            assertLoadClass(B.class, clB);
            assertLoadClassFail(C.class, clB);
         }
         finally
         {
            uninstall(contextB);
         }
         assertNoClassLoader(contextB);
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);
         assertLoadClassFail(C.class, clA);
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoClassLoader(contextA);
   }
   
   public void testNoReExport() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      a.setPathsAndPackageNames(A.class);
      KernelControllerContext contextA = install(a);
      try
      {
         ClassLoader clA = assertClassLoader(contextA);
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);
         assertLoadClassFail(C.class, clA);

         MockClassLoadingMetaData b = new MockClassLoadingMetaData("b");
         b.getRequirements().addRequirement(factory.createReExportPackage(A.class.getPackage().getName()));
         b.setPathsAndPackageNames(B.class);
         KernelControllerContext contextB = install(b);
         try
         {
            assertLoadClass(A.class, clA);
            assertLoadClassFail(B.class, clA);
            assertLoadClassFail(C.class, clA);
            ClassLoader clB = assertClassLoader(contextB);
            assertLoadClass(A.class, clB, clA);
            assertLoadClass(B.class, clB);
            assertLoadClassFail(C.class, clB);

            MockClassLoadingMetaData c = new MockClassLoadingMetaData("c");
            c.getRequirements().addRequirement(factory.createRequirePackage(B.class.getPackage().getName()));
            c.setPathsAndPackageNames(C.class);
            KernelControllerContext contextC = install(c);
            try
            {
               assertLoadClass(A.class, clA);
               assertLoadClassFail(B.class, clA);
               assertLoadClassFail(C.class, clA);
               assertLoadClass(A.class, clB, clA);
               assertLoadClass(B.class, clB);
               assertLoadClassFail(C.class, clB);
               ClassLoader clC = assertClassLoader(contextC);
               assertLoadClassFail(A.class, clC);
               assertLoadClass(B.class, clC, clB);
               assertLoadClass(C.class, clC);
            }
            finally
            {
               uninstall(contextC);
            }
            assertNoClassLoader(contextC);
            assertLoadClass(A.class, clA);
            assertLoadClassFail(B.class, clA);
            assertLoadClassFail(C.class, clA);
            assertLoadClass(A.class, clB, clA);
            assertLoadClass(B.class, clB);
            assertLoadClassFail(C.class, clB);
         }
         finally
         {
            uninstall(contextB);
         }
         assertNoClassLoader(contextB);
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);
         assertLoadClassFail(C.class, clA);
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoClassLoader(contextA);
   }
}
