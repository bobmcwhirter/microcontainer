/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.kernel.plugins.dependency;

import java.util.Iterator;
import java.util.Stack;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.dependency.spi.CallbackItem;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.logging.Logger;

/**
 * AbstractMetaDataVistor.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractMetaDataVisitor implements MetaDataVisitor
{
   /** The log */
   private static final Logger log = Logger.getLogger(AbstractMetaDataVisitor.class); 
   
   /** The current context for when the dependencies are required */
   protected ControllerState contextState = ControllerState.INSTANTIATED;

   /* The metadata */
   protected BeanMetaData bmd;

   /** The controller context */
   protected KernelControllerContext context;
   
   /**
    * Visited branch stack
    */
   protected Stack<MetaDataVisitorNode> visitorNodeStack;

   protected AbstractMetaDataVisitor(BeanMetaData bmd, KernelControllerContext context)
   {
      this.bmd = bmd;
      this.context = context;
      this.visitorNodeStack = new Stack<MetaDataVisitorNode>();
   }

   public void initialVisit(MetaDataVisitorNode node)
   {
      visitorNodeStack.push(node);
      try
      {
         internalInitialVisit(node);
      }
      finally
      {
         visitorNodeStack.pop();
      }
   }

   public void describeVisit(MetaDataVisitorNode node)
   {
      visitorNodeStack.push(node);
      try
      {
         internalDescribeVisit(node);
      }
      finally
      {
         visitorNodeStack.pop();
      }
   }

   protected void internalInitialVisit(MetaDataVisitorNode node)
   {
      boolean trace = log.isTraceEnabled();
      if (trace)
         log.trace("Initial visit node " + node);

      // Visit the children of this node
      Iterator<? extends MetaDataVisitorNode> children = node.getChildren();
      if (children != null)
      {
         ControllerState restoreState = contextState;
         while (children.hasNext())
         {
            MetaDataVisitorNode child = children.next();
            try
            {
               child.initialVisit(this);
            }
            finally
            {
               contextState = restoreState;
            }
         }
      }
   }

   protected void internalDescribeVisit(MetaDataVisitorNode node)
   {
      boolean trace = log.isTraceEnabled();
      if (trace)
         log.trace("Describe visit node " + node);

      // Visit the children of this node
      Iterator<? extends MetaDataVisitorNode> children = node.getChildren();
      if (children != null)
      {
         ControllerState restoreState = contextState;
         while (children.hasNext())
         {
            MetaDataVisitorNode child = children.next();
            try
            {
               child.describeVisit(this);
            }
            finally
            {
               contextState = restoreState;
            }
         }
      }
   }

   public KernelControllerContext getControllerContext()
   {
      return context;
   }

   public ControllerState getContextState()
   {
      return contextState;
   }

   public DependencyInfo getDependencyInfo()
   {
      return context.getDependencyInfo();
   }
   
   public void addDependency(DependencyItem dependency)
   {
      getDependencyInfo().addIDependOn(dependency);
   }

   public <T> void addInstallCallback(CallbackItem<T> callback)
   {
      getDependencyInfo().addInstallItem(callback);
   }

   public <T> void addUninstallCallback(CallbackItem<T> callback)
   {
      getDependencyInfo().addUninstallItem(callback);
   }

   public void setContextState(ControllerState contextState)
   {
      this.contextState = contextState;
   }

   public Stack<MetaDataVisitorNode> visitorNodeStack()
   {
      return visitorNodeStack;
   }

}
