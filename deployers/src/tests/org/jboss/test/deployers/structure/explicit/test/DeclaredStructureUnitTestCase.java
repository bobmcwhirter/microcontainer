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
package org.jboss.test.deployers.structure.explicit.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.plugins.structure.vfs.explicit.DeclaredStructure;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.deployers.spi.structure.vfs.StructureDeployer;
import org.jboss.test.deployers.BaseDeployersTest;
import org.jboss.virtual.VirtualFile;

/**
 * DeclaredStructure deployer unit tests.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1.1 $
 */
public class DeclaredStructureUnitTestCase extends BaseDeployersTest
{
   /** The jar structure deployer */
   private static final DeclaredStructure structure = new DeclaredStructure();
   
   public static Test suite()
   {
      return new TestSuite(DeclaredStructureUnitTestCase.class);
   }
   
   public DeclaredStructureUnitTestCase(String name)
   {
      super(name);
   }

   @Override
   protected void setUp() throws Exception
   {
      super.setUp();
      enableTrace("org.jboss.deployers");
   }

   @Override
   protected StructureDeployer getStrucutureDeployer()
   {
      return structure;
   }
   
   protected DeploymentContext getValidContext(String root, String path)
      throws Exception
   {
      DeploymentContext context = createDeploymentContext(root, path);
      boolean recognized = determineStructure(context, false);
      assertTrue("Structure should be valid: " + context.getName(), recognized);
      return context;
   }

   protected DeploymentContext assertNotValidContext(String root, String path, boolean addTopLevelInfo)
      throws Exception
   {
      DeploymentContext context = createDeploymentContext(root, path);
      boolean recognized = determineStructure(context, addTopLevelInfo);
      assertFalse("Structure should not be valid: " + context.getName(), recognized);
      assertEmpty(context.getChildren());
      return context;
   }
   
   public void testComplex() throws Exception
   {
      DeploymentContext root = getValidContext("/structure/explicit/", "complex.deployer");
      
      // Test it got all the candidates
      Map<String, Boolean> expected = new HashMap<String, Boolean>();
      expected.put(getVfsURL("/structure/explicit/complex.deployer/sub.jar/"), false);
      expected.put(getVfsURL("/structure/explicit/complex.deployer/x.war/"), false);
      assertContexts(expected, root.getChildren());
      // None of the complex.deployer children should be valid
      assertCandidatesNotValid(root);

      // Validate the root context info
      assertEquals("complex.deployer/META-INF", "complex.deployer/META-INF",
         root.getMetaDataLocation().getPathName());
      List<VirtualFile> rootCP = root.getClassPath();
      assertEquals("classpath.size = 4", 4, rootCP.size());
      HashSet<String> rootCPSet = new HashSet<String>();
      rootCPSet.add(getVfsURL("/structure/explicit/complex.deployer/cp-mf.jar/"));
      rootCPSet.add(getVfsURL("/structure/explicit/complex.deployer/jar1.jar/"));
      rootCPSet.add(getVfsURL("/structure/explicit/complex.deployer/lib-dir/jar0.jar/"));
      rootCPSet.add(getVfsURL("/structure/explicit/complex.deployer/lib-dir/jar2.jar/"));
      for(VirtualFile file : rootCP)
      {
         String url = file.toURL().toString();
         assertTrue(url, rootCPSet.contains(url));
      }

      Map<String, DeploymentContext> pathMap = createDeploymentPathMap(root);
      // Validate the sub.jar context info
      DeploymentContext subJar = pathMap.get("complex.deployer/sub.jar");
      assertNotNull("complex.deployer/sub.jar", subJar);
      assertEquals("complex.deployer/sub.jar/META-INF", "complex.deployer/sub.jar/META-INF",
            subJar.getMetaDataLocation().getPathName());
      List<VirtualFile> subJarCP = subJar.getClassPath();
      assertEquals("classpath.size = 1", 1, subJarCP.size());
      HashSet<String> subJarCPSet = new HashSet<String>();
      subJarCPSet.add(getVfsURL("/structure/explicit/complex.deployer/cp-mf.jar/"));
      for(VirtualFile file : subJarCP)
      {
         String url = file.toURL().toString();
         assertTrue(url, subJarCPSet.contains(url));
      }

      // Validate the x.war context info
      DeploymentContext xwar = pathMap.get("complex.deployer/x.war");
      assertNotNull("complex.deployer/x.war", xwar);
      assertEquals("complex.deployer/x.war/WEB-INF", "complex.deployer/x.war/WEB-INF",
            xwar.getMetaDataLocation().getPathName());
      List<VirtualFile> xWarCP = xwar.getClassPath();
      assertEquals("classpath.size = 2", 2, xWarCP.size());
      HashSet<String> xWarCPSet = new HashSet<String>();
      xWarCPSet.add(getVfsURL("/structure/explicit/complex.deployer/x.war/WEB-INF/classes/"));
      xWarCPSet.add(getVfsURL("/structure/explicit/complex.deployer/x.war/WEB-INF/lib/w0.jar/"));
      for(VirtualFile file : xWarCP)
      {
         String url = file.toURL().toString();
         assertTrue(url, xWarCPSet.contains(url));
      }
   }

   protected void assertContexts(Map<String, Boolean> expected, Set<DeploymentContext> actual) throws Exception
   {
      assertCandidateContexts(expected, actual);
   }

}
