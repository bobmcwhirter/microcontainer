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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * ManagedObject.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ManagedObject implements Serializable
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -2588364350006686542L;

   /** The properties */
   private Set<ManagedProperty> properties;
   
   /**
    * Create a new ManagedObject
    * 
    * @param properties the properties 
    */
   public ManagedObject(Set<ManagedProperty> properties)
   {
      if (properties == null)
         properties = Collections.emptySet();
      this.properties = properties;
   }
   
   // TODO identity, deployment info, scope, etc.
   
   /**
    * Get the property names
    * 
    * @return the property names
    */
   public Set<String> getPropertyNames()
   {
      Set<String> result = new HashSet<String>(properties.size());
      for (ManagedProperty property : properties)
         result.add(property.getName());
      return result;
   }
   
   /**
    * Get a property
    * 
    * @param name the name
    * @return the property
    */
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
   
   /**
    * Get the properties
    * 
    * @return the properties
    */
   public Set<ManagedProperty> getProperties()
   {
      return Collections.unmodifiableSet(properties);
   }

   /**
    * Get the real properties
    * 
    * @return the properties
    */
   protected Set<ManagedProperty> getManagedProperties()
   {
      return properties;
   }
   
   // TODO state, lifecycle, on-demand, etc.
   
   // TODO statistics
   
   // TODO listener api
}
