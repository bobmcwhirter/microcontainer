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
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import org.jboss.beans.metadata.plugins.AbstractInstallMetaData;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.api.annotations.InstallMethod;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.dependency.spi.ControllerState;

/**
 * Install method annotation plugin.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class InstallMethodParameterAnnotationPlugin extends InstallationParameterAnnotationPlugin<InstallMethod>
{
   public InstallMethodParameterAnnotationPlugin(Set<Annotation2ValueMetaDataAdapter<? extends Annotation>> adapters)
   {
      super(InstallMethod.class, adapters);
   }

   protected List<InstallMetaData> getInstalls(BeanMetaData beanMetaData)
   {
      return beanMetaData.getInstalls();
   }

   protected AbstractInstallMetaData createParametrizedMetaData(MethodInfo info, InstallMethod annotation)
   {
      AbstractInstallMetaData install = new AbstractInstallMetaData();
      install.setMethodName(info.getName());
      if (isAttributePresent(annotation.whenRequired()))
         install.setState(new ControllerState(annotation.whenRequired()));
      if (isAttributePresent(annotation.dependantState()))
         install.setDependentState(new ControllerState(annotation.dependantState()));
      return install;
   }

   protected void setParameterizedMetaData(AbstractInstallMetaData parameterizedMetaData, BeanMetaData beanMetaData)
   {
      AbstractBeanMetaData abmd = checkIfNotAbstractBeanMetaDataSpecific(beanMetaData);
      List<InstallMetaData> installs = beanMetaData.getInstalls();
      if (installs == null)
      {
         installs = new ArrayList<InstallMetaData>();
         abmd.setInstalls(installs);
      }
      installs.add(parameterizedMetaData);
   }
}
