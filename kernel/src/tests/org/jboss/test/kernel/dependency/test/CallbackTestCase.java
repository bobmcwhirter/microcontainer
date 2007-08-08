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

import java.util.List;
import java.util.ArrayList;

import junit.framework.Test;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.kernel.dependency.support.SimpleBeanRepository;
import org.jboss.test.kernel.dependency.support.SimpleBean;
import org.jboss.test.kernel.dependency.support.SimpleBeanImpl;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.InstallCallbackMetaData;
import org.jboss.beans.metadata.plugins.UninstallCallbackMetaData;
import org.jboss.beans.metadata.spi.CallbackMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;

/**
 * Callback tests.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class CallbackTestCase extends OldAbstractKernelDependencyTest
{
   public CallbackTestCase(String name)
         throws Throwable
   {
      super(name);
   }

   public CallbackTestCase(String name, boolean xmltest)
         throws Throwable
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(CallbackTestCase.class);
   }

   public void testCallbackCorrectOrder() throws Throwable
   {
      callbackCorrectOrder();

      ControllerContext context1 = assertInstall(0, "Name1");
      SimpleBeanRepository repository = (SimpleBeanRepository)context1.getTarget();
      assertNotNull(repository);
      List beans = repository.getBeans();
      assertEmpty(beans);

      ControllerContext context2 = assertInstall(1, "Name2");
      SimpleBean bean = (SimpleBean)context2.getTarget();
      assertNotNull(bean);

      assertFalse(beans.isEmpty());
      assertEquals(1, beans.size());
      assertTrue(bean == beans.get(0));
   }

   protected void callbackCorrectOrder() throws Throwable
   {
      buildMetaData(buildRepository());
   }

   public void testCallbackWrongOrder() throws Throwable
   {
      callbackWrongOrder();

      ControllerContext context2 = assertInstall(1, "Name2");
      SimpleBean bean = (SimpleBean)context2.getTarget();
      assertNotNull(bean);

      ControllerContext context1 = assertInstall(0, "Name1");
      SimpleBeanRepository repository = (SimpleBeanRepository)context1.getTarget();
      assertNotNull(repository);
      List beans = repository.getBeans();
      assertFalse(beans.isEmpty());
      assertEquals(1, beans.size());
      assertTrue(bean == beans.get(0));
   }

   protected void callbackWrongOrder() throws Throwable
   {
      buildMetaData(buildRepository());
   }

   public void testCallbackReinstall() throws Throwable
   {
      callbackReinstall();

      ControllerContext context1 = assertInstall(0, "Name1");
      SimpleBeanRepository repository = (SimpleBeanRepository)context1.getTarget();
      assertNotNull(repository);
      List beans = repository.getBeans();
      assertEmpty(beans);

      ControllerContext context2 = assertInstall(1, "Name2");
      SimpleBean bean = (SimpleBean)context2.getTarget();
      assertNotNull(bean);

      assertFalse(beans.isEmpty());
      assertEquals(1, beans.size());
      assertTrue(bean == beans.get(0));

      assertUninstall("Name1");
      assertEquals(ControllerState.ERROR, context1.getState());
      assertEmpty(beans);

      context1 = assertInstall(0, "Name1");
      repository = (SimpleBeanRepository)context1.getTarget();
      assertNotNull(repository);
      beans = repository.getBeans();
      assertFalse(beans.isEmpty());
      assertEquals(1, beans.size());
      assertTrue(bean == beans.get(0));

      assertUninstall("Name2");
      assertEquals(ControllerState.ERROR, context2.getState());
      assertEmpty(beans);
   }

   protected void callbackReinstall() throws Throwable
   {
      buildMetaData(buildRepository());
   }

   public void testCardinalityCallbackCorrectOrder() throws Throwable
   {
      callbackCardinalityCorrectOrder();

      ControllerContext context1 = assertInstall(0, "Name1", ControllerState.INSTANTIATED);
      SimpleBeanRepository repository = (SimpleBeanRepository)context1.getTarget();
      assertNotNull(repository);
      List beans = repository.getBeans();
      assertEmpty(beans);

      ControllerContext context2 = assertInstall(1, "Name2");
      SimpleBean bean1 = (SimpleBean)context2.getTarget();
      assertNotNull(bean1);
      assertEmpty(beans);

      ControllerContext context3 = assertInstall(2, "Name3");
      SimpleBean bean2 = (SimpleBean)context3.getTarget();
      assertNotNull(bean2);

      assertEquals(ControllerState.INSTALLED, context1.getState());
      assertFalse(beans.isEmpty());
      assertEquals(2, beans.size());
      boolean first = bean1 == beans.get(0);
      if (first)
         assertTrue(bean2 == beans.get(1));
      else
         assertTrue(bean2 == beans.get(0));
   }

   protected void callbackCardinalityCorrectOrder() throws Throwable
   {
      AbstractBeanMetaData repository = buildRepository(Cardinality.createUnlimitedCardinality(2));      
      buildMetaData(repository);
   }

   public void testCardinalityCallbackWrongOrder() throws Throwable
   {
      callbackCardinalityWrongOrder();

      ControllerContext context2 = assertInstall(1, "Name2");
      SimpleBean bean1 = (SimpleBean)context2.getTarget();
      assertNotNull(bean1);

      ControllerContext context3 = assertInstall(2, "Name3");
      SimpleBean bean2 = (SimpleBean)context3.getTarget();

      ControllerContext context1 = assertInstall(0, "Name1");
      SimpleBeanRepository repository = (SimpleBeanRepository)context1.getTarget();
      assertNotNull(repository);
      List beans = repository.getBeans();

      assertFalse(beans.isEmpty());
      assertEquals(2, beans.size());
      boolean first = bean1 == beans.get(0);
      if (first)
         assertTrue(bean2 == beans.get(1));
      else
         assertTrue(bean2 == beans.get(0));
   }

   protected void callbackCardinalityWrongOrder() throws Throwable
   {
      AbstractBeanMetaData repository = buildRepository(Cardinality.createUnlimitedCardinality(2));
      buildMetaData(repository);
   }

   public void testCardinalityCallbackReinstall() throws Throwable
   {
      callbackCardinalityReinstall();

      ControllerContext context1 = assertInstall(0, "Name1", ControllerState.INSTANTIATED);
      SimpleBeanRepository repository = (SimpleBeanRepository)context1.getTarget();
      assertNotNull(repository);
      List beans = repository.getBeans();
      assertEmpty(beans);

      ControllerContext context2 = assertInstall(1, "Name2");
      SimpleBean bean1 = (SimpleBean)context2.getTarget();
      assertNotNull(bean1);
      assertEmpty(beans);

      ControllerContext context3 = assertInstall(2, "Name3");
      SimpleBean bean2 = (SimpleBean)context3.getTarget();
      assertNotNull(bean2);
      assertEquals(ControllerState.INSTALLED, context1.getState());
      assertFalse(beans.isEmpty());
      assertEquals(2, beans.size());

      assertUninstall("Name1");
      assertEquals(ControllerState.ERROR, context1.getState());
      assertEmpty(beans);

      context1 = assertInstall(0, "Name1");
      repository = (SimpleBeanRepository)context1.getTarget();
      assertNotNull(repository);
      beans = repository.getBeans();
      assertFalse(beans.isEmpty());
      assertEquals(2, beans.size());

      assertUninstall("Name2");
      assertEquals(ControllerState.ERROR, context2.getState());
      assertEquals(ControllerState.INSTANTIATED, context1.getState());
      assertEmpty(beans);

      assertInstall(1, "Name2");
      assertEquals(ControllerState.INSTALLED, context1.getState());
      assertEquals(2, beans.size());

      assertUninstall("Name3");
      assertEquals(ControllerState.ERROR, context3.getState());
      assertEquals(ControllerState.INSTANTIATED, context1.getState());
      assertEmpty(beans);
   }

   protected void callbackCardinalityReinstall() throws Throwable
   {
      AbstractBeanMetaData repository = buildRepository(Cardinality.createUnlimitedCardinality(2));
      buildMetaData(repository);
   }

   protected void buildMetaData(BeanMetaData repository)
   {
      setBeanMetaDatas(new BeanMetaData[]{
            repository,
            new AbstractBeanMetaData("Name2", SimpleBeanImpl.class.getName()),
            new AbstractBeanMetaData("Name3", SimpleBeanImpl.class.getName())
      });
   }

   protected AbstractBeanMetaData buildRepository()
   {
      return buildRepository(null);
   }

   protected AbstractBeanMetaData buildRepository(Cardinality cardinality)
   {
      AbstractBeanMetaData repository = new AbstractBeanMetaData("Name1", SimpleBeanRepository.class.getName());
      List<CallbackMetaData> installs = new ArrayList<CallbackMetaData>();
      repository.setInstallCallbacks(installs);
      InstallCallbackMetaData install = new InstallCallbackMetaData();
      install.setMethodName("addSimpleBean");
      if (cardinality != null)
         install.setCardinality(cardinality);
      installs.add(install);
      List<CallbackMetaData> unstalls = new ArrayList<CallbackMetaData>();
      repository.setUninstallCallbacks(unstalls);
      UninstallCallbackMetaData uninstall = new UninstallCallbackMetaData();
      uninstall.setMethodName("removeSimpleBean");
      unstalls.add(uninstall);
      return repository;
   }
}
