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

import java.util.Collections;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Key;
import com.google.inject.name.Names;
import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractArrayMetaData;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.guice.spi.GuiceKernelRegistryEntryPlugin;
import org.jboss.kernel.plugins.bootstrap.basic.BasicBootstrap;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.test.guice.support.Singleton;
import org.jboss.test.guice.support.SingletonHolder;
import org.jboss.test.guice.support.Prototype;
import org.jboss.test.kernel.junit.MicrocontainerTest;

/**
 * Inject Guice objects into MC test.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class GuiceToMCTestCase extends MicrocontainerTest
{
   public GuiceToMCTestCase(String name)
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
      return suite(GuiceToMCTestCase.class);
   }

   private KernelController getController()
   {
      BasicBootstrap bootstrap = new BasicBootstrap();
      bootstrap.run();
      return bootstrap.getKernel().getController();
   }

   public void testGuice2MC() throws Throwable
   {
      final KernelController controller = getController();
      try
      {
         AbstractBeanMetaData guicePlugin = new AbstractBeanMetaData("GuicePlugin", GuiceKernelRegistryEntryPlugin.class.getName());
         AbstractConstructorMetaData constructor = new AbstractConstructorMetaData();
         AbstractArrayMetaData arrayMetaData = new AbstractArrayMetaData();
         final Singleton singleton = new Singleton();
         final Key<Prototype> prototypeKey = Key.get(Prototype.class, Names.named("prototype"));
         final Prototype prototype = new Prototype();
         Module module = new AbstractModule()
         {
            protected void configure()
            {
               bind(Singleton.class).toInstance(singleton);
               bind(prototypeKey).toInstance(prototype);
            }
         };
         arrayMetaData.add(new AbstractValueMetaData(module));
         constructor.setParameters(Collections.singletonList((ParameterMetaData)new AbstractParameterMetaData(arrayMetaData)));
         guicePlugin.setConstructor(constructor);
         controller.install(guicePlugin);

         BeanMetaData holderBean = new AbstractBeanMetaData("holder", SingletonHolder.class.getName());
         controller.install(holderBean);

         ControllerContext holderContext = controller.getInstalledContext("holder");
         assertNotNull(holderContext);
         SingletonHolder holder = (SingletonHolder)holderContext.getTarget();
         assertNotNull(holder);
         assertEquals(singleton, holder.getSingleton());

         ControllerContext prototypeContext = controller.getInstalledContext(prototypeKey);
         assertNotNull(prototypeContext);
         assertEquals(prototype, prototypeContext.getTarget());
      }
      finally
      {
         controller.shutdown();
      }
   }
}
