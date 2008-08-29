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
import org.jboss.beans.metadata.spi.builder.ParameterMetaDataBuilder;

/**
 * Helper class.
 *
 * @param <T> the parameter holder type
 * @see LifecycleMetaDataBuilder
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="mailto:adrian@jboss.org">Adrian Brock</a>
 */
public class ParameterMetaDataBuilderImpl<T extends MutableParameterizedMetaData> implements ParameterMetaDataBuilder
{
   /** The parameter holder */
   private T parameterHolder;

   /**
    * Create a new ParameterMetaDataBuilder.
    * 
    * @param parameterHolder the parameter holder
    * @throws IllegalArgumentException for a null parameter
    */
   public ParameterMetaDataBuilderImpl(T parameterHolder)
   {
      if (parameterHolder == null)
         throw new IllegalArgumentException("Null parameter holder");
      this.parameterHolder = parameterHolder;
   }

   /**
    * Get the parameters
    * 
    * @return the parameters
    */
   private List<ParameterMetaData> getParameters()
   {
      return parameterHolder.getParameters();
   }

   /**
    * Set the parameters
    * 
    * @param parameters the parameters
    */
   private void setParameters(List<ParameterMetaData> parameters)
   {
      parameterHolder.setParameters(parameters);
   }

   /**
    * Check the parameters
    * 
    * @return the parameters
    */
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

   /**
    * Add a parameter
    * 
    * @param type the type
    * @param value the value
    * @return the parameter
    */
   public ParameterMetaDataBuilder addParameterMetaData(String type, Object value)
   {
      List<ParameterMetaData> parameters = checkParameters();
      parameters.add(new AbstractParameterMetaData(type, value));
      return this;
   }

   /**
    * Add a parameter
    * 
    * @param type the type
    * @param value the value
    * @return the parameter
    */
   public ParameterMetaDataBuilder addParameterMetaData(String type, String value)
   {
      List<ParameterMetaData> parameters = checkParameters();
      parameters.add(new AbstractParameterMetaData(type, value));
      return this;
   }

   public ParameterMetaDataBuilder addParameterMetaData(String type, ValueMetaData value)
   {
      List<ParameterMetaData> parameters = checkParameters();
      parameters.add(new AbstractParameterMetaData(type, value));
      return this;
   }
}
