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

import java.util.Iterator;
import java.util.Set;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.joinpoint.spi.TargettedJoinpoint;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * ConfigureAction.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class ConfigureAction extends KernelControllerContextAction
{
   public void installAction(KernelControllerContext context) throws Throwable
   {
      KernelController controller = (KernelController) context.getController();
      Kernel kernel = controller.getKernel();
      KernelConfigurator configurator = kernel.getConfigurator();

      Object object = context.getTarget();
      BeanInfo info = context.getBeanInfo();
      BeanMetaData metaData = context.getBeanMetaData();
      Set joinPoints = configurator.getPropertySetterJoinPoints(info, metaData);
      setAttributes(context, object, joinPoints, false);
   }

   public void uninstallAction(KernelControllerContext context)
   {
      KernelController controller = (KernelController) context.getController();
      Kernel kernel = controller.getKernel();
      KernelConfigurator configurator = kernel.getConfigurator();

      Object object = context.getTarget();
      BeanInfo info = context.getBeanInfo();
      BeanMetaData metaData = context.getBeanMetaData();
      try
      {
         Set joinPoints = configurator.getPropertyNullerJoinPoints(info, metaData);
         setAttributes(context, object, joinPoints, true);
      }
      catch (Throwable t)
      {
         log.warn("Error unconfiguring bean " + context, t);
      }
   }

   /**
    * Set the attributes
    *
    * @param context      the context
    * @param target       the target
    * @param joinPoints   the attribute setter joinpoints
    * @param ignoreErrors whether to ignore errors
    * @throws Throwable for any unignored error
    */
   protected void setAttributes(KernelControllerContext context, Object target, Set joinPoints, boolean ignoreErrors) throws Throwable
   {
      if (joinPoints.isEmpty() == false)
      {
         boolean trace = log.isTraceEnabled();

         for (Iterator i = joinPoints.iterator(); i.hasNext();)
         {
            TargettedJoinpoint joinPoint = (TargettedJoinpoint) i.next();
            joinPoint.setTarget(target);
            try
            {
               dispatchJoinPoint(context, joinPoint);
            }
            catch (Throwable t)
            {
               if (ignoreErrors)
               {
                  if (trace)
                     log.trace("Ignored for " + joinPoint, t);
               }
               else
               {
                  throw t;
               }
            }
         }
      }
   }

}