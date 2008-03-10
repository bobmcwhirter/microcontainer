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
package org.jboss.test.classloader.delegate.test;

import junit.framework.Test;

import org.jboss.classloader.plugins.ClassLoaderUtils;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.DelegateLoader;
import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.classloader.spi.filter.FilteredDelegateLoader;
import org.jboss.classloader.spi.filter.LazyFilteredDelegateLoader;
import org.jboss.classloader.test.support.MockClassLoaderPolicy;
import org.jboss.test.classloader.AbstractClassLoaderTestWithSecurity;
import org.jboss.test.classloader.delegate.support.a.TestA1;
import org.jboss.test.classloader.delegate.support.b.TestB1;
import org.jboss.test.classloader.policy.support.TestClassLoaderPolicyFactory;

/**
 * DelegateUnitTestCase
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class DelegateLoaderUnitTestCase extends AbstractClassLoaderTestWithSecurity
{
   public static Test suite()
   {
      return suite(DelegateLoaderUnitTestCase.class);
   }

   public DelegateLoaderUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testLoadClassNoFilter() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      policy.setPathsAndPackageNames(TestA1.class);
      system.registerClassLoaderPolicy(policy);
      
      FilteredDelegateLoader delegate = new FilteredDelegateLoader(policy);
      assertLoadClass(TestA1.class, delegate);
   }
   
   public void testLoadClassFiltered() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      policy.setPathsAndPackageNames(TestA1.class);
      system.registerClassLoaderPolicy(policy);
      
      FilteredDelegateLoader delegate = new FilteredDelegateLoader(policy, ClassFilter.NOTHING);
      assertLoadClassFail(TestA1.class, delegate);
   }
   
   public void testLoadClassPolicyNotRegistered() throws Exception
   {
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      policy.setPathsAndPackageNames(TestA1.class);
      
      FilteredDelegateLoader delegate = new FilteredDelegateLoader(policy);
      assertLoadClassFail(TestA1.class, delegate);
   }
   
   public void testLazyLoadClass() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy policy = createMockClassLoaderPolicy("a");
      policy.setPaths(TestA1.class, TestB1.class);
      policy.setPackageNames(TestA1.class);
      ClassLoader cl = system.registerClassLoaderPolicy(policy);
      assertLoadClass(TestA1.class, cl);
      assertLoadClass(TestB1.class, cl);

      TestClassLoaderPolicyFactory factory = new TestClassLoaderPolicyFactory(policy, false);
      LazyFilteredDelegateLoader delegate = new LazyFilteredDelegateLoader(factory);
      assertLoadClassFail(TestA1.class, delegate);
      assertLoadClassFail(TestB1.class, delegate);
      
      factory.setCanCreate(true);
      assertLoadClass(TestA1.class, delegate);
      assertLoadClassFail(TestB1.class, delegate);
   }
   
   protected Class<?> assertLoadClass(Class<?> reference, DelegateLoader delegate) throws Exception
   {
      Class<?> result = delegate.loadClass(reference.getName());
      assertNotNull("Should have loaded " + reference.getName() + " from " + delegate, result);
      getLog().debug("Loaded " + ClassLoaderUtils.classToString(result) + " from " + delegate);
      return result;
   }

   protected void assertLoadClassFail(Class<?> reference, DelegateLoader delegate) throws Exception
   {
      Class<?> result = delegate.loadClass(reference.getName());
      String message = "Should not have loaded " + ClassLoaderUtils.classToString(result) + " from " + delegate;
      assertNull(message, result);
   }
}
