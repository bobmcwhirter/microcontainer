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

import org.jboss.dependency.plugins.AbstractController;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.metadata.spi.repository.MutableMetaDataRepository;
import org.jboss.metadata.spi.scope.ScopeKey;

/**
 * Install scope action.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class InstallScopeAction extends AbstractScopeAction
{
   public static final InstallScopeAction INSTANCE = new InstallScopeAction();

   protected void installAction(ControllerContext context) throws Throwable
   {
      ScopeKey scopeKey = getScopeKey(context);
      if (scopeKey != null)
      {
         Controller controller = context.getController();
         if (controller instanceof AbstractKernelController == false)
            throw new IllegalArgumentException("Can only handle AbstractKernelController: " + controller);

         AbstractKernelController kernelController = (AbstractKernelController)controller;
         KernelMetaDataRepository repository = kernelController.getKernel().getMetaDataRepository();
         MutableMetaDataRepository mmdr = repository.getMetaDataRepository();
         AbstractController abstractController = ScopeHierarchyBuilder.buildControllerHierarchy(kernelController, mmdr, scopeKey);
         if (abstractController instanceof ScopedKernelController == false)
            throw new IllegalArgumentException("Should be ScopedKernelController instance: " + abstractController);

         ScopedKernelController scopedController = (ScopedKernelController)abstractController;
         scopedController.addScopedControllerContext(context);
      }
   }

   protected void uninstallAction(ControllerContext context)
   {
      ScopeKey scopeKey = getScopeKey(context);
      if (scopeKey != null)
      {
         Controller controller = context.getController();
         if (controller instanceof KernelController == false)
            throw new IllegalArgumentException("Can only handle kernel controller: " + controller);

         KernelController kernelController = (KernelController)controller;
         KernelMetaDataRepository repository = kernelController.getKernel().getMetaDataRepository();

         // find scoped controller
         MutableMetaDataRepository mmdr = repository.getMetaDataRepository();
         ScopeHierarchyBuilder.cleanControllerHierarchy(mmdr, scopeKey, context);
      }
   }
}