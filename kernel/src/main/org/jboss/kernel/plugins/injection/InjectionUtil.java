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
package org.jboss.kernel.plugins.injection;

import org.jboss.beans.metadata.injection.InjectionMode;
import org.jboss.beans.metadata.injection.InjectionType;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelController;

import java.util.Set;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public final class InjectionUtil
{
   // static utl class
   private InjectionUtil()
   {
   }

   /**
    * Resolves injection bean.
    *
    * @param controller KernelController
    * @param targetClass class to which we match other contexts
    * @param propertyName used in ConfigureAction
    * @param state
    * @param mode
    * @param type
    * @param messageObject object that gives us the most info about exception
    * @return null if type InjectionType.LOOSE, else found bean or throws throwable
    * @throws Throwable
    */
   public static Object resolveInjection(KernelController controller,
                                  Class targetClass,
                                  String propertyName,
                                  ControllerState state,
                                  InjectionMode mode,
                                  InjectionType type,
                                  Object messageObject) throws Throwable
   {
      Object result = null;
      if (InjectionMode.BY_TYPE.equals(mode))
      {
         Set<ControllerContext> contexts = controller.getInstantiatedContexts(targetClass);
         int numberOfMatchingBeans = contexts.size();
         if (numberOfMatchingBeans > 1)
         {
            throw new Error("Should not be here, too many matching contexts - dependency failed! " + messageObject);
         }
         else if (numberOfMatchingBeans == 1)
         {
            ControllerContext context = contexts.iterator().next();
            // todo - should we do this?
            controller.change(context, state);
            result = context.getTarget();
         }
      }
      else if (InjectionMode.BY_NAME.equals(mode))
      {
         ControllerContext context = controller.getContext(propertyName, state);
         if (context != null)
         {
            result = context.getTarget();
         }
      }
      else
      {
         throw new IllegalArgumentException("Illegal injection mode: " + mode);
      }
      // check strict mode
      if (InjectionType.STRICT.equals(type) && result == null)
      {
         throw new Error("Should not be here, no context matches restrictions - dependency failed! " + messageObject);
      }
      return result;
   }

}
