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

import org.jboss.dependency.plugins.AbstractControllerContext;
import org.jboss.dependency.plugins.AbstractControllerContextActions;
import org.jboss.dependency.plugins.spi.action.ControllerContextAction;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;

/**
 * A TestControllerContext.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class OtherControllerContext extends AbstractControllerContext
{
   private static final AbstractControllerContextActions actions;
   
   static
   {
      HashMap<ControllerState, ControllerContextAction> map = new HashMap<ControllerState, ControllerContextAction>();
      map.put(ControllerState.DESCRIBED, new DescribeAction());
      map.put(ControllerState.INSTANTIATED, new InstantiateAction());
      map.put(ControllerState.CONFIGURED, new ConfigureAction());
      map.put(ControllerState.CREATE, new CreateAction());
      map.put(ControllerState.START, new StartAction());
      map.put(ControllerState.INSTALLED, new InstallAction());
      actions = new AbstractControllerContextActions(map);
   }
   
   private OtherDelegate delegate;
   
   public OtherControllerContext(OtherDelegate delegate)
   {
      super(delegate.getName(), actions, delegate.dependencies);
      this.delegate = delegate;
   }
   
   public static class DescribeAction implements ControllerContextAction
   {
      public void install(ControllerContext context) throws Throwable
      {
         ((OtherControllerContext) context).delegate.describeInstall();
      }

      public void uninstall(ControllerContext context)
      {
         ((OtherControllerContext) context).delegate.describeUninstall();
      }
   }
   
   public static class InstantiateAction implements ControllerContextAction
   {
      public void install(ControllerContext context) throws Throwable
      {
         ((OtherControllerContext) context).delegate.instantiateInstall();
      }

      public void uninstall(ControllerContext context)
      {
         ((OtherControllerContext) context).delegate.instantiateUninstall();
      }
   }
   
   public static class ConfigureAction implements ControllerContextAction
   {
      public void install(ControllerContext context) throws Throwable
      {
         ((OtherControllerContext) context).delegate.configureInstall();
      }

      public void uninstall(ControllerContext context)
      {
         ((OtherControllerContext) context).delegate.configureUninstall();
      }
   }
   
   public static class CreateAction implements ControllerContextAction
   {
      public void install(ControllerContext context) throws Throwable
      {
         ((OtherControllerContext) context).delegate.createInstall();
      }

      public void uninstall(ControllerContext context)
      {
         ((OtherControllerContext) context).delegate.createUninstall();
      }
   }
   
   public static class StartAction implements ControllerContextAction
   {
      public void install(ControllerContext context) throws Throwable
      {
         ((OtherControllerContext) context).delegate.startInstall();
      }

      public void uninstall(ControllerContext context)
      {
         ((OtherControllerContext) context).delegate.startUninstall();
      }
   }
   
   public static class InstallAction implements ControllerContextAction
   {
      public void install(ControllerContext context) throws Throwable
      {
         ((OtherControllerContext) context).delegate.installInstall();
      }

      public void uninstall(ControllerContext context)
      {
         ((OtherControllerContext) context).delegate.installUninstall();
      }
   }
}
