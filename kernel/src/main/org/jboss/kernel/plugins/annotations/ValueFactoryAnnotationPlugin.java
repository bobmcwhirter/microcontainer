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

import java.util.ArrayList;
import java.util.List;

import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueFactoryMetaData;
import org.jboss.beans.metadata.plugins.annotations.Parameter;
import org.jboss.beans.metadata.plugins.annotations.ValueFactory;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.dependency.spi.ControllerState;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ValueFactoryAnnotationPlugin extends PropertyAnnotationPlugin<ValueFactory>
{
   static ValueFactoryAnnotationPlugin INSTANCE = new ValueFactoryAnnotationPlugin();

   public ValueFactoryAnnotationPlugin()
   {
      super(ValueFactory.class);
   }

   public ValueMetaData createValueMetaData(ValueFactory annotation)
   {
      AbstractValueFactoryMetaData factory = new AbstractValueFactoryMetaData(annotation.bean(), annotation.method());
      if (isAttributePresent(annotation.defaultValue()))
         factory.setDefaultValue(annotation.defaultValue());
      List<ParameterMetaData> parameters = new ArrayList<ParameterMetaData>();
      if (isAttributePresent(annotation.parameter()))
         parameters.add(new AbstractParameterMetaData(String.class.getName(), annotation.parameter()));
      if (annotation.parameters().length > 0)
      {
         if (parameters.size() > 0)
            throw new IllegalArgumentException("Cannot set both parameter and parameters!");
         for(Parameter parameter : annotation.parameters())
         {
            AbstractParameterMetaData apmd = new AbstractParameterMetaData(ValueUtil.createValueMetaData(parameter));
            if (isAttributePresent(parameter.type()))
               apmd.setType(parameter.type());
            parameters.add(apmd);
         }
      }
      factory.setParameters(parameters);
      factory.setDependentState(new ControllerState(annotation.dependantState()));
      factory.setWhenRequiredState(new ControllerState(annotation.whenRequiredState()));
      return factory;
   }
}
