/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.kernel.spi.registry;

import org.jboss.kernel.spi.KernelObject;

/**
 * A registry.<p>
 * 
 * The registry is responsible for keeping track of named
 * beans so they can be invoked over the bus and used in dependencies.<p>
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="mailto:les.hazlewood@jboss.org">Les A. Hazlewood</a>
 * @version $Revision$
 */
public interface KernelRegistry extends KernelObject
{
   /** Event type for registered event */
   public static final String KERNEL_REGISTRY_REGISTERED = "KERNEL_REGISTRY_REGISTERED";

   /** Event type for unregistered event */
   public static final String KERNEL_REGISTRY_UNREGISTERED = "KERNEL_REGISTRY_UNREGISTERED";
   
   /**
    * Get a registration
    * 
    * @param name the name of the object
    * @return the registration
    * @throws IllegalArgumentException for a null name
    * @throws KernelRegistryEntryNotFoundException when not found
    */
   KernelRegistryEntry getEntry(Object name);
 
   /**
    * Register an object
    * 
    * @param name the name of the object
    * @param entry the registration
    * @throws IllegalArgumentException for a null name or object
    * @throws IllegalStateException when the name is already registered
    */
   void registerEntry(Object name, KernelRegistryEntry entry);
   
   /**
    * Unregister an object
    * 
    * @param name the name of the object
    * @return the unregistered entry
    * @throws IllegalArgumentException for a null name
    * @throws IllegalStateException when the name is not registered
    */
   KernelRegistryEntry unregisterEntry(Object name);

   /**
    * Returns whether or not there exists a KernelRegistryEntry with the
    * given name.
    * @param name name of the KernelRegistryEntry
    * @return true if there exists a KernelRegistryEntry with the given name,
    *         false otherwise.
    */
   boolean containsEntry( Object name );
}
