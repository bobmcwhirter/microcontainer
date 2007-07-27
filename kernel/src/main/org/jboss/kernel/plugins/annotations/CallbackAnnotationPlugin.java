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
import java.util.List;
import java.util.Set;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractCallbackMetaData;
import org.jboss.beans.metadata.spi.CallbackMetaData;
import org.jboss.dependency.spi.CallbackItem;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.reflect.spi.AnnotatedInfo;

/**
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
      Set<CallbackItem> callbacks = getCallbacks(dependency);
      if (callbacks != null && callbacks.isEmpty() == false)
      {
         for(CallbackItem ci : callbacks)
         {
            if (isNameEqual(info, ci.getAttributeName()))
               return true;
         }
      }
      return false;
   }

   protected abstract boolean isNameEqual(T info, String attributeName);

   protected abstract Set<CallbackItem> getCallbacks(DependencyInfo dependency);

   protected void internalApplyAnnotation(T info, C annotation, KernelControllerContext context)
   {
      AbstractBeanMetaData beanMetaData = (AbstractBeanMetaData)context.getBeanMetaData();
      List<CallbackMetaData> callbacks = getCallbacks(beanMetaData);
      AbstractCallbackMetaData callback = createCallback(info, annotation);
      callbacks.add(callback);
      executeVisit(context, callback);
   }

   protected abstract AbstractCallbackMetaData createCallback(T info, C annotation);

   protected abstract void applyInfo(AbstractCallbackMetaData callback, T info);

   protected abstract List<CallbackMetaData> getCallbacks(AbstractBeanMetaData beanMetaData);
}
