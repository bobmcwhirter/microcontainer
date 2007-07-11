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
package org.jboss.test.metatype.types.factory.test;

import junit.framework.Test;

import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.plugins.types.MutableCompositeMetaType;
import org.jboss.test.metatype.types.factory.support.TestSimpleAnnotation;

/**
 * AnnotationMetaTypeFactoryUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class AnnotationMetaTypeFactoryUnitTestCase extends AbstractMetaTypeFactoryTest
{
   /**
    * Create a new testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(AnnotationMetaTypeFactoryUnitTestCase.class);
   }
   
   /**
    * Create a new AnnotationMetaTypeFactoryUnitTestCase.
    * 
    * @param name the test name
    */
   public AnnotationMetaTypeFactoryUnitTestCase(String name)
   {
      super(name);
   }

   /**
    * Test the composite type generated for a simple annotation
    * 
    * @throws Exception for any problem
    */
   public void testSimpleAnnotation() throws Exception
   {
      MetaType result = resolve(TestSimpleAnnotation.class);
      CompositeMetaType actual = assertInstanceOf(result, CompositeMetaType.class);
      
      MutableCompositeMetaType expected = new MutableCompositeMetaType(TestSimpleAnnotation.class.getName(), TestSimpleAnnotation.class.getName());
      expected.addItem("something", "something", SimpleMetaType.STRING);
      expected.freeze();
      
      testComposite(expected, actual);
   }
}
