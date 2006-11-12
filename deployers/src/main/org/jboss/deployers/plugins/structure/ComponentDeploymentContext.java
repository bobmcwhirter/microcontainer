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
package org.jboss.deployers.plugins.structure;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.jboss.deployers.plugins.attachments.AttachmentsImpl;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.attachments.Attachments;
import org.jboss.deployers.spi.classloader.ClassLoaderFactory;
import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.deployers.spi.structure.DeploymentContextVisitor;
import org.jboss.deployers.spi.structure.DeploymentState;
import org.jboss.deployers.spi.structure.StructureDetermined;
import org.jboss.logging.Logger;
import org.jboss.virtual.VirtualFile;

/**
 * AbstractDeploymentContext.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ComponentDeploymentContext
   implements DeploymentContext, Serializable
{
   private static final long serialVersionUID = 1;

   /** The log */
   protected Logger log = Logger.getLogger(getClass());
   
   /** The name */
   private String name;
   
   /** The deployment unit */
   private DeploymentUnit unit;

   /** The parent context */
   private DeploymentContext parent;

   /** The component contexts */
   private Set<DeploymentContext> components = new CopyOnWriteArraySet<DeploymentContext>();
   
   /** The attachments */
   private transient Attachments transientAttachments = new AttachmentsImpl();
   
   /** The managed objects */
   private transient Attachments transientManagedObjects = new AttachmentsImpl();

   /**
    * Create a new ComponentDeploymentContext.
    * 
    * @param name the name
    * @param parent the parent
    * @throws IllegalArgumentException if the name or parent is null
    */
   public ComponentDeploymentContext(String name, DeploymentContext parent)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      if (parent == null)
         throw new IllegalArgumentException("Null parent");
      this.name = name;
      this.parent = parent;
   }

   public String getName()
   {
      return name;
   }

   public StructureDetermined getStructureDetermined()
   {
      return parent.getStructureDetermined();
   }

   public void setStructureDetermined(StructureDetermined determined)
   {
      throw new UnsupportedOperationException("Not supported for components");
   }
   
   public boolean isCandidate()
   {
      return parent.isCandidate();
   }

   public DeploymentState getState()
   {
      return parent.getState();
   }

   public void setState(DeploymentState state)
   {
      parent.setState(state);
   }

   public DeploymentUnit getDeploymentUnit()
   {
      if (unit == null)
         throw new IllegalStateException("Deployment unit has not been set");
      return unit;
   }

   public void setDeploymentUnit(DeploymentUnit unit)
   {
      this.unit = unit;
   }

   public VirtualFile getRoot()
   {
      return parent.getRoot();
   }

   /**
    * Set the root location
    * 
    * @param root the root
    */
   public void setRoot(VirtualFile root)
   {
      throw new UnsupportedOperationException("Not supported for components");
   }
   
   public void setMetaDataPath(String path)
   {
      throw new UnsupportedOperationException("Not supported for components");
   }

   public VirtualFile getMetaDataLocation()
   {
      return parent.getMetaDataLocation();
   }

   public void setMetaDataLocation(VirtualFile location)
   {
      throw new UnsupportedOperationException("Not supported for components");
   }

   public ClassLoader getClassLoader()
   {
      return parent.getClassLoader();
   }
   
   public void setClassLoader(ClassLoader classLoader)
   {
      throw new UnsupportedOperationException("Not supported for components");
   }
   
   public boolean createClassLoader(ClassLoaderFactory factory) throws DeploymentException
   {
      return false;
   }

   public void removeClassLoader()
   {
   }
   
   public List<VirtualFile> getClassPath()
   {
      return parent.getClassPath();
   }
   
   public void setClassPath(List<VirtualFile> paths)
   {
      throw new UnsupportedOperationException("Not supported for components");
   }

   public boolean isTopLevel()
   {
      return false;
   }

   public DeploymentContext getTopLevel()
   {
      return parent.getTopLevel();
   }
   
   public DeploymentContext getParent()
   {
      return parent;
   }

   public void setParent(DeploymentContext parent)
   {
      throw new UnsupportedOperationException("Not supported for components");
   }

   public Set<DeploymentContext> getChildren()
   {
      return Collections.emptySet();
   }

   public void addChild(DeploymentContext child)
   {
      throw new UnsupportedOperationException("Not supported for components");
   }

   public boolean removeChild(DeploymentContext child)
   {
      throw new UnsupportedOperationException("Not supported for components");
   }

   public boolean isComponent()
   {
      return true;
   }

   public Set<DeploymentContext> getComponents()
   {
      return Collections.unmodifiableSet(components);
   }

   public void addComponent(DeploymentContext component)
   {
      if (component == null)
         throw new IllegalArgumentException("Null component");
      components.add(component);
   }

   public boolean removeComponent(DeploymentContext component)
   {
      if (component == null)
         throw new IllegalArgumentException("Null component");
      return components.remove(component);
   }

   public void visit(DeploymentContextVisitor visitor) throws DeploymentException
   {
      if (visitor == null)
         throw new IllegalArgumentException("Null visitor");

      visit(this, visitor);
   }
   
   /**
    * Visit a context
    * 
    * @param context the context
    * @param visitor the visitor
    * @throws DeploymentException for any error
    */
   private void visit(DeploymentContext context, DeploymentContextVisitor visitor) throws DeploymentException
   {
      visitor.visit(context);
      try
      {
         Set<DeploymentContext> children = context.getChildren();
         if (children.isEmpty())
            return;
         
         DeploymentContext[] childContexts = children.toArray(new DeploymentContext[children.size()]);
         for (int i = 0; i < childContexts.length; ++i)
         {
            if (childContexts[i] == null)
               throw new IllegalStateException("Null child context for " + context.getName() + " children=" + children);
            try
            {
               visit(childContexts[i], visitor);
            }
            catch (Throwable t)
            {
               for (int j = i-1; j >= 0; --j)
                  visitError(childContexts[j], visitor, true);
               throw DeploymentException.rethrowAsDeploymentException("Error visiting: " + childContexts[i].getName(), t);
            }
         }
      }
      catch (Throwable t)
      {
         visitError(context, visitor, false);
         throw DeploymentException.rethrowAsDeploymentException("Error visiting: " + context.getName(), t);
      }
   }

   /**
    * Unwind the visit invoking the previously visited context's error handler
    * 
    * @param context the context
    * @param visitor the visitor
    * @param visitChildren whether to visit the children
    * @throws DeploymentException for any error
    */
   private void visitError(DeploymentContext context, DeploymentContextVisitor visitor, boolean visitChildren) throws DeploymentException
   {
      if (visitChildren)
      {
         Set<DeploymentContext> children = context.getChildren();
         if (children.isEmpty())
            return;
         
         for (DeploymentContext child : children)
         {
            try
            {
               visitError(child, visitor, true);
            }
            catch (Throwable t)
            {
               log.warn("Error during visit error: " + child.getName(), t);
            }
         }
         
      }
      try
      {
         visitor.error(context);
      }
      catch (Throwable t)
      {
         log.warn("Error during visit error: " + context.getName(), t);
      }
   }

   public Attachments getPredeterminedManagedObjects()
   {
      return parent.getPredeterminedManagedObjects();
   }
   
   public Attachments getTransientManagedObjects()
   {
      return transientManagedObjects;
   }
   
   public Attachments getTransientAttachments()
   {
      return transientAttachments;
   }

   public Throwable getProblem()
   {
      return parent.getProblem();
   }

   public void setProblem(Throwable problem)
   {
      parent.setProblem(problem);
   }

   public VirtualFile getMetaDataFile(String name)
   {
      return parent.getMetaDataFile(name);
   }

   public List<VirtualFile> getMetaDataFiles(String name, String suffix)
   {
      return parent.getMetaDataFiles(name, suffix);
   }
   
   public void deployed()
   {
      parent.deployed();
   }

   public boolean isDeployed()
   {
      return parent.isDeployed();
   }

   public void reset()
   {
      components.clear();
      
      transientManagedObjects.clear();
      transientAttachments.clear();
   }

   public String toString()
   {
      StringBuilder buffer = new StringBuilder();
      buffer.append(getClass().getSimpleName());
      buffer.append('@');
      buffer.append(System.identityHashCode(this));
      buffer.append('{').append(name).append('}');
      return buffer.toString();
   }
}
