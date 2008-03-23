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
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.beans.metadata.spi.builder.ParameterMetaDataBuilder;

/**
 * Helper class.
 * @see BeanMetaDataBuilder
 * @see ParameterMetaDataBuilderImpl
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class LifecycleMetaDataBuilder extends StateMetaDataBuilder<LifecycleMetaData>
{
   protected ParameterMetaDataBuilderImpl<AbstractLifecycleMetaData> builder;

   protected LifecycleMetaDataBuilder(AbstractBeanMetaData beanMetaData)
   {
      super(beanMetaData);
   }

   abstract LifecycleMetaData getLifecycle(AbstractBeanMetaData beanMetaData);

   protected LifecycleMetaData createLifecycleMetaData()
   {
      return new AbstractLifecycleMetaData();
   }

   protected void applyAfterSet(LifecycleMetaData lifecycle)
   {
      builder = new ParameterMetaDataBuilderImpl<AbstractLifecycleMetaData>((AbstractLifecycleMetaData)lifecycle);
   }

   /**
    * Check lifecycle.
    */
   protected void checkLlifecycle()
   {
      LifecycleMetaData lifecycle = getLifecycle(beanMetaData);
      if (lifecycle == null)
      {
         createStateActionMetaData(null);
      }
   }

   /**
    * Add parameter.
    *
    * @param type the type
    * @param value the value
    * @return parameter builder
    */
   public ParameterMetaDataBuilder addParameterMetaData(String type, Object value)
   {
      checkLlifecycle();
      return builder.addParameterMetaData(type, value);
   }

   /**
    * Add parameter.
    *
    * @param type the type
    * @param value the value
    * @return parameter builder
    */
   public ParameterMetaDataBuilder addParameterMetaData(String type, ValueMetaData value)
   {
      checkLlifecycle();
      return builder.addParameterMetaData(type, value);
   }
}
