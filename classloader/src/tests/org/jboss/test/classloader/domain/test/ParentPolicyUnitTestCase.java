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
package org.jboss.test.classloader.domain.test;

import junit.framework.Test;

import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.classloader.spi.filter.PackageClassFilter;
import org.jboss.classloader.test.support.MockClassLoaderPolicy;
import org.jboss.test.classloader.AbstractClassLoaderTestWithSecurity;
import org.jboss.test.classloader.domain.support.MatchClassFilter;
import org.jboss.test.classloader.domain.support.NoMatchClassFilter;

/**
 * ParentPolicyUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ParentPolicyUnitTestCase extends AbstractClassLoaderTestWithSecurity
{
   public static Test suite()
   {
      return suite(ParentPolicyUnitTestCase.class);
   }

   public ParentPolicyUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testNoParentLoadsFromJBossClassLoadersClassLoader() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", ParentPolicy.BEFORE, null);
      
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      
      assertLoadClass(Object.class, classLoader, null, true);
      assertPackage(ClassLoaderDomain.class, classLoader);
      assertLoadClass(ClassLoaderDomain.class, classLoader, null, true);
      assertPackage(ClassLoaderDomain.class, classLoader);
   }
   
   public void testBefore() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", ParentPolicy.BEFORE, null);
      
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      policy.setPathsAndPackageNames(ClassLoaderDomain.class);
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      
      assertLoadClass(Object.class, classLoader, null, true);
      assertLoadClass(ClassLoaderDomain.class, classLoader, null, true);
      assertPackage(ClassLoaderDomain.class, classLoader);
   }
   
   public void testBeforeButJavaOnly() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", ParentPolicy.BEFORE_BUT_JAVA_ONLY, null);
      
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      policy.setPathsAndPackageNames(ClassLoaderDomain.class);
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      
      assertLoadClass(Object.class, classLoader, null, true);
      assertLoadClass(ClassLoaderDomain.class, classLoader);
      assertPackage(ClassLoaderDomain.class, classLoader, policy);
   }
   
   public void testBeforeButJavaOnlyNotFound() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", ParentPolicy.BEFORE_BUT_JAVA_ONLY, null);
      
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      
      assertLoadClass(Object.class, classLoader, null, true);
      assertLoadClassFail(ClassLoaderDomain.class, classLoader);
   }
   
   public void testAfterNotReached() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", ParentPolicy.AFTER, null);
      
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      policy.setPathsAndPackageNames(ClassLoaderDomain.class);
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      
      assertLoadClass(Object.class, classLoader, null, true);
      assertLoadClass(ClassLoaderDomain.class, classLoader);
      assertPackage(ClassLoaderDomain.class, classLoader);
   }
   
   public void testAfterReached() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", ParentPolicy.AFTER, null);
      
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      
      assertLoadClass(Object.class, classLoader, null, true);
      assertLoadClass(ClassLoaderDomain.class, classLoader, null, true);
      assertPackage(ClassLoaderDomain.class, classLoader);
   }
   
   public void testAfterButJavaBeforeNotReached() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", ParentPolicy.AFTER_BUT_JAVA_BEFORE, null);
      
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      policy.setPathsAndPackageNames(ClassLoaderDomain.class);
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      
      assertLoadClass(Object.class, classLoader, null, true);
      assertLoadClass(ClassLoaderDomain.class, classLoader);
      assertPackage(ClassLoaderDomain.class, classLoader);
   }
   
   public void testAfterButJavaBeforeReached() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", ParentPolicy.AFTER_BUT_JAVA_BEFORE, null);
      
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      
      assertLoadClass(Object.class, classLoader, null, true);
      assertLoadClass(ClassLoaderDomain.class, classLoader, null, true);
      assertPackage(ClassLoaderDomain.class, classLoader);
   }
   
   public void testBeforeFilteredNotMatched() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      NoMatchClassFilter filter = new NoMatchClassFilter(ClassLoaderDomain.class);
      ParentPolicy parentPolicy = new ParentPolicy(filter, ClassFilter.NOTHING);
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", parentPolicy, null);
      
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      policy.setPathsAndPackageNames(ClassLoaderDomain.class);
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      
      assertLoadClass(Object.class, classLoader, null, true);
      assertLoadClass(ClassLoaderDomain.class, classLoader);
      assertTrue("Should have been filtered", filter.filtered);
      assertPackage(ClassLoaderDomain.class, classLoader, policy);
   }
   
   public void testBeforeFilteredMatched() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      MatchClassFilter filter = new MatchClassFilter(ClassLoaderDomain.class);
      ParentPolicy parentPolicy = new ParentPolicy(filter, ClassFilter.NOTHING);
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", parentPolicy, null);
      
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      policy.setPathsAndPackageNames(ClassLoaderDomain.class);
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      
      assertLoadClass(Object.class, classLoader, null, true);
      assertLoadClass(ClassLoaderDomain.class, classLoader, null, true);
      assertTrue("Should have been filtered", filter.filtered);
      assertPackage(ClassLoaderDomain.class, classLoader);
   }
   
   public void testAfterFilteredNotReached() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      NoMatchClassFilter filter = new NoMatchClassFilter(ClassLoaderDomain.class);
      ParentPolicy parentPolicy = new ParentPolicy(ClassFilter.JAVA_ONLY, filter);
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", parentPolicy, null);
      
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      policy.setPathsAndPackageNames(ClassLoaderDomain.class);
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      
      assertLoadClass(Object.class, classLoader, null, true);
      assertLoadClass(ClassLoaderDomain.class, classLoader);
      assertFalse("Should NOT have been filtered", filter.filtered);
      assertPackage(ClassLoaderDomain.class, classLoader, policy);
   }
   
   public void testAfterFilteredReachedNotMatched() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      NoMatchClassFilter filter = new NoMatchClassFilter(ClassLoaderDomain.class);
      ParentPolicy parentPolicy = new ParentPolicy(ClassFilter.JAVA_ONLY, filter);
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", parentPolicy, null);
      
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      
      assertLoadClass(Object.class, classLoader, null, true);
      assertLoadClassFail(ClassLoaderDomain.class, classLoader);
      assertTrue("Should have been filtered", filter.filtered);
   }
   
   public void testAfterFilteredReachedMatched() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      MatchClassFilter filter = new MatchClassFilter(ClassLoaderDomain.class);
      ParentPolicy parentPolicy = new ParentPolicy(ClassFilter.JAVA_ONLY, filter);
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", parentPolicy, null);
      
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      
      assertLoadClass(Object.class, classLoader, null, true);
      assertLoadClass(ClassLoaderDomain.class, classLoader, null, true);
      assertTrue("Should have been filtered", filter.filtered);
      assertPackage(ClassLoaderDomain.class, classLoader);
   }
   
   public void testNoMatchBeforeAndAfter() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      ParentPolicy parentPolicy = new ParentPolicy(ClassFilter.NOTHING, ClassFilter.NOTHING);
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", parentPolicy, null);
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      assertLoadClassFail(Object.class, classLoader);
   }
   
   public void testPackageFilterNoJava() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      PackageClassFilter filter = PackageClassFilter.createPackageClassFilter("dummy");
      ParentPolicy parentPolicy = new ParentPolicy(filter, ClassFilter.NOTHING);
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", parentPolicy, null);
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      assertLoadClassFail(Object.class, classLoader);
   }
   
   public void testPackageFilterIncludeJava() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      PackageClassFilter filter = PackageClassFilter.createPackageClassFilter("dummy");
      filter.setIncludeJava(true);
      ParentPolicy parentPolicy = new ParentPolicy(filter, ClassFilter.NOTHING);
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", parentPolicy, null);
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      assertLoadClass(Object.class, classLoader, null, true);
   }
}
