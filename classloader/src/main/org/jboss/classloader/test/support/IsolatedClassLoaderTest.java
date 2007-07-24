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

import java.util.HashSet;
import java.util.Set;

import junit.extensions.TestSetup;
import junit.framework.Test;

import org.jboss.classloader.plugins.system.DefaultClassLoaderSystem;
import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.classloader.spi.filter.PackageClassFilter;
import org.jboss.logging.Logger;
import org.jboss.test.AbstractTestCaseWithSetup;
import org.jboss.test.AbstractTestDelegate;
import org.jboss.test.logging.LoggingPlugin;
import org.jboss.test.security.PolicyPlugin;

/**
 * IsolatedClassLoaderTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class IsolatedClassLoaderTest extends AbstractTestCaseWithSetup
{
   // The last classloader system
   private static ClassLoaderSystem system;

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
    * @see #getParentPackages()
    * @param clazz the test class
    * @param packages the classes in packages that should also be included
    * @return the test
    */
   public static Test suite(Class<?> clazz, Class<?>... packages)
   {
      return suite(clazz, false, getParentPackages(), packages);
   }
   
   /**
    * Create a test with test's package visible and the packages
    * of the classes listed with the default parent packages
    *
    * It exports everything
    * 
    * @see #getParentPackages()
    * @param importAll whether to import all
    * @param clazz the test class
    * @param packages the classes in packages that should also be included
    * @return the test
    */
   public static Test suite(Class<?> clazz, boolean importAll, Class<?>... packages)
   {
      return suite(clazz, importAll, getParentPackages(), packages);
   }
   
   /**
    * Create a test with test's package visible and the packages
    * of the classes listed
    * 
    * It exports everything
    * 
    * @see #getParentPackages()
    * @param clazz the test class
    * @param importAll whether to import all
    * @param parentPackages the packages that are not isolated
    * @param packages the classes in packages that should also be included
    * @return the test
    */
   public static Test suite(Class<?> clazz, boolean importAll, Set<String> parentPackages, Class<?>... packages)
   {
      // A new classloader system for each test
      system = new DefaultClassLoaderSystem();

      // The parent filter
      PackageClassFilter filter = new PackageClassFilter(parentPackages.toArray(new String[parentPackages.size()]));
      filter.setIncludeJava(true);
      ParentPolicy parentPolicy = new ParentPolicy(filter, ClassFilter.NOTHING);
      ClassLoaderDomain domain = system.createAndRegisterDomain("TEST", parentPolicy);
      
      // Configure the policy for the test
      MockClassLoaderPolicy policy = new MockClassLoaderPolicy();
      Set<Class> classes = new HashSet<Class>();
      classes.add(clazz);
      for (Class<?> c : packages)
         classes.add(c);
      policy.setImportAll(importAll);
      policy.setPathsAndPackageNames(classes.toArray(new Class[classes.size()]));
      
      // Create the classloader
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);

      // Load the class from the isolated classloader
      try
      {
         clazz = classLoader.loadClass(clazz.getName());
      }
      catch (ClassNotFoundException e)
      {
         throw new RuntimeException("Unable to load test class in isolated classloader " + clazz, e);
      }
      
      // Create the test based on the isolated class
      return AbstractTestCaseWithSetup.suite(clazz);
   }

   /**
    * Get the packages that should not be isolated
    * (and by transience their dependent classes, e.g. log4j in the classpath)<p>
    * 
    * NOTE: The transient packages cannot be used directly by the test
    * unless explicity mentioned in this list.
    * 
    * @return the test support packages
    */
   public static Set<String> getParentPackages()
   {
      Set<String> result = new HashSet<String>();
      result.add(Test.class.getPackage().getName());
      result.add(TestSetup.class.getPackage().getName());
      result.add(AbstractTestCaseWithSetup.class.getPackage().getName());
      result.add(Logger.class.getPackage().getName());
      result.add(LoggingPlugin.class.getPackage().getName());
      result.add(PolicyPlugin.class.getPackage().getName());
      result.add(ClassLoaderSystem.class.getPackage().getName());
      result.add(IsolatedClassLoaderTest.class.getPackage().getName());
      return result;
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
   public ClassLoaderSystem getClassLoaderSystem()
   {
      return system;
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
   protected ClassLoader createClassLoader(String name, String packages) throws Exception
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
   protected ClassLoader createClassLoader(String name, boolean importAll, String packages) throws Exception
   {
      ClassLoaderSystem system = getClassLoaderSystem();
      ClassLoaderDomain domain = system.getDomain("TEST");
      MockClassLoaderPolicy policy = MockClassLoaderHelper.createMockClassLoaderPolicy(name);
      policy.setImportAll(importAll);
      policy.setPathsAndPackageNames(packages);
      return MockClassLoaderHelper.registerMockClassLoader(system, domain, policy);
   }

   /**
    * Unregister a classloader
    * 
    * @param classLoader the classloader
    * @throws Exception for any error
    */
   protected void unregisterClassLoader(ClassLoader classLoader) throws Exception
   {
      ClassLoaderSystem system = getClassLoaderSystem();
      system.unregisterClassLoader(classLoader);
   }
}
