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
package org.jboss.test.metatype.types.factory.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Types Factory Test Suite.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 55115 $
 */
public class TypesFactoryTestSuite extends TestSuite
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
      TestSuite suite = new TestSuite("Types Factory Tests");

      suite.addTest(SimpleMetaTypeFactoryUnitTestCase.suite());
      suite.addTest(SpecialMetaTypeFactoryUnitTestCase.suite());
      suite.addTest(EnumMetaTypeFactoryUnitTestCase.suite());
      suite.addTest(ArrayMetaTypeFactoryUnitTestCase.suite());
      suite.addTest(CollectionMetaTypeFactoryUnitTestCase.suite());
      suite.addTest(MapMetaTypeFactoryUnitTestCase.suite());
      suite.addTest(CompositeMetaTypeFactoryUnitTestCase.suite());
      suite.addTest(AnnotationMetaTypeFactoryUnitTestCase.suite());
      
      return suite;
   }
}
