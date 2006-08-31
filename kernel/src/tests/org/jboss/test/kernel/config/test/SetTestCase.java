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

import java.util.Set;
import java.util.HashSet;

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractSetMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.test.kernel.config.support.CustomSet;
import org.jboss.test.kernel.config.support.MyObject;
import org.jboss.test.kernel.config.support.SimpleBean;

/**
 * Set Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class SetTestCase extends AbstractKernelConfigTest
{
   MyObject object1 = new MyObject("object1");
   MyObject object2 = new MyObject("object2");
   String string1 = "string1";
   String string2 = "string2";

   public static Test suite()
   {
      return suite(SetTestCase.class);
   }

   public SetTestCase(String name)
   {
      super(name);
   }

   public SetTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public void testSimpleSetFromObjects() throws Throwable
   {
      SimpleBean bean = simpleSetFromObjects();
      assertNotNull(bean);
      
      Set result = bean.getSet();
      assertNotNull("Should be a set", result);
      
      HashSet<Object> expected = new HashSet<Object>();
      expected.add(object1);
      expected.add(object2);
      expected.add(object2);
      expected.add(object1);
      assertEquals(expected, result);
   }

   public SimpleBean simpleSetFromObjects() throws Throwable
   {
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      AbstractValueMetaData vmd1 = new AbstractValueMetaData(object1);
      AbstractValueMetaData vmd2 = new AbstractValueMetaData(object2);
      AbstractValueMetaData vmd3 = new AbstractValueMetaData(object1);

      AbstractSetMetaData smd = new AbstractSetMetaData();
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("set", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(bmd);
   }

   public void testSimpleSetFromStrings() throws Throwable
   {
      SimpleBean bean = simpleSetFromStrings();
      assertNotNull(bean);
      
      Set result = bean.getSet();
      assertNotNull("Should be a set", result);
      
      HashSet<Object> expected = new HashSet<Object>();
      expected.add(string1);
      expected.add(string2);
      expected.add(string2);
      expected.add(string1);
      assertEquals(expected, result);
   }

   protected SimpleBean simpleSetFromStrings() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractSetMetaData smd = new AbstractSetMetaData();
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("set", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testCustomSetExplicit() throws Throwable
   {
      SimpleBean bean = customSetExplicit();
      assertNotNull(bean);
      
      Set result = bean.getSet();
      assertNotNull("Should be a set", result);
      assertTrue("Not a CustomSet: " + result.getClass(), result instanceof CustomSet);
      
      HashSet<Object> expected = new HashSet<Object>();
      expected.add(string1);
      expected.add(string2);
      expected.add(string2);
      expected.add(string1);
      assertEquals(expected, result);
   }

   protected SimpleBean customSetExplicit() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractSetMetaData smd = new AbstractSetMetaData();
      smd.setType(CustomSet.class.getName());
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("set", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testCustomSetFromSignature() throws Throwable
   {
      SimpleBean bean = customSetFromSignature();
      assertNotNull(bean);
      
      Set result = bean.getCustomSet();
      assertNotNull("Should be a set", result);
      assertTrue("Not a CustomSet: " + result.getClass(), result instanceof CustomSet);
      
      HashSet<Object> expected = new HashSet<Object>();
      expected.add(string1);
      expected.add(string2);
      expected.add(string2);
      expected.add(string1);
      assertEquals(expected, result);
   }

   protected SimpleBean customSetFromSignature() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractSetMetaData smd = new AbstractSetMetaData();
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("customSet", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testCustomSetPreInstantiated() throws Throwable
   {
      SimpleBean bean = customSetPreInstantiated();
      assertNotNull(bean);
      
      Set result = bean.getPreInstantiatedSet();
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

   protected SimpleBean customSetPreInstantiated() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractSetMetaData smd = new AbstractSetMetaData();
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("preInstantiatedSet", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testSetWithValueTypeOverride() throws Throwable
   {
      SimpleBean bean = setWithValueTypeOverride();
      assertNotNull(bean);
      
      Set result = bean.getSet();
      assertNotNull("Should be a set", result);
      assertTrue("Not a CustomSet: " + result.getClass(), result instanceof CustomSet);
      
      HashSet<Object> expected = new HashSet<Object>();
      expected.add(string1);
      expected.add(string2);
      expected.add(string2);
      expected.add(string1);
      expected.add(new Integer(1));
      assertEquals(expected, result);
   }

   protected SimpleBean setWithValueTypeOverride() throws Throwable
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

      AbstractSetMetaData smd = new AbstractSetMetaData();
      smd.setType(CustomSet.class.getName());
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates
      smd.add(vmd4);

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("set", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }

   public void testSetNotASet() throws Throwable
   {
      try
      {
         setNotASet();
         fail("Expected a failure trying to set a set on a non set attribute");
      }
      catch (Exception expected)
      {
         checkSetNotASetException(expected);
      }
   }

   protected void checkSetNotASetException(Exception exception)
   {
      checkThrowable(ClassCastException.class, exception);
   }
   
   protected SimpleBean setNotASet() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractSetMetaData smd = new AbstractSetMetaData();
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

   public void testSetIsInterface() throws Throwable
   {
      try
      {
         setIsInterface();
         fail("Expected a failure trying to use an interface for the set type");
      }
      catch (Exception expected)
      {
         checkSetIsInterfaceException(expected);
      }
   }

   protected void checkSetIsInterfaceException(Exception exception)
   {
      checkThrowable(IllegalArgumentException.class, exception);
   }

   protected SimpleBean setIsInterface() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      
      AbstractBeanMetaData bmd = new AbstractBeanMetaData(SimpleBean.class.getName());
      HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      bmd.setProperties(properties);

      StringValueMetaData vmd1 = new StringValueMetaData(string1);
      StringValueMetaData vmd2 = new StringValueMetaData(string2);
      StringValueMetaData vmd3 = new StringValueMetaData(string1);

      AbstractSetMetaData smd = new AbstractSetMetaData();
      smd.setType("java.util.Set");
      smd.setElementType("java.lang.String");
      smd.setConfigurator(configurator);
      smd.add(vmd1);
      smd.add(vmd2);
      smd.add(vmd2); // tests duplicates
      smd.add(vmd3); // tests duplicates

      AbstractPropertyMetaData pmd = new AbstractPropertyMetaData("set", smd);
      properties.add(pmd);
      
      return (SimpleBean) instantiateAndConfigure(configurator, bmd);
   }
}