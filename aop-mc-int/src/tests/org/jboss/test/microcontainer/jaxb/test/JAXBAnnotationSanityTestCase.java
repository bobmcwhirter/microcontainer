/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.test.microcontainer.jaxb.test;

import java.net.URL;
import java.util.List;
import java.util.Set;

import junit.framework.Test;

import org.jboss.aop.advice.AdviceType;
import org.jboss.aop.microcontainer.beans.Aspect;
import org.jboss.aop.microcontainer.beans.AspectBinding;
import org.jboss.aop.microcontainer.beans.InterceptorEntry;
import org.jboss.aop.microcontainer.beans.Stack;
import org.jboss.aop.microcontainer.beans.StackEntry;
import org.jboss.aop.microcontainer.beans.TypeDef;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactory;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.test.aop.junit.ForceJAXBAOPMicrocontainerTest;
import org.jboss.test.microcontainer.beans.TestAspect;
import org.jboss.test.microcontainer.beans.TestAspectWithDependency;
import org.jboss.test.microcontainer.beans.TestInterceptorWithDependency;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class JAXBAnnotationSanityTestCase  extends ForceJAXBAOPMicrocontainerTest
{
   public static Test suite()
   {
      return suite(JAXBAnnotationSanityTestCase.class);
   }
   
   public JAXBAnnotationSanityTestCase(String name)
   {
      super(name);
   }

   @Override
   protected void setUp() throws Exception
   {
      super.setUp();
      enableTrace("org.jboss.xb.builder");
   }

   public void testAopAspectWithDefaultNameAndScope() throws Exception
   {
      checkAspectWithDefaultNameAndScope("JAXBAnnotationSanityTestCase_AopAspectWithDefaultNameAndScope.xml");
   }
   
   public void testDeploymentAspectWithDefaultNameAndScope() throws Exception
   {
      checkAspectWithDefaultNameAndScope("JAXBAnnotationSanityTestCase_DeploymentAspectWithDefaultNameAndScope.xml");
   }
   
   private void checkAspectWithDefaultNameAndScope(String name) throws Exception
   {
      List<BeanMetaData> beans = unmarshal(name, 2);
      // <aspect class="org.jboss.test.microcontainer.beans.TestAspect"/> 
      checkAspect(beans, TestAspect.class.getName(), TestAspect.class, null, "PER_VM", null, null);
   }
   
   public void testAopAspectWithNameAndDefaultScope() throws Exception
   {
      checkAspectWithNameAndDefaultScope("JAXBAnnotationSanityTestCase_AopAspectWithNameAndDefaultScope.xml");
   }

   public void testDeploymentAspectWithNameAndDefaultScope() throws Exception
   {
      checkAspectWithNameAndDefaultScope("JAXBAnnotationSanityTestCase_DeploymentAspectWithNameAndDefaultScope.xml");
   }

   private void checkAspectWithNameAndDefaultScope(String name) throws Exception
   {
      List<BeanMetaData> beans = unmarshal(name, 2);
      // <aspect name="named" class="org.jboss.test.microcontainer.beans.TestAspect"/> 
      checkAspect(beans, "named", TestAspect.class, null, "PER_VM", null, null);
   }

   public void testAopAspectWithNameAndScope() throws Exception
   {
      checkAspectWithNameAndScope("JAXBAnnotationSanityTestCase_AopAspectWithNameAndScope.xml");
   }

   public void testDeploymentAspectWithNameAndScope() throws Exception
   {
      checkAspectWithNameAndScope("JAXBAnnotationSanityTestCase_DeploymentAspectWithNameAndScope.xml");
   }

   private void checkAspectWithNameAndScope(String name) throws Exception
   {
      List<BeanMetaData> beans = unmarshal(name, 2);
      // <aspect name="withscope" class="org.jboss.test.microcontainer.beans.TestAspect" scope="PER_INSTANCE"/>
      checkAspect(beans, "withscope", TestAspect.class, null, "PER_INSTANCE", null, null);
   }

   public void testAopAspectWithFactory() throws Exception
   {
      checkAspectWithFactory("JAXBAnnotationSanityTestCase_AopAspectWithFactory.xml");
   }   
   
   public void testDeploymentAspectWithFactory() throws Exception
   {
      checkAspectWithFactory("JAXBAnnotationSanityTestCase_DeploymentAspectWithFactory.xml");
   }   
   
   private void checkAspectWithFactory(String name) throws Exception
   {
      List<BeanMetaData> beans = unmarshal(name, 2);
      // <aspect name="withfactory" factory="org.jboss.test.microcontainer.beans.TestAspect"/>
      checkAspect(beans, "withfactory", null, TestAspect.class, "PER_VM", null, null);
   }   
   
   public void testAopAspectWithManagerBean() throws Exception
   {
      checkAspectWithManagerBean("JAXBAnnotationSanityTestCase_AopAspectWithManagerBean.xml");
   }
   
   public void testDeploymentAspectWithManagerBean() throws Exception
   {
      checkAspectWithManagerBean("JAXBAnnotationSanityTestCase_DeploymentAspectWithManagerBean.xml");
   }
   
   private void checkAspectWithManagerBean(String name) throws Exception
   {
      List<BeanMetaData> beans = unmarshal(name, 2);
      // <aspect name="withmanagerbean" class="org.jboss.test.microcontainer.beans.TestAspect" manager-bean="CustomManager"/> - 2 beans
      checkAspect(beans, "withmanagerbean", TestAspect.class, null, "PER_VM", "CustomManager", null);
   }
   
   public void testAopAspectWithManagerProperty() throws Exception
   {
      checkAspectWithManagerProperty("JAXBAnnotationSanityTestCase_AopAspectWithManagerProperty.xml");
   }
   
   public void testDeploymentAspectWithManagerProperty() throws Exception
   {
      checkAspectWithManagerProperty("JAXBAnnotationSanityTestCase_DeploymentAspectWithManagerProperty.xml");
   }
   
   private void checkAspectWithManagerProperty(String name) throws Exception
   {
      List<BeanMetaData> beans = unmarshal(name, 2);
      // <aspect name="withmanagerproperty" class="org.jboss.test.microcontainer.beans.TestAspect" manager-bean="CustomManager" manager-property="property"/>
      checkAspect(beans, "withmanagerproperty", TestAspect.class, null, "PER_VM", "CustomManager", "property");
   }
   
   public void testAopAspectWithDependency() throws Exception
   {
      checkAspectWithDependency("JAXBAnnotationSanityTestCase_AopAspectWithDependency.xml");
   }

   public void testDeploymentAspectWithDependency() throws Exception
   {
      checkAspectWithDependency("JAXBAnnotationSanityTestCase_DeploymentAspectWithDependency.xml");
   }

   private void checkAspectWithDependency(String name) throws Exception
   {
      List<BeanMetaData> beans = unmarshal(name, 2);
      // <aop:aspect name="withdependency" class="org.jboss.test.microcontainer.beans.TestAspectWithDependency"> - 2 beans
      //    <property name="dependency"><inject bean="Dependency"/></property>
      // </aop:aspect>
      checkAspect(beans, "withdependency", TestAspectWithDependency.class, null, "PER_VM", null, null);
      checkDependency(beans, "Factory$withdependency", "dependency", "Dependency", null);
   }

   public void testAopInterceptorWithDependency() throws Exception
   {
      checkInterceptorWithDependency("JAXBAnnotationSanityTestCase_AopInterceptorWithDependency.xml");
   }

   public void testDeploymentInterceptorWithDependency() throws Exception
   {
      checkInterceptorWithDependency("JAXBAnnotationSanityTestCase_DeploymentInterceptorWithDependency.xml");
   }

   private void checkInterceptorWithDependency(String name) throws Exception
   {
      List<BeanMetaData> beans = unmarshal(name, 2);
      // <aop:Interceptor name="withdependency" class="org.jboss.test.microcontainer.beans.TestInterceptorWithDependency"> - 2 beans
      //    <property name="dependency"><inject bean="Dependency"/></property>
      // </aop:Interceptor>
      checkAspect(beans, "withdependency", TestInterceptorWithDependency.class, null, "PER_VM", null, null);
      checkDependency(beans, "Factory$withdependency", "dependency", "Dependency", null);
   }

   public void testAopAspectAndBean() throws Exception
   {
      checkAspectAndBean("JAXBAnnotationSanityTestCase_AopAspectAndBean.xml");
   }

   public void testDeploymentAspectAndBean() throws Exception
   {
      checkAspectAndBean("JAXBAnnotationSanityTestCase_DeploymentAspectAndBean.xml");
   }

   private void checkAspectAndBean(String name) throws Exception
   {
      List<BeanMetaData> beans = unmarshal(name, 5);
      // <aspect xmlns="urn:jboss:aop-beans:1.0" name="aspect" class="org.jboss.test.microcontainer.beans.TestAspect"/>
      // <bean name="bean1" class="Bean1"/>
      // <bean name="bean2" class="Bean2"/>
      // <bean name="bean3" class="Bean3"/>
      checkAspect(beans, "aspect", TestAspect.class, null, "PER_VM", null, null);
      checkBeanByName(beans, "bean1", "Bean1");
      checkBeanByName(beans, "bean2", "Bean2");
      checkBeanByName(beans, "bean3", "Bean3");
   }

   public void testAopBind() throws Exception
   {
      checkBind("JAXBAnnotationSanityTestCase_AopBind.xml");
   }

   public void testDeploymentBind() throws Exception
   {
      checkBind("JAXBAnnotationSanityTestCase_DeploymentBind.xml");
   }

   private void checkBind(String name) throws Exception
   {
      List<BeanMetaData> beans = unmarshal(name, 9);

      BeanMetaData bind = checkBeanByName(beans, "binding", AspectBinding.class.getName());
      assertEquals("all(Blah)", getSimpleProperty(bind, "pointcut"));
      assertEquals("hello", getSimpleProperty(bind, "cflow"));
      checkDependency(bind, "manager", "ManagerBean", "ManagerProperty");
      //TODO Check we have references to the interceptors/stacks
      
      BeanMetaData stackRef = checkBeanByName(beans, "binding$0", StackEntry.class.getName());
      assertEquals("TestStack", getSimpleProperty(stackRef, "stack"));
      checkDependency(stackRef, "binding", "binding", null);
      
      BeanMetaData adviceRef = checkBeanByName(beans, "binding$1", InterceptorEntry.class.getName());
      assertEquals(AdviceType.AROUND, getSimpleProperty(adviceRef, "type"));
      checkDependency(adviceRef, "aspect", "AdviceAspect", null);
      assertEquals("advice", getSimpleProperty(adviceRef, "aspectMethod"));
      
      BeanMetaData aroundRef = checkBeanByName(beans, "binding$2", InterceptorEntry.class.getName());
      assertEquals(AdviceType.AROUND, getSimpleProperty(aroundRef, "type"));
      checkDependency(aroundRef, "aspect", "AroundAspect", null);
      assertEquals("around", getSimpleProperty(aroundRef, "aspectMethod"));
      
      BeanMetaData beforeRef = checkBeanByName(beans, "binding$3", InterceptorEntry.class.getName());
      assertEquals(AdviceType.BEFORE, getSimpleProperty(beforeRef, "type"));
      checkDependency(beforeRef, "aspect", "BeforeAspect", null);
      assertEquals("before", getSimpleProperty(beforeRef, "aspectMethod"));
      
      BeanMetaData afterRef = checkBeanByName(beans, "binding$4", InterceptorEntry.class.getName());
      assertEquals(AdviceType.AFTER, getSimpleProperty(afterRef, "type"));
      checkDependency(afterRef, "aspect", "AfterAspect", null);
      assertEquals("after", getSimpleProperty(afterRef, "aspectMethod"));
      
      BeanMetaData throwingRef = checkBeanByName(beans, "binding$5", InterceptorEntry.class.getName());
      assertEquals(AdviceType.THROWING, getSimpleProperty(throwingRef, "type"));
      checkDependency(throwingRef, "aspect", "ThrowingAspect", null);
      assertEquals("throwing", getSimpleProperty(throwingRef, "aspectMethod"));
      
      BeanMetaData finallyRef = checkBeanByName(beans, "binding$6", InterceptorEntry.class.getName());
      assertEquals(AdviceType.FINALLY, getSimpleProperty(finallyRef, "type"));
      checkDependency(finallyRef, "aspect", "FinallyAspect", null);
      assertEquals("finally", getSimpleProperty(finallyRef, "aspectMethod"));
      
      BeanMetaData interceptorRef = checkBeanByName(beans, "binding$7", InterceptorEntry.class.getName());
      assertEquals(AdviceType.AROUND, getSimpleProperty(interceptorRef, "type"));
      checkDependency(interceptorRef, "aspect", "Interceptor", null);
      assertEquals(null, getSimpleProperty(interceptorRef, "aspectMethod"));
      
   }

   public void testAopStack() throws Exception
   {
      checkStack("JAXBAnnotationSanityTestCase_AopStack.xml");
   }

   public void testDeploymentStack() throws Exception
   {
      checkStack("JAXBAnnotationSanityTestCase_DeploymentStack.xml");
   }

   private void checkStack(String name) throws Exception
   {
      List<BeanMetaData> beans = unmarshal(name, 9);

      BeanMetaData stack = checkBeanByName(beans, "TestStack", Stack.class.getName());
      checkDependency(stack, "manager", "ManagerBean", "ManagerProperty");
      //TODO Check we have references to the interceptors/stacks
      
      BeanMetaData stackRef = checkBeanByName(beans, "TestStack$0", StackEntry.class.getName());
      assertEquals("TestStack", getSimpleProperty(stackRef, "stack"));
      assertEquals(Boolean.TRUE, getSimpleProperty(stackRef, "forStack"));
      
      BeanMetaData adviceRef = checkBeanByName(beans, "TestStack$1", InterceptorEntry.class.getName());
      assertEquals(AdviceType.AROUND, getSimpleProperty(adviceRef, "type"));
      checkDependency(adviceRef, "aspect", "AdviceAspect", null);
      assertEquals("advice", getSimpleProperty(adviceRef, "aspectMethod"));
      
      BeanMetaData aroundRef = checkBeanByName(beans, "TestStack$2", InterceptorEntry.class.getName());
      assertEquals(AdviceType.AROUND, getSimpleProperty(aroundRef, "type"));
      checkDependency(aroundRef, "aspect", "AroundAspect", null);
      assertEquals("around", getSimpleProperty(aroundRef, "aspectMethod"));
      
      BeanMetaData beforeRef = checkBeanByName(beans, "TestStack$3", InterceptorEntry.class.getName());
      assertEquals(AdviceType.BEFORE, getSimpleProperty(beforeRef, "type"));
      checkDependency(beforeRef, "aspect", "BeforeAspect", null);
      assertEquals("before", getSimpleProperty(beforeRef, "aspectMethod"));
      
      BeanMetaData afterRef = checkBeanByName(beans, "TestStack$4", InterceptorEntry.class.getName());
      assertEquals(AdviceType.AFTER, getSimpleProperty(afterRef, "type"));
      checkDependency(afterRef, "aspect", "AfterAspect", null);
      assertEquals("after", getSimpleProperty(afterRef, "aspectMethod"));
      
      BeanMetaData throwingRef = checkBeanByName(beans, "TestStack$5", InterceptorEntry.class.getName());
      assertEquals(AdviceType.THROWING, getSimpleProperty(throwingRef, "type"));
      checkDependency(throwingRef, "aspect", "ThrowingAspect", null);
      assertEquals("throwing", getSimpleProperty(throwingRef, "aspectMethod"));
      
      BeanMetaData finallyRef = checkBeanByName(beans, "TestStack$6", InterceptorEntry.class.getName());
      assertEquals(AdviceType.FINALLY, getSimpleProperty(finallyRef, "type"));
      checkDependency(finallyRef, "aspect", "FinallyAspect", null);
      assertEquals("finally", getSimpleProperty(finallyRef, "aspectMethod"));
      
      BeanMetaData interceptorRef = checkBeanByName(beans, "TestStack$7", InterceptorEntry.class.getName());
      assertEquals(AdviceType.AROUND, getSimpleProperty(interceptorRef, "type"));
      checkDependency(interceptorRef, "aspect", "Interceptor", null);
      assertEquals(null, getSimpleProperty(interceptorRef, "aspectMethod"));
      
   }

   public void testAopTypeDef() throws Exception
   {
      checkTypeDef("JAXBAnnotationSanityTestCase_AopTypeDef.xml");
   }

   public void testDeploymentTypeDef() throws Exception
   {
      checkTypeDef("JAXBAnnotationSanityTestCase_DeploymentTypeDef.xml");
   }

   private void checkTypeDef(String name) throws Exception
   {
      List<BeanMetaData> beans = unmarshal(name, 1);

      BeanMetaData typedef = checkBeanByName(beans, "TypeDef", TypeDef.class.getName());
      assertEquals("class(org.jboss.test.microcontainer.beans.POJO)", getSimpleProperty(typedef, "expr"));
      checkDependency(typedef, "manager", "ManagerBean", "ManagerProperty");
      //TODO Check we have references to the interceptors/stacks
   }

   private void checkAspect(List<BeanMetaData> beans, String name, Class<?> aspectClass, Class<?> factoryClass, String scope, String managerBean, String managerProperty)
   {
      if (aspectClass == null && factoryClass == null)
      {
         throw new RuntimeException("Must have either aspectClass or factoryClass");
      }
      BeanMetaData aspect = checkBeanByName(beans, name, Aspect.class.getName());
      assertEquals(scope, getSimpleProperty(aspect, "scope"));
      Boolean isfactory = factoryClass != null ? Boolean.TRUE : null;
      assertEquals(isfactory, getSimpleProperty(aspect, "factory"));

      
      BeanMetaData beanfactory = getBeanByName(beans, "Factory$" + name);
      assertEquals(GenericBeanFactory.class.getName(), beanfactory.getBean());
     
      String aspectClassName = aspectClass == null ? factoryClass.getName() : aspectClass.getName();
      assertEquals(aspectClassName, getSimpleProperty(beanfactory, "bean"));

      if (managerBean != null || managerProperty != null)
      {
         checkDependency(aspect, "manager", managerBean, managerProperty);
      }
   }
   
   private BeanMetaData checkBeanByName(List<BeanMetaData> beans, String name, String classname)
   {
      BeanMetaData bean = getBeanByName(beans, name);
      assertEquals(classname, bean.getBean());
      return bean;
   }
   
   private BeanMetaData getBeanByName(List<BeanMetaData> beans, String name)
   {
      for (BeanMetaData bean : beans)
      {
         assertNotNull(bean.getName(), bean + " has no name");
         if (bean.getName().equals(name))
         {
            return bean;
         }
      }
      
      fail("No bean found with name '" + name + "'");
      return null;
   }
   
   private Object getSimpleProperty(BeanMetaData bean, String name)
   {
      Set<PropertyMetaData> pmd = bean.getProperties();
      for (PropertyMetaData data : pmd)
      {
         if (data instanceof AbstractPropertyMetaData)
         {
            AbstractPropertyMetaData apmd = (AbstractPropertyMetaData)data;
            if (name.equals(apmd.getName()))
            {
               AbstractValueMetaData avmd = (AbstractValueMetaData)apmd.getValue();
               return avmd.getUnderlyingValue();
            }
            
         }
      }
      return null;
   }
   
   private void checkDependency(List<BeanMetaData> beans, String beanName, String name, String value, String property)
   {
      BeanMetaData bean = getBeanByName(beans, beanName);
      checkDependency(bean, name, value, property);
   }
   
   private void checkDependency(BeanMetaData bean, String name, String value, String property)
   {
      AbstractDependencyValueMetaData dependency = getDependencyProperty(bean, name);
      assertNotNull(dependency);
      assertEquals(value, dependency.getValue());
      assertEquals(property, dependency.getProperty());
   }
   
   private AbstractDependencyValueMetaData getDependencyProperty(BeanMetaData bean, String name)
   {
      Set<PropertyMetaData> pmd = bean.getProperties();
      for (PropertyMetaData data : pmd)
      {
         if (data instanceof AbstractPropertyMetaData)
         {
            AbstractPropertyMetaData apmd = (AbstractPropertyMetaData)data;
            if (name.equals(apmd.getName()))
            {
               return (AbstractDependencyValueMetaData)apmd.getValue();
            }
         }
      }
      return null;
   }
   
   private List<BeanMetaData> unmarshal(String name, int expectedBeans) throws Exception
   {
      URL url = getClass().getResource(name);
      assertNotNull(url);
      KernelDeployment deployment = unmarshal(url);
      List<BeanMetaData> beans = deployment.getBeans();
      assertNotNull(beans);
      assertEquals(expectedBeans, beans.size());
      return beans;
   }
   
}
