/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.jboss.deployers.plugins.structure.AbstractDeploymentContext;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.deployers.spi.structure.vfs.ClassPathInfo;
import org.jboss.deployers.spi.structure.vfs.ContextInfo;
import org.jboss.deployers.spi.structure.vfs.StructureBuilder;
import org.jboss.deployers.spi.structure.vfs.StructureMetaData;
import org.jboss.logging.Logger;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VFSUtils;
import org.jboss.virtual.VirtualFile;
import org.jboss.virtual.VisitorAttributes;
import org.jboss.virtual.plugins.vfs.helpers.SuffixMatchFilter;

/**
 * The default StructureBuilder. It translates a StructureMetaData instance
 * into a DeploymentContext tree.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class DefaultStructureBuilder
   implements StructureBuilder
{
   private static Logger log = Logger.getLogger(DefaultStructureBuilder.class);

   public void populateContext(DeploymentContext context, StructureMetaData metaData)
      throws DeploymentException
	{
      HashMap<String, DeploymentContext> contextMap = new HashMap<String, DeploymentContext>();
      // Validate that the root file has a valid ContextInfo
		VirtualFile root = context.getRoot();
      ContextInfo rootInfo = metaData.getContext(root.getPathName());
      if( rootInfo == null )
         throw new DeploymentException("Failed to find ContextInfo for context root: "+root);

      // Process the context from the root down
      VFS vfs = root.getVFS();
      contextMap.put(root.getPathName(), context);
      try
      {
         for(ContextInfo info : metaData.getContexts())
         {
            String vfsPath = info.getVfsPath();
            VirtualFile vf = vfs.findChild(vfsPath);
            ContextInfo parentInfo = info.getParent();
            String parentPath = parentInfo != null ? parentInfo.getVfsPath() : "";
            DeploymentContext ctx = contextMap.get(vfsPath);
            DeploymentContext parent = contextMap.get(parentPath);
            if( ctx == null )
            {
               if( parent != null )
               {
                  ctx = new AbstractDeploymentContext(vf, true, parent);
                  parent.addChild(ctx);
               }
               else
                  ctx = new AbstractDeploymentContext(vf);
            }
            processContext(ctx, vf, info, contextMap);
         }
      }
      catch(Exception e)
      {
         throw new DeploymentException("Failed to process context: "+context.getName(), e);
      }
      contextMap.clear();
	}

   protected void processContext(DeploymentContext context, VirtualFile virtualFile,
         ContextInfo info, HashMap<String, DeploymentContext> contextMap)
   {
      boolean trace = log.isTraceEnabled();
      if( trace )
         log.trace("Processing context: "+context+", info: "+info);
      String metaDataPath = info.getMetaDataPath();
      if( metaDataPath != null && metaDataPath.length() > 0 )
         context.setMetaDataPath(metaDataPath);
      ArrayList<VirtualFile> paths = new ArrayList<VirtualFile>();
      List<ClassPathInfo> classPath = info.getClassPath();
      boolean classPathHadVF = false;
      if( classPath != null )
      {
         for(ClassPathInfo cp : classPath)
         {
            try
            {
               VirtualFile child = virtualFile.findChild(cp.getPath());
               String suffixesOpt = (String) cp.getOption("suffixes");
               String[] suffixes = null;
               if( suffixesOpt != null )
                  suffixes = suffixesOpt.split(",");
               // Add the path if there is no suffix
               if( suffixes == null || suffixes.length == 0 )
               {
                  paths.add(child);
                  if( classPathHadVF == false )
                     classPathHadVF = child.equals(virtualFile);
                  if( trace )
                     log.trace("Added simple classpath entry: "+child);
                  // Process any Manifest Class-Path refs
                  VFSUtils.addManifestLocations(child, paths);
               }
               // Filter the immeadiate children against the suffixes
               else
               {
                  SuffixMatchFilter filter = new SuffixMatchFilter(Arrays.asList(suffixes), VisitorAttributes.DEFAULT);
                  List<VirtualFile> matches = child.getChildren(filter);
                  if( matches != null )
                  {
                     paths.addAll(matches);
                     if( trace )
                        log.trace("Added classpath matches: "+matches);
                     // Process any Manifest Class-Path refs
                     for(VirtualFile file : matches)
                     {
                        VFSUtils.addManifestLocations(file, paths);
                        if( classPathHadVF == false )
                           classPathHadVF = child.equals(virtualFile);
                     }
                  }
               }
            }
            catch(IOException e)
            {
               log.debug("Failed to find cp element: "+cp+", "+e.getMessage());
            }
         }
      }

      // If virtualFile was not already processed as part of the classpath
      if( classPathHadVF == false )
      {
         try
         {
            // Process any Manifest Class-Path refs on the context itself
            if( virtualFile.isLeaf() == false )
               VFSUtils.addManifestLocations(virtualFile, paths);
         }
         catch(IOException ignore)
         {
         }
      }
      // Set the classpath
      if( paths.size() > 0 )
         context.setClassPath(paths);
      // Add the context to the vfs path to context map
      contextMap.put(virtualFile.getPathName(), context);
   }

}
