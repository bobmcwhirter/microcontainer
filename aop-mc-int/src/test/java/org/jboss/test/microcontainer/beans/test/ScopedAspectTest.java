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
import org.jboss.test.microcontainer.beans.PerClassAspect;
import org.jboss.test.microcontainer.beans.PerInstanceAspect;
import org.jboss.test.microcontainer.beans.PerJoinPointAspect;
import org.jboss.test.microcontainer.beans.PerVmAspect;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public abstract class ScopedAspectTest extends AOPMicrocontainerTest
{

   public ScopedAspectTest(String name)
   {
      super(name);
   }

   public void testPerVm() throws Exception
   {
      POJO pojo = (POJO)getBean("POJO1A");
      POJO2 pojo2 = (POJO2)getBean("POJO2");
   
      PerVmAspect.last = null;
      pojo.method();
      PerVmAspect a1 = PerVmAspect.last;
      assertNotNull(a1);
      
      PerVmAspect.last = null;
      pojo2.method();
      PerVmAspect a2 = PerVmAspect.last;
      assertNotNull(a2);
      assertSame(a1, a2);
      
      PerVmAspect.last = null;
      pojo.method(3);
      PerVmAspect a3 = PerVmAspect.last;
      assertNotNull(a3);
      assertSame(a1, a3);
   }

   public void testPerClass() throws Exception
   {
      POJO pojo = (POJO)getBean("POJO1A");
      POJO2 pojo2 = (POJO2)getBean("POJO2");
      
      PerClassAspect.last = null;
      pojo.method();
      PerClassAspect a1 = PerClassAspect.last;
      assertNotNull(a1);
      
      PerClassAspect.last = null;
      pojo2.method();
      PerClassAspect a2 = PerClassAspect.last;
      assertNotNull(a2);
      assertNotSame(a1, a2);
      
      PerClassAspect.last = null;
      pojo.method(3);
      PerClassAspect a3 = PerClassAspect.last;
      assertNotNull(a3);
      assertSame(a1, a3);
   }

   public void testPerInstance() throws Exception
   {
      POJO pojoA = (POJO)getBean("POJO1A");
      POJO pojoB = (POJO)getBean("POJO1B");
      POJO2 pojo2 = (POJO2)getBean("POJO2");
      
      PerInstanceAspect.last = null;
      pojoA.method();
      PerInstanceAspect a1 = PerInstanceAspect.last;
      assertNotNull(a1);
      
      PerInstanceAspect.last = null;
      pojoA.method(2);
      PerInstanceAspect a2 = PerInstanceAspect.last;
      assertNotNull(a2);
      assertSame(a1, a2);
      
      PerInstanceAspect.last = null;
      pojoB.method();
      PerInstanceAspect a3 = PerInstanceAspect.last;
      assertNotNull(a3);
      assertNotSame(a3, a2);
      
      PerInstanceAspect.last = null;
      pojoB.method(4);
      PerInstanceAspect a4 = PerInstanceAspect.last;
      assertNotNull(a4);
      assertSame(a3, a4);
      
      PerInstanceAspect.last = null;
      pojo2.method();
      PerInstanceAspect a5 = PerInstanceAspect.last;
      assertNotNull(a5);
      assertNotSame(a5, a4);
      assertNotSame(a5, a2);
      
      PerInstanceAspect.last = null;
      pojo2.method(4);
      PerInstanceAspect a6 = PerInstanceAspect.last;
      assertNotNull(a6);
      assertSame(a5, a6);
   }

   public void testPerJoinPoint() throws Exception
   {
      POJO pojoA = (POJO)getBean("POJO1A");
      POJO pojoB = (POJO)getBean("POJO1B");
      POJO2 pojo2 = (POJO2)getBean("POJO2");
   
      PerJoinPointAspect.last = null;
      pojoA.method();
      PerJoinPointAspect a1 = PerJoinPointAspect.last;
      assertNotNull(a1);
      
      PerJoinPointAspect.last = null;
      pojoA.method(1);
      PerJoinPointAspect a2 = PerJoinPointAspect.last;
      assertNotNull(a2);
      
      PerJoinPointAspect.last = null;
      pojoB.method();
      PerJoinPointAspect a3 = PerJoinPointAspect.last;
      assertNotNull(a3);
      
      PerJoinPointAspect.last = null;
      pojoB.method(1);
      PerJoinPointAspect a4 = PerJoinPointAspect.last;
      assertNotNull(a4);
      
      PerJoinPointAspect.last = null;
      pojo2.method();
      PerJoinPointAspect a5 = PerJoinPointAspect.last;
      assertNotNull(a5);
      
      PerJoinPointAspect.last = null;
      pojo2.method(1);
      PerJoinPointAspect a6 = PerJoinPointAspect.last;
      assertNotNull(a6);
      
      assertNotSame(a1, a2);
      assertNotSame(a1, a3);
      assertNotSame(a1, a4);
      assertNotSame(a1, a5);
      assertNotSame(a1, a6);
      assertNotSame(a2, a3);
      assertNotSame(a2, a4);
      assertNotSame(a2, a5);
      assertNotSame(a2, a6);
      assertNotSame(a3, a4);
      assertNotSame(a3, a5);
      assertNotSame(a3, a6);
      assertNotSame(a4, a5);
      assertNotSame(a4, a6);
      assertNotSame(a5, a6);
      
      PerJoinPointAspect.last = null;
      pojoA.method();
      PerJoinPointAspect a7 = PerJoinPointAspect.last;
      assertNotNull(a7);
      assertSame(a1, a7);
   }

}