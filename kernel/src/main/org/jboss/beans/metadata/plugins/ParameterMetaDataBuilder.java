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
import java.util.ArrayList;
import java.util.List;

import org.jboss.beans.metadata.spi.ParameterMetaData;

/**
 * Helper class.
 * @see BeanMetaDataBuilder
 * @see LifecycleMetaDataBuilder
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ParameterMetaDataBuilder<T>
{
   private T parameterHolder;
   private Method GET_PARAMETERS;
   private Method SET_PARAMETERS;

   public ParameterMetaDataBuilder(T parameterHolder) throws IllegalArgumentException
   {
      this.parameterHolder = parameterHolder;
      try
      {
         GET_PARAMETERS = parameterHolder.getClass().getMethod("getParameters");
         SET_PARAMETERS = parameterHolder.getClass().getMethod("setParameters", List.class);
      }
      catch (NoSuchMethodException e)
      {
         throw new IllegalArgumentException("Holder MetaData object doesn't implement get or set parameters method: " + e);
      }
   }

   private List<ParameterMetaData> getParameters()
   {
      try
      {
         return (List<ParameterMetaData>) GET_PARAMETERS.invoke(parameterHolder);
      }
      catch (Exception e)
      {
         throw new IllegalArgumentException(e);
      }
   }

   private void setParameters(List<ParameterMetaData> parameters)
   {
      try
      {
         SET_PARAMETERS.invoke(parameterHolder, parameters);
      }
      catch (Exception e)
      {
         throw new IllegalArgumentException(e);
      }
   }

   public T addParameterMetaData(String type, Object value)
   {
      List<ParameterMetaData> parameters = getParameters();
      if (parameters == null)
      {
         parameters = new ArrayList<ParameterMetaData>();
         setParameters(parameters);
      }
      parameters.add(new AbstractParameterMetaData(type, value));
      return parameterHolder;
   }

}
