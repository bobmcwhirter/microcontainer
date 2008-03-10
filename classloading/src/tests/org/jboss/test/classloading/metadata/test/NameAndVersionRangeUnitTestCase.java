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
package org.jboss.test.classloading.metadata.test;

import junit.framework.Test;

import org.jboss.classloading.spi.helpers.NameAndVersionRangeSupport;
import org.jboss.classloading.spi.version.VersionRange;
import org.jboss.test.classloading.AbstractClassLoadingTestWithSecurity;

/**
 * NameAndVersionRangeUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class NameAndVersionRangeUnitTestCase extends AbstractClassLoadingTestWithSecurity
{
   public static Test suite()
   {
      return suite(NameAndVersionRangeUnitTestCase.class);
   }

   public NameAndVersionRangeUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testConstructors() throws Exception
   {
      NameAndVersionRangeSupport test = new NameAndVersionRangeSupport();
      assertNotNull(test.getName());
      assertEquals(VersionRange.ALL_VERSIONS, test.getVersionRange());

      test = new NameAndVersionRangeSupport("test");
      assertEquals("test", test.getName());
      assertEquals(VersionRange.ALL_VERSIONS, test.getVersionRange());

      VersionRange range = new VersionRange("1.0.0", "2.0.0");
      test = new NameAndVersionRangeSupport("test", range);
      assertEquals("test", test.getName());
      assertEquals(range, test.getVersionRange());
      
      try
      {
         fail("Should not be here for: " + new NameAndVersionRangeSupport(null));
      }
      catch (Throwable t)
      {
         checkDeepThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         fail("Should not be here for: " + new NameAndVersionRangeSupport(null, range));
      }
      catch (Throwable t)
      {
         checkDeepThrowable(IllegalArgumentException.class, t);
      }
   }
   
   public void testSetName() throws Exception
   {
      NameAndVersionRangeSupport test = new NameAndVersionRangeSupport();
      assertNotNull(test.getName());
      assertEquals(VersionRange.ALL_VERSIONS, test.getVersionRange());
      test.setName("name");
      assertEquals("name", test.getName());
      assertEquals(VersionRange.ALL_VERSIONS, test.getVersionRange());
      
      test = new NameAndVersionRangeSupport();
      try
      {
         test.setName(null);
      }
      catch (Throwable t)
      {
         checkDeepThrowable(IllegalArgumentException.class, t);
      }
   }
   
   public void testSetVersionRange() throws Exception
   {
      NameAndVersionRangeSupport test = new NameAndVersionRangeSupport();
      assertNotNull(test.getName());
      assertEquals(VersionRange.ALL_VERSIONS, test.getVersionRange());
      VersionRange range = new VersionRange("1.0.0", "2.0.0");
      test.setVersionRange(range);
      assertNotNull(test.getName());
      assertEquals(range, test.getVersionRange());
      test.setVersionRange(null);
      assertNotNull(test.getName());
      assertEquals(VersionRange.ALL_VERSIONS, test.getVersionRange());
   }
      
   public void testEquals() throws Exception
   {
      testEquals("a", VersionRange.ALL_VERSIONS, "a", VersionRange.ALL_VERSIONS, true);
      testEquals("a", VersionRange.ALL_VERSIONS, "a", null, true);
      VersionRange range1 = new VersionRange("1.0.0", true, "1.0.0", true);
      testEquals("b", range1, "b", range1, true);
      
      testEquals("a", VersionRange.ALL_VERSIONS, "b", VersionRange.ALL_VERSIONS, false);
      testEquals("a", range1, "a", VersionRange.ALL_VERSIONS, false);
      VersionRange range2 = new VersionRange("1.0.0", true, "2.0.0", true);
      testEquals("a", range1, "a", range2, false);
   }
   
   public void testSerialization() throws Exception
   {
      NameAndVersionRangeSupport test = new NameAndVersionRangeSupport("a", VersionRange.ALL_VERSIONS);
      NameAndVersionRangeSupport other = serializeDeserialize(test, NameAndVersionRangeSupport.class);
      assertEquals(test, other);
   }
   
   protected void testEquals(String name1, VersionRange range1, String name2, VersionRange range2, boolean result)
   {
      NameAndVersionRangeSupport test1 = new NameAndVersionRangeSupport(name1, range1);
      NameAndVersionRangeSupport test2 = new NameAndVersionRangeSupport(name2, range2);
      if (result)
      {
         assertTrue("Expected " + test1 + ".equals(" + test2 + ") to be true", test1.equals(test2));
         assertTrue("Expected " + test2 + ".equals(" + test1 + ") to be true", test2.equals(test1));
      }
      else
      {
         assertFalse("Expected " + test1 + ".equals(" + test2 + ") to be false", test1.equals(test2));
         assertFalse("Expected " + test2 + ".equals(" + test1 + ") to be false", test2.equals(test1));
      }
   }
}
