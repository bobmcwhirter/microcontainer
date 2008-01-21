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

import junit.framework.Test;
import org.jboss.deployers.structure.spi.classloading.Version;
import org.jboss.deployers.structure.spi.classloading.helpers.VersionImpl;
import org.jboss.test.deployers.structure.version.support.DummyVersion;
import org.jboss.test.deployers.structure.version.support.ZeroVersion;

/**
 * Version comparator tests.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class VersionComparatorTestCase extends AbstractVersionTest
{
   public VersionComparatorTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(VersionComparatorTestCase.class);
   }

   public void testSameImpl() throws Exception
   {
      registerVersionComparators();
      try
      {
         Version vi1 = VersionImpl.parseVersion("1.2.3");
         Version vi2 = VersionImpl.parseVersion("2.0.0.GA");
         assertLess(vi1, vi2);
         assertGreater(vi2, vi1);
         assertEquals(vi2, new VersionImpl(2, 0, 0, "GA"));

         DummyVersion dv1 = new DummyVersion(1);
         DummyVersion dv2 = new DummyVersion(2);
         DummyVersion dv3 = new DummyVersion(2);
         assertLess(dv1, dv2);
         assertGreater(dv2, dv1);
         assertEquals(dv2, dv3);
      }
      finally
      {
         clearVersionComparators();
      }
   }

   public void testDifferentImpl() throws Exception
   {
      registerVersionComparators();
      try
      {
         Version vi1 = VersionImpl.parseVersion("1.2.3");
         Version vi2 = VersionImpl.parseVersion("2.0.0.GA");
         DummyVersion dv1 = new DummyVersion(1);
         DummyVersion dv2 = new DummyVersion(2);

         assertLess(dv1, vi2);
         assertLess(vi1, dv2);
         assertGreater(vi2, dv1);
         assertGreater(dv2, vi1);
         assertEquals(vi2, dv2);
         assertEquals(dv2, vi2);
      }
      finally
      {
         clearVersionComparators();
      }
   }

   public void testFailure() throws Exception
   {
      ZeroVersion z1 = new ZeroVersion();
      assertFailVersion(z1, Version.DEFAULT_VERSION);
      assertFailVersion(z1, new DummyVersion(0));
      assertFailVersion(Version.DEFAULT_VERSION, z1);
      assertFailVersion(new DummyVersion(0), z1);
   }
}
