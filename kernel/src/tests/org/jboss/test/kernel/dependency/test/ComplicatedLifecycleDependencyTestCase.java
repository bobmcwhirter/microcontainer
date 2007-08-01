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

import java.util.Collections;

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractLifecycleMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.kernel.dependency.support.SimpleBeanWithComplicatedLifecycle;
import org.jboss.test.kernel.dependency.support.SimpleBeanWithLifecycle;

/**
 * Lifecycle Dependency Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class ComplicatedLifecycleDependencyTestCase extends OldAbstractKernelDependencyTest
{
   public static Test suite()
   {
      return suite(ComplicatedLifecycleDependencyTestCase.class);
   }
   
   public ComplicatedLifecycleDependencyTestCase(String name) throws Throwable
   {
      super(name);
   }

   public ComplicatedLifecycleDependencyTestCase(String name, boolean xmltest) throws Throwable
   {
      super(name, xmltest);
   }

   public void testComplicatedLifecycleDependencyCorrectOrder() throws Throwable
   {
      complicatedLifecycleDependencyCorrectOrder();
      
      ControllerContext context1 = assertInstall(0, "Name1");
      ControllerContext context2 = assertInstall(1, "Name2");
      ControllerContext context3 = assertInstall(2, "Name3");
      ControllerContext context4 = assertInstall(3, "Name4");
      ControllerContext context5 = assertInstall(4, "Name5");
      
      SimpleBeanWithLifecycle bean1 = (SimpleBeanWithLifecycle) context1.getTarget();
      assertNotNull(bean1);
      
      SimpleBeanWithLifecycle bean2 = (SimpleBeanWithLifecycle) context2.getTarget();
      assertNotNull(bean2);
      
      SimpleBeanWithLifecycle bean3 = (SimpleBeanWithLifecycle) context3.getTarget();
      assertNotNull(bean3);
      
      SimpleBeanWithLifecycle bean4 = (SimpleBeanWithLifecycle) context4.getTarget();
      assertNotNull(bean4);
      
      SimpleBeanWithComplicatedLifecycle bean5 = (SimpleBeanWithComplicatedLifecycle) context5.getTarget();
      assertNotNull(bean5);
      
      assertEquals(1, bean1.createOrder);
      assertEquals(2, bean1.startOrder);
      assertEquals(3, bean2.createOrder);
      assertEquals(4, bean2.startOrder);
      assertEquals(5, bean3.createOrder);
      assertEquals(6, bean3.startOrder);
      assertEquals(7, bean4.createOrder);
      assertEquals(8, bean4.startOrder);
      assertEquals(9, bean5.createOrder);
      assertEquals(10, bean5.startOrder);
      assertTrue(bean1 == bean5.createBean);
      assertTrue(bean2 == bean5.startBean);
      assertNull(bean5.stopBean);
      assertNull(bean5.destroyBean);
      assertEquals(-1, bean1.stopOrder);
      assertEquals(-1, bean1.destroyOrder);
      assertEquals(-1, bean2.stopOrder);
      assertEquals(-1, bean2.destroyOrder);
      assertEquals(-1, bean3.stopOrder);
      assertEquals(-1, bean3.destroyOrder);
      assertEquals(-1, bean4.stopOrder);
      assertEquals(-1, bean4.destroyOrder);
      assertEquals(-1, bean5.stopOrder);
      assertEquals(-1, bean5.destroyOrder);
   }

   public void complicatedLifecycleDependencyCorrectOrder() throws Throwable
   {
      buildMetaData();
   }

   public void testComplicatedLifecycleDependencyWrongOrder() throws Throwable
   {
      complicatedLifecycleDependencyWrongOrder();
      
      ControllerContext context5 = assertInstall(4, "Name5", ControllerState.CONFIGURED);
      ControllerContext context1 = assertInstall(0, "Name1");
      ControllerContext context4 = assertInstall(3, "Name4");
      assertContext("Name5", ControllerState.CREATE);
      ControllerContext context2 = assertInstall(1, "Name2");
      ControllerContext context3 = assertInstall(2, "Name3");
      assertContext("Name5", ControllerState.INSTALLED);
      
      SimpleBeanWithLifecycle bean1 = (SimpleBeanWithLifecycle) context1.getTarget();
      assertNotNull(bean1);
      
      SimpleBeanWithLifecycle bean2 = (SimpleBeanWithLifecycle) context2.getTarget();
      assertNotNull(bean2);
      
      SimpleBeanWithLifecycle bean3 = (SimpleBeanWithLifecycle) context3.getTarget();
      assertNotNull(bean3);
      
      SimpleBeanWithLifecycle bean4 = (SimpleBeanWithLifecycle) context4.getTarget();
      assertNotNull(bean4);
      
      SimpleBeanWithComplicatedLifecycle bean5 = (SimpleBeanWithComplicatedLifecycle) context5.getTarget();
      assertNotNull(bean5);
      
      assertEquals(1, bean1.createOrder);
      assertEquals(2, bean1.startOrder);
      assertEquals(3, bean4.createOrder);
      assertEquals(4, bean4.startOrder);
      assertEquals(5, bean5.createOrder);
      assertEquals(6, bean2.createOrder);
      assertEquals(7, bean2.startOrder);
      assertEquals(8, bean3.createOrder);
      assertEquals(9, bean3.startOrder);
      assertEquals(10, bean5.startOrder);
      assertTrue(bean1 == bean5.createBean);
      assertTrue(bean2 == bean5.startBean);
      assertNull(bean5.stopBean);
      assertNull(bean5.destroyBean);
      assertEquals(-1, bean1.stopOrder);
      assertEquals(-1, bean1.destroyOrder);
      assertEquals(-1, bean2.stopOrder);
      assertEquals(-1, bean2.destroyOrder);
      assertEquals(-1, bean3.stopOrder);
      assertEquals(-1, bean3.destroyOrder);
      assertEquals(-1, bean4.stopOrder);
      assertEquals(-1, bean4.destroyOrder);
      assertEquals(-1, bean5.stopOrder);
      assertEquals(-1, bean5.destroyOrder);
   }

   public void complicatedLifecycleDependencyWrongOrder() throws Throwable
   {
      buildMetaData();
   }

   public void testComplicatedLifecycleDependencyReinstall() throws Throwable
   {
      complicatedLifecycleDependencyReinstall();
      
      ControllerContext context1 = assertInstall(0, "Name1");
      ControllerContext context2 = assertInstall(1, "Name2");
      ControllerContext context3 = assertInstall(2, "Name3");
      ControllerContext context4 = assertInstall(3, "Name4");
      ControllerContext context5 = assertInstall(4, "Name5");
      
      SimpleBeanWithLifecycle bean1 = (SimpleBeanWithLifecycle) context1.getTarget();
      assertNotNull(bean1);
      
      SimpleBeanWithLifecycle bean2 = (SimpleBeanWithLifecycle) context2.getTarget();
      assertNotNull(bean2);
      
      SimpleBeanWithLifecycle bean3 = (SimpleBeanWithLifecycle) context3.getTarget();
      assertNotNull(bean3);
      
      SimpleBeanWithLifecycle bean4 = (SimpleBeanWithLifecycle) context4.getTarget();
      assertNotNull(bean4);
      
      SimpleBeanWithComplicatedLifecycle bean5 = (SimpleBeanWithComplicatedLifecycle) context5.getTarget();
      assertNotNull(bean5);
      
      assertEquals(1, bean1.createOrder);
      assertEquals(2, bean1.startOrder);
      assertEquals(3, bean2.createOrder);
      assertEquals(4, bean2.startOrder);
      assertEquals(5, bean3.createOrder);
      assertEquals(6, bean3.startOrder);
      assertEquals(7, bean4.createOrder);
      assertEquals(8, bean4.startOrder);
      assertEquals(9, bean5.createOrder);
      assertEquals(10, bean5.startOrder);
      assertTrue(bean1 == bean5.createBean);
      assertTrue(bean2 == bean5.startBean);
      assertNull(bean5.stopBean);
      assertNull(bean5.destroyBean);
      assertEquals(-1, bean1.stopOrder);
      assertEquals(-1, bean1.destroyOrder);
      assertEquals(-1, bean2.stopOrder);
      assertEquals(-1, bean2.destroyOrder);
      assertEquals(-1, bean3.stopOrder);
      assertEquals(-1, bean3.destroyOrder);
      assertEquals(-1, bean4.stopOrder);
      assertEquals(-1, bean4.destroyOrder);
      assertEquals(-1, bean5.stopOrder);
      assertEquals(-1, bean5.destroyOrder);
      
      assertUninstall("Name1");
      assertContext("Name2");
      assertContext("Name3");
      assertContext("Name4");
      assertContext("Name5", ControllerState.CONFIGURED);
      assertEquals(1, bean1.createOrder);
      assertEquals(2, bean1.startOrder);
      assertEquals(3, bean2.createOrder);
      assertEquals(4, bean2.startOrder);
      assertEquals(5, bean3.createOrder);
      assertEquals(6, bean3.startOrder);
      assertEquals(7, bean4.createOrder);
      assertEquals(8, bean4.startOrder);
      assertEquals(9, bean5.createOrder);
      assertEquals(10, bean5.startOrder);
      assertNull(bean5.createBean);
      assertNull(bean5.startBean);
      assertTrue(bean3 == bean5.stopBean);
      assertTrue(bean4 == bean5.destroyBean);
      assertEquals(13, bean1.stopOrder);
      assertEquals(14, bean1.destroyOrder);
      assertEquals(-1, bean2.stopOrder);
      assertEquals(-1, bean2.destroyOrder);
      assertEquals(-1, bean3.stopOrder);
      assertEquals(-1, bean3.destroyOrder);
      assertEquals(-1, bean4.stopOrder);
      assertEquals(-1, bean4.destroyOrder);
      assertEquals(11, bean5.stopOrder);
      assertEquals(12, bean5.destroyOrder);
      
      context1 = assertInstall(0, "Name1");
      
      bean1 = (SimpleBeanWithLifecycle) context1.getTarget();
      assertNotNull(bean1);
      assertContext("Name2");
      assertContext("Name3");
      assertContext("Name4");
      assertContext("Name5");
      assertEquals(15, bean1.createOrder);
      assertEquals(16, bean1.startOrder);
      assertEquals(3, bean2.createOrder);
      assertEquals(4, bean2.startOrder);
      assertEquals(5, bean3.createOrder);
      assertEquals(6, bean3.startOrder);
      assertEquals(7, bean4.createOrder);
      assertEquals(8, bean4.startOrder);
      assertEquals(17, bean5.createOrder);
      assertEquals(18, bean5.startOrder);
      assertTrue(bean1 == bean5.createBean);
      assertTrue(bean2 == bean5.startBean);
      assertNull(bean5.stopBean);
      assertNull(bean5.destroyBean);
      assertEquals(-1, bean1.stopOrder);
      assertEquals(-1, bean1.destroyOrder);
      assertEquals(-1, bean2.stopOrder);
      assertEquals(-1, bean2.destroyOrder);
      assertEquals(-1, bean3.stopOrder);
      assertEquals(-1, bean3.destroyOrder);
      assertEquals(-1, bean4.stopOrder);
      assertEquals(-1, bean4.destroyOrder);
      assertEquals(11, bean5.stopOrder);
      assertEquals(12, bean5.destroyOrder);
      
      assertUninstall("Name2");
      assertContext("Name1");
      assertContext("Name3");
      assertContext("Name4");
      assertContext("Name5", ControllerState.CREATE);
      assertEquals(15, bean1.createOrder);
      assertEquals(16, bean1.startOrder);
      assertEquals(3, bean2.createOrder);
      assertEquals(4, bean2.startOrder);
      assertEquals(5, bean3.createOrder);
      assertEquals(6, bean3.startOrder);
      assertEquals(7, bean4.createOrder);
      assertEquals(8, bean4.startOrder);
      assertEquals(17, bean5.createOrder);
      assertEquals(18, bean5.startOrder);
      assertTrue(bean1 == bean5.createBean);
      assertNull(bean5.startBean);
      assertTrue(bean3 == bean5.stopBean);
      assertNull(bean5.destroyBean);
      assertEquals(-1, bean1.stopOrder);
      assertEquals(-1, bean1.destroyOrder);
      assertEquals(20, bean2.stopOrder);
      assertEquals(21, bean2.destroyOrder);
      assertEquals(-1, bean3.stopOrder);
      assertEquals(-1, bean3.destroyOrder);
      assertEquals(-1, bean4.stopOrder);
      assertEquals(-1, bean4.destroyOrder);
      assertEquals(19, bean5.stopOrder);
      assertEquals(12, bean5.destroyOrder);
      
      context2 = assertInstall(1, "Name2");
      
      bean2 = (SimpleBeanWithLifecycle) context2.getTarget();
      assertNotNull(bean2);
      assertContext("Name1");
      assertContext("Name3");
      assertContext("Name4");
      assertContext("Name5");
      assertEquals(15, bean1.createOrder);
      assertEquals(16, bean1.startOrder);
      assertEquals(22, bean2.createOrder);
      assertEquals(23, bean2.startOrder);
      assertEquals(5, bean3.createOrder);
      assertEquals(6, bean3.startOrder);
      assertEquals(7, bean4.createOrder);
      assertEquals(8, bean4.startOrder);
      assertEquals(17, bean5.createOrder);
      assertEquals(24, bean5.startOrder);
      assertTrue(bean1 == bean5.createBean);
      assertTrue(bean2 == bean5.startBean);
      assertNull(bean5.stopBean);
      assertNull(bean5.destroyBean);
      assertEquals(-1, bean1.stopOrder);
      assertEquals(-1, bean1.destroyOrder);
      assertEquals(-1, bean2.stopOrder);
      assertEquals(-1, bean2.destroyOrder);
      assertEquals(-1, bean3.stopOrder);
      assertEquals(-1, bean3.destroyOrder);
      assertEquals(-1, bean4.stopOrder);
      assertEquals(-1, bean4.destroyOrder);
      assertEquals(19, bean5.stopOrder);
      assertEquals(12, bean5.destroyOrder);
      
      assertUninstall("Name3");
      assertContext("Name1");
      assertContext("Name2");
      assertContext("Name4");
      assertContext("Name5", ControllerState.CREATE);
      assertEquals(15, bean1.createOrder);
      assertEquals(16, bean1.startOrder);
      assertEquals(22, bean2.createOrder);
      assertEquals(23, bean2.startOrder);
      assertEquals(5, bean3.createOrder);
      assertEquals(6, bean3.startOrder);
      assertEquals(7, bean4.createOrder);
      assertEquals(8, bean4.startOrder);
      assertEquals(17, bean5.createOrder);
      assertEquals(24, bean5.startOrder);
      assertTrue(bean1 == bean5.createBean);
      assertNull(bean5.startBean);
      assertTrue(bean3 == bean5.stopBean);
      assertNull(bean5.destroyBean);
      assertEquals(-1, bean1.stopOrder);
      assertEquals(-1, bean1.destroyOrder);
      assertEquals(-1, bean2.stopOrder);
      assertEquals(-1, bean2.destroyOrder);
      assertEquals(26, bean3.stopOrder);
      assertEquals(27, bean3.destroyOrder);
      assertEquals(-1, bean4.stopOrder);
      assertEquals(-1, bean4.destroyOrder);
      assertEquals(25, bean5.stopOrder);
      assertEquals(12, bean5.destroyOrder);
      
      context3 = assertInstall(2, "Name3");
      
      bean3 = (SimpleBeanWithLifecycle) context3.getTarget();
      assertNotNull(bean3);
      assertContext("Name1");
      assertContext("Name2");
      assertContext("Name4");
      assertContext("Name5");
      assertEquals(15, bean1.createOrder);
      assertEquals(16, bean1.startOrder);
      assertEquals(22, bean2.createOrder);
      assertEquals(23, bean2.startOrder);
      assertEquals(28, bean3.createOrder);
      assertEquals(29, bean3.startOrder);
      assertEquals(7, bean4.createOrder);
      assertEquals(8, bean4.startOrder);
      assertEquals(17, bean5.createOrder);
      assertEquals(30, bean5.startOrder);
      assertTrue(bean1 == bean5.createBean);
      assertTrue(bean2 == bean5.startBean);
      assertNull(bean5.stopBean);
      assertNull(bean5.destroyBean);
      assertEquals(-1, bean1.stopOrder);
      assertEquals(-1, bean1.destroyOrder);
      assertEquals(-1, bean2.stopOrder);
      assertEquals(-1, bean2.destroyOrder);
      assertEquals(-1, bean3.stopOrder);
      assertEquals(-1, bean3.destroyOrder);
      assertEquals(-1, bean4.stopOrder);
      assertEquals(-1, bean4.destroyOrder);
      assertEquals(25, bean5.stopOrder);
      assertEquals(12, bean5.destroyOrder);
      
      assertUninstall("Name4");
      assertContext("Name1");
      assertContext("Name2");
      assertContext("Name3");
      assertContext("Name5", ControllerState.CONFIGURED);
      assertEquals(15, bean1.createOrder);
      assertEquals(16, bean1.startOrder);
      assertEquals(22, bean2.createOrder);
      assertEquals(23, bean2.startOrder);
      assertEquals(28, bean3.createOrder);
      assertEquals(29, bean3.startOrder);
      assertEquals(7, bean4.createOrder);
      assertEquals(8, bean4.startOrder);
      assertEquals(17, bean5.createOrder);
      assertEquals(30, bean5.startOrder);
      assertNull(bean5.createBean);
      assertNull(bean5.startBean);
      assertTrue(bean3 == bean5.stopBean);
      assertTrue(bean4 == bean5.destroyBean);
      assertEquals(-1, bean1.stopOrder);
      assertEquals(-1, bean1.destroyOrder);
      assertEquals(-1, bean2.stopOrder);
      assertEquals(-1, bean2.destroyOrder);
      assertEquals(-1, bean3.stopOrder);
      assertEquals(-1, bean3.destroyOrder);
      assertEquals(33, bean4.stopOrder);
      assertEquals(34, bean4.destroyOrder);
      assertEquals(31, bean5.stopOrder);
      assertEquals(32, bean5.destroyOrder);
      
      context4 = assertInstall(3, "Name4");
      
      bean4 = (SimpleBeanWithLifecycle) context4.getTarget();
      assertNotNull(bean4);
      assertContext("Name1");
      assertContext("Name2");
      assertContext("Name3");
      assertContext("Name5");
      assertEquals(15, bean1.createOrder);
      assertEquals(16, bean1.startOrder);
      assertEquals(22, bean2.createOrder);
      assertEquals(23, bean2.startOrder);
      assertEquals(28, bean3.createOrder);
      assertEquals(29, bean3.startOrder);
      assertEquals(35, bean4.createOrder);
      assertEquals(36, bean4.startOrder);
      assertEquals(37, bean5.createOrder);
      assertEquals(38, bean5.startOrder);
      assertTrue(bean1 == bean5.createBean);
      assertTrue(bean2 == bean5.startBean);
      assertNull(bean5.stopBean);
      assertNull(bean5.destroyBean);
      assertEquals(-1, bean1.stopOrder);
      assertEquals(-1, bean1.destroyOrder);
      assertEquals(-1, bean2.stopOrder);
      assertEquals(-1, bean2.destroyOrder);
      assertEquals(-1, bean3.stopOrder);
      assertEquals(-1, bean3.destroyOrder);
      assertEquals(-1, bean4.stopOrder);
      assertEquals(-1, bean4.destroyOrder);
      assertEquals(31, bean5.stopOrder);
      assertEquals(32, bean5.destroyOrder);
      
      assertUninstall("Name5");
      assertContext("Name1");
      assertContext("Name2");
      assertContext("Name3");
      assertContext("Name4");
      assertEquals(15, bean1.createOrder);
      assertEquals(16, bean1.startOrder);
      assertEquals(22, bean2.createOrder);
      assertEquals(23, bean2.startOrder);
      assertEquals(28, bean3.createOrder);
      assertEquals(29, bean3.startOrder);
      assertEquals(35, bean4.createOrder);
      assertEquals(36, bean4.startOrder);
      assertEquals(37, bean5.createOrder);
      assertEquals(38, bean5.startOrder);
      assertNull(bean5.createBean);
      assertNull(bean5.startBean);
      assertTrue(bean3 == bean5.stopBean);
      assertTrue(bean4 == bean5.destroyBean);
      assertEquals(-1, bean1.stopOrder);
      assertEquals(-1, bean1.destroyOrder);
      assertEquals(-1, bean2.stopOrder);
      assertEquals(-1, bean2.destroyOrder);
      assertEquals(-1, bean3.stopOrder);
      assertEquals(-1, bean3.destroyOrder);
      assertEquals(-1, bean4.stopOrder);
      assertEquals(-1, bean4.destroyOrder);
      assertEquals(39, bean5.stopOrder);
      assertEquals(40, bean5.destroyOrder);
      
      context5 = assertInstall(4, "Name5");
      
      bean5 = (SimpleBeanWithComplicatedLifecycle) context5.getTarget();
      assertNotNull(bean5);
      assertContext("Name1");
      assertContext("Name2");
      assertContext("Name3");
      assertContext("Name4");
      assertEquals(15, bean1.createOrder);
      assertEquals(16, bean1.startOrder);
      assertEquals(22, bean2.createOrder);
      assertEquals(23, bean2.startOrder);
      assertEquals(28, bean3.createOrder);
      assertEquals(29, bean3.startOrder);
      assertEquals(35, bean4.createOrder);
      assertEquals(36, bean4.startOrder);
      assertEquals(41, bean5.createOrder);
      assertEquals(42, bean5.startOrder);
      assertTrue(bean1 == bean5.createBean);
      assertTrue(bean2 == bean5.startBean);
      assertNull(bean5.stopBean);
      assertNull(bean5.destroyBean);
      assertEquals(-1, bean1.stopOrder);
      assertEquals(-1, bean1.destroyOrder);
      assertEquals(-1, bean2.stopOrder);
      assertEquals(-1, bean2.destroyOrder);
      assertEquals(-1, bean3.stopOrder);
      assertEquals(-1, bean3.destroyOrder);
      assertEquals(-1, bean4.stopOrder);
      assertEquals(-1, bean4.destroyOrder);
      assertEquals(-1, bean5.stopOrder);
      assertEquals(-1, bean5.destroyOrder);
   }

   public void complicatedLifecycleDependencyReinstall() throws Throwable
   {
      buildMetaData();
   }

   protected void buildMetaData()
   {
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("Name1", SimpleBeanWithLifecycle.class.getName());

      AbstractBeanMetaData metaData2 = new AbstractBeanMetaData("Name2", SimpleBeanWithLifecycle.class.getName());

      AbstractBeanMetaData metaData3 = new AbstractBeanMetaData("Name3", SimpleBeanWithLifecycle.class.getName());

      AbstractBeanMetaData metaData4 = new AbstractBeanMetaData("Name4", SimpleBeanWithLifecycle.class.getName());

      AbstractBeanMetaData metaData5 = new AbstractBeanMetaData("Name5", SimpleBeanWithComplicatedLifecycle.class.getName());

      AbstractLifecycleMetaData create = new AbstractLifecycleMetaData();
      create.setMethodName("notCreate");
      AbstractParameterMetaData parameter = new AbstractParameterMetaData(SimpleBeanWithLifecycle.class.getName(), new AbstractDependencyValueMetaData("Name1"));
      create.setParameters(Collections.singletonList((ParameterMetaData) parameter));
      metaData5.setCreate(create);

      AbstractLifecycleMetaData start = new AbstractLifecycleMetaData();
      start.setMethodName("notStart");
      parameter = new AbstractParameterMetaData();
      parameter.setValue(new AbstractDependencyValueMetaData("Name2"));
      start.setParameters(Collections.singletonList((ParameterMetaData) parameter));
      metaData5.setStart(start);

      AbstractLifecycleMetaData stop = new AbstractLifecycleMetaData();
      stop.setMethodName("notStop");
      parameter = new AbstractParameterMetaData();
      parameter.setValue(new AbstractDependencyValueMetaData("Name3"));
      stop.setParameters(Collections.singletonList((ParameterMetaData) parameter));
      metaData5.setStop(stop);

      AbstractLifecycleMetaData destroy = new AbstractLifecycleMetaData();
      destroy.setMethodName("notDestroy");
      parameter = new AbstractParameterMetaData();
      parameter.setValue(new AbstractDependencyValueMetaData("Name4"));
      destroy.setParameters(Collections.singletonList((ParameterMetaData) parameter));
      metaData5.setDestroy(destroy);

      setBeanMetaDatas(new BeanMetaData[] { metaData1, metaData2, metaData3, metaData4, metaData5 });
   }

   protected void setUp() throws Exception
   {
      super.setUp();
      SimpleBeanWithLifecycle.resetOrder();
   }
}