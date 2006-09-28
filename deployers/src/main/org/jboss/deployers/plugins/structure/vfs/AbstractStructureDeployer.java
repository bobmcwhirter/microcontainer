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
package org.jboss.deployers.plugins.structure.vfs;

import org.jboss.deployers.plugins.structure.vfs.jar.JARCandidateStructureVisitorFactory;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.deployers.spi.structure.vfs.StructureDeployer;
import org.jboss.logging.Logger;
import org.jboss.virtual.VirtualFile;
import org.jboss.virtual.VirtualFileVisitor;
import org.jboss.virtual.VisitorAttributes;

/**
 * AbstractStructureDeployer.<p>
 * 
 * We don't care about the order by default.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractStructureDeployer implements StructureDeployer
{
   /** The log */
   protected Logger log = Logger.getLogger(getClass());
   
   /** The candidate structure visitor factory */
   private CandidateStructureVisitorFactory candidateStructureVisitorFactory = JARCandidateStructureVisitorFactory.INSTANCE;
   
   public int getRelativeOrder()
   {
      return Integer.MAX_VALUE;
   }

   /**
    * Get the candidateStructureVisitorFactory.
    * 
    * @return the candidateStructureVisitorFactory.
    */
   public CandidateStructureVisitorFactory getCandidateStructureVisitorFactory()
   {
      return candidateStructureVisitorFactory;
   }

   /**
    * Set the candidateStructureVisitorFactory.
    * 
    * @param candidateStructureVisitorFactory the candidateStructureVisitorFactory.
    * @throws IllegalArgumentException for a null candidate structure
    */
   public void setCandidateStructureVisitorFactory(CandidateStructureVisitorFactory candidateStructureVisitorFactory)
   {
      if (candidateStructureVisitorFactory == null)
         throw new IllegalArgumentException("Null candidateStructureVisitorFactory");
      this.candidateStructureVisitorFactory = candidateStructureVisitorFactory;
   }

   public abstract boolean determineStructure(DeploymentContext context);
   
   /**
    * Add all children as candidates
    * 
    * @param parent the parent context
    * @throws Exception for any error
    */
   protected void addAllChildren(DeploymentContext parent) throws Exception
   {
      addChildren(parent, null);
   }
   
   /**
    * Add all children as candidates
    * 
    * @param parent the parent context
    * @param attributes the visitor attributes uses {@link VisitorAttributes#DEFAULT} when null
    * @throws Exception for any error
    */
   protected void addChildren(DeploymentContext parent, VisitorAttributes attributes) throws Exception
   {
      if (parent == null)
         throw new IllegalArgumentException("Null parent");
      
      VirtualFileVisitor visitor = candidateStructureVisitorFactory.createVisitor(parent, attributes);
      VirtualFile root = parent.getRoot();
      root.visit(visitor);
   }
}
