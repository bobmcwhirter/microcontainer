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

import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.classloading.spi.metadata.ExportAll;
import org.jboss.classloading.spi.vfs.metadata.VFSClassLoaderFactory;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.test.classloading.vfs.metadata.VFSClassLoadingMicrocontainerTest;
import org.jboss.test.classloading.vfs.metadata.support.a.A;
import org.jboss.test.classloading.vfs.metadata.support.b.B;

/**
 * DomainUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class DomainUnitTestCase extends VFSClassLoadingMicrocontainerTest
{
   public static Test suite()
   {
      return suite(DomainUnitTestCase.class);
   }

   public DomainUnitTestCase(String name)
   {
      super(name);
   }

   public void testDefaultDomain() throws Exception
   {
      VFSClassLoaderFactory a = new VFSClassLoaderFactory("a");
      a.getRoots().add(getRoot(A.class));
      a.setIncludedPackages(A.class.getPackage().getName());
      KernelDeployment depA = install(a);
      try
      {
         validate();
         ClassLoader clA = assertClassLoader(a);
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);
      }
      finally
      {
         undeploy(depA);
      }
      validate();
      assertNoClassLoader(a);
   }

   public void testSubDomainParentBefore() throws Exception
   {
      VFSClassLoaderFactory a = new VFSClassLoaderFactory("a");
      a.setExportAll(ExportAll.NON_EMPTY);
      a.getRoots().add(getRoot(A.class));
      a.setIncludedPackages(A.class.getPackage().getName());
      KernelDeployment depA = install(a);
      try
      {
         validate();
         ClassLoader clA = assertClassLoader(a);
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);
         
         VFSClassLoaderFactory b = new VFSClassLoaderFactory("b");
         b.setDomain("SubDomainBefore");
         b.getRoots().add(getRoot(B.class));
         b.setIncludedPackages(A.class.getPackage().getName() + "," + B.class.getPackage().getName());
         KernelDeployment depB = install(b);
         try
         {
            assertLoadClass(A.class, clA);
            assertLoadClassFail(B.class, clA);
            ClassLoader clB = assertClassLoader(b);
            assertLoadClass(B.class, clB);
            assertLoadClass(A.class, clB, clA);
         }
         finally
         {
            undeploy(depB);
         }
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);
         assertNoClassLoader(b);
      }
      finally
      {
         undeploy(depA);
      }
      assertNoClassLoader(a);
   }
   
   public void testSubDomainParentAfter() throws Exception
   {
      VFSClassLoaderFactory a = new VFSClassLoaderFactory("a");
      a.setExportAll(ExportAll.NON_EMPTY);
      a.getRoots().add(getRoot(A.class));
      a.setIncludedPackages(A.class.getPackage().getName());
      KernelDeployment depA = install(a);
      try
      {
         ClassLoader clA = assertClassLoader(a);
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);

         VFSClassLoaderFactory b = new VFSClassLoaderFactory("b");
         b.setDomain("SubDomainAfter");
         b.setJ2seClassLoadingCompliance(false);
         b.getRoots().add(getRoot(B.class));
         b.setIncludedPackages(A.class.getPackage().getName() + "," + B.class.getPackage().getName());
         KernelDeployment depB = install(b);
         try
         {
            assertLoadClass(A.class, clA);
            assertLoadClassFail(B.class, clA);
            ClassLoader clB = assertClassLoader(b);
            assertLoadClass(B.class, clB);
            assertLoadClass(A.class, clB);
         }
         finally
         {
            undeploy(depB);
         }
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);
         assertNoClassLoader(b);
      }
      finally
      {
         undeploy(depA);
      }
      assertNoClassLoader(a);
   }

   public void testParentDoesNotExist() throws Exception
   {
      VFSClassLoaderFactory a = new VFSClassLoaderFactory("a");
      a.setDomain("SubDomain");
      a.setParentDomain("DOESNOTEXIST");
      a.getRoots().add(getRoot(A.class));
      a.setIncludedPackages(A.class.getPackage().getName());
      KernelDeployment depA = install(a);
      try
      {
         assertNoClassLoader(a);
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalStateException.class, t);
      }
      finally
      {
         undeploy(depA);
      }
      assertNoClassLoader(a);
   }
   
   public void testParentDoesExists() throws Exception
   {
      ClassLoaderDomain domain = system.createAndRegisterDomain("TestDomain", ParentPolicy.BEFORE_BUT_JAVA_ONLY);
      try
      {
         VFSClassLoaderFactory a = new VFSClassLoaderFactory("a");
         a.setDomain("SubDomain");
         a.setParentDomain("TestDomain");
         a.getRoots().add(getRoot(A.class));
         a.setIncludedPackages(A.class.getPackage().getName());
         KernelDeployment depA = install(a);
         try
         {
            ClassLoader clA = assertClassLoader(a);
            assertLoadClass(A.class, clA);
            assertLoadClassFail(B.class, clA);
         }
         catch (Throwable t)
         {
            checkThrowable(IllegalStateException.class, t);
         }
         finally
         {
            undeploy(depA);
         }
         assertNoClassLoader(a);
      }
      finally
      {
         system.unregisterDomain(domain);
      }
   }
}
