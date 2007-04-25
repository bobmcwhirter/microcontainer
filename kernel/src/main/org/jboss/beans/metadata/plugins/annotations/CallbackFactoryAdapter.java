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
package org.jboss.beans.metadata.plugins.annotations;

import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.classadapter.spi.DependencyBuilderListItem;
import org.jboss.kernel.plugins.dependency.AttributeInfo;
import org.jboss.kernel.plugins.dependency.MethodAttributeInfo;
import org.jboss.kernel.plugins.dependency.PropertyAttributeInfo;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.reflect.spi.MethodInfo;

/**
 * Create dependency list item from Callback info holder - via adapter.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class CallbackFactoryAdapter
{
   protected DependencyBuilderListItem<KernelControllerContext> getDependency(CallbackInfo info, MethodInfo method)
   {
      return getDependency(info, new MethodAttributeInfo(method));
   }

   protected DependencyBuilderListItem<KernelControllerContext> getDependency(CallbackInfo info, PropertyInfo property)
   {
      return getDependency(info, new PropertyAttributeInfo(property));
   }

   protected DependencyBuilderListItem<KernelControllerContext> getDependency(CallbackInfo info, AttributeInfo attribute)
   {
      if (info.isInstallPhase())
         return new InstallCallbackDependencyBuilderListItem(info, attribute);
      else
         return new UninstallCallbackDependencyBuilderListItem(info, attribute);
   }
}
