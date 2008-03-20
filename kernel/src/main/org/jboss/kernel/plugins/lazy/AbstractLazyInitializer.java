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
import java.util.Set;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.lazy.LazyInitializer;
import org.jboss.kernel.spi.registry.KernelBus;
import org.jboss.util.JBossStringBuilder;

/**
 * Abstract lazy initializer.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractLazyInitializer implements LazyInitializer
{
   /**
    * Abstract invoke handler.
    */
   protected abstract class AbstractInvokeHandler
   {
      private String bean;
      private KernelBus bus;
      private Class<?> proxyClass;

      protected AbstractInvokeHandler(String bean, KernelBus bus, Class<?> proxyClass)
      {
         if (bean == null)
            throw new IllegalArgumentException("Null bean name.");
         this.bean = bean;
         if (bus == null)
            throw new IllegalArgumentException("Null Kernel bus.");
         this.bus = bus;
         this.proxyClass = proxyClass;
      }

      /**
       * Excute invoke.
       *
       * @param proxy the proxy
       * @param method the method
       * @param args the arguments
       * @return  result
       * @throws Throwable for any error
       */
      protected Object executeInvoke(Object proxy, Method method, Object[] args) throws Throwable
      {
         String methodName = method.getName();

         if ("hashCode".equals(methodName))
            return System.identityHashCode(proxy);
         if ("getClass".equalsIgnoreCase(methodName))
            return proxyClass;
         if ("equals".equals(methodName))
            return proxy == args[0];
         if ("toString".equals(methodName))
            return bean + "Proxy";

         if (isGetter(method))
         {
            return bus.get(bean, getLowerPropertyName(methodName));
         }
         else if (isSetter(method))
         {
            bus.set(bean, getLowerPropertyName(methodName), args[0]);
            return null;
         }
         else
         {
            int length = args != null ? args.length : 0;
            return bus.invoke(bean, methodName, args, new String[length]);
         }
      }
   }

   /**
    * Is getter.
    *
    * @param minfo method
    * @return true if metod is getter
    */
   protected static boolean isGetter(Method minfo)
   {
      String name = minfo.getName();
      Class<?> returnType = minfo.getReturnType();
      Class<?>[] parameters = minfo.getParameterTypes();
      if ((name.length() > 3 && name.startsWith("get")) || (name.length() > 2 && name.startsWith("is")))
      {
         // isBoolean() is not a getter for java.lang.Boolean
         if (name.startsWith("is") && Boolean.TYPE.equals(returnType) == false)
            return false;
         if (parameters.length == 0 && Void.TYPE.equals(returnType) == false)
            return true;
      }
      return false;
   }

   /**
    * Is setter.
    *
    * @param minfo method
    * @return true if method is setter
    */
   protected static boolean isSetter(Method minfo)
   {
      String name = minfo.getName();
      Class<?> returnType = minfo.getReturnType();
      Class<?>[] parameters = minfo.getParameterTypes();
      if ((name.length() > 3 && name.startsWith("set")))
      {
         if (parameters.length == 1 && Void.TYPE.equals(returnType))
            return true;
      }
      return false;
   }

   /**
    * Get lower property name.
    *
    * @param name the name
    * @return lower case property name
    */
   protected static String getLowerPropertyName(String name)
   {
      int start = name.startsWith("is") ? 2 : 3;
      name = name.substring(start);

      // If the second character is upper case then we don't make
      // the first character lower case
      if (name.length() > 1)
      {
         if (Character.isUpperCase(name.charAt(1)))
            return name;
      }

      JBossStringBuilder buffer = new JBossStringBuilder(name.length());
      buffer.append(Character.toLowerCase(name.charAt(0)));
      if (name.length() > 1)
         buffer.append(name.substring(1));
      return buffer.toString();
   }

   /**
    * Get kernel controller context.
    *
    * @param kernel the kernel
    * @param bean bean name
    * @return  kernel controller context
    * @throws IllegalArgumentException if context not found or not KernelControllerContext
    */
   protected KernelControllerContext getKernelControllerContext(Kernel kernel, String bean)
   {
      Controller controller = kernel.getController();
      ControllerContext context = controller.getContext(bean, ControllerState.DESCRIBED);
      if (context == null)
         throw new IllegalArgumentException("Should not be here, dependency failed.");
      if (context instanceof KernelControllerContext == false)
         throw new IllegalArgumentException("Context not KernelControllerContext: " + context);
      return KernelControllerContext.class.cast(context);
   }

   /**
    * Get bean info.
    *
    * @param kernel the kernel
    * @param bean the bean name
    * @return bean info instance
    */
   protected BeanInfo getBeanInfo(Kernel kernel, String bean)
   {
      KernelControllerContext context = getKernelControllerContext(kernel, bean);
      return context.getBeanInfo();
   }

   /**
    * Get classes from class names.
    *
    * @param configurator the configurator
    * @param classNames the class names
    * @param cl classloader
    * @return  array of classes
    * @throws Throwable for any error
    */
   @SuppressWarnings("deprecation")
   protected static Class<?>[] getClasses(KernelConfigurator configurator, Set<String> classNames, ClassLoader cl)
         throws Throwable
   {
      Class<?>[] classes = new Class[classNames.size()];
      int i = 0;
      for (String className : classNames)
      {
         classes[i] = configurator.getClassInfo(className, cl).getType();
      }
      return classes;
   }
}
