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
package org.jboss.test.classloading.metadata;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jboss.test.classloading.metadata.test.CapabilitiesMetaDataUnitTestCase;
import org.jboss.test.classloading.metadata.test.ClassLoadingMetaDataFactoryUnitTestCase;
import org.jboss.test.classloading.metadata.test.ClassLoadingMetaDataUnitTestCase;
import org.jboss.test.classloading.metadata.test.ManagedObjectClassLoadingMetaDataUnitTestCase;
import org.jboss.test.classloading.metadata.test.ModuleCapabilityUnitTestCase;
import org.jboss.test.classloading.metadata.test.ModuleRequirementUnitTestCase;
import org.jboss.test.classloading.metadata.test.NameAndVersionRangeUnitTestCase;
import org.jboss.test.classloading.metadata.test.NameAndVersionUnitTestCase;
import org.jboss.test.classloading.metadata.test.PackageCapabilityUnitTestCase;
import org.jboss.test.classloading.metadata.test.PackageRequirementUnitTestCase;
import org.jboss.test.classloading.metadata.test.RequirementsMetaDataUnitTestCase;
import org.jboss.test.classloading.metadata.xml.test.ClassLoadingMetaDataXmlUnitTestCase;

/**
 * Version Test Suite.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 37459 $
 */
public class MetaDataTestSuite extends TestSuite
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
      TestSuite suite = new TestSuite("MetaData Tests");

      suite.addTest(NameAndVersionUnitTestCase.suite());
      suite.addTest(NameAndVersionRangeUnitTestCase.suite());
      suite.addTest(ModuleCapabilityUnitTestCase.suite());
      suite.addTest(PackageRequirementUnitTestCase.suite());
      suite.addTest(ModuleRequirementUnitTestCase.suite());
      suite.addTest(PackageCapabilityUnitTestCase.suite());
      suite.addTest(CapabilitiesMetaDataUnitTestCase.suite());
      suite.addTest(RequirementsMetaDataUnitTestCase.suite());
      suite.addTest(ClassLoadingMetaDataFactoryUnitTestCase.suite());
      suite.addTest(ClassLoadingMetaDataUnitTestCase.suite());
      suite.addTest(ManagedObjectClassLoadingMetaDataUnitTestCase.suite());
      suite.addTest(ClassLoadingMetaDataXmlUnitTestCase.suite());
      
      return suite;
   }
}
