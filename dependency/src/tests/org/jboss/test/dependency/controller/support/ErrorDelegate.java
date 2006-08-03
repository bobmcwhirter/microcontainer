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
package org.jboss.test.dependency.controller.support;

import org.jboss.dependency.plugins.AbstractDependencyInfo;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.DependencyItem;

/**
 * An ErrorDelegate.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.2 $
 */
public class ErrorDelegate
{
   private Object name;
   
   private int failWhen;

   public ControllerMode mode = ControllerMode.MANUAL;
   
   public AbstractDependencyInfo dependencies = new AbstractDependencyInfo();

   public static final int FAIL_IN_DESCRIBE = 1;
   public static final int FAIL_IN_INSTANTIATE = 2;
   public static final int FAIL_IN_CONFIGURE = 3;
   public static final int FAIL_IN_CREATE = 4;
   public static final int FAIL_IN_START = 5;
   public static final int FAIL_IN_INSTALL = 6;
   public static final int FAIL_IN_UNDESCRIBE = -1;
   public static final int FAIL_IN_UNINSTANTIATE = -2;
   public static final int FAIL_IN_UNCONFIGURE = -3;
   public static final int FAIL_IN_UNCREATE = -4;
   public static final int FAIL_IN_UNSTART = -5;
   public static final int FAIL_IN_UNINSTALL = -6;
   
   public ErrorDelegate(Object name, int failWhen)
   {
      this.name = name;
      this.failWhen = failWhen;
   }
   
   public Object getName()
   {
      return name;
   }
   
   public void describeInstall()
   {
      if (failWhen == FAIL_IN_DESCRIBE)
         throw new Error("Fail");
   }
   
   public void describeUninstall()
   {
      if (failWhen == FAIL_IN_UNDESCRIBE)
         throw new Error("Fail");
   }
   
   public void instantiateInstall()
   {
      if (failWhen == FAIL_IN_INSTANTIATE)
         throw new Error("Fail");
   }
   
   public void instantiateUninstall()
   {
      if (failWhen == FAIL_IN_UNINSTANTIATE)
         throw new Error("Fail");
   }
   
   public void configureInstall()
   {
      if (failWhen == FAIL_IN_CONFIGURE)
         throw new Error("Fail");
   }
   
   public void configureUninstall()
   {
      if (failWhen == FAIL_IN_UNCONFIGURE)
         throw new Error("Fail");
   }
   
   public void createInstall()
   {
      if (failWhen == FAIL_IN_CREATE)
         throw new Error("Fail");
   }
   
   public void createUninstall()
   {
      if (failWhen == FAIL_IN_UNCREATE)
         throw new Error("Fail");
   }
   
   public void startInstall()
   {
      if (failWhen == FAIL_IN_START)
         throw new Error("Fail");
   }
   
   public void startUninstall()
   {
      if (failWhen == FAIL_IN_UNSTART)
         throw new Error("Fail");
   }
   
   public void installInstall()
   {
      if (failWhen == FAIL_IN_INSTALL)
         throw new Error("Fail");
   }
   
   public void installUninstall()
   {
      if (failWhen == FAIL_IN_UNINSTALL)
         throw new Error("Fail");
   }
   
   public void addDependency(DependencyItem dependency)
   {
      dependencies.addIDependOn(dependency);
   }
}
