/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.bundle.helper;

import junit.framework.Test;

import org.jboss.osgi.plugins.facade.helpers.BundleEntryVisitor;
import org.jboss.virtual.VirtualFile;

/**
 * A BundleEntryVisitorTest.
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public class BundleEntryVisitorTestCase extends AbstractBundleEntryTestCase
{

   private VirtualFile root;

   /**
    * Create a new BundleEntryVisitorTest.
    * 
    * @param name
    */
   public BundleEntryVisitorTestCase(String name)
   {
      super(name);
   }

   /**
    * Get Suite for BundleEntryVisitorTest
    * 
    * @return
    */
   public static Test suite()
   {
      return suite(BundleEntryVisitorTestCase.class);
   }

   /**
    * Setup the test
    */
   protected void setUp() throws Exception
   {
      super.setUp();
      root = getVirtualFile("/org/jboss/test/bundle/helper", "simple.jar");
   }

   /**
    * Test finding entries without wild cards (*)
    * 
    * @throws Exception
    */
   public void testFindEntriesNoWildCard() throws Exception
   {

      BundleEntryVisitor visitor = new BundleEntryVisitor("manifest.mf", true);
      root.visit(visitor);
      assertEquals(1, visitor.getEntries().size());

      assertEntry(root, "/META-INF/manifest.mf", visitor.getEntries().get(0));

      visitor = new BundleEntryVisitor("custom.properties", true);
      root.visit(visitor);
      assertEquals(1, visitor.getEntries().size());

      assertEntry(root, "/org/jboss/osgi/test/custom.properties", visitor.getEntries().get(0));

      visitor = new BundleEntryVisitor("custom.xml", true);
      root.visit(visitor);
      assertEquals(2, visitor.getEntries().size());

      assertEntry(root, "/org/jboss/osgi/custom.xml", visitor.getEntries().get(0));
      assertEntry(root, "/org/jboss/osgi/test/custom.xml", visitor.getEntries().get(1));

      visitor = new BundleEntryVisitor("fromroot.xml", true);
      root.visit(visitor);
      assertEquals(1, visitor.getEntries().size());
      assertEntry(root, "/fromroot.xml", visitor.getEntries().get(0));

      visitor = new BundleEntryVisitor("bogus.txt", false);
      root.visit(visitor);
      assertEquals(0, visitor.getEntries().size());

   }

   /**
    * Test finding entries without wild cards (*) or recursion 
    * 
    * @throws Exception
    */
   public void testFindEntriesNoWildCardOrRecurse() throws Exception
   {

      BundleEntryVisitor visitor = new BundleEntryVisitor("manifest.mf", false);
      root.visit(visitor);
      assertEquals(0, visitor.getEntries().size());

      visitor = new BundleEntryVisitor("custom.properties", false);
      root.visit(visitor);
      assertEquals(0, visitor.getEntries().size());

      visitor = new BundleEntryVisitor("fromroot.xml", false);
      root.visit(visitor);
      assertEquals(1, visitor.getEntries().size());
      assertEntry(root, "/fromroot.xml", visitor.getEntries().get(0));
   }

   /**
    * Test finding entries with wild cards (*)  
    * 
    * @throws Exception
    */
   public void testFindEntriesWildCard() throws Exception
   {

      // starts with wildcard
      BundleEntryVisitor visitor = new BundleEntryVisitor("*.mf", true);
      root.visit(visitor);
      assertEquals(1, visitor.getEntries().size());
      assertEntry(root, "/META-INF/manifest.mf", visitor.getEntries().get(0));

      visitor = new BundleEntryVisitor("*.properties", true);
      root.visit(visitor);
      assertEquals(1, visitor.getEntries().size());
      assertEntry(root, "/org/jboss/osgi/test/custom.properties", visitor.getEntries().get(0));

      // starts with wildcard and matches multiple
      visitor = new BundleEntryVisitor("*.xml", true);
      root.visit(visitor);
      assertEquals(4, visitor.getEntries().size());
      assertEntry(root, "/fromroot.xml", visitor.getEntries().get(0));
      assertEntry(root, "/META-INF/jboss-service.xml", visitor.getEntries().get(1));
      assertEntry(root, "/org/jboss/osgi/custom.xml", visitor.getEntries().get(2));
      assertEntry(root, "/org/jboss/osgi/test/custom.xml", visitor.getEntries().get(3));

      // ends in wildcard and matches multiple
      visitor = new BundleEntryVisitor("custom*", true);
      root.visit(visitor);
      assertEquals(3, visitor.getEntries().size());
      assertEntry(root, "/org/jboss/osgi/custom.xml", visitor.getEntries().get(0));
      assertEntry(root, "/org/jboss/osgi/test/custom.properties", visitor.getEntries().get(1));
      assertEntry(root, "/org/jboss/osgi/test/custom.xml", visitor.getEntries().get(2));

      // begins and ends with wildcard
      visitor = new BundleEntryVisitor("*xml*", true);
      root.visit(visitor);
      assertEquals(5, visitor.getEntries().size());
      assertEntry(root, "/fromroot.xml", visitor.getEntries().get(0));
      assertEntry(root, "/META-INF/jboss-service.xml", visitor.getEntries().get(1));
      assertEntry(root, "/org/jboss/osgi/custom.xml", visitor.getEntries().get(2));
      assertEntry(root, "/org/jboss/osgi/notanxmlfile.txt", visitor.getEntries().get(3));
      assertEntry(root, "/org/jboss/osgi/test/custom.xml", visitor.getEntries().get(4));

      // wildcard within
      visitor = new BundleEntryVisitor("jboss*.xml", true);
      root.visit(visitor);
      assertEquals(1, visitor.getEntries().size());
      assertEntry(root, "/META-INF/jboss-service.xml", visitor.getEntries().get(0));

      // several wildcards
      visitor = new BundleEntryVisitor("*t*xml*", true);
      root.visit(visitor);
      assertEquals(4, visitor.getEntries().size());
      assertEntry(root, "/fromroot.xml", visitor.getEntries().get(0));
      assertEntry(root, "/org/jboss/osgi/custom.xml", visitor.getEntries().get(1));
      assertEntry(root, "/org/jboss/osgi/notanxmlfile.txt", visitor.getEntries().get(2));
      assertEntry(root, "/org/jboss/osgi/test/custom.xml", visitor.getEntries().get(3));
   }

   /**
    * Test finding entries with wild cards (*) and no recursion 
    * 
    * @throws Exception
    */
   public void testFindEntriesWildCardNoRecurse() throws Exception
   {

      BundleEntryVisitor visitor = new BundleEntryVisitor("*.mf", false);
      root.visit(visitor);
      assertEquals(0, visitor.getEntries().size());

      visitor = new BundleEntryVisitor("custom.*", false);
      root.visit(visitor);
      assertEquals(0, visitor.getEntries().size());

      visitor = new BundleEntryVisitor("*.xml", false);
      root.visit(visitor);
      assertEquals(1, visitor.getEntries().size());
      assertEntry(root, "/fromroot.xml", visitor.getEntries().get(0));

      visitor = new BundleEntryVisitor("*xml*", false);
      root.visit(visitor);
      assertEquals(1, visitor.getEntries().size());
      assertEntry(root, "/fromroot.xml", visitor.getEntries().get(0));
   }
}
