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
 * @version $Revision$
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

   public Object get(Object name, String getter) throws Throwable
   {
      KernelRegistryEntry entry = registry.getEntry(name);
      if (entry instanceof AttributeDispatchContext)
      {
         AttributeDispatchContext dispatcher = (AttributeDispatchContext)entry;
         return dispatcher.get(getter);
      }
      else
         throw new IllegalArgumentException("Cannot execute get on non AttributeDispatchContext entry: " + entry);
   }

   public void set(Object name, String setter, Object value) throws Throwable
   {
      KernelRegistryEntry entry = registry.getEntry(name);
      if (entry instanceof AttributeDispatchContext)
      {
         AttributeDispatchContext dispatcher = (AttributeDispatchContext)entry;
         dispatcher.set(setter, value);
      }
      else
         throw new IllegalArgumentException("Cannot execute set on non AttributeDispatchContext entry: " + entry);
   }

   public Object invoke(Object name, String methodName, Object parameters[], String[] signature) throws Throwable
   {
      KernelRegistryEntry entry = registry.getEntry(name);
      if (entry instanceof InvokeDispatchContext)
      {
         InvokeDispatchContext dispatcher = (InvokeDispatchContext)entry;
         return dispatcher.invoke(methodName, parameters, signature);
      }
      else
         throw new IllegalArgumentException("Cannot execute invoke on non InvokeDispatchContext entry: " + entry);
   }
}
