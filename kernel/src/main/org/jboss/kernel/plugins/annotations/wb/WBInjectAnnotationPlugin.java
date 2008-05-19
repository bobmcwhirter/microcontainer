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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.metadata.api.annotations.Inject;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.kernel.plugins.annotations.PropertyAnnotationPlugin;

/**
 * Web beans kind of inject.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class WBInjectAnnotationPlugin extends PropertyAnnotationPlugin<Inject>
{
   private Set<Class<? extends Annotation>> excludedAnnotations = new HashSet<Class<? extends Annotation>>();

   public WBInjectAnnotationPlugin()
   {
      super(Inject.class);
      addExcludedAnnotation(Inject.class);
   }

   @SuppressWarnings("deprecation")
   protected ValueMetaData createValueMetaData(PropertyInfo info, Inject inject)
   {
      List<Annotation> annotations = new ArrayList<Annotation>();
      Annotation[] underlyingAnnotations = info.getUnderlyingAnnotations();
      for (Annotation annotation : underlyingAnnotations)
      {
         if (excludedAnnotations.contains(annotation.annotationType()) == false)
            annotations.add(annotation);
      }
      return new WBInjectionValueMetaData(info.getType().getType(), annotations.toArray(new Annotation[annotations.size()]));
   }

   /**
    * Add excluded annotations.
    *
    * @param annotationClass the excluded annotation's class.
    */
   public void addExcludedAnnotation(Class<? extends Annotation> annotationClass)
   {
      excludedAnnotations.add(annotationClass);
   }
}
