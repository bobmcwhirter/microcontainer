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
import java.util.HashSet;

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
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
public class ConstructorDependencyTestCase extends OldAbstractKernelDependencyTest
{
   public static Test suite()
   {
      return suite(ConstructorDependencyTestCase.class);
   }

   public ConstructorDependencyTestCase(String name) throws Throwable
   {
      super(name);
   }

   public ConstructorDependencyTestCase(String name, boolean xmltest) throws Throwable
   {
      super(name, xmltest);
   }

   public void testConstructorDependencyCorrectOrder() throws Throwable
   {
      constructorDependencyCorrectOrder();

      ControllerContext context1 = assertInstall(0, "Name1");
      ControllerContext context2 = assertInstall(1, "Name2");
      
      SimpleBean bean1 = (SimpleBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      SimpleBeanWithConstructorDependency bean2 = (SimpleBeanWithConstructorDependency) context2.getTarget();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());
      assertEquals(bean1, bean2.getSimpleBean());
   }

   public void constructorDependencyCorrectOrder() throws Throwable
   {
      buildMetaData();
   }

   public void testConstructorDependencyWrongOrder() throws Throwable
   {
      constructorDependencyWrongOrder();

      ControllerContext context2 = assertInstall(1, "Name2", ControllerState.DESCRIBED);
      
      ControllerContext context1 = assertInstall(0, "Name1");
      assertEquals(ControllerState.INSTALLED, context2.getState());
      
      SimpleBean bean1 = (SimpleBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      SimpleBeanWithConstructorDependency bean2 = (SimpleBeanWithConstructorDependency) context2.getTarget();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());
      assertEquals(bean1, bean2.getSimpleBean());
   }

   public void constructorDependencyWrongOrder() throws Throwable
   {
      buildMetaData();
   }

   public void testConstructorDependencyReinstall() throws Throwable
   {
      constructorDependencyReinstall();

      ControllerContext context1 = assertInstall(0, "Name1");
      ControllerContext context2 = assertInstall(1, "Name2");
      
      SimpleBean bean1 = (SimpleBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      SimpleBeanWithConstructorDependency bean2 = (SimpleBeanWithConstructorDependency) context2.getTarget();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());
      assertEquals(bean1, bean2.getSimpleBean());

      assertUninstall("Name1");
      assertEquals(ControllerState.ERROR, context1.getState());
      assertEquals(ControllerState.DESCRIBED, context2.getState());

      assertNotInstalled("Name2");

      context2 = assertContext("Name2", ControllerState.DESCRIBED);
      
      context1 = assertInstall(0, "Name1");
      assertEquals(ControllerState.INSTALLED, context2.getState());
      
      bean1 = (SimpleBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      bean2 = (SimpleBeanWithConstructorDependency) context2.getTarget();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());
      assertEquals(bean1, bean2.getSimpleBean());
      
      assertUninstall("Name2");
      assertEquals(ControllerState.INSTALLED, context1.getState());
      assertEquals(ControllerState.ERROR, context2.getState());
      
      bean1 = (SimpleBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());

      context2 = assertInstall(1, "Name2");
      
      bean1 = (SimpleBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      bean2 = (SimpleBeanWithConstructorDependency) context2.getTarget();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());
      assertEquals(bean1, bean2.getSimpleBean());
   }

   public void constructorDependencyReinstall() throws Throwable
   {
      buildMetaData();
   }

   protected void buildMetaData()
   {
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("Name1", SimpleBeanImpl.class.getName());
      HashSet<PropertyMetaData> attributes1 = new HashSet<PropertyMetaData>();
      attributes1.add(new AbstractPropertyMetaData("string", "String1"));
      metaData1.setProperties(attributes1);

      AbstractBeanMetaData metaData2 = new AbstractBeanMetaData("Name2", SimpleBeanWithConstructorDependencyImpl.class.getName());
      HashSet<PropertyMetaData> attributes2 = new HashSet<PropertyMetaData>();
      attributes2.add(new AbstractPropertyMetaData("string", "String2"));
      metaData2.setProperties(attributes2);
      ArrayList<ParameterMetaData> constructor2 = new ArrayList<ParameterMetaData>();
      constructor2.add(new AbstractParameterMetaData(SimpleBean.class.getName(), new AbstractDependencyValueMetaData("Name1")));
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      metaData2.setConstructor(cmd);
      cmd.setParameters(constructor2);

      setBeanMetaDatas(new BeanMetaData[] { metaData1, metaData2 });
   }
}