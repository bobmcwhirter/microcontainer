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
import java.util.Set;

import org.jboss.beans.metadata.api.annotations.Install;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractCallbackMetaData;
import org.jboss.beans.metadata.plugins.InstallCallbackMetaData;
import org.jboss.beans.metadata.spi.CallbackMetaData;
import org.jboss.dependency.spi.CallbackItem;
import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.reflect.spi.FieldInfo;

/**
 * Install field callback annotation plugin.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class InstallFieldCallbackAnnotationPlugin extends CallbackAnnotationPlugin<FieldInfo, Install>
{
   public static final InstallFieldCallbackAnnotationPlugin INSTANCE = new InstallFieldCallbackAnnotationPlugin();

   protected InstallFieldCallbackAnnotationPlugin()
   {
      super(Install.class);
   }

   protected boolean isElementTypeSupported(ElementType type)
   {
      return ElementType.FIELD == type;
   }

   protected boolean isEqual(FieldInfo info, CallbackItem<?> ci)
   {
      return info.getName().equals(ci.getAttributeName());
   }

   protected void applyInfo(AbstractCallbackMetaData callback, FieldInfo info)
   {
      callback.setProperty(info.getName());
   }

   protected Set<CallbackItem<?>> getCallbacks(DependencyInfo dependency)
   {
      return dependency.getInstallItems();
   }

   protected AbstractCallbackMetaData createCallback(FieldInfo info, Install annotation)
   {
      InstallCallbackMetaData callback = new InstallCallbackMetaData();
      callback.setState(new ControllerState(annotation.whenRequired()));
      callback.setDependentState(new ControllerState(annotation.dependentState()));
      if (isAttributePresent(annotation.cardinality()))
         callback.setCardinality(Cardinality.fromString(annotation.cardinality()));
      applyInfo(callback, info);
      return callback;
   }

   protected List<CallbackMetaData> getCallbacks(AbstractBeanMetaData beanMetaData)
   {
      List<CallbackMetaData> callbacks = beanMetaData.getInstallCallbacks();
      if (callbacks == null)
      {
         callbacks = new ArrayList<CallbackMetaData>();
         beanMetaData.setInstallCallbacks(callbacks);
      }
      return callbacks;
   }
}
