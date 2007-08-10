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
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Map;
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
   private static final long serialVersionUID = 2;
   /* writeObject format:
    * - int version
    * - Fields fields
    * - ManagedObject managedObject
    */
   private static final int VERSION1 = 1;
   /** The serialization version used by writeObject */
   private static final int STREAM_VERSION = VERSION1;

   /** The managed object */
   private ManagedObject managedObject;
   /** The managed object target for a ManagementObjectRef */
   private ManagedObject targetManagedObject;
   
   /** The fields */
   private Fields fields;

   /** The property name */
   private transient String name;

   /**
    * Create a new ManagedProperty that is not associated to
    * a ManagedObject.
    *
    * @param name the managed property name
    * @throws IllegalArgumentException for null fields or
    *    missing Fields.NAME
    */
   public ManagedPropertyImpl(String name)
   {
      this(null, new DefaultFieldsImpl(name));
   }

   /**
    * Create a new ManagedProperty that is not associated to
    * a ManagedObject.
    * 
    * @param fields the fields
    * @throws IllegalArgumentException for null fields or
    *    missing Fields.NAME
    */
   public ManagedPropertyImpl(Fields fields)
   {
      this(null, fields);
   }

   /**
    * Create a new ManagedProperty.
    * 
    * @param managedObject the managed object, may be null
    * @param fields the fields
    * @throws IllegalArgumentException for null fields or
    *    missing Fields.NAME
    */
   public ManagedPropertyImpl(ManagedObject managedObject, Fields fields)
   {
      init(managedObject, fields);
   }
   
   public ManagedObject getManagedObject()
   {
      return managedObject;
   }

   /**
    * Set managed object
    * 
    * @param managedObject the managed object
    */
   public void setManagedObject(ManagedObject managedObject)
   {
      this.managedObject = managedObject;
   }

   public ManagedObject getTargetManagedObject()
   {
      return targetManagedObject;
   }
   public void setTargetManagedObject(ManagedObject target)
   {
      this.targetManagedObject = target;
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
      
      throw new IllegalStateException("Field " + fieldName + " with value " + field + " is not of the expected type: " + expected.getName());
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

   public String getMappedName()
   {
      return getField(Fields.MAPPED_NAME, String.class);
   }

   public String getDescription()
   {
      return getField(Fields.DESCRIPTION, String.class);
   }
   
   /**
    * Set the description
    * 
    * @param description the description
    */
   public void setDescription(String description)
   {
      setField(Fields.DESCRIPTION, description);
   }


   /**
    * Get the annotations associated with the property
    * @return the annotations associated with the property
    */
   public Map<String, Annotation> getAnnotations()
   {
      Object set = getField(Fields.ANNOTATIONS, Object.class);
      return (Map<String, Annotation>) set;
   }
   public void setAnnotations(Map<String, Annotation> annotations)
   {
      setField(Fields.ANNOTATIONS, (Serializable) annotations);      
   }

   public MetaType getMetaType()
   {
      return getField(Fields.META_TYPE, MetaType.class);
   }
   
   /**
    * Set the meta type
    * 
    * @param type the meta type
    */
   public void setMetaType(MetaType type)
   {
      setField(Fields.META_TYPE, type);
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
   
   /**
    * Set the legal values
    * 
    * @param values the values
    */
   public void setLegalValues(Set<MetaValue> values)
   {
      setField(Fields.LEGAL_VALUES, (Serializable)values);
   }

   public Comparable getMinimumValue()
   {
      return getField(Fields.MINIMUM_VALUE, Comparable.class);
   }
   
   /**
    * Set the minimum value
    * 
    * @param value the value
    */
   public void setMinimumValue(Comparable value)
   {
      setField(Fields.MINIMUM_VALUE, (Serializable)value);
   }

   public Comparable getMaximumValue()
   {
      return getField(Fields.MAXIMUM_VALUE, Comparable.class);
   }

   /**
    * Set the maximum value
    * 
    * @param value the value
    */
   public void setMaximumValue(Comparable value)
   {
      setField(Fields.MAXIMUM_VALUE, (Serializable)value);
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
   
   /**
    * Set whether the field is mandatory
    * 
    * @param flag true for mandatory
    */
   public void setMandatory(boolean flag)
   {
      if (flag)
         setField(Fields.MANDATORY, flag);
      else
         setField(Fields.MANDATORY, null);
   }

   @Override
   public String toString()
   {
      StringBuilder tmp = new StringBuilder("ManagedProperty");
      tmp.append('{');
      tmp.append(name);
      if( getMappedName() != null )
      {
         tmp.append(',');
         tmp.append(getMappedName());
      }
      tmp.append('}');
      return tmp.toString(); 
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
      return getName().equals(other.getName());
   }
   
   /**
    * Initialise a ManagedPropertyImpl.
    * 
    * @param managedObject the managed object, may be null
    * @param fields the fields
    * @throws IllegalArgumentException for null fields or
    *    missing Fields.NAME
    */
   private void init(ManagedObject managedObject, Fields fields)
   {
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
   private void readObject(ObjectInputStream in)
      throws IOException, ClassNotFoundException
   {
      int version = in.readInt();
      if( version == VERSION1 )
         readVersion1(in);
      else
         throw new InvalidObjectException("Unknown version="+version);
   }
   /**
    * Write out the property fields
    * @param out
    * @throws IOException
    */
   private void writeObject(ObjectOutputStream out)
      throws IOException
   {
      out.writeInt(STREAM_VERSION);
      out.writeObject(fields);
      out.writeObject(managedObject);
   }

   /**
    * The VERSION1 expected format: 
    * - Fields fields
    * - ManagedObject managedObject
    */
   private void readVersion1(ObjectInputStream in)
      throws IOException, ClassNotFoundException
   {
      fields = (Fields) in.readObject();
      name = getField(Fields.NAME, String.class);
      if (name == null)
         throw new IOException("No " + Fields.NAME + " in fields");
      managedObject = (ManagedObject) in.readObject();      
   }
}
