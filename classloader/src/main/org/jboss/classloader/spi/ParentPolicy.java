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
package org.jboss.classloader.spi;

import org.jboss.classloader.spi.filter.ClassFilter;

/**
 * ParentPolicy
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ParentPolicy
{
   /** Standard Java Delegation */
   public static final ParentPolicy BEFORE = new ParentPolicy(ClassFilter.EVERYTHING, ClassFilter.NOTHING, "BEFORE");

   /** Servlet style */
   public static final ParentPolicy AFTER = new ParentPolicy(ClassFilter.NOTHING_BUT_JAVA, ClassFilter.EVERYTHING, "AFTER");

   /** Standard Java Delegation for java classes */
   public static final ParentPolicy BEFORE_BUT_JAVA_ONLY = new ParentPolicy(ClassFilter.JAVA_ONLY, ClassFilter.NOTHING, "BEFORE_BUT_JAVA_ONLY");

   /** Java classes before, everything else after */
   public static final ParentPolicy AFTER_BUT_JAVA_BEFORE = new ParentPolicy(ClassFilter.JAVA_ONLY, ClassFilter.EVERYTHING, "AFTER_BUT_JAVA_BEFORE");
   
   /** The before filter */
   private ClassFilter beforeFilter;
   
   /** The after filter */
   private ClassFilter afterFilter;

   /** A description of the policy */
   private String description;
   
   /**
    * Create a new ParentPolicy.
    * 
    * @param beforeFilter the before filter
    * @param afterFilter the after filter
    * @throws IllegalArgumentException for a null parameter
    */
   public ParentPolicy(ClassFilter beforeFilter, ClassFilter afterFilter)
   {
      this(beforeFilter, afterFilter, null);
   }

   /**
    * Create a new ParentPolicy.
    * 
    * @param beforeFilter the before filter
    * @param afterFilter the after filter
    * @param description of the policy
    * @throws IllegalArgumentException for a null parameter
    */
   public ParentPolicy(ClassFilter beforeFilter, ClassFilter afterFilter, String description)
   {
      if (beforeFilter == null)
         throw new IllegalArgumentException("Null beforeFilter");
      if (afterFilter == null)
         throw new IllegalArgumentException("Null afterFilter");
      
      this.beforeFilter = beforeFilter;
      this.afterFilter = afterFilter;
      this.description = description;
      if (description == null)
         this.description = "(before=" + beforeFilter + " after=" + afterFilter + ")";
   }

   /**
    * Get the beforeFilter.
    * 
    * @return the beforeFilter.
    */
   public ClassFilter getBeforeFilter()
   {
      return beforeFilter;
   }

   /**
    * Get the afterFilter.
    * 
    * @return the afterFilter.
    */
   public ClassFilter getAfterFilter()
   {
      return afterFilter;
   }
   
   @Override
   public String toString()
   {
      return description;
   }
}
