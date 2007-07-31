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
package org.jboss.kernel.plugins.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Stack;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.metadata.spi.retrieval.AnnotationItem;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;
import org.jboss.reflect.spi.AnnotatedInfo;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.dependency.spi.CallbackItem;
import org.jboss.logging.Logger;

/**
 * @param <T> info type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractAnnotationPlugin<T extends AnnotatedInfo, C extends Annotation> implements AnnotationPlugin<T, C>
{
   protected Logger log = Logger.getLogger(getClass());
   private Class<C> annotation;
   private Set<ElementType> types;

   protected AbstractAnnotationPlugin(Class<C> annotation)
   {
      if (annotation == null)
         throw new IllegalArgumentException("Null annotation!");
      this.annotation = annotation;
      this.types = new HashSet<ElementType>();
      Target target = annotation.getAnnotation(Target.class);
      if (target != null)
      {
         List<ElementType> list = Arrays.asList(target.value());
         for(ElementType type : list)
         {
            if (isElementTypeSupported(type))
            {
               types.add(type);
            }
            else
               log.warn("Unsupported type " + type + " on annotation " + annotation);
         }
      }
   }

   protected static boolean isAttributePresent(String value)
   {
      return ValueUtil.isAttributePresent(value);
   }

   protected abstract boolean isElementTypeSupported(ElementType type);

   public Set<ElementType> getSupportedTypes()
   {
      return Collections.unmodifiableSet(types);
   }

   public Class<C> getAnnotation()
   {
      return annotation;
   }

   protected boolean isMetaDataAlreadyPresent(T info, C annotation, KernelControllerContext context)
   {
      return isMetaDataAlreadyPresent(info, annotation, context.getBeanMetaData());
   }

   protected boolean isMetaDataAlreadyPresent(T info, C annotation, BeanMetaData beanMetaData)
   {
      return false;
   }

   protected void internalApplyAnnotation(T info, MetaDataRetrieval retrieval, C annotation, KernelControllerContext context) throws Throwable
   {
      internalApplyAnnotation(info, annotation, context);
   }

   protected void internalApplyAnnotation(T info, C annotation, KernelControllerContext context) throws Throwable
   {
      internalApplyAnnotation(info, annotation, context.getBeanMetaData());
   }

   protected void internalApplyAnnotation(T info, C annotation, BeanMetaData beanMetaData) throws Throwable
   {
   }

   public final void applyAnnotation(T info, MetaDataRetrieval retrieval, KernelControllerContext context) throws Throwable
   {
      // todo - match multiple annotations?
      AnnotationItem<C> item = retrieval.retrieveAnnotation(getAnnotation());
      if (item == null || isMetaDataAlreadyPresent(info, item.getAnnotation(), context))
         return;
      internalApplyAnnotation(info, retrieval, item.getAnnotation(), context);
   }

   protected void executeVisit(KernelControllerContext context, MetaDataVisitorNode node)
   {
      MetaDataVisitor visitor = getMetaDataVisitor(context);
      node.initialVisit(visitor);
      node.describeVisit(visitor);
   }

   protected MetaDataVisitor getMetaDataVisitor(KernelControllerContext context)
   {
      return new PluginMetaDataVisitor(context);
   }

   protected class PluginMetaDataVisitor implements MetaDataVisitor
   {
      private ControllerState contextState = ControllerState.INSTANTIATED;
      private KernelControllerContext context;
      private Stack<MetaDataVisitorNode> visitorNodeStack;

      public PluginMetaDataVisitor(KernelControllerContext context)
      {
         this.context = context;
         this.visitorNodeStack = new Stack<MetaDataVisitorNode>();
         // add bean meta data
         this.visitorNodeStack.add(context.getBeanMetaData());
      }

      public KernelControllerContext getControllerContext()
      {
         return context;
      }

      public ControllerState getContextState()
      {
         return contextState;
      }

      public void setContextState(ControllerState contextState)
      {
         this.contextState = contextState;
      }

      public void addDependency(DependencyItem dependency)
      {
         context.getDependencyInfo().addIDependOn(dependency);
      }

      public void addInstallCallback(CallbackItem callback)
      {
         context.getDependencyInfo().addInstallItem(callback);
      }

      public void addUninstallCallback(CallbackItem callback)
      {
         context.getDependencyInfo().addUninstallItem(callback);
      }

      public void initialVisit(MetaDataVisitorNode node)
      {
         visitorNodeStack.push(node);
         try
         {
            boolean trace = log.isTraceEnabled();
            if (trace)
               log.trace("Initial visit node " + node);

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
                     child.initialVisit(this);
                  }
                  finally
                  {
                     contextState = restoreState;
                  }
               }
            }
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
            boolean trace = log.isTraceEnabled();
            if (trace)
               log.trace("Describe visit node " + node);

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
                     child.describeVisit(this);
                  }
                  finally
                  {
                     contextState = restoreState;
                  }
               }
            }
         }
         finally
         {
            visitorNodeStack.pop();
         }
      }

      public Stack<MetaDataVisitorNode> visitorNodeStack()
      {
         return visitorNodeStack;
      }
   }
}