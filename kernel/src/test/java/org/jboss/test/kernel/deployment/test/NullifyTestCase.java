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
package org.jboss.test.kernel.deployment.test;

import java.util.Map;

import junit.framework.Test;
import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.bootstrap.basic.BasicBootstrap;
import org.jboss.kernel.plugins.config.property.PropertyKernelConfig;
import org.jboss.kernel.plugins.dependency.AbstractKernelController;
import org.jboss.kernel.plugins.dependency.ConfigureAction;
import org.jboss.kernel.spi.config.KernelConfig;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.BaseTestCase;
import org.jboss.test.kernel.deployment.support.NullifyTestBean1;

/**
 * NullifyTestCase
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class NullifyTestCase extends BaseTestCase
{
   private KernelController controller;
   private ThrowableConfigureAction describe = new ThrowableConfigureAction();

   public NullifyTestCase(String name) throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(NullifyTestCase.class);
   }

   public void testNullifying() throws Throwable
   {
      KernelController controller = getController();
      describe.error = false;

      AbstractBeanMetaData abmd = new AbstractBeanMetaData("Bean1", NullifyTestBean1.class.getName());
      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder(abmd);
      AbstractPropertyMetaData property = new AbstractPropertyMetaData("something", builder.createMap(null, String.class.getName(), String.class.getName()));
      property.setPropertyType(Map.class.getName());
      abmd.addProperty(property);

      KernelControllerContext context = controller.install(builder.getBeanMetaData());
      controller.change(context, ControllerState.INSTANTIATED);
      assertEquals(ControllerState.INSTANTIATED, context.getState());
      assertFalse(describe.error);
   }

   // -----------------------

   private class ThrowableConfigureAction extends ConfigureAction
   {
      public boolean error;

      @Override
      protected void setAttributes(KernelControllerContext context, Object target, BeanInfo info, BeanMetaData metaData, boolean nullify) throws Throwable
      {
         try
         {
            super.setAttributes(context, target, info, metaData, nullify);
         }
         catch (Throwable t)
         {
            error = true;
            throw t;
         }
      }
   }

   protected void setUp() throws Exception
   {
      super.setUp();

      controller = createController();
   }

   protected void tearDown() throws Exception
   {
      if (controller != null)
         controller.shutdown();
      controller = null;

      super.tearDown();
   }

   protected KernelController createController() throws Exception
   {
      // bootstrap
      KernelConfig config = createKernelConfig();
      BasicBootstrap bootstrap = config != null ? new BasicBootstrap(config) : new BasicBootstrap();
      bootstrap.run();
      Kernel kernel = bootstrap.getKernel();
      return kernel.getController();
   }

   protected KernelConfig createKernelConfig()
   {
      return new TestKernelConfig();
   }

   protected KernelController getController()
   {
      return controller;
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
      public TestController() throws Exception
      {
      }

      protected void install(ControllerContext context, ControllerState fromState, ControllerState toState) throws Throwable
      {
         if (ControllerState.CONFIGURED.equals(toState) && (context instanceof KernelControllerContext))
            describe.install(context);
         else
            super.install(context, fromState, toState);
      }

      protected void uninstall(ControllerContext context, ControllerState fromState, ControllerState toState)
      {
         if (ControllerState.CONFIGURED.equals(fromState) && (context instanceof KernelControllerContext))
            describe.uninstall(context);
         else
            super.uninstall(context, fromState, toState);
      }
   }
}