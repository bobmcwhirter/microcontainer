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
package org.jboss.test.microcontainer.beans2.test;

import java.util.List;

import junit.framework.Test;

import org.jboss.aop.microcontainer.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.beans2.Interceptions;
import org.jboss.test.microcontainer.beans2.POJO;
import org.jboss.test.microcontainer.beans2.TestAspect;
import org.jboss.test.microcontainer.beans2.TestInterceptor;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class PrecedenceBeansTestCase extends AOPMicrocontainerTest
{
   public void testIntercepted() throws Exception
   {
      Interceptions.reset();
      POJO pojo = (POJO)getBean("Bean");
      pojo.method(2);
      
      List interceptions = Interceptions.interceptions();
      assertEquals(2, interceptions.size());
      
      assertEquals(TestAspect.class, interceptions.get(0).getClass());
      assertEquals(TestInterceptor.class, interceptions.get(1).getClass());
   }
   
   public static Test suite()
   {
      return suite(PrecedenceBeansTestCase.class);
   }
   
   public PrecedenceBeansTestCase(String test)
   {
      super(test);
   }
}
