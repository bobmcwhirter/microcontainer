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
package org.jboss.test.kernel.junit;

import java.util.UUID;

import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;

/**
 * A Wired MicrocontainerTest.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 64256 $
 */
public abstract class WiredMicrocontainerTest extends MicrocontainerTest
{
   /** The bean name */
   private String beanName = createTestBeanName();

   /**
    * Create a new Wired Microcontainer test
    *
    * @param name the test name
    */
   protected WiredMicrocontainerTest(String name)
   {
      super(name);
   }

   protected void afterSetUp() throws Exception
   {
      KernelController controller = getController();
      try
      {
         controller.install(new AbstractBeanMetaData(beanName, getClass().getName()), this);
      }
      catch (Throwable t)
      {
         throw new Exception(t);
      }
      super.afterSetUp();
   }

   private KernelController getController()
   {
      MicrocontainerTestDelegate delegate = getMCDelegate();
      Kernel kernel = delegate.kernel;
      return kernel.getController();
   }

   protected void tearDown() throws Exception
   {
      KernelController controller = getController();
      controller.uninstall(beanName);
      super.tearDown();
   }

   /**
    * Get the test's bean name.
    * By default it's random uuid.
    *
    * @return test's bean name
    */
   protected String createTestBeanName()
   {
      return UUID.randomUUID().toString();
   }

   /**
    * Get the test's bean name.
    * By default it's what create method returns.
    *
    * @return test's bean name
    */
   protected String getTestBeanName()
   {
      return beanName;
   }
}
