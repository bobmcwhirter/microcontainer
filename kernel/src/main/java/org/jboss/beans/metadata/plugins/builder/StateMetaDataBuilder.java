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
package org.jboss.beans.metadata.plugins.builder;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.LifecycleMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;

/**
 * Helper class.
 * @see BeanMetaDataBuilder
 * @see org.jboss.beans.metadata.plugins.builder.ParameterMetaDataBuilderImpl
 *
 * @param <T> exact type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class StateMetaDataBuilder<T extends LifecycleMetaData> implements StateActionBuilder<T>
{
   /** The bean meta data */
   protected AbstractBeanMetaData beanMetaData;

   public StateMetaDataBuilder(AbstractBeanMetaData beanMetaData)
   {
      this.beanMetaData = beanMetaData;
   }

   /**
    * Set lifecycle metadata to bean metadata.
    *
    * @param beanMetaData the bean metadata
    * @param lifecycle the lifecycle
    */
   protected abstract void setLifecycle(AbstractBeanMetaData beanMetaData, T lifecycle);

   /**
    * Create lifecycle meta data.
    *
    * @return lifecycle metadata instance
    */
   protected abstract T createLifecycleMetaData();

   /**
    * Invoke after set.
    *
    * @param lifecycle the lifecycle
    */
   protected abstract void applyAfterSet(T lifecycle);

   public T createStateActionMetaData(String methodInfo)
   {
      T lifecycle = createLifecycleMetaData();
      if (methodInfo != null)
      {
         setMethodInfo(lifecycle, methodInfo);
      }
      setLifecycle(beanMetaData, lifecycle);
      applyAfterSet(lifecycle);
      return lifecycle;
   }

   /**
    * Set the method info.
    *
    * @param lifecycle the lifecycle
    * @param methodInfo the method info
    */
   protected void setMethodInfo(T lifecycle, String methodInfo)
   {
      lifecycle.setMethodName(methodInfo);
   }
}
