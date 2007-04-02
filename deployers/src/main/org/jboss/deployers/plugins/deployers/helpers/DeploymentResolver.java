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
package org.jboss.deployers.plugins.deployers.helpers;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelController;

/**
 * Create DeploymentControllerContext and setup
 * its dependencies regarding meta data.
 * Reverse on the undeploy.
 *
 * @param <T> the expected type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class DeploymentResolver<T> extends AbstractSimpleRealDeployer<T>
{
   private KernelController controller;

   protected DeploymentResolver(Class<T> deploymentType, Kernel kernel)
   {
      super(deploymentType);
      if (kernel == null)
         throw new IllegalArgumentException("Null kernel");
      controller = kernel.getController();
   }

   protected abstract void addDependencies(T deployment, DependencyInfo dependencyInfo);

   public void deploy(DeploymentUnit unit, T deployment) throws DeploymentException
   {
      ControllerContext context = null; //new DeploymentControllerContext();
      DependencyInfo dependencyInfo = context.getDependencyInfo();
      addDependencies(deployment, dependencyInfo);
      unit.addAttachment(ControllerContext.class, context);
      try
      {
         controller.install(context);
      }
      catch (Throwable t)
      {
         unit.removeAttachment(ControllerContext.class);
         DeploymentException.rethrowAsDeploymentException("Unable to resolve DependencyControllerContext.", t);
      }
   }

   protected abstract void removeDependencies(T deployment, DependencyInfo dependencyInfo);

   public void undeploy(DeploymentUnit unit, T deployment)
   {
      ControllerContext context = unit.getAttachment(ControllerContext.class);
      DependencyInfo dependencyInfo = context.getDependencyInfo();
      removeDependencies(deployment, dependencyInfo);
      controller.uninstall(context.getName());
   }
}
