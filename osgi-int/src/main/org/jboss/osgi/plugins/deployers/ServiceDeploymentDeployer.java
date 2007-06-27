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
package org.jboss.osgi.plugins.deployers;

import java.util.List;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.helpers.AbstractComponentDeployer;
import org.jboss.deployers.spi.deployer.helpers.DeploymentVisitor;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.osgi.spi.metadata.ServiceDeployment;
import org.jboss.osgi.spi.metadata.ServiceMetaData;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ServiceDeploymentDeployer extends AbstractComponentDeployer<ServiceDeployment, ServiceMetaData>
{
   /**
    * Create a new ServiceDeploymentDeployer.
    */
   public ServiceDeploymentDeployer()
   {
      setDeploymentVisitor(new ServiceDeploymentVisitor());
      setComponentVisitor(new ServiceMetaDataVisitor());
   }

   protected static void addServiceComponent(DeploymentUnit unit, ServiceMetaData service)
   {
      DeploymentUnit component = unit.addComponent(service.getId());
      component.addAttachment(ServiceMetaData.class.getName(), service);
   }

   protected static void removeServiceComponent(DeploymentUnit unit, ServiceMetaData service)
   {
      unit.removeComponent(service.getId());
   }

   /**
    * ServiceDeploymentVisitor.
    */
   private static class ServiceDeploymentVisitor implements DeploymentVisitor<ServiceDeployment>
   {
      public Class<ServiceDeployment> getVisitorType()
      {
         return ServiceDeployment.class;
      }

      public void deploy(DeploymentUnit unit, ServiceDeployment deployment) throws DeploymentException
      {
         List<ServiceMetaData> services = deployment.getServices();
         for (ServiceMetaData service : services)
            addServiceComponent(unit, service);
      }

      public void undeploy(DeploymentUnit unit, ServiceDeployment deployment)
      {
         List<ServiceMetaData> services = deployment.getServices();
         for (ServiceMetaData service : services)
            removeServiceComponent(unit, service);
      }
   }

   /**
    * ServiceMetaDataVisitor.
    */
   private static class ServiceMetaDataVisitor implements DeploymentVisitor<ServiceMetaData>
   {
      public Class<ServiceMetaData> getVisitorType()
      {
         return ServiceMetaData.class;
      }

      public void deploy(DeploymentUnit unit, ServiceMetaData deployment) throws DeploymentException
      {
         addServiceComponent(unit, deployment);
      }

      public void undeploy(DeploymentUnit unit, ServiceMetaData deployment)
      {
         removeServiceComponent(unit, deployment);
      }
   }
}
