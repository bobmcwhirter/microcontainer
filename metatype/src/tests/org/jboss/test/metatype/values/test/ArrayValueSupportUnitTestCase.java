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
package org.jboss.test.metatype.values.test;

import java.lang.reflect.Array;

import junit.framework.Test;

import org.jboss.metatype.api.types.ArrayMetaType;
import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.api.values.ArrayValueSupport;
import org.jboss.test.metatype.AbstractMetaTypeTest;

/**
 * Tests of the ArrayValueSupport class.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class ArrayValueSupportUnitTestCase extends AbstractMetaTypeTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(ArrayValueSupportUnitTestCase.class);
   }
   
   /**
    * Create a new ArrayValueSupportUnitTestCase.
    * 
    * @param name the test name
    */
   public ArrayValueSupportUnitTestCase(String name)
   {
      super(name);
   }

   @SuppressWarnings("unchecked")
   static <T> T[] convert(Object array, T[] t)
   {
      int length = Array.getLength(array);
      Class c = t.getClass().getComponentType();
      T[] result = (T[]) Array.newInstance(c, length);
      for(int n = 0; n < length; n ++)
      {
         result[n] = (T) Array.get(array, n);
      }
      return result;
   }

   public void testCharArray() throws Exception
   {
      ArrayMetaType type = new ArrayMetaType(1, SimpleMetaType.CHARACTER);
      char[] value = {'h', 'e', 'l', 'l', 'o'};
      ArrayValueSupport avs = new ArrayValueSupport(type, value);
      // Use getValue(int) accessor
      for(int n = 0; n < avs.getLength(); n ++)
      {
         Object element = avs.getValue(n);
         assertEquals(value[n], element);
      }
      // Use typesafe foreach Iterable
      int i = 0;
      for(Object c : avs)
      {
         assertEquals("["+i+"]", value[i++], c);         
      }
      // Validate the primative array
      char[] raw = (char[]) avs.getValue();
      for(int n = 0; n < value.length; n ++)
      {
         assertEquals(value[n], raw[n]);
      }
   }
   
   @SuppressWarnings("unchecked")
   public void testCharacterArray() throws Exception
   {
      ArrayMetaType type = new ArrayMetaType(1, SimpleMetaType.CHARACTER);
      Character[] value = {'h', 'e', 'l', 'l', 'o'};
      ArrayValueSupport avs = new ArrayValueSupport(type, value);
      for(int n = 0; n < avs.getLength(); n ++)
      {
         Object element = avs.getValue(n);
         assertEquals(value[n], element);
      }
      // Validate the raw array
      Character[] raw = (Character[]) avs.getValue();
      for(int n = 0; n < value.length; n ++)
      {
         assertEquals(value[n], raw[n]);
      }
   }

   public void test2DCharArray() throws Exception
   {
      ArrayMetaType type = new ArrayMetaType(2, SimpleMetaType.CHARACTER, true);
      char[][] value = {{'h', 'e'}, {'l', 'l', 'o'}};
      ArrayValueSupport avs = new ArrayValueSupport(type, value);
      assertEquals(value.length, avs.getLength());
      for(int m = 0; m < value.length; m ++)
      {
         Object arraym = avs.getValue(m);
         for(int n = 0; n < value[m].length; n ++)
         {
            // Have to use the java.lang.reflect.Array to access nested elements
            Object valuenm = Array.get(arraym, n);
            assertEquals("["+m+"]["+n+"]", value[m][n], valuenm);
         }
      }

      /* FIXME: Use typesafe foreach Iterable: current broken with CCE on [C
      int i = 0, j = 0;
      for(Character[] carray : avs)
      {
         for(Character c : carray)
         {
            Character cij = value[i ++][j ++];
            assertEquals("["+i+"], ["+j+"]", cij , c);
         }
      }
      */

      // Validate the primitive 2d array
      char[][] raw = (char[][]) avs.getValue();
      for(int m = 0; m < value.length; m ++)
      {
         for(int n = 0; n < value[m].length; n ++)
         {
            assertEquals("["+m+"]["+n+"]", value[m][n], raw[m][n]);
         }
      }
   }

}
