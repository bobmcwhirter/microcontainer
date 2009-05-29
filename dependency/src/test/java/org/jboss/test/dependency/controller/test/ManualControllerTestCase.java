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
 * A ManualControllerTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class ManualControllerTestCase extends AbstractDependencyTest
{
   public static Test suite()
   {
      return suite(ManualControllerTestCase.class);
   }
   
   public ManualControllerTestCase(String name)
   {
      super(name);
   }
   
   public void testManualInstall() throws Throwable
   {
      TestDelegate delegate = new TestDelegate("ManualInstallTest");
      delegate.setMode(ControllerMode.MANUAL);
      ControllerContext context = assertInstall(delegate, ControllerState.NOT_INSTALLED);
      assertEquals(-1, delegate.describeInstallOrder);
      assertEquals(-1, delegate.instantiateInstallOrder);
      assertEquals(-1, delegate.configureInstallOrder);
      assertEquals(-1, delegate.createInstallOrder);
      assertEquals(-1, delegate.startInstallOrder);
      assertEquals(-1, delegate.installInstallOrder);
      assertEquals(-1, delegate.installUninstallOrder);
      assertEquals(-1, delegate.startUninstallOrder);
      assertEquals(-1, delegate.createUninstallOrder);
      assertEquals(-1, delegate.configureUninstallOrder);
      assertEquals(-1, delegate.instantiateUninstallOrder);
      assertEquals(-1, delegate.describeUninstallOrder);
      assertChange(context, ControllerState.INSTANTIATED);
      assertEquals(1, delegate.describeInstallOrder);
      assertEquals(2, delegate.instantiateInstallOrder);
      assertEquals(-1, delegate.configureInstallOrder);
      assertEquals(-1, delegate.createInstallOrder);
      assertEquals(-1, delegate.startInstallOrder);
      assertEquals(-1, delegate.installInstallOrder);
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
   }
   
   public void testManualUninstall() throws Throwable
   {
      TestDelegate delegate = new TestDelegate("ManualUninstallTest");
      delegate.setMode(ControllerMode.MANUAL);
      ControllerContext context = assertInstall(delegate, ControllerState.NOT_INSTALLED);
      assertChange(context, ControllerState.INSTALLED); 
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
      assertUninstall(context);
      assertEquals(1, delegate.describeInstallOrder);
      assertEquals(2, delegate.instantiateInstallOrder);
      assertEquals(3, delegate.configureInstallOrder);
      assertEquals(4, delegate.createInstallOrder);
      assertEquals(5, delegate.startInstallOrder);
      assertEquals(6, delegate.installInstallOrder);
      assertEquals(7, delegate.installUninstallOrder);
      assertEquals(8, delegate.startUninstallOrder);
      assertEquals(9, delegate.createUninstallOrder);
      assertEquals(10, delegate.configureUninstallOrder);
      assertEquals(11, delegate.instantiateUninstallOrder);
      assertEquals(12, delegate.describeUninstallOrder);
   }
   
   public void testManualReinstall() throws Throwable
   {
      TestDelegate delegate = new TestDelegate("ManualReinstallTest");
      delegate.setMode(ControllerMode.MANUAL);
      ControllerContext context = assertInstall(delegate, ControllerState.NOT_INSTALLED);
      assertChange(context, ControllerState.INSTALLED);
      assertUninstall(context);
      context = assertInstall(delegate, ControllerState.NOT_INSTALLED);
      assertChange(context, ControllerState.INSTALLED);
      assertEquals(13, delegate.describeInstallOrder);
      assertEquals(14, delegate.instantiateInstallOrder);
      assertEquals(15, delegate.configureInstallOrder);
      assertEquals(16, delegate.createInstallOrder);
      assertEquals(17, delegate.startInstallOrder);
      assertEquals(18, delegate.installInstallOrder);
      assertEquals(7, delegate.installUninstallOrder);
      assertEquals(8, delegate.startUninstallOrder);
      assertEquals(9, delegate.createUninstallOrder);
      assertEquals(10, delegate.configureUninstallOrder);
      assertEquals(11, delegate.instantiateUninstallOrder);
      assertEquals(12, delegate.describeUninstallOrder);
   }
   
   public void testManualWithDependency() throws Throwable
   {
      TestDelegate dependee = new TestDelegate("Dependee");
      TestDelegate dependent = new TestDelegate("Dependent");
      dependent.addDependency(new AbstractDependencyItem("Dependent", "Dependee", ControllerState.START, ControllerState.INSTALLED));
      dependee.setMode(ControllerMode.MANUAL);
      ControllerContext dependeeContext = assertInstall(dependee, ControllerState.NOT_INSTALLED);
      dependent.setMode(ControllerMode.MANUAL);
      ControllerContext dependentContext = assertInstall(dependent, ControllerState.NOT_INSTALLED);
      
      assertChange(dependentContext, ControllerState.INSTALLED, ControllerState.CREATE);
      assertEquals(1, dependent.describeInstallOrder);
      assertEquals(2, dependent.instantiateInstallOrder);
      assertEquals(3, dependent.configureInstallOrder);
      assertEquals(4, dependent.createInstallOrder);
      assertEquals(-1, dependent.startInstallOrder);
      assertEquals(-1, dependent.installInstallOrder);
      assertEquals(-1, dependent.installUninstallOrder);
      assertEquals(-1, dependent.startUninstallOrder);
      assertEquals(-1, dependent.createUninstallOrder);
      assertEquals(-1, dependent.configureUninstallOrder);
      assertEquals(-1, dependent.instantiateUninstallOrder);
      assertEquals(-1, dependent.describeUninstallOrder);
      
      assertChange(dependeeContext, ControllerState.INSTALLED);
      assertEquals(1, dependent.describeInstallOrder);
      assertEquals(2, dependent.instantiateInstallOrder);
      assertEquals(3, dependent.configureInstallOrder);
      assertEquals(4, dependent.createInstallOrder);
      assertEquals(11, dependent.startInstallOrder);
      assertEquals(12, dependent.installInstallOrder);
      assertEquals(-1, dependent.installUninstallOrder);
      assertEquals(-1, dependent.startUninstallOrder);
      assertEquals(-1, dependent.createUninstallOrder);
      assertEquals(-1, dependent.configureUninstallOrder);
      assertEquals(-1, dependent.instantiateUninstallOrder);
      assertEquals(-1, dependent.describeUninstallOrder);
      assertEquals(5, dependee.describeInstallOrder);
      assertEquals(6, dependee.instantiateInstallOrder);
      assertEquals(7, dependee.configureInstallOrder);
      assertEquals(8, dependee.createInstallOrder);
      assertEquals(9, dependee.startInstallOrder);
      assertEquals(10, dependee.installInstallOrder);
      assertEquals(-1, dependee.installUninstallOrder);
      assertEquals(-1, dependee.startUninstallOrder);
      assertEquals(-1, dependee.createUninstallOrder);
      assertEquals(-1, dependee.configureUninstallOrder);
      assertEquals(-1, dependee.instantiateUninstallOrder);
      assertEquals(-1, dependee.describeUninstallOrder);
      
      assertChange(dependeeContext, ControllerState.START);
      assertEquals(1, dependent.describeInstallOrder);
      assertEquals(2, dependent.instantiateInstallOrder);
      assertEquals(3, dependent.configureInstallOrder);
      assertEquals(4, dependent.createInstallOrder);
      assertEquals(11, dependent.startInstallOrder);
      assertEquals(12, dependent.installInstallOrder);
      assertEquals(13, dependent.installUninstallOrder);
      assertEquals(14, dependent.startUninstallOrder);
      assertEquals(-1, dependent.createUninstallOrder);
      assertEquals(-1, dependent.configureUninstallOrder);
      assertEquals(-1, dependent.instantiateUninstallOrder);
      assertEquals(-1, dependent.describeUninstallOrder);
      assertEquals(5, dependee.describeInstallOrder);
      assertEquals(6, dependee.instantiateInstallOrder);
      assertEquals(7, dependee.configureInstallOrder);
      assertEquals(8, dependee.createInstallOrder);
      assertEquals(9, dependee.startInstallOrder);
      assertEquals(10, dependee.installInstallOrder);
      assertEquals(15, dependee.installUninstallOrder);
      assertEquals(-1, dependee.startUninstallOrder);
      assertEquals(-1, dependee.createUninstallOrder);
      assertEquals(-1, dependee.configureUninstallOrder);
      assertEquals(-1, dependee.instantiateUninstallOrder);
      assertEquals(-1, dependee.describeUninstallOrder);
   }
   
   public void testManualWithDependencyUninstallDependentFirst() throws Throwable
   {
      TestDelegate dependee = new TestDelegate("Dependee");
      TestDelegate dependent = new TestDelegate("Dependent");
      dependent.addDependency(new AbstractDependencyItem("Dependent", "Dependee", ControllerState.START, ControllerState.INSTALLED));
      dependee.setMode(ControllerMode.MANUAL);
      ControllerContext dependeeContext = assertInstall(dependee, ControllerState.NOT_INSTALLED);
      dependent.setMode(ControllerMode.MANUAL);
      ControllerContext dependentContext = assertInstall(dependent, ControllerState.NOT_INSTALLED);
      
      assertChange(dependentContext, ControllerState.INSTALLED, ControllerState.CREATE);
      assertEquals(1, dependent.describeInstallOrder);
      assertEquals(2, dependent.instantiateInstallOrder);
      assertEquals(3, dependent.configureInstallOrder);
      assertEquals(4, dependent.createInstallOrder);
      assertEquals(-1, dependent.startInstallOrder);
      assertEquals(-1, dependent.installInstallOrder);
      assertEquals(-1, dependent.installUninstallOrder);
      assertEquals(-1, dependent.startUninstallOrder);
      assertEquals(-1, dependent.createUninstallOrder);
      assertEquals(-1, dependent.configureUninstallOrder);
      assertEquals(-1, dependent.instantiateUninstallOrder);
      assertEquals(-1, dependent.describeUninstallOrder);
      
      assertChange(dependeeContext, ControllerState.INSTALLED);
      assertEquals(1, dependent.describeInstallOrder);
      assertEquals(2, dependent.instantiateInstallOrder);
      assertEquals(3, dependent.configureInstallOrder);
      assertEquals(4, dependent.createInstallOrder);
      assertEquals(11, dependent.startInstallOrder);
      assertEquals(12, dependent.installInstallOrder);
      assertEquals(-1, dependent.installUninstallOrder);
      assertEquals(-1, dependent.startUninstallOrder);
      assertEquals(-1, dependent.createUninstallOrder);
      assertEquals(-1, dependent.configureUninstallOrder);
      assertEquals(-1, dependent.instantiateUninstallOrder);
      assertEquals(-1, dependent.describeUninstallOrder);
      assertEquals(5, dependee.describeInstallOrder);
      assertEquals(6, dependee.instantiateInstallOrder);
      assertEquals(7, dependee.configureInstallOrder);
      assertEquals(8, dependee.createInstallOrder);
      assertEquals(9, dependee.startInstallOrder);
      assertEquals(10, dependee.installInstallOrder);
      assertEquals(-1, dependee.installUninstallOrder);
      assertEquals(-1, dependee.startUninstallOrder);
      assertEquals(-1, dependee.createUninstallOrder);
      assertEquals(-1, dependee.configureUninstallOrder);
      assertEquals(-1, dependee.instantiateUninstallOrder);
      assertEquals(-1, dependee.describeUninstallOrder);
      
      assertChange(dependentContext, ControllerState.CREATE);
      assertEquals(1, dependent.describeInstallOrder);
      assertEquals(2, dependent.instantiateInstallOrder);
      assertEquals(3, dependent.configureInstallOrder);
      assertEquals(4, dependent.createInstallOrder);
      assertEquals(11, dependent.startInstallOrder);
      assertEquals(12, dependent.installInstallOrder);
      assertEquals(13, dependent.installUninstallOrder);
      assertEquals(14, dependent.startUninstallOrder);
      assertEquals(-1, dependent.createUninstallOrder);
      assertEquals(-1, dependent.configureUninstallOrder);
      assertEquals(-1, dependent.instantiateUninstallOrder);
      assertEquals(-1, dependent.describeUninstallOrder);
      assertEquals(5, dependee.describeInstallOrder);
      assertEquals(6, dependee.instantiateInstallOrder);
      assertEquals(7, dependee.configureInstallOrder);
      assertEquals(8, dependee.createInstallOrder);
      assertEquals(9, dependee.startInstallOrder);
      assertEquals(10, dependee.installInstallOrder);
      assertEquals(-1, dependee.installUninstallOrder);
      assertEquals(-1, dependee.startUninstallOrder);
      assertEquals(-1, dependee.createUninstallOrder);
      assertEquals(-1, dependee.configureUninstallOrder);
      assertEquals(-1, dependee.instantiateUninstallOrder);
      assertEquals(-1, dependee.describeUninstallOrder);
      
      assertChange(dependeeContext, ControllerState.CREATE);
      assertEquals(1, dependent.describeInstallOrder);
      assertEquals(2, dependent.instantiateInstallOrder);
      assertEquals(3, dependent.configureInstallOrder);
      assertEquals(4, dependent.createInstallOrder);
      assertEquals(11, dependent.startInstallOrder);
      assertEquals(12, dependent.installInstallOrder);
      assertEquals(13, dependent.installUninstallOrder);
      assertEquals(14, dependent.startUninstallOrder);
      assertEquals(-1, dependent.createUninstallOrder);
      assertEquals(-1, dependent.configureUninstallOrder);
      assertEquals(-1, dependent.instantiateUninstallOrder);
      assertEquals(-1, dependent.describeUninstallOrder);
      assertEquals(5, dependee.describeInstallOrder);
      assertEquals(6, dependee.instantiateInstallOrder);
      assertEquals(7, dependee.configureInstallOrder);
      assertEquals(8, dependee.createInstallOrder);
      assertEquals(9, dependee.startInstallOrder);
      assertEquals(10, dependee.installInstallOrder);
      assertEquals(15, dependee.installUninstallOrder);
      assertEquals(16, dependee.startUninstallOrder);
      assertEquals(-1, dependee.createUninstallOrder);
      assertEquals(-1, dependee.configureUninstallOrder);
      assertEquals(-1, dependee.instantiateUninstallOrder);
      assertEquals(-1, dependee.describeUninstallOrder);
   }

   public void testManualAndAutoMixed() throws Throwable
   {
      ControllerContext manual = createControllerContext("Manual");
      manual.setMode(ControllerMode.MANUAL);
      install(manual);
      assertChange(manual, ControllerState.DESCRIBED, ControllerState.DESCRIBED);

      ControllerContext auto = createControllerContext("Auto");
      auto.setMode(ControllerMode.AUTOMATIC);
      install(auto);
      assertContext(auto);

      // check that the auto doesn't move the manual
      assertContext(manual, ControllerState.DESCRIBED);
   }
}
