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

import org.jboss.deployers.plugins.deployers.helpers.AbstractRealDeployer;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.deployment.xml.BasicXMLDeployer;
import org.jboss.kernel.spi.deployment.KernelDeployment;

/**
 * KernelDeployer.<p>
 * 
 * This deployer is responsible for deploying all metadata of
 * type {@link org.jboss.kernel.spi.deployment.KernelDeployment}.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class KernelDeployer extends AbstractRealDeployer<KernelDeployment>
{
   /** The kernel deployer */
   private final BasicXMLDeployer deployer;
   
   /**
    * Create a new BeanDeployer.
    * 
    * @param kernel the kernel
    * @throws IllegalArgumentException for a null kernel
    */
   public KernelDeployer(Kernel kernel)
   {
      super(KernelDeployment.class);
      if (kernel == null)
         throw new IllegalArgumentException("Null kernel");
      deployer = new BasicXMLDeployer(kernel);
   }

   public void deploy(DeploymentUnit unit) throws DeploymentException
   {
      Set<KernelDeployment> deployments = getAllMetaData(unit);
      for (KernelDeployment deployment : deployments)
      {
         try
         {
            deployer.deploy(deployment);
         }
         catch (Throwable t)
         {
            undeploy(unit); // TODO better unwind
            throw DeploymentException.rethrowAsDeploymentException("Error deploying: " + deployment.getName(), t);
         }
      }
   }

   public void undeploy(DeploymentUnit unit)
   {
      Set<KernelDeployment> deployments = getAllMetaData(unit);
      for (KernelDeployment deployment : deployments)
         deployer.undeploy(deployment);
   }
}
