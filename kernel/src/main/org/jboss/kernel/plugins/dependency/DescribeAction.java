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
import org.jboss.classadapter.spi.DependencyBuilderListItem;
import org.jboss.kernel.plugins.annotations.BeanAnnotationAdapterFactory;
import org.jboss.kernel.plugins.annotations.BeanAnnotationAdapter;
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
   @SuppressWarnings("unchecked")
   protected void installActionInternal(KernelControllerContext context) throws Throwable
   {
      BeanInfo info = context.getBeanInfo();
      if (info != null)
      {
         KernelController controller = (KernelController)context.getController();
         KernelMetaDataRepository repository = controller.getKernel().getMetaDataRepository();
         MetaData md = repository.getMetaData(context);
         // add custom dependencies (e.g. AOP layer).
         List<DependencyBuilderListItem> dependencies = (List) info.getDependencies(md);
         log.trace("Extra dependencies for " + context.getName() + " " + dependencies);
         if (dependencies != null && dependencies.isEmpty() == false)
         {
            for (DependencyBuilderListItem dependencyItem : dependencies)
            {
               dependencyItem.addDependency(context);
            }
         }
         // handle custom annotations
         AnnotationMetaDataVisitor annotationsVisitor = new AnnotationMetaDataVisitor(context);
         annotationsVisitor.before();
         try
         {
            getBeanAnnotationAdapter().applyAnnotations(annotationsVisitor);
         }
         finally
         {
            annotationsVisitor.after();
         }
      }
   }

   protected void uninstallActionInternal(KernelControllerContext context)
   {
      // handle custom annotations
      AnnotationMetaDataVisitor annotationsVisitor = new AnnotationMetaDataVisitor(context);
      annotationsVisitor.before();
      try
      {
         getBeanAnnotationAdapter().cleanAnnotations(annotationsVisitor);
      }
      catch(Throwable t)
      {
         log.debug("Error while cleaning the annotations: " + t);
      }
      finally
      {
         annotationsVisitor.after();
      }
   }

   protected BeanAnnotationAdapter getBeanAnnotationAdapter()
   {
      BeanAnnotationAdapterFactory factory = BeanAnnotationAdapterFactory.getInstance();
      return factory.getBeanAnnotationAdapter();
   }
}