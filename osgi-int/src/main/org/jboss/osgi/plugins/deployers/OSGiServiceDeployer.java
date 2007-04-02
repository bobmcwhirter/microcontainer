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
package org.jboss.osgi.plugins.deployers;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.deployers.plugins.deployers.helpers.AbstractSimpleRealDeployer;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.osgi.plugins.dependency.OSGiServiceReferenceContext;
import org.jboss.osgi.plugins.facade.BundleContextImpl;
import org.jboss.osgi.spi.metadata.ServiceMetaData;
import org.osgi.framework.BundleContext;

/**
 * Create OSGi meta data from manifest.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class OSGiServiceDeployer extends AbstractSimpleRealDeployer<ServiceMetaData>
{
   /** The kernel controller */
   private final KernelController controller;

   public OSGiServiceDeployer(Kernel kernel)
   {
      super(ServiceMetaData.class);
      if (kernel == null)
         throw new IllegalArgumentException("Null kernel");
      controller = kernel.getController();
   }

   public void deploy(DeploymentUnit unit, ServiceMetaData deployment) throws DeploymentException
   {
      // todo - get deployment context in non-depricated way
      BundleContext bundleContext = new BundleContextImpl(unit.getDeploymentContext());
      ControllerContext context = new OSGiServiceReferenceContext(deployment, bundleContext);
      try
      {
         controller.install(context);
      }
      catch (Throwable t)
      {
         throw DeploymentException.rethrowAsDeploymentException("Error deploying: " + deployment.getId(), t);
      }      
   }

   public void undeploy(DeploymentUnit unit, ServiceMetaData deployment)
   {
      controller.uninstall(deployment.getId());
   }
}
