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
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractCallbackMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.CallbackMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.dependency.spi.CallbackItem;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.reflect.spi.AnnotatedInfo;

/**
 * Callback annotation plugin.
 *
 * @param <T> info type
 * @param <C> annotation type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class CallbackAnnotationPlugin<T extends AnnotatedInfo, C extends Annotation> extends AbstractAnnotationPlugin<T, C>
{
   protected CallbackAnnotationPlugin(Class<C> annotation)
   {
      super(annotation);
   }

   protected boolean isElementTypeSupported(ElementType type)
   {
      return ElementType.METHOD == type;
   }

   protected boolean isMetaDataAlreadyPresent(T info, C annotation, KernelControllerContext context)
   {
      DependencyInfo dependency = context.getDependencyInfo();
      Set<CallbackItem<?>> callbacks = getCallbacks(dependency);
      if (callbacks != null && callbacks.isEmpty() == false)
      {
         for(CallbackItem<?> ci : callbacks)
         {
            if (isEqual(info, ci))
               return true;
         }
      }
      return false;
   }

   /**
    * Does callback item corespond to info instance.
    *
    * @param info the info
    * @param ci the callback item
    * @return true if callback matches info
    */
   protected abstract boolean isEqual(T info, CallbackItem<?> ci);

   /**
    * Get the callbacks.
    *
    * @param dependency the dependency info
    * @return set of callback items
    */
   protected abstract Set<CallbackItem<?>> getCallbacks(DependencyInfo dependency);

   protected List<? extends MetaDataVisitorNode> internalApplyAnnotation(T info, C annotation, BeanMetaData bmd)
   {
      AbstractBeanMetaData beanMetaData = checkIfNotAbstractBeanMetaDataSpecific(bmd);
      List<CallbackMetaData> callbacks = getCallbacks(beanMetaData);
      AbstractCallbackMetaData callback = createCallback(info, annotation);
      callbacks.add(callback);
      return Collections.singletonList(callback);
   }

   /**
    * Create new CallbackMetaData instance.
    *
    * @param info the info
    * @param annotation the annotation
    * @return new CallbackMetaData instance
    */
   protected abstract AbstractCallbackMetaData createCallback(T info, C annotation);

   /**
    * Apply additional information.
    *
    * @param callback the callback metadata
    * @param info the info
    */
   protected abstract void applyInfo(AbstractCallbackMetaData callback, T info);

   /**
    * Get the callback metadatas.
    *
    * @param beanMetaData the bean metadata
    * @return list of callback metadatas
    */
   protected abstract List<CallbackMetaData> getCallbacks(AbstractBeanMetaData beanMetaData);
}
