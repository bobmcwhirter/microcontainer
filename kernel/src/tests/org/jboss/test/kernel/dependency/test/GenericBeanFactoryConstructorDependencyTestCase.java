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
package org.jboss.test.kernel.dependency.test;

import java.util.ArrayList;

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactory;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.kernel.dependency.support.SimpleBean;
import org.jboss.test.kernel.dependency.support.SimpleBeanImpl;
import org.jboss.test.kernel.dependency.support.SimpleBeanWithConstructorDependency;
import org.jboss.test.kernel.dependency.support.SimpleBeanWithConstructorDependencyImpl;

/**
 * Constructor Dependency Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class GenericBeanFactoryConstructorDependencyTestCase extends OldAbstractKernelDependencyTest
{
   public static Test suite()
   {
      return suite(GenericBeanFactoryConstructorDependencyTestCase.class);
   }

   public GenericBeanFactoryConstructorDependencyTestCase(String name) throws Throwable
   {
      super(name);
   }

   public GenericBeanFactoryConstructorDependencyTestCase(String name, boolean xmltest) throws Throwable
   {
      super(name, xmltest);
   }

   public void testGenericBeanFactoryConstructorDependencyCorrectOrder() throws Throwable
   {
      constructorDependencyCorrectOrder();

      ControllerContext context1 = assertInstall(0, "Name1");
      ControllerContext context2 = assertInstall(1, "Name2");
      
      GenericBeanFactory factory1 = (GenericBeanFactory) context1.getTarget();
      SimpleBean bean1 = (SimpleBean) factory1.createBean();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      GenericBeanFactory factory2 = (GenericBeanFactory) context2.getTarget();
      SimpleBeanWithConstructorDependency bean2 = (SimpleBeanWithConstructorDependency) factory2.createBean();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());
      assertEquals(factory1, bean2.getFactory());
   }

   public void constructorDependencyCorrectOrder() throws Throwable
   {
      buildMetaData();
   }

   public void testGenericBeanFactoryConstructorDependencyWrongOrder() throws Throwable
   {
      constructorDependencyWrongOrder();

      ControllerContext context2 = assertInstall(1, "Name2", ControllerState.INSTANTIATED);
      
      ControllerContext context1 = assertInstall(0, "Name1");
      assertEquals(ControllerState.INSTALLED, context2.getState());
      
      GenericBeanFactory factory1 = (GenericBeanFactory) context1.getTarget();
      SimpleBean bean1 = (SimpleBean) factory1.createBean();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      GenericBeanFactory factory2 = (GenericBeanFactory) context2.getTarget();
      SimpleBeanWithConstructorDependency bean2 = (SimpleBeanWithConstructorDependency) factory2.createBean();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());
      assertEquals(factory1, bean2.getFactory());
   }

   public void constructorDependencyWrongOrder() throws Throwable
   {
      buildMetaData();
   }

   public void testGenericBeanFactoryConstructorDependencyReinstall() throws Throwable
   {
      constructorDependencyReinstall();

      ControllerContext context1 = assertInstall(0, "Name1");
      ControllerContext context2 = assertInstall(1, "Name2");
      
      GenericBeanFactory factory1 = (GenericBeanFactory) context1.getTarget();
      SimpleBean bean1 = (SimpleBean) factory1.createBean();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      GenericBeanFactory factory2 = (GenericBeanFactory) context2.getTarget();
      SimpleBeanWithConstructorDependency bean2 = (SimpleBeanWithConstructorDependency) factory2.createBean();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());
      assertEquals(factory1, bean2.getFactory());

      assertUninstall("Name1");
      assertEquals(ControllerState.ERROR, context1.getState());
      assertEquals(ControllerState.INSTANTIATED, context2.getState());

      assertNotInstalled("Name2");

      context2 = assertContext("Name2", ControllerState.INSTANTIATED);
      
      context1 = assertInstall(0, "Name1");
      assertEquals(ControllerState.INSTALLED, context2.getState());
      
      factory1 = (GenericBeanFactory) context1.getTarget();
      bean1 = (SimpleBean) factory1.createBean();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      factory2 = (GenericBeanFactory) context2.getTarget();
      bean2 = (SimpleBeanWithConstructorDependency) factory2.createBean();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());
      assertEquals(factory1, bean2.getFactory());
      
      assertUninstall("Name2");
      assertEquals(ControllerState.INSTALLED, context1.getState());
      assertEquals(ControllerState.ERROR, context2.getState());
      
      factory1 = (GenericBeanFactory) context1.getTarget();
      bean1 = (SimpleBean) factory1.createBean();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());

      context2 = assertInstall(1, "Name2");
      
      factory1 = (GenericBeanFactory) context1.getTarget();
      bean1 = (SimpleBean) factory1.createBean();
      assertEquals("String1", bean1.getString());
      
      factory2 = (GenericBeanFactory) context2.getTarget();
      bean2 = (SimpleBeanWithConstructorDependency) factory2.createBean();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());
      assertEquals(factory1, bean2.getFactory());
   }

   public void constructorDependencyReinstall() throws Throwable
   {
      buildMetaData();
   }

   protected void buildMetaData()
   {
      GenericBeanFactoryMetaData metaData1 = new GenericBeanFactoryMetaData("Name1", SimpleBeanImpl.class.getName());
      metaData1.addBeanProperty(new AbstractPropertyMetaData("string", "String1"));

      GenericBeanFactoryMetaData metaData2 = new GenericBeanFactoryMetaData("Name2", SimpleBeanWithConstructorDependencyImpl.class.getName());
      metaData2.addBeanProperty(new AbstractPropertyMetaData("string", "String2"));
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      ArrayList<ParameterMetaData> constructor2 = new ArrayList<ParameterMetaData>();
      constructor2.add(new AbstractParameterMetaData(GenericBeanFactory.class.getName(), new AbstractDependencyValueMetaData("Name1")));
      cmd.setParameters(constructor2);
      metaData2.setBeanConstructor(cmd);

      setBeanMetaDatas(new BeanMetaData[] { metaData1, metaData2 });
   }
}