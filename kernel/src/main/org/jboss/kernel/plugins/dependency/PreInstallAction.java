/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
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
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.metadata.spi.MetaData;

/**
 * PreInstallAction.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class PreInstallAction extends KernelControllerContextAction
{
   protected void installActionInternal(KernelControllerContext context) throws Throwable
   {
      KernelController controller = (KernelController) context.getController();
      Kernel kernel = controller.getKernel();
      KernelConfigurator configurator = kernel.getConfigurator();

      BeanMetaData metaData = context.getBeanMetaData();
      if (metaData.getBean() != null)
      {
         BeanInfo info = configurator.getBeanInfo(metaData);
         context.setBeanInfo(info);

         MetaData md = addMetaData(context);
         try
         {
            // todo
         }
         catch (Throwable t)
         {
            removeMetaData(context);
            throw t;
         }
      }
   }

   protected void uninstallActionInternal(KernelControllerContext context)
   {
      removeMetaData(context);
      context.setBeanInfo(null);
   }

   /**
    * Adds annotations to the bean.
    *
    * @param context the context
    * @return the metadata
    */
   private MetaData addMetaData(KernelControllerContext context)
   {
      KernelController controller = (KernelController) context.getController();
      KernelMetaDataRepository repository = controller.getKernel().getMetaDataRepository();
      repository.addMetaData(context);
      return repository.getMetaData(context);
   }

   /**
    * Remove any previously added metadata
    *
    * @param context the context
    */
   private void removeMetaData(KernelControllerContext context)
   {
      try
      {
         KernelController controller = (KernelController) context.getController();
         KernelMetaDataRepository repository = controller.getKernel().getMetaDataRepository();
         repository.removeMetaData(context);
      }
      catch (Throwable ignored)
      {
         log.warn("Unexpected error removing metadata: ", ignored);
      }
   }
}
