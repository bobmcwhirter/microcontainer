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
package org.jboss.test.kernel.config.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import junit.framework.Test;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.plugins.AbstractArrayMetaData;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractCollectionMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractListMetaData;
import org.jboss.beans.metadata.plugins.AbstractMapMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.AbstractSetMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.joinpoint.spi.JoinpointException;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.test.kernel.config.support.SimpleBean;

/**
 * Instantiation Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class InstantiateTestCase extends AbstractKernelConfigTest
{
   public static Test suite()
   {
      return suite(InstantiateTestCase.class);
   }

   public InstantiateTestCase(String name)
   {
      super(name);
   }

   public InstantiateTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public void testSimpleInstantiateFromBeanInfo() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      BeanInfo info = configurator.getBeanInfo(SimpleBean.class);

      SimpleBean bean = (SimpleBean) instantiate(configurator, info);
      assertEquals("()", bean.getConstructorUsed());
   }

   public void testSimpleInstantiateFromBeanMetaData() throws Throwable
   {
      SimpleBean bean = simpleInstantiateFromBeanMetaData();
      assertEquals("()", bean.getConstructorUsed());
   }

   protected SimpleBean simpleInstantiateFromBeanMetaData() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());

      return (SimpleBean) instantiate(configurator, bmd);
   }
   
   public void testParameterInstantiateFromBeanMetaData() throws Throwable
   {
      SimpleBean bean = parameterInstantiateFromBeanMetaData();
      assertEquals("Constructor Value", bean.getConstructorUsed());
      assertEquals("Constructor Value", bean.getAString());
   }

   protected SimpleBean parameterInstantiateFromBeanMetaData() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      
      ArrayList<ParameterMetaData> constructorParams = new ArrayList<ParameterMetaData>();
      AbstractParameterMetaData pmd = new AbstractParameterMetaData("Constructor Value");
      constructorParams.add(pmd);
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      cmd.setParameters(constructorParams);

      return (SimpleBean) instantiate(configurator, bmd);
   }
   
   public void testParameterInstantiateWithTypeOverride() throws Throwable
   {
      SimpleBean bean = parameterInstantiateWithTypeOverride();
      assertEquals("java.lang.Integer:7", bean.getConstructorUsed());
      assertEquals(new Integer(7), bean.getAnInt());
   }

   protected SimpleBean parameterInstantiateWithTypeOverride() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      
      ArrayList<ParameterMetaData> constructorParams = new ArrayList<ParameterMetaData>();
      AbstractParameterMetaData pmd = new AbstractParameterMetaData();
      pmd.setType("java.lang.Integer");
      StringValueMetaData svmd = new StringValueMetaData("7");
      pmd.setValue(svmd);
      constructorParams.add(pmd);
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      cmd.setParameters(constructorParams);

      return (SimpleBean) instantiate(configurator, bmd);
   }
   
   public void testParameterInstantiateFromNull() throws Throwable
   {
      SimpleBean bean = parameterInstantiateFromNull();
      assertEquals(null, bean.getConstructorUsed());
      assertEquals(null, bean.getAString());
   }

   protected SimpleBean parameterInstantiateFromNull() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      
      ArrayList<ParameterMetaData> constructorParams = new ArrayList<ParameterMetaData>();
      AbstractParameterMetaData pmd = new AbstractParameterMetaData();
      pmd.setType("java.lang.String");
      pmd.setValue(new AbstractValueMetaData());
      constructorParams.add(pmd);
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      cmd.setParameters(constructorParams);

      return (SimpleBean) instantiate(configurator, bmd);
   }
   
   public void testParameterInstantiateViaInterfaceWithTypeOverride() throws Throwable
   {
      SimpleBean bean = parameterInstantiateViaInterfaceWithTypeOverride();
      assertEquals("java.lang.Comparable:java.lang.Integer:12", bean.getConstructorUsed());
      assertEquals(12, bean.getAnObject());
   }

   protected SimpleBean parameterInstantiateViaInterfaceWithTypeOverride() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      
      ArrayList<ParameterMetaData> constructorParams = new ArrayList<ParameterMetaData>();
      AbstractParameterMetaData pmd = new AbstractParameterMetaData();
      pmd.setType("java.lang.Comparable");
      StringValueMetaData svmd = new StringValueMetaData("12");
      svmd.setConfigurator(configurator);
      svmd.setType("java.lang.Integer");
      pmd.setValue(svmd);
      constructorParams.add(pmd);
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      cmd.setParameters(constructorParams);

      return (SimpleBean) instantiate(configurator, bmd);
   }
   
   public void testParameterInstantiateWithCollection() throws Throwable
   {
      SimpleBean bean = parameterInstantiateWithCollection();
      assertEquals("java.util.Collection:java.util.ArrayList:[1, 2]", bean.getConstructorUsed());
      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add("1");
      expected.add("2");
      assertEquals(expected, bean.getCollection());
   }

   protected SimpleBean parameterInstantiateWithCollection() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      
      ArrayList<ParameterMetaData> constructorParams = new ArrayList<ParameterMetaData>();
      AbstractParameterMetaData pmd = new AbstractParameterMetaData();
      pmd.setType("java.util.Collection");
      AbstractCollectionMetaData collection = new AbstractCollectionMetaData();
      collection.setConfigurator(configurator);
      collection.setElementType("java.lang.String");
      collection.add(new StringValueMetaData("1"));
      collection.add(new StringValueMetaData("2"));
      pmd.setValue(collection);
      constructorParams.add(pmd);
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      cmd.setParameters(constructorParams);

      return (SimpleBean) instantiate(configurator, bmd);
   }
   
   public void testParameterInstantiateWithList() throws Throwable
   {
      SimpleBean bean = parameterInstantiateWithList();
      assertEquals("java.util.List:java.util.ArrayList:[1, 2]", bean.getConstructorUsed());
      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add("1");
      expected.add("2");
      assertEquals(expected, bean.getList());
   }

   protected SimpleBean parameterInstantiateWithList() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      
      ArrayList<ParameterMetaData> constructorParams = new ArrayList<ParameterMetaData>();
      AbstractParameterMetaData pmd = new AbstractParameterMetaData();
      pmd.setType("java.util.List");
      AbstractListMetaData collection = new AbstractListMetaData();
      collection.setConfigurator(configurator);
      collection.setElementType("java.lang.String");
      collection.add(new StringValueMetaData("1"));
      collection.add(new StringValueMetaData("2"));
      pmd.setValue(collection);
      constructorParams.add(pmd);
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      cmd.setParameters(constructorParams);

      return (SimpleBean) instantiate(configurator, bmd);
   }
   
   public void testParameterInstantiateWithSet() throws Throwable
   {
      SimpleBean bean = parameterInstantiateWithSet();
      assertEquals("java.util.Set:java.util.HashSet:[1]", bean.getConstructorUsed());
      HashSet<Object> expected = new HashSet<Object>();
      expected.add("1");
      assertEquals(expected, bean.getSet());
   }

   protected SimpleBean parameterInstantiateWithSet() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      
      ArrayList<ParameterMetaData> constructorParams = new ArrayList<ParameterMetaData>();
      AbstractParameterMetaData pmd = new AbstractParameterMetaData();
      pmd.setType("java.util.Set");
      AbstractSetMetaData collection = new AbstractSetMetaData();
      collection.setConfigurator(configurator);
      collection.setElementType("java.lang.String");
      collection.add(new StringValueMetaData("1"));
      pmd.setValue(collection);
      constructorParams.add(pmd);
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      cmd.setParameters(constructorParams);

      return (SimpleBean) instantiate(configurator, bmd);
   }
   
   public void testParameterInstantiateWithArray() throws Throwable
   {
      SimpleBean bean = parameterInstantiateWithArray();
      assertEquals("Array:[Ljava.lang.String;:[1, 2]", bean.getConstructorUsed());
      String[] expected = new String[] { "1", "2" };
      assertEquals(expected, bean.getArray());
   }

   protected SimpleBean parameterInstantiateWithArray() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      
      ArrayList<ParameterMetaData> constructorParams = new ArrayList<ParameterMetaData>();
      AbstractParameterMetaData pmd = new AbstractParameterMetaData();
      pmd.setType(new Object[0].getClass().getName());
      AbstractArrayMetaData collection = new AbstractArrayMetaData();
      collection.setConfigurator(configurator);
      collection.setType("[Ljava.lang.String;");
      collection.setElementType("java.lang.String");
      collection.add(new StringValueMetaData("1"));
      collection.add(new StringValueMetaData("2"));
      pmd.setValue(collection);
      constructorParams.add(pmd);
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      cmd.setParameters(constructorParams);

      return (SimpleBean) instantiate(configurator, bmd);
   }
   
   public void testParameterInstantiateWithMap() throws Throwable
   {
      SimpleBean bean = parameterInstantiateWithMap();
      assertEquals("java.util.Map:java.util.HashMap:{1=2}", bean.getConstructorUsed());
      HashMap<Object, Object> expected = new HashMap<Object, Object>();
      expected.put("1", "2");
      assertEquals(expected, bean.getMap());
   }

   protected SimpleBean parameterInstantiateWithMap() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      
      ArrayList<ParameterMetaData> constructorParams = new ArrayList<ParameterMetaData>();
      AbstractParameterMetaData pmd = new AbstractParameterMetaData();
      pmd.setType("java.util.Map");
      AbstractMapMetaData collection = new AbstractMapMetaData();
      collection.setConfigurator(configurator);
      collection.setKeyType("java.lang.String");
      collection.setValueType("java.lang.String");
      collection.put(new StringValueMetaData("1"), new StringValueMetaData("2"));
      pmd.setValue(collection);
      constructorParams.add(pmd);
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      cmd.setParameters(constructorParams);

      return (SimpleBean) instantiate(configurator, bmd);
   }
   
   public void testParameterInstantiateWithProperties() throws Throwable
   {
      SimpleBean bean = parameterInstantiateWithProperties();
      assertEquals("java.util.Hashtable:java.util.Properties:{1=2}", bean.getConstructorUsed());
      Properties expected = new Properties();
      expected.put("1", "2");
      assertEquals(expected, bean.getMap());
   }

   protected SimpleBean parameterInstantiateWithProperties() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      
      ArrayList<ParameterMetaData> constructorParams = new ArrayList<ParameterMetaData>();
      AbstractParameterMetaData pmd = new AbstractParameterMetaData();
      pmd.setType("java.util.Hashtable");
      AbstractMapMetaData collection = new AbstractMapMetaData();
      collection.setConfigurator(configurator);
      collection.setType("java.util.Properties");
      collection.setKeyType("java.lang.String");
      collection.setValueType("java.lang.String");
      collection.put(new StringValueMetaData("1"), new StringValueMetaData("2"));
      pmd.setValue(collection);
      constructorParams.add(pmd);
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      cmd.setParameters(constructorParams);

      return (SimpleBean) instantiate(configurator, bmd);
   }
   
   public void testValueInstantiateFromValue() throws Throwable
   {
      Object object = valueInstantiateFromValue();
      assertEquals("AString", object);
   }

   protected Object valueInstantiateFromValue() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData();
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      StringValueMetaData vmd = new StringValueMetaData("AString");
      vmd.setType(String.class.getName());
      vmd.setConfigurator(configurator);
      cmd.setValue(vmd);

      return instantiate(configurator, bmd);
   }
   
   public void testValueInstantiateFromCollection() throws Throwable
   {
      Object object = valueInstantiateFromCollection();
      assertNotNull(object);
      assertTrue(object instanceof Collection);
      assertEquals(new ArrayList(), object);
   }

   protected Object valueInstantiateFromCollection() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData();
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      AbstractCollectionMetaData vmd = new AbstractCollectionMetaData();
      cmd.setValue(vmd);

      return instantiate(configurator, bmd);
   }
   
   public void testValueInstantiateFromList() throws Throwable
   {
      Object object = valueInstantiateFromList();
      assertNotNull(object);
      assertTrue(object instanceof List);
      assertEquals(new ArrayList(), object);
   }

   protected Object valueInstantiateFromList() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData();
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      AbstractListMetaData vmd = new AbstractListMetaData();
      cmd.setValue(vmd);

      return instantiate(configurator, bmd);
   }
   
   public void testValueInstantiateFromSet() throws Throwable
   {
      Object object = valueInstantiateFromSet();
      assertNotNull(object);
      assertTrue(object instanceof Set);
      assertEquals(new HashSet(), object);
   }

   protected Object valueInstantiateFromSet() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData();
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      AbstractSetMetaData vmd = new AbstractSetMetaData();
      cmd.setValue(vmd);

      return instantiate(configurator, bmd);
   }
   
   public void testValueInstantiateFromArray() throws Throwable
   {
      Object object = valueInstantiateFromArray();
      assertNotNull(object);
      assertTrue(object.getClass().isArray());
   }

   protected Object valueInstantiateFromArray() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData();
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      AbstractArrayMetaData vmd = new AbstractArrayMetaData();
      vmd.setElementType(String.class.getName());
      vmd.setConfigurator(configurator);
      cmd.setValue(vmd);

      return instantiate(configurator, bmd);
   }
   
   public void testValueInstantiateFromObject() throws Throwable
   {
      Object object = valueInstantiateFromObject();
      assertNotNull(object);
      assertTrue(object.getClass() == Object.class);
   }

   protected Object valueInstantiateFromObject() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData();
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      AbstractValueMetaData vmd = new AbstractValueMetaData();
      vmd.setValue(new Object());
      cmd.setValue(vmd);

      return instantiate(configurator, bmd);
   }

   public void testConstructorDoesNotExist() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      
      ArrayList<ParameterMetaData> constructorParams = new ArrayList<ParameterMetaData>();
      AbstractParameterMetaData pmd = new AbstractParameterMetaData("doesNotExist", "Constructor Value");
      constructorParams.add(pmd);
      AbstractConstructorMetaData cmd = new AbstractConstructorMetaData();
      bmd.setConstructor(cmd);
      cmd.setParameters(constructorParams);

      try
      {
         instantiate(configurator, bmd);
         fail("Should not be here");
      }
      catch (JoinpointException expected)
      {
      }
   }
}