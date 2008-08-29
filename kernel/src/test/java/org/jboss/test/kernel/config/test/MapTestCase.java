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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.jboss.beans.metadata.plugins.*;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.test.kernel.config.support.CustomMap;
import org.jboss.test.kernel.config.support.MyObject;
import org.jboss.test.kernel.config.support.SimpleBean;
import org.jboss.test.kernel.config.support.UnmodifiableGetterBean;

import junit.framework.Test;

/**
 * Map Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class MapTestCase extends AbstractKernelConfigTest
{
   MyObject object1 = new MyObject("object1");
   MyObject object2 = new MyObject("object2");
   String string1 = "string1";
   String string2 = "string2";
   String integer = "integer";

   public static Test suite()
   {
      return suite(MapTestCase.class);
   }

   public MapTestCase(String name)
   {
      super(name);
   }

   public MapTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public void testSimpleMapFromObjects() throws Throwable
   {
      SimpleBean bean = simpleMapFromObjects();
      assertNotNull(bean);
      
      Map<?,?> result = bean.getMap();
      assertNotNull("Should be a map", result);
      
      Map<Object, Object> expected = new HashMap<Object, Object>();
      expected.put(object1, object2);
      expected.put(object2, object1);
      assertEquals(expected, result);
   }

   public SimpleBean simpleMapFromObjects() throws Throwable
   {
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);
      
      AbstractValueMetaData key1 = new AbstractValueMetaData(object1);
      AbstractValueMetaData value1 = new AbstractValueMetaData(object2);
      AbstractValueMetaData key2 = new AbstractValueMetaData(object2);
      AbstractValueMetaData value2 = new AbstractValueMetaData(object1);

      AbstractMapMetaData smd = new AbstractMapMetaData();
      smd.put(key1, value1);
      smd.put(key2, value2);

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("map", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(bmd);
   }

   public void testSimpleMapFromStrings() throws Throwable
   {
      SimpleBean bean = simpleMapFromStrings();
      assertNotNull(bean);
      
      Map<?,?> result = bean.getMap();
      assertNotNull("Should be a map", result);
      
      Map<Object, Object> expected = new HashMap<Object, Object>();
      expected.put(string1, string2);
      expected.put(string2, string1);
      assertEquals(expected, result);
   }

   protected SimpleBean simpleMapFromStrings() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();

      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData kmd1 = new StringValueMetaData(string1);
      StringValueMetaData kmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd1 = new StringValueMetaData(string2);
      StringValueMetaData vmd2 = new StringValueMetaData(string1);

      AbstractMapMetaData smd = new AbstractMapMetaData();
      smd.setKeyType("java.lang.String");
      smd.setValueType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.put(kmd1, vmd1);
      smd.put(kmd2, vmd2);

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("map", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testCustomMapExplicit() throws Throwable
   {
      SimpleBean bean = customMapExplicit();
      assertNotNull(bean);
      
      Map<?,?> result = bean.getMap();
      assertNotNull("Should be a map", result);
      assertTrue("Not a CustomMap: " + result.getClass(), result instanceof CustomMap);
      
      Map<Object, Object> expected = new HashMap<Object, Object>();
      expected.put(string1, string2);
      expected.put(string2, string1);
      assertEquals(expected, result);
   }

   protected SimpleBean customMapExplicit() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData kmd1 = new StringValueMetaData(string1);
      StringValueMetaData kmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd1 = new StringValueMetaData(string2);
      StringValueMetaData vmd2 = new StringValueMetaData(string1);

      AbstractMapMetaData smd = new AbstractMapMetaData();
      smd.setType(CustomMap.class.getName());
      smd.setKeyType("java.lang.String");
      smd.setValueType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.put(kmd1, vmd1);
      smd.put(kmd2, vmd2);

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("map", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testCustomMapFromSignature() throws Throwable
   {
      SimpleBean bean = customMapFromSignature();
      assertNotNull(bean);
      
      Map<?,?> result = bean.getCustomMap();
      assertNotNull("Should be a map", result);
      assertTrue("Not a CustomMap: " + result.getClass(), result instanceof CustomMap);
      
      Map<Object, Object> expected = new HashMap<Object, Object>();
      expected.put(string1, string2);
      expected.put(string2, string1);
      assertEquals(expected, result);
   }

   protected SimpleBean customMapFromSignature() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData kmd1 = new StringValueMetaData(string1);
      StringValueMetaData kmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd1 = new StringValueMetaData(string2);
      StringValueMetaData vmd2 = new StringValueMetaData(string1);

      AbstractMapMetaData smd = new AbstractMapMetaData();
      smd.setKeyType("java.lang.String");
      smd.setValueType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.put(kmd1, vmd1);
      smd.put(kmd2, vmd2);

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("customMap", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testCustomMapPreInstantiated() throws Throwable
   {
      SimpleBean bean = customMapPreInstantiated();
      assertNotNull(bean);
      
      Map<?,?> result = bean.getPreInstantiatedMap();
      assertNotNull("Should be a map", result);
      assertTrue("Not a CustomMap: " + result.getClass(), result instanceof CustomMap);
      assertTrue("Not preinstantiated", ((CustomMap) result).getPreInstantiated());
      
      Map<Object, Object> expected = new HashMap<Object, Object>();
      expected.put(string1, string2);
      expected.put(string2, string1);
      assertEquals(expected, result);

      Map<?,?> setter = bean.setterMap;
      assertNotNull(setter);
      assertFalse("Empty setterMap", setter.isEmpty());
   }

   protected SimpleBean customMapPreInstantiated() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();

      AbstractBeanMetaData bmd = new AbstractBeanMetaData("test1", SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData kmd1 = new StringValueMetaData(string1);
      StringValueMetaData kmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd1 = new StringValueMetaData(string2);
      StringValueMetaData vmd2 = new StringValueMetaData(string1);

      AbstractMapMetaData smd = new AbstractMapMetaData();
      smd.setKeyType("java.lang.String");
      smd.setValueType("java.lang.String");
      smd.put(kmd1, vmd1);
      smd.put(kmd2, vmd2);

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("preInstantiatedMap", smd);
      properties.add(pmd);
      
      AbstractMapMetaData lmd = new AbstractMapMetaData();
      lmd.setKeyType("java.lang.String");
      lmd.setValueType("java.lang.String");
      lmd.put(new StringValueMetaData("justKey"), new StringValueMetaData("justValue"));
      AbstractPropertyMetaData pmd2 = new AbstractPropertyMetaData("setterMap", lmd);
      properties.add(pmd2);

      return (SimpleBean) instantiate(controller, bmd);
   }

   public void testUnmodifiableMapPreInstantiated() throws Throwable
   {
      UnmodifiableGetterBean bean = unmodifiableMapPreInstantiated();
      assertNotNull(bean);

      Map<?,?> result = bean.getMap();
      assertNotNull("Should be a map", result);

      Map<Object, Object> expected = new HashMap<Object, Object>();
      expected.put(string1, string2);
      expected.put(string2, string1);
      assertEquals(expected, result);
   }

   protected UnmodifiableGetterBean unmodifiableMapPreInstantiated() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();

      AbstractBeanMetaData bmd = new AbstractBeanMetaData("test1", UnmodifiableGetterBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData kmd1 = new StringValueMetaData(string1);
      StringValueMetaData kmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd1 = new StringValueMetaData(string2);
      StringValueMetaData vmd2 = new StringValueMetaData(string1);

      AbstractMapMetaData smd = new AbstractMapMetaData();
      smd.setKeyType("java.lang.String");
      smd.setValueType("java.lang.String");
      smd.put(kmd1, vmd1);
      smd.put(kmd2, vmd2);

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("map", smd);
      pmd.setPreInstantiate(false);
      properties.add(pmd);

      return (UnmodifiableGetterBean) instantiate(controller, bmd);
   }

   public void testMapWithKeyTypeOverride() throws Throwable
   {
      SimpleBean bean = mapWithKeyTypeOverride();
      assertNotNull(bean);
      
      Map<?,?> result = bean.getMap();
      assertNotNull("Should be a map", result);
      assertTrue("Not a CustomMap: " + result.getClass(), result instanceof CustomMap);
      
      Map<Object, Object> expected = new HashMap<Object, Object>();
      expected.put(string1, string2);
      expected.put(string2, string1);
      expected.put(1, integer);
      assertEquals(expected, result);
   }

   protected SimpleBean mapWithKeyTypeOverride() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData kmd1 = new StringValueMetaData(string1);
      StringValueMetaData kmd2 = new StringValueMetaData(string2);
      StringValueMetaData kmd3 = new StringValueMetaData("1");
      StringValueMetaData vmd1 = new StringValueMetaData(string2);
      StringValueMetaData vmd2 = new StringValueMetaData(string1);
      StringValueMetaData vmd3 = new StringValueMetaData(integer);
      kmd3.setConfigurator(configurator);
      kmd3.setType("java.lang.Integer");

      AbstractMapMetaData smd = new AbstractMapMetaData();
      smd.setType(CustomMap.class.getName());
      smd.setKeyType("java.lang.String");
      smd.setValueType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.put(kmd1, vmd1);
      smd.put(kmd2, vmd2);
      smd.put(kmd3, vmd3);

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("map", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testMapWithValueTypeOverride() throws Throwable
   {
      SimpleBean bean = mapWithValueTypeOverride();
      assertNotNull(bean);
      
      Map<?,?> result = bean.getMap();
      assertNotNull("Should be a map", result);
      assertTrue("Not a CustomMap: " + result.getClass(), result instanceof CustomMap);
      
      Map<Object, Object> expected = new HashMap<Object, Object>();
      expected.put(string1, string2);
      expected.put(string2, string1);
      expected.put(integer, 1);
      assertEquals(expected, result);
   }

   protected SimpleBean mapWithValueTypeOverride() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData kmd1 = new StringValueMetaData(string1);
      StringValueMetaData kmd2 = new StringValueMetaData(string2);
      StringValueMetaData kmd3 = new StringValueMetaData(integer);
      StringValueMetaData vmd1 = new StringValueMetaData(string2);
      StringValueMetaData vmd2 = new StringValueMetaData(string1);
      StringValueMetaData vmd3 = new StringValueMetaData("1");
      vmd3.setConfigurator(configurator);
      vmd3.setType("java.lang.Integer");

      AbstractMapMetaData smd = new AbstractMapMetaData();
      smd.setType(CustomMap.class.getName());
      smd.setKeyType("java.lang.String");
      smd.setValueType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.put(kmd1, vmd1);
      smd.put(kmd2, vmd2);
      smd.put(kmd3, vmd3);

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("map", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testMapNotAMap() throws Throwable
   {
      try
      {
         mapNotAMap();
         fail("Expected a failure trying to set a map on a non map attribute");
      }
      catch (Exception expected)
      {
         checkMapNotAMapException(expected);
      }
   }

   protected void checkMapNotAMapException(Exception exception)
   {
      checkThrowable(ClassCastException.class, exception);
   }
   
   protected SimpleBean mapNotAMap() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData kmd1 = new StringValueMetaData(string1);
      StringValueMetaData kmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd1 = new StringValueMetaData(string2);
      StringValueMetaData vmd2 = new StringValueMetaData(string1);

      AbstractMapMetaData smd = new AbstractMapMetaData();
      smd.setKeyType("java.lang.String");
      smd.setValueType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.put(kmd1, vmd1);
      smd.put(kmd2, vmd2);

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("AString", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testMapIsInterface() throws Throwable
   {
      try
      {
         mapIsInterface();
         fail("Expected a failure trying to use an interface for the map type");
      }
      catch (Exception expected)
      {
         checkMapIsInterfaceException(expected);
      }
   }

   protected void checkMapIsInterfaceException(Exception exception)
   {
      checkThrowable(IllegalArgumentException.class, exception);
   }

   protected SimpleBean mapIsInterface() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData kmd1 = new StringValueMetaData(string1);
      StringValueMetaData kmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd1 = new StringValueMetaData(string2);
      StringValueMetaData vmd2 = new StringValueMetaData(string1);

      AbstractMapMetaData smd = new AbstractMapMetaData();
      smd.setType("java.util.Map");
      smd.setKeyType("java.lang.String");
      smd.setValueType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.put(kmd1, vmd1);
      smd.put(kmd2, vmd2);

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("map", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }
}