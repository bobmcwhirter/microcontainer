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
import org.jboss.beans.metadata.plugins.AbstractLifecycleMetaData;
import org.jboss.beans.metadata.spi.LifecycleMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;

/**
 * Helper class.
 * @see BeanMetaDataBuilder
 * @see ParameterMetaDataBuilder
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class LifecycleMetaDataBuilder
{
   private AbstractBeanMetaData beanMetaData;
   private ParameterMetaDataBuilder<AbstractLifecycleMetaData> builder;

   public LifecycleMetaDataBuilder(AbstractBeanMetaData beanMetaData) throws IllegalArgumentException
   {
      this.beanMetaData = beanMetaData;
   }

   abstract LifecycleMetaData getLifecycle(AbstractBeanMetaData beanMetaData);

   abstract void setLifecycle(AbstractBeanMetaData beanMetaData, LifecycleMetaData lifecycle);

   public LifecycleMetaData createLifecycleMetaData(String methodName)
   {
      AbstractLifecycleMetaData lifecycle = new AbstractLifecycleMetaData();
      if (methodName != null)
      {
         lifecycle.setMethodName(methodName);
      }
      setLifecycle(beanMetaData, lifecycle);
      builder = new ParameterMetaDataBuilder<AbstractLifecycleMetaData>(lifecycle);
      return lifecycle;
   }

   public LifecycleMetaData addParameterMetaData(String type, Object value)
   {
      LifecycleMetaData lifecycle = getLifecycle(beanMetaData);
      if (lifecycle == null)
      {
         createLifecycleMetaData(null);
      }
      return builder.addParameterMetaData(type, value);
   }

}
