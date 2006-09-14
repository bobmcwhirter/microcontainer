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
package org.jboss.test.deployers.deployer.test;

import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.plugins.deployers.helpers.ClassPathVisitor;
import org.jboss.deployers.plugins.deployment.MainDeployerImpl;
import org.jboss.deployers.plugins.structure.vfs.jar.JARStructure;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.deployers.spi.structure.DeploymentState;
import org.jboss.test.deployers.BaseDeployersTest;
import org.jboss.test.deployers.deployer.support.TestClassLoaderDeployer;
import org.jboss.virtual.VirtualFile;

/**
 * DeployerClassLoaderUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class DeployerClassLoaderUnitTestCase extends BaseDeployersTest
{
   public static Test suite()
   {
      return new TestSuite(DeployerClassLoaderUnitTestCase.class);
   }
   
   public DeployerClassLoaderUnitTestCase(String name)
   {
      super(name);
   }

   public void testClassLoader() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      TestClassLoaderDeployer deployer = new TestClassLoaderDeployer();
      main.addDeployer(deployer);
      
      DeploymentContext context = createSimpleDeployment("Single");
      main.addDeploymentContext(context);
      main.process();
      assertEquals(DeploymentState.DEPLOYED, context.getState());
      assertEquals(deployer.cl, context.getClassLoader());
      
      main.removeDeploymentContext(context.getName());
      main.process();
      assertEquals(DeploymentState.UNDEPLOYED, context.getState());
      assertNull(context.getClassLoader());
   }

   public void testSubdeploymentClassLoader() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      TestClassLoaderDeployer deployer = new TestClassLoaderDeployer();
      main.addDeployer(deployer);
      
      DeploymentContext top = createSimpleDeployment("Top");
      DeploymentContext sub = createSimpleDeployment("Sub");
      top.addChild(sub);
      main.addDeploymentContext(top);
      main.process();
      assertEquals(DeploymentState.DEPLOYED, top.getState());
      assertEquals(deployer.cl, top.getClassLoader());
      assertEquals(DeploymentState.DEPLOYED, sub.getState());
      assertEquals(deployer.cl, sub.getClassLoader());
      
      main.removeDeploymentContext(top.getName());
      main.process();
      assertEquals(DeploymentState.UNDEPLOYED, top.getState());
      assertNull(top.getClassLoader());
      assertEquals(DeploymentState.UNDEPLOYED, sub.getState());
      assertNull(sub.getClassLoader());
   }

   public void testClassPathVisitor() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      main.addStructureDeployer(new JARStructure());
      DeploymentContext context = createDeploymentContext("/structure/jar", "indirectory");
      main.addDeploymentContext(context);
      ClassPathVisitor visitor = new ClassPathVisitor();
      context.visit(visitor);
      HashSet<VirtualFile> expected = new HashSet<VirtualFile>();
      expected.add(context.getRoot());
      expected.add(context.getRoot().findChild("archive.zip"));
      expected.add(context.getRoot().findChild("archive.jar"));
      assertEquals(expected, visitor.getClassPath());
   }
}
