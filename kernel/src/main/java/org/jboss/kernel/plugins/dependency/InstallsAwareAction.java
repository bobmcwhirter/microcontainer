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
package org.jboss.kernel.plugins.dependency;

import java.util.List;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.dispatch.InvokeDispatchContext;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.dispatch.InvokeDispatchHelper;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * Installs/Uninstalls aware action.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class InstallsAwareAction extends KernelControllerContextAction
{
   /**
    * Get the action's state.
    *
    * @return get the action's state
    */
   protected abstract ControllerState getState();

   @Override
   public void installAction(KernelControllerContext context) throws Throwable
   {
      installActionInternal(context);
      doInstalls(context);
      setKernelControllerContext(context);
   }

   /**
    * Execute the installs.
    *
    * @param context the context
    * @throws Throwable for any error
    */
   protected void doInstalls(KernelControllerContext context) throws Throwable
   {
      KernelController controller = (KernelController)context.getController();
      Kernel kernel = controller.getKernel();
      KernelConfigurator configurator = kernel.getConfigurator();
      BeanMetaData metaData = context.getBeanMetaData();

      List<InstallMetaData> installs = metaData.getInstalls();
      if (installs != null && installs.isEmpty() == false)
      {
         int index = 0;
         try
         {
            int size = installs.size();
            for (; index < size; index++)
            {
               InstallMetaData install = installs.get(index);
               if (getState().equals(install.getState()))
               {
                  ControllerContext target = context;
                  if (install.getBean() != null)
                     target = controller.getContext(install.getBean(), install.getDependentState());
                  if (target instanceof InvokeDispatchContext)
                  {
                     ClassLoader previous = SecurityActions.setContextClassLoader(context);
                     try
                     {
                        InvokeDispatchHelper.invoke(
                              configurator,
                              target.getTarget(),
                              (InvokeDispatchContext)target,
                              install.getMethodName(),
                              install.getParameters()
                        );
                     }
                     finally
                     {
                        SecurityActions.resetContextClassLoader(previous);
                     }
                  }
                  else
                  {
                     throw new IllegalArgumentException("Cannot install, context " + target + " does not implement InvokeDispatchContext");
                  }
               }
            }
         }
         catch (Throwable t)
         {
            considerUninstalls(context, index);
            undoInstallAction(context);
            throw t;
         }
      }
   }

   /**
    * Consider the uninstalls.
    *
    * This method is here to be able to override
    * the behavior after installs failed.
    * e.g. perhaps only running uninstalls from the index.
    *
    * By default we run all uninstalls in the case
    * at least one install failed.
    *
    * @param context the context
    * @param index current installs index
    */
   protected void considerUninstalls(KernelControllerContext context, int index)
   {
      if (index > 0)
         doUninstalls(context);
   }

   /**
    * Undo the steps from install action
    * in the case of failed installs.
    * Usually this is what uninstallActionInternal does.
    *
    * @param context the context
    */
   protected void undoInstallAction(KernelControllerContext context)
   {
      uninstallActionInternal(context);
   }

   @Override
   public void uninstallAction(KernelControllerContext context)
   {
      unsetKernelControllerContext(context);
      doUninstalls(context);
      uninstallActionInternal(context);
   }

   /**
    * Execute uninstalls
    *
    * @param context the context
    */
   protected void doUninstalls(KernelControllerContext context)
   {
      BeanMetaData beanMetaData = context.getBeanMetaData();
      List<InstallMetaData> uninstalls = beanMetaData.getUninstalls();
      int size = uninstalls != null ? uninstalls.size() : 0;
      doUninstalls(context, size - 1);
   }

   /**
    * Execute uninstalls from the index.
    *
    * Leaving this method here in case
    * someone wants to use 'symetric' approach
    * to uninstalling installs.
    *
    * @param context the context
    * @param index   the current index
    */
   protected void doUninstalls(KernelControllerContext context, int index)
   {
      KernelController controller = (KernelController) context.getController();
      Kernel kernel = controller.getKernel();
      KernelConfigurator configurator = kernel.getConfigurator();
      BeanMetaData metaData = context.getBeanMetaData();

      List<InstallMetaData> uninstalls = metaData.getUninstalls();
      if (uninstalls != null && uninstalls.isEmpty() == false)
      {
         for (int i = index; i >= 0; --i)
         {
            InstallMetaData uninstall = uninstalls.get(i);
            if (getState().equals(uninstall.getState()))
            {
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
                  ClassLoader previous = null;
                  try
                  {
                     previous = SecurityActions.setContextClassLoader(context);
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
                  finally
                  {
                     if (previous != null)
                        SecurityActions.resetContextClassLoader(previous);
                  }
               }
               else
               {
                  log.warn("Cannot uninstall, context " + target + " does not implement InvokeDispatchContext for " + uninstall.getBean());
               }
            }
         }
      }
   }
}
