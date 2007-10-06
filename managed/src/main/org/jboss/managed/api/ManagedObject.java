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
package org.jboss.managed.api;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * ManagedObject is an interface for a manageable element. It
 * consists of:
 * - a name/name type for a registry/references
 * - an attachment name to associate the ManagedObject with a
 *    deployment attachment
 * - annotations from the metadata making up the ManagedObject
 * - the attachment instance
 * - the ManagedPropertys for the interface
 * - the ManagedOperations for the interface 
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1.1 $
 */
public interface ManagedObject extends ManagedCommon
{
   /**
    * Get the underlying object
    * 
    * @return the underlying object
    */
   Serializable getAttachment();

   /**
    * Get the annotations associated with the property
    * @return the annotations associated with the property
    */
   public Map<String, Annotation> getAnnotations();

   /**
    * Get the runtime component name.
    *
    * @see {@linkplain ManagementRuntimeRef}
    * @return name of runtime component if one exists, null if no component exists.
    */
   Object getComponentName();
}
