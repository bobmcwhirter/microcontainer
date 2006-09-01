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
package org.jboss.deployers.spi.structure.vfs;

import java.util.Comparator;

/**
 * StructureDeployer.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public interface StructureDeployer
{
   /**
    * Determine the structure of a deployment
    * 
    * @param context the context
    * @return true when it is recongnised
    */
   boolean determineStructure(VFSDeploymentContext context);

   /**
    * Get the relative order
    * 
    * @return the relative order
    */
   int getRelativeOrder();

   /** The comparator for relative ordering of deployers */
   Comparator<StructureDeployer> COMPARATOR = new StructureComparator();
   
   /**
    * The comparator for relative ordering of deployers
    */
   public class StructureComparator implements Comparator<StructureDeployer>
   {
      public int compare(StructureDeployer o1, StructureDeployer o2)
      {
         return o1.getRelativeOrder() - o2.getRelativeOrder();
      }
   }
}
