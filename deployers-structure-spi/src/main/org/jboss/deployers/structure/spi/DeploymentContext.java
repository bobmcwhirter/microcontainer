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
package org.jboss.deployers.structure.spi;

import java.util.List;
import java.util.Set;

import org.jboss.deployers.client.spi.Deployment;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.DeploymentState;
import org.jboss.deployers.spi.attachments.ManagedObjectsWithTransientAttachments;

/**
 * DeploymentContext.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision: 59630 $
 */
public interface DeploymentContext extends ManagedObjectsWithTransientAttachments
{
   /**
    * Get the deployment name
    * 
    * @return the name
    */
   String getName();

   /**
    * Get the simple vfs name of the deployment unit. This is the simple
    * name of the virtual file .
    * 
    * vfs path ------------------- relative path
    * deploy/some.ear              "some.ear"
    * deploy/some.ear/x.ejb        "x.ejb"
    * deploy/some.ear/y.sar        "y.sar"
    * deploy/some.ear/y.sar/z.rar  "z.rar"
    * @return the deployment unit simple path
    */
   String getSimpleName();

   /**
    * Get the path of this deployment relative to the top of the deployment
    * 
    * vfs path ------------------- relative path
    * deploy/some.ear              ""
    * deploy/some.ear/x.ejb        "/x.ejb"
    * deploy/some.ear/y.sar        "/y.sar"
    * deploy/some.ear/y.sar/z.rar  "/y.sar/z.rar"
    * 
    * @return the top-level deployment relative path
    */
   String getRelativePath();

   /**
    * Get the deployment types associated with this deployment.
    * @return set of deployment type names deployers have identified
    * in this deployment.
    */
   Set<String> getTypes();
   
   /**
    * Get the deployment state
    * 
    * @return the state
    */
   DeploymentState getState();
   
   /**
    * Set the deployment state
    * 
    * @param state the state
    */
   void setState(DeploymentState state);

   /**
    * Get the deployment (if this is a top level context)
    * 
    * @return the deployment
    */
   Deployment getDeployment();
   
   /**
    * Set the deployment
    * 
    * @param deployment the deployment
    * @throws IllegalArgumentException for a null deployment
    */
   void setDeployment(Deployment deployment);

   /**
    * Get the deployment unit
    * 
    * @return the deployment
    */
   DeploymentUnit getDeploymentUnit();

   /**
    * Set the deployment unit
    * 
    * @param unit the deployment unit
    */
   void setDeploymentUnit(DeploymentUnit unit);
   
   /**
    * Gets the classloader for this deployment unit
    * 
    * @return the classloader
    */
   ClassLoader getClassLoader();
   
   /**
    * Set the class loader
    * 
    * @param classLoader the new classloader
    */
   void setClassLoader(ClassLoader classLoader);

   /**
    * Create a classloader
    * 
    * @param factory the factory
    * @return false if there is already is a classloader
    * @throws DeploymentException for any error
    */
   boolean createClassLoader(ClassLoaderFactory factory) throws DeploymentException;

   /**
    * Remove the classloader created by the factory
    */
   void removeClassLoader();

   /**
    * Whether this is a top level deployment
    * 
    * @return true when top level
    */
   boolean isTopLevel();
   
   /**
    * Get the top level deployment
    * 
    * @return the top level deployment
    */
   DeploymentContext getTopLevel();
   
   /**
    * The parent
    * 
    * @return the parent
    */
   DeploymentContext getParent();
   
   /**
    * Set the parent
    * 
    * @param parent the parent
    */
   void setParent(DeploymentContext parent);
   
   /**
    * The children
    * 
    * @return the children
    */
   List<DeploymentContext> getChildren();

   /**
    * Add a child
    * 
    * @param child the child to add
    */
   void addChild(DeploymentContext child);

   /**
    * Remove a child
    * 
    * @param child the child to remove
    * @return whether it was removed
    */
   boolean removeChild(DeploymentContext child);
   
   /**
    * Whether this is a component
    * 
    * @return true when a component
    */
   boolean isComponent();
   
   /**
    * The components
    * 
    * @return the components
    */
   List<DeploymentContext> getComponents();

   /**
    * Add a component
    * 
    * @param component the componnet to add
    */
   void addComponent(DeploymentContext component);

   /**
    * Remove a component
    * 
    * @param component the component to remove
    * @return whether it was removed
    */
   boolean removeComponent(DeploymentContext component);
   
   /**
    * Get a resource loader
    * 
    * @return the resource loader
    */
   DeploymentResourceLoader getResourceLoader();
   
   /**
    * Get a resource classloader
    * 
    * @return the resource classloader loader
    */
   ClassLoader getResourceClassLoader();

   /**
    * Visit the context and the children
    * 
    * @param visitor the visitor
    * @throws DeploymentException for any error in the visitor
    * @throws IllegalArgumentException for a null visitor
    */
   void visit(DeploymentContextVisitor visitor) throws DeploymentException;

   /**
    * Whether the deployment was processed
    * 
    * @return true when processed
    */
   boolean isDeployed();

   /**
    * Touch the context to say it is deployed
    */
   void deployed();
   
   /**
    * Get the problem for this context
    * 
    * @return the problem
    */
   Throwable getProblem();

   /**
    * Set the problem for this context
    * 
    * @param problem the problem
    */
   void setProblem(Throwable problem);
}
