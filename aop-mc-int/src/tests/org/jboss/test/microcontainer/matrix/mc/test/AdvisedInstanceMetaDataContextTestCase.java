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
package org.jboss.test.microcontainer.matrix.mc.test;


import junit.framework.Test;

import org.jboss.aop.Advised;
import org.jboss.aop.microcontainer.junit.AOPMicrocontainerTest;
import org.jboss.aop.proxy.container.AspectManaged;
import org.jboss.test.microcontainer.matrix.Base;
import org.jboss.test.microcontainer.matrix.TestInterceptor;

/**
 * Only tested for MC since setting up the metadatacontext manually for the proxyfactory test is too cumbersome
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class AdvisedInstanceMetaDataContextTestCase extends AOPMicrocontainerTest
{
   public void testNoMetaDataContext() throws Exception
   {
      Base base = (Base)getBean("NotAdvised");
      assertTrue(base instanceof Advised);
      assertFalse(base instanceof AspectManaged);
      
      //Not the main purpose of the test but being paranoid never hurt
      TestInterceptor.reset();
      base.baseOnly();
      assertEquals(1, TestInterceptor.interceptions);
      assertNotNull(TestInterceptor.invoked);
      assertEquals("baseOnly", TestInterceptor.invoked.getName());
      assertNull(TestInterceptor.classAnnotation);
      assertNull(TestInterceptor.methodAnnotation);
      assertNull(TestInterceptor.metadata);
   }
   
   public void testClassMetaDataContext() throws Exception
   {
      Base.baseInvoked = false;
      Base base = (Base)getBean("ClassAnnotated");
      assertTrue(base instanceof Advised);
      assertTrue(base instanceof AspectManaged);

      //Not the main purpose of the test but being paranoid never hurt
      TestInterceptor.reset();
      base.baseOnly();
      assertTrue(Base.baseInvoked);
      assertEquals(1, TestInterceptor.interceptions);
      assertNotNull(TestInterceptor.invoked);
      assertEquals("baseOnly", TestInterceptor.invoked.getName());
      assertNotNull(TestInterceptor.classAnnotation);
      assertTrue(TestInterceptor.classAnnotation instanceof org.jboss.test.microcontainer.support.Test);
      assertNull(TestInterceptor.methodAnnotation);
      assertNull(TestInterceptor.metadata);
   }
   
   public void testPropertyMetaDataContext() throws Exception
   {
      Base.baseInvoked = false;
      Base base = (Base)getBean("PropertyAnnotated");
      assertTrue(base instanceof Advised);
      assertTrue(base instanceof AspectManaged);
      
      //Not the main purpose of the test but being paranoid never hurt
      TestInterceptor.reset();
      base.setProperty(5);
      assertTrue(Base.baseInvoked);
      assertEquals(1, TestInterceptor.interceptions);
      assertNotNull(TestInterceptor.invoked);
      assertEquals("setProperty", TestInterceptor.invoked.getName());
      assertNull(TestInterceptor.classAnnotation);
      assertNotNull(TestInterceptor.methodAnnotation);
      assertTrue(TestInterceptor.methodAnnotation instanceof org.jboss.test.microcontainer.support.Test);
      assertNull(TestInterceptor.metadata);
   }
   
   public static Test suite()
   {
      return suite(AdvisedInstanceMetaDataContextTestCase.class);
   }

   public AdvisedInstanceMetaDataContextTestCase(String name)
   {
      super(name);
   }
}
