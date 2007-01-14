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
import java.util.Set;
import java.util.HashSet;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.*;
import org.jboss.kernel.spi.registry.KernelRegistry;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.dependency.spi.DispatchContext;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.reflect.spi.MethodInfo;

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

      List installs = metaData.getInstalls();
      if (installs != null)
      {
         for (int i = 0; i < installs.size(); ++i)
         {
            InstallMetaData install = (InstallMetaData) installs.get(i);
            DispatchContext target = context;
            if (install.getBean() != null)
               target = (DispatchContext) controller.getContext(install.getBean(), install.getDependentState());
            invoke(configurator, target, install.getMethodName(), install.getParameters());
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

      List uninstalls = metaData.getUninstalls();
      if (uninstalls != null)
      {
         for (int i = uninstalls.size()-1; i >= 0; --i)
         {
            InstallMetaData uninstall = (InstallMetaData) uninstalls.get(i);
            DispatchContext target = context;
            if (uninstall.getBean() != null)
            {
               target = (DispatchContext) controller.getContext(uninstall.getBean(), uninstall.getDependentState());
               if (target == null)
               {
                  log.warn("Ignoring uninstall action on target in incorrect state " + uninstall.getBean());
                  continue;
               }
            }
            try
            {
               invoke(configurator, target, uninstall.getMethodName(), uninstall.getParameters());
            }
            catch (Throwable t)
            {
               log.warn("Ignoring uninstall action on target " + uninstall, t);
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

   protected Object invoke(KernelConfigurator configurator, DispatchContext context, String name, List<ParameterMetaData> params) throws Throwable
   {
      ClassLoader classLoader = context.getClassLoader();
      int size = (params != null) ? params.size() : 0;
      Object[] parameters = new Object[size];
      String[] signature = new String[size];
      for(int i = 0; i < size; i++)
      {
         ParameterMetaData pmd = params.get(i);
         signature[i] = pmd.getType();
         TypeInfo typeInfo;
         if (signature[i] != null)
         {
            typeInfo = configurator.getClassInfo(signature[i], classLoader);
         }
         else
         {
            typeInfo = findTypeInfo(configurator, context.getTarget(), name, i);
         }
         parameters[i] = pmd.getValue().getValue(typeInfo, classLoader);
      }
      return context.invoke(name, parameters, signature);
   }

   private TypeInfo findTypeInfo(KernelConfigurator configurator, Object target, String name, int index) throws Throwable
   {
      if (target == null)
      {
         return null;
      }
      BeanInfo beanInfo = configurator.getBeanInfo(target.getClass());
      Set<MethodInfo> methods = beanInfo.getMethods();
      Set<MethodInfo> possibleMethods = new HashSet<MethodInfo>();
      for(MethodInfo mi : methods)
      {
         if (name.equals(mi.getName()) && mi.getParameterTypes() != null && mi.getParameterTypes().length > index)
         {
            possibleMethods.add(mi);
         }
      }
      if (possibleMethods.isEmpty() || possibleMethods.size() > 1)
      {
         log.warn("Unable to determine parameter TypeInfo, method name: " + name + ", index: " + index + ", target: " + target);
         return null;
      }
      else
      {
         return possibleMethods.iterator().next().getParameterTypes()[index];
      }
   }

}