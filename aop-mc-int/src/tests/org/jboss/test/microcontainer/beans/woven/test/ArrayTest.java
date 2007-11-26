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
package org.jboss.test.microcontainer.beans.woven.test;

import org.jboss.aop.Advised;
import org.jboss.aop.microcontainer.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.beans.TestInterceptor;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public abstract class ArrayTest extends AOPMicrocontainerTest
{
   public ArrayTest(String name)
   {
      super(name);
   }

   public void testIntercepted() throws Exception
   {
      ClassWithArray pojo = (ClassWithArray)getBean("Bean");
      assertTrue(pojo instanceof Advised);
      
      TestInterceptor.invoked = false;
      TestArrayAspect.reset();
      pojo.setValue(1, 100);
      assertEquals(1, TestArrayAspect.index);
      assertEquals(100, TestArrayAspect.value);
      assertTrue(TestInterceptor.invoked);
      
      TestInterceptor.invoked = false;
      TestArrayAspect.reset();
      int i = pojo.getValue(1);
      assertEquals(1, TestArrayAspect.index);
      assertEquals(100, i);
      assertTrue(TestInterceptor.invoked);
   }
   
}