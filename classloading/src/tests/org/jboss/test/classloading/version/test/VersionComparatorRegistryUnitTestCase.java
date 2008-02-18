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
package org.jboss.test.classloading.version.test;

import junit.framework.Test;

import org.jboss.classloading.spi.version.Version;
import org.jboss.classloading.spi.version.VersionComparator;
import org.jboss.classloading.spi.version.VersionComparatorRegistry;
import org.jboss.test.classloading.AbstractClassLoadingTestWithSecurity;
import org.jboss.test.classloading.version.support.TestStringToMyVersionComparator;
import org.jboss.test.classloading.version.support.TestStringToStringComparator;
import org.jboss.test.classloading.version.support.TestStringToVersionComparator;
import org.jboss.test.classloading.version.support.MyVersion;
import org.jboss.test.classloading.version.support.MyVersionToMyVersionComparator;
import org.jboss.test.classloading.version.support.TestVersionToStringComparator;
import org.jboss.test.classloading.version.support.TestVersionToVersionComparator;

/**
 * VersionComparatorRegistryUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class VersionComparatorRegistryUnitTestCase extends AbstractClassLoadingTestWithSecurity
{
   public static Test suite()
   {
      return suite(VersionComparatorRegistryUnitTestCase.class);
   }

   public VersionComparatorRegistryUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testComparatorSameClass()
   {
      VersionComparatorRegistry registry = new VersionComparatorRegistry();
      MyVersion version1 = MyVersion.parseMyVersion("1.0");
      MyVersion version2 = MyVersion.parseMyVersion("2.0");
      
      try
      {
         registry.compare(version1, version2);
         fail("Should not be here");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      MyVersionToMyVersionComparator comparator = new MyVersionToMyVersionComparator();
      registry.registerVersionComparator(MyVersion.class, comparator);
      assertTrue(registry.compare(version1, version2) < 0);
      assertTrue(registry.compare(version2, version1) > 0);
      
      registry.removeVersionComparator(MyVersion.class);
      
      try
      {
         registry.compare(version1, version2);
         fail("Should not be here");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      registry.registerVersionComparator(MyVersion.class, MyVersion.class, comparator);
      assertTrue(registry.compare(version1, version2) < 0);
      assertTrue(registry.compare(version2, version1) > 0);
      
      registry.removeVersionComparator(MyVersion.class, MyVersion.class);
      
      try
      {
         registry.compare(version1, version2);
         fail("Should not be here");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }
   
   public void testComparatorDifferentClass()
   {
      VersionComparatorRegistry registry = new VersionComparatorRegistry();
      String version1 = "1.0";
      MyVersion version2 = MyVersion.parseMyVersion("2.0");
      
      try
      {
         registry.compare(version1, version2);
         fail("Should not be here");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      TestStringToMyVersionComparator comparator = new TestStringToMyVersionComparator();
      registry.registerVersionComparator(String.class, MyVersion.class, comparator);
      assertTrue(registry.compare(version1, version2) < 0);
      assertTrue(registry.compare(version2, version1) > 0);
      
      registry.removeVersionComparator(MyVersion.class, String.class);
      
      try
      {
         registry.compare(version1, version2);
         fail("Should not be here");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      registry.registerVersionComparator(String.class, MyVersion.class, comparator);
      assertTrue(registry.compare(version1, version2) < 0);
      assertTrue(registry.compare(version2, version1) > 0);
      
      registry.removeVersionComparator(MyVersion.class, String.class);
      
      try
      {
         registry.compare(version1, version2);
         fail("Should not be here");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }
   
   public void testReplaceBuiltInComparator() throws Exception
   {
      testReplaceBuiltInComparator(Version.class, new TestVersionToVersionComparator());
      testReplaceBuiltInComparator(String.class, new TestStringToStringComparator());
      testReplaceBuiltInComparator(Version.class, String.class, new TestVersionToStringComparator());
      testReplaceBuiltInComparator(String.class, Version.class, new TestStringToVersionComparator());
   }
   
   public void testVersionVersionCompare() throws Exception
   {
      testVersionVersionCompare(null, Version.DEFAULT_VERSION, 0);
      testVersionVersionCompare("", Version.DEFAULT_VERSION, 0);
      testVersionVersionCompare("0", Version.DEFAULT_VERSION, 0);

      testVersionVersionCompare("0", "0", 0);
      testVersionVersionCompare("0", "0.0", 0);
      testVersionVersionCompare("0", "0.0.0", 0);
      testVersionVersionCompare("0.0", "0.0.0", 0);

      testVersionVersionCompare("1", "1", 0);
      testVersionVersionCompare("1", "1.0", 0);
      testVersionVersionCompare("1", "1.0.0", 0);
      testVersionVersionCompare("1.0", "1.0.0", 0);

      testVersionVersionCompare("1", "2", -1);
      testVersionVersionCompare("1", "2.0", -1);
      testVersionVersionCompare("1", "2.0.0", -1);

      testVersionVersionCompare("1.0", "1.1", -1);
      testVersionVersionCompare("1.0", "1.1.0", -1);
      testVersionVersionCompare("1.0", "1.2", -1);
      testVersionVersionCompare("1.0", "1.2.0", -1);

      testVersionVersionCompare("1.1", "1.2", -1);
      testVersionVersionCompare("1.1", "1.2.0", -1);
      testVersionVersionCompare("1.1", "1.3", -1);
      testVersionVersionCompare("1.1", "1.3.0", -1);

      testVersionVersionCompare("1.0.0", "1.0.1", -1);
      testVersionVersionCompare("1.0.0", "1.0.2", -1);

      testVersionVersionCompare("1.1.1", "1.1.2", -1);
      testVersionVersionCompare("1.1.1", "1.1.3", -1);

      testVersionVersionCompare("1.0.0", "1.0.0.A", -1);
      testVersionVersionCompare("1.0.0.A", "1.0.0.B", -1);
      testVersionVersionCompare("1.0.0.AA", "1.0.0.AB", -1);
      testVersionVersionCompare("1.0.0.Beta1", "1.0.0.Beta2", -1);
      testVersionVersionCompare("1.0.0.Beta10", "1.0.0.Beta2", -1);
   }
   
   public void testVersionStringCompare() throws Exception
   {
      testVersionStringCompare("", Version.DEFAULT_VERSION, 0);
      testVersionStringCompare("0", Version.DEFAULT_VERSION, 0);

      testVersionStringCompare("0", "0", 0);
      testVersionStringCompare("0", "0.0", 0);
      testVersionStringCompare("0", "0.0.0", 0);
      testVersionStringCompare("0.0", "0.0.0", 0);

      testVersionStringCompare("1", "1", 0);
      testVersionStringCompare("1", "1.0", 0);
      testVersionStringCompare("1", "1.0.0", 0);
      testVersionStringCompare("1.0", "1.0.0", 0);

      testVersionStringCompare("1", "2", -1);
      testVersionStringCompare("1", "2.0", -1);
      testVersionStringCompare("1", "2.0.0", -1);

      testVersionStringCompare("1.0", "1.1", -1);
      testVersionStringCompare("1.0", "1.1.0", -1);
      testVersionStringCompare("1.0", "1.2", -1);
      testVersionStringCompare("1.0", "1.2.0", -1);

      testVersionStringCompare("1.1", "1.2", -1);
      testVersionStringCompare("1.1", "1.2.0", -1);
      testVersionStringCompare("1.1", "1.3", -1);
      testVersionStringCompare("1.1", "1.3.0", -1);

      testVersionStringCompare("1.0.0", "1.0.1", -1);
      testVersionStringCompare("1.0.0", "1.0.2", -1);

      testVersionStringCompare("1.1.1", "1.1.2", -1);
      testVersionStringCompare("1.1.1", "1.1.3", -1);

      testVersionStringCompare("1.0.0", "1.0.0.A", -1);
      testVersionStringCompare("1.0.0.A", "1.0.0.B", -1);
      testVersionStringCompare("1.0.0.AA", "1.0.0.AB", -1);
      testVersionStringCompare("1.0.0.Beta1", "1.0.0.Beta2", -1);
      testVersionStringCompare("1.0.0.Beta10", "1.0.0.Beta2", -1);
   }
   
   public void testStringStringCompare() throws Exception
   {
      testStringStringCompare("0", "0", 0);
      testStringStringCompare("0", "0.0", 0);
      testStringStringCompare("0", "0.0.0", 0);
      testStringStringCompare("0.0", "0.0.0", 0);

      testStringStringCompare("1", "1", 0);
      testStringStringCompare("1", "1.0", 0);
      testStringStringCompare("1", "1.0.0", 0);
      testStringStringCompare("1.0", "1.0.0", 0);

      testStringStringCompare("1", "2", -1);
      testStringStringCompare("1", "2.0", -1);
      testStringStringCompare("1", "2.0.0", -1);

      testStringStringCompare("1.0", "1.1", -1);
      testStringStringCompare("1.0", "1.1.0", -1);
      testStringStringCompare("1.0", "1.2", -1);
      testStringStringCompare("1.0", "1.2.0", -1);

      testStringStringCompare("1.1", "1.2", -1);
      testStringStringCompare("1.1", "1.2.0", -1);
      testStringStringCompare("1.1", "1.3", -1);
      testStringStringCompare("1.1", "1.3.0", -1);

      testStringStringCompare("1.0.0", "1.0.1", -1);
      testStringStringCompare("1.0.0", "1.0.2", -1);

      testStringStringCompare("1.1.1", "1.1.2", -1);
      testStringStringCompare("1.1.1", "1.1.3", -1);

      testStringStringCompare("1.0.0", "1.0.0.A", -1);
      testStringStringCompare("1.0.0.A", "1.0.0.B", -1);
      testStringStringCompare("1.0.0.AA", "1.0.0.AB", -1);
      testStringStringCompare("1.0.0.Beta1", "1.0.0.Beta2", -1);
      testStringStringCompare("1.0.0.Beta10", "1.0.0.Beta2", -1);
   }
   
   protected <T> void testReplaceBuiltInComparator(Class<T> classT, VersionComparator<T, T> comparator)
   {
      VersionComparatorRegistry registry = new VersionComparatorRegistry();
      try
      {
         registry.registerVersionComparator(classT, comparator);
         fail("Should not be here");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      try
      {
         registry.registerVersionComparator(classT, classT, comparator);
         fail("Should not be here");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      try
      {
         registry.removeVersionComparator(classT);
         fail("Should not be here");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      try
      {
         registry.removeVersionComparator(classT, classT);
         fail("Should not be here");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      try
      {
         registry.registerVersionComparator(classT, classT, null);
         fail("Should not be here");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }
   
   protected <T, U> void testReplaceBuiltInComparator(Class<T> classT, Class<U> classU, VersionComparator<T, U> comparator)
   {
      VersionComparatorRegistry registry = new VersionComparatorRegistry();
      try
      {
         registry.registerVersionComparator(classT, classU, comparator);
         fail("Should not be here");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      try
      {
         registry.removeVersionComparator(classT, classU);
         fail("Should not be here");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      try
      {
         registry.registerVersionComparator(classT, classU, null);
         fail("Should not be here");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }
   
   protected void testVersionVersionCompare(String test1, String test2, int result)
   {
      Version version1 = Version.parseVersion(test1);
      Version version2 = Version.parseVersion(test2);
      testVersionVersionCompareViaRegistry(version1, version2, result);
   }
   
   protected void testVersionVersionCompare(String test1, Version version2, int result)
   {
      Version version1 = Version.parseVersion(test1);
      testVersionVersionCompareViaRegistry(version1, version2, result);
   }
   
   protected void testVersionStringCompare(String version1, String test2, int result)
   {
      Version version2 = Version.parseVersion(test2);
      testVersionVersionCompareViaRegistry(version1, version2, result);
   }
   
   protected void testVersionStringCompare(String version1, Version version2, int result)
   {
      testVersionVersionCompareViaRegistry(version1, version2, result);
   }
   
   protected void testStringStringCompare(String version1, String version2, int result)
   {
      testVersionVersionCompareViaRegistry(version1, version2, result);
   }
   
   protected void testVersionVersionCompareViaRegistry(Object version1, Object version2, int result)
   {
      VersionComparatorRegistry registry = VersionComparatorRegistry.getInstance();
      if (result < 0)
      {
         assertTrue("Expected " + version1 + ".compareTo(" + version2 + ") to be negative " + registry.compare(version1, version2), registry.compare(version1, version2) < 0);
         assertTrue("Expected " + version2 + ".compareTo(" + version1 + ") to be positive " + registry.compare(version2, version1), registry.compare(version2, version1) > 0);
      }
      else if (result > 0)
      {
         assertTrue("Expected " + version1 + ".compareTo(" + version2 + ") to be positive " + registry.compare(version1, version2), registry.compare(version1, version2) > 0);
         assertTrue("Expected " + version2 + ".compareTo(" + version1 + ") to be negative" + registry.compare(version2, version1), registry.compare(version2, version1) < 0);
      }
      else
      {
         assertTrue("Expected " + version1 + ".compareTo(" + version2 + ") to be zero " + registry.compare(version1, version2), registry.compare(version1, version2) == 0);
         assertTrue("Expected " + version2 + ".compareTo(" + version1 + ") to be zero" + registry.compare(version2, version1), registry.compare(version2, version1) == 0);
      }

      if (result == 0)
      {
         assertTrue("Expected " + version1 + ".equals(" + version2 + ") to be true", registry.same(version1, version2));
         assertTrue("Expected " + version2 + ".equals(" + version1 + ") to be true", registry.same(version2, version1));
      }
      else
      {
         assertFalse("Expected " + version1 + ".equals(" + version2 + ") to be false", registry.same(version1, version2));
         assertFalse("Expected " + version2 + ".equals(" + version1 + ") to be false", registry.same(version2, version1));
      }
   }
}
