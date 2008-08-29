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

import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.metadata.api.annotations.Inject;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.kernel.plugins.annotations.PropertyAnnotationPlugin;
import org.jboss.reflect.spi.ParameterInfo;

/**
 * Web beans kind of inject.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class WBInjectAnnotationPlugin extends PropertyAnnotationPlugin<Inject>
{
   public static final WBInjectAnnotationPlugin INSTANCE = new WBInjectAnnotationPlugin();

   protected WBInjectAnnotationPlugin()
   {
      super(Inject.class);
   }

   @SuppressWarnings("deprecation")
   protected ValueMetaData createValueMetaData(PropertyInfo info, Inject inject)
   {
      return WBInjectionResolver.createValueMetaData(info.getType().getType(), info.getUnderlyingAnnotations());
   }

   @SuppressWarnings("deprecation")
   public ValueMetaData createValueMetaData(ParameterInfo info, Inject inject, ValueMetaData previousValue)
   {
      return WBInjectionResolver.createValueMetaData(info.getParameterType().getType(), info.getUnderlyingAnnotations());
   }
}
