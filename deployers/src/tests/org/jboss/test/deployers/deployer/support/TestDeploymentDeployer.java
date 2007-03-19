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

import java.util.List;

import org.jboss.deployers.plugins.deployers.helpers.AbstractComponentDeployer;
import org.jboss.deployers.plugins.deployers.helpers.SimpleDeploymentVisitor;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentUnit;

public class TestDeploymentDeployer
   extends AbstractComponentDeployer<TestDeployment, TestMetaData>
{

   public TestDeploymentDeployer()
   {
      setDeploymentVisitor(new TestDeploymentVisitor());
      setComponentVisitor(new TestMetaDataVisitor());
   }

   protected static void addTestComponent(DeploymentUnit unit, TestMetaData test)
   {
      DeploymentUnit component = unit.addComponent(test.getName());
      component.addAttachment(TestMetaData.class.getName(), test);
   }

   protected static void removeTestComponent(DeploymentUnit unit, TestMetaData test)
   {
      unit.removeComponent(test.getName());
   }
   
   /**
    * TestDeploymentVisitor.
    */
   public class TestDeploymentVisitor implements SimpleDeploymentVisitor<TestDeployment>
   {
      public Class<TestDeployment> getVisitorType()
      {
         return TestDeployment.class;
      }

      public void deploy(DeploymentUnit unit, TestDeployment deployment) throws DeploymentException
      {
         try
         {
            List<TestMetaData> tests = deployment.getBeans();
            if (tests == null || tests.isEmpty())
               return;
            
            for (TestMetaData test : tests)
               addTestComponent(unit, test);
         }
         catch (Throwable t)
         {
            throw DeploymentException.rethrowAsDeploymentException("Error deploying: " + deployment, t);
         }
      }

      public void undeploy(DeploymentUnit unit, TestDeployment deployment)
      {
         List<TestMetaData> tests = deployment.getBeans();
         if (tests == null)
            return;
         
         for (TestMetaData test : tests)
         {
            unit.removeComponent(test.getName());
         }
      }
   }

   /**
    * TestMetaDataVisitor.
    */
   public static class TestMetaDataVisitor implements SimpleDeploymentVisitor<TestMetaData>
   {
      public Class<TestMetaData> getVisitorType()
      {
         return TestMetaData.class;
      }

      public void deploy(DeploymentUnit unit, TestMetaData deployment) throws DeploymentException
      {
         addTestComponent(unit, deployment);
      }

      public void undeploy(DeploymentUnit unit, TestMetaData deployment)
      {
         removeTestComponent(unit, deployment);
      }
   }


}
