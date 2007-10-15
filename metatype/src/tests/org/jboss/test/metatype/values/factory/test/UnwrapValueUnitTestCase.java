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
package org.jboss.test.metatype.values.factory.test;

import java.util.Date;
import java.util.Arrays;

import junit.framework.Test;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.test.metatype.values.factory.support.TestEnum;
import org.jboss.test.metatype.values.factory.support.TestGeneric;

/**
 * UnwrapValueUnitTestCase.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class UnwrapValueUnitTestCase extends AbstractMetaValueFactoryTest
{
   /**
    * Create a testsuite for this test
    *
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(UnwrapValueUnitTestCase.class);
   }

   /**
    * Create a new UnwrapValueUnitTestCase
    *
    * @param name the test name
    */
   public UnwrapValueUnitTestCase(String name)
   {
      super(name);
   }

   public void testSimpleUnwrap() throws Exception
   {
      checkSingle(123);
      checkSingle(new Date());
   }

   public void testEnumUnwrap() throws Exception
   {
      checkSingle(TestEnum.ONE);
   }

   public void testGenericUnwrap() throws Exception
   {
      checkSingle(new TestGeneric("123"));
   }

   public void testArrayUnwrap() throws Exception
   {
      short[] shorts = new short[128];
      Double[] doubles = new Double[128];
      TestEnum[] enums = new TestEnum[128];
      TestGeneric[] generics = new TestGeneric[128];
      Integer[][] integers = new Integer[128][128];
      Integer[][][] triple = new Integer[10][10][10];
      for(int i = 0; i < 128; i++)
      {
         shorts[i] = (short)i;
         doubles[i] = i / Math.PI;
         enums[i] = TestEnum.values()[i % 3];
         generics[i] = new TestGeneric("#" + i);
         for(int j = 0; j < 128; j++)
         {
            integers[i][j] = 128 * i + j;
            for(int k = 0; k < 128; k++)
               triple[i % 10][j % 10][k % 10] = (100 * i + 10 * j + k) % 1000;
         }
      }

      checkArray(shorts, new Asserter()
      {
         public boolean assertArray(final Object original, final Object unwrapped)
         {
            return Arrays.equals((short[])original, (short[])unwrapped);
         }
      });

      checkArray(doubles, new Asserter()
      {
         public boolean assertArray(final Object original, final Object unwrapped)
         {
            return Arrays.equals((Double[])original, (Double[])unwrapped);
         }
      });

      checkArray(enums, new Asserter()
      {
         public boolean assertArray(final Object original, final Object unwrapped)
         {
            return Arrays.equals((Object[])original, (Object[])unwrapped);
         }
      });

      checkArray(generics, new Asserter()
      {
         public boolean assertArray(final Object original, final Object unwrapped)
         {
            return Arrays.equals((Object[])original, (Object[])unwrapped);
         }
      });

      checkArray(integers, new Asserter()
      {
         public boolean assertArray(final Object original, final Object unwrapped)
         {
            Integer[][] first = (Integer[][])original;
            Integer[][] second = (Integer[][])unwrapped;
            for(int i = 0; i < first.length; i++)
               for(int j = 0; j < first[0].length; j++)
                  if (first[i][j].equals(second[i][j]) == false)
                     return false;
            return true;
         }
      });

      checkArray(triple, new Asserter()
      {
         public boolean assertArray(final Object original, final Object unwrapped)
         {
            Integer[][][] first = (Integer[][][])original;
            Integer[][][] second = (Integer[][][])unwrapped;
            for(int i = 0; i < first.length; i++)
               for(int j = 0; j < first[0].length; j++)
                  for(int k = 0; k < first[0][0].length; k++)
                  if (first[i][j][k].equals(second[i][j][k]) == false)
                     return false;
            return true;
         }
      });
   }

   protected void checkSingle(Object object)
   {
      MetaValue metaValue = createMetaValue(object);
      assertNotNull(metaValue);
      Object unwrapped = unwrapMetaValue(metaValue, object.getClass());
      assertEquals(object, unwrapped);
   }

   protected void checkArray(Object object, Asserter asserter)
   {
      MetaValue metaValue = createMetaValue(object);
      assertNotNull(metaValue);
      Class<? extends Object> clazz = object.getClass();
      Object unwrapped = unwrapMetaValue(metaValue, clazz);
      assertTrue("Different arrays.", asserter.assertArray(object, unwrapped));
   }

   private interface Asserter
   {
      boolean assertArray(final Object original, final Object unwrapped);
   }
}
