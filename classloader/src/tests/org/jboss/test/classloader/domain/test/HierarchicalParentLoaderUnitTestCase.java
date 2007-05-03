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
import org.jboss.classloader.spi.Loader;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.classloader.test.support.MockClassLoaderPolicy;
import org.jboss.test.classloader.AbstractClassLoaderTestWithSecurity;
import org.jboss.test.classloader.domain.support.MockLoader;
import org.jboss.test.classloader.domain.support.NoMatchClassFilter;

/**
 * ParentPolicyUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class HierarchicalParentLoaderUnitTestCase extends AbstractClassLoaderTestWithSecurity
{
   public static Test suite()
   {
      return suite(HierarchicalParentLoaderUnitTestCase.class);
   }

   public HierarchicalParentLoaderUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testHierarchyBefore() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      ClassLoaderDomain parent = system.createAndRegisterDomain("parent", ParentPolicy.BEFORE_BUT_JAVA_ONLY);
      ClassLoaderDomain child = system.createAndRegisterDomain("child", ParentPolicy.BEFORE, parent);
      
      MockClassLoaderPolicy parentPolicy = createMockClassLoaderPolicy();
      parentPolicy.setPathsAndPackageNames(MockLoader.class, Loader.class);
      ClassLoader parentClassLoader = system.registerClassLoaderPolicy(parent, parentPolicy);
      
      MockClassLoaderPolicy childPolicy = createMockClassLoaderPolicy();
      childPolicy.setPathsAndPackageNames(MockLoader.class, Loader.class);
      ClassLoader classLoader = system.registerClassLoaderPolicy(child, childPolicy);
      
      assertLoadClass(MockLoader.class, classLoader, parentClassLoader);
   }
   
   public void testHierarchyBeforeNotFound() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      ClassLoaderDomain parent = system.createAndRegisterDomain("parent", ParentPolicy.BEFORE_BUT_JAVA_ONLY);
      ClassLoaderDomain child = system.createAndRegisterDomain("child", ParentPolicy.BEFORE, parent);
      
      MockClassLoaderPolicy childPolicy = createMockClassLoaderPolicy();
      childPolicy.setPathsAndPackageNames(MockLoader.class, Loader.class);
      ClassLoader classLoader = system.registerClassLoaderPolicy(child, childPolicy);
      
      assertLoadClass(MockLoader.class, classLoader);
   }
   
   public void testHierarchyAfterNotReached() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      ClassLoaderDomain parent = system.createAndRegisterDomain("parent", ParentPolicy.BEFORE_BUT_JAVA_ONLY);
      ClassLoaderDomain child = system.createAndRegisterDomain("child", ParentPolicy.AFTER_BUT_JAVA_BEFORE, parent);
      
      MockClassLoaderPolicy parentPolicy = createMockClassLoaderPolicy();
      parentPolicy.setPathsAndPackageNames(MockLoader.class, Loader.class);
      system.registerClassLoaderPolicy(parent, parentPolicy);
      
      MockClassLoaderPolicy childPolicy = createMockClassLoaderPolicy();
      childPolicy.setPathsAndPackageNames(MockLoader.class, Loader.class);
      ClassLoader classLoader = system.registerClassLoaderPolicy(child, childPolicy);
      
      assertLoadClass(MockLoader.class, classLoader);
   }
   
   public void testHierarchyAfterReached() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      ClassLoaderDomain parent = system.createAndRegisterDomain("parent", ParentPolicy.BEFORE_BUT_JAVA_ONLY);
      ClassLoaderDomain child = system.createAndRegisterDomain("child", ParentPolicy.AFTER_BUT_JAVA_BEFORE, parent);
      
      MockClassLoaderPolicy parentPolicy = createMockClassLoaderPolicy();
      parentPolicy.setPathsAndPackageNames(MockLoader.class, Loader.class);
      ClassLoader parentClassLoader = system.registerClassLoaderPolicy(parent, parentPolicy);
      
      MockClassLoaderPolicy childPolicy = createMockClassLoaderPolicy();
      ClassLoader classLoader = system.registerClassLoaderPolicy(child, childPolicy);
      
      assertLoadClass(MockLoader.class, classLoader, parentClassLoader);
   }
   
   public void testHierarchyFiltered() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      NoMatchClassFilter filter = new NoMatchClassFilter(MockLoader.class);
      ParentPolicy pp = new ParentPolicy(filter, ClassFilter.NOTHING);
      ClassLoaderDomain parent = system.createAndRegisterDomain("parent", ParentPolicy.BEFORE_BUT_JAVA_ONLY);
      ClassLoaderDomain child = system.createAndRegisterDomain("child", pp, parent);
      
      MockClassLoaderPolicy parentPolicy = createMockClassLoaderPolicy();
      parentPolicy.setPathsAndPackageNames(MockLoader.class, Loader.class);
      system.registerClassLoaderPolicy(parent, parentPolicy);
      
      MockClassLoaderPolicy childPolicy = createMockClassLoaderPolicy();
      childPolicy.setPathsAndPackageNames(MockLoader.class, Loader.class);
      ClassLoader classLoader = system.registerClassLoaderPolicy(child, childPolicy);
      
      assertLoadClass(MockLoader.class, classLoader);
      assertTrue("Should have been filtered", filter.filtered);
   }
}
