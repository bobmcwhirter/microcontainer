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
package org.jboss.managed.api;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Set;

import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.SimpleValue;

/**
 * ManagedProperty.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ManagedProperty implements Serializable
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 2268454772998030799L;
   
   /** The serialized form */
   private static final ObjectStreamField[] serialPersistentFields =
      new ObjectStreamField[]
      {
         new ObjectStreamField("fields", Fields.class),
      };

   /** The fields */
   private Fields fields;

   /** The property name */
   private transient String name;
   
   /**
    * Create a new ManagedProperty.
    * 
    * @param fields the fields
    * @throws IllegalArgumentException for null fields
    */
   public ManagedProperty(Fields fields)
   {
      if (fields == null)
         throw new IllegalArgumentException("Null fields");
      this.fields = fields;
      
      name = getField(Fields.NAME, String.class);
      if (name == null)
         throw new IllegalArgumentException("No " + Fields.NAME + " in fields");
   }
   
   /**
    * Get the fields
    * 
    * @return the fields
    */
   public Fields getFields()
   {
      return fields;
   }
   
   /**
    * Get a field
    *
    * TODO general reconstruction code for metatypes
    * @param <T> the expected type
    * @param fieldName the field name
    * @param expected the expected type
    * @return the value
    */
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
   
   /**
    * Set a field
    *
    * TODO metaType stuff
    * @param fieldName the field name
    * @param value the value
    */
   public void setField(String fieldName, Serializable value)
   {
      if (fieldName == null)
         throw new IllegalArgumentException("Null field name");
      
      getFields().setField(fieldName, value);
   }
   
   /**
    * Get the property's name
    * 
    * @return the property's name
    */
   public String getName()
   {
      return name;
   }

   /**
    * Get the description
    * 
    * @return the description
    */
   public String getDescription()
   {
      return getField(Fields.DESCRIPTION, String.class);
   }

   /**
    * Get the type
    * 
    * @return the type
    */
   public MetaType getMetaType()
   {
      return getField(Fields.META_TYPE, MetaType.class);
   }

   /**
    * Get the value
    * 
    * @return the value
    */
   public Object getValue()
   {
      return getField(Fields.VALUE, Object.class);
   }

   /**
    * Set the value
    * 
    * @param value the value
    */
   public void setValue(Serializable value)
   {
      setField(Fields.VALUE, value);
   }

   /**
    * Get the legal values
    * 
    * @return the legal values
    */
   @SuppressWarnings("unchecked")
   public Set<MetaValue> getLegalValues()
   {
      return getField(Fields.LEGAL_VALUES, Set.class);
   }

   /**
    * Get the minimum value
    * 
    * @return the minimum value
    */
   public MetaValue getMinimumValue()
   {
      return getField(Fields.MINIMUM_VALUE, MetaValue.class);
   }

   /**
    * Get the miximum value
    * 
    * @return the maximum value
    */
   public MetaValue getMaximumValue()
   {
      return getField(Fields.MAXIMUM_VALUE, MetaValue.class);
   }

   /**
    * Check whether this is a valid value
    * 
    * @param value the value
    * @return null for a valid value, an error message otherwise
    */
   public String checkValidValue(Serializable value)
   {
      // TODO check min/max/etc.
      return null;
   }
   
   /**
    * Whether the property is mandatory
    * 
    * @return true when mandatory
    */
   public boolean isMandatory()
   {
      Boolean result = getField(Fields.MANDATORY, Boolean.class);
      if (result == null)
         return false;
      return result.booleanValue();
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
      return name.equals(other.getName());
   }
   
   /**
    * Create a new ManagedProperty.
    * 
    * @param fields the fields
    * @throws IllegalArgumentException for null fields
    */
   private void init(Fields fields)
   {
      if (fields == null)
         throw new IllegalArgumentException("Null fields");
      this.fields = fields;
      
      name = getField(Fields.NAME, String.class);
      if (name == null)
         throw new IllegalArgumentException("No " + Fields.NAME + " in fields");
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
   {
      ObjectInputStream.GetField getField = in.readFields();
      Fields fields = (Fields) getField.get("fields", null);
      try
      {
         init(fields);
      }
      catch (Exception e)
      {
         throw new RuntimeException("Error deserializing managed property", e);
      }
   }
}
