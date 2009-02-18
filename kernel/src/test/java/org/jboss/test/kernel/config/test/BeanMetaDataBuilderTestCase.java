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
package org.jboss.test.kernel.config.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import org.jboss.beans.metadata.api.model.AutowireType;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractRelatedClassMetaData;
import org.jboss.beans.metadata.plugins.InstallCallbackMetaData;
import org.jboss.beans.metadata.plugins.UninstallCallbackMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderFactory;
import org.jboss.beans.metadata.spi.AnnotationMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.CallbackMetaData;
import org.jboss.beans.metadata.spi.LifecycleMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.RelatedClassMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.dependency.plugins.graph.Search;
import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.ErrorHandlingMode;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.deployment.AbstractKernelDeployer;
import org.jboss.kernel.plugins.deployment.AbstractKernelDeployment;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.metadata.spi.MetaData;
import org.jboss.test.kernel.config.support.SimpleAnnotation;
import org.jboss.test.kernel.config.support.SimpleAnnotationImpl;
import org.jboss.test.kernel.config.support.SimpleBean;
import org.jboss.test.kernel.config.support.SimpleCallbackBean;
import org.jboss.test.kernel.config.support.SimpleLifecycleBean;
import org.jboss.test.kernel.config.support.Transformer;
import org.jboss.test.kernel.config.support.TrimTransformer;

/**
 * Builder TestCase.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class BeanMetaDataBuilderTestCase extends AbstractKernelConfigTest
{
   public static Test suite()
   {
      return suite(BeanMetaDataBuilderTestCase.class);
   }

   public BeanMetaDataBuilderTestCase(String name) throws Throwable
   {
      super(name);
   }

   public void testConstructor() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("FromBMD", SimpleBean.class.getName());
      builder.addConstructorParameter(String.class.getName(), "TestConstructor");
      BeanMetaData beanMetaData = builder.getBeanMetaData();
      SimpleBean fbmd = (SimpleBean)instantiateAndConfigure(beanMetaData);

      assertNotNull(fbmd);
      assertNotNull(fbmd.getConstructorUsed());
      assertEquals("TestConstructor", fbmd.getConstructorUsed());
   }

   public void testProperty() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("PropBMD", SimpleBean.class.getName())
            .addPropertyMetaData("adouble", 3.1459)
            .addPropertyMetaData("anint", "123")
            .addPropertyMetaData("collection", new ArrayList<Object>());
      BeanMetaData beanMetaData = builder.getBeanMetaData();
      SimpleBean pbmd = (SimpleBean)instantiateAndConfigure(beanMetaData);

      assertNotNull(pbmd);
      assertEquals(3.1459, pbmd.getAdouble());
      assertEquals(123, pbmd.getAnint());
      assertNotNull(pbmd.getCollection());
      assertTrue(pbmd.getCollection().isEmpty());
   }

   public void testLifecycle() throws Throwable
   {
      Kernel kernel = bootstrap();

      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SLB", SimpleLifecycleBean.class.getName())
            .addCreateParameter(String.class.getName(), "ParamCreate")
            .setStart("doStart")
            .addStartParameter(String.class.getName(), "ParamStart")
            .setStop("doStop")
            .addStopParameter(String.class.getName(), "ParamStop")
            .addDestroyParameter(String.class.getName(), "ParamDestroy");
      BeanMetaData beanMetaData = builder.getBeanMetaData();

      KernelController controller = kernel.getController();

      KernelControllerContext context = controller.install(beanMetaData);
      SimpleLifecycleBean slb = (SimpleLifecycleBean)context.getTarget();

      assertNotNull(slb);
      assertEquals("ParamCreate", slb.getCreate());
      assertEquals("ParamStart", slb.getStart());

      controller.uninstall("SLB");

      assertEquals("ParamStop", slb.getStop());
      assertEquals("ParamDestroy", slb.getDestroy());
   }

   public void testInstall() throws Throwable
   {
      Kernel kernel = bootstrap();

      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SLB", SimpleLifecycleBean.class.getName())
            .addInstall("installParam", String.class.getName(), "Install")
            .addUninstall("uninstallParam", String.class.getName(), "Uninstall");
      BeanMetaData beanMetaData = builder.getBeanMetaData();

      KernelController controller = kernel.getController();

      KernelControllerContext context = controller.install(beanMetaData);
      SimpleLifecycleBean slb = (SimpleLifecycleBean)context.getTarget();

      assertNotNull(slb);
      assertEquals("Install", slb.getInstall());

      controller.uninstall("SLB");

      assertEquals("Uninstall", slb.getInstall());
   }

   @SuppressWarnings("deprecation")
   public void testDemandSupply() throws Throwable
   {
      BeanMetaDataBuilder demand = BeanMetaDataBuilderFactory.createBuilder("DemandBean", SimpleBean.class.getName());
      demand.addDemand("Barrier");
      BeanMetaDataFactory demandBean = demand.getBeanMetaDataFactory();

      BeanMetaDataBuilder supply = BeanMetaDataBuilderFactory.createBuilder("SupplyBean", SimpleLifecycleBean.class.getName());
      supply.addSupply("Barrier");
      BeanMetaDataFactory supplyBean = supply.getBeanMetaDataFactory();

      AbstractKernelDeployment deployment = new AbstractKernelDeployment();
      deployment.setBeanFactories(Arrays.asList(demandBean, supplyBean));

      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();
      AbstractKernelDeployer deployer = new AbstractKernelDeployer(kernel);

      deployer.deploy(deployment);
      try
      {
         Object db = controller.getInstalledContext("DemandBean").getTarget();
         assertNotNull(db);
         Object sb = controller.getInstalledContext("SupplyBean").getTarget();
         assertNotNull(sb);
      }
      finally
      {
         deployer.undeploy(deployment);
      }
   }

   @SuppressWarnings("deprecation")
   public void testDemandWithTargetState() throws Throwable
   {
      BeanMetaDataBuilder demand = BeanMetaDataBuilderFactory.createBuilder("DemandBean", SimpleBean.class.getName());
      demand.addDemand("SupplyBean", ControllerState.CREATE, ControllerState.START, null);
      BeanMetaDataFactory demandBean = demand.getBeanMetaDataFactory();

      BeanMetaDataBuilder supply = BeanMetaDataBuilderFactory.createBuilder("SupplyBean", SimpleLifecycleBean.class.getName());
      BeanMetaDataFactory supplyBean = supply.getBeanMetaDataFactory();

      AbstractKernelDeployment deployment = new AbstractKernelDeployment();
      deployment.setBeanFactories(Arrays.asList(demandBean, supplyBean));

      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();
      AbstractKernelDeployer deployer = new AbstractKernelDeployer(kernel);

      deployer.deploy(deployment);
      try
      {
         Object db = controller.getInstalledContext("DemandBean").getTarget();
         assertNotNull(db);
         Object sb = controller.getInstalledContext("SupplyBean").getTarget();
         assertNotNull(sb);
      }
      finally
      {
         deployer.undeploy(deployment);
      }
   }

   @SuppressWarnings("deprecation")
   public void testDependency() throws Throwable
   {
      BeanMetaDataBuilder dependOn = BeanMetaDataBuilderFactory.createBuilder("DependOnBean", SimpleBean.class.getName());
      dependOn.addDependency("DependencyResolver");
      BeanMetaDataFactory dependOnBean = dependOn.getBeanMetaDataFactory();

      BeanMetaDataBuilder resolver = BeanMetaDataBuilderFactory.createBuilder("DependencyResolver", SimpleLifecycleBean.class.getName());
      BeanMetaDataFactory resolverBean = resolver.getBeanMetaDataFactory();

      AbstractKernelDeployment deployment = new AbstractKernelDeployment();
      deployment.setBeanFactories(Arrays.asList(dependOnBean, resolverBean));

      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();
      AbstractKernelDeployer deployer = new AbstractKernelDeployer(kernel);

      deployer.deploy(deployment);
      try
      {
         Object db = controller.getInstalledContext("DependOnBean").getTarget();
         assertNotNull(db);
         Object rb = controller.getInstalledContext("DependencyResolver").getTarget();
         assertNotNull(rb);
      }
      finally
      {
         deployer.undeploy(deployment);
      }
   }

   @SuppressWarnings("deprecation")
   public void testCollectionProperties() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("CollectionBean", SimpleBean.class.getName());

      List<ValueMetaData> array = builder.createArray();
      array.add(builder.createValue(new Integer(5)));
      array.add(builder.createValue(new Integer(10)));
      builder.addPropertyMetaData("array", array);

      List<ValueMetaData> list = builder.createList();
      list.add(builder.createValue("One"));
      list.add(builder.createValue("Two"));
      builder.addPropertyMetaData("list", list);

      Set<ValueMetaData> set = builder.createSet();
      set.add(builder.createValue("En"));
      set.add(builder.createValue("To"));
      builder.addPropertyMetaData("set", set);

      Collection<ValueMetaData> collection = builder.createCollection();
      collection.add(builder.createValue("Eins"));
      collection.add(builder.createValue("Zwei"));
      builder.addPropertyMetaData("collection", collection);

      Map<ValueMetaData, ValueMetaData> map = builder.createMap();
      map.put(builder.createValue("One"), builder.createValue("Uno"));
      map.put(builder.createValue("Two"), builder.createValue("Dos"));
      builder.addPropertyMetaData("map", map);

      AbstractKernelDeployment deployment = new AbstractKernelDeployment();
      deployment.setBeanFactories(Arrays.asList(builder.getBeanMetaDataFactory()));

      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();
      AbstractKernelDeployer deployer = new AbstractKernelDeployer(kernel);

      deployer.deploy(deployment);
      try
      {
         Object o = controller.getInstalledContext("CollectionBean").getTarget();
         assertNotNull(o);
         assertInstanceOf(o, SimpleBean.class);
         SimpleBean bean = (SimpleBean)o;

         Object[] arr = bean.getArray();
         assertEquals(2, arr.length);
         assertEquals(5, arr[0]);
         assertEquals(10, arr[1]);

         List<?> lst = bean.getList();
         assertEquals(2, lst.size());
         assertEquals("One", lst.get(0));
         assertEquals("Two", lst.get(1));

         Set<?> st = bean.getSet();
         assertEquals(2, lst.size());
         assertTrue(st.contains("En"));
         assertTrue(st.contains("To"));

         Collection<?> coll = bean.getCollection();
         assertEquals(2, lst.size());
         assertTrue(coll.contains("Eins"));
         assertTrue(coll.contains("Zwei"));

         Map<?, ?> mp = bean.getMap();
         assertEquals(2, mp.size());
         assertEquals("Uno", mp.get("One"));
         assertEquals("Dos", mp.get("Two"));
      }
      finally
      {
         deployer.undeploy(deployment);
      }
   }

   @SuppressWarnings("deprecation")
   public void testReplacePropertyMetaData() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("ReplaceBean", SimpleBean.class.getName());

      builder.addPropertyMetaData("anInt", new Integer(1));
      builder.addPropertyMetaData("anInt", new Integer(5));

      builder.addPropertyMetaData("AString", "One");
      builder.addPropertyMetaData("AString", "Two");

      ValueMetaData value = builder.createValue("Three");
      builder.addPropertyMetaData("anObject", value);
      value = builder.createValue("Four");
      builder.addPropertyMetaData("anObject", value);

      List<ValueMetaData> array = builder.createArray();
      builder.addPropertyMetaData("array", array);

      array = builder.createArray();
      array.add(builder.createValue(new Integer(5)));
      array.add(builder.createValue(new Integer(10)));
      builder.addPropertyMetaData("array", array);

      Map<ValueMetaData, ValueMetaData> map = builder.createMap();
      builder.addPropertyMetaData("map", map);

      map = builder.createMap();
      map.put(builder.createValue("One"), builder.createValue("Uno"));
      map.put(builder.createValue("Two"), builder.createValue("Dos"));
      builder.addPropertyMetaData("map", map);

      AbstractKernelDeployment deployment = new AbstractKernelDeployment();
      deployment.setBeanFactories(Arrays.asList(builder.getBeanMetaDataFactory()));

      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();
      AbstractKernelDeployer deployer = new AbstractKernelDeployer(kernel);

      deployer.deploy(deployment);
      try
      {
         Object o = controller.getInstalledContext("ReplaceBean").getTarget();
         assertNotNull(o);
         assertInstanceOf(o, SimpleBean.class);
         SimpleBean bean = (SimpleBean)o;

         Integer integer = bean.getAnInt();
         assertEquals(new Integer(5), integer);

         String string = bean.getAString();
         assertEquals("Two", string);

         Object obj = bean.getAnObject();
         assertEquals("Four", obj);

         Object[] arr = bean.getArray();
         assertEquals(2, arr.length);
         assertEquals(5, arr[0]);
         assertEquals(10, arr[1]);

         Map<?, ?> mp = bean.getMap();
         assertEquals(2, mp.size());
         assertEquals("Uno", mp.get("One"));
         assertEquals("Dos", mp.get("Two"));
      }
      finally
      {
         deployer.undeploy(deployment);
      }
   }

   public void testCallbacks() throws Throwable
   {
      BeanMetaDataBuilder builder;
      BeanMetaData beanMetaData;
      List<CallbackMetaData> callbacks;
      CallbackMetaData callback;
      KernelControllerContext cc;
      Object target;
      Transformer<?> transformer;
      SimpleCallbackBean bean;

      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();
      try
      {
         beanMetaData = BeanMetaDataBuilder.createBuilder("t", TrimTransformer.class.getName()).getBeanMetaData();
         cc = controller.install(beanMetaData);
         assertNotNull(cc);
         assertEquals(ControllerState.INSTALLED, cc.getState());
         target = cc.getTarget();
         assertNotNull(target);
         transformer = assertInstanceOf(target, Transformer.class);

         // ct1

         builder = BeanMetaDataBuilder.createBuilder("ct1", SimpleCallbackBean.class.getName());
         builder.addPropertyInstallCallback("transformers");
         builder.addPropertyUninstallCallback("transformers");
         beanMetaData = builder.getBeanMetaData();

         callbacks = beanMetaData.getInstallCallbacks();
         assertNotNull(callbacks);
         assertEquals(1, callbacks.size());
         callback = callbacks.get(0);
         assertNotNull(callback);
         assertInstanceOf(callback, InstallCallbackMetaData.class, false);
         assertEquals("transformers", callback.getProperty());
         assertEquals(ControllerState.INSTALLED, callback.getDependentState());
         assertNull(callback.getState());
         assertNull(callback.getSignature());
         assertNull(callback.getMethodName());
         assertNull(callback.getCardinality());
         assertNull(callback.getParameters());

         callbacks = beanMetaData.getUninstallCallbacks();
         assertNotNull(callbacks);
         assertEquals(1, callbacks.size());
         callback = callbacks.get(0);
         assertNotNull(callback);
         assertInstanceOf(callback, UninstallCallbackMetaData.class, false);
         assertEquals("transformers", callback.getProperty());
         assertEquals(ControllerState.INSTALLED, callback.getDependentState());
         assertNull(callback.getState());
         assertNull(callback.getSignature());
         assertNull(callback.getMethodName());
         assertNull(callback.getCardinality());
         assertNull(callback.getParameters());

         cc = controller.install(beanMetaData);
         assertNotNull(cc);
         assertEquals(ControllerState.INSTALLED, cc.getState());
         target = cc.getTarget();
         assertNotNull(target);
         bean = assertInstanceOf(target, SimpleCallbackBean.class);
         assertNotNull(bean.getTransformers());
         assertEquals(1, bean.getTransformers().size());
         assertSame(transformer, bean.getTransformers().iterator().next());

         // ct2

         builder = BeanMetaDataBuilder.createBuilder("ct2", SimpleCallbackBean.class.getName());
         builder.addPropertyInstallCallback("transformers", Cardinality.ONE_TO_MANY);
         builder.addPropertyUninstallCallback("transformers", Cardinality.ONE_TO_MANY);
         beanMetaData = builder.getBeanMetaData();

         callbacks = beanMetaData.getInstallCallbacks();
         assertNotNull(callbacks);
         assertEquals(1, callbacks.size());
         callback = callbacks.get(0);
         assertNotNull(callback);
         assertInstanceOf(callback, InstallCallbackMetaData.class, false);
         assertEquals("transformers", callback.getProperty());
         assertEquals(ControllerState.INSTALLED, callback.getDependentState());
         assertNull(callback.getState());
         assertNull(callback.getSignature());
         assertNull(callback.getMethodName());
         assertEquals(Cardinality.ONE_TO_MANY, callback.getCardinality());
         assertNull(callback.getParameters());

         callbacks = beanMetaData.getUninstallCallbacks();
         assertNotNull(callbacks);
         assertEquals(1, callbacks.size());
         callback = callbacks.get(0);
         assertNotNull(callback);
         assertInstanceOf(callback, UninstallCallbackMetaData.class, false);
         assertEquals("transformers", callback.getProperty());
         assertEquals(ControllerState.INSTALLED, callback.getDependentState());
         assertNull(callback.getState());
         assertNull(callback.getSignature());
         assertNull(callback.getMethodName());
         assertEquals(Cardinality.ONE_TO_MANY, callback.getCardinality());
         assertNull(callback.getParameters());

         cc = controller.install(beanMetaData);
         assertNotNull(cc);
         assertEquals(ControllerState.INSTALLED, cc.getState());
         target = cc.getTarget();
         assertNotNull(target);
         bean = assertInstanceOf(target, SimpleCallbackBean.class);
         assertNotNull(bean.getTransformers());
         assertEquals(1, bean.getTransformers().size());
         assertSame(transformer, bean.getTransformers().iterator().next());

         // ct3

         builder = BeanMetaDataBuilder.createBuilder("ct3", SimpleCallbackBean.class.getName());
         builder.addPropertyInstallCallback("transformers", ControllerState.CREATE);
         builder.addPropertyUninstallCallback("transformers", ControllerState.CREATE);
         beanMetaData = builder.getBeanMetaData();

         callbacks = beanMetaData.getInstallCallbacks();
         assertNotNull(callbacks);
         assertEquals(1, callbacks.size());
         callback = callbacks.get(0);
         assertNotNull(callback);
         assertInstanceOf(callback, InstallCallbackMetaData.class, false);
         assertEquals("transformers", callback.getProperty());
         assertEquals(ControllerState.INSTALLED, callback.getDependentState());
         assertEquals(ControllerState.CREATE, callback.getState());
         assertNull(callback.getSignature());
         assertNull(callback.getMethodName());
         assertNull(callback.getCardinality());
         assertNull(callback.getParameters());

         callbacks = beanMetaData.getUninstallCallbacks();
         assertNotNull(callbacks);
         assertEquals(1, callbacks.size());
         callback = callbacks.get(0);
         assertNotNull(callback);
         assertInstanceOf(callback, UninstallCallbackMetaData.class, false);
         assertEquals("transformers", callback.getProperty());
         assertEquals(ControllerState.INSTALLED, callback.getDependentState());
         assertEquals(ControllerState.CREATE, callback.getState());
         assertNull(callback.getSignature());
         assertNull(callback.getMethodName());
         assertNull(callback.getCardinality());
         assertNull(callback.getParameters());

         cc = controller.install(beanMetaData);
         assertNotNull(cc);
         assertEquals(ControllerState.INSTALLED, cc.getState());
         target = cc.getTarget();
         assertNotNull(target);
         bean = assertInstanceOf(target, SimpleCallbackBean.class);
         assertNotNull(bean.getTransformers());
         assertEquals(1, bean.getTransformers().size());
         assertSame(transformer, bean.getTransformers().iterator().next());

         // ct4

         builder = BeanMetaDataBuilder.createBuilder("ct4", SimpleCallbackBean.class.getName());
         builder.addPropertyInstallCallback("transformers", Set.class.getName(), ControllerState.CREATE, ControllerState.START, Cardinality.ZERO_TO_ONE);
         builder.addPropertyUninstallCallback("transformers", Set.class.getName(), ControllerState.CREATE, ControllerState.START, Cardinality.ZERO_TO_ONE);
         beanMetaData = builder.getBeanMetaData();

         callbacks = beanMetaData.getInstallCallbacks();
         assertNotNull(callbacks);
         assertEquals(1, callbacks.size());
         callback = callbacks.get(0);
         assertNotNull(callback);
         assertInstanceOf(callback, InstallCallbackMetaData.class, false);
         assertEquals("transformers", callback.getProperty());
         assertEquals(ControllerState.START, callback.getDependentState());
         assertEquals(ControllerState.CREATE, callback.getState());
         assertEquals(Set.class.getName(), callback.getSignature());
         assertEquals(Cardinality.ZERO_TO_ONE, callback.getCardinality());
         assertNull(callback.getMethodName());
         assertNull(callback.getParameters());

         callbacks = beanMetaData.getUninstallCallbacks();
         assertNotNull(callbacks);
         assertEquals(1, callbacks.size());
         callback = callbacks.get(0);
         assertNotNull(callback);
         assertInstanceOf(callback, UninstallCallbackMetaData.class, false);
         assertEquals("transformers", callback.getProperty());
         assertEquals(ControllerState.START, callback.getDependentState());
         assertEquals(ControllerState.CREATE, callback.getState());
         assertEquals(Set.class.getName(), callback.getSignature());
         assertEquals(Cardinality.ZERO_TO_ONE, callback.getCardinality());
         assertNull(callback.getMethodName());
         assertNull(callback.getParameters());

         cc = controller.install(beanMetaData);
         assertNotNull(cc);
         assertEquals(ControllerState.INSTALLED, cc.getState());
         target = cc.getTarget();
         assertNotNull(target);
         bean = assertInstanceOf(target, SimpleCallbackBean.class);
         assertNotNull(bean.getTransformers());
         assertEquals(1, bean.getTransformers().size());
         assertSame(transformer, bean.getTransformers().iterator().next());

         // ct1

         builder = BeanMetaDataBuilder.createBuilder("mct1", SimpleCallbackBean.class.getName());
         builder.addMethodInstallCallback("addTransformer");
         builder.addMethodUninstallCallback("removeTransformer");
         beanMetaData = builder.getBeanMetaData();

         callbacks = beanMetaData.getInstallCallbacks();
         assertNotNull(callbacks);
         assertEquals(1, callbacks.size());
         callback = callbacks.get(0);
         assertNotNull(callback);
         assertInstanceOf(callback, InstallCallbackMetaData.class, false);
         assertEquals("addTransformer", callback.getMethodName());
         assertEquals(ControllerState.INSTALLED, callback.getDependentState());
         assertNull(callback.getState());
         assertNull(callback.getSignature());
         assertNull(callback.getProperty());
         assertNull(callback.getCardinality());
         assertNull(callback.getParameters());

         callbacks = beanMetaData.getUninstallCallbacks();
         assertNotNull(callbacks);
         assertEquals(1, callbacks.size());
         callback = callbacks.get(0);
         assertNotNull(callback);
         assertInstanceOf(callback, UninstallCallbackMetaData.class, false);
         assertEquals("removeTransformer", callback.getMethodName());
         assertEquals(ControllerState.INSTALLED, callback.getDependentState());
         assertNull(callback.getState());
         assertNull(callback.getSignature());
         assertNull(callback.getProperty());
         assertNull(callback.getCardinality());
         assertNull(callback.getParameters());

         cc = controller.install(beanMetaData);
         assertNotNull(cc);
         assertEquals(ControllerState.INSTALLED, cc.getState());
         target = cc.getTarget();
         assertNotNull(target);
         bean = assertInstanceOf(target, SimpleCallbackBean.class);
         assertNotNull(bean.getTransformers());
         assertEquals(1, bean.getTransformers().size());
         assertSame(transformer, bean.getTransformers().iterator().next());

         // ct2

         builder = BeanMetaDataBuilder.createBuilder("mct2", SimpleCallbackBean.class.getName());
         builder.addMethodInstallCallback("addTransformer", Cardinality.ONE_TO_MANY);
         builder.addMethodUninstallCallback("removeTransformer", Cardinality.ONE_TO_MANY);
         beanMetaData = builder.getBeanMetaData();

         callbacks = beanMetaData.getInstallCallbacks();
         assertNotNull(callbacks);
         assertEquals(1, callbacks.size());
         callback = callbacks.get(0);
         assertNotNull(callback);
         assertInstanceOf(callback, InstallCallbackMetaData.class, false);
         assertEquals("addTransformer", callback.getMethodName());
         assertEquals(ControllerState.INSTALLED, callback.getDependentState());
         assertNull(callback.getState());
         assertNull(callback.getSignature());
         assertNull(callback.getProperty());
         assertEquals(Cardinality.ONE_TO_MANY, callback.getCardinality());
         assertNull(callback.getParameters());

         callbacks = beanMetaData.getUninstallCallbacks();
         assertNotNull(callbacks);
         assertEquals(1, callbacks.size());
         callback = callbacks.get(0);
         assertNotNull(callback);
         assertInstanceOf(callback, UninstallCallbackMetaData.class, false);
         assertEquals("removeTransformer", callback.getMethodName());
         assertEquals(ControllerState.INSTALLED, callback.getDependentState());
         assertNull(callback.getState());
         assertNull(callback.getSignature());
         assertNull(callback.getProperty());
         assertEquals(Cardinality.ONE_TO_MANY, callback.getCardinality());
         assertNull(callback.getParameters());

         cc = controller.install(beanMetaData);
         assertNotNull(cc);
         assertEquals(ControllerState.INSTALLED, cc.getState());
         target = cc.getTarget();
         assertNotNull(target);
         bean = assertInstanceOf(target, SimpleCallbackBean.class);
         assertNotNull(bean.getTransformers());
         assertEquals(1, bean.getTransformers().size());
         assertSame(transformer, bean.getTransformers().iterator().next());

         // ct3

         builder = BeanMetaDataBuilder.createBuilder("mct3", SimpleCallbackBean.class.getName());
         builder.addMethodInstallCallback("addTransformer", ControllerState.CREATE);
         builder.addMethodUninstallCallback("removeTransformer", ControllerState.CREATE);
         beanMetaData = builder.getBeanMetaData();

         callbacks = beanMetaData.getInstallCallbacks();
         assertNotNull(callbacks);
         assertEquals(1, callbacks.size());
         callback = callbacks.get(0);
         assertNotNull(callback);
         assertInstanceOf(callback, InstallCallbackMetaData.class, false);
         assertEquals("addTransformer", callback.getMethodName());
         assertEquals(ControllerState.INSTALLED, callback.getDependentState());
         assertEquals(ControllerState.CREATE, callback.getState());
         assertNull(callback.getSignature());
         assertNull(callback.getProperty());
         assertNull(callback.getCardinality());
         assertNull(callback.getParameters());

         callbacks = beanMetaData.getUninstallCallbacks();
         assertNotNull(callbacks);
         assertEquals(1, callbacks.size());
         callback = callbacks.get(0);
         assertNotNull(callback);
         assertInstanceOf(callback, UninstallCallbackMetaData.class, false);
         assertEquals("removeTransformer", callback.getMethodName());
         assertEquals(ControllerState.INSTALLED, callback.getDependentState());
         assertEquals(ControllerState.CREATE, callback.getState());
         assertNull(callback.getSignature());
         assertNull(callback.getProperty());
         assertNull(callback.getCardinality());
         assertNull(callback.getParameters());

         cc = controller.install(beanMetaData);
         assertNotNull(cc);
         assertEquals(ControllerState.INSTALLED, cc.getState());
         target = cc.getTarget();
         assertNotNull(target);
         bean = assertInstanceOf(target, SimpleCallbackBean.class);
         assertNotNull(bean.getTransformers());
         assertEquals(1, bean.getTransformers().size());
         assertSame(transformer, bean.getTransformers().iterator().next());

         // ct4

         builder = BeanMetaDataBuilder.createBuilder("mct4", SimpleCallbackBean.class.getName());
         builder.addMethodInstallCallback("addTransformer", Transformer.class.getName(), ControllerState.CREATE, ControllerState.START, Cardinality.ZERO_TO_ONE);
         builder.addMethodUninstallCallback("removeTransformer", Transformer.class.getName(), ControllerState.CREATE, ControllerState.START, Cardinality.ZERO_TO_ONE);
         beanMetaData = builder.getBeanMetaData();

         callbacks = beanMetaData.getInstallCallbacks();
         assertNotNull(callbacks);
         assertEquals(1, callbacks.size());
         callback = callbacks.get(0);
         assertNotNull(callback);
         assertInstanceOf(callback, InstallCallbackMetaData.class, false);
         assertEquals("addTransformer", callback.getMethodName());
         assertEquals(ControllerState.START, callback.getDependentState());
         assertEquals(ControllerState.CREATE, callback.getState());
         assertEquals(Transformer.class.getName(), callback.getSignature());
         assertEquals(Cardinality.ZERO_TO_ONE, callback.getCardinality());
         assertNull(callback.getProperty());
         assertNull(callback.getParameters());

         callbacks = beanMetaData.getUninstallCallbacks();
         assertNotNull(callbacks);
         assertEquals(1, callbacks.size());
         callback = callbacks.get(0);
         assertNotNull(callback);
         assertInstanceOf(callback, UninstallCallbackMetaData.class, false);
         assertEquals("removeTransformer", callback.getMethodName());
         assertEquals(ControllerState.START, callback.getDependentState());
         assertEquals(ControllerState.CREATE, callback.getState());
         assertEquals(Transformer.class.getName(), callback.getSignature());
         assertEquals(Cardinality.ZERO_TO_ONE, callback.getCardinality());
         assertNull(callback.getProperty());
         assertNull(callback.getParameters());

         cc = controller.install(beanMetaData);
         assertNotNull(cc);
         assertEquals(ControllerState.INSTALLED, cc.getState());
         target = cc.getTarget();
         assertNotNull(target);
         bean = assertInstanceOf(target, SimpleCallbackBean.class);
         assertNotNull(bean.getTransformers());
         assertEquals(1, bean.getTransformers().size());
         assertSame(transformer, bean.getTransformers().iterator().next());
      }
      finally
      {
         controller.shutdown();
      }
   }

   public void testAliases() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();
      try
      {
         BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("bean", SimpleBean.class.getName());
         builder.addAlias("alias");
         controller.install(builder.getBeanMetaData());
         assertNotNull(controller.getInstalledContext("alias"));

         builder = BeanMetaDataBuilderFactory.createBuilder("other", SimpleBean.class.getName());
         Object foobar = "foobar";
         builder.setAliases(Collections.singleton(foobar));
         controller.install(builder.getBeanMetaData());
         assertNotNull(controller.getInstalledContext("foobar"));
      }
      finally
      {
         controller.shutdown();
      }
   }

   public void testAnnotations() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();
      try
      {
         BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("bean", SimpleBean.class.getName());
         builder.addAnnotation("@" + SimpleAnnotation.class.getName() + "(name=\"foobar\")");
         controller.install(builder.getBeanMetaData());
         ControllerContext cc = controller.getInstalledContext("bean");
         assertNotNull(cc);
         MetaData metaData = cc.getScopeInfo().getMetaData();
         assertNotNull(metaData);
         assertNotNull(metaData.getAnnotation(SimpleAnnotation.class));

         builder = BeanMetaDataBuilderFactory.createBuilder("other", SimpleBean.class.getName());
         builder.setAnnotations(Collections.singleton("@" + SimpleAnnotation.class.getName() + "(name=\"foobar\")"));
         controller.install(builder.getBeanMetaData());
         cc = controller.getInstalledContext("other");
         assertNotNull(cc);
         metaData = cc.getScopeInfo().getMetaData();
         assertNotNull(metaData);
         assertNotNull(metaData.getAnnotation(SimpleAnnotation.class));
      }
      finally
      {
         controller.shutdown();
      }
   }

   public void testNewEnums() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder(Object.class.getName());
      builder.setErrorHandlingMode(ErrorHandlingMode.MANUAL);
      builder.setAutowireType(AutowireType.CONSTRUCTOR);
      builder.setAutowireCandidate(false);

      BeanMetaData bmd = builder.getBeanMetaData();
      assertEquals(ErrorHandlingMode.MANUAL, bmd.getErrorHandlingMode());
      assertEquals(AutowireType.CONSTRUCTOR, bmd.getAutowireType());
      assertFalse(bmd.isAutowireCandidate());
   }

   public void testBeanMetaDataFactory() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder("bean", Object.class.getName());
      BeanMetaDataFactory f1 = builder.getBeanMetaDataFactory();
      assertNotNull(f1);
      BeanMetaDataFactory f2 = builder.asBeanMetaDataFactory();
      assertNotNull(f2);
      List<BeanMetaData> b1 = f1.getBeans();
      assertNotNull(b1);
      List<BeanMetaData> b2 = f1.getBeans();
      assertNotNull(b2);
      assertEquals(b1, b2);

      AbstractBeanMetaData abmd = new AbstractBeanMetaData("bean", Object.class.getName());
      builder = BeanMetaDataBuilder.createBuilder(abmd);
      assertSame(abmd, builder.getBeanMetaDataFactory());
      BeanMetaDataFactory bmdf = builder.asBeanMetaDataFactory();
      assertNotNull(bmdf);
      List<BeanMetaData> beans = bmdf.getBeans();
      assertNotNull(beans);
      assertFalse(beans.isEmpty());
      assertSame(abmd, beans.get(0));
   }

   public void testRelatedClassName() throws Throwable
   {
      RelatedClassMetaData rcmd = new AbstractRelatedClassMetaData(Object.class.getName());
      Set<RelatedClassMetaData> related = new HashSet<RelatedClassMetaData>();
      related.add(rcmd);

      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder("Bean");
      builder.addRelatedClass(Object.class.getName());
      BeanMetaData bmd = builder.getBeanMetaData();

      assertEquals(related, bmd.getRelated());
   }

   public void testRelatedClassNameWithEnabled() throws Throwable
   {
      AbstractRelatedClassMetaData rcmd = new AbstractRelatedClassMetaData(Object.class.getName());
      rcmd.setEnabledValue("aop");
      Set<RelatedClassMetaData> related = new HashSet<RelatedClassMetaData>();
      related.add(rcmd);

      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder("Bean");
      builder.addRelatedClass(Object.class.getName(), "aop");
      BeanMetaData bmd = builder.getBeanMetaData();

      assertEquals(related, bmd.getRelated());
   }

   public void testMultipleRelatedClassNameWithMultipleEnabled() throws Throwable
   {
      AbstractRelatedClassMetaData rcmd1 = new AbstractRelatedClassMetaData(Object.class.getName());
      rcmd1.setEnabled(new HashSet<Object>(Arrays.asList("aop", "md")));
      AbstractRelatedClassMetaData rcmd2 = new AbstractRelatedClassMetaData(String.class.getName());
      rcmd2.setEnabled(new HashSet<Object>(Arrays.asList("qwert", "foobar")));

      Set<RelatedClassMetaData> related = new HashSet<RelatedClassMetaData>();
      related.add(rcmd1);
      related.add(rcmd2);

      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder("Bean");
      builder.addRelatedClass(Object.class.getName(), "aop", "md");
      builder.addRelatedClass(String.class.getName(), "qwert", "foobar");
      BeanMetaData bmd = builder.getBeanMetaData();

      assertEquals(related, bmd.getRelated());
   }

   public void testIgnoredLifecycle() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder("test");
      builder.ignoreCreate();
      builder.ignoreStart();
      builder.ignoreStop();
      builder.ignoreDestroy();
      BeanMetaData bmd = builder.getBeanMetaData();
      assertIgnoredLifecycle(bmd.getCreate());
      assertIgnoredLifecycle(bmd.getStart());
      assertIgnoredLifecycle(bmd.getStop());
      assertIgnoredLifecycle(bmd.getDestroy());       
   }

   protected void assertIgnoredLifecycle(LifecycleMetaData lmd)
   {
      assertNotNull(lmd);
      assertTrue(lmd.isIgnored());
   }

   public void testContextualInjection() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder("test");
      builder.addPropertyMetaData("ci", builder.createContextualInject());
      BeanMetaData bmd = builder.getBeanMetaData();
      Set<PropertyMetaData> properties = bmd.getProperties();
      assertNotNull(properties);
      assertEquals(1, properties.size());
      PropertyMetaData pmd = properties.iterator().next();
      ValueMetaData vmd = pmd.getValue();
      assertNotNull(vmd);
      assertNull(vmd.getUnderlyingValue());
   }

   public void testSearch() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder("test");
      builder.addPropertyMetaData("ci", builder.createContextualInject(null, null, null, null, Search.WIDTH));
      BeanMetaData bmd = builder.getBeanMetaData();
      Set<PropertyMetaData> properties = bmd.getProperties();
      assertNotNull(properties);
      assertEquals(1, properties.size());
      PropertyMetaData pmd = properties.iterator().next();
      ValueMetaData vmd = pmd.getValue();
      assertNotNull(vmd);
      AbstractDependencyValueMetaData advmd = assertInstanceOf(vmd, AbstractDependencyValueMetaData.class);
      assertEquals(Search.WIDTH, advmd.getSearch());
   }

   public void testPropertyAnnotations() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder("test");
      builder.addPropertyMetaData("ci", builder.createContextualInject());
      builder.addPropertyAnnotation("ci", new SimpleAnnotationImpl());
      BeanMetaData bmd = builder.getBeanMetaData();

      Set<PropertyMetaData> properties = bmd.getProperties();
      assertNotNull(properties);
      assertEquals(1, properties.size());
      PropertyMetaData pmd = properties.iterator().next();
      Set<AnnotationMetaData> annotations = pmd.getAnnotations();
      assertNotNull(annotations);
      assertEquals(1, annotations.size());
   }
}
