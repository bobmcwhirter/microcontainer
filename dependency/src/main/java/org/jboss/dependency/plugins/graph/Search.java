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

import java.util.Map;

import org.jboss.dependency.spi.graph.LookupStrategy;
import org.jboss.dependency.spi.graph.SearchInfo;
import org.jboss.xb.annotations.JBossXmlEnum;

/**
 * Search enum.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@JBossXmlEnum(ignoreCase = true)
public enum Search implements SearchInfo
{
   DEFAULT(new DefaultSearchInfoWrapper()),
   LOCAL(new LocalLookupStrategy()),
   TOP(new TopLevelLookupStrategy()),
   PARENT_ONLY(new ParentOnlyLookupStrategy()),
   PARENT(new ParentLookupStrategy()),
   DEPTH(new DepthLookupStrategy()),
   LEAVES(new LeavesFirstLookupStrategy()),
   WIDTH(new WidthLookupStrategy()),
   CHILD_ONLY_DEPTH(new ChildrenOnlyDepthLookupStrategy()),
   CHILD_ONLY_LEAVES(new ChildrenOnlyLeavesFirstLookupStrategy()),
   CHILD_ONLY_WIDTH(new ChildrenOnlyWidthLookupStrategy());

   private SearchInfo delegate;

   Search(SearchInfo delegate)
   {
      this.delegate = delegate;
   }

   public String getType()
   {
      return delegate.getType();
   }

   public Map<String, ?> getInfo()
   {
      return delegate.getInfo();
   }

   public LookupStrategy getStrategy()
   {
      return delegate.getStrategy();
   }

   private static class DefaultSearchInfoWrapper implements SearchInfo
   {
      private DefaultLookupStrategy strategy = new DefaultLookupStrategy();

      public String getType()
      {
         return "<DEFAULT>";
      }

      public Map<String, ?> getInfo()
      {
         return null;
      }

      public LookupStrategy getStrategy()
      {
         return strategy;
      }
   }
}
