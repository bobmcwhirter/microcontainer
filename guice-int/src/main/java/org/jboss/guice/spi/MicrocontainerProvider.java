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
package org.jboss.guice.spi;

import com.google.inject.Provider;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.beans.metadata.spi.factory.BeanFactory;

/**
 * Microcontainer provider.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
class MicrocontainerProvider<T> implements Provider<T>
{
   private Class<T> type;
   private Object name;
   private Controller controller;

   protected MicrocontainerProvider(Class<T> type, Object name)
   {
      if (type == null)
         throw new IllegalArgumentException("Null type.");
      if (name == null)
         throw new IllegalArgumentException("Null name.");

      this.type = type;
      this.name = name;
   }

   void initialize(Controller controller)
   {
      this.controller = controller;
   }

   public T get()
   {
      if (controller == null)
         throw new IllegalArgumentException("Null controller, missing initialization.");

      ControllerContext context = controller.getInstalledContext(name);
      if (context != null)
      {
         Object target = context.getTarget();
         if (target instanceof BeanFactory)
         {
            try
            {
               target = ((BeanFactory)target).createBean();
            }
            catch (Throwable t)
            {
               throw new RuntimeException(t);
            }
         }
         return type.cast(target);
      }
      return null;
   }
}
