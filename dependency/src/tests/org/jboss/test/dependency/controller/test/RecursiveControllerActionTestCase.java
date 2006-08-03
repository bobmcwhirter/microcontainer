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
import org.jboss.test.dependency.controller.support.RecursiveControllerContext;
import org.jboss.test.dependency.controller.support.TestDelegate;

/**
 * RecursiveControllerActionTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class RecursiveControllerActionTestCase extends AbstractDependencyTest
{
   public static Test suite()
   {
      return suite(RecursiveControllerActionTestCase.class);
   }
   
   public RecursiveControllerActionTestCase(String name)
   {
      super(name);
   }
   
   public void testInstall() throws Throwable
   {
      TestDelegate delegate = new TestDelegate("InstallTestRecursive");
      RecursiveControllerContext context = new RecursiveControllerContext(delegate, this);
      assertInstall(context, ControllerState.INSTALLED);
      TestDelegate childDelegate = context.child.getDelegate();
      assertEquals(1, delegate.describeInstallOrder);
      assertEquals(2, delegate.instantiateInstallOrder);
      assertEquals(3, delegate.configureInstallOrder);
      assertEquals(4, delegate.createInstallOrder);
      assertEquals(5, childDelegate.describeInstallOrder);
      assertEquals(6, childDelegate.instantiateInstallOrder);
      assertEquals(7, childDelegate.configureInstallOrder);
      assertEquals(8, childDelegate.createInstallOrder);
      assertEquals(9, childDelegate.startInstallOrder);
      assertEquals(10, childDelegate.installInstallOrder);
      assertEquals(11, delegate.startInstallOrder);
      assertEquals(12, delegate.installInstallOrder);
      assertEquals(-1, delegate.installUninstallOrder);
      assertEquals(-1, delegate.startUninstallOrder);
      assertEquals(-1, delegate.createUninstallOrder);
      assertEquals(-1, delegate.configureUninstallOrder);
      assertEquals(-1, delegate.instantiateUninstallOrder);
      assertEquals(-1, delegate.describeUninstallOrder);
   }
   
   public void testUninstall() throws Throwable
   {
      TestDelegate delegate = new TestDelegate("UninstallTestRecursive");
      RecursiveControllerContext context = new RecursiveControllerContext(delegate, this);
      assertInstall(context, ControllerState.INSTALLED);
      TestDelegate childDelegate = context.child.getDelegate();
      assertUninstall(context);
      if (context.child.error != null)
         throw context.child.error;
      assertEquals(1, delegate.describeInstallOrder);
      assertEquals(2, delegate.instantiateInstallOrder);
      assertEquals(3, delegate.configureInstallOrder);
      assertEquals(4, delegate.createInstallOrder);
      assertEquals(5, childDelegate.describeInstallOrder);
      assertEquals(6, childDelegate.instantiateInstallOrder);
      assertEquals(7, childDelegate.configureInstallOrder);
      assertEquals(8, childDelegate.createInstallOrder);
      assertEquals(9, childDelegate.startInstallOrder);
      assertEquals(10, childDelegate.installInstallOrder);
      assertEquals(11, delegate.startInstallOrder);
      assertEquals(12, delegate.installInstallOrder);
      assertEquals(13, delegate.installUninstallOrder);
      assertEquals(14, delegate.startUninstallOrder);
      assertEquals(15, childDelegate.installUninstallOrder);
      assertEquals(16, childDelegate.startUninstallOrder);
      assertEquals(17, childDelegate.createUninstallOrder);
      assertEquals(18, childDelegate.configureUninstallOrder);
      assertEquals(19, childDelegate.instantiateUninstallOrder);
      assertEquals(20, childDelegate.describeUninstallOrder);
      assertEquals(21, delegate.createUninstallOrder);
      assertEquals(22, delegate.configureUninstallOrder);
      assertEquals(23, delegate.instantiateUninstallOrder);
      assertEquals(24, delegate.describeUninstallOrder);
   }
   
   public void testReinstall() throws Throwable
   {
      TestDelegate delegate = new TestDelegate("ReinstallTestRecursive");
      RecursiveControllerContext context = new RecursiveControllerContext(delegate, this);
      assertInstall(context, ControllerState.INSTALLED);
      TestDelegate childDelegate = context.child.getDelegate();
      assertUninstall(context);
      if (context.child.error != null)
         throw context.child.error;
      context = new RecursiveControllerContext(delegate, this);
      assertInstall(context, ControllerState.INSTALLED);
      TestDelegate childDelegate2 = context.child.getDelegate();
      if (context.child.error != null)
         throw context.child.error;
      assertEquals(25, delegate.describeInstallOrder);
      assertEquals(26, delegate.instantiateInstallOrder);
      assertEquals(27, delegate.configureInstallOrder);
      assertEquals(28, delegate.createInstallOrder);
      assertEquals(29, childDelegate2.describeInstallOrder);
      assertEquals(30, childDelegate2.instantiateInstallOrder);
      assertEquals(31, childDelegate2.configureInstallOrder);
      assertEquals(32, childDelegate2.createInstallOrder);
      assertEquals(33, childDelegate2.startInstallOrder);
      assertEquals(34, childDelegate2.installInstallOrder);
      assertEquals(35, delegate.startInstallOrder);
      assertEquals(36, delegate.installInstallOrder);
      assertEquals(13, delegate.installUninstallOrder);
      assertEquals(14, delegate.startUninstallOrder);
      assertEquals(15, childDelegate.installUninstallOrder);
      assertEquals(16, childDelegate.startUninstallOrder);
      assertEquals(17, childDelegate.createUninstallOrder);
      assertEquals(18, childDelegate.configureUninstallOrder);
      assertEquals(19, childDelegate.instantiateUninstallOrder);
      assertEquals(20, childDelegate.describeUninstallOrder);
      assertEquals(21, delegate.createUninstallOrder);
      assertEquals(22, delegate.configureUninstallOrder);
      assertEquals(23, delegate.instantiateUninstallOrder);
      assertEquals(24, delegate.describeUninstallOrder);
   }
   
   public void installChild(RecursiveControllerContext context) throws Throwable
   {
      TestDelegate delegate = new TestDelegate(context.getName() + "Child");
      context.child = new RecursiveControllerContext(delegate, null);
      assertInstall(context.child, ControllerState.INSTALLED);
   }
   
   public void uninstallChild(RecursiveControllerContext context)
   {
      try
      {
         assertUninstall(context.child);
      }
      catch (Throwable t)
      {
         context.child.error = t;
      }
   }
}
