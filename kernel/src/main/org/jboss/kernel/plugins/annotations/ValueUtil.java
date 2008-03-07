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

import org.jboss.beans.metadata.api.annotations.Inject;
import org.jboss.beans.metadata.api.annotations.NullValue;
import org.jboss.beans.metadata.api.annotations.Parameter;
import org.jboss.beans.metadata.api.annotations.StringValue;
import org.jboss.beans.metadata.api.annotations.ThisValue;
import org.jboss.beans.metadata.api.annotations.Value;
import org.jboss.beans.metadata.api.annotations.ValueFactory;
import org.jboss.beans.metadata.api.annotations.JavaBeanValue;
import org.jboss.beans.metadata.spi.ValueMetaData;

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
}
