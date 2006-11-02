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
package org.jboss.deployers.plugins.structure.vfs.jar;

import java.io.IOException;
import java.util.Set;

import org.jboss.deployers.plugins.structure.ContextInfoImpl;
import org.jboss.deployers.plugins.structure.vfs.AbstractStructureDeployer;
import org.jboss.deployers.spi.structure.vfs.StructureMetaData;
import org.jboss.deployers.spi.structure.vfs.StructuredDeployers;
import org.jboss.virtual.VirtualFile;
import org.jboss.virtual.plugins.context.jar.JarUtils;

/**
 * JARStructure.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class JARStructure extends AbstractStructureDeployer
{
   public JARStructure()
   {
      this(null);
   }

   /**
    * Sets the default relative order 10000.
    *
    */
   public JARStructure(Set<String> suffixes)
   {
      if( suffixes != null )
         JarUtils.setJarSuffixes(suffixes);
      setRelativeOrder(10000);
   }

   /**
    * Gets the set of suffixes recognised as jars
    * 
    * @return the set of suffixes
    */
   public Set<String> getSuffixes()
   {
      return JarUtils.getSuffixes();
   }
   /**
    * Gets the set of suffixes recognised as jars
    * 
    * @param suffixes - the set of suffixes
    */
   public void setSuffixes(Set<String> suffixes)
   {
      JarUtils.setJarSuffixes(suffixes);
   }

   public boolean determineStructure(VirtualFile root, StructureMetaData metaData, StructuredDeployers deployers)
   {
      String contextPath = null;
      try
      {
         if (root.isLeaf() == false)
         {
            // For non top level directories that don't look like jars
            // we require a META-INF otherwise each subdirectory would be a subdeployment
            if (JarUtils.isArchive(root.getName()) == false)
            {
               if (isTopLevel(root, metaData) == false)
               {
                  try
                  {
                     root.findChild("META-INF");
                     log.trace("... ok - non top level directory has a META-INF subdirectory");
                  }
                  catch (IOException e)
                  {
                     log.trace("... no - doesn't look like a jar and no META-INF subdirectory.");
                     return false;
                  }
               }
               else
               {
                  log.trace("... ok - doesn't look like a jar but it is a top level directory.");
               }
            }
         }
         else if(JarUtils.isArchive(root.getName()))
         {
            log.trace("... ok - its an archive or at least pretending to be.");
         }
         else
         {
            log.trace("... no - not a directory or an archive.");
            return false;
         }

         // Create a context for this jar file
         ContextInfoImpl context = new ContextInfoImpl(root.getPathName());
         // The metadata path is META-INF
         context.setMetaDataPath("META-INF");

         // The classpath is the root
         super.addClassPath(root, root, true, true, context);
         metaData.addContext(context);
         contextPath = context.getVfsPath();

         // We tentatively try all the children as potential subdeployments
         addAllChildren(root, metaData, deployers);
         return true;
      }
      catch (Exception e)
      {
         log.warn("Error determining structure: " + root.getName(), e);
         // Remove the invalid context
         if( contextPath != null )
            metaData.removeContext(contextPath);
         return false;
      }
   }
}
