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
import java.util.List;

import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.metadata.spi.MetaData;
import org.jboss.reflect.spi.AnnotatedInfo;

/**
 * Abstract annotation plugin.
 *
 * @param <T> info type
 * @param <C> annotation type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractAnnotationPlugin<T extends AnnotatedInfo, C extends Annotation> extends BaseMetaDataAnnotationPlugin<T, C> implements AnnotationPlugin<T, C>
{
   protected AbstractAnnotationPlugin(Class<C> annotation)
   {
      super(annotation);
   }

   /**
    * Is meta data already present.
    *
    * @param info the info
    * @param annotation the annotation
    * @param context the context
    * @return true if meta data already present
    */
   protected boolean isMetaDataAlreadyPresent(T info, C annotation, KernelControllerContext context)
   {
      return isMetaDataAlreadyPresent(info, annotation, context.getBeanMetaData());
   }

   /**
    * Apply annotation since it's not present.
    *
    * @param info the info
    * @param retrieval the metadata
    * @param annotation the annotation
    * @param context the context
    * @return list of added meta data visitor nodes
    * @throws Throwable for any error
    */
   protected List<? extends MetaDataVisitorNode> internalApplyAnnotation(T info, MetaData retrieval, C annotation, KernelControllerContext context) throws Throwable
   {
      return internalApplyAnnotation(info, retrieval, annotation, context.getBeanMetaData());
   }

   /**
    * Apply annotation since it's not present.
    *
    * @param info the info
    * @param retrieval the metadata
    * @param annotation the annotation
    * @param beanMetaData the bean metadata
    * @return list of added meta data visitor nodes
    * @throws Throwable for any error
    */
   protected List<? extends MetaDataVisitorNode> internalApplyAnnotation(T info, MetaData retrieval, C annotation, BeanMetaData beanMetaData) throws Throwable
   {
      return internalApplyAnnotation(info, annotation, beanMetaData);
   }

   /**
    * Covariant override. 
    */
   protected List<? extends MetaDataVisitorNode> internalApplyAnnotation(T info, C annotation, BeanMetaData beanMetaData) throws Throwable
   {
      log.warn("Probably missing annotation apply implementation: " + this);
      return null;
   }

   public final void applyAnnotation(T info, MetaData retrieval, MetaDataVisitor visitor) throws Throwable
   {
      boolean trace = log.isTraceEnabled();
      
      if (isCleanupOnly() == false)
      {
         Class<C> annotationClass = getAnnotation();
         C annotation = retrieval.getAnnotation(annotationClass);
         if (annotation == null)
         {
            if (trace)
               log.trace("No annotation: " + annotationClass.getName());
            return;
         }
         if (isMetaDataAlreadyPresent(info, annotation, visitor.getControllerContext()))
         {
            if (trace)
               log.trace("MetaDataAlreadyPresent, ignoring " + annotation);
            return;
         }
         if (trace)
            log.trace("Applying annotation: " + annotation);
         List<? extends MetaDataVisitorNode> nodes = internalApplyAnnotation(info, retrieval, annotation, visitor.getControllerContext());
         if (nodes != null && nodes.isEmpty() == false)
         {
            for(MetaDataVisitorNode node : nodes)
            {
               node.initialVisit(visitor);
               node.describeVisit(visitor);
            }
         }
      }
      else if (trace)
         log.trace("Annotation " + getAnnotation() + " is @CleanupOnly, nothing to apply on install.");
   }

   public void cleanAnnotation(T info, MetaData retrieval, MetaDataVisitor visitor) throws Throwable
   {
      boolean trace = log.isTraceEnabled();

      if (isCleanup())
      {
         Class<C> annotationClass = getAnnotation();
         C annotation = retrieval.getAnnotation(annotationClass);
         if (annotation == null)
         {
            if (trace)
               log.trace("No annotation: " + annotationClass.getName());
         }
         else
         {
            if (trace)
               log.trace("Cleaning annotation: " + annotation);
            internalCleanAnnotation(info, retrieval, annotation, visitor.getControllerContext());
         }
      }
      else if (trace)
         log.trace("Annotation " + getAnnotation() + " is not a @Cleanup annotation.");
   }

   /**
    * Clean annotation's actions.
    *
    * @param info the info
    * @param retrieval the metadata
    * @param annotation the annotation
    * @param context the context
    * @throws Throwable for any error
    */
   protected void internalCleanAnnotation(T info, MetaData retrieval, C annotation, KernelControllerContext context) throws Throwable
   {
      // empty      
   }

   /**
    * Check if we require impl detail on BeanMetaData.
    *
    * @param beanMetaData the bean metadata
    * @return abstract bean metadata instance
    */
   protected AbstractBeanMetaData checkIfNotAbstractBeanMetaDataSpecific(BeanMetaData beanMetaData)
   {
      if (beanMetaData instanceof AbstractBeanMetaData == false)
         throw new IllegalArgumentException("Can only handle AbstractBeanMetaData: " + beanMetaData);

      return AbstractBeanMetaData.class.cast(beanMetaData);
   }
}