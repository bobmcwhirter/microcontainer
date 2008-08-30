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
package org.jboss.test.kernel.dependency.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Dependency Test Suite.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class DependencyTestSuite extends TestSuite
{
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("Dependency Tests");

      suite.addTest(PlainDependencyTestCase.suite());
      suite.addTest(PlainDependencyXMLTestCase.suite());
      suite.addTest(PlainDependencyAnnotationTestCase.suite());
      suite.addTest(GenericBeanFactoryPlainDependencyTestCase.suite());
      suite.addTest(GenericBeanFactoryPlainDependencyXMLTestCase.suite());
      suite.addTest(OnDemandDependencyTestCase.suite());
      suite.addTest(OnDemandDependencyXMLTestCase.suite());
      suite.addTest(GenericBeanFactoryOnDemandDependencyTestCase.suite());
      suite.addTest(GenericBeanFactoryOnDemandDependencyXMLTestCase.suite());
      suite.addTest(PropertyDependencyTestCase.suite());
      suite.addTest(PropertyDependencyXMLTestCase.suite());
      suite.addTest(PropertyDependencyAnnotationTestCase.suite());
      suite.addTest(PropertyFieldDependencyTestCase.suite());
      suite.addTest(PropertyField2DependencyTestCase.suite());
      suite.addTest(GenericBeanFactoryPropertyDependencyTestCase.suite());
      suite.addTest(GenericBeanFactoryPropertyDependencyXMLTestCase.suite());
      suite.addTest(GenericBeanFactoryFieldPropertyDependencyTestCase.suite());
      suite.addTest(GenericBeanFactoryField2PropertyDependencyTestCase.suite());
      suite.addTest(ConstructorDependencyTestCase.suite());
      suite.addTest(ConstructorDependencyXMLTestCase.suite());
      suite.addTest(ConstructorDependencyAnnotationTestCase.suite());
      suite.addTest(GenericBeanFactoryConstructorDependencyTestCase.suite());
      suite.addTest(GenericBeanFactoryConstructorDependencyXMLTestCase.suite());
      suite.addTest(DemandDependencyTestCase.suite());
      suite.addTest(DemandDependencyXMLTestCase.suite());
      suite.addTest(DemandDependencyAnnotationTestCase.suite());
      suite.addTest(GenericBeanFactoryDemandDependencyTestCase.suite());
      suite.addTest(GenericBeanFactoryDemandDependencyXMLTestCase.suite());
      suite.addTest(PlainLifecycleDependencyTestCase.suite());
      suite.addTest(PlainLifecycleDependencyXMLTestCase.suite());
      suite.addTest(PlainLifecycleDependencyAnnotationTestCase.suite());
      suite.addTest(GenericBeanFactoryPlainLifecycleDependencyTestCase.suite());
      suite.addTest(GenericBeanFactoryPlainLifecycleDependencyXMLTestCase.suite());
      suite.addTest(ComplicatedLifecycleDependencyTestCase.suite());
      suite.addTest(ComplicatedLifecycleDependencyXMLTestCase.suite());
      suite.addTest(ComplicatedLifecycleDependencyAnnotationTestCase.suite());
      suite.addTest(KernelControllerContextAwareTestCase.suite());
      suite.addTest(KernelControllerContextAwareXMLTestCase.suite());
      suite.addTest(KernelControllerContextAwareAnnotationTestCase.suite());
      suite.addTest(InstallDependencyTestCase.suite());
      suite.addTest(InstallDependencyXMLTestCase.suite());
      suite.addTest(InstallDependencyAnnotationTestCase.suite());
      suite.addTest(GenericBeanFactoryInstallDependencyTestCase.suite());
      suite.addTest(GenericBeanFactoryInstallDependencyXMLTestCase.suite());
      suite.addTest(GenericBeanFactoryCallbackDependencyTestCase.suite());
      suite.addTest(GenericBeanFactoryCallbackDependencyXMLTestCase.suite());
      suite.addTest(InstallSelfDependencyTestCase.suite());
      suite.addTest(InstallSelfDependencyXMLTestCase.suite());
      suite.addTest(InstallSelfDependencyAnnotationTestCase.suite());
      suite.addTest(FromContextTestCase.suite());
      suite.addTest(FromContextXMLTestCase.suite());
      suite.addTest(FromContextAnnotationTestCase.suite());
      suite.addTest(PlainAliasTestCase.suite());
      suite.addTest(PlainAliasXMLTestCase.suite());
      suite.addTest(PlainAliasAnnotationTestCase.suite());
      suite.addTest(CallbackTestCase.suite());
      suite.addTest(CallbackXMLTestCase.suite());
      suite.addTest(CallbackAnnotationTestCase.suite());
      suite.addTest(CallbackCollectionTestCase.suite());
      suite.addTest(CallbackCollectionXMLTestCase.suite());
      suite.addTest(CallbackCollectionAnnotationTestCase.suite());
      suite.addTest(CallbackCollectionFieldsTestCase.suite());
      suite.addTest(CallbackCollectionFields2TestCase.suite());
      suite.addTest(MatcherDemandSupplyTestCase.suite());
      suite.addTest(MatcherDemandSupplyXMLTestCase.suite());
      suite.addTest(MatcherDemandSupplyAnnotationTestCase.suite());
      suite.addTest(NestedPropertyTestCase.suite());
      suite.addTest(NestedPropertyXMLTestCase.suite());
      suite.addTest(BeanValidatorBridgeTestCase.suite());
      suite.addTest(DuplicateAliasTestCase.suite());
      suite.addTest(DuplicateAliasXMLTestCase.suite());
      suite.addTest(DuplicateAliasAnnotationTestCase.suite());
      suite.addTest(ScopedDuplicateAliasTestCase.suite());
      suite.addTest(ScopedDuplicateAliasXMLTestCase.suite());
      suite.addTest(ScopedDuplicateAliasAnnotationTestCase.suite());
      return suite;
   }
}
