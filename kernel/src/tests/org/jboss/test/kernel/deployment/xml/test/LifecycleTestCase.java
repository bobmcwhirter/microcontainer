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

import java.util.ArrayList;
import java.util.HashSet;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.LifecycleMetaData;

/**
 * LifecycleTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class LifecycleTestCase extends AbstractXMLTest
{
   protected LifecycleMetaData getLifecycle(String name) throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean(name);
      LifecycleMetaData lifecycle = bean.getCreate();
      assertNotNull(lifecycle);
      return lifecycle;
   }

   public void testLifecycle() throws Exception
   {
      LifecycleMetaData lifecycle = getLifecycle("Lifecycle.xml");
      assertNull(lifecycle.getMethodName());
      assertNull(lifecycle.getAnnotations());
      assertNull(lifecycle.getParameters());
   }

   public void testLifecycleWithMethod() throws Exception
   {
      LifecycleMetaData lifecycle = getLifecycle("LifecycleWithMethod.xml");
      assertEquals("Method", lifecycle.getMethodName());
      assertNull(lifecycle.getAnnotations());
      assertNull(lifecycle.getParameters());
   }

   public void testLifecycleWithIgnored() throws Exception
   {
      LifecycleMetaData lifecycle = getLifecycle("LifecycleWithIgnored.xml");
      assertTrue(lifecycle.isIgnored());
      assertNull(lifecycle.getMethodName());
      assertNull(lifecycle.getAnnotations());
      assertNull(lifecycle.getParameters());
   }

   public void testLifecycleWithAnnotation() throws Exception
   {
      LifecycleMetaData lifecycle = getLifecycle("LifecycleWithAnnotation.xml");
      assertNull(lifecycle.getMethodName());
      HashSet<String> expected = new HashSet<String>();
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation1");
      assertAnnotations(expected, lifecycle.getAnnotations());
      assertNull(lifecycle.getParameters());
   }

   public void testLifecycleWithAnnotations() throws Exception
   {
      LifecycleMetaData lifecycle = getLifecycle("LifecycleWithAnnotations.xml");
      assertNull(lifecycle.getMethodName());
      HashSet<String> expected = new HashSet<String>();
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation1");
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation2");
      expected.add("org.jboss.test.kernel.deployment.xml.support.Annotation3");
      assertAnnotations(expected, lifecycle.getAnnotations());
      assertNull(lifecycle.getParameters());
   }

   public void testLifecycleWithParameter() throws Exception
   {
      LifecycleMetaData lifecycle = getLifecycle("LifecycleWithParameter.xml");
      assertNull(lifecycle.getMethodName());
      assertNull(lifecycle.getAnnotations());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Parameter1");
      assertParameters(expected, lifecycle.getParameters());
   }

   public void testLifecycleWithParameters() throws Exception
   {
      LifecycleMetaData lifecycle = getLifecycle("LifecycleWithParameters.xml");
      assertNull(lifecycle.getMethodName());
      assertNull(lifecycle.getAnnotations());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Parameter1");
      expected.add("Parameter2");
      expected.add("Parameter3");
      assertParameters(expected, lifecycle.getParameters());
   }
   
   public static Test suite()
   {
      return suite(LifecycleTestCase.class);
   }

   public LifecycleTestCase(String name)
   {
      super(name);
   }

   protected LifecycleTestCase(String name, boolean useClone)
   {
      super(name, useClone);
   }
}
