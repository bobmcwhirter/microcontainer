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
 * @version $Revision: 1.1 $
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

   public void testSortedSet()
   {
      Comparator<ContextInfo> c = new Comparator<ContextInfo>()
      {
         public int compare(ContextInfo o1, ContextInfo o2)
         {
            int compare = 0;
            if( o1 == null && o2 != null )
               compare = -1;
            else if( o1 != null && o2 == null )
               compare = 1;
            else
            {
               // Sort by depth and then name
               ContextInfo p1 = o1.getParent();
               ContextInfo p2 = o2.getParent();
               if( p1 != p2 )
               {
                  compare = compare(p1, p2);
               }
               else if( p1 != null )
               {
                  compare = o1.getVfsPath().compareTo(o2.getVfsPath());
               }
            }
            return compare;
         }
      };
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
      context = getContextInfo("top/root/subdir/sub3.jar");
      metaData.addContext(context);
      info.add(context);
      order.add(context);

      context = getContextInfo("top/root/sub2/sub21");
      metaData.addContext(context);
      info.add(context);
      order.add(context);
      context = getContextInfo("top/root/sub2/sub21.jar");
      metaData.addContext(context);
      info.add(context);
      order.add(context);
      metaData.addContext(context);
      context = getContextInfo("top/root/sub2/sub21.war");
      metaData.addContext(context);
      info.add(context);
      order.add(context);

      int count = 0;
      for(ContextInfo ci : info)
      {
         ContextInfo expected = order.get(count);
         assertSame("At: "+count, ci, expected);
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
      context = getContextInfo("top/root/subdir/sub3.jar");
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

      int count = 0;
      for(ContextInfo ci : metaData.getContexts())
      {
         ContextInfo expected = order.get(count);
         assertSame("At: "+count, ci, expected);
         count ++;
      }      
   }
}
