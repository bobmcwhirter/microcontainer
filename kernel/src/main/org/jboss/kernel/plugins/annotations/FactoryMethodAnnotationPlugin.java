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
import java.util.List;
import java.util.Collections;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.annotations.FactoryMethod;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.reflect.spi.ParameterInfo;

/**
 * Factory method annotation plugin.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class FactoryMethodAnnotationPlugin extends AbstractParameterAnnotationPlugin<MethodInfo, FactoryMethod, AbstractConstructorMetaData>
{
   public FactoryMethodAnnotationPlugin(Annotation2ValueMetaDataAdapter... adapters)
   {
      super(FactoryMethod.class, adapters);
   }

   protected boolean checkAnnotatedInfo(ElementType type)
   {
      return ElementType.METHOD == type;
   }

   protected boolean isMetaDataAlreadyPresent(MethodInfo info, FactoryMethod annotation, BeanMetaData beanMetaData)
   {
      if (info.isPublic() == false || info.isStatic() == false)
         throw new IllegalArgumentException("Method marked as @FactoryMethod must be public and static");
      return beanMetaData.getConstructor() != null;
   }

   protected ParameterInfo[] getParameters(MethodInfo info)
   {
      return info.getParameters();
   }

   protected List<? extends MetaDataVisitorNode> handleParameterlessInfo(MethodInfo info, FactoryMethod annotation, BeanMetaData beanMetaData)
   {
      AbstractConstructorMetaData constructorMetaData = createParametrizedMetaData(info, annotation, beanMetaData);
      setParameterizedMetaData(constructorMetaData, beanMetaData);
      return Collections.singletonList(constructorMetaData);
   }

   protected AbstractConstructorMetaData createParametrizedMetaData(MethodInfo info, FactoryMethod annotation, BeanMetaData beanMetaData)
   {
      AbstractConstructorMetaData constructor = new AbstractConstructorMetaData();
      constructor.setFactoryClass(beanMetaData.getBean());
      constructor.setFactoryMethod(info.getName());
      return constructor;
   }

   protected void setParameterizedMetaData(AbstractConstructorMetaData parameterizedMetaData, BeanMetaData beanMetaData)
   {
      AbstractBeanMetaData abmd = (AbstractBeanMetaData)beanMetaData;
      abmd.setConstructor(parameterizedMetaData);
   }

}
