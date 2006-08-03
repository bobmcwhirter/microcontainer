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

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.dependency.controller.support.TestDelegate;

/**
 * A ChangeAutomaticControllerTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class ChangeAutomaticControllerTestCase extends AbstractDependencyTest
{
   public static Test suite()
   {
      return suite(ChangeAutomaticControllerTestCase.class);
   }
   
   public ChangeAutomaticControllerTestCase(String name)
   {
      super(name);
   }
   
   public void testChangeAutomatic() throws Throwable
   {
      TestDelegate delegate = new TestDelegate("ChangeAutomaticTest");
      ControllerContext context = assertInstall(delegate);
      assertEquals(1, delegate.describeInstallOrder);
      assertEquals(2, delegate.instantiateInstallOrder);
      assertEquals(3, delegate.configureInstallOrder);
      assertEquals(4, delegate.createInstallOrder);
      assertEquals(5, delegate.startInstallOrder);
      assertEquals(6, delegate.installInstallOrder);
      assertEquals(-1, delegate.installUninstallOrder);
      assertEquals(-1, delegate.startUninstallOrder);
      assertEquals(-1, delegate.createUninstallOrder);
      assertEquals(-1, delegate.configureUninstallOrder);
      assertEquals(-1, delegate.instantiateUninstallOrder);
      assertEquals(-1, delegate.describeUninstallOrder);
      assertChange(context, ControllerState.CREATE);
      assertEquals(1, delegate.describeInstallOrder);
      assertEquals(2, delegate.instantiateInstallOrder);
      assertEquals(3, delegate.configureInstallOrder);
      assertEquals(4, delegate.createInstallOrder);
      assertEquals(5, delegate.startInstallOrder);
      assertEquals(6, delegate.installInstallOrder);
      assertEquals(7, delegate.installUninstallOrder);
      assertEquals(8, delegate.startUninstallOrder);
      assertEquals(-1, delegate.createUninstallOrder);
      assertEquals(-1, delegate.configureUninstallOrder);
      assertEquals(-1, delegate.instantiateUninstallOrder);
      assertEquals(-1, delegate.describeUninstallOrder);
      assertChange(context, ControllerState.INSTALLED);
      assertEquals(1, delegate.describeInstallOrder);
      assertEquals(2, delegate.instantiateInstallOrder);
      assertEquals(3, delegate.configureInstallOrder);
      assertEquals(4, delegate.createInstallOrder);
      assertEquals(9, delegate.startInstallOrder);
      assertEquals(10, delegate.installInstallOrder);
      assertEquals(7, delegate.installUninstallOrder);
      assertEquals(8, delegate.startUninstallOrder);
      assertEquals(-1, delegate.createUninstallOrder);
      assertEquals(-1, delegate.configureUninstallOrder);
      assertEquals(-1, delegate.instantiateUninstallOrder);
      assertEquals(-1, delegate.describeUninstallOrder);
   }
   
   public void testNullChangeAutomatic() throws Throwable
   {
      TestDelegate delegate = new TestDelegate("NullChangeAutomaticTest");
      ControllerContext context = assertInstall(delegate);
      assertEquals(1, delegate.describeInstallOrder);
      assertEquals(2, delegate.instantiateInstallOrder);
      assertEquals(3, delegate.configureInstallOrder);
      assertEquals(4, delegate.createInstallOrder);
      assertEquals(5, delegate.startInstallOrder);
      assertEquals(6, delegate.installInstallOrder);
      assertEquals(-1, delegate.installUninstallOrder);
      assertEquals(-1, delegate.startUninstallOrder);
      assertEquals(-1, delegate.createUninstallOrder);
      assertEquals(-1, delegate.configureUninstallOrder);
      assertEquals(-1, delegate.instantiateUninstallOrder);
      assertEquals(-1, delegate.describeUninstallOrder);
      assertChange(context, ControllerState.INSTALLED);
      assertEquals(1, delegate.describeInstallOrder);
      assertEquals(2, delegate.instantiateInstallOrder);
      assertEquals(3, delegate.configureInstallOrder);
      assertEquals(4, delegate.createInstallOrder);
      assertEquals(5, delegate.startInstallOrder);
      assertEquals(6, delegate.installInstallOrder);
      assertEquals(-1, delegate.installUninstallOrder);
      assertEquals(-1, delegate.startUninstallOrder);
      assertEquals(-1, delegate.createUninstallOrder);
      assertEquals(-1, delegate.configureUninstallOrder);
      assertEquals(-1, delegate.instantiateUninstallOrder);
      assertEquals(-1, delegate.describeUninstallOrder);
      assertChange(context, ControllerState.CREATE);
      assertEquals(1, delegate.describeInstallOrder);
      assertEquals(2, delegate.instantiateInstallOrder);
      assertEquals(3, delegate.configureInstallOrder);
      assertEquals(4, delegate.createInstallOrder);
      assertEquals(5, delegate.startInstallOrder);
      assertEquals(6, delegate.installInstallOrder);
      assertEquals(7, delegate.installUninstallOrder);
      assertEquals(8, delegate.startUninstallOrder);
      assertEquals(-1, delegate.createUninstallOrder);
      assertEquals(-1, delegate.configureUninstallOrder);
      assertEquals(-1, delegate.instantiateUninstallOrder);
      assertEquals(-1, delegate.describeUninstallOrder);
      assertChange(context, ControllerState.CREATE);
      assertEquals(1, delegate.describeInstallOrder);
      assertEquals(2, delegate.instantiateInstallOrder);
      assertEquals(3, delegate.configureInstallOrder);
      assertEquals(4, delegate.createInstallOrder);
      assertEquals(5, delegate.startInstallOrder);
      assertEquals(6, delegate.installInstallOrder);
      assertEquals(7, delegate.installUninstallOrder);
      assertEquals(8, delegate.startUninstallOrder);
      assertEquals(-1, delegate.createUninstallOrder);
      assertEquals(-1, delegate.configureUninstallOrder);
      assertEquals(-1, delegate.instantiateUninstallOrder);
      assertEquals(-1, delegate.describeUninstallOrder);
   }
}
