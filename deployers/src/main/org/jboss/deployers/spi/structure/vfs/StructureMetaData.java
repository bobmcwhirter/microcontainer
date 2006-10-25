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

import java.util.SortedSet;

/**
 * A map of vfs paths to deployment context information.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public interface StructureMetaData
{
   /**
    * Add a deployment context info. The context must have a vfs path, and its
    * parent must already have been added if it has not been set on the context.
    * 
    * @param context - the context info
    */
   public void addContext(ContextInfo context);
   /**
    * Get a context based on its vfs path.
    * @param vfsPath - the VFS path name
    * @return the ContextInfo if one exists for vfsPath.
    */
   public ContextInfo getContext(String vfsPath);
   /**
    * Remove a context based on its vfs path.
    * @param vfsPath - the VFS path name
    * @return the ContextInfo if one exists for vfsPath.
    */
   public ContextInfo removeContext(String vfsPath);
   /**
    * Get the deployment context info ordered from parent to child. This must
    * order parent contexts before their children.
    * @return deployment context info ordered from parent to child.
    */
   public SortedSet<ContextInfo> getContexts();
}
