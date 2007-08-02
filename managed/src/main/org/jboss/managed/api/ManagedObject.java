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
import java.util.Set;

import org.jboss.metatype.api.types.Name;

/**
 * ManagedObject.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public interface ManagedObject extends Serializable
{
   /**
    * Get the attachment name
    * 
    * @return the name
    */
   String getName();

   /**
    * Get the external name by which the ManagedObject
    * @see {@linkplain ManagedObjectRegistry}}
    * @return
    */
   Name getExternalName();

   /**
    * Get the underlying object
    * 
    * @return the underlying object
    */
   Serializable getAttachment();

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
   Set<ManagedProperty> getProperties();

   /**
    * Get the operations
    * 
    * @return the operations
    */
   Set<ManagedOperation> getOperations();
}
