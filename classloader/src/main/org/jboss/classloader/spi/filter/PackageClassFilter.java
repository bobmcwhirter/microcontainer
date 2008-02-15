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
package org.jboss.classloader.spi.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.jboss.classloader.plugins.filter.PatternClassFilter;

/**
 * A class filter using regular expressions
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class PackageClassFilter extends PatternClassFilter
{
   /** The patterns as regular expressions */
   private String[] packageNames;
   
   /**
    * Convert package names to class patterns
    * 
    * @param packageNames the package names
    * @return the patterns
    */
   private static String[] convertPackageNamesToClassPatterns(String[] packageNames)
   {
      if (packageNames == null)
         throw new IllegalArgumentException("Null package names");
      
      String[] patterns = new String[packageNames.length];
      for (int i = 0; i < packageNames.length; ++i)
      {
         if (packageNames[i] == null)
            throw new IllegalArgumentException("Null package name in " + Arrays.asList(packageNames));

         if (packageNames[i].length() == 0)
            // Base package - it is a match if there is no . in the class name
            patterns[i] = "[^.]*";
         else
            // Escape the dots in the package and match anything that has a single dot followed by non-dots
            patterns[i] = packageNames[i].replace(".", "\\.") + "\\.[^.]+";
      }
      return patterns;
   }
   
   /**
    * Convert package names to resource patterns
    * 
    * @param packageNames the package names
    * @return the patterns
    */
   private static String[] convertPackageNamesToResourcePatterns(String[] packageNames)
   {
      if (packageNames == null)
         throw new IllegalArgumentException("Null package names");
      
      String[] patterns = new String[packageNames.length];
      for (int i = 0; i < packageNames.length; ++i)
      {
         if (packageNames[i] == null)
            throw new IllegalArgumentException("Null package name in " + Arrays.asList(packageNames));

         if (packageNames[i].length() == 0)
            // Base package - it is a match if there is no / in the path
            patterns[i] = "[^/]*";
         else
            // Replace the dots with slashs and match non slashes after that
            patterns[i] = packageNames[i].replace(".", "/") + "/[^/]+";
      }
      return patterns;
   }

   /**
    * Create a package class filter<p>
    * 
    * Creates the filter from a comma seperated list
    * 
    * @param string the string
    * @return the filter
    */
   public static PackageClassFilter createPackageClassFilterFromString(String string)
   {
      StringTokenizer tokenizer = new StringTokenizer(string, ",");
      List<String> packages = new ArrayList<String>();
      while (tokenizer.hasMoreTokens())
         packages.add(tokenizer.nextToken());
      return createPackageClassFilter(packages);
   }
   
   /**
    * Create a new package class filter
    * 
    * @param packageNames the package names
    * @return the filter
    * @throws IllegalArgumentException for null packageNames
    */
   public static PackageClassFilter createPackageClassFilter(String... packageNames)
   {
      return new PackageClassFilter(packageNames);
   }
   
   /**
    * Create a new package class filter
    * 
    * @param packageNames the package names
    * @return the filter
    * @throws IllegalArgumentException for null packageNames
    */
   public static PackageClassFilter createPackageClassFilter(List<String> packageNames)
   {
      String[] packages;
      if (packageNames == null)
         packages = new String[0];
      else
         packages = packageNames.toArray(new String[packageNames.size()]);
      return new PackageClassFilter(packages);
   }
   
   /**
    * Create a new PackageClassFilter.
    * 
    * @param packageNames the packageNames
    * @throws IllegalArgumentException for null packageNames
    */
   public PackageClassFilter(String[] packageNames)
   {
      super(convertPackageNamesToClassPatterns(packageNames), 
            convertPackageNamesToResourcePatterns(packageNames),
            packageNames);
      this.packageNames = packageNames;
   }

   @Override
   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(Arrays.asList(packageNames));
      if (isIncludeJava())
         builder.append(" <INCLUDE_JAVA>");
      return builder.toString();
   }
}
