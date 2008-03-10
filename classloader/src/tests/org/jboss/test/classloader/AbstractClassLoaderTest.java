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
package org.jboss.test.classloader;

import junit.framework.TestCase;

import org.jboss.classloader.plugins.ClassLoaderUtils;
import org.jboss.classloader.plugins.jdk.AbstractJDKChecker;
import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.classloader.test.support.MockClassLoaderHelper;
import org.jboss.classloader.test.support.MockClassLoaderPolicy;
import org.jboss.test.AbstractTestCaseWithSetup;
import org.jboss.test.AbstractTestDelegate;

/**
 * AbstractClassLoaderTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractClassLoaderTest extends AbstractTestCaseWithSetup
{
   static
   {
      // Make sure the mock classloader doesn't think we are part of the JDK
      AbstractJDKChecker.getExcluded().add(TestCase.class);
   }
   
   public static AbstractTestDelegate getDelegate(Class<?> clazz)
   {
      return new AbstractTestDelegate(clazz);
   }

   @Override
   protected void setUp() throws Exception
   {
      super.setUp();
      configureLogging();
   }

   public AbstractClassLoaderTest(String name)
   {
      super(name);
   }

   protected ClassLoaderSystem createClassLoaderSystem()
   {
      // We always create a new one to avoid things in the default domain leaking across tests
      return MockClassLoaderHelper.createMockClassLoaderSystem();
   }
   
   protected ClassLoaderSystem createClassLoaderSystemWithModifiedBootstrap()
   {
      ClassLoaderSystem result = createClassLoaderSystem();
      result.getDefaultDomain().setParentPolicy(ParentPolicy.BEFORE_BUT_JAVA_ONLY);
      return result;
   }
   
   protected ClassLoader createClassLoaderSystemWithModifiedBootstrapAndMockClassLoader()
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      return createAndRegisterMockClassLoader(system);
   }
   
   protected ClassLoader registerPolicyWithDefaultDomain(ClassLoaderPolicy policy, ClassLoaderSystem system)
   {
      return system.registerClassLoaderPolicy(policy);
   }
   
   protected MockClassLoaderPolicy createMockClassLoaderPolicy()
   {
      return createMockClassLoaderPolicy(null);
   }
   
   protected MockClassLoaderPolicy createMockClassLoaderPolicy(String name)
   {
      return MockClassLoaderHelper.createMockClassLoaderPolicy(name);
   }
   
   protected ClassLoader createAndRegisterMockClassLoader(ClassLoaderSystem system)
   {
      return createAndRegisterMockClassLoader(system, "mock");
   }
   
   protected ClassLoader createAndRegisterMockClassLoader(ClassLoaderSystem system, String name)
   {
      return MockClassLoaderHelper.createAndRegisterMockClassLoader(system, (ClassLoaderDomain) null, name);
   }
   
   protected ClassLoader createAndRegisterMockClassLoader(ClassLoaderSystem system, ClassLoaderDomain domain)
   {
      return createAndRegisterMockClassLoader(system, domain, "mock");
   }
   
   protected ClassLoader createAndRegisterMockClassLoader(ClassLoaderSystem system, String domainName, String name)
   {
      return MockClassLoaderHelper.createAndRegisterMockClassLoader(system, domainName, name);
   }
   
   protected ClassLoader createAndRegisterMockClassLoader(ClassLoaderSystem system, String domainName, ParentPolicy parentPolicy, String name)
   {
      return MockClassLoaderHelper.createAndRegisterMockClassLoader(system, domainName, parentPolicy, name);
   }
   
   protected ClassLoader createAndRegisterMockClassLoader(ClassLoaderSystem system, String domainName, ParentPolicy parentPolicy, String parentDomainName, String name)
   {
      return MockClassLoaderHelper.createAndRegisterMockClassLoader(system, domainName, parentPolicy, parentDomainName, name);
   }
   
   protected ClassLoader createAndRegisterMockClassLoader(ClassLoaderSystem system, ClassLoaderDomain domain, String name)
   {
      return MockClassLoaderHelper.createAndRegisterMockClassLoader(system, domain, name);
   }
   
   protected void assertClassEquality(Class<?> expected, Class<?> actual)
   {
      assertTrue("Should be the same " + ClassLoaderUtils.classToString(expected) +" and " + ClassLoaderUtils.classToString(actual), expected == actual);
   }
   
   protected void assertNoClassEquality(Class<?> expected, Class<?> actual)
   {
      assertTrue("Should NOT be the same " + ClassLoaderUtils.classToString(expected) +" and " + ClassLoaderUtils.classToString(actual), expected != actual);
   }
   
   protected void assertClassLoader(Class<?> clazz, ClassLoader expected)
   {
      if (expected == null)
         return;
      boolean result = MockClassLoaderHelper.isExpectedClassLoader(clazz, expected);
      assertTrue(ClassLoaderUtils.classToString(clazz) + " should have expected classloader=" + expected, result);
   }
   
   protected Class<?> assertLoadClass(Class<?> reference, ClassLoader start)
   {
      return assertLoadClass(reference, start, start, false);
   }
   
   protected Class<?> assertLoadClass(Class<?> reference, ClassLoader start, boolean isReference)
   {
      return assertLoadClass(reference, start, start, isReference);
   }
   
   protected Class<?> assertLoadClass(Class<?> reference, ClassLoader start, ClassLoader expected)
   {
      return assertLoadClass(reference, start, expected, false);
   }
   
   protected Class<?> assertLoadClass(Class<?> reference, ClassLoader start, ClassLoader expected, boolean isReference)
   {
      Class<?> result = assertLoadClass(reference.getName(), start, expected);
      if (isReference)
         assertClassEquality(reference, result);
      else
         assertNoClassEquality(reference, result);
      return result;
   }
   
   protected Class<?> assertLoadClass(String name, ClassLoader start)
   {
      return assertLoadClass(name, start, start);
   }
   
   protected Class<?> assertLoadClass(String name, ClassLoader start, ClassLoader expected)
   {
      Class<?> result = null;
      try
      {
         result = start.loadClass(name);
         getLog().debug("Got class: " + ClassLoaderUtils.classToString(result) + " for " + name + " from " + start);
      }
      catch (ClassNotFoundException e)
      {
         failure("Did not expect CNFE for " + name + " from " + start, e);
      }
      assertClassLoader(result, expected);
      return result;
   }
   
   protected void assertLoadClassFail(Class<?> reference, ClassLoader start)
   {
      assertLoadClassFail(reference.getName(), start);
   }
      
   protected void assertLoadClassFail(String name, ClassLoader start)
   {
      try
      {
         start.loadClass(name);
         fail("Should not be here!");
      }
      catch (Exception expected)
      {
         checkThrowable(ClassNotFoundException.class, expected);
      }
   }
   
   protected Class<?> assertClassForName(Class<?> reference, ClassLoader start)
   {
      return assertClassForName(reference, start, start, false);
   }
   
   protected Class<?> assertClassForName(Class<?> reference, ClassLoader start, boolean isReference)
   {
      return assertClassForName(reference, start, start, isReference);
   }
   
   protected Class<?> assertClassForName(Class<?> reference, ClassLoader start, ClassLoader expected)
   {
      return assertClassForName(reference, start, expected, false);
   }
   
   protected Class<?> assertClassForName(Class<?> reference, ClassLoader start, ClassLoader expected, boolean isReference)
   {
      Class<?> result = assertClassForName(reference.getName(), start, expected);
      if (isReference)
         assertClassEquality(reference, result);
      else
         assertNoClassEquality(reference, result);
      return result;
   }
   
   protected Class<?> assertClassForName(String name, ClassLoader start)
   {
      return assertLoadClass(name, start, start);
   }
   
   protected Class<?> assertClassForName(String name, ClassLoader start, ClassLoader expected)
   {
      Class<?> result = null;
      try
      {
         result = Class.forName(name, true, start);
         getLog().debug("Got class: " + ClassLoaderUtils.classToString(result) + " for " + name + " from " + start);
      }
      catch (ClassNotFoundException e)
      {
         failure("Did not expect CNFE for " + name + " from " + start, e);
      }
      assertClassLoader(result, expected);
      return result;
   }
   
   protected void assertClassForNameFail(Class<?> reference, ClassLoader start)
   {
      assertClassForNameFail(reference.getName(), start);
   }
      
   protected void assertClassForNameFail(String name, ClassLoader start)
   {
      try
      {
         Class.forName(name, true, start);
      }
      catch (Exception expected)
      {
         checkThrowable(ClassNotFoundException.class, expected);
      }
   }
   
   protected void assertPackage(Class<?> reference, ClassLoader classLoader) throws Exception
   {
      Class<?> clazz = classLoader.loadClass(reference.getName());
      Package pkge = Package.getPackage(ClassLoaderUtils.getClassPackageName(clazz.getName()));
      assertEquals(pkge,clazz.getPackage());
   }
   
   protected void assertPackage(Class<?> reference, ClassLoader classLoader, MockClassLoaderPolicy policy) throws Exception
   {
      Class<?> clazz = classLoader.loadClass(reference.getName());
      Package pkge = clazz.getPackage();
      assertNotNull("Expected a package for " + clazz.getName(), pkge);
      assertEquals(policy.getName(), pkge.getImplementationTitle());
   }
   
   protected void assertFilterMatchesClassName(String test, ClassFilter filter)
   {
      getLog().debug("Checking " + test + " expect it to match filter=" + filter);
      boolean result = filter.matchesClassName(test);
      getLog().debug("Checked " + test + " result was " + result + " for filter=" + filter);
      assertTrue("Expected " + test + " to match " + filter, result);
   }
   
   protected void assertFilterNoMatchClassName(String test, ClassFilter filter)
   {
      getLog().debug("Checking " + test + " expect it NOT to match filter=" + filter);
      boolean result = filter.matchesClassName(test);
      getLog().debug("Checked " + test + " result was " + result + " for filter=" + filter);
      assertFalse("Expected " + test + " NOT to match " + filter, result);
   }
   
   protected void assertFilterMatchesResourcePath(String test, ClassFilter filter)
   {
      getLog().debug("Checking " + test + " expect it to match filter=" + filter);
      boolean result = filter.matchesResourcePath(test);
      getLog().debug("Checked " + test + " result was " + result + " for filter=" + filter);
      assertTrue("Expected " + test + " to match " + filter, result);
   }
   
   protected void assertFilterNoMatchResourcePath(String test, ClassFilter filter)
   {
      getLog().debug("Checking " + test + " expect it NOT to match filter=" + filter);
      boolean result = filter.matchesResourcePath(test);
      getLog().debug("Checked " + test + " result was " + result + " for filter=" + filter);
      assertFalse("Expected " + test + " NOT to match " + filter, result);
   }
   
   protected void assertFilterMatchesPackageName(String test, ClassFilter filter)
   {
      getLog().debug("Checking " + test + " expect it to match filter=" + filter);
      boolean result = filter.matchesPackageName(test);
      getLog().debug("Checked " + test + " result was " + result + " for filter=" + filter);
      assertTrue("Expected " + test + " to match " + filter, result);
   }
   
   protected void assertFilterNoMatchPackageName(String test, ClassFilter filter)
   {
      getLog().debug("Checking " + test + " expect it NOT to match filter=" + filter);
      boolean result = filter.matchesPackageName(test);
      getLog().debug("Checked " + test + " result was " + result + " for filter=" + filter);
      assertFalse("Expected " + test + " NOT to match " + filter, result);
   }

   protected void configureLogging()
   {
      //enableTrace("org.jboss.classloader");
   }
}
