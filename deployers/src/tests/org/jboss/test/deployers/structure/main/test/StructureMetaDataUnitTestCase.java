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
package org.jboss.test.deployers.structure.main.test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.plugins.structure.ContextInfoImpl;
import org.jboss.deployers.plugins.structure.StructureMetaDataImpl;
import org.jboss.deployers.spi.structure.vfs.ContextInfo;
import org.jboss.deployers.spi.structure.vfs.StructureMetaData;
import org.jboss.test.BaseTestCase;

/**
 * Tests of the default StructureMetaData implementation
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class StructureMetaDataUnitTestCase extends BaseTestCase
{
   public static Test suite()
   {
      return new TestSuite(StructureMetaDataUnitTestCase.class);
   }
   
   public StructureMetaDataUnitTestCase(String name)
   {
      super(name);
   }

   @Override
   protected void setUp() throws Exception
   {
      super.setUp();
      enableTrace("org.jboss.deployers");
   }

   protected StructureMetaData getStructureMetaData()
   {
      StructureMetaData metaData = new StructureMetaDataImpl();
      return metaData;
   }
   protected ContextInfo getContextInfo(String vfsPath)
   {
      ContextInfo context = new ContextInfoImpl(vfsPath);
      return context;
   }

   public void testStructureMetaDataParentChild()
   {
      StructureMetaData metaData = getStructureMetaData();
      ContextInfo context = getContextInfo("/top/root");
      metaData.addContext(context);
      context = getContextInfo("/top/root/sub1");
      metaData.addContext(context);
      context = getContextInfo("/top/root/sub2");
      metaData.addContext(context);
      context = getContextInfo("/top/root/sub2/sub21");
      metaData.addContext(context);
      context = getContextInfo("/top/root/sub2/sub21.war");
      metaData.addContext(context);
      context = getContextInfo("/top/root/sub2/sub21.jar");
      metaData.addContext(context);
      context = getContextInfo("/top/root/subdir/sub3.jar");
      metaData.addContext(context);

      // Validate the parent/child structure
      ContextInfo root = metaData.getContext("/top/root");
      assertNotNull("/top/root", root);
      ContextInfo sub1 = metaData.getContext("/top/root/sub1");
      assertNotNull("/top/root/sub1", sub1);
      ContextInfo sub2 = metaData.getContext("/top/root/sub2");
      assertNotNull("/top/root/sub2", sub2);
      assertSame(root, sub2.getParent());
      ContextInfo sub3 = metaData.getContext("/top/root/subdir/sub3.jar");
      assertNotNull("/top/root/subdir/sub3.jar", sub3);
      assertSame(root, sub3.getParent());

      ContextInfo sub21 = metaData.getContext("/top/root/sub2/sub21");
      assertNotNull("/top/root/sub2/sub21", sub21);
      assertSame(sub2, sub21.getParent());
      ContextInfo sub21War = metaData.getContext("/top/root/sub2/sub21.war");
      assertNotNull("/top/root/sub2/sub21", sub21War);
      assertSame(sub2, sub21War.getParent());
      ContextInfo sub21Jar = metaData.getContext("/top/root/sub2/sub21.jar");
      assertNotNull("/top/root/sub2/sub21", sub21Jar);
      assertSame(sub2, sub21Jar.getParent());

   }

   public void testStructureMetaDataParentChildRelative()
   {
      StructureMetaData metaData = new StructureMetaDataImpl();
      ContextInfo context = getContextInfo("top/root");
      metaData.addContext(context);
      context = getContextInfo("top/root/sub1");
      metaData.addContext(context);
      context = getContextInfo("top/root/sub2");
      metaData.addContext(context);
      context = getContextInfo("top/root/sub2/sub21");
      metaData.addContext(context);
      context = getContextInfo("top/root/sub2/sub21.war");
      metaData.addContext(context);
      context = getContextInfo("top/root/sub2/sub21.jar");
      metaData.addContext(context);
      context = getContextInfo("top/root/subdir/sub3.jar");
      metaData.addContext(context);

      // Validate the parent/child structure
      ContextInfo root = metaData.getContext("top/root");
      assertNotNull("top/root", root);
      ContextInfo sub1 = metaData.getContext("top/root/sub1");
      assertNotNull("top/root/sub1", sub1);
      ContextInfo sub2 = metaData.getContext("top/root/sub2");
      assertNotNull("top/root/sub2", sub2);
      assertSame(root, sub2.getParent());
      ContextInfo sub3 = metaData.getContext("top/root/subdir/sub3.jar");
      assertNotNull("top/root/subdir/sub3.jar", sub3);
      assertSame(root, sub3.getParent());

      ContextInfo sub21 = metaData.getContext("top/root/sub2/sub21");
      assertNotNull("top/root/sub2/sub21", sub21);
      assertSame(sub2, sub21.getParent());
      ContextInfo sub21War = metaData.getContext("top/root/sub2/sub21.war");
      assertNotNull("top/root/sub2/sub21", sub21War);
      assertSame(sub2, sub21War.getParent());
      ContextInfo sub21Jar = metaData.getContext("top/root/sub2/sub21.jar");
      assertNotNull("top/root/sub2/sub21", sub21Jar);
      assertSame(sub2, sub21Jar.getParent());
   }
   /**
    * JBMICROCONT-161 test for missing websubdir/relative.jar context info.
    * @throws Exception
    */
   public void testStructureMetaDataParentChildRelativeMC161()
      throws Exception
   {
      StructureMetaData metaData = new StructureMetaDataImpl();
      ContextInfo c0 = getContextInfo("");
      metaData.addContext(c0);
      ContextInfo c1 = getContextInfo("wars/notjbosstest-web.war");
      metaData.addContext(c1);
      ContextInfo c2 = getContextInfo("cts.jar");
      metaData.addContext(c2);
      ContextInfo c3 = getContextInfo("jbosstest-web-ejbs.jar");
      metaData.addContext(c3);
      ContextInfo c4 = getContextInfo("jbosstest-web.war");
      metaData.addContext(c4);
      ContextInfo c5 = getContextInfo("root-web.war");
      metaData.addContext(c5);
      ContextInfo c6 = getContextInfo("jbosstest-web-ejbs.jar/roles.properties");
      metaData.addContext(c6);
      ContextInfo c7 = getContextInfo("jbosstest-web-ejbs.jar/users.properties");
      metaData.addContext(c7);
      ContextInfo c8 = getContextInfo("websubdir/relative.jar");

      StructureMetaDataImpl.ContextComparator compare = new StructureMetaDataImpl.ContextComparator();
      assertFalse(compare.compare(c0, c8) == 0);
      assertFalse(compare.compare(c1, c8) == 0);
      assertFalse(compare.compare(c2, c8) == 0);
      assertFalse(compare.compare(c3, c8) == 0);
      assertFalse(compare.compare(c4, c8) == 0);
      assertFalse(compare.compare(c5, c8) == 0);
      assertFalse(compare.compare(c6, c8) == 0);
      assertFalse(compare.compare(c7, c8) == 0);
      metaData.addContext(c8);

      assertEquals("context count is 9", 9, metaData.getContexts().size());
      assertNotNull("wars/notjbosstest-web.war", metaData.getContext("wars/notjbosstest-web.war"));
      assertNotNull("cts.jar", metaData.getContext("cts.jar"));
      assertNotNull("jbosstest-web-ejbs.jar", metaData.getContext("jbosstest-web-ejbs.jar"));
      assertNotNull("jbosstest-web.war", metaData.getContext("jbosstest-web.war"));
      assertNotNull("root-web.war", metaData.getContext("root-web.war"));
      assertNotNull("jbosstest-web-ejbs.jar/roles.properties", metaData.getContext("jbosstest-web-ejbs.jar/roles.properties"));
      assertNotNull("jbosstest-web-ejbs.jar/users.properties", metaData.getContext("jbosstest-web-ejbs.jar/users.properties"));
      assertNotNull("websubdir/relative.jar", metaData.getContext("websubdir/relative.jar"));
   }

   public void testSortedSet()
   {
      StructureMetaDataImpl.ContextComparator c = new StructureMetaDataImpl.ContextComparator();
      TreeSet<ContextInfo> info = new TreeSet<ContextInfo>(c);

      ArrayList<ContextInfo> order = new ArrayList<ContextInfo>();
      StructureMetaData metaData = new StructureMetaDataImpl();
      ContextInfo context = getContextInfo("top/root");
      metaData.addContext(context);
      info.add(context);
      order.add(context);
      context = getContextInfo("top/root/sub1");
      metaData.addContext(context);
      info.add(context);
      order.add(context);
      context = getContextInfo("top/root/sub2");
      metaData.addContext(context);
      info.add(context);
      order.add(context);
      context = getContextInfo("top/root/subdir");
      metaData.addContext(context);
      info.add(context);
      order.add(context);

      // sub2 < subd
      context = getContextInfo("top/root/sub2/sub21");
      metaData.addContext(context);
      info.add(context);
      order.add(context);
      context = getContextInfo("top/root/sub2/sub21.jar");
      metaData.addContext(context);
      info.add(context);
      order.add(context);
      context = getContextInfo("top/root/sub2/sub21.war");
      metaData.addContext(context);
      info.add(context);
      order.add(context);

      context = getContextInfo("top/root/subdir/sub2.jar");
      metaData.addContext(context);
      info.add(context);
      order.add(context);
      context = getContextInfo("top/root/subdir/sub3.jar");
      metaData.addContext(context);
      info.add(context);
      order.add(context);

      int count = 0;
      for(ContextInfo ci : info)
      {
         ContextInfo expected = order.get(count);
         assertSame("At: "+count, expected, ci);
         count ++;
      }
   }

   public void testGetContextsOrder()
   {
      StructureMetaData metaData = new StructureMetaDataImpl();
      ContextInfo context = getContextInfo("top/root");
      ArrayList<ContextInfo> order = new ArrayList<ContextInfo>();
      metaData.addContext(context);
      order.add(context);

      // top/root children
      context = getContextInfo("top/root/sub1");
      metaData.addContext(context);
      order.add(context);
      context = getContextInfo("top/root/sub2");
      metaData.addContext(context);
      order.add(context);

      // top/root/sub2 children
      context = getContextInfo("top/root/sub2/sub21");
      metaData.addContext(context);
      order.add(context);
      context = getContextInfo("top/root/sub2/sub21.jar");
      metaData.addContext(context);
      order.add(context);
      context = getContextInfo("top/root/sub2/sub21.war");
      metaData.addContext(context);
      order.add(context);

      // top/root/subdir children
      context = getContextInfo("top/root/subdir/sub3.jar");
      metaData.addContext(context);
      order.add(context);

      int count = 0;
      for(ContextInfo ci : metaData.getContexts())
      {
         ContextInfo expected = order.get(count);
         assertSame("At: "+count, expected, ci);
         count ++;
      }      
   }
}
