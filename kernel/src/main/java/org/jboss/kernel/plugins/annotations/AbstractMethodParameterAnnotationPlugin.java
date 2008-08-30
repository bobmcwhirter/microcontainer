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
import java.util.Set;

import org.jboss.beans.metadata.plugins.builder.MutableParameterizedMetaData;
import org.jboss.metadata.spi.signature.MethodParametersSignature;
import org.jboss.metadata.spi.signature.Signature;
import org.jboss.reflect.spi.MethodInfo;

/**
 * Annotation plugin for handling annotations that take parameters in methods.
 *
 * @param <C> annotation type
 * @param <P> mutable parametrized type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractMethodParameterAnnotationPlugin<C extends Annotation, P extends MutableParameterizedMetaData> extends AbstractParameterAnnotationPlugin<MethodInfo, C, P>
{
   protected AbstractMethodParameterAnnotationPlugin(Class<C> annotation, Set<Annotation2ValueMetaDataAdapter<? extends Annotation>> adapters)
   {
      super(annotation, adapters);
   }

   protected Signature createParametersSignature(String name, String[] parameters, int index)
   {
      return new MethodParametersSignature(name, parameters, index);
   }
}