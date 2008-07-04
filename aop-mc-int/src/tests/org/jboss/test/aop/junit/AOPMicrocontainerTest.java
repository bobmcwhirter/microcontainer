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
package org.jboss.test.aop.junit;

import org.jboss.test.AbstractTestDelegate;
import org.jboss.test.kernel.junit.MicrocontainerTest;

/**
 * An AOPMicrocontainerTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AOPMicrocontainerTest extends MicrocontainerTest
{
   /**
    * Get the test delegate
    * 
    * @param clazz the test class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      String property = System.getProperty("jboss.mc.secure", "false");
      boolean enableSecurity = Boolean.valueOf(property).booleanValue();
      AOPMicrocontainerTestDelegate delegate = new AOPMicrocontainerTestDelegate(clazz);
      delegate.enableSecurity = enableSecurity;
      return delegate;
   }
   
   public static void setUseJaxb(boolean val)
   {
      System.setProperty("jboss.mc.jaxb", String.valueOf(val));
   }
   
   /**
    * Create a new AOPMicrocontainer test
    * 
    * @param name the test name
    */
   public AOPMicrocontainerTest(String name)
   {
      super(name);
   }
}
