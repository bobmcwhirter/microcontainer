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

import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderFactory;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.deployers.plugins.deployers.helpers.AbstractRealDeployer;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.osgi.plugins.facade.BundleContextImpl;
import org.jboss.osgi.spi.metadata.OSGiMetaData;
import org.osgi.framework.BundleContext;

/**
 * Create OSGi meta data from manifest.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class BundleActivatorDeployer extends AbstractRealDeployer<OSGiMetaData>
{
   private final KernelController controller;

   public BundleActivatorDeployer(Kernel kernel)
   {
      if (kernel == null)
         throw new IllegalArgumentException("Null kernel");
      controller = kernel.getController();
      setDeploymentVisitor(new BundleActivatorDeploymentVisitor());
   }

   protected String createBundleActivatorBeanName(OSGiMetaData metaData)
   {
      return metaData.getBundleSymbolicName() + "Activator";
   }

   private class BundleActivatorDeploymentVisitor extends OSGiMetaDataDeploymentVisitor
   {
      public void deploy(DeploymentUnit unit, OSGiMetaData deployment) throws DeploymentException
      {
         String bundleActivator = deployment.getBundleActivator();
         if (bundleActivator != null)
         {
            String name = createBundleActivatorBeanName(deployment);
            // todo - get deployment context in non-depricated way
            BundleContext bundleContext = new BundleContextImpl(unit.getDeploymentContext());
            BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder(name, bundleActivator)
                  .addStartParameter(BundleContext.class.getName(), bundleContext)
                  .addStopParameter(BundleContext.class.getName(), bundleContext);
            BeanMetaData beanMetaData = builder.getBeanMetaData();
            try
            {
               controller.install(beanMetaData);
            }
            catch (Throwable throwable)
            {
               throw DeploymentException.rethrowAsDeploymentException("Unable to install BundleActivator.", throwable);
            }
         }
      }

      public void undeploy(DeploymentUnit unit, OSGiMetaData deployment)
      {
         String bundleActivator = deployment.getBundleActivator();
         if (bundleActivator != null)
         {
            String name = createBundleActivatorBeanName(deployment);
            controller.uninstall(name);
         }
      }
   }
}
