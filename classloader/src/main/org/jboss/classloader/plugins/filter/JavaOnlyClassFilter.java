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

/**
 * A class filter that only delegates standard java classes
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class JavaOnlyClassFilter extends PatternClassFilter
{
   /** The singleton instance */ 
   public static final JavaOnlyClassFilter INSTANCE = new JavaOnlyClassFilter();
   
   /**
    * Create a new JavaOnlyClassFilter.
    */
   private JavaOnlyClassFilter()
   {
      super(new String[] { "java\\..+", "javax\\..+" }, 
            new String[] { "java/.+", "javax/.+" },
            new String[] { "java", "java\\..*", "javax", "javax\\..*" });
   }

   public String toString()
   {
      return "JAVA_ONLY";
   }

   @Override
   public void setIncludeJava(boolean includeJava)
   {
      throw new UnsupportedOperationException("This class is immutable");
   }
}
