/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.vfs.bundle;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgi.framework.Version;

/**
 * Represents an OSGi version range:
 * version-range ::= interval | atleast
 * interval ::= ( '[' | '(' ) floor ',' ceiling ( ']' | ')' )
 * atleast ::= version
 * floor ::= version
 * ceiling ::= version
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class VersionRange
{
   /** The lower bound of the range */
   private Version floor;
   /** The upper bound of the range */
   private Version ceiling;
   /** is the floor version a >(true) or >= constraint(false) */
   private boolean floorIsGreaterThan;
   /** is the ceiling version a <(true) or <= constraint(false) */
   private boolean ceilingIsLessThan;

   public static VersionRange parseRangeSpec(String rangeSpec)
   {
      VersionRange range = null;
      Version floor = null;
      Version ceiling = null;
      StringTokenizer st = new StringTokenizer(rangeSpec, ",[]()", true);
      boolean floorIsGreaterThan = false;
      boolean ceilingIsLessThan = false;
      while( st.hasMoreTokens() )
      {
         String token = st.nextToken();
         if( token.equals("[") )
            floorIsGreaterThan = false;
         else if( token.equals("(") )
            floorIsGreaterThan = true;
         else if( token.equals("]") )
            ceilingIsLessThan = false;
         else if( token.equals(")") )
            ceilingIsLessThan = true;
         else if( token.equals(",") )
            continue;
         else
         {
            // A version token
            if( floor == null )
               floor = new Version(token);
            else
               ceiling = new Version(token);
         }
            
      }
      range = new VersionRange(floor, ceiling, floorIsGreaterThan, ceilingIsLessThan);
      return range;
   }

   public VersionRange(String rangeSpec)
   {
      
   }

   public VersionRange(Version floor, Version ceiling, boolean floorIsLessThan, boolean ceilingIsLessThan)
   {
      this.floor = floor;
      this.ceiling = ceiling;
      this.floorIsGreaterThan = floorIsLessThan;
      this.ceilingIsLessThan = ceilingIsLessThan;
   }

   public boolean isInRange(Version v)
   {
      // Test a null floor version which implies 0.0.0 <= v <= inf
      boolean isInRange = floor == null;
      if( isInRange == false )
      {
         // Test the floor version
         int floorCompare = v.compareTo(floor);
         if( (floorIsGreaterThan && floorCompare > 0) || (floorIsGreaterThan == false && floorCompare >= 0) )
         {
            isInRange = ceiling == null;
            if( isInRange == false )
            {
               // Test the ceiling version
               int ceilingCompare = v.compareTo(ceiling);
               if( (ceilingIsLessThan && ceilingCompare < 0) || (ceilingIsLessThan == false && ceilingCompare <= 0) )
               {
                  isInRange = true;
               }
            }
         }
      }
      return isInRange;
   }

   public String toString()
   {
      StringBuilder tmp = new StringBuilder();
      if( floor == null )
         tmp.append("0.0.0");
      else
         tmp.append(floor.toString());
      tmp.append(" <");
      if( floorIsGreaterThan == false )
         tmp.append('=');
      tmp.append(" v ");
      // Ceiling
      tmp.append('<');
      if( ceilingIsLessThan == false )
         tmp.append('=');
      tmp.append(' ');
      if( ceiling == null )
         tmp.append("inf");
      else
         tmp.append(ceiling.toString());
      return tmp.toString();
   }
}
