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

import org.jboss.test.kernel.junit.MicrocontainerTest;
import org.jboss.test.guice.support.Singleton;
import org.jboss.test.guice.support.Prototype;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.plugins.bootstrap.basic.BasicBootstrap;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData;
import org.jboss.dependency.spi.Controller;
import org.jboss.guice.spi.GuiceIntegration;
import junit.framework.Test;
import com.google.inject.Injector;
import com.google.inject.Guice;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.name.Names;

/**
 * Guice integration test case.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class GuiceIntegrationTestCase extends MicrocontainerTest
{
   public GuiceIntegrationTestCase(String name)
   {
      super(name);
   }

   /**
    * Setup the test
    *
    * @return the test
    */
   public static Test suite()
   {
      return suite(GuiceIntegrationTestCase.class);
   }

   protected KernelController getController()
   {
      BasicBootstrap bootstrap = new BasicBootstrap();
      bootstrap.run();
      return bootstrap.getKernel().getController();
   }

   protected void installBeans(KernelController controller)
         throws Throwable
   {
      BeanMetaData singleton = new AbstractBeanMetaData("singleton", Singleton.class.getName());
      controller.install(singleton);

      BeanMetaData prototype = new GenericBeanFactoryMetaData("prototype", Prototype.class.getName());
      controller.install(prototype);
   }

   public void testBindFromMicrocontainer() throws Throwable
   {
      final KernelController controller = getController();
      try
      {
         installBeans(controller);

         Injector injector = Guice.createInjector(new AbstractModule()
         {
            protected void configure()
            {
               bind(Controller.class).toInstance(controller);
               bind(Singleton.class).toProvider(GuiceIntegration.fromMicrocontainer(Singleton.class, "singleton"));
               bind(Prototype.class).toProvider(GuiceIntegration.fromMicrocontainer(Prototype.class, "prototype"));
            }
         });

         assertNotNull(injector.getInstance(Singleton.class));
         assertSame(injector.getInstance(Singleton.class), injector.getInstance(Singleton.class));

         assertNotNull(injector.getInstance(Prototype.class));
         assertNotSame(injector.getInstance(Prototype.class), injector.getInstance(Prototype.class));
      }
      finally
      {
         controller.shutdown();
      }
   }

   public void testBindAll() throws Throwable
   {
      final KernelController controller = getController();
      try
      {
         installBeans(controller);

         Injector injector = Guice.createInjector(new AbstractModule()
         {
            protected void configure()
            {
               GuiceIntegration.bindAll(binder(), controller);
            }
         });

         Key<Singleton> singletonKey = Key.get(Singleton.class, Names.named("singleton"));
         Key<Prototype> prototypeKey = Key.get(Prototype.class, Names.named("prototype"));

         assertNotNull(injector.getInstance(singletonKey));
         assertSame(injector.getInstance(singletonKey), injector.getInstance(singletonKey));

         assertNotNull(injector.getInstance(prototypeKey));
         assertNotSame(injector.getInstance(prototypeKey), injector.getInstance(prototypeKey));
      }
      finally
      {
         controller.shutdown();
      }
   }
}
