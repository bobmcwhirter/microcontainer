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
package org.jboss.test.classloading.dependency;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jboss.test.classloading.dependency.test.DependencyUnitTestCase;
import org.jboss.test.classloading.dependency.test.DomainUnitTestCase;
import org.jboss.test.classloading.dependency.test.ImportAllUnitTestCase;
import org.jboss.test.classloading.dependency.test.MockClassLoadingMetaDataUnitTestCase;
import org.jboss.test.classloading.dependency.test.ModuleDependencyUnitTestCase;
import org.jboss.test.classloading.dependency.test.PackageDependencyUnitTestCase;
import org.jboss.test.classloading.dependency.test.ReExportModuleUnitTestCase;
import org.jboss.test.classloading.dependency.test.ReExportPackageUnitTestCase;
import org.jboss.test.classloading.dependency.test.UsesPackageUnitTestCase;

/**
 * Version Test Suite.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 37459 $
 */
public class DependencyTestSuite extends TestSuite
{
   /**
    * For running the testsuite from the command line
    * 
    * @param args the command line args
    */
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   /**
    * Create the testsuite
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      TestSuite suite = new TestSuite("Dependency Tests");

      suite.addTest(MockClassLoadingMetaDataUnitTestCase.suite());
      suite.addTest(DomainUnitTestCase.suite());
      suite.addTest(ImportAllUnitTestCase.suite());
      suite.addTest(ModuleDependencyUnitTestCase.suite());
      suite.addTest(PackageDependencyUnitTestCase.suite());
      suite.addTest(DependencyUnitTestCase.suite());
      suite.addTest(ReExportModuleUnitTestCase.suite());
      suite.addTest(ReExportPackageUnitTestCase.suite());
      suite.addTest(UsesPackageUnitTestCase.suite());
      
      return suite;
   }
}
