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

import java.util.Collection;
import java.util.Map;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.managed.api.ManagedObject;
import org.jboss.util.graph.Graph;


/**
 * MainDeployer.<p>
 * 
 * The basic contract is to add and remove deployment
 * contexts then use process method to actually
 * bring the deployments to required state. 
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Scott.Stark@jboss.org
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
    * Get the managed objects for a deployment context. This is a
    * mapping of the attachment names to the associated ManagedObject.
    *      
    * @param context the context
    * @return the managed object map keyed by the attachment names.
    * @throws DeploymentException for any error
    */
   public Map<String, ManagedObject> getManagedObjects(DeploymentContext context) throws DeploymentException;
   /**
    * Get the graph of managed objects starting with the top-level deployment associated with name.
    * @param name - the name of the top-level DeploymentContext to process.
    * @return the graph of managed objects for the top-level DeploymentContex and its children.
    * @throws DeploymentException
    */
   public Graph<Map<String, ManagedObject>> getManagedObjects(String name) throws DeploymentException;

   /**
    * Get the top level deployments
    * 
    * @return the top level deployments
    */
   Collection<DeploymentContext> getTopLevel();
   
   /**
    * Get all the deployments
    * 
    * @return the deployments
    */
   Collection<DeploymentContext> getAll();
   
   /**
    * Get the deployments in error
    * 
    * @return the deployments
    */
   Collection<DeploymentContext> getErrors();
   
   /**
    * Get the deployments missing a deployer
    * 
    * @return the deployments
    */
   Collection<DeploymentContext> getMissingDeployer();
   
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
    * Process the outstanding deployments.
    * This is equivalent to calling process(-1, Integer.MAX_VALUE).
    */
   void process();
   /**
    * Process all the outstanding deployments through the deployers whose
    * relative order is in the range [begin, end), which begin <= ro < end.
    * 
    * @param begin - the minimum relative order value of of deployers to
    * use
    * @param end - the max relative order value of of deployers to
    * use
    * @return the top-level DeploymentContexts that were processed 
    */
   Collection<DeploymentContext> process(int begin, int end);
   
   /**
    * Shutdown. Removes all the deployments.
    */
   void shutdown();
}
