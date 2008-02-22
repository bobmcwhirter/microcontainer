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
package org.jboss.metatype.spi.values;

import java.lang.reflect.Type;

import org.jboss.metatype.api.annotations.MetaMapping;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.MetaTypeFactory;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.ConstructorInfo;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.reflect.spi.TypeInfoFactory;

/**
 * MetaMapper.
 * 
 * @param <T> the mapped type
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class MetaMapper<T>
{
   /** The metatype factory */
   private static final MetaTypeFactory metaTypeFactory = MetaTypeFactory.getInstance();
   
   /**
    * Get a metamapper for a type info
    * 
    * @param typeInfo the type info
    * @return the metamapper or null if there isn't one
    */
   public static final MetaMapper<?> getMetaMapper(TypeInfo typeInfo)
   {
      if (typeInfo instanceof ClassInfo == false)
         return null;
      
      ClassInfo classInfo = (ClassInfo) typeInfo;
      MetaMapper<?> mapper = typeInfo.getAttachment(MetaMapper.class);
      if (mapper != null)
         return mapper;

      MetaMapping mapping = classInfo.getUnderlyingAnnotation(MetaMapping.class);
      if (mapping == null)
         return null;
      
      try
      {
         TypeInfoFactory factory = classInfo.getTypeInfoFactory();
         ClassInfo mapperClass = (ClassInfo) factory.getTypeInfo(mapping.value());
         ConstructorInfo constructor = mapperClass.getDeclaredConstructor(null);
         mapper = (MetaMapper<?>) constructor.newInstance(null);
         typeInfo.setAttachment(MetaMapper.class.getName(), mapper);
         return mapper;
      }
      catch (Error e)
      {
         throw e;
      }
      catch (RuntimeException e)
      {
         throw e;
      }
      catch (Throwable t)
      {
         throw new RuntimeException("Error creating MetaMapper " + mapping + " for " + typeInfo.getName());
      }
   }
   
   /**
    * Override this method to map to a specific type
    * 
    * @return the type
    */
   public Type mapToType()
   {
      return null;
   }
   
   /**
    * Override this method to create your own metatype
    * 
    * @return the metatype
    */
   public MetaType<?> getMetaType()
   {
      Type type = mapToType();
      if (type == null)
         throw new IllegalStateException("You haven't overridden getMetaType() or mapToType()");
      return metaTypeFactory.resolve(type);
   }
   
   /**
    * Create the meta value
    * 
    * @param metaType the metatype
    * @param object the object
    * @return the meta value
    */
   public abstract MetaValue createMetaValue(MetaType<?> metaType, T object);
   
   /**
    * Unwrap the metavalue
    * 
    * @param metaValue the meta value
    * @return the object
    */
   public abstract T unwrapMetaValue(MetaValue metaValue);
}
