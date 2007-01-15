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

import org.jboss.deployers.spi.deployer.Deployer;
import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.deployers.spi.deployer.StandardDeployerTypes;
import org.jboss.logging.Logger;

/**
 * AbstractDeployer.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1.1 $
 */
public abstract class AbstractDeployer implements Deployer
{
   /** The log */
   protected Logger log = Logger.getLogger(this.getClass());

   private int relativeOrder = Integer.MAX_VALUE;
   /** The type of the deployer */
   private String type = StandardDeployerTypes.UNSPECIFIED_TYPE;

   public boolean isRelevant(DeploymentUnit unit)
   {
      return true;
   }

   public int getRelativeOrder()
   {
      return relativeOrder;
   }
   public void setRelativeOrder(int order)
   {
      this.relativeOrder = order;
   }

   public String getType()
   {
      return type;
   }
   public void setType(String type)
   {
      this.type = type;
   }
   
}
