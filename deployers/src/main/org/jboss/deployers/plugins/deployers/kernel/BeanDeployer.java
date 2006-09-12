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

import org.jboss.deployers.plugins.deployers.helpers.SchemaResolverDeployer;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.virtual.VirtualFile;

/**
 * BeanDeployer.<p>
 * 
 * This deployer is responsible for looking for -beans.xml
 * and creating the metadata object.<p>
 * 
 * The {@link KernelDeployer} does the real work of deployment.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class BeanDeployer extends SchemaResolverDeployer<KernelDeployment>
{
   /**
    * Create a new BeanDeployer.
    * 
    * @throws IllegalArgumentException for a null kernel
    */
   public BeanDeployer()
   {
      super(KernelDeployment.class);
   }

   public int getRelativeOrder()
   {
      return 1000;
   }

   protected void init(DeploymentUnit unit, KernelDeployment metaData, VirtualFile file) throws Exception
   {
      String name = file.toURI().toString();
      metaData.setName(name);
   }

   public void deploy(DeploymentUnit unit) throws DeploymentException
   {
      createMetaData(unit, null, "-beans.xml");
   }

   public void undeploy(DeploymentUnit unit)
   {
   }
}
