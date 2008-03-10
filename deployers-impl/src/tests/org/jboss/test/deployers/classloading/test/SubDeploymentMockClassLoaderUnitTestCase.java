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
package org.jboss.test.deployers.classloading.test;

import java.util.Arrays;
import java.util.List;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.classloading.spi.metadata.ClassLoadingMetaData;
import org.jboss.deployers.client.spi.DeployerClient;
import org.jboss.deployers.client.spi.Deployment;
import org.jboss.deployers.spi.structure.ContextInfo;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.test.deployers.classloading.support.a.A;
import org.jboss.test.deployers.classloading.support.b.B;

/**
 * SubDeploymentMockClassLoaderUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class SubDeploymentMockClassLoaderUnitTestCase extends ClassLoaderDependenciesTest
{
   public static Test suite()
   {
      return new TestSuite(SubDeploymentMockClassLoaderUnitTestCase.class);
   }
   
   public SubDeploymentMockClassLoaderUnitTestCase(String name)
   {
      super(name);
   }

   public void testNoSubDeploymentClassLoader() throws Exception
   {
      DeployerClient deployer = getMainDeployer();

      Deployment deployment = createSimpleDeployment("top");
      addClassLoadingMetaData(deployment, "top", null, A.class);
      
      addChild(deployment, "sub");
      
      DeploymentUnit unit = assertDeploy(deployer, deployment);
      
      ClassLoader cl = unit.getClassLoader();
      assertLoadClass(cl, A.class);
      
      DeploymentUnit subDeployment = assertChild(unit, "top/sub");
      ClassLoader clSub = subDeployment.getClassLoader();
      assertEquals(cl, clSub);
      assertLoadClass(clSub, A.class, cl);
      
      assertUndeploy(deployer, deployment);
   }

   public void testSubDeploymentClassLoader() throws Exception
   {
      DeployerClient deployer = getMainDeployer();

      Deployment deployment = createSimpleDeployment("top");
      addClassLoadingMetaData(deployment, "top", null, A.class);
      
      ContextInfo sub = addChild(deployment, "sub");
      addClassLoadingMetaData(sub, "top/sub", null, A.class, B.class);
      
      DeploymentUnit unit = assertDeploy(deployer, deployment);
      
      assertEquals(Arrays.asList("top", "top/sub"), deployer2.deployed);
      assertEquals(NONE, deployer2.undeployed);
      
      ClassLoader cl = unit.getClassLoader();
      assertLoadClass(cl, A.class);
      assertLoadClassFail(cl, B.class);
      
      DeploymentUnit subDeployment = assertChild(unit, "top/sub");
      ClassLoader clSub = subDeployment.getClassLoader();
      assertLoadClass(clSub, A.class, cl);
      assertLoadClass(clSub, B.class);
      
      assertUndeploy(deployer, deployment);

      assertEquals(Arrays.asList("top", "top/sub"), deployer2.deployed);
      assertEquals(Arrays.asList("top/sub", "top"), deployer2.undeployed);
   }

   public void testMultipleSubDeploymentClassLoader() throws Exception
   {
      DeployerClient deployer = getMainDeployer();

      Deployment deployment = createSimpleDeployment("top");
      addClassLoadingMetaData(deployment, "top", null, A.class);
      
      ContextInfo sub1 = addChild(deployment, "sub1");
      addClassLoadingMetaData(sub1, "top/sub1", null, B.class);
      
      ContextInfo sub2 = addChild(deployment, "sub2");
      addClassLoadingMetaData(sub2, "top/sub2", null, B.class);
      
      DeploymentUnit unit = assertDeploy(deployer, deployment);
      
      ClassLoader cl = unit.getClassLoader();
      assertLoadClass(cl, A.class);
      assertLoadClassFail(cl, B.class);
      
      DeploymentUnit subDeployment1 = assertChild(unit, "top/sub1");
      ClassLoader clSub1 = subDeployment1.getClassLoader();
      assertLoadClass(clSub1, A.class, cl);
      Class<?> bFrom1 = assertLoadClass(clSub1, B.class);
      
      DeploymentUnit subDeployment2 = assertChild(unit, "top/sub2");
      ClassLoader clSub2 = subDeployment2.getClassLoader();
      assertLoadClass(clSub2, A.class, cl);
      Class<?> bFrom2 = assertLoadClass(clSub2, B.class);
      
      assertNotSame(clSub1, clSub2);
      assertNotSame(bFrom1, bFrom2);
      
      assertUndeploy(deployer, deployment);
   }

   public void testSubDeploymentClassLoaderParentLast() throws Exception
   {
      DeployerClient deployer = getMainDeployer();

      Deployment deployment = createSimpleDeployment("top");
      addClassLoadingMetaData(deployment, "top", null, A.class);
      
      ContextInfo sub = addChild(deployment, "sub");
      ClassLoadingMetaData clmd = addClassLoadingMetaData(sub, "top/sub", null, A.class, B.class);
      clmd.setJ2seClassLoadingCompliance(false);
      
      DeploymentUnit unit = assertDeploy(deployer, deployment);
      
      ClassLoader cl = unit.getClassLoader();
      assertLoadClass(cl, A.class);
      assertLoadClassFail(cl, B.class);
      
      DeploymentUnit subDeployment = assertChild(unit, "top/sub");
      ClassLoader clSub = subDeployment.getClassLoader();
      assertLoadClass(clSub, A.class);
      assertLoadClass(clSub, B.class);
      
      assertUndeploy(deployer, deployment);

      assertEquals(Arrays.asList("top", "top/sub"), deployer2.deployed);
      assertEquals(Arrays.asList("top/sub", "top"), deployer2.undeployed);
   }
   
   protected DeploymentUnit assertChild(DeploymentUnit parent, String name)
   {
      List<DeploymentUnit> children = parent.getChildren();
      for (DeploymentUnit child : children)
      {
         if (name.equals(child.getSimpleName()))
            return child;
      }
      throw new AssertionFailedError("Child " + name + " not found in " + children);
   }
}
