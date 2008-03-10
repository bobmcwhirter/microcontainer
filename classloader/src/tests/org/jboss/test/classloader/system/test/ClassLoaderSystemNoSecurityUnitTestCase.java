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

import java.net.URL;
import java.net.URLClassLoader;

import junit.framework.Test;

import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.test.classloader.AbstractClassLoaderTest;
import org.jboss.test.classloader.system.support.MockClassLoaderDomain;
import org.jboss.test.classloader.system.support.MockClassLoaderSystem;
import org.jboss.test.classloader.system.support.SimpleMockClassLoaderPolicy;

/**
 * ClassLoaderSystemUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ClassLoaderSystemNoSecurityUnitTestCase extends AbstractClassLoaderTest
{
   public static Test suite()
   {
      return suite(ClassLoaderSystemNoSecurityUnitTestCase.class);
   }

   public ClassLoaderSystemNoSecurityUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testGetInstance() throws Exception
   {
      ClassLoaderSystem instance = ClassLoaderSystem.getInstance();
      assertNotNull(instance);
      
      ClassLoaderSystem instance2 = ClassLoaderSystem.getInstance();
      assertTrue("Should be the same instance", instance == instance2);
   }
   
   public void testClassLoaderPolicyShutdownFromClassLoaderSystemShutdown() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      SimpleMockClassLoaderPolicy policy = new SimpleMockClassLoaderPolicy();
      MockClassLoaderDomain domain = system.createAndRegisterDomain("mock");
      system.registerClassLoaderPolicy(domain, policy);

      assertFalse(policy.shutdown);
      system.shutdown();
      assertTrue("Policy should be shutdown", policy.shutdown);
   }
   
   public void testClassLoaderPolicyShutdownFromClassLoaderDomainUnregister() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      SimpleMockClassLoaderPolicy policy = new SimpleMockClassLoaderPolicy();
      MockClassLoaderDomain domain = system.createAndRegisterDomain("mock");
      system.registerClassLoaderPolicy(domain, policy);

      assertFalse(policy.shutdown);
      system.unregisterDomain(domain);
      assertTrue("Policy should be shutdown", policy.shutdown);
   }
   
   public void testClassLoaderPolicyShutdownFromClassLoaderPolicyUnregister() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      SimpleMockClassLoaderPolicy policy = new SimpleMockClassLoaderPolicy();
      MockClassLoaderDomain domain = system.createAndRegisterDomain("mock");
      system.registerClassLoaderPolicy(domain, policy);

      assertFalse(policy.shutdown);
      system.unregisterClassLoaderPolicy(policy);
      assertTrue("Policy should be shutdown", policy.shutdown);
   }
   
   public void testClassLoaderPolicyShutdownFromClassLoaderUnregister() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      SimpleMockClassLoaderPolicy policy = new SimpleMockClassLoaderPolicy();
      MockClassLoaderDomain domain = system.createAndRegisterDomain("mock");
      ClassLoader cl = system.registerClassLoaderPolicy(domain, policy);

      assertFalse(policy.shutdown);
      system.unregisterClassLoader(cl);
      assertTrue("Policy should be shutdown", policy.shutdown);
   }
   
   public void testUnregisterClassLoaderNotRegistered() throws Exception
   {
      MockClassLoaderSystem system = createMockClassLoaderSystem();
      ClassLoader cl = new URLClassLoader(new URL[0]);

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

   protected MockClassLoaderSystem createMockClassLoaderSystem()
   {
      return new MockClassLoaderSystem();
   }
}
