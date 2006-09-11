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
package org.jboss.deployers.spi.deployment;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.structure.DeploymentContext;

/**
 * MainDeployer.<p>
 * 
 * The basic contract is to add and remove deployment
 * contexts then use process method to actually
 * bring the deployments to required state. 
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public interface MainDeployer
{
   /**
    * Get a deployment context
    * 
    * @param name the name
    * @return the context or null if not found
    */
   DeploymentContext getDeploymentContext(String name);
   
   /**
    * Add a deployment context
    * 
    * @param context the deployment context
    * @throws DeploymentException for any error
    */
   void addDeploymentContext(DeploymentContext context) throws DeploymentException;

   /**
    * Remove a deployment context
    * 
    * @param name the name of the context
    * @return false when the context was previously unknown
    * @throws DeploymentException for any error
    */
   boolean removeDeploymentContext(String name) throws DeploymentException;

   /**
    * Process the outstanding deployments
    */
   void process();
   
   /**
    * Shutdown. Removes all the deployments.
    */
   void shutdown();
}
