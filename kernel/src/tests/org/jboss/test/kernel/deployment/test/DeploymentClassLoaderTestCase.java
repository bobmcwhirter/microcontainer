/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.kernel.deployment.test;

import junit.framework.Test;
import org.jboss.beans.metadata.spi.factory.BeanFactory;
import org.jboss.kernel.spi.deployment.KernelDeployment;

/**
 * Deployment ClassLoader Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class DeploymentClassLoaderTestCase extends AbstractDeploymentTest
{
   public static Test suite()
   {
      return suite(DeploymentClassLoaderTestCase.class);
   }

   public DeploymentClassLoaderTestCase(String name) throws Throwable
   {
      super(name);
   }

   public void testDeploymentClassLoader() throws Throwable
   {
      KernelDeployment deployment = deploy("DeploymentClassLoaderTestCase_NotAutomatic.xml");
      try
      {
         validate();
         
         ClassLoader deploymentCL = (ClassLoader) getBean("DeploymentClassLoader");
         ClassLoader beanCL = (ClassLoader) getBean("BeanClassLoader");
         Object bean = getBean("DeploymentConfiguredClassLoader");
         assertEquals(deploymentCL, bean.getClass().getClassLoader());
         bean = getBean("BeanConfiguredClassLoader");
         assertEquals(beanCL, bean.getClass().getClassLoader());
         bean = getBean("NotConfiguredClassLoader");
         assertEquals(getClass().getClassLoader(), bean.getClass().getClassLoader());
         
         BeanFactory factory = (BeanFactory) getBean("FactoryDeploymentConfiguredClassLoader");
         bean = factory.createBean();
         assertEquals(deploymentCL, bean.getClass().getClassLoader());
         factory = (BeanFactory) getBean("FactoryBeanConfiguredClassLoader");
         bean = factory.createBean();
         assertEquals(beanCL, bean.getClass().getClassLoader());
         factory = (BeanFactory) getBean("FactoryNotConfiguredClassLoader");
         bean = factory.createBean();
         assertEquals(getClass().getClassLoader(), bean.getClass().getClassLoader());
      }
      finally
      {
         undeploy(deployment);
      }
   }
}