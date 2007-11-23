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
package org.jboss.test.microcontainer.xml.test;

import java.util.List;

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;

/**
 * AspectTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AspectTestCase extends AbstractAOPXMLTest
{
   public void testDeployment() throws Exception
   {
      List beans = unmarshalBeans("Aspect.xml", 2);
      
      GenericBeanFactoryMetaData adviceFactory = (GenericBeanFactoryMetaData) assertType(beans, 0, GenericBeanFactoryMetaData.class);
      assertEquals("Factory$TestAspect", adviceFactory.getName());
      assertEquals("TestClass", adviceFactory.getBeanClass());
      ValueMetaData vmd = adviceFactory.getBeanProperty("testProperty");
      assertNotNull(vmd);
      assertEquals("Hello", vmd.getUnderlyingValue());
      
      BeanMetaData aspect = (BeanMetaData) assertType(beans, 1, BeanMetaData.class);
      assertEquals("TestAspect", aspect.getName());
   }
   
   public static Test suite()
   {
      return suite(AspectTestCase.class);
   }

   public AspectTestCase(String name)
   {
      super(name);
   }
}
