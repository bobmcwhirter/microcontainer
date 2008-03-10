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
import org.jboss.classloading.spi.version.VersionComparatorRegistry;
import org.jboss.test.classloading.AbstractClassLoadingTestWithSecurity;

/**
 * VersionUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class VersionUnitTestCase extends AbstractClassLoadingTestWithSecurity
{
   public static Test suite()
   {
      return suite(VersionUnitTestCase.class);
   }

   public VersionUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testDefaultVersion() throws Exception
   {
      Version version = Version.DEFAULT_VERSION;
      assertVersion(version, 0, 0, 0, null);
   }
   
   public void testMajorMinorMicro() throws Exception
   {
      testVersion(0, 0, 0);
      testVersion(1, 2, 3);
      testVersion(3, 2, 1);
      
      assertBadVersion(-1, 0, 0);
      assertBadVersion(0, -1, 0);
      assertBadVersion(0, 0, -1);
   }
   
   public void testMajorMinorMicroQualifier() throws Exception
   {
      testVersion(0, 0, 0, "GA");
      testVersion(1, 2, 3, "CR");
      testVersion(3, 2, 1, "Beta1");
      testVersion(0, 0, 0, "1234567890");
      testVersion(0, 0, 0, "abcdefghijklmnopqrstuvwxyz");
      testVersion(0, 0, 0, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
      testVersion(0, 0, 0, "_");
      testVersion(0, 0, 0, "-");
      testVersion(0, 0, 0, null);
      
      assertBadVersion(-1, 0, 0, "GA");
      assertBadVersion(0, -1, 0, "GA");
      assertBadVersion(0, 0, -1, "GA");
      assertBadVersion(0, 0, 0, "G A");
      assertBadVersion(0, 0, 0, "!");
   }
   
   public void testParseVersion() throws Exception
   {
      testVersion(null, 0, 0, 0, null);
      testVersion("", 0, 0, 0, null);
      testVersion("1", 1, 0, 0, null);
      testVersion("1.2", 1, 2, 0, null);
      testVersion("1.2.3", 1, 2, 3, null);
      testVersion("1.2.3.GA", 1, 2, 3, "GA");
      testVersion("10.2.3.GA", 10, 2, 3, "GA");
      testVersion("1.20.3.GA", 1, 20, 3, "GA");
      testVersion("1.2.30.GA", 1, 2, 30, "GA");
      testVersion("0.0.0.1234567890", 0, 0, 0, "1234567890");
      testVersion("0.0.0.abcdefghijklmnopqrstuvwxyz", 0, 0, 0, "abcdefghijklmnopqrstuvwxyz");
      testVersion("0.0.0.ABCDEFGHIJKLMNOPQRSTUVWXYZ", 0, 0, 0, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
      testVersion("0.0.0._", 0, 0, 0, "_");
      testVersion("0.0.0.-", 0, 0, 0, "-");
      testVersion(" ", 0, 0, 0, null);
      testVersion(" 1.2.3.GA", 1, 2, 3, "GA");
      testVersion("1 .2.3.GA", 1, 2, 3, "GA");
      testVersion("1. 2.3.GA", 1, 2, 3, "GA");
      testVersion("1.2 .3.GA", 1, 2, 3, "GA");
      testVersion("1.2. 3.GA", 1, 2, 3, "GA");
      testVersion("1.2.3 .GA", 1, 2, 3, "GA");
      testVersion("1.2.3. GA", 1, 2, 3, "GA");
      testVersion("1.2.3.GA ", 1, 2, 3, "GA");
      
      assertBadVersion("x");
      assertBadVersion("1.x");
      assertBadVersion("1.2.x");
      assertBadVersion("1.2.3.GA.x");
      assertBadVersion("1.2.3.G A");
      assertBadVersion("1.2.3.!");
      assertBadVersion("1 0.2.3.GA");
      assertBadVersion("1.2 0.3.GA");
      assertBadVersion("1.2.3 0.GA");
      assertBadVersion(".2.3.GA");
      assertBadVersion("1..3.GA");
      assertBadVersion("1.2..GA");
      assertBadVersion("1.2.3.");
   }
   
   public void testEquals() throws Exception
   {
      testEquals(null, Version.DEFAULT_VERSION, true);
      testEquals("", Version.DEFAULT_VERSION, true);
      testEquals(" ", Version.DEFAULT_VERSION, true);
      testEquals(0, Version.DEFAULT_VERSION, true);

      testEquals(1, Version.DEFAULT_VERSION, false);
      testEquals(0, 1, Version.DEFAULT_VERSION, false);
      testEquals(0, 0, 1, Version.DEFAULT_VERSION, false);
      testEquals(0, 0, 0, "GA", Version.DEFAULT_VERSION, false);

      testEquals(null, (String) null, true);
      testEquals(null, "", true);
      testEquals(null, " ", true);
      testEquals("", " ", true);
      testEquals("", 0, true);
      testEquals("1", 1, true);
      testEquals("1.2", 1, 2, true);
      testEquals("1.2.3", 1, 2, 3, true);
      testEquals("1.2.3.GA", 1, 2, 3, "GA", true);

      testEquals("1", 1, 2, 3, "GA", false);
      testEquals("1.2", 1, 2, 3, "GA", false);
      testEquals("1.2.3", 1, 2, 3, "GA", false);
      testEquals("2.2.3.GA", 1, 2, 3, "GA", false);
      testEquals("1.2.2.GA", 1, 2, 3, "GA", false);
      testEquals("1.2.2.AG", 1, 2, 3, "GA", false);
      testEquals("10.2.3.GA", 1, 2, 3, "GA", false);
      testEquals("1.20.3.GA", 1, 2, 3, "GA", false);
      testEquals("1.2.30.GA", 1, 2, 3, "GA", false);
   }
   
   public void testComareTo() throws Exception
   {
      testCompare(null, Version.DEFAULT_VERSION, 0);
      testCompare("", Version.DEFAULT_VERSION, 0);
      testCompare("0", Version.DEFAULT_VERSION, 0);

      testCompare("0", "0", 0);
      testCompare("0", "0.0", 0);
      testCompare("0", "0.0.0", 0);
      testCompare("0.0", "0.0.0", 0);

      testCompare("1", "1", 0);
      testCompare("1", "1.0", 0);
      testCompare("1", "1.0.0", 0);
      testCompare("1.0", "1.0.0", 0);

      testCompare("1", "2", -1);
      testCompare("1", "2.0", -1);
      testCompare("1", "2.0.0", -1);

      testCompare("1.0", "1.1", -1);
      testCompare("1.0", "1.1.0", -1);
      testCompare("1.0", "1.2", -1);
      testCompare("1.0", "1.2.0", -1);

      testCompare("1.1", "1.2", -1);
      testCompare("1.1", "1.2.0", -1);
      testCompare("1.1", "1.3", -1);
      testCompare("1.1", "1.3.0", -1);

      testCompare("1.0.0", "1.0.1", -1);
      testCompare("1.0.0", "1.0.2", -1);

      testCompare("1.1.1", "1.1.2", -1);
      testCompare("1.1.1", "1.1.3", -1);

      testCompare("1.0.0", "1.0.0.A", -1);
      testCompare("1.0.0.A", "1.0.0.B", -1);
      testCompare("1.0.0.AA", "1.0.0.AB", -1);
      testCompare("1.0.0.Beta1", "1.0.0.Beta2", -1);
      testCompare("1.0.0.Beta10", "1.0.0.Beta2", -1);
   }
   
   public void testSerialization() throws Exception
   {
      Version version = new Version(1, 0, 0, "GA");;
      Version other = serializeDeserialize(version, Version.class);
      assertVersion(other, 1, 0, 0, "GA");
      assertEquals(version, other);
   }
   
   protected void testVersion(int major, int minor, int micro)
   {
      Version version = new Version(major, minor, micro);
      assertVersion(version, major, minor, micro);
      testToString(version, major, minor, micro, null);
   }
   
   protected void testVersion(int major, int minor, int micro, String qualifier)
   {
      Version version = new Version(major, minor, micro, qualifier);
      assertVersion(version, major, minor, micro, qualifier);
      testToString(version, major, minor, micro, qualifier);
   }
   
   protected void testVersion(String test, int major, int minor, int micro, String qualifier)
   {
      Version version = Version.parseVersion(test);
      assertVersion(version, major, minor, micro, qualifier);
      testToString(version, major, minor, micro, qualifier);
   }
   
   protected void testToString(Version version, int major, int minor, int micro, String qualifier)
   {
      String expected = major + "." + minor + "." + micro;
      if (qualifier != null)
      {
         qualifier = qualifier.trim();
         if (qualifier.length() > 0)
            expected += "." + qualifier;
      }
      assertEquals(expected, version.toString());
   }
   
   protected void assertBadVersion(int major, int minor, int micro)
   {
      try
      {
         new Version(major, minor, micro);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }
   
   protected void assertBadVersion(int major, int minor, int micro, String qualifier)
   {
      try
      {
         new Version(major, minor, micro, qualifier);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }
   
   protected void assertBadVersion(String version)
   {
      try
      {
         Version.parseVersion(version);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }
   
   protected void testEquals(String test1, String test2, boolean result)
   {
      Version version1 = Version.parseVersion(test1);
      Version version2 = Version.parseVersion(test2);
      testVersionEquals(version1, version2, result);
   }
   
   protected void testEquals(String test1, int major, boolean result)
   {
      Version version1 = Version.parseVersion(test1);
      Version version2 = Version.parseVersion("" + major);
      testVersionEquals(version1, version2, result);
      version2 = Version.parseVersion(" " + major);
      testVersionEquals(version1, version2, result);
      version2 = Version.parseVersion("" + major + " ");
      testVersionEquals(version1, version2, result);
      version2 = Version.parseVersion(" " + major + " ");
      testVersionEquals(version1, version2, result);
      version2 = Version.parseVersion(major + ".0");
      testVersionEquals(version1, version2, result);
      version2 = Version.parseVersion(major + ".0.0");
      testVersionEquals(version1, version2, result);
   }
   
   protected void testEquals(String test1, int major, int minor, boolean result)
   {
      Version version1 = Version.parseVersion(test1);
      Version version2 = Version.parseVersion(major + "." + minor);
      testVersionEquals(version1, version2, result);
      version2 = Version.parseVersion(major + ". " + minor);
      testVersionEquals(version1, version2, result);
      version2 = Version.parseVersion(major + "." + minor + " ");
      testVersionEquals(version1, version2, result);
      version2 = Version.parseVersion(major + ". " + minor + " ");
      testVersionEquals(version1, version2, result);
      version2 = Version.parseVersion(major + ". " + minor + ".0");
      testVersionEquals(version1, version2, result);
   }
   
   protected void testEquals(String test1, int major, int minor, int micro, boolean result)
   {
      Version version1 = Version.parseVersion(test1);
      Version version2 = Version.parseVersion(major + "." + minor + "." + micro);
      testVersionEquals(version1, version2, result);
      version2 = Version.parseVersion(major + "." + minor + ". " + micro);
      testVersionEquals(version1, version2, result);
      version2 = Version.parseVersion(major + "." + minor + "." + micro + " ");
      testVersionEquals(version1, version2, result);
      version2 = Version.parseVersion(major + "." + minor + ". " + micro + " ");
      testVersionEquals(version1, version2, result);
   }
   
   protected void testEquals(String test1, int major, int minor, int micro, String qualifier, boolean result)
   {
      Version version1 = Version.parseVersion(test1);
      Version version2 = Version.parseVersion(major + "." + minor + "." + micro + "." + qualifier);
      testVersionEquals(version1, version2, result);
      version2 = Version.parseVersion(major + "." + minor + "." + micro + ". " + qualifier);
      testVersionEquals(version1, version2, result);
      version2 = Version.parseVersion(major + "." + minor + "." + micro + "." + qualifier + " ");
      testVersionEquals(version1, version2, result);
      version2 = Version.parseVersion(major + "." + minor + "." + micro + ". " + qualifier + " ");
      testVersionEquals(version1, version2, result);
   }
   
   protected void testEquals(String test1, Version version2, boolean result)
   {
      Version version1 = Version.parseVersion(test1);
      testVersionEquals(version1, version2, result);
   }
   
   protected void testEquals(int major, Version version2, boolean result)
   {
      Version version1 = Version.parseVersion("" + major);
      testVersionEquals(version1, version2, result);
      version1 = Version.parseVersion(" " + major);
      testVersionEquals(version1, version2, result);
      version1 = Version.parseVersion(major + " ");
      testVersionEquals(version1, version2, result);
      version1 = Version.parseVersion(" " + major + " ");
      testVersionEquals(version1, version2, result);
      version1 = Version.parseVersion(major + ".0");
      testVersionEquals(version1, version2, result);
      version1 = Version.parseVersion(major + ".0.0");
      testVersionEquals(version1, version2, result);
   }
   
   protected void testEquals(int major, int minor, Version version2, boolean result)
   {
      Version version1 = Version.parseVersion(major + "." + minor);
      testVersionEquals(version1, version2, result);
      version1 = Version.parseVersion(major + ". " + minor);
      testVersionEquals(version1, version2, result);
      version1 = Version.parseVersion(major + "." + minor + " ");
      testVersionEquals(version1, version2, result);
      version1 = Version.parseVersion(major + ". " + minor + " ");
      testVersionEquals(version1, version2, result);
      version1 = Version.parseVersion(major + "." + minor + ".0");
      testVersionEquals(version1, version2, result);
   }
   
   protected void testEquals(int major, int minor, int micro, Version version2, boolean result)
   {
      Version version1 = Version.parseVersion(major + "." + minor + "." + micro);
      testVersionEquals(version1, version2, result);
      version1 = Version.parseVersion(major + "." + minor + ". " + micro);
      testVersionEquals(version1, version2, result);
      version1 = Version.parseVersion(major + "." + minor + "." + micro + " ");
      testVersionEquals(version1, version2, result);
      version1 = Version.parseVersion(major + "." + minor + ". " + micro + " ");
      testVersionEquals(version1, version2, result);
   }
   
   protected void testEquals(int major, int minor, int micro, String qualifier, Version version2, boolean result)
   {
      Version version1 = Version.parseVersion(major + "." + minor + "." + micro + "." + qualifier);
      testVersionEquals(version1, version2, result);
      version1 = Version.parseVersion(major + "." + minor + "." + micro + ". " + qualifier);
      testVersionEquals(version1, version2, result);
      version1 = Version.parseVersion(major + "." + minor + "." + micro + "." + qualifier + " ");
      testVersionEquals(version1, version2, result);
      version1 = Version.parseVersion(major + "." + minor + "." + micro + ". " + qualifier + " ");
      testVersionEquals(version1, version2, result);
   }
   
   protected void testVersionEquals(Version version1, Version version2, boolean result)
   {
      if (result)
      {
         assertTrue("Expected " + version1 + ".equals(" + version2 + ") to be true", version1.equals(version2));
         assertTrue("Expected " + version2 + ".equals(" + version1 + ") to be true", version2.equals(version1));
      }
      else
      {
         assertFalse("Expected " + version1 + ".equals(" + version2 + ") to be false", version1.equals(version2));
         assertFalse("Expected " + version2 + ".equals(" + version1 + ") to be false", version2.equals(version1));
      }
   }
   
   protected void testCompare(String test1, String test2, int result)
   {
      Version version1 = Version.parseVersion(test1);
      Version version2 = Version.parseVersion(test2);
      testVersionCompare(version1, version2, result);
      testVersionCompareViaRegistry(test1, test2, result);
   }
   
   protected void testCompare(String test1, Version version2, int result)
   {
      Version version1 = Version.parseVersion(test1);
      testVersionCompare(version1, version2, result);
      testVersionCompareViaRegistry(test1, version2, result);
   }
   
   protected void testVersionCompare(Version version1, Version version2, int result)
   {
      if (result < 0)
      {
         assertTrue("Expected " + version1 + ".compareTo(" + version2 + ") to be negative " + version1.compareTo(version2), version1.compareTo(version2) < 0);
         assertTrue("Expected " + version2 + ".compareTo(" + version1 + ") to be positive " + version2.compareTo(version1), version2.compareTo(version1) > 0);
      }
      else if (result > 0)
      {
         assertTrue("Expected " + version1 + ".compareTo(" + version2 + ") to be positive " + version1.compareTo(version2), version1.compareTo(version2) > 0);
         assertTrue("Expected " + version2 + ".compareTo(" + version1 + ") to be negative" + version2.compareTo(version1), version2.compareTo(version1) < 0);
      }
      else
      {
         assertTrue("Expected " + version1 + ".compareTo(" + version2 + ") to be zero " + version1.compareTo(version2), version1.compareTo(version2) == 0);
         assertTrue("Expected " + version2 + ".compareTo(" + version1 + ") to be zero" + version2.compareTo(version1), version2.compareTo(version1) == 0);
      }
      testVersionCompareViaRegistry(version1, version2, result);
   }
   
   protected void testVersionCompareViaRegistry(String version1, String version2, int result)
   {
      if (version1 == null)
         return;
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
   }
   
   protected void testVersionCompareViaRegistry(String version1, Version version2, int result)
   {
      if (version1 == null)
         return;
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
   }
   
   protected void testVersionCompareViaRegistry(Version version1, Version version2, int result)
   {
      if (version1 == null)
         return;
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
   }
}
