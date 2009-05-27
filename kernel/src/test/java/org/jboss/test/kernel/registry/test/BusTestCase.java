/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.kernel.registry.test;

import java.util.Date;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.dependency.AbstractKernelControllerContext;
import org.jboss.kernel.plugins.registry.basic.LifecycleAwareKernelBus;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.registry.KernelBus;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.test.kernel.AbstractKernelTest;
import org.jboss.test.kernel.registry.support.BusBean;

/**
 * Bus Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
@SuppressWarnings("deprecation")
public class BusTestCase extends AbstractKernelTest
{
   public static Test suite()
   {
      return suite(BusTestCase.class);
   }
   
   public BusTestCase(String name)
   {
      super(name);
   }

   public void testSetAndGet() throws Throwable
   {
      Kernel kernel = bootstrap();
      org.jboss.kernel.spi.registry.KernelRegistry registry = kernel.getRegistry();
      KernelConfigurator configurator = kernel.getConfigurator();
      registry.registerEntry("Bus", makeContext(configurator, "Name1", new BusBean()));

      KernelBus bus = kernel.getBus();
      Object result1 = bus.get("Bus", "value");
      assertNull("Result 1", result1);
      bus.set("Bus", "value", "BusBus");
      Object result2 = bus.get("Bus", "value");
      assertEquals("BusBus", result2);
   }

   public void testInvoke() throws Throwable
   {
      Kernel kernel = bootstrap();
      org.jboss.kernel.spi.registry.KernelRegistry registry = kernel.getRegistry();
      KernelConfigurator configurator = kernel.getConfigurator();

      KernelRegistryEntry entry1 = makeContext(configurator, "Name1", "A string");
      entry1.setState(ControllerState.INSTALLED);
      registry.registerEntry("Name1", entry1);

      KernelRegistryEntry entry2 = makeContext(configurator, "Name2", "B string");
      entry2.setState(ControllerState.INSTALLED);
      registry.registerEntry("Name2", entry2);

      KernelBus bus = kernel.getBus();
      Object result1 = bus.invoke("Name1", "toString", new Object[]{}, new String[]{});
      Object result2 = bus.invoke("Name2", "toString", new Object[]{}, new String[]{});
      assertEquals("A string", result1);
      assertEquals("B string", result2);
   }

   public void testLifecycle() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();

      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder("Name1", BusBean.class.getName());
      builder.addStartParameter(String.class.getName(), "123-Start");
      builder.setStop("doStop");
      builder.setDestroy("executeDestroy");
      builder.addDestroyParameter("int", 123);
      builder.addDestroyParameter(Date.class.getName(), new Date());

      KernelControllerContext context = controller.install(builder.getBeanMetaData());
      assertEquals(ControllerState.INSTALLED, context.getState());

      KernelBus bus = kernel.getBus();
      assertInstanceOf(bus, LifecycleAwareKernelBus.class);

      bus.invoke("Name1", "executeDestroy", new Object[]{-1, new Date()}, new String[]{int.class.getName(), Date.class.getName()});
      assertEquals(ControllerState.CONFIGURED, context.getState());

      bus.invoke("Name1", "create", null, null);
      assertEquals(ControllerState.CREATE, context.getState());

      bus.invoke("Name1", "start", new Object[]{"foobar"}, new String[]{String.class.getName()});
      assertEquals(ControllerState.START, context.getState());

      bus.invoke("Name1", "doStop", null, null);
      assertEquals(ControllerState.CREATE, context.getState());     
   }

   protected static KernelRegistryEntry makeContext(KernelConfigurator configurator, String name, Object target)
         throws Throwable
   {
      return new AbstractKernelControllerContext(
            configurator.getBeanInfo(target.getClass()),
            new AbstractBeanMetaData(name, target.getClass().getName()),
            target
      );
   }
}