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

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Set;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.registry.KernelBus;

/**
 * Javassist lazy initializer.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class JavassistLazyInitializer extends AbstractLazyInitializer
{
   @SuppressWarnings("deprecation")
   public Object initializeProxy(Kernel kernel, String bean, boolean exposeClass, Set<String> interfaces) throws Throwable
   {
      KernelControllerContext context = getKernelControllerContext(kernel, bean);
      BeanMetaData bmd = context.getBeanMetaData();
      KernelConfigurator configurator = kernel.getConfigurator();
      ClassLoader cl = Configurator.getClassLoader(bmd);
      Class<?> beanClass = getBeanClass(context, configurator, cl);

      ProxyFactory factory = new ProxyFactory();
      factory.setFilter(FINALIZE_FILTER);
      if (exposeClass)
      {
         factory.setSuperclass(beanClass);
      }
      if (interfaces != null && interfaces.size() > 0)
      {
         factory.setInterfaces(getClasses(configurator, interfaces, cl));
      }
      Class<?> proxyClass = getProxyClass(factory);
      ProxyObject proxy = (ProxyObject)proxyClass.newInstance();
      proxy.setHandler(new LazyHandler(bean, kernel.getBus(), beanClass));
      return proxy;
   }

   protected Class<?> getProxyClass(ProxyFactory factory)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm == null)
         return factory.createClass();
      else
         return AccessController.doPrivileged(new ClassCreator(factory));
   }

   private static final MethodFilter FINALIZE_FILTER = new MethodFilter()
   {
      public boolean isHandled(Method m)
      {
         // skip finalize methods
         return !("finalize".equals(m.getName()) && m.getParameterTypes().length == 0);
      }
   };

   /**
    * Lazy method handler.
    */
   public class LazyHandler extends AbstractInvokeHandler implements MethodHandler
   {
      public LazyHandler(String bean, KernelBus bus, Class<?> proxyClass)
      {
         super(bean, bus, proxyClass);
      }

      public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable
      {
         return executeInvoke(self, thisMethod, args);
      }
   }

   /**
    * Privileged class creator.
    */
   protected class ClassCreator implements PrivilegedAction<Class<?>>
   {
      private ProxyFactory factory;

      public ClassCreator(ProxyFactory factory)
      {
         this.factory = factory;
      }

      public Class<?> run()
      {
         return factory.createClass();
      }
   }
}
