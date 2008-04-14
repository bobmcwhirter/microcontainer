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

import java.util.Map;
import java.util.HashMap;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.bootstrap.basic.BasicBootstrap;
import org.jboss.kernel.spi.config.KernelConfig;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.BaseTestCase;
import org.jboss.test.kernel.annotations.support.AfterInstallVerifier;
import org.jboss.dependency.spi.ControllerState;

/**
 * Abstract annotation runner test.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractRunAnnotationsTest extends BaseTestCase
{
   private KernelController controller;
   private Map<Class<?>, AfterInstallVerifier<?>> verifiers = new HashMap<Class<?>, AfterInstallVerifier<?>>();
   
   protected AbstractRunAnnotationsTest(String name)
   {
      super(name);
   }

   protected void setUp() throws Exception
   {
      super.setUp();

      controller = createController();
   }

   protected void tearDown() throws Exception
   {
      if (controller != null)
         controller.shutdown();
      controller = null;

      super.tearDown();
   }

   protected void runAnnotationsOnTarget(Object target) throws Throwable
   {
      runAnnotationsOnTarget(target, BeanAccessMode.STANDARD);
   }

   @SuppressWarnings("unchecked")
   protected void runAnnotationsOnTarget(Object target, BeanAccessMode mode) throws Throwable
   {
      assertNotNull("Target is null", target);
      Class clazz = target.getClass();
      runAnnotations(clazz, target, mode);
   }

   protected void runAnnotationsOnClass(Class<?> clazz) throws Throwable
   {
      runAnnotationsOnClass(clazz, BeanAccessMode.STANDARD);      
   }

   protected void runAnnotationsOnClass(Class<?> clazz, BeanAccessMode mode) throws Throwable
   {
      runAnnotations(clazz, null);
   }

   protected <T> void runAnnotations(Class<T> clazz, T target) throws Throwable
   {
      runAnnotations(clazz, target, BeanAccessMode.STANDARD);
   }

   protected <T> void runAnnotations(Class<T> clazz, T target, BeanAccessMode mode) throws Throwable
   {
      KernelController controller = getController();
      String className = clazz.getName();
      String name = target != null ? target.toString() : (className + System.currentTimeMillis());
      AbstractBeanMetaData beanMetaData = new AbstractBeanMetaData(name, className);
      beanMetaData.setAccessMode(mode);
      try
      {
         KernelControllerContext context = controller.install(beanMetaData, target);
         checkContextState(context);
         if (target == null)
            target = clazz.cast(context.getTarget());
         doTestAfterInstall(clazz, target);
      }
      finally
      {
         controller.uninstall(name);
      }
   }

   protected void checkContextState(KernelControllerContext context)
   {
      assertEquals(ControllerState.INSTALLED, context.getState());
   }

   protected <T> void addVerifier(AfterInstallVerifier<T> verifier)
   {
      verifiers.put(verifier.getTargetClass(), verifier);
   }

   protected void removeVerifier(Class<?> clazz)
   {
      verifiers.remove(clazz);
   }

   /**
    * Useful for single tests.
    * Else determine the test by parameters.
    *
    * @param <T> the type
    * @param clazz the class
    * @param target the target
    */
   @SuppressWarnings("unchecked")
   protected <T> void doTestAfterInstall(Class<T> clazz, T target)
   {
      AfterInstallVerifier<T> verifier = (AfterInstallVerifier) verifiers.get(clazz);
      if (verifier != null)
         verifier.verify(target);
      else
         doTestAfterInstall(target);
   }

   protected void doTestAfterInstall(Object target)
   {
      doTestAfterInstall();
   }

   protected void doTestAfterInstall()
   {
   }

   protected KernelController createController() throws Exception
   {
      // bootstrap
      KernelConfig config = createKernelConfig();
      BasicBootstrap bootstrap = config != null ? new BasicBootstrap(config) : new BasicBootstrap();
      bootstrap.run();
      Kernel kernel = bootstrap.getKernel();
      return kernel.getController();
   }

   protected KernelConfig createKernelConfig()
   {
      return null;
   }

   protected KernelController getController()
   {
      return controller;
   }
}
