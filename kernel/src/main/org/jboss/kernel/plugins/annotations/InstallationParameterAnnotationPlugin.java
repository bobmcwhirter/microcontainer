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
import java.util.List;
import java.util.Collections;

import org.jboss.beans.metadata.plugins.AbstractInstallMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.reflect.spi.ParameterInfo;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.kernel.plugins.config.Configurator;

/**
 * Abstract installation annotation plugin.
 *
 * @param <C> annotation type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class InstallationParameterAnnotationPlugin<C extends Annotation> extends AbstractParameterAnnotationPlugin<MethodInfo, C, AbstractInstallMetaData>
{
   protected InstallationParameterAnnotationPlugin(Class<C> annotation, Annotation2ValueMetaDataAdapter... adapters)
   {
      super(annotation, adapters);
   }

   protected boolean checkAnnotatedInfo(ElementType type)
   {
      return ElementType.METHOD == type;
   }

   protected boolean isMetaDataAlreadyPresent(MethodInfo info, C annotation, BeanMetaData beanMetaData)
   {
      List<InstallMetaData> installs = getInstalls(beanMetaData);
      if (installs != null && installs.isEmpty() == false)
      {
         for(InstallMetaData install : installs)
         {
            // cannot compare when bean is set
            if (install.getBean() != null)
               continue;
            if (install.getMethodName().equals(info.getName()))
            {
               List<ParameterMetaData> parameters = install.getParameters();
               ParameterInfo[] parameterInfos = info.getParameters();
               if (parameters == null && parameterInfos != null && parameterInfos.length == 0)
                  return true;
               if (parameters != null && parameters.size() == 0 && parameterInfos == null)
                  return true;
               if (parameters != null && parameterInfos != null && parameters.size() == parameterInfos.length)
               {
                  int n = parameters.size();
                  String[] typeNames = new String[n];
                  TypeInfo[] typeInfos = new TypeInfo[n];
                  for(int i = 0; i < n; i++)
                  {
                     typeNames[i] = parameters.get(i).getType();
                     typeInfos[i] = parameterInfos[i].getParameterType();
                  }
                  return Configurator.equals(typeNames, typeInfos);
               }
            }
         }
      }
      return false;
   }

   /**
    * Get the list of InstallMetaData from bean metadata.
    *
    * @param beanMetaData the bean metadata
    * @return list of InstallMetaData
    */
   protected abstract List<InstallMetaData> getInstalls(BeanMetaData beanMetaData);

   protected ParameterInfo[] getParameters(MethodInfo info)
   {
      return info.getParameters();
   }

   protected List<? extends MetaDataVisitorNode> handleParameterlessInfo(MethodInfo info, C annotation, BeanMetaData beanMetaData)
   {
      AbstractInstallMetaData parametrizedMetaData = createParametrizedMetaData(info, annotation);
      setParameterizedMetaData(parametrizedMetaData, beanMetaData);
      return Collections.singletonList(parametrizedMetaData);
   }
}
