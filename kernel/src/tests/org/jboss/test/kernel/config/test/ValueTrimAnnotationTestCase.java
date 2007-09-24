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
package org.jboss.test.kernel.config.test;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.kernel.config.support.NoTrimTester;
import org.jboss.test.kernel.config.support.TrimTester;
import org.jboss.dependency.spi.ControllerState;

/**
 * Value trim annotation test cases.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ValueTrimAnnotationTestCase extends ValueTrimTestCase
{
   public ValueTrimAnnotationTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(ValueTrimAnnotationTestCase.class);
   }

   protected Object getTrimmedValue() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();
      KernelControllerContext context = controller.install(new AbstractBeanMetaData("tester", TrimTester.class.getName()));
      TrimTester tester = (TrimTester)context.getTarget();
      return tester.getValue();
   }

   protected Object getPlainValue() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();
      KernelControllerContext context = controller.install(new AbstractBeanMetaData("tester", NoTrimTester.class.getName()));
      assertEquals(ControllerState.ERROR, context.getState());
      throw new IllegalStateException();
   }

   protected Class<? extends Exception> getExceptionClass()
   {
      return IllegalStateException.class;
   }
}
