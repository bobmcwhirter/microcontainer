/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.dependency.controller.test;

import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

import org.jboss.dependency.plugins.AbstractController;
import org.jboss.dependency.plugins.AbstractControllerContext;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.ControllerStateModel;
import org.jboss.test.AbstractTestCaseWithSetup;
import org.jboss.test.AbstractTestDelegate;
import org.jboss.test.dependency.controller.support.MockControllerContextActions;
import org.jboss.test.dependency.controller.support.Ordering;
import org.jboss.test.dependency.controller.support.OtherControllerContext;
import org.jboss.test.dependency.controller.support.OtherDelegate;
import org.jboss.test.dependency.controller.support.TestControllerContext;
import org.jboss.test.dependency.controller.support.TestDelegate;

/**
 * An abstract controller test.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractDependencyTest extends AbstractTestCaseWithSetup
{
   protected Controller controller;
   
   public AbstractDependencyTest(String name)
   {
      super(name);
   }
   
   protected void setUp() throws Exception
   {
      super.setUp();
      controller = new AbstractController();
      Ordering.resetOrder();
      //enableTrace("org.jboss.dependency");
   }
   
   protected ControllerContext assertInstall(TestDelegate delegate) throws Throwable
   {
      return assertInstall(delegate, ControllerState.INSTALLED);
   }
   
   protected ControllerContext assertInstall(TestDelegate delegate, ControllerState expected) throws Throwable
   {
      TestControllerContext context = new TestControllerContext(delegate);
      assertInstall(context, expected);
      return context;
   }
   
   protected ControllerContext assertInstall(OtherDelegate delegate) throws Throwable
   {
      return assertInstall(delegate, ControllerState.INSTALLED);
   }
   
   protected ControllerContext assertInstall(OtherDelegate delegate, ControllerState expected) throws Throwable
   {
      OtherControllerContext context = new OtherControllerContext(delegate);
      assertInstall(context, expected);
      return context;
   }

   protected void install(ControllerContext context) throws Throwable
   {
      controller.install(context);
   }

   protected void uninstall(ControllerContext context) throws Throwable
   {
      controller.uninstall(context.getName());
   }

   protected ControllerStateModel getStateModel()
   {
      return controller.getStates();
   }

   protected void assertInstall(ControllerContext context, ControllerState expected) throws Throwable
   {
      controller.install(context);
      assertContext(context, expected);
   }

   protected void assertChange(ControllerContext context, ControllerState expected) throws Throwable
   {
      assertChange(context, expected, expected);
   }

   protected void assertChange(ControllerContext context, ControllerState required, ControllerState expected) throws Throwable
   {
      controller.change(context, required);
      assertContext(context, expected);
   }

   protected ControllerContext assertContext(String name, ControllerState expected) throws Throwable
   {
      ControllerContext context = controller.getContext(name, expected);
      assertEquals(expected, context.getState());
      return context;
   }

   protected void assertContext(ControllerContext context) throws Throwable
   {
      assertContext(context, ControllerState.INSTALLED);
   }

   protected void assertContext(ControllerContext context, ControllerState expected) throws Throwable
   {
      assertEquals(expected, context.getState());
   }

   protected void assertNoContext(ControllerContext context) throws Throwable
   {
      assertContext(context, ControllerState.ERROR);
   }

   protected void assertNoContext(Object name) throws Throwable
   {
      assertNull(controller.getContext(name, null));
   }
   
   protected void assertUninstall(ControllerContext context) throws Throwable
   {
      controller.uninstall(context.getName());
      assertEquals(ControllerState.ERROR, context.getState());
   }
   
   protected Set<Object> createAliases(Object... aliases)
   {
      if (aliases == null)
         return null;
      HashSet<Object> result = new HashSet<Object>(aliases.length);
      result.addAll(Arrays.asList(aliases));
      return result;
   }
   
   protected ControllerContext createControllerContext(Object name, Object... aliases)
   {
      AbstractControllerContext result = new AbstractControllerContext(name, new MockControllerContextActions());
      result.setAliases(createAliases(aliases));
      return result;
   }
   
   protected ControllerContext assertCreateInstall(Object name, Object... aliases) throws Throwable
   {
      ControllerContext context = createControllerContext(name, aliases);
      assertInstall(context, ControllerState.INSTALLED);
      return context;
   }

   /**
    * Default setup with security manager enabled
    * 
    * @param clazz the class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      AbstractTestDelegate delegate = new AbstractTestDelegate(clazz);
      delegate.enableSecurity = true;
      return delegate;
   }
}
