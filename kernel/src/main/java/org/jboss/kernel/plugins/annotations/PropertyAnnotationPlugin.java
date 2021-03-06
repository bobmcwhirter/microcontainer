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
import java.lang.annotation.ElementType;

import org.jboss.beans.info.spi.PropertyInfo;

/**
 * Abstract property annotation plugin.
 *
 * @param <C> annotation type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class PropertyAnnotationPlugin<C extends Annotation> extends InjectableMemberAnnotationPlugin<PropertyInfo, C>
   implements PropertyAware, Annotation2ValueMetaDataAdapter<C>
{
   protected PropertyAnnotationPlugin(Class<C> annotation)
   {
      super(annotation);
   }

   protected boolean isElementTypeSupported(ElementType type)
   {
      return ElementType.METHOD == type || ElementType.PARAMETER == type;
   }

   protected String getName(PropertyInfo info)
   {
      return info.getName();
   }
}
