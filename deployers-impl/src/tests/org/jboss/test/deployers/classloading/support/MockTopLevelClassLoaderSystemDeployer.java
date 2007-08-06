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
package org.jboss.test.deployers.classloading.support;

import java.util.ArrayList;
import java.util.List;

import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.test.support.MockClassLoaderPolicy;
import org.jboss.deployers.plugins.classloading.AbstractTopLevelClassLoaderSystemDeployer;
import org.jboss.deployers.plugins.classloading.Module;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.structure.spi.DeploymentContext;
import org.jboss.deployers.structure.spi.DeploymentUnit;

/**
 * MockTopLevelClassLoaderSystemDeployer.
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MockTopLevelClassLoaderSystemDeployer extends AbstractTopLevelClassLoaderSystemDeployer
{
   public List<String> deployed = new ArrayList<String>();
   public List<String> undeployed = new ArrayList<String>();

   @Override
   protected MockClassLoaderPolicy createTopLevelClassLoaderPolicy(DeploymentContext context, Module module) throws Exception
   {
      MockClassLoaderPolicy policy = new MockClassLoaderPolicy(context.getName());
      policy.setImportAll(module.isImportAll());
      policy.setDelegates(module.getDelegates());
      policy.setPathsAndPackageNames(module.getPackageNames());

      // TODO JBMICROCONT-182 - remove this hack
      context.getTransientAttachments().addAttachment(ClassLoaderPolicy.class, policy);
      
      return policy;
   }

   public void deploy(DeploymentUnit unit) throws DeploymentException
   {
      deployed.add(unit.getName());
      super.deploy(unit);
   }

   public void undeploy(DeploymentUnit unit)
   {
      undeployed.add(unit.getName());
      super.undeploy(unit);
   }
}
