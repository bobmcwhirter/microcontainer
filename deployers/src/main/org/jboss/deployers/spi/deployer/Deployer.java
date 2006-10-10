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
package org.jboss.deployers.spi.deployer;

import java.util.Comparator;

import org.jboss.deployers.spi.DeploymentException;

/**
 * Deployer.
 * 
 * TODO contract for redeployment
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public interface Deployer
{
   /** The parser order */
   public static final int PARSER_DEPLOYER = 2000;

   /** The class loader order */
   public static final int CLASSLOADER_DEPLOYER = 4000;

   public static final int POSTPROCESS_CLASSLOADING_DEPLOYER = 5000;
   
   /** The component order */
   public static final int COMPONENT_DEPLOYER = 7000; 

   /** The real order */
   public static final int REAL_DEPLOYER = 10000;
   
   /**
    * Whether the deployer is relevant for this unit
    * 
    * @param unit the unit
    * @return true when relevant
    */
   boolean isRelevant(DeploymentUnit unit);
   
   /**
    * Prepare a deployment
    * 
    * @param unit the unit
    * @throws DeploymentException for any error
    */
   void prepareDeploy(DeploymentUnit unit) throws DeploymentException;

   /**
    * Prepare an undeployment
    * 
    * @param unit the unit
    */
   void prepareUndeploy(DeploymentUnit unit);
   
   /**
    * Handoff a deployment
    * 
    * @param from the from deployment
    * @param to the to deployment
    * @throws DeploymentException for any error
    */
   void handoff(DeploymentUnit from, DeploymentUnit to) throws DeploymentException;
   
   /**
    * Commit a deployment
    * 
    * @param unit the unit
    * @throws DeploymentException for any error
    */
   void commitDeploy(DeploymentUnit unit) throws DeploymentException;

   /**
    * Prepare an undeployment
    * 
    * @param unit the unit
    */
   void commitUndeploy(DeploymentUnit unit);

   /**
    * Get the relative order
    * 
    * @return the relative order
    */
   int getRelativeOrder();
   
   /** The comparator for relative ordering of deployers */
   Comparator<Deployer> COMPARATOR = new DeployerComparator();
   
   /**
    * The comparator for relative ordering of deployers
    */
   public class DeployerComparator implements Comparator<Deployer>
   {
      public int compare(Deployer o1, Deployer o2)
      {
         int relative = o1.getRelativeOrder() - o2.getRelativeOrder();
         if (relative != 0)
            return relative;
         return o1.toString().compareTo(o2.toString());
      }
   }
}
