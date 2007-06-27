/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.test.deployers;

import java.util.Set;

import org.jboss.dependency.plugins.AbstractController;
import org.jboss.deployers.client.plugins.deployment.AbstractDeployment;
import org.jboss.deployers.client.spi.DeployerClient;
import org.jboss.deployers.client.spi.Deployment;
import org.jboss.deployers.client.spi.DeploymentFactory;
import org.jboss.deployers.plugins.deployers.DeployerWrapper;
import org.jboss.deployers.plugins.deployers.DeployersImpl;
import org.jboss.deployers.plugins.main.MainDeployerImpl;
import org.jboss.deployers.spi.deployer.Deployer;
import org.jboss.deployers.spi.deployer.Deployers;
import org.jboss.deployers.structure.spi.DeploymentContext;
import org.jboss.deployers.structure.spi.StructuralDeployers;
import org.jboss.deployers.structure.spi.StructureBuilder;
import org.jboss.deployers.structure.spi.helpers.AbstractStructuralDeployers;
import org.jboss.deployers.structure.spi.helpers.AbstractStructureBuilder;
import org.jboss.test.BaseTestCase;

/**
 * AbstractDeployerTest.
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractDeployerTest extends BaseTestCase
{
   public AbstractDeployerTest(String name)
   {
      super(name);
   }
   
   protected DeployerClient createMainDeployer()
   {
      return createMainDeployer(null);
   }
   
   protected DeployerClient createMainDeployer(Deployer... deployers)
   {
      MainDeployerImpl mainDeployer = new MainDeployerImpl();
      StructuralDeployers structure = createStructuralDeployers();
      mainDeployer.setStructuralDeployers(structure);
      Deployers theDeployers = createDeployers();
      mainDeployer.setDeployers(theDeployers);
      if (deployers != null)
      {
         for (Deployer deployer : deployers)
            addDeployer(mainDeployer, deployer);
      }
      return mainDeployer;
   }

   protected StructureBuilder createStructureBuilder()
   {
      return new AbstractStructureBuilder();
   }
   
   protected StructuralDeployers createStructuralDeployers()
   {
      StructureBuilder builder = createStructureBuilder();
      AbstractStructuralDeployers structure = new AbstractStructuralDeployers();
      structure.setStructureBuilder(builder);
      return structure;
   }
   
   protected Deployers createDeployers()
   {
      AbstractController controller = new AbstractController();
      return new DeployersImpl(controller);
   }
   
   protected void addDeployer(DeployerClient main, Deployer deployer)
   {
      MainDeployerImpl mainDeployerImpl = (MainDeployerImpl) main;
      DeployersImpl deployersImpl = (DeployersImpl) mainDeployerImpl.getDeployers();
      deployersImpl.addDeployer(deployer);
   }
   
   protected void removeDeployer(DeployerClient main, Deployer deployer)
   {
      MainDeployerImpl mainDeployerImpl = (MainDeployerImpl) main;
      DeployersImpl deployersImpl = (DeployersImpl) mainDeployerImpl.getDeployers();
      deployersImpl.removeDeployer(deployer);
   }
   
   protected void setDeployers(DeployerClient main, Set<Deployer> deployers)
   {
      MainDeployerImpl mainDeployerImpl = (MainDeployerImpl) main;
      DeployersImpl deployersImpl = (DeployersImpl) mainDeployerImpl.getDeployers();
      deployersImpl.setDeployers(deployers);
   }
   
   protected Set<DeployerWrapper> getDeployers(DeployerClient main)
   {
      MainDeployerImpl mainDeployerImpl = (MainDeployerImpl) main;
      DeployersImpl deployersImpl = (DeployersImpl) mainDeployerImpl.getDeployers();
      return deployersImpl.getDeployerWrappers();
   }
   
   protected DeploymentContext getDeploymentContext(DeployerClient main, String name)
   {
      MainDeployerImpl mainDeployerImpl = (MainDeployerImpl) main;
      return mainDeployerImpl.getDeploymentContext(name);
   }
   
   protected DeploymentContext assertDeploymentContext(DeployerClient main, String name)
   {
      DeploymentContext context = getDeploymentContext(main, name);
      assertNotNull(name + " not found", context);
      return context;
   }
   
   protected Deployment createSimpleDeployment(String name)
   {
      AbstractDeployment unit = new AbstractDeployment(name);
      DeploymentFactory factory = new DeploymentFactory();
      factory.addContext(unit, "");
      return unit;
   }

   @Override
   protected void configureLogging()
   {
      enableTrace("org.jboss.deployers");
   }
}
