/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.classloader;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jboss.test.classloader.bootstrap.BootstrapTestSuite;
import org.jboss.test.classloader.delegate.DelegateTestSuite;
import org.jboss.test.classloader.domain.ClassLoaderDomainTestSuite;
import org.jboss.test.classloader.filter.FilterTestSuite;
import org.jboss.test.classloader.jmx.JMXTestSuite;
import org.jboss.test.classloader.junit.JUnitTestSuite;
import org.jboss.test.classloader.old.OldTestSuite;
import org.jboss.test.classloader.policy.test.ClassLoaderPolicyUnitTestCase;
import org.jboss.test.classloader.resources.ResourceTestSuite;
import org.jboss.test.classloader.system.ClassLoaderSystemTestSuite;

/**
 * ClassLoader All Test Suite.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 37459 $
 */
public class ClassLoaderAllTestSuite extends TestSuite
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
      TestSuite suite = new TestSuite("ClassLoader All Tests");

      suite.addTest(ClassLoaderSystemTestSuite.suite());
      suite.addTest(ClassLoaderDomainTestSuite.suite());
      suite.addTest(ClassLoaderPolicyUnitTestCase.suite());
      suite.addTest(BootstrapTestSuite.suite());
      suite.addTest(OldTestSuite.suite());
      suite.addTest(FilterTestSuite.suite());
      suite.addTest(DelegateTestSuite.suite());
      suite.addTest(ResourceTestSuite.suite());
      suite.addTest(JMXTestSuite.suite());
      suite.addTest(JUnitTestSuite.suite());
      
      return suite;
   }
}
