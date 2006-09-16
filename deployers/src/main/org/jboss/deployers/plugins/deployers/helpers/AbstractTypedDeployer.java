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

import org.jboss.deployers.plugins.deployer.AbstractSimpleDeployer;

/**
 * AbstractTypedDeployer.
 * 
 * @param <T> the deployment type
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractTypedDeployer<T> extends AbstractSimpleDeployer
{
   /** The deployment type */
   private Class<T> deploymentType;

   /**
    * Create a new AbstractTypedDeployer.
    * 
    * @param deploymentType the deployment type
    * @throws IllegalArgumentException for a null deployment type
    */
   public AbstractTypedDeployer(Class<T> deploymentType)
   {
      if (deploymentType == null)
         throw new IllegalArgumentException("Null deploymentType");
      this.deploymentType = deploymentType;
   }

   /**
    * Get the deployment type
    * 
    * @return the type
    */
   protected Class<T> getDeploymentType()
   {
      return deploymentType;
   }
}
