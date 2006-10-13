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
package org.jboss.deployers.plugins.structure.vfs.explicit;

import java.util.ArrayList;
import java.util.List;

/**
 * Structure info for a DeploymentContext.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class ContextInfo
{
   static class Path
   {
      private String name;
      private String[] suffixes = {};
      Path(String name, String suffixes)
      {
         this.name = name;
         if( suffixes != null )
            this.suffixes = suffixes.split(",");
      }
      public String getName()
      {
         return name;
      }
      public String[] getSuffixes()
      {
         return suffixes;
      }
   }

   /** The relative VFS path */
   private String vfsPath;
   /** The optional context classpath */
   private ArrayList<Path> classPath;
   /** The optional context metadata path */
   private String metaDataPath;

   public List<Path> getClassPath()
   {
      return classPath;
   }
   public void setClassPath(List<Path> classPath)
   {
      if( this.classPath == null )
         this.classPath = new ArrayList<Path>();
      this.classPath.clear();
      this.classPath.addAll(classPath);
   }
   public String getMetaDataPath()
   {
      return metaDataPath;
   }
   public void setMetaDataPath(String metaDataPath)
   {
      this.metaDataPath = metaDataPath;
   }
   public String getVfsPath()
   {
      return vfsPath;
   }
   public void setVfsPath(String path)
   {
      this.vfsPath = path;
   }
}
