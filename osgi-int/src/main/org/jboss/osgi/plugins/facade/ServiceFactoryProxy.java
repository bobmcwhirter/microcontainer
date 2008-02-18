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
package org.jboss.osgi.plugins.facade;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import org.osgi.framework.ServiceFactory;

/**
 * An implementation of InvocationHandler used to proxy of the ServiceFactory
 * interface for OSGi services.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ServiceFactoryProxy implements InvocationHandler
{
   public static final String GET_SERVICE = "getService";
   public static final String UNGET_SERVICE = "ungetService";

/*
   private static final Logger log = Logger.getLogger(ServiceFactoryProxy.class);

   public static Method GET_SERVICE_METHOD;
   static
   {
      try
      {
         GET_SERVICE_METHOD = ServiceFactory.class.getMethod(GET_SERVICE, Bundle.class, ServiceRegistration.class);
      }
      catch (NoSuchMethodException e)
      {
         log.error("Unable to prepare getService method.", e);        
      }
   }
*/
   protected Object serviceFactory;
   protected Class<?>[] interfaces;
   protected Object service;
   protected Boolean checked;

   private ServiceFactoryProxy(Object serviceFactory, Class<?>[] interfaces)
   {
      this.serviceFactory = serviceFactory;
      this.interfaces = interfaces;
   }

   public static Object createProxy(Object serviceFactory, Class<?>[] interfaces)
   {
      int lenght = interfaces.length;
      Class<?>[] allIntefaces = new Class[lenght + 1];
      System.arraycopy(interfaces, 0, allIntefaces, 0, lenght);
      allIntefaces[lenght] = ServiceFactory.class;
      InvocationHandler handler = new ServiceFactoryProxy(serviceFactory, interfaces);
      return Proxy.newProxyInstance(ServiceFactory.class.getClassLoader(), allIntefaces, handler);
   }

   protected boolean checkInterfaces()
   {
      // already checked
      if (checked != null)
         return checked;
      // can be null
      if (service == null)
         return true;

      Class<?> serviceInterface = service.getClass();
      for(Class<?> exposedInterface : interfaces)
      {
         if (exposedInterface.isAssignableFrom(serviceInterface) == false)
         {
            return (checked = false);
         }
      }
      return (checked = true);
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
   {
      String methodName = method.getName();
      if (GET_SERVICE.equals(methodName))
      {
         service = method.invoke(serviceFactory, args);
         if (checkInterfaces() == false)
            return null;
         return service;
      }
      else if (UNGET_SERVICE.equals(methodName))
      {
         service = null;
         checked = null;
         return method.invoke(serviceFactory, args);
      }
      else
      {
         if (service == null)
            throw new IllegalStateException("Service not yet created from ServiceFactory!"); // todo - or create it now?
         if (checkInterfaces() == false)
            throw new IllegalArgumentException("Illegal exposed interfaces: " + service + "; " + Arrays.asList(interfaces));
         return method.invoke(service, args);
      }
   }
}
