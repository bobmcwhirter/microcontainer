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
import java.util.ArrayList;
import java.util.List;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractInstallMetaData;
import org.jboss.beans.metadata.api.annotations.UninstallMethod;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.reflect.spi.MethodInfo;

/**
 * Uninstall method annotation plugin.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class UninstallMethodParameterAnnotationPlugin extends InstallationParameterAnnotationPlugin<UninstallMethod>
{
   protected UninstallMethodParameterAnnotationPlugin(Annotation2ValueMetaDataAdapter<? extends Annotation>... adapters)
   {
      super(UninstallMethod.class, adapters);
   }

   protected List<InstallMetaData> getInstalls(BeanMetaData beanMetaData)
   {
      return beanMetaData.getUninstalls();
   }

   protected AbstractInstallMetaData createParametrizedMetaData(MethodInfo info, UninstallMethod annotation)
   {
      AbstractInstallMetaData uninstall = new AbstractInstallMetaData();
      uninstall.setMethodName(info.getName());
      if (isAttributePresent(annotation.whenRequired()))
         uninstall.setState(new ControllerState(annotation.whenRequired()));
      if (isAttributePresent(annotation.dependantState()))
         uninstall.setDependentState(new ControllerState(annotation.dependantState()));
      return uninstall;
   }

   protected void setParameterizedMetaData(AbstractInstallMetaData parameterizedMetaData, BeanMetaData beanMetaData)
   {
      AbstractBeanMetaData abmd = (AbstractBeanMetaData)beanMetaData;
      List<InstallMetaData> uninstalls = beanMetaData.getUninstalls();
      if (uninstalls == null)
      {
         uninstalls = new ArrayList<InstallMetaData>();
         abmd.setUninstalls(uninstalls);
      }
      uninstalls.add(parameterizedMetaData);
   }
}
