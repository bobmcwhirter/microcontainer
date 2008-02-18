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
import java.util.Map;
import java.util.Set;

import org.jboss.managed.api.annotation.ManagementObject;
import org.jboss.managed.api.annotation.ManagementObjectID;

/**
 * ManagedCommon is used to hold common
 * properties that both, server side and client side,
 * object can see                                    .
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public interface ManagedCommon extends Serializable
{
   /**
    * Get the attachment name
    * @see {@linkplain ManagementObject#attachmentName}}
    *
    * @return the name
    */
   String getAttachmentName();

   /**
    * Get the external name by which the ManagedObject is known
    * @see {@linkplain ManagementObject#name}}
    * @see {@linkplain ManagementObjectID#name}}
    * @return the name
    */
   String getName();
   /**
    * Get the external name type/qualifier.
    * @see {@linkplain ManagementObject#type}
    * @see {@linkplain ManagementObjectID#type}
    * @return the name type
    */
   String getNameType();

   /**
    * Get the property names
    *
    * @return the property names
    */
   Set<String> getPropertyNames();

   /**
    * Get a property
    *
    * @param name the name
    * @return the property
    */
   ManagedProperty getProperty(String name);

   /**
    * Get the properties
    *
    * @return the properties
    */
   Map<String, ManagedProperty> getProperties();

   /**
    * Get the operations
    *
    * @return the operations
    */
   Set<ManagedOperation> getOperations();
}
