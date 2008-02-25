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

import java.util.HashSet;
import java.util.Set;
import javax.management.MBeanRegistration;

import junit.framework.Test;

import org.jboss.classloader.plugins.ClassLoaderUtils;
import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderDomainMBean;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.Loader;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.classloader.spi.base.BaseClassLoaderDomain;
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
public class CustomParentLoaderUnitTestCase extends AbstractClassLoaderTestWithSecurity
{
   public static Test suite()
   {
      return suite(CustomParentLoaderUnitTestCase.class);
   }

   public CustomParentLoaderUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testCustomLoaderBefore() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      MockLoader loader = new MockLoader();
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", ParentPolicy.BEFORE, loader);
      
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      policy.setPathsAndPackageNames(MockLoader.class);
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      
      assertLoadClass(MockLoader.class, classLoader, null, true);
      checkGetResource(loader, MockLoader.class);
      checkLoadClass(loader, MockLoader.class);
   }
   
   public void testCustomLoaderBeforeNotFound() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      MockLoader loader = new MockLoader();
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", ParentPolicy.BEFORE, loader);
      
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      policy.setPathsAndPackageNames(ClassLoaderDomain.class);
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      
      assertLoadClass(ClassLoaderDomain.class, classLoader);
      checkGetResource(loader, ClassLoaderDomain.class, BaseClassLoaderDomain.class, ClassLoaderDomainMBean.class, MBeanRegistration.class, Loader.class, Object.class);
      checkLoadClass(loader);
   }
   
   public void testCustomLoaderAfterNotReached() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      MockLoader loader = new MockLoader();
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", ParentPolicy.AFTER, loader);
      
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      policy.setPathsAndPackageNames(MockLoader.class, Loader.class);
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      
      assertLoadClass(MockLoader.class, classLoader);
      checkGetResource(loader);
      checkLoadClass(loader);
   }
   
   public void testCustomLoaderAfterReached() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      MockLoader loader = new MockLoader();
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", ParentPolicy.AFTER, loader);
      
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      
      assertLoadClass(MockLoader.class, classLoader, null, true);
      checkGetResource(loader, MockLoader.class);
      checkLoadClass(loader, MockLoader.class);
   }
   
   public void testCustomLoaderFiltered() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      MockLoader loader = new MockLoader();
      NoMatchClassFilter filter = new NoMatchClassFilter(MockLoader.class);
      ParentPolicy parentPolicy = new ParentPolicy(filter, ClassFilter.NOTHING);
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", parentPolicy, loader);
      
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      policy.setPathsAndPackageNames(MockLoader.class, Loader.class);
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);
      
      assertLoadClass(MockLoader.class, classLoader);
      checkGetResource(loader);
      checkLoadClass(loader);
      assertTrue("Should have been filtered", filter.filtered);
   }
   
   protected void checkGetResource(MockLoader loader, Class<?>... classes)
   {
      if (classes == null || classes.length == 0)
      {
         assertEmpty(loader.getResources);
         return;
      }
      Set<String> resourcePaths = new HashSet<String>();
      for (Class<?> clazz : classes)
         resourcePaths.add(ClassLoaderUtils.classNameToPath(clazz.getName()));
      assertEquals(resourcePaths, loader.getResource);
   }
   
   protected void checkLoadClass(MockLoader loader, Class<?>... classes)
   {
      if (classes == null || classes.length == 0)
      {
         assertEmpty(loader.loadClass);
         return;
      }
      Set<String> classNames = new HashSet<String>();
      for (Class<?> clazz : classes)
         classNames.add(clazz.getName());
      assertEquals(classNames, loader.loadClass);
   }
}
