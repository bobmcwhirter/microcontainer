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
package org.jboss.test.deployers.deployer.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.managed.api.annotation.ManagementComponent;
import org.jboss.managed.api.annotation.ManagementObject;
import org.jboss.managed.api.annotation.ManagementProperty;

/**
 * Test datasource deployment like metadata
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
@ManagementObject
public class DSMetaData implements Serializable
{
   private static final long serialVersionUID = 1;
   private String diplayName;
   private List<ConnMetaData> deployments = new ArrayList<ConnMetaData>();

   @ManagementProperty(name="display-name", use={})
   public String getDiplayName()
   {
      return diplayName;
   }

   public void setDiplayName(String diplayName)
   {
      this.diplayName = diplayName;
   }

   public void addManagedConnectionFactoryDeployment(ConnMetaData deployment)
   {
      this.deployments.add(deployment);
   }

   @ManagementComponent
   public List<ConnMetaData> getDeployments()
   {
      return Collections.unmodifiableList(deployments);
   }

}
