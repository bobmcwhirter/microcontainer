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

import org.jboss.beans.metadata.api.annotations.Cleanup;
import org.jboss.beans.metadata.api.annotations.CleanupOnly;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.metadata.spi.MetaData;
import org.jboss.reflect.spi.AnnotatedInfo;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Annotation plugin based on bean metadata.
 *
 * @param <T> info type
 * @param <C> annotation type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class BaseMetaDataAnnotationPlugin<T extends AnnotatedInfo, C extends Annotation> extends JBossObject implements MetaDataAnnotationPlugin<T, C>
{
   private Class<C> annotation;
   private Set<ElementType> types;
   private boolean isCleanup;
   private boolean isCleanupOnly;

   protected BaseMetaDataAnnotationPlugin(Class<C> annotation)
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
         }
      }
      isCleanupOnly = annotation.isAnnotationPresent(CleanupOnly.class);
      isCleanup = isCleanupOnly || annotation.isAnnotationPresent(Cleanup.class);
   }

   public final void applyAnnotation(T info, MetaData retrieval, BeanMetaData beanMetaData) throws Throwable
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
         if (isMetaDataAlreadyPresent(info, annotation, beanMetaData))
         {
            if (trace)
               log.trace("MetaDataAlreadyPresent, ignoring " + annotation);
            return;
         }
         if (trace)
            log.trace("Applying annotation: " + annotation);

         internalApplyAnnotation(info, retrieval, annotation, beanMetaData);
      }
      else if (trace)
         log.trace("Annotation " + getAnnotation() + " is @CleanupOnly, nothing to apply on install.");
   }

   /**
    * Is meta data already present.
    *
    * @param info the info
    * @param annotation the annotation
    * @param beanMetaData the bean meta data
    * @return true if meta data already present
    */
   protected boolean isMetaDataAlreadyPresent(T info, C annotation, BeanMetaData beanMetaData)
   {
      return false;
   }

   /**
    * Apply annotation since it's not present.
    *
    * @param info the info
    * @param retrieval the metadata
    * @param annotation the annotation
    * @param beanMetaData the bean meta data
    * @return used to return covariant type
    * @throws Throwable for any error
    */
   protected Object internalApplyAnnotation(T info, MetaData retrieval, C annotation, BeanMetaData beanMetaData) throws Throwable
   {
      return internalApplyAnnotation(info, annotation, beanMetaData);
   }

   /**
    * Apply annotation since it's not present.
    *
    * @param info the info
    * @param annotation the annotation
    * @param beanMetaData the bean meta data
    * @return used to return covariant type
    * @throws Throwable for any error
    */
   protected abstract Object internalApplyAnnotation(T info, C annotation, BeanMetaData beanMetaData) throws Throwable;

   /**
    * Is annotation marked as cleanup.
    *
    * @return true if cleanup annotation
    */
   protected boolean isCleanup()
   {
      return isCleanup;
   }

   /**
    * Is annotation marked as cleanup only.
    *
    * @return true if clean up only
    */
   protected boolean isCleanupOnly()
   {
      return isCleanupOnly;
   }

   /**
    * Does attribute have value.
    * Helper method.
    *
    * @param value the value
    * @return true if atribute not null or non-empty
    */
   protected static boolean isAttributePresent(String value)
   {
      return ValueUtil.isAttributePresent(value);
   }

   /**
    * Does attribute have value.
    * Helper method.
    *
    * @param value the value
    * @return true if atribute not void.class
    */
   protected static boolean isAttributePresent(Class<?> value)
   {
      return ValueUtil.isAttributePresent(value);
   }

   /**
    * Is type supported by plugin.
    *
    * @param type the annotation element type
    * @return true if element supported
    */
   protected abstract boolean isElementTypeSupported(ElementType type);

   public Set<ElementType> getSupportedTypes()
   {
      return Collections.unmodifiableSet(types);
   }

   public Class<C> getAnnotation()
   {
      return annotation;
   }

   protected void toString(JBossStringBuilder buffer)
   {
      buffer.append("@annotation=").append(annotation);
      buffer.append(", types=").append(types);
      buffer.append(", cleanup=").append(isCleanup());
      buffer.append(", cleanupOnly=").append(isCleanupOnly());
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append("@annotation=").append(annotation);
   }
}
