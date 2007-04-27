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
package org.jboss.classloader.plugins.filter;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.classloader.spi.filter.ClassFilter;

/**
 * A class filter using regular expressions
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class PatternClassFilter implements ClassFilter
{
   /** The patterns as regular expressions */
   private Pattern[] patterns;
   
   /**
    * Create a new PatternClassFilter.
    * 
    * @param pattern the pattern
    */
   public PatternClassFilter(String pattern)
   {
      if (pattern == null)
         throw new IllegalArgumentException("Null pattern");
      patterns = new Pattern[1];
      patterns[0] = Pattern.compile(pattern);
   }
   
   /**
    * Create a new PatternClassFilter.
    * 
    * @param patterns the patterns
    * @throws IllegalArgumentException for a null pattern
    */
   public PatternClassFilter(String[] patterns)
   {
      if (patterns == null)
         throw new IllegalArgumentException("Null patterns");
      
      this.patterns = new Pattern[patterns.length];
      for (int i = 0; i < patterns.length; ++i)
      {
         if (patterns[i] == null)
            throw new IllegalArgumentException("Null pattern in " + Arrays.asList(patterns));
         this.patterns[i] = Pattern.compile(patterns[i]);
      }
   }

   public boolean matches(String className)
   {
      for (int i = 0; i < patterns.length; ++i)
      {
         Matcher matcher = patterns[i].matcher(className);
         if (matcher.matches())
            return true;
      }
      return false;
   }

   @Override
   public String toString()
   {
      return Arrays.asList(patterns).toString();
   }
}
