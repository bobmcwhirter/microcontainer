/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.deployers.client.spi;

import java.util.Collection;
import java.util.Map;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.DeploymentState;
import org.jboss.managed.api.ManagedObject;
import org.jboss.util.graph.Graph;

/**
 * DeployerClient.
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public interface DeployerClient
{
   /**
    * Get the top level deployments
    * 
    * @return the top level deployments
    */
   Collection<Deployment> getTopLevel();

   /**
    * Get a deployment
    * 
    * @param name the name of the deployment
    * @return the deployment or null if not found
    */
   Deployment getDeployment(String name);

   /**
    * Add a deployment
    * 
    * @param deployment the deployment
    * @throws DeploymentException for any error
    */
   void addDeployment(Deployment deployment) throws DeploymentException;

   /**
    * Remove a deployment
    * 
    * @param deployment the deployment to remove
    * @return false when the context was previously unknown
    * @throws DeploymentException for any error
    */
   boolean removeDeployment(Deployment deployment) throws DeploymentException;

   /**
    * Remove a deployment by name
    * 
    * @param name the name of the deployment
    * @return false when the context was previously unknown
    * @throws DeploymentException for any error
    */
   boolean removeDeployment(String name) throws DeploymentException;

   /**
    * Process the outstanding deployments.
    */
   void process();

   /**
    * Deploy a deployment
    * 
    * @param deployment the deployment
    * @throws DeploymentException for any error
    */
   void deploy(Deployment deployment) throws DeploymentException;

   /**
    * Undeploy a deployment
    * 
    * @param deployment the deployment
    * @return true when the deployment was undeployed
    * @throws DeploymentException for any error
    */
   boolean undeploy(Deployment deployment) throws DeploymentException;

   /**
    * Check all the deployments are complete
    * 
    * @throws DeploymentException when some deployment is not complete
    */
   void checkComplete() throws DeploymentException;

   /**
    * Check a single deployment is complete
    * 
    * @param deployment the deployment
    * @throws DeploymentException when the deployment is not complete
    */
   void checkComplete(Deployment deployment) throws DeploymentException;

   /**
    * Check a single deployment is complete
    * 
    * @param name the deployment name
    * @throws DeploymentException when the deployment is not complete
    */
   void checkComplete(String name) throws DeploymentException;
   
   /**
    * Undeploy a deployment by name
    * 
    * @param name the name of the deployment
    * @return true when the deployment was undeployed
    * @throws DeploymentException for any error
    */
   boolean undeploy(String name) throws DeploymentException;

   /**
    * Get a the state of deployment
    * 
    * @param name the name of the deployment
    * @return the deployment state 
    */
   DeploymentState getDeploymentState(String name);

   /**
    * Get the managed objects for a deployment context. This is a
    * mapping of the attachment names to the associated ManagedObject.
    *      
    * @param name the name of the deployment
    * @return the managed object map keyed by the attachment names.
    * @throws DeploymentException for any error
    */
   Map<String, ManagedObject> getManagedObjects(String name) throws DeploymentException;
   
   /**
    * Get the graph of managed objects starting with the top-level deployment associated with name.
    * 
    * @param name - the name of the top-level DeploymentContext to process.
    * @return the graph of managed objects for the top-level DeploymentContex and its children.
    * @throws DeploymentException
    */
   Graph<Map<String, ManagedObject>> getDeepManagedObjects(String name) throws DeploymentException;
}
