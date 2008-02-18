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
package org.jboss.managed.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.managed.api.ManagedProperty;

/**
 * A collection of ManagedComponent and structural information
 * about a deployment.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public interface ManagedDeployment
{
   /** The class of deployment */
   public enum DeploymentPhase {
      /** A deployment loaded during the server bootstrap phase */
      BOOTSTRAP,
      /** An mc/service deployment for a Deployer to be loaded after the BOOTSTRAP phase */
      DEPLOYER,
      /** Any deployment content to be loaded after the DEPLOYER phase */
      APPLICATION
   };

   /**
    * Get the full name of the associated DeploymentUnit.
    * @return full name of the DeploymentUnit
    */
   public String getName();
   /**
    * Get the simple name (x.ear) for the deployment
    * @return simple name of the deployment
    */
   public String getSimpleName();
   /**
    * Get the phase this deployment is associated with
    * @return the phase
    */
   public DeploymentPhase getDeploymentPhase();
   /**
    * Add a deployment/module type
    * @param type
    * @return true if the type was added, false if it already exists
    */
   public boolean addType(String type);
   /**
    * Get the deployment/module types.
    * @return deployment types
    */
   public Set<String> getTypes();
   /**
    * Get the deployment/module types.
    * @param types
    */
   public void setTypes(Set<String> types);

   /**
    * Get the managed property names
    * 
    * @return the property names
    */
   public Set<String> getPropertyNames();
   /**
    * Get a property
    * 
    * @param name the name
    * @return the property
    */
   public ManagedProperty getProperty(String name);
   
   /**
    * Get the properties
    * 
    * @return the properties
    */
   public Map<String, ManagedProperty> getProperties();

   /**
    * Get the managed object names
    * 
    * @return the property names
    */
   public Set<String> getManagedObjectNames();
   public Map<String, ManagedObject> getManagedObjects();
   public ManagedObject getManagedObject(String name);

   /**
    * 
    * @return the parent
    */
   public ManagedDeployment getParent();

   public ManagedComponent getComponent(String name);

   /**
    * Get the ManagedComponents for the deployment module.
    * @return ManagedComponents for the deployment module.
    */
   public Map<String, ManagedComponent> getComponents();
   /**
    * Get the nested deployment modules.
    * @return nested deployment modules.
    */
   public List<ManagedDeployment> getChildren();

   /**
    * Get the DeploymentTemplate names for components
    * that can be added to this deployment.
    * @return  the template names
    */
   public Set<String> getComponentTemplateNames();

   /**
    * Add a component to this deployment
    * @param name the name
    * @param comp the component
    */
   public void addComponent(String name, ManagedComponent comp);
   /**
    * 
    * @param name the name
    * @return true when it was removed
    */
   public boolean removeComponent(String name);

   /**
    * Get the DeploymentTemplate names for deployments
    * that can be added to this deployment.
    * @return the template names
    */
   public Set<String> getDeploymentTemplateNames();
   /**
    * Add a deployment
    * @param deplymentBaseName
    * @param info
    * @return the deployment
    */
   public ManagedDeployment addModule(String deplymentBaseName, DeploymentTemplateInfo info);
}
