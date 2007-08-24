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
package org.jboss.deployers.plugins.managed;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import org.jboss.deployers.spi.deployer.managed.ManagedComponentCreator;
import org.jboss.deployers.spi.deployer.managed.ManagedDeploymentCreator;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.managed.api.ManagedDeployment;
import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.api.ManagedDeployment.DeploymentPhase;
import org.jboss.managed.api.factory.ManagedObjectFactory;
import org.jboss.managed.plugins.ManagedDeploymentImpl;

/**
 * Create a ManagedDeployment for a DeploymentUnit and its ManagedObjects
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class DefaultManagedDeploymentCreator
   implements ManagedDeploymentCreator
{
   private ManagedObjectFactory moFactory = ManagedObjectFactory.getInstance();
   /** The metadata type to ManagedComponent handlers */
   private Map<Class, ManagedComponentCreator> mdCreators;

   public ManagedObjectFactory getMoFactory()
   {
      return moFactory;
   }
   public void setMoFactory(ManagedObjectFactory moFactory)
   {
      this.moFactory = moFactory;
   }

   public <T> void addManagedComponentCreator(ManagedComponentCreator<T> mcc)
   {
      Type type = mcc.getClass().getGenericInterfaces()[0];
      ParameterizedType pt = (ParameterizedType) type;
      Class ptType = (Class) pt.getActualTypeArguments()[0];
      mdCreators.put(ptType, mcc);
   }

   public ManagedDeployment build(DeploymentUnit unit,
         Map<String, ManagedObject> unitMOs,
         ManagedDeployment parent)
   {
      DeploymentPhase phase = unit.getAttachment(DeploymentPhase.class);
      if( phase == null )
         phase = DeploymentPhase.APPLICATION;
      ManagedDeployment md = new ManagedDeploymentImpl(unit.getName(), unit.getSimpleName(),
            phase, parent, unitMOs);

      return md;
   }

}
