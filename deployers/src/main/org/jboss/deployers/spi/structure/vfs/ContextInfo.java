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
package org.jboss.deployers.spi.structure.vfs;

import java.util.List;

/**
 * An encapsulation of deployment context information 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public interface ContextInfo
{
   /**
    * Get the context parent parent.
    * @return the parent if one exists, null otherwise
    */
   public ContextInfo getParent();
   /**
    * 
    * @param parent
    */
   public void setParent(ContextInfo parent);

   /**
    * Get the VFS path of the context
    * @return the VFS path of the context
    */
   public String getVfsPath();
   /**
    * Set the VFS path of the context
    * @param path - VFS path of the context
    */
   public void setVfsPath(String path);

   /**
    * Get the path of the metdata location.
    * @return the path of the metdata location.
    */
   public String getMetaDataPath();
   /**
    * Set the metadata path of the context.
    * @param metaDataPath - the path relative to root of the context
    */
   public void setMetaDataPath(String metaDataPath);

   /**
    * Get the deployment context classpath
    * @return the possibly null context classpath
    */
   public List<ClassPathInfo> getClassPath();
   /**
    * Set the deployment context classpath
    * @param classPath - the context classpath
    */
   public void setClassPath(List<ClassPathInfo> classPath);
}
