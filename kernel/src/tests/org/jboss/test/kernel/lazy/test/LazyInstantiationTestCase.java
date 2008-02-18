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
package org.jboss.test.kernel.lazy.test;

import java.util.Collections;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractLazyMetaData;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.AbstractTestDelegate;
import org.jboss.test.kernel.AbstractKernelTest;
import org.jboss.test.kernel.lazy.support.IRare;
import org.jboss.test.kernel.lazy.support.RareBean;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class LazyInstantiationTestCase extends AbstractKernelTest
{
   public LazyInstantiationTestCase(String name)
   {
      super(name);
   }

   /**
    * Default setup w/o security manager enabled
    *
    * @param clazz the class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      return new AbstractTestDelegate(clazz);
   }

   protected abstract String getFactoryClassName();

   public void testLazy() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();

      AbstractBeanMetaData bean = new AbstractBeanMetaData("bean", RareBean.class.getName());
      bean.setMode(ControllerMode.MANUAL);

      KernelControllerContext beanContext = controller.install(bean);
      controller.change(beanContext, ControllerState.NOT_INSTALLED);

      ModifiedLazyMetaData lazy = new ModifiedLazyMetaData("bean", getFactoryClassName());
      lazy.setInterfaces(Collections.singleton(IRare.class.getName()));
      KernelControllerContext lazyContext = controller.install(lazy);

      assertNotNull(lazyContext);
      assertEquals(ControllerState.DESCRIBED, lazyContext.getState());

      controller.change(beanContext, ControllerState.DESCRIBED);
      controller.change(lazyContext, ControllerState.INSTALLED);

      IRare lazyRare = (IRare)lazyContext.getTarget();
      assertNotNull(lazyRare);

      try
      {
         lazyRare.getHits();
         throw new RuntimeException("Should not be here.");
      }
      catch(Throwable t)
      {
         assertInstanceOf(t, IllegalArgumentException.class);
      }

      controller.change(beanContext, ControllerState.INSTALLED);

      assertEquals(0, lazyRare.getHits());
      lazyRare.setHits(10);
      assertEquals(5, lazyRare.checkHits(15));

      controller.change(beanContext, ControllerState.PRE_INSTALL);
      assertEquals(ControllerState.DESCRIBED, lazyContext.getState());
   }

   private class ModifiedLazyMetaData extends AbstractLazyMetaData
   {
      /** The serialVersionUID */
      private static final long serialVersionUID = 1L;

      public ModifiedLazyMetaData(String beanName, String factoryClassName)
      {
         super(beanName);
         setFactoryClassName(factoryClassName);
      }
   }
}
