/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.deployers.vfs.plugins.classloader;

import java.net.URL;
import java.util.Set;

import org.jboss.deployers.plugins.classloading.AbstractTopLevelClassLoaderSystemDeployer;
import org.jboss.deployers.plugins.classloading.Module;
import org.jboss.deployers.structure.spi.DeploymentContext;
import org.jboss.deployers.vfs.spi.structure.helpers.ClassPathVisitor;
import org.jboss.virtual.VirtualFile;
import org.jboss.virtual.plugins.context.memory.MemoryContextFactory;
import org.jboss.virtual.spi.VFSContext;

/**
 * VFSTopLevelClassLoaderSystemDeployer.
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class VFSTopLevelClassLoaderSystemDeployer extends AbstractTopLevelClassLoaderSystemDeployer
{
   @Override
   protected VFSClassLoaderPolicy createTopLevelClassLoaderPolicy(DeploymentContext context, Module module) throws Exception
   {
      ClassPathVisitor visitor = new ClassPathVisitor();
      context.visit(visitor);
      Set<VirtualFile> classPath = visitor.getClassPath();
      
      VirtualFile[] roots = new VirtualFile[classPath.size() + 1];
      int i = 0;
      for (VirtualFile path : classPath)
         roots[i++] = path;
      
      MemoryContextFactory factory = MemoryContextFactory.getInstance();
      VFSContext ctx = factory.createRoot(module.getDynamicClassRoot());
      
      URL url = new URL(module.getDynamicClassRoot() + "/classes");
      roots[i++] = factory.createDirectory(url).getVirtualFile();
      
      VFSClassLoaderPolicy policy = new VFSClassLoaderPolicy(roots);
      policy.setExportAll(module.getExportAll());
      policy.setImportAll(module.isImportAll());
      // TODO JBMICROCONT-182 more policy from "module"
      return policy;
   }
}
