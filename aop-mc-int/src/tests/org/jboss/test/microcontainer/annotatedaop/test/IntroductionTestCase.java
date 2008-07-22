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
package org.jboss.test.microcontainer.annotatedaop.test;

import java.util.Arrays;

import org.jboss.test.aop.junit.AnnotatedAOPMicrocontainerTest;
import org.jboss.test.microcontainer.annotatedaop.EmptyInterface1;
import org.jboss.test.microcontainer.annotatedaop.EmptyInterface2;
import org.jboss.test.microcontainer.annotatedaop.EmptyInterface3;
import org.jboss.test.microcontainer.annotatedaop.MixinA;
import org.jboss.test.microcontainer.annotatedaop.MixinInterface1;
import org.jboss.test.microcontainer.annotatedaop.MixinInterface2;
import org.jboss.test.microcontainer.annotatedaop.MixinInterface3;
import org.jboss.test.microcontainer.annotatedaop.SimplePOJO;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class IntroductionTestCase extends AnnotatedAOPMicrocontainerTest
{
   public IntroductionTestCase(String name)
   {
      super(name);
   }

   public void testIntroductionsAndMixins() throws Exception
   {
      SimplePOJO pojo = getBean();
      System.out.println("-------------->" + Arrays.asList(pojo.getClass().getInterfaces()));

      assertInstanceOf(pojo, EmptyInterface1.class);
      assertInstanceOf(pojo, EmptyInterface2.class);
      assertInstanceOf(pojo, EmptyInterface3.class);

      assertInstanceOf(pojo, MixinInterface1.class);
      assertEquals(1, ((MixinInterface1)pojo).mixin1());

      assertInstanceOf(pojo, MixinInterface2.class);
      assertEquals(2, ((MixinInterface2)pojo).mixin2());
      
      assertInstanceOf(pojo, MixinInterface3.class);
      assertEquals(3, ((MixinInterface3)pojo).mixin3());
      
      assertNotNull(MixinA.pojo);
   }
   
   private SimplePOJO getBean()
   {
      return (SimplePOJO)getBean("Bean");
   }
}
