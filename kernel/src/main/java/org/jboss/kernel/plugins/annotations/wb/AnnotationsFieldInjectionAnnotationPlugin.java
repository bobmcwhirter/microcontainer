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
package org.jboss.kernel.plugins.annotations.wb;

import java.lang.annotation.Annotation;

import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.kernel.plugins.annotations.FieldAnnotationPlugin;

/**
 * Generic annotations injection metadata value creator.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AnnotationsFieldInjectionAnnotationPlugin extends FieldAnnotationPlugin<Annotation>
{
   @SuppressWarnings("unchecked")
   public AnnotationsFieldInjectionAnnotationPlugin(Class annotation)
   {
      super(annotation);
   }

   protected boolean isMetaDataComplete(PropertyMetaData pmd)
   {
      if (pmd == null)
         return false;

      ValueMetaData value = pmd.getValue();
      Object underlyingValue = value.getUnderlyingValue();
      return (underlyingValue instanceof AnnotationsMatcher == false);
   }

   public ValueMetaData createValueMetaData(Annotation annotation, ValueMetaData previousValue)
   {
      if (previousValue == null)
         return new AbstractDependencyValueMetaData(new AnnotationsMatcher(annotation));

      Object underlyingValue = previousValue.getUnderlyingValue();
      if (underlyingValue instanceof AnnotationsMatcher)
      {
         AnnotationsMatcher am = (AnnotationsMatcher)underlyingValue;
         am.addAnnotation(annotation);
      }
      else
      {
         log.info("Ignoring custom annotation, previous value is not AnnotationsMatcher: " + previousValue);
      }
      return previousValue;
   }
}