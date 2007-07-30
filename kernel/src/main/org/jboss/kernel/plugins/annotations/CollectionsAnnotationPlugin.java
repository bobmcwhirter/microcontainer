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

import java.lang.annotation.Annotation;

import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.plugins.annotations.Value;
import org.jboss.beans.metadata.plugins.annotations.StringValue;
import org.jboss.beans.metadata.plugins.annotations.Inject;
import org.jboss.beans.metadata.plugins.annotations.ValueFactory;
import org.jboss.beans.metadata.plugins.annotations.NullValue;
import org.jboss.beans.metadata.plugins.annotations.ThisValue;

/**
 * @param <C> annotation type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class CollectionsAnnotationPlugin<C extends Annotation> extends PropertyAnnotationPlugin<C>
{
   public CollectionsAnnotationPlugin(Class<C> annotation)
   {
      super(annotation);
   }

   protected ValueMetaData createValueMetaData(Value value)
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

      if (vmd == null)
         throw new IllegalArgumentException("No value set on @Value annotation!");

      return vmd;
   }

   protected void checkValueMetaData(ValueMetaData value)
   {
      if (value != null)
         throw new IllegalArgumentException("@Value annotation has too many values set!");
   }
}
