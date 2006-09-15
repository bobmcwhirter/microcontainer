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
package org.jboss.deployers.plugins.deployers.kernel;

import java.util.Set;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.deployers.plugins.deployers.helpers.AbstractRealDeployer;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.dependency.AbstractKernelControllerContext;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * KernelDeployer.<p>
 * 
 * This deployer is responsible for deploying all metadata of
 * type {@link org.jboss.kernel.spi.deployment.KernelDeployment}.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class BeanMetaDataDeployer extends AbstractRealDeployer<BeanMetaData>
{
   /** The kernel controller */
   private final KernelController controller;
   
   /**
    * Create a new BeanDeployer.
    * 
    * @param kernel the kernel
    * @throws IllegalArgumentException for a null kernel
    */
   public BeanMetaDataDeployer(Kernel kernel)
   {
      super(BeanMetaData.class);
      if (kernel == null)
         throw new IllegalArgumentException("Null kernel");
      controller = kernel.getController();
   }

   public void deploy(DeploymentUnit unit) throws DeploymentException
   {
      Set<BeanMetaData> beans = getAllMetaData(unit);
      for (BeanMetaData bean : beans)
      {
         KernelControllerContext context = new AbstractKernelControllerContext(null, bean, null);

         try
         {
            controller.install(context);
         }
         catch (Throwable t)
         {
            undeploy(unit); // TODO better unwind
            throw DeploymentException.rethrowAsDeploymentException("Error deploying: " + bean.getName(), t);
         }
      }
   }

   public void undeploy(DeploymentUnit unit)
   {
      Set<BeanMetaData> beans = getAllMetaData(unit);
      for (BeanMetaData bean : beans)
         controller.uninstall(bean.getName());
   }
}
