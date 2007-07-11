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
package org.jboss.managed.plugins.factory;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.config.plugins.property.PropertyConfiguration;
import org.jboss.config.spi.Configuration;
import org.jboss.managed.api.Fields;
import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.api.ManagedProperty;
import org.jboss.managed.api.annotation.ManagementConstants;
import org.jboss.managed.api.annotation.ManagementObject;
import org.jboss.managed.api.annotation.ManagementProperties;
import org.jboss.managed.api.annotation.ManagementProperty;
import org.jboss.managed.api.factory.ManagedObjectFactory;
import org.jboss.managed.plugins.DefaultFieldsImpl;
import org.jboss.managed.plugins.ManagedObjectImpl;
import org.jboss.managed.plugins.ManagedPropertyImpl;
import org.jboss.managed.spi.factory.ManagedObjectBuilder;
import org.jboss.managed.spi.factory.ManagedObjectPopulator;
import org.jboss.metatype.api.types.GenericMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.MetaTypeFactory;
import org.jboss.metatype.api.values.GenericValueSupport;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.MetaValueFactory;
import org.jboss.reflect.spi.ClassInfo;

/**
 * AbstractManagedObjectFactory.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class AbstractManagedObjectFactory extends ManagedObjectFactory implements ManagedObjectBuilder, ManagedObjectPopulator<Serializable>
{
   /** The configuration */
   private static final Configuration configuration;

   /** The managed object meta type */
   public static final GenericMetaType MANAGED_OBJECT_META_TYPE = new GenericMetaType(ManagedObject.class.getName(), ManagedObject.class.getName());
   
   /** The meta type factory */
   private MetaTypeFactory metaTypeFactory = MetaTypeFactory.getInstance(); 

   /** The meta value factory */
   private MetaValueFactory metaValueFactory = MetaValueFactory.getInstance(); 
   
   /** The managed object builders */
   private Map<Class, WeakReference<ManagedObjectBuilder>> builders = new WeakHashMap<Class, WeakReference<ManagedObjectBuilder>>();

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
   
   @Override
   public <T extends Serializable> ManagedObject createManagedObject(Class<T> clazz)
   {
      if (clazz == null)
         throw new IllegalArgumentException("Null class");

      ManagedObject result = createSkeletonManagedObject(clazz);
      ManagedObjectPopulator<Serializable> populator = getPopulator(clazz);
      populator.createObject(result, clazz);
      
      return result;
   }

   @Override
   public ManagedObject initManagedObject(Serializable object)
   {
      if (object == null)
         throw new IllegalArgumentException("Null object");

      Class<? extends Serializable> clazz = object.getClass();
      ManagedObject result = createSkeletonManagedObject(clazz);
      ManagedObjectPopulator<Serializable> populator = getPopulator(clazz);
      populator.populateManagedObject(result, object);

      return result;
   }

   @Override
   public void setBuilder(Class<?> clazz, ManagedObjectBuilder builder)
   {
      synchronized (builders)
      {
         if (builder == null)
            builders.remove(clazz);
         builders.put(clazz, new WeakReference<ManagedObjectBuilder>(builder));
      }
   }
   
   /**
    * Create a skeleton managed object
    * 
    * @param <T> the type
    * @param clazz the clazz
    * @return the skeleton managed object
    */
   protected <T extends Serializable> ManagedObject createSkeletonManagedObject(Class<T> clazz)
   {
      if (clazz == null)
         throw new IllegalArgumentException("Null class");

      ManagedObjectBuilder builder = getBuilder(clazz);
      return builder.buildManagedObject(clazz);
   }
   
   public ManagedObject buildManagedObject(Class<? extends Serializable> clazz)
   {
      BeanInfo beanInfo = configuration.getBeanInfo(clazz);
      ClassInfo classInfo = beanInfo.getClassInfo();

      ManagementObject managementObject = classInfo.getUnderlyingAnnotation(ManagementObject.class);
      
      String name = ManagementConstants.GENERATED;
      if (managementObject != null)
         name = managementObject.name();
      if (ManagementConstants.GENERATED.equals(name))
         name = classInfo.getName();
      
      ManagementProperties propertyType = ManagementProperties.ALL;
      if (managementObject != null)
         propertyType = managementObject.properties();
      
      Set<ManagedProperty> properties = new HashSet<ManagedProperty>();
      
      Set<PropertyInfo> propertyInfos = beanInfo.getProperties();
      if (propertyInfos != null && propertyInfos.isEmpty() == false)
      {
         for (PropertyInfo propertyInfo : propertyInfos)
         {
            // Ignore the "class" property
            if ("class".equals(propertyInfo.getName()))
               continue;
            
            ManagementProperty managementProperty = propertyInfo.getUnderlyingAnnotation(ManagementProperty.class);

            boolean includeProperty = (propertyType == ManagementProperties.ALL);
            if (managementProperty != null)
               includeProperty = (managementProperty.ignored() == false);
            
            if (includeProperty)
            {
               Fields fields = new DefaultFieldsImpl();

               String propertyName = propertyInfo.getName();
               fields.setField(Fields.NAME, propertyName);

               String description = ManagementConstants.GENERATED;
               if (managementProperty != null)
                  description = managementProperty.description();
               if (description.equals(ManagementConstants.GENERATED))
                  description = propertyName;
               fields.setField(Fields.DESCRIPTION, description);

               boolean mandatory = false;
               if (managementProperty != null)
                  mandatory = managementProperty.mandatory();
               if (mandatory)
                  fields.setField(Fields.MANDATORY, Boolean.TRUE);
               
               boolean managed = false;
               if (managementProperty != null)
                  managed = managementProperty.managed();
               
               MetaType metaType;
               if (managed)
               {
                  metaType = MANAGED_OBJECT_META_TYPE;
               }
               else
               {
                  metaType = metaTypeFactory.resolve(propertyInfo.getType());
               }
               fields.setField(Fields.META_TYPE, metaType);

               // TODO others (legal values, min/max etc.)
               
               ManagedPropertyImpl property = new ManagedPropertyImpl(fields);
               properties.add(property);
            }
         }
      }
      
      ManagedObjectImpl result = new ManagedObjectImpl(name, properties);
      for (ManagedProperty property : properties)
      {
         ManagedPropertyImpl managedPropertyImpl = (ManagedPropertyImpl) property;
         managedPropertyImpl.setManagedObject(result);
      }
      
      return result;
   }

   public void createObject(ManagedObject managedObject, Class<? extends Serializable> clazz)
   {
      if (managedObject instanceof ManagedObjectImpl == false)
         throw new IllegalStateException("Unable to create object " + managedObject.getClass().getName());
      
      ManagedObjectImpl managedObjectImpl = (ManagedObjectImpl) managedObject;
      Serializable object = createUnderlyingObject(managedObjectImpl, clazz);
      populateManagedObject(managedObject, object);
   }
   
   public void populateManagedObject(ManagedObject managedObject, Serializable object)
   {
      if (managedObject instanceof ManagedObjectImpl == false)
         throw new IllegalStateException("Unable to populate managed object " + managedObject.getClass().getName());
      
      ManagedObjectImpl managedObjectImpl = (ManagedObjectImpl) managedObject;
      managedObjectImpl.setAttachment(object);
      populateValues(managedObjectImpl, object);
   }
   
   /**
    * Create the underlying object
    * 
    * @param managedObject the managed object
    * @param clazz the class
    * @return the object
    */
   protected Serializable createUnderlyingObject(ManagedObjectImpl managedObject, Class<? extends Serializable> clazz)
   {
      BeanInfo beanInfo = configuration.getBeanInfo(clazz);
      try
      {
         Object result = beanInfo.newInstance();
         return Serializable.class.cast(result);
      }
      catch (Throwable t)
      {
         throw new RuntimeException("Unable to create new object for " + managedObject + " clazz=" + clazz, t);
      }
   }
   
   /**
    * Populate the values
    * 
    * @param managedObject the managed object
    * @param object the object
    */
   protected void populateValues(ManagedObjectImpl managedObject, Serializable object)
   {
      BeanInfo beanInfo = configuration.getBeanInfo(object.getClass());

      Set<ManagedProperty> properties = managedObject.getProperties();
      if (properties != null && properties.size() > 0)
      {
         for (ManagedProperty property : properties)
         {
            MetaValue value = getValue(beanInfo, property, object);
            if (value != null)
               property.setField(Fields.VALUE, value);
         }
      }
   }

   /**
    * Get a value
    * 
    * @param beanInfo the bean info
    * @param property the property
    * @param object the object
    * @return the meta value
    */
   protected MetaValue getValue(BeanInfo beanInfo, ManagedProperty property, Serializable object)
   {
      String name = property.getName();

      PropertyInfo propertyInfo = beanInfo.getProperty(name);
      if (propertyInfo == null)
         throw new IllegalStateException("Unable to find property: " + name + " for " + object.getClass().getName());
      
      Object value = null;
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

      if (MANAGED_OBJECT_META_TYPE == property.getMetaType())
      {
         if (value instanceof Serializable == false)
            throw new IllegalStateException("Object is not serializable: " + value.getClass().getName());
         ManagedObject mo = initManagedObject((Serializable) value);
         return new GenericValueSupport(MANAGED_OBJECT_META_TYPE, mo);
      }
      
      return metaValueFactory.create(value, propertyInfo.getType());
   }
   
   /**
    * Get the builder for a class
    * 
    * @param clazz the class
    * @return the builder
    */
   protected ManagedObjectBuilder getBuilder(Class<?> clazz)
   {
      synchronized (builders)
      {
         WeakReference<ManagedObjectBuilder> weak = builders.get(clazz);
         if (weak != null)
            return weak.get();
      }
      return this;
   }
   
   /**
    * Get the populator for a class
    * 
    * @param clazz the class
    * @return the populator
    */
   @SuppressWarnings("unchecked")
   protected ManagedObjectPopulator<Serializable> getPopulator(Class<?> clazz)
   {
      ManagedObjectBuilder builder = getBuilder(clazz);
      if (builder instanceof ManagedObjectPopulator)
         return (ManagedObjectPopulator) builder;
      return this;
   }
}
