/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.beans.metadata.spi;

import java.util.Stack;

import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * A metadata vistor.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface MetaDataVisitor
{
   /**
    * Get the kernel controller context
    * 
    * @return the context
    */
   KernelControllerContext getControllerContext();

   /**
    * Get the context state
    * 
    * @return the context state
    */
   ControllerState getContextState();

   /**
    * Set the context state
    * 
    * @param contextState the context state
    */
   void setContextState(ControllerState contextState);

   /**
    * Add a dependency
    * 
    * @param dependency the dependency
    */
   void addDependency(DependencyItem dependency);
   
   /**
    * Visit the node
    * 
    * @param node the node
    */
   void initialVisit(MetaDataVisitorNode node);

   /**
    * Revisit the node
    *
    * @param node the node
    */
   void describeVisit(MetaDataVisitorNode node);

   /**
    * Current meta data visited branch
    *
    * @return stack of meta data objects in this branch
    */
   Stack<MetaDataVisitorNode> visitorNodeStack();
}
