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

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.joinpoint.spi.JoinpointException;
import org.jboss.joinpoint.spi.MethodJoinpoint;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.reflect.spi.MethodInfo;

/**
 * LifecycleAction.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public abstract class LifecycleAction extends KernelControllerContextAction
{
   /**
    * Create a new AbstractLifecycleAction.
    */
   public LifecycleAction()
   {
   }

   /**
    * Get the install method
    * 
    * @param beanMetaData
    * @return the method
    */
   public abstract String getInstallMethod(BeanMetaData beanMetaData);

   /**
    * Get install default method name
    *
    * @return install annotation name
    */
   public abstract String getDefaultInstallMethod();

   /**
    * Get install annotation class name
    *
    * @return install annotation name
    */
   public abstract String getInstallAnnotation();

   /**
    * Get the install parameters
    * 
    * @param beanMetaData
    * @return the parameters
    */
   public abstract List<ParameterMetaData> getInstallParameters(BeanMetaData beanMetaData);

   /**
    * Get the uninstall method
    * 
    * @param beanMetaData
    * @return the method
    */
   public abstract String getUninstallMethod(BeanMetaData beanMetaData);

   /**
    * Get uninstall default method name
    *
    * @return install annotation name
    */
   public abstract String getDefaultUninstallMethod();

   /**
    * Get uninstall annotation class name
    *
    * @return uninstall annotation name
    */
   public abstract String getUninstallAnnotation();

   /**
    * Get the uninstall parameters
    *
    * @param beanMetaData
    * @return the parameters
    */
   public abstract List<ParameterMetaData> getUninstallParameters(BeanMetaData beanMetaData);

   protected void installActionInternal(KernelControllerContext context) throws Throwable
   {
      boolean trace = log.isTraceEnabled();

      KernelController controller = (KernelController) context.getController();
      Kernel kernel = controller.getKernel();
      KernelConfigurator configurator = kernel.getConfigurator();

      Object target = context.getTarget();
      BeanInfo info = context.getBeanInfo();
      BeanMetaData metaData = context.getBeanMetaData();
      String method = getInstallMethod(context);
      List<ParameterMetaData> parameters = getInstallParameters(context);
      MethodJoinpoint joinpoint;
      try
      {
         ClassLoader cl = Configurator.getClassLoader(metaData);
         joinpoint = configurator.getMethodJoinPoint(info, cl, method, parameters, false, true);
      }
      catch (JoinpointException ignored)
      {
         if (trace)
         {
            if (parameters == null)
               log.trace("No " + method + " method for " + context);
            else
               log.trace("No " + method + parameters + " method for " + context);
         }
         return;
      }
      joinpoint.setTarget(target);
      dispatchJoinPoint(context, joinpoint);
   }

   protected void uninstallActionInternal(KernelControllerContext context)
   {
      boolean trace = log.isTraceEnabled();

      KernelController controller = (KernelController) context.getController();
      Kernel kernel = controller.getKernel();
      KernelConfigurator configurator = kernel.getConfigurator();

      Object target = context.getTarget();
      BeanInfo info = context.getBeanInfo();
      BeanMetaData metaData = context.getBeanMetaData();
      String method = getUninstallMethod(context);
      List<ParameterMetaData> parameters = getUninstallParameters(context);
      MethodJoinpoint joinpoint;
      try
      {
         ClassLoader cl = Configurator.getClassLoader(metaData);
         joinpoint = configurator.getMethodJoinPoint(info, cl, method, parameters, false, true);
         joinpoint.setTarget(target);
         dispatchJoinPoint(context, joinpoint);
      }
      catch (JoinpointException ignored)
      {
         if (trace)
         {
            if (parameters == null)
               log.trace("No " + method + " method for " + context);
            else
               log.trace("No " + method + parameters + " method for " + context);
         }
      }
      catch (Throwable throwable)
      {
         log.warn("Error during " + method + " for " + context.getName(), throwable);
      }
   }

   /**
    * Get the install method
    *
    * @param context
    * @return the method
    */
   public String getInstallMethod(KernelControllerContext context)
   {
      String installMethod = getInstallMethod(context.getBeanMetaData());
      if (installMethod != null)
      {
         return installMethod;
      }
      BeanInfo beanInfo = context.getBeanInfo();
      Set<MethodInfo> methods = beanInfo.getMethods();
      if (methods != null)
      {
         for (MethodInfo mi : methods)
         {
            if (mi.isAnnotationPresent(getInstallAnnotation()))
            {
               return mi.getName();
            }
         }
      }
      return getDefaultInstallMethod();
   }

   /**
    * Get the install parameters
    *
    * @param context
    * @return the parameters
    */
   public List<ParameterMetaData> getInstallParameters(KernelControllerContext context)
   {
      // todo some parameter support
      return getInstallParameters(context.getBeanMetaData());
   }

   /**
    * Get the uninstall method
    *
    * @param context
    * @return the method
    */
   public String getUninstallMethod(KernelControllerContext context)
   {
      String uninstallMethod = getUninstallMethod(context.getBeanMetaData());
      if (uninstallMethod != null)
      {
         return uninstallMethod;
      }
      BeanInfo beanInfo = context.getBeanInfo();
      Set<MethodInfo> methods = beanInfo.getMethods();
      if (methods != null)
      {
         for (MethodInfo mi : methods)
         {
            if (mi.isAnnotationPresent(getUninstallAnnotation()))
            {
               return mi.getName();
            }
         }
      }
      return getDefaultUninstallMethod();
   }

   /**
    * Get the uninstall parameters
    *
    * @param context the context
    * @return the parameters
    */
   public List<ParameterMetaData> getUninstallParameters(KernelControllerContext context)
   {
      // todo some parameter support
      return getUninstallParameters(context.getBeanMetaData());
   }

}