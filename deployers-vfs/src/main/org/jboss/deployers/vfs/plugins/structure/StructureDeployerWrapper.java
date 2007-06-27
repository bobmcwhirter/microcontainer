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
package org.jboss.deployers.vfs.plugins.structure;

import org.jboss.deployers.spi.structure.StructureMetaData;
import org.jboss.deployers.vfs.spi.structure.StructureDeployer;
import org.jboss.deployers.vfs.spi.structure.VFSStructuralDeployers;
import org.jboss.logging.Logger;
import org.jboss.virtual.VirtualFile;

/**
 * StructureDeployerWrapper.<p>
 * 
 * To avoid any problems with error handling by the deployers.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class StructureDeployerWrapper implements StructureDeployer
{
   /** The log */
   private Logger log;
   
   /** The structure deployer */
   private StructureDeployer deployer;   

   /**
    * Create a new StructureDeployerWrapper.
    * 
    * @param deployer the deployer
    */
   public StructureDeployerWrapper(StructureDeployer deployer)
   {
      if (deployer == null)
         throw new IllegalArgumentException("Null deployer");
      this.deployer = deployer;
      log = Logger.getLogger(deployer.getClass());
   }
   
   public boolean determineStructure(VirtualFile root, VirtualFile parent, VirtualFile file, StructureMetaData metaData, VFSStructuralDeployers deployers)
   {
      if (file == null)
         throw new IllegalArgumentException("Null file");
      
      try
      {
         boolean result = deployer.determineStructure(root, parent, file, metaData, deployers);
         if (log.isTraceEnabled())
         {
            if (result == false)
               log.trace("Not recognised: " + file.getName());
            else
               log.trace("Recognised: " + file.getName());
         }
         return result;
      }
      catch (Throwable t)
      {
         log.warn("Error during determineStructure: " + file.getName(), t);
         return false;
      }
   }
   
   public int getRelativeOrder()
   {
      return deployer.getRelativeOrder();
   }
   
   public void setRelativeOrder(int order)
   {
      deployer.setRelativeOrder(order);
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;
      if (obj == null || obj instanceof StructureDeployer == false)
         return false;
      if (obj instanceof StructureDeployerWrapper)
         obj = ((StructureDeployerWrapper) obj).deployer;
      return deployer.equals(obj);
   }
   
   @Override
   public int hashCode()
   {
      return deployer.hashCode();
   }
   
   @Override
   public String toString()
   {
      return deployer.toString();
   }
}
