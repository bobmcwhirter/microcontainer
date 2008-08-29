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
package org.jboss.test.dependency.controller.test;

import junit.framework.Test;
import org.jboss.dependency.spi.Cardinality;
import org.jboss.test.AbstractTestCaseWithSetup;
import org.jboss.test.AbstractTestDelegate;

/**
 * CardinalityTestCase.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class CardinalityTestCase extends AbstractTestCaseWithSetup
{
   public static Test suite()
   {
      return suite(CardinalityTestCase.class);
   }

   public CardinalityTestCase(String name)
   {
      super(name);
   }

   /**
    * Default setup.
    *
    * @param clazz the class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      return new AbstractTestDelegate(clazz);
   }

   public void testParseCardinality() throws Throwable
   {
      assertEquals(Cardinality.ZERO_TO_ONE, Cardinality.toCardinality("0..1"));
      assertEquals(Cardinality.ZERO_TO_MANY, Cardinality.toCardinality("0..n"));
      assertEquals(Cardinality.ONE_TO_ONE, Cardinality.toCardinality("1..1"));
      assertEquals(Cardinality.ONE_TO_MANY, Cardinality.toCardinality("1..n"));

      Cardinality c1 = Cardinality.toCardinality("2..20");
      assertEquals(2, c1.getLeft());
      assertEquals(20, c1.getRight());

      Cardinality c2 = Cardinality.createCardinality(2, 20);
      assertEquals(2, c2.getLeft());
      assertEquals(20, c2.getRight());

      Cardinality c3 = Cardinality.createUnlimitedCardinality(10);
      assertEquals(10, c3.getLeft());
      assertTrue(c3.isRightInfinity());

      Cardinality c4 = Cardinality.createLimitedCardinality(10);
      assertEquals(0, c4.getLeft());
      assertEquals(10, c4.getRight());

      Cardinality c5 = Cardinality.createCardinality(Cardinality.INFINITY, Cardinality.INFINITY);
      assertTrue(c5.isLeftInfinity());
      assertTrue(c5.isRightInfinity());
   }

   public void testRangeCardinality() throws Throwable
   {
      Cardinality c1 = Cardinality.fromString("5..10");
      assertTrue(c1.isInRange(5));
      assertTrue(c1.isInRange(7));
      assertTrue(c1.isInRange(10));
      assertFalse(c1.isInRange(-1));
      assertFalse(c1.isInRange(3));
      assertFalse(c1.isInRange(20));
      assertFalse(c1.isInRange(Cardinality.INFINITY));

      Cardinality c2 = Cardinality.ZERO_TO_MANY;
      assertFalse(c2.isInRange(-1));
      assertTrue(c2.isInRange(0));
      assertTrue(c2.isInRange(Integer.MAX_VALUE));

      Cardinality c3 = Cardinality.ONE_TO_MANY;
      assertFalse(c3.isInRange(-1));
      assertFalse(c3.isInRange(0));
      assertTrue(c3.isInRange(1));
      assertTrue(c3.isInRange(Integer.MAX_VALUE));

      Cardinality c4 = Cardinality.createCardinality(Cardinality.INFINITY, Cardinality.INFINITY);
      assertFalse(c4.isInRange(-1));
      assertFalse(c4.isInRange(0));
      assertFalse(c4.isInRange(1));
      assertFalse(c4.isInRange(10));
      assertFalse(c4.isInRange(Integer.MAX_VALUE));
   }
}
