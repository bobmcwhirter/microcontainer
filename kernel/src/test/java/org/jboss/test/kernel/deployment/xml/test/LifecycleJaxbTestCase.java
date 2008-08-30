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
import org.jboss.test.kernel.deployment.xml.support.Annotation1;
import org.jboss.test.kernel.deployment.xml.support.Annotation2;
import org.jboss.test.kernel.deployment.xml.support.Annotation3;

/**
 * LifecycleJaxbTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 63037 $
 */
public class LifecycleJaxbTestCase extends AbstractMCTest
{
   protected LifecycleMetaData getLifecycle() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      LifecycleMetaData lifecycle = bean.getCreate();
      assertNotNull(lifecycle);
      return lifecycle;
   }

   public void testLifecycle() throws Exception
   {
      LifecycleMetaData lifecycle = getLifecycle();
      assertNull(lifecycle.getMethodName());
      assertNull(lifecycle.getAnnotations());
      assertNull(lifecycle.getParameters());
   }

   public void testLifecycleWithMethod() throws Exception
   {
      LifecycleMetaData lifecycle = getLifecycle();
      assertEquals("Method", lifecycle.getMethodName());
      assertNull(lifecycle.getAnnotations());
      assertNull(lifecycle.getParameters());
   }

   public void testLifecycleWithIgnored() throws Exception
   {
      LifecycleMetaData lifecycle = getLifecycle();
      assertTrue(lifecycle.isIgnored());
      assertNull(lifecycle.getMethodName());
      assertNull(lifecycle.getAnnotations());
      assertNull(lifecycle.getParameters());
   }

   public void testLifecycleWithAnnotation() throws Exception
   {
      LifecycleMetaData lifecycle = getLifecycle();
      assertNull(lifecycle.getMethodName());
      HashSet<String> expected = new HashSet<String>();
      expected.add(Annotation1.class.getName());
      assertAnnotations(expected, lifecycle.getAnnotations());
      assertNull(lifecycle.getParameters());
   }

   public void testLifecycleWithAnnotations() throws Exception
   {
      LifecycleMetaData lifecycle = getLifecycle();
      assertNull(lifecycle.getMethodName());
      HashSet<String> expected = new HashSet<String>();
      expected.add(Annotation1.class.getName());
      expected.add(Annotation2.class.getName());
      expected.add(Annotation3.class.getName());
      assertAnnotations(expected, lifecycle.getAnnotations());
      assertNull(lifecycle.getParameters());
   }

   public void testLifecycleWithParameter() throws Exception
   {
      LifecycleMetaData lifecycle = getLifecycle();
      assertNull(lifecycle.getMethodName());
      assertNull(lifecycle.getAnnotations());
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("Parameter1");
      assertParameters(expected, lifecycle.getParameters());
   }

   public void testLifecycleWithParameters() throws Exception
   {
      LifecycleMetaData lifecycle = getLifecycle();
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
      return LifecycleJaxbTestCase.suite(LifecycleJaxbTestCase.class);
   }

   public LifecycleJaxbTestCase(String name)
   {
      super(name);
   }
}
