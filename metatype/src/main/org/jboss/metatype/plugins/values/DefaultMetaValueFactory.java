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
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.WeakHashMap;
import java.util.Map.Entry;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.config.plugins.property.PropertyConfiguration;
import org.jboss.config.spi.Configuration;
import org.jboss.metatype.api.types.ArrayMetaType;
import org.jboss.metatype.api.types.CollectionMetaType;
import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.types.EnumMetaType;
import org.jboss.metatype.api.types.GenericMetaType;
import org.jboss.metatype.api.types.MapCompositeMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.MetaTypeFactory;
import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.api.types.TableMetaType;
import org.jboss.metatype.api.values.ArrayValue;
import org.jboss.metatype.api.values.ArrayValueSupport;
import org.jboss.metatype.api.values.CollectionValue;
import org.jboss.metatype.api.values.CollectionValueSupport;
import org.jboss.metatype.api.values.CompositeValue;
import org.jboss.metatype.api.values.CompositeValueSupport;
import org.jboss.metatype.api.values.EnumValue;
import org.jboss.metatype.api.values.EnumValueSupport;
import org.jboss.metatype.api.values.GenericValue;
import org.jboss.metatype.api.values.GenericValueSupport;
import org.jboss.metatype.api.values.InstanceFactory;
import org.jboss.metatype.api.values.MapCompositeValueSupport;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.MetaValueFactory;
import org.jboss.metatype.api.values.SimpleValue;
import org.jboss.metatype.api.values.SimpleValueSupport;
import org.jboss.metatype.api.values.TableValue;
import org.jboss.metatype.api.values.TableValueSupport;
import org.jboss.metatype.plugins.types.DefaultMetaTypeFactory;
import org.jboss.metatype.spi.values.MetaMapper;
import org.jboss.metatype.spi.values.MetaValueBuilder;
import org.jboss.reflect.plugins.introspection.ParameterizedClassInfo;
import org.jboss.reflect.spi.ArrayInfo;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.reflect.spi.TypeInfoFactory;

/**
 * DefaultMetaValueFactory.
 * 
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
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
   private Map<Class<?>, WeakReference<MetaValueBuilder<?>>> builders = new WeakHashMap<Class<?>, WeakReference<MetaValueBuilder<?>>>();
 
   /** The Object type info */
   private static final TypeInfo OBJECT_TYPE_INFO = configuration.getTypeInfo(Object.class);

   /** The instance factory builders */
   private Map<Class<?>, InstanceFactory<?>> instanceFactoryMap = new WeakHashMap<Class<?>, InstanceFactory<?>>();

   public DefaultMetaValueFactory()
   {
      // set default collection instance factories
      setInstanceFactory(List.class, ListInstanceFactory.INSTANCE);
      setInstanceFactory(Set.class, SetInstanceFactory.INSTANCE);
      setInstanceFactory(SortedSet.class, SortedSetInstanceFactory.INSTANCE);
   }

   public void setBuilder(Class<?> clazz, MetaValueBuilder<?> builder)
   {
      synchronized (builders)
      {
         if (builder == null)
            builders.remove(clazz);
         builders.put(clazz, new WeakReference<MetaValueBuilder<?>>(builder));
      }
   }

   public <T> void setInstanceFactory(Class<T> clazz, InstanceFactory<T> factory)
   {
      synchronized(instanceFactoryMap)
      {
         if (factory == null)
            instanceFactoryMap.remove(clazz);
         else
            instanceFactoryMap.put(clazz, factory);
      }
   }

   /**
    * Create a simple value
    * 
    * @param type the type
    * @param value the value
    * @return the simple value
    */
   public static SimpleValue createSimpleValue(SimpleMetaType type, Serializable value)
   {
      if (value == null)
         return null;

      return new SimpleValueSupport(type, value);
   }

   /**
    * Create an enum value
    * 
    * @param <T> the enum type
    * @param type the type
    * @param value the value
    * @return the enum value
    */
   public static <T extends Enum<?>> EnumValue createEnumValue(EnumMetaType type, T value)
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
    * @return the generic value
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
    * Create a collection value
    *
    * @param type the type
    * @param value the value
    * @param mapping the mapping
    * @return the generic value
    */
   public CollectionValue createCollectionValue(CollectionMetaType type, Object value, Map<Object, MetaValue> mapping)
   {
      if (value == null)
         return null;

      Collection<?> collection = (Collection<?>)value;
      MetaValue[] elements = new MetaValue[collection.size()];
      int i = 0;
      for(Object ce : collection)
      {
         // recalculate element info, since usually more deterministic
         TypeInfo typeInfo = configuration.getTypeInfo(ce.getClass());
         MetaType metaType = metaTypeFactory.resolve(typeInfo);
         elements[i++] = internalCreate(ce, typeInfo, metaType);             
      }
      CollectionValue result = new CollectionValueSupport(type, elements);
      mapping.put(value, result);
      return result;
   }

   /**
    * Transform a primitive array into an Object[]. Converts
    * a primitive array like char[] to Object[]. 
    * 
    * @param type - the primitive array class type info.
    * @param value - the primitive array instance.
    * @return object array
    */
   public static Object[] convertPrimativeArray(TypeInfo type, Object value)
   {
      if (value == null)
         return null;

      Object[] oa;
      if( type instanceof ArrayInfo )
      {
         // Get the Object form of the element
         ArrayInfo arrayInfo = ArrayInfo.class.cast(type);
         TypeInfo etype = arrayInfo.getComponentType();
         int size = Array.getLength(value);
         oa = new Object[size];
         for(int n = 0; n < size; n ++)
         {
            Object nvalue = Array.get(value, n);
            // Recursively convert nested array elements
            if (etype.isArray())
            {
               oa[n] = convertPrimativeArray(etype, nvalue);
            }
            oa[n] = nvalue;
         }
      }
      else
      {
         oa = (Object[]) value;
      }
      
      return oa;      
   }

   /**
    * Transform a primitive array into an Object[]. Converts
    * a primitive array like char[] to Object[].
    *
    * @param value - the primitive array instance.
    * @return object array
    */
   public static Object[] convertPrimativeArray(Object value)
   {
      if (value == null)
         return null;
      return convertPrimativeArray(configuration.getTypeInfo(value.getClass()), value);
   }

   /**
    * Create an array value
    * 
    * @param type the type
    * @param value the value
    * @param mapping the mapping
    * @return the composite value
    */
   @SuppressWarnings("unchecked")
   public ArrayValue createArrayValue(ArrayMetaType type, Object value, Map<Object, MetaValue> mapping)
   {
      if (value == null)
         return null;

      ArrayValueSupport result = new ArrayValueSupport(type);
      mapping.put(value, result);
      
      Object[] array;
      
      MetaType elementType = type.getElementType();
      int dimension = type.getDimension();
      
      Object[] oldArray;
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
      {
         // See if this is a primitive array
         ArrayInfo arrayInfo = ArrayInfo.class.cast(classInfo);
         TypeInfo compInfo = arrayInfo.getComponentType();
         while(compInfo instanceof ArrayInfo)
         {
            arrayInfo = ArrayInfo.class.cast(compInfo);
            compInfo = arrayInfo.getComponentType();
         }
         // Translate 
         if (compInfo.isPrimitive())
            oldArray = convertPrimativeArray(classInfo, value);
         else
            oldArray = (Object[]) value;
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
         Object[] nestedOld;
         for (int i = 0; i < oldArray.length; ++i)
         {
            if ( !(oldArray[i] instanceof Object[]) )
               nestedOld = convertPrimativeArray(oldArray[i]);
            else
               nestedOld = (Object[]) oldArray[i];
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
      
      // See if this is a Map<String,?> type
      if(type instanceof MapCompositeMetaType)
      {
         if((value instanceof Map) == false)
            throw new RuntimeException("Expected Map value for: " + type+", was: "+(value != null ? value.getClass() : "null"));
         Map<String,?> map = (Map<String,?>) value;
         MapCompositeMetaType mapType = (MapCompositeMetaType) type;
         MetaType mapValueType = mapType.getValueType();
         MapCompositeValueSupport result = new MapCompositeValueSupport(mapValueType);
         for(Entry<String,?> entry : map.entrySet())
         {
            Object entryValue = entry.getValue();
            MetaValue entryMetaValue = internalCreate(entryValue, null, mapValueType);
            result.put(entry.getKey(), entryMetaValue);
         }
         mapping.put(value, result);
         return result;
      }

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

      for (String name : type.itemSet())
      {
         MetaType itemType = type.getType(name);
         Object itemValue;
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

   @Override
   public Object unwrap(MetaValue metaValue)
   {
      return internalUnwrap(metaValue, null);
   }

   @Override
   public Object unwrap(MetaValue metaValue, Type type)
   {
      TypeInfo typeInfo = configuration.getTypeInfo(type);
      return internalUnwrap(metaValue, typeInfo);
   }

   @Override
   public Object unwrap(MetaValue metaValue, TypeInfo type)
   {
      return internalUnwrap(metaValue, type);
   }

   /**
    * Unwrap value from meta value.
    *
    * @param metaValue the meta value
    * @param type expected type info
    * @return unwrapped value
    */
   protected Object internalUnwrap(MetaValue metaValue, TypeInfo type)
   {
      if (metaValue == null)
         return null;

      MetaMapper<?> mapper = MetaMapper.getMetaMapper(type);
      if (mapper != null)
         return mapper.unwrapMetaValue(metaValue);
      
      MetaType metaType = metaValue.getMetaType();

      if (metaType.isSimple())
      {
         Serializable value = ((SimpleValue)metaValue).getValue();
         return getValue(metaType, type, value);
      }
      else if (metaType.isEnum())
      {
         String value = ((EnumValue)metaValue).getValue();
         return getValue(metaType, type, value);
      }
      else if (metaType.isGeneric())
      {
         Serializable value = ((GenericValue)metaValue).getValue();
         return getValue(metaType, type, value);
      }
      else if (metaType.isArray())
      {
         ArrayValue arrayValue = (ArrayValue)metaValue;
         if (type == null)
            type= getTypeInfo(metaType, arrayValue.getValue());
         Object array = newArrayInstance(type, arrayValue.getLength());
         for (int i = 0; i < Array.getLength(array); i++)
         {
            Object element = arrayValue.getValue(i);
            if (element instanceof MetaValue)
               element = unwrapMetaValue((MetaValue)element, type, array);
            else if (element != null && element.getClass().isArray())
               element = unwrapArray(array, element);

            Array.set(array, i, element);
         }
         return array;
      }
      else if (metaType.isComposite())
      {
         CompositeValue compositeValue = (CompositeValue)metaValue;
         return unwrapComposite(compositeValue, type);
      }
      else if (metaType.isCollection())
      {
         CollectionValue collectionValue = (CollectionValue)metaValue;
         return unwrapCollection(collectionValue, type);
      }
      else if (metaType.isTable())
      {
         TableValue tableValue = (TableValue)metaValue;
         return unwrapTable(tableValue, type);
      }

      throw new IllegalArgumentException("Unsupported meta value: " + metaValue);
   }

   /**
    * Do a simple check.
    * If current type param is null,
    * try getting type info from meta type
    * and value's classloader.
    *
    * @param type the type info
    * @param value tester value
    * @param metaType the meta type
    * @return type info
    */
   protected TypeInfo checkTypeInfo(TypeInfo type, Object value, MetaType metaType)
   {
      if (type == null && value != null)
         type = getTypeInfo(metaType, value);
      return type;
   }

   /**
    * Get the value.
    * Join type check and value conversion.
    *
    * @param metaType the meta type
    * @param typeInfo the type info
    * @param value the value
    * @return the converted value
    */
   protected Object getValue(MetaType metaType, TypeInfo typeInfo, Object value)
   {
      typeInfo = checkTypeInfo(typeInfo, value, metaType);
      return convertValue(value, typeInfo);
   }

   /**
    * Unwrap MetaValue.
    *
    * @param element the meta value
    * @param type parent type
    * @param array parent array
    * @return unwrapped value
    */
   protected Object unwrapMetaValue(MetaValue element, TypeInfo type, Object array)
   {
      TypeInfo elementType;
      if (type instanceof ClassInfo)
         elementType = ((ClassInfo)type).getComponentType();
      else
         elementType = getTypeInfo(element.getMetaType(), array);
      return unwrap(element, elementType);
   }

   /**
    * Unwrap array.
    * @param array parent array
    * @param element current array element
    * @return unwrapped array element
    */
   protected Object unwrapArray(Object array, Object element)
   {
      TypeInfo elementType = configuration.getTypeInfo(array.getClass().getComponentType());
      int subSize = Array.getLength(element);
      Object newElement = newArrayInstance(elementType, subSize);
      for(int i = 0; i < subSize; i++)
      {
         Object subElement = Array.get(element, i);
         if (subElement instanceof MetaValue)
            subElement = unwrapMetaValue((MetaValue)subElement, elementType, newElement);
         if (subElement != null && subElement.getClass().isArray())
            subElement = unwrapArray(newElement, subElement);

         Array.set(newElement, i, subElement);
      }
      return newElement;
   }

   /**
    * Unwrap composite.
    *
    * @param compositeValue the composite value
    * @param typeInfo expected type info
    * @return unwrapped value
    */
   protected Object unwrapComposite(CompositeValue compositeValue, TypeInfo typeInfo)
   {
      CompositeMetaType compositeMetaType = compositeValue.getMetaType();
      String typeName = compositeMetaType.getTypeName();
      ClassLoader cl;
      if (typeInfo != null)
         cl = typeInfo.getType().getClassLoader();
      else
         cl = Thread.currentThread().getContextClassLoader();
      
      try
      {
         BeanInfo beanInfo = configuration.getBeanInfo(typeName, cl);
         ClassInfo classInfo = beanInfo.getClassInfo();
         if (classInfo.isInterface())
         {
            InvocationHandler handler = createCompositeValueInvocationHandler(compositeValue);
            Class<?> clazz = classInfo.getType();
            Class<?>[] interfaces = new Class[]{clazz};
            return Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, handler);            
         }
         Object bean = createNewInstance(beanInfo);
         for (String name : compositeMetaType.itemSet())
         {
            MetaValue itemValue = compositeValue.get(name);
            PropertyInfo propertyInfo = beanInfo.getProperty(name);
            Object value = unwrap(itemValue, propertyInfo.getType());
            propertyInfo.set(bean, value);
         }
         return bean;
      }
      catch (Throwable t)
      {
         throw new UndeclaredThrowableException(t);
      }
   }

   /**
    * Create composite invocation handler.
    *
    * @param compositeValue the composite value
    * @return composite invocation handler
    */
   protected InvocationHandler createCompositeValueInvocationHandler(CompositeValue compositeValue)
   {
      return new CompositeValueInvocationHandler(compositeValue);
   }

   /**
    * Unwrap collection meta value.
    *
    * @param collectionValue the collection value
    * @param type the type info
    * @return unwrapped collection
    */
   @SuppressWarnings("unchecked")
   protected Object unwrapCollection(CollectionValue collectionValue, TypeInfo type)
   {
      try
      {
         BeanInfo collectionInfo;
         // null is not instance of
         if (type instanceof ClassInfo)
         {
            collectionInfo = configuration.getBeanInfo(type);
         }
         else
         {
            MetaType metaType = collectionValue.getMetaType();
            collectionInfo = configuration.getBeanInfo(metaType.getTypeName(), Thread.currentThread().getContextClassLoader());
         }
         ClassInfo classInfo = collectionInfo.getClassInfo();
         Collection collection = (Collection)createNewInstance(collectionInfo);
         Iterator<MetaValue> iter = collectionValue.iterator();
         while (iter.hasNext())
         {
            MetaValue metaValue = iter.next();
            TypeInfo componentType = classInfo.getComponentType();
            // try better
            if (OBJECT_TYPE_INFO.equals(componentType))
               componentType = getTypeInfo(metaValue.getMetaType(), null);
            collection.add(unwrap(metaValue, componentType));
         }
         return collection;
      }
      catch (Throwable t)
      {
         throw new UndeclaredThrowableException(t);
      }
   }

   /**
    * Unwrap table meta value.
    *
    * @param tableValue the table value
    * @param type the type info
    * @return unwrapped table (map)
    */
   protected Object unwrapTable(TableValue tableValue, TypeInfo type)
   {
      if (type instanceof ParameterizedClassInfo)
      {
         ParameterizedClassInfo parameterizedType = (ParameterizedClassInfo)type;
         ClassInfo rawType = parameterizedType.getRawType();
         if (Map.class.isAssignableFrom(rawType.getType()))
         {
            TypeInfo keyType = parameterizedType.getActualTypeArguments()[0];
            TypeInfo valueType = parameterizedType.getActualTypeArguments()[1];
            return createMap(tableValue, keyType, valueType);
         }
      }
      throw new UnsupportedOperationException("Insufficient information to unwrap table: " + tableValue + ", " + type);
   }

   /**
    * Create a map
    *
    * @param tableValue the table value
    * @param keyType the key type
    * @param valueType the value type
    * @return the map
    */
   protected Map<?,?> createMap(TableValue tableValue, TypeInfo keyType, TypeInfo valueType)
   {
      if (tableValue == null)
         return null;

      Map<Object, Object> result = new HashMap<Object, Object>();
      Collection<CompositeValue> values = tableValue.values();
      for (CompositeValue entry : values)
      {
         Object key = unwrap(entry.get(DefaultMetaTypeFactory.MAP_KEY), keyType);
         Object val = unwrap(entry.get(DefaultMetaTypeFactory.MAP_VALUE), valueType);
         result.put(key, val);
      }
      return result;
   }

   /**
    * Create new instance.
    *
    * @param beanInfo the bean info
    * @return new instance
    * @throws Throwable for any error
    */
   protected Object createNewInstance(BeanInfo beanInfo) throws Throwable
   {
      ClassInfo classInfo = beanInfo.getClassInfo();
      if (classInfo.isInterface())
      {
         InstanceFactory<?> instanceFactory = instanceFactoryMap.get(classInfo.getType());
         if (instanceFactory == null)
            throw new IllegalArgumentException("Cannot instantiate interface BeanInfo, missing InstanceFactory: " + classInfo);

         return instanceFactory.instantiate(beanInfo);
      }
      return beanInfo.newInstance();
   }

   /**
    * Get new array instance.
    *
    * @param typeInfo the type info
    * @param size the size
    * @return new array instance
    */
   protected Object newArrayInstance(TypeInfo typeInfo, int size)
   {
      if (typeInfo == null)
         throw new IllegalArgumentException("Null type info.");

      try
      {
         return typeInfo.newArrayInstance(size);
      }
      catch (Throwable t)
      {
         throw new UndeclaredThrowableException(t);
      }
   }

   /**
    * Get the class info from meta type.
    *
    * @param metaType the meta type
    * @param value the value which can provide classloader
    * @return type info
    */
   protected TypeInfo getTypeInfo(MetaType metaType, Object value)
   {
      if (metaType == null)
         throw new IllegalArgumentException("Null meta type, cannot determine class name.");
      if (value == null)
         throw new IllegalArgumentException("Null value, cannot determine classloader.");

      // get the classloader from the array we plan to fill
      ClassLoader cl = value.getClass().getClassLoader();
      return getTypeInfo(metaType, cl);
   }

   /**
    * Get the class info from meta type.
    *
    * @param metaType the meta type
    * @param cl the classloader
    * @return class info
    */
   protected TypeInfo getTypeInfo(MetaType metaType, ClassLoader cl)
   {
      if (cl == null)
         cl = Thread.currentThread().getContextClassLoader();

      try
      {
         TypeInfoFactory tif = configuration.getTypeInfoFactory();
         if (metaType.isArray())
         {
            ArrayMetaType arrayMetaType = (ArrayMetaType)metaType;
            MetaType elementMetaType = arrayMetaType.getElementType();
            String elementTypeName = elementMetaType.getTypeName();
            if (arrayMetaType.isPrimitiveArray())
               elementTypeName = ArrayMetaType.getPrimitiveName(elementTypeName);
            TypeInfo elementTypeInfo = tif.getTypeInfo(elementTypeName, cl);
            int dimension = arrayMetaType.getDimension() - 1; // minus 1, since we already use first in next line
            TypeInfo typeInfo = elementTypeInfo.getArrayType();
            while(dimension > 0)
            {
               typeInfo = typeInfo.getArrayType();
               dimension--;
            }
            return typeInfo;
         }
         return tif.getTypeInfo(metaType.getTypeName(), cl);
      }
      catch (ClassNotFoundException e)
      {
         throw new UndeclaredThrowableException(e);
      }
   }

   /**
    * Create a meta value from the object
    * 
    * @param value the value
    * @param type the type
    * @param metaType the metaType
    * @return the meta value
    */
   @SuppressWarnings("unchecked")
   protected MetaValue internalCreate(Object value, TypeInfo type, MetaType metaType)
   {
      if (value == null)
         return null;

      if (type == null)
         type = configuration.getTypeInfo(value.getClass());

      value = convertValue(value, type);

      boolean start = false;
      if (metaType == null)
      {
         start = true;
         metaType = metaTypeFactory.resolve(type);
      }
      
      // For more complicated values we need to keep a mapping of objects to meta values
      // this avoids duplicate meta value construction and recursion 
      Map<Object, MetaValue> mapping;
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
            MetaMapper<Object> mapper = (MetaMapper) MetaMapper.getMetaMapper(type);
            if (mapper != null)
               result = mapper.createMetaValue(metaType, value);
         }

         if (result == null)
         {
            if (metaType.isSimple())
               result = createSimpleValue((SimpleMetaType) metaType, (Serializable) value);
            else if (metaType.isEnum())
               result = createEnumValue((EnumMetaType) metaType, (Enum<?>) value);
            else if (metaType.isArray())
               result = createArrayValue((ArrayMetaType) metaType, value, mapping);
            else if (metaType.isComposite())
               result = createCompositeValue((CompositeMetaType) metaType, value, mapping);
            else if (metaType.isTable())
               result = createTableValue((TableMetaType) metaType, (Map<?,?>) value, mapping);
            else if (metaType.isGeneric())
               result = createGenericValue((GenericMetaType) metaType, value, mapping);
            else if (metaType.isCollection())
               result = createCollectionValue((CollectionMetaType) metaType, value, mapping);
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
    * Convert the value.
    *
    * @param value the value
    * @param typeInfo type info
    * @return converted value if type info not null
    * @throws UndeclaredThrowableException for any error
    */
   protected Object convertValue(Object value, TypeInfo typeInfo)
   {
      try
      {
         return typeInfo != null ? typeInfo.convertValue(value) : value;
      }
      catch (Throwable t)
      {
         throw new UndeclaredThrowableException(t);
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
         WeakReference<MetaValueBuilder<?>> weak = builders.get(type.getType());
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
