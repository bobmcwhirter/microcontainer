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

import org.jboss.classloader.plugins.system.DefaultClassLoaderSystem;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.deployers.client.spi.DeployerClient;
import org.jboss.deployers.client.spi.Deployment;
import org.jboss.deployers.plugins.classloading.AbstractClassLoaderDescribeDeployer;
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
import org.jboss.deployers.structure.spi.classloading.helpers.RequirePackageImpl;
import org.jboss.test.deployers.AbstractDeployerTest;
import org.jboss.test.deployers.classloading.support.MockTopLevelClassLoaderSystemDeployer;

/**
 * ClassLoadersDependencies test.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class ClassLoaderDependenciesTest extends AbstractDeployerTest
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
   protected static <T> List<T> makeList(T... objects)
   {
      List<T> result = new ArrayList<T>();
      for (T object : objects)
         result.add(object);
      return result;
   }

   protected AbstractClassLoaderDescribeDeployer deployer1;
   protected MockTopLevelClassLoaderSystemDeployer deployer2;

   protected ClassLoaderDependenciesTest(String name)
   {
      super(name);
   }

   protected Class<?> assertLoadClass(ClassLoader start, Class<?> reference) throws Exception
   {
      return assertLoadClass(start, reference, start);
   }

   protected Class<?> assertLoadClass(ClassLoader start, Class<?> reference, ClassLoader expected) throws Exception
   {
      Class<?> clazz = start.loadClass(reference.getName());
      if (expected != null)
         assertEquals(expected, clazz.getClassLoader());
      return clazz;
   }

   protected void assertLoadClassFail(ClassLoader start, Class<?> reference) throws Exception
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

   protected void assertLoadClassIllegal(ClassLoader start, Class<?> reference) throws Exception
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

   protected static ClassLoaderMetaData addClassLoaderMetaData(Deployment deployment, Version version, Class<?>... packages)
   {
      return addClassLoaderMetaData(deployment, version, false, packages);
   }

   protected static ClassLoaderMetaData addClassLoaderMetaData(Deployment deployment, Version version, boolean useVersionOnPackages, Class<?>... packages)
   {
      ClassLoaderMetaData classLoaderMetaData = createMetaData(deployment, version, useVersionOnPackages, packages);
      addMetaData(deployment, classLoaderMetaData);
      return classLoaderMetaData;
   }

   protected static ClassLoaderMetaData createMetaData(Deployment deployment, Version version, Class<?>... packages)
   {
      return createMetaData(deployment, version, false, packages);
   }

   protected static ClassLoaderMetaData createMetaData(Deployment deployment, Version version, boolean useVersionOnPackages, Class<?>... packages)
   {
      ClassLoaderMetaData classLoaderMetaData = new ClassLoaderMetaData();
      classLoaderMetaData.setName(deployment.getName());

      List<Capability> capabilities = new ArrayList<Capability>();
      Capability capability = new ModuleCapabilityImpl(deployment.getName(), version);
      capabilities.add(capability);

      if (packages != null)
      {
         for (Class<?> pkg : packages)
         {
            if (useVersionOnPackages)
               capability = new PackageCapabilityImpl(pkg.getName(), version);
            else
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

   protected static void addRequirePackage(ClassLoaderMetaData classLoaderMetaData, Class<?> pck, VersionRange versionRange)
   {
      List<Requirement> requirements = classLoaderMetaData.getRequirements();
      if (requirements == null)
      {
         requirements = new ArrayList<Requirement>();
         classLoaderMetaData.setRequirements(requirements);
      }

      Requirement requirement = new RequirePackageImpl(pck.getName(), versionRange);
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

      deployer1 = new AbstractClassLoaderDescribeDeployer();
      deployer1.setClassLoading(classLoading);

      deployer2 = new MockTopLevelClassLoaderSystemDeployer();
      deployer2.setClassLoading(classLoading);
      deployer2.setSystem(system);

      return createMainDeployer(deployer1, deployer2);
   }
}
