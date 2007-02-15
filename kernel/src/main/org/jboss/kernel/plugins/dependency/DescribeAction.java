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
import org.jboss.dependency.plugins.AbstractDependencyItem;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.metadata.spi.MetaData;

/**
 * DescribeAction.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class DescribeAction extends KernelControllerContextAction
{
   protected void installActionInternal(KernelControllerContext context) throws Throwable
   {
      BeanInfo info = context.getBeanInfo();
      if (info != null)
      {
         KernelController controller = (KernelController)context.getController();
         KernelMetaDataRepository repository = controller.getKernel().getMetaDataRepository();
         MetaData md = repository.getMetaData(context);
         DependencyInfo depends = context.getDependencyInfo();
         // add custom dependencies (e.g. AOP layer).
         List<Object> dependencies = info.getDependencies(md);
         log.trace("Extra dependencies for " + context.getName() + " " + dependencies);
         if (dependencies != null)
         {
            BeanMetaData metaData = context.getBeanMetaData();
            for (Object dependencyName : dependencies)
            {
               AbstractDependencyItem dependency = new AbstractDependencyItem(metaData.getName(), dependencyName, ControllerState.INSTANTIATED, ControllerState.INSTALLED);
               depends.addIDependOn(dependency);
            }
         }
      }
   }

}