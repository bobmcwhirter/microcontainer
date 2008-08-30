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

import junit.framework.Test;

import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.dependency.controller.support.ErrorControllerContext;
import org.jboss.test.dependency.controller.support.ErrorDelegate;

/**
 * ErrorControllerActionTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ErrorControllerActionTestCase extends AbstractDependencyTest
{
   public static Test suite()
   {
      return suite(ErrorControllerActionTestCase.class);
   }
   
   public ErrorControllerActionTestCase(String name)
   {
      super(name);
   }
   
   public void testFailInDescribe() throws Throwable
   {
      ErrorDelegate delegate = new ErrorDelegate("test", ErrorDelegate.FAIL_IN_DESCRIBE);
      ErrorControllerContext context = new ErrorControllerContext(delegate);
      assertInstall(context, ControllerState.NOT_INSTALLED);
      assertChange(context, ControllerState.DESCRIBED, ControllerState.ERROR);
      assertUninstall(context);
   }
   
   public void testFailInInstantiate() throws Throwable
   {
      ErrorDelegate delegate = new ErrorDelegate("test", ErrorDelegate.FAIL_IN_INSTANTIATE);
      ErrorControllerContext context = new ErrorControllerContext(delegate);
      assertInstall(context, ControllerState.NOT_INSTALLED);
      assertChange(context, ControllerState.INSTANTIATED, ControllerState.ERROR);
      assertUninstall(context);
   }
   
   public void testFailInConfigure() throws Throwable
   {
      ErrorDelegate delegate = new ErrorDelegate("test", ErrorDelegate.FAIL_IN_CONFIGURE);
      ErrorControllerContext context = new ErrorControllerContext(delegate);
      assertInstall(context, ControllerState.NOT_INSTALLED);
      assertChange(context, ControllerState.CONFIGURED, ControllerState.ERROR);
      assertUninstall(context);
   }
   
   public void testFailInCreate() throws Throwable
   {
      ErrorDelegate delegate = new ErrorDelegate("test", ErrorDelegate.FAIL_IN_CREATE);
      ErrorControllerContext context = new ErrorControllerContext(delegate);
      assertInstall(context, ControllerState.NOT_INSTALLED);
      assertChange(context, ControllerState.CREATE, ControllerState.ERROR);
      assertUninstall(context);
   }
   
   public void testFailInStart() throws Throwable
   {
      ErrorDelegate delegate = new ErrorDelegate("test", ErrorDelegate.FAIL_IN_START);
      ErrorControllerContext context = new ErrorControllerContext(delegate);
      assertInstall(context, ControllerState.NOT_INSTALLED);
      assertChange(context, ControllerState.START, ControllerState.ERROR);
      assertUninstall(context);
   }
   
   public void testFailInInstall() throws Throwable
   {
      ErrorDelegate delegate = new ErrorDelegate("test", ErrorDelegate.FAIL_IN_INSTALL);
      ErrorControllerContext context = new ErrorControllerContext(delegate);
      assertInstall(context, ControllerState.NOT_INSTALLED);
      assertChange(context, ControllerState.INSTALLED, ControllerState.ERROR);
      assertUninstall(context);
   }
}
