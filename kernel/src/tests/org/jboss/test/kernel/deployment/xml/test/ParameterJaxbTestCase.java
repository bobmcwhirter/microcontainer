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
import org.jboss.test.kernel.deployment.xml.support.Annotation1;
import org.jboss.test.kernel.deployment.xml.support.Annotation2;
import org.jboss.test.kernel.deployment.xml.support.Annotation3;

/**
 * ParameterJaxbTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 56471 $
 */
public class ParameterJaxbTestCase extends AbstractMCTest
{
   protected ParameterMetaData getParameter() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
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
      ParameterMetaData parameter = getParameter();
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertNull(parameter.getValue());
   }
   
   public void testParameterWithBean() throws Exception
   {
      ParameterMetaData parameter = getParameter();
      assertNull(parameter.getAnnotations());
      assertBean(parameter.getValue());
   }
   
   public void testParameterWithClass() throws Exception
   {
      ParameterMetaData parameter = getParameter();
      assertEquals("ParameterClass", parameter.getType());
      assertNull(parameter.getAnnotations());
      assertNull(parameter.getValue());
   }

   public void testParameterWithAnnotation() throws Exception
   {
      ParameterMetaData parameter = getParameter();
      assertNull(parameter.getType());
      HashSet<String> expected = new HashSet<String>();
      expected.add(Annotation1.class.getName());
      assertAnnotations(expected, parameter.getAnnotations());
      assertNull(parameter.getValue());
   }
   
   public void testParameterWithAnnotations() throws Exception
   {
      ParameterMetaData parameter = getParameter();
      assertNull(parameter.getType());
      HashSet<String> expected = new HashSet<String>();
      expected.add(Annotation1.class.getName());
      expected.add(Annotation2.class.getName());
      expected.add(Annotation3.class.getName());
      assertAnnotations(expected, parameter.getAnnotations());
      assertNull(parameter.getValue());
   }
   
   public void testParameterWithPlainValue() throws Exception
   {
      ParameterMetaData parameter = getParameter();
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertPlainValue("PlainValue", parameter.getValue());
   }
   
   public void testParameterWithValue() throws Exception
   {
      ParameterMetaData parameter = getParameter();
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertValue("Value", parameter.getValue());
   }
   
   public void testParameterWithInjection() throws Exception
   {
      ParameterMetaData parameter = getParameter();
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertInjection(parameter.getValue());
   }
   
   public void testParameterWithCollection() throws Exception
   {
      ParameterMetaData parameter = getParameter();
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertCollection(parameter.getValue());
   }
   
   public void testParameterWithList() throws Exception
   {
      ParameterMetaData parameter = getParameter();
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertList(parameter.getValue());
   }
   
   public void testParameterWithSet() throws Exception
   {
      ParameterMetaData parameter = getParameter();
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertSet(parameter.getValue());
   }
   
   public void testParameterWithArray() throws Exception
   {
      ParameterMetaData parameter = getParameter();
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertArray(parameter.getValue());
   }
   
   public void testParameterWithMap() throws Exception
   {
      ParameterMetaData parameter = getParameter();
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertMap(parameter.getValue());
   }
   
   public void testParameterWithThis() throws Exception
   {
      ParameterMetaData parameter = getParameter();
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertThis(parameter.getValue());
   }
   
   public void testParameterWithWildcard() throws Exception
   {
      ParameterMetaData parameter = getParameter();
      assertNull(parameter.getType());
      assertNull(parameter.getAnnotations());
      assertWildcard(parameter.getValue());
   }
   
   public static Test suite()
   {
      return ParameterJaxbTestCase.suite(ParameterJaxbTestCase.class);
   }

   public ParameterJaxbTestCase(String name)
   {
      super(name);
   }
}
