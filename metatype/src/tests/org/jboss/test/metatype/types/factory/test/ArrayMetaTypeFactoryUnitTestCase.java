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

import org.jboss.metatype.api.types.ArrayMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.test.metatype.types.factory.support.TestSimpleComposite;

import junit.framework.Test;

/**
 * ArrayMetaTypeFactoryUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ArrayMetaTypeFactoryUnitTestCase extends AbstractMetaTypeFactoryTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(ArrayMetaTypeFactoryUnitTestCase.class);
   }
   
   /**
    * Create a new ArrayMetaTypeFactoryUnitTestCase.
    * 
    * @param name the test name
    */
   public ArrayMetaTypeFactoryUnitTestCase(String name)
   {
      super(name);
   }

   /**
    * Test the correct meta type is generated for a simple array
    * 
    * @throws Exception for any problem
    */
   @SuppressWarnings("unchecked")
   public void testSimpleArray() throws Exception
   {
      String[] array = new String[0];
      MetaType<?> result = resolve(array.getClass());
      ArrayMetaType<String> actual = assertInstanceOf(result, ArrayMetaType.class);
      ArrayMetaType<String> expected = new ArrayMetaType<String>(1, SimpleMetaType.STRING);
      testArray(expected, actual);
   }

   @SuppressWarnings("unchecked")
   public void testCharArray()
      throws Exception
   {
      char[] array = {'H', 'e', 'l', 'l', 'o'};
      MetaType result = resolve(array.getClass());

      ArrayMetaType actual = assertInstanceOf(result, ArrayMetaType.class);
      ArrayMetaType expected = new ArrayMetaType(1, SimpleMetaType.CHARACTER);
      testArray(expected, actual);
   }
   @SuppressWarnings("unchecked")
   public void testChar2DArray()
      throws Exception
   {
      char[][] array = {{'H', 'e', 'l', 'l', 'o'}};
      MetaType result = resolve(array.getClass());
      ArrayMetaType actual = assertInstanceOf(result, ArrayMetaType.class);
      ArrayMetaType expected = new ArrayMetaType(2, SimpleMetaType.CHARACTER);
      testArray(expected, actual);
   }

   /**
    * Test the correct meta type is generated for a composite array
    * 
    * @throws Exception for any problem
    */
   @SuppressWarnings("unchecked")
   public void testSimpleCompositeArray() throws Exception
   {
      TestSimpleComposite[] array = new TestSimpleComposite[0];
      MetaType result = resolve(array.getClass());
      MetaType composite = resolve(TestSimpleComposite.class);
      ArrayMetaType actual = assertInstanceOf(result, ArrayMetaType.class);
      ArrayMetaType expected = new ArrayMetaType(1, composite);
      testArray(expected, actual);
   }

   /**
    * Test the correct meta type is generated for a multidimensional simple array
    * 
    * @throws Exception for any problem
    */
   @SuppressWarnings("unchecked")
   public void testMultiSimpleArray() throws Exception
   {
      String[][] array = new String[0][0];
      MetaType result = resolve(array.getClass());
      ArrayMetaType actual = assertInstanceOf(result, ArrayMetaType.class);
      ArrayMetaType expected = new ArrayMetaType(2, SimpleMetaType.STRING);
      testArray(expected, actual);
   }

   /**
    * Test the correct meta type is generated for a mutli dimensional composite array
    * 
    * @throws Exception for any problem
    */
   @SuppressWarnings("unchecked")
   public void testMultiSimpleCompositeArray() throws Exception
   {
      TestSimpleComposite[][] array = new TestSimpleComposite[0][0];
      MetaType result = resolve(array.getClass());
      MetaType composite = resolve(TestSimpleComposite.class);
      ArrayMetaType actual = assertInstanceOf(result, ArrayMetaType.class);
      ArrayMetaType expected = new ArrayMetaType(2, composite);
      testArray(expected, actual);
   }
}
