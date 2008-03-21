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

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractArrayMetaData;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.guice.plugins.GuiceInjectorFactory;
import org.jboss.guice.plugins.GuiceObject;
import org.jboss.kernel.plugins.bootstrap.basic.KernelConstants;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.test.guice.support.Prototype;
import org.jboss.test.guice.support.Singleton;

/**
 * Guice test case via installed objects.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class GuiceObjectsTestCase extends AbstractGuiceTest
{
   public GuiceObjectsTestCase(String name)
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
      return suite(GuiceObjectsTestCase.class);
   }

/*
   // TODO - resolved when Guice Issue 49 is implemented 
   public void testBindFromListener() throws Throwable
   {
      final KernelController controller = getController();
      try
      {
         AbstractBeanMetaData injectorBean = new AbstractBeanMetaData("injector", GuiceInjectorFactory.class.getName());
         AbstractConstructorMetaData constructor = new AbstractConstructorMetaData();
         constructor.setFactoryClass(GuiceInjectorFactory.class.getName());
         constructor.setFactoryMethod("createInjector");
         List<ParameterMetaData> parameters = new ArrayList<ParameterMetaData>();
         parameters.add(new AbstractParameterMetaData(new AbstractDependencyValueMetaData(KernelConstants.KERNEL_NAME)));
         AbstractArrayMetaData array = new AbstractArrayMetaData();
         array.add(new AbstractValueMetaData(GuiceObject.LISTENER));
         array.add(new AbstractValueMetaData(GuiceObject.KERNEL));
         parameters.add(new AbstractParameterMetaData(array));
         constructor.setParameters(parameters);
         injectorBean.setConstructor(constructor);
         controller.install(injectorBean);

         BeanMetaData singleton = new AbstractBeanMetaData("singleton", Singleton.class.getName());
         controller.install(singleton);

         BeanMetaData prototype = new GenericBeanFactoryMetaData("prototype", Prototype.class.getName());
         controller.install(prototype);

         ControllerContext injectorContext = controller.getInstalledContext("injector");
         assertNotNull(injectorContext);
         Injector injector = (Injector)injectorContext.getTarget();

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
*/

   public void testBindAll() throws Throwable
   {
      final KernelController controller = getController();
      try
      {
         BeanMetaData singleton = new AbstractBeanMetaData("singleton", Singleton.class.getName());
         controller.install(singleton);

         installFactory(controller, "prototype", Prototype.class);

         AbstractBeanMetaData injectorBean = new AbstractBeanMetaData("injector", GuiceInjectorFactory.class.getName());
         AbstractConstructorMetaData constructor = new AbstractConstructorMetaData();
         constructor.setFactoryClass(GuiceInjectorFactory.class.getName());
         constructor.setFactoryMethod("createInjector");
         List<ParameterMetaData> parameters = new ArrayList<ParameterMetaData>();
         parameters.add(new AbstractParameterMetaData(new AbstractDependencyValueMetaData(KernelConstants.KERNEL_NAME)));
         AbstractArrayMetaData array = new AbstractArrayMetaData();
         array.add(new AbstractValueMetaData(GuiceObject.ALL));
         parameters.add(new AbstractParameterMetaData(array));
         constructor.setParameters(parameters);
         injectorBean.setConstructor(constructor);
         controller.install(injectorBean);

         Key<Singleton> singletonKey = Key.get(Singleton.class, Names.named("singleton"));
         Key<Prototype> prototypeKey = Key.get(Prototype.class, Names.named("prototype"));

         ControllerContext injectorContext = controller.getInstalledContext("injector");
         assertNotNull(injectorContext);
         Injector injector = (Injector)injectorContext.getTarget();

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
