/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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

import org.jboss.classloader.plugins.filter.CombiningClassFilter;
import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.classloader.spi.filter.PackageClassFilter;
import org.jboss.test.classloader.AbstractClassLoaderTestWithSecurity;

/**
 * CombiningFilterUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class CombiningFilterUnitTestCase extends AbstractClassLoaderTestWithSecurity
{
   public static Test suite()
   {
      return suite(CombiningFilterUnitTestCase.class);
   }

   public CombiningFilterUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testNothing() throws Exception
   {
      ClassFilter filter = CombiningClassFilter.create();
      assertFilterNoMatchClassName("gibberish", filter);
      assertFilterNoMatchClassName("", filter);
      assertFilterNoMatchClassName(null, filter);
      assertFilterNoMatchResourcePath("x.xml", filter);
      assertFilterNoMatchPackageName("gibberish", filter);
      assertFilterNoMatchPackageName("", filter);
      assertFilterNoMatchPackageName(null, filter);
   }
   
   public void testJavaLangReflectAnd() throws Exception
   {
      ClassFilter filter1 = PackageClassFilter.createPackageClassFilter("java.lang");
      ClassFilter filter2 = PackageClassFilter.createPackageClassFilter("java.lang.reflect");
      ClassFilter filter = CombiningClassFilter.create(true, filter1, filter2);
      assertFilterMatchesClassName("java.lang.Object", filter);
      assertFilterMatchesClassName("java.lang.X", filter);
      assertFilterNoMatchClassName("java.lang", filter);
      assertFilterNoMatchClassName("java.lang.", filter);
      assertFilterNoMatchClassName("java.lang.reflect.Method", filter);
      assertFilterNoMatchClassName("gibberish", filter);
      assertFilterNoMatchClassName("", filter);
      assertFilterNoMatchClassName(null, filter);
      assertFilterMatchesResourcePath("java/lang/something.xml", filter);
      assertFilterMatchesPackageName("java.lang", filter);
      assertFilterNoMatchPackageName("java.langx", filter);
      assertFilterNoMatchPackageName("java.lang.X.", filter);
      assertFilterNoMatchPackageName("gibberish", filter);
      assertFilterNoMatchPackageName("", filter);
      assertFilterNoMatchPackageName(null, filter);
   }
   
   public void testJavaLangReflectOr() throws Exception
   {
      ClassFilter filter1 = PackageClassFilter.createPackageClassFilter("java.lang");
      ClassFilter filter2 = PackageClassFilter.createPackageClassFilter("java.lang.reflect");
      ClassFilter filter = CombiningClassFilter.create(false, filter1, filter2);
      assertFilterMatchesClassName("java.lang.Object", filter);
      assertFilterMatchesClassName("java.lang.X", filter);
      assertFilterMatchesClassName("java.lang.reflect.Method", filter);
      assertFilterMatchesClassName("java.lang.reflect.X", filter);
      assertFilterMatchesClassName("java.lang.reflect", filter);
      assertFilterNoMatchClassName("java.lang", filter);
      assertFilterNoMatchClassName("java.lang.", filter);
      assertFilterNoMatchClassName("java.lang.reflect.", filter);
      assertFilterNoMatchClassName("java.lang.reflect.subpackage.Something", filter);
      assertFilterNoMatchClassName("gibberish", filter);
      assertFilterNoMatchClassName("", filter);
      assertFilterNoMatchClassName(null, filter);
      assertFilterMatchesPackageName("java.lang", filter);
      assertFilterNoMatchPackageName("java.langx", filter);
      assertFilterNoMatchPackageName("java.lang.X.", filter);
      assertFilterMatchesPackageName("java.lang.reflect", filter);
      assertFilterNoMatchPackageName("java.lang.reflectx", filter);
      assertFilterNoMatchPackageName("java.lang.reflect.X", filter);
      assertFilterNoMatchPackageName("gibberish", filter);
      assertFilterNoMatchPackageName("", filter);
      assertFilterNoMatchPackageName(null, filter);
   }
}
