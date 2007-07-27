/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.kernel;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jboss.test.kernel.bootstrap.test.BootstrapTestSuite;
import org.jboss.test.kernel.config.test.ConfigTestSuite;
import org.jboss.test.kernel.controller.test.ControllerTestSuite;
import org.jboss.test.kernel.dependency.test.DependencyTestSuite;
import org.jboss.test.kernel.deployment.test.DeploymentTestSuite;
import org.jboss.test.kernel.deployment.xml.test.XMLTestSuite;
import org.jboss.test.kernel.event.test.EventTestSuite;
import org.jboss.test.kernel.registry.test.RegistryTestSuite;
import org.jboss.test.kernel.inject.test.ContextualInjectionTestSuite;
import org.jboss.test.kernel.metadata.test.MetaDataTestSuite;
import org.jboss.test.kernel.annotations.test.AnnotationsTestSuite;

/**
 * Kernel Test Suite.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class KernelTestSuite extends TestSuite
{
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("Kernel Tests");

      suite.addTest(BootstrapTestSuite.suite());
      suite.addTest(RegistryTestSuite.suite());
      suite.addTest(EventTestSuite.suite());
      suite.addTest(ConfigTestSuite.suite());
      suite.addTest(DependencyTestSuite.suite());
      suite.addTest(ControllerTestSuite.suite());
      suite.addTest(DeploymentTestSuite.suite());
      suite.addTest(ContextualInjectionTestSuite.suite());
      suite.addTest(XMLTestSuite.suite());
      suite.addTest(MetaDataTestSuite.suite());
      suite.addTest(AnnotationsTestSuite.suite());

      return suite;
   }
}
