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
package org.jboss.kernel.plugins.dependency;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.InstallKernelControllerContextAware;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.dependency.KernelControllerContextAware;

/**
 * InstallAction.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
@SuppressWarnings("deprecation")
public class InstallAction extends InstallsAwareAction
{
   protected void installActionInternal(KernelControllerContext context) throws Throwable
   {
      KernelController controller = (KernelController) context.getController();
      Kernel kernel = controller.getKernel();
      org.jboss.kernel.spi.registry.KernelRegistry registry = kernel.getRegistry();

      BeanMetaData metaData = context.getBeanMetaData();
      Object name = metaData.getName();
      registry.registerEntry(name, context);
      try
      {
         controller.addSupplies(context);
      }
      catch (Throwable t)
      {
         try
         {
            registry.unregisterEntry(name);
         }
         catch (Throwable x)
         {
            log.warn("Ignoring error reversing install, throwing original error " + name, x);
         }
         throw t;
      }
   }

   protected Class<? extends KernelControllerContextAware> getActionAwareInterface()
   {
      return InstallKernelControllerContextAware.class;
   }

   protected void uninstallActionInternal(KernelControllerContext context)
   {
      KernelController controller = (KernelController) context.getController();
      Kernel kernel = controller.getKernel();
      org.jboss.kernel.spi.registry.KernelRegistry registry = kernel.getRegistry();
      Object name = context.getName();

      try
      {
         controller.removeSupplies(context);
      }
      catch (Throwable t)
      {
         log.warn("Ignoring removing supplies at uninstall " + name, t);
      }

      try
      {
         registry.unregisterEntry(name);
      }
      catch (Throwable t)
      {
         log.warn("Ignoring unregistered entry at uninstall " + name, t);
      }
   }

   protected ControllerState getState()
   {
      return ControllerState.INSTALLED;
   }
}