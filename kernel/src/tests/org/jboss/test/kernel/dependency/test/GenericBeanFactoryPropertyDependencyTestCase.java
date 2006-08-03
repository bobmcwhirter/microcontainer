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

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactory;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.kernel.dependency.support.SimpleBean;
import org.jboss.test.kernel.dependency.support.SimpleBeanImpl;

/**
 * Property Dependency Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class GenericBeanFactoryPropertyDependencyTestCase extends OldAbstractKernelDependencyTest
{
   public static Test suite()
   {
      return suite(GenericBeanFactoryPropertyDependencyTestCase.class);
   }

   public GenericBeanFactoryPropertyDependencyTestCase(String name) throws Throwable
   {
      super(name);
   }

   public GenericBeanFactoryPropertyDependencyTestCase(String name, boolean xmltest) throws Throwable
   {
      super(name, xmltest);
   }

   public void testGenericBeanFactoryPropertyDependencyCorrectOrder() throws Throwable
   {
      propertyDependencyCorrectOrder();

      ControllerContext context1 = assertInstall(0, "Name1");
      ControllerContext context2 = assertInstall(1, "Name2");
      
      SimpleBean bean1 = (SimpleBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      GenericBeanFactory factory = (GenericBeanFactory) context2.getTarget(); 
      SimpleBean bean2 = (SimpleBean) factory.createBean();
      assertNotNull(bean2);
      assertEquals("String1", bean2.getString());
   }

   public void propertyDependencyCorrectOrder() throws Throwable
   {
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("Name1", SimpleBeanImpl.class.getName());
      metaData1.addProperty(new AbstractPropertyMetaData("string", "String1"));
      
      GenericBeanFactoryMetaData metaData2 = new GenericBeanFactoryMetaData("Name2", SimpleBeanImpl.class.getName());
      metaData2.addBeanProperty(new AbstractPropertyMetaData("string", new AbstractDependencyValueMetaData("Name1", "string")));
      
      setBeanMetaDatas(new BeanMetaData[] { metaData1, metaData2 });
   }

   public void testGenericBeanFactoryPropertyDependencyWrongOrder() throws Throwable
   {
      propertyDependencyWrongOrder();
      
      ControllerContext context2 = assertInstall(1, "Name2", ControllerState.INSTANTIATED);
      ControllerContext context1 = assertInstall(0, "Name1");
      assertEquals(ControllerState.INSTALLED, context2.getState());
      
      SimpleBean bean1 = (SimpleBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      GenericBeanFactory factory = (GenericBeanFactory) context2.getTarget(); 
      SimpleBean bean2 = (SimpleBean) factory.createBean();
      assertNotNull(bean2);
      assertEquals("String1", bean2.getString());
   }

   public void propertyDependencyWrongOrder() throws Throwable
   {
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("Name1", SimpleBeanImpl.class.getName());
      metaData1.addProperty(new AbstractPropertyMetaData("string", "String1"));
      
      GenericBeanFactoryMetaData metaData2 = new GenericBeanFactoryMetaData("Name2", SimpleBeanImpl.class.getName());
      metaData2.addBeanProperty(new AbstractPropertyMetaData("string", new AbstractDependencyValueMetaData("Name1", "string")));
      
      setBeanMetaDatas(new BeanMetaData[] { metaData1, metaData2 });
   }

   public void testGenericBeanFactoryPropertyDependencyReinstall() throws Throwable
   {
      propertyDependencyReinstall();

      ControllerContext context1 = assertInstall(0, "Name1");
      ControllerContext context2 = assertInstall(1, "Name2");
      
      SimpleBean bean1 = (SimpleBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      GenericBeanFactory factory = (GenericBeanFactory) context2.getTarget(); 
      SimpleBean bean2 = (SimpleBean) factory.createBean();
      assertNotNull(bean2);
      assertEquals("String1", bean2.getString());

      assertUninstall("Name1");

      assertEquals(ControllerState.ERROR, context1.getState());
      assertEquals(ControllerState.INSTANTIATED, context2.getState());
      assertNotInstalled("Name2");

      context2 = assertContext("Name2", ControllerState.INSTANTIATED);
      
      context1 = assertInstall(0, "Name1");
      assertEquals(ControllerState.INSTALLED, context2.getState());

      bean1 = (SimpleBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      factory = (GenericBeanFactory) context2.getTarget(); 
      bean2 = (SimpleBean) factory.createBean();
      assertNotNull(bean2);
      assertEquals("String1", bean2.getString());
      
      assertUninstall("Name2");
      
      context1 = assertContext("Name1");
      bean1 = (SimpleBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      context2 = assertInstall(1, "Name2");
      
      bean1 = (SimpleBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      factory = (GenericBeanFactory) context2.getTarget(); 
      bean2 = (SimpleBean) factory.createBean();
      assertNotNull(bean2);
      assertEquals("String1", bean2.getString());
   }

   public void propertyDependencyReinstall() throws Throwable
   {
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("Name1", SimpleBeanImpl.class.getName());
      metaData1.addProperty(new AbstractPropertyMetaData("string", "String1"));
      
      GenericBeanFactoryMetaData metaData2 = new GenericBeanFactoryMetaData("Name2", SimpleBeanImpl.class.getName());
      metaData2.addBeanProperty(new AbstractPropertyMetaData("string", new AbstractDependencyValueMetaData("Name1", "string")));
      
      setBeanMetaDatas(new BeanMetaData[] { metaData1, metaData2 });
   }
}