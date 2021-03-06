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
package org.jboss.test.kernel.annotations.test;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.plugins.annotations.BeanAnnotationAdapter;
import org.jboss.kernel.plugins.config.property.PropertyKernelConfig;
import org.jboss.kernel.plugins.dependency.AbstractKernelController;
import org.jboss.kernel.plugins.dependency.DescribeAction;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.config.KernelConfig;

/**
 * Abstract bean annotation adapter test.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractBeanAnnotationAdapterTest extends AbstractRunAnnotationsTest
{
   protected AbstractBeanAnnotationAdapterTest(String name)
   {
      super(name);
   }

   protected abstract BeanAnnotationAdapter getBeanAnnotationAdapterClass();

   protected Set<Annotation> getAnnotations()
   {
      return Collections.emptySet();
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
      private TestDescribeAction describe = new TestDescribeAction();

      public TestController() throws Exception
      {
      }

      protected void install(ControllerContext context, ControllerState fromState, ControllerState toState) throws Throwable
      {
         if (ControllerState.DESCRIBED.equals(toState) && (context instanceof KernelControllerContext))
            describe.install(context);
         else
            super.install(context, fromState, toState);
      }

      protected void uninstall(ControllerContext context, ControllerState fromState, ControllerState toState)
      {
         if (ControllerState.DESCRIBED.equals(fromState) && (context instanceof KernelControllerContext))
            describe.uninstall(context);
         else
            super.uninstall(context, fromState, toState);
      }
   }

   private class TestDescribeAction extends DescribeAction
   {
      protected BeanAnnotationAdapter getBeanAnnotationAdapter()
      {
         return getBeanAnnotationAdapterClass();
      }
   }
}
