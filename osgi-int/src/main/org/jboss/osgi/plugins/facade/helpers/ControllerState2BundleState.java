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

import org.jboss.dependency.spi.ControllerState;
import org.jboss.osgi.plugins.facade.BundleState;

/**
 * 
 * A BundleState2DeploymentStage - Maps a DeploymentStage to BundleState
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public class ControllerState2BundleState
{
   private final ControllerState controllerState;
   private final BundleState bundleState;
   
   /**
    * 
    * Create a new DeploymentStage2BundleState.
    * 
    * @param deploymentStage
    * @param bundleState
    */
   public ControllerState2BundleState(ControllerState controllerState, BundleState bundleState)
   {
      super();
      this.controllerState = controllerState;
      this.bundleState = bundleState;
   }
   
   /**
    * Get controllerState
    * 
    * @return
    */
   public ControllerState getControllerState()
   {
      return controllerState;
   }
   
   /**
    * Get BundleState
    * 
    * @return
    */
   public BundleState getBundleState()
   {
      return bundleState;
   }
}
