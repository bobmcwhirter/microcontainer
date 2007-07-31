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

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.annotations.Factory;
import org.jboss.beans.metadata.plugins.annotations.Value;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.reflect.spi.ClassInfo;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ClassFactoryAnnotationPlugin extends AbstractAdaptersAnnotationPlugin<ClassInfo, Factory>
{
   protected ClassFactoryAnnotationPlugin(Annotation2ValueMetaDataAdapter... adapters)
   {
      super(Factory.class, adapters);
   }

   protected boolean isElementTypeSupported(ElementType type)
   {
      return ElementType.TYPE == type;
   }

   protected boolean isMetaDataAlreadyPresent(ClassInfo info, Factory annotation, BeanMetaData beanMetaData)
   {
      return beanMetaData.getConstructor() != null;
   }

   protected void internalApplyAnnotation(ClassInfo info, Factory annotation, KernelControllerContext context) throws Throwable
   {
      AbstractBeanMetaData bean = (AbstractBeanMetaData)context.getBeanMetaData();
      AbstractConstructorMetaData constructor = new AbstractConstructorMetaData();
      if (isAttributePresent(annotation.factoryClass()))
      {
         constructor.setFactoryClass(annotation.factoryClass());
      }
      else
      {
         constructor.setFactory(ValueUtil.createValueMetaData(annotation.factory()));
      }
      constructor.setFactoryMethod(annotation.factoryMethod());
      if (annotation.parameters().length > 0)
      {
         List<ParameterMetaData> parameters = new ArrayList<ParameterMetaData>();
         for(Value parameter : annotation.parameters())
         {
            AbstractParameterMetaData apmd = new AbstractParameterMetaData(ValueUtil.createValueMetaData(parameter));
            if (isAttributePresent(parameter.type()))
               apmd.setType(parameter.type());
            parameters.add(apmd);
         }
         constructor.setParameters(parameters);
      }
      bean.setConstructor(constructor);
      executeVisit(context, constructor);
   }
}
