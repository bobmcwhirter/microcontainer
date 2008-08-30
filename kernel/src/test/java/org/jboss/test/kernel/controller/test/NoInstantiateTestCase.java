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
package org.jboss.test.kernel.controller.test;

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.kernel.AbstractKernelTest;
import org.jboss.test.kernel.controller.support.SimpleBean;

/**
 * No instantiate Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class NoInstantiateTestCase extends AbstractKernelTest
{
   public static Test suite()
   {
      return suite(NoInstantiateTestCase.class);
   }

   public NoInstantiateTestCase(String name) throws Throwable
   {
      super(name);
   }

   public void testNoInstantiate() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();

      SimpleBean bean = new SimpleBean();
      
      AbstractBeanMetaData metaData = new AbstractBeanMetaData("Name1", bean.getClass().getName());
      
      KernelControllerContext context = controller.install(metaData, bean);
      assertEquals(ControllerState.INSTALLED, context.getState());
      
      assertEquals(bean, context.getTarget());
      
      controller.uninstall("Name1");
      
      assertEquals(bean, context.getTarget());
      
      context = controller.install(metaData, bean);
      assertEquals(ControllerState.INSTALLED, context.getState());
      
      assertEquals(bean, context.getTarget());
   }
}