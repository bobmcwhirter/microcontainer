/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.classloader.test.support;

import junit.framework.Test;

import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.test.AbstractTestCaseWithSetup;
import org.jboss.test.AbstractTestDelegate;

/**
 * IsolatedClassLoaderTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class IsolatedClassLoaderTest extends AbstractTestCaseWithSetup
{
   // The last helper
   private static IsolatedClassLoaderTestHelper helper;

   /**
    * Get the delegate
    * 
    * @param clazz the test class
    * @return the delegate
    */
   public static AbstractTestDelegate getDelegate(Class<?> clazz)
   {
      return new AbstractTestDelegate(clazz);
   }

   /**
    * Create a test with just test's package visible
    * and the default parent packages
    *
    * It imports nothing
    * 
    * @param clazz the test class
    * @return the test
    */
   public static Test suite(Class<?> clazz)
   {
      return suite(clazz, false);
   }

   /**
    * Create a test with just test's package visible
    * and the default parent packages
    * 
    * It exports everything
    * 
    * @param clazz the test class
    * @param importAll whether to import all
    * @return the test
    */
   public static Test suite(Class<?> clazz, boolean importAll)
   {
      return suite(clazz, importAll, new Class[0]);
   }
   
   /**
    * Create a test with test's package visible and the packages
    * of the classes listed with the default parent packages
    * 
    * It exports everything
    * It imports nothing
    * 
    * @param clazz the test class
    * @param packages the classes in packages that should also be included
    * @return the test
    */
   public static Test suite(Class<?> clazz, Class<?>... packages)
   {
      return suite(clazz, false, packages);
   }
   
   /**
    * Create a test with test's package visible and the packages
    * of the classes listed
    * 
    * It exports everything
    * 
    * @param clazz the test class
    * @param importAll whether to import all
    * @param packages the classes in packages that should also be included
    * @return the test
    */
   public static Test suite(Class<?> clazz, boolean importAll, Class<?>... packages)
   {
      helper = new IsolatedClassLoaderTestHelper();
      Class<?> newClass = helper.initializeClassLoader(clazz, importAll, packages);
      // Create the test based on the isolated class
      return AbstractTestCaseWithSetup.suite(newClass);
   }
   
   @Override
   protected void setUp() throws Exception
   {
      super.setUp();
      configureLogging();
   }

   /**
    * Create a new IsolatedClassLoaderTest.
    * 
    * @param name the test name
    */
   public IsolatedClassLoaderTest(String name)
   {
      super(name);
   }

   /**
    * Get the classloader system, this will be configured with
    * a domain called TEST that includes the test case's classloader
    * 
    * @return the classloader
    */
   public static ClassLoaderSystem getClassLoaderSystem()
   {
      return helper.getSystem();
   }
   
   /**
    * Create a classloader
    *
    * It exports everything
    * It imports everything
    * 
    * @param name the name
    * @param packages the packages
    * @return the classloader
    * @throws Exception for any error
    */
   protected static ClassLoader createClassLoader(String name, String... packages) throws Exception
   {
      return createClassLoader(name, true, packages);
   }
   
   /**
    * Create a classloader
    * 
    * It exports everything
    *
    * @param name the name
    * @param importAll whether to import all
    * @param packages the packages
    * @return the classloader
    * @throws Exception for any error
    */
   protected static ClassLoader createClassLoader(String name, boolean importAll, String... packages) throws Exception
   {
      return helper.createClassLoader(name, importAll, packages);
   }

   /**
    * Unregister a classloader
    * 
    * @param classLoader the classloader
    * @throws Exception for any error
    */
   protected static void unregisterClassLoader(ClassLoader classLoader) throws Exception
   {
      helper.unregisterClassLoader(classLoader);
   }
}
