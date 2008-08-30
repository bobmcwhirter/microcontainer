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
package org.jboss.kernel.plugins.registry;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.dependency.spi.dispatch.InvokeDispatchContext;

/**
 * Bean Kernel registry entry.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class BeanKernelRegistryEntry extends AbstractKernelRegistryEntry implements InvokeDispatchContext
{
   private BeanInfo beanInfo;

   public BeanKernelRegistryEntry(Object target, BeanInfo beanInfo)
   {
      super(target);
      if (beanInfo == null)
         throw new IllegalArgumentException("Null bean info.");
      this.beanInfo = beanInfo;
   }

   public BeanKernelRegistryEntry(Object name, Object target, BeanInfo beanInfo)
   {
      super(name, target);
      this.beanInfo = beanInfo;
   }

   public Object get(String name) throws Throwable
   {
      return beanInfo.getProperty(target, name);
   }

   public void set(String name, Object value) throws Throwable
   {
      beanInfo.setProperty(target, name, value);
   }

   public Object invoke(String name, Object parameters[], String[] signature) throws Throwable
   {
      return beanInfo.invoke(target, name, signature, parameters);
   }

   public ClassLoader getClassLoader() throws Throwable
   {
      if (target == null)
         throw new IllegalArgumentException("Cannot get classloader, target is null.");

      // this already checks for permission
      return target.getClass().getClassLoader();
   }
}
