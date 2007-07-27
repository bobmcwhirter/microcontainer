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
import java.util.Arrays;
import java.util.List;

import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.builder.MutableParameterizedMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.metadata.spi.retrieval.AnnotationItem;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;
import org.jboss.metadata.spi.signature.MethodParametersSignature;
import org.jboss.metadata.spi.signature.Signature;
import org.jboss.reflect.spi.AnnotatedInfo;
import org.jboss.reflect.spi.ParameterInfo;
import org.jboss.reflect.spi.TypeInfo;

/**
 * @param <T> info type
 * @param <C> annotation type
 * @param <P> mutable parametrized type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractParameterAnnotationPlugin<T extends AnnotatedInfo, C extends Annotation, P extends MutableParameterizedMetaData> extends AbstractAnnotationPlugin<T, C>
{
   protected List<Annotation2ValueMetaDataAdapter> adapters;

   protected AbstractParameterAnnotationPlugin(Class<C> annotation, Annotation2ValueMetaDataAdapter... adapters)
   {
      super(annotation);
      this.adapters = new ArrayList<Annotation2ValueMetaDataAdapter>();
      if (adapters == null || adapters.length == 0)
         throw new IllegalArgumentException("Annotation adapters are empty!");
      this.adapters.addAll(Arrays.asList(adapters));
   }

   protected abstract boolean checkAnnotatedInfo(ElementType type);

   protected boolean isElementTypeSupported(ElementType type)
   {
      return ElementType.PARAMETER == type || checkAnnotatedInfo(type);
   }

   protected abstract ParameterInfo[] getParameters(T info);

   protected void handleParameterlessInfo(T info, C annotation, KernelControllerContext context)
   {
      handleParameterlessInfo(info, annotation, context.getBeanMetaData());
   }

   protected abstract void handleParameterlessInfo(T info, C annotation, BeanMetaData beanMetaData);

   protected P createParametrizedMetaData(T info, C annotation, KernelControllerContext context)
   {
      return createParametrizedMetaData(info, annotation, context.getBeanMetaData());
   }

   protected P createParametrizedMetaData(T info, C annotation, BeanMetaData beanMetaData)
   {
      return createParametrizedMetaData(info, annotation);
   }

   protected P createParametrizedMetaData(T info, C annotation)
   {
      return createParametrizedMetaData(info);
   }

   protected P createParametrizedMetaData(T info)
   {
      throw new IllegalArgumentException("Should implement one of createParameterizedMetaData methods!");
   }

   protected void setParameterizedMetaData(P parameterizedMetaData, KernelControllerContext context)
   {
      setParameterizedMetaData(parameterizedMetaData, context.getBeanMetaData());
   }

   protected abstract void setParameterizedMetaData(P parameterizedMetaData, BeanMetaData beanMetaData);

   @SuppressWarnings("unchecked")
   protected void internalApplyAnnotation(T info, MetaDataRetrieval retrieval, C annotation, KernelControllerContext context) throws Throwable
   {
      ParameterInfo[] parameters = getParameters(info);
      if (parameters == null || parameters.length == 0)
      {
         handleParameterlessInfo(info, annotation, context);
         return;
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
         MetaDataRetrieval mdr = retrieval.getComponentMetaDataRetrieval(pis);
         if (mdr != null)
         {
            ValueMetaData value = null;
            for(Annotation2ValueMetaDataAdapter adapter : adapters)
            {
               // todo - match multiple annotations?
               AnnotationItem item = mdr.retrieveAnnotation(adapter.getAnnotation());
               if (item != null)
               {
                  value = adapter.createValueMetaData(item.getAnnotation());
                  break;
               }
            }
            if (value == null)
               throw new IllegalArgumentException("No such Annotation2ValueMetaData adapter or no annotation on PropertyInfo: " + pi);
            pmds.add(new AbstractParameterMetaData(pi.getParameterType().getName(), value));
         }
         else
         {
            throw new IllegalArgumentException("MetaDataRetrieval for parameter must exist: " + pi);
         }
      }
      P parameterizedMetaData = createParametrizedMetaData(info, annotation, context);
      setParameterizedMetaData(parameterizedMetaData, context);
      parameterizedMetaData.setParameters(pmds);
      if (parameterizedMetaData instanceof MetaDataVisitorNode == false)
         throw new IllegalArgumentException("ParameterizedMetaData not MetaDataVisitor: " + parameterizedMetaData);
      executeVisit(context, (MetaDataVisitorNode)parameterizedMetaData);
   }

}
