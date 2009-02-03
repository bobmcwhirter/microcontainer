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

import org.jboss.beans.metadata.api.annotations.Inject;
import org.jboss.beans.metadata.api.annotations.JavaBeanValue;
import org.jboss.beans.metadata.api.annotations.NullValue;
import org.jboss.beans.metadata.api.annotations.Parameter;
import org.jboss.beans.metadata.api.annotations.Search;
import org.jboss.beans.metadata.api.annotations.StringValue;
import org.jboss.beans.metadata.api.annotations.ThisValue;
import org.jboss.beans.metadata.api.annotations.Value;
import org.jboss.beans.metadata.api.annotations.ValueFactory;
import org.jboss.beans.metadata.api.model.FromContext;
import org.jboss.beans.metadata.plugins.AbstractInjectionValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.AbstractSearchValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueFactoryMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.graph.SearchInfo;

/**
 * Simple util class.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
final class ValueUtil
{
   /**
    * Does value already exist.
    *
    * @param value the value to check
    */
   private static void checkValueMetaData(ValueMetaData value)
   {
      if (value != null)
         throw new IllegalArgumentException("@Value/@Parameter annotation has too many values set!");
   }

   /**
    * Is attribute present.
    *
    * @param value the attribute value
    * @return true if not null and not-empty
    */
   static boolean isAttributePresent(String value)
   {
      return value != null && value.length() > 0;
   }

   /**
    * Is attribute present.
    *
    * @param value the attribute value
    * @return true if not void.class
    */
   static boolean isAttributePresent(Class<?> value)
   {
      return value != null && (void.class.equals(value) == false);
   }

   /**
    * Create value meta data from @Value annotation.
    *
    * @param value the @Value annotation
    * @return new ValueMetaData instance
    */
   static ValueMetaData createValueMetaData(Value value)
   {
      ValueMetaData vmd = null;

      StringValue string = value.string();
      if (isAttributePresent(string.value()))
      {
         vmd = StringValueAnnotationPlugin.INSTANCE.createValueMetaData(string);
      }

      Inject inject = value.inject();
      if (inject.valid())
      {
         checkValueMetaData(vmd);
         vmd = InjectAnnotationPlugin.INSTANCE.createValueMetaData(inject);
      }

      ValueFactory vf = value.valueFactory();
      if (isAttributePresent(vf.bean()))
      {
         checkValueMetaData(vmd);
         vmd = ValueFactoryAnnotationPlugin.INSTANCE.createValueMetaData(vf);
      }

      ThisValue thisValue = value.thisValue();
      if (thisValue.valid())
      {
         checkValueMetaData(vmd);
         vmd = ThisValueAnnotationPlugin.INSTANCE.createValueMetaData(thisValue);
      }

      NullValue nullValue = value.nullValue();
      if (nullValue.valid())
      {
         checkValueMetaData(vmd);
         vmd = NullValueAnnotationPlugin.INSTANCE.createValueMetaData(nullValue);
      }

      JavaBeanValue javabean = value.javabean();
      if (isAttributePresent(javabean.value()))
      {
         checkValueMetaData(vmd);
         vmd = JavaBeanValueAnnotationPlugin.INSTANCE.createValueMetaData(javabean);
      }

      if (vmd == null)
         throw new IllegalArgumentException("No value set on @Value annotation!");

      return vmd;
   }

   /**
    * Create value meta data from @Parameter annotation.
    *
    * @param parameter the @Parameter annotation
    * @return new ValueMetaData instance
    */
   static ValueMetaData createValueMetaData(Parameter parameter)
   {
      ValueMetaData vmd = null;

      StringValue string = parameter.string();
      if (isAttributePresent(string.value()))
      {
         vmd = StringValueAnnotationPlugin.INSTANCE.createValueMetaData(string);
      }

      Inject inject = parameter.inject();
      if (inject.valid())
      {
         checkValueMetaData(vmd);
         vmd = InjectAnnotationPlugin.INSTANCE.createValueMetaData(inject);
      }

      ThisValue thisValue = parameter.thisValue();
      if (thisValue.valid())
      {
         checkValueMetaData(vmd);
         vmd = ThisValueAnnotationPlugin.INSTANCE.createValueMetaData(thisValue);
      }

      NullValue nullValue = parameter.nullValue();
      if (nullValue.valid())
      {
         checkValueMetaData(vmd);
         vmd = NullValueAnnotationPlugin.INSTANCE.createValueMetaData(nullValue);
      }

      JavaBeanValue javabean = parameter.javabean();
      if (isAttributePresent(javabean.value()))
      {
         checkValueMetaData(vmd);
         vmd = JavaBeanValueAnnotationPlugin.INSTANCE.createValueMetaData(javabean);
      }

      if (vmd == null)
         throw new IllegalArgumentException("No value set on @Value annotation!");

      return vmd;
   }

   /**
    * Create injection value meta data.
    *
    * @param annotation the annotation
    * @return injection value meta data
    */
   static ValueMetaData createValueMetaData(Inject annotation)
   {
      AbstractInjectionValueMetaData injection = new AbstractInjectionValueMetaData();
      if (isAttributePresent(annotation.bean()))
         injection.setValue(annotation.bean());
      if (isAttributePresent(annotation.property()))
         injection.setProperty(annotation.property());
      if (isAttributePresent(annotation.dependentState()))
         injection.setDependentState(new ControllerState(annotation.dependentState()));
      if (isAttributePresent(annotation.whenRequired()))
         injection.setWhenRequiredState(new ControllerState(annotation.whenRequired()));
      if (isAttributePresent(annotation.search()))
         injection.setSearch(org.jboss.dependency.plugins.graph.Search.getInstance(annotation.search()));
      injection.setInjectionOption(annotation.option());
      injection.setInjectionType(annotation.type());
      if (FromContext.NOOP.equals(annotation.fromContext()) == false)
         injection.setFromContext(annotation.fromContext());
      return injection;
   }

   /**
    * Create value factory value meta data.
    *
    * @param annotation the annotation
    * @return value factory meta data
    */
   static ValueMetaData createValueMetaData(ValueFactory annotation)
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
            AbstractParameterMetaData apmd = new AbstractParameterMetaData(createValueMetaData(parameter));
            if (isAttributePresent(parameter.type()))
               apmd.setType(parameter.type().getName());
            parameters.add(apmd);
         }
      }
      factory.setParameters(parameters);
      factory.setDependentState(new ControllerState(annotation.dependantState()));
      factory.setWhenRequiredState(new ControllerState(annotation.whenRequiredState()));
      return factory;
   }

   /**
    * Create search value meta data.
    *
    * @param annotation the annotation
    * @return search meta data
    */
   @SuppressWarnings("deprecation")
   static ValueMetaData createValueMetaData(Search annotation)
   {
      String searchType = annotation.type();
      SearchInfo type = org.jboss.dependency.plugins.graph.Search.getInstance(searchType);
      ControllerState state = null;
      if (isAttributePresent(annotation.dependentState()))
         state = new ControllerState(annotation.dependentState());
      String property = null;
      if (isAttributePresent(annotation.property()))
         property = annotation.property();

      return new AbstractSearchValueMetaData(
            annotation.bean(),
            state,
            type,
            property
      );
   }
}
