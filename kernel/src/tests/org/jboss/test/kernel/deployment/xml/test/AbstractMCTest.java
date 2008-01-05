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
package org.jboss.test.kernel.deployment.xml.test;

import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jboss.beans.metadata.plugins.AbstractArrayMetaData;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractCollectionMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractListMetaData;
import org.jboss.beans.metadata.plugins.AbstractMapMetaData;
import org.jboss.beans.metadata.plugins.AbstractSetMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.beans.metadata.plugins.ThisValueMetaData;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData;
import org.jboss.beans.metadata.spi.AnnotationMetaData;
import org.jboss.beans.metadata.spi.CallbackMetaData;
import org.jboss.beans.metadata.spi.DemandMetaData;
import org.jboss.beans.metadata.spi.DependencyMetaData;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.SupplyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.javabean.plugins.jaxb.JavaBean;
import org.jboss.kernel.plugins.deployment.AbstractKernelDeployment;
import org.jboss.test.xb.builder.AbstractBuilderTest;
import org.jboss.xb.binding.JBossXBException;

/**
 * AbstractMCTest.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class AbstractMCTest extends AbstractBuilderTest
{
   public AbstractMCTest(String name)
   {
      super(name);
   }

   protected AbstractKernelDeployment unmarshalDeployment() throws Exception
   {
      return unmarshalObject(AbstractKernelDeployment.class, AbstractKernelDeployment.class, JavaBean.class);
   }

   protected AbstractBeanMetaData unmarshalBean() throws Exception
   {
      return unmarshalObject(AbstractBeanMetaData.class, AbstractKernelDeployment.class, JavaBean.class);
   }

   protected GenericBeanFactoryMetaData unmarshalBeanFactory() throws Exception
   {
      return unmarshalObject(GenericBeanFactoryMetaData.class, AbstractKernelDeployment.class, JavaBean.class);
   }

   protected void assertAnnotations(Set<String> expected, Set<AnnotationMetaData> annotations)
   {
      assertNotNull(annotations);
      assertEquals(expected.size(), annotations.size());
      HashSet<String> clonedExpected = new HashSet<String>(expected);
      for (Iterator i = annotations.iterator(); i.hasNext();)
      {
         AnnotationMetaData annotation = (AnnotationMetaData) i.next();
         if (clonedExpected.remove(annotation.getAnnotationInstance().annotationType().getName()) == false)
            fail("Did not expect " + annotation + " expected " + expected);
      }
      if (clonedExpected.size() != 0)
         fail("Expected " + expected + " got " + annotations);
   }

   protected void assertProperties(Set<String> expected, Set<PropertyMetaData> properties)
   {
      assertNotNull(properties);
      assertEquals(expected.size(), properties.size());
      HashSet<String> clonedExpected = new HashSet<String>(expected);
      for (Iterator i = properties.iterator(); i.hasNext();)
      {
         PropertyMetaData property = (PropertyMetaData) i.next();
         if (clonedExpected.remove(property.getName()) == false)
            fail("Did not expect " + property + " expected " + expected);
      }
      if (clonedExpected.size() != 0)
         fail("Expected " + expected + " got " + properties);
   }

   protected void assertDepends(Set<String> expected, Set<DependencyMetaData> depends)
   {
      assertNotNull(depends);
      assertEquals(expected.size(), depends.size());
      HashSet<String> clonedExpected = new HashSet<String>(expected);
      for (DependencyMetaData depend : depends)
      {
         if (clonedExpected.remove(depend.getDependency()) == false)
            fail("Did not expect " + depend + " expected " + expected);
      }
      if (clonedExpected.size() != 0)
         fail("Expected " + expected + " got " + depends);
   }

   protected void assertDemands(Set<String> expected, Set<DemandMetaData> demands)
   {
      assertNotNull(demands);
      assertEquals(expected.size(), demands.size());
      HashSet<String> clonedExpected = new HashSet<String>(expected);
      for (DemandMetaData demand : demands)
      {
         if (clonedExpected.remove(demand.getDemand()) == false)
            fail("Did not expect " + demand + " expected " + expected);
      }
      if (clonedExpected.size() != 0)
         fail("Expected " + expected + " got " + demands);
   }

   protected void assertSupplies(Set<String> expected, Set<SupplyMetaData> supplies)
   {
      assertNotNull(supplies);
      assertEquals(expected.size(), supplies.size());
      HashSet<String> clonedExpected = new HashSet<String>(expected);
      for (Iterator i = supplies.iterator(); i.hasNext();)
      {
         SupplyMetaData supply = (SupplyMetaData) i.next();
         if (clonedExpected.remove(supply.getSupply()) == false)
            fail("Did not expect " + supply + " expected " + expected);
      }
      if (clonedExpected.size() != 0)
         fail("Expected " + expected + " got " + supplies);
   }

   protected void assertInstalls(List expected, List installs)
   {
      assertNotNull(installs);
      assertEquals(expected.size(), installs.size());
      for (int i = 0; i < expected.size(); ++i)
      {
         InstallMetaData install = (InstallMetaData) installs.get(i);
         String method = (String) expected.get(i);
         assertEquals(method, install.getMethodName());
      }
   }

   protected void assertCallbacks(List expected, List callbacks)
   {
      assertNotNull(callbacks);
      assertEquals(expected.size(), callbacks.size());
      for (int i = 0; i < expected.size(); ++i)
      {
         CallbackMetaData callback = (CallbackMetaData) callbacks.get(i);
         String method = (String) expected.get(i);
         assertEquals(method, callback.getMethodName());
      }
   }

   protected void assertParameters(List expected, List parameters)
   {
      assertNotNull(parameters);
      assertEquals(expected.size(), parameters.size());
      for (int i = 0; i < expected.size(); ++i)
      {
         ParameterMetaData parameter = (ParameterMetaData) parameters.get(i);
         String method = (String) expected.get(i);
         assertEquals(method, parameter.getType());
      }
   }

   protected void assertPlainValue(String expected, ValueMetaData valueMetaData)
   {
      assertValue(expected, valueMetaData);
   }

   protected void assertValue(String expected, ValueMetaData valueMetaData)
   {
      assertNotNull(valueMetaData);
      assertTrue(valueMetaData instanceof StringValueMetaData);
      Object value = valueMetaData.getUnderlyingValue();
      assertEquals(expected, value);
   }

   protected void assertInjection(ValueMetaData value)
   {
      assertNotNull(value);
      assertTrue(value instanceof AbstractDependencyValueMetaData);
   }

   protected void assertNullValue(ValueMetaData value)
   {
      assertNotNull(value);
      assertTrue(value instanceof AbstractValueMetaData);
      assertNull(value.getUnderlyingValue());
   }

   protected void assertWildcard(ValueMetaData value)
   {
      assertNotNull(value);
      assertEquals(AbstractValueMetaData.class, value.getClass());
      Object wildcard = value.getUnderlyingValue();
      assertNotNull(wildcard);
   }

   protected void assertCollection(ValueMetaData value)
   {
      assertNotNull(value);
      assertTrue(value instanceof AbstractCollectionMetaData);
   }

   protected void assertList(ValueMetaData value)
   {
      assertNotNull(value);
      assertTrue(value instanceof AbstractListMetaData);
   }

   protected void assertSet(ValueMetaData value)
   {
      assertNotNull(value);
      assertTrue(value instanceof AbstractSetMetaData);
   }

   protected void assertArray(ValueMetaData value)
   {
      assertNotNull(value);
      assertTrue(value instanceof AbstractArrayMetaData);
   }

   protected void assertMap(ValueMetaData value)
   {
      assertNotNull(value);
      assertTrue(value instanceof AbstractMapMetaData);
   }

   protected void assertThis(ValueMetaData value)
   {
      assertNotNull(value);
      assertTrue(value instanceof ThisValueMetaData);
   }

   protected void checkJBossXBException(Class expected, Throwable throwable)
   {
      checkThrowable(JBossXBException.class, throwable);
      JBossXBException e = (JBossXBException) throwable;
      checkThrowable(expected, e.getCause());
   }

   /**
    * Find the xml
    *
    * @param name the name
    * @return the url of the xml
    */
   protected String findXML(String name)
   {
      name = getName().substring(4) + ".xml";

      URL url = getClass().getResource(name);
      if (url == null)
         fail(name + " not found");
      return url.toString();
   }
}
