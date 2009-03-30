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
package org.jboss.test.system.controller.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jboss.test.system.controller.basic.test.ControllerBasicTestSuite;
import org.jboss.test.system.controller.classloader.test.ControllerClassLoaderTestSuite;
import org.jboss.test.system.controller.configure.test.ControllerConfigureTestSuite;
import org.jboss.test.system.controller.instantiate.test.ControllerInstantiateTestSuite;
import org.jboss.test.system.controller.integration.test.IntegrationTestSuite;
import org.jboss.test.system.controller.lifecycle.test.ControllerLifecycleTestSuite;
import org.jboss.test.system.controller.parse.test.ControllerParseTestSuite;

/**
 * Controller Test Suite.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class ControllerTestSuite extends TestSuite
{
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("Controller Tests");

      suite.addTest(ControllerBasicTestSuite.suite());
      suite.addTest(ControllerParseTestSuite.suite());
      suite.addTest(ControllerClassLoaderTestSuite.suite());
      suite.addTest(ControllerInstantiateTestSuite.suite());
      suite.addTest(ControllerConfigureTestSuite.suite());
      suite.addTest(ControllerLifecycleTestSuite.suite());
      suite.addTest(IntegrationTestSuite.suite());
      
      return suite;
   }
}
