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
package org.jboss.test.guice.test;

import org.jboss.kernel.plugins.bootstrap.basic.BasicBootstrap;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.test.kernel.junit.MicrocontainerTest;
import org.jboss.beans.metadata.spi.factory.GenericBeanFactoryMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;

/**
 * Abstract Guice test.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractGuiceTest extends MicrocontainerTest
{
   protected AbstractGuiceTest(String name)
   {
      super(name);
   }

   protected KernelController getController()
   {
      BasicBootstrap bootstrap = new BasicBootstrap();
      bootstrap.run();
      return bootstrap.getKernel().getController();
   }

   protected GenericBeanFactoryMetaData installFactory(KernelController controller, String name, Class<?> beanClass) throws Throwable
   {
      return installFactory(controller, name, beanClass.getName());
   }

   protected GenericBeanFactoryMetaData installFactory(KernelController controller, String name, String beanClassName) throws Throwable
   {
      GenericBeanFactoryMetaData factory = new GenericBeanFactoryMetaData();
      factory.setName(name);
      factory.setBean(beanClassName);

      for (BeanMetaData bean : factory.getBeans())
         controller.install(bean);

      return factory;
   }
}
