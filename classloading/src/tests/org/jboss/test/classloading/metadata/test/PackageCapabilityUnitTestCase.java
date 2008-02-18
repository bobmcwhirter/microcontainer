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

import org.jboss.classloading.plugins.metadata.PackageCapability;
import org.jboss.classloading.spi.version.Version;
import org.jboss.test.classloading.AbstractClassLoadingTestWithSecurity;

/**
 * PackageCapabilityUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class PackageCapabilityUnitTestCase extends AbstractClassLoadingTestWithSecurity
{
   public static Test suite()
   {
      return suite(PackageCapabilityUnitTestCase.class);
   }

   public PackageCapabilityUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testConstructors() throws Exception
   {
      PackageCapability test = new PackageCapability();
      assertNotNull(test.getName());
      assertEquals(Version.DEFAULT_VERSION, test.getVersion());

      test = new PackageCapability("test");
      assertEquals("test", test.getName());
      assertEquals(Version.DEFAULT_VERSION, test.getVersion());

      test = new PackageCapability("test", "version");
      assertEquals("test", test.getName());
      assertEquals("version", test.getVersion());
      
      try
      {
         fail("Should not be here for: " + new PackageCapability(null));
      }
      catch (Throwable t)
      {
         checkDeepThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         fail("Should not be here for: " + new PackageCapability(null, "version"));
      }
      catch (Throwable t)
      {
         checkDeepThrowable(IllegalArgumentException.class, t);
      }
   }
   
   public void testSetName() throws Exception
   {
      PackageCapability test = new PackageCapability();
      assertNotNull(test.getName());
      assertEquals(Version.DEFAULT_VERSION, test.getVersion());
      test.setName("name");
      assertEquals("name", test.getName());
      assertEquals(Version.DEFAULT_VERSION, test.getVersion());
      
      test = new PackageCapability();
      try
      {
         test.setName(null);
      }
      catch (Throwable t)
      {
         checkDeepThrowable(IllegalArgumentException.class, t);
      }
   }
   
   public void testSetVersion() throws Exception
   {
      PackageCapability test = new PackageCapability();
      assertNotNull(test.getName());
      assertEquals(Version.DEFAULT_VERSION, test.getVersion());
      test.setVersion("version");
      assertNotNull(test.getName());
      assertEquals("version", test.getVersion());
      test.setVersion(null);
      assertNotNull(test.getName());
      assertEquals(Version.DEFAULT_VERSION, test.getVersion());
   }
      
   public void testEquals() throws Exception
   {
      testEquals("a", Version.DEFAULT_VERSION, "a", Version.DEFAULT_VERSION, true);
      testEquals("a", Version.DEFAULT_VERSION, "a", null, true);
      testEquals("a", Version.DEFAULT_VERSION, "a", "0.0.0", true);
      testEquals("b", "1.0.0", "b", "1.0.0", true);
      
      testEquals("a", Version.DEFAULT_VERSION, "b", Version.DEFAULT_VERSION, false);
      testEquals("a", "1.0.0", "a", Version.DEFAULT_VERSION, false);
      testEquals("a", "1.0.0", "a", "0.0.0", false);
      testEquals("a", "1.0.0", "a", "2.0.0", false);
   }
   
   public void testSerialization() throws Exception
   {
      PackageCapability test = new PackageCapability("a", Version.DEFAULT_VERSION);
      PackageCapability other = serializeDeserialize(test, PackageCapability.class);
      assertEquals(test, other);
   }
   
   protected void testEquals(String name1, Object version1, String name2, Object version2, boolean result)
   {
      PackageCapability test1 = new PackageCapability(name1, version1);
      PackageCapability test2 = new PackageCapability(name2, version2);
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
