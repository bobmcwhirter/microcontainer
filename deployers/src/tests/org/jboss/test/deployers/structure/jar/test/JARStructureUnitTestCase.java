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
package org.jboss.test.deployers.structure.jar.test;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.plugins.structure.AbstractDeploymentContext;
import org.jboss.deployers.plugins.structure.vfs.jar.JARStructure;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.test.deployers.BaseDeployersTest;
import org.jboss.virtual.VirtualFile;

/**
 * JARStructureUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class JARStructureUnitTestCase extends BaseDeployersTest
{
   /** The jar structure deployer */
   private static final JARStructure structure = new JARStructure();
   
   public static Test suite()
   {
      return new TestSuite(JARStructureUnitTestCase.class);
   }
   
   public JARStructureUnitTestCase(String name)
   {
      super(name);
   }
   
   protected void setUp() throws Exception
   {
      super.setUp();
      enableTrace("org.jboss.deployers");
   }

   protected boolean determineStructure(DeploymentContext context)
   {
      log.debug("Determining structure: " + context.getName());
      return structure.determineStructure(context);
   }
   
   protected DeploymentContext getValidContext(String root, String path) throws Exception
   {
      VirtualFile file = getVirtualFile(root, path);
      DeploymentContext context = new AbstractDeploymentContext(file);
      assertTrue("Structure should be valid: " + file, determineStructure(context));
      return context;
   }

   protected DeploymentContext assertValidContext(String root, String path) throws Exception
   {
      return getValidContext(root, path);
   }
   
   protected DeploymentContext assertNotValidContext(String root, String path) throws Exception
   {
      VirtualFile file = getVirtualFile(root, path);
      DeploymentContext context = new AbstractDeploymentContext(file);
      assertFalse("Structure should not be valid: " + file, determineStructure(context));
      assertEmpty(context.getChildren());
      return context;
   }
   
   public void testSimple() throws Exception
   {
      DeploymentContext context = getValidContext("/structure/", "jar/simple");
      
      // Test it got all the candidates
      Set<String> expected = new HashSet<String>();
      expected.add(getURL("/structure/jar/simple/simple1.txt"));
      expected.add(getURL("/structure/jar/simple/simple2.txt"));
      assertContexts(expected, context.getChildren());

      assertCandidatesNotValid(context);
   }
   
   public void testRootNotAnArchive() throws Exception
   {
      assertNotValidContext("/structure/", "jar/notanarchive/NotAnArchive.jar");
      assertNotValidContext("/structure/", "jar/notanarchive/NotAnArchive.zip");
   }
   
   public void testSubdeploymentNotAnArchive() throws Exception
   {
      DeploymentContext context = getValidContext("/structure/", "jar/notanarchive");
      
      // Test it got all the candidates
      Set<String> expected = new HashSet<String>();
      expected.add(getURL("/structure/jar/notanarchive/NotAnArchive.jar"));
      expected.add(getURL("/structure/jar/notanarchive/NotAnArchive.zip"));
      assertContexts(expected, context.getChildren());

      assertCandidatesNotValid(context);
   }
   
   public void testJarAsRoot() throws Exception
   {
      assertValidContext("/structure/", "jar/indirectory/archive.jar");
      assertValidContext("/structure/", "jar/indirectory/archive.zip");
   }
   
   public void testJarInDirectory() throws Exception
   {
      DeploymentContext context = getValidContext("/structure/", "jar/indirectory");
      
      // Test it got all the candidates
      Set<String> expected = new HashSet<String>();
      expected.add(getJarURL("/structure/jar/indirectory/archive.jar").toString());
      expected.add(getJarURL("/structure/jar/indirectory/archive.zip").toString());
      assertContexts(expected, context.getChildren());
      
      assertCandidatesValid(context);
   }
   
   public void testSubdirectoryNotAJar() throws Exception
   {
      DeploymentContext context = getValidContext("/structure/", "jar/subdirnotajar");
      
      // Test it got all the candidates
      Set<String> expected = new HashSet<String>();
      expected.add(getURL("/structure/jar/subdirnotajar/sub").toString());
      assertContexts(expected, context.getChildren());
      
      assertCandidatesNotValid(context);
   }
   
   public void testSubdirectoryIsAJar() throws Exception
   {
      DeploymentContext context = getValidContext("/structure/", "jar/subdirisajar");
      
      // Test it got all the candidates
      Set<String> expected = new HashSet<String>();
      expected.add(getURL("/structure/jar/subdirisajar/sub.jar").toString());
      assertContexts(expected, context.getChildren());
      
      assertCandidatesValid(context);
   }
   
   public void testdirectoryHasMetaInf() throws Exception
   {
      assertValidContext("/structure/", "jar/subdirhasmetainf/sub");
   }
   
   public void testSubdirectoryHasMetaInf() throws Exception
   {
      DeploymentContext context = getValidContext("/structure/", "jar/subdirhasmetainf");
      
      // Test it got all the candidates
      Set<String> expected = new HashSet<String>();
      expected.add(getURL("/structure/jar/subdirhasmetainf/sub").toString());
      assertContexts(expected, context.getChildren());
      
      assertCandidatesValid(context);
   }
}
