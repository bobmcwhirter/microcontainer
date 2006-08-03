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

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.joinpoint.plugins.Config;
import org.jboss.joinpoint.spi.JoinpointFactory;
import org.jboss.joinpoint.spi.MethodJoinpoint;
import org.jboss.joinpoint.spi.TargettedJoinpoint;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.config.KernelConfig;
import org.jboss.kernel.spi.registry.KernelBus;
import org.jboss.kernel.spi.registry.KernelRegistry;
import org.jboss.test.kernel.AbstractKernelTest;

/**
 * Bus Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
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

   public void testInvoke() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelRegistry registry = kernel.getRegistry();
      KernelConfig config = kernel.getConfig(); 
      registry.registerEntry("Name1", makeEntry("A string"));
      registry.registerEntry("Name2", makeEntry("B string"));
      KernelBus bus = kernel.getBus();
      TargettedJoinpoint joinPoint = getMethodJoinpoint(config, String.class, "toString");
      Object result1 = bus.invoke("Name1", joinPoint);
      Object result2 = bus.invoke("Name2", joinPoint);
      assertEquals("A string", result1);
      assertEquals("B string", result2);
   }

   protected MethodJoinpoint getMethodJoinpoint(KernelConfig config, Class clazz, String string) throws Throwable
   {
      BeanInfo info = config.getBeanInfo(clazz);
      JoinpointFactory jpf = info.getJoinpointFactory();
      return Config.getMethodJoinpoint(null, jpf, string, null, null);
   }
}