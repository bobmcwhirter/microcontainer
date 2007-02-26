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
package org.jboss.test.microcontainer.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * All Test Suite.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AspectMCAllTestSuite extends TestSuite
{
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("All Tests");

      suite.addTest(BeanCallingMethodInCtorTestCase.suite());
      suite.addTest(BeanNoDefaultCtorUsingParamTestCase.suite());
      suite.addTest(ConstructorInterceptorWithDependencyTestCase.suite());
      suite.addTest(InterceptedTestCase.suite());
      suite.addTest(InterceptedUseCaseXmlTestCase.suite());
      suite.addTest(InterceptorWithAnnotationDependencyTestCase.suite());
      suite.addTest(InterceptorWithDependencyTestCase.suite());
      suite.addTest(InterceptorWithNestedAnnotationDependencyTestCase.suite());
      suite.addTest(InterceptorWithNestedMethodAnnotationDependencyTestCase.suite());
      suite.addTest(InterceptorWithOverriddenClassAnnotationDependencyTestCase.suite());
      suite.addTest(InterceptorWithOverriddenMethodAnnotationDependencyForChildTestCase.suite());
      suite.addTest(InterceptorWithOverriddenMethodAnnotationDependencyTestCase.suite());
      suite.addTest(IntroductionDependencyTestCase.suite());
      suite.addTest(JMXDecoratedTestCase.suite());
      suite.addTest(JMXLifecycleTestCase.suite());
      suite.addTest(JndiDecoratedTestCase.suite());
      suite.addTest(JndiLifeCycleTestCase.suite());
      suite.addTest(MetaDataTestCase.suite());
      suite.addTest(MixinTestCase.suite());
      suite.addTest(MultipleLifecycleTestCase.suite());
      suite.addTest(ScopingAopTestCase.suite());

      return suite;
   }
}
