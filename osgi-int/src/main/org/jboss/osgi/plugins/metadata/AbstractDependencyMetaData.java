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
package org.jboss.osgi.plugins.metadata;

import java.io.Serializable;

import org.jboss.dependency.plugins.AbstractDependencyItem;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.osgi.spi.dependency.ServiceControllerContext;
import org.jboss.osgi.spi.metadata.DependencyMetaData;
import org.jboss.osgi.spi.metadata.ServiceMetaDataVisitor;
import org.jboss.util.JBossStringBuilder;

/**
 * A dependency.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AbstractDependencyMetaData extends AbstractMetaDataVisitorNode
      implements DependencyMetaData, Serializable
{
   private static final long serialVersionUID = 1L;

   /** The dependency, may transient? */
   protected Object dependency;

   /**
    * Create a new dependency
    */
   public AbstractDependencyMetaData()
   {
   }

   /**
    * Create a new dependency
    *
    * @param dependency the dependency
    */
   public AbstractDependencyMetaData(Object dependency)
   {
      this.dependency = dependency;
   }

   /**
    * Set the dependency
    *
    * @param dependency the dependency
    */
   public void setDependency(Object dependency)
   {
      this.dependency = dependency;
      flushJBossObjectCache();
   }

   public Object getDependency()
   {
      return dependency;
   }

   public void visit(ServiceMetaDataVisitor visitor)
   {
      ServiceControllerContext context = visitor.getControllerContext();
      DependencyItem item = new LifecycleDependencyItem(context.getName(), ControllerState.CREATE);
      visitor.addDependency(item);
      item = new LifecycleDependencyItem(context.getName(), ControllerState.START);
      visitor.addDependency(item);
      
      visitor.visit(this);
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("dependency=").append(dependency);
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(dependency);
   }

   /**
    * A LifecycleDependencyItem.
    */
   public class LifecycleDependencyItem extends AbstractDependencyItem
   {
      /**
       * Create a new LifecycleDependencyItem.
       *
       * @param name the name
       * @param state the state
       */
      public LifecycleDependencyItem(Object name, ControllerState state)
      {
         super(name, dependency, state, state);
      }
   }
}
