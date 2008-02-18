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
package org.jboss.test.managed.factory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Set;

import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.api.ManagedProperty;
import org.jboss.managed.api.factory.ManagedObjectFactory;
import org.jboss.managed.plugins.factory.AbstractManagedObjectFactory;
import org.jboss.managed.spi.factory.ManagedObjectBuilder;
import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.types.GenericMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.MetaTypeFactory;
import org.jboss.metatype.api.values.GenericValueSupport;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.MetaValueFactory;
import org.jboss.test.BaseTestCase;
import org.jboss.util.Strings;

/**
 * AbstractManagedObjectFactoryTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractManagedObjectFactoryTest extends BaseTestCase
{
   /** The managed object factory */
   private static final ManagedObjectFactory managedObjectFactory = ManagedObjectFactory.getInstance();
   
   /** The meta type factory */
   private static final MetaTypeFactory metaTypeFactory = MetaTypeFactory.getInstance();
   
   /** The meta value factory */
   private static final MetaValueFactory metaValueFactory = MetaValueFactory.getInstance();
   
   /**
    * Create a new AbstractManagedObjectFactoryTest.
    * 
    * @param name the test name
    */
   public AbstractManagedObjectFactoryTest(String name)
   {
      super(name);
   }
   
   /**
    * Initialize a managed object
    * 
    * @param object the object
    * @return the managed object
    */
   protected ManagedObject initManagedObject(Serializable object)
   {
      ManagedObject result = managedObjectFactory.initManagedObject(object, null, null);
      getLog().debug("Inited managed: " + result + " for object=" + Strings.defaultToString(object));
      return result;
   }

   /**
    * Create a managed object
    * 
    * @param <T> the type
    * @param clazz the type
    * @return the managed object
    */
   protected <T extends Serializable> ManagedObject createManagedObject(Class<T> clazz)
   {
      ManagedObject result = managedObjectFactory.createManagedObject(clazz);
      getLog().debug("Created managed: " + result + " for class=" + clazz.getName());
      return result;
   }

   /**
    * Set a builder for a class
    * 
    * @param clazz the class
    * @param builder the builder
    */
   protected void setBuilder(Class<?> clazz, ManagedObjectBuilder builder)
   {
      managedObjectFactory.setBuilder(clazz, builder);
   }
   
   /**
    * Resolve a meta type
    * 
    * @param clazz the class
    * @return the meta type
    */
   protected MetaType<?> resolve(Class<?> clazz)
   {
      return metaTypeFactory.resolve(clazz);
   }
   
   /**
    * Create and check a managed object
    * 
    * @param clazz the object
    * @return the managed obejct
    */ 
   protected ManagedObject createAndCheckDefaultManagedObject(Class<? extends Serializable> clazz)
   {
      ManagedObject managedObject = createManagedObject(clazz);
      checkManagedObjectDefaults(clazz, managedObject);
      checkDefaultManagedProperties(managedObject, clazz);
      return managedObject;
   }
   
   /**
    * Initialize and check a managed object
    * 
    * @param test the object
    * @return the managed object
    */
   protected ManagedObject checkManagedObject(Serializable test)
   {
      ManagedObject managedObject = initManagedObject(test);
      checkManagedObjectDefaults(test.getClass(), managedObject);
      checkDefaultManagedProperties(managedObject, test.getClass());
      return managedObject;
   }

   /**
    * Check managed object defaults
    * 
    * @param clazz the class
    * @param managedObject the managed object
    */
   protected void checkManagedObjectDefaults(Class<? extends Serializable> clazz, ManagedObject managedObject)
   {
      checkManagedObjectDefaults(clazz, managedObject, null);
   }
   
   /**
    * Check managed object defaults
    * 
    * @param clazz the class
    * @param managedObject the managed object
    * @param expectedAttachment the expected attachment
    */
   protected void checkManagedObjectDefaults(Class<? extends Serializable> clazz, ManagedObject managedObject, Object expectedAttachment)
   {
      checkManagedObject(clazz, managedObject, clazz.getName(), expectedAttachment);
   }
   
   /**
    * Check a managed object
    * 
    * @param clazz the class
    * @param managedObject the managed object
    * @param name the managed object name
    * @param expectedAttachment the expected attachment
    */
   protected void checkManagedObject(Class<? extends Serializable> clazz, ManagedObject managedObject, String name, Object expectedAttachment)
   {
      getLog().debug("CheckMangedObject: clazz=" + clazz.getName() + " managedObject=" + managedObject + " name=" + name + " expectedAttachment=" + Strings.defaultToString(expectedAttachment));
      assertNotNull(managedObject);
      String managedObjectName = managedObject.getName();
      getLog().debug("... name=" + managedObjectName);
      assertEquals(name, managedObjectName);
      Object attachment = managedObject.getAttachment(); 
      getLog().debug("... attachment=" + Strings.defaultToString(attachment));
      assertInstanceOf(attachment, clazz, false);
      if (expectedAttachment != null)
         assertTrue("ManagedObject should have the correct attachment expected " + Strings.defaultToString(expectedAttachment) + " got " + Strings.defaultToString(attachment), expectedAttachment == attachment);
   }
   
   /**
    * Check default managed properties
    * 
    * @param managedObject the managed object
    * @param clazz the class
    */
   protected void checkDefaultManagedProperties(ManagedObject managedObject, Class<?> clazz)
   {
      CompositeMetaType metaType = assertInstanceOf(resolve(clazz), CompositeMetaType.class);
      Set<String> expectedNames = metaType.itemSet();
      Set<String> propertyNames = managedObject.getPropertyNames();
      getLog().debug("checkDefaultProperties");
      getLog().debug("... expectedNames=" + expectedNames);
      getLog().debug("..... actualNames=" + propertyNames);
      assertEquals("Expected properties " + expectedNames + " got " + propertyNames, expectedNames.size(), propertyNames.size());
      for (String name : expectedNames)
      {
         ManagedProperty managedProperty = managedObject.getProperty(name);
         assertNotNull("Expected property '" + name + "' in " + propertyNames, managedProperty);
         assertEquals(managedObject, managedProperty.getManagedObject());
      }
   }
   
   /**
    * Check managed properties
    * 
    * @param managedObject the managed object
    * @param names the expected property names
    */
   protected void checkManagedProperties(ManagedObject managedObject, String... names)
   {
      Set<String> propertyNames = managedObject.getPropertyNames();
      getLog().debug("checkManagedProperties");
      getLog().debug("... expectedNames=" + Arrays.asList(names));
      getLog().debug("..... actualNames=" + propertyNames);
      assertEquals("Expected properties " + Arrays.asList(names) + " got " + propertyNames, names.length, propertyNames.size());
      for (String name : names)
      {
         ManagedProperty managedProperty = managedObject.getProperty(name);
         assertNotNull("Expected property '" + name + "' in " + propertyNames, managedProperty);
         assertEquals(managedObject, managedProperty.getManagedObject());
      }
   }
   
   /**
    * Check property defaults
    * 
    * @param managedObject the managed object
    * @param name the property name
    * @param clazz the property class
    */
   protected void checkPropertyDefaults(ManagedObject managedObject, String name, Class<?> clazz)
   {
      checkPropertyDefaults(managedObject, name, clazz, null);
   }
   
   /**
    * Check property defaults
    * 
    * @param managedObject the managed object
    * @param name the property name
    * @param clazz the property class
    * @param value the expected property value
    */
   protected void checkPropertyDefaults(ManagedObject managedObject, String name, Class<?> clazz, Object value)
   {
      checkProperty(managedObject, name, clazz, name, false, value);
   }
   
   /**
    * Check property
    * 
    * @param managedObject the managed object
    * @param name the property name
    * @param clazz the property class
    * @param description the property description
    * @param mandatory whether the property is expected mandatory
    * @param value the expected property value
    */
   protected void checkProperty(ManagedObject managedObject, String name, Class<?> clazz, String description, boolean mandatory, Object value)
   {
      MetaType<?> expectedType = resolve(clazz);
      MetaValue expectedValue = metaValueFactory.create(value, clazz);
      checkProperty(managedObject, name, expectedType, expectedValue, description, mandatory);
   }
   
   /**
    * Check a property is a managed object
    * 
    * @param managedObject the managed object
    * @param name the property name
    * @param description the property description
    * @param mandatory whether the property is expected mandatory
    * @param value the expected property value
    */
   protected void checkPropertyIsManagedObject(ManagedObject managedObject, String name, String description, boolean mandatory, Object value)
   {
      GenericMetaType expectedType = AbstractManagedObjectFactory.MANAGED_OBJECT_META_TYPE;
      MetaValue expectedValue = new GenericValueSupport(expectedType, initManagedObject((Serializable) value));
      checkProperty(managedObject, name, expectedType, expectedValue, description, mandatory);
   }
   
   /**
    * Check a property
    * 
    * @param managedObject the managed object
    * @param name the property name
    * @param expectedType the expected meta type
    * @param expectedValue the expected meta value
    * @param description the property description
    * @param mandatory whether the property is expected mandatory
    */
   protected void checkProperty(ManagedObject managedObject, String name, MetaType<?> expectedType, MetaValue expectedValue, String description, boolean mandatory)
   {
      getLog().debug("checkProperty name=" + name + " type=" + expectedType + " desc=" + description + " mandatory=" + mandatory + " value=" + expectedValue);
      ManagedProperty managedProperty = managedObject.getProperty(name);
      getLog().debug("... managedProperty=" + managedProperty);
      assertNotNull("Expected property " + name + " in " + managedObject.getPropertyNames(), managedProperty);
      String propertyName = managedProperty.getName();
      getLog().debug("... name=" + propertyName);
      assertEquals("Property '" + name + "' has the wrong name", name, propertyName);
      String propertyDescription = managedProperty.getDescription();
      getLog().debug("... description=" + propertyDescription);
      assertEquals("Property '" + name + "' has the wrong description", description, propertyDescription);
      boolean propertyMandatory = managedProperty.isMandatory();
      getLog().debug("... mandatory=" + propertyMandatory);
      assertEquals("Property '" + name + "' has the wrong mandatory flag", mandatory, propertyMandatory);
      getLog().debug("... expected metaType=" + expectedType);
      MetaType<?> actualType = managedProperty.getMetaType();
      getLog().debug("..... actual metaType=" + expectedType);
      assertEquals("Property '" + name + "' has the wrong type", expectedType, actualType);
      getLog().debug("... expected value=" + expectedValue);
      Object actualValue = managedProperty.getValue();
      getLog().debug("..... actual value=" + actualValue);
      assertEquals("Property '" + name + "' has the wrong value", expectedValue, actualValue);
   }
}
