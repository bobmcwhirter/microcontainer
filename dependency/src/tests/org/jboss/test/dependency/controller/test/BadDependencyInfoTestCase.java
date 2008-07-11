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
package org.jboss.test.dependency.controller.test;

import java.lang.reflect.Method;

import junit.framework.Test;
import org.jboss.dependency.plugins.AbstractControllerContext;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.test.dependency.controller.support.MockControllerContextActions;
import org.jboss.test.dependency.controller.support.ProxyDependencyInfo;
import org.jboss.test.dependency.controller.support.ProxyDependencyItem;

/**
 * Test bad DependencyInfo/Item.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class BadDependencyInfoTestCase extends AbstractDependencyTest
{
   private static final int numberOfInvocations = 10;

   public BadDependencyInfoTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(BadDependencyInfoTestCase.class);
   }

   public void testDependencyInfoMethods() throws Throwable
   {
      Method[] methods = DependencyInfo.class.getDeclaredMethods();
      for(int i = numberOfInvocations; i >= 0; i--)
      {
         for (Method method : methods)
         {
            // Should we suppress this?
            if ("getLifecycleCallbacks".equals(method.getName()))
               continue;
            
            AbstractControllerContext context = new AbstractControllerContext(
                  method.getName(),
                  new MockControllerContextActions(),
                  ProxyDependencyInfo.createDependencyInfo(method, i)
            );
            install(context);
            assertTrue(context.getName().toString(), ControllerState.ERROR.equals(context.getState()) || ControllerState.INSTALLED.equals(context.getState()));
            uninstall(context);            
         }
      }
   }

   public void testDependencyItemMethodsOnMe() throws Throwable
   {
      Method[] methods = DependencyItem.class.getDeclaredMethods();
      for(int i = numberOfInvocations; i >= 0; i--)
      {
         for (Method method : methods)
         {
            for(ControllerState whenRequired : getStateModel())
            {
               ControllerContext context = createControllerContext(method.getName());
               DependencyInfo info = context.getDependencyInfo();
               info.addDependsOnMe(ProxyDependencyItem.createDependencyInfo(method, i, whenRequired));
               install(context);
               assertTrue(context.getName().toString(), ControllerState.ERROR.equals(context.getState()) || ControllerState.INSTALLED.equals(context.getState()));
               uninstall(context);
            }
         }
      }
   }

   public void testDependencyItemMethodsOnThem() throws Throwable
   {
      Method[] methods = DependencyItem.class.getDeclaredMethods();
      for(int i = numberOfInvocations; i >= 0; i--)
      {
         for (Method method : methods)
         {
            for(ControllerState whenRequired : getStateModel())
            {
               ControllerContext context = createControllerContext(method.getName());
               DependencyInfo info = context.getDependencyInfo();
               info.addIDependOn(ProxyDependencyItem.createDependencyInfo(method, i, whenRequired));
               install(context);
               ControllerState previous = getStateModel().getPreviousState(whenRequired);
               assertTrue(context.getName().toString(), ControllerState.ERROR.equals(context.getState()) || previous == null || previous.equals(context.getState()));
               uninstall(context);
            }
         }
      }
   }

   public void testDependencyItemMethodsOnThemResolved() throws Throwable
   {
      ControllerContext bean = createControllerContext("bean");
      Method[] methods = DependencyItem.class.getDeclaredMethods();
      for(int i = numberOfInvocations; i >= 0; i--)
      {
         for (Method method : methods)
         {
            for(ControllerState whenRequired : getStateModel())
            {
               ControllerContext context = createControllerContext(method.getName());
               DependencyInfo info = context.getDependencyInfo();
               info.addIDependOn(ProxyDependencyItem.createDependencyInfo(method, i, whenRequired));
               install(context);
               install(bean);
               assertTrue(context.getName().toString(), ControllerState.ERROR.equals(context.getState()) || ControllerState.INSTALLED.equals(context.getState()));
               uninstall(bean);
               uninstall(context);
            }
         }
      }
   }
}
