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

import java.io.IOException;
import java.net.URI;
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
import org.jboss.virtual.VFSUtils;
import org.jboss.virtual.VirtualFile;

/**
 * AbstractDeploymentContext.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class AbstractDeploymentContext implements DeploymentContext
{
   /** The log */
   protected Logger log = Logger.getLogger(getClass());
   
   /** The name */
   private String name;

   /** Whether the structure is determined */
   private StructureDetermined structureDetermined = StructureDetermined.NO;

   /** The deployment state */
   private DeploymentState state;
   
   /** The deployment unit */
   private DeploymentUnit unit;

   /** The root */
   private VirtualFile root;
   
   /** The meta data location */
   private VirtualFile metaDataLocation;
   
   /** The class paths */
   private List<VirtualFile> classPath;
   
   /** The class loader */
   private ClassLoader classLoader;

   /** The class loader factory for this deployment */
   private ClassLoaderFactory classLoaderFactory;
   
   /** Whether this is a candidate deployment */
   private boolean candidate = false;

   /** The parent context */
   private DeploymentContext parent;

   /** The child contexts */
   private Set<DeploymentContext> children = new CopyOnWriteArraySet<DeploymentContext>();

   /** The component contexts */
   private Set<DeploymentContext> components = new CopyOnWriteArraySet<DeploymentContext>();
   
   /** The predtermined managed objects */
   private Attachments predeterminedManagedObjects = new AttachmentsImpl();
   
   /** The attachments */
   private Attachments transientAttachments = new AttachmentsImpl();
   
   /** The managed objects */
   private Attachments transientManagedObjects = new AttachmentsImpl();
   
   /** Throwable */
   private Throwable problem;
   
   /**
    * Get the deployment name
    * 
    * @param file the file
    * @return the name;
    */
   public static String getDeploymentName(VirtualFile file)
   {
      if (file == null)
         throw new IllegalArgumentException("Null file");
      try
      {
         URI uri = file.toURI();
         String name = uri.toString();
         return name;
      }
      catch (Exception e)
      {
         throw new IllegalArgumentException("File does not have a valid uri: " + file, e);
      }
   }

   /**
    * Create a new AbstractDeploymentContext.
    * 
    * @param name the name
    * @throws IllegalArgumentException if the name is null
    */
   public AbstractDeploymentContext(String name)
   {
      this(name, false);
   }

   /**
    * Create a new AbstractDeploymentContext.
    * 
    * @param name the name
    * @param candidate whether this is a candidate
    * @throws IllegalArgumentException if the name is null
    */
   public AbstractDeploymentContext(String name, boolean candidate)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      this.name = name;
      this.candidate = candidate;
   }

   /**
    * Create a new AbstractDeploymentContext.
    * 
    * @param name the name
    * @param parent the parent
    * @throws IllegalArgumentException if the name or parent is null
    */
   public AbstractDeploymentContext(String name, DeploymentContext parent)
   {
      this(name, false, parent);
   }

   /**
    * Create a new AbstractDeploymentContext.
    * 
    * @param name the name
    * @param candidate whether this is a candidate
    * @param parent the parent
    * @throws IllegalArgumentException if the name or parent is null
    */
   public AbstractDeploymentContext(String name, boolean candidate, DeploymentContext parent)
   {
      this(name, candidate);
      if (parent == null)
         throw new IllegalArgumentException("Null parent");
      setParent(parent);
   }
   
   /**
    * Create a new AbstractDeploymentContext.
    * 
    * @param root the root
    * @throws IllegalArgumentException if the file/root is null 
    */
   public AbstractDeploymentContext(VirtualFile root)
   {
      this(getDeploymentName(root), false);
      setRoot(root);
   }
   
   /**
    * Create a new AbstractDeploymentContext.
    * 
    * @param root the root
    * @param candidate whether this is a candidate
    * @throws IllegalArgumentException if the file/root is null 
    */
   public AbstractDeploymentContext(VirtualFile root, boolean candidate)
   {
      this(getDeploymentName(root), candidate);
      setRoot(root);
   }
   
   /**
    * Create a new AbstractDeploymentContext.
    * 
    * @param root the root
    * @param candidate whether this is a candidate
    * @param parent the parent
    * @throws IllegalArgumentException if the file/root or parent is null 
    */
   public AbstractDeploymentContext(VirtualFile root, boolean candidate, DeploymentContext parent)
   {
      this(getDeploymentName(root), candidate, parent);
      setRoot(root);
   }

   public String getName()
   {
      return name;
   }

   public StructureDetermined getStructureDetermined()
   {
      return structureDetermined;
   }

   public void setStructureDetermined(StructureDetermined determined)
   {
      if (determined == null)
         throw new IllegalArgumentException("Null determined");
      this.structureDetermined = determined;
   }
   
   public boolean isCandidate()
   {
      return candidate;
   }

   public DeploymentState getState()
   {
      return state;
   }

   public void setState(DeploymentState state)
   {
      this.state = state;
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
      return root;
   }

   /**
    * Set the root location
    * 
    * @param root the root
    */
   public void setRoot(VirtualFile root)
   {
      this.root = root;
   }
   
   public void setMetaDataPath(String path)
   {
      if (path == null)
         setMetaDataLocation(null);
      try
      {
         setMetaDataLocation(root.findChild(path));
      }
      catch (IOException e)
      {
         log.debug("Meta data path does not exist: root=" + root.getPathName() + " path=" + path);
      }
   }

   public VirtualFile getMetaDataLocation()
   {
      return metaDataLocation;
   }

   public void setMetaDataLocation(VirtualFile location)
   {
      this.metaDataLocation = location;
      if (log.isTraceEnabled() && location != null)
         log.trace("MetaData location for " + root.getPathName() + " is " + location.getPathName());
   }

   public ClassLoader getClassLoader()
   {
      return classLoader;
   }
   
   public void setClassLoader(ClassLoader classLoader)
   {
      this.classLoader = classLoader;
      if (classLoader != null)
         log.trace("ClassLoader for " + name + " is " + classLoader);
   }
   
   public boolean createClassLoader(ClassLoaderFactory factory) throws DeploymentException
   {
      if (factory == null)
         throw new IllegalArgumentException("Null factory");

      ClassLoader cl = getClassLoader();
      if (cl != null)
         return false;

      try
      {
         cl = factory.createClassLoader(this);
         if (cl != null)
         {
            setClassLoader(cl);
            this.classLoaderFactory = factory;
         }
      }
      catch (Throwable t)
      {
         throw DeploymentException.rethrowAsDeploymentException("Error creating classloader for " + getName(), t);
      }
      return true;
   }

   public void removeClassLoader()
   {
      if (classLoaderFactory == null)
         return;
      try
      {
         classLoaderFactory.removeClassLoader(this);
      }
      catch (Throwable t)
      {
         log.warn("Error removing classloader for " + getName(), t);
      }
      classLoaderFactory = null;
      setClassLoader(null);
   }

   
   public List<VirtualFile> getClassPath()
   {
      return classPath;
   }
   
   public void setClassPath(List<VirtualFile> paths)
   {
      this.classPath = paths;
      if (log.isTraceEnabled() && paths != null)
         log.trace("ClassPath for " + root.getPathName() + " is " + VFSUtils.getPathsString(paths));
   }

   public boolean isTopLevel()
   {
      return parent == null;
   }

   public DeploymentContext getTopLevel()
   {
      DeploymentContext result = this;
      DeploymentContext parent = getParent();
      while (parent != null)
      {
         result = parent;
         parent = parent.getParent();
      }
      return result;
   }
   
   public DeploymentContext getParent()
   {
      return parent;
   }

   public void setParent(DeploymentContext parent)
   {
      if (parent != null && this.parent != null)
         throw new IllegalStateException("Context already has a parent " + getName());
      this.parent = parent;
   }

   public Set<DeploymentContext> getChildren()
   {
      return Collections.unmodifiableSet(children);
   }

   public void addChild(DeploymentContext child)
   {
      if (child == null)
         throw new IllegalArgumentException("Null child");
      children.add(child);
      if (log.isTraceEnabled())
         log.trace("Adding child " + child.getName() + " to " + getName() + " candidate=" + child.isCandidate());
   }

   public boolean removeChild(DeploymentContext child)
   {
      if (child == null)
         throw new IllegalArgumentException("Null child");
      return children.remove(child);
   }

   public boolean isComponent()
   {
      return false;
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
      log.debug("Added component " + component.getName() + " to " + getName());
   }

   public boolean removeComponent(DeploymentContext component)
   {
      if (component == null)
         throw new IllegalArgumentException("Null component");

      Set<DeploymentContext> componentComponents = component.getComponents();
      if (componentComponents.isEmpty() == false)
         log.warn("Removing component " + name + " which still has components " + componentComponents);
      boolean result = components.remove(component);
      if (result)
         log.debug("Removed component " + component.getName() + " from " + getName());
      return result;
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
      return predeterminedManagedObjects;
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
      return problem;
   }

   public void setProblem(Throwable problem)
   {
      this.problem = problem;
   }

   public VirtualFile getMetaDataFile(String name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      try
      {
         // There isn't a metadata location so let's see whether the root matches.
         if (metaDataLocation == null)
         {
            // It has to be a plain file that is not an archive
            if (root != null && root.isFile() && root.isArchive() == false)
            {
               String fileName = root.getName();
               if (fileName.equals(name))
                  return root;
            }
            
            // No match
            return null;
         }
         // Look in the meta data location
         return metaDataLocation.findChild(name);
      }
      catch (Exception e)
      {
         log.debug("Error retrieving meta data: " + name, e);
         return null;
      }
   }

   public List<VirtualFile> getMetaDataFiles(String name, String suffix)
   {
      if (name == null && suffix == null)
         throw new IllegalArgumentException("Null name and suffix");
      try
      {
         // There isn't a metadata location so let's see whether the root matches.
         // i.e. the top level is an xml
         if (metaDataLocation == null)
         {
            // It has to be a plain file that is not an archive
            if (root != null && root.isFile() && root.isArchive() == false)
            {
               String fileName = root.getName();
               if (name != null && fileName.equals(name))
                  return Collections.singletonList(root);
               if (suffix != null && fileName.endsWith(suffix))
                  return Collections.singletonList(root);
            }
            
            // No match
            return Collections.emptyList();
         }
         // Look in the meta data location
         return metaDataLocation.getChildren(new MetaDataMatchFilter(name, suffix));
      }
      catch (Exception e)
      {
         log.debug("Error retrieving meta data: name=" + name + " suffix=" + suffix, e);
         return Collections.emptyList();
      }
   }
   
   public void reset()
   {
      if (structureDetermined != StructureDetermined.PREDETERMINED)
         children.clear();
      else
      {
         for (DeploymentContext child : children)
            child.reset();
      }
      components.clear();
      
      classLoader = null;
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
