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

import org.jboss.beans.metadata.plugins.AbstractArrayMetaData;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractClassLoaderMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;

/**
 * Non-kernel ClassLoader dependency test case.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class ConstructorClassLoaderTestCase extends OldAbstractKernelDependencyTest
{
   public static Test suite()
   {
      return suite(ConstructorClassLoaderTestCase.class);
   }

   public ConstructorClassLoaderTestCase(String name) throws Throwable
   {
      super(name);
   }

   public ConstructorClassLoaderTestCase(String name, boolean xmltest) throws Throwable
   {
      super(name, xmltest);
   }

   public void testConstructorClassLoaderCorrectOrder() throws Throwable
   {
      constructorClassLoaderCorrectOrder();

      assertInstall(3, "URL");
      ControllerContext clCtx = assertInstall(0, "VFSClassLoader");
      ControllerContext bean1Ctx = assertInstall(1, "VFSBean1");
      ControllerContext bean2Ctx = assertInstall(2, "VFSBean2");
      Object clTarget = clCtx.getTarget();
      assertEquals("org.jboss.test.classloading.vfs.VFSClassLoader", clTarget.getClass().getName());
      ClassLoader cl0 = clTarget.getClass().getClassLoader();
      assertNotSame("org.jboss.test.classloading.vfs.VFSClassLoader", cl0.getClass().getName());

      Object bean2 = bean2Ctx.getTarget();
      ClassLoader cl2 = bean2.getClass().getClassLoader();
      assertEquals(clTarget, cl2);
      assertEquals("org.jboss.test.classloading.vfs.VFSClassLoader", cl2.getClass().getName());

      Object bean1 = bean1Ctx.getTarget();
      ClassLoader cl1 = bean1.getClass().getClassLoader();
      assertEquals(clTarget, cl1);
      assertEquals("org.jboss.test.classloading.vfs.VFSClassLoader", cl1.getClass().getName());
   }

   public void constructorClassLoaderCorrectOrder() throws Throwable
   {
      AbstractBeanMetaData metaData3 = new AbstractBeanMetaData("URL", "java.net.URL");
      AbstractConstructorMetaData cmd3 = new AbstractConstructorMetaData();
      metaData3.setConstructor(cmd3);
      cmd3.setFactoryClass("org.jboss.test.classloading.vfs.ClassLoaderUtil");
      cmd3.setFactoryMethod("getLocation");
      AbstractParameterMetaData pmd3 = new AbstractParameterMetaData(getClass().getName());
      ArrayList<ParameterMetaData> params3 = new ArrayList<ParameterMetaData>();
      params3.add(pmd3);
      cmd3.setParameters(params3);

      AbstractDependencyValueMetaData url = new AbstractDependencyValueMetaData("URL");
      
      AbstractBeanMetaData metaData0 = new AbstractBeanMetaData("VFSClassLoader",
         "org.jboss.test.classloading.vfs.VFSClassLoader");
      AbstractConstructorMetaData clCMD = new AbstractConstructorMetaData();
      AbstractArrayMetaData array = new AbstractArrayMetaData();
      array.add(url);
      AbstractParameterMetaData urls = new AbstractParameterMetaData(null, array);
      ArrayList<ParameterMetaData> constructor0 = new ArrayList<ParameterMetaData>();
      constructor0.add(urls);
      clCMD.setParameters(constructor0);
      clCMD.setFactoryClass("org.jboss.test.classloading.vfs.VFSClassLoaderFactory");
      clCMD.setFactoryMethod("newClassLoader");
      metaData0.setConstructor(clCMD);

      AbstractDependencyValueMetaData vfsCL = new AbstractDependencyValueMetaData("VFSClassLoader");

      String bean1Type = "org.jboss.test.kernel.dependency.classloader.SimpleBeanImpl";
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("VFSBean1", bean1Type);
      HashSet<PropertyMetaData> attributes1 = new HashSet<PropertyMetaData>();
      attributes1.add(new AbstractPropertyMetaData("string", "String1"));
      metaData1.setProperties(attributes1);
      metaData1.setClassLoader(new AbstractClassLoaderMetaData(vfsCL));

      AbstractBeanMetaData metaData2 = new AbstractBeanMetaData("VFSBean2",
         "org.jboss.test.kernel.dependency.classloader.SimpleBeanWithConstructorClassLoaderImpl");
      HashSet<PropertyMetaData> attributes2 = new HashSet<PropertyMetaData>();
      attributes2.add(new AbstractPropertyMetaData("string", "String2"));
      metaData2.setProperties(attributes2);
      metaData2.setClassLoader(new AbstractClassLoaderMetaData(vfsCL));
      ArrayList<ParameterMetaData> constructor2 = new ArrayList<ParameterMetaData>();
      String bean1Iface = "org.jboss.test.kernel.dependency.classloader.SimpleBean";
      AbstractDependencyValueMetaData bean2Depends = new AbstractDependencyValueMetaData("VFSBean1");
      AbstractParameterMetaData param = new AbstractParameterMetaData(bean1Iface, bean2Depends);
      constructor2.add(param);
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      metaData2.setConstructor(cmd);
      cmd.setParameters(constructor2);

      setBeanMetaDatas(new BeanMetaData[] { metaData0, metaData1, metaData2, metaData3 });
   }

   public void testConstructorClassLoaderWrongOrder() throws Throwable
   {
      constructorClassLoaderWrongOrder();

      assertInstall(3, "URL");
      ControllerContext bean1Ctx = assertInstall(1, "VFSBean1", ControllerState.NOT_INSTALLED);
      ControllerContext bean2Ctx = assertInstall(2, "VFSBean2", ControllerState.NOT_INSTALLED);
      ControllerContext clCtx = assertInstall(0, "VFSClassLoader");
      assertEquals(ControllerState.INSTALLED, bean1Ctx.getState());
      assertEquals(ControllerState.INSTALLED, bean2Ctx.getState());

      Object clTarget = clCtx.getTarget();
      assertEquals("org.jboss.test.classloading.vfs.VFSClassLoader", clTarget.getClass().getName());
      ClassLoader cl0 = clTarget.getClass().getClassLoader();
      assertNotSame("org.jboss.test.classloading.vfs.VFSClassLoader", cl0.getClass().getName());

      Object bean2 = bean2Ctx.getTarget();
      ClassLoader cl2 = bean2.getClass().getClassLoader();
      assertEquals(clTarget, cl2);
      assertEquals("org.jboss.test.classloading.vfs.VFSClassLoader", cl2.getClass().getName());

      Object bean1 = bean1Ctx.getTarget();
      ClassLoader cl1 = bean1.getClass().getClassLoader();
      assertEquals(clTarget, cl1);
      assertEquals("org.jboss.test.classloading.vfs.VFSClassLoader", cl1.getClass().getName());
   }

   public void constructorClassLoaderWrongOrder() throws Throwable
   {
      AbstractBeanMetaData metaData3 = new AbstractBeanMetaData("URL", "java.net.URL");
      AbstractConstructorMetaData cmd3 = new AbstractConstructorMetaData();
      metaData3.setConstructor(cmd3);
      cmd3.setFactoryClass("org.jboss.test.classloading.vfs.ClassLoaderUtil");
      cmd3.setFactoryMethod("getLocation");
      AbstractParameterMetaData pmd3 = new AbstractParameterMetaData(getClass().getName());
      ArrayList<ParameterMetaData> params3 = new ArrayList<ParameterMetaData>();
      params3.add(pmd3);
      cmd3.setParameters(params3);

      AbstractDependencyValueMetaData url = new AbstractDependencyValueMetaData("URL");

      AbstractBeanMetaData metaData0 = new AbstractBeanMetaData("VFSClassLoader",
         "org.jboss.test.classloading.vfs.VFSClassLoader");
      AbstractConstructorMetaData clCMD = new AbstractConstructorMetaData();
      AbstractArrayMetaData array = new AbstractArrayMetaData();
      array.add(url);
      AbstractParameterMetaData urls = new AbstractParameterMetaData(null, array);
      ArrayList<ParameterMetaData> constructor0 = new ArrayList<ParameterMetaData>();
      constructor0.add(urls);
      clCMD.setParameters(constructor0);
      clCMD.setFactoryClass("org.jboss.test.classloading.vfs.VFSClassLoaderFactory");
      clCMD.setFactoryMethod("newClassLoader");
      metaData0.setConstructor(clCMD);

      AbstractDependencyValueMetaData vfsCL = new AbstractDependencyValueMetaData("VFSClassLoader");

      String bean1Type = "org.jboss.test.kernel.dependency.classloader.SimpleBeanImpl";
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("VFSBean1", bean1Type);
      HashSet<PropertyMetaData> attributes1 = new HashSet<PropertyMetaData>();
      attributes1.add(new AbstractPropertyMetaData("string", "String1"));
      metaData1.setProperties(attributes1);
      metaData1.setClassLoader(new AbstractClassLoaderMetaData(vfsCL));

      AbstractBeanMetaData metaData2 = new AbstractBeanMetaData("VFSBean2",
         "org.jboss.test.kernel.dependency.classloader.SimpleBeanWithConstructorClassLoaderImpl");
      HashSet<PropertyMetaData> attributes2 = new HashSet<PropertyMetaData>();
      attributes2.add(new AbstractPropertyMetaData("string", "String2"));
      metaData2.setProperties(attributes2);
      metaData2.setClassLoader(new AbstractClassLoaderMetaData(vfsCL));
      ArrayList<ParameterMetaData> constructor2 = new ArrayList<ParameterMetaData>();
      String bean1Iface = "org.jboss.test.kernel.dependency.classloader.SimpleBean";
      AbstractDependencyValueMetaData bean2Depends = new AbstractDependencyValueMetaData("VFSBean1");
      AbstractParameterMetaData param = new AbstractParameterMetaData(bean1Iface, bean2Depends);
      constructor2.add(param);
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      metaData2.setConstructor(cmd);
      cmd.setParameters(constructor2);

      setBeanMetaDatas(new BeanMetaData[] { metaData0, metaData1, metaData2, metaData3 });
   }

   public void testConstructorClassLoaderReinstall() throws Throwable
   {
      constructorClassLoaderReinstall();

      assertInstall(3, "URL");
      ControllerContext clCtx = assertInstall(0, "VFSClassLoader");
      ControllerContext bean1Ctx = assertInstall(1, "VFSBean1");
      ControllerContext bean2Ctx = assertInstall(2, "VFSBean2");

      Object clTarget = clCtx.getTarget();
      assertEquals("org.jboss.test.classloading.vfs.VFSClassLoader", clTarget.getClass().getName());
      ClassLoader cl0 = clTarget.getClass().getClassLoader();
      assertNotSame("org.jboss.test.classloading.vfs.VFSClassLoader", cl0.getClass().getName());

      Object bean2 = bean2Ctx.getTarget();
      ClassLoader cl2 = bean2.getClass().getClassLoader();
      assertEquals(clTarget, cl2);
      assertEquals("org.jboss.test.classloading.vfs.VFSClassLoader", cl2.getClass().getName());

      Object bean1 = bean1Ctx.getTarget();
      ClassLoader cl1 = bean1.getClass().getClassLoader();
      assertEquals(clTarget, cl1);
      assertEquals("org.jboss.test.classloading.vfs.VFSClassLoader", cl1.getClass().getName());
      
      assertUninstall("VFSClassLoader");
      assertEquals(ControllerState.ERROR, clCtx.getState());
      assertEquals(ControllerState.NOT_INSTALLED, bean1Ctx.getState());
      assertEquals(ControllerState.NOT_INSTALLED, bean2Ctx.getState());

      clCtx = assertInstall(0, "VFSClassLoader");
      assertEquals(ControllerState.INSTALLED, bean1Ctx.getState());
      assertEquals(ControllerState.INSTALLED, bean2Ctx.getState());

      clTarget = clCtx.getTarget();
      assertEquals("org.jboss.test.classloading.vfs.VFSClassLoader", clTarget.getClass().getName());
      cl0 = clTarget.getClass().getClassLoader();
      assertNotSame("org.jboss.test.classloading.vfs.VFSClassLoader", cl0.getClass().getName());

      bean2 = bean2Ctx.getTarget();
      cl2 = bean2.getClass().getClassLoader();
      assertEquals(clTarget, cl2);
      assertEquals("org.jboss.test.classloading.vfs.VFSClassLoader", cl2.getClass().getName());

      bean1 = bean1Ctx.getTarget();
      cl1 = bean1.getClass().getClassLoader();
      assertEquals(clTarget, cl1);
      assertEquals("org.jboss.test.classloading.vfs.VFSClassLoader", cl1.getClass().getName());
   }

   public void constructorClassLoaderReinstall() throws Throwable
   {
      AbstractBeanMetaData metaData3 = new AbstractBeanMetaData("URL", "java.net.URL");
      AbstractConstructorMetaData cmd3 = new AbstractConstructorMetaData();
      metaData3.setConstructor(cmd3);
      cmd3.setFactoryClass("org.jboss.test.classloading.vfs.ClassLoaderUtil");
      cmd3.setFactoryMethod("getLocation");
      AbstractParameterMetaData pmd3 = new AbstractParameterMetaData(getClass().getName());
      ArrayList<ParameterMetaData> params3 = new ArrayList<ParameterMetaData>();
      params3.add(pmd3);
      cmd3.setParameters(params3);

      AbstractDependencyValueMetaData url = new AbstractDependencyValueMetaData("URL");

      AbstractBeanMetaData metaData0 = new AbstractBeanMetaData("VFSClassLoader",
         "org.jboss.test.classloading.vfs.VFSClassLoader");
      AbstractConstructorMetaData clCMD = new AbstractConstructorMetaData();
      AbstractArrayMetaData array = new AbstractArrayMetaData();
      array.add(url);
      AbstractParameterMetaData urls = new AbstractParameterMetaData(null, array);
      ArrayList<ParameterMetaData> constructor0 = new ArrayList<ParameterMetaData>();
      constructor0.add(urls);
      clCMD.setParameters(constructor0);
      clCMD.setFactoryClass("org.jboss.test.classloading.vfs.VFSClassLoaderFactory");
      clCMD.setFactoryMethod("newClassLoader");
      metaData0.setConstructor(clCMD);

      AbstractDependencyValueMetaData vfsCL = new AbstractDependencyValueMetaData("VFSClassLoader");

      String bean1Type = "org.jboss.test.kernel.dependency.classloader.SimpleBeanImpl";
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("VFSBean1", bean1Type);
      HashSet<PropertyMetaData> attributes1 = new HashSet<PropertyMetaData>();
      attributes1.add(new AbstractPropertyMetaData("string", "String1"));
      metaData1.setProperties(attributes1);
      metaData1.setClassLoader(new AbstractClassLoaderMetaData(vfsCL));

      AbstractBeanMetaData metaData2 = new AbstractBeanMetaData("VFSBean2",
         "org.jboss.test.kernel.dependency.classloader.SimpleBeanWithConstructorClassLoaderImpl");
      HashSet<PropertyMetaData> attributes2 = new HashSet<PropertyMetaData>();
      attributes2.add(new AbstractPropertyMetaData("string", "String2"));
      metaData2.setProperties(attributes2);
      metaData2.setClassLoader(new AbstractClassLoaderMetaData(vfsCL));
      ArrayList<ParameterMetaData> constructor2 = new ArrayList<ParameterMetaData>();
      String bean1Iface = "org.jboss.test.kernel.dependency.classloader.SimpleBean";
      AbstractDependencyValueMetaData bean2Depends = new AbstractDependencyValueMetaData("VFSBean1");
      AbstractParameterMetaData param = new AbstractParameterMetaData(bean1Iface, bean2Depends);
      constructor2.add(param);
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      metaData2.setConstructor(cmd);
      cmd.setParameters(constructor2);

      setBeanMetaDatas(new BeanMetaData[] { metaData0, metaData1, metaData2, metaData3 });
   }
}