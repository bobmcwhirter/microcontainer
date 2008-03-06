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
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.config.spi.Configuration;
import org.jboss.logging.Logger;
import org.jboss.managed.api.DeploymentTemplateInfo;
import org.jboss.managed.api.Fields;
import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.api.ManagedProperty;
import org.jboss.managed.api.annotation.ManagementConstants;
import org.jboss.managed.api.annotation.ManagementObjectID;
import org.jboss.managed.api.annotation.ManagementObjectRef;
import org.jboss.managed.api.annotation.ManagementProperty;
import org.jboss.managed.api.annotation.ManagementRuntimeRef;
import org.jboss.managed.plugins.BasicDeploymentTemplateInfo;
import org.jboss.managed.plugins.DefaultFieldsImpl;
import org.jboss.managed.plugins.WritethroughManagedPropertyImpl;
import org.jboss.managed.spi.factory.ManagedPropertyConstraintsPopulator;
import org.jboss.managed.spi.factory.ManagedPropertyConstraintsPopulatorFactory;
import org.jboss.metatype.api.types.ArrayMetaType;
import org.jboss.metatype.api.types.CollectionMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.MetaTypeFactory;
import org.jboss.metatype.api.values.MetaValueFactory;
import org.jboss.reflect.spi.TypeInfo;

/**
 * Creates a DeploymentTemplateInfo from a ManagedObject view, or
 * the deployment attachment class
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class DeploymentTemplateInfoFactory
{
   private static final Logger log = Logger.getLogger(DeploymentTemplateInfoFactory.class);
   /** The configuration */
   private static final Configuration configuration = PropertyConfigurationAccess.getConfiguration();
   /** The meta type factory */
   private MetaTypeFactory metaTypeFactory = MetaTypeFactory.getInstance(); 

   /** The meta value factory */
   private MetaValueFactory metaValueFactory = MetaValueFactory.getInstance();
 
   
   public MetaTypeFactory getMetaTypeFactory()
   {
      return metaTypeFactory;
   }
   public void setMetaTypeFactory(MetaTypeFactory metaTypeFactory)
   {
      this.metaTypeFactory = metaTypeFactory;
   }
   public MetaValueFactory getMetaValueFactory()
   {
      return metaValueFactory;
   }
   public void setMetaValueFactory(MetaValueFactory metaValueFactory)
   {
      this.metaValueFactory = metaValueFactory;
   }

   /**
    * Create a DeploymentTemplateInfo from the ManagedObject view. This is
    * based on locating the ManagedPropertys with a ManagementProperty
    * annotation with a includeInTemplate=true field.
    * @param mo - the ManagedObject for the deployment template
    * @param name - the name of the deployment template
    * @param description - a description of the deployment template
    * @return a DeploymentTemplateInfo containing the template properties.
    */
   public DeploymentTemplateInfo createTemplateInfo(ManagedObject mo, String name,
         String description)
   {
      Map<String, ManagedProperty> infoProps = new HashMap<String, ManagedProperty>();
      Map<String, ManagedProperty> props = mo.getProperties();
      if(props != null)
      {
         for(ManagedProperty prop : props.values())
         {
            Map<String, Annotation> pannotations = prop.getAnnotations();
            if(pannotations != null)
            {
               ManagementProperty mp = (ManagementProperty) pannotations.get(ManagementProperty.class.getName());
               if(mp != null && mp.includeInTemplate())
                  infoProps.put(prop.getName(), prop);
            }
         }
      }
      DeploymentTemplateInfo info = new BasicDeploymentTemplateInfo(name, description, infoProps);
      return info;
   }
   /**
    * Create a DeploymentTemplateInfo by scanning the metadata attachment
    * class for ManagementProperty annotations.
    * @param attachmentClass - the metadata class to scan for ManagementProperty annotations
    * @param name - the template name
    * @param description - the template description
    * @return the DeploymentTemplateInfo instance
    * @throws Exception on failure to create the DeploymentTemplateInfo
    */
   public DeploymentTemplateInfo createTemplateInfo(Class<?> attachmentClass, String name,
         String description)
      throws Exception
   {
      return createTemplateInfo(BasicDeploymentTemplateInfo.class, attachmentClass, name,
         description);
   }
   /**
    * Create a DeploymentTemplateInfo by scanning the metadata attachment
    * class for ManagementProperty annotations.
    * @param infoClass - the DeploymentTemplateInfo implementation to use. Must
    * have a ctor with sig (String,String,Map).
    * @param attachmentClass - the metadata class to scan for ManagementProperty annotations
    * @param name - the template name
    * @param description - the template description
    * @return the DeploymentTemplateInfo instance
    * @throws Exception on failure to create the DeploymentTemplateInfo
    */
   public DeploymentTemplateInfo createTemplateInfo(Class<? extends DeploymentTemplateInfo> infoClass,
         Class<?> attachmentClass, String name,
         String description)
      throws Exception
   {      
      BeanInfo beanInfo = configuration.getBeanInfo(attachmentClass);
      Map<String, ManagedProperty> infoProps = new HashMap<String, ManagedProperty>();
      Set<PropertyInfo> propertyInfos = beanInfo.getProperties();
      if (propertyInfos != null && propertyInfos.isEmpty() == false)
      {
         for (PropertyInfo propertyInfo : propertyInfos)
         {
            ManagementProperty managementProperty = propertyInfo.getUnderlyingAnnotation(ManagementProperty.class);
            if(managementProperty != null && managementProperty.includeInTemplate())
            {
               ManagedProperty mp = createProperty(propertyInfo, managementProperty);
               infoProps.put(mp.getName(), mp);
            }
         }
      }
      Class[] parameterTypes = {String.class, String.class, Map.class};
      Constructor<? extends DeploymentTemplateInfo> ctor = infoClass.getConstructor(parameterTypes);
      DeploymentTemplateInfo info = ctor.newInstance(name, description, infoProps);
      return info;
   }

   protected ManagedProperty createProperty(PropertyInfo propertyInfo, ManagementProperty managementProperty)
   {
      boolean trace = log.isTraceEnabled();
      ManagedProperty property = null;
      // Check for a simple property
      boolean includeProperty = (managementProperty.ignored() == false);
      if (includeProperty)
      {
         ManagementObjectID id = propertyInfo.getUnderlyingAnnotation(ManagementObjectID.class);
         ManagementObjectRef ref = propertyInfo.getUnderlyingAnnotation(ManagementObjectRef.class);
         ManagementRuntimeRef runtimeRef = propertyInfo.getUnderlyingAnnotation(ManagementRuntimeRef.class);
         HashMap<String, Annotation> propAnnotations = new HashMap<String, Annotation>();
         propAnnotations.put(ManagementProperty.class.getName(), managementProperty);

         Fields fields = null;
         if (managementProperty != null)
         {
            Class<? extends Fields> factory = managementProperty.fieldsFactory();
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
               metaType = new ArrayMetaType(1, AbstractManagedObjectFactory.MANAGED_OBJECT_META_TYPE);
            else if (typeInfo.isCollection())
               metaType = new CollectionMetaType(typeInfo.getName(), AbstractManagedObjectFactory.MANAGED_OBJECT_META_TYPE);
            else
               metaType = AbstractManagedObjectFactory.MANAGED_OBJECT_META_TYPE;
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
            Class<? extends ManagedPropertyConstraintsPopulatorFactory> factoryClass = managementProperty.constraintsFactory();
            ManagedPropertyConstraintsPopulatorFactory factory = factoryClass.newInstance();
            ManagedPropertyConstraintsPopulator populator = factory.newInstance();
            if (populator != null)
            {
               Class clazz = propertyInfo.getBeanInfo().getClassInfo().getType();
               populator.populateManagedProperty(clazz, propertyInfo, fields);
            }
         }
         catch(Exception e)
         {
            log.debug("Failed to populate constraints for: "+propertyInfo, e);
         }

         if (managementProperty != null)
         {
            Class<? extends ManagedProperty> factory = managementProperty.propertyFactory();
            if (factory != ManagementProperty.NULL_PROPERTY_FACTORY.class)
               property = AbstractManagedObjectFactory.createManagedProperty(factory, fields);
         }
         // we should have write-through by default
         // use factory to change this default behavior
         if (property == null)
            property = new WritethroughManagedPropertyImpl(fields, metaValueFactory, null);
      }
      return property;
   }
}
