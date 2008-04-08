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

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.dependency.spi.ControllerState;

/**
 * FactoryTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class FactoryTestCase extends AbstractXMLTest
{
   protected AbstractValueMetaData getFactory(String name) throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean(name);
      ConstructorMetaData constructor = bean.getConstructor();
      assertNotNull(constructor);
      ValueMetaData factory = constructor.getFactory();
      assertNotNull(factory);
      assertTrue(factory instanceof AbstractValueMetaData);
      return (AbstractValueMetaData) factory;
   }

   protected AbstractDependencyValueMetaData getFactoryDependency(String name) throws Exception
   {
      return (AbstractDependencyValueMetaData) getFactory(name);
   }

   public void testFactoryWithBean() throws Exception
   {
      AbstractDependencyValueMetaData dependency = getFactoryDependency("FactoryWithBean.xml");
      assertEquals("Bean1", dependency.getValue());
      assertNull(dependency.getProperty());
      assertEquals(ControllerState.INSTALLED, dependency.getDependentState());
   }

   public void testFactoryWithProperty() throws Exception
   {
      AbstractDependencyValueMetaData dependency = getFactoryDependency("FactoryWithProperty.xml");
      assertEquals("Dummy", dependency.getValue());
      assertEquals("Property1", dependency.getProperty());
      assertEquals(ControllerState.INSTALLED, dependency.getDependentState());
   }

   public void testFactoryWithState() throws Exception
   {
      AbstractDependencyValueMetaData dependency = getFactoryDependency("FactoryWithState.xml");
      assertEquals("Dummy", dependency.getValue());
      assertNull(dependency.getProperty());
      assertEquals(ControllerState.CONFIGURED, dependency.getDependentState());
   }

   public void testFactoryWithWildcard() throws Exception
   {
      assertWildcard(getFactory("FactoryWithWildcard.xml"));
   }

   public static Test suite()
   {
      return suite(FactoryTestCase.class);
   }

   public FactoryTestCase(String name)
   {
      super(name);
   }

   protected FactoryTestCase(String name, boolean useClone)
   {
      super(name, useClone);
   }
}
