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

import java.lang.reflect.Method;
import java.net.URL;

import junit.framework.Test;

import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.deployment.xml.BasicXMLDeployer;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.test.AbstractTestDelegate;
import org.jboss.test.kernel.AbstractKernelTest;
import org.jboss.test.kernel.config.support.XMLUtil;
import org.jboss.test.kernel.deployment.support.container.Bean1Type;
import org.jboss.test.kernel.deployment.support.container.Bean2Type;
import org.jboss.test.kernel.deployment.support.container.BeanContainer;

/**
 * Bean Container Test Case.
 * 
 * @author <a href="scott.stark@jboss.com">Scott Stark</a>
 * @version $Revision:$
 */
public class BeanContainerUsageTestCase extends AbstractKernelTest
{
   private Kernel kernel;

   public static Test suite()
   {
      return suite(BeanContainerUsageTestCase.class);
   }

   public BeanContainerUsageTestCase(String name)
   {
      super(name);
   }

   @SuppressWarnings("unchecked")
   public void testDependencyInjectionOfBean() throws Throwable
   {
      bootstrap();
      BeanContainer<Bean1Type> container1 = (BeanContainer<Bean1Type>) getBean("BeanContainer1Type");
      BeanContainer<Bean2Type> container2 = (BeanContainer<Bean2Type>) getBean("BeanContainer2Type");
      Bean2Type bean21 = container2.getBean();
      Bean1Type bean11 = bean21.getBean1();
      assertNotNull(bean11);
      // Create another Bean2Type instance
      Bean2Type bean22 = container2.getBean();
      assertTrue(bean22 != bean21);
      // The injected bean should not be the same as injected into bean21
      Bean1Type bean12 = bean22.getBean1();
      assertNotNull(bean12);
      assertTrue(bean12 != bean11);
   }

   protected KernelDeployment getDeploymentForDependencyInjectionOfBean()
   {
      return null;
   }

   /**
    * Either deploy a test specific xml descriptor, or obtain a test
    * specific KernelDeployment by looking for a method
    * "getDeploymentFor"+ getName().substring(4);
    */
   protected Kernel bootstrap() throws Throwable
   {
      kernel = super.bootstrap();
      BasicXMLDeployer deployer = new BasicXMLDeployer(kernel, ControllerMode.AUTOMATIC);
      String testName = "getDeploymentFor"+ getName().substring(4);
      Method getDeployment = getClass().getDeclaredMethod(testName, null);
      KernelDeployment deployment = (KernelDeployment) getDeployment.invoke(this, null);
      if(deployment == null)
      {
         String xmlName = getClass().getName() + "_" + super.getName();
         xmlName = xmlName.replace('.', '/') + ".xml";
         URL url = getClass().getClassLoader().getResource(xmlName);
         if (url != null)
            deployer.deploy(url);
         else
            getLog().debug("No test specific deployment " + xmlName);
      }
      else
      {
         deployer.deploy(deployment);
      }
      deployer.validate();
      return kernel;
   }
   protected Object getBean(final Object name)
   {
      KernelControllerContext context = getControllerContext(name, ControllerState.INSTALLED);
      return context.getTarget();
   }
   protected KernelControllerContext getControllerContext(final Object name, final ControllerState state)
   {
      KernelController controller = kernel.getController();
      KernelControllerContext context = (KernelControllerContext) controller.getContext(name, state);
      if (context == null)
         throw new IllegalStateException("Bean not found " + name + " at state " + state);
      return context;
   }

}
