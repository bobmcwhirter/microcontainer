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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.deployers.spi.structure.DeploymentState;
import org.jboss.deployers.spi.structure.StructureDetermined;
import org.jboss.logging.Logger;
import org.jboss.util.UnreachableStatementException;
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

   /** Whether this is a candidate deployment */
   private boolean candidate = false;

   /** The parent context */
   private DeploymentContext parent;

   /** The child contexts */
   private Set<DeploymentContext> children = new CopyOnWriteArraySet<DeploymentContext>();
   
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
         URL url = file.toURL();
         String name = url.toString();
         return name;
      }
      catch (MalformedURLException e)
      {
         throw new IllegalArgumentException("File does not have a valid url: " + file, e);
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
         log.trace("MetaData locaton for " + root.getPathName() + " is " + location.getPathName());
   }

   public ClassLoader getClassLoader()
   {
      return classLoader;
   }
   
   public void setClassLoader(ClassLoader classLoader)
   {
      this.classLoader = classLoader;
      if (classLoader != null)
         log.trace("ClassLoader for " + root.getPathName() + " is " + classLoader);
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
   }

   public boolean removeChild(DeploymentContext child)
   {
      if (child == null)
         throw new IllegalArgumentException("Null child");
      return children.remove(child);
   }

   public Throwable getProblem()
   {
      return problem;
   }

   public void setProblem(Throwable problem)
   {
      this.problem = problem;
   }

   public URL getMetaData(String name)
   {
      if (metaDataLocation == null)
         return null;
      try
      {
         VirtualFile child = metaDataLocation.findChild(name);
         if (child == null)
            return null;
         return child.toURL();
      }
      catch (Exception e)
      {
         log.debug("Error retrieving meta data: " + name, e);
         return null;
      }
   }

   public InputStream getMetaDataAsStream(String name)
   {
      URL url = getMetaData(name);
      if (url == null)
         return null;
      try
      {
         return url.openStream();
      }
      catch (Exception e)
      {
         log.debug("Error retrieving meta data: " + name + " from " + url, e);
         return null;
      }
   }
   
   public AbstractDeploymentContext clone()
   {
      try
      {
         return (AbstractDeploymentContext) super.clone();
      }
      catch (CloneNotSupportedException e)
      {
         throw new UnreachableStatementException();
      }
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
   
   public void dump()
   {
      log.trace("TODO: dump");
   }
}
