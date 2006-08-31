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
import java.util.List;
import java.util.HashSet;

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractListMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.test.kernel.config.support.CustomList;
import org.jboss.test.kernel.config.support.MyObject;
import org.jboss.test.kernel.config.support.SimpleBean;

/**
 * List Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class ListTestCase extends AbstractKernelConfigTest
{
   MyObject object1 = new MyObject("object1");
   MyObject object2 = new MyObject("object2");
   String string1 = "string1";
   String string2 = "string2";
   
   public static Test suite()
   {
      return suite(ListTestCase.class);
   }

   public ListTestCase(String name)
   {
      super(name);
   }

   public ListTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public void testSimpleListFromObjects() throws Throwable
   {
      SimpleBean bean = simpleListFromObjects();
      assertNotNull(bean);
      
      List result = bean.getList();
      assertNotNull("Should be a list", result);
      
      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add(object1);
      expected.add(object2);
      expected.add(object2);
      expected.add(object1);
      assertEquals(expected, result);
   }

   public SimpleBean simpleListFromObjects() throws Throwable
   {
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      AbstractValueMetaData vmd1 = new AbstractValueMetaData(object1);
      AbstractValueMetaData vmd2 = new AbstractValueMetaData(object2);
      AbstractValueMetaData vmd3 = new AbstractValueMetaData(object1);

      AbstractListMetaData smd = new AbstractListMetaData();
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("list", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(bmd);
   }

   public void testSimpleListFromStrings() throws Throwable
   {
      SimpleBean bean = simpleListFromStrings();
      assertNotNull(bean);
      
      List result = bean.getList();
      assertNotNull("Should be a list", result);
      
      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add(string1);
      expected.add(string2);
      expected.add(string2);
      expected.add(string1);
      assertEquals(expected, result);
   }

   protected SimpleBean simpleListFromStrings() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractListMetaData smd = new AbstractListMetaData();
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("list", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testCustomListExplicit() throws Throwable
   {
      SimpleBean bean = customListExplicit();
      assertNotNull(bean);
      
      List result = bean.getList();
      assertNotNull("Should be a list", result);
      assertTrue("Not a CustomList: " + result.getClass(), result instanceof CustomList);
      
      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add(string1);
      expected.add(string2);
      expected.add(string2);
      expected.add(string1);
      assertEquals(expected, result);
   }

   protected SimpleBean customListExplicit() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractListMetaData smd = new AbstractListMetaData();
      smd.setType(CustomList.class.getName());
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("list", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testCustomListFromSignature() throws Throwable
   {
      SimpleBean bean = customListFromSignature();
      assertNotNull(bean);
      
      List result = bean.getCustomList();
      assertNotNull("Should be a list", result);
      assertTrue("Not a CustomList: " + result.getClass(), result instanceof CustomList);
      
      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add(string1);
      expected.add(string2);
      expected.add(string2);
      expected.add(string1);
      assertEquals(expected, result);
   }

   protected SimpleBean customListFromSignature() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractListMetaData smd = new AbstractListMetaData();
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("customList", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testCustomListPreInstantiated() throws Throwable
   {
      SimpleBean bean = customListPreInstantiated();
      assertNotNull(bean);
      
      List result = bean.getPreInstantiatedList();
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

   protected SimpleBean customListPreInstantiated() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractListMetaData smd = new AbstractListMetaData();
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("preInstantiatedList", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testListWithValueTypeOverride() throws Throwable
   {
      SimpleBean bean = listWithValueTypeOverride();
      assertNotNull(bean);
      
      List result = bean.getList();
      assertNotNull("Should be a list", result);
      assertTrue("Not a CustomList: " + result.getClass(), result instanceof CustomList);
      
      ArrayList<Object> expected = new ArrayList<Object>();
      expected.add(string1);
      expected.add(string2);
      expected.add(string2);
      expected.add(string1);
      expected.add(new Integer(1));
      assertEquals(expected, result);
   }

   protected SimpleBean listWithValueTypeOverride() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);
      StringValueMetaData vmd4 = new StringValueMetaData("1");
      vmd4.setConfigurator(configurator);
      vmd4.setType("java.lang.Integer");

      AbstractListMetaData smd = new AbstractListMetaData();
      smd.setType(CustomList.class.getName());
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates
      smd.add(vmd4);

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("list", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testListNotAList() throws Throwable
   {
      try
      {
         listNotAList();
         fail("Expected a failure trying to set a list on a non list attribute");
      }
      catch (Exception expected)
      {
         checkListNotAListException(expected);
      }
   }

   protected void checkListNotAListException(Exception exception)
   {
      checkThrowable(ClassCastException.class, exception);
   }
   
   protected SimpleBean listNotAList() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractListMetaData smd = new AbstractListMetaData();
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("AString", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testListIsInterface() throws Throwable
   {
      try
      {
         listIsInterface();
         fail("Expected a failure trying to use an interface for the list type");
      }
      catch (Exception expected)
      {
         checkListIsInterfaceException(expected);
      }
   }

   protected void checkListIsInterfaceException(Exception exception)
   {
      checkThrowable(IllegalArgumentException.class, exception);
   }

   protected SimpleBean listIsInterface() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractListMetaData smd = new AbstractListMetaData();
      smd.setType("java.util.List");
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("list", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }
}