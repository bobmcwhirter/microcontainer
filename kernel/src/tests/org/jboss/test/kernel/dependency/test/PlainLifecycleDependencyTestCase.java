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
import org.jboss.beans.metadata.plugins.AbstractDependencyMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.DependencyMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.kernel.dependency.support.SimpleBeanWithLifecycle;

/**
 * Lifecycle Dependency Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class PlainLifecycleDependencyTestCase extends OldAbstractKernelDependencyTest
{
   public static Test suite()
   {
      return suite(PlainLifecycleDependencyTestCase.class);
   }

   public PlainLifecycleDependencyTestCase(String name) throws Throwable
   {
      super(name);
   }

   public PlainLifecycleDependencyTestCase(String name, boolean xmltest) throws Throwable
   {
      super(name, xmltest);
   }

   public void testPlainLifecycleDependencyCorrectOrder() throws Throwable
   {
      plainLifecycleDependencyCorrectOrder();
      
      ControllerContext context1 = assertInstall(0, "Name1");
      ControllerContext context2 = assertInstall(1, "Name2");
      
      SimpleBeanWithLifecycle bean1 = (SimpleBeanWithLifecycle) context1.getTarget();
      assertNotNull(bean1);
      
      SimpleBeanWithLifecycle bean2 = (SimpleBeanWithLifecycle) context2.getTarget();
      assertNotNull(bean2);
      
      assertEquals(1, bean1.createOrder);
      assertEquals(3, bean2.createOrder);
      assertEquals(2, bean1.startOrder);
      assertEquals(4, bean2.startOrder);
   }

   public void plainLifecycleDependencyCorrectOrder() throws Throwable
   {
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("Name1", SimpleBeanWithLifecycle.class.getName());
      
      AbstractBeanMetaData metaData2 = new AbstractBeanMetaData("Name2", SimpleBeanWithLifecycle.class.getName());
      HashSet<DependencyMetaData> depends = new HashSet<DependencyMetaData>();
      depends.add(new AbstractDependencyMetaData("Name1"));
      metaData2.setDepends(depends);

      setBeanMetaDatas(new BeanMetaData[] { metaData1, metaData2 });
   }

   public void testPlainLifecycleDependencyWrongOrder() throws Throwable
   {
      plainLifecycleDependencyWrongOrder();
      
      ControllerContext context2 = assertInstall(1, "Name2", ControllerState.CONFIGURED);
      ControllerContext context1 = assertInstall(0, "Name1");
      assertEquals(ControllerState.INSTALLED, context2.getState());
      
      SimpleBeanWithLifecycle bean1 = (SimpleBeanWithLifecycle) context1.getTarget();
      assertNotNull(bean1);
      
      SimpleBeanWithLifecycle bean2 = (SimpleBeanWithLifecycle) context2.getTarget();
      assertNotNull(bean2);
      
      assertEquals(1, bean1.createOrder);
      assertEquals(2, bean2.createOrder);
      assertEquals(3, bean1.startOrder);
      assertEquals(4, bean2.startOrder);
   }

   public void plainLifecycleDependencyWrongOrder() throws Throwable
   {
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("Name1", SimpleBeanWithLifecycle.class.getName());
      
      AbstractBeanMetaData metaData2 = new AbstractBeanMetaData("Name2", SimpleBeanWithLifecycle.class.getName());
      HashSet<DependencyMetaData> depends = new HashSet<DependencyMetaData>();
      depends.add(new AbstractDependencyMetaData("Name1"));
      metaData2.setDepends(depends);

      setBeanMetaDatas(new BeanMetaData[] { metaData1, metaData2 });
   }

   public void testPlainLifecycleDependencyReinstall() throws Throwable
   {
      plainLifecycleDependencyReinstall();
      
      ControllerContext context1 = assertInstall(0, "Name1");
      ControllerContext context2 = assertInstall(1, "Name2");
      
      SimpleBeanWithLifecycle bean1 = (SimpleBeanWithLifecycle) context1.getTarget();
      assertNotNull(bean1);
      
      SimpleBeanWithLifecycle bean2 = (SimpleBeanWithLifecycle) context2.getTarget();
      assertNotNull(bean2);
      
      assertEquals(1, bean1.createOrder);
      assertEquals(2, bean1.startOrder);
      assertEquals(3, bean2.createOrder);
      assertEquals(4, bean2.startOrder);
      
      uninstall("Name1");
      assertContext("Name2", ControllerState.CONFIGURED);

      assertEquals(5, bean2.stopOrder);
      assertEquals(6, bean1.stopOrder);
      assertEquals(7, bean2.destroyOrder);
      assertEquals(8, bean1.destroyOrder);

      context1 = assertInstall(0, "Name1");
      
      bean1 = (SimpleBeanWithLifecycle) context1.getTarget();
      assertNotNull(bean1);
      assertContext("Name2", ControllerState.INSTALLED);
      
      assertEquals(9, bean1.createOrder);
      assertEquals(10, bean2.createOrder);
      assertEquals(11, bean1.startOrder);
      assertEquals(12, bean2.startOrder);
      
      uninstall("Name2");
      assertContext("Name1", ControllerState.INSTALLED);

      assertEquals(13, bean2.stopOrder);
      assertEquals(14, bean2.destroyOrder);
      assertEquals(-1, bean1.stopOrder);
      assertEquals(-1, bean1.destroyOrder);

      context2 = assertInstall(1, "Name2");
      
      bean2 = (SimpleBeanWithLifecycle) context2.getTarget();
      assertNotNull(bean2);
      assertContext("Name1", ControllerState.INSTALLED);
      
      assertEquals(15, bean2.createOrder);
      assertEquals(16, bean2.startOrder);
      assertEquals(9, bean1.createOrder);
      assertEquals(11, bean1.startOrder);
   }

   public void plainLifecycleDependencyReinstall() throws Throwable
   {
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("Name1", SimpleBeanWithLifecycle.class.getName());
      
      AbstractBeanMetaData metaData2 = new AbstractBeanMetaData("Name2", SimpleBeanWithLifecycle.class.getName());
      HashSet<DependencyMetaData> depends = new HashSet<DependencyMetaData>();
      depends.add(new AbstractDependencyMetaData("Name1"));
      metaData2.setDepends(depends);

      setBeanMetaDatas(new BeanMetaData[] { metaData1, metaData2 });
   }

   protected void setUp() throws Exception
   {
      super.setUp();
      SimpleBeanWithLifecycle.resetOrder();
   }
}