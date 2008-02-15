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
package org.jboss.test.classloader.system.test;

import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;

import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.classloader.test.support.MockClassLoaderHelper;
import org.jboss.classloader.test.support.MockClassLoaderPolicy;
import org.jboss.test.classloader.AbstractClassLoaderTestWithSecurity;
import org.jboss.test.classloader.system.support.MockClassLoaderDomain;
import org.jboss.test.classloader.system.support.MockClassLoaderSystem;

/**
 * ClassLoaderSystemUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ClassLoaderSystemUnitTestCase extends AbstractClassLoaderTestWithSecurity
{
   public static Test suite()
   {
      return suite(ClassLoaderSystemUnitTestCase.class);
   }

   public ClassLoaderSystemUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testGetInstance() throws Exception
   {
      try
      {
         ClassLoaderSystem.getInstance();
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(AccessControlException.class, e);
      }
   }

   public void testDefaultDomain() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      assertTrue(system.isRegistered(ClassLoaderSystem.DEFAULT_DOMAIN_NAME));
      ClassLoaderDomain domain = system.getDefaultDomain();
      assertNotNull(domain);
      assertEquals(ClassLoaderSystem.DEFAULT_DOMAIN_NAME, domain.getName());
      assertTrue(system.isRegistered(ClassLoaderSystem.DEFAULT_DOMAIN_NAME));
      assertTrue(system.isDomainRegistered(domain));
   }
   
   public void testRegisterDomain() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();

      assertFalse(system.isRegistered("mock"));
      MockClassLoaderDomain domain = new MockClassLoaderDomain("mock");
      assertFalse(system.isDomainRegistered(domain));
      
      system.registerDomain(domain);
      assertTrue(system.isRegistered("mock"));
      assertTrue(system.isDomainRegistered(domain));
   }
   
   public void testRegisterDomains() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();

      assertFalse(system.isRegistered("a"));
      MockClassLoaderDomain a = new MockClassLoaderDomain("a");
      assertFalse(system.isDomainRegistered(a));

      assertFalse(system.isRegistered("b"));
      MockClassLoaderDomain b = new MockClassLoaderDomain("b");
      assertFalse(system.isDomainRegistered(a));
      
      system.registerDomain(a);
      system.registerDomain(b);
      
      assertTrue(system.isRegistered("a"));
      assertTrue(system.isDomainRegistered(a));
      assertTrue(system.isRegistered("b"));
      assertTrue(system.isDomainRegistered(b));
   }
   
   public void testRegisterNullDomain() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      
      try
      {
         system.registerDomain(null);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }
   }
   
   public void testRegisterDefaultDomain() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();

      MockClassLoaderDomain domain = new MockClassLoaderDomain(ClassLoaderSystem.DEFAULT_DOMAIN_NAME);
      try
      {
         system.registerDomain(domain);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalStateException.class, e);
      }
   }
   
   public void testRegisterDuplicateDomain() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();

      assertFalse(system.isRegistered("a"));
      MockClassLoaderDomain a = new MockClassLoaderDomain("a");
      assertFalse(system.isDomainRegistered(a));
      
      system.registerDomain(a);
      
      assertTrue(system.isRegistered("a"));
      assertTrue(system.isDomainRegistered(a));
      
      try
      {
         system.registerDomain(a);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalStateException.class, e);
      }
   }
   
   public void testRegisterDuplicateDomainName() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();

      assertFalse(system.isRegistered("a"));
      MockClassLoaderDomain a = new MockClassLoaderDomain("a");
      assertFalse(system.isDomainRegistered(a));

      MockClassLoaderDomain b = new MockClassLoaderDomain("a");
      
      system.registerDomain(a);
      
      assertTrue(system.isRegistered("a"));
      assertTrue(system.isDomainRegistered(a));
      
      try
      {
         system.registerDomain(b);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalStateException.class, e);
      }
   }
   
   public void testUnregisterDomain() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();

      MockClassLoaderDomain domain = new MockClassLoaderDomain("mock");
      
      system.registerDomain(domain);
      assertTrue(system.isRegistered("mock"));
      assertTrue(system.isDomainRegistered(domain));
      
      system.unregisterDomain(domain);
      assertFalse(system.isRegistered("mock"));
      assertFalse(system.isDomainRegistered(domain));
   }
   
   public void testUnregisterWrongDomain() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();

      MockClassLoaderDomain domain = new MockClassLoaderDomain("mock");
      
      system.registerDomain(domain);
      assertTrue(system.isRegistered("mock"));
      assertTrue(system.isDomainRegistered(domain));

      MockClassLoaderDomain notDomain = new MockClassLoaderDomain("mock");
      
      try
      {
         system.unregisterDomain(notDomain);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalStateException.class, e);
      }
      assertTrue(system.isRegistered("mock"));
      assertTrue(system.isDomainRegistered(domain));
   }
   
   public void testUnregisterDefaultDomain() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      
      ClassLoaderDomain domain = system.getDefaultDomain();
      try
      {
         system.unregisterDomain(domain);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }
      assertTrue(system.isRegistered(ClassLoaderSystem.DEFAULT_DOMAIN_NAME));
      assertTrue(system.isDomainRegistered(domain));
   }
   
   public void testUnregisterNullDomain() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      
      try
      {
         system.unregisterDomain(null);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }
   }
   
   public void testUnregisterUnregisteredDomain() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystem();
      ClassLoaderDomain domain = system.createAndRegisterDomain("test");
      system.unregisterDomain(domain);
      try
      {
         system.unregisterDomain(domain);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalStateException.class, e);
      }
   }

   public void testDomainShutdownFromClassLoaderSystemShutdown() throws Exception
   {
      ClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderDomain domain = new MockClassLoaderDomain();
      system.registerDomain(domain);
      
      assertFalse(domain.shutdown);
      system.shutdown();
      assertTrue("Domain should be shutdown", domain.shutdown);
   }

   public void testDomainShutdownFromUnregister() throws Exception
   {
      ClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderDomain domain = new MockClassLoaderDomain();
      system.registerDomain(domain);
      
      assertFalse(domain.shutdown);
      system.unregisterDomain(domain);
      assertTrue("Domain should be shutdown", domain.shutdown);
   }
   
   public void testRegisterClassLoaderDefaultDomain() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderDomain domain = (MockClassLoaderDomain) system.getDefaultDomain();
      ClassLoader cl1 = createAndRegisterMockClassLoader(system);
      
      List<ClassLoader> expected = new ArrayList<ClassLoader>();
      expected.add(cl1);
      
      assertEquals(expected, domain.added);
      assertEmpty(domain.removed);

      ClassLoader cl2 = createAndRegisterMockClassLoader(system, domain);
      expected.add(cl2);
      
      assertEquals(expected, domain.added);
      assertEmpty(domain.removed);
   }
   
   public void testRegisterClassLoaderExplicitDomain() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderDomain domain = system.createAndRegisterDomain("mock");
      ClassLoader cl1 = createAndRegisterMockClassLoader(system, domain);
      
      List<ClassLoader> expected = new ArrayList<ClassLoader>();
      expected.add(cl1);
      
      assertEquals(expected, domain.added);
      assertEmpty(domain.removed);

      ClassLoader cl2 = createAndRegisterMockClassLoader(system, domain);
      expected.add(cl2);
      
      assertEquals(expected, domain.added);
      assertEmpty(domain.removed);
   }
   
   public void testRegisterClassLoaderConstructDomain() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      ClassLoader cl1 = createAndRegisterMockClassLoader(system, "test", "mock");
      MockClassLoaderDomain domain = (MockClassLoaderDomain) system.getDomain("test");
      assertEquals(ParentPolicy.BEFORE, domain.getParentPolicy());
      assertFalse(domain.getParent() instanceof ClassLoaderDomain);
      
      List<ClassLoader> expected = new ArrayList<ClassLoader>();
      expected.add(cl1);
      
      assertEquals(expected, domain.added);
      assertEmpty(domain.removed);
   }
   
   public void testRegisterClassLoaderConstructDomainWithoutParentDomain() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      ClassLoader cl1 = createAndRegisterMockClassLoader(system, "test", ParentPolicy.AFTER, "mock");
      MockClassLoaderDomain domain = (MockClassLoaderDomain) system.getDomain("test");
      assertEquals(ParentPolicy.AFTER, domain.getParentPolicy());
      assertFalse(domain.getParent() instanceof ClassLoaderDomain);
      
      List<ClassLoader> expected = new ArrayList<ClassLoader>();
      expected.add(cl1);
      
      assertEquals(expected, domain.added);
      assertEmpty(domain.removed);
   }
   
   public void testRegisterClassLoaderConstructDomainWithParentDomain() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      ClassLoader cl1 = createAndRegisterMockClassLoader(system, "test", ParentPolicy.AFTER, ClassLoaderSystem.DEFAULT_DOMAIN_NAME, "mock");
      MockClassLoaderDomain domain = (MockClassLoaderDomain) system.getDomain("test");
      MockClassLoaderDomain parent = (MockClassLoaderDomain) system.getDomain(ClassLoaderSystem.DEFAULT_DOMAIN_NAME);
      assertEquals(ParentPolicy.AFTER, domain.getParentPolicy());
      assertEquals(parent, domain.getParent());
      
      List<ClassLoader> expected = new ArrayList<ClassLoader>();
      expected.add(cl1);
      
      assertEquals(expected, domain.added);
      assertEmpty(domain.removed);
   }
   
   public void testRegisterClassLoaderConstructDomainWithParentDomainAlreadyExists() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderDomain domain = new MockClassLoaderDomain("test");
      system.registerDomain(domain);
      ClassLoader cl1 = createAndRegisterMockClassLoader(system, "test", ParentPolicy.AFTER, ClassLoaderSystem.DEFAULT_DOMAIN_NAME, "mock");
      
      List<ClassLoader> expected = new ArrayList<ClassLoader>();
      expected.add(cl1);
      
      assertEquals(expected, domain.added);
      assertEmpty(domain.removed);
   }
   
   public void testRegisterClassLoaderConstructDomainWithParentPolicyAlreadyExists() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderDomain domain = new MockClassLoaderDomain("test");
      system.registerDomain(domain);
      ClassLoader cl1 = createAndRegisterMockClassLoader(system, "test", ParentPolicy.AFTER, "mock");
      
      List<ClassLoader> expected = new ArrayList<ClassLoader>();
      expected.add(cl1);
      
      assertEquals(expected, domain.added);
      assertEmpty(domain.removed);
   }
   
   public void testRegisterClassLoaderConstructDomainAlreadyExists() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderDomain domain = new MockClassLoaderDomain("test");
      system.registerDomain(domain);
      ClassLoader cl1 = createAndRegisterMockClassLoader(system, "test", "mock");
      
      List<ClassLoader> expected = new ArrayList<ClassLoader>();
      expected.add(cl1);
      
      assertEquals(expected, domain.added);
      assertEmpty(domain.removed);
   }

   public void testRegisterNullClassLoaderPolicyDefaultDomain()
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      try
      {
         system.registerClassLoaderPolicy(null);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }
   }

   public void testRegisterNullClassLoaderPolicyExplicitDomain()
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderDomain domain = new MockClassLoaderDomain();
      system.registerDomain(domain);
      try
      {
         system.registerClassLoaderPolicy(domain, null);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }
   }

   public void testRegisterClassLoaderPolicyNullDomain()
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      try
      {
         system.registerClassLoaderPolicy((ClassLoaderDomain) null, policy);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }
   }

   public void testRegisterClassLoaderPolicyWithNotRegisteredDomain()
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderDomain domain = new MockClassLoaderDomain();
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      try
      {
         system.registerClassLoaderPolicy(domain, policy);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalStateException.class, e);
      }
   }

   public void testRegisterClassLoaderPolicyWithUnregisteredDomain()
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderDomain domain = new MockClassLoaderDomain();
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      system.registerDomain(domain);
      system.unregisterDomain(domain);
      try
      {
         system.registerClassLoaderPolicy(domain, policy);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalStateException.class, e);
      }
   }

   public void testRegisterClassLoaderPolicyTwiceDefaultDomain()
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      system.registerClassLoaderPolicy(policy);
      try
      {
         system.registerClassLoaderPolicy(policy);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalStateException.class, e);
      }
   }

   public void testRegisterClassLoaderPolicyTwiceExplicitDomain()
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderDomain domain = new MockClassLoaderDomain();
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      system.registerDomain(domain);
      system.registerClassLoaderPolicy(domain, policy);
      try
      {
         system.registerClassLoaderPolicy(domain, policy);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalStateException.class, e);
      }
   }

   public void testRegisterClassLoaderPolicyTwiceDifferentDomains()
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderDomain domain1 = new MockClassLoaderDomain("domain1");
      MockClassLoaderDomain domain2 = new MockClassLoaderDomain("domain2");
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      system.registerDomain(domain1);
      system.registerDomain(domain2);
      system.registerClassLoaderPolicy(domain1, policy);
      try
      {
         system.registerClassLoaderPolicy(domain2, policy);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalStateException.class, e);
      }
   }

   public void testRegisterClassLoaderPolicyAndConstructDomainNoParentDomain()
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      try
      {
         system.registerClassLoaderPolicy("test", ParentPolicy.BEFORE, "DOESNOTEXIST", policy);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalStateException.class, e);
      }
      assertFalse(system.isRegistered("test"));
   }

   public void testRegisterClassLoaderPolicyAndConstructDomainNoParentPolicy()
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      try
      {
         system.registerClassLoaderPolicy("test", null, ClassLoaderSystem.DEFAULT_DOMAIN_NAME, policy);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }
      try
      {
         system.registerClassLoaderPolicy("test", null, policy);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }
      assertFalse(system.isRegistered("test"));
   }

   public void testRegisterClassLoaderPolicyAndConstructDomainNoDomainName()
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      try
      {
         system.registerClassLoaderPolicy(null, ParentPolicy.BEFORE, ClassLoaderSystem.DEFAULT_DOMAIN_NAME, policy);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }
      try
      {
         system.registerClassLoaderPolicy(null, ParentPolicy.BEFORE, policy);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }
      try
      {
         system.registerClassLoaderPolicy((String) null, policy);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }
   }
   
   public void testUnregisterClassLoaderDefaultDomain() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderDomain domain = (MockClassLoaderDomain) system.getDefaultDomain();
      ClassLoader cl1 = createAndRegisterMockClassLoader(system);
      
      List<ClassLoader> expected = new ArrayList<ClassLoader>();
      expected.add(cl1);
      
      assertEquals(expected, domain.added);
      assertEmpty(domain.removed);

      system.unregisterClassLoader(cl1);
      
      assertEquals(expected, domain.added);
      assertEquals(expected, domain.removed);
   }
   
   public void testUnregisterClassLoaderExplicitDomain() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderDomain domain = system.createAndRegisterDomain("mock");
      ClassLoader cl1 = createAndRegisterMockClassLoader(system, domain);
      
      List<ClassLoader> expected = new ArrayList<ClassLoader>();
      expected.add(cl1);
      
      assertEquals(expected, domain.added);
      assertEmpty(domain.removed);

      system.unregisterClassLoader(cl1);
      
      assertEquals(expected, domain.added);
      assertEquals(expected, domain.removed);
   }
   
   public void testUnregisterClassLoaderPolicyExplicitDomain() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      MockClassLoaderDomain domain = system.createAndRegisterDomain("mock");
      ClassLoader cl1 = MockClassLoaderHelper.registerMockClassLoader(system, domain, policy);
      
      List<ClassLoader> expected = new ArrayList<ClassLoader>();
      expected.add(cl1);
      
      assertEquals(expected, domain.added);
      assertEmpty(domain.removed);

      system.unregisterClassLoaderPolicy(policy);
      
      assertEquals(expected, domain.added);
      assertEquals(expected, domain.removed);
   }
   
   public void testUnregisterNullClassLoader() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();

      try
      {
         system.unregisterClassLoader(null);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }
   }
   
   public void testUnregisterNullClassLoaderPolicy() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();

      try
      {
         system.unregisterClassLoaderPolicy(null);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }
   }
   
   public void testUnregisterClassLoaderUnregistered() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      ClassLoader cl = system.registerClassLoaderPolicy(policy);
      system.unregisterClassLoaderPolicy(policy);

      try
      {
         system.unregisterClassLoader(cl);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalStateException.class, e);
      }
   }
   
   public void testUnregisterClassLoaderPolicyNotRegistered() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();

      try
      {
         system.unregisterClassLoaderPolicy(policy);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalStateException.class, e);
      }
   }
   
   public void testUnregisterClassLoaderPolicyUnregistered() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      system.registerClassLoaderPolicy(policy);
      system.unregisterClassLoaderPolicy(policy);

      try
      {
         system.unregisterClassLoaderPolicy(policy);
         fail("Should not be here!");
      }
      catch (Exception e)
      {
         checkThrowable(IllegalStateException.class, e);
      }
   }

   protected MockClassLoaderSystem createMockClassLoaderSystem()
   {
      return new MockClassLoaderSystem();
   }
}
