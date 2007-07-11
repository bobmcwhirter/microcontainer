/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.managed.spi.factory;

import java.io.Serializable;

import org.jboss.managed.api.ManagedObject;

/**
 * ManagedObjectPopulator.
 * 
 * @param <T> the underlying object
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public interface ManagedObjectPopulator<T extends Serializable>
{
   /**
    * Create a new underlying object
    * 
    * @param managedObject the managed object
    * @param clazz the class
    */
   void createObject(ManagedObject managedObject, Class<? extends Serializable> clazz);

   /**
    * Populate the managed object
    * 
    * @param managedObject the managed object
    * @param object the object used to populate the managed object
    */
   void populateManagedObject(ManagedObject managedObject, T object);
}
