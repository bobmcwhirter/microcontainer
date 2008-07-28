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
package org.jboss.test.kernel.deployment.xml.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * XML Test Suite.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class XMLTestSuite extends TestSuite
{
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("XML Tests");

      suite.addTest(DeploymentTestCase.suite());
      suite.addTest(BeanTestCase.suite());
      suite.addTest(BeanFactoryTestCase.suite());
      suite.addTest(ClassLoaderTestCase.suite());
      suite.addTest(ConstructorTestCase.suite());
      suite.addTest(FactoryTestCase.suite());
      suite.addTest(ParameterTestCase.suite());
      suite.addTest(PropertyTestCase.suite());
      suite.addTest(RelatedClassTestCase.suite());
      suite.addTest(LifecycleTestCase.suite());
      suite.addTest(DependencyTestCase.suite());
      suite.addTest(DemandTestCase.suite());
      suite.addTest(SupplyTestCase.suite());
      suite.addTest(InstallTestCase.suite());
      suite.addTest(ValueTestCase.suite());
      suite.addTest(InjectionTestCase.suite());
      suite.addTest(CollectionTestCase.suite());
      suite.addTest(ListTestCase.suite());
      suite.addTest(SetTestCase.suite());
      suite.addTest(ArrayTestCase.suite());
      suite.addTest(MapTestCase.suite());
      suite.addTest(AnnotationTestCase.suite());
      suite.addTest(AliasTestCase.suite());
      suite.addTest(CallbackTestCase.suite());
      suite.addTest(ValueFactoryTestCase.suite());
      // policy
      suite.addTest(ScopeTestCase.suite());
      suite.addTest(BindingTestCase.suite());
      suite.addTest(PolicyTestCase.suite());

      // jaxb
      suite.addTest(DeploymentJaxbTestCase.suite());
      suite.addTest(BeanJaxbTestCase.suite());
      suite.addTest(BeanFactoryJaxbTestCase.suite());
      suite.addTest(ClassLoaderJaxbTestCase.suite());
      suite.addTest(ConstructorJaxbTestCase.suite());
      suite.addTest(FactoryJaxbTestCase.suite());
      suite.addTest(ParameterJaxbTestCase.suite());
      suite.addTest(PropertyJaxbTestCase.suite());
      suite.addTest(RelatedClassJaxbTestCase.suite());
      suite.addTest(LifecycleJaxbTestCase.suite());
      suite.addTest(DependencyJaxbTestCase.suite());
      suite.addTest(DemandJaxbTestCase.suite());
      suite.addTest(SupplyJaxbTestCase.suite());
      suite.addTest(InstallJaxbTestCase.suite());
      suite.addTest(ValueJaxbTestCase.suite());
      suite.addTest(InjectionJaxbTestCase.suite());
      suite.addTest(CollectionJaxbTestCase.suite());
      suite.addTest(ListJaxbTestCase.suite());
      suite.addTest(SetJaxbTestCase.suite());
      suite.addTest(ArrayJaxbTestCase.suite());
      suite.addTest(MapJaxbTestCase.suite());
      suite.addTest(AnnotationJaxbTestCase.suite());
      suite.addTest(CallbackJaxbTestCase.suite());
      suite.addTest(ValueFactoryJaxbTestCase.suite());

      // clone
      suite.addTest(BeanCloneTestCase.suite());
      suite.addTest(ClassLoaderCloneTestCase.suite());
      suite.addTest(ConstructorCloneTestCase.suite());
      suite.addTest(FactoryCloneTestCase.suite());
      suite.addTest(ParameterCloneTestCase.suite());
      suite.addTest(PropertyCloneTestCase.suite());
      suite.addTest(RelatedClassCloneTestCase.suite());
      suite.addTest(LifecycleCloneTestCase.suite());
      suite.addTest(DependencyCloneTestCase.suite());
      suite.addTest(DemandCloneTestCase.suite());
      suite.addTest(SupplyCloneTestCase.suite());
      suite.addTest(InstallCloneTestCase.suite());
      suite.addTest(ValueCloneTestCase.suite());
      suite.addTest(InjectionCloneTestCase.suite());
      suite.addTest(CollectionCloneTestCase.suite());
      suite.addTest(ListCloneTestCase.suite());
      suite.addTest(SetCloneTestCase.suite());
      suite.addTest(ArrayCloneTestCase.suite());
      suite.addTest(MapCloneTestCase.suite());
      suite.addTest(AnnotationCloneTestCase.suite());
      suite.addTest(CallbackCloneTestCase.suite());
      suite.addTest(ValueFactoryCloneTestCase.suite());

      // policy
      suite.addTest(ScopeJaxbTestCase.suite());
      suite.addTest(BindingJaxbTestCase.suite());
      suite.addTest(PolicyJaxbTestCase.suite());

      return suite;
   }
}
