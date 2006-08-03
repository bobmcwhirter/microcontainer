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

import org.jboss.dependency.plugins.AbstractDependencyItem;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.dependency.controller.support.TestDelegate;

/**
 * A ChangeDependencyTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class ChangeDependencyTestCase extends AbstractDependencyTest
{
   public static Test suite()
   {
      return suite(ChangeDependencyTestCase.class);
   }
   
   public ChangeDependencyTestCase(String name)
   {
      super(name);
   }
   
   public void testChangeDependencyCorrectOrder() throws Throwable
   {
      TestDelegate delegate1 = getDelegate1();
      ControllerContext context1 = assertInstall(delegate1, ControllerState.NOT_INSTALLED);
      assertEquals(-1, delegate1.describeInstallOrder);
      assertEquals(-1, delegate1.instantiateInstallOrder);
      assertEquals(-1, delegate1.configureInstallOrder);
      assertEquals(-1, delegate1.createInstallOrder);
      assertEquals(-1, delegate1.startInstallOrder);
      assertEquals(-1, delegate1.installInstallOrder);
      assertEquals(-1, delegate1.installUninstallOrder);
      assertEquals(-1, delegate1.startUninstallOrder);
      assertEquals(-1, delegate1.createUninstallOrder);
      assertEquals(-1, delegate1.configureUninstallOrder);
      assertEquals(-1, delegate1.instantiateUninstallOrder);
      assertEquals(-1, delegate1.describeUninstallOrder);
      assertChange(context1, ControllerState.INSTALLED);
      assertEquals(1, delegate1.describeInstallOrder);
      assertEquals(2, delegate1.instantiateInstallOrder);
      assertEquals(3, delegate1.configureInstallOrder);
      assertEquals(4, delegate1.createInstallOrder);
      assertEquals(5, delegate1.startInstallOrder);
      assertEquals(6, delegate1.installInstallOrder);
      assertEquals(-1, delegate1.installUninstallOrder);
      assertEquals(-1, delegate1.startUninstallOrder);
      assertEquals(-1, delegate1.createUninstallOrder);
      assertEquals(-1, delegate1.configureUninstallOrder);
      assertEquals(-1, delegate1.instantiateUninstallOrder);
      assertEquals(-1, delegate1.describeUninstallOrder);
      TestDelegate delegate2 = getDelegate2();
      ControllerContext context2 = assertInstall(delegate2, ControllerState.NOT_INSTALLED);
      assertEquals(-1, delegate2.describeInstallOrder);
      assertEquals(-1, delegate2.instantiateInstallOrder);
      assertEquals(-1, delegate2.configureInstallOrder);
      assertEquals(-1, delegate2.createInstallOrder);
      assertEquals(-1, delegate2.startInstallOrder);
      assertEquals(-1, delegate2.installInstallOrder);
      assertEquals(-1, delegate2.installUninstallOrder);
      assertEquals(-1, delegate2.startUninstallOrder);
      assertEquals(-1, delegate2.createUninstallOrder);
      assertEquals(-1, delegate2.configureUninstallOrder);
      assertEquals(-1, delegate2.instantiateUninstallOrder);
      assertEquals(-1, delegate2.describeUninstallOrder);
      assertChange(context2, ControllerState.INSTALLED);
      assertEquals(7, delegate2.describeInstallOrder);
      assertEquals(8, delegate2.instantiateInstallOrder);
      assertEquals(9, delegate2.configureInstallOrder);
      assertEquals(10, delegate2.createInstallOrder);
      assertEquals(11, delegate2.startInstallOrder);
      assertEquals(12, delegate2.installInstallOrder);
      assertEquals(-1, delegate2.installUninstallOrder);
      assertEquals(-1, delegate2.startUninstallOrder);
      assertEquals(-1, delegate2.createUninstallOrder);
      assertEquals(-1, delegate2.configureUninstallOrder);
      assertEquals(-1, delegate2.instantiateUninstallOrder);
      assertEquals(-1, delegate2.describeUninstallOrder);
   }
   
   public void testChangeDependencyWrongOrder() throws Throwable
   {
      TestDelegate delegate2 = getDelegate2();
      ControllerContext context2 = assertInstall(delegate2, ControllerState.NOT_INSTALLED);
      assertEquals(-1, delegate2.describeInstallOrder);
      assertEquals(-1, delegate2.instantiateInstallOrder);
      assertEquals(-1, delegate2.configureInstallOrder);
      assertEquals(-1, delegate2.createInstallOrder);
      assertEquals(-1, delegate2.startInstallOrder);
      assertEquals(-1, delegate2.installInstallOrder);
      assertEquals(-1, delegate2.installUninstallOrder);
      assertEquals(-1, delegate2.startUninstallOrder);
      assertEquals(-1, delegate2.createUninstallOrder);
      assertEquals(-1, delegate2.configureUninstallOrder);
      assertEquals(-1, delegate2.instantiateUninstallOrder);
      assertEquals(-1, delegate2.describeUninstallOrder);
      assertChange(context2, ControllerState.INSTALLED, ControllerState.CONFIGURED);
      assertEquals(1, delegate2.describeInstallOrder);
      assertEquals(2, delegate2.instantiateInstallOrder);
      assertEquals(3, delegate2.configureInstallOrder);
      assertEquals(-1, delegate2.createInstallOrder);
      assertEquals(-1, delegate2.startInstallOrder);
      assertEquals(-1, delegate2.installInstallOrder);
      assertEquals(-1, delegate2.installUninstallOrder);
      assertEquals(-1, delegate2.startUninstallOrder);
      assertEquals(-1, delegate2.createUninstallOrder);
      assertEquals(-1, delegate2.configureUninstallOrder);
      assertEquals(-1, delegate2.instantiateUninstallOrder);
      assertEquals(-1, delegate2.describeUninstallOrder);
      TestDelegate delegate1 = getDelegate1();
      ControllerContext context1 = assertInstall(delegate1, ControllerState.NOT_INSTALLED);
      assertEquals(-1, delegate1.describeInstallOrder);
      assertEquals(-1, delegate1.instantiateInstallOrder);
      assertEquals(-1, delegate1.configureInstallOrder);
      assertEquals(-1, delegate1.createInstallOrder);
      assertEquals(-1, delegate1.startInstallOrder);
      assertEquals(-1, delegate1.installInstallOrder);
      assertEquals(-1, delegate1.installUninstallOrder);
      assertEquals(-1, delegate1.startUninstallOrder);
      assertEquals(-1, delegate1.createUninstallOrder);
      assertEquals(-1, delegate1.configureUninstallOrder);
      assertEquals(-1, delegate1.instantiateUninstallOrder);
      assertEquals(-1, delegate1.describeUninstallOrder);
      assertContext(context2, ControllerState.CONFIGURED);
      assertEquals(1, delegate2.describeInstallOrder);
      assertEquals(2, delegate2.instantiateInstallOrder);
      assertEquals(3, delegate2.configureInstallOrder);
      assertEquals(-1, delegate2.createInstallOrder);
      assertEquals(-1, delegate2.startInstallOrder);
      assertEquals(-1, delegate2.installInstallOrder);
      assertEquals(-1, delegate2.installUninstallOrder);
      assertEquals(-1, delegate2.startUninstallOrder);
      assertEquals(-1, delegate2.createUninstallOrder);
      assertEquals(-1, delegate2.configureUninstallOrder);
      assertEquals(-1, delegate2.instantiateUninstallOrder);
      assertEquals(-1, delegate2.describeUninstallOrder);
      assertChange(context1, ControllerState.INSTALLED);
      assertEquals(4, delegate1.describeInstallOrder);
      assertEquals(5, delegate1.instantiateInstallOrder);
      assertEquals(6, delegate1.configureInstallOrder);
      assertEquals(7, delegate1.createInstallOrder);
      assertEquals(8, delegate1.startInstallOrder);
      assertEquals(9, delegate1.installInstallOrder);
      assertEquals(-1, delegate1.installUninstallOrder);
      assertEquals(-1, delegate1.startUninstallOrder);
      assertEquals(-1, delegate1.createUninstallOrder);
      assertEquals(-1, delegate1.configureUninstallOrder);
      assertEquals(-1, delegate1.instantiateUninstallOrder);
      assertEquals(-1, delegate1.describeUninstallOrder);
      assertContext(context2);
      assertEquals(1, delegate2.describeInstallOrder);
      assertEquals(2, delegate2.instantiateInstallOrder);
      assertEquals(3, delegate2.configureInstallOrder);
      assertEquals(10, delegate2.createInstallOrder);
      assertEquals(11, delegate2.startInstallOrder);
      assertEquals(12, delegate2.installInstallOrder);
      assertEquals(-1, delegate2.installUninstallOrder);
      assertEquals(-1, delegate2.startUninstallOrder);
      assertEquals(-1, delegate2.createUninstallOrder);
      assertEquals(-1, delegate2.configureUninstallOrder);
      assertEquals(-1, delegate2.instantiateUninstallOrder);
      assertEquals(-1, delegate2.describeUninstallOrder);
   }
   
   public void testChangeDependencyReinstall() throws Throwable
   {
      TestDelegate delegate1 = getDelegate1();
      ControllerContext context1 = assertInstall(delegate1, ControllerState.NOT_INSTALLED);
      assertEquals(-1, delegate1.describeInstallOrder);
      assertEquals(-1, delegate1.instantiateInstallOrder);
      assertEquals(-1, delegate1.configureInstallOrder);
      assertEquals(-1, delegate1.createInstallOrder);
      assertEquals(-1, delegate1.startInstallOrder);
      assertEquals(-1, delegate1.installInstallOrder);
      assertEquals(-1, delegate1.installUninstallOrder);
      assertEquals(-1, delegate1.startUninstallOrder);
      assertEquals(-1, delegate1.createUninstallOrder);
      assertEquals(-1, delegate1.configureUninstallOrder);
      assertEquals(-1, delegate1.instantiateUninstallOrder);
      assertEquals(-1, delegate1.describeUninstallOrder);
      assertChange(context1, ControllerState.INSTALLED);
      assertEquals(1, delegate1.describeInstallOrder);
      assertEquals(2, delegate1.instantiateInstallOrder);
      assertEquals(3, delegate1.configureInstallOrder);
      assertEquals(4, delegate1.createInstallOrder);
      assertEquals(5, delegate1.startInstallOrder);
      assertEquals(6, delegate1.installInstallOrder);
      assertEquals(-1, delegate1.installUninstallOrder);
      assertEquals(-1, delegate1.startUninstallOrder);
      assertEquals(-1, delegate1.createUninstallOrder);
      assertEquals(-1, delegate1.configureUninstallOrder);
      assertEquals(-1, delegate1.instantiateUninstallOrder);
      assertEquals(-1, delegate1.describeUninstallOrder);
      TestDelegate delegate2 = getDelegate2();
      ControllerContext context2 = assertInstall(delegate2, ControllerState.NOT_INSTALLED);
      assertEquals(-1, delegate2.describeInstallOrder);
      assertEquals(-1, delegate2.instantiateInstallOrder);
      assertEquals(-1, delegate2.configureInstallOrder);
      assertEquals(-1, delegate2.createInstallOrder);
      assertEquals(-1, delegate2.startInstallOrder);
      assertEquals(-1, delegate2.installInstallOrder);
      assertEquals(-1, delegate2.installUninstallOrder);
      assertEquals(-1, delegate2.startUninstallOrder);
      assertEquals(-1, delegate2.createUninstallOrder);
      assertEquals(-1, delegate2.configureUninstallOrder);
      assertEquals(-1, delegate2.instantiateUninstallOrder);
      assertEquals(-1, delegate2.describeUninstallOrder);
      assertChange(context2, ControllerState.INSTALLED);
      assertEquals(7, delegate2.describeInstallOrder);
      assertEquals(8, delegate2.instantiateInstallOrder);
      assertEquals(9, delegate2.configureInstallOrder);
      assertEquals(10, delegate2.createInstallOrder);
      assertEquals(11, delegate2.startInstallOrder);
      assertEquals(12, delegate2.installInstallOrder);
      assertEquals(-1, delegate2.installUninstallOrder);
      assertEquals(-1, delegate2.startUninstallOrder);
      assertEquals(-1, delegate2.createUninstallOrder);
      assertEquals(-1, delegate2.configureUninstallOrder);
      assertEquals(-1, delegate2.instantiateUninstallOrder);
      assertEquals(-1, delegate2.describeUninstallOrder);
      assertUninstall(context2);
      assertEquals(7, delegate2.describeInstallOrder);
      assertEquals(8, delegate2.instantiateInstallOrder);
      assertEquals(9, delegate2.configureInstallOrder);
      assertEquals(10, delegate2.createInstallOrder);
      assertEquals(11, delegate2.startInstallOrder);
      assertEquals(12, delegate2.installInstallOrder);
      assertEquals(13, delegate2.installUninstallOrder);
      assertEquals(14, delegate2.startUninstallOrder);
      assertEquals(15, delegate2.createUninstallOrder);
      assertEquals(16, delegate2.configureUninstallOrder);
      assertEquals(17, delegate2.instantiateUninstallOrder);
      assertEquals(18, delegate2.describeUninstallOrder);
      assertContext("Name1", ControllerState.INSTALLED);
      assertEquals(1, delegate1.describeInstallOrder);
      assertEquals(2, delegate1.instantiateInstallOrder);
      assertEquals(3, delegate1.configureInstallOrder);
      assertEquals(4, delegate1.createInstallOrder);
      assertEquals(5, delegate1.startInstallOrder);
      assertEquals(6, delegate1.installInstallOrder);
      assertEquals(-1, delegate1.installUninstallOrder);
      assertEquals(-1, delegate1.startUninstallOrder);
      assertEquals(-1, delegate1.createUninstallOrder);
      assertEquals(-1, delegate1.configureUninstallOrder);
      assertEquals(-1, delegate1.instantiateUninstallOrder);
      assertEquals(-1, delegate1.describeUninstallOrder);
      context2 = assertInstall(delegate2, ControllerState.NOT_INSTALLED);
      assertEquals(7, delegate2.describeInstallOrder);
      assertEquals(8, delegate2.instantiateInstallOrder);
      assertEquals(9, delegate2.configureInstallOrder);
      assertEquals(10, delegate2.createInstallOrder);
      assertEquals(11, delegate2.startInstallOrder);
      assertEquals(12, delegate2.installInstallOrder);
      assertEquals(13, delegate2.installUninstallOrder);
      assertEquals(14, delegate2.startUninstallOrder);
      assertEquals(15, delegate2.createUninstallOrder);
      assertEquals(16, delegate2.configureUninstallOrder);
      assertEquals(17, delegate2.instantiateUninstallOrder);
      assertEquals(18, delegate2.describeUninstallOrder);
      assertChange(context2, ControllerState.INSTALLED);
      assertEquals(19, delegate2.describeInstallOrder);
      assertEquals(20, delegate2.instantiateInstallOrder);
      assertEquals(21, delegate2.configureInstallOrder);
      assertEquals(22, delegate2.createInstallOrder);
      assertEquals(23, delegate2.startInstallOrder);
      assertEquals(24, delegate2.installInstallOrder);
      assertEquals(13, delegate2.installUninstallOrder);
      assertEquals(14, delegate2.startUninstallOrder);
      assertEquals(15, delegate2.createUninstallOrder);
      assertEquals(16, delegate2.configureUninstallOrder);
      assertEquals(17, delegate2.instantiateUninstallOrder);
      assertEquals(18, delegate2.describeUninstallOrder);
      assertUninstall(context1);
      assertEquals(1, delegate1.describeInstallOrder);
      assertEquals(2, delegate1.instantiateInstallOrder);
      assertEquals(3, delegate1.configureInstallOrder);
      assertEquals(4, delegate1.createInstallOrder);
      assertEquals(5, delegate1.startInstallOrder);
      assertEquals(6, delegate1.installInstallOrder);
      assertEquals(28, delegate1.installUninstallOrder);
      assertEquals(29, delegate1.startUninstallOrder);
      assertEquals(30, delegate1.createUninstallOrder);
      assertEquals(31, delegate1.configureUninstallOrder);
      assertEquals(32, delegate1.instantiateUninstallOrder);
      assertEquals(33, delegate1.describeUninstallOrder);
      assertContext("Name2", ControllerState.CONFIGURED);
      assertEquals(19, delegate2.describeInstallOrder);
      assertEquals(20, delegate2.instantiateInstallOrder);
      assertEquals(21, delegate2.configureInstallOrder);
      assertEquals(22, delegate2.createInstallOrder);
      assertEquals(23, delegate2.startInstallOrder);
      assertEquals(24, delegate2.installInstallOrder);
      assertEquals(25, delegate2.installUninstallOrder);
      assertEquals(26, delegate2.startUninstallOrder);
      assertEquals(27, delegate2.createUninstallOrder);
      assertEquals(16, delegate2.configureUninstallOrder);
      assertEquals(17, delegate2.instantiateUninstallOrder);
      assertEquals(18, delegate2.describeUninstallOrder);
      context1 = assertInstall(delegate1, ControllerState.NOT_INSTALLED);
      assertEquals(1, delegate1.describeInstallOrder);
      assertEquals(2, delegate1.instantiateInstallOrder);
      assertEquals(3, delegate1.configureInstallOrder);
      assertEquals(4, delegate1.createInstallOrder);
      assertEquals(5, delegate1.startInstallOrder);
      assertEquals(6, delegate1.installInstallOrder);
      assertEquals(28, delegate1.installUninstallOrder);
      assertEquals(29, delegate1.startUninstallOrder);
      assertEquals(30, delegate1.createUninstallOrder);
      assertEquals(31, delegate1.configureUninstallOrder);
      assertEquals(32, delegate1.instantiateUninstallOrder);
      assertEquals(33, delegate1.describeUninstallOrder);
      assertChange(context1, ControllerState.INSTALLED);
      assertEquals(34, delegate1.describeInstallOrder);
      assertEquals(35, delegate1.instantiateInstallOrder);
      assertEquals(36, delegate1.configureInstallOrder);
      assertEquals(37, delegate1.createInstallOrder);
      assertEquals(38, delegate1.startInstallOrder);
      assertEquals(39, delegate1.installInstallOrder);
      assertEquals(28, delegate1.installUninstallOrder);
      assertEquals(29, delegate1.startUninstallOrder);
      assertEquals(30, delegate1.createUninstallOrder);
      assertEquals(31, delegate1.configureUninstallOrder);
      assertEquals(32, delegate1.instantiateUninstallOrder);
      assertEquals(33, delegate1.describeUninstallOrder);
      assertContext("Name2", ControllerState.INSTALLED);
      assertEquals(19, delegate2.describeInstallOrder);
      assertEquals(20, delegate2.instantiateInstallOrder);
      assertEquals(21, delegate2.configureInstallOrder);
      assertEquals(40, delegate2.createInstallOrder);
      assertEquals(41, delegate2.startInstallOrder);
      assertEquals(42, delegate2.installInstallOrder);
      assertEquals(25, delegate2.installUninstallOrder);
      assertEquals(26, delegate2.startUninstallOrder);
      assertEquals(27, delegate2.createUninstallOrder);
      assertEquals(16, delegate2.configureUninstallOrder);
      assertEquals(17, delegate2.instantiateUninstallOrder);
      assertEquals(18, delegate2.describeUninstallOrder);
   }
   
   protected TestDelegate getDelegate1()
   {
      TestDelegate result = new TestDelegate("Name1");
      result.setMode(ControllerMode.MANUAL);
      return result;
   }
   
   protected TestDelegate getDelegate2()
   {
      TestDelegate result = new TestDelegate("Name2");
      result.setMode(ControllerMode.MANUAL);
      result.addDependency(new AbstractDependencyItem("Name2", "Name1", ControllerState.CREATE, ControllerState.INSTALLED));
      return result;
   }
}
