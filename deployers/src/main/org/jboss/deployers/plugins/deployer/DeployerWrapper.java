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
package org.jboss.deployers.plugins.deployer;

import java.util.Map;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.Deployer;
import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.deployers.spi.managed.ManagedObjectBuilder;
import org.jboss.logging.Logger;
import org.jboss.managed.api.ManagedObject;

/**
 * DeployerWrapper.<p>
 * 
 * To avoid any problems with error handling by the deployers.
 * 
 * TODO change logging when full protocol is implemented
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class DeployerWrapper implements Deployer, ManagedObjectBuilder
{
   /** The log */
   private Logger log; 
   
   /** The deployer */
   private Deployer deployer;
   
   /** The managed object builder */
   private ManagedObjectBuilder managedObjectBuilder;
   
   /**
    * Create a new DeployerWrapper.
    * 
    * @param deployer the deployer
    */
   public DeployerWrapper(Deployer deployer)
   {
      if (deployer == null)
         throw new IllegalArgumentException("Null deployer");
      this.deployer = deployer;
      this.log = Logger.getLogger(deployer.getClass());
      
      // Check to see whether the deployer also builds managed  objects
      if (deployer instanceof ManagedObjectBuilder)
         managedObjectBuilder = (ManagedObjectBuilder) deployer;
   }
   
   public ManagedObjectBuilder getManagedObjectBuilder()
   {
      return managedObjectBuilder;
   }
   public void setManagedObjectBuilder(ManagedObjectBuilder managedObjectBuilder)
   {
      this.managedObjectBuilder = managedObjectBuilder;
   }

   public String getType()
   {
      return deployer.getType();
   }

   public boolean isRelevant(DeploymentUnit unit)
   {
      if (unit == null)
         throw new IllegalArgumentException("Null unit");

      try
      {
         boolean result = deployer.isRelevant(unit);
         if (log.isTraceEnabled())
            log.trace("isRelevant " + unit.getName() + " result=" + result);
         return result;
      }
      catch (Throwable t)
      {
         log.warn("Error during isRelevant: " + unit.getName(), t);
         return false;
      }
   }

   public void prepareDeploy(DeploymentUnit unit) throws DeploymentException
   {
      if (unit == null)
         throw new IllegalArgumentException("Null unit");

      try
      {
         log.trace("Preparing deployment: " + unit.getName());
         deployer.prepareDeploy(unit);
         log.trace("Prepared deployment:  " + unit.getName());
      }
      catch (Throwable t)
      {
         log.error("Error during prepare deployment: " + unit.getName(), t);
         throw DeploymentException.rethrowAsDeploymentException("Error during prepare deployment: " + unit.getName(), t);
      }
   }

   public void prepareUndeploy(DeploymentUnit unit)
   {
      if (unit == null)
         throw new IllegalArgumentException("Null unit");

      try
      {
         // TODO log.trace("Prepare undeployment: " + unit.getName());
         log.trace("Undeploying: " + unit.getName());
         deployer.prepareUndeploy(unit);
         // TODO log.trace("Prepared undeployment:  " + unit.getName());
         log.trace("Undeployed:  " + unit.getName());
      }
      catch (Throwable t)
      {
         // TODO log.warn("Error during prepare undeployment: " + unit.getName(), t);
         log.warn("Error during undeployment: " + unit.getName(), t);
      }
   }

   public void handoff(DeploymentUnit from, DeploymentUnit to) throws DeploymentException
   {
      if (from == null)
         throw new IllegalArgumentException("Null from deployment");
      if (to == null)
         throw new IllegalArgumentException("Null to deployment");

      try
      {
         log.trace("Handing off from=" + from.getName() + " to=" + to.getName());
         deployer.handoff(from, to);
         log.trace("Handed off from=" + from.getName() + " to=" + to.getName());
      }
      catch (Throwable t)
      {
         log.warn("Error during handoff from=" + from.getName() + " to=" + to.getName(), t);
         throw DeploymentException.rethrowAsDeploymentException("Error during handoff from=" + from.getName() + " to=" + to.getName(), t);
      }
   }

   public void commitDeploy(DeploymentUnit unit) throws DeploymentException
   {
      if (unit == null)
         throw new IllegalArgumentException("Null unit");

      try
      {
         // TODO log.debug("Commiting deployment: " + unit.getName());
         log.trace("Deploying: " + unit.getName());
         deployer.commitDeploy(unit);
         // TODO log.debug("Commited deployment:  " + unit.getName());
         log.trace("Deployed:  " + unit.getName());
      }
      catch (Throwable t)
      {
         // TODO log.error("Error during commit deploy: " + unit.getName(), t);
         log.error("Error during deployment: " + unit.getName(), t);
         // TODO throw DeploymentException.rethrowAsDeploymentException("Error during commit deployment: " + unit.getName(), t);
         throw DeploymentException.rethrowAsDeploymentException("Error during deployment: " + unit.getName(), t);
      }
   }

   public void commitUndeploy(DeploymentUnit unit)
   {
      if (unit == null)
         throw new IllegalArgumentException("Null unit");

      try
      {
         log.trace("Commiting undeployment: " + unit.getName());
         deployer.commitUndeploy(unit);
         log.trace("Commited undeployment:  " + unit.getName());
      }
      catch (Throwable t)
      {
         log.warn("Error during commit undeployment: " + unit.getName(), t);
      }
   }
   
   public int getRelativeOrder()
   {
      return deployer.getRelativeOrder();
   }

   public void setRelativeOrder(int order)
   {
      deployer.setRelativeOrder(order);
   }

   public void build(DeploymentUnit unit, Map<String, ManagedObject> managedObjects) throws DeploymentException
   {
      // Not a managed object builder
      if (managedObjectBuilder == null)
         return;
      
      if (unit == null)
         throw new IllegalArgumentException("Null unit");
      if (managedObjects == null)
         throw new IllegalArgumentException("Null managed objects");

      try
      {
         log.trace("build: " + unit.getName());
         managedObjectBuilder.build(unit, managedObjects);
         log.trace("built: " + unit.getName());
      }
      catch (Throwable t)
      {
         log.warn("Error during commit undeployment: " + unit.getName(), t);
      }
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;
      if (obj == null || obj instanceof Deployer == false)
         return false;
      if (obj instanceof DeployerWrapper)
         obj = ((DeployerWrapper) obj).deployer;
      return deployer.equals(obj);
   }
   
   @Override
   public int hashCode()
   {
      return deployer.hashCode();
   }
   
   @Override
   public String toString()
   {
      return deployer.toString();
   }
}
