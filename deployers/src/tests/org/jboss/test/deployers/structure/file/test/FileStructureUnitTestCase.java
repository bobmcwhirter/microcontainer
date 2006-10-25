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
package org.jboss.test.deployers.structure.file.test;

import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.plugins.structure.vfs.file.FileStructure;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.deployers.spi.structure.vfs.StructureDeployer;
import org.jboss.test.deployers.BaseDeployersTest;

/**
 * FileStructureUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class FileStructureUnitTestCase extends BaseDeployersTest
{
   /** The file structure deployer */
   private static final FileStructure structure = new FileStructure();
   
   public static Test suite()
   {
      return new TestSuite(FileStructureUnitTestCase.class);
   }
   
   public FileStructureUnitTestCase(String name)
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

   protected DeploymentContext assertValidContext(String root, String path, boolean addTopLevelInfo)
      throws Exception
   {
      DeploymentContext context = createDeploymentContext(root, path);
      boolean recoginized = determineStructure(context, addTopLevelInfo);
      assertTrue("Structure should be valid: " + context.getName(), recoginized);
      assertEmpty(context.getChildren());
      return context;
   }
   
   protected DeploymentContext assertNotValidContext(String root, String path,
         boolean isDirectory, boolean addTopLevelInfo)
      throws Exception
   {
      DeploymentContext context = createDeploymentContext(root, path);
      boolean recoginized = determineStructure(context, addTopLevelInfo);
      assertFalse("Structure should not be valid: " + context.getName(), recoginized);
      assertEmpty(context.getChildren());
      return context;
   }
   
   public void testSimple() throws Exception
   {
      assertValidContext("/structure/", "file/simple/simple-service.xml", false);
   }
   
   public void testNotKnownButTopLevel() throws Exception
   {
      assertValidContext("/structure/", "file/notknown/test-unknown.xml", true);
      assertValidContext("/structure/", "file/notknown/unknown.xml", true);
   }

   public void testDirectory() throws Exception
   {
      assertNotValidContext("/structure/", "file/directory", false, false);
   }
   
   protected void assertContexts(Map<String, Boolean> expected, Set<DeploymentContext> actual) throws Exception
   {
      assertCandidateContexts(expected, actual);
   }
}
