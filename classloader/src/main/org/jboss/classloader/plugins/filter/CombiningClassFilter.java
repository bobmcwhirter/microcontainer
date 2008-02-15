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
package org.jboss.classloader.plugins.filter;

import org.jboss.classloader.spi.filter.ClassFilter;

/**
 * CombiningClassFilter.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class CombiningClassFilter implements ClassFilter
{
   /** Whether it is an "and" filter */
   private boolean and = false;
   
   /** The filters */
   private ClassFilter[] filters;
   
   /**
    * Create a new CombiningClassFilter.
    * 
    * @param filters the filters
    * @return the filter
    * @throws IllegalArgumentException for null filters
    */
   public static CombiningClassFilter create(ClassFilter... filters)
   {
      return new CombiningClassFilter(false, filters);
   }
   
   /**
    * Create a new CombiningClassFilter.
    * 
    * @param and whether it is an "and" filter
    * @param filters the filters
    * @return the filter
    * @throws IllegalArgumentException for null filters
    */
   public static CombiningClassFilter create(boolean and, ClassFilter... filters)
   {
      return new CombiningClassFilter(and, filters);
   }
   
   /**
    * Create a new CombiningClassFilter.
    * 
    * @param and whether it is an "and" filter
    * @param filters the filters
    * @throws IllegalArgumentException for null filters
    */
   public CombiningClassFilter(boolean and, ClassFilter[] filters)
   {
      if (filters == null)
         throw new IllegalArgumentException("Null filters");
      this.and = and;
      this.filters = filters;
   }
   
   public boolean matchesClassName(String className)
   {
      for (ClassFilter filter : filters)
      {
         if (filter.matchesClassName(className))
            return true;
         else if (and)
            return false;
      }
      return false;
   }

   public boolean matchesResourcePath(String resourcePath)
   {
      for (ClassFilter filter : filters)
      {
         if (filter.matchesResourcePath(resourcePath))
            return true;
         else if (and)
            return false;
      }
      return false;
   }

   public boolean matchesPackageName(String packageName)
   {
      for (ClassFilter filter : filters)
      {
         if (filter.matchesPackageName(packageName))
            return true;
         else if (and)
            return false;
      }
      return false;
   }
   
   @Override
   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < filters.length; ++i)
      {
         builder.append(filters[i]);
         if (i < filters.length-1)
            builder.append(", ");
      }
      return builder.toString();
   }
}
