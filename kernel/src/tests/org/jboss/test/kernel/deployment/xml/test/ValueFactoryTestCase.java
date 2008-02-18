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

import java.util.Set;
import java.util.List;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueFactoryMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.dependency.spi.ControllerState;

/**
 * ValueFactoryTestCase.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class ValueFactoryTestCase extends AbstractXMLTest
{
   protected AbstractValueFactoryMetaData getValueFactory(String name) throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean(name);
      Set<PropertyMetaData> properties = bean.getProperties();
      assertNotNull(properties);
      assertEquals(1, properties.size());
      PropertyMetaData property = properties.iterator().next();
      assertNotNull(property);
      ValueMetaData value = property.getValue();
      assertNotNull(value);
      assertTrue(value instanceof AbstractValueFactoryMetaData);
      return (AbstractValueFactoryMetaData) value;
   }

   public void testValueFactoryWithBean() throws Exception
   {
      AbstractValueFactoryMetaData dependency = getValueFactory("ValueFactoryWithBean.xml");
      assertEquals("Dummy", dependency.getValue());
      assertEquals("getValue", dependency.getMethod());
      assertNull(dependency.getDependentState());
   }

   public void testValueFactoryWithParameter() throws Exception
   {
      AbstractValueFactoryMetaData dependency = getValueFactory("ValueFactoryWithParameter.xml");
      assertEquals("Dummy", dependency.getValue());
      assertEquals("getValue", dependency.getMethod());
      List<ParameterMetaData> parameters = dependency.getParameters();
      assertNotNull(parameters);
      assertFalse(parameters.isEmpty());
      assertTrue(parameters.size() == 1);
      ParameterMetaData pmd = parameters.get(0);
      assertNotNull(pmd);
      ValueMetaData value = pmd.getValue();
      assertNotNull(value);
      assertEquals("foo.bar.key", value.getUnderlyingValue());
      assertNull(dependency.getDependentState());
   }

   public void testValueFactoryWithParameters() throws Exception
   {
      AbstractValueFactoryMetaData dependency = getValueFactory("ValueFactoryWithParameters.xml");
      assertEquals("Dummy", dependency.getValue());
      assertEquals("getValue", dependency.getMethod());
      List<ParameterMetaData> parameters = dependency.getParameters();
      assertNotNull(parameters);
      assertFalse(parameters.isEmpty());
      assertTrue(parameters.size() == 2);
      ParameterMetaData pmd1 = parameters.get(0);
      assertNotNull(pmd1);
      ValueMetaData value1 = pmd1.getValue();
      assertNotNull(value1);
      assertEquals("foo.bar.key", value1.getUnderlyingValue());
      ParameterMetaData pmd2 = parameters.get(1);
      assertNotNull(pmd2);
      ValueMetaData value2 = pmd2.getValue();
      assertNotNull(value2);
      assertEquals("mydefault", value2.getUnderlyingValue());
      assertNull(dependency.getDependentState());
   }

   public void testValueFactoryWithDefault() throws Exception
   {
      AbstractValueFactoryMetaData dependency = getValueFactory("ValueFactoryWithDefault.xml");
      assertEquals("Dummy", dependency.getValue());
      assertEquals("getValue", dependency.getMethod());
      assertEquals("mydefault", dependency.getDefaultValue());
      assertNull(dependency.getDependentState());
   }

   public void testValueFactoryWithState() throws Exception
   {
      AbstractValueFactoryMetaData dependency = getValueFactory("ValueFactoryWithState.xml");
      assertEquals("Dummy", dependency.getValue());
      assertEquals("getValue", dependency.getMethod());
      assertEquals(ControllerState.CONFIGURED, dependency.getDependentState());
   }

   public void testValueFactoryWithWhenRequired() throws Exception
   {
      AbstractValueFactoryMetaData dependency = getValueFactory("ValueFactoryWithWhenRequired.xml");
      assertEquals("Dummy", dependency.getValue());
      assertEquals("getValue", dependency.getMethod());
      assertEquals(ControllerState.CREATE, dependency.getWhenRequiredState());
   }

   public void testValueFactoryBadNoBean() throws Exception
   {
      try
      {
         AbstractValueFactoryMetaData dependency = getValueFactory("ValueFactoryBadNoBean.xml");
         assertNull(dependency.getValue());
      }
      catch (Exception expected)
      {
         checkJBossXBException(IllegalArgumentException.class, expected);
      }
   }

   public void testValueFactoryBadNoMethod() throws Exception
   {
      try
      {
         AbstractValueFactoryMetaData dependency = getValueFactory("ValueFactoryBadNoMethod.xml");
         assertNull(dependency.getValue());
      }
      catch (Exception expected)
      {
         checkJBossXBException(IllegalArgumentException.class, expected);
      }
   }

   public static Test suite()
   {
      return suite(ValueFactoryTestCase.class);
   }

   public ValueFactoryTestCase(String name)
   {
      super(name);
   }
}
