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
import java.util.Collections;
import java.util.Set;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractLifecycleMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.reflect.spi.ParameterInfo;

/**
 * Abstract lifecycle annotation plugin.
 *
 * @param <C> annotation type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class LifecycleParameterAnnotationPlugin<C extends Annotation> extends AbstractMethodParameterAnnotationPlugin<C, AbstractLifecycleMetaData>
{
   protected LifecycleParameterAnnotationPlugin(Class<C> annotation, Set<Annotation2ValueMetaDataAdapter<? extends Annotation>> adapters)
   {
      super(annotation, adapters);
   }

   protected boolean checkAnnotatedInfo(ElementType type)
   {
      return ElementType.METHOD == type;
   }

   /**
    * Is lifecycle metadata present.
    *
    * @param beanMetaData the bean metadata
    * @return true if lifecycle metadata exists
    */
   protected abstract boolean isLifecyclePresent(BeanMetaData beanMetaData);

   protected AbstractLifecycleMetaData createLifecycleMetaData(String method, C annotation)
   {
      AbstractLifecycleMetaData lifecycle = new AbstractLifecycleMetaData(method);
      applyLifecycleAnnotation(lifecycle, annotation);
      return lifecycle;
   }

   /**
    * Apply lifecycle annotation.
    *
    * @param lifecycle lifecycle metadata
    * @param annotation the annotation
    */
   protected abstract void applyLifecycleAnnotation(AbstractLifecycleMetaData lifecycle, C annotation);

   /**
    * Set lifecycle metadata.
    *
    * @param beanMetaData the bean metadata
    * @param lifecycle the lifecycle metadata
    */
   protected abstract void setLifecycleMetaData(AbstractBeanMetaData beanMetaData, AbstractLifecycleMetaData lifecycle);

   protected boolean isMetaDataAlreadyPresent(MethodInfo info, C annotation, BeanMetaData beanMetaData)
   {
      return isLifecyclePresent(beanMetaData);
   }

   protected ParameterInfo[] getParameters(MethodInfo info)
   {
      return info.getParameters();
   }

   protected List<? extends MetaDataVisitorNode> handleParameterlessInfo(MethodInfo info, C annotation, BeanMetaData beanMetaData)
   {
      AbstractBeanMetaData abmd = checkIfNotAbstractBeanMetaDataSpecific(beanMetaData);
      AbstractLifecycleMetaData lifecycle = createLifecycleMetaData(info.getName(), annotation);
      setLifecycleMetaData(abmd, lifecycle);
      return Collections.singletonList(lifecycle);
   }

   protected AbstractLifecycleMetaData createParametrizedMetaData(MethodInfo info, C annotation)
   {
      return createLifecycleMetaData(info.getName(), annotation);
   }

   protected void setParameterizedMetaData(AbstractLifecycleMetaData parameterizedMetaData, BeanMetaData beanMetaData)
   {
      AbstractBeanMetaData abmd = checkIfNotAbstractBeanMetaDataSpecific(beanMetaData);
      setLifecycleMetaData(abmd, parameterizedMetaData);
   }
}
