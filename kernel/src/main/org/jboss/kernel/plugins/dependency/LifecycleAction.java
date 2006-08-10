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
    * @param context the context
    * @return the method
    */
   public abstract String getInstallMethod(KernelControllerContext context);

   /**
    * Get the install parameters
    * 
    * @param context the context
    * @return the parameters
    */
   public abstract List<ParameterMetaData> getInstallParameters(KernelControllerContext context);

   /**
    * Get the uninstall method
    * 
    * @param context the context
    * @return the method
    */
   public abstract String getUninstallMethod(KernelControllerContext context);

   /**
    * Get the uninstall parameters
    * 
    * @param context the context
    * @return the parameters
    */
   public abstract List<ParameterMetaData> getUninstallParameters(KernelControllerContext context);
   
   public void installAction(KernelControllerContext context) throws Throwable
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
      MethodJoinpoint joinpoint = null;
      ClassLoader cl = Configurator.getClassLoader(metaData);
      try
      {
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

      // Dispatch the joinpoint using the target class loader
      joinpoint.setTarget(target);
      ClassLoader tcl = Thread.currentThread().getContextClassLoader();
      try
      {
         if( cl != null )
            Thread.currentThread().setContextClassLoader(cl);
         dispatchJoinPoint(context, joinpoint);
      }
      finally
      {
         if( cl != null )
            Thread.currentThread().setContextClassLoader(tcl);
      }
   }

   public void uninstallAction(KernelControllerContext context)
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
      MethodJoinpoint joinpoint = null;
      ClassLoader cl = null;
      ClassLoader tcl = Thread.currentThread().getContextClassLoader();
      try
      {
         cl = Configurator.getClassLoader(metaData);
         joinpoint = configurator.getMethodJoinPoint(info, cl, method, parameters, false, true);
         joinpoint.setTarget(target);
         // Dispatch the joinpoint using the target class loader
         if( cl != null )
            Thread.currentThread().setContextClassLoader(cl);
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
         return;
      }
      catch (Throwable throwable)
      {
         log.warn("Error during " + method, throwable);
      }
      finally
      {
         if( cl != null )
            Thread.currentThread().setContextClassLoader(tcl);         
      }
   }
}