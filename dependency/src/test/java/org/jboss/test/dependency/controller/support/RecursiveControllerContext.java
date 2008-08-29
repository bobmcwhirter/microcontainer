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

import java.util.HashMap;

import org.jboss.dependency.plugins.AbstractControllerContextActions;
import org.jboss.dependency.plugins.action.ControllerContextAction;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.dependency.controller.test.RecursiveControllerActionTestCase;

/**
 * A TestControllerContext.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class RecursiveControllerContext extends TestControllerContext
{
   private static final AbstractControllerContextActions actions;
   
   static
   {
      HashMap<ControllerState, ControllerContextAction> map = new HashMap<ControllerState, ControllerContextAction>();
      map.put(ControllerState.DESCRIBED, new DescribeAction());
      map.put(ControllerState.INSTANTIATED, new InstantiateAction());
      map.put(ControllerState.CONFIGURED, new ConfigureAction());
      map.put(ControllerState.CREATE, new RecursiveCreateAction());
      map.put(ControllerState.START, new StartAction());
      map.put(ControllerState.INSTALLED, new InstallAction());
      actions = new AbstractControllerContextActions(map);
   }
   
   RecursiveControllerActionTestCase recursive;
   
   public RecursiveControllerContext child;
   
   public Throwable error;
   
   public RecursiveControllerContext(TestDelegate delegate, RecursiveControllerActionTestCase recursive)
   {
      super(delegate, actions);
      this.recursive = recursive;
   }
   
   public static class RecursiveCreateAction extends CreateAction
   {
      public void install(ControllerContext context) throws Throwable
      {
         super.install(context);
         RecursiveControllerContext recursiveContext = (RecursiveControllerContext) context;
         if (recursiveContext.recursive != null)
            recursiveContext.recursive.installChild(recursiveContext);
      }

      public void uninstall(ControllerContext context)
      {
         RecursiveControllerContext recursiveContext = (RecursiveControllerContext) context;
         if (recursiveContext.recursive != null)
            recursiveContext.recursive.uninstallChild(recursiveContext);
         super.uninstall(context);
      }
   }
}
