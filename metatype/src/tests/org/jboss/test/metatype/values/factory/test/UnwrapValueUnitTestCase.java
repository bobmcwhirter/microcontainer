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

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.test.metatype.values.factory.support.SimpleCompositeInterface;
import org.jboss.test.metatype.values.factory.support.TestEnum;
import org.jboss.test.metatype.values.factory.support.TestGeneric;
import org.jboss.test.metatype.values.factory.support.TestRecursiveSimpleComposite;
import org.jboss.test.metatype.values.factory.support.TestSimpleComposite;
import org.jboss.test.metatype.values.factory.support.TestSimpleCompositeInterface;

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
      checkSingle(123, true);
      checkSingle(123, false);
      checkSingle(new Date(), true);
      checkSingle(new Date(), false);
   }

   public void testEnumUnwrap() throws Exception
   {
      checkSingle(TestEnum.ONE, true);
      checkSingle(TestEnum.ONE, false);
   }

   public void testGenericUnwrap() throws Exception
   {
      checkSingle(new TestGeneric("123"), true);
      checkSingle(new TestGeneric("123"), false);
   }

   public void testCompositeUnwrap() throws Exception
   {
      TestSimpleComposite composite = new TestSimpleComposite("something");
      checkSingle(composite, true);
      checkSingle(composite, false);

      checkSingle(new TestRecursiveSimpleComposite("something", composite), true);
      checkSingle(new TestRecursiveSimpleComposite("something", composite), false);

      checkSingle(new TestSimpleCompositeInterface("something"), SimpleCompositeInterface.class);
   }

   public void testCollectionUnwrap() throws Exception
   {
      Integer i1 = 123;
      Integer i2 = 123;
      checkCollection(new ArrayList<Integer>(), i1, i2);
      checkCollection(new ArrayList<Integer>(), List.class, i1, i2);
      checkCollection(new HashSet<Integer>(), i1, i2);
      checkCollection(new HashSet<Integer>(), Set.class, i1, i2);

      TestEnum one = TestEnum.ONE;
      TestEnum two = TestEnum.TWO;
      TestEnum three = TestEnum.THREE;
      checkCollection(new ArrayList<TestEnum>(), one, two, three, one);
      checkCollection(new ArrayList<TestEnum>(), List.class, one, two, three, one);
      checkCollection(new HashSet<TestEnum>(), one, two, three, one);
      checkCollection(new HashSet<TestEnum>(), Set.class, one, two, three, one);

      TestGeneric g1 = new TestGeneric("123");
      TestGeneric g2 = new TestGeneric("123");
      checkCollection(new ArrayList<TestGeneric>(), g1, g2);
      checkCollection(new ArrayList<TestGeneric>(), List.class, g1, g2);
      checkCollection(new HashSet<TestGeneric>(), g1, g2);
      checkCollection(new HashSet<TestGeneric>(), Set.class, g1, g2);

      TestSimpleComposite c1 = new TestSimpleComposite("123");
      TestSimpleComposite c2 = new TestSimpleComposite("123");
      checkCollection(new ArrayList<TestSimpleComposite>(), c1, c2);
      checkCollection(new ArrayList<TestSimpleComposite>(), List.class, c1, c2);
      checkCollection(new HashSet<TestSimpleComposite>(), c1, c2);
      checkCollection(new HashSet<TestSimpleComposite>(), Set.class, c1, c2);
   }

   public void testArrayUnwrap() throws Exception
   {
      short[] shorts = new short[128];
      Double[] doubles = new Double[128];
      TestEnum[] enums = new TestEnum[128];
      TestGeneric[] generics = new TestGeneric[128];
      TestSimpleComposite[] composits = new TestSimpleComposite[128];
      TestRecursiveSimpleComposite[] recursiveComposites = new TestRecursiveSimpleComposite[128];
      Integer[][] integers = new Integer[128][128];
      Integer[][][] triple = new Integer[10][10][10];
      for(int i = 0; i < 128; i++)
      {
         shorts[i] = (short)i;
         doubles[i] = i / Math.PI;
         enums[i] = TestEnum.values()[i % 3];
         generics[i] = new TestGeneric("#" + i);
         composits[i] = new TestSimpleComposite("#" + i);
         recursiveComposites[i] = new TestRecursiveSimpleComposite("#" + i, composits[i]);
         for(int j = 0; j < 128; j++)
         {
            integers[i][j] = 128 * i + j;
            for(int k = 0; k < 128; k++)
               triple[i % 10][j % 10][k % 10] = (100 * i + 10 * j + k) % 1000;
         }
      }

      checkArray(shorts, true, new Asserter()
      {
         public boolean assertArray(final Object original, final Object unwrapped)
         {
            return Arrays.equals((short[])original, (short[])unwrapped);
         }
      });
      checkArray(shorts, false, new Asserter()
      {
         public boolean assertArray(final Object original, final Object unwrapped)
         {
            return Arrays.equals((short[])original, (short[])unwrapped);
         }
      });

      checkArray(doubles, true, new Asserter()
      {
         public boolean assertArray(final Object original, final Object unwrapped)
         {
            return Arrays.equals((Double[])original, (Double[])unwrapped);
         }
      });
      checkArray(doubles, false, new Asserter()
      {
         public boolean assertArray(final Object original, final Object unwrapped)
         {
            return Arrays.equals((Double[])original, (Double[])unwrapped);
         }
      });

      Asserter objectsAsserter = new Asserter()
      {
         public boolean assertArray(final Object original, final Object unwrapped)
         {
            return Arrays.equals((Object[])original, (Object[])unwrapped);
         }
      };

      checkArray(enums, true, objectsAsserter);
      checkArray(enums, false, objectsAsserter);

      checkArray(generics, true, objectsAsserter);
      checkArray(generics, false, objectsAsserter);

      checkArray(composits, true, objectsAsserter);
      checkArray(composits, false, objectsAsserter);

      checkArray(recursiveComposites, true, objectsAsserter);
      checkArray(recursiveComposites, false, objectsAsserter);

      Asserter integersAsserter = new Asserter()
      {
         public boolean assertArray(final Object original, final Object unwrapped)
         {
            Integer[][] first = (Integer[][])original;
            Integer[][] second = (Integer[][])unwrapped;
            for (int i = 0; i < first.length; i++)
               for (int j = 0; j < first[0].length; j++)
                  if (first[i][j].equals(second[i][j]) == false)
                     return false;
            return true;
         }
      };
      checkArray(integers, true, integersAsserter);
      checkArray(integers, false, integersAsserter);

      Asserter tripleAsserter = new Asserter()
      {
         public boolean assertArray(final Object original, final Object unwrapped)
         {
            Integer[][][] first = (Integer[][][])original;
            Integer[][][] second = (Integer[][][])unwrapped;
            for (int i = 0; i < first.length; i++)
               for (int j = 0; j < first[0].length; j++)
                  for (int k = 0; k < first[0][0].length; k++)
                     if (first[i][j][k].equals(second[i][j][k]) == false)
                        return false;
            return true;
         }
      };
      checkArray(triple, true, tripleAsserter);
      checkArray(triple, false, tripleAsserter);
   }

   public void testTableUnwrap() throws Exception
   {
      Method method = getClass().getMethod("compositeValueMap");
      Type mapType = method.getGenericReturnType();

      Map<Integer, String> map = new HashMap<Integer, String>();
      map.put(123, "123");
      checkSingle(map, mapType);
   }

   public Map<Integer, String> compositeValueMap()
   {
      return null;
   }

   protected void checkSingle(Object object, boolean typeInfoFromObject)
   {
      MetaValue metaValue = createMetaValue(object);
      assertNotNull(metaValue);
      Object unwrapped;
      if (typeInfoFromObject)
         unwrapped = unwrapMetaValue(metaValue, object.getClass());
      else
         unwrapped = unwrapMetaValue(metaValue);
      assertEquals(object, unwrapped);
   }

   protected void checkSingle(Object object, Type type)
   {
      MetaValue metaValue = createMetaValue(object, type);
      assertNotNull(metaValue);
      Object unwrapped = unwrapMetaValue(metaValue, type);
      assertEquals(object, unwrapped);
   }

   protected <T> void checkCollection(Collection<T> collection, T... params) throws Exception
   {
      collection.addAll(Arrays.asList(params));
      checkSingle(collection, true);
      checkSingle(collection, false);
   }

   protected <T> void checkCollection(Collection<T> collection, Class<? extends Collection> clazz,  T... params) throws Exception
   {
      collection.addAll(Arrays.asList(params));
      checkSingle(collection, clazz);
   }

   protected void checkArray(Object object, boolean typeInfoFromObject, Asserter asserter)
   {
      MetaValue metaValue = createMetaValue(object);
      assertNotNull(metaValue);
      Object unwrapped;
      if (typeInfoFromObject)
         unwrapped = unwrapMetaValue(metaValue, object.getClass());
      else
         unwrapped = unwrapMetaValue(metaValue);
      assertTrue("Different arrays.", asserter.assertArray(object, unwrapped));
   }

   private interface Asserter
   {
      boolean assertArray(final Object original, final Object unwrapped);
   }
}
