/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.kernel.annotations.test.inheritance;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Testing annotations inheritance.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AnnotationsInheritanceTestSuite extends TestSuite
{
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("Annotation Inheritance tests");

      suite.addTest(PropertyAnnotationInheritanceTestCase.suite());
      suite.addTest(ConstructorAnnotationInheritanceTestCase.suite());
      suite.addTest(LifecycleAnnotationInheritanceTestCase.suite());
      suite.addTest(CallbackAnnotationInheritanceTestCase.suite());
      suite.addTest(InstallationAnnotationInheritanceTestCase.suite());
      suite.addTest(SetsAnnotationInheritanceTestCase.suite());
      suite.addTest(FactoryAnnotationInheritanceTestCase.suite());
      suite.addTest(ExternalAnnotationInheritanceTestCase.suite());

      return suite;
   }
}
