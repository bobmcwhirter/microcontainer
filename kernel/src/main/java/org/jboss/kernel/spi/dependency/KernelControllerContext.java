/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.kernel.spi.dependency;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.dispatch.LifecycleDispatchContext;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;

/**
 * Information about dependencies and state. Extension of {@link ControllerContext}
 * that allows access to extra information when running in the {@link Kernel}. When 
 * deploying a {@link BeanMetaData} into the {@link Kernel}, a 
 * <code>KernelControllerContext</code> is created by the Microcontainer, rather tha
 * vanilla {@link ControllerContext}. 
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface KernelControllerContext extends KernelRegistryEntry, LifecycleDispatchContext
{
   /**
    * Get the kernel this kernel controller context was deployed into.
    * 
    * @return the kernel
    */
   Kernel getKernel();

   /**
    * Get the BeanInfo for the bean represented by this kernel controller context
    * 
    * @return the bean info
    */
   BeanInfo getBeanInfo();

   /**
    * Set the bean info for the bean represented by this kernel controller context.
    * This method should only be called by the {@link KernelController}.
    * 
    * @param info the bean info
    */
   void setBeanInfo(BeanInfo info);

   /**
    * Get the metadata representing the bean to be installed by this kernel controller context.
    * 
    * @return the bean metadata
    */
   BeanMetaData getBeanMetaData();

   /**
    * Set the target. This will be the bean instance once the kernel controller context reaches the
    * {@link ControllerState#INSTANTIATED} state.
    * 
    * @param target the target
    */
   void setTarget(Object target);
}
