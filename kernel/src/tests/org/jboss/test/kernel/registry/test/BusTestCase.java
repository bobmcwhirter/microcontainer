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

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.dependency.AbstractKernelControllerContext;
import org.jboss.kernel.spi.registry.KernelBus;
import org.jboss.kernel.spi.registry.KernelRegistry;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.test.kernel.AbstractKernelTest;
import org.jboss.test.kernel.registry.support.BusBean;

/**
 * Bus Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
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
      KernelRegistry registry = kernel.getRegistry();
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
      KernelRegistry registry = kernel.getRegistry();
      KernelConfigurator configurator = kernel.getConfigurator();
      registry.registerEntry("Name1", makeContext(configurator, "Name1", "A string"));
      registry.registerEntry("Name2", makeContext(configurator, "Name2", "B string"));
      KernelBus bus = kernel.getBus();
      Object result1 = bus.invoke("Name1", "toString", new Object[]{}, new String[]{});
      Object result2 = bus.invoke("Name2", "toString", new Object[]{}, new String[]{});
      assertEquals("A string", result1);
      assertEquals("B string", result2);
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