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
package org.jboss.classloader.test.support;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.jboss.classloader.plugins.system.DefaultClassLoaderSystem;
import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.Loader;
import org.jboss.classloader.spi.ParentPolicy;

/**
 * MockClassLoaderHelper<p>
 * 
 * WARNING. This class/package should be excluded from the release
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MockClassLoaderHelper
{
   /**
    * Create a mock classloading system<p>
    * 
    * No security problem. A user can create their own classloader system, so what?
    * 
    * @return the new classloading system
    */
   public static ClassLoaderSystem createMockClassLoaderSystem()
   {
      return new DefaultClassLoaderSystem();
   }
   
   /**
    * Create a mock classloader policy with the given name
    * 
    * @param name the name
    * @return the policy
    */
   public static MockClassLoaderPolicy createMockClassLoaderPolicy(final String name)
   {
      return AccessController.doPrivileged(new PrivilegedAction<MockClassLoaderPolicy>()
      {
         public MockClassLoaderPolicy run()
         {
            return new MockClassLoaderPolicy(name);
         }
      });
   }
   
   /**
    * Create and register a mock classloader policy<p>
    * 
    * No security problem here. The user needs access to a ClassLoaderSystem
    * to register a classloader with it.
    * 
    * @param system the system
    * @param domain the domain
    * @param name the name
    * @return the classloader
    */
   public static ClassLoader createAndRegisterMockClassLoader(final ClassLoaderSystem system, final ClassLoaderDomain domain, final String name)
   {
      return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>()
      {
         public ClassLoader run()
         {
            MockClassLoaderPolicy policy = new MockClassLoaderPolicy(name);
            if (domain == null)
               return system.registerClassLoaderPolicy(policy);
            else
               return system.registerClassLoaderPolicy(domain, policy);
         }
      });
   }
   
   /**
    * Create and register a mock classloader policy<p>
    * 
    * No security problem here. The user needs access to a ClassLoaderSystem
    * to register a classloader with it.
    * 
    * @param system the system
    * @param domainName the domain name
    * @param name the name
    * @return the classloader
    */
   public static ClassLoader createAndRegisterMockClassLoader(final ClassLoaderSystem system, final String domainName, final String name)
   {
      return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>()
      {
         public ClassLoader run()
         {
            MockClassLoaderPolicy policy = new MockClassLoaderPolicy(name);
            return system.registerClassLoaderPolicy(domainName, policy);
         }
      });
   }
   
   /**
    * Create and register a mock classloader policy<p>
    * 
    * No security problem here. The user needs access to a ClassLoaderSystem
    * to register a classloader with it.
    * 
    * @param system the system
    * @param domainName the domain name
    * @param parentPolicy the parent policy
    * @param name the name
    * @return the classloader
    */
   public static ClassLoader createAndRegisterMockClassLoader(final ClassLoaderSystem system, final String domainName, final ParentPolicy parentPolicy, final String name)
   {
      return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>()
      {
         public ClassLoader run()
         {
            MockClassLoaderPolicy policy = new MockClassLoaderPolicy(name);
            return system.registerClassLoaderPolicy(domainName, parentPolicy, policy);
         }
      });
   }
   
   /**
    * Create and register a mock classloader policy<p>
    * 
    * No security problem here. The user needs access to a ClassLoaderSystem
    * to register a classloader with it.
    * 
    * @param system the system
    * @param domainName the domain name
    * @param parentPolicy the parent policy
    * @param parentDomainName the parent domain name
    * @param name the name
    * @return the classloader
    */
   public static ClassLoader createAndRegisterMockClassLoader(final ClassLoaderSystem system, final String domainName, final ParentPolicy parentPolicy, final String parentDomainName, final String name)
   {
      return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>()
      {
         public ClassLoader run()
         {
            MockClassLoaderPolicy policy = new MockClassLoaderPolicy(name);
            return system.registerClassLoaderPolicy(domainName, parentPolicy, parentDomainName, policy);
         }
      });
   }
   
   /**
    * Create and register a mock classloader policy<p>
    * 
    * No security problem here. The user needs access to a ClassLoaderSystem
    * to register a classloader with it.
    * 
    * @param system the system
    * @param domainName the domain name
    * @param parentPolicy the parent policy
    * @param parent the parent loader
    * @param name the name
    * @return the classloader
    */
   public static ClassLoader createAndRegisterMockClassLoader(final ClassLoaderSystem system, final String domainName, final ParentPolicy parentPolicy, final Loader parent, final String name)
   {
      return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>()
      {
         public ClassLoader run()
         {
            MockClassLoaderPolicy policy = new MockClassLoaderPolicy(name);
            return system.registerClassLoaderPolicy(domainName, parentPolicy, parent, policy);
         }
      });
   }
   
   /**
    * Register a mock classloader policy<p>
    * 
    * No security problem here. The user needs access to a ClassLoaderSystem
    * to register a classloader with it.
    * 
    * @param system the system
    * @param domain the domain
    * @param policy the policy
    * @return the classloader
    */
   public static ClassLoader registerMockClassLoader(final ClassLoaderSystem system, final ClassLoaderDomain domain, final MockClassLoaderPolicy policy)
   {
      return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>()
      {
         public ClassLoader run()
         {
            if (domain == null)
               return system.registerClassLoaderPolicy(policy);
            else
               return system.registerClassLoaderPolicy(domain, policy);
         }
      });
   }

   /**
    * Check whether a class has the expected classloader
    * 
    * @param clazz the class
    * @param expected the expected classloader
    * @return true when it has the expected classloader
    */
   public static boolean isExpectedClassLoader(final Class<?> clazz, final ClassLoader expected)
   {
      ClassLoader classLoader = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>()
      {
         public ClassLoader run()
         {
            return clazz.getClassLoader();
         }
      });
      if (classLoader == null)
         return expected == null;
      return classLoader.equals(expected);
   }
}
