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
package org.jboss.test.kernel.annotations.test.factory;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractDemandMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractSupplyMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.CallbackMetaData;
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.beans.metadata.spi.DemandMetaData;
import org.jboss.beans.metadata.spi.DependencyMetaData;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.beans.metadata.spi.LifecycleMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.SupplyMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.kernel.spi.annotations.AnnotationToBeanMetaDataFactory;
import org.jboss.metadata.plugins.context.AbstractMetaDataContext;
import org.jboss.metadata.plugins.loader.memory.MemoryMetaDataLoader;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;
import org.jboss.metadata.spi.retrieval.MetaDataRetrievalToMetaDataBridge;
import org.jboss.metadata.spi.scope.CommonLevels;
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.test.BaseTestCase;
import org.jboss.test.kernel.annotations.support.AliasesImpl;
import org.jboss.test.kernel.annotations.support.AllIoCAnnotations;
import org.jboss.test.kernel.annotations.support.AnnotationTester;
import org.jboss.test.kernel.annotations.support.SimpleBeanMetaDataAnnotationAdapter;
import org.jboss.test.kernel.annotations.support.ConstructorAnnotationTester;

/**
 * Simple field test.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AnnotationToBeanMetaDataFactoryTestCase extends BaseTestCase
{
   public AnnotationToBeanMetaDataFactoryTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(AnnotationToBeanMetaDataFactoryTestCase.class);
   }

   protected void testInstalls(List<InstallMetaData> installs)
   {
      assertNotNull(installs);
      assertEquals(2, installs.size());
      for (InstallMetaData imd : installs)
      {
         Object bean = imd.getBean();
         if (bean == null)
         {
            assertEquals("invoke", imd.getMethodName());
         }
         else
         {
            assertEquals("bean", bean);
            assertEquals("method", imd.getMethodName());
         }
      }
   }

   protected void testLifecycle(LifecycleMetaData lifecycle, String method)
   {
      assertNotNull(lifecycle);
      assertEquals(method, lifecycle.getMethodName());
   }

   protected void testBMD(BeanMetaData bmd) throws Throwable
   {
      assertNotNull(bmd);
      assertEquals(Collections.singleton((Object)"alias"), bmd.getAliases());
      assertEquals(Collections.singleton((SupplyMetaData)new AbstractSupplyMetaData("supply")), bmd.getSupplies());
      assertEquals(Collections.singleton((DemandMetaData)new AbstractDemandMetaData("demand")), bmd.getDemands());
      assertEquals(Collections.singleton((DependencyMetaData)new AbstractDependencyMetaData("depend")), bmd.getDepends());
      ConstructorMetaData cmd = bmd.getConstructor();
      assertNotNull(cmd);
      assertEquals(AnnotationTester.class.getName(), cmd.getFactoryClass());
      assertEquals("factoryMethod", cmd.getFactoryMethod());
      testInstalls(bmd.getInstalls());
      testInstalls(bmd.getUninstalls());

      List<CallbackMetaData> callbacks = bmd.getInstallCallbacks();
      assertNotNull(callbacks);
      assertEquals(1, callbacks.size());
      CallbackMetaData callback = callbacks.get(0);
      assertTrue(callback.getMethodName().contains("Something"));
      callbacks = bmd.getUninstallCallbacks();
      assertNotNull(callbacks);
      assertEquals(1, callbacks.size());
      callback = callbacks.get(0);
      assertTrue(callback.getMethodName().contains("Something"));

      BeanAccessMode mode = bmd.getAccessMode();
      if (mode == BeanAccessMode.ALL)
      {
         Set<PropertyMetaData> propertys = bmd.getProperties();
         assertNotNull(propertys);
         assertEquals(2, propertys.size());
      }
      else
      {
         Set<PropertyMetaData> propertys = bmd.getProperties();
         assertNotNull(propertys);
         assertEquals(1, propertys.size());
         PropertyMetaData pmd = propertys.iterator().next();
         assertEquals("number", pmd.getName());
         assertInstanceOf(pmd.getValue(), AbstractDependencyValueMetaData.class);
      }
      testLifecycle(bmd.getCreate(), "myCreate");
      testLifecycle(bmd.getStart(), "myStart");
      testLifecycle(bmd.getStop(), "myStop");
      testLifecycle(bmd.getDestroy(), "myDestroy");
   }

   public void testDefaultAnnotation2BMDFactory() throws Throwable
   {
      BeanMetaData bmd1 = AnnotationToBeanMetaDataFactory.createBeanMetaData(AllIoCAnnotations.class);
      testBMD(bmd1);

      bmd1 = AnnotationToBeanMetaDataFactory.createBeanMetaData(AllIoCAnnotations.class, BeanAccessMode.ALL);
      testBMD(bmd1);

      bmd1 = AnnotationToBeanMetaDataFactory.createBeanMetaData(ConstructorAnnotationTester.class);
      ConstructorMetaData cmd = bmd1.getConstructor();
      assertNotNull(cmd);
      List<ParameterMetaData> parameters = cmd.getParameters();
      assertNotNull(parameters);
      assertEquals(1, parameters.size());
   }

   public void testProvidedMetaData() throws Throwable
   {
      MemoryMetaDataLoader memory = new MemoryMetaDataLoader(new ScopeKey(CommonLevels.INSTANCE, "foobar"));
      memory.addAnnotation(new AliasesImpl("alias"));
      MetaDataRetrieval retrieval = new AbstractMetaDataContext(null, memory);
      MetaData metaData = new MetaDataRetrievalToMetaDataBridge(retrieval);

      BeanMetaData bmd = AnnotationToBeanMetaDataFactory.createBeanMetaData(Object.class, metaData);
      assertNotNull(bmd);
      assertEquals(Collections.singleton((Object)"alias"), bmd.getAliases());
   }

   public void testCustomAdapter() throws Throwable
   {
      BeanMetaData bmd = AnnotationToBeanMetaDataFactory.createBeanMetaData(Object.class, new SimpleBeanMetaDataAnnotationAdapter());
      assertEquals(ControllerMode.ASYNCHRONOUS, bmd.getMode());
   }

   public void testIllegalAccessMode() throws Throwable
   {
      AbstractBeanMetaData abmd = new AbstractBeanMetaData();
      abmd.setAccessMode(BeanAccessMode.FIELDS);
      try
      {
         AnnotationToBeanMetaDataFactory.fillBeanMetaData(Object.class, BeanAccessMode.ALL, abmd);
         fail("Should not be here.");
      }
      catch (Throwable t)
      {
         assertInstanceOf(t, IllegalArgumentException.class);
      }
   }
}