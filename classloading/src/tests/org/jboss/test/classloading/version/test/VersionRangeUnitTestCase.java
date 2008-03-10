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
import org.jboss.classloading.spi.version.VersionRange;
import org.jboss.test.classloading.AbstractClassLoadingTestWithSecurity;

/**
 * VersionRangeUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class VersionRangeUnitTestCase extends AbstractClassLoadingTestWithSecurity
{
   public static Test suite()
   {
      return suite(VersionRangeUnitTestCase.class);
   }

   public VersionRangeUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testAllVersions() throws Exception
   {
      VersionRange range = VersionRange.ALL_VERSIONS;
      assertVersionRange(range, Version.DEFAULT_VERSION, null);
   }
   
   public void testLow() throws Exception
   {
      testVersionRangeFromString("1.2.3");
      testVersionRangeFromString(null);
   }
   
   public void testLowHigh() throws Exception
   {
      testVersionRangeFromString(null, null);
      testVersionRangeFromString("1.2.3", null);
      testVersionRangeFromString(null, "4.5.6");
      testVersionRangeFromString("1.2.3", "4.5.6");
      
      assertBadVersionRangeFromString("2", "1");
      assertBadVersionRangeFromString("1.1", "1.0");
      assertBadVersionRangeFromString("1.1.1", "1.1.0");

      assertBadVersionRangeFromString("1", "1");
      assertBadVersionRangeFromString("1.1", "1.1");
      assertBadVersionRangeFromString("1.1.1", "1.1.1");
   }
   
   public void testLowHighWithInclusive() throws Exception
   {
      testVersionRangeFromStringAllPerms(null, null);
      testVersionRangeFromStringAllPerms("1.2.3", null);
      testVersionRangeFromStringAllPerms(null, "4.5.6");
      testVersionRangeFromStringAllPerms("1.2.3", "4.5.6");

      testVersionRangeFromString(null, true, "0.0.0", true);
      testVersionRangeFromString("1.0.0", true, "1.0.0", true);

      
      assertBadVersionRangeFromStringAllPerms("2", "1");
      assertBadVersionRangeFromStringAllPerms("1.1", "1.0");
      assertBadVersionRangeFromStringAllPerms("1.1.1", "1.1.0");

      assertBadVersionRangeFromStringNotBoth("1.1", "1.1");
      assertBadVersionRangeFromStringNotBoth("1.1.1", "1.1.1");
      
      assertBadVersionRangeFromStringNotBoth(null, "0.0.0");
   }
   
   public void testEquals() throws Exception
   {
      testEqualsFromString(null, null, null, null, true);
      testEqualsFromString("0.0.0", null, null, null, true);
      testEqualsFromString("0.0.0", null, "0.0.0", null, true);

      testEquals(Version.DEFAULT_VERSION, null, null, null, true);
      testEquals(Version.DEFAULT_VERSION, null, Version.DEFAULT_VERSION, null, true);

      testEquals(Version.DEFAULT_VERSION, null, "0.0.0", null, true);

      testEqualsFromString("1.0.0", null, "1.0.0", null, true);
      testEqualsFromString(null, "1.0.0", null, "1.0.0", true);
      testEqualsFromString("1.2.3", "4.5.6", "1.2.3", "4.5.6", true);

      testEqualsFromString(null, false, null, false, null, false, null, false, true);
      testEqualsFromString(null, false, null, false, null, false, null, true, true);
      testEqualsFromString(null, false, null, false, null, true, null, false, false);
      testEqualsFromString(null, false, null, false, null, true, null, true, false);
      testEqualsFromString(null, false, null, true, null, false, null, false, true);
      testEqualsFromString(null, false, null, true, null, false, null, true, true);
      testEqualsFromString(null, false, null, true, null, true, null, false, false);
      testEqualsFromString(null, false, null, true, null, true, null, true, false);
      testEqualsFromString(null, true, null, false, null, false, null, false, false);
      testEqualsFromString(null, true, null, false, null, false, null, true, false);
      testEqualsFromString(null, true, null, false, null, true, null, false, true);
      testEqualsFromString(null, true, null, false, null, true, null, true, true);
      testEqualsFromString(null, true, null, true, null, false, null, false, false);
      testEqualsFromString(null, true, null, true, null, false, null, true, false);
      testEqualsFromString(null, true, null, true, null, true, null, false, true);
      testEqualsFromString(null, true, null, true, null, true, null, true, true);

      testEqualsFromString(null, false, null, false, "0.0.0", false, null, false, true);
      testEqualsFromString(null, false, null, false, "0.0.0", true, null, false, false);

      testEqualsFromString("1.0.0", true, "1.0.0", true, "0.0.0", false, null, false, false);
      testEqualsFromString("1.0.0", true, "1.0.0", true, "0.0.0", false, null, true, false);
      testEqualsFromString("1.0.0", true, "1.0.0", true, "0.0.0", true, null, false, false);
      testEqualsFromString("1.0.0", true, "1.0.0", true, "0.0.0", true, null, true, false);

      testEqualsFromString("1.2.3", false, "4.5.6", false, "1.2.3", false, "4.5.6", false, true);
      testEqualsFromString("1.2.3", false, "4.5.6", false, "1.2.3", false, "4.5.6", true, false);
      testEqualsFromString("1.2.3", false, "4.5.6", false, "1.2.3", true, "4.5.6", false, false);
      testEqualsFromString("1.2.3", false, "4.5.6", false, "1.2.3", true, "4.5.6", true, false);
      testEqualsFromString("1.2.3", false, "4.5.6", true, "1.2.3", false, "4.5.6", false, false);
      testEqualsFromString("1.2.3", false, "4.5.6", true, "1.2.3", false, "4.5.6", true, true);
      testEqualsFromString("1.2.3", false, "4.5.6", true, "1.2.3", true, "4.5.6", false, false);
      testEqualsFromString("1.2.3", false, "4.5.6", true, "1.2.3", true, "4.5.6", true, false);
      testEqualsFromString("1.2.3", true, "4.5.6", false, "1.2.3", false, "4.5.6", false, false);
      testEqualsFromString("1.2.3", true, "4.5.6", false, "1.2.3", false, "4.5.6", true, false);
      testEqualsFromString("1.2.3", true, "4.5.6", false, "1.2.3", true, "4.5.6", false, true);
      testEqualsFromString("1.2.3", true, "4.5.6", false, "1.2.3", true, "4.5.6", true, false);
      testEqualsFromString("1.2.3", true, "4.5.6", true, "1.2.3", false, "4.5.6", false, false);
      testEqualsFromString("1.2.3", true, "4.5.6", true, "1.2.3", false, "4.5.6", true, false);
      testEqualsFromString("1.2.3", true, "4.5.6", true, "1.2.3", true, "4.5.6", false, false);
      testEqualsFromString("1.2.3", true, "4.5.6", true, "1.2.3", true, "4.5.6", true, true);
   }
   
   public void testIsInRange() throws Exception
   {
      testIsInRangeFromString(null, null, "0.0.0", true);
      testIsInRangeFromString(null, null, "1.2.3", true);
      testIsInRangeFromString("0.0.0", null, "0.0.0", true);
      testIsInRangeFromString("0.0.0", null, "1.2.3", true);

      testIsInRangeFromString("1.2.3", "4.5.6", "1.2.3", true);
      testIsInRangeFromString("1.2.3", "4.5.6", "4.5.6", false);

      testIsInRangeFromStringAllPerms("1.2.3", "4.5.6", "1.2.4", true);
      testIsInRangeFromStringAllPerms("1.2.3", "4.5.6", "4.5.5", true);
      testIsInRangeFromStringAllPerms("1.2.3", "4.5.6", "1.3", true);
      testIsInRangeFromStringAllPerms("1.2.3", "4.5.6", "4.4", true);
      testIsInRangeFromStringAllPerms("1.2.3", "4.5.6", "2.3.4", true);
      
      testIsInRangeFromStringAllPerms("1.2.3", "4.5.6", "0.0.0", false);
      testIsInRangeFromStringAllPerms("1.2.3", "4.5.6", "1.1", false);
      testIsInRangeFromStringAllPerms("1.2.3", "4.5.6", "1.2.2", false);
      testIsInRangeFromStringAllPerms("1.2.3", "4.5.6", "1.0", false);
      testIsInRangeFromStringAllPerms("1.2.3", "4.5.6", "0.9", false);
      testIsInRangeFromStringAllPerms("1.2.3", "4.5.6", "4.6", false);
      testIsInRangeFromStringAllPerms("1.2.3", "4.5.6", "4.5.7", false);
      testIsInRangeFromStringAllPerms("1.2.3", "4.5.6", "5.0", false);

      testIsInRangeFromString(null, false, null, false, "0.0.0", false);
      testIsInRangeFromString(null, false, null, true, "0.0.0", false);
      testIsInRangeFromString(null, true, null, false, "0.0.0", true);
      testIsInRangeFromString(null, true, null, true, "0.0.0", true);
      testIsInRangeFromString("0.0.0", false, null, false, "0.0.0", false);
      testIsInRangeFromString("0.0.0", false, null, true, "0.0.0", false);
      testIsInRangeFromString("0.0.0", true, null, false, "0.0.0", true);
      testIsInRangeFromString("0.0.0", true, null, true, "0.0.0", true);

      testIsInRangeFromString("1.2.3", false, "4.5.6", false, "1.2.3", false);
      testIsInRangeFromString("1.2.3", false, "4.5.6", true, "1.2.3", false);
      testIsInRangeFromString("1.2.3", true, "4.5.6", false, "1.2.3", true);
      testIsInRangeFromString("1.2.3", true, "4.5.6", true, "1.2.3", true);

      testIsInRangeFromString("1.2.3", false, "4.5.6", false, "4.5.6", false);
      testIsInRangeFromString("1.2.3", false, "4.5.6", true, "4.5.6", true);
      testIsInRangeFromString("1.2.3", true, "4.5.6", false, "4.5.6", false);
      testIsInRangeFromString("1.2.3", true, "4.5.6", true, "4.5.6", true);
   }
   
   public void testSerialization() throws Exception
   {
      VersionRange range = new VersionRange("1.0.0", "2.0.0");;
      VersionRange other = serializeDeserialize(range, VersionRange.class);
      assertVersionRange(other, "1.0.0", "2.0.0");
      assertEquals(range, other);
   }
   
   protected void testVersionRangeFromString(String low)
   {
      testVersionRange(low);
      
      Version lowVersion = null;
      if (low != null)
         lowVersion = Version.parseVersion(low);
      testVersionRange(lowVersion);
   }
   
   protected void testVersionRangeFromString(String low, String high)
   {
      testVersionRange(low, high);
      
      Version lowVersion = null;
      if (low != null)
         lowVersion = Version.parseVersion(low);
      Version highVersion = null;
      if (high != null)
         highVersion = Version.parseVersion(high);
      testVersionRange(lowVersion, highVersion);
   }
   
   protected void testVersionRangeFromStringAllPerms(String low, String high)
   {
      testVersionRangeFromString(low, false, high, false);
      testVersionRangeFromString(low, true, high, false);
      testVersionRangeFromString(low, false, high, true);
      testVersionRangeFromString(low, true, high, true);
   }
   
   protected void testVersionRangeFromString(String low, boolean lowInclusive, String high, boolean highInclusive)
   {
      testVersionRange(low, lowInclusive, high, highInclusive);
      
      Version lowVersion = null;
      if (low != null)
         lowVersion = Version.parseVersion(low);
      Version highVersion = null;
      if (high != null)
         highVersion = Version.parseVersion(high);
      testVersionRange(lowVersion, lowInclusive, highVersion, highInclusive);
   }
   
   protected void testVersionRange(Object low)
   {
      VersionRange range = new VersionRange(low);
      assertVersionRange(range, low, null);
      testToString(range, low, true, null, false);
   }
   
   protected void testVersionRange(Object low, Object high)
   {
      VersionRange range = new VersionRange(low, high);
      assertVersionRange(range, low, high);
      testToString(range, low, true, high, false);
   }
   
   protected void testVersionRange(Object low, boolean lowInclusive, Object high, boolean highInclusive)
   {
      VersionRange range = new VersionRange(low, lowInclusive, high, highInclusive);
      assertVersionRange(range, low, lowInclusive, high, highInclusive);
      testToString(range, low, lowInclusive, high, highInclusive);
   }
   
   protected void testToString(VersionRange range, Object low, boolean lowInclusive, Object high, boolean highInclusive)
   {
      StringBuilder expected = new StringBuilder();
      if (low == null)
         low = Version.DEFAULT_VERSION;
      if (lowInclusive)
         expected.append("[").append(low);
      else
         expected.append("(").append(low);
      expected.append(",");
      if (high == null)
         expected.append("?").append(")");
      else if (highInclusive)
         expected.append(high).append("]");
      else
         expected.append(high).append(")");
      assertEquals(expected.toString(), range.toString());
   }
   
   protected void assertBadVersionRangeFromString(String low, String high)
   {
      assertBadVersionRange(low, high);

      Version lowVersion = null;
      if (low != null)
         lowVersion = Version.parseVersion(low);
      Version highVersion = null;
      if (high != null)
         highVersion = Version.parseVersion(high);
      assertBadVersionRange(lowVersion, highVersion);
   }
   
   protected void assertBadVersionRange(Object low, Object high)
   {
      try
      {
         fail("Should not be here! Got " + new VersionRange(low, high));
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }
   
   protected void assertBadVersionRangeFromStringAllPerms(String low, String high)
   {
      assertBadVersionRangeFromString(low, false, high, false);
      assertBadVersionRangeFromString(low, true, high, false);
      assertBadVersionRangeFromString(low, false, high, true);
      assertBadVersionRangeFromString(low, true, high, true);
   }
   
   protected void assertBadVersionRangeFromStringNotBoth(String low, String high)
   {
      assertBadVersionRangeFromString(low, false, high, false);
      assertBadVersionRangeFromString(low, true, high, false);
      assertBadVersionRangeFromString(low, false, high, true);
   }
   
   protected void assertBadVersionRangeFromString(String low, boolean lowInclusive, String high, boolean highInclusive)
   {
      assertBadVersionRange(low, lowInclusive, high, highInclusive);

      Version lowVersion = null;
      if (low != null)
         lowVersion = Version.parseVersion(low);
      Version highVersion = null;
      if (high != null)
         highVersion = Version.parseVersion(high);
      assertBadVersionRange(lowVersion, lowInclusive, highVersion, highInclusive);
   }
   
   protected void assertBadVersionRange(Object low, boolean lowInclusive, Object high, boolean highInclusive)
   {
      try
      {
         fail("Should not be here! Got: " + new VersionRange(low, lowInclusive, high, highInclusive));
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }
   
   protected void testEqualsFromString(String low1, String high1, String low2, String high2, boolean result)
   {
      testEqualsFromString(low1, true, high1, false, low2, true, high2, false, result);
   }
   
   protected void testEqualsFromString(String low1, boolean low1Inclusive, String high1, boolean high1Inclusive, String low2, boolean low2Inclusive, String high2, boolean high2Inclusive, boolean result)
   {
      testEquals(low1, low1Inclusive, high1, high1Inclusive, low2, low2Inclusive, high2, high2Inclusive, result);
      
      Version versionLow1 = null;
      if (low1 != null)
         versionLow1 = Version.parseVersion(low1);
      Version versionHigh1 = null;
      if (high1 != null)
         versionHigh1 = Version.parseVersion(high1);
      Version versionLow2 = null;
      if (low2 != null)
         versionLow2 = Version.parseVersion(low2);
      Version versionHigh2 = null;
      if (high2 != null)
         versionHigh2 = Version.parseVersion(high2);
      testEquals(versionLow1, low1Inclusive, versionHigh1, high1Inclusive, versionLow2, low2Inclusive, versionHigh2, high2Inclusive, result);
   }
   
   protected void testEquals(Object low1, Object high1, Object low2, Object high2, boolean result)
   {
      testEquals(low1, true, high1, false, low2, true, high2, false, result);
   }
   
   protected void testEquals(Object low1, boolean low1Inclusive, Object high1, boolean high1Inclusive, Object low2, boolean low2Inclusive, Object high2, boolean high2Inclusive, boolean result)
   {
      VersionRange range1 = new VersionRange(low1, low1Inclusive, high1, high1Inclusive);
      VersionRange range2 = new VersionRange(low2, low2Inclusive, high2, high2Inclusive);
      testEquals(range1, range2, result);
   }
   
   protected void testEquals(VersionRange range1, VersionRange range2, boolean result)
   {
      if (result)
      {
         assertTrue("Expected " + range1 + ".equals(" + range2 + ") to be true", range1.equals(range2));
         assertTrue("Expected " + range2 + ".equals(" + range1 + ") to be true", range2.equals(range1));
      }
      else
      {
         assertFalse("Expected " + range1 + ".equals(" + range2 + ") to be false", range1.equals(range2));
         assertFalse("Expected " + range2 + ".equals(" + range1 + ") to be false", range2.equals(range1));
      }
   }
   
   protected void testIsInRangeFromString(String low, String high, String test, boolean result)
   {
      testIsInRangeFromString(low, true, high, false, test, result);
   }
   
   protected void testIsInRangeFromStringAllPerms(String low, String high, String test, boolean result)
   {
      testIsInRangeFromString(low, false, high, false, test, result);
      testIsInRangeFromString(low, false, high, true, test, result);
      testIsInRangeFromString(low, true, high, false, test, result);
      testIsInRangeFromString(low, true, high, true, test, result);
   }
   
   protected void testIsInRangeFromString(String low, boolean lowInclusive, String high, boolean highInclusive, String test, boolean result)
   {
      testIsInRange(low, lowInclusive, high, highInclusive, test, result);

      Version lowVersion = null;
      if (low != null)
         lowVersion = Version.parseVersion(low);
      Version highVersion = null;
      if (high != null)
         highVersion = Version.parseVersion(high);
      Version version = Version.parseVersion(test);
      testIsInRange(lowVersion, lowInclusive, highVersion, highInclusive, version, result);
   }
   
   protected void testIsInRange(Object low, boolean lowInclusive, Object high, boolean highInclusive, Object version, boolean result)
   {
      VersionRange range = new VersionRange(low, lowInclusive, high, highInclusive);
      testIsInRange(range, version, result);
   }
   
   protected void testIsInRange(VersionRange range, Object version, boolean result)
   {
      if (result)
         assertTrue("Expected " + range + ".isInRange(" + version + ") to be true", range.isInRange(version));
      else
         assertFalse("Expected " + range + ".isInRange(" + version + ") to be false", range.isInRange(version));
   }
}
