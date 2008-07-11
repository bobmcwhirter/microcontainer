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
package org.jboss.test.dependency.controller.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.jboss.dependency.plugins.AbstractDependencyInfo;
import org.jboss.dependency.spi.DependencyInfo;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ProxyDependencyInfo extends AbstractDependencyInfo implements InvocationHandler
{
   private Method method;
   private int allowedHits;

   public ProxyDependencyInfo(Method method, int allowedHits)
   {
      this.method = method;
      this.allowedHits = allowedHits;
   }

   public static DependencyInfo createDependencyInfo(Method method, int allowedHits)
   {
      return (DependencyInfo)Proxy.newProxyInstance(
            DependencyInfo.class.getClassLoader(),
            new Class<?>[]{DependencyInfo.class},
            new ProxyDependencyInfo(method, allowedHits));
   }

   public Object invoke(Object proxy, Method m, Object[] args) throws Throwable
   {
      if (m.equals(method))
      {
         if (allowedHits <= 0)
            throw new RuntimeException("Failed");

         allowedHits--;
      }

      return m.invoke(this, args);
   }
}
