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
package org.jboss.dependency.plugins;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;

/**
 * Scoped controller.
 *
 * The only scoping logic is local lookup and
 * add/remove of controller context.
 * Subclasses should provide parent lookup after looking
 * at the current scoped instance.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class ScopedController extends AbstractController
{
   private AbstractController underlyingController;

   protected void setUnderlyingController(AbstractController underlyingController)
   {
      this.underlyingController = underlyingController;
   }

   /**
    * Get the context only in this scope.
    * No hierarchy lookup.
    *
    * @param name the context name
    * @param state the controller state
    * @return found context or null if not available
    */
   public ControllerContext getContextLocally(Object name, ControllerState state)
   {
      return super.getContext(name, state);
   }

   /**
    * Is controller scoped.
    *
    * @return true if scoped
    */
   protected boolean isScoped()
   {
      return underlyingController != null;
   }

   protected void addControllerContext(ControllerContext context)
   {
      if (isScoped())
      {
         lockWrite();
         try
         {
            underlyingController.removeControllerContext(context);
            context.setController(this);
            registerControllerContext(context);
         }
         catch (Throwable t)
         {
            // put the context back to original
            context.setController(underlyingController);
            underlyingController.addControllerContext(context);
            // rethrow
            if (t instanceof RuntimeException)
               throw (RuntimeException)t;
            else
               throw new RuntimeException(t);
         }
         finally
         {
            unlockWrite();
         }
      }
      else
         super.addControllerContext(context);
   }

   protected void removeControllerContext(ControllerContext context)
   {
      if (isScoped())
      {
         lockWrite();
         try
         {
            unregisterControllerContext(context);
            context.setController(underlyingController);
            underlyingController.addControllerContext(context);
         }
         finally
         {
            unlockWrite();
         }
      }
      else
         super.removeControllerContext(context);
   }

}
