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
import java.util.Set;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Iterator;
import java.lang.annotation.Annotation;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactory;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.LifecycleMetaData;
import org.jboss.beans.metadata.spi.AnnotationMetaData;
import org.jboss.beans.metadata.spi.NamedAliasMetaData;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.kernel.plugins.deployment.AbstractKernelDeployment;
import org.jboss.test.kernel.deployment.xml.support.TestBeanMetaDataFactory;
import org.jboss.test.kernel.deployment.xml.support.TestBeanMetaDataFactory1;
import org.jboss.test.kernel.deployment.xml.support.TestBeanMetaDataFactory2;
import org.jboss.test.kernel.deployment.xml.support.Annotation1;
import org.jboss.test.kernel.deployment.xml.support.AnnotationWithAttributes;

/**
 * DeploymentTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class DeploymentTestCase extends AbstractXMLTest
{
   public void testDeployment() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment("Deployment.xml");
      assertNull(deployment.getName());
      assertNull(deployment.getClassLoader());
      assertNull(deployment.getBeans());
   }

   public void testDeploymentWithName() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment("DeploymentWithName.xml");
      assertEquals("SimpleDeployment", deployment.getName());
      assertNull(deployment.getClassLoader());
      assertNull(deployment.getBeans());
   }

   public void testDeploymentWithClassLoader() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment("DeploymentWithClassLoader.xml");
      assertEquals("SimpleDeployment", deployment.getName());
      assertNotNull(deployment.getClassLoader());
      assertEmpty(deployment.getBeans());
   }

   public void testDeploymentWithBean() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment("DeploymentWithBean.xml");
      assertEquals("SimpleDeployment", deployment.getName());
      assertNull(deployment.getClassLoader());
      List<BeanMetaData> beans = deployment.getBeans();
      assertNotNull(beans);
      assertEquals(1, beans.size());
      BeanMetaData bean = beans.get(0);
      assertNotNull(bean);
      assertEquals("Bean1", bean.getName());
      assertEquals(Object.class.getName(), bean.getBean());
   }

   public void testDeploymentWithMultipleBeans() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment("DeploymentWithMultipleBeans.xml");
      assertEquals("SimpleDeployment", deployment.getName());
      assertNull(deployment.getClassLoader());
      List<BeanMetaData> beans = deployment.getBeans();
      assertNotNull(beans);
      assertEquals(3, beans.size());
      BeanMetaData bean = beans.get(0);
      assertNotNull(bean);
      assertEquals("Bean1", bean.getName());
      assertEquals(Object.class.getName(), bean.getBean());
      bean = beans.get(1);
      assertNotNull(bean);
      assertEquals("Bean2", bean.getName());
      assertEquals(Object.class.getName(), bean.getBean());
      bean = beans.get(2);
      assertNotNull(bean);
      assertEquals("Bean3", bean.getName());
      assertEquals(Object.class.getName(), bean.getBean());
   }

   public void testDeploymentWithBeanFactory() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment("DeploymentWithBeanFactory.xml");
      assertEquals("SimpleDeployment", deployment.getName());
      assertNull(deployment.getClassLoader());
      List<BeanMetaData> beans = deployment.getBeans();
      assertNotNull(beans);
      assertEquals(1, beans.size());
      BeanMetaData bean = beans.get(0);
      assertNotNull(bean);
      assertEquals("Bean1", bean.getName());
      assertEquals(GenericBeanFactory.class.getName(), bean.getBean());
   }

   public void testDeploymentWithMultipleBeanFactorys() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment("DeploymentWithMultipleBeanFactorys.xml");
      assertEquals("SimpleDeployment", deployment.getName());
      assertNull(deployment.getClassLoader());
      List<BeanMetaData> beans = deployment.getBeans();
      assertNotNull(beans);
      assertEquals(3, beans.size());
      BeanMetaData bean = beans.get(0);
      assertNotNull(bean);
      assertEquals("Bean1", bean.getName());
      assertEquals(GenericBeanFactory.class.getName(), bean.getBean());
      bean = beans.get(1);
      assertNotNull(bean);
      assertEquals("Bean2", bean.getName());
      assertEquals(GenericBeanFactory.class.getName(), bean.getBean());
      bean = beans.get(2);
      assertNotNull(bean);
      assertEquals("Bean3", bean.getName());
      assertEquals(GenericBeanFactory.class.getName(), bean.getBean());
   }

   public void testDeploymentWithMultipleBeanMetaDataFactorys() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment("DeploymentWithMultipleBeanMetaDataFactorys.xml");
      assertEquals("SimpleDeployment", deployment.getName());
      assertNull(deployment.getClassLoader());
      List<BeanMetaDataFactory> beanFactories = deployment.getBeanFactories();
      assertNotNull(beanFactories);
      assertEquals(3, beanFactories.size());
      BeanMetaDataFactory factory = beanFactories.get(0);
      assertEquals(TestBeanMetaDataFactory.class.getName(), factory.getClass().getName());
      factory = beanFactories.get(1);
      assertEquals(TestBeanMetaDataFactory1.class.getName(), factory.getClass().getName());
      factory = beanFactories.get(2);
      assertEquals(TestBeanMetaDataFactory2.class.getName(), factory.getClass().getName());
      List<BeanMetaData> beans = deployment.getBeans();
      assertNotNull(beans);
      assertEquals(6, beans.size());
      BeanMetaData bean = beans.get(0);
      assertNotNull(bean);
      assertEquals("Bean1", bean.getBean());
      bean = beans.get(1);
      assertNotNull(bean);
      assertEquals("Bean2", bean.getBean());
      bean = beans.get(2);
      assertNotNull(bean);
      assertEquals("Bean3", bean.getBean());
      bean = beans.get(3);
      assertNotNull(bean);
      assertEquals("Bean4", bean.getBean());
      bean = beans.get(4);
      assertNotNull(bean);
      assertEquals("Bean5", bean.getBean());
      bean = beans.get(5);
      assertNotNull(bean);
      assertEquals("Bean6", bean.getBean());
   }

   public void testDeploymentWithBeanMetaDataFactory() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment("DeploymentWithBeanMetaDataFactory.xml");
      assertEquals("SimpleDeployment", deployment.getName());
      assertNull(deployment.getClassLoader());
      List<BeanMetaDataFactory> beanFactories = deployment.getBeanFactories();
      assertNotNull(beanFactories);
      assertEquals(1, beanFactories.size());
      BeanMetaDataFactory factory = beanFactories.get(0);
      assertEquals(TestBeanMetaDataFactory.class.getName(), factory.getClass().getName());
      List<BeanMetaData> beans = deployment.getBeans();
      assertNotNull(beans);
      assertEquals(2, beans.size());
      BeanMetaData bean = beans.get(0);
      assertNotNull(bean);
      assertEquals("Bean1", bean.getBean());
      bean = beans.get(1);
      assertNotNull(bean);
      assertEquals("Bean2", bean.getBean());
   }

   public void testDeploymentWithMixed() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment("DeploymentWithMixed.xml");
      assertEquals("SimpleDeployment", deployment.getName());
      assertNull(deployment.getClassLoader());
      List<BeanMetaData> beans = deployment.getBeans();
      assertNotNull(beans);
      assertEquals(8, beans.size());
      BeanMetaData bean = beans.get(0);
      assertNotNull(bean);
      assertEquals("Bean1", bean.getName());
      assertEquals(Object.class.getName(), bean.getBean());
      bean = beans.get(1);
      assertNotNull(bean);
      assertEquals("Bean2", bean.getName());
      assertEquals(GenericBeanFactory.class.getName(), bean.getBean());
      bean = beans.get(2);
      assertNotNull(bean);
      assertEquals("Bean1", bean.getBean());
      bean = beans.get(3);
      assertNotNull(bean);
      assertEquals("Bean2", bean.getBean());
      bean = beans.get(4);
      assertNotNull(bean);
      assertEquals("Bean3", bean.getName());
      assertEquals(Object.class.getName(), bean.getBean());
      bean = beans.get(5);
      assertNotNull(bean);
      assertEquals("Bean4", bean.getName());
      assertEquals(GenericBeanFactory.class.getName(), bean.getBean());
      bean = beans.get(6);
      assertNotNull(bean);
      assertEquals("Bean3", bean.getBean());
      bean = beans.get(7);
      assertNotNull(bean);
      assertEquals("Bean4", bean.getBean());
   }

   public void testDeploymentWithLifecycle() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment("DeploymentWithLifecycle.xml");

      LifecycleMetaData create = deployment.getCreate();
      assertNotNull(create);
      LifecycleMetaData start = deployment.getStart();
      assertNotNull(start);
      LifecycleMetaData stop = deployment.getStop();
      assertNotNull(stop);
      LifecycleMetaData destroy = deployment.getDestroy();
      assertNotNull(destroy);

      List<BeanMetaData> beans = deployment.getBeans();
      assertNotNull(beans);
      assertEquals(1, beans.size());
      BeanMetaData bean = beans.iterator().next();

      assertEquals(create, bean.getCreate());
      assertEquals(start, bean.getStart());
      assertEquals(stop, bean.getStop());
      assertEquals(destroy, bean.getDestroy());
   }

   public void testDeploymentWithMode() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment("DeploymentWithMode.xml");

      ControllerMode mode = deployment.getMode();
      assertNotNull(mode);
      assertEquals(ControllerMode.ON_DEMAND, mode);

      List<BeanMetaData> beans = deployment.getBeans();
      assertNotNull(beans);
      assertEquals(1, beans.size());
      BeanMetaData bean = beans.iterator().next();

      assertEquals(mode, bean.getMode());
   }

   public void testDeploymentWithAlias() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment("DeploymentWithAlias.xml");
      Set<NamedAliasMetaData> aliases = deployment.getAliases();
      assertNotNull(aliases);
      assertEquals(2, aliases.size());
      Set<String> stringAlias = new TreeSet<String>();
      for(NamedAliasMetaData namd : aliases)
      {
         assertNotNull(namd);
         Object ann = namd.getAliasValue();
         assertNotNull(ann);
         stringAlias.add(ann.toString());
      }
      Iterator<String> iter = stringAlias.iterator();
      String first = iter.next();
      assertEquals("FirstAlias", first);
      String second = iter.next();
      assertEquals("SecondAlias", second);
   }

   public void testDeploymentWithAnnotations() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment("DeploymentWithAnnotations.xml");
      Set<AnnotationMetaData> annotations = deployment.getAnnotations();
      assertNotNull(annotations);
      assertEquals(2, annotations.size());
      List<AnnotationMetaData> annotationList = new ArrayList<AnnotationMetaData>(annotations);
      Annotation ann = annotationList.get(0).getAnnotationInstance();
      assertNotNull(ann);
      assertEquals(Annotation1.class.getName(), ann.annotationType().getName());
      ann = annotationList.get(1).getAnnotationInstance();
      assertNotNull(ann);
      assertEquals(AnnotationWithAttributes.class.getName(), ann.annotationType().getName());
      assertTrue(ann instanceof AnnotationWithAttributes);
      AnnotationWithAttributes ann1 = (AnnotationWithAttributes)ann;
      assertNotNull(ann1.clazz());
      assertEquals(Integer.class, ann1.clazz());
      assertNotNull(ann1.integer());
      assertEquals(100, ann1.integer());
      assertNotNull(ann1.str());
      assertEquals("Annotations are nice", ann1.str());
   }

   public static Test suite()
   {
      return suite(DeploymentTestCase.class);
   }

   public DeploymentTestCase(String name)
   {
      super(name);
   }
}
