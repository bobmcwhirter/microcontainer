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
import java.util.Collections;
import java.util.List;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.metadata.spi.MetaData;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.ParameterInfo;
import org.jboss.reflect.spi.TypeInfo;

/**
 * Constructor value annotation plugin.
 *
 * @param <C> annotation type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class ConstructorValueAnnotationPlugin<C extends Annotation> extends ClassAnnotationPlugin<C> implements Annotation2ValueMetaDataAdapter<C>
{
   protected ConstructorValueAnnotationPlugin(Class<C> annotation)
   {
      super(annotation);
   }

   protected boolean isMetaDataAlreadyPresent(ClassInfo info, C annotation, BeanMetaData beanMetaData)
   {
      return beanMetaData.getConstructor() != null;
   }

   protected List<? extends MetaDataVisitorNode> internalApplyAnnotation(ClassInfo info, MetaData retrieval, C annotation, KernelControllerContext context) throws Throwable
   {
      BeanMetaData bmd = context.getBeanMetaData();
      AbstractBeanMetaData beanMetaData = checkIfNotAbstractBeanMetaDataSpecific(bmd);
      AbstractConstructorMetaData constructor = new AbstractConstructorMetaData();
      constructor.setValue(createValueMetaData(annotation));
      beanMetaData.setConstructor(constructor);
      // reset, if present
      beanMetaData.setBean(null);
      context.setBeanInfo(null);
      return Collections.singletonList(constructor);
   }

   public ValueMetaData createValueMetaData(ParameterInfo parameterInfo, C annotation, ValueMetaData previousValue)
   {
      return createValueMetaData(parameterInfo.getParameterType(), annotation, previousValue);
   }

   /**
    * Create value metadata.
    *
    * @param type the type info
    * @param annotation the annotation
    * @param previousValue the previous value
    * @return value metadata instance
    */
   public ValueMetaData createValueMetaData(TypeInfo type, C annotation, ValueMetaData previousValue)
   {
      return createValueMetaData(type, annotation);
   }

   /**
    * Create value metadata.
    *
    * @param type the type info
    * @param annotation the annotation
    * @return value metadata instance
    */
   public ValueMetaData createValueMetaData(TypeInfo type, C annotation)
   {
      return createValueMetaData(annotation, null);
   }

   /**
    * Create value metadata.
    *
    * @param annotation the annotation
    * @param previousValue the previous value
    * @return value metadata instance
    */
   public ValueMetaData createValueMetaData(C annotation, ValueMetaData previousValue)
   {
      return createValueMetaData(annotation);
   }

   /**
    * Create the value metadata from annotation.
    *
    * @param annotation the annotation
    * @return new value metadata
    */
   protected abstract ValueMetaData createValueMetaData(C annotation);
}
