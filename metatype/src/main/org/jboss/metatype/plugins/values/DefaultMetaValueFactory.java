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
package org.jboss.metatype.plugins.values;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.WeakHashMap;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.config.plugins.property.PropertyConfiguration;
import org.jboss.config.spi.Configuration;
import org.jboss.metatype.api.types.ArrayMetaType;
import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.types.EnumMetaType;
import org.jboss.metatype.api.types.GenericMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.MetaTypeFactory;
import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.api.types.TableMetaType;
import org.jboss.metatype.api.values.ArrayValue;
import org.jboss.metatype.api.values.ArrayValueSupport;
import org.jboss.metatype.api.values.CompositeValue;
import org.jboss.metatype.api.values.CompositeValueSupport;
import org.jboss.metatype.api.values.EnumValue;
import org.jboss.metatype.api.values.EnumValueSupport;
import org.jboss.metatype.api.values.GenericValue;
import org.jboss.metatype.api.values.GenericValueSupport;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.MetaValueFactory;
import org.jboss.metatype.api.values.SimpleValue;
import org.jboss.metatype.api.values.SimpleValueSupport;
import org.jboss.metatype.api.values.TableValue;
import org.jboss.metatype.api.values.TableValueSupport;
import org.jboss.metatype.plugins.types.DefaultMetaTypeFactory;
import org.jboss.metatype.spi.values.MetaValueBuilder;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.TypeInfo;

/**
 * DefaultMetaValueFactory.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class DefaultMetaValueFactory extends MetaValueFactory
{
   /** The metatype factory */
   private MetaTypeFactory metaTypeFactory = MetaTypeFactory.getInstance();
   
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

   /** The thread local stack of objects mapped to meta values */
   private ThreadLocal<Stack<Map<Object, MetaValue>>> mappingStack = new ThreadLocal<Stack<Map<Object, MetaValue>>>()
   {
      protected Stack<Map<Object, MetaValue>> initialValue()
      {
         return new Stack<Map<Object, MetaValue>>();
      }
      
   };

   /** The builders */
   private Map<Class, WeakReference<MetaValueBuilder>> builders = new WeakHashMap<Class, WeakReference<MetaValueBuilder>>();
 
   public void setBuilder(Class<?> clazz, MetaValueBuilder builder)
   {
      synchronized (builders)
      {
         if (builder == null)
            builders.remove(clazz);
         builders.put(clazz, new WeakReference<MetaValueBuilder>(builder));
      }
   }
   
   /**
    * Create a simple value
    * 
    * @param <T> the simple type
    * @param type the type
    * @param value the value
    * @return the simple value
    */
   public static <T extends Serializable> SimpleValue<T> createSimpleValue(SimpleMetaType<T> type, T value)
   {
      if (value == null)
         return null;

      return new SimpleValueSupport<T>(type, value);
   }

   /**
    * Create an enum value
    * 
    * @param <T> the enum type
    * @param type the type
    * @param value the value
    * @return the enum value
    */
   public static <T extends Enum> EnumValue createEnumValue(EnumMetaType type, T value)
   {
      if (value == null)
         return null;

      return new EnumValueSupport(type, value.name());
   }

   /**
    * Create a generic value
    * 
    * @param type the type
    * @param value the value
    * @param mapping the mapping
    * @return the enum value
    */
   public static GenericValue createGenericValue(GenericMetaType type, Object value, Map<Object, MetaValue> mapping)
   {
      if (value == null)
         return null;
      
      if (value instanceof Serializable == false)
         throw new IllegalArgumentException("Not serializable: " + value.getClass().getName());

      GenericValue result = new GenericValueSupport(type, (Serializable) value);
      mapping.put(value, result);
      return result;
   }

   /**
    * Create an array value
    * 
    * @param type the type
    * @param value the value
    * @param mapping the mapping
    * @return the composite value
    */
   public ArrayValue createArrayValue(ArrayMetaType type, Object value, Map<Object, MetaValue> mapping)
   {
      if (value == null)
         return null;

      ArrayValueSupport result = new ArrayValueSupport(type);
      mapping.put(value, result);
      
      Object[] array = null;
      
      MetaType elementType = type.getElementType();
      int dimension = type.getDimension();
      
      Object[] oldArray = null;
      Class<?> componentType;
      try
      {
         componentType = Class.forName(type.getClassName());
      }
      catch (Exception e)
      {
         throw new RuntimeException("Unable to determine component type for " + type, e);
      }
      
      ClassInfo classInfo = configuration.getClassInfo(value.getClass());
      if (classInfo.isArray())
         oldArray = (Object[]) value;
      else if (classInfo.isCollection())
      {
         Collection c = (Collection) value;
         oldArray = c.toArray();
      }
      else
         throw new UnsupportedOperationException("Cannot construct array for " + value.getClass());
      
      array = createArray(elementType, componentType.getComponentType(), dimension, oldArray);
      result.setValue(array);
      return result;
   }

   /**
    * Create an array
    * 
    * @param elementType the element type
    * @param componentType the component type
    * @param dimension the dimension
    * @param oldArray the old array
    * @return the array
    */
   protected Object[] createArray(MetaType elementType, Class<?> componentType, int dimension, Object[] oldArray)
   {
      if (oldArray == null)
         return null;
      
      Object[] newArray = new Object[oldArray.length];
      
      if (dimension > 1)
      {
         for (int i = 0; i < oldArray.length; ++i)
         {
            Object[] nestedOld = (Object[]) oldArray[i];
            Object[] result = createArray(elementType, componentType.getComponentType(), dimension-1, nestedOld);
            newArray[i] = result;
         }
      }
      else
      {
         for (int i = 0; i < oldArray.length; ++i)
            newArray[i] = internalCreate(oldArray[i], null, elementType);
      }
      
      return newArray;
   }
   
   /**
    * Create a composite value
    * 
    * @param type the type
    * @param value the value
    * @param mapping the mapping
    * @return the composite value
    */
   public CompositeValue createCompositeValue(CompositeMetaType type, Object value, Map<Object, MetaValue> mapping)
   {
      if (value == null)
         return null;
      
      CompositeValueSupport result = new CompositeValueSupport(type);
      mapping.put(value, result);

      
      BeanInfo beanInfo;
      try
      {
         ClassLoader cl = value.getClass().getClassLoader();
         if (cl == null)
            beanInfo = configuration.getBeanInfo(value.getClass());
         else
            beanInfo = configuration.getBeanInfo(type.getTypeName(), cl);
      }
      catch (Exception e)
      {
         throw new RuntimeException("Error retrieving BeanInfo for " + type);
      }

      for (String name : type.keySet())
      {
         MetaType itemType = type.getType(name);
         Object itemValue = null;
         try
         {
            itemValue = beanInfo.getProperty(value, name);
         }
         catch (RuntimeException e)
         {
            throw e;
         }
         catch (Error e)
         {
            throw e;
         }
         catch (Throwable t)
         {
            throw new RuntimeException("Error getting property: " + name + " for " + value.getClass(), t);
         }

         MetaValue item = internalCreate(itemValue, null, itemType);
         result.set(name, item);
      }
      
      return result;
   }

   /**
    * Create a table value
    * 
    * @param type the type
    * @param value the value
    * @param mapping the mapping
    * @return the table value
    */
   @SuppressWarnings("unchecked")
   public TableValue createTableValue(TableMetaType type, Map value, Map<Object, MetaValue> mapping)
   {
      if (value == null)
         return null;
      
      TableValueSupport table = new TableValueSupport(type);
      mapping.put(value, table);
      
      CompositeMetaType entryType = type.getRowType();
      MetaType keyType = entryType.getType(DefaultMetaTypeFactory.MAP_KEY);
      MetaType valType = entryType.getType(DefaultMetaTypeFactory.MAP_VALUE);
      
      for (Iterator<Map.Entry> i = value.entrySet().iterator(); i.hasNext();)
      {
         Map.Entry entry = i.next();
         MetaValue key = internalCreate(entry.getKey(), null, keyType);
         MetaValue val = internalCreate(entry.getValue(), null, valType);
         CompositeValueSupport data = new CompositeValueSupport(entryType, DefaultMetaTypeFactory.MAP_ITEM_NAMES, new MetaValue[] { key, val });
         table.put(data);
      }
      
      return table;
   }
   
   @Override
   public MetaValue create(Object value)
   {
      return internalCreate(value, null, null);
   }
   
   @Override
   public MetaValue create(Object value, Type type)
   {
      TypeInfo typeInfo = configuration.getTypeInfo(type);
      return internalCreate(value, typeInfo, null);
   }
   
   @Override
   public MetaValue create(Object value, TypeInfo type)
   {
      return internalCreate(value, type, null);
   }
   
   /**
    * Create a meta value from the object
    * 
    * @param value the value
    * @param type the type
    * @param metaType the metaType
    * @return the meta value
    */
   protected MetaValue internalCreate(Object value, TypeInfo type, MetaType metaType)
   {
      if (value == null)
         return null;

      if (type == null)
         type = configuration.getTypeInfo(value.getClass());
      
      boolean start = (metaType == null);
      if (metaType == null)
      {
         metaType = metaTypeFactory.resolve(type);
      }
      
      // For more complicated values we need to keep a mapping of objects to meta values
      // this avoids duplicate meta value construction and recursion 
      Map<Object, MetaValue> mapping = null;
      if (start)
      {
         // This is the start of the mapping
         mapping = new HashMap<Object, MetaValue>();
         mappingStack.get().push(mapping);
      }
      else
      {
         // Check the existing mapping
         mapping = mappingStack.get().peek();
         MetaValue result = mapping.get(value);
         // Seen this before
         if (result != null)
            return result;
      }

      try
      {
         MetaValue result = isBuilder(metaType, type, value, mapping);
         if (result == null)
         {
            if (metaType.isSimple())
               result = createSimpleValue((SimpleMetaType<Serializable>) metaType, (Serializable) value);
            else if (metaType.isEnum())
               result = createEnumValue((EnumMetaType) metaType, (Enum) value);
            else if (metaType.isArray())
               result = createArrayValue((ArrayMetaType) metaType, value, mapping);
            else if (metaType.isComposite())
               result = createCompositeValue((CompositeMetaType) metaType, value, mapping);
            else if (metaType.isTable())
               result = createTableValue((TableMetaType) metaType, (Map) value, mapping);
            else if (metaType.isGeneric())
               result = createGenericValue((GenericMetaType) metaType, value, mapping);
            else 
               throw new IllegalStateException("Unknown metaType: " + metaType);
         }
         return result;
      }
      finally
      {
         // Remove the mapping from the stack
         if (start)
            mappingStack.get().pop();
      }
   }
   
   /**
    * Check for a builder
    * 
    * @param metaType the meta type
    * @param type the type
    * @param value the value
    * @param mapping the mappings
    * @return the meta value
    */
   @SuppressWarnings("unchecked")
   protected MetaValue isBuilder(MetaType metaType, TypeInfo type, Object value, Map<Object, MetaValue> mapping)
   {
      MetaValueBuilder builder = null;
      synchronized (builders)
      {
         WeakReference<MetaValueBuilder> weak = builders.get(type.getType());
         if (weak != null)
            builder = weak.get();
      }
      if (builder == null)
         return null;
      MetaValue result = builder.buildMetaValue(metaType, value);
      if (result != null)
         mapping.put(value, result);
      
      return result;
   }
}