/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.kernel.controller.support;

import org.jboss.test.AbstractTestCaseWithSetup;

/**
 * TestClassLoaderBean.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class TestClassLoaderBean
{
   private static ClassLoader classLoader;
   
   private String something = "something";

   public static ClassLoader getAndResetClassLoader()
   {
      ClassLoader result = classLoader;
      classLoader = null;
      return result;
   }
   
   protected static void checkClassLoader()
   {
      SecurityManager securityManager = AbstractTestCaseWithSetup.suspendSecurity();
      try
      {
         classLoader = Thread.currentThread().getContextClassLoader();
      }
      finally
      {
         AbstractTestCaseWithSetup.resumeSecurity(securityManager);
      }
   }
   
   public static TestClassLoaderBean createInstance()
   {
      checkClassLoader();
      return new TestClassLoaderBean();
   }
   
   public TestClassLoaderBean()
   {
       
   }
   
   public TestClassLoaderBean(String something)
   {
      checkClassLoader();
      this.something = something;
   }
   
   public String getSomething()
   {
      checkClassLoader();
      return something;
   }
   
   public void setSomething(String something)
   {
      checkClassLoader();
      this.something = something;
   }
   
   public void doLifecycle()
   {
      checkClassLoader();
   }
}
