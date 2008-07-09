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
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.metadata.spi.MutableMetaData;
import org.jboss.metadata.spi.repository.MutableMetaDataRepository;
import org.jboss.metadata.spi.retrieval.MetaDataItem;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.metadata.plugins.loader.memory.MemoryMetaDataLoader;

/**
 * ScopeHierarchyBuilder.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public final class ScopeHierarchyBuilder
{
   /**
    * Create whole hierarchy.
    * Fill in missing controllers.
    *
    * @param top the top controller
    * @param mmdr the mutable metadata repository
    * @param scopeKey the current scope key
    * @return controller for current scope key
    * @throws Throwable for any error
    */
   public static AbstractController buildControllerHierarchy(AbstractKernelController top, MutableMetaDataRepository mmdr, ScopeKey scopeKey) throws Throwable
   {
      if (scopeKey == null)
         return top;

      MetaDataRetrieval mdr = mmdr.getMetaDataRetrieval(scopeKey);
      if (mdr != null)
      {
         MetaDataItem<ScopedKernelController> mdi = mdr.retrieveMetaData(ScopedKernelController.class);
         if (mdi != null)
            return mdi.getValue();
      }
      else
      {
         mdr = new MemoryMetaDataLoader(scopeKey);
         mmdr.addMetaDataRetrieval(mdr);          
      }

      AbstractController parentController = buildControllerHierarchy(top, mmdr, scopeKey.getParent());
      ScopedKernelController scopedKernelController = new ScopedKernelController(top.getKernel(), parentController, scopeKey);
      ((MutableMetaData)mdr).addMetaData(scopedKernelController, ScopedKernelController.class);

      return scopedKernelController;
   }

   /**
    * Clean controller hierarchy.
    *
    * @param mmdr the mutable metadata repository
    * @param scopeKey the current scope key
    * @param context the context to remove
    */
   public static void cleanControllerHierarchy(MutableMetaDataRepository mmdr, ScopeKey scopeKey, ControllerContext context)
   {
      if (scopeKey == null)
         return;

      MetaDataRetrieval mdr = mmdr.getMetaDataRetrieval(scopeKey);
      if (mdr == null)
      {
         throw new IllegalArgumentException("Expecting MetaDataRetrieval instance in scope: " + scopeKey);
      }
      MetaDataItem<ScopedKernelController> controllerItem = mdr.retrieveMetaData(ScopedKernelController.class);
      if (controllerItem == null)
      {
         throw new IllegalArgumentException("Expecting ScopedKernelController instance in scope:" + scopeKey);
      }
      ScopedKernelController scopedController = controllerItem.getValue();
      if (context != null)
         scopedController.removeScopedControllerContext(context);

      if (scopedController.isActive() == false)
      {
         try
         {
            ((MutableMetaData)mdr).removeMetaData(ScopedKernelController.class);
         }
         finally
         {
            scopedController.release();
         }
      }

      cleanControllerHierarchy(mmdr, scopeKey.getParent(), null);
   }
}