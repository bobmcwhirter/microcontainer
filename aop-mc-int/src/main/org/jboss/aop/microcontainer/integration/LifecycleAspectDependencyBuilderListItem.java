/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.aop.microcontainer.integration;

import java.util.ArrayList;
import java.util.List;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.dependency.plugins.AbstractLifecycleCallbackItem;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
class LifecycleAspectDependencyBuilderListItem extends AspectDependencyBuilderListItem
{
   ControllerState state;
   String installMethod;
   String uninstallMethod;
   
   LifecycleAspectDependencyBuilderListItem(String beanName, ControllerState state, String installMethod, String uninstallMethod)
   {
      super(beanName);
      this.state = state;
      this.installMethod = installMethod;
      this.uninstallMethod = uninstallMethod;
   }

   public boolean equals(Object o)
   {
      if (super.equals(o))
      {
         if (o instanceof LifecycleAspectDependencyBuilderListItem)
         {
            return state.equals(((LifecycleAspectDependencyBuilderListItem)o).state);
         }
      }
      return false;
   }
   
   public int hashCode()
   {
      return dependencyName.hashCode();
   }

   public void addDependency(KernelControllerContext context)
   {
      AbstractLifecycleCallbackItem callback = new AbstractLifecycleCallbackItem(dependencyName, state, ControllerState.INSTALLED, installMethod, uninstallMethod);
      DependencyInfo di = context.getDependencyInfo();
      di.addLifecycleCallback(callback);

      super.addDependency(context);
   }
}
