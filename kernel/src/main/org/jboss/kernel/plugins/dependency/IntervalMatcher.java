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
package org.jboss.kernel.plugins.dependency;

import java.io.Serializable;

import org.jboss.kernel.api.dependency.NumberMatcher;

/**
 * Interval matcher.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class IntervalMatcher extends NumberMatcher implements Serializable
{
   private static final long serialVersionUID = 1L;
   /**
    * The lower bound of the range
    */
   private Number floor;
   /**
    * The upper bound of the range
    */
   private Number ceiling;
   /**
    * is the floor a >(true) or >= constraint(false)
    */
   private boolean floorIsGreaterThan;
   /**
    * is the ceiling a <(true) or <= constraint(false)
    */
   private boolean ceilingIsLessThan;

   public IntervalMatcher(Number floor, Number ceiling, boolean floorIsGreaterThan, boolean ceilingIsLessThan)
   {
      this.floor = floor;
      this.ceiling = ceiling;
      this.floorIsGreaterThan = floorIsGreaterThan;
      this.ceilingIsLessThan = ceilingIsLessThan;
   }

   /**
    * Compare other value with edge value.
    * Return default value if edge is null.
    *
    * @param other the other
    * @param edge the edge
    * @param defaultValue default value
    * @return -1 if edge greater than other, 1 if other greater than edge, 0 if equal
    */
   protected int compareTo(Number other, Number edge, int defaultValue)
   {
      if (edge == null)
         return defaultValue;

      double dOther = other.doubleValue();
      double dEdge = edge.doubleValue();
      return Double.compare(dOther, dEdge);
   }

   protected boolean matchByType(Number other)
   {
      boolean isInRange = floor == null;
      if (isInRange == false)
      {
         // Test the floor
         int floorCompare = compareTo(other, floor, 1);
         if ((floorIsGreaterThan && floorCompare > 0) || (floorIsGreaterThan == false && floorCompare >= 0))
         {
            isInRange = ceiling == null;
            if (isInRange == false)
            {
               // Test the ceiling
               int ceilingCompare = compareTo(other, ceiling, -1);
               if ((ceilingIsLessThan && ceilingCompare < 0) || (ceilingIsLessThan == false && ceilingCompare <= 0))
               {
                  isInRange = true;
               }
            }
         }
      }
      return isInRange;
   }
}
