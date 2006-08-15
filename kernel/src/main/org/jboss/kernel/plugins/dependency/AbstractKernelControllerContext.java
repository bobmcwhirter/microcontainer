/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.Stack;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.dependency.plugins.AbstractControllerContext;
import org.jboss.dependency.plugins.AbstractDependencyInfo;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.repository.spi.MetaDataContext;
import org.jboss.util.JBossStringBuilder;

/**
 * Controller context.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractKernelControllerContext extends AbstractControllerContext implements KernelControllerContext
{
   /** The default actions */
   private static final KernelControllerContextActions actions = KernelControllerContextActions.getInstance();

   /** The no instantiate actions */
   private static final KernelControllerContextActions noInstantiate = KernelControllerContextActions.getNoInstantiate();
   
   /** The BeanInfo */
   protected BeanInfo info;

   /** The meta data */
   protected BeanMetaData metaData;
   
   /** The access control context */
   protected AccessControlContext accessContext;
   
   /**
    * Create an abstract controller context
    * 
    * @param info the bean info
    * @param metaData the meta data
    * @param target any target object
    */
   public AbstractKernelControllerContext(BeanInfo info, BeanMetaData metaData, Object target)
   {
      super(metaData.getName(), target == null ? actions : noInstantiate, new AbstractDependencyInfo(), target);
      this.info = info;
      this.metaData = metaData;
      ControllerMode mode = metaData.getMode();
      if (mode != null)
         setMode(mode);
      if (System.getSecurityManager() != null)
         accessContext = AccessController.getContext();
   }

   public Kernel getKernel()
   {
      KernelController controller = (KernelController) getController();
      if (controller == null)
         throw new IllegalStateException("Context is not installed in controller");
      return controller.getKernel();
   }
   
   public BeanInfo getBeanInfo()
   {
      return info;
   }

   /**
    * Set the bean info
    * 
    * @param info the bean info
    */
   public void setBeanInfo(BeanInfo info)
   {
      this.info = info;
      flushJBossObjectCache();
   }

   public BeanMetaData getBeanMetaData()
   {
      return metaData;
   }

   public void toString(JBossStringBuilder buffer)
   {
      if (metaData != null)
         buffer.append(" metadata=").append(metaData);
      super.toString(buffer);
   }

   public void setController(Controller controller)
   {
      super.setController(controller);
      preprocessMetaData();
   }

   /**
    * Preprocess the metadata for this context
    */
   protected void preprocessMetaData()
   {
      if (metaData == null)
         return;
      PreprocessMetaDataVisitor visitor = new PreprocessMetaDataVisitor(metaData);
      AccessController.doPrivileged(visitor);
   }

   public MetaDataContext getMetaDataContext()
   {
      if (info != null)
         return info.getMetaDataContext();
      return null;
   }

   public void setMetaDataContext(MetaDataContext mctx)
   {
      info = info.getInstanceInfo();
      info.setMetaDataContext(mctx);
   }

   /**
    * Get the access control context of the code that created this context.<p>
    * 
    * This will be null when there is no security manager.
    * 
    * @return any access control context
    */
   protected AccessControlContext getAccessControlContext()
   {
      return accessContext;
   }

   protected abstract class AbstractMetaDataVistor implements MetaDataVisitor, PrivilegedAction<Object>
   {
      /** The current context for when the dependencies are required */
      protected ControllerState contextState = ControllerState.INSTANTIATED;

      /** The metadata */
      protected BeanMetaData bmd;

      /** Visited branch stack */
      protected Stack visitorNodeStack;

      protected AbstractMetaDataVistor(BeanMetaData bmd)
      {
         this.bmd = bmd;
      }

      public void visit(MetaDataVisitorNode node)
      {
      }

      public void revisit(MetaDataVisitorNode node)
      {
      }

      public KernelControllerContext getControllerContext()
      {
         return AbstractKernelControllerContext.this;
      }

      public ControllerState getContextState()
      {
         return contextState;
      }

      public void addDependency(DependencyItem dependency)
      {
         getDependencyInfo().addIDependOn(dependency);
      }

      public void setContextState(ControllerState contextState)
      {
         this.contextState = contextState;
      }

      public Stack visitorNodeStack()
      {
         return visitorNodeStack;
      }

   }

   /**
    * A visitor for the metadata that looks for dependencies.
    */
   protected class PreprocessMetaDataVisitor extends AbstractMetaDataVistor
   {
      /**
       * Create a new MetaDataVisitor.
       */
      public PreprocessMetaDataVisitor(BeanMetaData bmd)
      {
         super(bmd);
      }
      
      /**
       * Visit the bean metadata node, this is the starting point
       */
      public Object run()
      {
         bmd.visit(this);
         return null;
      }
      
      /**
       * Visit a node
       * 
       * @param node the node
       */
      public void visit(MetaDataVisitorNode node)
      {
         boolean trace = log.isTraceEnabled();
         if (trace)
            log.trace("Visit node " + node);
         
         // Visit the children of this node
         Iterator children = node.getChildren();
         if (children != null)
         {
            ControllerState restoreState = contextState;
            while (children.hasNext())
            {
               MetaDataVisitorNode child = (MetaDataVisitorNode) children.next();
               try
               {
                  child.visit(this);
               }
               finally
               {
                  contextState = restoreState;
               }
            }
         }
      }

   }

   /**
    * A visitor for the metadata that looks for dependencies.
    */
   protected class DescribedMetaDataVisitor extends AbstractMetaDataVistor
   {
      /**
       * Create a new MetaDataVisitor.
       */
      public DescribedMetaDataVisitor(BeanMetaData bmd)
      {
         super(bmd);
         this.visitorNodeStack = new Stack();
      }

      /**
       * Visit the bean metadata node, this is the starting point
       */
      public Object run()
      {
         visitorNodeStack.push(bmd);
//         bmd.revisit(this);
         visitorNodeStack.pop();
         visitorNodeStack = null;
         return null;
      }

      /**
       * Visit a node
       *
       * @param node the node
       */
      public void revisit(MetaDataVisitorNode node)
      {
         boolean trace = log.isTraceEnabled();
         if (trace)
            log.trace("Revisit node " + node);

         // Visit the children of this node
         Iterator children = node.getChildren();
         if (children != null)
         {
            ControllerState restoreState = contextState;
            while (children.hasNext())
            {
               MetaDataVisitorNode child = (MetaDataVisitorNode) children.next();
               visitorNodeStack.push(child);
               try
               {
//                  child.revisit(this);
               }
               finally
               {
                  contextState = restoreState;
                  visitorNodeStack.pop();
               }
            }
         }
      }
   }

}