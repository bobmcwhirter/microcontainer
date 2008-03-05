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
package org.jboss.managed.plugins;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.jboss.managed.api.DeploymentTemplateInfo;
import org.jboss.managed.api.ManagedProperty;

/**
 * A simple bean type of implementation of DeploymentTemplateInfo
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class BasicDeploymentTemplateInfo
   implements DeploymentTemplateInfo, Serializable
{
   private static final long serialVersionUID = 1;
   /** The name the template is registered under with the profile service */
   private String name;
   /** The template description */
   private String description;
   /** The name of the ManagedProperty in the Map<String, ManagedObject> map
    * obtained from the mainDeployer.getManagedObjects(VFSDeployment)
    * for the DeploymentTemplate this info is associated with.
    */
   private String rootManagedPropertyName;
   /**
    * The template properties
    */
   private Map<String, ManagedProperty> properties;

   public BasicDeploymentTemplateInfo(String name, String description)
   {
      this(name, description, new HashMap<String, ManagedProperty>());
   }
   public BasicDeploymentTemplateInfo(String name, String description,
         Map<String, ManagedProperty> properties)
   {
      this.name = name;
      this.description = description;
      this.properties = properties;
   }

   public String getDescription()
   {
      return description;
   }

   public String getName()
   {
      return name;
   }

   public String getRootManagedPropertyName()
   {
      return rootManagedPropertyName;
   }
   public void setRootManagedPropertyName(String rootManagedPropertyName)
   {
      this.rootManagedPropertyName = rootManagedPropertyName;
   }

   public Map<String, ManagedProperty> getProperties()
   {
      return properties;
   }
   public void setProperties(Map<String, ManagedProperty> properties)
   {
      this.properties = properties;
   }
   public void addProperty(ManagedProperty property)
   {
      this.properties.put(property.getName(), property);
   }

   public String toString()
   {
      StringBuilder tmp = new StringBuilder(super.toString());
      tmp.append('{');
      tmp.append(name);
      tmp.append(",description=");
      tmp.append(description);
      tmp.append(",properties=");
      tmp.append(properties);
      tmp.append('}');
      return tmp.toString();
   }
}
