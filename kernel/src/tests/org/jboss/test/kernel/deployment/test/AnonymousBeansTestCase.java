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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractListMetaData;
import org.jboss.beans.metadata.plugins.AbstractMapMetaData;
import org.jboss.beans.metadata.plugins.AbstractSetMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.kernel.plugins.deployment.AbstractKernelDeployment;
import org.jboss.test.kernel.deployment.support.SimpleBean;
import org.jboss.test.kernel.deployment.support.SimpleBeanImpl;
import org.jboss.test.kernel.deployment.support.SimpleObjectWithBeans;

/**
 * Test anonymous beans.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AnonymousBeansTestCase extends AbstractDeploymentTest
{
   public AnonymousBeansTestCase(String name) throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(AnonymousBeansTestCase.class);
   }

   // ---- tests

   protected void doSetupBeans() throws Throwable
   {
      AbstractKernelDeployment deployment = new AbstractKernelDeployment();
      deployment.setName("somename");
      List<BeanMetaDataFactory> beans = new ArrayList<BeanMetaDataFactory>();
      deployment.setBeanFactories(beans);

      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder("const_value", SimpleObjectWithBeans.class.getName());
      builder.setConstructorValue(new AbstractBeanMetaData(null, SimpleObjectWithBeans.class.getName()));
      beans.add((BeanMetaDataFactory)builder.getBeanMetaData());

      builder = BeanMetaDataBuilder.createBuilder("const_param", SimpleObjectWithBeans.class.getName());
      builder.addConstructorParameter(SimpleBean.class.getName(), new AbstractBeanMetaData(null, SimpleBeanImpl.class.getName()));
      beans.add((BeanMetaDataFactory)builder.getBeanMetaData());

      builder = BeanMetaDataBuilder.createBuilder("prop", SimpleObjectWithBeans.class.getName());
      builder.addPropertyMetaData("simpleBean", new AbstractBeanMetaData(null, SimpleBeanImpl.class.getName()));
      beans.add((BeanMetaDataFactory)builder.getBeanMetaData());

      builder = BeanMetaDataBuilder.createBuilder("list", SimpleObjectWithBeans.class.getName());
      AbstractListMetaData list = new AbstractListMetaData();
      list.add(new AbstractBeanMetaData(null, SimpleBeanImpl.class.getName()));
      builder.addPropertyMetaData("list", list);
      beans.add((BeanMetaDataFactory)builder.getBeanMetaData());

      builder = BeanMetaDataBuilder.createBuilder("set", SimpleObjectWithBeans.class.getName());
      AbstractSetMetaData set = new AbstractSetMetaData();
      set.add(new AbstractBeanMetaData(null, SimpleBeanImpl.class.getName()));
      builder.addPropertyMetaData("set", set);
      beans.add((BeanMetaDataFactory)builder.getBeanMetaData());

      builder = BeanMetaDataBuilder.createBuilder("map", SimpleObjectWithBeans.class.getName());
      AbstractMapMetaData map = new AbstractMapMetaData();
      map.put(new AbstractBeanMetaData(null, SimpleBeanImpl.class.getName()), new AbstractBeanMetaData(null, SimpleBeanImpl.class.getName()));
      builder.addPropertyMetaData("map", map);
      beans.add((BeanMetaDataFactory)builder.getBeanMetaData());

      builder = BeanMetaDataBuilder.createBuilder("nested", SimpleObjectWithBeans.class.getName());
      BeanMetaDataBuilder nested = BeanMetaDataBuilder.createBuilder(null, SimpleObjectWithBeans.class.getName());
      nested.addConstructorParameter(SimpleBean.class.getName(), new AbstractBeanMetaData(null, SimpleBeanImpl.class.getName()));
      builder.addPropertyMetaData("nested", nested.getBeanMetaData());
      beans.add((BeanMetaDataFactory)builder.getBeanMetaData());

      deploy(deployment);
   }

   public void testAnonymous() throws Throwable
   {
      doSetupBeans();

      SimpleObjectWithBeans const_value = assertBean("const_value", SimpleObjectWithBeans.class);
      SimpleObjectWithBeans const_value_1 = assertBean("const_value#1", SimpleObjectWithBeans.class);
      assertSame(const_value, const_value_1);

      SimpleObjectWithBeans const_param = assertBean("const_param", SimpleObjectWithBeans.class);
      SimpleBean const_param_1 = assertBean("const_param#1", SimpleBean.class);
      assertSame(const_param.getSimpleBean(), const_param_1);

      SimpleObjectWithBeans prop = assertBean("prop", SimpleObjectWithBeans.class);
      SimpleBean prop_bean = assertBean("prop$simpleBean#1", SimpleBean.class);
      assertSame(prop.getSimpleBean(), prop_bean);

      SimpleObjectWithBeans list = assertBean("list", SimpleObjectWithBeans.class);
      SimpleBean list_bean = assertBean("list#1", SimpleBean.class);
      assertNotNull(list.getList());
      assertFalse(list.getList().isEmpty());
      assertSame(list.getList().get(0), list_bean);

      SimpleObjectWithBeans set = assertBean("set", SimpleObjectWithBeans.class);
      SimpleBean set_bean = assertBean("set#1", SimpleBean.class);
      assertNotNull(set.getSet());
      assertFalse(set.getSet().isEmpty());
      assertSame(set.getSet().iterator().next(), set_bean);

      SimpleObjectWithBeans map = assertBean("map", SimpleObjectWithBeans.class);
      SimpleBean map_bean_1 = assertBean("map#1", SimpleBean.class);
      SimpleBean map_bean_2 = assertBean("map#2", SimpleBean.class);
      assertNotNull(map.getMap());
      assertFalse(map.getMap().isEmpty());
      assertSame(map.getMap().keySet().iterator().next(), map_bean_1);
      assertSame(map.getMap().values().iterator().next(), map_bean_2);

      SimpleObjectWithBeans nested = assertBean("nested", SimpleObjectWithBeans.class);
      SimpleObjectWithBeans nested_nested = assertBean("nested$nested#2", SimpleObjectWithBeans.class);
      SimpleBean nested_1 = assertBean("nested#1", SimpleBean.class);
      assertSame(nested.getNested(), nested_nested);
      assertSame(nested_nested.getSimpleBean(), nested_1);
   }
}