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
      assertFilterMatches("gibberish", filter);
      assertFilterMatches("", filter);
      assertFilterMatches(null, filter);
   }
   
   public void testNothing() throws Exception
   {
      ClassFilter filter = ClassFilter.NOTHING;
      assertFilterNoMatch("gibberish", filter);
      assertFilterNoMatch("", filter);
      assertFilterNoMatch(null, filter);
   }
   
   public void testJavaOnly() throws Exception
   {
      ClassFilter filter = ClassFilter.JAVA_ONLY;
      assertFilterMatches("java.x", filter);
      assertFilterMatches("java.lang.Object", filter);
      assertFilterMatches("java.lang.ref.Method", filter);
      assertFilterMatches("java.util.Collection", filter);
      assertFilterMatches("javax.x", filter);
      assertFilterMatches("javax.naming.Context", filter);
      assertFilterNoMatch("java.", filter);
      assertFilterNoMatch("java", filter);
      assertFilterNoMatch("javaa.", filter);
      assertFilterNoMatch("javaa.whatever", filter);
      assertFilterNoMatch("javax", filter);
      assertFilterNoMatch("javax.", filter);
      assertFilterNoMatch("javaxa.", filter);
      assertFilterNoMatch("javaxa.whatever", filter);
      assertFilterNoMatch("gibberish", filter);
      assertFilterNoMatch("", filter);
      assertFilterNoMatch(null, filter);
   }
}
