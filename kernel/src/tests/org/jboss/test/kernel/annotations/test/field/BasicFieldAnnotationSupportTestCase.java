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
package org.jboss.test.kernel.annotations.test.field;

import org.jboss.test.kernel.annotations.test.AbstractBeanAnnotationAdapterTest;
import org.jboss.test.kernel.annotations.test.AfterInstallVerifier;
import org.jboss.test.kernel.annotations.support.TestBean;
import org.jboss.test.kernel.annotations.support.MyDeployer;
import org.jboss.test.kernel.annotations.support.InjectTester;
import org.jboss.test.kernel.annotations.support.ValueFactoryTester;
import org.jboss.test.kernel.annotations.support.CallbacksTester;
import org.jboss.kernel.plugins.annotations.BeanAnnotationAdapter;
import org.jboss.kernel.plugins.annotations.BeanAnnotationAdapterFactory;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.info.spi.BeanAccessMode;
import junit.framework.Test;

/**
 * Basic field annotation IoC support
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class BasicFieldAnnotationSupportTestCase extends AbstractBeanAnnotationAdapterTest
{
   public BasicFieldAnnotationSupportTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(BasicFieldAnnotationSupportTestCase.class);
   }

   protected BeanAnnotationAdapter getBeanAnnotationAdapterClass()
   {
      return BeanAnnotationAdapterFactory.getInstance().getBeanAnnotationAdapter();
   }

   protected <T> void testFields(T target, AfterInstallVerifier<T> verifier, BeanAccessMode mode) throws Throwable
   {
      addVerifier(verifier);
      try
      {
         runAnnotationsOnTarget(target, mode);
      }
      finally
      {
         removeVerifier(target.getClass());
      }
   }

   public void testInjection() throws Throwable
   {
      KernelController controller = getController();
      controller.install(new AbstractBeanMetaData("pb1", TestBean.class.getName()));
      controller.install(new AbstractBeanMetaData("pb2", TestBean.class.getName()));
      controller.install(new AbstractBeanMetaData("pb3", TestBean.class.getName()));
      controller.install(new AbstractBeanMetaData("deployer", MyDeployer.class.getName()));
      InjectTester tester = new InjectTester();
      testFields(tester, new InjectTesterVerifier(), BeanAccessMode.FIELDS);
      testFields(tester, new AllInjectTesterVerifier(), BeanAccessMode.ALL);
   }

   public void testValueFactory() throws Throwable
   {
      KernelController controller = getController();
      controller.install(new AbstractBeanMetaData("pb1", TestBean.class.getName()));
      controller.install(new AbstractBeanMetaData("pb2", TestBean.class.getName()));
      controller.install(new AbstractBeanMetaData("pb3", TestBean.class.getName()));
      ValueFactoryTester tester = new ValueFactoryTester();
      testFields(tester, new VFTesterVerifier(), BeanAccessMode.FIELDS);
      testFields(tester, new AllVFTesterVerifier(), BeanAccessMode.ALL);
   }

   public void testInstalls() throws Throwable
   {
      KernelController controller = getController();
      controller.install(new AbstractBeanMetaData("pb1", TestBean.class.getName()));
      controller.install(new AbstractBeanMetaData("pb2", TestBean.class.getName()));
      controller.install(new AbstractBeanMetaData("pb3", TestBean.class.getName()));
      CallbacksTester tester = new CallbacksTester();
      testFields(tester, new CallbacksTesterVerifier(), BeanAccessMode.FIELDS);
      testFields(tester, new AllCallbacksTesterVerifier(), BeanAccessMode.ALL);
   }

   private class InjectTesterVerifier implements AfterInstallVerifier<InjectTester>
   {
      public void verify(InjectTester target)
      {
         assertNotNull(target.publicBean);
         assertNotNull(target.publicDeployer);
      }

      public Class<InjectTester> getTargetClass()
      {
         return InjectTester.class;
      }
   }

   private class AllInjectTesterVerifier extends InjectTesterVerifier
   {
      public void verify(InjectTester target)
      {
         super.verify(target);
         assertNotNull(target.getPrivateBean());
         assertNotNull(target.getProtectedBean());
         assertNotNull(target.getPrivateDeployer());
         assertNotNull(target.getProtectedDeployer());
      }
   }

   private class VFTesterVerifier implements AfterInstallVerifier<ValueFactoryTester>
   {
      public void verify(ValueFactoryTester target)
      {
         assertEquals("PB3", target.publicBean);
      }

      public Class<ValueFactoryTester> getTargetClass()
      {
         return ValueFactoryTester.class;
      }
   }

   private class AllVFTesterVerifier extends VFTesterVerifier
   {
      public void verify(ValueFactoryTester target)
      {
         super.verify(target);
         assertEquals("PB1", target.getPrivateBean());
         assertEquals("PB2", target.getProtectedBean());
      }
   }

   private class CallbacksTesterVerifier implements AfterInstallVerifier<CallbacksTester>
   {
      public void verify(CallbacksTester target)
      {
         assertNotNull(target.publicBeans);
         assertEquals(3, target.publicBeans.size());
      }

      public Class<CallbacksTester> getTargetClass()
      {
         return CallbacksTester.class;
      }
   }

   private class AllCallbacksTesterVerifier extends CallbacksTesterVerifier
   {
      public void verify(CallbacksTester target)
      {
         super.verify(target);
         assertNotNull(target.getProtectedBeans());
         assertEquals(3, target.getProtectedBeans().size());
         assertNotNull(target.getPrivateBeans());
         assertEquals(3, target.getPrivateBeans().size());
      }
   }
}
