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
import org.jboss.deployers.structure.spi.classloading.VersionRange;
import org.jboss.deployers.structure.spi.classloading.helpers.VersionImpl;
import org.jboss.test.deployers.structure.version.support.DummyVersion;

/**
 * Test version range.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class VersionRangeTestCase extends AbstractVersionTest
{
   public VersionRangeTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(VersionRangeTestCase.class);
   }

   public void testRange() throws Exception
   {
      registerVersionComparators();
      try
      {
         Version low1 = VersionImpl.parseVersion("1");
         Version high1 = VersionImpl.parseVersion("2");
         Version v1 = VersionImpl.parseVersion("1");
         Version v2 = VersionImpl.parseVersion("1.5");
         Version v3 = VersionImpl.parseVersion("2");

         VersionRange vr1 = new VersionRange(low1, true, high1, true); // [1,2]
         VersionRange vr2 = new VersionRange(low1, false, high1, true); // (1,2]
         VersionRange vr3 = new VersionRange(low1, true, high1, false); // [1,2)
         VersionRange vr4 = new VersionRange(low1, false, high1, false); // (1,2)

         assertTrue(vr1.isInRange(v1));
         assertTrue(vr1.isInRange(v2));
         assertTrue(vr1.isInRange(v3));

         assertFalse(vr2.isInRange(v1));
         assertTrue(vr2.isInRange(v2));
         assertTrue(vr2.isInRange(v3));

         assertTrue(vr3.isInRange(v1));
         assertTrue(vr3.isInRange(v2));
         assertFalse(vr3.isInRange(v3));

         assertFalse(vr4.isInRange(v1));
         assertTrue(vr4.isInRange(v2));
         assertFalse(vr4.isInRange(v3));

         v1 = new DummyVersion(1);
         v3 = new DummyVersion(2);

         assertTrue(vr1.isInRange(v1));
         assertTrue(vr1.isInRange(v2));
         assertTrue(vr1.isInRange(v3));

         assertFalse(vr2.isInRange(v1));
         assertTrue(vr2.isInRange(v2));
         assertTrue(vr2.isInRange(v3));

         assertTrue(vr3.isInRange(v1));
         assertTrue(vr3.isInRange(v2));
         assertFalse(vr3.isInRange(v3));

         assertFalse(vr4.isInRange(v1));
         assertTrue(vr4.isInRange(v2));
         assertFalse(vr4.isInRange(v3));
      }
      finally
      {
         clearVersionComparators();
      }
   }
}
