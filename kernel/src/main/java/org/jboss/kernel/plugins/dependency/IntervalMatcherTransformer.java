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

import java.util.StringTokenizer;

import org.jboss.kernel.api.dependency.NonNullMatcherTransformer;
import org.jboss.kernel.api.dependency.Matcher;

/**
 * Interval expression matcher transformer.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class IntervalMatcherTransformer extends NonNullMatcherTransformer
{
   protected Matcher internalTransform(Object value)
   {
      String interval = value.toString();

      Number floor = null;
      Number ceiling = null;
      StringTokenizer st = new StringTokenizer(interval, ",[]()", true);
      Boolean floorIsGreaterThan = null;
      Boolean ceilingIsLessThan = null;
      boolean mid = false;
      while (st.hasMoreTokens())
      {
         String token = st.nextToken();
         if (token.equals("["))
            floorIsGreaterThan = false;
         else if (token.equals("("))
            floorIsGreaterThan = true;
         else if (token.equals("]"))
            ceilingIsLessThan = false;
         else if (token.equals(")"))
            ceilingIsLessThan = true;
         else if (token.equals(","))
            mid = true;
         else if (token.equals("\"") == false)
         {
            if (floor == null)
               floor = Double.parseDouble(token);
            else
               ceiling = Double.parseDouble(token);
         }

      }
      // check for parenthesis
      if (floorIsGreaterThan == null || ceilingIsLessThan == null)
      {
         // non-empty interval usage
         if (mid)
            throw new IllegalArgumentException("Missing parenthesis: " + interval);
         // single value
         floorIsGreaterThan = false;
         ceilingIsLessThan = false;
      }

      return new IntervalMatcher(floor, ceiling, floorIsGreaterThan, ceilingIsLessThan);
   }
}
