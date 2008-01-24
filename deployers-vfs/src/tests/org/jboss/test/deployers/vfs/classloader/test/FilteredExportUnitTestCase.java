/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.deployers.vfs.classloader.test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.jboss.classloader.plugins.system.DefaultClassLoaderSystem;
import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.deployers.structure.spi.classloading.ExportAll;
import org.jboss.deployers.vfs.plugins.classloader.VFSClassLoaderPolicy;
import org.jboss.test.BaseTestCase;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;

/**
 * FilteredExportUnitTestCase tests of multiple bundle behavior.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class FilteredExportUnitTestCase extends BaseTestCase
{
   ClassLoaderSystem system;

   public FilteredExportUnitTestCase(String name)
   {
      super(name);
   }

   protected void setUp()
      throws Exception
   {
      super.setUp();
      system = new DefaultClassLoaderSystem();
      ClassLoaderDomain domain = system.getDefaultDomain();
      domain.setParentPolicy(ParentPolicy.BEFORE_BUT_JAVA_ONLY);
   }
   protected void tearDown()
      throws Exception
   {
      system.shutdown();
      super.tearDown();
   }
   protected ClassLoader buildClassLoader(ExportAll exportAll, Map<String, String> expected, VirtualFile[] files, String[] exportPkgs)
      throws Exception
   {
      return buildClassLoader(exportAll, expected, files, exportPkgs, null);
   }
   protected ClassLoader buildClassLoader(ExportAll exportAll, Map<String, String> expected, VirtualFile[] files,
      String[] exportPkgs, Set<String> excludedPkgs)
      throws Exception
   {
      VFSClassLoaderPolicy policy = VFSClassLoaderPolicy.createVFSClassLoaderPolicy(files);
      if(excludedPkgs != null)
         policy.setExcludedPackages(excludedPkgs);
      if(exportPkgs != null)
         policy.setExportedPackages(exportPkgs);
      else
         policy.setExportAll(exportAll);
      policy.setImportAll(true);
      String[] packageNames = policy.getPackageNames();
      Set<String> actual = makeSet(packageNames);
      log.info(policy+" : packages: "+actual);
      if(expected != null)
         assertEquals(expected.keySet(), actual);
      
      ClassLoader classLoader = system.registerClassLoaderPolicy(policy);
      return classLoader;
   }

   protected String getContents(InputStream is) throws Exception
   {
      StringBuilder builder = new StringBuilder();
      InputStreamReader reader = new InputStreamReader(is);
      int character = reader.read();
      while (character != -1)
      {
         builder.append((char) character);
         character = reader.read();
      }
      return builder.toString();
   }
   
   protected Set<String> makeSet(String... elements)
   {
      assertNotNull(elements);
      Set<String> result = new HashSet<String>();
      for (String string : elements)
         result.add(string);
      return result;
   }
   
   protected Map<String,String> makeSimpleMap(String prefix, String... elements)
   {
      assertNotNull(prefix);
      assertNotNull(elements);
      Map<String, String> result = new HashMap<String, String>();
      for (String string : elements)
         result.put(string, prefix + "." + string);
      return result;
   }
   
   protected Map<String,String> makeComplexMap(String... elements)
   {
      assertNotNull(elements);
      Map<String, String> result = new HashMap<String, String>();
      for (int i = 0; i < elements.length; i += 2)
         result.put(elements[i], elements[i+1] + '.' + elements[i]);
      return result;
   }

   public static Test suite()
   {
      return new TestSuite(FilteredExportUnitTestCase.class);
   }

   /**
    * 
    * @throws Exception
    */
   public void testEar1() throws Exception
   {
      Map<String,String> expectedEar = makeSimpleMap("testear1.ear",
            "",
            "util"
      );
      // Need to get the testear1.ear URL from a class resource
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      URL libClassURL = loader.getResource("ClassInTestear1Lib.class");
      assertNotNull(libClassURL);
      URL ear1URL = new URL(libClassURL, "../../");

      VirtualFile earRoot = VFS.getRoot(ear1URL);
      VirtualFile[] ear1Files = {earRoot.getChild("lib/jar1.jar")};
      ClassLoader ear1Loader = buildClassLoader(ExportAll.NON_EMPTY, expectedEar, ear1Files, null);
      // ejb1.jar
      Map<String,String> expectedEjb1 = makeSimpleMap("testear1.ear",
            "",
            "pkg1.ejbs",
            "pkg1.ifaces"
      );
      /*
      URL usersURL = ear1Loader.getResource("META-INF/users.properties");
      log.info("users.properties: "+usersURL);
      assertNotNull(usersURL);
      assertTrue(usersURL.toString().contains("testear1.ear/META-INF"));
*/
      VirtualFile ejb1Root = earRoot.getChild("ejb1.jar");
      VirtualFile[] ejb1Files = {ejb1Root};
      ClassLoader ejb1Loader = buildClassLoader(ExportAll.NON_EMPTY, expectedEjb1, ejb1Files, null);

      URL usersURL = ejb1Loader.getResource("users.properties");
      log.info("users.properties: "+usersURL);
      assertNotNull(usersURL);
      assertTrue(usersURL.toString().contains("ejb1"));

      Enumeration<URL> userURLs = ejb1Loader.getResources("users.properties");
      assertNotNull(userURLs);
      int count = 0;
      boolean sawEarUsersProperties = false;
      boolean sawEjbUsersProperties = false;
      while(userURLs.hasMoreElements())
      {
         URL url = userURLs.nextElement();
         if(url.toString().contains("lib/jar1.jar"))
            sawEarUsersProperties = true;
         if(url.toString().contains("ejb1.jar"))
            sawEjbUsersProperties = true;
         log.info(url);
         count ++;
      }
      assertEquals("Saw 2 users.properties", 2, count);
      assertTrue("sawEarUsersProperties", sawEarUsersProperties);
      assertTrue("sawEjbUsersProperties", sawEjbUsersProperties);

      // war1.war
      Map<String,String> expectedWeb1 = makeSimpleMap("testear1.ear",
            "",
            "web"
      );
      super.enableTrace("org.jboss.deployers.vfs.plugins.classloader");
      VirtualFile war1Root = earRoot.getChild("war1.war");
      VirtualFile war1Classes = war1Root.getChild("WEB-INF/classes");
      VirtualFile war1WebInf = war1Root.getChild("WEB-INF");
      VirtualFile[] war1Files = {war1Root, war1Classes, war1WebInf};
      String[] webPkgs = {"", "web"};
      ClassLoader war1Loader = buildClassLoader(ExportAll.NON_EMPTY, expectedWeb1, war1Files, webPkgs);
      Set<String> excludedPkgs = makeSet("WEB-INF", "META-INF", "java", "classes");
      ClassLoader war1LoaderAll = buildClassLoader(ExportAll.NON_EMPTY, expectedWeb1, war1Files, null, excludedPkgs);
      URL jdkClassURL = war1Loader.getResource("java/lang/JdkClass.class");
      assertNull(jdkClassURL);
      // Test that the java.* package was excluded
      try
      {
         Class<?> jdkClass = war1LoaderAll.loadClass("java.lang.JdkClass");
         fail("Was able to load java.lang.JdkClass: "+jdkClass.getProtectionDomain());
      }
      catch(ClassNotFoundException e)
      {
         log.debug("CNFE for java.lang.JdkClass");
      }
      jdkClassURL = war1LoaderAll.getResource("java/lang/JdkClass.class");
      assertNull(jdkClassURL);
      system.unregisterClassLoader(war1LoaderAll);
      // Should be able to load java/lang/JdkClass.class as resource if java is not excluded
      war1LoaderAll = buildClassLoader(ExportAll.NON_EMPTY, null, war1Files, null);
      jdkClassURL = war1LoaderAll.getResource("java/lang/JdkClass.class");
      assertNull(jdkClassURL);
      
   }

   /**
    * Compressed ear version of testEar1
    * @throws Exception
    */
   public void testEar1x() throws Exception
   {
      Map<String,String> expectedEar = makeSimpleMap("testear1x.ear",
            "",
            "util"
      );
      URL ear1URL = getResource("/classloader/testear1x.ear");
      log.info(ear1URL);
      assertNotNull(ear1URL);
      VirtualFile earRoot = VFS.getRoot(ear1URL);
      log.info(earRoot);
      VirtualFile[] ear1Files = {earRoot.getChild("lib/jar1.jar")};
      ClassLoader ear1Loader = buildClassLoader(ExportAll.NON_EMPTY, expectedEar, ear1Files, null);
      // ejb1.jar
      Map<String,String> expectedEjb1 = makeSimpleMap("testear1x.ear",
            "",
            "pkg1.ejbs",
            "pkg1.ifaces"
      );
      VirtualFile ejb1Root = earRoot.getChild("ejb1.jar");
      log.info(ejb1Root);
      VirtualFile[] ejb1Files = {ejb1Root};
      ClassLoader ejb1Loader = buildClassLoader(ExportAll.NON_EMPTY, expectedEjb1, ejb1Files, null);

      URL usersURL = ejb1Loader.getResource("users.properties");
      log.info("users.properties: "+usersURL);
      assertNotNull(usersURL);
      assertTrue(usersURL.toString().contains("ejb1"));

      Enumeration<URL> userURLs = ejb1Loader.getResources("users.properties");
      assertNotNull(userURLs);
      int count = 0;
      boolean sawEarUsersProperties = false;
      boolean sawEjbUsersProperties = false;
      while(userURLs.hasMoreElements())
      {
         URL url = userURLs.nextElement();
         if(url.toString().contains("lib/jar1.jar"))
            sawEarUsersProperties = true;
         if(url.toString().contains("ejb1.jar"))
            sawEjbUsersProperties = true;
         log.info(url);
         count ++;
      }
      assertEquals("Saw 2 users.properties", 2, count);
      assertTrue("sawEarUsersProperties", sawEarUsersProperties);
      assertTrue("sawEjbUsersProperties", sawEjbUsersProperties);
   }
}
