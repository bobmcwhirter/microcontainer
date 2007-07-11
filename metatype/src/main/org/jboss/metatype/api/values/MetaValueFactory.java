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
package org.jboss.metatype.api.values;

import java.lang.reflect.Type;

import org.jboss.metatype.plugins.values.MetaValueFactoryBuilder;
import org.jboss.metatype.spi.values.MetaValueBuilder;
import org.jboss.reflect.spi.TypeInfo;

/**
 * MetaValueFactory.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class MetaValueFactory
{
   /** The singleton builder */
   private static final MetaValueFactoryBuilder builder = new MetaValueFactoryBuilder();

   /**
    * Get the metatype factory
    * 
    * @return the instance
    */
   public static final MetaValueFactory getInstance()
   {
      return builder.create();
   }
   
   /**
    * Create a meta value
    * 
    * @param value the value
    * @return the meta value
    */
   public abstract MetaValue create(Object value);
   
   /**
    * Create a meta value
    * 
    * @param value the value
    * @param type the type
    * @return the meta value
    */
   public abstract MetaValue create(Object value, Type type);
   
   /**
    * Create a meta value
    * 
    * @param value the value
    * @param type the type
    * @return the meta value
    */
   public abstract MetaValue create(Object value, TypeInfo type);
   
   /**
    * Set a meta value builder.
    * 
    * @param clazz the class
    * @param builder the builder
    */
   public abstract void setBuilder(Class<?> clazz, MetaValueBuilder builder);
}
