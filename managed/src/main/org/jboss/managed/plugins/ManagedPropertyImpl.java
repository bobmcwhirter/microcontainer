/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.managed.plugins;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Set;

import org.jboss.managed.api.Fields;
import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.api.ManagedProperty;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.SimpleValue;

/**
 * ManagedProperty.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ManagedPropertyImpl implements ManagedProperty
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 2268454772998030799L;
   
   /** The serialized form */
   private static final ObjectStreamField[] serialPersistentFields =
      new ObjectStreamField[]
      {
         new ObjectStreamField("managedObject", ManagedObject.class),
         new ObjectStreamField("fields", Fields.class),
      };

   /** The managed object */
   private ManagedObject managedObject;
   
   /** The fields */
   private Fields fields;

   /** The property name */
   private transient String name;
   
   /**
    * Create a new ManagedProperty.
    * 
    * @param managedObject the managed object
    * @param fields the fields
    * @throws IllegalArgumentException for null fields
    */
   public ManagedPropertyImpl(ManagedObject managedObject, Fields fields)
   {
      init(managedObject, fields);
   }
   
   public ManagedObject getManagedObject()
   {
      return managedObject;
   }

   public Fields getFields()
   {
      return fields;
   }
   
   // TODO general reconstruction code for metatypes
   @SuppressWarnings("unchecked")
   public <T> T getField(String fieldName, Class<T> expected)
   {
      if (fieldName == null)
         throw new IllegalArgumentException("Null field name");
      if (expected == null)
         throw new IllegalArgumentException("Null expected type");
      
      Serializable field = getFields().getField(fieldName);
      
      if (field == null)
         return null;

      if (expected.isInstance(field))
         return expected.cast(field);
      
      if (field instanceof SimpleValue)
      {
         SimpleValue value = (SimpleValue) field;
         Object result = value.getValue();
         if (result == null)
            return null;
         return expected.cast(result);
      }
      
      throw new IllegalStateException("Field " + fieldName + " with value " + field + " is  a of the expected type: " + expected.getName());
   }
   
   // TODO metaType stuff
   public void setField(String fieldName, Serializable value)
   {
      if (fieldName == null)
         throw new IllegalArgumentException("Null field name");
      
      getFields().setField(fieldName, value);
   }
   
   public String getName()
   {
      return name;
   }

   public String getDescription()
   {
      return getField(Fields.DESCRIPTION, String.class);
   }

   public MetaType getMetaType()
   {
      return getField(Fields.META_TYPE, MetaType.class);
   }

   public Object getValue()
   {
      return getField(Fields.VALUE, Object.class);
   }

   public void setValue(Serializable value)
   {
      setField(Fields.VALUE, value);
   }

   @SuppressWarnings("unchecked")
   public Set<MetaValue> getLegalValues()
   {
      return getField(Fields.LEGAL_VALUES, Set.class);
   }

   public Comparable getMinimumValue()
   {
      return getField(Fields.MINIMUM_VALUE, Comparable.class);
   }

   public Comparable getMaximumValue()
   {
      return getField(Fields.MAXIMUM_VALUE, Comparable.class);
   }

   public String checkValidValue(Serializable value)
   {
      // TODO check min/max/etc.
      return null;
   }
   
   public boolean isMandatory()
   {
      Boolean result = getField(Fields.MANDATORY, Boolean.class);
      if (result == null)
         return false;
      return result;
   }

   @Override
   public String toString()
   {
      return "ManagedProperty{" + name + "}"; 
   }

   @Override
   public int hashCode()
   {
      return name.hashCode(); 
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;
      if (obj == null || obj instanceof ManagedProperty == false)
         return false;
      
      ManagedProperty other = (ManagedProperty) obj;
      return getName().equals(other.getName()) && getManagedObject().equals(other.getManagedObject()) ;
   }
   
   /**
    * Initialise a ManagedPropertyImpl.
    * 
    * @param managedObject the managed object
    * @param fields the fields
    * @throws IllegalArgumentException for null fields
    */
   private void init(ManagedObject managedObject, Fields fields)
   {
      if (managedObject == null)
         throw new IllegalArgumentException("Null managed object");
      if (fields == null)
         throw new IllegalArgumentException("Null fields");
      
      this.managedObject = managedObject;
      this.fields = fields;
      
      name = getField(Fields.NAME, String.class);
      if (name == null)
         throw new IllegalArgumentException("No " + Fields.NAME + " in fields");
   }

   /**
    * Read from a stream
    * 
    * @param in the stream
    * @throws IOException for IO problem
    * @throws ClassNotFoundException for a classloading problem
    */
   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
   {
      ObjectInputStream.GetField getField = in.readFields();
      ManagedObject managedObject = (ManagedObject) getField.get("managedObject", null);
      Fields fields = (Fields) getField.get("fields", null);
      try
      {
         init(managedObject, fields);
      }
      catch (Exception e)
      {
         throw new RuntimeException("Error deserializing managed property", e);
      }
   }
}
