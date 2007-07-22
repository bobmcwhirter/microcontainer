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

import java.util.ArrayList;
import java.util.List;

import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;

/**
 * Helper class.
 *
 * @param <T> the parameter holder type
 * @see BeanMetaDataBuilderImpl
 * @see LifecycleMetaDataBuilder
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ParameterMetaDataBuilder<T extends MutableParameterizedMetaData>
{
   private T parameterHolder;

   public ParameterMetaDataBuilder(T parameterHolder) throws IllegalArgumentException
   {
      this.parameterHolder = parameterHolder;
   }

   private List<ParameterMetaData> getParameters()
   {
      return parameterHolder.getParameters();
   }

   private void setParameters(List<ParameterMetaData> parameters)
   {
      parameterHolder.setParameters(parameters);
   }

   private List<ParameterMetaData> checkParameters()
   {
      List<ParameterMetaData> parameters = getParameters();
      if (parameters == null)
      {
         parameters = new ArrayList<ParameterMetaData>();
         setParameters(parameters);
      }
      return parameters;
   }

   public T addParameterMetaData(String type, Object value)
   {
      List<ParameterMetaData> parameters = checkParameters();
      parameters.add(new AbstractParameterMetaData(type, value));
      return parameterHolder;
   }

   public T addParameterMetaData(String type, ValueMetaData value)
   {
      List<ParameterMetaData> parameters = checkParameters();
      parameters.add(new AbstractParameterMetaData(type, value));
      return parameterHolder;
   }

}
