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
package org.jboss.test.aop.junit;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.test.AbstractTestCaseWithSetup;
import org.jboss.test.AbstractTestDelegate;

/**
 * AbstractTypeTest.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractTypeTest extends AbstractTestCaseWithSetup
{
   public AbstractTypeTest(String name)
   {
      super(name);
   }
   
   /**
    * Get the test delegate
    *
    * @param clazz the test class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      String property = System.getProperty("jboss.mc.secure", "false");
      boolean enableSecurity = Boolean.valueOf(property);
      AbstractTypeTestDelegate delegate = new AbstractTypeTestDelegate(clazz);
      delegate.enableSecurity = enableSecurity;
      return delegate;
   }

   protected KernelController getController()
   {
      AbstractTypeTestDelegate delegate = (AbstractTypeTestDelegate)getDelegate();
      return delegate.getKernel().getController();
   }

   protected ControllerContext getControllerContext(String name) throws Throwable
   {
      return getControllerContext(name, null);
   }

   protected ControllerContext getControllerContext(String name, ControllerState state) throws Throwable
   {
      return getController().getContext(name, state);
   }

   protected ControllerContext assertControllerContext(String name, ControllerState state) throws Throwable
   {
      ControllerContext context = getControllerContext(name, state);
      assertNotNull(context);
      return context;
   }

   protected <T> T getBean(String name, Class<T> expectedType) throws Throwable
   {
      ControllerContext context = assertControllerContext(name, null);
      Object target = context.getTarget();
      assertNotNull(target);
      return assertInstanceOf(target, expectedType);
   }

   protected AbstractTypeTestDelegate.Type getType(Object name)
   {
      AbstractTypeTestDelegate delegate = (AbstractTypeTestDelegate)getDelegate();
      AbstractTypeTestDelegate.Type type = delegate.getTypes().get(name);
      if (type == null)
         throw new IllegalArgumentException("No such context installed by name: " + name);
      return type;
   }

   protected void assertIsProxy(Object name)
   {
      assertEquals(name + " is not proxy.", AbstractTypeTestDelegate.Type.PROXY, getType(name));
   }

   protected void assertIsPojo(Object name)
   {
      assertEquals(name + " is not pojo.", AbstractTypeTestDelegate.Type.POJO, getType(name));
   }

   protected void assertIsWoven(Object name)
   {
      assertEquals(name + " is not woven.", AbstractTypeTestDelegate.Type.WOVEN, getType(name));
   }

   protected void assertIsAspectized(Object name)
   {
      AbstractTypeTestDelegate.Type type = getType(name);
      assertTrue(name + " is not aspectized.", AbstractTypeTestDelegate.Type.WOVEN == type || AbstractTypeTestDelegate.Type.PROXY == type);
   }
}
