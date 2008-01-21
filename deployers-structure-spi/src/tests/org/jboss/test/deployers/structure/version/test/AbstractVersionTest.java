/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
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
package org.jboss.test.deployers.structure.version.test;

import org.jboss.deployers.structure.spi.classloading.Version;
import org.jboss.deployers.structure.spi.classloading.VersionComparatorRegistry;
import org.jboss.deployers.structure.spi.classloading.helpers.VersionImpl;
import org.jboss.test.BaseTestCase;
import org.jboss.test.deployers.structure.version.support.DVIVersionComparator;
import org.jboss.test.deployers.structure.version.support.DummyVersion;
import org.jboss.test.deployers.structure.version.support.DummyVersionComparator;

/**
 * Abstract Version test.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractVersionTest extends BaseTestCase
{
   protected AbstractVersionTest(String name)
   {
      super(name);
   }

   protected void registerVersionComparators() throws Exception
   {
      VersionComparatorRegistry registry = VersionComparatorRegistry.getInstance();

      DummyVersionComparator dummyVersionComparator = new DummyVersionComparator();
      DVIVersionComparator dviVersionComparator = new DVIVersionComparator();

      registry.registerVersionComparator(DummyVersion.class, dummyVersionComparator);
      registry.registerVersionComparator(DummyVersion.class, VersionImpl.class, dviVersionComparator);
   }

   protected void clearVersionComparators() throws Exception
   {
      VersionComparatorRegistry registry = VersionComparatorRegistry.getInstance();

      registry.removeVersionComparator(DummyVersion.class);
      assertNull(registry.getComparator(DummyVersion.class, DummyVersion.class));
      registry.removeVersionComparator(DummyVersion.class, VersionImpl.class);
      assertNull(registry.getComparator(DummyVersion.class, VersionImpl.class));
   }

   protected void assertFailVersion(int major, int minor, int micro, String qualifier)
   {
      try
      {
         VersionImpl version = new VersionImpl(major, minor, micro, qualifier);
         fail("Should not be here: " + version);
      }
      catch(Exception e)
      {
         assertInstanceOf(e, IllegalArgumentException.class);
      }
   }

   protected void assertFailVersion(Version v1, Version v2)
   {
      try
      {
         assertNotNull(v1);
         assertNotNull(v2);
         v1.compareTo(v2);         
         fail("Should not be here.");
      }
      catch(Exception e)
      {
         assertInstanceOf(e, IllegalArgumentException.class);
      }
   }

   protected void assertGreater(Version v1, Version v2)
   {
      assertTrue(v1.compareTo(v2) > 0);
   }

   protected void assertLess(Version v1, Version v2)
   {
      assertTrue(v1.compareTo(v2) < 0);
   }

   protected void assertEquals(Version v1, Version v2)
   {
      assertTrue(v1.compareTo(v2) == 0);
   }
}
