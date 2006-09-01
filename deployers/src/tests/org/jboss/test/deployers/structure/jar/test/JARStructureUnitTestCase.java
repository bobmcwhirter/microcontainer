/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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

import org.jboss.deployers.plugins.structure.vfs.AbstractVFSDeploymentContext;
import org.jboss.deployers.plugins.structure.vfs.JARStructure;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.deployers.spi.structure.vfs.VFSDeploymentContext;
import org.jboss.test.deployers.BaseDeployersTest;
import org.jboss.vfs.spi.VirtualFile;

/**
 * A ChangeAutomaticControllerTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 37459 $
 */
public class JARStructureUnitTestCase extends BaseDeployersTest
{
   public static Test suite()
   {
      return new TestSuite(JARStructureUnitTestCase.class);
   }
   
   public JARStructureUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testSimple() throws Exception
   {
      JARStructure structure = new JARStructure();
      VirtualFile file = getVirtualFile("/structure/", "jar/simple");
      AbstractVFSDeploymentContext context = new AbstractVFSDeploymentContext(file);
      assertTrue(structure.determineStructure(context));
      
      // Test it got all the candidates
      Set<String> expected = new HashSet<String>();
      expected.add(getResource("/structure/jar/simple/NotAnArchive.jar").toString());
      expected.add(getResource("/structure/jar/simple/NotAnArchive.zip").toString());
      expected.add(getResource("/structure/jar/simple/simple.txt").toString());
      assertContexts(expected, context.getChildren());
      
      // Test none of the candidates work
      for (DeploymentContext child : context.getChildren())
      {
         VFSDeploymentContext vfsContext = (VFSDeploymentContext) child;
         assertFalse(structure.determineStructure(vfsContext));
      }
   }
   
   public void testNested() throws Exception
   {
      JARStructure structure = new JARStructure();
      VirtualFile file = getVirtualFile("/structure/", "jar/nested");
      AbstractVFSDeploymentContext context = new AbstractVFSDeploymentContext(file);
      assertTrue(structure.determineStructure(context));
      
      // Test it got all the candidates
      Set<String> expected = new HashSet<String>();
      expected.add(getResource("/structure/jar/nested/archive.jar").toString());
      expected.add(getResource("/structure/jar/nested/archive.zip").toString());
      assertContexts(expected, context.getChildren());
      
      // Test all of the candidates work
      for (DeploymentContext child : context.getChildren())
      {
         VFSDeploymentContext vfsContext = (VFSDeploymentContext) child;
         assertTrue("Should be valid " + child.getName(), structure.determineStructure(vfsContext));
      }
   }
}
