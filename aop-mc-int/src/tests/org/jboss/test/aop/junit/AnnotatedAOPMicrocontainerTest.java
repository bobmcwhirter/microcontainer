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
package org.jboss.test.aop.junit;

import org.jboss.test.AbstractTestDelegate;
import org.jboss.test.kernel.junit.MicrocontainerTest;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class AnnotatedAOPMicrocontainerTest extends MicrocontainerTest
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
      AnnotatedAOPMicrocontainerTestDelegate delegate = new AnnotatedAOPMicrocontainerTestDelegate(clazz);
      delegate.enableSecurity = enableSecurity;
      return delegate;
   }
   
   
   /**
    * Create a new AnnotatedAOPMicrocontainer test
    * 
    * @param name the test name
    */
   public AnnotatedAOPMicrocontainerTest(String name)
   {
      super(name);
   }
}
