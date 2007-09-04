/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.jboss.managed.api.Fields;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.SimpleValue;

/**
 * A default implementation of the Fields interface.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class DefaultFieldsImpl
   implements Fields
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1;

   /** The fields */
   private HashMap<String, Serializable> fields = new HashMap<String, Serializable>();

   /**
    * Create a new DefaultFieldsImpl.
    */
   public DefaultFieldsImpl()
   {      
   }

   /**
    * Create a new DefaultFieldsImpl.
    * 
    * @param name the property name
    */
   public DefaultFieldsImpl(String name)
   {
      this.setName(name);
   }

   /**
    * Get the property name
    * 
    * @return the name
    */
   public String getName()
   {
      return getField(NAME, String.class);
   }

   /**
    * Set the property name
    * 
    * @param name the name
    */
   public void setName(String name)
   {
      setField(NAME, name);
   }

   /**
    * Get the description
    * 
    * @return the description
    */
   public String getDescription()
   {
      return getField(DESCRIPTION, String.class);
   }

   /**
    * Set the description
    * 
    * @param description the description
    */
   public void setDescription(String description)
   {
      setField(DESCRIPTION, description);
   }

   /**
    * Get the meta type
    * 
    * @return the meta type
    */
   public MetaType getMetaType()
   {
      return getField(META_TYPE, MetaType.class);
   }
   
   /**
    * Set the meta type
    * 
    * @param type the meta type
    */
   public void setMetaType(MetaType type)
   {
      setField(META_TYPE, type);
   }

   /**
    * Get the value
    * 
    * @return the value
    */
   public Object getValue()
   {
      return getField(VALUE);
   }
   
   /**
    * Set the value
    * 
    * @param value the value
    */
   public void setValue(Serializable value)
   {
      setField(VALUE, value);
   }

   /**
    * Get the legal values
    * 
    * @return the values
    */
   @SuppressWarnings("unchecked")
   public Set<MetaValue> getLegalValues()
   {
      return getField(LEGAL_VALUES, Set.class);
   }
   
   /**
    * Set the legal values
    * 
    * @param values the values
    */
   public void setLegalValues(Set<MetaValue> values)
   {
      setField(LEGAL_VALUES, (Serializable)values);
   }

   /**
    * Get the minimum value
    * 
    * @return the minimum value
    */
   public Comparable getMinimumValue()
   {
      return getField(MINIMUM_VALUE, Comparable.class);
   }
   
   /**
    * Set the minimum value
    * 
    * @param value the value
    */
   public void setMinimumValue(Comparable value)
   {
      setField(MINIMUM_VALUE, (Serializable)value);
   }

   /**
    * Get the maximum value
    * 
    * @return the value
    */
   public Comparable getMaximumValue()
   {
      return getField(MAXIMUM_VALUE, Comparable.class);
   }
   
   /**
    * Get the maximum value
    * 
    * @param value the value
    */
   public void setMaximumValue(Comparable value)
   {
      setField(MAXIMUM_VALUE, (Serializable)value);
   }

   /**
    * Get whether the property is mandatory
    * 
    * @return true when mandaotry
    */
   public boolean isMandatory()
   {
      Boolean result = getField(MANDATORY, Boolean.class);
      if (result == null)
         return false;
      return result;
   }
   
   /**
    * Set the mandatory value
    * 
    * @param flag true when mandatory
    */
   public void setMandatory(boolean flag)
   {
      if (flag)
         setField(MANDATORY, flag);
      else
         setField(MANDATORY, null);
   }

   /**
    * Get a field
    * 
    * @param name the field name
    */
   public Serializable getField(String name)
   {
      return fields.get(name);
   }

   /**
    * Set a field
    * 
    * @param name the field name
    * @param value the value
    */
   public void setField(String name, Serializable value)
   {
      fields.put(name, value);
   }

   /**
    * Get a field
    * 
    * @param <T> the expected type
    * @param fieldName the field name
    * @param expected the expected type
    * @return the field value
    */
   @SuppressWarnings("unchecked")
   public <T> T getField(String fieldName, Class<T> expected)
   {
      if (fieldName == null)
         throw new IllegalArgumentException("Null field name");
      if (expected == null)
         throw new IllegalArgumentException("Null expected type");
      
      Serializable field = getField(fieldName);
      
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

   /*
   private void writeObject(java.io.ObjectOutputStream out)
      throws IOException
   {
      for (Map.Entry<String, Serializable> entry : fields.entrySet())
      {
         
      }
   }
   private void readObject(java.io.ObjectInputStream in)
      throws IOException, ClassNotFoundException
   {
   
   }
   */
}
