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

import java.util.HashSet;

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractArrayMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.test.kernel.config.support.MyObject;
import org.jboss.test.kernel.config.support.SimpleBean;

/**
 * Array Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class ArrayTestCase extends AbstractKernelConfigTest
{
   MyObject object1 = new MyObject("object1");
   MyObject object2 = new MyObject("object2");
   String string1 = "string1";
   String string2 = "string2";

   public static Test suite()
   {
      return suite(ArrayTestCase.class);
   }
   
   public ArrayTestCase(String name)
   {
      super(name);
   }

   public ArrayTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public void testSimpleArrayFromObjects() throws Throwable
   {
      SimpleBean bean = simpleArrayFromObjects();
      assertNotNull(bean);
      
      Object[] result = bean.getArray();
      assertNotNull("Should be a array", result);
      
      Object[] expected = new Object[] { object1, object2, object2, object1 };
      assertEquals(expected, result);
   }
   
   protected SimpleBean simpleArrayFromObjects() throws Throwable
   {
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      AbstractValueMetaData vmd1 = new AbstractValueMetaData(object1);
      AbstractValueMetaData vmd2 = new AbstractValueMetaData(object2);
      AbstractValueMetaData vmd3 = new AbstractValueMetaData(object1);

      AbstractArrayMetaData smd = new AbstractArrayMetaData();
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("array", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(bmd);
   }

   public void testSimpleArrayFromStrings() throws Throwable
   {
      SimpleBean bean = simpleArrayFromStrings();
      assertNotNull(bean);
      
      Object[] result = bean.getArray();
      assertNotNull("Should be a array", result);
      
      Object[] expected = new Object[] { string1, string2, string2, string1 };
      assertEquals(expected, result);
   }

   protected SimpleBean simpleArrayFromStrings() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractArrayMetaData smd = new AbstractArrayMetaData();
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("array", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testCustomArrayExplicit() throws Throwable
   {
      SimpleBean bean = customArrayExplicit();
      assertNotNull(bean);
      
      Object[] result = bean.getArray();
      assertNotNull("Should be a array", result);
      assertTrue("Not a String[]: " + result.getClass(), result instanceof String[]);
      
      Object[] expected = new Object[] { string1, string2, string2, string1 };
      assertEquals(expected, result);
   }

   protected SimpleBean customArrayExplicit() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractArrayMetaData smd = new AbstractArrayMetaData();
      smd.setType(new String[0].getClass().getName());
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("array", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testCustomArrayFromSignature() throws Throwable
   {
      SimpleBean bean = customArrayFromSignature();
      assertNotNull(bean);
      
      String[] result = bean.getCustomArray();
      assertNotNull("Should be a array", result);
      
      Object[] expected = new Object[] { string1, string2, string2, string1 };
      assertEquals(expected, result);
   }

   protected SimpleBean customArrayFromSignature() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractArrayMetaData smd = new AbstractArrayMetaData();
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("customArray", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testArrayWithValueTypeOverride() throws Throwable
   {
      SimpleBean bean = arrayWithValueTypeOverride();
      assertNotNull(bean);
      
      Object[] result = bean.getArray();
      assertNotNull("Should be a array", result);
      
      Object[] expected = new Object[] { string1, string2, string2, string1, new Integer(1) };
      assertEquals(expected, result);
   }

   protected SimpleBean arrayWithValueTypeOverride() throws Throwable
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

      AbstractArrayMetaData smd = new AbstractArrayMetaData();
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates
      smd.add(vmd4);

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("array", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testArrayNotAArray() throws Throwable
   {
      try
      {
         arrayNotAArray();
         fail("Expected a failure trying to set a array on a non array attribute");
      }
      catch (Exception expected)
      {
         checkArrayNotAArrayException(expected);
      }
   }

   protected void checkArrayNotAArrayException(Exception exception)
   {
      checkThrowable(ClassCastException.class, exception);
   }
   
   protected SimpleBean arrayNotAArray() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractArrayMetaData smd = new AbstractArrayMetaData();
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

   public void testArrayIsInterface() throws Throwable
   {
      SimpleBean bean = arrayIsInterface();
      assertNotNull(bean);
      
      Object[] result = bean.getArray();
      assertNotNull("Should be a array", result);
      assertTrue("Not a Comparable[]: " + result.getClass(), result instanceof Comparable[]);
      
      Object[] expected = new Object[] { string1, string2, string2, string1 };
      assertEquals(expected, result);
   }

   protected SimpleBean arrayIsInterface() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractArrayMetaData smd = new AbstractArrayMetaData();
      smd.setType(new Comparable[0].getClass().getName());
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("array", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }
}