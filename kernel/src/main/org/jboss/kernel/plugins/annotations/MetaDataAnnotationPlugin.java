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
import java.util.Set;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.metadata.spi.MetaData;
import org.jboss.reflect.spi.AnnotatedInfo;

/**
 * Annotation plugin based on bean metadata.
 * 
 * @param <T> the annotated info
 * @param <C> the annotation type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public interface MetaDataAnnotationPlugin<T extends AnnotatedInfo, C extends Annotation>
{
   /**
    * Get the annotation class we are handling.
    *
    * @return annotation class
    */
   Class<C> getAnnotation();

   /**
    * Get all supported types.
    *
    * @return set of supported types
    */
   Set<ElementType> getSupportedTypes();

   /**
    * Apply annotations to bean metadata.
    *
    * @param info the annotated info we are checking
    * @param retrieval the metadata retrieval
    * @param beanMetaData the bean metadata
    * @throws Throwable for any error
    */
   void applyAnnotation(T info, MetaData retrieval, BeanMetaData beanMetaData) throws Throwable;
}
