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

import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.kernel.plugins.annotations.MetaDataAnnotationPlugin;
import org.jboss.metadata.spi.MetaData;
import org.jboss.reflect.spi.AnnotatedInfo;

/**
 * Annotation plugin contract based on MeteDataVistor.
 *
 * @param <T> info type
 * @param <C> annotation type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public interface AnnotationPlugin<T extends AnnotatedInfo, C extends Annotation> extends MetaDataAnnotationPlugin<T, C>
{
   /**
    * Apply the check for annotation.
    *
    * @param info the info
    * @param retrieval metadata instance
    * @param visitor current context visitor
    * @throws Throwable for any error
    */
   void applyAnnotation(T info, MetaData retrieval, MetaDataVisitor visitor) throws Throwable;

   /**
    * Check if the annotation requires cleaning after itself.
    *
    * @param info the info
    * @param retrieval metadata instance
    * @param visitor current context visitor
    * @throws Throwable for any error
    */
   void cleanAnnotation(T info, MetaData retrieval, MetaDataVisitor visitor) throws Throwable;
}
