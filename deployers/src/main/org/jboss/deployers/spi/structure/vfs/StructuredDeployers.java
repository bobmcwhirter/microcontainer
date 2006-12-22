/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.deployers.spi.structure.vfs;

import java.util.SortedSet;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.virtual.VirtualFile;

/**
 * An encapsulation of the avaialble StructureDeployer
 *  
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public interface StructuredDeployers
{
   /**
    * 
    * @param file - root of the deployment in the VFS
    * @param metaData - the existing metadata to use/populate with structure metadata
    * @return true if the deployment was recognized, false if it was not
    * @throws DeploymentException - thrown on failure to parse the deployment
    */
   public boolean determineStructure(VirtualFile file, StructureMetaData metaData)
      throws DeploymentException;

   /**
    * Get the ordered set of deployers.
    * @return the ordered set of deployers.
    */
   public SortedSet<StructureDeployer> getDeployers();

   /**
    * Are there any deployers
    * @return true when empty
    */
   public boolean isEmpty();
}
