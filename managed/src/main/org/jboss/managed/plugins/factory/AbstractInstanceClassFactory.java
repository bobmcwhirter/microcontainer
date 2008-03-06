/*
 * JBoss, Home of Professional Open Source
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.managed.plugins.factory;

import java.io.Serializable;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.config.plugins.property.PropertyConfiguration;
import org.jboss.config.spi.Configuration;
import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.api.ManagedProperty;
import org.jboss.managed.api.annotation.ManagementObjectRef;
import org.jboss.managed.api.annotation.ManagementRuntimeRef;
import org.jboss.managed.api.factory.ManagedObjectFactory;
import org.jboss.managed.plugins.factory.AbstractManagedObjectFactory;
import org.jboss.managed.spi.factory.InstanceClassFactory;
import org.jboss.managed.spi.factory.RuntimeComponentNameTransformer;
import org.jboss.metatype.api.types.ArrayMetaType;
import org.jboss.metatype.api.types.CollectionMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.values.ArrayValueSupport;
import org.jboss.metatype.api.values.CollectionValueSupport;
import org.jboss.metatype.api.values.GenericValueSupport;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.MetaValueFactory;
import org.jboss.reflect.spi.TypeInfo;

/**
 * A base InstanceClassFactory implementation that 
 * 
 * @param <T> the instance type
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class AbstractInstanceClassFactory<T extends Serializable>
   implements InstanceClassFactory<T>
{
   /** The configuration */
   private Configuration configuration = new PropertyConfiguration();
   /** The meta value factory */
   private MetaValueFactory metaValueFactory = MetaValueFactory.getInstance();
   /** The instance to name transformers */
   private Map<TypeInfo, RuntimeComponentNameTransformer> transformers = new WeakHashMap<TypeInfo, RuntimeComponentNameTransformer>();
   private ManagedObjectFactory mof = new AbstractManagedObjectFactory();

   public Map<TypeInfo, RuntimeComponentNameTransformer> getTransformers()
   {
      return transformers;
   }
   public void setTransformers(Map<TypeInfo, RuntimeComponentNameTransformer> transformers)
   {
      this.transformers = transformers;
   }

   public Configuration getConfiguration()
   {
      return configuration;
   }
   public void setConfiguration(Configuration configuration)
   {
      this.configuration = configuration;
   }

   public MetaValueFactory getMetaValueFactory()
   {
      return metaValueFactory;
   }
   public void setMetaValueFactory(MetaValueFactory metaValueFactory)
   {
      this.metaValueFactory = metaValueFactory;
   }

   public ManagedObjectFactory getMof()
   {
      return mof;
   }
   public void setMof(ManagedObjectFactory mof)
   {
      this.mof = mof;
   }

   
   public Class<? extends Serializable> getManagedObjectClass(T attachment)
         throws ClassNotFoundException
   {
      return attachment.getClass();
   }

   public Object getComponentName(BeanInfo beanInfo, ManagedProperty property,
         T attachment, MetaValue value)
   {
      if (beanInfo != null && property != null && value != null)
      {
         String name = getPropertyName(property);
         PropertyInfo propertyInfo = beanInfo.getProperty(name);

         ManagementRuntimeRef componentRef = propertyInfo.getUnderlyingAnnotation(ManagementRuntimeRef.class);
         if (componentRef != null)
         {
            Object original = this.unwrapValue(beanInfo, property, value);
            try
            {
               Class<? extends RuntimeComponentNameTransformer> tClass = componentRef.transformer();
               RuntimeComponentNameTransformer transformer;
               if (tClass != ManagementRuntimeRef.DEFAULT_NAME_TRANSFORMER.class)
                  transformer = getComponentNameTransformer(configuration.getTypeInfo(tClass));
               else
                  transformer = getComponentNameTransformer(propertyInfo.getType());

               return (transformer != null) ? transformer.transform(original) : original;
            }
            catch (Throwable t)
            {
               throw new UndeclaredThrowableException(t);
            }
         }
      }
      return null;
   }

   /**
    * Default InstanceClassFactory implementation simply returns the
    * instance class. 
    * 
    * @param instance the instance
    * @return the class
    */
   public Class<? extends Serializable> getManagedObjectClass(Object instance)
   {
      Serializable s = (Serializable) instance;
      return s.getClass();
   }

   public MetaValue getValue(BeanInfo beanInfo, ManagedProperty property, T object)
   {
      String name = getPropertyName(property);
      PropertyInfo propertyInfo = beanInfo.getProperty(name);

      Object value;
      try
      {
         value = propertyInfo.get(object);
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
         throw new RuntimeException("Error getting property " + name + " for " + object.getClass().getName(), t);
      }

      if (value == null)
         return null;

      MetaType propertyType = property.getMetaType();
      if (AbstractManagedObjectFactory.MANAGED_OBJECT_META_TYPE == propertyType)
      {
         if (value instanceof Serializable == false)
            throw new IllegalStateException("Object is not serializable: " + value.getClass().getName());
         // Look for a ManagementObjectRef
         ManagementObjectRef ref = (ManagementObjectRef) property.getAnnotations().get(ManagementObjectRef.class.getName());
         String moName = (ref != null ? ref.name() : value.getClass().getName());
         String moNameType = (ref != null ? ref.type() : "");
         ManagedObject mo = mof.initManagedObject((Serializable) value, moName, moNameType);
         return new GenericValueSupport(AbstractManagedObjectFactory.MANAGED_OBJECT_META_TYPE, mo);
      }
      else if (propertyType.isArray())
      {
         ArrayMetaType arrayType = ArrayMetaType.class.cast(propertyType);
         if (AbstractManagedObjectFactory.MANAGED_OBJECT_META_TYPE == arrayType.getElementType())
         {
            Collection<?> cvalue = getAsCollection(value);
            ArrayMetaType moType = new ArrayMetaType(1, AbstractManagedObjectFactory.MANAGED_OBJECT_META_TYPE);
            ArrayValueSupport moArrayValue = new ArrayValueSupport(moType);
            List<GenericValueSupport> tmp = new ArrayList<GenericValueSupport>();
            for(Object element : cvalue)
            {
               ManagedObject mo = mof.initManagedObject((Serializable) element, null, null);
               tmp.add(new GenericValueSupport(AbstractManagedObjectFactory.MANAGED_OBJECT_META_TYPE, mo));
            }
            GenericValueSupport[] mos = new GenericValueSupport[tmp.size()];
            moArrayValue.setValue(tmp.toArray(mos));
            return moArrayValue;
         }
      }
      else if (propertyType.isCollection())
      {
         CollectionMetaType collectionType = CollectionMetaType.class.cast(propertyType);
         if (AbstractManagedObjectFactory.MANAGED_OBJECT_META_TYPE == collectionType.getElementType())
         {
            Collection<?> cvalue = getAsCollection(value);
            List<GenericValueSupport> tmp = new ArrayList<GenericValueSupport>();
            for(Object element : cvalue)
            {
               ManagedObject mo = mof.initManagedObject((Serializable) element, null, null);
               tmp.add(new GenericValueSupport(AbstractManagedObjectFactory.MANAGED_OBJECT_META_TYPE, mo));
            }
            GenericValueSupport[] mos = new GenericValueSupport[tmp.size()];
            CollectionMetaType moType = new CollectionMetaType(propertyType.getClassName(), AbstractManagedObjectFactory.MANAGED_OBJECT_META_TYPE);
            return new CollectionValueSupport(moType, tmp.toArray(mos));
         }
      }

      return metaValueFactory.create(value, propertyInfo.getType());
   }


   /**
    * Set a value
    *
    * @param beanInfo the bean info
    * @param property the property
    * @param object the object
    * @param value the meta value
    */
   public void setValue(BeanInfo beanInfo, ManagedProperty property, T object, MetaValue value)
   {
      String name = getPropertyName(property);
      PropertyInfo propertyInfo = beanInfo.getProperty(name);

      Object unwrapValue = unwrapValue(beanInfo, property, value);
      try
      {
         setValue(beanInfo, propertyInfo, object, unwrapValue);
      }
      catch(Throwable t)
      {
         throw new UndeclaredThrowableException(t);
      }
   }

   /**
    * Get component name transformer.
    *
    * @param type the type info
    * @return transformer instance
    * @throws Throwable for any error
    */
   protected RuntimeComponentNameTransformer getComponentNameTransformer(TypeInfo type) throws Throwable
   {
      synchronized(transformers)
      {
         RuntimeComponentNameTransformer transformer = transformers.get(type);
         if (transformer != null)
            return transformer;

         TypeInfo rcntType = configuration.getTypeInfo(RuntimeComponentNameTransformer.class);
         if (rcntType.isAssignableFrom(type))
         {
            BeanInfo beanInfo = configuration.getBeanInfo(type);
            RuntimeComponentNameTransformer newTransformer = (RuntimeComponentNameTransformer)beanInfo.newInstance();
            transformers.put(type, newTransformer);
            return newTransformer;
         }

         return null;
      }
   }

   protected Object unwrapValue(BeanInfo beanInfo, ManagedProperty property, MetaValue value)
   {
      String name = getPropertyName(property);
      PropertyInfo propertyInfo = beanInfo.getProperty(name);

      Object unwrapValue = metaValueFactory.unwrap(value, propertyInfo.getType());
      return unwrapValue;
   }
   protected void setValue(BeanInfo beanInfo, PropertyInfo propertyInfo, Object object, Object unwrapValue)
      throws Throwable
   {
      propertyInfo.set(object, unwrapValue);
   }

   protected Collection<?> getAsCollection(Object value)
   {
      if( value.getClass().isArray() )
         return Arrays.asList(value);
      else if (value instanceof Collection)
         return Collection.class.cast(value);
      return null;
   }

   /**
    * Get the property name.
    *
    * @param property managed property
    * @return property name
    */
   protected String getPropertyName(ManagedProperty property)
   {
      // First look to the mapped name
      String name = property.getMappedName();
      if (name == null)
         property.getName();
      return name;
   }
}
