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
package org.jboss.test.classloading.vfs.metadata.test;

import junit.framework.Test;

import org.jboss.classloading.spi.metadata.ClassLoadingMetaDataFactory;
import org.jboss.classloading.spi.vfs.metadata.VFSClassLoaderFactory;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.test.classloading.vfs.metadata.VFSClassLoadingMicrocontainerTest;
import org.jboss.test.classloading.vfs.metadata.support.a.A;
import org.jboss.test.classloading.vfs.metadata.support.b.B;
import org.jboss.test.classloading.vfs.metadata.support.c.C;

/**
 * ReExportPackageUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ReExportPackageUnitTestCase extends VFSClassLoadingMicrocontainerTest
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

      VFSClassLoaderFactory a = new VFSClassLoaderFactory("a");
      a.getRoots().add(getRoot(A.class));
      a.setIncludedPackages(A.class.getPackage().getName());
      a.getCapabilities().addCapability(factory.createModule("a"));
      a.getCapabilities().addCapability(factory.createPackage(A.class.getPackage().getName()));
      KernelDeployment depA = install(a);
      try
      {
         ClassLoader clA = assertClassLoader(a);
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);
         assertLoadClassFail(C.class, clA);

         VFSClassLoaderFactory b = new VFSClassLoaderFactory("b");
         b.getRoots().add(getRoot(B.class));
         b.setIncludedPackages(B.class.getPackage().getName());
         b.getCapabilities().addCapability(factory.createModule("b"));
         b.getCapabilities().addCapability(factory.createPackage(B.class.getPackage().getName()));
         b.getRequirements().addRequirement(factory.createReExportPackage(A.class.getPackage().getName()));
         KernelDeployment depB = install(b);
         try
         {
            assertLoadClass(A.class, clA);
            assertLoadClassFail(B.class, clA);
            assertLoadClassFail(C.class, clA);
            ClassLoader clB = assertClassLoader(b);
            assertLoadClass(A.class, clB, clA);
            assertLoadClass(B.class, clB);
            assertLoadClassFail(C.class, clB);

            VFSClassLoaderFactory c = new VFSClassLoaderFactory("c");
            c.getRoots().add(getRoot(C.class));
            c.setIncludedPackages(C.class.getPackage().getName());
            c.getRequirements().addRequirement(factory.createRequireModule("b"));
            KernelDeployment depC = install(c);
            try
            {
               assertLoadClass(A.class, clA);
               assertLoadClassFail(B.class, clA);
               assertLoadClassFail(C.class, clA);
               assertLoadClass(A.class, clB, clA);
               assertLoadClass(B.class, clB);
               assertLoadClassFail(C.class, clB);
               ClassLoader clC = assertClassLoader(c);
               assertLoadClass(A.class, clC, clA);
               assertLoadClass(B.class, clC, clB);
               assertLoadClass(C.class, clC);
            }
            finally
            {
               undeploy(depC);
            }
            assertNoClassLoader(c);
            assertLoadClass(A.class, clA);
            assertLoadClassFail(B.class, clA);
            assertLoadClassFail(C.class, clA);
            assertLoadClass(A.class, clB, clA);
            assertLoadClass(B.class, clB);
            assertLoadClassFail(C.class, clB);
         }
         finally
         {
            undeploy(depB);
         }
         assertNoClassLoader(b);
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);
         assertLoadClassFail(C.class, clA);
      }
      finally
      {
         undeploy(depA);
      }
      assertNoClassLoader(a);
   }
   
   public void testNoReExport() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();

      VFSClassLoaderFactory a = new VFSClassLoaderFactory("a");
      a.getRoots().add(getRoot(A.class));
      a.setIncludedPackages(A.class.getPackage().getName());
      a.getCapabilities().addCapability(factory.createModule("a"));
      a.getCapabilities().addCapability(factory.createPackage(A.class.getPackage().getName()));
      KernelDeployment depA = install(a);
      try
      {
         ClassLoader clA = assertClassLoader(a);
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);
         assertLoadClassFail(C.class, clA);

         VFSClassLoaderFactory b = new VFSClassLoaderFactory("b");
         b.getRoots().add(getRoot(B.class));
         b.setIncludedPackages(B.class.getPackage().getName());
         b.getCapabilities().addCapability(factory.createModule("b"));
         b.getCapabilities().addCapability(factory.createPackage(B.class.getPackage().getName()));
         b.getRequirements().addRequirement(factory.createReExportModule("a"));
         KernelDeployment depB = install(b);
         try
         {
            assertLoadClass(A.class, clA);
            assertLoadClassFail(B.class, clA);
            assertLoadClassFail(C.class, clA);
            ClassLoader clB = assertClassLoader(b);
            assertLoadClass(A.class, clB, clA);
            assertLoadClass(B.class, clB);
            assertLoadClassFail(C.class, clB);

            VFSClassLoaderFactory c = new VFSClassLoaderFactory("c");
            c.getRoots().add(getRoot(C.class));
            c.setIncludedPackages(C.class.getPackage().getName());
            c.getRequirements().addRequirement(factory.createRequirePackage(B.class.getPackage().getName()));
            KernelDeployment depC = install(c);
            try
            {
               assertLoadClass(A.class, clA);
               assertLoadClassFail(B.class, clA);
               assertLoadClassFail(C.class, clA);
               assertLoadClass(A.class, clB, clA);
               assertLoadClass(B.class, clB);
               assertLoadClassFail(C.class, clB);
               ClassLoader clC = assertClassLoader(c);
               assertLoadClassFail(A.class, clC);
               assertLoadClass(B.class, clC, clB);
               assertLoadClass(C.class, clC);
            }
            finally
            {
               undeploy(depC);
            }
            assertNoClassLoader(c);
            assertLoadClass(A.class, clA);
            assertLoadClassFail(B.class, clA);
            assertLoadClassFail(C.class, clA);
            assertLoadClass(A.class, clB, clA);
            assertLoadClass(B.class, clB);
            assertLoadClassFail(C.class, clB);
         }
         finally
         {
            undeploy(depB);
         }
         assertNoClassLoader(b);
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);
         assertLoadClassFail(C.class, clA);
      }
      finally
      {
         undeploy(depA);
      }
      assertNoClassLoader(a);
   }
}
