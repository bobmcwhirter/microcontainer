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

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.joinpoint.spi.MethodJoinpoint;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.registry.KernelRegistry;

/**
 * InstallAction.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class InstallAction extends KernelControllerContextAction
{
   public void installAction(KernelControllerContext context) throws Throwable
   {
      KernelController controller = (KernelController) context.getController();
      Kernel kernel = controller.getKernel();
      KernelRegistry registry = kernel.getRegistry();

      BeanMetaData metaData = context.getBeanMetaData();
      Object name = metaData.getName();
      registry.registerEntry(name, context);
      controller.addSupplies(context);
      
      List installs = metaData.getInstalls();
      if (installs != null)
      {
         for (int i = 0; i < installs.size(); ++i)
         {
            InstallMetaData install = (InstallMetaData) installs.get(i);
            KernelControllerContext target = context;
            if (install.getBean() != null)
               target = (KernelControllerContext) controller.getContext(install.getBean(), install.getDependentState());
            dispatch(target, install.getMethodName(), install.getParameters());
         }
      }
   }
   
   public void uninstallAction(KernelControllerContext context)
   {
      KernelController controller = (KernelController) context.getController();
      Kernel kernel = controller.getKernel();
      KernelRegistry registry = kernel.getRegistry();
      BeanMetaData metaData = context.getBeanMetaData();
      Object name = metaData.getName();
      
      List uninstalls = metaData.getUninstalls();
      if (uninstalls != null)
      {
         for (int i = uninstalls.size()-1; i >= 0; --i)
         {
            InstallMetaData uninstall = (InstallMetaData) uninstalls.get(i);
            KernelControllerContext target = context;
            if (uninstall.getBean() != null)
            {
               target = (KernelControllerContext) controller.getContext(uninstall.getBean(), uninstall.getDependentState());
               if (target == null)
               {
                  log.warn("Ignoring uninstall action on target in incorrect state " + uninstall.getBean());
                  continue;
               }
            }
            try
            {
               dispatch(target, uninstall.getMethodName(), uninstall.getParameters());
            }
            catch (Throwable t)
            {
               log.warn("Ignoring uninstall action on target " + uninstall, t);
               continue;
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
   
   protected void dispatch(KernelControllerContext context, String method, List<ParameterMetaData> parameters) throws Throwable
   {
      KernelController controller = (KernelController) context.getController();
      KernelConfigurator configurator = controller.getKernel().getConfigurator();
      BeanInfo info = context.getBeanInfo();
      BeanMetaData metaData = context.getBeanMetaData();
      ClassLoader cl = Configurator.getClassLoader(metaData);
      MethodJoinpoint joinpoint = configurator.getMethodJoinPoint(info, cl, method, parameters, false, true);
      joinpoint.setTarget(context.getTarget());
      dispatchJoinPoint(context, joinpoint);
   }
}