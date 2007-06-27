/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.deployers.structure.spi.classloading;

/**
 * VersionRange.
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class VersionRange
{
   /** The low range */
   private Version low;
   
   /** Whether low is inclusive */
   private boolean lowInclusive;
   
   /** The high range */
   private Version high;
   
   /** Whether high is inclusive */
   private boolean highInclusive;
   
   /**
    * Create a new VersionRange with just a low inclusive check
    * 
    * @param low the low range (null for no lower bound)
    */
   public VersionRange(Version low)
   {
      this(low, null);
   }
   
   /**
    * Create a new VersionRange with low inclusive and high non-inclusive
    * 
    * @param low the low range (null for no lower bound)
    * @param high the high range (null for no higher bound)
    */
   public VersionRange(Version low, Version high)
   {
      this(low, true, high, false);
   }
   
   /**
    * Create a new VersionRange.
    * 
    * @param low the low range (null for no lower bound)
    * @param lowInclusive whether the low bound is inclusive
    * @param high the high range (null for no higher bound)
    * @param highInclusive whether the high bound is inclusive
    */
   public VersionRange(Version low, boolean lowInclusive, Version high, boolean highInclusive)
   {
      this.low = low;
      this.lowInclusive = lowInclusive;
      this.high = high;
      this.highInclusive = highInclusive;
   }

   /**
    * Get the low.
    * 
    * @return the low.
    */
   public Version getLow()
   {
      return low;
   }

   /**
    * Get the lowInclusive.
    * 
    * @return the lowInclusive.
    */
   public boolean isLowInclusive()
   {
      return lowInclusive;
   }

   /**
    * Get the high.
    * 
    * @return the high.
    */
   public Version getHigh()
   {
      return high;
   }

   /**
    * Get the highInclusive.
    * 
    * @return the highInclusive.
    */
   public boolean isHighInclusive()
   {
      return highInclusive;
   }

   /**
    * Test whether a version is in range
    * 
    * @param version the version to test
    * @return true when the version is in range
    * @throws IllegalArgumentException for a null version
    */
   public  boolean isInRange(Version version)
   {
      if (version == null)
         throw new IllegalArgumentException("Null version");

      int comparison; 
      if (low != null)
      {
         comparison = low.compareTo(version);
         if (comparison < 0)
            return false;
         if (lowInclusive == false && comparison == 0)
            return false;
      }
      if (high != null)
      {
         comparison = high.compareTo(version);
         if (comparison > 0)
            return false;
         if (highInclusive == false && comparison == 0)
            return false;
      }
      return true;
   }
}
