/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.junit.test;

import junit.framework.Test;

import org.jboss.test.AbstractTestDelegate;
import org.jboss.test.kernel.junit.MicrocontainerTest;

/**
 * A JUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class JUnitTestCase extends MicrocontainerTest
{
   /**
    * Bootstrap the test
    * 
    * @return the test
    */   
   public static Test suite()
   {
      return suite(JUnitTestCase.class);
   }
   
   /**
    * Get the test delegate with security enabled
    * 
    * @param clazz the test class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class clazz) throws Exception
   {
      AbstractTestDelegate delegate = MicrocontainerTest.getDelegate(clazz);
      delegate.enableSecurity = true;
      return delegate;
   }

   /**
    * Create a new JUnitTestCase.
    * 
    * @param name the test name
    */
   public JUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testOne() throws Exception
   {
      System.out.println(getBean("TheBean"));
   }
   
   public void testTwo() throws Exception
   {
      System.out.println(getBean("TheBean"));
   }
}
