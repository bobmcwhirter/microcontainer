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
package org.jboss.deployers.plugins.classloading;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.structure.spi.classloading.ClassLoaderMetaData;
import org.jboss.deployers.structure.spi.classloading.ExportAll;
import org.jboss.deployers.structure.spi.classloading.Version;

/**
 * ClassLoading.
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ClassLoading
{
   /** The classloading domains by name */
   private Map<String, Domain> domains = new ConcurrentHashMap<String, Domain>();
   
   /**
    * Add a deployment unit
    * 
    * @param deploymentUnit the deployment unit
    * @throws IllegalArgumentException for a null deployment unit
    */
   public void addDeploymentUnit(DeploymentUnit deploymentUnit)
   {
      if (deploymentUnit == null)
         throw new IllegalArgumentException("Null deployment unit");
      
      ClassLoaderMetaData metadata = deploymentUnit.getAttachment(ClassLoaderMetaData.class);
      if (metadata == null)
      {
         // REVIEW: There should probably be a warning here?
         metadata = new ClassLoaderMetaData();
         metadata.setName(deploymentUnit.getName());
         metadata.setExportAll(ExportAll.NON_EMPTY);
         metadata.setImportAll(true);
         metadata.setVersion(Version.DEFAULT_VERSION);
         deploymentUnit.addAttachment(ClassLoaderMetaData.class, metadata);
      }
      
      String domainName = metadata.getDomain();
      if (domainName == null)
      {
         domainName = ClassLoaderSystem.DEFAULT_DOMAIN_NAME;
         metadata.setDomain(domainName);
      }
      
      Domain domain;
      synchronized (domains)
      {
         domain = domains.get(domainName);
         if (domain == null)
         {
            domain = new Domain(domainName);
            domains.put(domainName, domain);
         }
      }
      
      domain.addDeploymentUnit(deploymentUnit, metadata);
   }
   
   /**
    * Remove a deployment unit
    * 
    * @param deploymentUnit the deployment unit
    * @throws IllegalArgumentException for a null deployment context
    */
   public void removeDeploymentUnit(DeploymentUnit deploymentUnit)
   {
      if (deploymentUnit == null)
         throw new IllegalArgumentException("Null deployment context");

      Module module = deploymentUnit.getAttachment(Module.class);
      if (module == null)
         throw new IllegalStateException("Deployment Unit has no module: " + deploymentUnit);
      module.release();
   }
}
