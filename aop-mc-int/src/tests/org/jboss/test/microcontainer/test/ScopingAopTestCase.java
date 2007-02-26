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
package org.jboss.test.microcontainer.test;

import junit.framework.Test;
import org.jboss.aop.microcontainer.junit.AOPMicrocontainerTest;
import org.jboss.aop.microcontainer.junit.AOPMicrocontainerTestDelegate;
import org.jboss.aop.microcontainer.junit.ScopingAOPMicrocontainerTestDelegate;
import org.jboss.test.AbstractTestDelegate;
import org.jboss.test.microcontainer.support.SimpleBean;

/**
 * Test adding aspects to scoped beans.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ScopingAopTestCase extends AOPMicrocontainerTest
{
   public static Test suite()
   {
      return suite(ScopingAopTestCase.class);
   }

   public ScopingAopTestCase(String name)
   {
      super(name);
   }

   /**
    * Get the test delegate
    *
    * @param clazz the test class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class clazz) throws Exception
   {
      String property = System.getProperty("jboss.mc.secure", "false");
      boolean enableSecurity = Boolean.valueOf(property);
      AOPMicrocontainerTestDelegate delegate = new ScopingAOPMicrocontainerTestDelegate(clazz);
      delegate.enableSecurity = enableSecurity;
      return delegate;
   }

   public void testScopingWithAop() throws Exception
   {
      SimpleBean simple1 = (SimpleBean)getBean("simple1");
      assertNotNull(simple1);
      SimpleBean simple2 = (SimpleBean)getBean("simple2");
      assertNotNull(simple2);
   }

}
