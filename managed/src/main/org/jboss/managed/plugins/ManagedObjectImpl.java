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
import java.util.HashSet;
import java.util.Set;

import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.api.ManagedProperty;

/**
 * ManagedObjectImpl.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ManagedObjectImpl implements ManagedObject
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -2588364350006686542L;

   /** The attachment name */
   private String name;
   
   /** The attachment */
   private Serializable attachment;
   
   /** The properties */
   private Set<ManagedProperty> properties;
   
   /**
    * Create a new ManagedObjectImpl
    * 
    * @param name the attachment name
    */
   public ManagedObjectImpl(String name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      
      this.name = name;
      properties = new HashSet<ManagedProperty>();
   }
   
   /**
    * Create a new ManagedObjectImpl
    * 
    * @param name the attachment name
    * @param properties the properties 
    */
   public ManagedObjectImpl(String name, Set<ManagedProperty> properties)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      if (properties == null)
         throw new IllegalArgumentException("Null properties");
      
      this.name = name;
      this.properties = properties;
   }
   
   /**
    * Create a new ManagedObjectImpl
    * 
    * @param name the attachment name
    * @param properties the properties 
    * @param attachment the attachment
    */
   public ManagedObjectImpl(String name, Set<ManagedProperty> properties, Serializable attachment)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      if (properties == null)
         throw new IllegalArgumentException("Null properties");
      
      this.name = name;
      this.properties = properties;
      setAttachment(attachment);
   }
   
   public String getName()
   {
      return name;
   }

   public Set<String> getPropertyNames()
   {
      Set<String> result = new HashSet<String>(properties.size());
      for (ManagedProperty property : properties)
         result.add(property.getName());
      return result;
   }
   
   public ManagedProperty getProperty(String name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      
      for (ManagedProperty property : properties)
      {
         if (name.equals(property.getName()))
            return property;
      }
      return null;
   }
   
   public Set<ManagedProperty> getProperties()
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
}
