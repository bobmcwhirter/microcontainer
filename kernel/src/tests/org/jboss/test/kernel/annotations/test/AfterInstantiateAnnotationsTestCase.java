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
package org.jboss.test.kernel.annotations.test;

import java.util.List;
import java.util.Set;

import junit.framework.Test;
import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.CallbackMetaData;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.beans.metadata.spi.LifecycleMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.kernel.plugins.annotations.BeanAnnotationAdapter;
import org.jboss.kernel.plugins.annotations.BeanAnnotationAdapterFactory;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.kernel.annotations.support.AfterInstantiateTester;
import org.jboss.test.kernel.annotations.support.OtherVerifier;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AfterInstantiateAnnotationsTestCase extends AbstractBeanAnnotationAdapterTest
{
   public AfterInstantiateAnnotationsTestCase(String name)
   {
      super(name);
   }

   protected BeanAnnotationAdapter getBeanAnnotationAdapterClass()
   {
      return BeanAnnotationAdapterFactory.getInstance().getBeanAnnotationAdapter();
   }

   public static Test suite()
   {
      return suite(AfterInstantiateAnnotationsTestCase.class);
   }

   public void testAfterInstantiationBeanInfo() throws Throwable
   {
      KernelController controller = getController();
      // other
      KernelControllerContext occ = controller.install(new AbstractBeanMetaData("other", OtherVerifier.class.getName()));
      assertNotNull(occ);
      Object tcc = occ.getTarget();
      assertNotNull(tcc);

      // tester
      AbstractBeanMetaData abmd = new AbstractBeanMetaData();
      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder(abmd);
      builder.setName("tester");
      builder.setAccessMode(BeanAccessMode.ALL);
      builder.setFactoryClass(AfterInstantiateTester.class.getName());
      builder.setFactoryMethod("getTester");

      KernelControllerContext kcc = controller.install(builder.getBeanMetaData());
      try
      {
         assertNotNull(kcc);
         BeanMetaData beanMetaData = kcc.getBeanMetaData();
         // test aliases
         assertNotNull(controller.getContext("qwert", null));
         // propertys
         Set<PropertyMetaData> properties = beanMetaData.getProperties();
         assertNotNull(properties);
         assertEquals(2, properties.size());
         // lifecycle
         LifecycleMetaData create = beanMetaData.getCreate();
         assertNotNull(create);
         assertEquals("createMe", create.getMethodName());
         LifecycleMetaData start = beanMetaData.getStart();
         assertNotNull(start);
         assertEquals("startMe", start.getMethodName());
         LifecycleMetaData stop = beanMetaData.getStop();
         assertNotNull(stop);
         assertEquals("stopMe", stop.getMethodName());
         LifecycleMetaData destroy = beanMetaData.getDestroy();
         assertNotNull(destroy);
         assertEquals("destroyMe", destroy.getMethodName());
         // callbacks
         List<CallbackMetaData> incallbacks = beanMetaData.getInstallCallbacks();
         assertNotNull(incallbacks);
         assertEquals(2, incallbacks.size());
         List<CallbackMetaData> uncallbacks = beanMetaData.getUninstallCallbacks();
         assertNotNull(uncallbacks);
         assertEquals(2, uncallbacks.size());
         // installs
         List<InstallMetaData> installs = beanMetaData.getInstalls();
         assertNotNull(installs);
         assertEquals(2, installs.size());
         List<InstallMetaData> uninstalls = beanMetaData.getUninstalls();
         assertNotNull(uninstalls);
         assertEquals(2, uninstalls.size());

         Object tkcc = kcc.getTarget();
         assertNotNull(tkcc);
         AfterInstantiateTester tester = assertInstanceOf(tkcc, AfterInstantiateTester.class);
         assertTrue(tester.time != 0);
      }
      finally
      {
         controller.uninstall(kcc.getName());
      }
   }
}
