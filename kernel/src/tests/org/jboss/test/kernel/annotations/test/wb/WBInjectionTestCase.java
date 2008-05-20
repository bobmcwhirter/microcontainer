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
package org.jboss.test.kernel.annotations.test.wb;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.plugins.annotations.AbstractBeanAnnotationAdapter;
import org.jboss.kernel.plugins.annotations.Annotation2ValueMetaDataAdapter;
import org.jboss.kernel.plugins.annotations.BeanAnnotationAdapter;
import org.jboss.kernel.plugins.annotations.ConstructorParameterAnnotationPlugin;
import org.jboss.kernel.plugins.annotations.StartLifecycleAnnotationPlugin;
import org.jboss.kernel.plugins.annotations.wb.WBInjectAnnotationPlugin;
import org.jboss.kernel.plugins.annotations.wb.WBInjectFieldAnnotationPlugin;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.kernel.annotations.support.AfterInstallVerifier;
import org.jboss.test.kernel.annotations.support.GBProvider;
import org.jboss.test.kernel.annotations.support.RBProvider;
import org.jboss.test.kernel.annotations.support.RGBProvider;
import org.jboss.test.kernel.annotations.support.RGProvider;
import org.jboss.test.kernel.annotations.support.WBTester;
import org.jboss.test.kernel.annotations.test.AbstractBeanAnnotationAdapterTest;

/**
 * WB injection tests.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class WBInjectionTestCase extends AbstractBeanAnnotationAdapterTest
{
   public WBInjectionTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(WBInjectionTestCase.class);
   }

   protected BeanAnnotationAdapter getBeanAnnotationAdapterClass()
   {
      AbstractBeanAnnotationAdapter adapter = new AbstractBeanAnnotationAdapter();
      // wb injection
      adapter.addAnnotationPlugin(WBInjectFieldAnnotationPlugin.INSTANCE);
      adapter.addAnnotationPlugin(WBInjectAnnotationPlugin.INSTANCE);
      // parameter adapter
      Annotation2ValueMetaDataAdapter<? extends Annotation> paramAdapter = WBInjectAnnotationPlugin.INSTANCE;
      Set<Annotation2ValueMetaDataAdapter<? extends Annotation>> adapters = new HashSet<Annotation2ValueMetaDataAdapter<? extends Annotation>>();
      adapters.add(paramAdapter);
      // parameter injections
      adapter.addAnnotationPlugin(new ConstructorParameterAnnotationPlugin(adapters));
      adapter.addAnnotationPlugin(new StartLifecycleAnnotationPlugin(adapters));

      return adapter;
   }

   public void testInjection() throws Throwable
   {
      KernelController controller = getController();
      controller.install(new AbstractBeanMetaData("rg", RGProvider.class.getName()));
      controller.install(new AbstractBeanMetaData("rb", RBProvider.class.getName()));
      controller.install(new AbstractBeanMetaData("gb", GBProvider.class.getName()));

      runAnnotationsOnClass(WBTester.class, BeanAccessMode.ALL);
   }

   public void testMultiple() throws Throwable
   {
      KernelController controller = getController();
      controller.install(new AbstractBeanMetaData("gb", GBProvider.class.getName()));
      controller.install(new AbstractBeanMetaData("rgb", RGBProvider.class.getName()));

      AfterInstallVerifier<WBTester> verifier = new AfterInstallVerifier<WBTester>()
      {
         public void verifyContext(KernelControllerContext context)
         {
            assertEquals(ControllerState.DESCRIBED, context.getState());
         }

         public void verify(WBTester target)
         {
            assertNull(target);
         }

         public Class<WBTester> getTargetClass()
         {
            return WBTester.class;
         }
      };
      addVerifier(verifier);
      try
      {
         runAnnotationsOnClass(WBTester.class, BeanAccessMode.ALL);
      }
      finally
      {
         removeVerifier(WBTester.class);
      }
   }

   protected void doTestAfterInstall(Object target)
   {
      WBTester demander = (WBTester)target;
      assertEquals("RG", demander.getRg_provider() + "");
      assertEquals("RB", demander.getRb_provider() + "");
      assertEquals("GB", demander.getGb_provider1() + "");
      assertEquals("GB", demander.getGb_provider2() + "");
      assertEquals(demander.getGb_provider1(), demander.getGb_provider2());
   }
}