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
package org.jboss.kernel.plugins.deployment;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.logging.Logger;

/**
 * BasicKernelDeployer.<p>
 * 
 * An extension to the abstract kernel deployer that keeps track
 * of deployments and adds a simple shutdown method.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class BasicKernelDeployer extends AbstractKernelDeployer
{
   /** The log */
   private static final Logger log = Logger.getLogger(BasicKernelDeployer.class);

   /** The deployments */
   protected List<KernelDeployment> deployments = new CopyOnWriteArrayList<KernelDeployment>();

   /**
    * Create a new BasicKernelDeployer.
    * 
    * @param kernel the kernel
    */
   public BasicKernelDeployer(Kernel kernel)
   {
      super(kernel);
   }

   public void deploy(KernelDeployment deployment) throws Throwable
   {
      final boolean trace = log.isTraceEnabled();
      if (trace)
         log.trace("Deploying " + deployment);
      super.deploy(deployment);
      deployments.add(deployment);
      if (trace)
         log.trace("Deployed " + deployment);
   }

   public void undeploy(KernelDeployment deployment)
   {
      final boolean trace = log.isTraceEnabled();
      if (trace)
         log.trace("Undeploying " + deployment);
      deployments.remove(deployment);
      super.undeploy(deployment);
      if (trace)
         log.trace("Undeployed " + deployment);
   }

   /**
    * Shutdown the deployer (undeploys everything)
    */
   public void shutdown()
   {
      ListIterator iterator = deployments.listIterator(deployments.size());
      while (iterator.hasPrevious())
      {
         KernelDeployment deployment = (KernelDeployment) iterator.previous();
         undeploy(deployment);
      }
   }
}
