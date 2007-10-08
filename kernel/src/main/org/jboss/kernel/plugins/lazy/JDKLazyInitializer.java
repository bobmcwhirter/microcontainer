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
package org.jboss.kernel.plugins.lazy;

import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Set;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.registry.KernelBus;

/**
 * Java JDK lazy initializer.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class JDKLazyInitializer extends AbstractLazyInitializer
{
   public Object initializeProxy(Kernel kernel, String bean, boolean exposeClass, Set<String> interfaces) throws Throwable
   {
      if (exposeClass)
         throw new IllegalArgumentException("Cannot expose class via JDK LazyInitializer.");
      if (interfaces == null || interfaces.isEmpty())
         throw new IllegalArgumentException("Null interfaces.");

      KernelControllerContext context = getKernelControllerContext(kernel, bean);
      BeanInfo beanInfo = context.getBeanInfo();
      if (beanInfo == null)
         throw new IllegalArgumentException("Cannot proxy factory beans.");
      LazyHandler lazyHandler = new LazyHandler(bean, kernel.getBus(), beanInfo.getClassInfo().getType());
      ClassLoader cl = Configurator.getClassLoader(context.getBeanMetaData());
      return Proxy.newProxyInstance(getClass().getClassLoader(), getClasses(kernel.getConfigurator(), interfaces, cl), lazyHandler);
   }

   /**
    * Lazy invocation handler.
    */
   public class LazyHandler extends AbstractInvokeHandler implements InvocationHandler
   {
      public LazyHandler(String bean, KernelBus bus, Class proxyClass)
      {
         super(bean, bus, proxyClass);
      }

      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
      {
         return executeInvoke(proxy, method, args);
      }
   }
}
