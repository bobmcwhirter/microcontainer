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
import java.util.List;
import java.util.Set;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.dependency.plugins.AbstractDependencyItem;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.repository.spi.KernelRepository;
import org.jboss.repository.spi.MetaDataContext;
import org.jboss.repository.spi.MetaDataContextFactory;

/**
 * DescribeAction.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class DescribeAction extends KernelControllerContextAction
{
   public void installAction(KernelControllerContext context) throws Throwable
   {
      KernelController controller = (KernelController) context.getController();
      Kernel kernel = controller.getKernel();
      KernelConfigurator configurator = kernel.getConfigurator();

      BeanMetaData metaData = context.getBeanMetaData();
      if (metaData.getBean() != null)
      {
         BeanInfo info = configurator.getBeanInfo(metaData);
         context.setBeanInfo(info);

         info = addAnnotations(context, metaData, info);

         DependencyInfo depends = context.getDependencyInfo();
         // add custom dependencies (e.g. AOP layer).
         List<Object> dependencies = info.getDependencies();
         if (dependencies != null)
         {
            for (Object dependencyName : dependencies)
            {
               AbstractDependencyItem dependency = new AbstractDependencyItem(metaData.getName(), dependencyName, ControllerState.INSTANTIATED, ControllerState.INSTALLED);
               depends.addIDependOn(dependency);
            }
         }
      }
   }

   public void uninstallAction(KernelControllerContext context)
   {
      context.setMetaDataContext(null);
      context.setBeanInfo(null);
   }

   /**
    * Adds annotations to the bean. If annotations are added, returns the bean info for the instance
    * @return The class bean info if no annotations exist or the instance bean info if annotations exist
    */
   private BeanInfo addAnnotations(KernelControllerContext context, BeanMetaData beanMetaData, BeanInfo beanInfo)
   {
      MetaDataContext metaCtx = addClassAnnotations(context, beanMetaData, beanInfo);
      addPropertyAnnotations(metaCtx, context, beanMetaData, beanInfo);
      return context.getBeanInfo();
   }

   private MetaDataContext addClassAnnotations(KernelControllerContext context, BeanMetaData beanMetaData, BeanInfo beanInfo)
   {
      Set annotations = beanMetaData.getAnnotations();

      MetaDataContext metaCtx = null;

      if (annotations != null && annotations.size() > 0)
      {
         metaCtx = getMetaDataContext(context);
         if (metaCtx != null)
         {
            metaCtx.addAnnotations(annotations);
         }
      }

      return metaCtx;
   }

   private MetaDataContext addPropertyAnnotations(MetaDataContext metaCtx, KernelControllerContext context, BeanMetaData beanMetaData, BeanInfo beanInfo)
   {
      Set properties = beanMetaData.getProperties();

      if (properties != null && properties.size() > 0)
      {
         for (Iterator it = properties.iterator() ; it.hasNext() ; )
         {
            PropertyMetaData property = (PropertyMetaData)it.next();
            Set propertyAnnotations = property.getAnnotations();
            if (propertyAnnotations != null && propertyAnnotations.size() > 0)
            {
               if (metaCtx == null)
               {
                  metaCtx = getMetaDataContext(context);
               }
               if (metaCtx != null)
               {
                  //metaCtx.addPropertyAnnotations(property.getName(), propertyAnnotations);
                  Set propertyInfos = beanInfo.getProperties();
                  if (propertyInfos != null && propertyInfos.size() > 0)
                  {
                     metaCtx.addPropertyAnnotations(property.getName(), beanInfo.getProperties(), propertyAnnotations);
                  }
               }
            }
         }
      }

      return metaCtx;
   }

   private MetaDataContext getMetaDataContext(KernelControllerContext context)
   {
      //TODO: Hardcoding this doesn't feel right...
      ControllerContext repCtx = context.getController().getContext("Repository", ControllerState.INSTALLED);

      if (repCtx == null)
      {
         log.warn("You have defined annotations for bean '" + context.getName() + "', but no KernelRepository has been installed under the name 'Repository'");
         return null;
      }

      KernelRepository repository = (KernelRepository)repCtx.getTarget();
      MetaDataContextFactory metaFactory = context.getBeanInfo().getMetaDataContextFactory();
      MetaDataContext metaCtx = metaFactory.getMetaDataContext(repository, context.getName());

      context.setMetaDataContext(metaCtx);

      return metaCtx;
   }

}