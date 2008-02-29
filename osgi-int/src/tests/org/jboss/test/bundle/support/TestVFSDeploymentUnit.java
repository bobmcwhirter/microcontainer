/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.bundle.support;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.deployers.client.spi.main.MainDeployer;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.attachments.MutableAttachments;
import org.jboss.deployers.structure.spi.ClassLoaderFactory;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.structure.spi.DeploymentUnitVisitor;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentResourceLoader;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentUnit;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.MutableMetaData;
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.virtual.VirtualFile;

/**
 * A TestVFSDeploymentUnit.
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public class TestVFSDeploymentUnit implements VFSDeploymentUnit
{
   private static final long serialVersionUID = 1L;

   private final VirtualFile root;

   public TestVFSDeploymentUnit(VirtualFile root)
   {
      this.root = root;
   }

   public void addClassPath(List<VirtualFile> files)
   {
      // FIXME addClassPath

   }

   public void addClassPath(VirtualFile... files)
   {
      // FIXME addClassPath

   }

   public List<VirtualFile> getClassPath()
   {
      // FIXME getClassPath
      return null;
   }

   public VirtualFile getFile(String path)
   {
      try
      {
         return root.getChild(path);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   public VirtualFile getMetaDataFile(String name)
   {
      return null;
   }

   public List<VirtualFile> getMetaDataFiles(String name, String suffix)
   {
      // FIXME getMetaDataFiles
      return null;
   }

   public VFSDeploymentUnit getParent()
   {
      // FIXME getParent
      return null;
   }

   public VFSDeploymentResourceLoader getResourceLoader()
   {
      // FIXME getResourceLoader
      return null;
   }

   public VirtualFile getRoot()
   {
      return root;
   }

   public VFSDeploymentUnit getTopLevel()
   {
      // FIXME getTopLevel
      return null;
   }

   public List<VFSDeploymentUnit> getVFSChildren()
   {
      // FIXME getVFSChildren
      return null;
   }

   public void setClassPath(List<VirtualFile> classPath)
   {
      // FIXME setClassPath

   }

   public DeploymentUnit addComponent(String name)
   {
      // FIXME addComponent
      return null;
   }

   public void addControllerContextName(Object name)
   {
      // FIXME addControllerContextName

   }

   public void addIDependOn(DependencyItem dependency)
   {
      // FIXME addIDependOn

   }

   public boolean createClassLoader(ClassLoaderFactory factory) throws DeploymentException
   {
      // FIXME createClassLoader
      return false;
   }

   public <T> Set<? extends T> getAllMetaData(Class<T> type)
   {
      // FIXME getAllMetaData
      return null;
   }

   public List<DeploymentUnit> getChildren()
   {
      // FIXME getChildren
      return null;
   }

   public ClassLoader getClassLoader()
   {
      // FIXME getClassLoader
      return null;
   }

   public List<DeploymentUnit> getComponents()
   {
      // FIXME getComponents
      return null;
   }

   public Set<Object> getControllerContextNames()
   {
      // FIXME getControllerContextNames
      return null;
   }

   public DependencyInfo getDependencyInfo()
   {
      // FIXME getDependencyInfo
      return null;
   }

   public MainDeployer getMainDeployer()
   {
      // FIXME getMainDeployer
      return null;
   }

   public MetaData getMetaData()
   {
      // FIXME getMetaData
      return null;
   }

   public MutableMetaData getMutableMetaData()
   {
      // FIXME getMutableMetaData
      return null;
   }

   public ScopeKey getMutableScope()
   {
      // FIXME getMutableScope
      return null;
   }

   public String getName()
   {
      // FIXME getName
      return null;
   }

   public String getRelativePath()
   {
      // FIXME getRelativePath
      return null;
   }

   public ClassLoader getResourceClassLoader()
   {
      // FIXME getResourceClassLoader
      return null;
   }

   public ScopeKey getScope()
   {
      // FIXME getScope
      return null;
   }

   public String getSimpleName()
   {
      // FIXME getSimpleName
      return null;
   }

   public MutableAttachments getTransientManagedObjects()
   {
      // FIXME getTransientManagedObjects
      return null;
   }

   public Set<String> getTypes()
   {
      // FIXME getTypes
      return null;
   }

   public boolean isComponent()
   {
      // FIXME isComponent
      return false;
   }

   public boolean isTopLevel()
   {
      // FIXME isTopLevel
      return false;
   }

   public void removeClassLoader(ClassLoaderFactory factory)
   {
      // FIXME removeClassLoader

   }

   public boolean removeComponent(String name)
   {
      // FIXME removeComponent
      return false;
   }

   public void removeControllerContextName(Object name)
   {
      // FIXME removeControllerContextName

   }

   public void removeIDependOn(DependencyItem dependency)
   {
      // FIXME removeIDependOn

   }

   public void setMutableScope(ScopeKey key)
   {
      // FIXME setMutableScope

   }

   public void setScope(ScopeKey key)
   {
      // FIXME setScope

   }

   public void visit(DeploymentUnitVisitor visitor) throws DeploymentException
   {
      // FIXME visit

   }

   public <T> T addAttachment(Class<T> type, T attachment)
   {
      // FIXME addAttachment
      return null;
   }

   public Object addAttachment(String name, Object attachment)
   {
      // FIXME addAttachment
      return null;
   }

   public <T> T addAttachment(String name, T attachment, Class<T> expectedType)
   {
      // FIXME addAttachment
      return null;
   }

   public void clear()
   {
      // FIXME clear

   }

   public void clearChangeCount()
   {
      // FIXME clearChangeCount

   }

   public int getChangeCount()
   {
      // FIXME getChangeCount
      return 0;
   }

   public <T> T removeAttachment(Class<T> type)
   {
      // FIXME removeAttachment
      return null;
   }

   public <T> T removeAttachment(String name, Class<T> expectedType)
   {
      // FIXME removeAttachment
      return null;
   }

   public Object removeAttachment(String name)
   {
      // FIXME removeAttachment
      return null;
   }

   public void setAttachments(Map<String, Object> map)
   {
      // FIXME setAttachments

   }

   public <T> T getAttachment(Class<T> type)
   {
      // FIXME getAttachment
      return null;
   }

   public <T> T getAttachment(String name, Class<T> expectedType)
   {
      // FIXME getAttachment
      return null;
   }

   public Object getAttachment(String name)
   {
      // FIXME getAttachment
      return null;
   }

   public Map<String, Object> getAttachments()
   {
      // FIXME getAttachments
      return null;
   }

   public boolean hasAttachments()
   {
      // FIXME hasAttachments
      return false;
   }

   public boolean isAttachmentPresent(Class<?> type)
   {
      return false;
   }

   public boolean isAttachmentPresent(String name, Class<?> expectedType)
   {
      // FIXME isAttachmentPresent
      return false;
   }

   public boolean isAttachmentPresent(String name)
   {
      // FIXME isAttachmentPresent
      return false;
   }

}
