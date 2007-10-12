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
package org.jboss.managed.api.factory;

import java.io.Serializable;

import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.plugins.factory.ManagedObjectFactoryBuilder;
import org.jboss.managed.spi.factory.InstanceClassFactory;
import org.jboss.managed.spi.factory.ManagedObjectBuilder;

/**
 * ManagedObjectFactory.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class ManagedObjectFactory
{
   /** The managed object factory instance */
   private static ManagedObjectFactoryBuilder builder = new ManagedObjectFactoryBuilder();
   
   /**
    * Get the managed object factory instance
    * 
    * @return the instance
    */
   public static ManagedObjectFactory getInstance()
   {
      return builder.create();
   }
   
   /**
    * Create a managed object from the given object
    * 
    * @param object the object
    * @param name - the name of the managed object. If null, the name will
    *    be derived from the object annotations or attachment name.
    * @param type - the name of the managed object. If null, the name will
    *    be derived from the object annotations or default to "".
    * 
    * @see ManagementObjectID
    * 
    * @return the managed object
    * @throws IllegalArgumentException for a null object
    */
   public abstract ManagedObject initManagedObject(Serializable object, String name, String nameType);

   /**
    * Create a shell managed object from the given class
    *
    * @param <T> the class
    * @param clazz the class
    * @return the managed object
    * @throws IllegalArgumentException for a null class
    */
   public abstract <T extends Serializable> ManagedObject createManagedObject(Class<T> clazz);

   /**
    * Set a managed object builder
    * 
    * @param clazz the class
    * @param builder the builder (null to remove the builder)
    */
   public abstract void setBuilder(Class<?> clazz, ManagedObjectBuilder builder);

   /**
    * Set the InstanceClassFactory for an instance type.
    *
    * @param <T> the class type
    * @param clazz the class
    * @param factory - the factory used to obtain the class to scan for
    * management annotations.
    */
   public abstract <T extends Serializable> void setInstanceClassFactory(Class<T> clazz, InstanceClassFactory<T> factory);

   /**
    * Get the InstanceClassFactory for an instance type.
    *
    * @param <T> the class type
    * @param clazz the class
    * @return the factory used to obtain the class to scan for
    * management annotations. 
    */
   public abstract <T extends Serializable> InstanceClassFactory<T> getInstanceClassFactory(Class<T> clazz);
}
