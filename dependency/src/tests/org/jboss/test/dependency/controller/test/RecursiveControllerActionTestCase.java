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
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.test.dependency.controller.support.RecursiveControllerContext;
import org.jboss.test.dependency.controller.support.TestControllerContext;
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

      TestDelegate other = new TestDelegate("Other");
      TestControllerContext otherContext = new TestControllerContext(other);
      DependencyItem item = new AbstractDependencyItem(other.getName(), delegate.getName(), ControllerState.CREATE, ControllerState.CONFIGURED);
      otherContext.getDependencyInfo().addIDependOn(item);
      assertInstall(otherContext, ControllerState.CONFIGURED);
      
      assertInstall(context, ControllerState.INSTALLED);
      assertContext(otherContext, ControllerState.INSTALLED);
      assertContext(context.child, ControllerState.INSTALLED);
   }
   
   public void testUninstall() throws Throwable
   {
      TestDelegate delegate = new TestDelegate("UninstallTestRecursive");
      RecursiveControllerContext context = new RecursiveControllerContext(delegate, this);

      TestDelegate other = new TestDelegate("Other");
      TestControllerContext otherContext = new TestControllerContext(other);
      DependencyItem item = new AbstractDependencyItem(other.getName(), delegate.getName(), ControllerState.CREATE, ControllerState.CONFIGURED);
      otherContext.getDependencyInfo().addIDependOn(item);
      assertInstall(otherContext, ControllerState.CONFIGURED);
      
      assertInstall(context, ControllerState.INSTALLED);
      assertContext(otherContext, ControllerState.INSTALLED);
      assertContext(context.child, ControllerState.INSTALLED);
      
      assertUninstall(context);
      if (context.child.error != null)
         throw context.child.error;
      assertNoContext(context.child);
      assertUninstall(otherContext);
   }
   
   public void testReinstall() throws Throwable
   {
      TestDelegate delegate = new TestDelegate("ReinstallTestRecursive");
      RecursiveControllerContext context = new RecursiveControllerContext(delegate, this);

      TestDelegate other = new TestDelegate("Other");
      TestControllerContext otherContext = new TestControllerContext(other);
      DependencyItem item = new AbstractDependencyItem(other.getName(), delegate.getName(), ControllerState.CREATE, ControllerState.CONFIGURED);
      otherContext.getDependencyInfo().addIDependOn(item);
      assertInstall(otherContext, ControllerState.CONFIGURED);

      assertInstall(context, ControllerState.INSTALLED);
      assertContext(otherContext, ControllerState.INSTALLED);
      assertContext(context.child, ControllerState.INSTALLED);

      assertUninstall(context);
      if (context.child.error != null)
         throw context.child.error;
      assertNoContext(context.child);
      assertContext(otherContext, ControllerState.CONFIGURED);
      
      context = new RecursiveControllerContext(delegate, this);
      assertInstall(context, ControllerState.INSTALLED);
      if (context.child.error != null)
         throw context.child.error;
      assertContext(otherContext, ControllerState.INSTALLED);
      assertContext(context.child, ControllerState.INSTALLED);
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
