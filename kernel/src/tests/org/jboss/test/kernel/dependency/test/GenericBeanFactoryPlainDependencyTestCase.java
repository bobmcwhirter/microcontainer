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

import java.util.HashSet;

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactory;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.kernel.dependency.support.SimpleBeanImpl;
import org.jboss.test.kernel.dependency.support.SimpleBeanWithDependency;
import org.jboss.test.kernel.dependency.support.SimpleBeanWithDependencyImpl;

/**
 * GenericBeanFactory Dependency Test Case.
 * 
 * @author <a href="bill@jboss.com">Bill Burke</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class GenericBeanFactoryPlainDependencyTestCase extends OldAbstractKernelDependencyTest
{
   public static Test suite()
   {
      return suite(GenericBeanFactoryPlainDependencyTestCase.class);
   }
   
   public GenericBeanFactoryPlainDependencyTestCase(String name) throws Throwable
   {
      super(name);
   }

   public GenericBeanFactoryPlainDependencyTestCase(String name, boolean xmltest) throws Throwable
   {
      super(name, xmltest);
   }

   public void testGenericBeanFactoryDependencyCorrectOrder() throws Throwable
   {
      genericBeanFactoryDependencyCorrectOrder();

      assertInstall(0, "simple");
      ControllerContext context2 = assertInstall(1, "aspect");

      GenericBeanFactory factory = (GenericBeanFactory) context2.getTarget();
      SimpleBeanWithDependency bean = (SimpleBeanWithDependency) factory.createBean();
      assertEquals("factory", bean.getString());
      assertEquals("String1", bean.getSimpleBean().getString());
   }

   public void genericBeanFactoryDependencyCorrectOrder()
   {
      buildMetaData();
   }

   public void testGenericBeanFactoryDependencyWrongOrder() throws Throwable
   {
      genericBeanFactoryDependencyWrongOrder();

      ControllerContext context2 = assertInstall(1, "aspect", ControllerState.INSTANTIATED);
      assertInstall(0, "simple");
      assertEquals(ControllerState.INSTALLED, context2.getState());

      GenericBeanFactory factory = (GenericBeanFactory) context2.getTarget();
      SimpleBeanWithDependency bean = (SimpleBeanWithDependency) factory.createBean();
      assertEquals("factory", bean.getString());
      assertEquals("String1", bean.getSimpleBean().getString());
   }

   public void genericBeanFactoryDependencyWrongOrder()
   {
      buildMetaData();
   }

   public void testGenericBeanFactoryDependencyReinstall() throws Throwable
   {
      genericBeanFactoryDependencyReinstall();

      ControllerContext context1 = assertInstall(0, "simple");
      ControllerContext context2 = assertInstall(1, "aspect");

      GenericBeanFactory factory = (GenericBeanFactory) context2.getTarget();
      SimpleBeanWithDependency bean = (SimpleBeanWithDependency) factory.createBean();
      assertEquals("factory", bean.getString());
      assertEquals("String1", bean.getSimpleBean().getString());
      
      assertUninstall("simple");
      assertEquals(ControllerState.ERROR, context1.getState());
      assertEquals(ControllerState.INSTANTIATED, context2.getState());
      
      context1 = assertInstall(0, "simple");
      assertEquals(ControllerState.INSTALLED, context2.getState());

      factory = (GenericBeanFactory) context2.getTarget();
      bean = (SimpleBeanWithDependency) factory.createBean();
      assertEquals("factory", bean.getString());
      assertEquals("String1", bean.getSimpleBean().getString());
      
      assertUninstall("aspect");
      assertEquals(ControllerState.INSTALLED, context1.getState());
      assertEquals(ControllerState.ERROR, context2.getState());
      
      context2 = assertInstall(1, "aspect");

      factory = (GenericBeanFactory) context2.getTarget();
      bean = (SimpleBeanWithDependency) factory.createBean();
      assertEquals("factory", bean.getString());
      assertEquals("String1", bean.getSimpleBean().getString());
   }

   public void genericBeanFactoryDependencyReinstall()
   {
      buildMetaData();
   }

   protected void buildMetaData()
   {
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("simple", SimpleBeanImpl.class.getName());
      HashSet<PropertyMetaData> attributes1 = new HashSet<PropertyMetaData>();
      attributes1.add(new AbstractPropertyMetaData("string", "String1"));
      metaData1.setProperties(attributes1);

      GenericBeanFactoryMetaData metaData2 = new GenericBeanFactoryMetaData("aspect");
      metaData2.addProperty(new AbstractPropertyMetaData("bean", SimpleBeanWithDependencyImpl.class.getName()));
      metaData2.addProperty(new AbstractPropertyMetaData("constructor", new AbstractConstructorMetaData()));
      metaData2.addBeanProperty(new AbstractPropertyMetaData("simpleBean", new AbstractDependencyValueMetaData("simple")));
      metaData2.addBeanProperty(new AbstractPropertyMetaData("string", new AbstractValueMetaData("factory")));

      setBeanMetaDatas(new BeanMetaData[] { metaData1, metaData2 });
   }
}