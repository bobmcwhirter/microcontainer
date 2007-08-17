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
package org.jboss.test.kernel.annotations.test.override;

import junit.textui.TestRunner;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Testing XML override of annotations.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AnnotationsOverrideTestSuite extends TestSuite
{
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("Annotation Override tests");

      suite.addTest(PropertyAnnotationOverrideTestCase.suite());
      suite.addTest(PropertyAnnotationOverrideXMLTestCase.suite());
      suite.addTest(ConstructorAnnotationOverrideTestCase.suite());
      suite.addTest(ConstructorAnnotationOverrideXMLTestCase.suite());
      suite.addTest(LifecycleAnnotationOverrideTestCase.suite());
      suite.addTest(LifecycleAnnotationOverrideXMLTestCase.suite());
      suite.addTest(CallbackAnnotationOverrideTestCase.suite());
      suite.addTest(CallbackAnnotationOverrideXMLTestCase.suite());
      suite.addTest(InstallationAnnotationOverrideTestCase.suite());
      suite.addTest(InstallationAnnotationOverrideXMLTestCase.suite());
      suite.addTest(SetsAnnotationOverrideTestCase.suite());
      suite.addTest(SetsAnnotationOverrideXMLTestCase.suite());
      suite.addTest(FactoryAnnotationOverrideTestCase.suite());
      suite.addTest(FactoryAnnotationOverrideXMLTestCase.suite());
      suite.addTest(ExternalAnnotationOverrideTestCase.suite());
      suite.addTest(ExternalAnnotationOverrideXMLTestCase.suite());

      return suite;
   }
}

