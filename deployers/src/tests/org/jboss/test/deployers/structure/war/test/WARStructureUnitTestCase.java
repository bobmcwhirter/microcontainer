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
package org.jboss.test.deployers.structure.war.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.plugins.structure.AbstractDeploymentContext;
import org.jboss.deployers.plugins.structure.vfs.war.WARStructure;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.test.deployers.BaseDeployersTest;
import org.jboss.virtual.VirtualFile;

/**
 * WARStructureUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class WARStructureUnitTestCase extends BaseDeployersTest
{
   /** The war structure deployer */
   private static final WARStructure structure = new WARStructure();
   
   public static Test suite()
   {
      return new TestSuite(WARStructureUnitTestCase.class);
   }
   
   public WARStructureUnitTestCase(String name)
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

   protected DeploymentContext assertValidContext(String root, String path) throws Exception
   {
      VirtualFile file = getVirtualFile(root, path);
      DeploymentContext context = new AbstractDeploymentContext(file);
      assertTrue("Structure should be valid: " + file, determineStructure(context));
      assertEmpty(context.getChildren());
      return context;
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
      assertValidContext("/structure/", "war/simple/simple.war");
   }
   
   public void testNotAnArchive() throws Exception
   {
      assertNotValidContext("/structure/", "war/notanarchive/notanarchive.war");
   }
   
   public void testWarDirectory() throws Exception
   {
      assertValidContext("/structure/", "war/directory.war");
   }
   
   public void testDirectoryNotAWar() throws Exception
   {
      assertNotValidContext("/structure/", "war/directorynotawar");
   }
   
   public void testDirectoryWithWebInf() throws Exception
   {
      assertValidContext("/structure/", "war/directorywithwebinf");
   }
}
