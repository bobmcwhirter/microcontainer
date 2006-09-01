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

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.deployers.spi.structure.DeploymentState;
import org.jboss.deployers.spi.structure.StructureDetermined;
import org.jboss.logging.Logger;
import org.jboss.util.UnreachableStatementException;
import org.jboss.vfs.spi.VirtualFile;

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
   
   /** The meta data location */
   private VirtualFile metaDataLocation;
   
   /** The class loader */
   private ClassLoader classLoader;

   /** The parent context */
   private DeploymentContext parent;

   /** The child contexts */
   private Set<DeploymentContext> children = new CopyOnWriteArraySet<DeploymentContext>();
   
   /** Throwable */
   private Throwable problem;

   /**
    * Create a new AbstractDeploymentContext.
    * 
    * @param name the name
    */
   public AbstractDeploymentContext(String name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      this.name = name;
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
      return false;
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

   public VirtualFile getMetaDataLocation()
   {
      return metaDataLocation;
   }

   public void setMetaDataLocation(VirtualFile location)
   {
      this.metaDataLocation = location;
   }

   public ClassLoader getClassLoader()
   {
      if (classLoader == null)
         throw new IllegalStateException("Attempt to retrieve classloader when it has not been set.");
      return classLoader;
   }
   
   public void setClassLoader(ClassLoader classLoader)
   {
      this.classLoader = classLoader;
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
