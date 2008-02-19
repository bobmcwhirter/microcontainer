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
package org.jboss.deployers.vfs.plugins.classloader;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.classloading.plugins.vfs.PackageVisitor;
import org.jboss.classloading.spi.metadata.Capability;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaDataFactory;
import org.jboss.classloading.spi.metadata.ExportAll;
import org.jboss.classloading.spi.vfs.policy.VFSClassLoaderPolicy;
import org.jboss.deployers.plugins.classloading.AbstractDeploymentClassLoaderPolicyModule;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentUnit;
import org.jboss.deployers.vfs.spi.structure.helpers.ClassPathVisitor;
import org.jboss.virtual.VirtualFile;

/**
 * VFSDeploymentClassLoaderPolicyModule.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class VFSDeploymentClassLoaderPolicyModule extends AbstractDeploymentClassLoaderPolicyModule
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;

   /** The cached roots */
   private VirtualFile[] vfsRoots;
   
   /**
    * Create a new VFSDeploymentClassLoaderPolicyModule.
    * 
    * @param unit the deployment unit
    * @throws IllegalArgumentException for a null deployment unit
    */
   public VFSDeploymentClassLoaderPolicyModule(VFSDeploymentUnit unit)
   {
      super(unit);
   }
 
   @Override
   public VFSDeploymentUnit getDeploymentUnit()
   {
      return (VFSDeploymentUnit) super.getDeploymentUnit();
   }

   @Override
   protected List<Capability> determineCapabilities()
   {
      // While we are here, check the roots
      VirtualFile[] roots = determineVFSRoots();

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
      
      // Do we determine package capabilities
      ClassFilter included = getIncluded();
      ClassFilter excluded = getExcluded();
      ClassFilter excludedExport = getExcludedExport();
      ExportAll exportAll = getExportAll();
      if (exportAll != null)
      {
         Set<String> exportedPackages = PackageVisitor.determineAllPackages(roots, exportAll, included, excluded, excludedExport);
         for (String packageName : exportedPackages)
         {
            capability = factory.createPackage(packageName, version);
            capabilities.add(capability);
         }
      }
      
      return capabilities;
   }
   
   /**
    * Get the virtual file roots
    * 
    * @return the roots
    */
   protected VirtualFile[] determineVFSRoots()
   {
      if (vfsRoots != null)
         return vfsRoots;

      ClassPathVisitor visitor = new ClassPathVisitor();
      try
      {
         getDeploymentUnit().visit(visitor);
      }
      catch (DeploymentException e)
      {
         throw new RuntimeException("Error visiting deployment: " + e);
      }
      Set<VirtualFile> classPath = visitor.getClassPath();
      
      vfsRoots = classPath.toArray(new VirtualFile[classPath.size()]);
      return vfsRoots;
   }

   @Override
   public VFSClassLoaderPolicy getPolicy()
   {
      return (VFSClassLoaderPolicy) super.getPolicy();
   }
   
   @Override
   protected VFSClassLoaderPolicy determinePolicy()
   {
      VirtualFile[] roots = determineVFSRoots();
      VFSClassLoaderPolicy policy = VFSClassLoaderPolicy.createVFSClassLoaderPolicy(getContextName(), roots);
      String[] packageNames = getPackageNames();
      policy.setExportedPackages(packageNames);
      policy.setIncluded(getIncluded());
      policy.setExcluded(getExcluded());
      policy.setExcludedExport(getExcludedExport());
      policy.setExportAll(getExportAll());
      policy.setImportAll(isImportAll());
      policy.setCacheable(isCacheable());
      policy.setBlackListable(isBlackListable());
      policy.setDelegates(getDelegates());
      return policy;
   }

   @Override
   public void reset()
   {
      super.reset();
      vfsRoots = null;
   }
}
