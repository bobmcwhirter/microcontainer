/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.test.microcontainer.beans.test;

import org.jboss.test.AbstractTestDelegate;
import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.aop.junit.AOPMicrocontainerTestDelegate;
import org.jboss.test.microcontainer.beans.AnnotationWithValueAspect;
import org.jboss.test.microcontainer.beans.POJO;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class AnnotationOverridePropertyReplacementTest extends AOPMicrocontainerTest
{
   public AnnotationOverridePropertyReplacementTest(String name)
   {
      // FIXME AnnotationOverridePropertyReplacementTest constructor
      super(name);
   }

   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      //Run this test without security since we need to set system properties
      System.setProperty("integer", "100");
      AOPMicrocontainerTestDelegate delegate = new AOPMicrocontainerTestDelegate(clazz);
      return delegate;
   }
   
   public void testPropertyReplacement()
   {
      AnnotationWithValueAspect.value = null;
      POJO pojo = (POJO)getBean("Bean");
      pojo.method(2);
      assertNull(AnnotationWithValueAspect.value);
      
      pojo.method();
      assertNotNull(AnnotationWithValueAspect.value);
      assertEquals(100, AnnotationWithValueAspect.value.integer());
   }

   
}
