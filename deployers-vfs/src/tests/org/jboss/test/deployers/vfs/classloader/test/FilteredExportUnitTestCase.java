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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.jboss.classloader.plugins.system.DefaultClassLoaderSystem;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.deployers.structure.spi.classloading.ExportAll;
import org.jboss.deployers.vfs.plugins.classloader.VFSClassLoaderPolicy;
import org.jboss.test.BaseTestCase;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;

/**
 * ExportAllUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class FilteredExportUnitTestCase extends BaseTestCase
{
   ClassLoaderSystem system = new DefaultClassLoaderSystem();
   
   public FilteredExportUnitTestCase(String name)
   {
      super(name);
   }

   protected ClassLoader buildClassLoader(ExportAll exportAll, Map<String, String> expected, VirtualFile[] files)
      throws Exception
   {
      VFSClassLoaderPolicy policy = VFSClassLoaderPolicy.createVFSClassLoaderPolicy(files);
      policy.setExportAll(exportAll);
      policy.setImportAll(true);
      
      String[] packageNames = policy.getPackageNames();
      Set<String> actual = makeSet(packageNames);
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

   public void testEar1() throws Exception
   {
      Map<String,String> expectedEar = makeSimpleMap("testear1.ear",
            "",
            "util"
      );
      URL ear1URL = getResource("/classloader/testear1.ear");
      assertNotNull(ear1URL);
      VirtualFile earRoot = VFS.getRoot(ear1URL);
      VirtualFile[] ear1Files = {earRoot.getChild("lib/jar1.jar")};
      ClassLoader ear1Loader = buildClassLoader(ExportAll.NON_EMPTY, expectedEar, ear1Files);
      // ejb1.jar
      Map<String,String> expectedEjb1 = makeSimpleMap("testear1.ear",
            "",
            "pkg1.ejbs",
            "pkg1.ifaces"
      );
      URL ejb1URL = getResource("/classloader/testear1.ear/ejb1.jar");
      assertNotNull(ejb1URL);
      VirtualFile ejb1Root = VFS.getRoot(ejb1URL);
      VirtualFile[] ejb1Files = {ejb1Root};
      ClassLoader ejb1Loader = buildClassLoader(ExportAll.NON_EMPTY, expectedEjb1, ejb1Files);

      URL usersURL = ejb1Loader.getResource("users.properties");
      log.info("users.properties: "+usersURL);
      assertNotNull(usersURL);
      assertTrue(usersURL.toString().contains("ejb1"));
   }

}
