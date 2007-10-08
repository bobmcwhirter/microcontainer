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
package org.jboss.kernel.spi.lazy;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.config.KernelConfigurator;

/**
 * Create lazy proxy of a bean.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class LazyProxyFactory
{
   private static LazyInitializer initializer;
   private static Map<String, String> initializerMap;

   static
   {
      initializerMap = new LinkedHashMap<String, String>();
      // test class name, actual LazyInitializer implementation
      initializerMap.put("javassist.util.proxy.ProxyObject", "org.jboss.kernel.plugins.lazy.JavassistLazyInitializer");
      initializerMap.put("org.jboss.aop.proxy.container.AOPProxyFactoryParameters", "org.jboss.aop.microcontainer.lazy.JBossAOPLazyInitializer");
      initializerMap.put("java.lang.reflect.Proxy", "org.jboss.kernel.plugins.lazy.JDKLazyInitializer");
   }

   /**
    * Get the LazyInitializater instance.
    *
    * @param configurator the configurator
    * @return initializer instance
    */
   protected static LazyInitializer getInitializer(KernelConfigurator configurator)
   {
      if (initializer == null)
      {
         for(Map.Entry<String, String> entry : initializerMap.entrySet())
         {
            if (testLibExists(entry.getKey()))
            {
               initializer = createInitializer(configurator, entry.getValue());
               if (initializer != null)
                  break;
            }
         }
      }
      if (initializer == null)
         throw new IllegalArgumentException("Cannot initialize LazyInitializater, check classpath for missing classes.");
      return initializer;
   }

   /**
    * Check if test class exists.
    *
    * @param className class name to test
    * @return true if test successful, false otherwise
    */
   protected static boolean testLibExists(String className)
   {
      try
      {
         return Class.forName(className) != null;
      }
      catch (ClassNotFoundException e)
      {
         return false;
      }
   }

   /**
    * Create initializer instance.
    *
    * @param configurator the configurator
    * @param initializerClassName initializer class name
    * @return initializer instance or null if we fail
    */
   protected static LazyInitializer createInitializer(KernelConfigurator configurator, String initializerClassName)
   {
      try
      {
         BeanInfo beanInfo = configurator.getBeanInfo(initializerClassName, LazyInitializer.class.getClassLoader());
         Object result = beanInfo.newInstance();
         return LazyInitializer.class.cast(result);
      }
      catch (Throwable ignored)
      {
      }
      return null;
   }

   /**
    * Create lazy proxy.
    *
    * @param kernel the kernel
    * @param bean the bean to wrap
    * @param exposeClass do we expose full class
    * @param interfaces interfaces to expose
    * @return the proxy
    * @throws Throwable for any error
    */
   public static Object getProxy(Kernel kernel, String bean, boolean exposeClass, Set<String> interfaces) throws Throwable
   {
      LazyInitializer lazyInitializer = getInitializer(kernel.getConfigurator());
      return lazyInitializer.initializeProxy(kernel, bean, exposeClass, interfaces);
   }
}
