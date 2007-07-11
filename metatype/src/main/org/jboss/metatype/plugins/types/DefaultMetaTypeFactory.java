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
package org.jboss.metatype.plugins.types;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.config.plugins.property.PropertyConfiguration;
import org.jboss.config.spi.Configuration;
import org.jboss.metatype.api.annotations.CompositeKey;
import org.jboss.metatype.api.annotations.Generic;
import org.jboss.metatype.api.types.ArrayMetaType;
import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.types.EnumMetaType;
import org.jboss.metatype.api.types.GenericMetaType;
import org.jboss.metatype.api.types.ImmutableCompositeMetaType;
import org.jboss.metatype.api.types.ImmutableTableMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.MetaTypeFactory;
import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.api.types.TableMetaType;
import org.jboss.metatype.spi.types.MetaTypeBuilder;
import org.jboss.reflect.spi.ArrayInfo;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.EnumConstantInfo;
import org.jboss.reflect.spi.EnumInfo;
import org.jboss.reflect.spi.InterfaceInfo;
import org.jboss.reflect.spi.TypeInfo;

/**
 * DefaultMetaTypeFactory.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class DefaultMetaTypeFactory extends MetaTypeFactory
{
   /** The map key */
   public static final String MAP_KEY = "key";

   /** The map value */
   public static final String MAP_VALUE = "value";
   
   /** Map index names */
   public static final String[] MAP_INDEX_NAMES = { MAP_KEY };
   
   /** Map item names */
   public static final String[] MAP_ITEM_NAMES = { MAP_KEY, MAP_VALUE };
   
   /** The configuration */
   private static Configuration configuration;
   
   static
   {
      configuration = AccessController.doPrivileged(new PrivilegedAction<Configuration>()
      {
         public Configuration run()
         {
            return new PropertyConfiguration();
         }
      });
   }

   /** The object type info */
   private TypeInfo objectTypeInfo = configuration.getTypeInfo(Object.class); 

   /** The builders */
   private Map<Class, WeakReference<MetaTypeBuilder>> builders = new WeakHashMap<Class, WeakReference<MetaTypeBuilder>>();

   /**
    * Create a new DefaultMetaTypeFactory.
    */
   public DefaultMetaTypeFactory()
   {
      // Special types
      setBuilder(Class.class, ClassMetaTypeBuilder.INSTANCE);
   }
   
   @Override
   public MetaType resolve(Type type)
   {
      TypeInfo typeInfo = configuration.getTypeInfo(type);
      return resolve(typeInfo);
   }
   
   /**
    * Resolve the meta type
    * 
    * @param typeInfo the type
    * @return the meta type
    */
   public MetaType resolve(TypeInfo typeInfo)
   {
      // Look for a cached value
      MetaType result = typeInfo.getAttachment(MetaType.class);
      if (result == null)
      {
         // Generate it
         result = generate(typeInfo);
         
         // Cache it
         typeInfo.setAttachment(MetaType.class.getName(), result);
      }
      
      // Return the result 
      return result;
   }
   
   public void setBuilder(Class<?> clazz, MetaTypeBuilder builder)
   {
      synchronized (builders)
      {
         if (builder == null)
            builders.remove(clazz);
         builders.put(clazz, new WeakReference<MetaTypeBuilder>(builder));
      }
   }

   /**
    * Generate the metatype
    * 
    * @param typeInfo the type info
    * @return the metatype
    */
   public MetaType generate(TypeInfo typeInfo)
   {
      MetaType result = isBuilder(typeInfo);
      if (result != null)
         return result;

      result = isGeneric(typeInfo);
      if (result != null)
         return result;
      
      if (typeInfo.isEnum())
         return generateEnum((EnumInfo) typeInfo);

      ClassInfo annotationType = isAnnotation(typeInfo);
      if (annotationType != null)
         return generateAnnotation(annotationType);
      
      if (typeInfo.isArray())
         return generateArray((ArrayInfo) typeInfo);
      
      if (typeInfo.isCollection())
         return generateCollection((ClassInfo) typeInfo);
      
      if (typeInfo.isMap())
         return generateMap((ClassInfo) typeInfo);
      
      result = SimpleMetaType.isSimpleType(typeInfo.getName());
      if (result != null)
         return result;
      
      return generateBean((ClassInfo) typeInfo);
   }
   
   /**
    * Generate an enum metatype
    * 
    * @param typeInfo the type info
    * @return the metatype
    */
   public EnumMetaType generateEnum(EnumInfo typeInfo)
   {
      EnumConstantInfo[] constants = typeInfo.getEnumConstants();
      List<String> validValues = new ArrayList<String>(constants.length);
      for (EnumConstantInfo constant : constants)
         validValues.add(constant.getName());
      return new EnumMetaType(typeInfo.getName(), validValues);
   }

   /**
    * Whether this type is an annotation
    * 
    * @param typeInfo the type info
    * @return the annotation type info
    */
   public ClassInfo isAnnotation(TypeInfo typeInfo)
   {
      if (typeInfo.isAnnotation())
         return (ClassInfo) typeInfo;
      
      if (typeInfo instanceof ClassInfo)
      {
         ClassInfo classInfo = (ClassInfo) typeInfo;
         InterfaceInfo[] interfaces = classInfo.getInterfaces();
         if (interfaces != null)
         {
            for (int i = 0; i < interfaces.length; ++i)
            {
               if (interfaces[i].isAnnotation())
                  return interfaces[i];
            }
         }
      }
      
      // Doesn't look like an annotation
      return null;
   }
   
   /**
    * Generate an annotation metatype
    * 
    * @param typeInfo the type info
    * @return the metatype
    */
   public CompositeMetaType generateAnnotation(ClassInfo typeInfo)
   {
      return generateBean(typeInfo);
   }
   
   /**
    * Generate an array metatype
    * 
    * @param typeInfo the type info
    * @return the metatype
    */
   public ArrayMetaType generateArray(ArrayInfo typeInfo)
   {
      int dimension = 1;
      TypeInfo componentType = typeInfo.getComponentType();
      while (componentType.isArray())
      {
         ++dimension;
         componentType = ((ArrayInfo) componentType).getComponentType();
      }
      MetaType componentMetaType = resolve(componentType);
      return new ArrayMetaType(dimension, componentMetaType);
   }
   
   /**
    * Generate a collection metatype
    * 
    * @param typeInfo the type info
    * @return the metatype
    */
   public ArrayMetaType generateCollection(ClassInfo typeInfo)
   {
      TypeInfo elementType = objectTypeInfo;
      
      TypeInfo[] types = typeInfo.getActualTypeArguments();
      if (types != null)
         elementType = types[0];
      
      MetaType elementMetaType = resolve(elementType);
      return new ArrayMetaType(1, elementMetaType);
   }
   
   /**
    * Generate a map metatype
    * 
    * @param typeInfo the type info
    * @return the metatype
    */
   public TableMetaType generateMap(ClassInfo typeInfo)
   {
      TypeInfo keyType = objectTypeInfo;
      TypeInfo valueType = objectTypeInfo;
      
      TypeInfo[] types = typeInfo.getActualTypeArguments();
      if (types != null)
      {
         keyType = types[0];
         valueType = types[1];
      }
      return createMapType(keyType, valueType);
   }
   
   /**
    * Create a map type
    * 
    * @param keyType the key type
    * @param valueType the value type
    * @return the map type
    */
   public TableMetaType createMapType(TypeInfo keyType, TypeInfo valueType)
   {
      String name = Map.class.getName();
      MetaType[] itemTypes = { resolve(keyType), resolve(valueType) };
      CompositeMetaType entryType = createMapEntryType(itemTypes);
      return new ImmutableTableMetaType(name, name, entryType, MAP_INDEX_NAMES);
   }
   
   /**
    * Create a map entry type
    * 
    * @param itemTypes the item types
    * @return the map entry type
    */
   public static CompositeMetaType createMapEntryType(MetaType[] itemTypes)
   {
      String entryName = Map.Entry.class.getName();
      return new ImmutableCompositeMetaType(entryName, entryName, MAP_ITEM_NAMES, MAP_ITEM_NAMES, itemTypes);
   }
   
   /**
    * Generate a bean metatype
    * 
    * @param typeInfo the type info
    * @return the metatype
    */
   public CompositeMetaType generateBean(ClassInfo typeInfo)
   {
      BeanInfo beanInfo = configuration.getBeanInfo(typeInfo);
      MutableCompositeMetaType result = new MutableCompositeMetaType(typeInfo.getName(), typeInfo.getName());
      typeInfo.setAttachment(MetaType.class.getName(), result);

      Set<String> keys = null;
      Set<PropertyInfo> properties = beanInfo.getProperties();
      if (properties != null && properties.size() > 0)
      {
         for (PropertyInfo property : properties)
         {
            String name = property.getName();
            if ("class".equals(name) == false)
            {
               TypeInfo itemTypeInfo = property.getType();
               MetaType metaType = resolve(itemTypeInfo);
               result.addItem(name, name, metaType);
               if (property.isAnnotationPresent(CompositeKey.class))
               {
                  if (keys == null)
                     keys = new LinkedHashSet<String>();
                  keys.add(name);
               }
            }
         }
      }
      if (keys != null)
         result.setKeys(keys);
      result.freeze();
      return result;
   }
   
   /**
    * Check for builders
    * 
    * @param typeInfo the type info
    * @return the meta type when it is special
    */
   public MetaType isBuilder(TypeInfo typeInfo)
   {
      MetaTypeBuilder builder = null;
      synchronized (builders)
      {
         WeakReference<MetaTypeBuilder> weak = builders.get(typeInfo.getType());
         if (weak != null)
            builder = weak.get();
      }
      if (builder == null)
         return null;
      return builder.buildMetaType();
   }
   
   /**
    * Check for generic
    * 
    * @param typeInfo the type info
    * @return the meta type when it is generic
    */
   public GenericMetaType isGeneric(TypeInfo typeInfo)
   {
      if (typeInfo instanceof ClassInfo == false)
         return null;
      
      ClassInfo classInfo = (ClassInfo) typeInfo;
      Generic generic = classInfo.getUnderlyingAnnotation(Generic.class);
      if (generic != null)
         return new GenericMetaType(typeInfo.getName(), typeInfo.getName());
      return null;
   }
}
