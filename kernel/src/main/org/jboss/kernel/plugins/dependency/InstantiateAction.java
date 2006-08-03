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

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.joinpoint.spi.Joinpoint;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.dependency.KernelControllerContextAware;
import org.jboss.repository.spi.MetaDataContext;

/**
 * InstantiateAction.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class InstantiateAction extends KernelControllerContextAction
{
   public void installAction(KernelControllerContext context) throws Throwable
   {
      KernelController controller = (KernelController) context.getController();
      Kernel kernel = controller.getKernel();
      KernelConfigurator configurator = kernel.getConfigurator();

      BeanMetaData metaData = context.getBeanMetaData();
      BeanInfo info = context.getBeanInfo();
      final Joinpoint joinPoint = configurator.getConstructorJoinPoint(info, metaData.getConstructor(), metaData);

      Object object = dispatchJoinPoint(context, joinPoint); 
      context.setTarget(object);
      
      MetaDataContext metaCtx = context.getMetaDataContext();
      if (metaCtx != null)
      {
         metaCtx.setTarget(object);
      }
      
      try
      {
         if (object != null && context.getBeanInfo() == null)
         {
            info = configurator.getBeanInfo(object.getClass());
            context.setBeanInfo(info);
         }
         
         if (object != null && object instanceof KernelControllerContextAware)
            ((KernelControllerContextAware) object).setKernelControllerContext(context);
      }
      catch (Throwable t)
      {
         uninstall(context);
         throw t;
      }
   }
   
   public void uninstallAction(KernelControllerContext context)
   {
      try
      {
         Object object = context.getTarget();
         if (object != null && object instanceof KernelControllerContextAware)
            ((KernelControllerContextAware) object).unsetKernelControllerContext(context);
      }
      catch (Throwable ignored)
      {
         log.debug("Ignored error unsetting context ", ignored);
      }
      finally
      {
         context.setTarget(null);
      }
   }
}