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
package org.jboss.test.classloader.filter.test;

import junit.framework.Test;

import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.test.classloader.AbstractClassLoaderTestWithSecurity;

/**
 * FilterUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class FilterUnitTestCase extends AbstractClassLoaderTestWithSecurity
{
   public static Test suite()
   {
      return suite(FilterUnitTestCase.class);
   }

   public FilterUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testEverything() throws Exception
   {
      ClassFilter filter = ClassFilter.EVERYTHING;
      assertFilterMatchesClassName("gibberish", filter);
      assertFilterMatchesClassName("", filter);
      assertFilterMatchesClassName(null, filter);
      assertFilterMatchesResourcePath("gibberish", filter);
      assertFilterMatchesResourcePath("", filter);
      assertFilterMatchesResourcePath(null, filter);
      assertFilterMatchesPackageName("gibberish", filter);
      assertFilterMatchesPackageName("", filter);
      assertFilterMatchesPackageName(null, filter);
   }
   
   public void testNothing() throws Exception
   {
      ClassFilter filter = ClassFilter.NOTHING;
      assertFilterNoMatchClassName("gibberish", filter);
      assertFilterNoMatchClassName("", filter);
      assertFilterNoMatchClassName(null, filter);
      assertFilterNoMatchResourcePath("gibberish", filter);
      assertFilterNoMatchResourcePath("", filter);
      assertFilterNoMatchResourcePath(null, filter);
      assertFilterNoMatchPackageName("gibberish", filter);
      assertFilterNoMatchPackageName("", filter);
      assertFilterNoMatchPackageName(null, filter);
   }
   
   public void testJavaOnly() throws Exception
   {
      ClassFilter filter = ClassFilter.JAVA_ONLY;
      assertFilterMatchesClassName("java.x", filter);
      assertFilterMatchesClassName("java.lang.Object", filter);
      assertFilterMatchesClassName("java.lang.ref.Method", filter);
      assertFilterMatchesClassName("java.util.Collection", filter);
      assertFilterMatchesClassName("javax.x", filter);
      assertFilterMatchesClassName("javax.naming.Context", filter);
      assertFilterNoMatchClassName("java.", filter);
      assertFilterNoMatchClassName("java", filter);
      assertFilterNoMatchClassName("javaa.", filter);
      assertFilterNoMatchClassName("javaa.whatever", filter);
      assertFilterNoMatchClassName("javax", filter);
      assertFilterNoMatchClassName("javax.", filter);
      assertFilterNoMatchClassName("javaxa.", filter);
      assertFilterNoMatchClassName("javaxa.whatever", filter);
      assertFilterNoMatchClassName("gibberish", filter);
      assertFilterNoMatchClassName("", filter);
      assertFilterNoMatchClassName(null, filter);
      assertFilterMatchesResourcePath("java/x", filter);
      assertFilterMatchesResourcePath("java/lang/Object", filter);
      assertFilterMatchesResourcePath("java/lang/ref/Method", filter);
      assertFilterMatchesResourcePath("java/util/Collection", filter);
      assertFilterMatchesResourcePath("javax/x", filter);
      assertFilterMatchesResourcePath("javax/naming/Context", filter);
      assertFilterNoMatchResourcePath("java/", filter);
      assertFilterNoMatchResourcePath("java", filter);
      assertFilterNoMatchResourcePath("javaa.", filter);
      assertFilterNoMatchResourcePath("javaa/whatever", filter);
      assertFilterNoMatchResourcePath("javax", filter);
      assertFilterNoMatchResourcePath("javax/", filter);
      assertFilterNoMatchResourcePath("javaxa/", filter);
      assertFilterNoMatchResourcePath("javaxa/whatever", filter);
      assertFilterNoMatchResourcePath("gibberish", filter);
      assertFilterNoMatchResourcePath("", filter);
      assertFilterNoMatchResourcePath(null, filter);
      assertFilterMatchesPackageName("java", filter);
      assertFilterMatchesPackageName("java.lang", filter);
      assertFilterMatchesPackageName("java.lang.ref", filter);
      assertFilterMatchesPackageName("java.util", filter);
      assertFilterMatchesPackageName("javax", filter);
      assertFilterMatchesPackageName("javax.naming", filter);
      assertFilterNoMatchPackageName("javaa.", filter);
      assertFilterNoMatchPackageName("javaa.whatever", filter);
      assertFilterNoMatchPackageName("javaxa.", filter);
      assertFilterNoMatchPackageName("javaxa.whatever", filter);
      assertFilterNoMatchPackageName("gibberish", filter);
      assertFilterNoMatchPackageName("", filter);
      assertFilterNoMatchPackageName(null, filter);
   }
}
