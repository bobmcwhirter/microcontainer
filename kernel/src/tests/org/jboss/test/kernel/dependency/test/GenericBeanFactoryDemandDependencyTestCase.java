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
import org.jboss.beans.metadata.plugins.AbstractDemandMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.AbstractSupplyMetaData;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactory;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.DemandMetaData;
import org.jboss.beans.metadata.spi.SupplyMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.kernel.dependency.support.SimpleBean;
import org.jboss.test.kernel.dependency.support.SimpleBeanImpl;

/**
 * Demand Dependency Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class GenericBeanFactoryDemandDependencyTestCase extends OldAbstractKernelDependencyTest
{
   public static Test suite()
   {
      return suite(GenericBeanFactoryDemandDependencyTestCase.class);
   }

   public GenericBeanFactoryDemandDependencyTestCase(String name) throws Throwable
   {
      super(name);
   }

   public GenericBeanFactoryDemandDependencyTestCase(String name, boolean xmltest) throws Throwable
   {
      super(name, xmltest);
   }

   public void testGenericBeanFactoryDemandDependencyCorrectOrder() throws Throwable
   {
      demandDependencyCorrectOrder();
      
      ControllerContext context1 = assertInstall(0, "Name1");      
      ControllerContext context2 = assertInstall(1, "Name2");      
      
      GenericBeanFactory factory1 = (GenericBeanFactory) context1.getTarget();
      SimpleBean bean1 = (SimpleBean) factory1.createBean();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      GenericBeanFactory factory2 = (GenericBeanFactory) context2.getTarget();
      SimpleBean bean2 = (SimpleBean) factory2.createBean();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());
   }

   public void demandDependencyCorrectOrder() throws Throwable
   {
      GenericBeanFactoryMetaData metaData1 = new GenericBeanFactoryMetaData("Name1", SimpleBeanImpl.class.getName());
      metaData1.addBeanProperty(new AbstractPropertyMetaData("string", "String1"));
      HashSet<SupplyMetaData> supplies = new HashSet<SupplyMetaData>();
      supplies.add(new AbstractSupplyMetaData("WhatIWant"));
      metaData1.setSupplies(supplies);
      
      GenericBeanFactoryMetaData metaData2 = new GenericBeanFactoryMetaData("Name2", SimpleBeanImpl.class.getName());
      metaData2.addBeanProperty(new AbstractPropertyMetaData("string", "String2"));
      HashSet<DemandMetaData> demands = new HashSet<DemandMetaData>();
      demands.add(new AbstractDemandMetaData("WhatIWant"));
      metaData2.setDemands(demands);
      
      setBeanMetaDatas(new BeanMetaData[] { metaData1, metaData2 });
   }

   public void testGenericBeanFactoryDemandDependencyWrongOrder() throws Throwable
   {
      demandDependencyWrongOrder();
      
      ControllerContext context2 = assertInstall(1, "Name2", ControllerState.PRE_INSTALL);      
      ControllerContext context1 = assertInstall(0, "Name1");      
      assertEquals(ControllerState.INSTALLED, context2.getState());
      
      GenericBeanFactory factory1 = (GenericBeanFactory) context1.getTarget();
      SimpleBean bean1 = (SimpleBean) factory1.createBean();
      assertEquals("String1", bean1.getString());
      
      GenericBeanFactory factory2 = (GenericBeanFactory) context2.getTarget();
      SimpleBean bean2 = (SimpleBean) factory2.createBean();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());
   }

   public void demandDependencyWrongOrder() throws Throwable
   {
      GenericBeanFactoryMetaData metaData1 = new GenericBeanFactoryMetaData("Name1", SimpleBeanImpl.class.getName());
      metaData1.addBeanProperty(new AbstractPropertyMetaData("string", "String1"));
      HashSet<SupplyMetaData> supplies = new HashSet<SupplyMetaData>();
      supplies.add(new AbstractSupplyMetaData("WhatIWant"));
      metaData1.setSupplies(supplies);
      
      GenericBeanFactoryMetaData metaData2 = new GenericBeanFactoryMetaData("Name2", SimpleBeanImpl.class.getName());
      metaData2.addBeanProperty(new AbstractPropertyMetaData("string", "String2"));
      HashSet<DemandMetaData> demands = new HashSet<DemandMetaData>();
      demands.add(new AbstractDemandMetaData("WhatIWant"));
      metaData2.setDemands(demands);
      
      setBeanMetaDatas(new BeanMetaData[] { metaData1, metaData2 });
   }

   public void testGenericBeanFactoryDemandDependencyReinstall() throws Throwable
   {
      demandDependencyReinstall();
      
      ControllerContext context1 = assertInstall(0, "Name1");      
      ControllerContext context2 = assertInstall(1, "Name2");      
      
      GenericBeanFactory factory1 = (GenericBeanFactory) context1.getTarget();
      SimpleBean bean1 = (SimpleBean) factory1.createBean();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      GenericBeanFactory factory2 = (GenericBeanFactory) context2.getTarget();
      SimpleBean bean2 = (SimpleBean) factory2.createBean();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());

      assertUninstall("Name1");
      assertEquals(ControllerState.ERROR, context1.getState());
      assertEquals(ControllerState.PRE_INSTALL, context2.getState());

      assertNotInstalled("Name2");
      assertContext("Name2", ControllerState.PRE_INSTALL);
      
      context1 = assertInstall(0, "Name1");      
      assertNotNull(context1);
      assertEquals(ControllerState.INSTALLED, context2.getState());
      
      factory1 = (GenericBeanFactory) context1.getTarget();
      bean1 = (SimpleBean) factory1.createBean();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      factory2 = (GenericBeanFactory) context2.getTarget();
      bean2 = (SimpleBean) factory2.createBean();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());

      assertUninstall("Name2");
      assertEquals(ControllerState.INSTALLED, context1.getState());
      assertEquals(ControllerState.ERROR, context2.getState());
      
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      context2 = assertInstall(1, "Name2");
      
      factory1 = (GenericBeanFactory) context1.getTarget();
      bean1 = (SimpleBean) factory1.createBean();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      factory2 = (GenericBeanFactory) context2.getTarget();
      bean2 = (SimpleBean) factory2.createBean();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());
   }

   public void demandDependencyReinstall() throws Throwable
   {
      GenericBeanFactoryMetaData metaData1 = new GenericBeanFactoryMetaData("Name1", SimpleBeanImpl.class.getName());
      metaData1.addBeanProperty(new AbstractPropertyMetaData("string", "String1"));
      HashSet<SupplyMetaData> supplies = new HashSet<SupplyMetaData>();
      supplies.add(new AbstractSupplyMetaData("WhatIWant"));
      metaData1.setSupplies(supplies);
      
      GenericBeanFactoryMetaData metaData2 = new GenericBeanFactoryMetaData("Name2", SimpleBeanImpl.class.getName());
      metaData2.addBeanProperty(new AbstractPropertyMetaData("string", "String2"));
      HashSet<DemandMetaData> demands = new HashSet<DemandMetaData>();
      demands.add(new AbstractDemandMetaData("WhatIWant"));
      metaData2.setDemands(demands);
      
      setBeanMetaDatas(new BeanMetaData[] { metaData1, metaData2 });
   }
}