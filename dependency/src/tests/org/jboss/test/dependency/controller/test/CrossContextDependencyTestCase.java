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
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.dependency.controller.support.OtherDelegate;
import org.jboss.test.dependency.controller.support.TestDelegate;

/**
 * A CrossContext DependencyTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class CrossContextDependencyTestCase extends AbstractDependencyTest
{
   public static Test suite()
   {
      return suite(CrossContextDependencyTestCase.class);
   }
   
   public CrossContextDependencyTestCase(String name)
   {
      super(name);
   }
   
   public void testDependencyCorrectOrder() throws Throwable
   {
      TestDelegate delegate1 = getDelegate1();
      assertInstall(delegate1);
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
      OtherDelegate delegate2 = getDelegate2();
      assertInstall(delegate2);
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
   
   public void testDependencyWrongOrder() throws Throwable
   {
      OtherDelegate delegate2 = getDelegate2();
      ControllerContext context2 = assertInstall(delegate2, ControllerState.PRE_INSTALL);
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
      TestDelegate delegate1 = getDelegate1();
      assertInstall(delegate1);
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
      assertContext(context2);
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
   
   public void testDependencyReinstall() throws Throwable
   {
      TestDelegate delegate1 = getDelegate1();
      ControllerContext context1 = assertInstall(delegate1);
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
      OtherDelegate delegate2 = getDelegate2();
      ControllerContext context2 = assertInstall(delegate2);
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
      context2 = assertInstall(delegate2);
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
      assertEquals(31, delegate1.installUninstallOrder);
      assertEquals(32, delegate1.startUninstallOrder);
      assertEquals(33, delegate1.createUninstallOrder);
      assertEquals(34, delegate1.configureUninstallOrder);
      assertEquals(35, delegate1.instantiateUninstallOrder);
      assertEquals(36, delegate1.describeUninstallOrder);
      assertContext("Name2", ControllerState.PRE_INSTALL);
      assertEquals(19, delegate2.describeInstallOrder);
      assertEquals(20, delegate2.instantiateInstallOrder);
      assertEquals(21, delegate2.configureInstallOrder);
      assertEquals(22, delegate2.createInstallOrder);
      assertEquals(23, delegate2.startInstallOrder);
      assertEquals(24, delegate2.installInstallOrder);
      assertEquals(25, delegate2.installUninstallOrder);
      assertEquals(26, delegate2.startUninstallOrder);
      assertEquals(27, delegate2.createUninstallOrder);
      assertEquals(28, delegate2.configureUninstallOrder);
      assertEquals(29, delegate2.instantiateUninstallOrder);
      assertEquals(30, delegate2.describeUninstallOrder);
      assertInstall(delegate1);
      assertEquals(37, delegate1.describeInstallOrder);
      assertEquals(38, delegate1.instantiateInstallOrder);
      assertEquals(39, delegate1.configureInstallOrder);
      assertEquals(40, delegate1.createInstallOrder);
      assertEquals(41, delegate1.startInstallOrder);
      assertEquals(42, delegate1.installInstallOrder);
      assertEquals(31, delegate1.installUninstallOrder);
      assertEquals(32, delegate1.startUninstallOrder);
      assertEquals(33, delegate1.createUninstallOrder);
      assertEquals(34, delegate1.configureUninstallOrder);
      assertEquals(35, delegate1.instantiateUninstallOrder);
      assertEquals(36, delegate1.describeUninstallOrder);
      assertContext("Name2", ControllerState.INSTALLED);
      assertEquals(43, delegate2.describeInstallOrder);
      assertEquals(44, delegate2.instantiateInstallOrder);
      assertEquals(45, delegate2.configureInstallOrder);
      assertEquals(46, delegate2.createInstallOrder);
      assertEquals(47, delegate2.startInstallOrder);
      assertEquals(48, delegate2.installInstallOrder);
      assertEquals(25, delegate2.installUninstallOrder);
      assertEquals(26, delegate2.startUninstallOrder);
      assertEquals(27, delegate2.createUninstallOrder);
      assertEquals(28, delegate2.configureUninstallOrder);
      assertEquals(29, delegate2.instantiateUninstallOrder);
      assertEquals(30, delegate2.describeUninstallOrder);
   }
   
   protected TestDelegate getDelegate1()
   {
      return new TestDelegate("Name1");
   }
   
   protected OtherDelegate getDelegate2()
   {
      OtherDelegate result = new OtherDelegate("Name2");
      result.addDependency(new AbstractDependencyItem("Name2", "Name1", ControllerState.DESCRIBED, ControllerState.INSTALLED));
      return result;
   }
}
