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

import java.util.HashSet;
import java.util.List;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;

/**
 * ParameterTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class ParameterTestCase extends AbstractXMLTest
{
   protected ParameterMetaData getParameter(String name) throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean(name);
      ConstructorMetaData constructor = bean.getConstructor();
      assertNotNull(constructor);
      List<ParameterMetaData> parameters = constructor.getParameters();
      assertNotNull(parameters);
      assertEquals(1, parameters.size());
      ParameterMetaData parameter = parameters.get(0);
      assertNotNull(parameter);
      return parameter;
   }
   
   public void testParameter() throws Exception
   {
      ParameterMetaData parameter = getParameter("Parameter.xml");
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertNull(parameter.getValue());
   }
   
   public void testParameterWithBean() throws Exception
   {
      ParameterMetaData parameter = getParameter("ParameterWithBean.xml");
      assertNull(parameter.getAnnotations());
      assertBean(parameter.getValue());
   }
   
   public void testParameterWithClass() throws Exception
   {
      ParameterMetaData parameter = getParameter("ParameterWithClass.xml");
      assertEquals("ParameterClass", parameter.getType());
      assertNull(parameter.getAnnotations());
      assertNull(parameter.getValue());
   }

   public void testParameterWithAnnotation() throws Exception
   {
      ParameterMetaData parameter = getParameter("ParameterWithAnnotation.xml");
      assertNull(parameter.getType());
      HashSet<String> expected = new HashSet<String>();
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation1");
      assertAnnotations(expected, parameter.getAnnotations());
      assertNull(parameter.getValue());
   }
   
   public void testParameterWithAnnotations() throws Exception
   {
      ParameterMetaData parameter = getParameter("ParameterWithAnnotations.xml");
      assertNull(parameter.getType());
      HashSet<String> expected = new HashSet<String>();
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation1");
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation2");
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation3");
      assertAnnotations(expected, parameter.getAnnotations());
      assertNull(parameter.getValue());
   }
   
   public void testParameterWithPlainValue() throws Exception
   {
      ParameterMetaData parameter = getParameter("ParameterWithPlainValue.xml");
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertPlainValue("PlainValue", parameter.getValue());
   }
   
   public void testParameterWithValue() throws Exception
   {
      ParameterMetaData parameter = getParameter("ParameterWithValue.xml");
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertValue("Value", parameter.getValue());
   }
   
   public void testParameterWithInjection() throws Exception
   {
      ParameterMetaData parameter = getParameter("ParameterWithInjection.xml");
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertInjection(parameter.getValue());
   }
   
   public void testParameterWithCollection() throws Exception
   {
      ParameterMetaData parameter = getParameter("ParameterWithCollection.xml");
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertCollection(parameter.getValue());
   }
   
   public void testParameterWithList() throws Exception
   {
      ParameterMetaData parameter = getParameter("ParameterWithList.xml");
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertList(parameter.getValue());
   }
   
   public void testParameterWithSet() throws Exception
   {
      ParameterMetaData parameter = getParameter("ParameterWithSet.xml");
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertSet(parameter.getValue());
   }
   
   public void testParameterWithArray() throws Exception
   {
      ParameterMetaData parameter = getParameter("ParameterWithArray.xml");
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertArray(parameter.getValue());
   }
   
   public void testParameterWithMap() throws Exception
   {
      ParameterMetaData parameter = getParameter("ParameterWithMap.xml");
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertMap(parameter.getValue());
   }
   
   public void testParameterWithThis() throws Exception
   {
      ParameterMetaData parameter = getParameter("ParameterWithThis.xml");
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertThis(parameter.getValue());
   }
   
   public void testParameterWithWildcard() throws Exception
   {
      ParameterMetaData parameter = getParameter("ParameterWithWildcard.xml");
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertWildcard(parameter.getValue());
   }
   
   public static Test suite()
   {
      return suite(ParameterTestCase.class);
   }

   public ParameterTestCase(String name)
   {
      super(name);
   }

   protected ParameterTestCase(String name, boolean useClone)
   {
      super(name, useClone);
   }
}
