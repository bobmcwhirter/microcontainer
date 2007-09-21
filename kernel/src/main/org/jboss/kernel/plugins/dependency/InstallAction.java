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

import java.util.List;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.dispatch.InvokeDispatchContext;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.dispatch.InvokeDispatchHelper;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.InstallKernelControllerContextAware;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.dependency.KernelControllerContextAware;
import org.jboss.kernel.spi.registry.KernelRegistry;

/**
 * InstallAction.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class InstallAction extends KernelControllerContextAction
{
   protected void installActionInternal(KernelControllerContext context) throws Throwable
   {
      KernelController controller = (KernelController) context.getController();
      Kernel kernel = controller.getKernel();
      KernelRegistry registry = kernel.getRegistry();
      KernelConfigurator configurator = kernel.getConfigurator();

      BeanMetaData metaData = context.getBeanMetaData();
      Object name = metaData.getName();
      registry.registerEntry(name, context);
      controller.addSupplies(context);

      List<InstallMetaData> installs = metaData.getInstalls();
      if (installs != null)
      {
         for (InstallMetaData install : installs)
         {
            ControllerContext target = context;
            if (install.getBean() != null)
               target = controller.getContext(install.getBean(), install.getDependentState());
            if (target instanceof InvokeDispatchContext)
            {
               InvokeDispatchHelper.invoke(
                     configurator,
                     target.getTarget(),
                     (InvokeDispatchContext)target,
                     install.getMethodName(),
                     install.getParameters()
               );
            }
            else
            {
               throw new IllegalArgumentException("Cannot install, context " + target + " does not implement InvokeDispatchContext");
            }
         }
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
      KernelRegistry registry = kernel.getRegistry();
      KernelConfigurator configurator = kernel.getConfigurator();
      BeanMetaData metaData = context.getBeanMetaData();
      Object name = metaData.getName();

      List<InstallMetaData> uninstalls = metaData.getUninstalls();
      if (uninstalls != null)
      {
         for (int i = uninstalls.size()-1; i >= 0; --i)
         {
            InstallMetaData uninstall = uninstalls.get(i);
            ControllerContext target = context;
            if (uninstall.getBean() != null)
            {
               target = controller.getContext(uninstall.getBean(), uninstall.getDependentState());
               if (target == null)
               {
                  log.warn("Ignoring uninstall action on target in incorrect state " + uninstall.getBean());
                  continue;
               }
            }
            if (target instanceof InvokeDispatchContext)
            {
               try
               {
                  InvokeDispatchHelper.invoke(
                        configurator,
                        target.getTarget(), 
                        (InvokeDispatchContext)target,
                        uninstall.getMethodName(),
                        uninstall.getParameters()
                  );
               }
               catch (Throwable t)
               {
                  log.warn("Ignoring uninstall action on target " + uninstall, t);
               }
            }
            else
            {
               throw new IllegalArgumentException("Cannot uninstall, context " + target + " does not implement InvokeDispatchContext");
            }
         }
      }

      try
      {
         controller.removeSupplies(context);
         registry.unregisterEntry(name);
      }
      catch (Throwable t)
      {
         log.warn("Ignoring unregistered entry at uninstall " + name);
      }
   }

}