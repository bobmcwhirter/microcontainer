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

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.Deployer;
import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.logging.Logger;

/**
 * DeployerWrapper.<p>
 * 
 * To avoid any problems with error handling by the deployers.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class DeployerWrapper implements Deployer
{
   /** The log */
   private Logger log; 
   
   /** The deployer */
   private Deployer deployer;
   
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

   public void deploy(DeploymentUnit unit) throws DeploymentException
   {
      if (unit == null)
         throw new IllegalArgumentException("Null unit");

      try
      {
         log.debug("Deploying: " + unit.getName());
         deployer.deploy(unit);
         log.debug("Deployed:  " + unit.getName());
      }
      catch (Throwable t)
      {
         log.error("Error during deploy: " + unit.getName(), t);
         throw DeploymentException.rethrowAsDeploymentException("Error during deploy: " + unit.getName(), t);
      }
   }

   public void undeploy(DeploymentUnit unit)
   {
      if (unit == null)
         throw new IllegalArgumentException("Null unit");
      try
      {
         log.debug("Undeploying: " + unit.getName());
         deployer.undeploy(unit);
         log.debug("Undeployed:  " + unit.getName());
      }
      catch (Throwable t)
      {
         log.warn("Error during undeploy: " + unit.getName(), t);
      }
   }
   
   public int getRelativeOrder()
   {
      return deployer.getRelativeOrder();
   }

   @Override
   public boolean equals(Object obj)
   {
      return deployer.equals(obj);
   }
   
   @Override
   public int hashCode()
   {
      return deployer.hashCode();
   }
}
