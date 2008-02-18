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

import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.classloading.spi.dependency.policy.mock.MockClassLoadingMetaData;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.classloading.dependency.support.a.A;
import org.jboss.test.classloading.dependency.support.b.B;

/**
 * DomainUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class DomainUnitTestCase extends AbstractMockClassLoaderUnitTest
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
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      a.setPathsAndPackageNames(A.class);
      KernelControllerContext contextA = install(a);
      try
      {
         ClassLoader clA = assertClassLoader(contextA);
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoClassLoader(contextA);
   }
   
   public void testSubDomainParentBefore() throws Exception
   {
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      a.setPathsAndPackageNames(A.class);
      KernelControllerContext contextA = install(a);
      try
      {
         ClassLoader clA = assertClassLoader(contextA);
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);

         MockClassLoadingMetaData b = new MockClassLoadingMetaData("b");
         b.setDomain("SubDomain");
         b.setPathsAndPackageNames(A.class, B.class);
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
   
   public void testSubDomainParentAfter() throws Exception
   {
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      a.setPathsAndPackageNames(A.class);
      KernelControllerContext contextA = install(a);
      try
      {
         ClassLoader clA = assertClassLoader(contextA);
         assertLoadClass(A.class, clA);
         assertLoadClassFail(B.class, clA);

         MockClassLoadingMetaData b = new MockClassLoadingMetaData("b");
         b.setDomain("SubDomain");
         b.setJ2seClassLoadingCompliance(false);
         b.setPathsAndPackageNames(A.class, B.class);
         KernelControllerContext contextB = install(b);
         try
         {
            assertLoadClass(A.class, clA);
            assertLoadClassFail(B.class, clA);
            ClassLoader clB = assertClassLoader(contextB);
            assertLoadClass(B.class, clB);
            assertLoadClass(A.class, clB);
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
   
   public void testParentDoesNotExist() throws Exception
   {
      MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
      a.setDomain("SubDomain");
      a.setParentDomain("DOESNOTEXIST");
      a.setPathsAndPackageNames(A.class);
      KernelControllerContext contextA = install(a);
      try
      {
         assertNoClassLoader(contextA);
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalStateException.class, t);
      }
      finally
      {
         uninstall(contextA);
      }
      assertNoClassLoader(contextA);
   }
   
   public void testParentDoesExists() throws Exception
   {
      ClassLoaderDomain domain = system.createAndRegisterDomain("TestDomain", ParentPolicy.BEFORE_BUT_JAVA_ONLY);
      try
      {
         MockClassLoadingMetaData a = new MockClassLoadingMetaData("a");
         a.setDomain("SubDomain");
         a.setParentDomain("TestDomain");
         a.setPathsAndPackageNames(A.class);
         KernelControllerContext contextA = install(a);
         try
         {
            ClassLoader clA = assertClassLoader(contextA);
            assertLoadClass(A.class, clA);
            assertLoadClassFail(B.class, clA);
         }
         catch (Throwable t)
         {
            checkThrowable(IllegalStateException.class, t);
         }
         finally
         {
            uninstall(contextA);
         }
         assertNoClassLoader(contextA);
      }
      finally
      {
         system.unregisterDomain(domain);
      }
   }
}
