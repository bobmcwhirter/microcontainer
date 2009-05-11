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

import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.beans.POJO;
import org.jboss.test.microcontainer.beans.POJO2;
import org.jboss.test.microcontainer.beans.ScopedFactoryAspect;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ScopedAspectFactoryPerClassJoinpointAopTestCase extends AOPMicrocontainerTest
{

   public ScopedAspectFactoryPerClassJoinpointAopTestCase(String name)
   {
      super(name);
   }

   public void testPerClassJoinPoint() throws Exception
   {
      POJO pojoA = (POJO)getBean("POJO1A");
      POJO pojoB = (POJO)getBean("POJO1B");
      POJO2 pojo2 = (POJO2)getBean("POJO2");
   
      ScopedFactoryAspect.last = null;      
      pojoA.method();
      ScopedFactoryAspect a1 = ScopedFactoryAspect.last;
      assertNotNull(a1);
      
      ScopedFactoryAspect.last = null;      
      pojoA.method(1);
      ScopedFactoryAspect a2 = ScopedFactoryAspect.last;
      assertNotNull(a2);
      
      ScopedFactoryAspect.last = null;      
      pojoB.method();
      ScopedFactoryAspect a3 = ScopedFactoryAspect.last;
      assertNotNull(a3);
      
      ScopedFactoryAspect.last = null;      
      pojoB.method(1);
      ScopedFactoryAspect a4 = ScopedFactoryAspect.last;
      assertNotNull(a4);
      
      ScopedFactoryAspect.last = null;      
      pojo2.method();
      ScopedFactoryAspect a5 = ScopedFactoryAspect.last;
      assertNotNull(a5);
      
      ScopedFactoryAspect.last = null;      
      pojo2.method(1);
      ScopedFactoryAspect a6 = ScopedFactoryAspect.last;
      assertNotNull(a6);
      
      
      assertNotSame(a1, a2);
      assertSame(a1, a3);
      assertNotSame(a1, a4);
      assertNotSame(a1, a5);
      assertNotSame(a1, a6);
      assertNotSame(a2, a3);
      assertSame(a2, a4);
      assertNotSame(a2, a5);
      assertNotSame(a2, a6);
      assertNotSame(a3, a4);
      assertNotSame(a3, a5);
      assertNotSame(a3, a6);
      assertNotSame(a4, a5);
      assertNotSame(a4, a6);
      assertNotSame(a5, a6);
      
      ScopedFactoryAspect.last = null;
      pojoA.method();
      ScopedFactoryAspect a7 = ScopedFactoryAspect.last;
      assertNotNull(a7);
      assertSame(a1, a7);
   }

}