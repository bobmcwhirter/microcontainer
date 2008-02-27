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
package org.jboss.test.bundle.support;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jboss.deployers.client.spi.Deployment;
import org.jboss.deployers.client.spi.main.MainDeployer;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.DeploymentState;
import org.jboss.deployers.spi.deployer.DeploymentStage;
import org.jboss.managed.api.ManagedDeployment;
import org.jboss.managed.api.ManagedObject;
import org.jboss.util.graph.Graph;

/**
 * TestMainDeployer.
 * 
 * @author johnbailey
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class TestMainDeployer implements MainDeployer
{

   /** Create a new TestMainDeployer.
    */
   public TestMainDeployer()
   {
   }

   private Map<String, DeploymentStage> changesRequested = new HashMap<String, DeploymentStage>();

   public void shutdown()
   {

   }

   public void addDeployment(Deployment deployment) throws DeploymentException
   {

   }

   public void change(String deploymentName, DeploymentStage stage) throws DeploymentException
   {
      changesRequested.put(deploymentName, stage);
   }

   public DeploymentStage getDeploymentStage(String deploymentName) throws DeploymentException
   {
      return changesRequested.get(deploymentName);
   }

   public void checkComplete() throws DeploymentException
   {

   }

   public void checkComplete(Deployment... deployment) throws DeploymentException
   {

   }

   public void checkComplete(String... names) throws DeploymentException
   {

   }

   public void checkStructureComplete(Deployment... deployments) throws DeploymentException
   {

   }

   public void checkStructureComplete(String... names) throws DeploymentException
   {

   }

   public void deploy(Deployment... deployments) throws DeploymentException
   {

   }

   public Graph<Map<String, ManagedObject>> getDeepManagedObjects(String name) throws DeploymentException
   {
      return null;
   }

   public Deployment getDeployment(String name)
   {
      return null;
   }

   public DeploymentState getDeploymentState(String name)
   {
      return null;
   }

   public ManagedDeployment getManagedDeployment(String name) throws DeploymentException
   {
      return null;
   }

   public Map<String, ManagedObject> getManagedObjects(String name) throws DeploymentException
   {
      return null;
   }

   public Collection<Deployment> getTopLevel()
   {
      return null;
   }

   public void process()
   {

   }

   public boolean removeDeployment(Deployment deployment) throws DeploymentException
   {
      return false;
   }

   public boolean removeDeployment(String name) throws DeploymentException
   {
      return false;
   }

   public void undeploy(Deployment... deployments) throws DeploymentException
   {

   }

   public void undeploy(String... names) throws DeploymentException
   {
   }

   public boolean changeCalled(String deploymentName, DeploymentStage stage)
   {
      DeploymentStage requestedChange = changesRequested.get(deploymentName);
      if (requestedChange != null)
      {
         return requestedChange.equals(stage);
      }
      return false;
   }

}