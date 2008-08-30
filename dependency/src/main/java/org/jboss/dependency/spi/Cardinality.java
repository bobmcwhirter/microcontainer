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

import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Cardinality def.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class Cardinality extends JBossObject implements Serializable
{
   private static final long serialVersionUID = 2L;
  
   public static final int INFINITY = -1;

   public static final Cardinality ZERO_TO_ONE = new Cardinality("0..1", 0, 1);
   public static final Cardinality ZERO_TO_MANY = new Cardinality("0..n", 0, INFINITY);
   public static final Cardinality ONE_TO_ONE = new Cardinality("1..1", 1, 1);
   public static final Cardinality ONE_TO_MANY = new Cardinality("1..n", 1, INFINITY);

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
    * Is number in cardinality's range.
    *
    * @param number number to check
    * @return left <= number <= right
    */
   public boolean isInRange(int number)
   {
      return (number < 0 || number < left || left == INFINITY) == false && (number <= right || right == INFINITY);
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
      return fromString(type);
   }

   /**
    * Get limit from string.
    *
    * @param limit from string
    * @return limit as int from string
    */
   protected static int getLimitFromString(String limit)
   {
      try
      {
         return Integer.parseInt(limit);
      }
      catch (NumberFormatException e)
      {
         return INFINITY;
      }
   }

   /**
    * Get limit as string.
    *
    * @param limit right limit
    * @return limit as string
    */
   protected static String getLimitFromInt(int limit)
   {
      return limit == INFINITY ? "n" : String.valueOf(limit);
   }

   /**
    * Parse cardinality from string - #1..#2.
    *
    * @param string cardinality string
    * @return cardinality
    */
   public static Cardinality fromString(String string)
   {
      if (string == null)
         throw new IllegalArgumentException("Null string.");

      if (string.contains(".."))
      {
         String[] args = string.split("\\.\\.");
         if (args == null || args.length != 2)
            throw new IllegalArgumentException("Illegal cardinality format: " + string);
         return createCardinality(getLimitFromString(args[0]), getLimitFromString(args[1]));
      }
      else
         return createUnlimitedCardinality(getLimitFromString(string));
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
      return new Cardinality(getLimitFromInt(left) + ".." + getLimitFromInt(right), left, right);
   }

   protected int getHashCode()
   {
      return type.hashCode();
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append("type=").append(type);
   }

   protected void toString(JBossStringBuilder buffer)
   {
      buffer.append("type=").append(type);
   }

   public boolean equals(Object obj)
   {
      if (obj instanceof Cardinality == false)
         return false;
      Cardinality card = (Cardinality)obj;
      return left == card.left && right == card.right; 
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

   public boolean isLeftInfinity()
   {
      return left <= INFINITY;
   }

   public boolean isRightInfinity()
   {
      return right <= INFINITY;
   }
}
