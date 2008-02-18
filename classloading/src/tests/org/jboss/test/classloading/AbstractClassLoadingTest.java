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
package org.jboss.test.classloading;

import org.jboss.classloader.plugins.ClassLoaderUtils;
import org.jboss.classloader.test.support.MockClassLoaderHelper;
import org.jboss.classloading.spi.version.Version;
import org.jboss.classloading.spi.version.VersionRange;
import org.jboss.test.AbstractTestCaseWithSetup;
import org.jboss.test.AbstractTestDelegate;

/**
 * AbstractClassLoadingTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractClassLoadingTest extends AbstractTestCaseWithSetup
{
   public static AbstractTestDelegate getDelegate(Class<?> clazz)
   {
      return new AbstractTestDelegate(clazz);
   }

   @Override
   protected void setUp() throws Exception
   {
      super.setUp();
      configureLogging();
   }

   public AbstractClassLoadingTest(String name)
   {
      super(name);
   }

   protected void assertVersion(Version version, int major, int minor, int micro)
   {
      assertVersion(version, major, minor, micro, null);
   }

   protected void assertVersion(Version version, int major, int minor, int micro, String qualifier)
   {
      assertNotNull(version);
      assertEquals(major, version.getMajor());
      assertEquals(minor, version.getMinor());
      assertEquals(micro, version.getMicro());
      if (qualifier == null)
         assertEquals("", version.getQualifier());
      else
         assertEquals(qualifier, version.getQualifier());
   }

   protected void assertVersionRange(VersionRange range, Object low, Object high)
   {
      assertVersionRange(range, low, true, high, false);
   }

   protected void assertVersionRange(VersionRange range, Object low, boolean lowInclusive, Object high, boolean highInclusive)
   {
      assertNotNull(range);
      if (low == null)
         assertEquals(Version.DEFAULT_VERSION, range.getLow());
      else
         assertEquals(low, range.getLow());
      assertEquals(lowInclusive, range.isLowInclusive());
      assertEquals(high, range.getHigh());
      assertEquals(highInclusive, range.isHighInclusive());
   }
   
   protected void assertClassLoader(Class<?> clazz, ClassLoader expected)
   {
      if (expected == null)
         return;
      boolean result = MockClassLoaderHelper.isExpectedClassLoader(clazz, expected);
      assertTrue(ClassLoaderUtils.classToString(clazz) + " should have expected classloader=" + expected, result);
   }
   
   protected void assertClassEquality(Class<?> expected, Class<?> actual)
   {
      assertTrue("Should be the same " + ClassLoaderUtils.classToString(expected) +" and " + ClassLoaderUtils.classToString(actual), expected == actual);
   }
   
   protected void assertNoClassEquality(Class<?> expected, Class<?> actual)
   {
      assertTrue("Should NOT be the same " + ClassLoaderUtils.classToString(expected) +" and " + ClassLoaderUtils.classToString(actual), expected != actual);
   }
   
   protected Class<?> assertLoadClass(Class<?> reference, ClassLoader start)
   {
      return assertLoadClass(reference, start, start, false);
   }
   
   protected Class<?> assertLoadClass(Class<?> reference, ClassLoader start, boolean isReference)
   {
      return assertLoadClass(reference, start, start, isReference);
   }
   
   protected Class<?> assertLoadClass(Class<?> reference, ClassLoader start, ClassLoader expected)
   {
      return assertLoadClass(reference, start, expected, false);
   }
   
   protected Class<?> assertLoadClass(Class<?> reference, ClassLoader start, ClassLoader expected, boolean isReference)
   {
      Class<?> result = assertLoadClass(reference.getName(), start, expected);
      if (isReference)
         assertClassEquality(reference, result);
      else
         assertNoClassEquality(reference, result);
      return result;
   }
   
   protected Class<?> assertLoadClass(String name, ClassLoader start)
   {
      return assertLoadClass(name, start, start);
   }
   
   protected Class<?> assertLoadClass(String name, ClassLoader start, ClassLoader expected)
   {
      Class<?> result = null;
      try
      {
         result = start.loadClass(name);
         getLog().debug("Got class: " + ClassLoaderUtils.classToString(result) + " for " + name + " from " + start);
      }
      catch (ClassNotFoundException e)
      {
         failure("Did not expect CNFE for " + name + " from " + start, e);
      }
      assertClassLoader(result, expected);
      return result;
   }
   
   protected void assertLoadClassFail(Class<?> reference, ClassLoader start)
   {
      assertLoadClassFail(reference.getName(), start);
   }
      
   protected void assertLoadClassFail(String name, ClassLoader start)
   {
      try
      {
         start.loadClass(name);
         fail("Should not be here!");
      }
      catch (Exception expected)
      {
         checkThrowable(ClassNotFoundException.class, expected);
      }
   }

   protected void configureLogging()
   {
      //enableTrace("org.jboss.classloading");
   }
}
