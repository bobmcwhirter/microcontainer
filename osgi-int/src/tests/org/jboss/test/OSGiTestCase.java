/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test;

import java.net.URL;

import org.jboss.deployers.client.spi.DeployerClient;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentStage;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.structure.spi.main.MainDeployerStructure;
import org.jboss.deployers.vfs.spi.client.VFSDeployment;
import org.jboss.deployers.vfs.spi.client.VFSDeploymentFactory;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentUnit;
import org.jboss.test.kernel.junit.MicrocontainerTest;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;

/**
 * OSGiTestCase - Parent Test Case for OSGi tests.  
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public abstract class OSGiTestCase extends MicrocontainerTest
{

   /**
    * Create a new OSGiTestCase.
    * 
    * @param name
    */
   public OSGiTestCase(String name)
   {
      super(name);
   }

   /**
    * Get the AbstractTestDelegate
    * 
    * @param clazz
    * @return The AbstractTestDelegate
    * @throws Exception
    */
   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      return new OSGiTestDelegate(clazz);
   }

   /**
    * Get OSGiTestDelegate
    */
   protected OSGiTestDelegate getDelegate()
   {
      return (OSGiTestDelegate) super.getDelegate();
   }

   /**
    * Get DeployerClient from Delegate
    * 
    * @return The DeployerClient
    */
   protected DeployerClient getDeployerClient()
   {
      return getDelegate().getMainDeployer();
   }

   /**
    * Assert the given DeploymentUnit is in the specified DeployemntStage
    * 
    * @param unit A DeploymentUnit
    * @param stage A DeploymentStage
    * @throws DeploymentException If the DeployerClient is unable to determine the current stage
    */
   protected void assertStage(DeploymentUnit unit, DeploymentStage stage) throws DeploymentException
   {
      assertEquals(stage, getDeployerClient().getDeploymentStage(unit.getName()));
   }

   /**
    * Create a VFSDeployment
    * 
    * @param root the root location of the deployment
    * @param child the child to deploy
    * @return VFSDeployment
    * @throws Exception
    */
   protected VFSDeployment createVFSDeployment(String root, String child) throws Exception
   {
      URL resourceRoot = getClass().getResource(root);
      if (resourceRoot == null)
         fail("Resource not found: " + root);
      VirtualFile deployment = VFS.getVirtualFile(resourceRoot, child);
      if (deployment == null)
         fail("Child not found " + child + " from " + resourceRoot);
      return createVFSDeployment(deployment);
   }

   /**
    * Create a VFSDeployment
    * 
    * @param root the VirtualFile to deploy
    * @return VFSDeployment
    * @throws Exception
    */
   protected VFSDeployment createVFSDeployment(VirtualFile root) throws Exception
   {
      VFSDeploymentFactory factory = VFSDeploymentFactory.getInstance();
      return factory.createVFSDeployment(root);
   }

   /**
    * Create a VFSDeployment and add the Deployment to the DeployerClient, and return the DeploymentUnit 
    * 
    * @param root the location of the VirtualFile to deploy
    * @param child the child to deploy
    * @return VFSDeploymentUnit for the deployment
    * @throws Exception
    */
   protected VFSDeploymentUnit addDeployment(String root, String child) throws Exception
   {
      VFSDeployment deployment = createVFSDeployment(root, child);
      getDeployerClient().addDeployment(deployment);
      getDeployerClient().process();
      return (VFSDeploymentUnit) getMainDeployerStructure().getDeploymentUnit(deployment.getName(), true);
   }

   /**
    * Get MainDeployerStructure from Delegate
    * 
    * @return MainDeployerStructure
    */
   protected MainDeployerStructure getMainDeployerStructure()
   {
      return getDelegate().getMainDeployer();
   }
}
