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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.builder.MutableParameterizedMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.signature.MethodParametersSignature;
import org.jboss.metadata.spi.signature.Signature;
import org.jboss.reflect.spi.AnnotatedInfo;
import org.jboss.reflect.spi.ParameterInfo;
import org.jboss.reflect.spi.TypeInfo;

/**
 * Annotation plugin for handling annotations that take parameters.
 *
 * @param <T> info type
 * @param <C> annotation type
 * @param <P> mutable parametrized type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractParameterAnnotationPlugin<T extends AnnotatedInfo, C extends Annotation, P extends MutableParameterizedMetaData> extends AbstractAdaptersAnnotationPlugin<T, C>
{
   protected AbstractParameterAnnotationPlugin(Class<C> annotation, Annotation2ValueMetaDataAdapter<? extends Annotation>... adapters)
   {
      super(annotation, adapters);
   }

   /**
    * Check additional element type -
    * apart from Parameter element type.
    *
    * @param type the type
    * @return true if additional type is supported
    */
   protected abstract boolean checkAnnotatedInfo(ElementType type);

   protected boolean isElementTypeSupported(ElementType type)
   {
      return ElementType.PARAMETER == type || checkAnnotatedInfo(type);
   }

   /**
    * Get the parameters infos from type.
    *
    * @param info the type
    * @return array of parameter info
    */
   protected abstract ParameterInfo[] getParameters(T info);

   /**
    * Handle info which has zero parameters.
    *
    * @param info the info
    * @param annotation the annotation
    * @param context the context
    * @return list of added meta data visitor nodes
    */
   protected List<? extends MetaDataVisitorNode> handleParameterlessInfo(T info, C annotation, KernelControllerContext context)
   {
      return handleParameterlessInfo(info, annotation, context.getBeanMetaData());
   }

   /**
    * Handle info which has zero parameters.
    *
    * @param info the info
    * @param annotation the annotation
    * @param beanMetaData the bean metadata
    * @return list of added meta data visitor nodes
    */
   protected abstract List<? extends MetaDataVisitorNode> handleParameterlessInfo(T info, C annotation, BeanMetaData beanMetaData);

   /**
    * Create new Parametrized metadata.
    *
    * @param info the info
    * @param annotation the annotation
    * @param context the context
    * @return new ParameterizedMetaData instance
    */
   protected P createParametrizedMetaData(T info, C annotation, KernelControllerContext context)
   {
      return createParametrizedMetaData(info, annotation, context.getBeanMetaData());
   }

   /**
    * Create new Parametrized metadata.
    *
    * @param info the info
    * @param annotation the annotation
    * @param beanMetaData the bean metadata
    * @return new ParameterizedMetaData instance
    */
   protected P createParametrizedMetaData(T info, C annotation, BeanMetaData beanMetaData)
   {
      return createParametrizedMetaData(info, annotation);
   }

   /**
    * Create new Parametrized metadata.
    *
    * @param info the info
    * @param annotation the annotation
    * @return new ParameterizedMetaData instance
    */
   protected P createParametrizedMetaData(T info, C annotation)
   {
      return createParametrizedMetaData(info);
   }

   /**
    * Create new Parametrized metadata.
    *
    * @param info the info
    * @return new ParameterizedMetaData instance
    */
   protected P createParametrizedMetaData(T info)
   {
      throw new IllegalArgumentException("Should implement one of createParameterizedMetaData methods!");
   }

   /**
    * Set the ParameterizedMetaData instance.
    *
    * @param parameterizedMetaData the parameterized metadata
    * @param context the context
    */
   protected void setParameterizedMetaData(P parameterizedMetaData, KernelControllerContext context)
   {
      setParameterizedMetaData(parameterizedMetaData, context.getBeanMetaData());
   }

   /**
    * Set the ParameterizedMetaData instance.
    * @param parameterizedMetaData the parameterized metadata
    * @param beanMetaData the bean metadata
    */
   protected abstract void setParameterizedMetaData(P parameterizedMetaData, BeanMetaData beanMetaData);

   @SuppressWarnings("unchecked")
   protected List<? extends MetaDataVisitorNode> internalApplyAnnotation(T info, MetaData retrieval, C annotation, KernelControllerContext context) throws Throwable
   {
      boolean trace = log.isTraceEnabled();

      ParameterInfo[] parameters = getParameters(info);
      if (parameters == null || parameters.length == 0)
      {
         if (trace)
            log.trace("Info " + info + " has zero parameters.");
         return handleParameterlessInfo(info, annotation, context);
      }

      TypeInfo[] typeInfos = new TypeInfo[parameters.length];
      for(int i=0; i < parameters.length; i++)
         typeInfos[i] = parameters[i].getParameterType();

      List<ParameterMetaData> pmds = new ArrayList<ParameterMetaData>();
      for(int i=0; i < parameters.length; i++)
      {
         ParameterInfo pi = parameters[i];
         Signature pis = new MethodParametersSignature(
               pi.getName(),
               Configurator.getParameterTypes(log.isTraceEnabled(), typeInfos),
               i
         );
         MetaData mdr = retrieval.getComponentMetaData(pis);
         if (mdr != null)
         {
            ValueMetaData value = null;
            for(Annotation2ValueMetaDataAdapter adapter : adapters)
            {
               // todo - match multiple annotations?
               Annotation adapterAnnotation = mdr.getAnnotation(adapter.getAnnotation());
               if (adapterAnnotation != null)
               {
                  value = adapter.createValueMetaData(adapterAnnotation);
                  break;
               }
            }
            if (value == null)
               throw new IllegalArgumentException("No such Annotation2ValueMetaData adapter or no annotation on ParameterInfo: " + pi);
            if (trace)
               log.trace("Adding new ParameterMetaData for annotation: " + value);
            pmds.add(new AbstractParameterMetaData(pi.getParameterType().getName(), value));
         }
         else
         {
            throw new IllegalArgumentException("MetaData for parameter must exist: " + pi);
         }
      }
      P parameterizedMetaData = createParametrizedMetaData(info, annotation, context);
      setParameterizedMetaData(parameterizedMetaData, context);
      parameterizedMetaData.setParameters(pmds);
      if (parameterizedMetaData instanceof MetaDataVisitorNode == false)
         throw new IllegalArgumentException("ParameterizedMetaData not MetaDataVisitor: " + parameterizedMetaData);
      return Collections.singletonList((MetaDataVisitorNode)parameterizedMetaData);
   }

}
