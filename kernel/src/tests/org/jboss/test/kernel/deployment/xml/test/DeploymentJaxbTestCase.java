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
package org.jboss.test.kernel.deployment.xml.test;

import java.util.List;

import junit.framework.Test;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.kernel.plugins.deployment.AbstractKernelDeployment;
import org.jboss.test.kernel.deployment.xml.support.TestBeanMetaDataFactory;
import org.jboss.test.kernel.deployment.xml.support.TestBeanMetaDataFactory1;
import org.jboss.test.kernel.deployment.xml.support.TestBeanMetaDataFactory2;

/**
 * DeploymentJaxbTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 56476 $
 */
public class DeploymentJaxbTestCase extends AbstractMCTest
{
   public void testDeployment() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment();
      assertNull(deployment.getName());
      assertNull(deployment.getClassLoader());
      assertNull(deployment.getBeans());
   }

   public void testDeploymentWithName() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment();
      assertEquals("SimpleDeployment", deployment.getName());
      assertNull(deployment.getClassLoader());
      assertNull(deployment.getBeans());
   }

   public void testDeploymentWithClassLoader() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment();
      assertEquals("SimpleDeployment", deployment.getName());
      assertNotNull(deployment.getClassLoader());
      assertNull(deployment.getBeans());
   }

   public void testDeploymentWithBean() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment();
      assertEquals("SimpleDeployment", deployment.getName());
      assertNull(deployment.getClassLoader());
      List beans = deployment.getBeans();
      assertNotNull(beans);
      assertEquals(1, beans.size());
      BeanMetaData bean = (BeanMetaData) beans.get(0);
      assertNotNull(bean);
      assertEquals("Bean1", bean.getName());
      assertEquals(Object.class.getName(), bean.getBean());
   }

   public void testDeploymentWithMultipleBeans() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment();
      assertEquals("SimpleDeployment", deployment.getName());
      assertNull(deployment.getClassLoader());
      List beans = deployment.getBeans();
      assertNotNull(beans);
      assertEquals(3, beans.size());
      BeanMetaData bean = (BeanMetaData) beans.get(0);
      assertNotNull(bean);
      assertEquals("Bean1", bean.getName());
      assertEquals(Object.class.getName(), bean.getBean());
      bean = (BeanMetaData) beans.get(1);
      assertNotNull(bean);
      assertEquals("Bean2", bean.getName());
      assertEquals(Object.class.getName(), bean.getBean());
      bean = (BeanMetaData) beans.get(2);
      assertNotNull(bean);
      assertEquals("Bean3", bean.getName());
      assertEquals(Object.class.getName(), bean.getBean());
   }

   public void testDeploymentWithBeanFactory() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment();
      assertEquals("SimpleDeployment", deployment.getName());
      assertNull(deployment.getClassLoader());
      List beans = deployment.getBeans();
      assertNotNull(beans);
      assertEquals(1, beans.size());
      BeanMetaData bean = (BeanMetaData) beans.get(0);
      assertNotNull(bean);
      assertEquals("Bean1", bean.getName());
      assertEquals("GenericBeanFactory", bean.getBean());
   }

   public void testDeploymentWithMultipleBeanFactorys() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment();
      assertEquals("SimpleDeployment", deployment.getName());
      assertNull(deployment.getClassLoader());
      List beans = deployment.getBeans();
      assertNotNull(beans);
      assertEquals(3, beans.size());
      BeanMetaData bean = (BeanMetaData) beans.get(0);
      assertNotNull(bean);
      assertEquals("Bean1", bean.getName());
      assertEquals("GenericBeanFactory", bean.getBean());
      bean = (BeanMetaData) beans.get(1);
      assertNotNull(bean);
      assertEquals("Bean2", bean.getName());
      assertEquals("GenericBeanFactory", bean.getBean());
      bean = (BeanMetaData) beans.get(2);
      assertNotNull(bean);
      assertEquals("Bean3", bean.getName());
      assertEquals("GenericBeanFactory", bean.getBean());
   }

   public void testDeploymentWithMultipleBeanMetaDataFactorys() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment();
      assertEquals("SimpleDeployment", deployment.getName());
      assertNull(deployment.getClassLoader());
      List beanFactories = deployment.getBeanFactories();
      assertNotNull(beanFactories);
      assertEquals(3, beanFactories.size());
      BeanMetaDataFactory factory = (BeanMetaDataFactory) beanFactories.get(0);
      assertEquals(TestBeanMetaDataFactory.class.getName(), factory.getClass().getName());
      factory = (BeanMetaDataFactory) beanFactories.get(1);
      assertEquals(TestBeanMetaDataFactory1.class.getName(), factory.getClass().getName());
      factory = (BeanMetaDataFactory) beanFactories.get(2);
      assertEquals(TestBeanMetaDataFactory2.class.getName(), factory.getClass().getName());
      List beans = deployment.getBeans();
      assertNotNull(beans);
      assertEquals(6, beans.size());
      BeanMetaData bean = (BeanMetaData) beans.get(0);
      assertNotNull(bean);
      assertEquals("Bean1", bean.getBean());
      bean = (BeanMetaData) beans.get(1);
      assertNotNull(bean);
      assertEquals("Bean2", bean.getBean());
      bean = (BeanMetaData) beans.get(2);
      assertNotNull(bean);
      assertEquals("Bean3", bean.getBean());
      bean = (BeanMetaData) beans.get(3);
      assertNotNull(bean);
      assertEquals("Bean4", bean.getBean());
      bean = (BeanMetaData) beans.get(4);
      assertNotNull(bean);
      assertEquals("Bean5", bean.getBean());
      bean = (BeanMetaData) beans.get(5);
      assertNotNull(bean);
      assertEquals("Bean6", bean.getBean());
   }

   public void testDeploymentWithBeanMetaDataFactory() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment();
      assertEquals("SimpleDeployment", deployment.getName());
      assertNull(deployment.getClassLoader());
      List beanFactories = deployment.getBeanFactories();
      assertNotNull(beanFactories);
      assertEquals(1, beanFactories.size());
      BeanMetaDataFactory factory = (BeanMetaDataFactory) beanFactories.get(0);
      assertEquals(TestBeanMetaDataFactory.class.getName(), factory.getClass().getName());
      List beans = deployment.getBeans();
      assertNotNull(beans);
      assertEquals(2, beans.size());
      BeanMetaData bean = (BeanMetaData) beans.get(0);
      assertNotNull(bean);
      assertEquals("Bean1", bean.getBean());
      bean = (BeanMetaData) beans.get(1);
      assertNotNull(bean);
      assertEquals("Bean2", bean.getBean());
   }

   public void testDeploymentWithMixed() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment();
      assertEquals("SimpleDeployment", deployment.getName());
      assertNull(deployment.getClassLoader());
      List beans = deployment.getBeans();
      assertNotNull(beans);
      assertEquals(8, beans.size());
      BeanMetaData bean = (BeanMetaData) beans.get(0);
      assertNotNull(bean);
      assertEquals("Bean1", bean.getName());
      assertEquals(Object.class.getName(), bean.getBean());
      bean = (BeanMetaData) beans.get(1);
      assertNotNull(bean);
      assertEquals("Bean2", bean.getName());
      assertEquals("GenericBeanFactory", bean.getBean());
      bean = (BeanMetaData) beans.get(2);
      assertNotNull(bean);
      assertEquals("Bean1", bean.getBean());
      bean = (BeanMetaData) beans.get(3);
      assertNotNull(bean);
      assertEquals("Bean2", bean.getBean());
      bean = (BeanMetaData) beans.get(4);
      assertNotNull(bean);
      assertEquals("Bean3", bean.getName());
      assertEquals(Object.class.getName(), bean.getBean());
      bean = (BeanMetaData) beans.get(5);
      assertNotNull(bean);
      assertEquals("Bean4", bean.getName());
      assertEquals("GenericBeanFactory", bean.getBean());
      bean = (BeanMetaData) beans.get(6);
      assertNotNull(bean);
      assertEquals("Bean3", bean.getBean());
      bean = (BeanMetaData) beans.get(7);
      assertNotNull(bean);
      assertEquals("Bean4", bean.getBean());
   }
   
   public static Test suite()
   {
      return DeploymentJaxbTestCase.suite(DeploymentJaxbTestCase.class);
   }

   public DeploymentJaxbTestCase(String name)
   {
      super(name);
   }
}
