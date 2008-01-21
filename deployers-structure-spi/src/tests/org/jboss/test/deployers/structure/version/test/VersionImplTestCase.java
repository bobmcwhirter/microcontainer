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

/**
 * Version impl tests.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class VersionImplTestCase extends AbstractVersionTest
{
   public VersionImplTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(VersionImplTestCase.class);
   }

   protected void assertVersion(VersionImpl version)
   {
      assertEquals(1, version.getMajor());
      assertEquals(2, version.getMinor());
      assertEquals(3, version.getMicro());
      assertEquals("CR1", version.getQualifier());
      assertEquals(new VersionImpl(1, 2, 3, "CR1"), version);
   }

   public void testSimpleVersion() throws Exception
   {
      assertVersion(new VersionImpl(1, 2, 3, "CR1"));
      assertVersion(VersionImpl.parseVersion("1.2.3.CR1"));
      assertEquals(Version.DEFAULT_VERSION, VersionImpl.parseVersion(null));
      assertEquals(new VersionImpl(1, 2, 3), VersionImpl.parseVersion("1.2.3"));
   }

   public void testIllegalVersion() throws Exception
   {
      assertFailVersion(-1, 2, 3, "Beta10");
      assertFailVersion(1, -2, 3, "CR1");
      assertFailVersion(1, 2, -3, "CR2");
      assertFailVersion(1, 2, 3, "GA@");
   }

   public void testCompareVersion() throws Exception
   {
      VersionImpl v1 = new VersionImpl(1, 2, 3);
      VersionImpl v2 = new VersionImpl(1, 2, 4);
      assertLess(v1, v2);
      assertGreater(v2, v1);
      assertEquals(v1, VersionImpl.parseVersion("1.2.3"));

      VersionImpl v3 = new VersionImpl(1, 2, 3, "CR1");
      VersionImpl v4 = new VersionImpl(1, 2, 3, "CR2");
      assertLess(v3, v4);
      assertGreater(v4, v3);
      assertEquals(v3, VersionImpl.parseVersion("1.2.3.CR1"));

      VersionImpl v5 = new VersionImpl(1, 2, 3);
      VersionImpl v6 = new VersionImpl(1, 3, 0);
      assertLess(v5, v6);
      assertGreater(v6, v5);

      VersionImpl v7 = new VersionImpl(1, 2, 3);
      VersionImpl v8 = new VersionImpl(2, 0, 0);
      assertLess(v7, v8);
      assertGreater(v8, v7);
   }
}
