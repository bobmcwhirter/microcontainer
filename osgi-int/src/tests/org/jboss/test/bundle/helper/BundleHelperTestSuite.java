/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
package org.jboss.test.bundle.helper;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * A BundleHelperTestSuite.
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public class BundleHelperTestSuite extends TestSuite
{

   /**
    * Run test using TestRunner
    * 
    * @param args
    */
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   /**
    * Get suite of BundleHelper tests
    * 
    * @return suite for test
    */
   public static Test suite()
   {
      TestSuite suite = new TestSuite("Bundle Helper Tests");
      suite.addTest(BundleHeaderTestCase.suite());
      suite.addTest(BundleEntryVisitorTestCase.suite());
      suite.addTest(BundleEntryHelperTestCase.suite());
      return suite;
   }

}
