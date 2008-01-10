/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import junit.extensions.TestSetup;
import junit.framework.Test;

import org.jboss.classloader.plugins.filter.CombiningClassFilter;
import org.jboss.classloader.plugins.system.DefaultClassLoaderSystem;
import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.DelegateLoader;
import org.jboss.classloader.spi.Loader;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.classloader.spi.filter.ClassFilter;
import org.jboss.classloader.spi.filter.FilteredDelegateLoader;
import org.jboss.classloader.spi.filter.PackageClassFilter;
import org.jboss.logging.Logger;
import org.jboss.test.AbstractTestCaseWithSetup;
import org.jboss.test.logging.LoggingPlugin;
import org.jboss.test.security.PolicyPlugin;

/**
 * IsolatedClassLoaderTestHelper.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class IsolatedClassLoaderTestHelper
{
   /** The classloader system */
   private ClassLoaderSystem system;
   
   /** The classLoader domain */
   private ClassLoaderDomain domain;
   
   /** The classLoader policy */
   private ClassLoaderPolicy policy;
   
   /**
    * Get the domain.
    * 
    * @return the domain.
    */
   public ClassLoaderDomain getDomain()
   {
      return domain;
   }

   /**
    * Get the system.
    * 
    * @return the system.
    */
   public ClassLoaderSystem getSystem()
   {
      return system;
   }

   /**
    * Get the policy.
    * 
    * @return the policy.
    */
   public ClassLoaderPolicy getPolicy()
   {
      return policy;
   }

   /**
    * Set the policy.
    * 
    * @param policy the policy.
    */
   public void setPolicy(ClassLoaderPolicy policy)
   {
      this.policy = policy;
   }

   /**
    * Get the packages that should not be isolated
    * (and by transience their dependent classes, e.g. log4j in the classpath)<p>
    * 
    * NOTE: The transient packages cannot be used directly by the test
    * unless explicity mentioned in this list.
    * 
    * The list can be expanded by using the jboss.test.parent.pkgs system property with a 
    * comma-separated list of package names, e.g. <br>
    * -Djboss.test.parent.pkgs=org.jboss.package1, org.jboss.package2
    * 
    * @return the test support packages
    */
   public static Set<String> getParentPackages()
   {
      Set<String> result = new HashSet<String>();
      result.add(Test.class.getPackage().getName());
      result.add(TestSetup.class.getPackage().getName());
      result.add(AbstractTestCaseWithSetup.class.getPackage().getName());
      result.add(Logger.class.getPackage().getName());
      result.add(LoggingPlugin.class.getPackage().getName());
      result.add(PolicyPlugin.class.getPackage().getName());
      result.add(ClassLoaderSystem.class.getPackage().getName());
      result.add(IsolatedClassLoaderTest.class.getPackage().getName());
      
      String pkgString = AccessController.doPrivileged(new PrivilegedAction<String>() 
      {
         public String run() 
         {
            return System.getProperty("jboss.test.parent.pkgs");
         }}
      );

      if (pkgString != null)
      {
         StringTokenizer tok = new StringTokenizer(pkgString, ",");
         while(tok.hasMoreTokens())
         {
            String pkg = tok.nextToken();
            result.add(pkg.trim());
         }
      }
      
      return result;
   }

   /**
    * Initialize the classloader system
    * 
    * @param clazz the original clazz
    * @param importAll whether to import all
    * @param packages the reference classes for the packages
    * @return the clazz loaded from the new classloading system
    */
   public Class<?> initializeClassLoader(Class<?> clazz, boolean importAll, Class<?>... packages)
   {
      MockClassLoaderPolicy policy = new MockClassLoaderPolicy();
      Set<Class<?>> classes = new HashSet<Class<?>>();
      classes.add(clazz);
      for (Class<?> c : packages)
         classes.add(c);
      policy.setImportAll(importAll);
      policy.setPathsAndPackageNames(classes.toArray(new Class[classes.size()]));

      return initializeClassLoader(clazz, policy);
   }

   /**
    * Initialize the classloader system
    * 
    * @param clazz the original clazz
    * @param policy the policy
    * @return the clazz loaded from the new classloading system
    */
   public Class<?> initializeClassLoader(Class<?> clazz, ClassLoaderPolicy policy)
   {
      ClassLoaderSystem system = new DefaultClassLoaderSystem();
      return initializeClassLoader(clazz, system, policy, getParentPackages());
   }

   /**
    * Initialize the classloader system
    * 
    * @param clazz the original clazz
    * @param system the system
    * @param policy the policy
    * @return the clazz loaded from the new classloading system
    */
   public Class<?> initializeClassLoader(Class<?> clazz, ClassLoaderSystem system, ClassLoaderPolicy policy)
   {
      return initializeClassLoader(clazz, system, policy, getParentPackages());
   }

   /**
    * Initialize the classloader system
    * 
    * @param clazz the original clazz
    * @param system the system
    * @param policy the policy
    * @param parentPackages the parentPackages
    * @return the clazz loaded from the new classloading system
    */
   public Class<?> initializeClassLoader(Class<?> clazz, ClassLoaderSystem system, ClassLoaderPolicy policy, Set<String> parentPackages)
   {
      String[] parentPkgs = parentPackages.toArray(new String[parentPackages.size()]);
      return initializeClassLoader(clazz, system, policy, parentPkgs);
   }

   /**
    * Initialize the classloader system
    * 
    * @param clazz the original clazz
    * @param system the system
    * @param policy the policy
    * @param parentPackages the parentPackages
    * @return the clazz loaded from the new classloading system
    */
   public Class<?> initializeClassLoader(Class<?> clazz, ClassLoaderSystem system, ClassLoaderPolicy policy, String... parentPackages)
   {
      // The parent filter
      PackageClassFilter filter = new PackageClassFilter(parentPackages);
      filter.setIncludeJava(true);
      return initializeClassLoader(clazz, system, filter, ClassFilter.NOTHING, policy);
   }

   /**
    * Initialize the classloader system
    * 
    * @param clazz the original clazz
    * @param parentFilter the parent filter
    * @param importAll whether to import all
    * @param packages the reference classes for the packages
    * @return the clazz loaded from the new classloading system
    */
   public Class<?> initializeClassLoader(Class<?> clazz, ClassFilter parentFilter, boolean importAll, Class<?>... packages)
   {
      MockClassLoaderPolicy policy = new MockClassLoaderPolicy();
      Set<Class<?>> classes = new HashSet<Class<?>>();
      classes.add(clazz);
      for (Class<?> c : packages)
         classes.add(c);
      policy.setImportAll(importAll);
      policy.setPathsAndPackageNames(classes.toArray(new Class[classes.size()]));
      return initializeClassLoader(clazz, parentFilter, policy);
   }

   /**
    * Initialize the classloader system
    * 
    * @param clazz the original clazz
    * @param parentFilter the parent filter
    * @param policy the policy
    * @return the clazz loaded from the new classloading system
    */
   public Class<?> initializeClassLoader(Class<?> clazz, ClassFilter parentFilter, ClassLoaderPolicy policy)
   {
      ClassLoaderSystem system = new DefaultClassLoaderSystem();
      return initializeClassLoader(clazz, system, parentFilter, policy);
   }

   /**
    * Initialize the classloader system
    * 
    * @param clazz the original clazz
    * @param system the system
    * @param parentFilter the parent filter
    * @param policy the policy
    * @return the clazz loaded from the new classloading system
    */
   public Class<?> initializeClassLoader(Class<?> clazz, ClassLoaderSystem system, ClassFilter parentFilter, ClassLoaderPolicy policy)
   {
      Set<String> parentPackages = getParentPackages();
      String[] parentPkgs = parentPackages.toArray(new String[parentPackages.size()]);
      PackageClassFilter filter = new PackageClassFilter(parentPkgs);
      filter.setIncludeJava(true);
      CombiningClassFilter beforeFilter = CombiningClassFilter.create(filter, parentFilter);
      ParentPolicy parentPolicy = new ParentPolicy(beforeFilter, ClassFilter.NOTHING);
      return initializeClassLoader(clazz, system, parentPolicy, policy);
   }

   /**
    * Initialize the classloader system
    * 
    * @param clazz the original clazz
    * @param system the system
    * @param beforeFilter the before filter
    * @param afterFilter the after filter
    * @param policy the policy
    * @return the clazz loaded from the new classloading system
    */
   public Class<?> initializeClassLoader(Class<?> clazz, ClassLoaderSystem system, ClassFilter beforeFilter, ClassFilter afterFilter, ClassLoaderPolicy policy)
   {
      ParentPolicy parentPolicy = new ParentPolicy(beforeFilter, afterFilter);
      return initializeClassLoader(clazz, system, parentPolicy, policy);
   }

   /**
    * Initialize the classloader system
    * 
    * @param clazz the original clazz
    * @param system the system
    * @param parentPolicy the parent policy
    * @param policy the policy
    * @return the clazz loaded from the new classloading system
    */
   public Class<?> initializeClassLoader(Class<?> clazz, ClassLoaderSystem system, ParentPolicy parentPolicy, ClassLoaderPolicy policy)
   {
      ClassLoaderDomain domain = system.createAndRegisterDomain("TEST", parentPolicy);
      return initializeClassLoader(clazz, system, domain, policy);
   }

   /**
    * Initialize the classloader system
    * 
    * @param clazz the original clazz
    * @param system the system
    * @param domain the domain
    * @param policy the policy
    * @return the clazz loaded from the new classloading system
    */
   public Class<?> initializeClassLoader(Class<?> clazz, ClassLoaderSystem system, ClassLoaderDomain domain, ClassLoaderPolicy policy)
   {
      // Remember some information
      this.system = system;
      this.domain = domain;
      this.policy = policy;
      
      // Create the classloader
      ClassLoader classLoader = system.registerClassLoaderPolicy(domain, policy);

      // Load the class from the isolated classloader
      try
      {
         clazz = classLoader.loadClass(clazz.getName());
      }
      catch (ClassNotFoundException e)
      {
         throw new RuntimeException("Unable to load test class in isolated classloader " + clazz, e);
      }
      
      return clazz;
   }
   
   /**
    * Create a classloader
    * 
    * It exports everything
    *
    * @param name the name
    * @param importAll whether to import all
    * @param packages the packages
    * @return the classloader
    * @throws Exception for any error
    */
   public ClassLoader createClassLoader(String name, boolean importAll, String... packages) throws Exception
   {
      MockClassLoaderPolicy policy = MockClassLoaderHelper.createMockClassLoaderPolicy(name);
      policy.setImportAll(importAll);
      policy.setPathsAndPackageNames(packages);
      return createClassLoader(policy);
   }
   
   /**
    * Create a classloader
    *
    * @param policy the policy
    * @return the classloader
    * @throws Exception for any error
    */
   public ClassLoader createClassLoader(MockClassLoaderPolicy policy) throws Exception
   {
      ClassLoaderDomain domain = getDomain();
      return createClassLoader(domain, policy);
   }
   
   /**
    * Create a classloader
    *
    * @param domainName the domainName
    * @param policy the policy
    * @return the classloader
    * @throws Exception for any error
    */
   public ClassLoader createClassLoader(String domainName, MockClassLoaderPolicy policy) throws Exception
   {
      ClassLoaderSystem system = getSystem();
      ClassLoaderDomain domain = system.getDomain(domainName);
      return createClassLoader(domain, policy);
   }
   
   /**
    * Create a classloader
    *
    * @param domain the domain
    * @param policy the policy
    * @return the classloader
    * @throws Exception for any error
    */
   public ClassLoader createClassLoader(ClassLoaderDomain domain, MockClassLoaderPolicy policy) throws Exception
   {
      ClassLoaderSystem system = getSystem();
      return MockClassLoaderHelper.registerMockClassLoader(system, domain, policy);
   }

   /**
    * Unregister a classloader
    * 
    * @param classLoader the classloader
    * @throws Exception for any error
    */
   public void unregisterClassLoader(ClassLoader classLoader) throws Exception
   {
      ClassLoaderSystem system = getSystem();
      system.unregisterClassLoader(classLoader);
   }

   /**
    * Create the default delegate loader
    * 
    * @return the loaders
    */
   public List<? extends DelegateLoader> createDefaultDelegates()
   {
      return createDelegates(getPolicy());
   }

   /**
    * Create delegate loaders from policies
    * 
    * @param policies the policies
    * @return the loaders
    */
   public List<? extends DelegateLoader> createDelegates(ClassLoaderPolicy... policies)
   {
      List<DelegateLoader> delegates = new ArrayList<DelegateLoader>();
      for (ClassLoaderPolicy policy : policies)
         delegates.add(new FilteredDelegateLoader(policy));
      return delegates;
   }

   /**
    * Create a scoped classloader domain using the test domain as parent
    * using the parent first policy
    * 
    * @param name the name
    * @return the domain
    */
   public ClassLoaderDomain createScopedClassLoaderDomainParentFirst(String name)
   {
      return createScopedClassLoaderDomain(name, ParentPolicy.BEFORE, getDomain());
   }

   /**
    * Create a scoped classloader domain using the test domain as parent
    * using the parent last policy
    * 
    * @param name the name
    * @return the domain
    */
   public ClassLoaderDomain createScopedClassLoaderDomainParentLast(String name)
   {
      return createScopedClassLoaderDomain(name, ParentPolicy.AFTER_BUT_JAVA_BEFORE, getDomain());
   }

   /**
    * Create a scoped classloader domain using the test domain as parent
    * 
    * @param name the name
    * @param parentPolicy the parent policy
    * @return the domain
    */
   public ClassLoaderDomain createScopedClassLoaderDomain(String name, ParentPolicy parentPolicy)
   {
      return createScopedClassLoaderDomain(name, parentPolicy, getDomain());
   }

   /**
    * Create a scoped classloader domain
    * 
    * @param name the name
    * @param parentPolicy the parent policy
    * @param parent the parent
    * @return the domain
    */
   public ClassLoaderDomain createScopedClassLoaderDomain(String name, ParentPolicy parentPolicy, Loader parent)
   {
      ClassLoaderSystem system = getSystem();
      return system.createAndRegisterDomain(name, parentPolicy, parent);
   }

   /**
    * Unregister a domain
    * 
    * @param name the domain name
    */
   public void unregisterDomain(String name)
   {
      ClassLoaderSystem system = getSystem();
      ClassLoaderDomain domain = system.getDomain(name);
      if (domain == null)
         throw new IllegalStateException("Domain is not registered: " + name);
      system.unregisterDomain(domain);
   }
}
