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

import org.jboss.beans.metadata.plugins.annotations.Inject;
import org.jboss.beans.metadata.plugins.annotations.FromContext;
import org.jboss.beans.metadata.plugins.AbstractInjectionValueMetaData;
import org.jboss.beans.metadata.plugins.InjectionOption;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.AutowireType;
import org.jboss.dependency.spi.ControllerState;

/**
 * Inject value annotation plugin.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class InjectAnnotationPlugin extends PropertyAnnotationPlugin<Inject>
{
   static InjectAnnotationPlugin INSTANCE = new InjectAnnotationPlugin();

   public InjectAnnotationPlugin()
   {
      super(Inject.class);
   }

   public ValueMetaData createValueMetaData(Inject annotation)
   {
      AbstractInjectionValueMetaData injection = new AbstractInjectionValueMetaData();
      if (isAttributePresent(annotation.bean()))
         injection.setValue(annotation.bean());
      if (isAttributePresent(annotation.property()))
         injection.setProperty(annotation.property());
      injection.setDependentState(new ControllerState(annotation.dependentState()));
      if (isAttributePresent(annotation.whenRequired()))
         injection.setWhenRequiredState(new ControllerState(annotation.whenRequired()));
      injection.setInjectionOption(InjectionOption.getInstance(annotation.option().toString()));
      injection.setInjectionType(AutowireType.getInstance(annotation.type().toString()));
      if (FromContext.NONE.equals(annotation.fromContext()) == false)
         injection.setFromContext(org.jboss.beans.metadata.plugins.FromContext.getInstance(annotation.fromContext().toString()));
      return injection;
   }
}
