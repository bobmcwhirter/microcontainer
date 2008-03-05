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
package org.jboss.managed.plugins.factory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.jboss.managed.api.DeploymentTemplateInfo;
import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.api.ManagedProperty;
import org.jboss.managed.api.annotation.ManagementProperty;
import org.jboss.managed.plugins.BasicDeploymentTemplateInfo;

/**
 * Creates the DeploymentTemplateInfo from the ManagedObject view.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class DeploymentTemplateInfoFactory
{
   /**
    * Create a DeploymentTemplateInfo from the ManagedObject view. This is
    * based on locating the ManagedPropertys with a ManagementProperty
    * annotation with a includeInTemplate=true field.
    * @param mo - the ManagedObject for the deployment template
    * @param name - the name of the deployment template
    * @param description - a description of the deployment template
    * @return a DeploymentTemplateInfo containing the template properties.
    */
   public DeploymentTemplateInfo createTemplateInfo(ManagedObject mo, String name,
         String description)
   {
      Map<String, ManagedProperty> infoProps = new HashMap<String, ManagedProperty>();
      Map<String, ManagedProperty> props = mo.getProperties();
      if(props != null)
      {
         for(ManagedProperty prop : props.values())
         {
            Map<String, Annotation> pannotations = prop.getAnnotations();
            if(pannotations != null)
            {
               ManagementProperty mp = (ManagementProperty) pannotations.get(ManagementProperty.class.getName());
               if(mp != null && mp.includeInTemplate())
                  infoProps.put(prop.getName(), prop);
            }
         }
      }
      DeploymentTemplateInfo info = new BasicDeploymentTemplateInfo(name, description, infoProps);
      return info;
   }
}
