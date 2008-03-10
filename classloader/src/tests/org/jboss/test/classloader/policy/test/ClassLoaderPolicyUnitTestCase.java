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
package org.jboss.test.classloader.policy.test;

import java.io.FilePermission;
import java.net.URL;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.ProtectionDomain;
import java.util.Collections;

import javax.naming.Context;

import junit.framework.Test;

import org.jboss.classloader.plugins.ClassLoaderUtils;
import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.classloader.spi.filter.FilteredDelegateLoader;
import org.jboss.classloader.test.support.MockClassLoaderPolicy;
import org.jboss.test.classloader.AbstractClassLoaderTest;
import org.jboss.test.classloader.policy.support.TestClass;
import org.jboss.test.classloader.policy.support.TestClassLoaderPolicy;
import org.jboss.test.classloader.policy.support.TestClassLoaderPolicyFactory;
import org.jboss.test.classloader.policy.support.TestDelegateLoader;

/**
 * ClassLoaderSystemUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ClassLoaderPolicyUnitTestCase extends AbstractClassLoaderTest
{
   public static Test suite()
   {
      return suite(ClassLoaderPolicyUnitTestCase.class);
   }

   public ClassLoaderPolicyUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testLoadClassInvoked() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      TestClassLoaderPolicy policy = new TestClassLoaderPolicy();
      ClassLoader classLoader = system.registerClassLoaderPolicy(policy);
      
      Class<?> clazz = assertLoadClass(TestClass.class, classLoader);
      assertEquals("getResourceAsStream should have been invoked", TestClassLoaderPolicy.testClassPath, policy.getResourceAsStreamInvoked);
      assertEquals("getResource should have been invoked", TestClassLoaderPolicy.testClassPath, policy.getResourceInvoked);

      Package pkge = clazz.getPackage();
      getLog().debug("Package " + pkge);
      assertEquals(ClassLoaderUtils.getClassPackageName(TestClass.class.getName()), pkge.getName());
      assertEquals("SpecTitle", pkge.getSpecificationTitle());
      assertEquals("SpecVendor", pkge.getSpecificationVendor());
      assertEquals("SpecVersion", pkge.getSpecificationVersion());
      assertEquals("ImplTitle", pkge.getImplementationTitle());
      assertEquals("ImplVendor", pkge.getImplementationVendor());
      assertEquals("ImplVersion", pkge.getImplementationVersion());
      assertTrue("Package should be sealed ", pkge.isSealed());
      assertTrue("URL should be sealed ", pkge.isSealed(TestClassLoaderPolicy.sealBase));
      
      ProtectionDomain pd = clazz.getProtectionDomain();
      CodeSource cs = pd.getCodeSource();
      assertEquals(TestClassLoaderPolicy.codeSourceURL, cs.getLocation());
      
      PermissionCollection permissions = pd.getPermissions();
      URL url = getClass().getClassLoader().getResource("java/lang/Object.class");
      String file = url.getFile();
      FilePermission fp = new FilePermission(file, "read");
      assertTrue("Should have read permission", permissions.implies(fp));
      RuntimePermission rp = new RuntimePermission("createClassLoader");
      assertFalse("Shouldn't have classloader permissions", permissions.implies(rp));
   }
   
   public void testGetResourceInvoked() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      TestClassLoaderPolicy policy = new TestClassLoaderPolicy();
      ClassLoader classLoader = system.registerClassLoaderPolicy(policy);
      
      classLoader.getResource("test");
      assertEquals("getResource() should have been invoked", "test", policy.getResourceInvoked);
   }
   
   public void testGetResourcesInvoked() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      TestClassLoaderPolicy policy = new TestClassLoaderPolicy();
      ClassLoader classLoader = system.registerClassLoaderPolicy(policy);
      
      classLoader.getResources("test");
      assertEquals("getResources() should have been invoked", "test", policy.getResourcesInvoked);
   }
   
   public void testIsJDKRequest() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      // Hide java
      ParentPolicy parentPolicy = new ParentPolicy(ClassFilter.NOTHING, ClassFilter.NOTHING);
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", parentPolicy);
      TestClassLoaderPolicy policy = new TestClassLoaderPolicy();
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);

      // Can't see java
      assertLoadClassFail(Object.class, classLoader);

      // Should be able to load javax.naming.Context
      assertLoadClass(Context.class, classLoader, null, true);
   }
   
   public void testNotImportAll() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy mock = createMockClassLoaderPolicy();
      mock.setPathsAndPackageNames(ClassLoaderDomain.class);
      system.registerClassLoaderPolicy(mock);

      TestClassLoaderPolicy policy = new TestClassLoaderPolicy();
      ClassLoader classLoader = system.registerClassLoaderPolicy(policy);
      
      assertLoadClassFail(ClassLoaderDomain.class, classLoader);
      assertNotNull("Should have tried to load through us ", policy.getResourceInvoked);
   }
   
   public void testImportAll() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy mock = createMockClassLoaderPolicy();
      mock.setPathsAndPackageNames(ClassLoaderDomain.class);
      ClassLoader mockClassLoader = system.registerClassLoaderPolicy(mock);

      TestClassLoaderPolicy policy = new TestClassLoaderPolicy();
      policy.isImportAll = true;
      ClassLoader classLoader = system.registerClassLoaderPolicy(policy);
      
      assertLoadClass(ClassLoaderDomain.class, classLoader, mockClassLoader);
      assertNull("Should NOT have tried to load through us ", policy.getResourceInvoked);
   }
   
   public void testPackageIndexed() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy mock = createMockClassLoaderPolicy();
      mock.setImportAll(true);
      ClassLoader mockClassLoader = system.registerClassLoaderPolicy(mock);

      TestClassLoaderPolicy policy = new TestClassLoaderPolicy();
      policy.packageNames = new String[] { ClassLoaderUtils.getClassPackageName(ClassLoaderDomain.class.getName())};
      system.registerClassLoaderPolicy(policy);
      
      assertLoadClassFail(ClassLoaderDomain.class, mockClassLoader);
      assertNotNull("Should have tried to load through us ", policy.getResourceInvoked);
   }
   
   public void testPackageNotIndexed() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy mock = createMockClassLoaderPolicy();
      mock.setImportAll(true);
      ClassLoader mockClassLoader = system.registerClassLoaderPolicy(mock);

      TestClassLoaderPolicy policy = new TestClassLoaderPolicy();
      system.registerClassLoaderPolicy(policy);
      
      assertLoadClassFail(ClassLoaderDomain.class, mockClassLoader);
      assertNull("Should NOT have tried to load through us ", policy.getResourceInvoked);
   }
   
   public void testDelegateInvoked() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      TestClassLoaderPolicy policy1 = new TestClassLoaderPolicy();
      TestClassLoaderPolicy policy2 = new TestClassLoaderPolicy();
      policy1.delegates = Collections.singletonList(new FilteredDelegateLoader(policy2));
      ClassLoader classLoader = system.registerClassLoaderPolicy(policy1);
      system.registerClassLoaderPolicy(policy2);
      
      assertLoadClassFail(ClassLoaderDomain.class, classLoader);
      assertNotNull("Should have tried policy1 ", policy1.getResourceInvoked);
      assertNotNull("Should have tried policy2 ", policy2.getResourceInvoked);
   }
   
   public void testDelegateInvokedSuccessfully() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      TestClassLoaderPolicy policy1 = new TestClassLoaderPolicy();
      TestClassLoaderPolicy policy2 = new TestClassLoaderPolicy();
      policy1.delegates = Collections.singletonList(new FilteredDelegateLoader(policy2));
      ClassLoader classLoader1 = system.registerClassLoaderPolicy(policy1);
      ClassLoader classLoader2 = system.registerClassLoaderPolicy(policy2);
      
      assertLoadClass(TestClass.class, classLoader1, classLoader2);
      assertNull("Should NOT have tried policy1 ", policy1.getResourceInvoked);
      assertNotNull("Should have tried policy2 ", policy2.getResourceInvoked);
   }
   
   public void testNonDelegateNotInvoked() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      TestClassLoaderPolicy policy1 = new TestClassLoaderPolicy();
      TestClassLoaderPolicy policy2 = new TestClassLoaderPolicy();
      ClassLoader classLoader = system.registerClassLoaderPolicy(policy1);
      system.registerClassLoaderPolicy(policy2);
      
      assertLoadClassFail(ClassLoaderDomain.class, classLoader);
      assertNotNull("Should have tried policy1 ", policy1.getResourceInvoked);
      assertNull("Should NOT have tried policy2 ", policy2.getResourceInvoked);
   }
   
   public void testExportLoaderInvoked() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy mock = createMockClassLoaderPolicy();
      mock.setImportAll(true);
      ClassLoader mockClassLoader = system.registerClassLoaderPolicy(mock);

      TestClassLoaderPolicy policy = new TestClassLoaderPolicy();
      policy.packageNames = new String[] { ClassLoaderUtils.getClassPackageName(ClassLoaderDomain.class.getName())};
      TestDelegateLoader exported = new TestDelegateLoader(policy);
      policy.exported = exported;
      system.registerClassLoaderPolicy(policy);
      
      assertLoadClassFail(ClassLoaderDomain.class, mockClassLoader);
      assertNotNull("Should have tried to the export delegate ", exported.getResourceInvoked);
      assertNotNull("Should have tried to the export delegate to load the class ", exported.loadClassInvoked);
      assertNull("Should NOT have tried to load through us ", policy.getResourceInvoked);
   }
   
   public void testFactoryInvoked() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      TestClassLoaderPolicy policy1 = new TestClassLoaderPolicy();
      TestClassLoaderPolicy policy2 = new TestClassLoaderPolicy();
      TestClassLoaderPolicyFactory factory2 = new TestClassLoaderPolicyFactory(policy2, true);
      policy1.delegates = Collections.singletonList(new FilteredDelegateLoader(factory2));
      ClassLoader classLoader = system.registerClassLoaderPolicy(policy1);
      system.registerClassLoaderPolicy(policy2);
      
      assertLoadClassFail(ClassLoaderDomain.class, classLoader);
      assertNotNull("Should have tried policy1 ", policy1.getResourceInvoked);
      assertNotNull("Should have tried policy2 ", policy2.getResourceInvoked);
   }
   
   public void testFactoryInvokedSuccessfully() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      TestClassLoaderPolicy policy1 = new TestClassLoaderPolicy();
      TestClassLoaderPolicy policy2 = new TestClassLoaderPolicy();
      TestClassLoaderPolicyFactory factory2 = new TestClassLoaderPolicyFactory(policy2, true);
      policy1.delegates = Collections.singletonList(new FilteredDelegateLoader(factory2));
      ClassLoader classLoader1 = system.registerClassLoaderPolicy(policy1);
      ClassLoader classLoader2 = system.registerClassLoaderPolicy(policy2);
      
      assertLoadClass(TestClass.class, classLoader1, classLoader2);
      assertNull("Should NOT have tried policy1 ", policy1.getResourceInvoked);
      assertNotNull("Should have tried policy2 ", policy2.getResourceInvoked);
   }
   
   public void testLazyFactoryInvoked() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      TestClassLoaderPolicy policy1 = new TestClassLoaderPolicy();
      TestClassLoaderPolicy policy2 = new TestClassLoaderPolicy();
      TestClassLoaderPolicyFactory factory2 = new TestClassLoaderPolicyFactory(policy2, false);
      policy1.delegates = Collections.singletonList(new FilteredDelegateLoader(factory2));
      ClassLoader classLoader = system.registerClassLoaderPolicy(policy1);
      system.registerClassLoaderPolicy(policy2);
      
      assertLoadClassFail(ClassLoaderDomain.class, classLoader);
      assertNotNull("Should have tried policy1 ", policy1.getResourceInvoked);
      assertNull("Should have tried policy2 ", policy2.getResourceInvoked);

      factory2.setCanCreate(true);
      policy1.getResourceInvoked = null;
      assertLoadClassFail(ClassLoaderDomain.class, classLoader);
      assertNull("Should NOT have tried policy1 ", policy1.getResourceInvoked);
      assertNotNull("Should have tried policy2 ", policy2.getResourceInvoked);
   }
   
   public void testLazyFactoryInvokedSuccessfully() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy policy1 = createMockClassLoaderPolicy();
      TestClassLoaderPolicy policy2 = new TestClassLoaderPolicy();
      TestClassLoaderPolicyFactory factory2 = new TestClassLoaderPolicyFactory(policy2, false);
      policy1.setDelegates(Collections.singletonList(new FilteredDelegateLoader(factory2)));
      ClassLoader classLoader1 = system.registerClassLoaderPolicy(policy1);
      ClassLoader classLoader2 = system.registerClassLoaderPolicy(policy2);

      assertLoadClassFail(TestClass.class, classLoader1);
      assertNull("Should NOT have tried policy2 ", policy2.getResourceInvoked);
      
      factory2.setCanCreate(true);
      assertLoadClass(TestClass.class, classLoader1, classLoader2);
      assertNotNull("Should have tried policy2 ", policy2.getResourceInvoked);
   }
   
   public void testExportFactoryInvoked() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy mock = createMockClassLoaderPolicy();
      mock.setImportAll(true);
      ClassLoader mockClassLoader = system.registerClassLoaderPolicy(mock);

      TestClassLoaderPolicy policy = new TestClassLoaderPolicy();
      TestClassLoaderPolicyFactory factory = new TestClassLoaderPolicyFactory(policy, true);
      policy.packageNames = new String[] { ClassLoaderUtils.getClassPackageName(ClassLoaderDomain.class.getName())};
      TestDelegateLoader exported = new TestDelegateLoader(factory);
      policy.exported = exported;
      system.registerClassLoaderPolicy(policy);
      
      assertLoadClassFail(ClassLoaderDomain.class, mockClassLoader);
      assertNotNull("Should have tried to the export delegate ", exported.getResourceInvoked);
      assertNotNull("Should have tried to the export delegate to load the class ", exported.loadClassInvoked);
      assertNull("Should NOT have tried to load through us ", policy.getResourceInvoked);
   }
   
   public void testLazyExportNotAllowed() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      TestClassLoaderPolicy policy = new TestClassLoaderPolicy();
      TestClassLoaderPolicyFactory factory = new TestClassLoaderPolicyFactory(policy, false);
      policy.packageNames = new String[] { ClassLoaderUtils.getClassPackageName(ClassLoaderDomain.class.getName())};
      TestDelegateLoader exported = new TestDelegateLoader(factory);
      policy.exported = exported;
      try
      {
         system.registerClassLoaderPolicy(policy);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalStateException.class, t);
      }
   }
   
   public void testCircularFactoryInDelegates() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      TestClassLoaderPolicy policy1 = new TestClassLoaderPolicy();
      TestClassLoaderPolicy policy2 = new TestClassLoaderPolicy();
      TestClassLoaderPolicyFactory factory1 = new TestClassLoaderPolicyFactory(policy1, true);
      TestClassLoaderPolicyFactory factory2 = new TestClassLoaderPolicyFactory(policy2, true);
      policy1.setDelegates(Collections.singletonList(new FilteredDelegateLoader(factory2)));
      policy2.setDelegates(Collections.singletonList(new FilteredDelegateLoader(factory1)));
      ClassLoader classLoader1 = system.registerClassLoaderPolicy(policy1);
      ClassLoader classLoader2 = system.registerClassLoaderPolicy(policy2);

      assertLoadClass(TestClass.class, classLoader1, classLoader2);
      assertNull("Should NOT have tried policy1 ", policy1.getResourceInvoked);
      assertNotNull("Should have tried policy2 ", policy2.getResourceInvoked);

      policy1.getResourceInvoked = null;
      assertLoadClass(TestClass.class, classLoader2);
      assertNull("Should NOT have tried policy1 ", policy1.getResourceInvoked);
      assertNotNull("Should have tried policy2 ", policy2.getResourceInvoked);
   }
}
