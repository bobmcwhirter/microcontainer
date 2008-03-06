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
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.config.spi.Configuration;
import org.jboss.logging.Logger;
import org.jboss.managed.api.Fields;
import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.api.ManagedOperation;
import org.jboss.managed.api.ManagedParameter;
import org.jboss.managed.api.ManagedProperty;
import org.jboss.managed.api.ManagedOperation.Impact;
import org.jboss.managed.api.annotation.AnnotationDefaults;
import org.jboss.managed.api.annotation.ManagementComponent;
import org.jboss.managed.api.annotation.ManagementConstants;
import org.jboss.managed.api.annotation.ManagementObject;
import org.jboss.managed.api.annotation.ManagementObjectID;
import org.jboss.managed.api.annotation.ManagementObjectRef;
import org.jboss.managed.api.annotation.ManagementOperation;
import org.jboss.managed.api.annotation.ManagementParameter;
import org.jboss.managed.api.annotation.ManagementProperties;
import org.jboss.managed.api.annotation.ManagementProperty;
import org.jboss.managed.api.annotation.ManagementRuntimeRef;
import org.jboss.managed.api.factory.ManagedObjectFactory;
import org.jboss.managed.plugins.DefaultFieldsImpl;
import org.jboss.managed.plugins.ManagedObjectImpl;
import org.jboss.managed.plugins.ManagedOperationImpl;
import org.jboss.managed.plugins.ManagedParameterImpl;
import org.jboss.managed.plugins.WritethroughManagedPropertyImpl;
import org.jboss.managed.spi.factory.InstanceClassFactory;
import org.jboss.managed.spi.factory.ManagedObjectBuilder;
import org.jboss.managed.spi.factory.ManagedObjectPopulator;
import org.jboss.managed.spi.factory.ManagedParameterConstraintsPopulator;
import org.jboss.managed.spi.factory.ManagedParameterConstraintsPopulatorFactory;
import org.jboss.managed.spi.factory.ManagedPropertyConstraintsPopulator;
import org.jboss.managed.spi.factory.ManagedPropertyConstraintsPopulatorFactory;
import org.jboss.managed.spi.factory.RuntimeComponentNameTransformer;
import org.jboss.metatype.api.types.ArrayMetaType;
import org.jboss.metatype.api.types.CollectionMetaType;
import org.jboss.metatype.api.types.GenericMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.MetaTypeFactory;
import org.jboss.metatype.api.values.ArrayValueSupport;
import org.jboss.metatype.api.values.CollectionValueSupport;
import org.jboss.metatype.api.values.GenericValueSupport;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.MetaValueFactory;
import org.jboss.metatype.api.values.SimpleValue;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.reflect.spi.ParameterInfo;
import org.jboss.reflect.spi.TypeInfo;

/**
 * AbstractManagedObjectFactory.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1.1 $
 */
public class AbstractManagedObjectFactory extends ManagedObjectFactory
   implements ManagedObjectBuilder, InstanceClassFactory<Serializable>, ManagedObjectPopulator<Serializable>
{
   private static final Logger log = Logger.getLogger(AbstractManagedObjectFactory.class);

   /** The configuration */
   private static final Configuration configuration = PropertyConfigurationAccess.getConfiguration();

   /** The managed object meta type */
   public static final GenericMetaType MANAGED_OBJECT_META_TYPE = new GenericMetaType(ManagedObject.class.getName(), ManagedObject.class.getName());
   
   /** The meta type factory */
   private MetaTypeFactory metaTypeFactory = MetaTypeFactory.getInstance(); 

   /** The meta value factory */
   private MetaValueFactory metaValueFactory = MetaValueFactory.getInstance();
   
   /** The managed object builders */
   private Map<Class<?>, ManagedObjectBuilder> builders = new WeakHashMap<Class<?>, ManagedObjectBuilder>();

   /** The instance to class factories */
   private Map<Class<?>, InstanceClassFactory<? extends Serializable>> instanceFactories = new WeakHashMap<Class<?>, InstanceClassFactory<? extends Serializable>>();

   /** The instance to name transformers */
   private Map<TypeInfo, RuntimeComponentNameTransformer> transformers = new WeakHashMap<TypeInfo, RuntimeComponentNameTransformer>();

   /**
    * Create a ManagedProperty by looking to the factory for ctor(Fields)
    * @param factory - the ManagedProperty implementation class
    * @param fields - the fields to pass to the ctor
    * @return the managed property if successful, null otherwise
    */
   public static ManagedProperty createManagedProperty(Class<? extends ManagedProperty> factory, Fields fields)
   {
      ManagedProperty property = null;
      try
      {
         Class<?>[] sig = {Fields.class};
         Constructor<? extends ManagedProperty> ctor = factory.getConstructor(sig);
         Object[] args = {fields};
         property = ctor.newInstance(args);
      }
      catch(Exception e)
      {
         log.debug("Failed to create ManagedProperty", e);
      }
      return property;
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
   @SuppressWarnings("unchecked")
   public ManagedObject initManagedObject(Serializable object, String name, String nameType)
   {
      if (object == null)
         throw new IllegalArgumentException("Null object");

      Class<? extends Serializable> clazz = object.getClass();
      InstanceClassFactory icf = getInstanceClassFactory(clazz);
      Class<? extends Serializable> moClass;
      try
      {
         moClass = icf.getManagedObjectClass(object);
      }
      catch(ClassNotFoundException e)
      {
         return null;
      }
      ManagedObject result = createSkeletonManagedObject(moClass);
      if (result == null )
      {
         log.debug("Null ManagedObject created for: "+moClass);
         return null;
      }
      ManagedObjectPopulator<Serializable> populator = getPopulator(moClass);
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
         else
            builders.put(clazz, builder);
      }
   }

   @Override
   public <T extends Serializable> void setInstanceClassFactory(Class<T> clazz, InstanceClassFactory<T> factory)
   {
      synchronized (instanceFactories)
      {
         if (factory == null)
            instanceFactories.remove(clazz);
         else
            instanceFactories.put(clazz, factory);
      }      
   }

   public void setNameTransformers(Class<?> clazz, RuntimeComponentNameTransformer transformer)
   {
      TypeInfo type = configuration.getTypeInfo(clazz);
      setNameTransformers(type, transformer);
   }

   public void setNameTransformers(TypeInfo type, RuntimeComponentNameTransformer transformer)
   {
      synchronized (transformers)
      {
         if (transformer == null)
            transformers.remove(type);
         else
            transformers.put(type, transformer);
      }
   }

   /**
    * Default InstanceClassFactory implementation simply returns the
    * instance class. 
    */
   public Class<? extends Serializable> getManagedObjectClass(Serializable instance)
   {
      return instance.getClass();
   }

   /**
    * Create a skeleton managed object
    * 
    * @param <T> the type
    * @param clazz the clazz
    * @return the skeleton managed object, null if clazz is not
    *    marked as a ManagementObject.
    * {@linkplain ManagementObject}
    */
   protected <T extends Serializable> ManagedObject createSkeletonManagedObject(Class<T> clazz)
   {
      if (clazz == null)
         throw new IllegalArgumentException("Null class");

      ManagedObjectBuilder builder = getBuilder(clazz);
      return builder.buildManagedObject(clazz);
   }
   
   /**
    * The ManagedObjectBuilder.buildManagedObject implementation. This is based
    * on the org.jboss.managed.api.annotation.* package annotations.
    * @param clazz the attachment class
    * @return the ManagementObject if clazz is properly annotated, null if
    *    it does not have a ManagementObject annotation.
    */
   @SuppressWarnings("unchecked")
   public ManagedObject buildManagedObject(Class<? extends Serializable> clazz)
   {
      boolean trace = log.isTraceEnabled();
      BeanInfo beanInfo = configuration.getBeanInfo(clazz);
      ClassInfo classInfo = beanInfo.getClassInfo();

      ManagementObject managementObject = classInfo.getUnderlyingAnnotation(ManagementObject.class);
      if( managementObject == null )
      {
         if (trace)
            log.trace("No ManagementObject annotation, skipping ManagedObject for class: "+clazz);
         // Skip the ManagedObject creation
         return null;
      }

      HashMap<String, Annotation> moAnnotations = new HashMap<String, Annotation>();
      moAnnotations.put(ManagementObject.class.getName(), managementObject);
      ManagementObjectID moID = classInfo.getUnderlyingAnnotation(ManagementObjectID.class);
      if (moID != null)
         moAnnotations.put(ManagementObjectID.class.getName(), moID);

      // Process the ManagementObject fields
      boolean isRuntime = managementObject.isRuntime();
      String name = classInfo.getName();
      String nameType = null;
      String attachmentName = classInfo.getName();
      Class<? extends Fields> moFieldsFactory = null;
      Class<? extends ManagedPropertyConstraintsPopulatorFactory> moConstraintsFactory = null;
      Class<? extends ManagedProperty> moPropertyFactory = null;
      if (managementObject != null)
      {
         name = managementObject.name();
         if (name.length() == 0 || name.equals(ManagementConstants.GENERATED))
            name = classInfo.getName();
         nameType = managementObject.type();
         if (nameType.length() == 0)
            nameType = null;
         attachmentName = managementObject.attachmentName();
         if (attachmentName.length() == 0)
            attachmentName = classInfo.getName();
         // Check for a component specification
         ManagementComponent mc = managementObject.componentType();
         if (mc.equals(AnnotationDefaults.COMP_TYPE) == false)
            moAnnotations.put(ManagementComponent.class.getName(), mc);
         // ManagementObject level default factory classes
         moFieldsFactory = managementObject.fieldsFactory();
         moConstraintsFactory = managementObject.constraintsFactory();
         moPropertyFactory = managementObject.propertyFactory();
      }

      if (trace)
      {
         log.trace("Building MangedObject(name="+name+",nameType="+nameType
               +",attachmentName="+attachmentName+",isRuntime="+isRuntime+")");
      }

      ManagementProperties propertyType = ManagementProperties.ALL;
      if (managementObject != null)
         propertyType = managementObject.properties();

      // Build the ManagedProperties
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
            ManagementObjectID id = propertyInfo.getUnderlyingAnnotation(ManagementObjectID.class);
            ManagementObjectRef ref = propertyInfo.getUnderlyingAnnotation(ManagementObjectRef.class);
            ManagementRuntimeRef runtimeRef = propertyInfo.getUnderlyingAnnotation(ManagementRuntimeRef.class);
            HashMap<String, Annotation> propAnnotations = new HashMap<String, Annotation>();
            if (managementProperty != null)
               propAnnotations.put(ManagementProperty.class.getName(), managementProperty);
            if (id != null)
            {
               propAnnotations.put(ManagementObjectID.class.getName(), id);
               // This overrides the MO nameType
               nameType = id.type();
            }
            if (ref != null)
               propAnnotations.put(ManagementObjectRef.class.getName(), ref);
            if (runtimeRef != null)
               propAnnotations.put(ManagementRuntimeRef.class.getName(), runtimeRef);

            // Check for a simple property
            boolean includeProperty = (propertyType == ManagementProperties.ALL);
            if (managementProperty != null)
               includeProperty = (managementProperty.ignored() == false);

            if (includeProperty)
            {
               Fields fields = null;
               if (managementProperty != null)
               {
                  Class<? extends Fields> factory = moFieldsFactory;
                  if (factory == ManagementProperty.NULL_FIELDS_FACTORY.class)
                     factory = managementProperty.fieldsFactory();
                  if (factory != ManagementProperty.NULL_FIELDS_FACTORY.class)
                  {
                     try
                     {
                        fields = factory.newInstance();
                     }
                     catch (Exception e)
                     {
                        log.debug("Failed to created Fields", e);
                     }
                  }
               }
               if (fields == null)
                  fields = new DefaultFieldsImpl();

               if( propertyInfo instanceof Serializable )
               {
                  Serializable info = Serializable.class.cast(propertyInfo);
                  fields.setField(Fields.PROPERTY_INFO, info);
               }

               String propertyName = propertyInfo.getName();
               if (managementProperty != null)
                  propertyName = managementProperty.name();
               if( propertyName.length() == 0 )
                  propertyName = propertyInfo.getName();
               fields.setField(Fields.NAME, propertyName);

               // This should probably always the the propertyInfo name?
               String mappedName = propertyInfo.getName();
               if (managementProperty != null)
                  mappedName = managementProperty.mappedName();
               if( mappedName.length() == 0 )
                  mappedName = propertyInfo.getName();
               fields.setField(Fields.MAPPED_NAME, mappedName);

               String description = ManagementConstants.GENERATED;
               if (managementProperty != null)
                  description = managementProperty.description();
               if (description.equals(ManagementConstants.GENERATED))
                  description = propertyName;
               fields.setField(Fields.DESCRIPTION, description);

               if (trace)
               {
                  log.trace("Building MangedProperty(name="+propertyName
                        +",mappedName="+mappedName
                        +") ,annotations="+propAnnotations);
               }

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
                  TypeInfo typeInfo = propertyInfo.getType();
                  if(typeInfo.isArray())
                     metaType = new ArrayMetaType(1, MANAGED_OBJECT_META_TYPE);
                  else if (typeInfo.isCollection())
                     metaType = new CollectionMetaType(typeInfo.getName(), MANAGED_OBJECT_META_TYPE);
                  else
                     metaType = MANAGED_OBJECT_META_TYPE;
               }
               else
               {
                  metaType = metaTypeFactory.resolve(propertyInfo.getType());
               }
               fields.setField(Fields.META_TYPE, metaType);
               if (propAnnotations.isEmpty() == false)
                  fields.setField(Fields.ANNOTATIONS, propAnnotations);

               // Delegate others (legal values, min/max etc.) to the constraints factory
               try
               {
                  Class<? extends ManagedPropertyConstraintsPopulatorFactory> factoryClass = moConstraintsFactory;
                  if (factoryClass == ManagementProperty.NULL_CONSTRAINTS.class)
                  {
                     if (managementProperty != null)
                        factoryClass = managementProperty.constraintsFactory();
                  }
                  ManagedPropertyConstraintsPopulatorFactory factory = factoryClass.newInstance();
                  ManagedPropertyConstraintsPopulator populator = factory.newInstance();
                  if (populator != null)
                     populator.populateManagedProperty(clazz, propertyInfo, fields);
               }
               catch(Exception e)
               {
                  log.debug("Failed to populate constraints for: "+propertyInfo, e);
               }

               ManagedProperty property = null;
               if (managementProperty != null)
               {
                  Class<? extends ManagedProperty> factory = moPropertyFactory;
                  if (factory == ManagementProperty.NULL_PROPERTY_FACTORY.class)
                     factory = managementProperty.propertyFactory();
                  if (factory != ManagementProperty.NULL_PROPERTY_FACTORY.class)
                     property = getManagedProperty(factory, fields);
               }
               // we should have write-through by default
               // use factory to change this default behavior
               if (property == null)
                  property = createDefaultManagedProperty(fields);
               properties.add(property);
            }
            else if (trace)
               log.trace("Ignoring property: " + propertyInfo);
         }
      }

      /* TODO: Operations. In general the bean metadata does not contain
         operation information.
      */
      Set<ManagedOperation> operations = new HashSet<ManagedOperation>();
      
      Set<MethodInfo> methodInfos = beanInfo.getMethods();
      if (methodInfos != null && methodInfos.isEmpty() == false)
      {
         for (MethodInfo methodInfo : methodInfos)
         {
            ManagementOperation managementOp = methodInfo.getUnderlyingAnnotation(ManagementOperation.class);
            if (managementOp == null)
               continue;

            ManagedOperation op = getManagedOperation(methodInfo, managementOp);
            operations.add(op);
         }
      }

      ManagedObjectImpl result = new ManagedObjectImpl(name, properties);
      result.setAnnotations(moAnnotations);
      if (nameType != null)
         result.setNameType(nameType);
      if (attachmentName != null)
         result.setAttachmentName(attachmentName);
      if (operations.size() > 0 )
         result.setOperations(operations);
      for (ManagedProperty property : properties)
         property.setManagedObject(result);
      return result;
   }

   /**
    * Create default MangedProperty instance.
    * Override this method for different default.
    *
    * @param fields the fields
    * @return new ManagedProperty instance
    */
   protected ManagedProperty createDefaultManagedProperty(Fields fields)
   {
      return new WritethroughManagedPropertyImpl(fields, metaValueFactory, this);
   }

   public void createObject(ManagedObject managedObject, Class<? extends Serializable> clazz)
   {
      if (managedObject == null)
         throw new IllegalArgumentException("Null managed object");
      
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
   @SuppressWarnings("unchecked")
   protected void populateValues(ManagedObjectImpl managedObject, Serializable object)
   {
      InstanceClassFactory icf = getInstanceClassFactory(object.getClass());
      Class moClass;
      try
      {
         moClass = icf.getManagedObjectClass(object);
      }
      catch(ClassNotFoundException e)
      {
         throw new IllegalStateException(e);
      }
      BeanInfo beanInfo = configuration.getBeanInfo(moClass);

      Object componentName = null;
      Map<String, ManagedProperty> properties = managedObject.getProperties();
      if (properties != null && properties.size() > 0)
      {
         for (ManagedProperty property : properties.values())
         {
            MetaValue value = icf.getValue(beanInfo, property, object);
            if (value != null)
               property.setField(Fields.VALUE, value);
            /* Need to look for a ManagementObjectID at the property level which
               defines the ManagedObject id name from the property value.
             */
            Map<String, Annotation> annotations = property.getAnnotations();
            if (annotations == null)
               continue;
            ManagementObjectID id = (ManagementObjectID) annotations.get(ManagementObjectID.class.getName());
            if (id != null)
            {
               if (value == null || value.getMetaType().isSimple() == false)
               {
                  log.warn("Cannot create String name from non-Simple property: "
                        +property+", value="+value);
                  continue;
               }
               SimpleValue svalue = (SimpleValue) value;
               String name = "" + svalue.getValue();
               managedObject.setName(name);
            }
            ManagementRuntimeRef runtimeRef = (ManagementRuntimeRef) annotations.get(ManagementRuntimeRef.class.getName());
            if (runtimeRef != null)
            {
               componentName = icf.getComponentName(beanInfo, property, object, value);
               // let's try this as well
               if (componentName == null && icf != this)
                  componentName = getComponentName(beanInfo, property, object, value);
            }
         }
      }
      if (componentName == null)
         componentName = icf.getComponentName(null, null, object, null);
      // set it, even if it's null
      managedObject.setComponentName(componentName);
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

   public Object getComponentName(BeanInfo beanInfo, ManagedProperty property, Serializable object, MetaValue value)
   {
      if (beanInfo != null && property != null && value != null)
      {
         String name = getPropertyName(property);
         PropertyInfo propertyInfo = beanInfo.getProperty(name);

         ManagementRuntimeRef componentRef = propertyInfo.getUnderlyingAnnotation(ManagementRuntimeRef.class);
         if (componentRef != null)
         {
            Object original = metaValueFactory.unwrap(value, propertyInfo.getType());
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
    * Get a value
    * 
    * @param beanInfo the bean info
    * @param property the property
    * @param object the object
    * @return the meta value
    */
   @SuppressWarnings("unchecked")
   public MetaValue getValue(BeanInfo beanInfo, ManagedProperty property, Serializable object)
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
      if (MANAGED_OBJECT_META_TYPE == propertyType)
      {
         if (value instanceof Serializable == false)
            throw new IllegalStateException("Object is not serializable: " + value.getClass().getName());
         // Look for a ManagementObjectRef
         ManagementObjectRef ref = (ManagementObjectRef) property.getAnnotations().get(ManagementObjectRef.class.getName());
         String moName = (ref != null ? ref.name() : value.getClass().getName());
         String moNameType = (ref != null ? ref.type() : "");
         ManagedObject mo = initManagedObject((Serializable) value, moName, moNameType);
         return new GenericValueSupport(MANAGED_OBJECT_META_TYPE, mo);
      }
      else if (propertyType.isArray())
      {
         ArrayMetaType arrayType = ArrayMetaType.class.cast(propertyType);
         if (MANAGED_OBJECT_META_TYPE == arrayType.getElementType())
         {
            Collection<?> cvalue = getAsCollection(value);
            // todo - AJ: changed some generics by best guess
            ArrayMetaType moType = new ArrayMetaType(1, MANAGED_OBJECT_META_TYPE);
            ArrayValueSupport moArrayValue = new ArrayValueSupport(moType);
            List<GenericValueSupport> tmp = new ArrayList<GenericValueSupport>();
            for(Object element : cvalue)
            {
               ManagedObject mo = initManagedObject((Serializable) element, null, null);
               tmp.add(new GenericValueSupport(MANAGED_OBJECT_META_TYPE, mo));
            }
            GenericValueSupport[] mos = new GenericValueSupport[tmp.size()];
            moArrayValue.setValue(tmp.toArray(mos));
            return moArrayValue;
         }
      }
      else if (propertyType.isCollection())
      {
         CollectionMetaType collectionType = CollectionMetaType.class.cast(propertyType);
         if (MANAGED_OBJECT_META_TYPE == collectionType.getElementType())
         {
            Collection<?> cvalue = getAsCollection(value);
            List<GenericValueSupport> tmp = new ArrayList<GenericValueSupport>();
            for(Object element : cvalue)
            {
               ManagedObject mo = initManagedObject((Serializable) element, null, null);
               tmp.add(new GenericValueSupport(MANAGED_OBJECT_META_TYPE, mo));
            }
            GenericValueSupport[] mos = new GenericValueSupport[tmp.size()];
            CollectionMetaType moType = new CollectionMetaType(propertyType.getClassName(), MANAGED_OBJECT_META_TYPE);
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
   public void setValue(BeanInfo beanInfo, ManagedProperty property, Serializable object, MetaValue value)
   {
      String name = getPropertyName(property);
      PropertyInfo propertyInfo = beanInfo.getProperty(name);

      Object plainValue = metaValueFactory.unwrap(value, propertyInfo.getType());
      try
      {
         propertyInfo.set(object, plainValue);
      }
      catch (Throwable t)
      {
         throw new UndeclaredThrowableException(t);
      }
   }

   /**
    * 
    * @param methodInfo
    * @param opAnnotation
    * @return the managed operation
    */
   protected ManagedOperation getManagedOperation(MethodInfo methodInfo, ManagementOperation opAnnotation)
   {
      String name = methodInfo.getName();
      String description = opAnnotation.description();
      Impact impact = opAnnotation.impact();
      ManagementParameter[] params = opAnnotation.params();
      ParameterInfo[] paramInfo = methodInfo.getParameters();
      TypeInfo returnInfo = methodInfo.getReturnType();
      MetaType returnType = metaTypeFactory.resolve(returnInfo);
      ArrayList<ManagedParameter> mparams = new ArrayList<ManagedParameter>();
      Class<? extends ManagedParameterConstraintsPopulatorFactory> opConstraintsFactor = opAnnotation.constraintsFactory();

      if( paramInfo != null )
      {
         for(int i = 0; i < paramInfo.length; i ++)
         {
            ParameterInfo pinfo = paramInfo[i];
            String pname = pinfo.getName();
            String pdescription = null;
            ManagementParameter mpa = null;
            // Look to ManagementParameter for info
            if (i < params.length)
            {
               mpa = params[i];
               if (mpa.name().equals(AnnotationDefaults.EMPTY_STRING) == false)
                  pname = mpa.name();
               if (mpa.description().equals(AnnotationDefaults.EMPTY_STRING) == false)
                  pdescription = mpa.description();
            }
            // Generate a name if there is none
            if (pname == null)
               pname = "arg#" + i;
            Fields fields =  new DefaultFieldsImpl(pname);
            if (pdescription != null)
               fields.setField(Fields.DESCRIPTION, pdescription);
            MetaType metaType = metaTypeFactory.resolve(pinfo.getParameterType());
            fields.setField(Fields.META_TYPE, metaType);
            // Delegate others (legal values, min/max etc.) to the constraints factory
            try
            {
               Class<? extends ManagedParameterConstraintsPopulatorFactory> factoryClass = opConstraintsFactor;
               if (factoryClass == ManagementParameter.NULL_CONSTRAINTS.class)
               {
                  if (mpa != null)
                     factoryClass = mpa.constraintsFactory();
               }
               ManagedParameterConstraintsPopulatorFactory factory = factoryClass.newInstance();
               ManagedParameterConstraintsPopulator populator = factory.newInstance();
               if (populator != null)
                  populator.populateManagedParameter(name, pinfo, fields);
            }
            catch(Exception e)
            {
               log.debug("Failed to populate constraints for: "+pinfo, e);
            }

            ManagedParameterImpl mp = new ManagedParameterImpl(fields);
            mparams.add(mp);
         }
      }
      ManagedParameter[] parameters = new ManagedParameter[mparams.size()];
      mparams.toArray(parameters);

      return new ManagedOperationImpl(name, description, impact, parameters, returnType);
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
         ManagedObjectBuilder builder = builders.get(clazz);
         if (builder != null)
            return builder;
      }
      return this;
   }

   /**
    * Get the instance factory for a class
    * 
    * @param clazz the class
    * @return the InstanceClassFactory
    */
   @SuppressWarnings("unchecked")
   public <T extends Serializable> InstanceClassFactory<T> getInstanceClassFactory(Class<T> clazz)
   {
      synchronized (instanceFactories)
      {
         InstanceClassFactory factory = instanceFactories.get(clazz);
         if (factory != null)
            return factory;
      }
      return (InstanceClassFactory<T>)this;
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

   protected Collection<?> getAsCollection(Object value)
   {
      if( value.getClass().isArray() )
         return Arrays.asList(value);
      else if (value instanceof Collection)
         return Collection.class.cast(value);
      return null;
   }

   /**
    * Look for ctor(Fields)
    * @param factory - the ManagedProperty implementation class
    * @param fields - the fields to pass to the ctor
    * @return the managed property
    */
   protected ManagedProperty getManagedProperty(Class<? extends ManagedProperty> factory, Fields fields)
   {
      return createManagedProperty(factory, fields);
   }
}
