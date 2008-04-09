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
import java.util.HashMap;

import junit.framework.Test;
import org.jboss.beans.metadata.spi.factory.BeanFactory;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.ControllerStateModel;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.deployment.xml.BasicXMLDeployer;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.test.kernel.AbstractKernelTest;
import org.jboss.test.kernel.deployment.support.container.BaseContext;
import org.jboss.test.kernel.deployment.support.container.Bean1Type;
import org.jboss.test.kernel.deployment.support.container.Bean2Type;
import org.jboss.test.kernel.deployment.support.container.BeanContainer;
import org.jboss.test.kernel.deployment.support.container.BeanPool;

/**
 * Bean Container Test Case.
 * 
 * @author <a href="scott.stark@jboss.com">Scott Stark</a>
 * @version $Revision$
 */
public class BeanContainerUsageTestCase extends AbstractKernelTest
{
   private Kernel kernel;
   private BasicXMLDeployer deployer;

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
      BeanPool<Bean1Type> pool1 = (BeanPool<Bean1Type>) getBean("Bean1TypePool");
      BeanContainer<Bean2Type> container2 = (BeanContainer<Bean2Type>) getBean("BeanContainer2Type");
      Bean2Type bean21 = container2.getBean();
      getLog().debug("bean21: "+bean21);
      Bean1Type bean11 = bean21.getBean1();
      assertEquals(0, pool1.size());
      getLog().debug("bean11: "+bean11);
      assertNotNull(bean11);
      // Create another Bean2Type instance
      Bean2Type bean22 = container2.getBean();
      getLog().debug("bean22: "+bean22);
      assertTrue(bean22 != bean21);
      // The injected bean should not be the same as injected into bean21
      Bean1Type bean12 = bean22.getBean1();
      getLog().debug("bean12: "+bean12);
      assertNotNull(bean12);
      assertTrue(bean12 != bean11);

      // Create another bean 2 type, should fail
      Bean2Type bean23 = container2.getBean();
      getLog().debug("bean23: "+bean23);
      Bean1Type bean13Injected = bean23.getBean1();
      getLog().debug("bean13Injected: "+bean13Injected);
      assertNotNull(bean13Injected);
      deployer.shutdown();
   }
   public void testDependencyInjectionOfBeanWithMismatchedPoolSizes()
      throws Throwable
   {
      bootstrap();
      BeanContainer<Bean2Type> container2 = (BeanContainer<Bean2Type>) getBean("BeanContainer2Type");
      try
      {
         Bean2Type bean21 = container2.getBean();
         fail("Should not have been able to create a Bean2Type");
      }
      catch(IllegalStateException e)
      {
         getLog().info("Saw expected IllegalStateException");
      }
      deployer.shutdown();
   }

   public void testComponentBeanFactory()
      throws Throwable
   {
      bootstrap();
      /*BeanContextFactory<Bean1Type> contextFactory = (BeanContextFactory<Bean1Type>) bean;
      assertEquals(Bean1Type.class.getName(), contextFactory.getBeanClass());
      assertNotNull(bean);
      contextFactory.
      */
      BeanFactory factory = (BeanFactory) getBean("ComponentBeanFactory#ContextFactory");
      getLog().info("ComponentBeanFactory#ContextFactory bean: "+factory);
      assertNotNull(factory);

      BaseContext <Bean1Type, BeanContainer<Bean1Type>> context =
         (BaseContext <Bean1Type, BeanContainer<Bean1Type>>) factory.createBean();
      getLog().info("ComponentBeanFactory#ContextFactory.createBean result: "+context);
      assertNotNull(context);
      Bean1Type bean1 = context.getInstance();
      getLog().info("BeanContext.instance: "+bean1);
      HashMap<Class, Object> interceptors1 = context.getInterceptorInstances();
      getLog().info("BeanContext.interceptorInstances: "+interceptors1);

      Bean1Type bean11 = (Bean1Type) getBean("ComponentBeanFactory#BeanInstance");
      getLog().info("ComponentBeanFactory#BeanInstance bean: "+bean11);
      assertNotNull(bean11);
      assertTrue(bean1 == bean11);

      deployer.shutdown();
   }

   /**
    * There is no xml version of ?
    * @return
    */
   protected KernelDeployment getDeploymentForDependencyInjectionOfBean()
   {
      return null;
   }
   /**
    * There is no xml based version of testComponentBeanFactory
    * @return
    */
   protected KernelDeployment getComponentBeanFactory()
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
      deployer = new BasicXMLDeployer(kernel, ControllerMode.AUTOMATIC);
      String testName = "getDeploymentFor"+ getName().substring(4);
      KernelDeployment deployment = null;
      try
      {
         Method getDeployment = getClass().getDeclaredMethod(testName, null);
         deployment = (KernelDeployment) getDeployment.invoke(this, null);
      }
      catch(NoSuchMethodException e)
      {
         getLog().debug("Ignoring: "+e);
      }

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
      {
         getLog().error("Bean not found " + name + " at state " + state);
         ControllerStateModel states = controller.getStates();
         for(ControllerState s : states)
         {
            getLog().info(s+": "+controller.getContextsByState(s));
         }
         throw new IllegalStateException("Bean not found " + name + " at state " + state);
      }
      return context;
   }

}
