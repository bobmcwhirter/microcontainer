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
package org.jboss.test.kernel.annotations.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.jboss.test.kernel.annotations.test.factory.AnnotationFactoryTestSuite;
import org.jboss.test.kernel.annotations.test.field.AnnotationFieldTestSuite;
import org.jboss.test.kernel.annotations.test.inheritance.AnnotationsInheritanceTestSuite;
import org.jboss.test.kernel.annotations.test.override.AnnotationsOverrideTestSuite;
import org.jboss.test.kernel.annotations.test.wb.WBTestSuite;

/**
 * Annotations tests.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AnnotationsTestSuite extends TestSuite
{
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("Annotations Tests");

      suite.addTest(AnnotationSupportTestSuite.suite());
      suite.addTest(AnnotationsOverrideTestSuite.suite());
      suite.addTest(AnnotationsInheritanceTestSuite.suite());
      suite.addTest(AnnotationFieldTestSuite.suite());
      suite.addTest(AnnotationFactoryTestSuite.suite());
      suite.addTest(WBTestSuite.suite());

      return suite;
   }
}
