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

import org.jboss.classloading.spi.metadata.ExportAll;
import org.jboss.classloading.spi.vfs.metadata.VFSClassLoaderFactory;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.test.classloading.vfs.metadata.VFSClassLoadingMicrocontainerTest;
import org.jboss.test.classloading.vfs.metadata.support.a.A;
import org.jboss.test.classloading.vfs.metadata.support.b.B;

/**
 * ImportAllUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ImportAllUnitTestCase extends VFSClassLoadingMicrocontainerTest
{
   public static Test suite()
   {
      return suite(ImportAllUnitTestCase.class);
   }

   public ImportAllUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testImportAll() throws Exception
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
         b.setImportAll(true);
         b.getRoots().add(getRoot(B.class));
         b.setIncludedPackages(B.class.getPackage().getName());
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
}
