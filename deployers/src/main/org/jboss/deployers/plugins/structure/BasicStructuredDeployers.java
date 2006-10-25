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
package org.jboss.deployers.plugins.structure;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.structure.vfs.StructureDeployer;
import org.jboss.deployers.spi.structure.vfs.StructureMetaData;
import org.jboss.deployers.spi.structure.vfs.StructuredDeployers;
import org.jboss.virtual.VirtualFile;

/**
 * A basic StructuredDeployers implementation.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class BasicStructuredDeployers
   implements StructuredDeployers
{
   
   private SortedSet<StructureDeployer> structureDeployers;

   public BasicStructuredDeployers()
   {
      this(new TreeSet<StructureDeployer>(StructureDeployer.COMPARATOR));
   }
   public BasicStructuredDeployers(SortedSet<StructureDeployer> structureDeployers)
   {
      this.structureDeployers = structureDeployers;
   }

   public boolean isEmpty()
   {
      return structureDeployers == null ? true : structureDeployers.isEmpty();
   }

   public boolean determineStructure(VirtualFile file, StructureMetaData metaData)
      throws DeploymentException
   {
      StructureDeployer[] theDeployers; 
      synchronized (this)
      {
         if (structureDeployers.isEmpty())
            throw new IllegalStateException("No structure deployers");
         
         theDeployers = structureDeployers.toArray(new StructureDeployer[structureDeployers.size()]);
      }

      boolean result = false;
      for (StructureDeployer deployer : theDeployers)
      {
         if (deployer.determineStructure(file, metaData, this))
         {
            result = true;
            break;
         }
      }
      return result;
   }

   /**
    * Get the 
    */
   public SortedSet<StructureDeployer> getDeployers()
   {
      return structureDeployers;
   }
   public void setDeployers(Set<StructureDeployer> deployers)
   {
      // Remove all the old deployers that are not in the new set
      HashSet<StructureDeployer> oldDeployers = new HashSet<StructureDeployer>(structureDeployers);
      oldDeployers.removeAll(deployers);
      for (StructureDeployer deployer : oldDeployers)
         removeDeployer(deployer);
      
      // Add all the new deployers that were not already present
      HashSet<StructureDeployer> newDeployers = new HashSet<StructureDeployer>(deployers);
      newDeployers.removeAll(structureDeployers);
      for (StructureDeployer deployer : newDeployers)
         addDeployer(deployer);
   }

   /**
    * Add a structure deployer
    * 
    * @param deployer the deployer
    */
   public synchronized void addDeployer(StructureDeployer deployer)
   {
      if (deployer == null)
         throw new IllegalArgumentException("Null deployer");
      structureDeployers.add(deployer);
   }

   /**
    * Remove a structure deployer
    * 
    * @param deployer the deployer
    */
   public synchronized void removeDeployer(StructureDeployer deployer)
   {
      if (deployer == null)
         throw new IllegalArgumentException("Null deployer");
      structureDeployers.remove(deployer);
   }

}
