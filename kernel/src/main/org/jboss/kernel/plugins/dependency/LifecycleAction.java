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
import org.jboss.beans.metadata.spi.LifecycleMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.joinpoint.spi.JoinpointException;
import org.jboss.joinpoint.spi.MethodJoinpoint;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * LifecycleAction.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
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
    * Get install Lifecycle metadata.
    *
    * @param beanMetaData the bean meta data
    * @return install lifecycle metadata
    */
   protected abstract LifecycleMetaData getInstallLifecycle(BeanMetaData beanMetaData);

   /**
    * Get uninstall Lifecycle metadata.
    *
    * @param beanMetaData the bean meta data
    * @return uninstall lifecycle metadata
    */
   protected abstract LifecycleMetaData getUninstallLifecycle(BeanMetaData beanMetaData);

   /**
    * Get the method name from lifecycle meta data.
    *
    * @param lifecycle the lifecycle meta data
    * @return method name or null if null lifecycle
    */
   protected String getMethod(LifecycleMetaData lifecycle)
   {
      if (lifecycle != null)
         return lifecycle.getMethodName();
      return null;
   }

   /**
    * Get the parameters.
    *
    * @param lifecycle the lifecycle meta data
    * @return lifecycle parameters or null if null lifecycle
    */
   protected List<ParameterMetaData> getParameters(LifecycleMetaData lifecycle)
   {
      if (lifecycle != null)
         return lifecycle.getParameters();
      return null;
   }

   /**
    * Get the install method
    *
    * @param beanMetaData bean meta data
    * @return the method
    */
   protected String getInstallMethod(BeanMetaData beanMetaData)
   {
      return getMethod(getInstallLifecycle(beanMetaData));
   }

   /**
    * Get install default method name
    *
    * @return install annotation name
    */
   public abstract String getDefaultInstallMethod();

   /**
    * Get the install parameters
    *
    * @param beanMetaData bean meta data
    * @return the parameters
    */
   protected List<ParameterMetaData> getInstallParameters(BeanMetaData beanMetaData)
   {
      return getParameters(getInstallLifecycle(beanMetaData));
   }

   /**
    * Get the uninstall method
    *
    * @param beanMetaData bean meta data
    * @return the method
    */
   protected String getUninstallMethod(BeanMetaData beanMetaData)
   {
      return getMethod(getUninstallLifecycle(beanMetaData));
   }

   /**
    * Get uninstall default method name
    *
    * @return install annotation name
    */
   public abstract String getDefaultUninstallMethod();

   /**
    * Get the uninstall parameters
    *
    * @param beanMetaData bean meta data
    * @return the parameters
    */
   protected List<ParameterMetaData> getUninstallParameters(BeanMetaData beanMetaData)
   {
      return getParameters(getUninstallLifecycle(beanMetaData));
   }

   /**
    * Is install ignored.
    *
    * @param context the context
    * @return true if ignored found on any metadata
    */
   protected boolean isInstallInvocationIgnored(KernelControllerContext context)
   {
      return isInvocationIgnored(getInstallLifecycle(context.getBeanMetaData()));
   }

   /**
    * Is uninstall ignored.
    *
    * @param context the context
    * @return true if ignored found on any metadata
    */
   protected boolean isUninstallInvocationIgnored(KernelControllerContext context)
   {
      return isInvocationIgnored(getUninstallLifecycle(context.getBeanMetaData()));
   }

   /**
    * Is invocation ignored.
    *
    * @param lifecycle the lifecycle meta data
    * @return true if ignored set on lifecycle
    */
   protected boolean isInvocationIgnored(LifecycleMetaData lifecycle)
   {
      return lifecycle != null && lifecycle.isIgnored();
   }

   protected void installActionInternal(KernelControllerContext context) throws Throwable
   {
      boolean trace = log.isTraceEnabled();

      if (isInstallInvocationIgnored(context) == false)
      {

         KernelController controller = (KernelController)context.getController();
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
      else if (trace)
         log.trace("Ignoring " + getDefaultInstallMethod() + " lifecycle invocation.");
   }

   protected void uninstallActionInternal(KernelControllerContext context)
   {
      boolean trace = log.isTraceEnabled();

      if (isUninstallInvocationIgnored(context) == false)
      {

         KernelController controller = (KernelController)context.getController();
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
      else if (trace)
         log.trace("Ignoring " + getDefaultUninstallMethod() + " lifecycle invocation.");
   }

   /**
    * Get the install method
    *
    * @param context the context
    * @return the method
    */
   public String getInstallMethod(KernelControllerContext context)
   {
      String installMethod = getInstallMethod(context.getBeanMetaData());
      if (installMethod != null)
      {
         return installMethod;
      }
      return getDefaultInstallMethod();
   }

   /**
    * Get the install parameters
    *
    * @param context the context
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
    * @param context the context
    * @return the method
    */
   public String getUninstallMethod(KernelControllerContext context)
   {
      String uninstallMethod = getUninstallMethod(context.getBeanMetaData());
      if (uninstallMethod != null)
      {
         return uninstallMethod;
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