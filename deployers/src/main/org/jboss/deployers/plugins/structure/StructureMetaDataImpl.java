/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.deployers.plugins.structure;

import java.util.Comparator;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jboss.deployers.spi.structure.vfs.ContextInfo;
import org.jboss.deployers.spi.structure.vfs.StructureMetaData;

/**
 * Metadata describing the structure of a deployment.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class StructureMetaDataImpl implements StructureMetaData
{
   private HashMap<String, ContextInfo> contextMap =
      new HashMap<String, ContextInfo>();
   private TreeSet<ContextInfo> contextSet = new  TreeSet<ContextInfo>(new ContextComparator());

   public void addContext(ContextInfo context)
   {
      String key = context.getVfsPath();
      contextMap.put(key, context);
      ContextInfo parent = context.getParent();
      // If the parent has not been set try to find it
      if( parent == null )
      {
         String[] keys = key.split("/");
         StringBuilder parentKey = new StringBuilder();
         for(int n = 0; n < keys.length-1; n ++)
         {
            key = keys[n];
            parentKey.append(key);
            parent = contextMap.get(parentKey.toString());
            if( parent != null )
               context.setParent(parent);
            parentKey.append('/');
         }
         // Handle a VFS rooted at ""
         if( keys.length == 1 )
         {
            // Look for "" as the parent
            parent = contextMap.get("");
            if( parent != null )
               context.setParent(parent);
         }
      }
      contextSet.add(context);
   }

   public ContextInfo getContext(String vfsPath)
   {
      return contextMap.get(vfsPath);
   }

   public ContextInfo removeContext(String vfsPath)
   {
      ContextInfo info = contextMap.remove(vfsPath);
      if( info != null )
         contextSet.remove(info);
      return info;
   }

   public SortedSet<ContextInfo> getContexts()
   {
      return contextSet;
   }

   public String toString()
   {
      StringBuilder tmp = new StringBuilder(super.toString());
      tmp.append("[ContextInfo:");
      tmp.append(contextSet.toString());
      tmp.append(']');
      return tmp.toString();
   }

   private class ContextComparator implements Comparator<ContextInfo>
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
   }
}
