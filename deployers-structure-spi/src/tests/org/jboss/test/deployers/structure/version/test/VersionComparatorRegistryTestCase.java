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
import org.jboss.deployers.structure.spi.classloading.VersionComparatorRegistry;
import org.jboss.deployers.structure.spi.classloading.helpers.VersionImpl;
import org.jboss.deployers.structure.spi.classloading.helpers.VersionImplComparator;
import org.jboss.test.deployers.structure.version.support.DVIVersionComparator;
import org.jboss.test.deployers.structure.version.support.DummyVersion;
import org.jboss.test.deployers.structure.version.support.DummyVersionComparator;
import org.jboss.test.deployers.structure.version.support.ZeroVersion;

/**
 * Version comparator registry tests.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class VersionComparatorRegistryTestCase extends AbstractVersionTest
{
   public VersionComparatorRegistryTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(VersionComparatorRegistryTestCase.class);
   }

   public void testRegistry() throws Exception
   {
      VersionComparatorRegistry registry = VersionComparatorRegistry.getInstance();

      DummyVersionComparator dummyVersionComparator = new DummyVersionComparator();
      DVIVersionComparator dviVersionComparator = new DVIVersionComparator();
      try
      {
         registry.registerVersionComparator(DummyVersion.class, dummyVersionComparator);
         registry.registerVersionComparator(DummyVersion.class, VersionImpl.class, dviVersionComparator);

         assertSame(dummyVersionComparator, registry.getComparator(DummyVersion.class, DummyVersion.class));
         assertSame(dviVersionComparator, registry.getComparator(DummyVersion.class, VersionImpl.class));
         // expecting pre-registered
         assertInstanceOf(registry.getComparator(VersionImpl.class, VersionImpl.class), VersionImplComparator.class);
         // expecting switch
         assertNotNull(registry.getComparator(VersionImpl.class, DummyVersion.class));
         // expecting nulls
         assertNull(registry.getComparator(ZeroVersion.class, ZeroVersion.class));
         assertNull(registry.getComparator(ZeroVersion.class, DummyVersion.class));
         assertNull(registry.getComparator(ZeroVersion.class, VersionImpl.class));
         assertNull(registry.getComparator(DummyVersion.class, ZeroVersion.class));
         assertNull(registry.getComparator(VersionImpl.class, ZeroVersion.class));
      }
      finally
      {
         clearVersionComparators();
      }
   }
}
