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

import java.io.Serializable;
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
import org.jboss.kernel.plugins.deployment.AbstractKernelDeployment;
import org.jboss.test.AbstractTestCaseWithSetup;
import org.jboss.test.AbstractTestDelegate;
import org.jboss.xb.binding.JBossXBException;

/**
 * AbstractXMLTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractXMLTest extends AbstractTestCaseWithSetup
{
   protected String rootName = getRootName();
   
   /**
    * Create a new AbstractXMLTest.
    * 
    * @param name the name of the test
    */
   public AbstractXMLTest(String name)
   {
      super(name);
   }

   /**
    * Unmarshal a deployent
    * 
    * @param name the name
    * @return the unmarshalled object
    * @throws Exception for any error
    */
   protected AbstractKernelDeployment unmarshalDeployment(String name) throws Exception
   {
      return unmarshal(name, AbstractKernelDeployment.class);
   }

   /**
    * Unmarshal a bean
    * 
    * @param name the name
    * @return the unmarshalled object
    * @throws Exception for any error
    */
   protected AbstractBeanMetaData unmarshalBean(String name) throws Exception
   {
      return unmarshal(name, AbstractBeanMetaData.class);
   }

   /**
    * Unmarshal a bean factory
    * 
    * @param name the name
    * @return the unmarshalled object
    * @throws Exception for any error
    */
   protected GenericBeanFactoryMetaData unmarshalBeanFactory(String name) throws Exception
   {
      return unmarshal(name, GenericBeanFactoryMetaData.class);
   }
   
   /**
    * Unmarshal some xml
    * 
    * @param <T> the expected type
    * @param name the name
    * @param expected the expected class
    * @return the unmarshalled object
    * @throws Exception for any error
    */
   protected <T> T unmarshal(String name, Class<T> expected) throws Exception
   {
      String url = findXML(name);
      Object object = getJBossXBDelegate().unmarshal(url);
      if (object == null)
         fail("No object from " + name);

      Serializable serializable = assertInstanceOf(object, Serializable.class, false);

      // Test that serialize/deserialize works accurately reproduces the object
      object = deserialize(serialize(serializable));

      assertTrue("Object '" + object + "' cannot be assigned to " + expected.getName(), expected.isAssignableFrom(object.getClass()));
      return expected.cast(object);
   }
   
   /**
    * Find the xml
    * 
    * @param name the name
    * @return the url of the xml
    */
   protected String findXML(String name)
   {
      URL url = getResource(name);
      if (url == null)
         fail(name + " not found");
      return url.toString();
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
   
   protected void assertBeanFactoryProperties(Set<String> expected, GenericBeanFactoryMetaData factory)
   {
      assertNotNull(factory);
      PropertyMetaData propertiesProperty = factory.getProperty("properties");
      assertNotNull(propertiesProperty);
      AbstractMapMetaData map = (AbstractMapMetaData) propertiesProperty.getValue();
      assertNotNull(map);
      Set properties = map.keySet();
      assertEquals(expected.size(), properties.size());
      HashSet<String> clonedExpected = new HashSet<String>(expected);
      for (Iterator i = properties.iterator(); i.hasNext();)
      {
         AbstractValueMetaData property = (AbstractValueMetaData) i.next();
         if (clonedExpected.remove(property.getUnderlyingValue()) == false)
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
   
   protected void checkJBossXBException(Class<? extends Throwable> expected, Throwable throwable)
   {
      checkThrowable(JBossXBException.class, throwable);
      JBossXBException e = (JBossXBException) throwable;
      checkThrowable(expected, e.getCause());
   }

   /**
    * Setup the test delegate
    * 
    * @param clazz the class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class clazz) throws Exception
   {
      return new XMLTestDelegate(clazz);
   }

   protected XMLTestDelegate getJBossXBDelegate()
   {
      return (XMLTestDelegate) getDelegate();
   }
   
   protected void setUp() throws Exception
   {
      super.setUp();
      configureLogging();
   }
   
   /**
    * Get the package root name
    * 
    * @return the root name
    */
   protected String getRootName()
   {
      String longName = getClass().getName();
      int dot = longName.lastIndexOf('.');
      if (dot != -1)
         return longName.substring(dot + 1);
      return longName;
   }
   
   protected void configureLogging()
   {
      //enableTrace("org.jboss.xb");
   }
}
