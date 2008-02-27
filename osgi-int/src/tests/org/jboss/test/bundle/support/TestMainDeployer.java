/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
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

public class TestMainDeployer implements MainDeployer
{

   /** Create a new TestMainDeployer.
    * 
    * @param bundleImplTestCase
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