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
package org.jboss.deployers.plugins.deployers.helpers;

import java.util.Set;

import org.jboss.deployers.plugins.deployer.AbstractSimpleDeployer;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentUnit;

/**
 * AbstractRealDeployer.
 * 
 * @param <T> the deployment type
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractRealDeployer<T> extends AbstractSimpleDeployer
{
   /** The visitor */
   private SimpleDeploymentVisitor<T> visitor;

   /** The deployment type */
   private Class<T> deploymentType;

   /** Whether the warning has been displayed */
   private boolean warned;

   /**
    * Set the default relative order to REAL_DEPLOYER.
    *
    */
   public AbstractRealDeployer()
   {
      setRelativeOrder(REAL_DEPLOYER);
   }

   /**
    * Set the deployment visitor
    * 
    * @param visitor the visitor
    * @throws IllegalArgumentException if the visitor is null
    */
   protected void setDeploymentVisitor(SimpleDeploymentVisitor<T> visitor)
   {
      if (visitor == null)
         throw new IllegalArgumentException("Null visitor");
      this.visitor = visitor;
      deploymentType = visitor.getVisitorType();
      if (deploymentType == null)
         throw new IllegalArgumentException("Null visitor type");
   }

   public void deploy(DeploymentUnit unit) throws DeploymentException
   {
      if (visitor == null)
      {
         if (warned == false)
         {
            log.error("INTERNAL ERROR: Visitor is null for " + getClass().getName());
            warned = true;
         }
         return;
      }

      try
      {
         Set<? extends T> deployments = unit.getAllMetaData(deploymentType);
         for (T deployment : deployments)
            visitor.deploy(unit, deployment);
      }
      catch (Throwable t)
      {
         undeploy(unit);
         throw DeploymentException.rethrowAsDeploymentException("Error deploying: " + unit.getName(), t);
      }
   }

   public void undeploy(DeploymentUnit unit)
   {
      if (visitor == null)
         return;
      Set<? extends T> deployments = unit.getAllMetaData(deploymentType);
      for (T deployment : deployments)
         visitor.undeploy(unit, deployment);
   }

}
