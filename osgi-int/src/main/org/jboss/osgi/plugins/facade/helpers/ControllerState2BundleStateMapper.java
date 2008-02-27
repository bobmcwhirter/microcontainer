/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.osgi.plugins.facade.helpers;

import java.util.ArrayList;
import java.util.List;

import org.jboss.dependency.spi.ControllerState;
import org.jboss.osgi.plugins.facade.BundleState;

/**
 * 
 * A DeploymentStage2BundleStateMapper - Maps a DeploymentStage to a BundleState
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public class ControllerState2BundleStateMapper
{

   private static List<ControllerState2BundleState> deploymentStage2BundleStates = new ArrayList<ControllerState2BundleState>();
   static
   {
      deploymentStage2BundleStates.add(new ControllerState2BundleState(ControllerState.ERROR, BundleState.UNINSTALLED));
      deploymentStage2BundleStates.add(new ControllerState2BundleState(ControllerState.NOT_INSTALLED, BundleState.INSTALLED));
      deploymentStage2BundleStates.add(new ControllerState2BundleState(ControllerState.PRE_INSTALL, BundleState.INSTALLED));
      deploymentStage2BundleStates.add(new ControllerState2BundleState(ControllerState.DESCRIBED, BundleState.INSTALLED));
      deploymentStage2BundleStates.add(new ControllerState2BundleState(ControllerState.INSTANTIATED, BundleState.RESOLVED));
      deploymentStage2BundleStates.add(new ControllerState2BundleState(ControllerState.CONFIGURED, BundleState.RESOLVED));
      deploymentStage2BundleStates.add(new ControllerState2BundleState(ControllerState.CREATE, BundleState.RESOLVED));
      deploymentStage2BundleStates.add(new ControllerState2BundleState(ControllerState.START, BundleState.STARTING));
      deploymentStage2BundleStates.add(new ControllerState2BundleState(ControllerState.INSTALLED, BundleState.ACTIVE));
   }

   /**
    * Given a DeploymentStage returns corresponding BundleState
    * 
    * @param controllerState
    * @return BundleState
    */
   public static BundleState mapBundleState(final ControllerState controllerState)
   {
      for (ControllerState2BundleState deploymentStage2BundleState : deploymentStage2BundleStates)
      {
         if (deploymentStage2BundleState.getControllerState().equals(controllerState))
         {
            return deploymentStage2BundleState.getBundleState();
         }
      }
      return BundleState.UNINSTALLED;
   }

}
