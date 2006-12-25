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
package org.jboss.beans.metadata.plugins;

import java.lang.reflect.Method;

import org.jboss.beans.metadata.spi.LifecycleMetaData;

/**
 * Helper class.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 * @see BeanMetaDataBuilder
 * @see ParameterMetaDataBuilder
 */
public class LifecycleMetaDataBuilder
{
   private AbstractBeanMetaData beanMetaData;
   private Method GET_METHOD;
   private Method SET_METHOD;
   private ParameterMetaDataBuilder<LifecycleMetaData> builder;

   public enum LifecycleType
   {
      CREATE("Create"),
      START("Start"),
      STOP("Stop"),
      DESTROY("Destroy");

      private String type;

      LifecycleType(String type)
      {
         this.type = type;
      }

      public String toString()
      {
         return type;
      }
   }

   public LifecycleMetaDataBuilder(AbstractBeanMetaData beanMetaData, LifecycleType type) throws IllegalArgumentException
   {
      this.beanMetaData = beanMetaData;
      try
      {
         GET_METHOD = AbstractBeanMetaData.class.getMethod("get" + type);
         SET_METHOD = AbstractBeanMetaData.class.getMethod("set" + type, LifecycleMetaData.class);
      }
      catch (NoSuchMethodException e)
      {
         throw new IllegalArgumentException("Holder MetaData object doesn't implement get or set " + type + " method: " + e);
      }
   }

   private LifecycleMetaData getLifecycle()
   {
      try
      {
         return (LifecycleMetaData) GET_METHOD.invoke(beanMetaData);
      }
      catch (Exception e)
      {
         throw new IllegalArgumentException(e);
      }
   }

   private void setLifecycle(LifecycleMetaData lifecycle)
   {
      try
      {
         SET_METHOD.invoke(beanMetaData, lifecycle);
      }
      catch (Exception e)
      {
         throw new IllegalArgumentException(e);
      }
   }

   public LifecycleMetaData createLifecycleMetaData(String methodName)
   {
      AbstractLifecycleMetaData lifecycle = new AbstractLifecycleMetaData();
      if (methodName != null)
      {
         lifecycle.setMethodName(methodName);
      }
      setLifecycle(lifecycle);
      builder = new ParameterMetaDataBuilder<LifecycleMetaData>(lifecycle);
      return lifecycle;
   }

   public LifecycleMetaData addParameterMetaData(String type, Object value)
   {
      LifecycleMetaData lifecycle = getLifecycle();
      if (lifecycle == null)
      {
         createLifecycleMetaData(null);
      }
      return builder.addParameterMetaData(type, value);
   }

}
