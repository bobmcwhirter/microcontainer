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
package org.jboss.deployers.spi.structure;

import java.io.Serializable;
import java.util.List;

import org.jboss.deployers.spi.attachments.ManagedObjectAttachments;
import org.jboss.deployers.spi.attachments.PredeterminedManagedObjectAttachments;

/**
 * An encapsulation of context information
 *  
 * @author Scott.Stark@jboss.org
 * @author adrian@jboss.org
 * @version $Revision: 1.1$
 */
public interface ContextInfo extends ManagedObjectAttachments, Serializable
{
   /** The default metadata path */
   String DEFAULT_METADATA_PATH = "META-INF";
   
   /**
    * Get the logical path of the context
    * 
    * @return the logical path
    */
   String getPath();

   /**
    * Get the logical path of the metdata location.
    * 
    * @return the path of the metdata location.
    */
   String getMetaDataPath();

   /**
    * Get the classpath locations within the context
    * 
    * @return the possibly null context classpath
    */
   List<ClassPathEntry> getClassPath();
   
   /**
    * Add a class path entry
    * 
    * @param entry the entry to add
    * @throws IllegalArgumentException for a null entry
    */
   void addClassPathEntry(ClassPathEntry entry);
}
