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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.metadata.spi.retrieval.AnnotationItem;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;
import org.jboss.reflect.spi.AnnotatedInfo;
import org.jboss.util.JBossObject;

/**
 * @param <T> info type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractAnnotationPlugin<T extends AnnotatedInfo, C extends Annotation> extends JBossObject implements AnnotationPlugin<T, C>
{
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

   protected List<? extends MetaDataVisitorNode> internalApplyAnnotation(T info, MetaDataRetrieval retrieval, C annotation, KernelControllerContext context) throws Throwable
   {
      return internalApplyAnnotation(info, annotation, context);
   }

   protected List<? extends MetaDataVisitorNode> internalApplyAnnotation(T info, C annotation, KernelControllerContext context) throws Throwable
   {
      return internalApplyAnnotation(info, annotation, context.getBeanMetaData());
   }

   protected List<? extends MetaDataVisitorNode> internalApplyAnnotation(T info, C annotation, BeanMetaData beanMetaData) throws Throwable
   {
      return Collections.emptyList();
   }

   public final void applyAnnotation(T info, MetaDataRetrieval retrieval, MetaDataVisitor visitor) throws Throwable
   {
      AnnotationItem<C> item = retrieval.retrieveAnnotation(getAnnotation());
      if (item == null || isMetaDataAlreadyPresent(info, item.getAnnotation(), visitor.getControllerContext()))
         return;
      List<? extends MetaDataVisitorNode> nodes = internalApplyAnnotation(info, retrieval, item.getAnnotation(), visitor.getControllerContext());
      if (nodes != null && nodes.isEmpty() == false)
      {
         for(MetaDataVisitorNode node : nodes)
         {
            node.initialVisit(visitor);
            node.describeVisit(visitor);
         }
      }
   }

}