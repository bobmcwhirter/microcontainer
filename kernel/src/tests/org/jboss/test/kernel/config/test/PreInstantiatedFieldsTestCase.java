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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractCollectionMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractListMetaData;
import org.jboss.beans.metadata.plugins.AbstractMapMetaData;
import org.jboss.beans.metadata.plugins.AbstractSetMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.test.kernel.config.support.CustomCollection;
import org.jboss.test.kernel.config.support.SimplerBean;
import org.jboss.test.kernel.config.support.CustomList;
import org.jboss.test.kernel.config.support.CustomMap;
import org.jboss.test.kernel.config.support.CustomSet;
import org.jboss.test.AbstractTestDelegate;

/**
 * Preinstantiated fields test cases.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class PreInstantiatedFieldsTestCase extends AbstractKernelConfigTest
{
   String string1 = "string1";
   String string2 = "string2";

   public PreInstantiatedFieldsTestCase(String name)
   {
      super(name);
   }

   public PreInstantiatedFieldsTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(PreInstantiatedFieldsTestCase.class);
   }

   /**
    * Default setup with security manager enabled
    *
    * @param clazz the class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      AbstractTestDelegate delegate = new AbstractTestDelegate(clazz);
      delegate.enableSecurity = false;
      return delegate;
   }

   public void testCustomCollectionPreInstantiated2() throws Throwable
   {
      SimplerBean bean = customCollectionPreInstantiated();
      assertNotNull(bean);

      Collection<?> result = bean.getPreInstantiatedCollection();
      assertNotNull("Should be a collection", result);
      assertTrue("Not a CustomCollection: " + result.getClass(), result instanceof CustomCollection);
      assertTrue("Not preinstantiated", ((CustomCollection) result).getPreInstantiated());

      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add(string1);
      expected.add(string2);
      expected.add(string2);
      expected.add(string1);
      assertEquals(expected, result);
   }

   protected SimplerBean customCollectionPreInstantiated() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();

      AbstractBeanMetaData bmd = new AbstractBeanMetaData("test1", SimplerBean.class.getName());
      bmd.setAccessMode(BeanAccessMode.ALL);
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractCollectionMetaData smd = new AbstractCollectionMetaData();
      smd.setElementType("java.lang.String");
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("preInstantiatedCollection", smd);
      properties.add(pmd);

      return (SimplerBean) instantiate(controller, bmd);
   }

   public void testCustomListPreInstantiated2() throws Throwable
   {
      SimplerBean bean = customListPreInstantiated();
      assertNotNull(bean);

      List<?> result = bean.getPreInstantiatedList();
      assertNotNull("Should be a list", result);
      assertTrue("Not a CustomList: " + result.getClass(), result instanceof CustomList);
      assertTrue("Not preinstantiated", ((CustomList) result).getPreInstantiated());

      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add(string1);
      expected.add(string2);
      expected.add(string2);
      expected.add(string1);
      assertEquals(expected, result);
   }

   protected SimplerBean customListPreInstantiated() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();

      AbstractBeanMetaData bmd = new AbstractBeanMetaData("test1", SimplerBean.class.getName());
      bmd.setAccessMode(BeanAccessMode.ALL);
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractListMetaData smd = new AbstractListMetaData();
      smd.setElementType("java.lang.String");
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd1 = new AbstractPropertyMetaData("preInstantiatedList", smd);
      properties.add(pmd1);

      return (SimplerBean) instantiate(controller, bmd);
   }

   public void testCustomMapPreInstantiated2() throws Throwable
   {
      SimplerBean bean = customMapPreInstantiated();
      assertNotNull(bean);

      Map<?,?> result = bean.getPreInstantiatedMap();
      assertNotNull("Should be a map", result);
      assertTrue("Not a CustomMap: " + result.getClass(), result instanceof CustomMap);
      assertTrue("Not preinstantiated", ((CustomMap) result).getPreInstantiated());

      Map<Object, Object> expected = new HashMap<Object, Object>();
      expected.put(string1, string2);
      expected.put(string2, string1);
      assertEquals(expected, result);
   }

   protected SimplerBean customMapPreInstantiated() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();

      AbstractBeanMetaData bmd = new AbstractBeanMetaData("test1", SimplerBean.class.getName());
      bmd.setAccessMode(BeanAccessMode.ALL);
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

      return (SimplerBean) instantiate(controller, bmd);
   }

   public void testCustomSetPreInstantiated2() throws Throwable
   {
      SimplerBean bean = customSetPreInstantiated();
      assertNotNull(bean);

      Set<?> result = bean.getPreInstantiatedSet();
      assertNotNull("Should be a set", result);
      assertTrue("Not a CustomSet: " + result.getClass(), result instanceof CustomSet);
      assertTrue("Not preinstantiated", ((CustomSet) result).getPreInstantiated());

      HashSet<Object> expected = new HashSet<Object>();
      expected.add(string1);
      expected.add(string2);
      expected.add(string2);
      expected.add(string1);
      assertEquals(expected, result);
   }

   protected SimplerBean customSetPreInstantiated() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();

      AbstractBeanMetaData bmd = new AbstractBeanMetaData("test1", SimplerBean.class.getName());
      bmd.setAccessMode(BeanAccessMode.ALL);
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractSetMetaData smd = new AbstractSetMetaData();
      smd.setElementType("java.lang.String");
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("preInstantiatedSet", smd);
      properties.add(pmd);

      return (SimplerBean) instantiate(controller, bmd);
   }
}
