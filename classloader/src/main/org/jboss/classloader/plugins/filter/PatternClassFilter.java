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
   /** The class patterns as regular expressions */
   private Pattern[] classPatterns;

   /** The resource patterns as regular expressions */
   private Pattern[] resourcePatterns;

   /** The package patterns as regular expressions */
   private Pattern[] packagePatterns;
   
   /** Whether to include java */
   private boolean includeJava = false;
   
   /**
    * Create a new PatternClassFilter.
    * 
    * @param classPatterns the class patterns
    * @param resourcePatterns the resource patterns
    * @param packagePatterns the package patterns
    * @throws IllegalArgumentException for a null pattern
    */
   public PatternClassFilter(String[] classPatterns, String[] resourcePatterns, String[] packagePatterns)
   {
      if (classPatterns == null)
         throw new IllegalArgumentException("Null patterns");
      
      this.classPatterns = new Pattern[classPatterns.length];
      for (int i = 0; i < classPatterns.length; ++i)
      {
         if (classPatterns[i] == null)
            throw new IllegalArgumentException("Null pattern in " + Arrays.asList(classPatterns));
         this.classPatterns[i] = Pattern.compile(classPatterns[i]);
      }

      if (resourcePatterns == null)
      {
         this.resourcePatterns = this.classPatterns;
         return;
      }
      
      this.resourcePatterns = new Pattern[resourcePatterns.length];
      for (int i = 0; i < resourcePatterns.length; ++i)
      {
         if (resourcePatterns[i] == null)
            throw new IllegalArgumentException("Null pattern in " + Arrays.asList(resourcePatterns));
         this.resourcePatterns[i] = Pattern.compile(resourcePatterns[i]);
      }

      if (packagePatterns == null)
      {
         this.packagePatterns = this.classPatterns;
         return;
      }
      
      this.packagePatterns = new Pattern[packagePatterns.length];
      for (int i = 0; i < packagePatterns.length; ++i)
      {
         if (packagePatterns[i] == null)
            throw new IllegalArgumentException("Null pattern in " + Arrays.asList(packagePatterns));
         this.packagePatterns[i] = Pattern.compile(packagePatterns[i]);
      }
   }

   /**
    * Get the includeJava.
    * 
    * @return the includeJava.
    */
   public boolean isIncludeJava()
   {
      return includeJava;
   }

   /**
    * Set the includeJava.
    * 
    * @param includeJava the includeJava.
    */
   public void setIncludeJava(boolean includeJava)
   {
      this.includeJava = includeJava;
   }

   public boolean matchesClassName(String className)
   {
      if (className == null)
         return false;
      
      for (int i = 0; i < classPatterns.length; ++i)
      {
         Matcher matcher = classPatterns[i].matcher(className);
         if (matcher.matches())
            return true;
      }
      if (includeJava == false)
         return false;
      return JavaOnlyClassFilter.INSTANCE.matchesClassName(className);
   }

   public boolean matchesResourcePath(String resourcePath)
   {
      if (resourcePath == null)
         return false;
      
      for (int i = 0; i < resourcePatterns.length; ++i)
      {
         Matcher matcher = resourcePatterns[i].matcher(resourcePath);
         if (matcher.matches())
            return true;
      }
      if (includeJava == false)
         return false;
      return JavaOnlyClassFilter.INSTANCE.matchesResourcePath(resourcePath);
   }

   public boolean matchesPackageName(String packageName)
   {
      if (packageName == null)
         return false;
      
      for (int i = 0; i < packagePatterns.length; ++i)
      {
         Matcher matcher = packagePatterns[i].matcher(packageName);
         if (matcher.matches())
            return true;
      }
      if (includeJava == false)
         return false;
      return JavaOnlyClassFilter.INSTANCE.matchesPackageName(packageName);
   }

   @Override
   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(Arrays.asList(classPatterns));
      if (isIncludeJava())
         builder.append(" <INCLUDE_JAVA>");
      return builder.toString();
   }
}
