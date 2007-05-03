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
package org.jboss.test.classloader.domain;

import org.jboss.test.classloader.domain.test.CustomParentLoaderUnitTestCase;
import org.jboss.test.classloader.domain.test.HierarchicalParentLoaderUnitTestCase;
import org.jboss.test.classloader.domain.test.ParentPolicyUnitTestCase;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * ClassLoaderDomain Test Suite.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 37459 $
 */
public class ClassLoaderDomainTestSuite extends TestSuite
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
      TestSuite suite = new TestSuite("ClassLoader Domain Tests");

      suite.addTest(ParentPolicyUnitTestCase.suite());
      suite.addTest(CustomParentLoaderUnitTestCase.suite());
      suite.addTest(HierarchicalParentLoaderUnitTestCase.suite());
      
      return suite;
   }
}
