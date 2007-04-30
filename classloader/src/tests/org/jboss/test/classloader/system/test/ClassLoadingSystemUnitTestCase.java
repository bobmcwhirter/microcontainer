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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;

import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.test.classloader.AbstractClassLoaderTest;
import org.jboss.test.classloader.system.support.MockClassLoaderDomain;
import org.jboss.test.classloader.system.support.MockClassLoaderSystem;

/**
 * ClassLoadingSystemUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ClassLoadingSystemUnitTestCase extends AbstractClassLoaderTest
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
      ClassLoaderSystem instance = ClassLoaderSystem.getInstance();
      assertNotNull(instance);
   }
   
   public void testDefaultDomain() throws Exception
   {
      ClassLoaderDomain domain = createClassLoaderSystem().getDefaultDomain();
      assertNotNull(domain);
      assertEquals(ClassLoaderSystem.DEFAULT_DOMAIN_NAME, domain.getName());
   }
   
   public void testRegisterExplicitDomain() throws Exception
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
   
   public void testUnregisterExplicitDomain() throws Exception
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
