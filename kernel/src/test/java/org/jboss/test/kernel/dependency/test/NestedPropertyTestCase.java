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
package org.jboss.test.kernel.dependency.test;

import java.util.Collections;
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
import org.jboss.test.kernel.dependency.support.NestedBean;

/**
 * Nested property tests.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class NestedPropertyTestCase extends OldAbstractKernelDependencyTest
{
   public NestedPropertyTestCase(String name) throws Throwable
   {
      super(name);
   }

   public NestedPropertyTestCase(String name, boolean xmltest) throws Throwable
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(NestedPropertyTestCase.class);
   }

   public void testNestedSet() throws Throwable
   {
      buildSetMetaData();

      ControllerContext context = assertInstall(0, "NestedBean");
      Object target = context.getTarget();
      assertNotNull(target);
      assertInstanceOf(target, NestedBean.class);
      NestedBean root = (NestedBean)target;
      NestedBean lev1 = root.getBean();
      assertNotNull(lev1);
      NestedBean lev2 = lev1.getBean();
      assertNotNull(lev2);
      assertEquals("String12", lev2.getString());
   }

   public void testNestedInject() throws Throwable
   {
      buildInjectMetaData();

      ControllerContext context = assertInstall(1, "NestedBean");
      Object target = context.getTarget();
      assertNotNull(target);
      assertInstanceOf(target, NestedBean.class);
      NestedBean root = (NestedBean)target;
      NestedBean lev1 = root.getBean();
      assertNotNull(lev1);
      NestedBean lev2 = lev1.getBean();
      assertNotNull(lev2);
      assertEquals("String1234", lev2.getString());

      ControllerContext injecteeCC = assertInstall(0, "InjecteeBean");
      Object injectee = injecteeCC.getTarget();
      assertNotNull(injectee);
      assertInstanceOf(injectee, NestedBean.class);
      NestedBean injecteeBean = (NestedBean)injectee;
      assertEquals("String1234", injecteeBean.getString());
   }

   protected void buildSetMetaData() throws Throwable
   {
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("NestedBean", NestedBean.class.getName());
      AbstractConstructorMetaData constructor1 = new AbstractConstructorMetaData();
      ParameterMetaData parameter1 = new AbstractParameterMetaData(int.class.getName(), 5);
      constructor1.setParameters(Collections.singletonList(parameter1));
      metaData1.setConstructor(constructor1);
      HashSet<PropertyMetaData> attributes1 = new HashSet<PropertyMetaData>();
      attributes1.add(new AbstractPropertyMetaData("bean.bean.string", "String12"));
      metaData1.setProperties(attributes1);

      setBeanMetaDatas(new BeanMetaData[]{metaData1});
   }

   protected void buildInjectMetaData() throws Throwable
   {
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("InjecteeBean", NestedBean.class.getName());
      AbstractConstructorMetaData constructor1 = new AbstractConstructorMetaData();
      ParameterMetaData parameter1 = new AbstractParameterMetaData(int.class.getName(), 5);
      constructor1.setParameters(Collections.singletonList(parameter1));
      metaData1.setConstructor(constructor1);
      HashSet<PropertyMetaData> attributes1 = new HashSet<PropertyMetaData>();
      AbstractDependencyValueMetaData injectedValue = new AbstractDependencyValueMetaData("NestedBean", "bean.bean.string");
      AbstractPropertyMetaData propertyMetaData = new AbstractPropertyMetaData("string", injectedValue);
      attributes1.add(propertyMetaData);
      metaData1.setProperties(attributes1);

      AbstractBeanMetaData metaData2 = new AbstractBeanMetaData("NestedBean", NestedBean.class.getName());
      AbstractConstructorMetaData constructor2 = new AbstractConstructorMetaData();
      ParameterMetaData parameter2 = new AbstractParameterMetaData(int.class.getName(), 5);
      constructor2.setParameters(Collections.singletonList(parameter2));
      metaData2.setConstructor(constructor1);
      HashSet<PropertyMetaData> attributes2 = new HashSet<PropertyMetaData>();
      attributes2.add(new AbstractPropertyMetaData("bean.bean.string", "String1234"));
      metaData2.setProperties(attributes2);

      setBeanMetaDatas(new BeanMetaData[]{metaData1, metaData2});
   }
}