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
package org.jboss.test.classloader.jmx.test;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import junit.framework.Test;

import org.jboss.classloader.plugins.ClassLoaderUtils;
import org.jboss.classloader.plugins.jdk.AbstractJDKChecker;
import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.classloader.test.support.MockClassLoaderPolicy;
import org.jboss.classloading.spi.RealClassLoader;
import org.jboss.test.classloader.AbstractClassLoaderTest;
import org.jboss.test.classloader.jmx.support.Simple;
import org.jboss.test.classloader.jmx.support.a.A;
import org.jboss.test.classloader.jmx.support.b.B;

/**
 * JMXUnitTestCase
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class JMXUnitTestCase extends AbstractClassLoaderTest
{
   private static final ObjectName CLASSLOADER_SYSTEM_OBJECT_NAME;
   
   static
   {
      try
      {
         CLASSLOADER_SYSTEM_OBJECT_NAME = new ObjectName("test:type=ClassLoaderSystem");
      }
      catch (MalformedObjectNameException e)
      {
         throw new RuntimeException("Unexpected error", e);
      }
      AbstractJDKChecker.getExcluded().add(JMXUnitTestCase.class);
   }
   
   public static Test suite()
   {
      return suite(JMXUnitTestCase.class);
   }
   
   public JMXUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testJMXClassLoader() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy policy = createMockClassLoaderPolicy("simple");
      policy.setPathsAndPackageNames(Simple.class);
      RealClassLoader cl = (RealClassLoader) system.registerClassLoaderPolicy(policy);
      
      MBeanServer server = MBeanServerFactory.newMBeanServer();
      ObjectName clName = cl.getObjectName();
      server.registerMBean(cl, clName);
      getLog().debug("Registered classloader " + cl + " with name " + clName);
      
      ObjectName name = new ObjectName("test:test=simple");
      server.createMBean(Simple.class.getName(), name, clName);

      MBeanInfo info = server.getMBeanInfo(name);
      assertEquals(Simple.class.getName(), info.getClassName());
      
      Object actual = server.getAttribute(name, "ClassLoader");
      getLog().debug("Actual ClassLoader=" + actual + " expected " + cl);
      assertEquals(cl, actual);
   }

   @SuppressWarnings("unchecked")
   public void testClassLoaderSystemMBean() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      MBeanServer server = MBeanServerFactory.newMBeanServer();
      server.registerMBean(system, CLASSLOADER_SYSTEM_OBJECT_NAME);
      
      Set<String> domainNames = (Set) server.getAttribute(CLASSLOADER_SYSTEM_OBJECT_NAME, "DomainNames");
      Set<String> expected = makeSet(ClassLoaderSystem.DEFAULT_DOMAIN_NAME);
      assertEquals(expected, domainNames);

      ClassLoaderDomain defaultDomain = system.getDefaultDomain();
      ObjectName defaultDomainObjectName = defaultDomain.getObjectName();
      Set<ObjectName> domains = (Set) server.getAttribute(CLASSLOADER_SYSTEM_OBJECT_NAME, "Domains");
      Set<ObjectName> expectedObjectNames = makeSet(defaultDomainObjectName);
      assertEquals(expectedObjectNames, domains);
      
      String domainName = (String) server.getAttribute(defaultDomainObjectName, "Name");
      assertEquals(ClassLoaderSystem.DEFAULT_DOMAIN_NAME, domainName);
   }

   @SuppressWarnings("unchecked")
   public void testRegisterDomain() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      MBeanServer server = MBeanServerFactory.newMBeanServer();
      server.registerMBean(system, CLASSLOADER_SYSTEM_OBJECT_NAME);
      
      ClassLoaderDomain defaultDomain = system.getDefaultDomain();
      ObjectName defaultDomainObjectName = defaultDomain.getObjectName();
      
      ClassLoaderDomain domain = system.createAndRegisterDomain("test");
      ObjectName testObjectName = domain.getObjectName();
      
      Set<String> domainNames = (Set) server.getAttribute(CLASSLOADER_SYSTEM_OBJECT_NAME, "DomainNames");
      Set<String> expected = makeSet("test", ClassLoaderSystem.DEFAULT_DOMAIN_NAME);
      assertEquals(expected, domainNames);
      
      Set<ObjectName> domains = (Set) server.getAttribute(CLASSLOADER_SYSTEM_OBJECT_NAME, "Domains");
      Set<ObjectName> expectedObjectNames = makeSet(defaultDomainObjectName, testObjectName);
      assertEquals(expectedObjectNames, domains);
      
      String domainName = (String) server.getAttribute(testObjectName, "Name");
      assertEquals("test", domainName);
      
      system.unregisterDomain(domain);
      
      assertFalse(server.isRegistered(testObjectName));

      domainNames = (Set) server.getAttribute(CLASSLOADER_SYSTEM_OBJECT_NAME, "DomainNames");
      expected = makeSet(ClassLoaderSystem.DEFAULT_DOMAIN_NAME);
      assertEquals(expected, domainNames);
      
      domains = (Set) server.getAttribute(CLASSLOADER_SYSTEM_OBJECT_NAME, "Domains");
      expectedObjectNames = makeSet(defaultDomainObjectName);
      assertEquals(expectedObjectNames, domains);
   }

   @SuppressWarnings("unchecked")
   public void testLazyRegisterDomain() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      MBeanServer server = MBeanServerFactory.newMBeanServer();
      ClassLoaderDomain test = system.createAndRegisterDomain("test");
      server.registerMBean(system, CLASSLOADER_SYSTEM_OBJECT_NAME);
      
      Set<String> domainNames = (Set) server.getAttribute(CLASSLOADER_SYSTEM_OBJECT_NAME, "DomainNames");
      Set<String> expected = makeSet("test", ClassLoaderSystem.DEFAULT_DOMAIN_NAME);
      assertEquals(expected, domainNames);
      
      ClassLoaderDomain defaultDomain = system.getDefaultDomain();
      ObjectName defaultDomainObjectName = defaultDomain.getObjectName();
      ObjectName testObjectName = test.getObjectName();
      Set<ObjectName> domains = (Set) server.getAttribute(CLASSLOADER_SYSTEM_OBJECT_NAME, "Domains");
      Set<ObjectName> expectedObjectNames = makeSet(defaultDomainObjectName, testObjectName);
      assertEquals(expectedObjectNames, domains);
   }

   @SuppressWarnings("unchecked")
   public void testUnregisterClassLoaderSystemUnregistersDomains() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      MBeanServer server = MBeanServerFactory.newMBeanServer();
      ClassLoaderDomain test = system.createAndRegisterDomain("test");
      server.registerMBean(system, CLASSLOADER_SYSTEM_OBJECT_NAME);
      
      Set<String> domainNames = (Set) server.getAttribute(CLASSLOADER_SYSTEM_OBJECT_NAME, "DomainNames");
      Set<String> expected = makeSet("test", ClassLoaderSystem.DEFAULT_DOMAIN_NAME);
      assertEquals(expected, domainNames);
      
      ClassLoaderDomain defaultDomain = system.getDefaultDomain();
      ObjectName defaultDomainObjectName = defaultDomain.getObjectName();
      ObjectName testObjectName = test.getObjectName();
      Set<ObjectName> domains = (Set) server.getAttribute(CLASSLOADER_SYSTEM_OBJECT_NAME, "Domains");
      Set<ObjectName> expectedObjectNames = makeSet(defaultDomainObjectName, testObjectName);
      assertEquals(expectedObjectNames, domains);
      
      server.unregisterMBean(CLASSLOADER_SYSTEM_OBJECT_NAME);
      assertFalse(server.isRegistered(CLASSLOADER_SYSTEM_OBJECT_NAME));
      for (ObjectName domain : domains)
         assertFalse(server.isRegistered(domain));
   }

   public void testClassLoaderDomainMBean() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      MBeanServer server = MBeanServerFactory.newMBeanServer();
      server.registerMBean(system, CLASSLOADER_SYSTEM_OBJECT_NAME);

      ClassLoaderDomain defaultDomain = system.getDefaultDomain();
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", ParentPolicy.AFTER_BUT_JAVA_BEFORE, defaultDomain);

      ObjectName testObjectName = domain.getObjectName();
      assertEquals(CLASSLOADER_SYSTEM_OBJECT_NAME, server.getAttribute(testObjectName, "System"));
      assertEquals(domain.getName(), server.getAttribute(testObjectName, "Name"));
      assertEquals(ParentPolicy.AFTER_BUT_JAVA_BEFORE.toString(), server.getAttribute(testObjectName, "ParentPolicyName"));
      assertEquals(defaultDomain.getObjectName(), server.getAttribute(testObjectName, "ParentDomain"));
      assertEquals(defaultDomain.getName(), server.getAttribute(testObjectName, "ParentDomainName"));
   }

   public void testRegisterClassLoader() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      MBeanServer server = MBeanServerFactory.newMBeanServer();
      server.registerMBean(system, CLASSLOADER_SYSTEM_OBJECT_NAME);

      ClassLoaderDomain domain = system.createAndRegisterDomain("test");

      MockClassLoaderPolicy policy = createMockClassLoaderPolicy("simple");
      policy.setPathsAndPackageNames(Simple.class);
      RealClassLoader cl = (RealClassLoader) system.registerClassLoaderPolicy(domain.getName(), policy);
      
      assertTrue(server.isRegistered(cl.getObjectName()));
      
      system.unregisterClassLoader((ClassLoader) cl);

      assertFalse(server.isRegistered(cl.getObjectName()));
   }

   public void testLazyRegisterClassLoader() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      MBeanServer server = MBeanServerFactory.newMBeanServer();

      ClassLoaderDomain domain = system.createAndRegisterDomain("test");

      MockClassLoaderPolicy policy = createMockClassLoaderPolicy("simple");
      policy.setPathsAndPackageNames(Simple.class);
      RealClassLoader cl = (RealClassLoader) system.registerClassLoaderPolicy(domain.getName(), policy);
      
      server.registerMBean(system, CLASSLOADER_SYSTEM_OBJECT_NAME);
      assertTrue(server.isRegistered(cl.getObjectName()));
   }

   public void testUnregisterClassLoaderSystemUnregistersClassLoader() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      MBeanServer server = MBeanServerFactory.newMBeanServer();
      server.registerMBean(system, CLASSLOADER_SYSTEM_OBJECT_NAME);

      ClassLoaderDomain domain = system.createAndRegisterDomain("test");

      MockClassLoaderPolicy policy = createMockClassLoaderPolicy("simple");
      policy.setPathsAndPackageNames(Simple.class);
      RealClassLoader cl = (RealClassLoader) system.registerClassLoaderPolicy(domain.getName(), policy);
      
      assertTrue(server.isRegistered(cl.getObjectName()));
      
      server.unregisterMBean(CLASSLOADER_SYSTEM_OBJECT_NAME);

      assertFalse(server.isRegistered(cl.getObjectName()));
   }

   public void testUnregisterDomainUnregistersClassLoader() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      MBeanServer server = MBeanServerFactory.newMBeanServer();
      server.registerMBean(system, CLASSLOADER_SYSTEM_OBJECT_NAME);

      ClassLoaderDomain domain = system.createAndRegisterDomain("test");

      MockClassLoaderPolicy policy = createMockClassLoaderPolicy("simple");
      policy.setPathsAndPackageNames(Simple.class);
      RealClassLoader cl = (RealClassLoader) system.registerClassLoaderPolicy(domain.getName(), policy);
      
      assertTrue(server.isRegistered(cl.getObjectName()));

      system.unregisterDomain(domain);

      assertFalse(server.isRegistered(cl.getObjectName()));
   }

   @SuppressWarnings("unchecked")
   public void testClassLoaderDomainClassLoaders() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      MBeanServer server = MBeanServerFactory.newMBeanServer();
      server.registerMBean(system, CLASSLOADER_SYSTEM_OBJECT_NAME);

      ClassLoaderDomain defaultDomain = system.getDefaultDomain();
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", ParentPolicy.BEFORE_BUT_JAVA_ONLY, defaultDomain);

      MockClassLoaderPolicy a1 = createMockClassLoaderPolicy("a1");
      a1.setPathsAndPackageNames(A.class);
      RealClassLoader clA1 = (RealClassLoader) system.registerClassLoaderPolicy(domain, a1);

      MockClassLoaderPolicy a2 = createMockClassLoaderPolicy("a2");
      a2.setPathsAndPackageNames(A.class);
      RealClassLoader clA2 = (RealClassLoader) system.registerClassLoaderPolicy(domain, a2);

      MockClassLoaderPolicy b1 = createMockClassLoaderPolicy("b1");
      b1.setPathsAndPackageNames(B.class);
      RealClassLoader clB1 = (RealClassLoader) system.registerClassLoaderPolicy(domain, b1);

      MockClassLoaderPolicy b2 = createMockClassLoaderPolicy("b2");
      b2.setPaths(B.class);
      RealClassLoader clB2 = (RealClassLoader) system.registerClassLoaderPolicy(domain, b2);
      
      ObjectName testDomain = domain.getObjectName();
      List<ObjectName> classLoaders = (List) server.getAttribute(testDomain, "ClassLoaders");
      List<ObjectName> expected = Arrays.asList(clA1.getObjectName(), clA2.getObjectName(), clB1.getObjectName(), clB2.getObjectName());
      assertEquals(expected, classLoaders);
      
      Map<String, List<ObjectName>> packageClassLoaders = (Map) server.getAttribute(testDomain, "ExportingClassLoaders");
      Map<String, List<ObjectName>> expectedMap = new HashMap<String, List<ObjectName>>();
      expectedMap.put(A.class.getPackage().getName(), Arrays.asList(clA1.getObjectName(), clA2.getObjectName()));
      expectedMap.put(B.class.getPackage().getName(), Arrays.asList(clB1.getObjectName()));
      assertEquals(expectedMap, packageClassLoaders);
      
      classLoaders = (List) server.invoke(testDomain, "getExportingClassLoaders", new Object[] { A.class.getPackage().getName() }, new String[] { String.class.getName()});
      expected = Arrays.asList(clA1.getObjectName(), clA2.getObjectName());

      classLoaders = (List) server.invoke(testDomain, "getExportingClassLoaders", new Object[] { B.class.getPackage().getName() }, new String[] { String.class.getName()});
      expected = Arrays.asList(clB1.getObjectName());

      Class<?> clazz = (Class<?>) server.invoke(testDomain, "loadClass", new Object[] { A.class.getName() }, new String[] { String.class.getName() });
      assertEquals(((ClassLoader) clA1).loadClass(A.class.getName()), clazz);

      ObjectName result = (ObjectName) server.invoke(testDomain, "findClassLoaderForClass", new Object[] { A.class.getName() }, new String[] { String.class.getName() });
      assertEquals(clA1.getObjectName(), result);
      assertNull(server.invoke(testDomain, "findClassLoaderForClass", new Object[] { Object.class.getName() }, new String[] { String.class.getName() }));

      String resourceName = ClassLoaderUtils.classNameToPath(A.class.getName());
      Set<URL> urls = (Set) server.invoke(testDomain, "loadResources", new Object[] { resourceName }, new String[] { String.class.getName()});
      Set<URL> expectedURLs = makeSet(getClass().getClassLoader().getResource(resourceName));
      assertEquals(expectedURLs, urls);
   }

   @SuppressWarnings("unchecked")
   public void testClassLoaderMBean() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      MBeanServer server = MBeanServerFactory.newMBeanServer();
      server.registerMBean(system, CLASSLOADER_SYSTEM_OBJECT_NAME);

      ClassLoaderDomain defaultDomain = system.getDefaultDomain();
      ClassLoaderDomain domain = system.createAndRegisterDomain("test", ParentPolicy.BEFORE_BUT_JAVA_ONLY, defaultDomain);

      MockClassLoaderPolicy a = createMockClassLoaderPolicy("a");
      a.setPathsAndPackageNames(A.class);
      RealClassLoader clA = (RealClassLoader) system.registerClassLoaderPolicy(domain.getName(), a);

      MockClassLoaderPolicy b = createMockClassLoaderPolicy("b");
      b.setPathsAndPackageNames(B.class);
      RealClassLoader clB = (RealClassLoader) system.registerClassLoaderPolicy(domain.getName(), b);
      
      MockClassLoaderPolicy test = createMockClassLoaderPolicy("test");
      test.setImportAll(true);
      test.setDelegates(Arrays.asList(a.getExported(), b.getExported()));
      test.setPathsAndPackageNames(A.class, B.class);
      RealClassLoader cl = (RealClassLoader) system.registerClassLoaderPolicy(domain, test);
      
      ObjectName testObjectName = cl.getObjectName();
      assertEquals(domain.getObjectName(), server.getAttribute(testObjectName, "ClassLoaderDomain"));
      assertEquals("test", server.getAttribute(testObjectName, "Name"));
      assertTrue((Boolean) server.getAttribute(testObjectName, "ImportAll"));
      assertTrue((Boolean) server.getAttribute(testObjectName, "Valid"));
      Set<String> expectedPackages = makeSet(A.class.getPackage().getName(), B.class.getPackage().getName());
      assertEquals(expectedPackages, server.getAttribute(testObjectName, "ExportedPackages"));
      List<ObjectName> expectedImports = Arrays.asList(clA.getObjectName(), clB.getObjectName()); 
      assertEquals(expectedImports, server.getAttribute(testObjectName, "Imports"));
      assertEquals(test.toLongString(), server.getAttribute(testObjectName, "PolicyDetails"));
   }

   @SuppressWarnings("unchecked")
   public void testClassLoader() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();
      MBeanServer server = MBeanServerFactory.newMBeanServer();
      server.registerMBean(system, CLASSLOADER_SYSTEM_OBJECT_NAME);
      system.getDefaultDomain().setParentPolicy(ParentPolicy.BEFORE_BUT_JAVA_ONLY);

      MockClassLoaderPolicy test = createMockClassLoaderPolicy("test");
      test.setPathsAndPackageNames(A.class, B.class);
      RealClassLoader cl = (RealClassLoader) system.registerClassLoaderPolicy(test);
      
      ObjectName testObjectName = cl.getObjectName();

      Set<String> loadedClasses = (Set) server.getAttribute(testObjectName, "LoadedClasses");
      assertFalse(loadedClasses.contains(A.class.getName()));
      Class<?> expected = ((ClassLoader) cl).loadClass(A.class.getName());
      assertEquals(expected, server.invoke(testObjectName, "loadClass", new Object[] { A.class.getName() }, new String[] { String.class.getName() }));
      loadedClasses = (Set) server.getAttribute(testObjectName, "LoadedClasses");
      assertTrue(loadedClasses.contains(A.class.getName()));

      assertEquals(cl.getObjectName(), server.invoke(testObjectName, "findClassLoaderForClass", new Object[] { A.class.getName() }, new String[] { String.class.getName() }));
      assertNull(server.invoke(testObjectName, "findClassLoaderForClass", new Object[] { Object.class.getName() }, new String[] { String.class.getName() }));
      
      String resourceName = ClassLoaderUtils.classNameToPath(B.class.getName());
      Set<String> loadedResourceNames = (Set) server.getAttribute(testObjectName, "LoadedResourceNames");
      assertFalse(loadedResourceNames.contains(resourceName));
      Set<URL> expectedURLs = makeSet(((ClassLoader) cl).getResource(resourceName));
      assertEquals(expectedURLs, server.invoke(testObjectName, "loadResources", new Object[] { resourceName }, new String[] { String.class.getName() }));
      loadedResourceNames = (Set) server.getAttribute(testObjectName, "LoadedResourceNames");
      assertTrue(loadedResourceNames.contains(resourceName));
   }
      
   protected static <T> Set<T> makeSet(T... values)
   {
      Set<T> result = new HashSet<T>();
      Collections.addAll(result, values);
      return result;
   }
}
