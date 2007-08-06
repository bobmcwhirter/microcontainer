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
package org.jboss.deployers.spi.deployer.helpers;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.structure.spi.DeploymentUnit;


/**
 * An abstract more complicated VFS real deployer where the input
 * is optional instead of mandatory.
 * 
 * @param <T> the deployment type 
 * @author <a href="mailto:carlo.dewolf@jboss.com">Carlo de Wolf</a>
 * @author adrian@jboss.org
 * @version $Revision: 63991 $
 */
public abstract class AbstractOptionalRealDeployer<T> extends AbstractRealDeployer
{
   /** The optional input */
   private Class<T> optionalInput;

   /**
    * Create a new AbstractOptionalRealDeployer.
    * 
    * @param optionalInput the optional input
    * @throws IllegalArgumentException for null input
    */
   public AbstractOptionalRealDeployer(Class<T> optionalInput)
   {
      if (optionalInput == null)
         throw new IllegalArgumentException("Null optional Input");
      this.optionalInput = optionalInput;
      setInputs(optionalInput);
   }

   public void deploy(DeploymentUnit unit) throws DeploymentException
   {
      deploy(unit, unit.getAttachment(optionalInput));
   }

   /**
    * Deploy
    * 
    * @param unit the deployment unit
    * @param deployment the optional attachment
    * @throws DeploymentException for any error
    */
   public abstract void deploy(DeploymentUnit unit, T deployment) throws DeploymentException;


   @Override
   public void undeploy(DeploymentUnit unit)
   {
      undeploy(unit, unit.getAttachment(optionalInput));
   }

   /**
    * Undeploy
    * 
    * @param unit the deployment unit
    * @param deployment the optional attachment
    */
   public void undeploy(DeploymentUnit unit, T deployment)
   {
      // Nothing
   }
}
