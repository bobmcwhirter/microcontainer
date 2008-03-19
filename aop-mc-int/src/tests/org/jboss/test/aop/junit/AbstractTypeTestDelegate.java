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
package org.jboss.test.aop.junit;

import java.util.Map;
import java.util.HashMap;

import org.jboss.kernel.plugins.bootstrap.AbstractBootstrap;
import org.jboss.kernel.plugins.bootstrap.basic.BasicBootstrap;
import org.jboss.kernel.plugins.config.property.PropertyKernelConfig;
import org.jboss.kernel.plugins.dependency.AbstractKernelController;
import org.jboss.kernel.plugins.dependency.InstantiateAction;
import org.jboss.kernel.spi.config.KernelConfig;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.Kernel;
import org.jboss.test.kernel.junit.MicrocontainerTestDelegate;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.aop.proxy.container.AspectManaged;
import org.jboss.aop.Advised;

/**
 * AbstractTypeTestDelegate.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class AbstractTypeTestDelegate extends MicrocontainerTestDelegate
{
   /** Is proxy, woven or pojo map -> <bean name, type> */
   private Map<Object, Type> types = new HashMap<Object, Type>();

   public AbstractTypeTestDelegate(Class<?> clazz) throws Exception
   {
      super(clazz);
   }

   protected AbstractBootstrap getBootstrap() throws Exception
   {
      return new BasicBootstrap(createKernelConfig());
   }

   Map<Object, Type> getTypes()
   {
      return types;
   }

   Kernel getKernel()
   {
      return kernel;
   }

   protected KernelConfig createKernelConfig()
   {
      return new TestKernelConfig();
   }

   private class TestKernelConfig extends PropertyKernelConfig
   {
      public TestKernelConfig()
      {
         super(null);
      }

      public KernelController createKernelController() throws Throwable
      {
         return new TestController();
      }
   }

   private class TestController extends AbstractKernelController
   {
      private TestInstantiateAction instantiate = new TestInstantiateAction();

      public TestController() throws Exception
      {
      }

      protected void install(ControllerContext context, ControllerState fromState, ControllerState toState) throws Throwable
      {
         if (ControllerState.INSTANTIATED.equals(toState))
            instantiate.install(context);
         else
            super.install(context, fromState, toState);
      }

      protected void uninstall(ControllerContext context, ControllerState fromState, ControllerState toState)
      {
         if (ControllerState.INSTANTIATED.equals(fromState))
            instantiate.uninstall(context);
         else
            super.uninstall(context, fromState, toState);
      }
   }

   private class TestInstantiateAction extends InstantiateAction
   {
      protected void installActionInternal(KernelControllerContext context) throws Throwable
      {
         super.installActionInternal(context);
         checkTargetType(context.getName(), context.getTarget());
      }

      protected void uninstallActionInternal(KernelControllerContext context)
      {
         types.remove(context.getName());
         super.uninstallActionInternal(context);
      }
   }

   protected void checkTargetType(Object name, Object target) throws Throwable
   {
      Type type;
      if (target instanceof AspectManaged)
      {
         type = Type.PROXY;
      }
      else if (target instanceof Advised)
      {
         type = Type.WOVEN;
      }
      else
      {
         type = Type.POJO;
      }
      types.put(name, type);
   }

   public enum Type
   {
      PROXY,
      WOVEN,
      POJO
   }
}
