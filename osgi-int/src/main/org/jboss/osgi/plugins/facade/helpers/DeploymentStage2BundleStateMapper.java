/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, JBoss Inc., and individual contributors as indicated
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

import org.jboss.deployers.spi.deployer.DeploymentStage;
import org.jboss.deployers.spi.deployer.DeploymentStages;
import org.jboss.osgi.plugins.facade.BundleState;

/**
 * A DeploymentStage2BundleStateMapper.
 * Maps a DeploymentStage to a BundleState
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public class DeploymentStage2BundleStateMapper
{
   private static List<DeploymentStage2BundleState> deploymentStage2BundleStates = new ArrayList<DeploymentStage2BundleState>();

   static
   {
      deploymentStage2BundleStates.add(new DeploymentStage2BundleState(DeploymentStages.NOT_INSTALLED, BundleState.UNINSTALLED));
      deploymentStage2BundleStates.add(new DeploymentStage2BundleState(DeploymentStages.CLASSLOADER, BundleState.RESOLVED));
      deploymentStage2BundleStates.add(new DeploymentStage2BundleState(DeploymentStages.REAL, BundleState.STARTING));
      deploymentStage2BundleStates.add(new DeploymentStage2BundleState(DeploymentStages.INSTALLED, BundleState.ACTIVE));
   }

   /**
    * Given a DeploymentStage returns corresponding BundleState
    * 
    * @param controllerState the controller state
    * @return BundleState the bundle state
    */
   public static BundleState mapBundleState(final DeploymentStage controllerState)
   {
      for (DeploymentStage2BundleState deploymentStage2BundleState : deploymentStage2BundleStates)
      {
         if (deploymentStage2BundleState.getDeploymentStage().equals(controllerState))
         {
            return deploymentStage2BundleState.getBundleState();
         }
      }
      return BundleState.UNINSTALLED;
   }

}
