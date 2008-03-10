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
package org.jboss.test.classloader.junit.test;

import java.util.Set;

import javax.naming.InitialContext;

import junit.framework.Test;

import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.test.support.IsolatedClassLoaderTest;
import org.jboss.test.classloader.junit.notsupport.NotSupport;
import org.jboss.test.classloader.junit.support.Support;

/**
 * IsolatedClassLoaderUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class IsolatedClassLoaderUnitTestCase extends IsolatedClassLoaderTest
{
   private static final String NOT_SUPPORT_PACKAGE = "org.jboss.test.classloader.junit.notsupport";
   private static final String NOT_SUPPORT_CLASS = NOT_SUPPORT_PACKAGE + ".NotSupport";
   
   public static Test suite()
   {
      return suite(IsolatedClassLoaderUnitTestCase.class, true, Support.class);
   }

   public IsolatedClassLoaderUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testClassLoader()
   {
      ClassLoader classLoader = getClass().getClassLoader();
      assertFalse(classLoader.equals(IsolatedClassLoaderTest.class.getClassLoader()));
   }
   
   public void testClassLoaderSystem()
   {
      ClassLoaderSystem system = getClassLoaderSystem();
      assertTrue(system.isRegistered("TEST"));
   }
   
   public void testSupport()
   {
      assertEquals(Support.class.getClassLoader(), getClass().getClassLoader());
   }
   
   public void testNotSupport()
   {
      // This should fail since it is not suite() above
      try
      {
         getLog().debug(NotSupport.class);
         fail("Should not be here!");
      }
      catch (Throwable expected)
      {
         checkThrowable(NoClassDefFoundError.class, expected);
      }
   }
   
   public void testCreateClassLoader() throws Exception
   {
      ClassLoader classLoader = createClassLoader("NewClassLoader", NOT_SUPPORT_PACKAGE);
      try
      {
         Class<?> clazz = classLoader.loadClass(NOT_SUPPORT_CLASS);
         assertEquals(classLoader, clazz.getClassLoader());

         Class<?> clazz2 = getClass().getClassLoader().loadClass(NOT_SUPPORT_CLASS);
         assertEquals(classLoader, clazz2.getClassLoader());
      }
      finally
      {
         unregisterClassLoader(classLoader);
      }
   }
   
   public void testUnregisterClassLoader() throws Exception
   {
      ClassLoader classLoader = createClassLoader("NewClassLoader", NOT_SUPPORT_PACKAGE);
      unregisterClassLoader(classLoader);
      
      try
      {
         classLoader.loadClass(NOT_SUPPORT_CLASS);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalStateException.class, t);
      }
      
      try
      {
         getClass().getClassLoader().loadClass(NOT_SUPPORT_CLASS);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(ClassNotFoundException.class, t);
      }
   }
   
   public void testJavaClass() throws Exception
   {
      Class<?> clazz = getClass().getClassLoader().loadClass("java.util.Set");
      assertEquals(Set.class, clazz);
      clazz = getClass().getClassLoader().loadClass("javax.naming.InitialContext");
      assertEquals(InitialContext.class, clazz);
   }
}
