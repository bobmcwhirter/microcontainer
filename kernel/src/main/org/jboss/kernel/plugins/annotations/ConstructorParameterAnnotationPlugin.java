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

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.annotations.Constructor;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.reflect.spi.ConstructorInfo;
import org.jboss.reflect.spi.ParameterInfo;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ConstructorParameterAnnotationPlugin extends AbstractParameterAnnotationPlugin<ConstructorInfo, Constructor, AbstractConstructorMetaData>
{
   protected ConstructorParameterAnnotationPlugin(Annotation2ValueMetaDataAdapter... adapters)
   {
      super(Constructor.class, adapters);
   }

   protected boolean checkAnnotatedInfo(ElementType type)
   {
      return ElementType.CONSTRUCTOR == type;
   }

   protected boolean isMetaDataAlreadyPresent(ConstructorInfo info, Constructor annotation, BeanMetaData beanMetaData)
   {
      return beanMetaData.getConstructor() != null;
   }

   protected ParameterInfo[] getParameters(ConstructorInfo info)
   {
      return info.getParameters();
   }

   protected void handleParameterlessInfo(ConstructorInfo info, Constructor annotation, BeanMetaData beanMetaData)
   {
      // do nothing, default will be used
   }

   protected AbstractConstructorMetaData createParametrizedMetaData(ConstructorInfo info)
   {
      return new AbstractConstructorMetaData();
   }

   protected void setParameterizedMetaData(AbstractConstructorMetaData parameterizedMetaData, BeanMetaData beanMetaData)
   {
      AbstractBeanMetaData abmd = (AbstractBeanMetaData)beanMetaData;
      abmd.setConstructor(parameterizedMetaData);
   }
}
