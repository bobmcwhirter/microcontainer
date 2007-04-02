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
package org.jboss.dependency.spi;

import java.io.Serializable;

/**
 * Cardinality def.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class Cardinality implements Serializable
{
   private static final long serialVersionUID = 1L;
  
   private static final int INFINITY = -1;

   public static Cardinality ZERO_TO_ONE = new Cardinality("0..1", 0, 1);
   public static Cardinality ZERO_TO_MANY = new Cardinality("0..n", 0, INFINITY);
   public static Cardinality ONE_TO_ONE = new Cardinality("1..1", 1, 1);
   public static Cardinality ONE_TO_MANY = new Cardinality("1..n", 1, INFINITY);

   private static Cardinality[] values = new Cardinality[]
         {
               ZERO_TO_ONE,
               ZERO_TO_MANY,
               ONE_TO_ONE,
               ONE_TO_MANY
         };

   private String type;
   private int left;
   private int right;

   private Cardinality(String type, int left, int right)
   {
      this.type = type;
      this.left = left;
      this.right = right;
   }

   /**
    * Get the cardinality by type.
    *
    * @param type the commont type
    * @return cardinality by type
    */
   public static Cardinality toCardinality(String type)
   {
      for (Cardinality c : values)
      {
         if (c.getType().equals(type))
         {
            return c;
         }
      }
      throw new IllegalArgumentException("Illegal Cardinality value: " + type);
   }

   /**
    * Create cardinality which starts at #start and has no limit.
    *
    * @param start the left number of cardinality interval
    * @return cardinality
    * @see org.jboss.dependency.spi.Cardinality#createCardinality(int,int)
    */
   public static Cardinality createUnlimitedCardinality(int start)
   {
      return createCardinality(start, INFINITY);
   }

   /**
    * Create cardinality which ends at #end and starts at zero.
    *
    * @param end the right number of cardinality interval
    * @return cardinality
    * @see org.jboss.dependency.spi.Cardinality#createCardinality(int,int)
    */
   public static Cardinality createLimitedCardinality(int end)
   {
      return createCardinality(0, end);
   }

   /**
    * Create cardinality with [#left, #right] interval.
    *
    * @param left  left point in interval
    * @param right right point in interval
    * @return cardinality
    */
   public static Cardinality createCardinality(int left, int right)
   {
      return new Cardinality(left + ".." + right, left, right);
   }

   public String getType()
   {
      return type;
   }

   public int getLeft()
   {
      return left;
   }

   public int getRight()
   {
      return right;
   }
}
