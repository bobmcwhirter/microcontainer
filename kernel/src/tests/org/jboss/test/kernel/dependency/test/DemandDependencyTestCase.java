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
import org.jboss.beans.metadata.plugins.AbstractDemandMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.AbstractSupplyMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.DemandMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
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
public class DemandDependencyTestCase extends OldAbstractKernelDependencyTest
{
   public static Test suite()
   {
      return suite(DemandDependencyTestCase.class);
   }

   public DemandDependencyTestCase(String name) throws Throwable
   {
      super(name);
   }

   public DemandDependencyTestCase(String name, boolean xmltest) throws Throwable
   {
      super(name, xmltest);
   }

   public void testDemandDependencyCorrectOrder() throws Throwable
   {
      demandDependencyCorrectOrder();
      
      ControllerContext context1 = assertInstall(0, "Name1");      
      ControllerContext context2 = assertInstall(1, "Name2");      
      
      SimpleBean bean1 = (SimpleBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      SimpleBean bean2 = (SimpleBean) context2.getTarget();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());
   }

   public void demandDependencyCorrectOrder() throws Throwable
   {
      buildMetaData();
   }

   public void testDemandDependencyWrongOrder() throws Throwable
   {
      demandDependencyWrongOrder();
      
      ControllerContext context2 = assertInstall(1, "Name2", getWhenRequiredState());
      ControllerContext context1 = assertInstall(0, "Name1");      
      assertEquals(ControllerState.INSTALLED, context2.getState());
      
      SimpleBean bean1 = (SimpleBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      SimpleBean bean2 = (SimpleBean) context2.getTarget();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());
   }

   public void demandDependencyWrongOrder() throws Throwable
   {
      buildMetaData();
   }

   public void testDemandDependencyReinstall() throws Throwable
   {
      demandDependencyReinstall();
      
      ControllerContext context1 = assertInstall(0, "Name1");      
      ControllerContext context2 = assertInstall(1, "Name2");      
      
      SimpleBean bean1 = (SimpleBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      SimpleBean bean2 = (SimpleBean) context2.getTarget();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());

      assertUninstall("Name1");
      assertEquals(ControllerState.ERROR, context1.getState());
      assertEquals(getWhenRequiredState(), context2.getState());

      assertNotInstalled("Name2");
      assertContext("Name2", getWhenRequiredState());
      
      context1 = assertInstall(0, "Name1");      
      assertNotNull(context1);
      assertEquals(ControllerState.INSTALLED, context2.getState());
      
      bean1 = (SimpleBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      
      bean2 = (SimpleBean) context2.getTarget();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());

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
      
      bean2 = (SimpleBean) context2.getTarget();
      assertNotNull(bean2);
      assertEquals("String2", bean2.getString());
   }

   public void demandDependencyReinstall() throws Throwable
   {
      buildMetaData();
   }

   protected ControllerState getWhenRequiredState()
   {
      return ControllerState.PRE_INSTALL;
   }

   protected void buildMetaData()
   {
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("Name1", SimpleBeanImpl.class.getName());
      HashSet<PropertyMetaData> attributes1 = new HashSet<PropertyMetaData>();
      attributes1.add(new AbstractPropertyMetaData("string", "String1"));
      metaData1.setProperties(attributes1);
      HashSet<SupplyMetaData> supplies = new HashSet<SupplyMetaData>();
      supplies.add(new AbstractSupplyMetaData("WhatIWant"));
      metaData1.setSupplies(supplies);

      AbstractBeanMetaData metaData2 = new AbstractBeanMetaData("Name2", SimpleBeanImpl.class.getName());
      HashSet<PropertyMetaData> attributes2 = new HashSet<PropertyMetaData>();
      attributes2.add(new AbstractPropertyMetaData("string", "String2"));
      metaData2.setProperties(attributes2);
      HashSet<DemandMetaData> demands = new HashSet<DemandMetaData>();
      demands.add(new AbstractDemandMetaData("WhatIWant"));
      metaData2.setDemands(demands);

      setBeanMetaDatas(new BeanMetaData[] { metaData1, metaData2 });
   }
}