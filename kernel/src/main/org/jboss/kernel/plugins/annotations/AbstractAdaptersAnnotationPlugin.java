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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.reflect.spi.AnnotatedInfo;

/**
 * @param <T> info type
 * @param <C> annotation type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractAdaptersAnnotationPlugin<T extends AnnotatedInfo, C extends Annotation> extends AbstractAnnotationPlugin<T, C>
{
   protected List<Annotation2ValueMetaDataAdapter> adapters;

   protected AbstractAdaptersAnnotationPlugin(Class<C> annotation, Annotation2ValueMetaDataAdapter... adapters)
   {
      super(annotation);
      this.adapters = new ArrayList<Annotation2ValueMetaDataAdapter>();
      if (adapters == null || adapters.length == 0)
         throw new IllegalArgumentException("Annotation adapters are empty!");
      this.adapters.addAll(Arrays.asList(adapters));
   }

}
