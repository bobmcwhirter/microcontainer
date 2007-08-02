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

import org.jboss.beans.metadata.plugins.AbstractInstallMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.annotations.ExternalInstall;
import org.jboss.beans.metadata.plugins.annotations.Value;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.reflect.spi.ClassInfo;

/**
 * @param <C> annotation type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class ExternalInstallationAnnotationPlugin<C extends Annotation> extends ClassAnnotationPlugin<C>
{
   protected ExternalInstallationAnnotationPlugin(Class<C> annotation)
   {
      super(annotation);
   }

   protected abstract List<InstallMetaData> getExistingInstallMetaData(BeanMetaData beanMetaData);

   protected abstract ExternalInstall[] getExternalInstalls(C annotation);

   protected List<InstallMetaData> getInstallMetaData(C annotation)
   {
      List<InstallMetaData> installs = new ArrayList<InstallMetaData>();
      for(ExternalInstall install : getExternalInstalls(annotation))
      {
         installs.add(createInstallMetaData(install));
      }
      return installs;
   }

   protected InstallMetaData createInstallMetaData(ExternalInstall install)
   {
      AbstractInstallMetaData installMetaData = new AbstractInstallMetaData();
      installMetaData.setBean(install.bean());
      installMetaData.setMethodName(install.method());
      if (isAttributePresent(install.dependantState()))
         installMetaData.setDependentState(new ControllerState(install.dependantState()));
      for (Value value : install.parameters())
      {
         List<ParameterMetaData> parameters = installMetaData.getParameters();
         if (parameters == null)
         {
            parameters = new ArrayList<ParameterMetaData>();
            installMetaData.setParameters(parameters);
         }
         AbstractParameterMetaData parameter = new AbstractParameterMetaData(ValueUtil.createValueMetaData(value));
         if (isAttributePresent(value.type()))
            parameter.setType(value.type());
         parameters.add(parameter);
      }
      return installMetaData;
   }

   protected void internalApplyAnnotation(ClassInfo info, C annotation, KernelControllerContext context) throws Throwable
   {
      BeanMetaData beanMetaData = context.getBeanMetaData();
      List<InstallMetaData> existing = getExistingInstallMetaData(beanMetaData);
      if (existing == null)
         throw new IllegalArgumentException("Must set empty Set to installs/uninstalls!");

      List<InstallMetaData> installs = getInstallMetaData(annotation);
      if (installs != null && installs.isEmpty() == false)
      {
         for(InstallMetaData install : installs)
         {
            boolean doAdd = true;
            for(InstallMetaData existingInstall : existing)
            {
               if (isDifferent(install, existingInstall) == false)
               {
                  doAdd = false;
                  break;
               }
            }
            if (doAdd)
            {
               existing.add(install);
               executeVisit(context, install);
            }
         }
      }
   }

   protected static boolean isDifferent(InstallMetaData first, InstallMetaData second)
   {
      if (notEqual(first.getBean(), second.getBean()))
         return true;

      if (notEqual(first.getMethodName(), second.getMethodName()))
         return true;

      List<ParameterMetaData> fstParameters = first.getParameters();
      List<ParameterMetaData> sndParameters = second.getParameters();
      int fstSize = fstParameters != null ? fstParameters.size() : 0;
      int sndSize = sndParameters != null ? sndParameters.size() : 0;
      if (fstSize != sndSize)
         return true;

      for(int i = 0; i < fstSize; i++)
      {
         ParameterMetaData fstParameter = fstParameters.get(i);
         ParameterMetaData sndParameter = sndParameters.get(i);
         // types
         if (notEqual(fstParameter.getType(), sndParameter.getType()))
            return true;
         // values
         ValueMetaData fstValue = fstParameter.getValue();
         ValueMetaData sndValue = sndParameter.getValue();
         Class fstClass = fstValue != null ? fstValue.getClass() : null;
         Class sndClass = sndValue != null ? sndValue.getClass() : null;
         // class guess
         if (notEqual(fstClass, sndClass))
            return true;

         Object fstObject = fstValue != null ? fstValue.getUnderlyingValue() : null;
         Object sndObject = sndValue != null ? sndValue.getUnderlyingValue() : null;
         // underlying value guess - only on both being non-null
         // e.g. since those who already passed have their underlying value set,
         // those who didn't, don't have it, but they are the 'same' install
         // see ThisValueMetaData
         if ((fstObject != null && sndObject != null) && notEqual(fstObject, sndObject))
            return true;
      }

      return false;
   }

}
