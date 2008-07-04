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
package org.jboss.dependency.plugins.graph;

import org.jboss.dependency.spi.graph.LookupStrategy;
import org.jboss.dependency.spi.graph.SearchInfo;

/**
 * Search enum.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public enum Search implements SearchInfo
{
   DEFAULT(new DefaultSearchInfoWrapper()),
   LOCAL(new LocalLookupStrategy()),
   TOP(new TopLevelLookupStrategy()),
   PARENT(new ParentOnlyLookupStrategy()),
   DEPTH(new DepthLookupStrategy()),
   LEAVES(new LeavesFirstLookupStrategy()),
   WIDTH(new WidthLookupStrategy()),
   CHILD_ONLY_DEPTH(new ChildrenOnlyDepthLookupStrategy()),
   CHILD_ONLY_LEAVES(new ChildrenOnlyLeavesFirstLookupStrategy()),
   CHILD_ONLY_WIDTH(new ChildrenOnlyWidthLookupStrategy());

   private SearchInfo info;

   Search(SearchInfo info)
   {
      this.info = info;
   }

   public String type()
   {
      return info.type();
   }

   public LookupStrategy getStrategy()
   {
      return info.getStrategy();
   }

   private static class DefaultSearchInfoWrapper implements SearchInfo
   {
      private DefaultLookupStrategy strategy = new DefaultLookupStrategy();

      public String type()
      {
         return "<DEFAULT>";
      }

      public LookupStrategy getStrategy()
      {
         return strategy;
      }
   }
}
