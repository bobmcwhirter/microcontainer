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
import org.jboss.test.classloader.AbstractClassLoaderTestWithSecurity;
import org.jboss.test.classloader.system.support.MockClassLoaderDomain;
import org.jboss.test.classloader.system.support.MockClassLoaderSystem;

/**
 * ClassLoadingSystemUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ClassLoadingSystemUnitTestCase extends AbstractClassLoaderTestWithSecurity
{
   public static Test suite()
   {
      return suite(ClassLoadingSystemUnitTestCase.class);
   }

   public ClassLoadingSystemUnitTestCase(String name)
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
   
   protected MockClassLoaderSystem createMockClassLoaderSystem()
   {
      return new MockClassLoaderSystem();
   }
}
