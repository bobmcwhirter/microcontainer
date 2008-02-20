/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.deployers;

import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

import org.jboss.deployers.client.spi.DeployerClient;
import org.jboss.deployers.structure.spi.main.MainDeployerStructure;
import org.jboss.deployers.vfs.spi.client.VFSDeployment;
import org.jboss.deployers.vfs.spi.client.VFSDeploymentFactory;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentUnit;
import org.jboss.test.AbstractTestDelegate;
import org.jboss.test.kernel.junit.MicrocontainerTest;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;

/**
 * BootstrapDeployersTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class BootstrapDeployersTest extends MicrocontainerTest
{
   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      return new BootstrapDeployersTestDelegate(clazz);
   }
   
   public BootstrapDeployersTest(String name)
   {
      super(name);
   }
   
   protected DeployerClient getDeployerClient()
   {
      return getDelegate().getMainDeployer();
   }
   
   protected MainDeployerStructure getMainDeployerStructure()
   {
      return getDelegate().getMainDeployer();
   }
   
   protected String getRoot(Class<?> clazz)
   {
      ProtectionDomain pd = clazz.getProtectionDomain();
      CodeSource cs = pd.getCodeSource();
      URL location = cs.getLocation();
      return location.toString();
   }

   protected BootstrapDeployersTestDelegate getDelegate()
   {
      return (BootstrapDeployersTestDelegate) super.getDelegate();
   }
   
   protected VFSDeployment createVFSDeployment(String root) throws Exception
   {
      URL resourceRoot = getClass().getResource(root);
      if (resourceRoot == null)
         throw new IllegalStateException("Resource not found: " + root);
      VirtualFile rootFile = VFS.getRoot(resourceRoot);
      return createVFSDeployment(rootFile);
   }
   
   protected VFSDeployment createVFSDeployment(VirtualFile root) throws Exception
   {
      VFSDeploymentFactory factory = VFSDeploymentFactory.getInstance();
      return factory.createVFSDeployment(root);
   }
   
   protected VFSDeploymentUnit assertDeploy(String root) throws Exception
   {
      VFSDeployment deployment = createVFSDeployment(root);
      getDeployerClient().deploy(deployment);
      return (VFSDeploymentUnit) getMainDeployerStructure().getDeploymentUnit(deployment.getName(), true);
   }
}
