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
package org.jboss.deployers.vfs.spi.client;

import org.jboss.deployers.client.spi.DeploymentFactory;
import org.jboss.virtual.VirtualFile;

/**
 * VFSDeploymentFactory.
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class VFSDeploymentFactory extends DeploymentFactory
{
   /**
    * Get an instance of the factory
    * 
    * @return the factory
    */
   public static VFSDeploymentFactory getInstance()
   {
      return VFSDeploymentBuilder.getInstance();
   }
   
   /**
    * Create a new VFS deployment 
    * 
    * @param root the root virtual file
    * @return the deployment
    * @throws IllegalArgumentException for a null root
    */
   public VFSDeployment createVFSDeployment(VirtualFile root)
   {
      return VFSDeploymentBuilder.getInstance().newVFSDeployment(root);
   }

   /**
    * Create a new VFS deployment 
    * 
    * @param root the root virtual file
    * @return the deployment
    * @throws IllegalArgumentException for a null root
    */
   protected abstract VFSDeployment newVFSDeployment(VirtualFile root);
}
