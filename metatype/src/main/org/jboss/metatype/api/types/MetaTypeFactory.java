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
package org.jboss.metatype.api.types;

import java.lang.reflect.Type;

import org.jboss.metatype.plugins.types.MetaTypeFactoryBuilder;
import org.jboss.metatype.spi.types.MetaTypeBuilder;
import org.jboss.reflect.spi.TypeInfo;

/**
 * MetaTypeFactory.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class MetaTypeFactory
{
   /** The singleton builder */
   private static final MetaTypeFactoryBuilder builder = new MetaTypeFactoryBuilder();

   /**
    * Get the metatype factory
    * 
    * @return the instance
    */
   public static final MetaTypeFactory getInstance()
   {
      return builder.create();
   }
   
   /**
    * Resolve a metatype
    * 
    * @param type the type
    * @return the metatype
    * @throws IllegalArgumentException for a null type
    */
   public abstract MetaType resolve(Type type);
   
   /**
    * Resolve a metatype
    * 
    * @param type the type
    * @return the metatype
    * @throws IllegalArgumentException for a null type
    */
   public abstract MetaType resolve(TypeInfo type);
   
   /**
    * Resolve a metatype
    * 
    * @param className the class name
    * @param classLoader the classloader
    * @return the metatype
    * @throws IllegalArgumentException for a null className or classloader
    * @throws ClassNotFoundException when the class is not found 
    */
   public MetaType resolve(String className, ClassLoader classLoader) throws ClassNotFoundException
   {
      if (className == null)
         throw new IllegalArgumentException("Null className");
      if (classLoader == null)
         throw new IllegalArgumentException("Null classLoader");
      Class clazz = classLoader.loadClass(className);
      return resolve(clazz);
   }
   
   /**
    * Set a meta type builder
    * 
    * @param clazz the class
    * @param builder the builder
    */
   public abstract void setBuilder(Class<?> clazz, MetaTypeBuilder builder);
}
