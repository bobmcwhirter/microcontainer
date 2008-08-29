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
package org.jboss.test.kernel.dependency.test;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.test.kernel.AbstractKernelTest;
import org.jboss.test.kernel.dependency.support.BVBTester;
import org.jboss.test.kernel.dependency.support.MockBeanValidatorBridge;

/**
 * BVB test.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class BeanValidatorBridgeTestCase extends AbstractKernelTest
{
   public BeanValidatorBridgeTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(BeanValidatorBridgeTestCase.class);
   }

   public void testBVBLookup() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();

      MockBeanValidatorBridge bridge = new MockBeanValidatorBridge();
      controller.install(new AbstractBeanMetaData("bvb", MockBeanValidatorBridge.class.getName()), bridge);

      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder("tester", BVBTester.class.getName());
      builder.addPropertyMetaData("something", 123);
      builder.addInstall("invokeSomething", Object.class.getName(), "123");
      controller.install(builder.getBeanMetaData());

      assertNotNull(bridge.getJoinpoint());
      assertInstanceOf(bridge.getTarget(), BVBTester.class);
      assertEquals("something", bridge.getProperty());
      assertEquals("invokeSomething", bridge.getMethod());
   }
}
