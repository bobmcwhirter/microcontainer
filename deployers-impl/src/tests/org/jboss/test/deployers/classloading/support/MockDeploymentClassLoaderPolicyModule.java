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
package org.jboss.test.deployers.classloading.support;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jboss.classloader.test.support.MockClassLoaderHelper;
import org.jboss.classloader.test.support.MockClassLoaderPolicy;
import org.jboss.classloading.spi.dependency.policy.mock.MockClassLoadingMetaData;
import org.jboss.classloading.spi.metadata.Capability;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaData;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaDataFactory;
import org.jboss.deployers.plugins.classloading.AbstractDeploymentClassLoaderPolicyModule;
import org.jboss.deployers.structure.spi.DeploymentUnit;

/**
 * MockDeploymentClassLoaderPolicyModule.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MockDeploymentClassLoaderPolicyModule extends AbstractDeploymentClassLoaderPolicyModule
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;
   
   /**
    * Create a new MockDeploymentClassLoaderPolicyModule.
    * 
    * @param unit the deployment unit
    * @throws IllegalArgumentException for a null deployment unit
    */
   public MockDeploymentClassLoaderPolicyModule(DeploymentUnit unit)
   {
      super(unit);

      ClassLoadingMetaData metaData = unit.getAttachment(ClassLoadingMetaData.class);
      if (metaData == null || (metaData instanceof MockClassLoadingMetaData == false))
         throw new IllegalArgumentException("MetaData is not mocked: " + metaData);
   }

   @Override
   protected List<Capability> determineCapabilities()
   {
      List<Capability> capabilities = super.determineCapabilities();
      if (capabilities != null)
         return capabilities;
      
      // We need to work it out
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();
      capabilities = new CopyOnWriteArrayList<Capability>();

      // We have a module capability
      Object version = getVersion();
      Capability capability = factory.createModule(getName(), version);
      capabilities.add(capability);

      MockClassLoadingMetaData metadata = getClassLoadingMetaData();
      String[] exported = metadata.getExportedPackages();
      // Do we determine package capabilities?
      if (exported != null)
      {
         for (String packageName : exported)
         {
            capability = factory.createPackage(packageName, version);
            capabilities.add(capability);
         }
      }
      return capabilities;
   }

   @Override
   protected MockClassLoadingMetaData getClassLoadingMetaData()
   {
      return (MockClassLoadingMetaData) super.getClassLoadingMetaData();
   }

   @Override
   public MockClassLoaderPolicy getPolicy()
   {
      return (MockClassLoaderPolicy) super.getPolicy();
   }
   
   @Override
   protected MockClassLoaderPolicy determinePolicy()
   {
      MockClassLoadingMetaData metaData = getClassLoadingMetaData();
      MockClassLoaderPolicy policy = MockClassLoaderHelper.createMockClassLoaderPolicy(getContextName());
      policy.setPrefix(metaData.getPrefix());
      policy.setPackageNames(getPackageNames());
      policy.setPaths(metaData.getPaths());
      policy.setIncluded(metaData.getIncludedClasses());
      policy.setExcluded(metaData.getExcludedClasses());
      policy.setImportAll(isImportAll());
      policy.setDelegates(getDelegates());
      return policy;
   }
}
