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

import java.util.HashSet;
import java.util.Set;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.helpers.AbstractRealDeployerWithInput;
import org.jboss.deployers.spi.deployer.helpers.DeploymentVisitor;
import org.jboss.deployers.structure.spi.DeploymentUnit;

/**
 * TestSimpleDeployer.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class TestComponentRealDeployer extends AbstractRealDeployerWithInput<TestComponentMetaData>
{
   public Set<TestComponentMetaData> deployed = new HashSet<TestComponentMetaData>();
   public Set<TestComponentMetaData> undeployed = new HashSet<TestComponentMetaData>();
   
   public TestComponentRealDeployer(int order)
   {
      setDeploymentVisitor(new TestComponentMetaDataVisitor());
      setWantComponents(true);
      setRelativeOrder(order);
   }
   
   public class TestComponentMetaDataVisitor implements DeploymentVisitor<TestComponentMetaData>
   {
      public Class<TestComponentMetaData> getVisitorType()
      {
         return TestComponentMetaData.class;
      }

      public void deploy(DeploymentUnit unit, TestComponentMetaData deployment) throws DeploymentException
      {
         deployed.add(deployment);
         if (deployment.fail)
            throw new DeploymentException("Asked to fail");
      }

      public void undeploy(DeploymentUnit unit, TestComponentMetaData deployment)
      {
         undeployed.add(deployment);
      }
   }
}
