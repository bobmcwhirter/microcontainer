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

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.api.ManagedOperation;
import org.jboss.managed.api.ManagedProperty;

/**
 * ManagedObjectImpl.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1.1 $
 */
public class ManagedObjectImpl implements ManagedObject
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 2L;

   /** The object name used for ManagementRef resolution */
   private String name;
   /** The name type/qualifier used for ManagementRef resolution */
   private String nameType;
   /** The attachment name */
   private String attachmentName;

   /** The attachment */
   private Serializable attachment;
   /** The object annotations <Class name, Annotation> */
   private Map<String, Annotation> annotations = Collections.emptyMap();
   /** The properties */
   private Map<String, ManagedProperty> properties;
   /** The operations */
   private Set<ManagedOperation> operations;

   /**
    * Create a new ManagedObjectImpl
    * 
    * @param name - The object name used for ManagementRef resolution
    */
   public ManagedObjectImpl(String name)
   {
      this(name, name, null, toMap(null), new HashSet<ManagedOperation>(), null);
   }
   
   /**
    * Create a new ManagedObjectImpl
    * 
    * @param name - The object name used for ManagementRef resolution
    * @param properties the properties 
    */
   public ManagedObjectImpl(String name, Set<ManagedProperty> properties)
   {
      this(name, name, null, properties, new HashSet<ManagedOperation>(), null);
   }

   /**
    * Create a new ManagedObjectImpl
    * 
    * @param name - The object name used for ManagementRef resolution
    * @param properties the properties
    * @param operations the operations
    */
   public ManagedObjectImpl(String name, Set<ManagedProperty> properties,
         HashSet<ManagedOperation> operations)
   {
      this(name, name, null, properties, operations, null);
   }

   /**
    * Create a new ManagedObjectImpl
    * 
    * @param name - The object name used for ManagementRef resolution
    * @param nameType - The name type/qualifier used for ManagementRef resolution
    * @param attachmentName the attachment name
    * @param properties the properties
    * @param operations the operations
    * @param attachment the attachment
    */
   public ManagedObjectImpl(String name, String nameType,
         String attachmentName,
         Set<ManagedProperty> properties,
         HashSet<ManagedOperation> operations, Serializable attachment)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      if (properties == null)
         throw new IllegalArgumentException("Null properties");
      
      this.name = name;
      this.properties = toMap(properties);
      this.operations = operations;
      setAttachment(attachment);
   }
   public ManagedObjectImpl(String name, String nameType,
         String attachmentName,
         Map<String, ManagedProperty> properties,
         HashSet<ManagedOperation> operations, Serializable attachment)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      if (properties == null)
         throw new IllegalArgumentException("Null properties");
      
      this.name = name;
      this.properties = properties;
      this.operations = operations;
      setAttachment(attachment);
   }

   public String getName()
   {
      return name;
   }
   public void setName(String name)
   {
      this.name = name;
   }

   public String getNameType()
   {
      return nameType;
   }
   public void setNameType(String nameType)
   {
      this.nameType = nameType;
   }

   public String getAttachmentName()
   {
      return attachmentName;
   }
   public void setAttachmentName(String attachmentName)
   {
      this.attachmentName = attachmentName;
   }

   /**
    * Get the annotations associated with the property
    * @return the annotations associated with the property
    */
   public Map<String, Annotation> getAnnotations()
   {
      return annotations;
   }
   public void setAnnotations(Map<String, Annotation> annotations)
   {
      if (this.annotations.isEmpty())
         this.annotations = new HashMap<String, Annotation>();
      this.annotations.clear();
      this.annotations.putAll(annotations);
   }

   public Set<String> getPropertyNames()
   {
      return properties.keySet();
   }
   
   public ManagedProperty getProperty(String name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      
      return properties.get(name);
   }
   
   public Map<String, ManagedProperty> getProperties()
   {
      return properties;
   }

   public Serializable getAttachment()
   {
      return attachment;
   }

   /**
    * Set the attachment.
    * 
    * @param attachment the attachment.
    */
   public void setAttachment(Serializable attachment)
   {
      this.attachment = attachment;
   }
   
   public Set<ManagedOperation> getOperations()
   {
      return operations;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;
      if (obj == null || obj instanceof ManagedObject == false)
         return false;
      
      ManagedObject other = (ManagedObject) obj;
      return getName().equals(other.getName()) && getProperties().equals(other.getProperties());
   }
   
   @Override
   public int hashCode()
   {
      return name.hashCode();
   }
   
   @Override
   public String toString()
   {
      return "ManagedObject{" + name + "}"; 
   }

   /**
    * Append the name and props 
    * @param sb the buffer to append the name and props to
    */
   protected void toString(StringBuilder sb)
   {
      sb.append("name=");
      sb.append(name);
      sb.append(", nameType=");
      sb.append(nameType);
      sb.append(", attachmentName=");
      sb.append(attachmentName);
      sb.append(", properties=");
      sb.append(properties);
   }

   private static Map<String, ManagedProperty> toMap(Set<ManagedProperty> props)
   {
      HashMap<String, ManagedProperty> properties = new HashMap<String, ManagedProperty>();
      if (props != null)
      {
         for (ManagedProperty prop : props)
         {
            properties.put(prop.getName(), prop);
         }
      }
      return properties;
   }
}
