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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.classloader.plugins.system.DefaultClassLoaderSystem;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.deployers.client.spi.DeployerClient;
import org.jboss.deployers.client.spi.Deployment;
import org.jboss.deployers.plugins.classloading.AbstractClassLoaderDesribeDeployer;
import org.jboss.deployers.plugins.classloading.ClassLoading;
import org.jboss.deployers.spi.attachments.MutableAttachments;
import org.jboss.deployers.spi.attachments.PredeterminedManagedObjectAttachments;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.structure.spi.classloading.Capability;
import org.jboss.deployers.structure.spi.classloading.ClassLoaderMetaData;
import org.jboss.deployers.structure.spi.classloading.Requirement;
import org.jboss.deployers.structure.spi.classloading.Version;
import org.jboss.deployers.structure.spi.classloading.VersionRange;
import org.jboss.deployers.structure.spi.classloading.helpers.ModuleCapabilityImpl;
import org.jboss.deployers.structure.spi.classloading.helpers.PackageCapabilityImpl;
import org.jboss.deployers.structure.spi.classloading.helpers.RequireModuleImpl;
import org.jboss.test.deployers.AbstractDeployerTest;
import org.jboss.test.deployers.classloading.support.MockTopLevelClassLoaderSystemDeployer;
import org.jboss.test.deployers.classloading.support.a.A;
import org.jboss.test.deployers.classloading.support.b.B;

/**
 * HeuristicAllOrNothingUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MockClassLoaderDependenciesUnitTestCase extends AbstractDeployerTest
{
   public static final String NameA = "A";
   public static final String NameB = "B";
   
   public static final List<String> NONE = Collections.emptyList();
   public static final List<String> A = makeList(NameA);
   public static final List<String> B = makeList(NameB);
   public static final List<String> AB = makeList(NameA, NameB);
   public static final List<String> BA = makeList(NameB, NameA);
   public static final List<String> BAA = makeList(NameB, NameA, NameA);
   public static final List<String> BABA = makeList(NameB, NameA, NameB, NameA);
   
   @SuppressWarnings("unchecked")
   private static <T> List<T> makeList(T... objects)
   {
      List<T> result = new ArrayList<T>();
      for (T object : objects)
         result.add(object);
      return result;
   }
   
   private AbstractClassLoaderDesribeDeployer deployer1;
   private MockTopLevelClassLoaderSystemDeployer deployer2;
   
   public static Test suite()
   {
      return new TestSuite(MockClassLoaderDependenciesUnitTestCase.class);
   }
   
   public MockClassLoaderDependenciesUnitTestCase(String name)
   {
      super(name);
   }

   public void testSimpleClassLoader() throws Exception
   {
      DeployerClient deployer = getMainDeployer();

      Deployment deployment = createSimpleDeployment(NameA);
      addMetaData(deployment, null, A.class);
      
      DeploymentUnit unit = assertDeploy(deployer, deployment);

      assertEquals(A, deployer2.deployed);
      assertEquals(NONE, deployer2.undeployed);
      
      ClassLoader cl = unit.getClassLoader();
      assertLoadClass(cl, A.class);
      
      assertUndeploy(deployer, deployment);

      assertLoadClass(cl, A.class);

      assertEquals(A, deployer2.deployed);
      assertEquals(A, deployer2.undeployed);
   }

   public void testADependsUponModuleBCorrectWay() throws Exception
   {
      DeployerClient deployer = getMainDeployer();

      Deployment deploymentB = createSimpleDeployment(NameB);
      addMetaData(deploymentB, null, B.class);
      DeploymentUnit unitB = assertDeploy(deployer, deploymentB);
      
      ClassLoader clB = unitB.getClassLoader();
      assertLoadClass(clB, B.class);

      assertEquals(B, deployer2.deployed);
      assertEquals(NONE, deployer2.undeployed);

      Deployment deploymentA = createSimpleDeployment(NameA);
      ClassLoaderMetaData classLoaderMetaData = addMetaData(deploymentA, null, A.class);
      addRequireModule(classLoaderMetaData, "B", null);
      DeploymentUnit unitA = assertDeploy(deployer, deploymentA);
      
      ClassLoader clA = unitA.getClassLoader();
      assertLoadClass(clA, B.class, clB);

      assertEquals(BA, deployer2.deployed);
      assertEquals(NONE, deployer2.undeployed);
      
      assertUndeploy(deployer, deploymentA);
      assertLoadClassIllegal(clA, B.class);

      assertEquals(BA, deployer2.deployed);
      assertEquals(A, deployer2.undeployed);
      
      assertUndeploy(deployer, deploymentB);
      assertLoadClass(clB, B.class);

      assertEquals(BA, deployer2.deployed);
      assertEquals(AB, deployer2.undeployed);
   }

   public void testADependsUponModuleBWrongWay() throws Exception
   {
      DeployerClient deployer = getMainDeployer();

      Deployment deploymentA = createSimpleDeployment(NameA);
      ClassLoaderMetaData classLoaderMetaData = addMetaData(deploymentA, null, A.class);
      addRequireModule(classLoaderMetaData, "B", null);
      DeploymentUnit unitA = deploy(deployer, deploymentA);
      
      assertNoClassLoader(unitA);

      assertEquals(NONE, deployer2.deployed);
      assertEquals(NONE, deployer2.undeployed);

      Deployment deploymentB = createSimpleDeployment(NameB);
      addMetaData(deploymentB, null, B.class);
      DeploymentUnit unitB = assertDeploy(deployer, deploymentB);
      
      ClassLoader clB = unitB.getClassLoader();
      assertLoadClass(clB, B.class);

      assertEquals(BA, deployer2.deployed);
      assertEquals(NONE, deployer2.undeployed);

      ClassLoader clA = unitA.getClassLoader();
      assertLoadClass(clA, B.class, clB);
      
      assertUndeploy(deployer, deploymentA);
      assertLoadClassIllegal(clA, B.class);

      assertEquals(BA, deployer2.deployed);
      assertEquals(A, deployer2.undeployed);
      
      assertUndeploy(deployer, deploymentB);
      assertLoadClass(clB, B.class);

      assertEquals(BA, deployer2.deployed);
      assertEquals(AB, deployer2.undeployed);
   }

   public void testADependsUponModuleBRedeployA() throws Exception
   {
      DeployerClient deployer = getMainDeployer();

      Deployment deploymentA = createSimpleDeployment(NameA);
      ClassLoaderMetaData classLoaderMetaData = addMetaData(deploymentA, null, A.class);
      addRequireModule(classLoaderMetaData, "B", null);
      DeploymentUnit unitA = deploy(deployer, deploymentA);
      
      assertNoClassLoader(unitA);

      assertEquals(NONE, deployer2.deployed);
      assertEquals(NONE, deployer2.undeployed);

      Deployment deploymentB = createSimpleDeployment(NameB);
      addMetaData(deploymentB, null, B.class);
      DeploymentUnit unitB = assertDeploy(deployer, deploymentB);
      
      ClassLoader clB = unitB.getClassLoader();
      assertLoadClass(clB, B.class);

      assertEquals(BA, deployer2.deployed);
      assertEquals(NONE, deployer2.undeployed);

      ClassLoader clA = unitA.getClassLoader();
      assertLoadClass(clA, B.class, clB);
      
      assertUndeploy(deployer, deploymentA);
      assertLoadClassIllegal(clA, B.class);

      assertEquals(BA, deployer2.deployed);
      assertEquals(A, deployer2.undeployed);
      
      unitA = assertDeploy(deployer, deploymentA);
      clA = unitA.getClassLoader();
      assertLoadClass(clA, B.class, clB);

      assertEquals(BAA, deployer2.deployed);
      assertEquals(A, deployer2.undeployed);
   }

   public void testADependsUponModuleBRedeployB() throws Exception
   {
      DeployerClient deployer = getMainDeployer();

      Deployment deploymentA = createSimpleDeployment(NameA);
      ClassLoaderMetaData classLoaderMetaData = addMetaData(deploymentA, null, A.class);
      addRequireModule(classLoaderMetaData, "B", null);
      DeploymentUnit unitA = deploy(deployer, deploymentA);
      
      assertNoClassLoader(unitA);

      assertEquals(NONE, deployer2.deployed);
      assertEquals(NONE, deployer2.undeployed);

      Deployment deploymentB = createSimpleDeployment(NameB);
      addMetaData(deploymentB, null, B.class);
      DeploymentUnit unitB = assertDeploy(deployer, deploymentB);
      
      ClassLoader clB = unitB.getClassLoader();
      assertLoadClass(clB, B.class);

      assertEquals(BA, deployer2.deployed);
      assertEquals(NONE, deployer2.undeployed);

      ClassLoader clA = unitA.getClassLoader();
      assertLoadClass(clA, B.class, clB);
      
      enableTrace("org.jboss.deployers");
      enableTrace("org.jboss.dependency");
      assertUndeploy(deployer, deploymentB);
      assertLoadClassIllegal(clA, B.class);

      assertEquals(BA, deployer2.deployed);
      assertEquals(AB, deployer2.undeployed);
      
      unitB = assertDeploy(deployer, deploymentB);
      clA = unitA.getClassLoader();
      clB = unitB.getClassLoader();
      assertLoadClass(clA, B.class, clB);

      assertEquals(BABA, deployer2.deployed);
      assertEquals(AB, deployer2.undeployed);
   }

   protected Class assertLoadClass(ClassLoader start, Class reference) throws Exception
   {
      return assertLoadClass(start, reference, start);
   }

   protected Class assertLoadClass(ClassLoader start, Class reference, ClassLoader expected) throws Exception
   {
      Class clazz = start.loadClass(reference.getName());
      if (expected != null)
         assertEquals(expected, clazz.getClassLoader());
      return clazz;
   }

   protected void assertLoadClassFail(ClassLoader start, Class reference) throws Exception
   {
      try
      {
         start.loadClass(reference.getName());
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(ClassNotFoundException.class, e);
      }
   }

   protected void assertLoadClassIllegal(ClassLoader start, Class reference) throws Exception
   {
      try
      {
         start.loadClass(reference.getName());
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalStateException.class, e);
      }
   }

   protected void assertNoClassLoader(DeploymentUnit unit) throws Exception
   {
      try
      {
         unit.getClassLoader();
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalStateException.class, e);
      }
   }
   
   protected static ClassLoaderMetaData addMetaData(Deployment deployment, Version version, Class... packages)
   {
      ClassLoaderMetaData classLoaderMetaData = createMetaData(deployment, version, packages);
      addMetaData(deployment, classLoaderMetaData);
      return classLoaderMetaData;
   }
   
   protected static ClassLoaderMetaData createMetaData(Deployment deployment, Version version, Class... packages)
   {
      ClassLoaderMetaData classLoaderMetaData = new ClassLoaderMetaData();
      classLoaderMetaData.setName(deployment.getName());
      
      List<Capability> capabilities = new ArrayList<Capability>();
      Capability capability = new ModuleCapabilityImpl(deployment.getName(), version);
      capabilities.add(capability);

      if (packages != null)
      {
         for (Class pkg : packages)
         {
            capability = new PackageCapabilityImpl(pkg.getName());
            capabilities.add(capability);
         }
      }

      classLoaderMetaData.setCapabilities(capabilities);
      return classLoaderMetaData;
   }
   
   protected static void addRequireModule(ClassLoaderMetaData classLoaderMetaData, String moduleName, VersionRange versionRange)
   {
      List<Requirement> requirements = classLoaderMetaData.getRequirements();
      if (requirements == null)
      {
         requirements = new ArrayList<Requirement>();
         classLoaderMetaData.setRequirements(requirements);
      }
      
      Requirement requirement = new RequireModuleImpl(moduleName, versionRange);
      requirements.add(requirement);
   }

   protected static void addMetaData(PredeterminedManagedObjectAttachments attachments, ClassLoaderMetaData md)
   {
      MutableAttachments mutable = (MutableAttachments) attachments.getPredeterminedManagedObjects();
      mutable.addAttachment(ClassLoaderMetaData.class, md);
   }
   
   protected DeployerClient getMainDeployer()
   {
      ClassLoading classLoading = new ClassLoading();
      ClassLoaderSystem system = new DefaultClassLoaderSystem();
      system.getDefaultDomain().setParentPolicy(ParentPolicy.BEFORE_BUT_JAVA_ONLY);
      
      deployer1 = new AbstractClassLoaderDesribeDeployer();
      deployer1.setClassLoading(classLoading);
      
      deployer2 = new MockTopLevelClassLoaderSystemDeployer();
      deployer2.setClassLoading(classLoading);
      deployer2.setSystem(system);

      return createMainDeployer(deployer1, deployer2);
   }
}
