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

import org.jboss.dependency.plugins.AbstractDependencyItem;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ProxyDependencyItem extends AbstractDependencyItem implements InvocationHandler
{
   private Method method;
   private int allowedHits;

   public ProxyDependencyItem(Method method, int allowedHits, ControllerState whenRequired)
   {
      super(method.getName(), "bean", whenRequired, null);
      this.method = method;
      this.allowedHits = allowedHits;
   }

   public static DependencyItem createDependencyInfo(Method method, int allowedHits, ControllerState whenRequired)
   {
      return (DependencyItem)Proxy.newProxyInstance(
            DependencyItem.class.getClassLoader(),
            new Class<?>[]{DependencyItem.class},
            new ProxyDependencyItem(method, allowedHits, whenRequired));
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