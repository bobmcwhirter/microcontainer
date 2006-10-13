package org.jboss.deployers.plugins.structure.vfs.explicit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.deployers.plugins.structure.AbstractDeploymentContext;
import org.jboss.deployers.plugins.structure.vfs.AbstractCandidateStructureVisitor;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.logging.Logger;
import org.jboss.virtual.VFSUtils;
import org.jboss.virtual.VirtualFile;
import org.jboss.virtual.VisitorAttributes;
import org.jboss.virtual.plugins.vfs.helpers.SuffixMatchFilter;

/**
 * A CandidateStructureVisitor that uses the explicit
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class StructureMetaDataFileVisitor extends AbstractCandidateStructureVisitor
{
   private static Logger log = Logger.getLogger(StructureMetaDataFileVisitor.class);
   private StructureMetaData metaData;
   private String parentPath;

   public StructureMetaDataFileVisitor(StructureMetaData metaData, DeploymentContext parent, VisitorAttributes attributes)
   {
      super(parent, attributes);
      this.metaData = metaData;
      VirtualFile parentFile = parent.getRoot();
      this.parentPath = parentFile.getPathName();
      ContextInfo info = metaData.getContext(parentPath);
      processContext(parent, info, parentFile);
   }

   protected DeploymentContext createCandidate(VirtualFile virtualFile)
   {
      DeploymentContext context = null;
      String path = virtualFile.getPathName();
      ContextInfo info = metaData.getContext(path);
      if( info != null && path.equals(parentPath) == false )
      {
         context = new AbstractDeploymentContext(virtualFile, true, getParent());
         processContext(context, info, virtualFile);
      }      
      
      return context;
   }

   protected void processContext(DeploymentContext context, ContextInfo info, VirtualFile virtualFile)
   {
      if( info == null )
         return;

      boolean trace = log.isTraceEnabled();
      if( trace )
         log.trace("Processing context: "+context+", info: "+info);
      context.setMetaDataPath(info.getMetaDataPath());
      ArrayList<VirtualFile> paths = new ArrayList<VirtualFile>();
      List<ContextInfo.Path> classPath = info.getClassPath();
      if( classPath != null )
      {
         for(ContextInfo.Path cp : classPath)
         {
            try
            {
               VirtualFile child = virtualFile.findChild(cp.getName());
               String[] suffixes = cp.getSuffixes();
               // Add the path if there is no suffix
               if( suffixes == null || suffixes.length == 0 )
               {
                  paths.add(child);
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

      try
      {
         // Process any Manifest Class-Path refs on the context itself
         VFSUtils.addManifestLocations(virtualFile, paths);
      }
      catch(IOException ignore)
      {
      }
      // Set the classpath
      if( paths.size() > 0 )
         context.setClassPath(paths);
   }
}
