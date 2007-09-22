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
package org.jboss.kernel.plugins.registry.basic;

import org.jboss.dependency.spi.dispatch.AttributeDispatchContext;
import org.jboss.dependency.spi.dispatch.InvokeDispatchContext;
import org.jboss.kernel.plugins.registry.AbstractKernelBus;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;

/**
 * Basic Kernel bus.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class BasicKernelBus extends AbstractKernelBus
{
   /**
    * Create a new basic bus
    * 
    * @throws Exception for any error
    */
   public BasicKernelBus() throws Exception
   {
   }

   /**
    * Execute dispatch.
    *
    * @param <T> exact context type
    * @param name the entry name
    * @param clazz the context class
    * @param dispatcher the dispatcher
    * @return dispatcher's result
    * @throws Throwable for any error
    */
   protected <T> Object execute(Object name, Class<T> clazz, Dispatcher<T> dispatcher) throws Throwable
   {
      KernelRegistryEntry entry = registry.getEntry(name);
      if (entry == null)
         throw new IllegalArgumentException("No such entry: " + name);
      if (clazz.isAssignableFrom(entry.getClass()) == false)
         throw new IllegalArgumentException("Cannot execute " + dispatcher + " on non " + clazz.getSimpleName() + " entry: " + entry);
      return dispatcher.dispatch(clazz.cast(entry));
   }

   public Object get(Object name, final String getter) throws Throwable
   {
      return execute(name, AttributeDispatchContext.class, new Dispatcher<AttributeDispatchContext>()
      {
         public Object dispatch(AttributeDispatchContext context) throws Throwable
         {
            return context.get(getter);
         }

         public String toString()
         {
            return "get";
         }
      });
   }

   public void set(Object name, final String setter, final Object value) throws Throwable
   {
      execute(name, AttributeDispatchContext.class, new Dispatcher<AttributeDispatchContext>()
      {
         public Object dispatch(AttributeDispatchContext context) throws Throwable
         {
            context.set(setter, value);
            return null;
         }

         public String toString()
         {
            return "set";
         }
      });
   }

   public Object invoke(Object name, final String methodName, final Object parameters[], final String[] signature) throws Throwable
   {
      return execute(name, InvokeDispatchContext.class, new Dispatcher<InvokeDispatchContext>()
      {
         public Object dispatch(InvokeDispatchContext context) throws Throwable
         {
            return context.invoke(methodName, parameters, signature);
         }

         public String toString()
         {
            return "invoke";
         }
      });
   }

   /**
    * Simple dispatch on context.
    *
    * @param <T> exact context type
    */
   private interface Dispatcher<T>
   {
      /**
       * Invoke simple dispatcher.
       *
       * @param context the context
       * @throws Throwable for any error
       * @return
       */
      Object dispatch(T context) throws Throwable;
   }
}
