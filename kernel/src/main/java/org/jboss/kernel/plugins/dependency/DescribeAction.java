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
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.config.KernelConfig;
import org.jboss.kernel.spi.dependency.DependencyBuilder;
import org.jboss.kernel.spi.dependency.DependencyBuilderListItem;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.metadata.spi.MetaData;

/**
 * DescribeAction.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @version $Revision$
 */
public class DescribeAction extends AnnotationsAction
{
   @SuppressWarnings("unchecked")
   protected void installActionInternal(KernelControllerContext context) throws Throwable
   {
      BeanInfo info = context.getBeanInfo();
      if (info != null)
      {
         KernelController controller = (KernelController)context.getController();
         Kernel kernel = controller.getKernel();
         KernelMetaDataRepository repository = kernel.getMetaDataRepository();
         MetaData md = repository.getMetaData(context);
         KernelConfig config = kernel.getConfig();
         DependencyBuilder dependencyBuilder = config.getDependencyBuilder();
         // add custom dependencies (e.g. AOP layer).
         List<DependencyBuilderListItem> dependencies = dependencyBuilder.getDependencies(info, md);
         if (log.isTraceEnabled())
            log.trace("Extra dependencies for " + context.getName() + " " + dependencies);
         if (dependencies != null && dependencies.isEmpty() == false)
         {
            for (DependencyBuilderListItem dependencyItem : dependencies)
            {
               dependencyItem.addDependency(context);
            }
         }
         // handle custom annotations
         applyAnnotations(context);
      }
   }

   protected void uninstallActionInternal(KernelControllerContext context)
   {
      BeanInfo info = context.getBeanInfo();
      if (info != null)
      {
         // handle custom annotations
         cleanAnnotations(context);

         KernelController controller = (KernelController)context.getController();
         Kernel kernel = controller.getKernel();
         KernelMetaDataRepository repository = kernel.getMetaDataRepository();
         MetaData md = repository.getMetaData(context);

         DependencyBuilder dependencyBuilder;
         try
         {
            KernelConfig config = kernel.getConfig();
            dependencyBuilder = config.getDependencyBuilder(); 
         }
         catch (Throwable e)
         {
            log.debug("Error while cleaning the annotations: " + e);
            return;
         }

         // remove custom dependencies (e.g. AOP layer).
         List<DependencyBuilderListItem> dependencies = dependencyBuilder.getDependencies(info, md);
         if (log.isTraceEnabled())
            log.trace("Unwind extra dependencies for " + context.getName() + " " + dependencies);
         if (dependencies != null && dependencies.isEmpty() == false)
         {
            for (DependencyBuilderListItem dependencyItem : dependencies)
            {
               dependencyItem.removeDependency(context);
            }
         }
      }      
   }

   protected ControllerState getState()
   {
      return ControllerState.DESCRIBED;
   }
}