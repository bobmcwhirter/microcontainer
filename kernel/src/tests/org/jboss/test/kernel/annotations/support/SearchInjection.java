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
package org.jboss.test.kernel.annotations.support;

import org.jboss.beans.metadata.api.annotations.Search;
import org.jboss.metadata.plugins.scope.ApplicationScope;
import org.jboss.metadata.plugins.scope.DeploymentScope;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@ApplicationScope("main")
@DeploymentScope("core")
public class SearchInjection
{
   private ScopeTester top;
   private ScopeTester parent;
   private ScopeTester local;
   private ScopeTester normal;
   private ScopeTester withChildren;
   private ScopeTester childrenOnly;
   private ScopeTester leaves;

   public ScopeTester getTop()
   {
      return top;
   }

   @Search(bean = "bean", type = "toplevel")
   public void setTop(ScopeTester top)
   {
      this.top = top;
   }

   public ScopeTester getParent()
   {
      return parent;
   }

   @Search(bean = "bean", type = "parentonly")
   public void setParent(ScopeTester parent)
   {
      this.parent = parent;
   }

   public ScopeTester getLocal()
   {
      return local;
   }

   @Search(bean = "bean", type = "local")
   public void setLocal(ScopeTester local)
   {
      this.local = local;
   }

   public ScopeTester getNormal()
   {
      return normal;
   }

   @Search(bean = "bean", type = "<default>")
   public void setNormal(ScopeTester normal)
   {
      this.normal = normal;
   }

   public ScopeTester getWithChildren()
   {
      return withChildren;
   }

   @Search(bean = "bean", type = "depth")
   public void setWithChildren(ScopeTester withChildren)
   {
      this.withChildren = withChildren;
   }

   public ScopeTester getChildrenOnly()
   {
      return childrenOnly;
   }

   @Search(bean = "bean", type = "ChildrenOnlyDepth")
   public void setChildrenOnly(ScopeTester childrenOnly)
   {
      this.childrenOnly = childrenOnly;
   }

   public ScopeTester getLeaves()
   {
      return leaves;
   }

   @Search(bean = "bean", type = "leavesfirst")
   public void setLeaves(ScopeTester leaves)
   {
      this.leaves = leaves;
   }
}