/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueFactoryMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.factory.GenericBeanFactoryMetaData;
import org.jboss.kernel.plugins.deployment.AbstractKernelDeployment;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.test.kernel.deployment.support.container.BaseContext;
import org.jboss.test.kernel.deployment.support.container.Bean1Type;
import org.jboss.test.kernel.deployment.support.container.BeanContainer;
import org.jboss.test.kernel.deployment.support.container.BeanContextFactory;
import org.jboss.test.kernel.deployment.support.container.InstanceInterceptor;
import org.jboss.test.kernel.deployment.support.container.plugin.GenericComponentFactory;
import org.jboss.test.kernel.deployment.support.container.spi.ComponentFactory;
import org.jboss.test.kernel.deployment.support.container.spi.ComponentInstance;
import org.jboss.test.kernel.deployment.support.container.spi.ComponentVisitor;

/**
 * Programatic version of the BeanContainerUsageTestCase tests
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class BeanContainerUsageMDTestCase extends BeanContainerUsageTestCase
{
   public static Test suite()
   {
      return suite(BeanContainerUsageMDTestCase.class);
   }

   public BeanContainerUsageMDTestCase(String name)
   {
      super(name);
   }

   /**
    * Test of the ComponentFactory usecase
    * 
    * @see {@link GenericComponentFactory}
    * @see {@link ComponentFactory}
    * @throws Throwable
    */
   @SuppressWarnings("unchecked")
   public void testComponentBeanFactory()
      throws Throwable
   {
      bootstrap();
      // Lookup the ComponentFactory implementation
      ComponentFactory<BaseContext<Bean1Type, BeanContainer<Bean1Type>>> factory =
         (ComponentFactory<BaseContext<Bean1Type, BeanContainer<Bean1Type>>>) getBean("ComponentBeanFactory");
      getLog().info("ComponentBeanFactory bean: "+factory);
      // Create a component instance
      ComponentInstance<BaseContext<Bean1Type, BeanContainer<Bean1Type>>> contextInstance =
         factory.createComponents("ComponentBeanFactory");
      // Validate the component bean names
      List<String> beanNames = contextInstance.getComponentNames();
      getLog().info("createComponents(ComponentBeanFactory): "+beanNames);

      HashSet<String> expectedBeanNames = new HashSet<String>();
      expectedBeanNames.add("ComponentBeanFactory@ContextFactory#1");
      expectedBeanNames.add("ComponentBeanFactory@BeanInstance#1");
      expectedBeanNames.add("ComponentBeanFactory@Interceptor:0#1");
      assertEquals(expectedBeanNames, new HashSet<String>(beanNames));
      long compID = contextInstance.getComponentID();
      assertEquals(1, compID);
      BaseContext<Bean1Type, BeanContainer<Bean1Type>> context = contextInstance.getContext();
      assertNotNull(context);
      String contextName = contextInstance.getContextName();
      assertEquals("ComponentBeanFactory@ContextFactory#1", contextName);
      Object contextBean = getBean("ComponentBeanFactory@ContextFactory#1");
      assertTrue(contextBean == context);
      Bean1Type bean1 = (Bean1Type) getBean("ComponentBeanFactory@BeanInstance#1");
      assertTrue(bean1 == context.getInstance());
   
      Object interceptor = getBean("ComponentBeanFactory@Interceptor:0#1");
      assertNotNull(interceptor);
      List expectedInterceptors = new ArrayList();
      expectedInterceptors.add(interceptor);
      List interceptors = context.getInterceptors();
      assertEquals(interceptors, expectedInterceptors);

      factory.destroyComponents(contextInstance);
      expectedBeanNames.clear();
      expectedBeanNames.add("ComponentBeanFactory");
      // Only the ComponentBeanFactory should exist
      assertBeansExist(expectedBeanNames);
      contextInstance = null;

      // Create a second component instance
      ComponentInstance<BaseContext<Bean1Type, BeanContainer<Bean1Type>>> contextInstance2 =
         factory.createComponents("ComponentBeanFactory");
      // Validate the component bean names
      List<String> beanNames2 = contextInstance2.getComponentNames();
      getLog().info("createComponents(ComponentBeanFactory): "+beanNames2);
      expectedBeanNames = new HashSet<String>();
      expectedBeanNames.add("ComponentBeanFactory@ContextFactory#2");
      expectedBeanNames.add("ComponentBeanFactory@BeanInstance#2");
      expectedBeanNames.add("ComponentBeanFactory@Interceptor:0#2");
      assertEquals(expectedBeanNames, new HashSet<String>(beanNames2));
      compID = contextInstance2.getComponentID();
      assertEquals(2, compID);
      BaseContext<Bean1Type, BeanContainer<Bean1Type>> context2 = contextInstance2.getContext();
      assertNotNull(context2);
      String contextName2 = contextInstance2.getContextName();
      assertEquals("ComponentBeanFactory@ContextFactory#2", contextName2);
      Object contextBean2 = getBean("ComponentBeanFactory@ContextFactory#2");
      assertTrue(contextBean2 == context2);
      Bean1Type bean2 = (Bean1Type) getBean("ComponentBeanFactory@BeanInstance#2");
      assertTrue(bean2 == context2.getInstance());
      assertTrue(bean2 != bean1);
 
      Object interceptor2 = getBean("ComponentBeanFactory@Interceptor:0#2");
      assertNotNull(interceptor2);
      List expectedInterceptors2 = new ArrayList();
      expectedInterceptors2.add(interceptor2);
      List interceptors2 = context2.getInterceptors();
      assertEquals(interceptors2, expectedInterceptors2);

      factory.destroyComponents(contextInstance2);
      expectedBeanNames.clear();
      expectedBeanNames.add("ComponentBeanFactory");
      // Only the ComponentBeanFactory should exist
      assertBeansExist(expectedBeanNames);

      shutdown();
   }

   /*
   <beanfactory name="Bean1TypeFactory" class="org.jboss.test.kernel.deployment.support.container.Bean1Type"/>
   <bean name="Bean1TypePool" class="org.jboss.test.kernel.deployment.support.container.BeanPool">
      <property name="factory"><inject bean="Bean1TypeFactory"/></property>
   </bean>
   <bean name="BeanContainer1Type" class="org.jboss.test.kernel.deployment.support.container.BeanContainer">
      <property name="pool"><inject bean="Bean1TypePool"/></property>
   </bean>
   <beanfactory name="Bean2TypeFactory" class="org.jboss.test.kernel.deployment.support.container.Bean2Type">
      <property name="bean1"><value-factory bean="Bean1TypePool" method="createBean" /></property>
   </beanfactory>
   <bean name="Bean2TypePool" class="org.jboss.test.kernel.deployment.support.container.BeanPool">
      <property name="factory"><inject bean="Bean2TypeFactory"/></property>
   </bean>
   <bean name="BeanContainer2Type" class="org.jboss.test.kernel.deployment.support.container.BeanContainer">
      <property name="pool"><inject bean="Bean2TypePool"/></property>
   </bean>
    */
   protected KernelDeployment getDeploymentForDependencyInjectionOfBean()
   {
      AbstractKernelDeployment deployment = new AbstractKernelDeployment();
      deployment.setName("DependencyInjectionOfBean");
      ArrayList<BeanMetaDataFactory> beanFactories = new ArrayList<BeanMetaDataFactory>();
      // Bean1TypeFactory
      GenericBeanFactoryMetaData Bean1TypeFactory = new GenericBeanFactoryMetaData("Bean1TypeFactory",
            "org.jboss.test.kernel.deployment.support.container.Bean1Type");
      beanFactories.add(Bean1TypeFactory);
      // Bean1TypePool
      AbstractBeanMetaData Bean1TypePool = new AbstractBeanMetaData("Bean1TypePool",
            "org.jboss.test.kernel.deployment.support.container.BeanPool");
      Set<PropertyMetaData> Bean1TypePool_propertys = new HashSet<PropertyMetaData>();
      Bean1TypePool_propertys.add(new AbstractPropertyMetaData("factory",
            new AbstractDependencyValueMetaData("Bean1TypeFactory")));
      Bean1TypePool.setProperties(Bean1TypePool_propertys);
      beanFactories.add(Bean1TypePool);
      // BeanContainer1Type
      AbstractBeanMetaData BeanContainer1Type = new AbstractBeanMetaData("BeanContainer1Type",
         "org.jboss.test.kernel.deployment.support.container.BeanContainer");
      Set<PropertyMetaData> BeanContainer1Type_propertys = new HashSet<PropertyMetaData>();
      BeanContainer1Type_propertys.add(new AbstractPropertyMetaData("pool",
            new AbstractDependencyValueMetaData("Bean1TypePool")));
      BeanContainer1Type.setProperties(BeanContainer1Type_propertys);
      beanFactories.add(BeanContainer1Type);

      // Bean2TypeFactory
      GenericBeanFactoryMetaData Bean2TypeFactory = new GenericBeanFactoryMetaData("Bean2TypeFactory",
      "org.jboss.test.kernel.deployment.support.container.Bean2Type");
      Set<PropertyMetaData> Bean2TypeFactory_propertys = new HashSet<PropertyMetaData>();
      AbstractValueFactoryMetaData bean1CreateMethod = new AbstractValueFactoryMetaData("Bean1TypePool", "createBean"); 
      Bean2TypeFactory_propertys.add(new AbstractPropertyMetaData("bean1", bean1CreateMethod));
      Bean2TypeFactory.setProperties(Bean2TypeFactory_propertys);
      beanFactories.add(Bean2TypeFactory);
      // Bean2TypePool
      AbstractBeanMetaData Bean2TypePool = new AbstractBeanMetaData("Bean2TypePool",
            "org.jboss.test.kernel.deployment.support.container.BeanPool");
      Set<PropertyMetaData> Bean2TypePool_propertys = new HashSet<PropertyMetaData>();
      Bean2TypePool_propertys.add(new AbstractPropertyMetaData("factory",
            new AbstractDependencyValueMetaData("Bean2TypeFactory")));
      Bean2TypePool.setProperties(Bean2TypePool_propertys);
      beanFactories.add(Bean2TypePool);
      // BeanContainer2Type
      AbstractBeanMetaData BeanContainer2Type = new AbstractBeanMetaData("BeanContainer2Type",
         "org.jboss.test.kernel.deployment.support.container.BeanContainer");
      Set<PropertyMetaData> BeanContainer2Type_propertys = new HashSet<PropertyMetaData>();
      BeanContainer2Type_propertys.add(new AbstractPropertyMetaData("pool",
            new AbstractDependencyValueMetaData("Bean2TypePool")));
      BeanContainer2Type.setProperties(BeanContainer2Type_propertys);
      beanFactories.add(BeanContainer2Type);

      deployment.setBeanFactories(beanFactories);

      return deployment;
   }
   /*
   <beanfactory name="Bean1TypeFactory" class="org.jboss.test.kernel.deployment.support.container.Bean1Type"/>
   <bean name="Bean1TypePool" class="org.jboss.test.kernel.deployment.support.container.BeanPool">
      <constructor>
         <parameter>3</parameter>
      </constructor>
      <property name="factory"><inject bean="Bean1TypeFactory"/></property>
   </bean>
   <bean name="BeanContainer1Type" class="org.jboss.test.kernel.deployment.support.container.BeanContainer">
      <property name="pool"><inject bean="Bean1TypePool"/></property>
   </bean>
   <beanfactory name="Bean2TypeFactory" class="org.jboss.test.kernel.deployment.support.container.Bean2Type">
      <property name="bean1"><value-factory bean="Bean1TypePool" method="createBean" /></property>
   </beanfactory>
   <bean name="Bean2TypePool" class="org.jboss.test.kernel.deployment.support.container.BeanPool">
      <constructor>
         <parameter>4</parameter>
      </constructor>
      <property name="factory"><inject bean="Bean2TypeFactory"/></property>
   </bean>
   <bean name="BeanContainer2Type" class="org.jboss.test.kernel.deployment.support.container.BeanContainer">
      <property name="pool"><inject bean="Bean2TypePool"/></property>
   </bean>
   */
   protected KernelDeployment getDeploymentForDependencyInjectionOfBeanWithMismatchedPoolSizes()
   {
      KernelDeployment deployment = getDeploymentForDependencyInjectionOfBean();
      // Update the pool ctors
      List<BeanMetaDataFactory> beanFactories = deployment.getBeanFactories();
      for(BeanMetaDataFactory bmdf : beanFactories)
      {
         if(bmdf instanceof AbstractBeanMetaData)
         {
            AbstractBeanMetaData abmd = (AbstractBeanMetaData) bmdf;
            if(abmd.getName().equals("Bean1TypePool"))
            {
               AbstractConstructorMetaData ctor = new AbstractConstructorMetaData();
               ArrayList<ParameterMetaData> params = new ArrayList<ParameterMetaData>();
               params.add(new AbstractParameterMetaData(int.class.getName(), "3"));
               ctor.setParameters(params);
               abmd.setConstructor(ctor);
            }
            else if(abmd.getName().equals("Bean2TypePool"))
            {
               AbstractConstructorMetaData ctor = new AbstractConstructorMetaData();
               ArrayList<ParameterMetaData> params = new ArrayList<ParameterMetaData>();
               params.add(new AbstractParameterMetaData(int.class.getName(), "4"));
               ctor.setParameters(params);
               abmd.setConstructor(ctor);
            }
         }
      }
      return deployment;
   }
   /**
    * MetaData version of testComponentBeanFactory
    * 
    * @return the kernel deployment
    */
   @SuppressWarnings("unchecked")
   protected KernelDeployment getDeploymentForComponentBeanFactory()
   {
      AbstractKernelDeployment deployment = new AbstractKernelDeployment();
      deployment.setName("ComponentBeanFactory");
      ArrayList<BeanMetaDataFactory> beanFactories = new ArrayList<BeanMetaDataFactory>();
      // Bean context factory for Bean1Type
      BeanContainer<Bean1Type> container = new BeanContainer<Bean1Type>();
      BeanContextFactory<Bean1Type> contextFactory = new BeanContextFactory<Bean1Type>();
      contextFactory.setBeanClass(Bean1Type.class.getName());
      contextFactory.setContainer(container);
      String[] interceptorNames = {InstanceInterceptor.class.getName()};
      contextFactory.setInterceptorNames(Arrays.asList(interceptorNames));
      /*
      BeanMetaDataFactory contextFactoryMD = installBeanInstance("ComponentBeanFactory", contextFactory);
      beanFactories.add(contextFactoryMD);
      */
      ComponentVisitor visitor = null;
      GenericComponentFactory componentFactory = new GenericComponentFactory(contextFactory, visitor);
      BeanMetaDataFactory componentFactoryMD = installBeanInstance("ComponentBeanFactory", componentFactory);
      beanFactories.add(componentFactoryMD);
      deployment.setBeanFactories(beanFactories);

      return deployment;
   }

   protected BeanMetaDataFactory installBeanInstance(String name, Object bean)
   {
      AbstractBeanMetaData beanMD = new AbstractBeanMetaData(name, bean.getClass().getName());
      beanMD.setConstructor(new AlreadyInstantiated(bean));
      return beanMD;
   }

   public static class AlreadyInstantiated extends AbstractConstructorMetaData
   {
      private static final long serialVersionUID = 1L;
      
      private Object bean;

      public class Factory
      {

         public Object create()
         {
            return bean;
         }
      }

      public AlreadyInstantiated(Object bean)
      {
         this.bean = bean;
         this.setFactory(new AbstractValueMetaData(new Factory()));
         this.setFactoryClass(Factory.class.getName());
         this.setFactoryMethod("create");
      }
   }
}
