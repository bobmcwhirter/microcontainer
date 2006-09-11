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
package org.jboss.test.deployers.deployer.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jboss.deployers.plugins.deployer.AbstractSimpleDeployer;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentUnit;

/**
 * TestDeployerOrdering.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class TestSimpleDeployer extends AbstractSimpleDeployer
{
   private static int order = 0;

   private int relativeOrder;
   
   private Map<DeploymentUnit, Integer> deployed = new HashMap<DeploymentUnit, Integer>();
   private Map<DeploymentUnit, Integer> undeployed = new HashMap<DeploymentUnit, Integer>();

   public static void reset()
   {
      order = 0;
   }
   
   public TestSimpleDeployer()
   {
      this.relativeOrder = Integer.MAX_VALUE;
   }
   
   public TestSimpleDeployer(int relativeOrder)
   {
      this.relativeOrder = relativeOrder;
   }

   public void clear()
   {
      deployed.clear();
      undeployed.clear();
   }
   
   public int getDeployOrder()
   {
      if (deployed.isEmpty())
         return -1;
      return Collections.max(deployed.values());
   }

   public int getUndeployOrder()
   {
      if (undeployed.isEmpty())
         return -1;
      return Collections.max(undeployed.values());
   }
   
   public Set<DeploymentUnit> getDeployedUnits()
   {
      return deployed.keySet();
   }

   public Set<DeploymentUnit> getUndeployedUnits()
   {
      return undeployed.keySet();
   }
   
   public Map<DeploymentUnit, Integer> getDeployed()
   {
      return deployed;
   }

   public Map<DeploymentUnit, Integer> getUndeployed()
   {
      return undeployed;
   }
   
   public int getRelativeOrder()
   {
      return relativeOrder;
   }

   public void deploy(DeploymentUnit unit) throws DeploymentException
   {
      deployed.put(unit, ++order);
   }

   public void undeploy(DeploymentUnit unit)
   {
      undeployed.put(unit, ++order);
   }
}
