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
package org.jboss.kernel.plugins.dependency;

import java.util.HashMap;
import java.util.Map;

import org.jboss.dependency.plugins.AbstractControllerContextActions;
import org.jboss.dependency.plugins.action.ControllerContextAction;
import org.jboss.dependency.spi.ControllerState;

/**
 * The KernelControllerActions.<p>
 * 
 * When there is a security manager and we are using a native context
 * we switch to our privileges to run the actions and switch back to callout to beans.<p>
 * 
 * Otherwise, we just use the caller's privileges.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class KernelControllerContextActions extends AbstractControllerContextActions
{
   /** The single instance */
   private static KernelControllerContextActions instance;

   /** Actions without instantiate */
   private static KernelControllerContextActions noInstantiate;
   
   /**
    * Get the instance
    * 
    * @return the actions
    */
   public static KernelControllerContextActions getInstance()
   {
      if (instance == null)
      {
         Map<ControllerState, ControllerContextAction> actions = new HashMap<ControllerState, ControllerContextAction>();
         actions.put(ControllerState.PRE_INSTALL, new PreInstallAction());
         actions.put(ControllerState.DESCRIBED, new DescribeAction());
         actions.put(ControllerState.INSTANTIATED, new InstantiateAction());
         actions.put(ControllerState.CONFIGURED, new ConfigureAction());
         actions.put(ControllerState.CREATE, new CreateDestroyLifecycleAction());
         actions.put(ControllerState.START, new StartStopLifecycleAction());
         actions.put(ControllerState.INSTALLED, new InstallAction());
         instance = new KernelControllerContextActions(actions);
      }
      return instance;
   }
   
   /**
    * Get no instantiate actions
    * 
    * @return the actions
    */
   public static KernelControllerContextActions getNoInstantiate()
   {
      if (noInstantiate == null)
      {
         Map<ControllerState, ControllerContextAction> actions = new HashMap<ControllerState, ControllerContextAction>();
         actions.put(ControllerState.PRE_INSTALL, new PreInstallAction());
         actions.put(ControllerState.DESCRIBED, new DescribeAction());
         actions.put(ControllerState.CONFIGURED, new ConfigureAction());
         actions.put(ControllerState.CREATE, new CreateDestroyLifecycleAction());
         actions.put(ControllerState.START, new StartStopLifecycleAction());
         actions.put(ControllerState.INSTALLED, new InstallAction());
         noInstantiate = new KernelControllerContextActions(actions);
      }
      return noInstantiate;
   }
   
   /**
    * Create a new KernelControllerContextActions.
    * 
    * @param actions the actions
    */
   protected KernelControllerContextActions(Map<ControllerState, ControllerContextAction> actions)
   {
      super(actions);
   }
}
