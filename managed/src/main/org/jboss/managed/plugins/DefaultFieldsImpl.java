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

import java.io.Serializable;
import java.util.HashMap;
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
   private static final long serialVersionUID = 1;

   private HashMap<String, Serializable> fields = new HashMap<String, Serializable>();

   public DefaultFieldsImpl()
   {      
   }
   public DefaultFieldsImpl(String name)
   {
      this.setName(name);
   }

   public String getName()
   {
      return getField(NAME, String.class);
   }
   public void setName(String name)
   {
      setField(NAME, name);
   }

   public String getDescription()
   {
      return getField(DESCRIPTION, String.class);
   }
   public void setDescription(String description)
   {
      setField(DESCRIPTION, description);
   }

   public MetaType getMetaType()
   {
      return getField(META_TYPE, MetaType.class);
   }
   public void setMetaType(MetaType type)
   {
      setField(META_TYPE, type);
   }

   public Object getValue()
   {
      return getField(VALUE);
   }
   public void setValue(Serializable value)
   {
      setField(VALUE, value);
   }

   @SuppressWarnings("unchecked")
   public Set<MetaValue> getLegalValues()
   {
      return getField(LEGAL_VALUES, Set.class);
   }
   public void setLegalValues(Set<MetaValue> values)
   {
      setField(LEGAL_VALUES, (Serializable)values);
   }

   public Comparable getMinimumValue()
   {
      return getField(MINIMUM_VALUE, Comparable.class);
   }
   public void setMinimumValue(Comparable value)
   {
      setField(MINIMUM_VALUE, (Serializable)value);
   }

   public Comparable getMaximumValue()
   {
      return getField(MAXIMUM_VALUE, Comparable.class);
   }
   public void setMaximumValue(Comparable value)
   {
      setField(MAXIMUM_VALUE, (Serializable)value);
   }

   public boolean isMandatory()
   {
      Boolean result = getField(MANDATORY, Boolean.class);
      if (result == null)
         return false;
      return result;
   }
   public void setMandatory(boolean flag)
   {
      setField(MANDATORY, flag);
   }

   public Serializable getField(String name)
   {
      return fields.get(name);
   }

   public void setField(String name, Serializable value)
   {
      fields.put(name, value);
   }

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

}
