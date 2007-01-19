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
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.kernel.config.support.SimpleBean;
import org.jboss.test.kernel.config.support.SimpleLifecycleBean;

/**
 * Builder TestCase.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class BeanMetaDataBuilderTestCase extends AbstractKernelConfigTest
{
   public static Test suite()
   {
      return suite(BeanMetaDataBuilderTestCase.class);
   }

   public BeanMetaDataBuilderTestCase(String name) throws Throwable
   {
      super(name);
   }

   public void testConstructor() throws Throwable
   {
      Kernel kernel = bootstrap();

      BeanMetaDataBuilder builder = new BeanMetaDataBuilder("FromBMD", SimpleBean.class.getName());
      builder.addConstructorParameter(String.class.getName(), "TestConstructor");
      BeanMetaData beanMetaData = builder.getBeanMetaData();
      SimpleBean fbmd = (SimpleBean)instantiateAndConfigure(kernel.getConfigurator(), beanMetaData);

      assertNotNull(fbmd);
      assertNotNull(fbmd.getConstructorUsed());
      assertEquals("TestConstructor", fbmd.getConstructorUsed());
   }

   public void testLifecycle() throws Throwable
   {
      Kernel kernel = bootstrap();

      BeanMetaDataBuilder builder = new BeanMetaDataBuilder("SLB", SimpleLifecycleBean.class.getName())
         .addCreateParameter(String.class.getName(), "ParamCreate")
         .setStart("doStart")
         .addStartParameter(String.class.getName(), "ParamStart")
         .setStop("doStop")
         .addStopParameter(String.class.getName(), "ParamStop")
         .addDestroyParameter(String.class.getName(), "ParamDestroy");
      BeanMetaData beanMetaData = builder.getBeanMetaData();

      KernelController controller = kernel.getController();

      KernelControllerContext context = controller.install(beanMetaData);
      SimpleLifecycleBean slb = (SimpleLifecycleBean)context.getTarget();

      assertNotNull(slb);
      assertEquals("ParamCreate", slb.getCreate());
      assertEquals("ParamStart", slb.getStart());

      controller.uninstall("SLB");

      assertEquals("ParamStop", slb.getStop());
      assertEquals("ParamDestroy", slb.getDestroy());
   }

   public void testInstall() throws Throwable
   {
      Kernel kernel = bootstrap();

      BeanMetaDataBuilder builder = new BeanMetaDataBuilder("SLB", SimpleLifecycleBean.class.getName())
         .addInstall("installParam", String.class.getName(), "Install")
         .addUninstall("uninstallParam", String.class.getName(), "Uninstall");
      BeanMetaData beanMetaData = builder.getBeanMetaData();

      KernelController controller = kernel.getController();

      KernelControllerContext context = controller.install(beanMetaData);
      SimpleLifecycleBean slb = (SimpleLifecycleBean)context.getTarget();

      assertNotNull(slb);
      assertEquals("Install", slb.getInstall());

      controller.uninstall("SLB");

      assertEquals("Uninstall", slb.getInstall());
   }

   public void testDemandSupply() throws Throwable
   {
      // todo
   }

   public void testDependency() throws Throwable
   {
      // todo
   }

}
